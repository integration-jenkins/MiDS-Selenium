// src/api/axios.js
import axios from 'axios';

const instance = axios.create({
    baseURL: 'http://localhost:6083',
});

instance.interceptors.request.use(config => {
    const token = localStorage.getItem('jwtToken');
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

export default instance;