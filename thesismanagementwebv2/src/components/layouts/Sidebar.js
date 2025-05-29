import React, { useContext, useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next'
import { MyDispatcherContext, MyUserContext } from '../../configs/MyContexts';
import { collection, onSnapshot, query } from 'firebase/firestore';
import { db } from '../../configs/FirebaseConfig';
import { Button, Card, Image, ListGroup, Modal } from 'react-bootstrap';
import { Link, useNavigate } from 'react-router-dom';
import ChatRoom from '../messages/ChatRoom';
import "./style.css";
import FloatingChat from '../messages/FloatingChat';
import { useToast } from '../contexts/ToastProvider';
import ChatItem from '../messages/ChatItem';

export default function Sidebar() {
  const { t } = useTranslation();
  const toast = useToast();
  const user = useContext(MyUserContext);
  const dispatch = useContext(MyDispatcherContext);
  const [users, setUsers] = useState([]);
  const [activeChats, setActiveChats] = useState([]);
  const nav = useNavigate();


  // fetch du lieu
  useEffect(() => {
    if (!user) return;

    const q = query(collection(db, 'users'));
    const unsubcribe = onSnapshot(q, (snapshot) => {
      const userList = snapshot.docs
        .map((doc) => ({ id: doc.id, ...doc.data() }))
        .filter((u) => u.userId !== user.uid);
      setUsers(userList);
      console.log('Users fetch', userList);
    }, (error) => {
      console.log('Users fetch failed', error);
    });
    return () => unsubcribe();
  }, [user]);

  // chọn người dùng để chat.
  const handleUserClick = (selectedUser) => {
    if (activeChats.find((chat) => chat.userId === selectedUser.userId)) {
      console.log("Trùng id");
      return;
    }

    if (activeChats.length > 2) {
      toast(t('max-chats'), "warning");
      return;
    }

    setActiveChats([...activeChats, selectedUser]);
  }

  const handleCloseChat = (userId) => {
    setActiveChats(activeChats.filter((chat) => chat.userId !== userId));
  }

  if (!user) {
    return <p>{t('login-required')}</p>
  }

  return (
    <div style={{ minHeight: "700px" }} className='sidebar'>

      {console.log("Active chat: ", activeChats)}
      <h5 className='sidebar-header text-center mb-3'>{t('sidebar-users')}</h5>

      <div style={{}}>
        {users.length === 0 ? (
          <p className='text-muted'>{t('sidebar-nousers')}</p>
        ) : (
          <ListGroup style={{ minHeight: "450px" }} >
            {users.map((u) => (
              <>
                <div style={{ borderRadius: "20px" }}>
                  <ListGroup.Item
                    key={u.userId}
                    action
                    onClick={() => handleUserClick(u)}
                    style={{ minHeight: "60px" }}
                    className='p-2 mt-2 chat-list-container'
                    active={activeChats.some((chat) => chat.userId === u.userId)}
                  >
                    <ChatItem currentUser={user} item={u} />
                  </ListGroup.Item>
                </div>
              </>
            ))}
          </ListGroup>
        )}
      </div>

      <Link className='p-2 mt-2 chat-list-container fw-semibold'
        style={{textDecoration: "none", color: "white", marginLeft: "10px"}}
        to={"/gemini"}
      >
        Gemini
      </Link>

      {activeChats.map((chatUser, index) => (
        <FloatingChat
          key={chatUser.userId}
          user={chatUser}
          onClose={() => handleCloseChat(chatUser.userId)}
          index={index}
        />
      ))}
    </div>
  );
}
