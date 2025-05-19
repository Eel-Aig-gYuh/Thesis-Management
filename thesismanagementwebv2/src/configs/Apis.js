import axios from "axios";
import Cookies from "js-cookie"; 

const BASE_URL = "http://localhost:8080/ThesisManagement/api";

export const endpoints = {
    'login': '/auth/login/',
    'current-user': '/secure/profile/'
}

export const authApis = () => {
    return axios.create({
        baseURL: BASE_URL,
        headers: {
            'Authorization': `Bearer ${Cookies.load('token')}`
        }
    })
}

export default axios.create({
    baseURL: BASE_URL
});