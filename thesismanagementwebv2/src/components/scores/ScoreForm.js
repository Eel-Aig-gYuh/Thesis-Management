import { useState, useEffect, useContext } from "react";
import { useTranslation } from "react-i18next";
import { useToast } from "../contexts/ToastProvider";
import { useParams } from "react-router-dom";
import { Table, Form, Button, Alert } from "react-bootstrap";
import { MyUserContext } from "../../configs/MyContexts";
import MySpinner from "../layouts/MySpinner";
import ConfirmModal from "../utils/ConfirmModal";
import { submitCouncilScores } from "../../services/scoreService";
import "./style.css";

// Component ScoreForm: Hiển thị bảng để nhập điểm cho các khóa luận
// Props:
// - thesesData: Mảng [{thesisId, thesisTitle, criteria}] hoặc đối tượng {"thesisId": {thesisTitle, criteria}}
// - councilStatus: Trạng thái hội đồng ("SCHEDULED")
const ScoreForm = ({ thesesData, councilStatus }) => {
    const { t } = useTranslation(); // Dịch văn bản
    const toast = useToast(); // Thông báo
    const { councilId } = useParams(); // Lấy councilId
    const user = useContext(MyUserContext); // Người dùng

    // In dữ liệu để kiểm tra
    console.log("Dữ liệu thesesData:", thesesData);

    // Chuyển thesesData thành mảng
    let thesesList = [];
    if (Array.isArray(thesesData)) {
        thesesList = thesesData;
    } else if (thesesData && typeof thesesData === "object") {
        for (const thesisId in thesesData) {
            thesesList.push({
                thesisId: thesisId,
                thesisTitle: thesesData[thesisId].thesisTitle,
                criteria: thesesData[thesisId].criteria,
            });
        }
    }
    console.log("Danh sách khóa luận:", thesesList);

    // Tìm tất cả tiêu chí độc nhất
    const allCriteria = [];
    const seenCriteriaIds = [];
    for (const thesis of thesesList) {
        if (thesis.criteria && Array.isArray(thesis.criteria)) {
            for (const criterion of thesis.criteria) {
                if (!seenCriteriaIds.includes(criterion.criteriaId)) {
                    seenCriteriaIds.push(criterion.criteriaId);
                    allCriteria.push(criterion);
                }
            }
        }
    }
    console.log("Danh sách tiêu chí:", allCriteria);

    // Trạng thái điểm: { thesisId: { criteriaId: điểm } }
    const [scores, setScores] = useState({});

    // Đồng bộ scores với thesesData
    useEffect(() => {
        const newScores = {};
        for (const thesis of thesesList) {
            newScores[thesis.thesisId] = newScores[thesis.thesisId] || {};
            if (thesis.criteria && Array.isArray(thesis.criteria)) {
                for (const criterion of thesis.criteria) {
                    // Ưu tiên score từ thesesData nếu không phải null, nếu không thì lấy từ scores hiện tại hoặc ""
                    newScores[thesis.thesisId][criterion.criteriaId] =
                        criterion.score != null
                            ? criterion.score.toString()
                            : scores[thesis.thesisId]?.[criterion.criteriaId] || "";
                }
            }
        }
        setScores(newScores);
    }, [thesesData]);

    const [errorMessage, setErrorMessage] = useState("");
    const [isLoading, setIsLoading] = useState(false);
    const [showConfirm, setShowConfirm] = useState(null);

    // Cập nhật điểm
    const updateScore = (value, thesisId, criteriaId) => {
        setScores(currentScores => ({
            ...currentScores,
            [thesisId]: {
                ...currentScores[thesisId],
                [criteriaId]: value,
            },
        }));
        setErrorMessage("");
    };

    // Kiểm tra điểm hợp lệ
    const checkScoresValid = (thesisId, thesisCriteria) => {
        if (!thesisCriteria || !Array.isArray(thesisCriteria)) return false;
        for (const criterion of thesisCriteria) {
            const score = parseFloat(scores[thesisId]?.[criterion.criteriaId]);
            if (isNaN(score) || score < 0 || score > criterion.maxScore) {
                setErrorMessage(
                    t("invalid-score", {
                        criteria: criterion.name || criterion.criteriaName || "Tiêu chí",
                        max: criterion.maxScore,
                    })
                );
                return false;
            }
        }
        return true;
    };

    // Chuẩn bị lưu điểm
    const prepareSaveScores = (thesisId, thesisCriteria) => {
        if (!checkScoresValid(thesisId, thesisCriteria)) return;

        const payload = {
            councilId: parseInt(councilId),
            thesisId: thesisId,
            userId: user.id,
            scores: thesisCriteria.map(criterion => ({
                criteriaId: criterion.criteriaId,
                score: parseFloat(scores[thesisId][criterion.criteriaId]),
            })),
        };
        console.log("Dữ liệu gửi:", payload);

        setShowConfirm(payload);
    };

    // Gửi điểm
    const sendScores = async () => {
        if (!showConfirm) return;

        setIsLoading(true);
        try {
            await submitCouncilScores(councilId, showConfirm);
            toast(t("submit-scores-success"), "success");

            setScores(currentScores => ({
                ...currentScores,
                [showConfirm.thesisId]: Object.keys(currentScores[showConfirm.thesisId]).reduce((acc, critId) => ({
                    ...acc,
                    [critId]: "",
                }), {}),
            }));
            setShowConfirm(null);
        } catch (error) {
            const errorMsg = error.response?.data || error.message;
            toast(`${t("submit-scores-failure")}: ${errorMsg}`, "danger");
            setErrorMessage(errorMsg);
        } finally {
            setIsLoading(false);
        }
    };

    // Kiểm tra quyền
    const canGrade = councilStatus === "SCHEDULED" && user?.role === "ROLE_GIANGVIEN";

    if (!canGrade) {
        return <Alert variant="warning">{t("no-grading-permission")}</Alert>;
    }

    if (!thesesList.length) {
        console.error("Không có dữ liệu khóa luận");
        return <Alert variant="danger">{t("no-theses-data")}</Alert>;
    }

    return (
        <div className="container mt-4 card-container">
            <h2 className="text-center mb-4">{t("grade-theses")}</h2>
            {errorMessage && <Alert variant="danger">{errorMessage}</Alert>}
            {isLoading && <MySpinner />}
            <Table striped bordered hover responsive>
                <thead>
                    <tr>
                        <th>{t("thesis-title")}</th>
                        {allCriteria.map(criterion => (
                            <th key={criterion.criteriaId}>
                                {(criterion.name || criterion.criteriaName)} ({criterion.maxScore.toFixed(1)})
                            </th>
                        ))}
                        <th>{t("action")}</th>
                    </tr>
                </thead>
                <tbody>
                    {thesesList.map(thesis => (
                        <tr key={thesis.thesisId}>
                            <td>{thesis.thesisTitle}</td>
                            {allCriteria.map(criterion => {
                                const hasCriterion = thesis.criteria?.some(
                                    crit => crit.criteriaId === criterion.criteriaId
                                );
                                return (
                                    <td key={criterion.criteriaId}>
                                        {hasCriterion ? (
                                            <Form.Control
                                                type="number"
                                                step="0.1"
                                                min="0"
                                                max={criterion.maxScore}
                                                value={scores[thesis.thesisId]?.[criterion.criteriaId] || ""}
                                                onChange={(e) =>
                                                    updateScore(e.target.value, thesis.thesisId, criterion.criteriaId)
                                                }
                                                disabled={!canGrade}
                                                required
                                            />
                                        ) : (
                                            <span>-</span>
                                        )}
                                    </td>
                                );
                            })}
                            <td>
                                <Button
                                    variant="primary"
                                    size="sm"
                                    onClick={() => prepareSaveScores(thesis.thesisId, thesis.criteria)}
                                    disabled={!canGrade || isLoading}
                                >
                                    {t("save-scores")}
                                </Button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </Table>
            <ConfirmModal
                show={!!showConfirm}
                onHide={() => setShowConfirm(null)}
                onConfirm={sendScores}
                title={t("confirm-submit-scores-title")}
                message={t("confirm-submit-scores-message")}
                confirmText={t("save-scores")}
                cancelText={t("cancel")}
            />
        </div>
    );
};

export default ScoreForm;