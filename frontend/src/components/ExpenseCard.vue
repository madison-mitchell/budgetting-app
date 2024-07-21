<template>
    <div class="relative bg-white shadow-md border border-grey-50 rounded-lg p-6 flex flex-col justify-between">
        <!-- Recurring status icon -->
        <div class="absolute top-2 left-2">
            <span v-if="expense.recurring" class="text-green-300 hover:text-green-500">
                <i class="fa-solid fa-repeat" title="Recurring Expense"></i>
            </span>
            <span v-else class="text-gray-300 hover:text-gray-500">
                <i class="fa-solid fa-repeat" title="Not A Recurring Expense"></i>
            </span>
        </div>

        <div class="absolute top-2 right-2" @click="togglePaid">
            <span v-if="expense.paid" class="text-green-100 hover:text-green-500">
                <i class="fas fa-check-circle" title="Paid Expense"></i>
            </span>
            <span v-else class="text-red-100 hover:text-red-500">
                <i class="fas fa-times-circle" title="Unpaid Expense"></i>
            </span>
        </div>

        <div>
            <h3 class="text-lg font-semibold text-gray-800">{{ expense.description }}</h3>
            <h2 class="text-md font-semibold text-gray-500">{{ expense.user.username }}</h2>
            <div class="flex w-full justify-center">
                <p
                    class="text-lg font-semibold mt-4"
                    :class="{
                        'text-red-600': expense.amount > -budget,
                        // 'text-orange-500': expense.amount >= 0 && expense.amount < 50,
                        'text-sky-500': expense.amount <= -budget,
                    }">
                    {{ formatAmount(expense.amount) }}
                </p>
                <p class="text-lg font-semibold mt-4 px-2 text-gray-600">/</p>
                <p class="text-lg font-semibold mt-4 text-gray-600">{{ formatAmount(budget) }}</p>
            </div>
        </div>
        <div class="flex justify-between items-center mt-4">
            <p class="text-sm text-gray-600">{{ expense.categoryName }}</p>
            <p class="text-sm text-gray-600">{{ expense.dueDate }}</p>
        </div>
        <div class="flex justify-between items-center mt-4">
            <p class="text-sm text-gray-600">{{ expense.accountId.bankName }}</p>
            <p class="text-sm text-gray-600">**** {{ getLastFourDigits(expense.accountId.accountNumber) }}</p>
        </div>
        <div class="flex justify-between items-center mt-4">
            <label class="text-sm text-gray-600 flex items-center">
                <input type="checkbox" v-model="expense.paid" @change="togglePaid" class="mr-2" />
                Paid
            </label>
            <p class="text-sm text-gray-600">{{ formatAmount(expense.paymentAmount) }}</p>
        </div>
        <div class="flex justify-between items-center mt-4">
            <!-- <p class="text-sm text-gray-600">Budget: {{ formatAmount(budget) }}</p> -->
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
        budget: {
            type: Number,
            required: true,
        },
    },
    methods: {
        formatAmount(amount) {
            if (amount === null || amount === undefined || isNaN(amount)) {
                return '$0.00';
            }
            const formattedAmount = Number(amount)
                .toFixed(2)
                .replace(/\d(?=(\d{3})+\.)/g, '$&,');
            return amount < 0 ? `-$${formattedAmount.slice(1)}` : `$${formattedAmount}`;
        },
        getLastFourDigits(accountNumber) {
            return accountNumber ? accountNumber.slice(-4) : '****';
        },
        togglePaid() {
            this.$emit('update-expense', {
                ...this.expense,
                paid: !this.expense.paid,
            });
        },
        togglePaid() {
            this.$emit('update-expense', {
                ...this.expense,
                paid: this.expense.paid,
            });
        },
    },
    mounted() {
        console.log('expense: ', this.expense);
    },
};
</script>

<style scoped></style>
