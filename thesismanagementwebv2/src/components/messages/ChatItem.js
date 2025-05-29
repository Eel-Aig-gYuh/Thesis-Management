import { collection, doc, onSnapshot, orderBy, query } from 'firebase/firestore';
import React, { useEffect, useState } from 'react'
import { db } from '../../configs/FirebaseConfig';
import { getRoomId } from '../utils/FirebaseUtils';
import { useTranslation } from 'react-i18next';
import moment from 'moment';
import 'moment/locale/vi';
import { Col, Image, Row } from 'react-bootstrap';

export default function ChatItem({ item, currentUser }) {
    const { t } = useTranslation();
    const [lastMessage, setLastMessage] = useState(null);

    useEffect(() => {
        const roomId = getRoomId(currentUser?.uid, item?.userId);
        const docRef = doc(db, 'rooms', roomId);
        const messageRef = collection(docRef, 'messages');
        const q = query(messageRef, orderBy('createdAt', 'desc'));

        const unsub = onSnapshot(q, (snapshot) => {
            const messages = snapshot.docs.map((doc) => doc.data());
            setLastMessage(messages[0] || null);
        });

        return () => unsub();
    }, [currentUser, item]);

    const renderLastMessage = () => {
        if (!lastMessage) return t('say-hi');
        if (currentUser?.uid === lastMessage?.userId) {
            return `${t('you')}: ${lastMessage?.text}`;
        }
        return lastMessage?.text;
    }

    const renderTime = () => {
        if (!lastMessage) return '';
        const timestamp =
            lastMessage.createdAt.seconds * 1000 +
            lastMessage.createdAt.nanoseconds / 1000000;
        return moment(timestamp).fromNow();
    };


    return (
        <div className='chat-item-container'>
            <Row>
                <Col md={2}>
                    <Image className='chat-avatar'
                        src={item?.avatar?.uri || 'https://res.cloudinary.com/dnqt29l2e/image/upload/v1747068051/c47vxjryuhnfz2ljk3dn.jpg'}
                        alt="avatar"
                        width={40}
                        height={40}
                        roundedCircle
                    />
                </Col>

                <Col style={{marginLeft: "15px"}}>
                    <div className='chat-user-info'>
                        <div className='chat-username fw-bold' style={{color: "black"}}>
                            {`${item?.lastname} ${item?.firstname}`}
                        </div>

                        <div className='chat-lastmessages fw-semibold' style={{fontSize: "12px", color: "grey"}}>
                            {renderLastMessage()}
                        </div>
                    </div>

                    <div className='chat-time'>
                        <span style={{fontSize: "12px"}}>
                            {renderTime()}
                        </span>
                    </div>
                </Col>
            </Row>



        </div>
    )
}
