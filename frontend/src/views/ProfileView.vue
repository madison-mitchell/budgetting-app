<template>
    <div class="max-w-7xl mx-auto p-12">
        <h2 class="text-2xl font-semibold text-gray-900 mb-6">Profile</h2>
        <div v-if="errorMessage" class="text-red-500">{{ errorMessage }}</div>
        <div v-if="auth" class="bg-white shadow-md border border-grey-50 rounded-lg p-6 text-left">
            <p><strong>Username:</strong> {{ auth.username }}</p>
            <p><strong>First Name:</strong> {{ auth.firstName }}</p>
            <p><strong>Last Name:</strong> {{ auth.lastName }}</p>
            <p><strong>Email:</strong> {{ auth.email }}</p>
            <p><strong>Phone:</strong> {{ auth.phoneNumber }}</p>
            <p><strong>Address:</strong> {{ auth.address }}</p>
            <p><strong>Birthdate:</strong> {{ formatDate(auth.dateOfBirth) }}</p>
            <p><strong>Last Login:</strong> {{ auth.lastLogin }}</p>
            <p><strong>Role:</strong> {{ auth.role }}</p>
        </div>
    </div>
</template>

<script>
import axios from 'axios';
import authService from '../services/authService';

export default {
    data() {
        return {
            auth: null,
            errorMessage: '',
        };
    },
    mounted() {
        this.fetchUserProfile();
    },
    methods: {
        fetchUserProfile() {
            const currentUser = authService.getCurrentUser();
            if (!currentUser) {
                this.errorMessage = 'User not authenticated';
                return;
            }

            axios
                .get(`http://localhost:8080/api/users/profile`, {
                    headers: {
                        Authorization: `Bearer ${currentUser.jwt}`,
                    },
                    params: {
                        username: currentUser.username,
                    },
                })
                .then((response) => {
                    this.auth = response.data;
                })
                .catch((error) => {
                    console.error('Failed to fetch user profile:', error);
                    this.errorMessage = 'Failed to fetch user profile';
                });
        },
        formatDate(datetime) {
            const date = new Date(datetime);
            return date.toLocaleDateString();
        },
    },
};
</script>

<style scoped>
/* Add any scoped styles here if needed */
</style>
