import { useEffect, useRef, useState, useContext, useMemo } from "react";
import { fetchTheses, updateThesisStatus, fetchMyTheses } from "../../services/thesisService";
import useLazyLoad from "../hooks/useLazyLoad";
import { Alert, Button, Card, CardBody, Col, Form, Row } from "react-bootstrap";
import "./style.css";
import "../../i18n/index";
import { Link, useSearchParams } from "react-router-dom";
import { useTranslation } from "react-i18next";
import AssignReviewerBox from "./AssignReviewerBox";
import { useToast } from "../contexts/ToastProvider";
import { MyUserContext } from "../../configs/MyContexts";
import ConfirmModal from "../utils/ConfirmModal";

const STATUS_COLORS = {
    DRAFT: "#9B786F",
    REGISTERED: "#374785",
    APPROVED: "#14A76C",
    REJECTED: "#FF652F",
    CANCELLED: "black",
};

const ThesisListPage = () => {
    const { t } = useTranslation();
    const [searchParams, setSearchParams] = useSearchParams();
    const [openAssignBox, setOpenAssignBox] = useState(null);
    const [showConfirm, setShowConfirm] = useState(false);
    const [cancelThesisId, setCancelThesisId] = useState(null);
    const toast = useToast();
    const user = useContext(MyUserContext);

    const [filters, setFilters] = useState({
        title: searchParams.get("title") || "",
        status: searchParams.get("status") || "",
        order: searchParams.get("order") || "desc",
    });

    const fetchFunc = useMemo(() => {
        if (user?.role === "ROLE_GIAOVU") {
            return fetchTheses;
        }
        else if (user?.role === "ROLE_SINHVIEN" || user?.role === "ROLE_GIANGVIEN") {
            return fetchMyTheses;
        }
    }, [user?.role]);
    const { items: theses, loadMore, hasMore, loading, resetItems } = useLazyLoad(fetchFunc, filters);

    const observerRef = useRef(null);

    // Sync filters with URL
    useEffect(() => {
        const newSearchParams = {};
        if (filters.title) newSearchParams.title = filters.title;
        if (filters.status) newSearchParams.status = filters.status;
        newSearchParams.order = filters.order;
        setSearchParams(newSearchParams, { replace: true });
    }, [filters, setSearchParams]);

    // Intersection observer for lazy loading
    useEffect(() => {
        const observer = new IntersectionObserver(
            (entries) => {
                if (entries[0].isIntersecting && hasMore && !loading) {
                    loadMore();
                }
            },
            { threshold: 1.0 }
        );

        if (observerRef.current) observer.observe(observerRef.current);
        return () => observer.disconnect();
    }, [hasMore, loading, loadMore]);

    const handleFilterChange = (e) => {
        const { name, value } = e.target;
        setFilters((prev) => ({ ...prev, [name]: value }));
    };

    const handleSearch = (e) => {
        e.preventDefault();
        resetItems();
    };

    // Handle status change
    const handleStatusChange = async (thesisId, newStatus) => {
        if (!user || user.role !== "ROLE_GIAOVU") {
            toast(t("only-giaovu-can-update"), "danger");
            return;
        }

        try {
            await updateThesisStatus(thesisId, { status: newStatus });
            toast(`Cập nhật trạng thái luận văn thành công: ${t(newStatus.toLowerCase())}`, "success");
            resetItems();
        } catch (error) {
            toast(
                `Lỗi khi cập nhật trạng thái: ${error.response?.data || error.message}`,
                "danger"
            );
        }
    };

    const handleClickCancel = (id) => {
        setCancelThesisId(id);
        setShowConfirm(true);
    };

    const handleConfirmCancel = async () => {
        if (!user || user.role !== "ROLE_GIAOVU") {
            toast(t("only-giaovu-can-update"), "danger");
            setShowConfirm(false);
            return;
        }

        if (!cancelThesisId) {
            toast(t("no-thesis-selected"), "danger");
            setShowConfirm(false);
            return;
        }

        try {
            await updateThesisStatus(cancelThesisId, { status: "CANCELLED" });
            toast(t("cancel-thesis-success"), "success");
            resetItems();
        } catch (err) {
            toast(`${t("cancel-thesis-failure")}: ${err.response?.data || err.message}`, "danger");
        } finally {
            setShowConfirm(false);
            setCancelThesisId(null);
        }
    };

    // Group theses by semester
    const groupedBySemester = theses.reduce((acc, thesis) => {
        if (!acc[thesis.semester]) acc[thesis.semester] = [];
        acc[thesis.semester].push(thesis);
        return acc;
    }, {});

    // Render no results message
    const renderNoResultsMessage = () => {
        if (Object.keys(groupedBySemester).length === 0 && !loading) {
            let message = t("no-theses-found");
            if (filters.title && filters.status) {
                message = t("no-theses-found-with-title-and-status", {
                    title: filters.title,
                    status: t(filters.status.toLowerCase()),
                });
            } else if (filters.title) {
                message = t("no-theses-found-with-title", { title: filters.title });
            } else if (filters.status) {
                message = t("no-theses-found-with-status", {
                    status: t(filters.status.toLowerCase()),
                });
            }
            return (
                <Alert variant="info" className="text-center">
                    {message}
                </Alert>
            );
        }
        return null;
    };

    return (
        <div>
            <h2 className="theis-title-list text-center mb-4"> {t("thesis-title-list")}</h2>

            <Form onSubmit={handleSearch} className="mb-4">
                <Row className="form-action">
                    <Col md={4}>
                        <Form.Control className="border-input"
                            type="text"
                            placeholder={t("search-by-title")}
                            name="title"
                            value={filters.title}
                            onChange={handleFilterChange}
                        />
                    </Col>
                    <Col md={3}>
                        <Form.Select name="status" value={filters.status} className="border-input" onChange={handleFilterChange}>
                            <option value="">{t("all-statuses")}</option>
                            <option value="DRAFT">{t("draft")}</option>
                            <option value="REGISTERED">{t("registered")}</option>
                            <option value="APPROVED">{t("approved")}</option>
                            <option value="REJECTED">{t("rejected")}</option>
                            <option value="CANCELLED">{t("cancel")}</option>
                        </Form.Select>
                    </Col>
                    <Col md={3}>
                        <Form.Select name="order" value={filters.order} className="border-input" onChange={handleFilterChange}>
                            <option value="desc">{t("newest")}</option>
                            <option value="asc">{t("oldest")}</option>
                        </Form.Select>
                    </Col>
                    <Col md={2}>
                        <Button type="submit" className="w-100 btn-action">{t("filter")}</Button>
                    </Col>
                </Row>
            </Form>

            {renderNoResultsMessage()}

            {Object.keys(groupedBySemester).map((semester) => (
                <div key={semester} className="mb-4">
                    <h4 className="thesis-semester" style={{ textAlign: "right", fontWeight: "bold", }}>{semester}</h4>

                    <Row>
                        {groupedBySemester[semester].map((thesis) => (
                            <Col key={thesis.id} xs={12} className="mb-5 card-container" style={{ paddingBottom: "10px" }}>
                                <Card
                                    className="thesis-card px-3 py-2 mt-2"
                                    style={{
                                        backgroundColor: "white"
                                    }}
                                >
                                    <Card.Title>
                                        <div className="thesis-card-title"
                                            style={{ backgroundColor: STATUS_COLORS[thesis.status] || STATUS_COLORS.DRAFT, color: "white" }}>
                                            <strong> {t(thesis.status.toLowerCase())}</strong>
                                        </div>
                                    </Card.Title>
                                    <Card.Body>
                                        <Card.Title className="mb-4 thesis-content-title">
                                            <div className="fs-4" style={{ fontWeight: "bold" }}>
                                                {thesis.title}
                                            </div>
                                        </Card.Title>

                                        <Row>
                                            <Col>
                                                <CardBody className="thesis-content-container">
                                                    <Card.Text className="mb-2">
                                                        <strong>{t('status')}: </strong> {t(thesis.status.toLowerCase())}
                                                    </Card.Text>
                                                    <Card.Text className="mb-2">
                                                        <strong>{t('main-thesis-person')} </strong>{" "}
                                                        {thesis.students[0]?.lastname + " " + thesis.students[0]?.firstname}
                                                    </Card.Text>
                                                    <Card.Text className="mb-2">
                                                        <strong>{t("advisors")}: </strong>{" "}
                                                        {thesis.supervisors[0]?.lastname + " " + thesis.supervisors[0]?.firstname}
                                                    </Card.Text>
                                                    <Card.Text className="mb-2">
                                                        <strong style={{color: `${thesis.reviewers.length === 0 ? '#FF652F' : 'black'}`}}>{t("reviewers")}: </strong>{" "}
                                                        {thesis.reviewers.length > 0 ? (
                                                            <>
                                                                {thesis?.reviewers[0]?.lastname + " " + thesis?.reviewers[0]?.firstname}
                                                            </>
                                                        ) : (
                                                            <div className="fw-semibold" style={{color: "#FF652F", textAlign: "right"}}>{t('not-assign-yet')}</div>
                                                        )}
                                                    </Card.Text>
                                                </CardBody>

                                                <Row md={12}>
                                                    <Col md={6}>
                                                        <Button
                                                            as={Link}
                                                            to={`/thesis/${thesis.id}`}
                                                            variant="warning"
                                                            className="me-2 mb-2 mt-4 thesis-btn"
                                                        >
                                                            {t("btn-thesis-detail")}
                                                        </Button>
                                                    </Col>

                                                    <Col md={6} className="text-end">
                                                        {user?.role === "ROLE_GIAOVU" && (
                                                            <Button
                                                                as={Link}
                                                                to={`/thesis/edit/${thesis.id}`}
                                                                variant="warning"
                                                                className="me-2 mb-2 mt-4 thesis-btn"
                                                            >
                                                                {t("btn-thesis-edit")}
                                                            </Button>
                                                        )}
                                                    </Col>
                                                </Row>
                                            </Col>

                                            <Col>
                                                {user && user.role === "ROLE_GIAOVU" && (
                                                    <Row>
                                                        <Col>
                                                            <Form.Select
                                                                value={thesis.status}
                                                                onChange={(e) => handleStatusChange(thesis.id, e.target.value)}
                                                                className="mb-2 thesis-btn"
                                                                disabled={loading || thesis.status === "CANCELLED"}
                                                            >
                                                                <option value="DRAFT">{t("draft")}</option>
                                                                <option value="REGISTERED">{t("registered")}</option>
                                                                <option value="APPROVED">{t("approved")}</option>
                                                                <option value="REJECTED">{t("rejected")}</option>
                                                            </Form.Select>
                                                        </Col>

                                                        <Col>
                                                            <Button
                                                                variant="danger"
                                                                className="mb-2 thesis-btn"
                                                                onClick={() => handleClickCancel(thesis.id)}
                                                            >
                                                                {t("btn-thesis-cancel")}
                                                            </Button>
                                                        </Col>
                                                    </Row>
                                                )}

                                                {user && user.role === "ROLE_GIAOVU" && (
                                                    <Row>
                                                        <Col>

                                                        </Col>
                                                        <Col>
                                                            <Button
                                                                variant="success"
                                                                disabled={thesis.reviewers.length > 0}
                                                                className="me-2 mt-3 mb-2 d-flex justify-content-end thesis-btn"
                                                                onClick={() => setOpenAssignBox(thesis.id)}
                                                            >
                                                                {t("btn-thesis-assign-review")}
                                                            </Button>
                                                        </Col>
                                                    </Row>
                                                )}

                                                {openAssignBox === thesis.id && (
                                                    <AssignReviewerBox
                                                        thesisId={thesis.id}
                                                        onSuccess={() => {
                                                            setOpenAssignBox(null);
                                                            resetItems();
                                                        }}
                                                        onCancel={() => setOpenAssignBox(null)}
                                                    />
                                                )}
                                            </Col>
                                        </Row>
                                    </Card.Body>
                                </Card>
                            </Col>
                        ))}
                    </Row>
                </div>
            ))}

            <ConfirmModal
                show={showConfirm}
                onHide={() => {
                    setShowConfirm(false);
                    setCancelThesisId(null);
                }}
                onConfirm={handleConfirmCancel}
                title={t("confirm-cancel-title")}
                message={t("confirm-cancel-message")}
                confirmText={t("confirm-cancel-thesis")}
                cancelText={t("cancel")}
            />

            <div ref={observerRef} style={{ height: "30px" }}></div>

            {loading && <p className="text-center text-secondary">{t("loading")}</p>}
            {!hasMore && <p className="text-center text-muted fs-5 pb-4">{t("no-more-data")}</p>}
        </div>
    );
};

export default ThesisListPage;