<template>
    <div class="max-w-7xl mx-auto p-12">
        <h2 class="text-2xl font-semibold text-gray-900 mb-6">Dashboard</h2>
        <div class="grid gap-6 auto-rows-min grid-cols-1 md:grid-cols-2 lg:grid-cols-3">
            <!-- Bank Accounts Overview -->
            <div class="bg-white shadow-md border border-grey-50 rounded-lg p-6">
                <h3 class="text-lg font-semibold text-gray-800">Bank Accounts</h3>

                <table class="table-auto w-full">
                    <tbody>
                        <tr v-for="account in accounts" :key="account.id">
                            <td class="text-left px-4 w-full">{{ account.bankName }}</td>
                            <td :class="{ 'text-red-600': account.balance < 0, 'text-right tracking-wide px-4': true }" class="text-right w-40">
                                {{ formatBalance(account.balance) }}
                            </td>
                        </tr>
                    </tbody>
                </table>
                <h3 class="text-lg font-semibold mt-4">Total Balance: {{ formatBalance(totalBalance) }}</h3>
            </div>
            <!-- Expenses Overview -->
            <div class="bg-white shadow-md border border-grey-50 rounded-lg p-6">
                <h3 class="text-lg font-semibold text-gray-800">Expenses</h3>
                <ul>
                    <li v-for="expense in expenses" :key="expense.id">{{ expense.description }}: {{ formatBalance(expense.amount) }}</li>
                </ul>
            </div>
            <!-- Top Categories Overview -->
            <div class="bg-white shadow-md border border-grey-50 rounded-lg py-6 px-10 md:col-span-2 lg:col-span-1 lg:row-span-2">
                <h3 class="text-lg font-semibold text-gray-800">Categories</h3>
                <table class="table-auto w-full">
                    <tbody>
                        <div v-for="parentCategory in topCategories" :key="parentCategory.id">
                            <tr class="font-semibold flex justify-between">
                                <td class="text-left">{{ parentCategory.name }}</td>
                                <td :class="{ 'text-red-600': parentCategory.totalAmount < 0, 'text-right tracking-wide px-4': true }" class="text-right">
                                    {{ formatBalance(parentCategory.totalAmount) }} / {{ formatBalance(parentCategory.budget) }}
                                </td>
                            </tr>
                            <tr v-for="childCategory in parentCategory.children || []" :key="childCategory.id" class="flex justify-between">
                                <td class="text-left pl-4">{{ childCategory.name }}</td>
                                <td :class="{ 'text-red-600': childCategory.totalAmount < 0, 'text-right tracking-wide px-4': true }">
                                    {{ formatBalance(childCategory.totalAmount) }} / {{ formatBalance(childCategory.budget) }}
                                </td>
                            </tr>
                        </div>
                    </tbody>
                </table>
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
            topCategories: [],
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
                .get('http://localhost:8080/api/transactions/category-totals', {
                    headers: {
                        Authorization: 'Bearer ' + authService.getCurrentUser().jwt,
                    },
                })
                .then((response) => {
                    console.log('Fetched category data:', response.data);
                    this.categories = this.organizeCategories(response.data);
                    console.log('Organized categories:', this.categories);
                    this.topCategories = this.getTopAndBottomCategories(this.categories);
                    console.log('Top and bottom categories:', this.topCategories);
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
                if (!categories[item.parentCategoryId]) {
                    categories[item.parentCategoryId] = {
                        id: item.parentCategoryId,
                        name: item.parentName,
                        totalAmount: 0,
                        budget: 0,
                        children: [],
                    };
                }
                const parent = categories[item.parentCategoryId];
                const child = {
                    relationId: item.relationId,
                    id: item.childId,
                    name: item.childName,
                    totalAmount: item.totalAmount,
                    budget: item.budget,
                };

                parent.children.push(child);
                parent.totalAmount += child.totalAmount;
                parent.budget += child.budget;
            });

            return Object.values(categories);
        },
        getTopAndBottomCategories(categories) {
            // Sort categories by totalAmount
            categories.sort((a, b) => b.totalAmount - a.totalAmount);

            // Get top 3 and bottom 3 categories
            const top3 = categories.slice(0, 3);
            const bottom3 = categories.slice(-3);

            return [...top3, ...bottom3];
        },
    },
};
</script>

<style scoped></style>
