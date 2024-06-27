<template>
    <div v-if="show" class="fixed inset-0 z-50 overflow-auto bg-smoke-light flex items-center justify-center text-sm">
        <div class="relative p-8 bg-white w-full max-w-4xl m-auto flex-col flex rounded-lg shadow-lg">
            <h2 class="text-2xl font-semibold mb-4">{{ isUpdate ? 'Update' : 'Add New' }} Transaction</h2>
            <form @submit.prevent="handleSubmit" class="grid grid-cols-4 gap-4">
                <label for="amount" class="block font-medium text-gray-700 text-left">Amount</label>
                <input
                    type="text"
                    v-model="formattedAmount"
                    @input="updateAmount"
                    class="block w-full rounded-md border border-gray-200 shadow-sm focus:border-indigo-500 focus:ring focus:ring-indigo-500 focus:ring-opacity-50 px-1"
                    placeholder="$0.00"
                    required />

                <label for="time_of_transaction" class="block font-medium text-gray-700 text-left">Date</label>
                <input
                    type="datetime-local"
                    v-model="transaction.time_of_transaction"
                    class="block w-full rounded-md border border-gray-200 shadow-sm focus:border-indigo-500 focus:ring focus:ring-indigo-500 focus:ring-opacity-50 px-1"
                    required />

                <label for="merchant" class="block font-medium text-gray-700 text-left">Merchant</label>
                <input
                    type="text"
                    v-model="transaction.merchant"
                    class="block w-full rounded-md border border-gray-200 shadow-sm focus:border-indigo-500 focus:ring focus:ring-indigo-500 focus:ring-opacity-50 px-1"
                    required />

                <label for="category_id" class="block font-medium text-gray-700 text-left">Category</label>
                <select
                    v-model="transaction.category_id"
                    @change="logCategory"
                    class="block w-full rounded-md border border-gray-200 shadow-sm focus:border-indigo-500 focus:ring focus:ring-indigo-500 focus:ring-opacity-50 px-1"
                    required>
                    <option v-for="category in sortedCategories" :key="category.id" :value="category.id">{{ category.childCategory.name }}</option>
                </select>

                <label for="recurring" class="block font-medium text-gray-700 text-left">Recurring</label>
                <input
                    type="checkbox"
                    v-model="transaction.recurring"
                    class="rounded border border-gray-200 shadow-sm focus:border-indigo-500 focus:ring focus:ring-indigo-500 focus:ring-opacity-50 px-1 w-4" />

                <label for="frequency" class="block font-medium text-gray-700 text-left">Frequency</label>
                <select
                    v-model="transaction.frequency"
                    class="block w-full rounded-md border border-gray-200 shadow-sm focus:border-indigo-500 focus:ring focus:ring-indigo-500 focus:ring-opacity-50 px-1">
                    <option value="Daily">Daily</option>
                    <option value="Weekly">Weekly</option>
                    <option value="Bi-Weekly">Bi-weekly</option>
                    <option value="Monthly">Monthly</option>
                    <option value="Yearly">Yearly</option>
                </select>

                <label for="is_planned" class="block font-medium text-gray-700 text-left">Is Planned</label>
                <input
                    type="checkbox"
                    v-model="transaction.is_planned"
                    class="rounded border border-gray-200 shadow-sm focus:border-indigo-500 focus:ring focus:ring-indigo-500 focus:ring-opacity-50 px-1 w-4" />

                <label for="planned_amount" class="block font-medium text-gray-700 text-left">Planned Amount</label>
                <input
                    type="number"
                    v-model="transaction.planned_amount"
                    step="0.01"
                    class="block w-full rounded-md border border-gray-200 shadow-sm focus:border-indigo-500 focus:ring focus:ring-indigo-500 focus:ring-opacity-50 px-1" />

                <label for="has_split" class="block font-medium text-gray-700 text-left">Has Split</label>
                <input
                    type="checkbox"
                    v-model="transaction.has_split"
                    class="rounded border border-gray-200 shadow-sm focus:border-indigo-500 focus:ring focus:ring-indigo-500 focus:ring-opacity-50 px-1 w-4" />

                <label for="description" class="block font-medium text-gray-700 text-left">Description</label>
                <input
                    type="text"
                    v-model="transaction.description"
                    class="block w-full rounded-md border border-gray-200 shadow-sm focus:border-indigo-500 focus:ring focus:ring-indigo-500 focus:ring-opacity-50 px-1"
                    required />

                <label for="reviewed" class="block font-medium text-gray-700 text-left">Reviewed</label>
                <input
                    type="checkbox"
                    v-model="transaction.reviewed"
                    class="rounded border border-gray-200 shadow-sm focus:border-indigo-500 focus:ring focus:ring-indigo-500 focus:ring-opacity-50 px-1 w-4" />

                <label for="notes" class="block font-medium text-gray-700 text-left">Notes</label>
                <input
                    type="text"
                    v-model="transaction.notes"
                    class="block w-full rounded-md border border-gray-200 shadow-sm focus:border-indigo-500 focus:ring focus:ring-indigo-500 focus:ring-opacity-50 px-1" />

                <label for="included" class="block font-medium text-gray-700 text-left">Included</label>
                <input
                    type="checkbox"
                    v-model="transaction.included"
                    class="rounded border border-gray-200 shadow-sm focus:border-indigo-500 focus:ring focus:ring-indigo-500 focus:ring-opacity-50 px-1 w-4" />

                <label for="type" class="block font-medium text-gray-700 text-left">Type</label>
                <select
                    v-model="transaction.type"
                    class="block w-full rounded-md border border-gray-200 shadow-sm focus:border-indigo-500 focus:ring focus:ring-indigo-500 focus:ring-opacity-50 px-1"
                    required>
                    <option value="Regular">Regular</option>
                    <option value="Income">Income</option>
                </select>

                <div class="col-span-4 flex justify-end mt-4">
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
            formattedAmount: '',
        };
    },
    computed: {
        isUpdate() {
            return this.transactionData && this.transactionData.id;
        },
        sortedCategories() {
            return this.categories.sort((a, b) => {
                const nameA = a.childCategory.name.toUpperCase();
                const nameB = b.childCategory.name.toUpperCase();
                if (nameA < nameB) {
                    return -1;
                }
                if (nameA > nameB) {
                    return 1;
                }
                return 0;
            });
        },
    },
    watch: {
        transactionData: {
            handler(newVal) {
                if (newVal) {
                    this.transaction = { ...newVal };
                    this.formattedAmount = this.formatAmount(this.transaction.amount);
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
            if (!this.transaction.category_id || !this.transaction.time_of_transaction) {
                console.error('All required fields must be filled out.');
                return;
            }

            const auth = authService.getCurrentUser();
            if (auth && auth.userId) {
                const localDate = new Date(this.transaction.time_of_transaction);
                const utcDate = new Date(localDate.getUTCFullYear(), localDate.getUTCMonth(), localDate.getUTCDate(), localDate.getUTCHours(), localDate.getUTCMinutes(), localDate.getUTCSeconds());

                const newTransaction = {
                    ...this.transaction,
                    amount: this.transaction.amount / 100, // Convert to actual amount
                    time_of_transaction: utcDate.toISOString(),
                    user_id: auth.userId,
                };

                console.log('New Transaction:', newTransaction); // Log the new transaction object
                this.$emit('save', newTransaction);
                this.close();
            } else {
                console.error('User not authenticated');
            }
        },
        updateAmount(event) {
            const value = event.target.value.replace(/\D/g, '');
            this.transaction.amount = parseInt(value, 10) || 0;
            this.formattedAmount = this.formatAmount(this.transaction.amount);
        },
        formatAmount(amount) {
            const stringAmount = amount.toString().padStart(3, '0');
            const dollars = stringAmount.slice(0, -2);
            const cents = stringAmount.slice(-2);
            return `$${dollars}.${cents}`;
        },
        logCategory(event) {
            console.log('Selected Category ID:', this.transaction.category_id);
        },
    },
};
</script>

<style scoped>
.bg-smoke-light {
    background-color: rgba(0, 0, 0, 0.5);
}
</style>
