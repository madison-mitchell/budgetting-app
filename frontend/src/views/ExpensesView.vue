<template>
    <div class="max-w-7xl mx-auto p-12">
        <h2 class="text-2xl font-semibold text-gray-900 mb-6">Expenses</h2>
        <div v-if="errorMessage" class="text-red-500">{{ errorMessage }}</div>
        <div v-else class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            <ExpenseCard v-for="expense in expenses" :key="expense.id" :expense="expense" />
            <AddButton @click="showModal = true" />
        </div>

        <!-- Modal -->
        <AddItemModal v-if="showModal" :show="showModal" itemType="Expense" :fields="expenseFields" @close="showModal = false" @add-item="handleAddExpense" />
    </div>
</template>

<script>
import axios from 'axios';
import authService from '../services/authService';
import ExpenseCard from '../components/ExpenseCard.vue';
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
            showModal: false,
            errorMessage: '',
            expenseFields: [
                { name: 'description', label: 'Description', type: 'text', required: true },
                { name: 'amount', label: 'Amount', type: 'number', required: true },
                {
                    name: 'categoryId',
                    label: 'Category',
                    type: 'select',
                    options: this.getCategories(),
                    required: true,
                },
                {
                    name: 'bankAccountId',
                    label: 'Bank Account',
                    type: 'select',
                    options: this.getBankAccounts(),
                    required: true,
                },
                { name: 'date', label: 'Date', type: 'date', required: true },
            ],
        };
    },
    mounted() {
        this.fetchExpenses();
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
        handleAddExpense(newExpense) {
            this.expenses.push(newExpense);
            this.showModal = false;
        },
        getCategories() {
            // Fetch categories from API or use predefined list
            return [];
        },
        getBankAccounts() {
            // Fetch bank accounts from API or use predefined list
            return [];
        },
    },
};
</script>

<style scoped>
/* Add any scoped styles here if needed */
</style>
