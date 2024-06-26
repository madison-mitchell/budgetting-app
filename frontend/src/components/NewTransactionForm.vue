<template>
    <tr class="text-sm text-left bg-gray-100">
        <td class="pl-4 py-4">
            <input type="date" v-model="newTransaction.timeOfTransaction" class="form-input w-full bg-gray-100 border border-gray-300 border-1 rounded-lg px-1" />
        </td>
        <td class="pl-4 py-4 flex justify-between">
            <!-- <input type="text" v-model="newTransaction.type" class="form-input bg-gray-100 border border-gray-300 border-1 rounded-lg px-1 w-4" /> -->

            <select
                v-model="newTransaction.type"
                class="form-select bg-gray-100 border border-gray-300 border-1 rounded-lg px-1 w-20 mr-1"
                :class="{ 'text-green-600': newTransaction.type === 'Income' }">
                <option value="Regular">R</option>
                <option value="Income">I</option>
                <option value="Transfer">T</option>
            </select>

            <input type="text" v-model="newTransaction.merchant" class="form-input w-full bg-gray-100 border border-gray-300 border-1 rounded-lg px-1" />
        </td>
        <td class="pl-4 py-4">
            <input type="text" v-model="newTransaction.description" class="form-input w-full bg-gray-100 border border-gray-300 border-1 rounded-lg px-1" />
        </td>
        <td class="pl-4 py-4">
            <input type="number" v-model="newTransaction.amount" step="0.01" class="form-input w-full bg-gray-100 border border-gray-300 border-1 rounded-lg px-1" placeholder="0.00" />
        </td>
        <td class="pl-4 py-4"></td>
        <!-- <td class="pl-4 py-4">
            <input type="text" v-model="newTransaction.type" class="form-input w-full bg-gray-100 border border-gray-300 border-1 rounded-lg px-1" />
        </td> -->
        <td class="pl-4 py-4 text-center">
            <input type="checkbox" v-model="newTransaction.isPlanned" disabled class="form-checkbox h-4 w-4 transition duration-150 ease-in-out" />
        </td>
        <td class="pl-4 py-4">
            <input type="number" v-model="newTransaction.plannedAmount" step="0.01" class="form-input w-full bg-gray-100 border border-gray-300 border-1 rounded-lg px-1" placeholder="0.00" />
        </td>
        <td class="px-4 py-4 flex justify-between space-x-1">
            <select v-model="newTransaction.categoryId" class="form-select w-full bg-gray-100 border border-gray-300 border-1 rounded-lg px-1">
                <option v-for="category in categories" :key="category.id" :value="category.id">{{ category.childCategory.name }}</option>
            </select>

            <button @click="addTransaction" class="bg-sky-500 hover:bg-sky-700 text-white text-xs font-bold px-3 py-2 rounded-full">+</button>
        </td>
        <!-- <td class="pr-3">
            <button @click="addTransaction" class="bg-sky-500 hover:bg-sky-700 text-white text-xs font-bold px-3 py-2 rounded-full">+</button>
        </td> -->
    </tr>
</template>

<script>
import authService from '../services/authService';

export default {
    props: {
        categories: {
            type: Array,
            required: true,
        },
    },
    data() {
        return {
            newTransaction: {
                timeOfTransaction: '',
                merchant: '',
                description: '',
                amount: 0,
                type: '',
                isPlanned: true, // Set default value to true
                plannedAmount: 0,
                categoryId: '', // New property to store selected category
                userId: '',
            },
        };
    },
    methods: {
        addTransaction() {
            const auth = authService.getCurrentUser();
            console.log('auth: ', auth);

            if (auth && auth.userId) {
                const transaction = {
                    ...this.newTransaction,
                    userId: auth.userId, // Include user ID in the payload
                };

                console.log('transaction to add: ', transaction);
                this.$emit('add-transaction', transaction);
                this.clearForm();
            } else {
                console.error('ERROR');
                console.error('User not authenticated');
            }
        },
        clearForm() {
            this.newTransaction = {
                timeOfTransaction: '',
                merchant: '',
                description: '',
                amount: 0,
                type: 'Regular',
                isPlanned: true,
                plannedAmount: 0,
                categoryId: '',
            };
        },
    },
};
</script>
