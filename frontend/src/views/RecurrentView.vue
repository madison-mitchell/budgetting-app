<template>
    <div class="max-w-7xl mx-auto p-12">
        <h2 class="text-2xl font-semibold text-gray-900 mb-6">Recurring Transactions</h2>
        <div class="flex justify-end mb-6">
            <AddButton @click="showModal = true" class="h-full" />
        </div>
        <div v-if="errorMessage" class="text-red-500">{{ errorMessage }}</div>
        <div v-else class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            <ExpenseCard v-for="expense in sortedExpenses" :key="expense.id" :expense="expense" />
        </div>

        <!-- Modal -->
        <AddItemModal
            v-if="showModal"
            :show="showModal"
            itemType="Expense"
            :fields="expenseFields"
            :categoryOptions="categories"
            :accountOptions="accounts"
            :transactionOptions="transactions"
            @close="showModal = false"
            @add-item="handleAddExpense" />
    </div>
</template>

<script>
import axios from 'axios';
import { format } from 'date-fns';
import authService from '@/services/authService';
import ExpenseCard from '../components/recurrent/RecurrentCard.vue';
import AddButton from '../components/AddButton.vue';
import AddItemModal from '../components/AddItemModal.vue';

export default {
    components: {
        ExpenseCard,
        AddButton,
        AddItemModal,
    },
    data() {
        return {
            expenses: [],
            budgets: [],
            showModal: false,
            errorMessage: '',
            selectedMonth: format(new Date(), 'yyyy-MM'),
            categories: [],
            accounts: [],
            transactions: [],
            expenseFields: [
                { name: 'name', label: 'Name', type: 'text', required: true },
                { name: 'merchant', label: 'Merchant', type: 'text', required: false },
                { name: 'amount', label: 'Amount', type: 'number', required: true },
                { name: 'childCategoryName', label: 'Category', type: 'select', required: true, options: [] },
                { name: 'totalBudget', label: 'Total Budget', type: 'number', required: false },
                { name: 'notes', label: 'Notes', type: 'text', required: false },
                { name: 'dueDate', label: 'Due Date', type: 'date', required: true },
                { name: 'frequency', label: 'Frequency', type: 'text', required: false },
                { name: 'myShareBudget', label: 'My Share Budget', type: 'number', required: false },
                { name: 'accountId', label: 'Account', type: 'select', required: true, options: [] },
                { name: 'myShare', label: 'My Share', type: 'number', required: false },
                { name: 'isSharedExpense', label: 'Shared Expense', type: 'checkbox', required: false },
                { name: 'paymentDate', label: 'Payment Date', type: 'date', required: false },
                { name: 'reviewed', label: 'Reviewed', type: 'checkbox', required: false },
                { name: 'sharedPartyName', label: 'Shared Party Name', type: 'text', required: false },
                { name: 'recurring', label: 'Recurring', type: 'checkbox', required: false },
                { name: 'otherPartyShare', label: 'Other Party Share', type: 'number', required: false },
                { name: 'paid', label: 'Paid', type: 'checkbox', required: false },
                { name: 'transactionId', label: 'Transaction', type: 'select', required: true, options: [] },
            ],
        };
    },
    mounted() {
        this.fetchExpenses();
        this.fetchBudgets();
        this.fetchCategories();
        this.fetchAccounts();
        this.fetchTransactions();
    },
    computed: {
        sortedExpenses() {
            return this.sortExpensesByDueDate(this.expenses);
        },
    },
    methods: {
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
                    this.errorMessage = 'Failed to fetch expenses.';
                    console.error(error);
                });
        },
        fetchBudgets() {
            const [year, month] = this.selectedMonth.split('-');
            const token = authService.getCurrentUser().jwt;

            axios
                .get('http://localhost:8080/api/category-budgets', {
                    params: { month: parseInt(month), year: parseInt(year) },
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                })
                .then((response) => {
                    this.budgets = response.data;
                })
                .catch((error) => {
                    console.error('Failed to fetch budgets:', error);
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
                    this.categories = response.data.map((category) => ({ value: category.id, label: category.childCategory.name }));
                    console.log('this.categories: ', this.categories);
                })
                .catch((error) => {
                    console.error('Failed to fetch categories:', error);
                });
        },
        fetchAccounts() {
            axios
                .get('http://localhost:8080/api/bank-accounts', {
                    headers: {
                        Authorization: 'Bearer ' + authService.getCurrentUser().jwt,
                    },
                })
                .then((response) => {
                    this.accounts = response.data.map((account) => ({ value: account.id, label: account.name }));
                })
                .catch((error) => {
                    console.error('Failed to fetch accounts:', error);
                });
        },
        fetchTransactions() {
            axios
                .get('http://localhost:8080/api/transactions', {
                    headers: {
                        Authorization: 'Bearer ' + authService.getCurrentUser().jwt,
                    },
                })
                .then((response) => {
                    console.log('response.data: ', response.data);
                    this.transactions = response.data.map((transaction) => ({
                        value: transaction.id,
                        label: this.formatDate(transaction.timeOfTransaction) + ' ' + transaction.merchant + ' ' + transaction.categoryId.childCategory.name,
                    }));
                    console.log('this.transactions: ', this.transactions);
                })
                .catch((error) => {
                    console.error('Failed to fetch transactions:', error);
                });
        },
        sortExpensesByDueDate(expenses) {
            return expenses.sort((a, b) => {
                const dateA = new Date(a.dueDate);
                const dateB = new Date(b.dueDate);
                return dateA - dateB;
            });
        },
        handleAddExpense(newExpense) {
            this.expenses.push(newExpense);
            this.showModal = false;
        },
        getBudgetForCategory(categoryId) {
            if (!Array.isArray(this.budgets)) {
                console.error('Budgets is not an array:', this.budgets);
                return 0;
            }

            const budgetItem = this.budgets.find((b) => {
                if (!b.category || typeof b.category.id === 'undefined') {
                    console.error('Invalid budget item or missing category ID:', b);
                    return false;
                }

                return b.category.id === categoryId;
            });

            return budgetItem ? budgetItem.budgetAmount : 0;
        },
        formatDate(datetime) {
            const date = new Date(datetime);
            return date.toLocaleDateString();
        },
    },
};
</script>
