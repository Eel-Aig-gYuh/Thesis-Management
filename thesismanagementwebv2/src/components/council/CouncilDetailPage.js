import { useEffect, useState, useContext } from "react";
import { useTranslation } from "react-i18next";
import { useToast } from "../contexts/ToastProvider";
import { Link, useNavigate, useParams } from "react-router-dom";
import { Alert, Card, Col, Row, Button } from "react-bootstrap";
import { MyUserContext } from "../../configs/MyContexts";
import MySpinner from "../layouts/MySpinner";
import { getCouncilById, cancelCouncil, lockCouncil } from "../../services/councilService";
import ConfirmModal from "../utils/ConfirmModal";
import ScoreForm from "../scores/ScoreForm";
import { getGradingCriteria } from "../../services/scoreService";

const STATUS_COLORS = {
    SCHEDULED: "#d1ecf1",
    ONGOING: "#d4edda",
    COMPLETED: "#fff3cd",
    CANCELED: "#f8d7da",
    LOCKED: "#e2e3e5",
};

const CouncilDetailPage = () => {
    const { t } = useTranslation();
    const toast = useToast();
    const navigate = useNavigate();
    const { councilId } = useParams();
    const user = useContext(MyUserContext);

    const [council, setCouncil] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [showCancelModal, setShowCancelModal] = useState(false);
    const [showLockModal, setLockModal] = useState(false);
    const [showScoreForm, setShowScoreForm] = useState(false);

    const [showGradingScore, setShowGradingScore] = useState(false);
    const [loadingCriteria, setLoadingCriteria] = useState(false);
    const [thesisCriteria, setThesisCriteria] = useState([]);

    // Check if user can grade (ROLE_GIANGVIEN and council member)
    const canGrade = (council) => {
        if (user?.role !== "ROLE_GIANGVIEN") return false;

        return council.members.some(
            (m) => m.user.id === user.id && (m.role === "MEMBER" || m.role === "REVIEWER")
        );
    };


    // Kiểm tra councilId và lấy chi tiết hội đồng
    useEffect(() => {
        if (!councilId) {
            setError(t("invalid-council-id"));
            setLoading(false);
            toast(t("invalid-council-id"), "danger");
            return;
        }

        const fetchCouncil = async () => {
            setLoading(true);
            try {
                const data = await getCouncilById(councilId);
                console.log("Council data:", data);
                setCouncil(data);
            } catch (err) {
                console.error("Fetch council error:", err);
                setError(err.response?.data || err.message);
                toast(`${t("fetch-council-failure")}: ${err.response?.data || err.message}`, "danger");
            } finally {
                setLoading(false);
            }
        };
        fetchCouncil();
    }, [councilId, t]);



    useEffect(() => {
        if (showScoreForm) {
            const payload = {
                "councilId": councilId,
                "thesisIds": council?.theses?.map(thesis => thesis.id) || []
            }

            const fetchThesisCriteria = async () => {
                setLoadingCriteria(true);
                try {
                    const data = await getGradingCriteria(payload);
                    console.log("Council data:", data);
                    setThesisCriteria(data);
                } catch (err) {
                    setError(err.response?.data || err.message);
                    toast(`${t("fetch-criteria-thesis-failure")}: ${err.response?.data || err.message}`, "danger");
                } finally {
                    setLoading(false);
                }
            }
            fetchThesisCriteria();
        }
    }, [councilId, t, showScoreForm]);

    // Hàm hủy hội đồng
    const handleCancelCouncil = async () => {
        if (!user || user.role !== "ROLE_GIAOVU") {
            toast(t("only-giaovu-can-cancel-council"), "danger");
            return;
        }

        try {
            await cancelCouncil(councilId, { status: 'CANCELED' });
            toast(t("cancel-council-success"), "success");
            navigate("/council");
        } catch (error) {
            console.error("Cancel council error:", error);
            toast(`${t("cancel-council-failure")}: ${error.response?.data || error.message}`, "danger");
        }
    };

    // Hàm khóa hội đồng
    const handleLockCouncil = async () => {
        if (!user || user.role !== "ROLE_GIAOVU") {
            toast(t("only-giaovu-can-cancel-council"), "danger");
            return;
        }

        setLoading(true);

        try {
            await lockCouncil(councilId);
            toast(t("lock-council-success"), "success");
            navigate("/council");
        } catch (error) {
            console.error("Cancel council error:", error);
            toast(`${t("cancel-council-failure")}: ${error.response?.data || error.message}`, "danger");
        } finally {
            setLoading(true);
        }
    };


    const toggleScorePage = async () => {
        setShowScoreForm(!showScoreForm);
    }

    if (loading) return <MySpinner />;
    if (error) return <Alert variant="danger">{error}</Alert>;
    if (!council) return null;

    const isLocked = council?.status === "LOCKED";
    const isCancelled = council?.status === "CANCELED";

    return (
        <div className="container mt-4">
            <h2 className="text-center mb-4">📋 {t("council-details")}</h2>
            <Card style={{ backgroundColor: STATUS_COLORS[council.status] || "#ffffff" }}>
                <Card.Body>
                    <Row>
                        <Col md={6}>
                            <Card.Title>{council.name}</Card.Title>
                            <Card.Text>
                                <strong>{t("defense-date")}:</strong>{" "}
                                {new Date(council.defenseDate).toLocaleString()}
                            </Card.Text>
                            <Card.Text>
                                <strong>{t("defense-location")}:</strong> {council.defenseLocation}
                            </Card.Text>
                            <Card.Text>
                                <strong>{t("status")}:</strong> {t(council.status.toLowerCase())}
                            </Card.Text>
                            <Card.Text>
                                <strong>{t("created-by")}:</strong>{" "}
                                {council.createdBy.lastname} {council.createdBy.firstname}
                            </Card.Text>
                            <Card.Text>
                                <strong>{t("created-at")}:</strong>{" "}
                                {new Date(council.createdAt).toLocaleString()}
                            </Card.Text>
                        </Col>
                        <Col md={6}>
                            <Card.Text>
                                <strong>{t("members")}:</strong>
                                <ul>
                                    {council.members.map((member, index) => (
                                        <li key={index}>
                                            {member.user.lastname} {member.user.firstname} ({t(member.role.toLowerCase())})
                                        </li>
                                    ))}
                                </ul>
                            </Card.Text>
                            <Card.Text>
                                <strong>{t("theses")}:</strong>
                                <ul>
                                    <strong>{t("theses")}:</strong>
                                    {council.theses.length === 0 ? (
                                        <span>{t("no-theses")}</span>
                                    ) : (
                                        <ul>
                                            {council.theses.map((thesis) => (
                                                <li key={thesis.id}>
                                                    {thesis.title}
                                                    {canGrade(council) && council.status === "SCHEDULED" && (
                                                        <Button
                                                            as={Link}
                                                            to={`/thesis/${thesis.id}`}
                                                            variant="info"
                                                            size="sm"
                                                            className="ms-2"
                                                        >
                                                            {t("grade-thesis")}
                                                        </Button>
                                                    )}
                                                </li>
                                            ))}
                                        </ul>
                                    )}
                                </ul>
                            </Card.Text>
                        </Col>
                    </Row>
                    <div className="text-end mt-3">
                        <Link className="btn btn-secondary me-2" to="/council">
                            {t("back")}
                        </Link>

                        {user?.role === "ROLE_GIANGVIEN" && (
                            <Button
                                variant="success"
                                className="me-2"
                                onClick={toggleScorePage}
                                disabled={isLocked || isCancelled}
                            >
                                {t("grade-score-btn")}
                            </Button>
                        )}

                        {user?.role === "ROLE_GIAOVU" && (
                            <>
                                <Button
                                    variant="success"
                                    className="me-2"
                                    onClick={() => setLockModal(true)}
                                    disabled={isLocked || isCancelled}
                                >
                                    {t("lock-council-btn")}
                                </Button>

                                <Link
                                    className={`btn btn-warning me-2 ${isLocked ? "disabled" : ""}`}
                                    to={isLocked ? "#" : `/council/edit/${councilId}`}
                                    onClick={(e) => isLocked && e.preventDefault()}
                                >
                                    {t("edit")}
                                </Link>
                                <Button
                                    variant="danger"
                                    className="me-2"
                                    onClick={() => setShowCancelModal(true)}
                                    disabled={isLocked || isCancelled}
                                >
                                    {t("cancel-council-btn")}
                                </Button>
                            </>
                        )}

                        <ConfirmModal
                            show={showCancelModal}
                            onHide={() => setShowCancelModal(false)}
                            onConfirm={() => {
                                handleCancelCouncil();
                                setShowCancelModal(false);
                            }}
                            title={t("confirm-cancel-council-title")}
                            message={t("confirm-cancel-council-message")}
                            confirmText={t("cancel-council-btn")}
                            cancelText={t("cancel")}
                        />

                        <ConfirmModal
                            show={showLockModal}
                            onHide={() => setLockModal(false)}
                            onConfirm={() => {
                                handleLockCouncil();
                                setLockModal(false);
                            }}
                            title={t("confirm-lock-council-title")}
                            message={t("confirm-lock-council-message")}
                            confirmText={t("lock-council-btn")}
                            cancelText={t("cancel")}
                        />
                    </div>
                </Card.Body>
            </Card>

            {showScoreForm && (
                <Card className="mt-2">
                    <Card.Body>
                        <ScoreForm
                            thesesData={thesisCriteria}
                            councilStatus={council?.status}
                        />
                    </Card.Body>

                </Card>
            )}
        </div>
    );
};

export default CouncilDetailPage;