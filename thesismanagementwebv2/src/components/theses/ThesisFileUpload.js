import { useContext, useMemo, useState } from "react";
import { useTranslation } from "react-i18next";
import { useToast } from "../contexts/ToastProvider";
import { Button, Form, ProgressBar } from "react-bootstrap";
import { uploadThesisFile } from "../../services/thesisService";
import { Link } from "react-router-dom";
import { MyUserContext } from "../../configs/MyContexts";

const ThesisFileUpload = ({ thesisId, onUploadSuccess, fileUrls }) => {
    const { t } = useTranslation();
    const toast = useToast();
    const [file, setFile] = useState(null);
    const [uploading, setUploading] = useState(false);
    const [progress, setProgress] = useState(0);
    const user = useContext(MyUserContext);

    const handleChange = (e) => {
        setProgress(20);
        setFile(e.target.files[0]);
    };

    const handleUpload = async () => {
        if (!file) {
            toast(t("select-a-file"), "warning");
            return;
        }

        try {
            setUploading(true);

            const response = await uploadThesisFile(thesisId, file);
            setProgress(80);
            toast(t("upload-success"), "success");
            onUploadSuccess(response);
        } catch (error) {
            toast(`${t("upload-failure")}: ${error.message}`, "danger");
        } finally {
            setUploading(false);
            setProgress(0);
        }
    };

    return (
        <div className="mt-3">
            {user?.role === "ROLE_SINHVIEN" && (
                <>
                    <Form.Group>
                        <Form.Label>{t("upload-thesis-file")}</Form.Label>
                        <Form.Control
                            type="file"
                            onChange={handleChange}
                            disabled={uploading}
                            accept=".pdf,.doc,.docx"
                        />
                        <Form.Text className="text-muted">
                            {t("upload-file-hint")}
                        </Form.Text>
                    </Form.Group>
                    <Button
                        variant="primary"
                        disabled={uploading || !file}
                        onClick={handleUpload}
                        className="mt-2"
                    >
                        {uploading ? t("uploading") : t("upload")}
                    </Button>
                </>
            )}
            {uploading && (
                <ProgressBar
                    className="mt-2"
                    now={progress}
                    label={`${progress}%`}
                />
            )}
            <div className="mt-3">
                <h6>{t("uploaded-files")}</h6>
                {fileUrls?.length > 0 ? (
                    <ul>
                        {fileUrls.map((file, index) => (
                            <li key={file.id || index}>
                                <Link
                                    to={file.fileUrl}
                                >
                                    {file.fileName}
                                </Link>
                                ({new Date(file.submittedAt).toLocaleString()})
                            </li>
                        ))}
                    </ul>
                ) : (
                    <p>{t("no-files")}</p>
                )}
            </div>
        </div>
    );
};

export default ThesisFileUpload;