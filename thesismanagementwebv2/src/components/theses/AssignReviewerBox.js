import { useState } from 'react'
import { useToast } from '../contexts/ToastProvider'
import { useTranslation } from 'react-i18next';
import "../../i18n/index"
import { assignReviewer, updateThesisStatus } from '../../services/thesisService';
import SearchableUserSelectBox from '../utils/SearchableUserSelectBox';
import { Button } from 'react-bootstrap';
import ConfirmModal from '../utils/ConfirmModal';
import "./style.css";

export default function AssignReviewerBox({ thesisId, onSuccess, onCancel }) {
    const toast = useToast();
    const { t } = useTranslation();
    const [selectedReviewerId, setSelectedReviewerId] = useState(null);
    const [loading, setLoading] = useState(false);
    const [showConfirm, setShowConfirm] = useState(false);

    const handleAssign = async () => {
        setLoading(true);
        try {
            await assignReviewer(thesisId, {
                reviewerIds: [selectedReviewerId]
            });

            await updateThesisStatus(thesisId, { status: "REGISTERED" });

            toast(t("assign-reviewer-success"), "success");
            setSelectedReviewerId(null);
            onSuccess?.();
        } catch (err) {
            toast(t("assign-reviewer-failure"), "danger");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="mt-3 p-3 form-container">
            <h5>{t("assign-reviewer-title")}</h5>

            <SearchableUserSelectBox
                label={t('assign-reviewer-label')}
                role="ROLE_GIANGVIEN"
                isMulti={true}
                selectedIds={selectedReviewerId ? [selectedReviewerId] : []}
                onChange={(ids) => setSelectedReviewerId(ids[0] || null)}
            />

            <div className="d-flex justify-content-between mt-3">
                <div>
                    <Button className='thesis-btn' variant="secondary" onClick={onCancel}>
                        {t("close")}
                    </Button>
                </div>

                <Button
                    variant="success"
                    disabled={!selectedReviewerId}
                    className="thesis-btn"
                    onClick={() => setShowConfirm(true)}
                >
                    {t("assign")}
                </Button>
            </div>

            <ConfirmModal
                show={showConfirm}
                onHide={() => setShowConfirm(false)}
                onConfirm={handleAssign}
                title="Xác nhận phân công"
                message="Bạn có chắc chắn muốn phân công giảng viên này làm phản biện?"
                confirmText="Phân công"
                cancelText="Hủy"
                loading={loading}
            />
        </div>
    );
}
