<template>
    <div class="max-w-7xl mx-auto p-12">
        <h2 class="text-2xl font-semibold text-gray-900 mb-6">Transactions</h2>
        <div v-if="errorMessage" class="text-red-500">{{ errorMessage }}</div>
        <div v-else>
            <TransactionsTable
                :transactions="filteredTransactions"
                :categories="categories"
                :selected-month="selectedMonth"
                :available-months="availableMonths"
                @update-transaction="updateTransaction"
                @open-modal="openModal"
                @filter-transactions="filterTransactionsByMonth"
                @change-month="handleMonthChange" />
            <NewTransactionModal v-if="showModal" :show="showModal" :categories="categories" @close="closeModal" @save="addTransaction" />
        </div>
    </div>
</template>

<script>
import axios from 'axios';
import { format, parseISO, startOfMonth, endOfMonth } from 'date-fns';
import authService from '@/services/authService';
import TransactionsTable from '../components/transactions/TransactionsTable.vue';
import NewTransactionModal from '../components/transactions/NewTransactionModal.vue';

export default {
    components: {
        TransactionsTable,
        NewTransactionModal,
    },
    data() {
        return {
            transactions: [],
            categories: [],
            errorMessage: '',
            selectedMonth: format(new Date(), 'yyyy-MM'),
            availableMonths: [],
            showModal: false,
        };
    },
    mounted() {
        this.fetchTransactions();
        this.fetchCategories();
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
                    this.setupAvailableMonths();
                })
                .catch((error) => {
                    this.errorMessage = 'Failed to fetch transactions.';
                    console.error(error);
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
                    this.errorMessage = 'Failed to fetch categories.';
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
                    this.setupAvailableMonths();
                })
                .catch((error) => {
                    this.errorMessage = 'Failed to add transaction.';
                    console.error(error);
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
                this.selectedMonth = currentMonth;
            } else if (!this.selectedMonth && this.availableMonths.length > 0) {
                this.selectedMonth = this.availableMonths[0].value;
            }
        },
        filterTransactionsByMonth() {
            const selectedMonth = this.selectedMonth;
            if (selectedMonth) {
                const start = startOfMonth(parseISO(`${selectedMonth}-01`));
                const end = endOfMonth(parseISO(`${selectedMonth}-01`));
                return this.transactions.filter((transaction) => {
                    const transactionDate = parseISO(transaction.timeOfTransaction);
                    return transactionDate >= start && transactionDate <= end;
                });
            } else {
                return this.transactions;
            }
        },
        handleMonthChange(newMonth) {
            this.selectedMonth = newMonth;
            this.filterTransactionsByMonth();
        },
        updateTransaction(updatedTransaction) {
            const index = this.transactions.findIndex((t) => t.id === updatedTransaction.id);
            if (index !== -1) {
                this.transactions.splice(index, 1, updatedTransaction);
                this.setupAvailableMonths();
            }
        },
        openModal() {
            this.showModal = true;
        },
        closeModal() {
            this.showModal = false;
        },
        addTransaction(newTransaction) {
            this.handleAddTransaction(newTransaction);
            this.closeModal();
        },
    },
    computed: {
        filteredTransactions() {
            return this.filterTransactionsByMonth();
        },
    },
};
</script>

<style scoped>
/* Add any scoped styles here if needed */
</style>
