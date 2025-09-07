import Apis, { endpoints } from "../configs/Apis";
import Cookies from "js-cookie";


export const uploadImageChat = async (fileUrl) => {
    const payload = {
        "image": fileUrl
    }
    console.log(`Gửi request upload file khóa luận ID: `, payload);

    try {
        const response = await Apis.post(endpoints['chat/upload-image'], payload,
            {
                headers: {
                    "Content-Type": "multipart/form-data",
                    'Authorization': `Bearer ${Cookies.get('token')}`
                }
            }
        );
        console.log("Phản hồi từ API: ", response.data);
        return response.data;
    } catch (error) {
        console.error("Lỗi khi gọi API:", error.response?.data || error.message);
        throw error;
    }
}