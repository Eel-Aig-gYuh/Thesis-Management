import Apis, { authApis, endpoints } from "../configs/Apis";
import Cookies from "js-cookie";


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
};

export const uploadAvatarFile = async (fileUrl) => {
    const payload = {
        "avatar": fileUrl
    }
    console.log(`Gửi request upload file khóa luận ID: `, payload);

    try {
        const response = await Apis.post(endpoints['users/upload-avatar'], payload,
            {
                headers: {
                    "Content-Type": "multipart/form-data",
                    'Authorization': `Bearer ${Cookies.get('token')}`
                }
            }
        );
        return response.data;
        console.log("Phản hồi từ API: ", response.data);
    } catch (error) {
        console.error("Lỗi khi gọi API:", error.response?.data || error.message);
        throw error;
    }
}