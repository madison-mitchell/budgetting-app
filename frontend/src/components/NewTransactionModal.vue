<template>
    <div v-if="show" class="fixed inset-0 z-50 overflow-auto bg-smoke-light flex items-center justify-center text-sm">
        <div class="relative p-8 bg-white w-full max-w-2xl m-auto flex-col flex rounded-lg shadow-lg">
            <h2 class="text-2xl font-semibold mb-4">{{ isUpdate ? 'Update' : 'Add New' }} Transaction</h2>
            <form @submit.prevent="handleSubmit" class="grid grid-cols-2 gap-4">
                <div>
                    <label for="amount" class="block font-medium text-gray-700">Amount</label>
                    <input
                        type="number"
                        v-model="transaction.amount"
                        step="0.01"
                        class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring focus:ring-indigo-500 focus:ring-opacity-50"
                        required />
                </div>
                <div>
                    <label for="category_id" class="block font-medium text-gray-700">Category</label>
                    <select
                        v-model="transaction.category_id"
                        class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring focus:ring-indigo-500 focus:ring-opacity-50"
                        required>
                        <option v-for="category in categories" :key="category.id" :value="category.id">{{ category.childCategory.name }}</option>
                    </select>
                </div>
                <div>
                    <label for="description" class="block font-medium text-gray-700">Description</label>
                    <input
                        type="text"
                        v-model="transaction.description"
                        class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring focus:ring-indigo-500 focus:ring-opacity-50"
                        required />
                </div>
                <div>
                    <label for="time_of_transaction" class="block font-medium text-gray-700">Date</label>
                    <input
                        type="datetime-local"
                        v-model="transaction.time_of_transaction"
                        class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring focus:ring-indigo-500 focus:ring-opacity-50"
                        required />
                </div>
                <div>
                    <label for="notes" class="block font-medium text-gray-700">Notes</label>
                    <input
                        type="text"
                        v-model="transaction.notes"
                        class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring focus:ring-indigo-500 focus:ring-opacity-50" />
                </div>
                <div class="flex items-center mt-4 mx-auto">
                    <label for="recurring" class="block font-medium text-gray-700">Recurring</label>
                    <input
                        type="checkbox"
                        v-model="transaction.recurring"
                        class="ml-2 mt-1 rounded border-gray-300 shadow-sm focus:border-indigo-500 focus:ring focus:ring-indigo-500 focus:ring-opacity-50" />
                </div>
                <div>
                    <label for="merchant" class="block font-medium text-gray-700">Merchant</label>
                    <input
                        type="text"
                        v-model="transaction.merchant"
                        class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring focus:ring-indigo-500 focus:ring-opacity-50"
                        required />
                </div>
                <div class="flex items-center mt-4 mx-auto">
                    <label for="included" class="block font-medium text-gray-700">Included</label>
                    <input
                        type="checkbox"
                        v-model="transaction.included"
                        class="ml-2 mt-1 rounded border-gray-300 shadow-sm focus:border-indigo-500 focus:ring focus:ring-indigo-500 focus:ring-opacity-50" />
                </div>
                <div>
                    <label for="frequency" class="block font-medium text-gray-700">Frequency</label>
                    <input
                        type="text"
                        v-model="transaction.frequency"
                        class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring focus:ring-indigo-500 focus:ring-opacity-50" />
                </div>
                <div class="flex items-center mt-4 mx-auto">
                    <label for="reviewed" class="block font-medium text-gray-700">Reviewed</label>
                    <input
                        type="checkbox"
                        v-model="transaction.reviewed"
                        class="ml-2 mt-1 rounded border-gray-300 shadow-sm focus:border-indigo-500 focus:ring focus:ring-indigo-500 focus:ring-opacity-50" />
                </div>
                <div>
                    <label for="type" class="block font-medium text-gray-700">Type</label>
                    <select
                        v-model="transaction.type"
                        class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring focus:ring-indigo-500 focus:ring-opacity-50"
                        required>
                        <option value="Regular">Regular</option>
                        <option value="Income">Income</option>
                    </select>
                </div>
                <div class="flex items-center mt-4 mx-auto">
                    <label for="is_planned" class="block font-medium text-gray-700">Is Planned</label>
                    <input
                        type="checkbox"
                        v-model="transaction.is_planned"
                        class="ml-2 mt-1 rounded border-gray-300 shadow-sm focus:border-indigo-500 focus:ring focus:ring-indigo-500 focus:ring-opacity-50" />
                </div>
                <div>
                    <label for="planned_amount" class="block font-medium text-gray-700">Planned Amount</label>
                    <input
                        type="number"
                        v-model="transaction.planned_amount"
                        step="0.01"
                        class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring focus:ring-indigo-500 focus:ring-opacity-50" />
                </div>
                <div class="flex items-center mt-4 mx-auto">
                    <label for="has_split" class="block font-medium text-gray-700">Has Split</label>
                    <input
                        type="checkbox"
                        v-model="transaction.has_split"
                        class="ml-2 mt-1 rounded border-gray-300 shadow-sm focus:border-indigo-500 focus:ring focus:ring-indigo-500 focus:ring-opacity-50" />
                </div>
                <div>
                    <label for="account_balance" class="block font-medium text-gray-700">Account Balance</label>
                    <input
                        type="number"
                        v-model="transaction.account_balance"
                        step="0.01"
                        class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring focus:ring-indigo-500 focus:ring-opacity-50" />
                </div>
                <div class="col-span-2 flex justify-end mt-4">
                    <button
                        type="button"
                        @click="close"
                        class="mr-2 bg-gray-500 hover:bg-gray-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:ring focus:ring-gray-300 focus:ring-opacity-50">
                        Cancel
                    </button>
                    <button type="submit" class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:ring focus:ring-blue-300 focus:ring-opacity-50">
                        Save
                    </button>
                </div>
            </form>
        </div>
    </div>
</template>

<script>
import authService from '../services/authService';

export default {
    props: {
        show: Boolean,
        categories: Array,
        transactionData: Object,
    },
    data() {
        return {
            transaction: {
                id: null,
                account_id: '',
                amount: 0,
                category_id: '',
                description: '',
                time_of_transaction: '',
                notes: '',
                merchant: '',
                recurring: false,
                frequency: '',
                included: true,
                reviewed: false, // Default to false for boolean field
                type: 'Debit',
                user_id: '',
                is_planned: false,
                planned_amount: 0.0,
                has_split: false,
                account_balance: 0.0,
            },
        };
    },
    computed: {
        isUpdate() {
            return this.transactionData && this.transactionData.id;
        },
    },
    watch: {
        transactionData: {
            handler(newVal) {
                if (newVal) {
                    this.transaction = { ...newVal };
                }
            },
            immediate: true,
            deep: true,
        },
    },
    methods: {
        close() {
            this.$emit('close');
        },
        handleSubmit() {
            const auth = authService.getCurrentUser();
            if (auth && auth.userId) {
                const localDate = new Date(this.transaction.time_of_transaction);
                const utcDate = new Date(localDate.getUTCFullYear(), localDate.getUTCMonth(), localDate.getUTCDate(), localDate.getUTCHours(), localDate.getUTCMinutes(), localDate.getUTCSeconds());

                const newTransaction = {
                    ...this.transaction,
                    time_of_transaction: utcDate.toISOString(),
                    user_id: auth.userId,
                };
                this.$emit('save', newTransaction);
                this.close();
            } else {
                console.error('User not authenticated');
            }
        },
    },
};
</script>

<style scoped>
.bg-smoke-light {
    background-color: rgba(0, 0, 0, 0.5);
}
</style>
