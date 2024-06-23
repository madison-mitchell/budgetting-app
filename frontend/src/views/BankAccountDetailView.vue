<template>
    <div class="max-w-7xl mx-auto p-6 md:p-12">
        <h2 class="text-2xl font-semibold text-gray-900 mb-6">Bank Account Transactions</h2>
        <div v-if="loading" class="text-center">Loading...</div>
        <div v-else>
            <TransactionsTable :transactions="transactions" :categories="categories" @add-transaction="addNewTransaction" @update-transaction="updateTransaction" />
        </div>
    </div>
</template>

<script>
import axios from 'axios';
import authService from '../services/authService';
import TransactionsTable from '../components/TransactionsTable.vue';

export default {
    components: {
        TransactionsTable,
    },
    props: {
        accountId: {
            type: String,
            required: true,
        },
    },
    data() {
        return {
            transactions: [],
            categories: [],
            loading: true,
            userId: null,
            newTransaction: {
                timeOfTransaction: '',
                merchant: '',
                description: '',
                amount: 0,
                type: '',
                isPlanned: true, // Set default value to true
                plannedAmount: 0,
                balance: 0,
                categoryId: '',
            },
        };
    },
    mounted() {
        this.fetchTransactions();
        this.fetchCategories();

        const user = authService.getCurrentUser();
        if (user && user.id) {
            this.userId = user.id;
        } else {
            console.error('User not authenticated');
        }
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
                    console.log('this.transactions: ', this.transactions);

                    this.loading = false;
                })
                .catch((error) => {
                    console.error('Failed to fetch transactions:', error);
                    this.loading = false;
                });
        },
        fetchCategories() {
            axios
                .get('http://localhost:8080/api/categories/relation', {
                    headers: {
                        Authorization: 'Bearer ' + authService.getCurrentUser().jwt,
                    },
                })
                .then((response) => {
                    this.categories = response.data;
                })
                .catch((error) => {
                    console.error('Failed to fetch categories:', error);
                });
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
        addNewTransaction(newTransaction) {
            const user = authService.getCurrentUser();
            const userId = user && user.userId ? user.userId : null;

            if (!userId) {
                console.error('User ID not found');
                return;
            }

            const newTransactionDto = {
                bankAccountId: this.accountId,
                userId: userId,
                categoryId: newTransaction.categoryId,
                amount: newTransaction.amount,
                description: newTransaction.description,
                timeOfTransaction: newTransaction.timeOfTransaction,
                notes: newTransaction.notes,
                merchant: newTransaction.merchant,
                recurring: newTransaction.recurring,
                frequency: newTransaction.frequency,
                included: newTransaction.included,
                reviewed: newTransaction.reviewed,
                type: newTransaction.type,
                isPlanned: newTransaction.isPlanned,
                plannedAmount: newTransaction.plannedAmount,
            };

            console.log('newTransactionDto: ', newTransactionDto);

            axios
                .post('http://localhost:8080/api/transactions', newTransactionDto, {
                    headers: {
                        Authorization: 'Bearer ' + authService.getCurrentUser().jwt,
                    },
                })
                .then((response) => {
                    this.transactions.push(response.data);
                    this.calculateBalances();
                    console.log('New transaction added successfully');
                })
                .catch((error) => {
                    console.error('Failed to add new transaction:', error);
                });
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
        calculateBalances() {
            let balance = 0;
            // Ensure transactions are sorted by date before calculating balances
            const sortedByDate = this.transactions.sort((a, b) => new Date(a.timeOfTransaction) - new Date(b.timeOfTransaction));
            this.transactions = sortedByDate.map((transaction) => {
                balance += transaction.amount;
                return { ...transaction, balance };
            });
        },
        clearNewTransactionForm() {
            this.newTransaction = {
                timeOfTransaction: '',
                merchant: '',
                description: '',
                amount: 0,
                type: '',
                isPlanned: true,
                plannedAmount: 0,
                balance: 0,
                categoryId: '',
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
