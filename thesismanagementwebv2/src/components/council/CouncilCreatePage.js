import { useEffect, useMemo, useState, useContext } from "react";
import { useTranslation } from "react-i18next";
import { useToast } from "../contexts/ToastProvider";
import { useNavigate, useParams } from "react-router-dom";
import { Button, Col, Form, Row } from "react-bootstrap";
import SearchableUserSelectBox from "../utils/SearchableUserSelectBox";
import ThesisDualList from "../theses/ThesisDualList";
import useLazyLoad from "../hooks/useLazyLoad";
import { fetchAvailableTheses } from "../../services/thesisService";
import CouncilMembersEditor from "./CouncilMemberEditors";
import { cancelCouncil, createCouncil, getCouncilById, updateCouncil } from "../../services/councilService";
import { MyUserContext } from "../../configs/MyContexts";
import MySpinner from "../layouts/MySpinner";
import ConfirmModal from "../utils/ConfirmModal";

const CouncilCreatePage = () => {
    const { t } = useTranslation();
    const toast = useToast();
    const navigate = useNavigate();
    const { councilId } = useParams();
    const user = useContext(MyUserContext);

    const isEditMode = !!councilId;

    const [council, setCouncil] = useState({
        name: "",
        defenseDate: "",
        defenseLocation: "",
        members: [
            { userId: null, name: "", role: "CHAIRMAN", showSelect: false },
            { userId: null, name: "", role: "SECRETARY", showSelect: false },
            { userId: null, name: "", role: "REVIEWER", showSelect: false },
        ],
        thesisIds: [],
    });
    const [selectedTheses, setSelectedTheses] = useState([]);
    const [filters, setFilters] = useState({});
    const [loading, setLoading] = useState(isEditMode);
    const [showLoadDraftModal, setShowLoadDraftModal] = useState(false);

    const { items: availableTheses, loadMore, hasMore, resetItems } = useLazyLoad(fetchAvailableTheses, filters);

    const uniqueTheses = useMemo(() => {
        return Array.from(new Map(availableTheses.map((thesis) => [thesis.id, thesis])).values());
    }, [availableTheses]);

    // Kiểm tra quyền ROLE_GIAOVU
    useEffect(() => {
        if (!user || user.role !== "ROLE_GIAOVU") {
            toast(
                isEditMode ? t("only-giaovu-can-edit-council") : t("only-giaovu-can-create-council"),
                "danger"
            );
            navigate("/council");
        }
    }, [user, toast, navigate, t, isEditMode]);

    // Lấy dữ liệu hội đồng nếu ở chế độ chỉnh sửa
    useEffect(() => {
        if (isEditMode) {
            if (!councilId) {
                toast(t("invalid-council-id"), "danger");
                navigate("/council");
                return;
            }

            const fetchCouncil = async () => {
                setLoading(true);
                try {
                    const data = await getCouncilById(councilId);
                    console.log("API response data:", data);
                    console.log("defenseDate:", data.defenseDate, "Type:", typeof data.defenseDate);

                    // Định dạng defenseDate
                    let formattedDefenseDate = null;
                    if (data.defenseDate) {
                        let date;
                        if (typeof data.defenseDate === "number" || typeof data.defenseDate === "string") {
                            date = new Date(data.defenseDate);
                        } else if (data.defenseDate instanceof Date) {
                            date = data.defenseDate;
                        }

                        if (date && !isNaN(date.getTime())) {
                            const year = date.getFullYear();
                            const month = String(date.getMonth() + 1).padStart(2, "0");
                            const day = String(date.getDate()).padStart(2, "0");
                            const hours = String(date.getHours()).padStart(2, "0");
                            const minutes = String(date.getMinutes()).padStart(2, "0");
                            formattedDefenseDate = `${year}-${month}-${day}T${hours}:${minutes}`;
                        } else {
                            console.warn("Cannot parse defenseDate, setting to null:", data.defenseDate);
                        }
                    }

                    setCouncil({
                        name: data.name || "",
                        defenseDate: formattedDefenseDate || "",
                        defenseLocation: data.defenseLocation || "",
                        members: data.members
                            ? data.members.map((member) => ({
                                userId: member.user?.id || null,
                                name: member.user
                                    ? `${member.user.lastname} ${member.user.firstname}`
                                    : "",
                                role: member.role || "",
                                showSelect: false,
                            }))
                            : [],
                        thesisIds: data.theses ? data.theses.map((thesis) => thesis.id) : [],
                    });
                    setSelectedTheses(data.theses || []);
                } catch (err) {
                    console.error("Fetch council error:", err);
                    toast(`${t("fetch-council-failure")}: ${err.response?.data || err.message}`, "danger");
                } finally {
                    setLoading(false);
                }
            };
            fetchCouncil();
        }
    }, [isEditMode]);

    // Hàm tải nháp
    const loadDraft = () => {
        try {
            const draft = JSON.parse(localStorage.getItem("councilDraft"));
            if (draft && draft.council && Array.isArray(draft.selectedTheses)) {
                setCouncil({
                    name: draft.council.name || "",
                    defenseDate: draft.council.defenseDate || "",
                    defenseLocation: draft.council.defenseLocation || "",
                    members: Array.isArray(draft.council.members)
                        ? draft.council.members
                            .filter((member) => member && (member.userId || member.name) && member.role)
                            .map((member) => ({
                                userId: member.userId || null,
                                name: member.name || "",
                                role: member.role || "",
                                showSelect: member.showSelect || false,
                            }))
                        : [
                            { userId: null, name: "", role: "CHAIRMAN", showSelect: false },
                            { userId: null, name: "", role: "SECRETARY", showSelect: false },
                            { userId: null, name: "", role: "REVIEWER", showSelect: false },
                        ],
                    thesisIds: Array.isArray(draft.selectedTheses)
                        ? draft.selectedTheses
                            .filter((thesis) => thesis && thesis.id)
                            .map((thesis) => thesis.id)
                        : [],
                });
                setSelectedTheses(
                    Array.isArray(draft.selectedTheses)
                        ? draft.selectedTheses
                            .filter((thesis) => thesis && thesis.id)
                            .map((thesis) => ({
                                id: thesis.id,
                                title: thesis.title || "Unknown Thesis",
                                status: thesis.status || "",
                            }))
                        : []
                );
                toast(t("load-draft-success"), "success");
            } else {
                toast(t("no-draft-found"), "danger");
            }
        } catch (error) {
            console.error("Load draft error:", error);
            toast(t("no-draft-found"), "danger");
        }
    };


    const handleSubmit = async () => {
        if (!user || user.role !== "ROLE_GIAOVU") {
            toast(
                isEditMode ? t("only-giaovu-can-edit-council") : t("only-giaovu-can-create-council"),
                "danger"
            );
            return;
        }

        // Validate dữ liệu
        if (!council.name.trim()) {
            toast(t("council-name-required"), "danger");
            return;
        }
        if (!council.defenseDate) {
            toast(t("defense-date-required"), "danger");
            return;
        }
        if (!council.defenseLocation.trim()) {
            toast(t("defense-location-required"), "danger");
            return;
        }
        if (council.members.length < 3) {
            toast(t("min-three-members"), "danger");
            return;
        }
        if (council.members.length > 5) {
            toast(t("max-five-members"), "danger");
            return;
        }
        const roles = council.members.map((member) => member.role);
        console.log(roles);

        if (!roles.includes("CHAIRMAN")) {
            toast(t("chair-required"), "danger");
            return;
        }
        if (!roles.includes("SECRETARY")) {
            toast(t("secretary-required"), "danger");
            return;
        }
        if (!roles.includes("REVIEWER")) {
            toast(t("reviewer-required"), "danger");
            return;
        }
        if (roles.filter((role) => role === "CHAIR").length > 1) {
            toast(t("only-one-chair"), "danger");
            return;
        }
        if (roles.filter((role) => role === "SECRETARY").length > 1) {
            toast(t("only-one-secretary"), "danger");
            return;
        }
        if (roles.filter((role) => role === "REVIEWER").length > 1) {
            toast(t("only-one-reviewer"), "danger");
            return;
        }
        if (council.members.some((member) => !member.userId || !member.role)) {
            toast(t("all-members-need-user-and-role"), "danger");
            return;
        }
        const idCounts = council.members.reduce((acc, member) => {
            if (member.userId) {
                acc[member.userId] = (acc[member.userId] || 0) + 1;
            }
            return acc;
        }, {});
        const duplicateUserIds = Object.keys(idCounts).filter((id) => idCounts[id] > 1);
        if (duplicateUserIds.length > 0) {
            toast(t("no-duplicate-members"), "danger");
            return;
        }
        if (selectedTheses.length === 0) {
            toast(t("at-least-one-thesis"), "danger");
            return;
        }
        if (selectedTheses.length > 5) {
            toast(t("max-five-theses"), "danger");
            return;
        }

        // Chuẩn bị payload cho API
        const payload = {
            name: council.name,
            defenseDate: council.defenseDate,
            defenseLocation: council.defenseLocation,
            members: council.members.map((member) => ({
                userId: member.userId,
                role: member.role,
            })),
            thesisIds: selectedTheses.map((thesis) => thesis.id),
        };

        try {
            setLoading(true);

            if (isEditMode) {
                console.log(payload);
                console.log(user);
                await updateCouncil(councilId, payload);
                toast(t("update-council-success"), "success");
            } else {
                await createCouncil(payload);
                toast(t("create-council-success"), "success");
            }
            navigate("/council");
        } catch (error) {
            console.error("Submit error:", error);
            toast(
                `${isEditMode ? t("update-council-failure") : t("create-council-failure")
                }: ${error.response?.data || error.message}`,
                "danger"
            );
        } finally {
            setLoading(false);
        }
    };

    // Đồng bộ selectedTheses với council.thesisIds
    useEffect(() => {
        setCouncil((prev) => ({
            ...prev,
            thesisIds: selectedTheses.map((thesis) => thesis.id),
        }));
    }, [selectedTheses]);

    return (
        <div className="container mt-4 bg-white p-4 rounded-4">
            <h2 className="theis-title-list text-center mb-5 fw-bold" style={{ color: "white" }}>
                {isEditMode ? t("edit-council") : t("create-council")}
            </h2>

            {loading ? (
                <MySpinner />
            ) : (
                <div>
                    <Row className="content-info">
                        <Col md={6}>
                            <Form.Group className="mb-3">
                                <Form.Label>
                                    <div className="fw-bold">
                                        {t("council-name")}
                                    </div>
                                </Form.Label>
                                <Form.Control
                                    value={council.name}
                                    onChange={(e) => setCouncil({ ...council, name: e.target.value })}
                                    placeholder={t("enter-council-name")}
                                />
                            </Form.Group>
                            <Form.Group className="mb-3">
                                <Form.Label>
                                    <div className="fw-bold">
                                        {t("defense-date")}
                                    </div>
                                </Form.Label>
                                <Form.Control
                                    type="datetime-local"
                                    min={new Date().toISOString().slice(0, 16)}
                                    value={council.defenseDate}
                                    onChange={(e) => setCouncil({ ...council, defenseDate: e.target.value })}
                                />
                            </Form.Group>
                            <Form.Group className="mb-3">
                                <Form.Label>
                                    <div className="fw-bold">
                                        {t("defense-location")}
                                    </div>
                                </Form.Label>
                                <Form.Control
                                    value={council.defenseLocation}
                                    onChange={(e) => setCouncil({ ...council, defenseLocation: e.target.value })}
                                    placeholder={t("enter-defense-location")}
                                />
                            </Form.Group>
                        </Col>

                        <Col md={6}>
                            <Form.Group className="mb-3">
                                <Form.Label>
                                    <div className="fw-bold">
                                        {t("council-members")}
                                    </div>
                                </Form.Label>
                                <CouncilMembersEditor
                                    members={council.members}
                                    onChange={(updatedMembers) => setCouncil({ ...council, members: updatedMembers })}
                                    onAddMember={() => {
                                        if (council.members.length >= 5) {
                                            toast(t("max-five-members"), "danger");
                                            return;
                                        }
                                        setCouncil((prev) => ({
                                            ...prev,
                                            members: [
                                                ...prev.members,
                                                { userId: null, name: "", role: "MEMBER", showSelect: false },
                                            ],
                                        }));
                                    }}
                                    onRemoveMember={(index) => {
                                        if (council.members.length <= 3) {
                                            toast(t("min-three-members"), "danger");
                                            return;
                                        }
                                        const updated = [...council.members];
                                        updated.splice(index, 1);
                                        setCouncil({ ...council, members: updated });
                                    }}
                                />
                                <Row>
                                    <Col md={1}></Col>
                                    <Col md={11} style={{ textAlign: "right" }}>
                                        <Form.Text className="text-muted">
                                            ? {t("members-limit-hint", { count: council.members.length })}
                                        </Form.Text>
                                    </Col>
                                </Row>
                            </Form.Group>
                        </Col>
                    </Row>

                    <Row className="content-info mt-4">
                        <Form.Group className="mb-3 mt-4">
                            <ThesisDualList
                                availableTheses={uniqueTheses}
                                selectedTheses={selectedTheses}
                                setSelectedTheses={(newSelected) => {
                                    if (newSelected.length > 5) {
                                        toast(t("max-five-theses"), "danger");
                                        return;
                                    }
                                    setSelectedTheses(newSelected);
                                }}
                                loadMore={loadMore}
                                hasMore={hasMore}
                                loading={loading}
                                resetItems={resetItems}
                            />
                            <Row>
                                <Col md={6} style={{ textAlign: "right" }}>
                                    <Form.Text className="text-muted">
                                        ? {t("theses-limit-hint", { count: selectedTheses.length })}
                                    </Form.Text>
                                </Col>
                                <Col md={6} >
                                </Col>
                            </Row>
                        </Form.Group>
                    </Row>

                    <div className="text-end mt-4">
                        <Button
                            variant="secondary"
                            className="me-2 thesis-btn"
                            onClick={() => navigate("/council")}
                        >
                            {t("cancel")}
                        </Button>
                        <Button
                            variant="warning"
                            className="me-2 thesis-btn"
                            onClick={() => {
                                localStorage.setItem("councilDraft", JSON.stringify({ council, selectedTheses }));
                                toast(t("save-draft-success"), "success");
                            }}
                        >
                            {t("save-draft")}
                        </Button>
                        <Button
                            variant="info"
                            className="me-2 thesis-btn"
                            onClick={() => setShowLoadDraftModal(true)}
                        >
                            {t("load-draft")}
                        </Button>

                        <ConfirmModal
                            show={showLoadDraftModal}
                            onHide={() => setShowLoadDraftModal(false)}
                            onConfirm={() => {
                                loadDraft();
                                setShowLoadDraftModal(false);
                            }}
                            title={t("confirm-load-draft-title")}
                            message={t("confirm-load-draft-message")}
                            confirmText={t("load-draft")}
                            cancelText={t("cancel")}
                        />

                        <Button variant="primary" className="thesis-btn" onClick={handleSubmit}>
                            {isEditMode ? t("update-council-btn") : t("create-council-btn")}
                        </Button>
                    </div>
                </div>
            )}
        </div>
    );
};

export default CouncilCreatePage;