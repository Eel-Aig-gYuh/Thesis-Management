import { authApis, endpoints } from "../configs/Apis";

export const createCriteria = async (data) => {
    console.log("Gửi request với params:", data);
    try {
        const response = await authApis().post(endpoints['criteria/create'], data);
        console.log("Phản hồi từ API: ", response.data);

        return response.data;
    } catch (error) {
        console.error("Lỗi khi gọi API:", error.response?.data || error.message);
        throw error;
    }
};