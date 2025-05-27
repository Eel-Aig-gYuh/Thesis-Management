import { useState, useEffect, useMemo, useContext } from "react";
import { useTranslation } from "react-i18next";
import { useToast } from "../contexts/ToastProvider";
import { Link, useNavigate } from "react-router-dom";
import { Button, Form, Card, Table, Row, Col, Alert } from "react-bootstrap";
import ThesisDualList from "../theses/ThesisDualList";
import { fetchAvailableThesesCriteria } from "../../services/thesisService";
import useLazyLoad from "../hooks/useLazyLoad";
import { MyUserContext } from "../../configs/MyContexts";
import { createCriteria } from "../../services/criteriaService";

const CriteriaCreatePage = () => {
    const { t } = useTranslation();
    const toast = useToast();
    const navigate = useNavigate();
    const user = useContext(MyUserContext);

    // Criteria state
    const [criterias, setCriterias] = useState([
        { criteriaName: "", maxScore: "" },
        { criteriaName: "", maxScore: "" },
        { criteriaName: "", maxScore: "" },
    ]);
    const [error, setError] = useState("");

    // Thesis selection state
    const [selectedTheses, setSelectedTheses] = useState([]);
    const [page, setPage] = useState(0);
    const [loading, setLoading] = useState(false);
    const [search, setSearch] = useState("");
    const [filters, setFilters] = useState([]);

    const { items: availableTheses, loadMore, hasMore, resetItems } = useLazyLoad(fetchAvailableThesesCriteria, filters);

    const uniqueTheses = useMemo(() => {
        return Array.from(new Map(availableTheses.map((thesis) => [thesis.id, thesis])).values());
    }, [availableTheses]);

    // Handle criteria input change
    const handleCriteriaChange = (index, field, value) => {
        const updatedCriterias = [...criterias];
        updatedCriterias[index] = { ...updatedCriterias[index], [field]: value };
        setCriterias(updatedCriterias);
        setError("");
    };

    // Add new criterion
    const addCriteria = () => {
        setCriterias([...criterias, { criteriaName: "", maxScore: "" }]);
        setError("");
    };

    // Delete criterion (only if index >= 3)
    const deleteCriteria = (index) => {
        if (index < 3) {
            toast(t("cannot-delete-minimum-criteria"), "warning");
            return;
        }
        const updatedCriterias = criterias.filter((_, i) => i !== index);
        setCriterias(updatedCriterias);
        setError("");
    };

    // Validate and submit form
    const handleSubmit = async (e) => {
        e.preventDefault();

        // Validation
        if (criterias.length < 3) {
            setError(t("minimum-3-criteria"));
            return;
        }

        const totalScore = criterias.reduce((sum, c) => sum + (parseFloat(c.maxScore) || 0), 0);
        if (totalScore !== 10) {
            setError(t("total-score-must-be-10"));
            return;
        }

        if (criterias.some(c => !c.criteriaName || !c.maxScore || c.maxScore <= 0)) {
            setError(t("invalid-criteria"));
            return;
        }

        if (selectedTheses.length === 0) {
            setError(t("select-at-least-one-thesis"));
            return;
        }

        // Prepare payload
        const payload = {
            criterias: criterias.map(c => ({
                criteriaName: c.criteriaName,
                maxScore: parseFloat(c.maxScore),
            })),
            thesisId: selectedTheses.map(t => t.id),
            createdBy: [user?.id], // Hardcoded as per sample; adjust based on auth context
        };

        try {
            await createCriteria(payload);
            toast(t("create-criteria-success"), "success");
            navigate("/thesis"); // Redirect after success
        } catch (err) {
            toast(`${t("create-criteria-failure")}: ${err.message}`, "danger");
            setError(err.message);
        }
    };

    return (
        <div className="container mt-4">
            <h2 className="text-center mb-4">📝 {t("create-grading-criteria")}</h2>
            <Card>
                <Card.Body>
                    <Form onSubmit={handleSubmit}>
                        <h5>{t("grading-criteria")}</h5>
                        {error && <Alert variant="danger">{error}</Alert>}
                        <Table striped bordered hover responsive>
                            <thead>
                                <tr>
                                    <th>{t("criteria-name")}</th>
                                    <th>{t("max-score")}</th>
                                    <th>{t("actions")}</th>
                                </tr>
                            </thead>
                            <tbody>
                                {criterias.map((criteria, index) => (
                                    <tr key={index}>
                                        <td>
                                            <Form.Control
                                                type="text"
                                                value={criteria.criteriaName}
                                                onChange={(e) => handleCriteriaChange(index, "criteriaName", e.target.value)}
                                                placeholder={t("enter-criteria-name")}
                                                required
                                            />
                                        </td>
                                        <td>
                                            <Form.Control
                                                type="number"
                                                step="0.1"
                                                min="0"
                                                value={criteria.maxScore}
                                                onChange={(e) => handleCriteriaChange(index, "maxScore", e.target.value)}
                                                placeholder={t("enter-max-score")}
                                                required
                                            />
                                        </td>
                                        <td>
                                            {index >= 3 && (
                                                <Button
                                                    variant="danger"
                                                    size="sm"
                                                    onClick={() => deleteCriteria(index)}
                                                >
                                                    {t("delete")}
                                                </Button>
                                            )}
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                            <tfoot>
                                <tr>
                                    <td colSpan="2">
                                        <strong>{t("total-score")}: </strong>
                                        {criterias.reduce((sum, c) => sum + (parseFloat(c.maxScore) || 0), 0).toFixed(2)}
                                    </td>
                                    <td>
                                        <Button variant="primary" size="sm" onClick={addCriteria}>
                                            {t("add-criteria")}
                                        </Button>
                                    </td>
                                </tr>
                            </tfoot>
                        </Table>

                        <h5 className="mt-4">{t("select-theses")}</h5>
                        <ThesisDualList
                            availableTheses={uniqueTheses}
                            selectedTheses={selectedTheses}
                            setSelectedTheses={setSelectedTheses}
                            maxHeight={300}
                            loadMore={loadMore}
                            hasMore={hasMore}
                            loading={loading}
                            resetItems={resetItems}
                        />

                        <div className="text-end mt-4">
                            <Button 
                                variant="secondary" 
                                className="me-2" 
                                as={Link}
                                to = {"/thesis/"}
                                >
                                {t("cancel")}
                            </Button>
                            <Button variant="primary" type="submit">
                                {t("save")}
                            </Button>
                        </div>
                    </Form>
                </Card.Body>
            </Card>
        </div>
    );
};

export default CriteriaCreatePage;