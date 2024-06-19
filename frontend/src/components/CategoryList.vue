<template>
    <div class="space-y-2 shadow-md border border-grey-50 rounded-lg p-6 bg-white">
        <div v-for="(parentCategory, index) in sortedCategories" :key="index" class="mt-0 mb-0 p-0">
            <div class="flex justify-between items-center">
                <h3 class="text-md font-semibold text-gray-800">{{ parentCategory.name }}</h3>
                <h3 class="text-md font-semibold" :class="{ 'text-red-600': parentCategory.totalAmount < 0 }">
                    {{ formatBalance(parentCategory.totalAmount) }} /
                    <span @click="editBudget(parentCategory, 'parent')" class="cursor-pointer">
                        <span v-if="!parentCategory.editing">{{ formatBalance(parentCategory.budget) }}</span>
                        <input v-else v-model="parentCategory.budgetFormatted" @blur="saveBudget(parentCategory, 'parent')" class="w-20 text-right" />
                    </span>
                </h3>
            </div>
            <table class="table-auto w-full text-sm">
                <tbody>
                    <tr v-for="childCategory in parentCategory.children" :key="childCategory.id">
                        <td class="text-left pl-6">{{ childCategory.name }}</td>
                        <td :class="{ 'text-red-600': childCategory.totalAmount < 0, 'text-gray-500': true }" class="text-right tracking-wide">
                            {{ formatBalance(childCategory.totalAmount) }} /
                            <span @click="editBudget(childCategory, 'child')" class="cursor-pointer">
                                <span v-if="!childCategory.editing">{{ formatBalance(childCategory.budget) }}</span>
                                <input v-else v-model="childCategory.budgetFormatted" @blur="saveBudget(childCategory, 'child')" class="w-20 text-right" />
                            </span>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
</template>

<script>
import axios from 'axios';
import authService from '../services/authService';

export default {
    props: {
        categories: {
            type: Array,
            required: true,
        },
    },
    methods: {
        formatBalance(balance) {
            if (balance === null || balance === undefined) {
                return '$0.00';
            }
            const numBalance = Number(balance);
            if (isNaN(numBalance)) {
                return '$0.00';
            }
            const formattedBalance = numBalance.toFixed(2).replace(/\d(?=(\d{3})+\.)/g, '$&,');
            return numBalance < 0 ? `-$${formattedBalance.slice(1)}` : `$${formattedBalance}`;
        },
        formatInputBalance(balance) {
            if (balance === null || balance === undefined) {
                return '0.00';
            }
            const numBalance = Number(balance);
            return isNaN(numBalance) ? '0.00' : numBalance.toFixed(2);
        },
        editBudget(category, type) {
            this.resetEditing();
            category.editing = true;
            category.budgetFormatted = this.formatInputBalance(category.budget);
        },
        saveBudget(category, type) {
            category.editing = false;
            category.budget = parseFloat(category.budgetFormatted);
            const relationId = category.relationId;
            if (!relationId) {
                console.error('relationId is undefined for category', category);
                return;
            }
            axios
                .put(
                    `http://localhost:8080/api/categories/relation/${relationId}/budget`,
                    { budget: category.budget },
                    {
                        headers: {
                            Authorization: 'Bearer ' + authService.getCurrentUser().jwt,
                        },
                    }
                )
                .then((response) => {
                    console.log('Budget updated successfully for relationId:', relationId);
                })
                .catch((error) => {
                    console.error('Failed to update budget:', error);
                });
        },
        resetEditing() {
            this.categories.forEach((parentCategory) => {
                parentCategory.editing = false;
                parentCategory.children.forEach((childCategory) => {
                    childCategory.editing = false;
                });
            });
        },
    },
    computed: {
        sortedCategories() {
            if (!this.categories) return [];
            const nonZeroCategories = this.categories.filter((category) => category.totalAmount !== 0);
            const zeroCategories = this.categories.filter((category) => category.totalAmount === 0);
            return [...nonZeroCategories.sort((a, b) => b.totalAmount - a.totalAmount), ...zeroCategories];
        },
    },
};
</script>

<style scoped></style>
