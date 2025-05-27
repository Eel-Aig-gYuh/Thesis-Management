export const uploadThesisFileToCloudinary = async (file) => {
    const url = `https://api.cloudinary.com/v1_1/${process.env.REACT_APP_CLOUDINARY_CLOUD_NAME}/raw/upload`;

    const formData = new FormData();
    formData.append("file", file);
    formData.append("upload_preset", process.env.REACT_APP_CLOUDINARY_UPLOAD_PRESET);

    const response = await fetch(url, {
        method: "POST",
        body: formData,
    });

    if (!response.ok) {
        const err = await response.json();
        throw new Error(err.error?.message || "Upload failed");
    }

    const data = await response.json();
    return data.secure_url; // This is the uploaded file URL
};