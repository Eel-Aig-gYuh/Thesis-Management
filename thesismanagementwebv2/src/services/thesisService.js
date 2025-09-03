import Apis, { authApis, endpoints } from "../configs/Apis";
import Cookies from "js-cookie";

export const fetchTheses = async (page = 1, filters = {}) => {
    const params = { page, ...filters };
    console.log("Gửi request với params:", params);

    try {
        const res = await authApis().get(endpoints['thesis'], { params });
        console.log("Phản hồi từ API:", res.data); // Debug
        return {
            data: res.data.theses,
            totalPages: res.data.totalPages,
        };
    } catch (error) {
        console.error("Lỗi khi gọi API:", error.response?.data || error.message);
        throw error;
    }
};

export const fetchFileByThesisId = async(thesisId) => {
    console.log("Gửi request lấy file thesis: ");

    try {
        const res = await authApis().get(endpoints['thesis/file'](thesisId));
        console.log("Phản hồi từ API:", res.data);
        return {
            data: res.data.files,
            thesisId: res.data.thesisId
        };
    } catch (error) {
        console.error("Lỗi khi gọi API:", error.response?.data || error.message);
        throw error;
    }
}

export const fetchScoreDetailByThesisId = async(thesisId) => {
    console.log("Gửi request lấy file thesis score detail: ");

    try {
        const res = await authApis().get(endpoints['thesis/score-detail'](thesisId));
        console.log("Phản hồi từ API:", res.data);
        return res;
    } catch (error) {
        console.error("Lỗi khi gọi API:", error.response?.data || error.message);
        throw error;
    }
}

export const fetchMyTheses = async (page = 1, filters = {}) => {
    const params = { page, ...filters };
    console.log("Gửi request với params:", params);

    try {
        const res = await authApis().get(endpoints['thesis/my-thesis'], { params });
        console.log("Phản hồi từ API:", res.data); // Debug
        return {
            data: res.data.theses,
            totalPages: res.data.totalPages,
        };
    } catch (error) {
        console.error("Lỗi khi gọi API:", error.response?.data || error.message);
        throw error;
    }
};

export const fetchMyThesesInCouncil = async (page = 1, filters = {}) => {
    const params = { page, ...filters };
    console.log("Gửi request với params in council:", params);

    try {
        const res = await authApis().get(endpoints['thesis/my-thesis/council'], { params });
        console.log("Phản hồi từ API:", res.data); // Debug
        return {
            data: res.data.theses,
            totalPages: res.data.totalPages,
        };
    } catch (error) {
        console.error("Lỗi khi gọi API:", error.response?.data || error.message);
        throw error;
    }
};

export const fetchThesisById = async (thesisId) => {
    console.log("Gửi request với params:", thesisId);

    try {
        const res = await authApis().get(endpoints['thesis/detail'](thesisId));
        console.log("Phản hồi từ API:", res.data);
        return res.data;
    } catch (error) {
        console.error("Lỗi khi gọi API:", error.response?.data || error.message);
        throw error;
    }
}

export const fetchAvailableTheses = async (page = 1, filters = {}) => {
    const params = { page, ...filters };
    console.log("Gửi request với params:", params);

    try {
        const res = await authApis().get(endpoints['thesis/un-council'], { params });
        console.log("Phản hồi từ API:", res.data); // Debug
        return {
            data: res.data.theses,
            totalPages: res.data.totalPages,
        };
    } catch (error) {
        console.error("Lỗi khi gọi API:", error.response?.data || error.message);
        throw error;
    }
};

export const fetchAvailableThesesCriteria = async (page = 1, filters = {}) => {
    const params = { page, ...filters };
    console.log("Gửi request với params:", params);

    try {
        const res = await authApis().get(endpoints['thesis/un-criteria'], { params });
        console.log("Phản hồi từ API:", res.data); // Debug
        return {
            data: res.data.theses,
            totalPages: res.data.totalPages,
        };
    } catch (error) {
        console.error("Lỗi khi gọi API:", error.response?.data || error.message);
        throw error;
    }
};

export const createThesis = async (data) => {
    console.log("Gửi request với params:", data);
    try {
        const response = await authApis().post(endpoints['thesis/create'], data);
        console.log("Phản hồi từ API: ", response.data);

        return response.data;
    } catch (error) {
        console.error("Lỗi khi gọi API:", error.response?.data || error.message);
        throw error;
    }
};

export const assignReviewer = async (thesisId, dto) => {
    console.log("Gửi request: ", dto);

    try {
        const res = await authApis().post(endpoints['thesis/assign-reviewer'](thesisId), dto);
        console.log("Phản hồi từ API: ", res.data);
    } catch (error) {
        console.error("Lỗi khi gọi API:", error.response?.data || error.message);
        throw error;
    }
};

export const updateThesisStatus = async (thesisId, status) => {
    console.log(`Gửi request cập nhật trạng thái luận văn ID: ${thesisId}, status: ${status}`);
    try {
        const res = await authApis().patch(endpoints['thesis/status'](thesisId), status);
        console.log("Phản hồi từ API: ", res.data);
    } catch (error) {
        console.error("Lỗi khi gọi API:", error.response?.data || error.message);
        throw error;
    }
};

export const updateThesis = async (thesisId, data) => {
    console.log(`Gửi request cập nhật khóa luận ID: ${thesisId}`, data);
    try {
        const response = await authApis().put(endpoints['thesis/edit'](thesisId), data);
        console.log("Phản hồi từ API: ", response.data);
    } catch (error) {
        console.error("Lỗi khi gọi API:", error.response?.data || error.message);
        throw error;
    }
};

export const calculateAverageScore = async (thesisId) => {
    console.log(`Gửi request để lấy điểm trung bình của khóa luận ID: ${thesisId}`);

    try {
        const response = await authApis().post(endpoints['thesis/update-average-score'](thesisId));
        console.log("Phản hồi từ API ", response.data);
    } catch (error) {
        console.error("Lỗi khi gọi API:", error.response?.data || error.message);
        throw error;
    }
}

export const uploadThesisFile = async (thesisId, fileUrl) => {
    const payload = {
        "thesisId": thesisId,
        "file": fileUrl
    }
    console.log(`Gửi request upload file khóa luận ID: ${thesisId}`, payload);

    try {
        const response = await Apis.post(endpoints['thesis/upload-file'](thesisId), payload,
            {
                headers: {
                    "Content-Type": "multipart/form-data",
                    'Authorization': `Bearer ${Cookies.get('token')}`
                }
            }
        );
        console.log("Phản hồi từ API: ", response.data);
    } catch (error) {
        console.error("Lỗi khi gọi API:", error.response?.data || error.message);
        throw error;
    }
}