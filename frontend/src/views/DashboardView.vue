<!-- src/views/DashboardView.vue -->
<template>
    <div class="max-w-7xl mx-auto p-12 text-gray-600">
        <h2 class="text-2xl font-semibold text-gray-900 mb-6">Dashboard</h2>
        <div class="grid gap-6 auto-rows-min grid-cols-1 md:grid-cols-2 lg:grid-cols-3">
            <!-- Bank Accounts Overview -->
            <AccountsOverviewCard :accounts="accounts" :totalBalance="totalBalance" />

            <!-- Recent Transactions -->
            <!-- <div class="bg-white shadow-md border border-grey-50 rounded-lg p-6">
                <h3 class="text-lg font-semibold text-gray-800">Recent Transactions</h3>
                <ul>
                    <li v-for="transaction in recentTransactions" :key="transaction.id">{{ transaction.description }}: {{ formatBalance(transaction.amount) }}</li>
                </ul>
            </div> -->

            <!-- Expenses Overview -->
            <!-- <div class="bg-white shadow-md border border-grey-50 rounded-lg p-6">
                <h3 class="text-lg font-semibold text-gray-800">Expenses Overview</h3>
                <ul>
                    <li v-for="expense in expenses" :key="expense.id">{{ expense.description }}: {{ formatBalance(expense.amount) }}</li>
                </ul>
            </div> -->

            <!-- Budget Overview -->
            <!-- <div class="bg-white shadow-md border border-grey-50 rounded-lg p-6">
                <h3 class="text-lg font-semibold text-gray-800">Budget Overview</h3>
                <table class="table-auto w-full">
                    <tbody>
                        <tr v-for="category in budgetCategories" :key="category.id">
                            <td class="text-left px-4 w-full">{{ category.name }}</td>
                            <td class="text-right tracking-wide px-4">{{ formatBalance(category.spent) }} / {{ formatBalance(category.budget) }}</td>
                        </tr>
                    </tbody>
                </table>
            </div> -->

            <!-- Income vs. Expenses -->
            <!-- <div class="bg-white shadow-md border border-grey-50 rounded-lg p-6">
                <h3 class="text-lg font-semibold text-gray-800">Income vs. Expenses</h3>
                <canvas id="incomeVsExpensesChart"></canvas>
            </div> -->

            <!-- Top Spending Categories -->
            <!-- <div class="bg-white shadow-md border border-grey-50 rounded-lg p-6">
                <h3 class="text-lg font-semibold text-gray-800">Top Spending Categories</h3>
                <ul>
                    <li v-for="category in topSpendingCategories" :key="category.id">{{ category.name }}: {{ formatBalance(category.amount) }}</li>
                </ul>
            </div> -->

            <!-- Upcoming Bills/Payments -->
            <!-- <div class="bg-white shadow-md border border-grey-50 rounded-lg p-6">
                <h3 class="text-lg font-semibold text-gray-800">Upcoming Bills/Payments</h3>
                <ul>
                    <li v-for="bill in upcomingBills" :key="bill.id">{{ bill.description }}: {{ formatBalance(bill.amount) }} (Due: {{ formatDate(bill.dueDate) }})</li>
                </ul>
            </div> -->
        </div>
    </div>
</template>

<script>
import axios from 'axios';
import authService from '@/services/authService';
import { format } from 'date-fns';
import { Chart } from 'chart.js';
import AccountsOverviewCard from '@/components/dashboard/AccountsOverviewCard.vue';

export default {
    components: {
        AccountsOverviewCard,
    },
    data() {
        return {
            accounts: [],
            totalBalance: 0,
            recentTransactions: [],
            expenses: [],
            budgetCategories: [],
            topSpendingCategories: [],
            upcomingBills: [],
        };
    },
    mounted() {
        this.fetchData();
        // this.renderIncomeVsExpensesChart();
    },
    methods: {
        fetchData() {
            this.fetchBankAccounts();
            // this.fetchRecentTransactions();
            // this.fetchExpenses();
            // this.fetchBudgetCategories();
            // this.fetchTopSpendingCategories();
            // this.fetchUpcomingBills();
        },
        fetchBankAccounts() {
            axios
                .get('http://localhost:8080/api/bank-accounts', {
                    headers: { Authorization: 'Bearer ' + authService.getCurrentUser().jwt },
                })
                .then((response) => {
                    this.accounts = response.data;
                    this.totalBalance = this.accounts.reduce((acc, account) => acc + account.currentBalance, 0);
                })
                .catch((error) => {
                    console.error('Failed to fetch bank accounts:', error);
                });
        },
        // fetchRecentTransactions() {
        //     axios
        //         .get('http://localhost:8080/api/transactions/recent', {
        //             headers: { Authorization: 'Bearer ' + authService.getCurrentUser().jwt },
        //         })
        //         .then((response) => {
        //             this.recentTransactions = response.data;
        //         })
        //         .catch((error) => {
        //             console.error('Failed to fetch recent transactions:', error);
        //         });
        // },
        // fetchExpenses() {
        //     axios
        //         .get('http://localhost:8080/api/expenses', {
        //             headers: { Authorization: 'Bearer ' + authService.getCurrentUser().jwt },
        //         })
        //         .then((response) => {
        //             this.expenses = response.data;
        //         })
        //         .catch((error) => {
        //             console.error('Failed to fetch expenses:', error);
        //         });
        // },
        // fetchBudgetCategories() {
        //     axios
        //         .get('http://localhost:8080/api/budget-categories', {
        //             headers: { Authorization: 'Bearer ' + authService.getCurrentUser().jwt },
        //         })
        //         .then((response) => {
        //             this.budgetCategories = response.data;
        //         })
        //         .catch((error) => {
        //             console.error('Failed to fetch budget categories:', error);
        //         });
        // },
        // fetchTopSpendingCategories() {
        //     axios
        //         .get('http://localhost:8080/api/spending-categories/top', {
        //             headers: { Authorization: 'Bearer ' + authService.getCurrentUser().jwt },
        //         })
        //         .then((response) => {
        //             this.topSpendingCategories = response.data;
        //         })
        //         .catch((error) => {
        //             console.error('Failed to fetch top spending categories:', error);
        //         });
        // },
        // fetchUpcomingBills() {
        //     axios
        //         .get('http://localhost:8080/api/upcoming-bills', {
        //             headers: { Authorization: 'Bearer ' + authService.getCurrentUser().jwt },
        //         })
        //         .then((response) => {
        //             this.upcomingBills = response.data;
        //         })
        //         .catch((error) => {
        //             console.error('Failed to fetch upcoming bills:', error);
        //         });
        // },
        formatBalance(balance) {
            if (balance === null || balance === undefined) {
                return '$0.00';
            }
            const formattedBalance = balance.toFixed(2).replace(/\d(?=(\d{3})+\.)/g, '$&,');
            return balance < 0 ? `-$${formattedBalance.slice(1)}` : `$${formattedBalance}`;
        },
        formatDate(date) {
            const options = { year: 'numeric', month: 'short', day: 'numeric' };
            return new Date(date).toLocaleDateString(undefined, options);
        },
        // renderIncomeVsExpensesChart() {
        //     const ctx = document.getElementById('incomeVsExpensesChart').getContext('2d');
        //     new Chart(ctx, {
        //         type: 'pie',
        //         data: {
        //             labels: ['Income', 'Expenses'],
        //             datasets: [
        //                 {
        //                     data: [this.income, this.expensesTotal],
        //                     backgroundColor: ['#36a2eb', '#ff6384'],
        //                 },
        //             ],
        //         },
        //     });
        // },
    },
    computed: {
        // income() {
        //     return this.budgetCategories.find((category) => category.name === 'Income')?.spent || 0;
        // },
        // expensesTotal() {
        //     return this.budgetCategories.reduce((total, category) => (category.name !== 'Income' ? total + category.spent : total), 0);
        // },
    },
};
</script>

<style scoped>
/* Add any scoped styles here if needed */
</style>
