<template>
    <nav class="flex justify-between p-4 text-gray-500 active:text-sky-500">
        <div>
            <router-link v-if="!isAuthenticated" to="/login" class="mr-4">LOGIN</router-link>
            <router-link v-else to="/profile" class="mr-4">PROFILE</router-link>
            <button v-if="isAuthenticated" @click="logout" class="mr-4 text-red-500">LOGOUT</button>
        </div>

        <div>
            <router-link to="/dashboard" class="mr-4">DASHBOARD</router-link>
            <router-link to="/bank-accounts" class="mr-4">BANK ACCOUNTS</router-link><router-link to="/transactions" class="mr-4">TRANSACTIONS</router-link>
            <router-link to="/expenses" class="mr-4">EXPENSES</router-link>
        </div>
    </nav>
</template>

<script>
import authService from '../services/authService';

export default {
    data() {
        return {
            isAuthenticated: !!authService.getCurrentUser(),
        };
    },
    methods: {
        logout() {
            authService.logout();
            this.isAuthenticated = false;
            this.$router.push('/login');
        },
    },
    watch: {
        $route() {
            this.isAuthenticated = !!authService.getCurrentUser();
        },
    },
};
</script>

<style scoped>
/* Add any scoped styles here if needed */
</style>
