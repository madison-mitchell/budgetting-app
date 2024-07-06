<template>
    <nav class="flex justify-evenly p-4 text-gray-500">
        <div>
            <router-link to="/dashboard" class="mr-4 hover:text-sky-400">DASHBOARD</router-link>
            <router-link to="/bank-accounts" class="mr-4 hover:text-sky-400">BANK ACCOUNTS</router-link>
            <router-link to="/transactions" class="mr-4 hover:text-sky-400">TRANSACTIONS</router-link>
            <router-link to="/expenses" class="mr-4 hover:text-sky-400">EXPENSES</router-link>
            <router-link to="/categories" class="mr-4 hover:text-sky-400">CATEGORIES</router-link>
        </div>
    </nav>
</template>

<script>
export default {
    name: 'Navigation',
};
</script>

<style scoped>
/* Add any scoped styles here if needed */
</style>
