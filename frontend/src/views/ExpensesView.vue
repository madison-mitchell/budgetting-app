<template>
    <div class="max-w-7xl mx-auto p-12">
        <h2 class="text-2xl font-semibold text-gray-900 mb-6">Expenses</h2>
        <div class="flex justify-end mb-6">
            <AddButton @click="showModal = true" class="h-full" />
        </div>
        <div v-if="errorMessage" class="text-red-500">{{ errorMessage }}</div>
        <div v-else class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            <!-- <ExpenseCard v-for="expense in sortedExpenses" :key="expense.id" :expense="expense" :budget="getBudgetForCategory(expense.category.id)" /> -->
            <ExpenseCard v-for="expense in sortedExpenses" :key="expense.id" :expense="expense" />
        </div>

        <!-- Modal -->
        <AddItemModal v-if="showModal" :show="showModal" itemType="Expense" :fields="expenseFields" @close="showModal = false" @add-item="handleAddExpense" />
    </div>
</template>

<script>
import axios from 'axios';
import { format, subMonths, addMonths } from 'date-fns';
import authService from '@/services/authService';
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
            budgets: [],
            showModal: false,
            errorMessage: '',
            selectedMonth: format(new Date(), 'yyyy-MM'),
        };
    },
    mounted() {
        this.fetchExpenses();
        this.fetchBudgets();
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
                    // this.budgets = this.organizeCategories(response.data);
                    this.budgets = response.data;
                })
                .catch((error) => {
                    console.error('Failed to fetch budgets:', error);
                });
        },
        sortExpensesByDueDate(expenses) {
            return expenses.sort((a, b) => {
                const dateA = new Date(a.dueDate);
                const dateB = new Date(b.dueDate);
                return dateA - dateB;
            });
        },
        getCategories() {
            return [];
        },
        getBankAccounts() {
            return [];
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
    },
};
</script>

<style scoped>
/* Add any scoped styles here if needed */
</style>
