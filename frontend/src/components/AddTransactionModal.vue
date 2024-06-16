<template>
    <div class="fixed inset-0 flex items-center justify-center bg-gray-500 bg-opacity-75">
        <div class="bg-white p-6 rounded-lg">
            <h2 class="text-lg font-semibold mb-4">Add Transaction</h2>
            <form @submit.prevent="handleSubmit">
                <div class="mb-4">
                    <label for="description" class="block text-sm font-medium text-gray-700">Description</label>
                    <input
                        type="text"
                        id="description"
                        v-model="newTransaction.description"
                        required
                        class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-sky-500 focus:ring-sky-500 sm:text-sm" />
                </div>
                <div class="mb-4">
                    <label for="amount" class="block text-sm font-medium text-gray-700">Amount</label>
                    <input
                        type="number"
                        id="amount"
                        v-model="newTransaction.amount"
                        required
                        class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-sky-500 focus:ring-sky-500 sm:text-sm" />
                </div>
                <!-- Add other fields as necessary -->
                <div class="flex justify-end">
                    <button type="button" @click="$emit('close')" class="bg-gray-500 text-white px-4 py-2 rounded-md mr-2">Cancel</button>
                    <button type="submit" class="bg-sky-500 text-white px-4 py-2 rounded-md">Add</button>
                </div>
            </form>
        </div>
    </div>
</template>

<script>
export default {
    data() {
        return {
            newTransaction: {
                description: '',
                amount: 0,
                // Add other fields as necessary
            },
        };
    },
    methods: {
        handleSubmit() {
            this.$emit('add-transaction', this.newTransaction);
            this.newTransaction = {
                description: '',
                amount: 0,
                // Reset other fields as necessary
            };
        },
    },
};
</script>

<style scoped>
/* Add any scoped styles here if needed */
</style>
