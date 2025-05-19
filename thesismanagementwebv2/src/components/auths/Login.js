import React, { useContext, useState } from 'react'
import { useNavigate } from 'react-router-dom';
import { MyDispatcherContext } from '../../configs/MyContexts';
import Apis, { authApis, endpoints } from '../../configs/Apis';
import Cookies from 'js-cookie';
import MySpinner from '../layouts/MySpinner'
import { Alert, Button, Col, Container, Form, Row } from 'react-bootstrap';
import Sidebar from '../layouts/Sidebar';
import NavbarVertical from '../layouts/NavbarVertical';

export default function Login() {
    const info = [{
        title: "Tên đăng nhập",
        field: "username",
        type: "text"
    }, {
        title: "Mật khẩu",
        field: "password",
        type: "password"
    }];

    const [user, setUser] = useState({});
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
            let res = await Apis.post(endpoints['login'], {
                ...user
            });

            console.log(user);

            Cookies.save('token', res.data.token);

            let u = await authApis().get(endpoints['current-user']);
            console.info(u.data);

            dispatch({
                "type": "login",
                "payload": u.data
            });
            nav("/");
        } catch (ex) {
            console.error(ex);
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
