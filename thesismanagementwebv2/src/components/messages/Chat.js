import { useContext, useState } from "react";
import { MyUserContext } from "../../configs/MyContexts";
import { Link, useNavigate } from "react-router-dom";
import "./style.css";
import ChatItem from "./ChatItem";

export default function Chat({ users, currentUser }) {
    const user = useContext(MyUserContext);
    const [isFocused, setIsFocused] = useState(false);
    const nav = useNavigate();
    const [message, setMessage] = useState([]);

    return (
        <div className="chat-container">
            <div className="user-list">
                {users.map((item, index) => (
                    <Link
                        key={item.userId || index}
                        to={`/chat/${item.userId}`}
                        className="chat-link"
                    >
                        <ChatItem item={item} index={index} currentUser={currentUser} />
                    </Link>
                ))}
            </div>
        </div>
    );
}