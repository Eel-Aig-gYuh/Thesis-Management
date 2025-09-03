import { useState, useEffect, useContext, useReducer } from "react";
import { useTranslation } from "react-i18next";
import { useToast } from "../contexts/ToastProvider";
import { Alert, Card, Col, Row, Image, CardBody, Button, Form } from "react-bootstrap";
import { MyDispatcherContext, MyUserContext } from "../../configs/MyContexts";
import MySpinner from "../layouts/MySpinner";
import "./style.css";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faPenToSquare } from "@fortawesome/free-solid-svg-icons";
import { authApis, endpoints } from "../../configs/Apis";
import { uploadAvatarFile } from "../../services/userService";

const STATUS_COLORS = {
    ROLE_ADMIN: "#FF652F",
    ROLE_GIAOVU: "#14A76C",
    ROLE_SINHVIEN: "#9B786F",
    ROLE_GIANGVIEN: "#374785",
};


const ProfilePage = () => {
    const { t } = useTranslation();
    const toast = useToast();
    const user = useContext(MyUserContext);
    const dispatch = useContext(MyDispatcherContext);
    const [error, setError] = useState("");
    const [loading, setLoading] = useState(false);
    const [showUpload, setShowUpload] = useState(false);
    const [uploading, setUploading] = useState(false);
    const [file, setFile] = useState(null);

    const toggleUpload = async () => {
        setShowUpload(!showUpload);
    }

    const handleChange = (e) => {
        setFile(e.target.files[0]);
    };

    const handleUploadSuccess = (updatedUser) => {
        dispatch({
            type: "updateUser",
            payload: updatedUser
        });
    };

    const handleUpload = async () => {
        if (!file) {
            toast(t("select-a-file"), "warning");
            return;
        }

        try {
            setUploading(true);
            const res = await uploadAvatarFile(file);
            console.log(res);
            handleUploadSuccess(res);
            toast(t("upload-success"), "success");
            setFile(null);
            setShowUpload(false);
        } catch (error) {
            toast(`${t("upload-failure")}: ${error.message}`, "danger");
        } finally {
            setUploading(false);
        }
    };

    if (!user) {
        return <Alert variant="warning">{t("please-login")}</Alert>;
    }

    return (
        <div className="container mt-4">
            <h2 className="theis-title-list text-center mb-5 fw-bold" style={{ color: "white" }}>
                {t("profile")}
            </h2>

            {error && <Alert variant="danger">{error}</Alert>}
            {loading ? (
                <MySpinner />
            ) : user ? (
                <div>
                    <Card className="content-info bg-white">
                        <Card.Title>
                            <div className="thesis-card-title"
                                style={{ backgroundColor: STATUS_COLORS[user?.role] || STATUS_COLORS.DRAFT, color: "white" }}>
                                <strong>{t(user?.role)}</strong>
                            </div>
                        </Card.Title>
                        <Card.Body>
                            <Row>
                                <Col md={4} className="text-center">
                                    <Image
                                        src={user?.avatar || "https://res.cloudinary.com/dnqt29l2e/image/upload/v1747068051/c47vxjryuhnfz2ljk3dn.jpg"}
                                        roundedCircle
                                        style={{ width: "150px", height: "150px", objectFit: "cover" }}
                                        className="mb-3"
                                    />


                                    <div>
                                        <Button className="stats-btn" onClick={toggleUpload}>
                                            <FontAwesomeIcon icon={faPenToSquare} className="p-1" />
                                        </Button>
                                    </div>

                                    <div >
                                        <span className="isActive-container"></span>
                                        <span className="isActive"></span>
                                    </div>
                                </Col>

                                <Col>
                                    <CardBody className="thesis-content-container">
                                        <Card.Text>
                                            <strong>{t("name")}: </strong>{" "}
                                            {user?.lastname} {user?.firstname}
                                        </Card.Text>
                                        <Card.Text>
                                            <strong>{t("username")}: </strong>{" "}
                                            {user?.username}
                                        </Card.Text>
                                        <Card.Text>
                                            <strong>Email: </strong>{" "}
                                            {user?.email}
                                        </Card.Text>
                                        <Card.Text>
                                            <strong>{t("major")}:</strong>{" "}
                                            {user?.major}
                                        </Card.Text>

                                    </CardBody>
                                </Col>
                            </Row>
                        </Card.Body>
                    </Card>

                    {showUpload && (
                        <Card className="p-4 content-info mt-4">
                            <Form.Group>
                                <Form.Label>
                                    <div className="fw-bold">
                                        {t("upload-avatar")}
                                    </div>
                                </Form.Label>
                                <Form.Control
                                    type="file"
                                    onChange={handleChange}
                                    disabled={uploading}
                                    accept=".img,.jpa,.jpg"
                                />
                                <Form.Text className="text-muted">
                                    {t("upload-avatar-hint")}
                                </Form.Text>
                            </Form.Group>
                            <Button
                                disabled={uploading || !file}
                                onClick={handleUpload}
                                className="mt-2 stats-btn mt-4"
                            >
                                {uploading ? t("uploading") : t("upload")}
                            </Button>
                        </Card>
                    )}
                </div>
            ) : (
                <Alert variant="info">{t("no-profile-data")}</Alert>
            )}
        </div>
    );
};

export default ProfilePage;
