import { useState, useEffect, useContext, use } from "react";
import { useTranslation } from "react-i18next";
import { useToast } from "../contexts/ToastProvider";
import { useNavigate, useParams } from "react-router-dom";
import { createThesis, fetchThesisById, updateThesis } from "../../services/thesisService";
import { Alert, Button, Col, Form, Row } from "react-bootstrap";
import MySpinner from "../layouts/MySpinner";
import SearchableUserSelectBox from "../utils/SearchableUserSelectBox";
import { MyUserContext } from "../../configs/MyContexts";
import { fetchDepartment } from "../../services/departmentService";
import { faQuestion } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";

export default function ThesisFormPage() {
    const { t } = useTranslation();
    const toast = useToast();
    const navigate = useNavigate();
    const { thesisId } = useParams();
    const user = useContext(MyUserContext);
    const [departments, setDepartments] = useState([]);

    const [formData, setFormData] = useState({
        title: "",
        semester: "",
        studentIds: [],
        supervisorIds: [],
        status: "DRAFT",
        departmentId: null,
    });
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        const loadDepartments = async () => {
            setLoading(true);
            try {
                const res = await fetchDepartment();
                console.log(res);
                setDepartments(res);
                console.log(departments);
            } catch (err) {
                toast(`${t("fetch-department-failure")}: ${err.response?.data || err.message}`, "danger");
                setError(err.response?.data || err.message);
            } finally {
                setLoading(false);
            }
        };
        loadDepartments();
    }, []);

    // Fetch thesis data if editing
    useEffect(() => {
        if (thesisId) {
            const fetchThesis = async () => {
                setLoading(true);
                try {
                    const thesis = await fetchThesisById(thesisId);
                    setFormData({
                        title: thesis.title || "",
                        semester: thesis.semester || "",
                        studentIds: thesis.students?.map((s) => s.id) || [],
                        supervisorIds: thesis.supervisors?.map((s) => s.id) || [],
                        status: thesis.status || "DRAFT",
                        departmentId: thesis.department?.id || null,
                    });
                } catch (err) {
                    toast(`${t("fetch-thesis-failure")}: ${err.response?.data || err.message}`, "danger");
                    setError(err.response?.data || err.message);
                } finally {
                    setLoading(false);
                }
            };
            fetchThesis();
        }
    }, [thesisId, toast, t]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData((prev) => ({ ...prev, [name]: value }));
    };

    const handleSupervisorChange = (ids) => {
        if (ids.length > 2) {
            toast(t("max-two-supervisors"), "danger");
            return;
        }
        setFormData((prev) => ({ ...prev, supervisorIds: ids }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!user || user.role !== "ROLE_GIAOVU") {
            toast(t("only-giaovu-can-update"), "danger");
            return;
        }

        setLoading(true);
        try {
            if (thesisId) {
                await updateThesis(thesisId, formData);
                toast(t("update-thesis-success"), "success");
            } else {
                await createThesis({ ...formData, status: "DRAFT" });
                toast(t("create-thesis-success"), "success");
                setFormData({ title: "", semester: "", studentIds: [], supervisorIds: [], status: "DRAFT" });
            }
            navigate("/thesis");
        } catch (err) {
            const errorMessage = err.response?.data || err.message;
            toast(
                `${thesisId ? t("update-thesis-failure") : t("create-thesis-failure")}: ${errorMessage}`,
                "danger"
            );
            setError(errorMessage);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="thesis-form-container">
            <h2 className="theis-title-list text-center mb-4 fw-bold" style={{ color: "white" }}>
                {thesisId ? t("edit-thesis") : t("create-thesis")}
            </h2>


            {error && <Alert variant="danger">{error}</Alert>}
            {loading ? (
                <MySpinner />
            ) : (
                <Form onSubmit={handleSubmit}>
                    <Form.Group className="mb-3">
                        <Form.Label>
                            <div className="fw-bold"> 
                                {t("thesis-title")}
                            </div>
                        </Form.Label>
                        <Form.Control
                            type="text"
                            name="title"
                            value={formData.title}
                            onChange={handleChange}
                            required
                        />
                    </Form.Group>

                    <Row className="thesis-form-info">
                        <Col md={6}>
                            <Form.Group className="mb-3">
                                <Form.Label>
                                    <div className="fw-bold">
                                        {t("department")}
                                    </div>
                                </Form.Label>
                                <Form.Select
                                    name="departmentId"
                                    value={formData.departmentId || ""}
                                    onChange={handleChange}
                                    required
                                >
                                    <option value="">{t("select-department")}</option>
                                    {Array.isArray(departments) && departments.map((dept) => (
                                        <option key={dept.id} value={dept.id}>
                                            {dept.name}
                                        </option>
                                    ))}
                                </Form.Select>
                            </Form.Group>
                        </Col>
                        <Col md={6}>
                            <Form.Group className="mb-3">
                                <Form.Label>
                                    <div className="fw-bold">
                                        {t("semester")}
                                    </div>
                                </Form.Label>
                                <Form.Control
                                    type="text"
                                    name="semester"
                                    value={formData.semester}
                                    onChange={handleChange}
                                />
                            </Form.Group>
                        </Col>
                    </Row>
                    <Row>
                        <Col md={6}>
                            <SearchableUserSelectBox
                                role="ROLE_SINHVIEN"
                                selectedIds={formData.studentIds}
                                onChange={(ids) => setFormData((prev) => ({ ...prev, studentIds: ids }))}
                                label={t("students")}
                            />
                        </Col>
                        <Col md={6}>
                            <Form.Group className="mb-3">
                                <SearchableUserSelectBox
                                    role="ROLE_GIANGVIEN"
                                    selectedIds={formData.supervisorIds}
                                    onChange={handleSupervisorChange}
                                    label={t("supervisors")}
                                />
                                <Form.Text className="text-muted" style={{textAlign: "right"}}>
                                    <div className="">
                                        <FontAwesomeIcon icon={faQuestion} />  {t("max-two-supervisors-hint")}
                                    </div>
                                </Form.Text>
                            </Form.Group>
                        </Col>
                    </Row>
                    <Row>
                        <Col md={6}></Col>
                        <Col md={6} style={{textAlign: 'right'}}>
                            <Button className="thesis-btn text-end" 
                                type="submit" variant="primary" disabled={loading}>
                                {loading ? <MySpinner size="sm" /> : thesisId ? t("update") : t("create-thesis")}
                            </Button>
                        </Col>
                    </Row>
                </Form>
            )}
        </div>
    );
}