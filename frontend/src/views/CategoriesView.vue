<template>
    <div class="max-w-7xl mx-auto p-12">
        <h2 class="text-2xl font-semibold text-gray-900 mb-6">Categories</h2>
        <div class="space-y-6">
            <div v-for="(parentCategory, index) in sortedCategories" :key="index" class="bg-white shadow-md border border-grey-50 rounded-lg p-6">
                <div class="flex justify-between items-center">
                    <h3 class="text-lg font-semibold text-gray-800">{{ parentCategory.name }}</h3>
                    <h3 class="text-lg font-semibold" :class="{ 'text-red-600': parentCategory.totalAmount < 0 }">
                        {{ formatBalance(parentCategory.totalAmount) }} / {{ formatBalance(parentCategory.budget) }}
                    </h3>
                </div>
                <table class="table-auto w-full mt-4">
                    <tbody>
                        <tr v-for="childCategory in parentCategory.children" :key="childCategory.id">
                            <td class="text-left px-4">{{ childCategory.name }}</td>
                            <td :class="{ 'text-red-600': childCategory.totalAmount < 0, 'text-gray-500': true }" class="text-right tracking-wide">
                                {{ formatBalance(childCategory.totalAmount) }} / {{ formatBalance(childCategory.budget) }}
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</template>

<script>
import axios from 'axios';
import authService from '../services/authService';

export default {
    data() {
        return {
            categories: [],
        };
    },
    mounted() {
        this.fetchCategories();
    },
    methods: {
        fetchCategories() {
            axios
                .get('http://localhost:8080/api/transactions/category-totals', {
                    headers: {
                        Authorization: 'Bearer ' + authService.getCurrentUser().jwt,
                    },
                })
                .then((response) => {
                    this.categories = this.organizeCategories(response.data);
                })
                .catch((error) => {
                    console.error('Failed to fetch categories:', error);
                });
        },
        formatBalance(balance) {
            if (balance === null || balance === undefined) {
                return '$0.00';
            }
            const formattedBalance = balance.toFixed(2).replace(/\d(?=(\d{3})+\.)/g, '$&,');
            return balance < 0 ? `-$${formattedBalance.slice(1)}` : `$${formattedBalance}`;
        },
        organizeCategories(categoryData) {
            const categories = {};
            categoryData.forEach((item) => {
                const parent = { id: item.parentId, name: item.parentName, totalAmount: 0, budget: 0, children: [] };
                const child = { id: item.childId, name: item.childName, totalAmount: item.totalAmount, budget: item.budget };

                if (!categories[parent.id]) {
                    categories[parent.id] = parent;
                }
                categories[parent.id].children.push(child);
                categories[parent.id].totalAmount += child.totalAmount;
                categories[parent.id].budget += child.budget;
            });
            return Object.values(categories);
        },
    },
    computed: {
        sortedCategories() {
            const nonZeroCategories = this.categories.filter((category) => category.totalAmount !== 0);
            const zeroCategories = this.categories.filter((category) => category.totalAmount === 0);
            return [...nonZeroCategories.sort((a, b) => b.totalAmount - a.totalAmount), ...zeroCategories];
        },
    },
};
</script>

<style scoped></style>
