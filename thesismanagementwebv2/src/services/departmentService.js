import { authApis, endpoints } from "../configs/Apis";

export const fetchDepartment = async () => {
    console.log("Gửi request lấy danh sách department:");

    try {
        const res = await authApis().get(endpoints['department']);
        console.log("Phản hồi từ API:", res.data); // Debug
        return res;
    } catch (error) {
        console.error("Lỗi khi gọi API:", error.response?.data || error.message);
        throw error;
    }
};