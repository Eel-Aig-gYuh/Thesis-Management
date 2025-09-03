import { createContext, useContext, useState } from 'react'
import { Toast, ToastContainer } from 'react-bootstrap';
import "../../i18n/index";
import "./style.css";
import { useTranslation } from 'react-i18next';

const ToastContext = createContext();

export const useToast = () => useContext(ToastContext);

export const ToastProvider = ({children}) => {
    const [toast, setToast] = useState({message: "", show: false, variant: "success"});
    const { t, i18n } = useTranslation();

    const showToast = (message, variant = "success") => {
        setToast({message, show: true, variant});

        // Tự ẩn sau 5s
        setTimeout(() => {
            setToast({ ...toast, show: false});
        }, 5000);
    }

    return (
        <ToastContext.Provider value={showToast}>
            {children}

            <ToastContainer position='top-end' className='p-3 toast-container'>
                <Toast show={toast.show} onClose={() => setToast({ ...toast, show: false})} bg={toast.variant} className='slide-in'>
                    <Toast.Header>
                        <strong className='me-auto'>{t('noti-title')}</strong>
                    </Toast.Header>

                    <Toast.Body className='text-white'>{toast.message}</Toast.Body>
                </Toast>
            </ToastContainer>

        </ToastContext.Provider>
    );
}
