import { initializeApp } from 'firebase/app';
import { getFirestore } from 'firebase/firestore'


const firebaseGeminiConfig = {
    apiKey: process.env.REACT_APP_GEMINI_FIREBASE_API_KEY,
    authDomain: process.env.REACT_APP_GEMINI_FIREBASE_AUTH_DOMAIN,
    projectId: process.env.REACT_APP_GEMINI_FIREBASE_PROJECT_ID,
    storageBucket: process.env.REACT_APP_GEMINI_FIREBASE_STORAGE_BUCKET,
    messagingSenderId: process.env.REACT_APP_GEMINI_FIREBASE_MESSAGING_SENDER_ID,
    appId: process.env.REACT_APP_GEMINI_FIREBASE_APP_ID,
    measurementId: process.env.REACT_APP_GEMINI_FIREBASE_MEASUREMENT_ID
};

// Init Firebase
const geapp = initializeApp(firebaseGeminiConfig, 'geminifirebase');

// Init Firestore and Auth
export const gedb = getFirestore(geapp);
export default geapp;