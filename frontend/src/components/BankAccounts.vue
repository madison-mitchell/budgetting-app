<template>
    <div class="max-w-7xl mx-auto p-12">
        <h2 class="text-2xl font-semibold text-gray-900 mb-6">Bank Accounts</h2>
        <div v-if="errorMessage" class="text-red-500">{{ errorMessage }}</div>
        <div v-else class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            <div v-for="account in bankAccounts" :key="account.id" class="bg-white shadow-md border border-grey-50 rounded-lg p-6 flex flex-col justify-between">
                <div>
                    <h3 class="text-lg font-semibold text-gray-800">{{ account.name }}</h3>
                    <p
                        class="text-lg font-semibold mt-4"
                        :class="{
                            'text-red-600': account.balance < 0,
                            'text-orange-500': account.balance >= 0 && account.balance < 50,
                            'text-sky-500': account.balance >= 50,
                        }">
                        {{ formatBalance(account.balance) }}
                    </p>
                </div>
                <div class="flex justify-between items-center mt-4">
                    <p class="text-sm text-gray-600">{{ account.bankName }}</p>
                    <p class="text-sm text-gray-600">**** {{ getLastFourDigits(account.accountNumber) }}</p>
                </div>
            </div>
            <!-- Add New Account Card -->
            <div
                class="bg-white shadow-md border border-grey-50 rounded-full p-6 w-24 m-10 flex flex-col justify-center items-center cursor-pointer text-gray-400 hover:text-sky-500"
                @click="showModal = true">
                <span class="text-4xl">+</span>
            </div>
        </div>

        <!-- Modal -->
        <AddAccountModal v-if="showModal" :show="showModal" @close="showModal = false" @add-account="handleAddAccount" />
    </div>
</template>

<script>
import axios from 'axios';
import authService from '../services/authService';
import AddAccountModal from './AddAccountModal.vue';

export default {
    components: {
        AddAccountModal,
    },
    data() {
        return {
            bankAccounts: [],
            showModal: false,
            errorMessage: '',
        };
    },
    mounted() {
        this.fetchBankAccounts();
    },
    methods: {
        fetchBankAccounts() {
            axios
                .get('http://localhost:8080/api/bank-accounts', {
                    headers: {
                        Authorization: 'Bearer ' + authService.getCurrentUser().jwt,
                    },
                })
                .then((response) => {
                    this.bankAccounts = response.data;
                })
                .catch((error) => {
                    this.errorMessage = 'Failed to fetch bank accounts.';
                    console.error(error);
                });
        },
        formatBalance(balance) {
            const formattedBalance = balance.toFixed(2).replace(/\d(?=(\d{3})+\.)/g, '$&,');
            return balance < 0 ? `-$${formattedBalance.slice(1)}` : `$${formattedBalance}`;
        },
        getLastFourDigits(accountNumber) {
            return accountNumber.slice(-4);
        },
        handleAddAccount(newAccount) {
            axios
                .post('http://localhost:8080/api/bank-accounts', newAccount, {
                    headers: {
                        Authorization: 'Bearer ' + authService.getCurrentUser().jwt,
                    },
                })
                .then((response) => {
                    this.bankAccounts.push(response.data);
                    this.showModal = false;
                })
                .catch((error) => {
                    this.errorMessage = 'Failed to add account.';
                    console.error(error);
                });
        },
    },
};
</script>

<style scoped>
/* Add any scoped styles here if needed */
</style>
