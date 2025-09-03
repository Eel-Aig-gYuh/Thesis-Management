import { GoogleGenAI } from "@google/genai";

const ai = new GoogleGenAI({ apiKey: process.env.REACT_APP_GEMINI_API_KEY });

// async function main() {
//     const response = await ai.models.generateContent({
//         model: "gemini-2.0-flash",
//         contents: "Explain how AI works in a few words",
//     });
//     console.log(response.text);
// }

export default ai;