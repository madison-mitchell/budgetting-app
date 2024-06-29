<!-- src/views/DashboardView.vue -->
<template>
    <div class="max-w-7xl mx-auto p-12 text-gray-600">
        <div class="grid gap-6 auto-rows-min grid-cols-1 md:grid-cols-2 lg:grid-cols-3">
            <!-- Bank Accounts Overview -->
            <AccountsOverviewCard :accounts="accounts" :totalBalance="totalBalance" />

            <!-- Simplified Transactions Table -->
            <div class="bg-white shadow-md border border-grey-50 rounded-lg p-6">
                <TransactionsTableCard :transactions="filteredTransactions" @update-transaction="updateTransaction" />
            </div>

            <!-- Other dashboard components (Expenses Overview, etc.) -->
        </div>
    </div>
</template>

<script>
import axios from 'axios';
import authService from '@/services/authService';
import { format, parseISO, startOfMonth, endOfMonth } from 'date-fns';
import AccountsOverviewCard from '@/components/dashboard/AccountsOverviewCard.vue';
import TransactionsTableCard from '@/components/dashboard/TransactionsTableCard.vue';

export default {
    components: {
        AccountsOverviewCard,
        TransactionsTableCard,
    },
    data() {
        return {
            accounts: [],
            totalBalance: 0,
            transactions: [],
            availableMonths: [],
            currentMonth: format(new Date(), 'yyyy-MM'),
        };
    },
    mounted() {
        this.fetchData();
    },
    methods: {
        fetchData() {
            this.fetchBankAccounts();
            this.fetchTransactions();
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
        fetchTransactions() {
            const [year, month] = this.currentMonth.split('-');
            axios
                .get('http://localhost:8080/api/transactions', {
                    params: { month: parseInt(month), year: parseInt(year) },
                    headers: { Authorization: 'Bearer ' + authService.getCurrentUser().jwt },
                })
                .then((response) => {
                    this.transactions = response.data;
                    this.setupAvailableMonths();
                })
                .catch((error) => {
                    console.error('Failed to fetch transactions:', error);
                });
        },
        setupAvailableMonths() {
            const monthsSet = new Set();
            this.transactions.forEach((transaction) => {
                const transactionMonth = format(parseISO(transaction.timeOfTransaction), 'yyyy-MM');
                monthsSet.add(transactionMonth);
            });

            this.availableMonths = [...monthsSet]
                .sort()
                .reverse()
                .map((month) => {
                    return {
                        value: month,
                        text: format(parseISO(`${month}-01`), 'MMMM yyyy'),
                    };
                });

            const currentMonth = format(new Date(), 'yyyy-MM');
            if (this.availableMonths.some((month) => month.value === currentMonth)) {
                this.currentMonth = currentMonth;
            } else if (!this.currentMonth && this.availableMonths.length > 0) {
                this.currentMonth = this.availableMonths[0].value;
            }
        },
        handleMonthChange(newMonth) {
            this.currentMonth = newMonth;
            this.fetchTransactions();
        },
        updateTransaction(updatedTransaction) {
            const index = this.transactions.findIndex((t) => t.id === updatedTransaction.id);
            if (index !== -1) {
                this.transactions.splice(index, 1, updatedTransaction);
            }
        },
    },
    computed: {
        filteredTransactions() {
            const selectedMonth = this.currentMonth;
            let filteredTransactions = this.transactions;

            if (selectedMonth) {
                const start = startOfMonth(parseISO(`${selectedMonth}-01`));
                const end = endOfMonth(parseISO(`${selectedMonth}-01`));
                filteredTransactions = this.transactions.filter((transaction) => {
                    const transactionDate = parseISO(transaction.timeOfTransaction);
                    return transactionDate >= start && transactionDate <= end && !transaction.reviewed;
                });
            }

            return filteredTransactions;
        },
    },
};
</script>

<style scoped>
/* Add any scoped styles here if needed */
</style>
