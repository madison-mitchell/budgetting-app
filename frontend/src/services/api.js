import axios from 'axios';

const apiClient = axios.create({
    baseURL: 'http://localhost:8080/api', // Your backend API base URL
    withCredentials: false, // This is the default
    headers: {
        Accept: 'application/json',
        'Content-Type': 'application/json',
    },
});

export default {
    getBankAccounts() {
        return apiClient.get('/bank-accounts');
    },
    // Add more methods for other API endpoints as needed
};
