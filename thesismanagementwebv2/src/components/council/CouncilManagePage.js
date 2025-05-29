import { useEffect, useRef, useState, useContext, useMemo } from "react";
import { useTranslation } from "react-i18next";
import { useToast } from "../contexts/ToastProvider";
import { useSearchParams } from "react-router-dom";
import { Alert, Button, Card, CardBody, Col, Form, Row } from "react-bootstrap";
import { Link } from "react-router-dom";
import useLazyLoad from "../hooks/useLazyLoad";
import { MyUserContext } from "../../configs/MyContexts";
import MySpinner from "../layouts/MySpinner";
import { getCouncils, getMyCouncils } from "../../services/councilService";

const STATUS_COLORS = {
    SCHEDULED: "#374785",
    LOCKED: "#14A76C",
    COMPLETED: "#9B786F",
    CANCELED: "#FF652F",
};

const CouncilManagePage = () => {
    const { t } = useTranslation();
    const toast = useToast();
    const user = useContext(MyUserContext);
    const [searchParams, setSearchParams] = useSearchParams();

    const [filters, setFilters] = useState({
        title: searchParams.get("title") || "",
        status: searchParams.get("status") || "",
        order: searchParams.get("order") || "desc",
    });

    const fetchFunc = useMemo(() => {
        if (user?.role === "ROLE_GIAOVU") {
            return getCouncils;
        } else if (user?.role === "ROLE_GIANGVIEN") {
            return getMyCouncils;
        }
    }, [user?.role]);

    const { items: councils, loadMore, hasMore, loading, resetItems } = useLazyLoad(fetchFunc, filters);

    const observerRef = useRef(null);

    // Sync filters với URL
    useEffect(() => {
        const newSearchParams = {};
        if (filters.title) newSearchParams.title = filters.title;
        if (filters.status) newSearchParams.status = filters.status;
        newSearchParams.order = filters.order;
        setSearchParams(newSearchParams, { replace: true });
    }, [filters, setSearchParams]);

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

    // Nhóm councils theo department
    const groupedByDepartment = councils.reduce((acc, council) => {
        if (!acc[council.department]) acc[council.department] = [];
        acc[council.department].push(council);
        return acc;
    }, {});

    const renderNoResults = () => {
        if (Object.keys(groupedByDepartment).length === 0 && !loading) {
            return (
                <Alert variant="info" className="text-center">
                    {t("no-councils-found")}
                </Alert>
            );
        }
        return null;
    };

    const handleFilterChange = (e) => {
        const { name, value } = e.target;
        setFilters((prev) => ({ ...prev, [name]: value }));
    };

    const handleSearch = (e) => {
        e.preventDefault();
        resetItems();
    };

    return (
        <div className="container mt-4">
            <h2 className="council-title-list text-center mb-4">{t("manage-councils")}</h2>

            <Form onSubmit={handleSearch} className="mb-4">
                <Row className="form-action">
                    <Col md={4}>
                        <Form.Control
                            className="border-input"
                            type="text"
                            placeholder={t("search-by-title")}
                            name="title"
                            value={filters.title}
                            onChange={handleFilterChange}
                        />
                    </Col>
                    <Col md={3}>
                        <Form.Select
                            name="status"
                            value={filters.status}
                            className="border-input"
                            onChange={handleFilterChange}
                        >
                            <option value="">{t("all-statuses")}</option>
                            <option value="SCHEDULED">{t("scheduled")}</option>
                            <option value="LOCKED">{t("locked")}</option>
                            <option value="COMPLETED">{t("completed")}</option>
                            <option value="CANCELED">{t("cancel")}</option>
                        </Form.Select>
                    </Col>
                    <Col md={3}>
                        <Form.Select
                            name="order"
                            value={filters.order}
                            className="border-input"
                            onChange={handleFilterChange}
                        >
                            <option value="desc">{t("newest")}</option>
                            <option value="asc">{t("oldest")}</option>
                        </Form.Select>
                    </Col>
                    <Col md={2}>
                        <Button type="submit" className="w-100 btn-action">{t("filter")}</Button>
                    </Col>
                </Row>
            </Form>

            {renderNoResults()}

            {Object.keys(groupedByDepartment).map((department) => (
                <div key={department} className="mb-4">
                    <h4 className="council-department" style={{ textAlign: "right", fontWeight: "bold" }}>
                        {department}
                    </h4>
                    <Row>
                        {groupedByDepartment[department].map((council) => (
                            <Col key={council.id} xs={12} className="mb-5 card-container" style={{ paddingBottom: "10px" }}>
                                <Card
                                    className="council-card px-3 py-2 mt-2"
                                    style={{
                                        backgroundColor: "white"
                                    }}
                                >
                                    <Card.Title>
                                        <div className="thesis-card-title"
                                            style={{ backgroundColor: STATUS_COLORS[council.status] || STATUS_COLORS.DRAFT, color: "white" }}>
                                            <strong> {t(council.status.toLowerCase())}</strong>
                                        </div>
                                    </Card.Title>
                                    <Card.Body>
                                        <Card.Title className="mb-4 thesis-content-title">
                                            <div className="fs-4" style={{ fontWeight: "bold" }}>
                                                {council.name}
                                            </div>
                                        </Card.Title>

                                        <Row>
                                            <Col md={6}>
                                                <CardBody className="thesis-content-container">
                                                    <Card.Text>
                                                        <strong>{t("defense-location")}:</strong>{" "}
                                                        {council.defenseLocation}
                                                    </Card.Text>
                                                    <Card.Text>
                                                        <strong>{t("defense-date")}:</strong>{" "}
                                                        {new Date(council.defenseDate).toLocaleString()}
                                                    </Card.Text>
                                                    <Card.Text>
                                                        <strong>{t("status")}:</strong>{" "}
                                                        {t(council.status.toLowerCase())}
                                                    </Card.Text>
                                                    <Card.Text>
                                                        <strong>{t("created-by")}:</strong>{" "}
                                                        {council.createdBy.lastname} {council.createdBy.firstname}
                                                    </Card.Text>
                                                    <Card.Text>
                                                        <strong>{t("created-at")}:</strong>{" "}
                                                        {new Date(council.createdAt).toLocaleString()}
                                                    </Card.Text>

                                                </CardBody>
                                                <Row className="mt-4">
                                                    <Col md={7}>
                                                        <Button
                                                            as={Link}
                                                            to={`/council/${council.id}`}
                                                            variant="warning"
                                                            className="me-2 thesis-btn"
                                                        >
                                                            {t("view-details-council")}
                                                        </Button>
                                                    </Col>

                                                    <Col md={5} className="text-end">
                                                        {user?.role === "ROLE_GIAOVU" && (
                                                            <Button
                                                                as={Link}
                                                                to={`/council/edit/${council.id}`}
                                                                variant="warning"
                                                                className="mb-2 thesis-btn"
                                                            >
                                                                {t("edit")}
                                                            </Button>
                                                        )}
                                                    </Col>
                                                </Row>
                                            </Col>
                                            <Col md={6}>
                                                <Card.Text>
                                                    <strong>{t("theses")}:</strong>
                                                    <ul>
                                                        {council.theses.map((t, index) => (
                                                            <li key={index}>{t.title}</li>
                                                        ))}
                                                    </ul>
                                                </Card.Text>

                                                <Card.Text>
                                                    <strong>{t("members")}:</strong>
                                                    <ul>
                                                        {council.members.map((m, index) => (
                                                            <li key={index}>
                                                                {m.user.lastname} {m.user.firstname} ({m.role})
                                                            </li>
                                                        ))}
                                                    </ul>
                                                </Card.Text>
                                            </Col>

                                        </Row>
                                    </Card.Body>
                                </Card>
                            </Col>
                        ))}
                    </Row>
                </div>
            ))}

            <div ref={observerRef} style={{ height: "30px" }}></div>

            {loading && <MySpinner />}
            {!hasMore && Object.keys(groupedByDepartment).length > 0 && (
                <p className="text-center text-muted">{t("no-more-data")}</p>
            )}
        </div>
    );
};

export default CouncilManagePage;