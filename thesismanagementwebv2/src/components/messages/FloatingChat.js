import React, { useRef, useState } from 'react'
import { Button, Card, Col, Row } from 'react-bootstrap';
import { useTranslation } from 'react-i18next'
import ChatRoom from './ChatRoom';

export default function FloatingChat({ user, onClose, index }) {
    const { t } = useTranslation();
    const [isCollapsed, setIsCollapsed] = useState(false);

    const xPosition = -280 - index * 310;

    return (
        <Card
            style={{
                position: 'fixed',
                bottom: '20px',
                right: '20px',
                width: '300px',
                maxHeight: isCollapsed ? '56px' : '400px',
                zIndex: 3000 + index,
                boxShadow: '0 4px 8px rgba(0,0,0,0.2)',
                border: "3px solid black",
                borderRadius: "20px",
            }}
        >
            <Card.Header
                className='d-flex justify-content-between align-items-center'
                style={{ padding: '8px' }}
            >
                <Row className='justify-content-between align-items-center'>                    
                    <Col>
                        <Card.Title className='mb-0'>
                            <div style={{ fontSize: "18px" }}>
                               {user.lastname} {user.firstname}
                            </div>
                        </Card.Title>
                    </Col>
                    <Col md={2}>
                        <Button
                            className='thesis-btn me-2' variant='warning'
                            style={{width: "40px"}}
                            onClick={() => setIsCollapsed(!isCollapsed)}>
                            {isCollapsed ? '+' : '-'}
                        </Button>
                    </Col>

                    <Col md={2} style={{marginRight: "10px"}}>
                        <Button className='thesis-btn' variant='dark' 
                            style={{width: "40px"}}
                            onClick={onClose}
                        >
                            x
                        </Button>
                    </Col>
                </Row>
            </Card.Header>

            <Card.Body style={{ overflowY: 'auto', height: 'calc(100% - 56px)', minHeight: "400px" }}>
                <ChatRoom userId={user.userId} />
            </Card.Body>

        </Card>
    )
}
