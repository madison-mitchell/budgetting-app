<template>
    <div class="max-w-7xl mx-auto p-12">
        <h2 class="text-2xl font-semibold text-gray-900 mb-6">Bank Accounts</h2>
        <div v-if="errorMessage" class="text-red-500">{{ errorMessage }}</div>
        <div v-else class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            <AccountCard v-for="account in bankAccounts" :key="account.id" :account="account" />
            <!-- Add New Account Card -->
            <AddButton @click="showModal = true" />
        </div>

        <!-- Modal -->
        <AddItemModal v-if="showModal" :show="showModal" itemType="Account" :fields="accountFields" @close="showModal = false" @add-item="handleAddAccount" />
    </div>
</template>

<script>
import axios from 'axios';
import authService from '../services/authService';
import AccountCard from '../components/AccountCard.vue';
import AddButton from '../components/AddButton.vue';
import AddItemModal from '../components/AddItemModal.vue';

export default {
    components: {
        AccountCard,
        AddButton,
        AddItemModal,
    },
    data() {
        return {
            bankAccounts: [],
            showModal: false,
            errorMessage: '',
            accountFields: [
                { name: 'name', label: 'Name', type: 'text', required: true },
                { name: 'bankName', label: 'Bank Name', type: 'text', required: true },
                { name: 'accountNumber', label: 'Account Number', type: 'text', required: true },
                { name: 'balance', label: 'Balance', type: 'number', required: true },
                { name: 'accountType', label: 'Account Type', type: 'text', required: true },
                { name: 'paymentMethod', label: 'Payment Method', type: 'text', required: true },
            ],
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
        handleAddAccount(newAccount) {
            this.bankAccounts.push(newAccount);
            this.showModal = false;
        },
    },
};
</script>

<style scoped>
/* Add any scoped styles here if needed */
</style>
