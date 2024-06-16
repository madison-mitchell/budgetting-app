<template>
    <div class="max-w-7xl mx-auto p-12">
        <h2 class="text-2xl font-semibold text-gray-900 mb-6">Transactions</h2>
        <div v-if="errorMessage" class="text-red-500">{{ errorMessage }}</div>
        <div v-else class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            <TransactionCard v-for="transaction in transactions" :key="transaction.id" :transaction="transaction" />
            <!-- Add New Transaction Button -->
            <AddTransactionButton @add-transaction="handleAddTransaction" />
        </div>
    </div>
</template>

<script>
import axios from 'axios';
import authService from '../services/authService';
import TransactionCard from '../components/TransactionCard.vue';
import AddTransactionButton from '../components/AddTransactionButton.vue';

export default {
    components: {
        TransactionCard,
        AddTransactionButton,
    },
    data() {
        return {
            transactions: [],
            errorMessage: '',
        };
    },
    mounted() {
        this.fetchTransactions();
    },
    methods: {
        fetchTransactions() {
            axios
                .get('http://localhost:8080/api/transactions', {
                    headers: {
                        Authorization: 'Bearer ' + authService.getCurrentUser().jwt,
                    },
                })
                .then((response) => {
                    this.transactions = response.data;
                })
                .catch((error) => {
                    this.errorMessage = 'Failed to fetch transactions.';
                    console.error(error);
                });
        },
        handleAddTransaction(newTransaction) {
            axios
                .post('http://localhost:8080/api/transactions', newTransaction, {
                    headers: {
                        Authorization: 'Bearer ' + authService.getCurrentUser().jwt,
                    },
                })
                .then((response) => {
                    this.transactions.push(response.data);
                })
                .catch((error) => {
                    this.errorMessage = 'Failed to add transaction.';
                    console.error(error);
                });
        },
    },
};
</script>

<style scoped>
/* Add any scoped styles here if needed */
</style>
