<template>
    <div class="fixed inset-0 flex items-center justify-center z-50">
        <div class="absolute inset-0 bg-gray-500 opacity-75"></div>
        <div class="bg-white p-6 rounded-lg shadow-lg z-10 w-1/2">
            <div class="flex justify-between items-center mb-4">
                <h2 class="text-xl font-semibold">Add {{ itemType }}</h2>
                <button @click="$emit('close')" class="text-gray-500 hover:text-gray-800">&times;</button>
            </div>
            <form @submit.prevent="addItem">
                <div v-for="field in fields" :key="field.name" class="mb-4">
                    <label :class="fieldClass">{{ field.label }}</label>
                    <input v-if="field.type !== 'select'" v-model="itemData[field.name]" :type="field.type" :class="inputClass" :required="field.required" />
                    <select v-else v-model="itemData[field.name]" :class="inputClass" :required="field.required">
                        <option v-for="option in field.options" :key="option.value" :value="option.value">
                            {{ option.label }}
                        </option>
                    </select>
                </div>
                <div class="flex justify-end">
                    <button type="submit" class="bg-sky-500 hover:bg-sky-600 text-white font-semibold py-2 px-4 rounded">Add {{ itemType }}</button>
                </div>
            </form>
        </div>
    </div>
</template>

<script>
import axios from 'axios';
import authService from '../services/authService';

export default {
    props: {
        show: {
            type: Boolean,
            required: true,
        },
        itemType: {
            type: String,
            required: true,
        },
        fields: {
            type: Array,
            required: true,
        },
    },
    data() {
        return {
            itemData: {},
        };
    },
    methods: {
        addItem() {
            const endpoint = this.getEndpoint();
            axios
                .post(`http://localhost:8080/api/${endpoint}`, this.itemData, {
                    headers: {
                        Authorization: 'Bearer ' + authService.getCurrentUser().jwt,
                    },
                })
                .then((response) => {
                    this.$emit('add-item', response.data);
                    this.$emit('close');
                })
                .catch((error) => {
                    console.error('Failed to add item', error);
                });
        },
        getEndpoint() {
            switch (this.itemType) {
                case 'Account':
                    return 'bank-accounts';
                case 'Transaction':
                    return 'transactions';
                case 'Expense':
                    return 'expenses';
                default:
                    throw new Error('Invalid item type');
            }
        },
    },
    computed: {
        fieldClass() {
            return 'block text-sm font-medium text-gray-700';
        },
        inputClass() {
            return 'mt-1 block w-full';
        },
    },
};
</script>

<style scoped>
/* Add any scoped styles here if needed */
</style>
