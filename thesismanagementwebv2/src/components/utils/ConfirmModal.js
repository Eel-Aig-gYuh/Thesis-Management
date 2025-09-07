import { Modal, Button } from 'react-bootstrap';
import { useTranslation } from 'react-i18next';
import MySpinner from '../layouts/MySpinner';

const ConfirmModal = ({ show, onHide, onConfirm, title, message, confirmText, cancelText, loading }) => {
    const { t } = useTranslation();

    return (
        <Modal show={show} onHide={onHide} centered>
            <Modal.Header closeButton>
                <Modal.Title>{title || t("confirm-title")}</Modal.Title>
            </Modal.Header>
            <Modal.Body>{message || t("confirm-message")}</Modal.Body>
            <Modal.Footer>
                <div style={{justifyContent: "flex-start"}}>
                    {loading && (<MySpinner/>)}
                </div>

                <Button variant="secondary" onClick={onHide} disabled={loading}>
                    {cancelText || t("cancel")}
                </Button>
                <Button variant="danger" onClick={onConfirm} disabled={loading}>
                    {confirmText || t("confirm")}
                </Button>
            </Modal.Footer>
        </Modal>
    );
};

export default ConfirmModal;