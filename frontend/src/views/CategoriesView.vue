<template>
    <div class="max-w-7xl mx-auto p-12">
        <h2 class="text-2xl font-semibold text-gray-900 mb-6">Categories</h2>
        <div class="flex space-x-4 items-center shadow-md border border-grey-50 rounded-lg p-6 bg-gray-100 mb-4">
            <label for="monthSelect" class="text-sm font-medium text-gray-700">Select Month</label>
            <div class="relative w-full">
                <select
                    v-model="selectedMonth"
                    @change="fetchCategories"
                    id="monthSelect"
                    class="text-sm appearance-none w-full bg-white border border-gray-300 hover:border-gray-500 px-4 py-2 pr-8 rounded leading-tight focus:outline-none focus:shadow-outline">
                    <option v-for="month in availableMonths" :key="month.value" :value="month.value">{{ month.text }}</option>
                </select>
            </div>
        </div>
        <CategoryList :categories="categories" />
    </div>
</template>

<script>
import axios from 'axios';
import { format, subMonths, addMonths } from 'date-fns';
import authService from '../services/authService';
import CategoryList from '@/components/CategoryList.vue';

export default {
    components: {
        CategoryList,
    },
    data() {
        return {
            categories: [],
            selectedMonth: format(new Date(), 'yyyy-MM'),
            availableMonths: this.getAvailableMonths(),
        };
    },
    mounted() {
        this.fetchCategories();
    },
    methods: {
        getAvailableMonths() {
            const months = [];
            const today = new Date();

            // Adding past months
            for (let i = 0; i < 6; i++) {
                const monthDate = subMonths(today, i);
                months.push({
                    value: format(monthDate, 'yyyy-MM'),
                    text: format(monthDate, 'MMMM yyyy'),
                });
            }

            // Adding future months
            for (let i = 1; i <= 6; i++) {
                const monthDate = addMonths(today, i);
                months.push({
                    value: format(monthDate, 'yyyy-MM'),
                    text: format(monthDate, 'MMMM yyyy'),
                });
            }

            // Sort months in chronological order
            return months.sort((a, b) => (a.value > b.value ? 1 : -1));
        },
        fetchCategories() {
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
                    console.log('API response data:', response.data);
                    this.categories = this.organizeCategories(response.data);
                    console.log('Organized categories:', this.categories);
                })
                .catch((error) => {
                    console.error('Failed to fetch categories:', error);
                });
        },
        organizeCategories(categoryData) {
            const categories = {};

            categoryData.forEach((item) => {
                const parentCategoryId = item.category.parentCategory.id;
                const parentCategoryName = item.category.parentCategory.name;
                const childCategoryId = item.category.childCategory.id;
                const childCategoryName = item.category.childCategory.name;
                const totalAmount = item.amountSpent;
                const budget = item.budgetAmount;

                if (!categories[parentCategoryId]) {
                    categories[parentCategoryId] = {
                        id: parentCategoryId,
                        name: parentCategoryName,
                        totalAmount: 0,
                        budget: 0,
                        children: [],
                    };
                }

                const parent = categories[parentCategoryId];
                const child = {
                    id: childCategoryId,
                    name: childCategoryName,
                    totalAmount: totalAmount,
                    budget: budget,
                    relationId: item.id,
                };
                parent.children.push(child);
                parent.totalAmount += totalAmount;
                parent.budget += budget;
            });

            console.log('Categories after organization:', categories);
            return Object.values(categories);
        },
    },
};
</script>

<style scoped>
/* Style for the select dropdown */
#monthSelect {
    height: auto;
    overflow-y: auto;
}

#monthSelect option {
    height: 1.5em; /* Adjust the height as needed */
}

#monthSelect {
    max-height: 8em; /* Adjust the max height to show 5 options (approx) */
}
</style>
