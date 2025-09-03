import { signOut } from "firebase/auth";
import Cookies from "js-cookie"; 
import { auth } from "../configs/FirebaseConfig";

export default (current, action) => {
    switch (action.type) {
        case "login":
            return action.payload;
        case "logout":
            Cookies.remove('token');
            Cookies.remove('firebase_token');
            return null;
        case "updateUser": 
            return {
                ...current,
                ...action.payload, // gộp thông tin mới vào user hiện tại
            };
    }
    return current;
}