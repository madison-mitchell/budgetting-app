<template>
    <div
        class="relative shadow-md border border-grey-50 rounded-lg pb-6 flex flex-col justify-between hover:shadow-lg"
        :class="{ 'bg-gradient-to-br from-cyan-100 to-sky-200': expense.sharedExpense, 'bg-white': !expense.sharedExpense }">
        <div class="expense-header flex justify-between items-start w-full">
            <div class="pt-4 pl-6 pr-2 pb-2 text-left">
                <h3 class="text-lg font-semibold text-sky-500 uppercase">{{ expense.category.childCategory.name }}</h3>
                <p class="text-xs text-gray-600">{{ expense.merchant }} - {{ expense.frequency }}</p>
            </div>
            <div class="rounded-bl-lg rounded-tr-lg" :class="{ 'bg-white text-gray-600': expense.sharedExpense, 'text-sky-700 bg-gradient-to-br from-cyan-100 to-sky-200': !expense.sharedExpense }">
                <p class="pt-8 pr-6 pl-2 pb-2 w-14 text-sm">{{ formatDayWithOrdinal(expense.dueDate) }}</p>
            </div>
        </div>

        <div class="expense-details my-4 px-6">
            <div class="expense-description mb-4">
                <h3 class="text-sm text-gray-700">{{ expense.name }}</h3>
                <h3 v-if="expense.sharedExpense" class="text-xs text-gray-700">Shared Expense</h3>
                <h3 v-if="expense.sharedPartyName" class="text-xs text-gray-700">{{ expense.sharedPartyName }}</h3>
                <h3 class="text-sm text-gray-400">{{ expense.notes }}</h3>
                <!-- <h2 class="text-md font-semibold text-gray-500">{{ expense.user.username }}</h2> -->
            </div>

            <div v-if="expense.sharedExpense" class="expenses-shared-amount flex flex-col w-full justify-center">
                <h3 v-if="expense.sharedExpense" class="mt-4 text-sm text-left text-sky-600">MY SHARE:</h3>
                <div class="flex w-full justify-center">
                    <p class="text-lg font-semibold" :class="{ 'text-red-600': overSharedBudget, 'text-sky-500': underSharedBudget, 'text-emerald-500': meetsSharedBudget }">
                        {{ formatAmount(expense.myShare) }}
                    </p>
                    <p class="text-lg font-semibold px-2 text-gray-600">|</p>
                    <p class="text-lg font-semibold text-gray-600">{{ formatAmount(expense.myShareBudget) }}</p>
                </div>
            </div>

            <div class="expense-amount flex flex-col w-full justify-center" :class="{ 'text-lg': !expense.sharedExpense, 'text-sm': expense.sharedExpense }">
                <h3 v-if="expense.sharedExpense" class="mt-4 text-sm text-left text-sky-600">TOTAL:</h3>
                <div class="flex w-full justify-center">
                    <p
                        class="font-semibold"
                        :class="{
                            'text-red-600': overTotalBudget || underIncomeTotalBudget,
                            'text-sky-500': underTotalBudget || overIncomeTotalBudget,
                            'text-emerald-500': meetsTotalBudget,
                        }">
                        {{ formatAmount(expense.amount) }}
                    </p>
                    <p class="font-semibold px-2 text-gray-600">|</p>
                    <p class="font-semibold text-gray-600">{{ formatAmount(expense.totalBudget) }}</p>
                </div>
            </div>
        </div>

        <div class="expense-recurring absolute bottom-2 left-2">
            <span v-if="expense.recurring" :class="{ 'text-green-300 hover:text-green-500': !expense.sharedExpense, 'text-green-500 hover:text-green-700': expense.sharedExpense }">
                <i class="fa-solid fa-repeat" title="Recurring Expense"></i>
            </span>
            <span v-else :class="{ 'text-gray-300 hover:text-gray-500': !expense.sharedExpense, 'text-gray-500 hover:text-gray-700': expense.sharedExpense }">
                <i class="fa-solid fa-repeat" title="Not A Recurring Expense"></i>
            </span>
        </div>

        <div class="expense-paid absolute bottom-2 right-2" @click="togglePaid">
            <span v-if="expense.paid" :class="{ 'text-green-300 hover:text-green-500': !expense.sharedExpense, 'text-green-500 hover:text-green-700': expense.sharedExpense }">
                <i class="fas fa-check-circle" title="Paid Expense"></i>
            </span>
            <span v-else :class="{ 'text-red-100 hover:text-red-500': !expense.sharedExpense, 'text-red-500 hover:text-red-700': expense.sharedExpense }">
                <i class="fas fa-times-circle" title="Unpaid Expense"></i>
            </span>
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
        // budget: {
        //     type: Number,
        //     required: true,
        // },
    },
    computed: {
        positiveAmount() {
            this.expense.amount = this.expense.amount > 0 ? this.expense.amount : -this.expense.amount;
        },
        positiveTotalBudget() {
            this.expense.totalBudget = this.expense.totalBudget > 0 ? this.expense.totalBudget : -this.expense.totalBudget;
        },
        overTotalBudget() {
            if (this.expense.totalBudget < 0) {
                return this.expense.amount < this.expense.totalBudget;
            }
        },
        underTotalBudget() {
            if (this.expense.totalBudget < 0) {
                return this.expense.amount > this.expense.totalBudget;
            }
        },
        meetsTotalBudget() {
            return this.expense.amount === this.expense.totalBudget;
        },
        overSharedBudget() {
            if (this.expense.myShareBudget < 0) {
                return this.expense.myShare < this.expense.myShareBudget;
            }
        },
        underSharedBudget() {
            if (this.expense.myShareBudget < 0) {
                return this.expense.myShare > this.expense.myShareBudget;
            }
        },
        meetsSharedBudget() {
            return this.expense.myShare === this.expense.myShareBudget;
        },
        overIncomeTotalBudget() {
            if (this.expense.category.id === 1) {
                return this.expense.amount > this.expense.totalBudget;
            }
        },
        underIncomeTotalBudget() {
            if (this.expense.category.id === 1) {
                return this.expense.amount < this.expense.totalBudget;
            }
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
        formatDate(dateTimeString) {
            // Parse the date-time string into a Date object
            const date = new Date(dateTimeString);

            // Use Intl.DateTimeFormat to format the date
            const formatter = new Intl.DateTimeFormat('en-GB', {
                // year: 'numeric',
                // month: '2-digit',
                day: '2-digit',
            });

            return formatter.format(date);
        },
        formatDayWithOrdinal(dateTimeString) {
            // Parse the date-time string into a Date object
            const date = new Date(dateTimeString);

            // Get the day of the month
            const day = date.getDate();

            // Determine the ordinal suffix
            let suffix;
            if (day > 3 && day < 21) {
                suffix = 'th';
            } else {
                switch (day % 10) {
                    case 1:
                        suffix = 'st';
                        break;
                    case 2:
                        suffix = 'nd';
                        break;
                    case 3:
                        suffix = 'rd';
                        break;
                    default:
                        suffix = 'th';
                        break;
                }
            }

            return `${day}${suffix}`;
        },
    },
    mounted() {
        console.log('expense: ', this.expense);
    },
};
</script>

<style scoped></style>
