<template>
    <div class="max-w-7xl mx-auto p-12">
        <h2 class="text-2xl font-semibold text-gray-900 mb-6">Profile</h2>
        <div v-if="errorMessage" class="text-red-500">{{ errorMessage }}</div>
        <div v-if="user" class="bg-white shadow-md border border-grey-50 rounded-lg p-6 text-left">
            <p><strong>Username:</strong> {{ user.username }}</p>
            <p><strong>First Name:</strong> {{ user.firstName }}</p>
            <p><strong>Last Name:</strong> {{ user.lastName }}</p>
            <p><strong>Email:</strong> {{ user.email }}</p>
            <p><strong>Phone:</strong> {{ user.phoneNumber }}</p>
            <p><strong>Address:</strong> {{ user.address }}</p>
            <p><strong>Birthdate:</strong> {{ formatDate(user.dateOfBirth) }}</p>
            <p><strong>Last Login:</strong> {{ user.lastLogin }}</p>
            <p><strong>Role:</strong> {{ user.role }}</p>
            <!-- Add more fields as necessary -->
        </div>
    </div>
</template>

<script>
import axios from 'axios';
import authService from '../services/authService';

export default {
    data() {
        return {
            user: null,
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
                    this.user = response.data;
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
