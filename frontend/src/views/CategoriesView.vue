<template>
    <div class="max-w-7xl mx-auto p-12">
        <h2 class="text-2xl font-semibold text-gray-900 mb-6">Categories</h2>
        <CategoryList :categories="categories" />
    </div>
</template>

<script>
import axios from 'axios';
import authService from '../services/authService';
import CategoryList from '@/components/CategoryList.vue';

export default {
    components: {
        CategoryList,
    },
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
        organizeCategories(categoryData) {
            const categories = {};
            categoryData.forEach((item) => {
                if (!categories[item.parentId]) {
                    categories[item.parentId] = {
                        id: item.parentId,
                        name: item.parentName,
                        totalAmount: 0,
                        budget: 0,
                        children: [],
                    };
                }
                const parent = categories[item.parentId];
                const child = {
                    id: item.childId,
                    name: item.childName,
                    totalAmount: item.totalAmount,
                    budget: item.budget,
                    relationId: item.relationId,
                };
                parent.children.push(child);
                parent.totalAmount += child.totalAmount;
                parent.budget += child.budget;
            });
            return Object.values(categories);
        },
    },
};
</script>

<style scoped></style>
