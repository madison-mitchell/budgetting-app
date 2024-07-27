<template>
    <div class="fixed inset-0 flex items-center justify-center z-50">
        <div class="absolute inset-0 bg-gray-500 opacity-75"></div>
        <div class="bg-white p-6 rounded-lg shadow-lg z-10 w-1/2">
            <div class="flex justify-between items-center mb-4">
                <h2 class="text-xl font-semibold">Add {{ itemType }}</h2>
                <button @click="$emit('close')" class="text-gray-500 hover:text-gray-800">&times;</button>
            </div>
            <form @submit.prevent="addItem">
                <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                    <div v-for="field in fields" :key="field.name" class="mb-4 text-left">
                        <label :class="fieldClass" class="w-full">
                            {{ field.label }}
                            <span v-if="field.required" class="text-red-500">*</span>
                        </label>
                        <input
                            v-if="field.type !== 'select' && field.type !== 'checkbox'"
                            v-model="itemData[field.name]"
                            :type="field.type"
                            :class="inputClass"
                            :required="field.required"
                            class="w-full border border-0.5 rounded-md border-gray-200 text-sm p-1" />
                        <input v-if="field.type === 'checkbox'" v-model="itemData[field.name]" type="checkbox" :class="inputClass" class="text-sm" :required="field.required" />
                        <select
                            v-else-if="field.type === 'select'"
                            v-model="itemData[field.name]"
                            :class="inputClass"
                            :required="field.required"
                            class="w-full border border-0.5 rounded-md border-gray-200 p-0.5 text-sm">
                            <option v-for="option in getOptions(field.name)" :key="option.value" :value="option.value" class="text-sm">
                                {{ option.label }}
                            </option>
                        </select>
                    </div>
                </div>
                <div class="flex justify-end mt-4">
                    <button type="submit" class="bg-sky-500 hover:bg-sky-600 text-white font-semibold py-2 px-4 rounded">Add {{ itemType }}</button>
                </div>
            </form>
        </div>
    </div>
</template>

<script>
import axios from 'axios';
import authService from '@/services/authService';

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
        categoryOptions: {
            type: Array,
            required: true,
        },
        accountOptions: {
            type: Array,
            required: true,
        },
        transactionOptions: {
            type: Array,
            required: true,
        },
    },
    data() {
        return {
            itemData: this.initializeItemData(),
        };
    },
    methods: {
        initializeItemData() {
            const data = {};
            this.fields.forEach((field) => {
                if (field.type === 'checkbox') {
                    data[field.name] = false;
                } else {
                    data[field.name] = '';
                }
            });
            return data;
        },
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
        getOptions(fieldName) {
            if (fieldName === 'childCategoryName') {
                return this.categoryOptions;
            }
            if (fieldName === 'accountId') {
                return this.accountOptions;
            }
            if (fieldName === 'transactionId') {
                return this.transactionOptions;
            }
            return [];
        },
    },
    watch: {
        fields: {
            handler() {
                this.itemData = this.initializeItemData();
            },
            immediate: true,
            deep: true,
        },
    },
    computed: {
        fieldClass() {
            return 'block text-sm font-medium text-gray-700';
        },
        inputClass() {
            return 'mt-1 block';
        },
    },
};
</script>

<style scoped>
/* Add any scoped styles here if needed */
</style>
