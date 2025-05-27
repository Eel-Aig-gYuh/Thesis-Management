import { Outlet } from "react-router-dom";
import NavbarVertical from "./NavbarVertical";
import Sidebar from "./Sidebar";
import { Col, Container, Row } from "react-bootstrap";

const PageLayout = () => {
    return (
        <Container fluid>
            <Row className="vh-100">
                {/* Left Nav */}
                <Col md={2} className="bg-dark text-white p-3 rounded-4">
                    <NavbarVertical />
                </Col>

                {/* Main Content */}
                <Col md={8} className="p-4">
                    <Outlet/>
                </Col>

                {/* Right Sidebar */}
                <Col md={2} className="bg-light border-start p-3 rounded-4">
                    <Sidebar />
                </Col>
            </Row>
        </Container>
    );
};

export default PageLayout;