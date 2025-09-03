import { useEffect, useState, useContext } from "react";
import { useTranslation } from "react-i18next";
import { useToast } from "../contexts/ToastProvider";
import { Link, useParams } from "react-router-dom";
import { Alert, Button, Card, Col, Row } from "react-bootstrap";
import { MyUserContext } from "../../configs/MyContexts";
import MySpinner from "../layouts/MySpinner";
import { calculateAverageScore, fetchFileByThesisId, fetchScoreDetailByThesisId, fetchThesisById } from "../../services/thesisService";
import ThesisFileUpload from "./ThesisFileUpload";
import ThesisScoreDetail from "./ThesisScoreDetail";

const STATUS_COLORS = {
    DRAFT: "#9B786F",
    REGISTERED: "#374785",
    APPROVED: "#14A76C",
    REJECTED: "#FF652F",
    CANCELLED: "black",
};

const ThesisDetailPage = () => {
    const { t } = useTranslation();
    const toast = useToast();
    const { thesisId } = useParams();
    const user = useContext(MyUserContext);

    const [thesis, setThesis] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const [loadingCalculate, setLoadingCalculate] = useState(false);

    const [showUpload, setShowUpload] = useState(false);
    const [fileUrls, setFileUrls] = useState([]);
    const [loadingFiles, setLoadingFiles] = useState(false);

    const [showScores, setShowScores] = useState(false);
    const [scoresData, setScoresData] = useState(null);
    const [loadingScores, setLoadingScores] = useState(false);

    const canUpload = user?.role === "ROLE_SINHVIEN" && ["SUBMITTED", "APPROVED"].includes(thesis?.status);

    // Kiểm tra thesisId và lấy chi tiết khóa luận
    const fetchThesis = async () => {
        setLoading(true);
        try {
            const data = await fetchThesisById(thesisId);
            console.log("Thesis data:", data);
            setThesis(data);
        } catch (err) {
            console.error("Fetch thesis error:", err);
            setError(err.response?.data || err.message);
            toast(`${t("fetch-thesis-failure")}: ${err.response?.data || err.message}`, "danger");
        } finally {
            setLoading(false);
        }
    };
    useEffect(() => {
        if (!thesisId) {
            setError(t("invalid-thesis-id"));
            setLoading(false);
            toast(t("invalid-thesis-id"), "danger");
            return;
        }
        fetchThesis();
    }, [thesisId, t]);

    const handleUploadSuccess = (response) => {
        toast("Uploaded file response:" + response, "success");
        // Có thể cập nhật thesis nếu cần (e.g., thêm file vào danh sách files)
        const fetchFiles = async () => {
            try {
                const res = await fetchFileByThesisId(thesisId);
                setFileUrls(res.data);
            } catch (err) {
                toast(`${t("fetch-files-failure")}: ${err.message}`, "danger");
            }
        };
        fetchFiles();
    };

    // lấy danh sách file
    useEffect(() => {
        if (showUpload) {
            const fetchFiles = async () => {
                setLoadingFiles(true);
                try {
                    const res = await fetchFileByThesisId(thesisId);
                    console.log("Thesis files:", res.data);
                    setFileUrls(res.data);
                } catch (err) {
                    console.error("Fetch files error:", err);
                    toast(`${t("fetch-files-failure")}: ${err.message}`, "danger");
                } finally {
                    setLoadingFiles(false);
                }
            };
            fetchFiles();
        }
    }, [showUpload]);

    // Lấy điểm chi tiết khi showScores = true
    useEffect(() => {
        if (showScores) {
            const fetchScores = async () => {
                setLoadingScores(true);
                try {
                    const res = await fetchScoreDetailByThesisId(thesisId);
                    console.log("Scores data:", res.data);
                    setScoresData(res.data);
                } catch (err) {
                    console.error("Fetch scores error:", err);
                    toast(`${t("fetch-scores-failure")}: ${err.message}`, "danger");
                } finally {
                    setLoadingScores(false);
                }
            };
            fetchScores();
        }
    }, [showScores, t]);

    const toggleShowUploadPage = () => {
        setShowUpload(!showUpload);
    }

    const toggleScorePage = () => {
        setShowScores(!showScores);
    }

    const toggleUpdateScore = async () => {
        setLoadingCalculate(true);
        try {
            await calculateAverageScore(thesisId);
            toast(t('getAverageSuccess'), "success");
            fetchThesis();
        } catch {
            toast(t('getAverageFailure'), "danger");
        }
        setLoadingCalculate(false);
    }

    if (loading) return <MySpinner />;
    if (error) return <Alert variant="danger">{error}</Alert>;
    if (!thesis) return null;

    return (
        <div className="container mt-4">
            <h2 className="text-center mb-4 theis-title-list">
                {t("thesis-details")}
            </h2>

            <div className="content-title-list mt-4 mb-4">
                <div className="fs-4 fw-bold">
                    {thesis.title}
                </div>
            </div>

            <Card className="card-container">
                <Card.Title>
                    <div className="thesis-card-title"
                        style={{ backgroundColor: STATUS_COLORS[thesis.status] || STATUS_COLORS.DRAFT, color: "white" }}>
                        <strong> {t(thesis.status.toLowerCase())}</strong>
                    </div>
                </Card.Title>

                <Card.Body className="content-body-list mt-4 mb-4">
                    <Row>
                        <Col md={6}>
                            <Card.Text>
                                <strong>{t("semester")}:</strong> {thesis.semester}
                            </Card.Text>
                            <Card.Text>
                                <strong>{t("status")}:</strong> {t(thesis.status.toLowerCase())}
                            </Card.Text>
                            <Card.Text>
                                <strong>{t("created-by")}:</strong>{" "}
                                {thesis.createdBy.lastname} {thesis.createdBy.firstname}
                            </Card.Text>
                            <Card.Text>
                                <strong>{t("created-at")}:</strong>{" "}
                                {new Date(thesis.createdAt).toLocaleString()}
                            </Card.Text>
                            <Card.Text>
                                <strong>{t("average-score")}:</strong>{" "}
                                {thesis.averageScore !== null ? thesis.averageScore : t("not-scored")}
                            </Card.Text>
                        </Col>
                        <Col md={6}>
                            <Card.Text>
                                <strong>{t("students")}:</strong>
                                <ul>
                                    {thesis.students.map((student, index) => (
                                        <li key={index}>
                                            {student.lastname} {student.firstname} ({student.email})
                                        </li>
                                    ))}
                                </ul>
                            </Card.Text>
                            <Card.Text>
                                <strong>{t("supervisors")}:</strong>
                                <ul>
                                    {thesis.supervisors.map((supervisor, index) => (
                                        <li key={index}>
                                            {supervisor.lastname} {supervisor.firstname} ({supervisor.email})
                                        </li>
                                    ))}
                                </ul>
                            </Card.Text>
                            <Card.Text>
                                <strong>{t("reviewers")}:</strong>
                                <ul>
                                    {thesis.reviewers.map((reviewer, index) => (
                                        <li key={index}>
                                            {reviewer.lastname} {reviewer.firstname} ({reviewer.email})
                                        </li>
                                    ))}
                                </ul>
                            </Card.Text>
                        </Col>
                    </Row>
                </Card.Body>

                <div className="mb-3">
                    <Row>
                        <Col md={4}>
                            <Link style={{ marginLeft: "10px" }} className="btn thesis-btn btn-secondary me-2" to="/thesis">
                                {t("back")}
                            </Link>
                        </Col>
                        <Col md={8} className="text-end">
                            {user?.role === "ROLE_GIAOVU" && (
                                <Button
                                    variant="warning"
                                    className="me-2 thesis-btn"
                                    onClick={toggleUpdateScore}
                                >
                                    {t("get-average-score-btn")}
                                </Button>
                            )}

                            <Button
                                variant="success"
                                className="me-2 thesis-btn"
                                onClick={toggleShowUploadPage}
                            >
                                {t("show-upload-btn")}
                            </Button>

                            <Button
                                variant="info"
                                className="me-2 thesis-btn"
                                onClick={toggleScorePage}
                            >
                                {showScores ? t("hide-scores-btn") : t("show-scores-btn")}
                            </Button>
                        </Col>

                    </Row>
                </div>
            </Card>

            {loadingCalculate && (
                <MySpinner></MySpinner>
            )}

            {showUpload && (
                <Card className="mt-3 card-container">
                    <Card.Body>
                        {loadingFiles ? (
                            <p>{t("loading-files")}</p>
                        ) : (
                            <ThesisFileUpload
                                thesisId={thesisId}
                                onUploadSuccess={handleUploadSuccess}
                                fileUrls={fileUrls}
                            />
                        )}
                    </Card.Body>
                </Card>
            )}

            {showScores && (
                <Card className="mt-3 card-container">
                    <Card.Body>
                        {loadingScores ? (
                            <MySpinner />
                        ) : (
                            <ThesisScoreDetail
                                scores={scoresData?.scores || []}
                                averageScore={scoresData?.averageScore}
                            />
                        )}
                    </Card.Body>
                </Card>
            )}
        </div>
    );
};

export default ThesisDetailPage;