import React from 'react'
import { Alert, Button, Col, Container, Row } from 'react-bootstrap'
import NavbarVertical from './layouts/NavbarVertical'
import { Form } from 'react-router-dom'
import MySpinner from './layouts/MySpinner'
import Sidebar from './layouts/Sidebar'

export default function Home() {
  return (
    <Container fluid>
      <Row className="vh-100">
        {/* Left Nav */}
        <Col md={2} className="bg-dark text-white p-3 rounded-4">
          <NavbarVertical />
        </Col>

        {/* Main Content */}
        <Col md={8} className="p-4">
          <div>
            <h1 className="text-center text-success mt-1">TRANG CHỦ</h1>

            
          </div>
        </Col>

        {/* Right Sidebar */}
        <Col md={2} className="bg-light border-start p-3 rounded-4">
          <Sidebar />
        </Col>
      </Row>
    </Container>
  )
}
