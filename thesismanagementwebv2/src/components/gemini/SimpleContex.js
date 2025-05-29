import { GoogleGenAI } from '@google/genai';
import React, { useEffect, useRef, useState } from 'react'
import { useTranslation } from 'react-i18next'
import ai from '../../configs/GeminiConfig';
import { useToast } from '../contexts/ToastProvider';
import { addDoc, collection, onSnapshot, orderBy, query } from 'firebase/firestore';
import { gedb } from '../../configs/GeminiFirebaseConfig';
import "./style.css";
import { Button, Col, Form, Row } from 'react-bootstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faBars, faPaperPlane } from '@fortawesome/free-solid-svg-icons';
import moment from 'moment';
import 'moment/locale/vi';

export default function SimpleContex() {
    const { t } = useTranslation();
    const [prompt, setPrompt] = useState('');
    const toast = useToast();
    const [response, setResponse] = useState('');
    const [loading, setLoading] = useState(false);

    const [messages, setMessages] = useState([]);
    const [chatHistory, setChatHistory] = useState([]);
    const [isSidebarOpen, setIsSidebarOpen] = useState(false);
    const chatEndRef = useRef(null);

    useEffect(() => {
        chatEndRef?.current?.scrollIntoView({ behavior: 'smooth' });
    }, [messages]);

    // Lấy lịch sử chat từ Firestore
    useEffect(() => {
        const q = query(collection(gedb, 'trainingData'), orderBy('timestamp', 'desc'));
        const unsubcribe = onSnapshot(q, (snapshot) => {
            const history = snapshot.docs.map(doc => ({ id: doc.id, ...doc.data() }));
            setChatHistory(history);
        });
        return () => unsubcribe();
    }, []);


    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!prompt.trim()) return;

        setLoading(true);

        const newPrompt = { type: 'prompt', content: prompt, timestamp: new Date() };
        setMessages((prev) => [...prev, newPrompt]);

        try {
            // Khởi tạo Gemini API
            const response = await ai.models.generateContent({
                model: "gemini-2.0-flash",
                contents: prompt,
            });

            const generatedText = response.text || t('gemini-dont-know');

            const newResponse = { type: 'response', content: generatedText, timestamp: new Date() };
            setMessages((prev) => [...prev, newResponse]);

            // Lưu vào firestore;
            await addDoc(collection(gedb, 'trainingData'), {
                prompt: prompt,
                response: generatedText,
                timestamp: new Date(),
            });

            setPrompt('');
            console.log("Đã lưu dữ liệu vào firestore");

        } catch (error) {
            console.log("Lỗi gemini: ", error);
            toast(t('gemini-out-firebase'), "warning");
            const errorMsg = { type: 'response', content: t('gemini-out-firebase'), timestamp: new Date() };
            setMessages((prev) => [...prev, errorMsg]);
        } finally {
            setLoading(false);
        }
    };

    const toggleSidebar = () => {
        setIsSidebarOpen(!isSidebarOpen);
    };

    return (
        <div className='bg-dark gemini-container p-4' style={{ height: "100%", color: "white" }}>
            <Row>
                {console.log(messages)}
                <Col md={10}>
                    <div className='mb-2 fw-semibold'>
                        {t('gemini-title')}
                    </div>
                    <div style={{ fontSize: "13px", fontWeight: "semibold", fontStyle: "italic" }}>
                        {t('gemini-model')}
                    </div>
                </Col>

                <Col md={2} className='text-end'>
                    <Button className='thesis-btn' onClick={toggleSidebar}>
                        <FontAwesomeIcon icon={faBars} />
                    </Button>
                </Col>
            </Row>
            <div className='container-fluid mt-4 d-flex flex-column' style={{ flex: 1, height: '85%' }}>
                <div
                    style={{ backgroundColor: "black" }}
                    className={`col-md-3 gemini-sidebar ${isSidebarOpen ? 'open' : 'closed'}`}>
                    <div className='p-3'>
                        <div className=' fw-bold justify-content-between align-items-center mb-3'>
                            <div className='border-bottom rounded-3 p-2'>
                                <h5 style={{ fontSize: "20px", fontWeight: "bold" }}>{t('gemini-history')}</h5>
                            </div>

                            <div className='gemini-chat-history'>
                                {chatHistory?.map((chat) => (
                                    <div key={chat.id} className='border-bottom py-2'>
                                        <p style={{ fontSize: '14px', }}>
                                            <strong>{t('question')}: </strong> <p style={{ color: "grey" }}>{chat.prompt.substring(0, 50)} ...</p>
                                        </p>
                                        <p style={{ fontSize: '14px', }}>
                                            <small><strong>{t('response')}: </strong> <p style={{ color: "grey" }}>{chat.response.substring(0, 50)} ... </p></small>
                                        </p>
                                        <small style={{ fontSize: '10px', fontStyle: 'italic' }}>
                                            <strong>{new Date(chat.timestamp.seconds * 1000).toLocaleString()}</strong>
                                        </small>
                                    </div>
                                ))}
                            </div>
                        </div>
                    </div>
                </div>

                <div className={`col-md-${isSidebarOpen ? '9' : '12'} d-flex flex-column`} style={{ minHeight: '100%' }}>
                    <div className='p-3 flex-grow-1 overflow-auto gemini-chat-area'>
                        {messages.map((msg, index) => (
                            <div key={index}
                                className={`mb-3 ${msg.type === 'prompt' ? 'text-end' : 'text-start'}`}>
                                <div className={`card d-inline-block ${msg.type === 'prompt' ? 'bg-primary text-white' : 'bg-light'} mx-2`}
                                    style={{ maxWidth: '70%' }}
                                >
                                    <div className='card-body'>
                                        <p className='card-text'>{msg.content}</p>
                                        <small style={{ fontSize: '10px', fontStyle: 'italic' }}>
                                            <strong>{msg?.timestamp?.toLocaleTimeString()}</strong>
                                        </small>
                                    </div>
                                </div>
                            </div>
                        ))}

                        <div ref={chatEndRef} />
                    </div>


                    <div className='mt-auto p-3 border-top justify-content-between align-items-center'>
                        <Form onSubmit={handleSubmit}>
                            <div className='input-group'>
                                <textarea
                                    className='form-control'
                                    value={prompt}
                                    onChange={(e) => setPrompt(e.target.value)}
                                    placeholder={t('gemeni-request')}
                                    rows={2}
                                />

                                <Button className='thesis-btn me-2 p-2 mt-2'
                                    style={{ marginLeft: "10px" }}
                                    type='submit' disabled={loading}>
                                    {loading ? "..." : <FontAwesomeIcon icon={faPaperPlane} />}
                                </Button>
                            </div>
                        </Form>
                    </div>
                </div>
            </div>
        </div>
    )
}
