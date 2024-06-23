<template>
    <!-- <div class="overflow-x-auto"> -->
    <div>
        <div class="mb-4 flex items-center">
            <label for="month-selector" class="text-sm font-medium text-gray-700 w-40">Time Period</label>
            <select
                id="month-selector"
                v-model="selectedMonth"
                @change="filterTransactionsByMonth"
                class="flex-grow border-gray-300 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm rounded-md">
                <option v-for="month in availableMonths" :key="month.value" :value="month.value">{{ month.text }}</option>
            </select>
        </div>

        <table class="min-w-full bg-white rounded-lg shadow overflow-hidden">
            <thead>
                <tr class="w-full bg-gray-100 border-b">
                    <th class="px-6 py-3 text-left text-sm font-medium text-gray-600 tracking-wider cursor-pointer" @click="sortBy('timeOfTransaction')">Date</th>
                    <th class="px-6 py-3 text-left text-sm font-medium text-gray-600 tracking-wider cursor-pointer" @click="sortBy('merchant')">Merchant</th>
                    <th class="px-6 py-3 text-left text-sm font-medium text-gray-600 tracking-wider cursor-pointer" @click="sortBy('description')">Description</th>
                    <th class="px-6 py-3 text-left text-sm font-medium text-gray-600 tracking-wider cursor-pointer" @click="sortBy('amount')">Amount</th>
                    <th class="px-6 py-3 text-left text-sm font-medium text-gray-600 tracking-wider cursor-pointer" @click="sortBy('balance')">Balance</th>
                    <th class="px-6 py-3 text-left text-sm font-medium text-gray-600 tracking-wider cursor-pointer" @click="sortBy('type')">Type</th>
                    <th class="px-6 py-3 text-left text-sm font-medium text-gray-600 tracking-wider cursor-pointer" @click="sortBy('isPlanned')">Planned</th>
                    <th class="px-6 py-3 text-left text-sm font-medium text-gray-600 tracking-wider cursor-pointer" @click="sortBy('plannedAmount')">Planned Amount</th>
                    <th class="px-6 py-3 text-left text-sm font-medium text-gray-600 tracking-wider cursor-pointer" @click="sortBy('category')">Category</th>
                </tr>
            </thead>
            <tbody>
                <TransactionRow v-for="transaction in sortedTransactions" :key="transaction.id" :transaction="transaction" @update-transaction="updateTransaction" />
                <NewTransactionForm :categories="categories" @add-transaction="addTransaction" />
            </tbody>
        </table>
    </div>
</template>

<script>
import { format, startOfMonth, endOfMonth, parseISO } from 'date-fns';
import TransactionRow from './TransactionRow.vue';
import NewTransactionForm from './NewTransactionForm.vue';
import axios from 'axios';
import authService from '../services/authService';

export default {
    components: {
        TransactionRow,
        NewTransactionForm,
    },
    props: {
        transactions: {
            type: Array,
            required: true,
        },
        categories: {
            type: Array,
            required: true,
        },
    },
    data() {
        return {
            selectedMonth: '',
            availableMonths: [],
            sortedTransactions: [],
        };
    },
    watch: {
        transactions: {
            handler(newTransactions) {
                this.setupAvailableMonths();
                this.filterTransactionsByMonth();
                this.sortTransactions();
            },
            deep: true,
        },
    },
    mounted() {
        this.setupAvailableMonths();
        this.filterTransactionsByMonth();
        this.sortTransactions();
    },
    methods: {
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

            if (!this.selectedMonth && this.availableMonths.length > 0) {
                this.selectedMonth = this.availableMonths[0].value;
            }
        },
        filterTransactionsByMonth() {
            const selectedMonth = this.selectedMonth;
            if (selectedMonth) {
                const start = startOfMonth(parseISO(`${selectedMonth}-01`));
                const end = endOfMonth(parseISO(`${selectedMonth}-01`));
                this.sortedTransactions = this.transactions.filter((transaction) => {
                    const transactionDate = parseISO(transaction.timeOfTransaction);
                    return transactionDate >= start && transactionDate <= end;
                });
            } else {
                this.sortedTransactions = this.transactions;
            }
        },
        sortTransactions() {
            this.sortedTransactions.sort((a, b) => new Date(a.timeOfTransaction) - new Date(b.timeOfTransaction));
        },
        sortBy(field) {
            this.sortField = field;
            this.sortOrder = this.sortOrder === 'asc' ? 'desc' : 'asc';
            this.sortedTransactions.sort((a, b) => {
                let modifier = this.sortOrder === 'asc' ? 1 : -1;
                if (a[this.sortField] < b[this.sortField]) return -1 * modifier;
                if (a[this.sortField] > b[this.sortField]) return 1 * modifier;
                return 0;
            });
        },
        updateTransaction(transaction) {
            this.$emit('update-transaction', transaction);
        },
        addTransaction(newTransaction) {
            this.$emit('add-transaction', newTransaction);
        },
    },
};
</script>
