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
            <div class="bg-white shadow-md border border-grey-50 rounded-lg py-6 px-10">
                <h3 class="text-lg font-semibold text-gray-800">Categories</h3>
                <div v-for="(parentCategory, index) in categories" :key="index">
                    <h4 class="text-md text-left font-semibold text-gray-700">{{ parentCategory.name }}</h4>
                    <table class="table-auto w-full ml-4">
                        <tbody>
                            <tr v-for="childCategory in parentCategory.children" :key="childCategory.id">
                                <td class="text-left px-4 w-40">{{ childCategory.name }}</td>
                                <td class="text-right tracking-wide px-4">{{ formatBalance(childCategory.totalAmount) }}</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
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
            categories: [],
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
                .get(`http://localhost:8080/api/transactions/category-totals`, {
                    headers: {
                        Authorization: 'Bearer ' + authService.getCurrentUser().jwt,
                    },
                })
                .then((response) => {
                    console.log('Fetched category data:', response.data);
                    this.categories = this.organizeCategories(response.data);
                    console.log('Organized categories:', this.categories);
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
            if (balance === null || balance === undefined) {
                return '$0.00';
            }
            const formattedBalance = balance.toFixed(2).replace(/\d(?=(\d{3})+\.)/g, '$&,');
            return balance < 0 ? `-$${formattedBalance.slice(1)}` : `$${formattedBalance}`;
        },
        organizeCategories(categoryData) {
            const categories = {};
            categoryData.forEach((item) => {
                const parent = { id: item.parentId, name: item.parentName };
                const child = { id: item.childId, name: item.childName, totalAmount: item.totalAmount };

                if (!categories[parent.id]) {
                    categories[parent.id] = { name: parent.name, children: [] };
                }
                categories[parent.id].children.push(child);
            });
            return Object.values(categories);
        },
    },
};
</script>

<style scoped>
/* Add any scoped styles here if needed */
</style>
