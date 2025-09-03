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
import { createUserWithEmailAndPassword, signInWithEmailAndPassword } from 'firebase/auth';
import app, { auth, db } from '../../configs/FirebaseConfig';
import { doc, getDoc, setDoc, updateDoc } from 'firebase/firestore';

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
        setLoading(true);

        try {
            // 1. Đăng nhập hệ thống backend
            const res = await Apis.post(endpoints['auth/login'], user);
            Cookies.set('token', res.data.token);

            // 2. Lấy thông tin người dùng từ hệ thống backend
            const u = await authApis().get(endpoints['current-user']);

            // 3. Firebase Auth: Đăng nhập hoặc tạo tài khoản
            let firebaseUser;
            try {
                firebaseUser = await signInWithEmailAndPassword(auth, u.data.email, user.password);
            } catch (err) {
                if (['auth/user-not-found'].includes(err.code)) {
                    console.log("Tạo người dùng mới trên Firebase");
                    firebaseUser = await createUserWithEmailAndPassword(auth, u.data.email, u.data.password);
                } else {
                    throw err;
                }
            }

            console.log("Firebase login thành công:", firebaseUser.user.uid);

            // 4. Lưu token Firebase
            const firebaseToken = await firebaseUser.user.getIdToken();
            Cookies.set('firebase_token', firebaseToken, { expires: 1 });

            console.log(firebaseUser.user.uid);
            // 5. Đồng bộ Firestore
            const userRef = doc(db, 'users', firebaseUser.user.uid);
            const lastFirestoreSyncKey = `lastFirestoreSync_${firebaseUser.user.uid}`;
            const lastSyncTimestamp = localStorage.getItem(lastFirestoreSyncKey);
            const now = Date.now();
            const syncInterval = 24 * 60 * 60 * 1000; // Đồng bộ tối đa mỗi 24 giờ (hoặc tùy bạn)

            // Chỉ đồng bộ Firestore nếu chưa bao giờ đồng bộ HOẶC đã quá khoảng thời gian cho phép
            if (!lastSyncTimestamp || (now - parseInt(lastSyncTimestamp, 10) > syncInterval)) {
                console.log("Thực hiện đồng bộ Firestore...");
                const userSnap = await getDoc(userRef);

                if (!userSnap.exists()) {
                    console.log("Tạo mới user Firestore");
                    await setDoc(userRef, {
                        userId: firebaseUser.user.uid,
                        firstname: u.data.firstname || 'User',
                        lastname: u.data.lastname || '',
                        role: u.data.role,
                        avatar: { uri: u.data.avatar || 'https://res.cloudinary.com/dnqt29l2e/image/upload/v1747068051/c47vxjryuhnfz2ljk3dn.jpg' },
                        email: u.data.email || user.username,
                    });
                } else {
                    console.log("User đã tồn tại trong Firestore (không ghi mới)");
                    // Tùy chọn: Nếu muốn cập nhật thông tin user mỗi 24h, 
                    // bạn có thể dùng updateDoc ở đây thay vì chỉ setDoc cho user mới
                    await updateDoc(userRef, { 
                        firstname: u.data.firstname || 'User',
                        lastname: u.data.lastname || '',
                        avatar: { uri: u.data.avatar || 'https://res.cloudinary.com/dnqt29l2e/image/upload/v1747068051/c47vxjryuhnfz2ljk3dn.jpg' },
                    });
                }
                localStorage.setItem(lastFirestoreSyncKey, now.toString()); // Cập nhật thời gian sync
            } else {
                console.log("Bỏ qua đồng bộ Firestore: Đã đồng bộ gần đây.");
            }

            // 6. Lưu redux state
            dispatch({
                type: "login",
                payload: { ...u.data, uid: firebaseUser.user.uid }
            });

            // 7. Điều hướng và hiển thị toast
            toast(t("noti-login-success"), "success");
            const isDefaultPw = user.password === process.env.REACT_APP_DEFAULT_PASSWORD;
            const isNotAdmin = u.data.role !== "ROLE_ADMIN";

            console.log("Mật khẩu mặc định", process.env.REACT_APP_DEFAULT_PASSWORD);
            console.log("Mật khẩu trong form", user.password);

            nav(isDefaultPw && isNotAdmin ? "/auth/change-password" : "/");
        } catch (err) {
            console.error("Lỗi khi đăng nhập:", err);
            toast(t("noti-login-failure"), "danger");
        } finally {
            setLoading(false);
        }
    };

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
