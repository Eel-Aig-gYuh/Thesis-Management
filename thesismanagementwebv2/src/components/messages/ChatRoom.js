import React, { useContext, useEffect, useRef, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { MyUserContext } from '../../configs/MyContexts';
import { getRoomId } from '../utils/FirebaseUtils';
import { addDoc, collection, doc, onSnapshot, orderBy, query, serverTimestamp, setDoc, Timestamp } from 'firebase/firestore';
import { db } from '../../configs/FirebaseConfig';
import { useToast } from '../contexts/ToastProvider';
import { Button, Form, ListGroup } from 'react-bootstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faImage, faPaperPlane } from '@fortawesome/free-solid-svg-icons';
import { useTranslation } from 'react-i18next';
import MessageList from "./MessageList";
import { uploadImageChat } from '../../services/chatService';

const ChatRoom = ({ userId }) => {
    const user = useContext(MyUserContext);
    const [messages, setMessages] = useState([]);
    const [newMessage, setNewMessage] = useState('');

    const toast = useToast();
    const { t } = useTranslation();
    const nav = useNavigate();
    const [isLoading, setIsLoading] = useState(false);
    const scrollViewRef = useRef(null);
    const fileInputRef = useRef(null);

    const roomId = getRoomId(user?.uid, userId);

    const createdRoomIfNotExists = async () => {
        await setDoc(doc(db, 'rooms', roomId), {
            roomId,
            createdAt: Timestamp.fromDate(new Date())
        });
    }

    useEffect(() => {
        createdRoomIfNotExists();

        const q = query(collection(db, 'rooms', roomId, 'messages'), orderBy('timestamp', 'asc'));
        const unsubscribe = onSnapshot(q, (snapshot) => {
            const messageList = snapshot.docs.map((doc) => ({ id: doc.id, ...doc.data() }));
            setMessages(messageList);
        }, (error) => {
            console.log("Error fetching messages: ", error);
        });

        return () => unsubscribe();
    }, [roomId]);

    const handleSendMessage = async (e) => {
        e.preventDefault();
        setIsLoading(true);
        if (!newMessage.trim()) return;

        try {
            await addDoc(collection(db, 'rooms', roomId, 'messages'), {
                text: newMessage,
                senderId: user.uid,
                type: 'text',
                timestamp: serverTimestamp(),
            });

            setNewMessage('');
        } catch (error) {
            console.error("Lỗi gửi tin nhắn");
            toast(t('chat-send-msg-failure'), "danger");
        } finally {
            setIsLoading(false);
        }
    };

    const handleImageUpload = async(e) => {
        const file = e.target.files[0];
        if (!file) return;

        setIsLoading(true);

        try {
            const response = await uploadImageChat(file);

            // lưu ảnh vào firebase
            await addDoc(collection(db, 'rooms', roomId, 'messages'), {
                text: response,
                senderId: user.uid,
                type: 'image',
                timestamp: serverTimestamp(),
            })

            fileInputRef.current.value = null;
        } catch(err) {
            toast(t('upload-img-fail'), "warning");
        } finally { 
            setIsLoading(false);
        }
    }

    return (
        <div>
            <ListGroup style={{ maxHeight: '300px', overflowY: 'auto', marginBottom: '10px', }}>
                <div style={{ height: '300px' }}>
                    <div style={{ flex: 1, height: "250px", flexDirection: 'column-reverse', display: "flex" }}>
                        <div style={{  }}>
                            {messages !== null && (
                                <MessageList messages={messages} currentUser={user} scrollViewRef={scrollViewRef} />
                            )}
                        </div>
                    </div>

                    <div>
                        <Form onSubmit={handleSendMessage}>
                            <Form.Group className='d-flex'>
                                <Form.Control
                                    type='file'
                                    accept='image/*'
                                    onChange={handleImageUpload}
                                    ref={fileInputRef}
                                    style={{display: "none"}}
                                    id='image-upload'
                                />

                                <label htmlFor='image-upload' className='me-2' style={{alignContent: 'center'}}>
                                    <FontAwesomeIcon icon={faImage} className='fs-3 text-center' style={{cursor: "pointer", color: isLoading ? "grey": "black"}}/>
                                </label>
                                
                                <Form.Control
                                    type='text'
                                    value={newMessage}
                                    onChange={(e) => setNewMessage(e.target.value)}
                                    placeholder={t('chat-input-msg')}
                                    disabled={isLoading}
                                />

                                <Button type='submit' className='thesis-btn ms-2' disabled={isLoading} style={{backgroundColor: "black"}}>
                                    <FontAwesomeIcon icon={faPaperPlane} />
                                </Button>
                            </Form.Group>
                        </Form>
                    </div>
                </div>
            </ListGroup>

        </div>
    )
}

export default React.memo(ChatRoom);
