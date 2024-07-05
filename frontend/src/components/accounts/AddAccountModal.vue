<template>
    <div v-if="show" class="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50">
        <div class="bg-white p-6 rounded-lg shadow-lg w-96">
            <h3 class="text-lg font-semibold text-gray-800 mb-4">Add New Account</h3>
            <div class="mb-4">
                <label class="block text-sm font-medium text-gray-700">Account Name</label>
                <input type="text" v-model="newAccount.name" class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-sky-500 focus:ring-sky-500 sm:text-sm" />
            </div>
            <div class="mb-4">
                <label class="block text-sm font-medium text-gray-700">Bank Name</label>
                <input type="text" v-model="newAccount.bank_name" class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-sky-500 focus:ring-sky-500 sm:text-sm" />
            </div>
            <div class="mb-4">
                <label class="block text-sm font-medium text-gray-700">Account Number</label>
                <input type="text" v-model="newAccount.accountNumber" class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-sky-500 focus:ring-sky-500 sm:text-sm" />
            </div>
            <div class="mb-4">
                <label class="block text-sm font-medium text-gray-700">Initial currentBalance</label>
                <input type="number" v-model="newAccount.currentBalance" class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-sky-500 focus:ring-sky-500 sm:text-sm" />
            </div>
            <div class="flex justify-end mt-4">
                <button @click="addAccount" class="bg-sky-600 text-white px-4 py-2 rounded-md shadow-sm hover:bg-sky-500 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-sky-500 mr-2">
                    Add Account
                </button>
                <button
                    @click="$emit('close')"
                    class="bg-gray-300 text-gray-700 px-4 py-2 rounded-md shadow-sm hover:bg-gray-400 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-gray-400">
                    Cancel
                </button>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    props: {
        show: {
            type: Boolean,
            required: true,
        },
    },
    data() {
        return {
            newAccount: {
                name: '',
                bank_name: '',
                accountNumber: '',
                currentBalance: 0,
            },
        };
    },
    methods: {
        addAccount() {
            this.$emit('add-account', this.newAccount);
            this.newAccount = {
                name: '',
                bank_name: '',
                accountNumber: '',
                currentBalance: 0,
            };
        },
    },
};
</script>

<style scoped>
/* Add any scoped styles here if needed */
</style>
