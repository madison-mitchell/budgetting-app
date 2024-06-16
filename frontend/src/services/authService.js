// src/services/authService.js

import axios from 'axios';

const API_URL = 'http://localhost:8080';

class AuthService {
    login(user) {
        return axios
            .post(API_URL + '/api/auth/authenticate', {
                username: user.username,
                password: user.password,
            })
            .then((response) => {
                if (response.data.jwt) {
                    localStorage.setItem('user', JSON.stringify(response.data));
                }
                return response.data;
            });
    }

    logout() {
        localStorage.removeItem('user');
    }

    getCurrentUser() {
        return JSON.parse(localStorage.getItem('user'));
    }
}

export default new AuthService();
