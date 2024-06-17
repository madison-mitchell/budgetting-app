<template>
    <div class="max-w-7xl mx-auto p-12">
        <h2 class="text-2xl font-semibold text-gray-900 mb-6">Dashboard</h2>
        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            <!-- Bank Accounts Overview -->
            <div class="bg-white shadow-md border border-grey-50 rounded-lg p-6">
                <h3 class="text-lg font-semibold text-gray-800">Bank Accounts</h3>
                <p class="text-lg font-semibold mt-4">Total Balance: {{ formatBalance(totalBalance) }}</p>
                <ul>
                    <li v-for="account in accounts" :key="account.id">{{ account.bankName }} : {{ formatBalance(account.balance) }}</li>
                </ul>
            </div>
            <!-- Transaction Categories Overview -->
            <div class="bg-white shadow-md border border-grey-50 rounded-lg p-6">
                <h3 class="text-lg font-semibold text-gray-800">Transaction Categories</h3>
                <ul>
                    <li v-for="transaction in transactions" :key="transaction.id">{{ transaction.category.parentCategory.name }}</li>
                </ul>
            </div>
            <!-- Expenses Overview -->
            <div class="bg-white shadow-md border border-grey-50 rounded-lg p-6">
                <h3 class="text-lg font-semibold text-gray-800">Expenses</h3>
                <ul>
                    <li v-for="expense in expenses" :key="expense.id">{{ expense.description }}: {{ formatBalance(expense.amount) }}</li>
                </ul>
            </div>
        </div>
    </div>
</template>

<script>
import axios from 'axios';
import authService from '../services/authService';

export default {
    data() {
        return {
            totalBalance: 0,
            transactions: [],
            expenses: [],
            accounts: [],
        };
    },
    mounted() {
        this.fetchData();
    },
    methods: {
        fetchData() {
            this.fetchBankAccounts();
            this.fetchTransactionCategories();
            this.fetchExpenses();
        },
        fetchBankAccounts() {
            axios
                .get('http://localhost:8080/api/bank-accounts', {
                    headers: {
                        Authorization: 'Bearer ' + authService.getCurrentUser().jwt,
                    },
                })
                .then((response) => {
                    this.totalBalance = response.data.reduce((acc, account) => acc + account.balance, 0);
                    this.accounts = response.data;
                })
                .catch((error) => {
                    console.error('Failed to fetch bank accounts:', error);
                });
        },
        fetchTransactionCategories() {
            axios
                .get('http://localhost:8080/api/transactions/categories', {
                    headers: {
                        Authorization: 'Bearer ' + authService.getCurrentUser().jwt,
                    },
                })
                .then((response) => {
                    this.transactions = response.data;
                })
                .catch((error) => {
                    console.error('Failed to fetch transaction categories:', error);
                });
        },
        fetchExpenses() {
            axios
                .get('http://localhost:8080/api/expenses', {
                    headers: {
                        Authorization: 'Bearer ' + authService.getCurrentUser().jwt,
                    },
                })
                .then((response) => {
                    this.expenses = response.data;
                })
                .catch((error) => {
                    console.error('Failed to fetch expenses:', error);
                });
        },
        formatBalance(balance) {
            const formattedBalance = balance.toFixed(2).replace(/\d(?=(\d{3})+\.)/g, '$&,');
            return balance < 0 ? `-$${formattedBalance.slice(1)}` : `$${formattedBalance}`;
        },
    },
};
</script>

<style scoped>
/* Add any scoped styles here if needed */
</style>
