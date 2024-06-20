<template>
    <div v-if="transaction" class="relative bg-white shadow-md hover:shadow-lg border border-grey-50 rounded-lg p-6 flex flex-col justify-between">
        <!-- Reviewed status icon -->
        <div class="absolute top-2 right-2" @click="toggleReviewed">
            <span v-if="transaction.reviewed === 'reviewed'" class="text-green-100 hover:text-green-500">
                <i class="fas fa-check-circle"></i>
            </span>
            <span v-else class="text-red-100 hover:text-red-500">
                <i class="fas fa-times-circle"></i>
            </span>
        </div>

        <div>
            <h3 class="text-lg font-semibold text-gray-800">{{ transaction.category.parentCategory.name }}</h3>
            <h3 class="text-lg font-semibold text-gray-800">{{ transaction.category.childCategory.name }}</h3>
            <h2 class="text-md font-semibold text-gray-500">{{ formatDate(transaction.timeOfTransaction) }}</h2>
            <h2 class="text-md font-semibold text-gray-500">{{ transaction.user.username }}</h2>
            <p
                class="text-lg font-semibold mt-4"
                :class="{
                    'text-red-600': transaction.amount < 0,
                    'text-orange-500': transaction.amount >= 0 && transaction.amount < 50,
                    'text-sky-500': transaction.amount >= 50,
                }">
                {{ formatAmount(transaction.amount) }}
            </p>
        </div>
        <div class="flex justify-between items-center mt-4">
            <p class="text-sm text-gray-600">{{ transaction.bankAccount?.bankName }}</p>
            <p class="text-sm text-gray-600">**** {{ getLastFourDigits(transaction.bankAccount?.accountNumber) }}</p>
        </div>
    </div>
</template>

<script>
import axios from 'axios';
import authService from '../services/authService';

export default {
    props: {
        transaction: {
            type: Object,
            required: true,
        },
    },
    methods: {
        formatAmount(amount) {
            if (amount === undefined || amount === null) {
                return '$0.00';
            }
            const formattedAmount = amount.toFixed(2).replace(/\d(?=(\d{3})+\.)/g, '$&,');
            return amount < 0 ? `-$${formattedAmount.slice(1)}` : `$${formattedAmount}`;
        },
        getLastFourDigits(accountNumber) {
            return accountNumber ? accountNumber.slice(-4) : '****';
        },
        toggleReviewed() {
            // Toggle the reviewed status locally
            this.transaction.reviewed = this.transaction.reviewed === 'reviewed' ? 'unreviewed' : 'reviewed';

            // Update the reviewed status on the backend
            axios
                .put(`http://localhost:8080/api/transactions/${this.transaction.id}`, this.transaction, {
                    headers: {
                        Authorization: 'Bearer ' + authService.getCurrentUser().jwt,
                    },
                })
                .then((response) => {
                    // Handle successful update if needed
                })
                .catch((error) => {
                    console.error('Failed to update transaction reviewed status', error);
                    // Optionally revert the status change if the update fails
                    this.transaction.reviewed = this.transaction.reviewed === 'reviewed' ? 'unreviewed' : 'reviewed';
                });
        },
        formatDate(datetime) {
            const date = new Date(datetime);
            return date.toLocaleDateString();
        },
    },
};
</script>

<style scoped>
/* Add any scoped styles here if needed */
</style>
