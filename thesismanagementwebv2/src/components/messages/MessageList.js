import React, { useEffect } from 'react';
import MessageItem from './MessageItem';

export default function MessageList({ messages, currentUser, scrollViewRef }) {
  useEffect(() => {
    // Cuộn xuống cuối mỗi khi messages thay đổi
    if (scrollViewRef?.current) {
      scrollViewRef.current.scrollIntoView({ behavior: 'smooth' });
    }
  }, [messages]);

  return (
    <div className="chat-message-container" style={{ overflowY: 'auto', maxHeight: '80vh', padding: '1rem'}}>
      {messages?.map((message, index) => (
        <MessageItem key={index} message={message} currentUser={currentUser} />
      ))}
      {/* Phần tử để scroll vào cuối */}
      <div ref={scrollViewRef} />
    </div>
  );
}
