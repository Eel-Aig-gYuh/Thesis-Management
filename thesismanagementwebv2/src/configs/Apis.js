import axios from "axios";
import Cookies from "js-cookie"; 

const BASE_URL = process.env.REACT_APP_DEFAULT_URL;

export const endpoints = {
    'auth/login': '/auth/login',
    'current-user': '/secure/profile',
    'auth/change-password': (userId) => `/secure/users/${userId}/password`,
    'users': '/secure/users/',
    'users/upload-avatar': '/secure/users/upload-avatar',

    'thesis': '/secure/thesis/',
    'thesis/detail': (thesisId) => `/secure/thesis/${thesisId}`,
    'thesis/un-council': '/secure/thesis/uncouncil/',
    'thesis/un-criteria': '/secure/thesis/un-criteria/',
    'thesis/create': '/secure/thesis/create',
    'thesis/edit': (thesisId) => `/secure/thesis/${thesisId}`,
    'thesis/status': (thesisId) => `/secure/thesis/${thesisId}/status`,
    'thesis/assign-reviewer': (thesisId) => `/secure/thesis/${thesisId}/assign-reviewer`,
    'thesis/my-thesis': '/secure/thesis/my-thesis',
    'thesis/my-thesis/council': '/secure/thesis/my-thesis/council',
    'thesis/upload-file': (thesisId) => `/secure/thesis/${thesisId}/upload-file`,
    'thesis/file': (thesisId) => `/secure/thesis/${thesisId}/file`,
    'thesis/score-detail': (thesisId) => `/secure/thesis/${thesisId}/score-detail`,
    'thesis/update-average-score': (thesisId) => `/secure/thesis/${thesisId}/average-score`,

    'department': '/secure/department/',

    'council': "/secure/council/",
    'council/my-council': "/secure/council/my-council/",
    'council/detail': (councilId) => `/secure/council/${councilId}`,
    'council/create': '/secure/council/create',
    'council/lock': (councilId) => `/secure/council/${councilId}/lock`,

    'criteria/create': '/secure/criteria/create',
    'criteria/score': '/secure/score/prepare',
    'score/submit': (councilId) => `/secure/score/${councilId}/submit-score`,

    'statistic/score': "/secure/score/statistic/scores",
    'statistic/pariticipation': "/secure/score/statistic/participation",
    'statistic/get-pdf': "/secure/score/report/average-scores/",
}

export const authApis = () => {
    return axios.create({
        baseURL: BASE_URL,
        headers: {
            'Authorization': `Bearer ${Cookies.get('token')}`
        }
    })
}

export default axios.create({
    baseURL: BASE_URL
});