<template>
    <div class="relative bg-white shadow-md border border-grey-50 rounded-lg p-6 flex flex-col justify-between">
        <!-- Recurring status icon -->
        <div class="absolute top-2 left-2">
            <span v-if="expense.recurring" class="text-gray-300 hover:text-gray-500">
                <i class="fa-solid fa-repeat"></i>
            </span>
            <span v-else class="text-sky-300 hover:text-sky-500">
                <i class="fa-solid fa-repeat"></i>
            </span>
        </div>

        <div class="absolute top-2 right-2" @click="toggleReviewed">
            <span v-if="expense.reviewed === 'reviewed'" class="text-green-100 hover:text-green-500">
                <i class="fas fa-check-circle"></i>
            </span>
            <span v-else class="text-red-100 hover:text-red-500">
                <i class="fas fa-times-circle"></i>
            </span>
        </div>

        <div>
            <h3 class="text-lg font-semibold text-gray-800">{{ expense.description }}</h3>
            <h2 class="text-md font-semibold text-gray-500">{{ expense.user.username }}</h2>
            <p
                class="text-lg font-semibold mt-4"
                :class="{
                    'text-red-600': expense.amount < 0,
                    'text-orange-500': expense.amount >= 0 && expense.amount < 50,
                    'text-sky-500': expense.amount >= 50,
                }">
                {{ formatAmount(expense.amount) }}
            </p>
        </div>
        <div class="flex justify-between items-center mt-4">
            <p class="text-sm text-gray-600">{{ expense.categoryName }}</p>
            <p class="text-sm text-gray-600">{{ expense.dueDate }}</p>
        </div>
        <div class="flex justify-between items-center mt-4">
            <p class="text-sm text-gray-600">{{ expense.bankAccount.bankName }}</p>
            <p class="text-sm text-gray-600">**** {{ getLastFourDigits(expense.bankAccount.accountNumber) }}</p>
        </div>
    </div>
</template>

<script>
export default {
    props: {
        expense: {
            type: Object,
            required: true,
        },
    },
    methods: {
        formatAmount(amount) {
            const formattedAmount = amount.toFixed(2).replace(/\d(?=(\d{3})+\.)/g, '$&,');
            return amount < 0 ? `-$${formattedAmount.slice(1)}` : `$${formattedAmount}`;
        },
        getLastFourDigits(accountNumber) {
            return accountNumber ? accountNumber.slice(-4) : '****';
        },
    },
};
</script>

<style scoped>
/* Add any scoped styles here if needed */
</style>
