<template>
    <nav>
        <router-link v-if="!isLoggedIn" to="/">LOGIN</router-link>
        <router-link v-else to="/profile">PROFILE</router-link> | <router-link to="/dashboard">DASHBOARD</router-link> | <router-link to="/bank-accounts">BANK ACCOUNTS</router-link> |
        <router-link to="/transactions">TRANSACTIONS</router-link> |
        <router-link to="/expenses">EXPENSES</router-link>
    </nav>
</template>

<script>
import authService from '../services/authService';

export default {
    data() {
        return {
            isLoggedIn: false,
        };
    },
    mounted() {
        this.isLoggedIn = authService.getCurrentUser() !== null;
    },
};
</script>

<style scoped>
/* Add any scoped styles here if needed */
</style>
