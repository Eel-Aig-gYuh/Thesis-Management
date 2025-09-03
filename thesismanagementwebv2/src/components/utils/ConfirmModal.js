import { Modal, Button } from 'react-bootstrap';
import { useTranslation } from 'react-i18next';

const ConfirmModal = ({ show, onHide, onConfirm, title, message, confirmText, cancelText }) => {
    const { t } = useTranslation();

    return (
        <Modal show={show} onHide={onHide} centered>
            <Modal.Header closeButton>
                <Modal.Title>{title || t("confirm-title")}</Modal.Title>
            </Modal.Header>
            <Modal.Body>{message || t("confirm-message")}</Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={onHide}>
                    {cancelText || t("cancel")}
                </Button>
                <Button variant="danger" onClick={onConfirm}>
                    {confirmText || t("confirm")}
                </Button>
            </Modal.Footer>
        </Modal>
    );
};

export default ConfirmModal;