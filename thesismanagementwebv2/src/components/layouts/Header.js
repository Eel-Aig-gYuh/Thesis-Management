import React, { useContext } from 'react'
import { Button, Nav, Navbar, ToastContainer } from 'react-bootstrap'
import "./style.css";
import '../../i18n/index';
import { useTranslation } from 'react-i18next';
import { Link } from 'react-router-dom';
import { MyDispatcherContext, MyUserContext } from '../../configs/MyContexts';


export default function Header() {
  const user = useContext(MyUserContext);
  const dispatch = useContext(MyDispatcherContext);

  const { t, i18n } = useTranslation();

  return (
    <div className="curved-header">
      <Navbar className="curved-navbar" expand="lg" style={{ padding: "1rem 2rem" }}>


        <div className="curved-navbar container d-flex justify-content-between align-items-center w-100">
          <Link to="/" className="btn text-white fw-bold mx-3 fs-3">Ghee's Thesis</Link>

          <Navbar.Toggle aria-controls="basic-navbar-nav" />
          <Navbar.Collapse id="basic-navbar-nav" className="justify-content-center">
            <Nav className="mx-auto">
              
              
            </Nav>
          </Navbar.Collapse>

          <Button variant="btn btn-secondary" className='me-2' onClick={() => i18n.changeLanguage(i18n.language === 'en' ? 'vi' : 'en')}>
            🌐 {i18n.language === 'en' ? 'EN' : 'VI'}
          </Button>

          {user === null ? <>
            <Link to="/auth/login" className="btn btn-light fw-semibold rounded">{t('login')}</Link>
          </> : <>
            <Link to="/" className="nav-link text-white fw-bold mx-3">
              <img src={user.avatar} width="40" className="rounded-circle" />
                {user.username}!
            </Link>
          </>}
        </div>
      </Navbar>
    </div>
  )
}
