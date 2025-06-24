// src/api/axios.js
import axios from 'axios';
import BASE_URL from './config';
const instance = axios.create({
    baseURL: BASE_URL, // Updated to match the backend's port
});

instance.interceptors.request.use(config => {
    const token = localStorage.getItem('jwtToken');
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

export default instance;