import React, { useContext, useState } from 'react'
import { useNavigate } from 'react-router-dom';
import { MyDispatcherContext } from '../../configs/MyContexts';
import Apis, { authApis, endpoints } from '../../configs/Apis';
import Cookies from 'js-cookie';
import MySpinner from '../layouts/MySpinner';
import { Alert, Button, Col, Container, Form, Row, Toast } from 'react-bootstrap';
import Sidebar from '../layouts/Sidebar';
import NavbarVertical from '../layouts/NavbarVertical';
import { useTranslation } from 'react-i18next';
import '../../i18n/index';
import { useToast } from '../contexts/ToastProvider';

export default function Login() {
    const { t, i18n } = useTranslation();
    
    const info = [{
        title: "Tên tài khoản",
        field: "username",
        type: "text"
    }, {
        title: "Mật khẩu",
        field: "password",
        type: "password"
    }];

    const [user, setUser] = useState({});
    const toast = useToast();

    const [msg, setMsg] = useState();
    const [loading, setLoading] = useState(false);

    const nav = useNavigate();
    const dispatch = useContext(MyDispatcherContext);

    const setState = (value, field) => {
        setUser({ ...user, [field]: value });
    }

    const login = async (e) => {
        e.preventDefault();

        try {
            setLoading(true);
            let res = await Apis.post(endpoints['auth/login'], {
                ...user
            });

            console.log(user);

            Cookies.set('token', res.data.token);

            let u = await authApis().get(endpoints['current-user']);
            console.info(u.data.password);

            dispatch({
                "type": "login",
                "payload": u.data
            });

            // hiển thị thông báo.
            toast(t("noti-login-success"), "success");
            console.log(process.env.REACT_APP_DEFAULT_PASSWORD);
            if (u?.data?.password === process.env.REACT_APP_DEFAULT_PASSWORD && u?.data?.role !== "ROLE_ADMIN") {
                nav("/auth/change-password");
            } else {
                nav("/");
            }
        } catch (ex) {
            console.error(ex);
            toast(t("noti-login-failure"), "danger");
        } finally {
            setLoading(false);
        }
    }

    return (
        <Container fluid>
            <Row className="vh-100">
                {/* Left Nav */}
                <Col md={2} className="bg-dark text-white p-3">
                    <NavbarVertical />
                </Col>

                {/* Main Content */}
                <Col md={8} className="p-4">
                    <div>
                        <h1 className="text-center text-success mt-1">ĐĂNG NHẬP</h1>

                        {msg && <Alert variant="danger">{msg}</Alert>}

                        <Form onSubmit={login}>
                            {info.map(i => <Form.Control value={user[i.field]} onChange={e => setState(e.target.value, i.field)} className="mt-3 mb-1" key={i.field} type={i.type} placeholder={i.title} required />)}

                            {loading === true ? <MySpinner /> : <Button type="submit" variant="success" className="mt-3 mb-1">Đăng nhập</Button>}
                        </Form>
                    </div>

                    
                </Col>

                {/* Right Sidebar */}
                <Col md={2} className="bg-light border-start p-3">
                    <Sidebar />
                </Col>
            </Row>
        </Container>
    );
}
