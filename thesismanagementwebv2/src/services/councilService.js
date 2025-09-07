import { authApis, endpoints } from "../configs/Apis";


export const createCouncil = async(data) => {
    console.log("Gửi request với params:", data);
    try {
        const response = await authApis().post(endpoints['council/create'], data);
        console.log("Phản hồi từ API: ", response.data);
        
        return response.data;
    } catch (error) {
        console.error("Lỗi khi gọi API:", error.response?.data || error.message);
        console.error(error.response?.data);
        throw error;
    }
};

export const getCouncils = async (page = 1, filters = {}) => {
    const params = { page, ...filters };

    console.log("Fetching councils with filters:", filters);
    try {
        const response = await authApis().get(endpoints['council'], { params });
        console.log("Councils response:", response.data);
        return {
            data: response.data.councils,
            totalPages: response.data.totalPages,
        } // Giả định API trả về { councils: [], totalPages: number, currentPage: number }
    } catch (error) {
        console.error("Error fetching councils:", error.response?.data || error.message);
        throw error;
    }
};

export const getMyCouncils = async (page = 1, filters = {}) => {
    const params = { page, ...filters };

    console.log("Fetching councils with filters:", filters);
    try {
        const response = await authApis().get(endpoints['council/my-council'], { params });
        console.log("Councils response:", response.data);
        return {
            data: response.data.councils,
            totalPages: response.data.totalPages,
        } // Giả định API trả về { councils: [], totalPages: number, currentPage: number }
    } catch (error) {
        console.error("Error fetching councils:", error.response?.data || error.message);
        throw error;
    }
};

export const getCouncilById = async (councilId) => {
    console.log(`Fetching council ID: ${councilId}`);
    try {
        const response = await authApis().get(endpoints['council/detail'](councilId));
        console.log("Council response:", response.data);
        return response.data;
    } catch (error) {
        console.error("Error fetching council:", error.response?.data || error.message);
        throw error;
    }
};

export const updateCouncil = async (councilId, payload) => {
    console.log(`Updating council ID: ${councilId} with payload:`, { payload });
    try {
        const response = await authApis().put(endpoints['council/detail'](councilId), payload);
        console.log("Update council response:", response.data);
        return response.data;
    } catch (error) {
        console.error("Error updating council:", error.response?.data || error.message);
        throw error;
    }
};

export const cancelCouncil = async (councilId, payload) => {
    console.log(`Updating council ID: ${councilId} with payload:`, { payload });
    try {
        const response = await authApis().put(endpoints['council/detail'](councilId), payload);
        console.log("Update council response:", response.data);
        return response.data;
    } catch (error) {
        console.error("Error updating council:", error.response?.data || error.message);
        throw error;
    }
};

export const lockCouncil = async (councilId) => {
    console.log(`Locking council ID: ${councilId} with payload:`);
    try {
        const response = await authApis().post(endpoints['council/lock'](councilId));
        console.log("Lock council response:", response.data);
        return response.data;
    } catch (error) {
        console.error("Error updating council:", error.response?.data || error.message);
        throw error;
    }
};

export const deleteCouncil = async (councilId) => {
    console.log(`Deleting council ID: ${councilId}`);
    try {
        const response = await authApis().delete(endpoints['council/detail'](councilId));
        console.log("Delete council response:", response.data);
        return response.data;
    } catch (error) {
        console.error("Error deleting council:", error.response?.data || error.message);
        throw error;
    }
};