import { authApis, endpoints } from "../configs/Apis";


export const fetchUsers = async (page = 1, { role, firstname = "" }) => {
    const params = {
        page,
        role: role || undefined,
        firstname: firstname || undefined,
    };
    console.log("Gửi params:", params); // Debug

    try {
        const res = await authApis().get(endpoints['users'], { params });
        console.log("Phản hồi từ API:", res.data); // Debug

        return {
            data: res.data.users,
            totalPages: res.data.totalPages,
        };
    } catch (error) {
        console.error("Lỗi khi gọi API:", error.response?.data || error.message);
        throw error;
    }
}