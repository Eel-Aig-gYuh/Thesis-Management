import React, { useContext } from 'react'
import { MyDispatcherContext, MyUserContext } from '../../configs/MyContexts';
import { Link, useNavigate } from 'react-router-dom';
import "./style.css";
import "../../i18n/index";
import { useTranslation } from 'react-i18next';
import { Col, Row } from 'react-bootstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faAddressCard, faBarcode, faBook, faPenToSquare } from "@fortawesome/free-solid-svg-icons";
import { FontWeight } from '@cloudinary/url-gen/qualifiers';
import { signOut } from 'firebase/auth';
import { auth } from '../../configs/FirebaseConfig';

export default function NavbarVertical() {
  const user = useContext(MyUserContext);
  const dispatch = useContext(MyDispatcherContext);
  const nav = useNavigate();

  const { t } = useTranslation();

  const handleLogout = async() => {
    dispatch({ type: 'logout' });
    await signOut(auth);
    nav('/');
  }

  const role = user?.role || null;

  const renderMenuByRole = () => {
    if (!role) return null;

    switch (role) {
      case 'ROLE_ADMIN':
        return (
          <div className="sidebar-div">
            <SidebarItem className='sidebar-item' label="Quản lý người dùng" path="/admin/users" />
            <SidebarItem className='sidebar-item' label="Quản lý hệ thống" path="/admin/settings" />
          </div>
        );
      case 'ROLE_GIAOVU':
        return (
          <div className='sidebar-div'>
            <Sidebar label={t('feat-thesis')} />

            <div className="sidebar-submenu ms-3">
              <Row>
                <Col md={2}>
                  <FontAwesomeIcon icon={faBook} style={{ color: "#ffffff", fontSize: "20px", display: "block", paddingTop: "12px" }} />
                </Col>
                <Col md={10}>
                  <SidebarItem label={t('feat-thesis-view')} path="/thesis/" />
                </Col>
              </Row>

              <Row>
                <Col md={2}>
                  <FontAwesomeIcon icon={faPenToSquare} style={{ color: "#ffffff", fontSize: "20px", display: "block", paddingTop: "12px" }} />
                </Col>
                <Col md={10}>
                  <SidebarItem label={t('feat-thesis-create')} path="/thesis/create/" />
                </Col>
              </Row>
            </div>

            <Sidebar className='sidebar-item' label={t('feat-council')} />

            <div className="sidebar-submenu ms-3">
              <Row>
                <Col md={2}>
                  <FontAwesomeIcon icon={faBarcode} style={{ color: "#ffffff", fontSize: "20px", display: "block", paddingTop: "12px" }} />
                </Col>
                <Col md={10}>
                  <SidebarItem label={t('feat-council-view')} path="/council/" />
                </Col>
              </Row>

              <Row>
                <Col md={2}>
                  <FontAwesomeIcon icon={faPenToSquare} style={{ color: "#ffffff", fontSize: "20px", display: "block", paddingTop: "12px" }} />
                </Col>
                <Col md={10}>
                  <SidebarItem label={t('feat-council-create')} path="/council/create/" />
                </Col>
              </Row>
            </div>


            <Sidebar className='sidebar-item' label={t('feat-criteria')} />

            <div className="sidebar-submenu ms-3">
              <Row>
                <Col md={2}>
                  <FontAwesomeIcon icon={faPenToSquare} style={{ color: "#ffffff", fontSize: "20px", display: "block", paddingTop: "12px" }} />
                </Col>
                <Col md={10}>
                  <SidebarItem label={t("create-criteria")} path="criteria/create" />
                </Col>
              </Row>
            </div>


            <Sidebar className='sidebar-item' label={t('feat-statistic')} path="/statistics" />

          </div>
        );
      case 'ROLE_GIANGVIEN':
        return (
          <div className='sidebar-div'>

            <Sidebar className='sidebar-item' label={t("feat-thesis-advisor")} path="/thesis/" />
            <Sidebar className='sidebar-item' label={t("feat-thesis-review")} path="/council/my-council/" />
          </div>
        );
      case 'ROLE_SINHVIEN':
        return (
          <div className='sidebar-div'>
            <Sidebar className='sidebar-item' label={t('feat-my-thesis')} path="/thesis/" />
            
            <div className="sidebar-submenu ms-3">
              <Row>
                <Col md={2}>
                  <FontAwesomeIcon icon={faBook} style={{ color: "#ffffff", fontSize: "20px", display: "block", paddingTop: "12px" }} />
                </Col>
                <Col md={10}>
                  <SidebarItem label={t('feat-thesis-view')} path="/thesis/" />
                </Col>
              </Row>
            </div>
          </div>
        );
      default:
        return null;
    }
  }

  return (
    <div className="sidebar">
      <div className="sidebar-header">
        <h3>Thesis Web</h3>
      </div>

      <div className="sidebar-menu">
        {renderMenuByRole()}
      </div>

      {user && (
        <div className="sidebar-footer">
          <div className='sidebar-div'>
            <Row>
              <Col md={2}>
                <FontAwesomeIcon icon={faAddressCard} style={{ color: 'white', fontSize: "30px", display: "block", paddingTop: "5px" }} />
              </Col>
              <Col md={10}>
                <Link to="/auth/change-password" className='sidebar-item d-block mb-2 text-white text-decoration-none'> {t("change-password")} </Link>
              </Col>
            </Row>
          </div>
          <button onClick={handleLogout} className="logout-button">
            {t("logout")}
          </button>
        </div>
      )}
    </div>
  );
}

function Sidebar({ label, path }) {
  return (
    <Link className='sidebar-item d-block mt-2 mb-2 text-white text-decoration-none' style={{ fontWeight: "bold", borderBottom: "2px solid white", }} to={path}>{label}</Link>
  );
}

function SidebarItem({ label, path }) {

  return (
    <Link className='sidebar-item d-block mt-2 mb-2 text-white text-decoration-none' to={path}>{label}</Link>
  );

}