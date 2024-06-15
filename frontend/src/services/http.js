// src/services/http.js

import axios from 'axios';
import authService from './authService';

const http = axios.create({
    baseURL: 'http://localhost:8080',
    headers: {
        'Content-Type': 'application/json',
    },
});

http.interceptors.request.use(
    (config) => {
        const user = authService.getCurrentUser();
        if (user && user.jwt) {
            config.headers['Authorization'] = 'Bearer ' + user.jwt;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

export default http;
