import { useEffect, useRef, useState, useContext, useMemo } from "react";
import { useTranslation } from "react-i18next";
import { useToast } from "../contexts/ToastProvider";
import { useNavigate } from "react-router-dom";
import { Alert, Button, Card, Col, Row } from "react-bootstrap";
import { Link } from "react-router-dom";
import useLazyLoad from "../hooks/useLazyLoad";
import { MyUserContext } from "../../configs/MyContexts";
import ConfirmModal from "../utils/ConfirmModal";
import MySpinner from "../layouts/MySpinner";
import { getCouncils, deleteCouncil, getMyCouncils } from "../../services/councilService";

const STATUS_COLORS = {
    SCHEDULED: "#d1ecf1",
    LOCKED: "#d4edda",
    COMPLETED: "#fff3cd",
    CANCELED: "#f8d7da",
};

const CouncilManagePage = () => {
    const { t } = useTranslation();
    const toast = useToast();
    const navigate = useNavigate();
    const user = useContext(MyUserContext);

    const [showConfirm, setShowConfirm] = useState(false);
    const [deleteCouncilId, setDeleteCouncilId] = useState(null);
    const [filters, setFilters] = useState({ page: 1 });

    const fetchFunc = useMemo(() => {
        if (user?.role === "ROLE_GIAOVU") {
            return getCouncils;
        }
        else if (user?.role === "ROLE_GIANGVIEN") {
            return getMyCouncils;
        }
    }, [user?.role]);

    const { items: councils, loadMore, hasMore, loading, resetItems } = useLazyLoad(fetchFunc, filters);
    const observerRef = useRef(null);

    // Intersection observer cho lazy loading
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

    const renderNoResults = () => {
        if (councils.length === 0 && !loading) {
            return (
                <Alert variant="info" className="text-center">
                    {t("no-councils-found")}
                </Alert>
            );
        }
        return null;
    };

    return (
        <div className="container mt-4">
            <h2 className="text-center mb-4">📋 {t("manage-councils")}</h2>

            {user?.role === "ROLE_GIAOVU" && (
                <Button
                    as={Link}
                    to="/council/create"
                    variant="success"
                    className="mb-4"
                >
                    {t("create-council")}
                </Button>
            )}

            {renderNoResults()}

            <Row>
                {councils.map((council) => (
                    <Col key={council.id} xs={12} className="mb-3">
                        <Card
                            className="council-card px-3 py-2"
                            style={{
                                backgroundColor: STATUS_COLORS[council.status] || "#ffffff",
                            }}
                        >
                            <Card.Body>
                                <Card.Title className="mb-2">{council.name}</Card.Title>
                                <Row>
                                    <Col md={6}>
                                        <Card.Text>
                                            <strong>{t("defense-date")}:</strong>{" "}
                                            {new Date(council.defenseDate).toLocaleString()}
                                        </Card.Text>
                                        <Card.Text>
                                            <strong>{t("status")}:</strong>{" "}
                                            {t(council.status.toLowerCase())}
                                        </Card.Text>
                                        <Card.Text>
                                            <strong>{t("members")}:</strong>{" "}
                                            {council.members
                                                .map(
                                                    (m) =>
                                                        `${m.user.lastname} ${m.user.firstname} (${m.role})`
                                                )
                                                .join(", ")}
                                        </Card.Text>
                                    </Col>
                                    <Col md={6}>
                                        <Card.Text>
                                            <strong>{t("theses")}:</strong>{" "}
                                            {council.theses.length} ({council.theses.map((t) => t.title).join(", ")})
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
                                </Row>
                                <div className="mt-3">
                                    <Button
                                        as={Link}
                                        to={`/council/${council.id}`}
                                        variant="info"
                                        className="me-2 mb-2"
                                    >
                                        {t("view-details")}
                                    </Button>
                                    {user?.role === "ROLE_GIAOVU" && (
                                        <Button
                                            as={Link}
                                            to={`/council/edit/${council.id}`}
                                            variant="warning"
                                            className="me-2 mb-2"
                                        >
                                            {t("edit")}
                                        </Button>
                                    )}
                                </div>
                            </Card.Body>
                        </Card>
                    </Col>
                ))}
            </Row>

            <div ref={observerRef} style={{ height: "30px" }}></div>

            {loading && <MySpinner />}
            {!hasMore && councils.length > 0 && (
                <p className="text-center text-muted">{t("no-more-data")}</p>
            )}
        </div>
    );
};

export default CouncilManagePage;