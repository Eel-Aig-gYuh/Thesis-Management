import React, { useContext } from 'react'
import { Button, Dropdown, Nav, Navbar } from 'react-bootstrap'
import "./style.css";
import '../../i18n/index';
import { useTranslation } from 'react-i18next';
import { Link, useNavigate } from 'react-router-dom';
import { MyDispatcherContext, MyUserContext } from '../../configs/MyContexts';
import { signOut } from 'firebase/auth';
import { auth } from '../../configs/FirebaseConfig';


export default function Header() {
  const user = useContext(MyUserContext);
  const nav = useNavigate();
  const dispatch = useContext(MyDispatcherContext);

  const { t, i18n } = useTranslation();

  const handleLogout = async() => {
    dispatch({ type: 'logout' });
    await signOut(auth);
    nav('/');
  }

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

          <Button className='me-3 stats-btn' style={{ paddingLeft: "30px", paddingRight: "30px", }} onClick={() => i18n.changeLanguage(i18n.language === 'en' ? 'vi' : 'en')}>
            🌐 {i18n.language === 'en' ? 'EN' : 'VI'}
          </Button>

          {user === null ? <>
            <Link to="/auth/login" className="me-2 btn stats-btn">{t('login')}</Link>
          </> : <>
            <Dropdown align="end">
              <Dropdown.Toggle variant="link" className="nav-link text-white fw-bold mx-3 p-0 d-flex align-items-center" id="dropdown-user">
                <img src={user?.avatar || 'https://res.cloudinary.com/dnqt29l2e/image/upload/v1747068051/c47vxjryuhnfz2ljk3dn.jpg'} width="40" height="40" className="rounded-circle me-2" />

                <div style={{marginLeft: "20px"}}>
                  {user?.username}
                </div>
              </Dropdown.Toggle>

              <Dropdown.Menu>
                <Dropdown.Item as={Link} to="/profile">{t('profile')}</Dropdown.Item>
                <Dropdown.Item onClick={handleLogout}>{t('logout')}</Dropdown.Item>
              </Dropdown.Menu>
            </Dropdown>
          </>}
        </div>
      </Navbar>
    </div>
  )
}
