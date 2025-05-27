import { authApis, endpoints } from "../configs/Apis";

export const submitCouncilScores = async (councilId, payload) => {
    try {
        const response = await authApis().post(endpoints['score/submit'](councilId), payload);
        return response.data;
    } catch (error) {
        throw error.response?.data || error.message;
    }
};


export const getGradingCriteria = async (payload) => {
    console.log("Gửi request để lấy tiêu chí chấm điểm", payload)
    try {
        const response = await authApis().post(endpoints['criteria/score'], payload);
        console.log("Phản hồi từ api", response.data);
        return response.data;
    } catch (error) {
        throw error.response?.data || error.message;
    }
};
