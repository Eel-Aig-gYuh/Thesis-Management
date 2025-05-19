import Cookies from "js-cookie"; 

export default (current, action) => {
    switch (action.type) {
        case "login":
            return action.payload;
        case "logout":
            Cookies.remove('token');
            return null;
    }
    return current;
}