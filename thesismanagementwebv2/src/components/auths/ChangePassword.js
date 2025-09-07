import React, { useContext, useState } from 'react'
import { MyDispatcherContext, MyUserContext } from '../../configs/MyContexts'
import { useNavigate } from 'react-router-dom';
import "../../i18n/index"
import { Alert, Button, Col, Row, Form } from 'react-bootstrap';
import { useTranslation } from 'react-i18next';
import { authApis, endpoints } from '../../configs/Apis';
import { useToast } from '../contexts/ToastProvider';
import MySpinner from '../layouts/MySpinner';
import { auth } from '../../configs/FirebaseConfig';
import { updatePassword } from 'firebase/auth';

export default function ChangePassword() {
    const user = useContext(MyUserContext);
    const dispatch = useContext(MyDispatcherContext);
    const toast = useToast();
    const nav = useNavigate();

    const [loading, setLoading] = useState(false);

    const { t } = useTranslation();

    const info = [{
        title: t("pass-oldpass"),
        field: "oldPassword",
        type: "password"
    }, {
        title: t("pass-newpass"),
        field: "newPassword",
        type: "password"
    }, {
        title: t("pass-confirm"),
        field: "confirmPassword",
        type: "password"
    }];

    const [form, setForm] = useState({});
    const [err, setErr] = useState(null);

    const handleChange = (field, value) => {
        setForm({ ...form, [field]: value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        setLoading(true);

        console.log(user.id);
        console.log(form);

        if (form.new_password !== form.confirm_password) {
            setErr('Mật khẩu mới không khớp!');
            setLoading(false);
            return;
        }

        if (form.newPassword === process.env.REACT_APP_DEFAULT_PASSWORD) {
            setErr('Mật khẩu không hợp lệ !');
            setLoading(false);
            return ;
        }

        try {
            await authApis().put(endpoints['auth/change-password'](user.id), {
                oldPassword: form.oldPassword,
                newPassword: form.newPassword,
            });

            // cập nhật mật khẩu mới trong firebase
            const firebaseUser=auth.currentUser;
            if (firebaseUser) {
                await updatePassword(firebaseUser, form.newPassword);
                console.log("Firebase đã cập nhật password mới !");
            } else {
                console.log("Không có user trên firebase");
            }



            toast(t("pass-change-success"), "success");
            nav("/");
        } catch (ex) {
            setErr('Đổi mật khẩu thất bại. Vui lòng kiểm tra lại.');
            toast(t("pass-change-failure"), "danger");
            console.error(ex);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div>
            <Row className="justify-content-md-center">
                <Col md={6}>
                    <h2 className="text-center mb-4">Đổi mật khẩu</h2>

                    {err && <Alert variant="danger">{err}</Alert>}

                    <Form onSubmit={handleSubmit}>
                        {info.map(i => <Form.Control value={form[i.field]} onChange={e => handleChange(i.field, e.target.value)} className="mt-3 mb-1" key={i.field} type={i.type} placeholder={i.title} required />)}

                        {loading === true ? <MySpinner /> : <Button type="submit" variant="primary" className="mt-3 mb-1 w-100">Đổi mật khẩu</Button>}
                    </Form>
                </Col>
            </Row>
        </div>
    );
}
