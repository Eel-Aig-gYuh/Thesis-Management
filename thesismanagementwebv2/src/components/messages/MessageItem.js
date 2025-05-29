import React from 'react'
import styled from 'styled-components';

const MessageContainer = styled.div`
    height: 100%;
    display: flex;
    justify-content: ${(props) => (props.isMine ? 'flex-end' : 'flex-start')};
    margin-bottom: 10px;
`;

const MessageBubble = styled.div`
    max-width: 60%;
    padding: 10px;
    border-radius: 10px;
    font-weight: bold;
    background-color: ${(props) => (props.isMine ? 'black' : '#F2B705')};
    color: ${(props) => (props.isMine ? 'white' : 'black')};
    border: 1px solid ${(props) => (props.isMine ? "white" : "black")};
`;

export default function MessageItem({ message, currentUser }) {
    console.log(message);
    const isMine = currentUser?.uid === message?.senderId;

    return (
        <MessageContainer isMine={isMine}>
            <MessageBubble isMine={isMine}>
                <div style={{fontSize: "13px"}}>
                    {message?.text}
                </div>
            </MessageBubble>
        </MessageContainer>
  )
}
