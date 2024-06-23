<template>
    <div class="max-w-7xl mx-auto p-6 md:p-12">
        <h2 class="text-2xl font-semibold text-gray-900 mb-6">Bank Account Transactions</h2>
        <div v-if="loading" class="text-center">Loading...</div>
        <div v-else>
            <div class="overflow-x-auto">
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
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="transaction in sortedTransactions" :key="transaction.id" class="border-b hover:bg-gray-50 text-left text-sm">
                            <td class="px-6 py-4 whitespace-nowrap">{{ formatDate(transaction.timeOfTransaction) }}</td>
                            <td class="px-6 py-4 whitespace-nowrap">{{ transaction.merchant }}</td>
                            <td class="px-6 py-4 whitespace-nowrap">{{ transaction.description }}</td>
                            <td class="px-6 py-4 whitespace-nowrap" :class="{ 'text-green-600': transaction.amount > 0 }">{{ formatBalance(transaction.amount) }}</td>
                            <td class="px-6 py-4 whitespace-nowrap" :class="{ 'text-red-500': transaction.balance < 0 }">{{ formatBalance(transaction.balance) }}</td>
                            <td class="px-6 py-4 whitespace-nowrap">{{ transaction.type }}</td>
                            <td class="px-6 py-4 whitespace-nowrap text-center">
                                <input
                                    type="checkbox"
                                    v-model="transaction.isPlanned"
                                    @change="updateTransaction(transaction)"
                                    class="form-checkbox h-4 w-5 text-green-600 transition duration-150 ease-in-out" />
                            </td>
                            <td class="px-6 py-4 whitespace-nowrap">{{ formatBalance(transaction.plannedAmount) }}</td>
                        </tr>
                        <tr class="text-sm text-left">
                            <td class="px-6 py-4">
                                <input type="date" v-model="newTransaction.timeOfTransaction" class="form-input w-full" />
                            </td>
                            <td class="px-6 py-4">
                                <input type="text" v-model="newTransaction.merchant" class="form-input w-full" />
                            </td>
                            <td class="px-6 py-4">
                                <input type="text" v-model="newTransaction.description" class="form-input w-full" />
                            </td>
                            <td class="px-6 py-4">
                                <input type="number" v-model="newTransaction.amount" step="0.01" class="form-input w-full" placeholder="0.00" />
                            </td>
                            <td class="px-6 py-4"></td>
                            <td class="px-6 py-4">
                                <input type="text" v-model="newTransaction.type" class="form-input w-full" />
                            </td>
                            <td class="px-6 py-4 text-center">
                                <input type="checkbox" v-model="newTransaction.isPlanned" disabled class="form-checkbox h-4 w-4 text-green-600 transition duration-150 ease-in-out" />
                            </td>
                            <td class="px-6 py-4">
                                <input type="number" v-model="newTransaction.plannedAmount" step="0.01" class="form-input w-full" placeholder="0.00" />
                            </td>
                        </tr>
                    </tbody>
                </table>
                <div class="mt-4">
                    <button @click="addNewTransaction" class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded">Add Transaction</button>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import axios from 'axios';
import authService from '../services/authService';

export default {
    props: {
        accountId: {
            type: String,
            required: true,
        },
    },
    data() {
        return {
            transactions: [],
            newTransaction: {
                timeOfTransaction: '',
                merchant: '',
                description: '',
                amount: 0,
                type: '',
                isPlanned: true, // Set default value to true
                plannedAmount: 0,
                balance: 0,
            },
            loading: true,
            sortField: 'timeOfTransaction',
            sortOrder: 'asc',
        };
    },
    computed: {
        sortedTransactions() {
            return this.transactions.sort((a, b) => {
                let modifier = this.sortOrder === 'asc' ? 1 : -1;
                if (a[this.sortField] < b[this.sortField]) return -1 * modifier;
                if (a[this.sortField] > b[this.sortField]) return 1 * modifier;
                return 0;
            });
        },
    },
    mounted() {
        this.fetchTransactions();
    },
    methods: {
        fetchTransactions() {
            axios
                .get(`http://localhost:8080/api/transactions/account/${this.accountId}`, {
                    headers: {
                        Authorization: 'Bearer ' + authService.getCurrentUser().jwt,
                    },
                })
                .then((response) => {
                    this.transactions = response.data;
                    this.calculateBalances();
                    this.loading = false;
                })
                .catch((error) => {
                    console.error('Failed to fetch transactions:', error);
                    this.loading = false;
                });
        },
        calculateBalances() {
            let balance = 0;
            // Ensure transactions are sorted by date before calculating balances
            const sortedByDate = this.transactions.sort((a, b) => new Date(a.timeOfTransaction) - new Date(b.timeOfTransaction));
            this.transactions = sortedByDate.map((transaction) => {
                balance += transaction.amount;
                return { ...transaction, balance };
            });
        },
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
        formatCurrencyPlaceholder(value) {
            return new Intl.NumberFormat('en-US', {
                style: 'currency',
                currency: 'USD',
                minimumFractionDigits: 2,
            }).format(value);
        },
        sortBy(field) {
            if (this.sortField === field) {
                this.sortOrder = this.sortOrder === 'asc' ? 'desc' : 'asc';
            } else {
                this.sortField = field;
                this.sortOrder = 'asc';
            }
        },
        updateTransaction(transaction) {
            axios
                .put(`http://localhost:8080/api/transactions/${transaction.id}`, transaction, {
                    headers: {
                        Authorization: 'Bearer ' + authService.getCurrentUser().jwt,
                    },
                })
                .then((response) => {
                    console.log('Transaction updated successfully');
                })
                .catch((error) => {
                    console.error('Failed to update transaction:', error);
                });
        },
        addNewTransaction() {
            this.newTransaction.balance = this.calculateFutureBalance(this.newTransaction);
            this.transactions.push({ ...this.newTransaction, id: Date.now() });
            this.calculateBalances();
            this.clearNewTransactionForm();
        },
        calculateFutureBalance(newTransaction) {
            let balance = this.transactions.length ? this.transactions[this.transactions.length - 1].balance : 0;
            return balance + newTransaction.amount;
        },
        clearNewTransactionForm() {
            this.newTransaction = {
                timeOfTransaction: '',
                merchant: '',
                description: '',
                amount: 0,
                type: '',
                planned: false,
                plannedAmount: 0,
                balance: 0,
            };
        },
    },
};
</script>

<style scoped>
.table-auto th,
.table-auto td {
    border: 1px solid #ddd;
}
.table-auto th {
    background-color: #f9f9f9;
    text-align: left;
    cursor: pointer;
}
</style>
