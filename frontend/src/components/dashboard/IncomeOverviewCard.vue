<template>
    <div class="bg-white shadow-md border border-grey-50 rounded-lg p-4">
        <h2 class="text-lg font-semibold mb-4">Income</h2>
        <div class="mb-4">
            <div class="flex w-full justify-center text-gray-400 text-xs">
                <h4 class="w-20 text-right">earned</h4>
                <h4 class="px-2">|</h4>
                <h4 class="w-20 text-left">estimated</h4>
            </div>
            <div class="flex justify-center w-full">
                <h2 class="text-2xl w-32 text-right" :class="{ 'text-green-500': totalIncomeEarned >= estimatedIncome }">{{ formatAmount(totalIncomeEarned) }}</h2>
                <h2 class="text-2xl px-2 text-gray-400" :class="{ 'text-green-500': totalIncomeEarned >= estimatedIncome }">|</h2>
                <input
                    :value="inputValue"
                    @input="onInput"
                    @blur="onBlur"
                    @focus="onFocus"
                    type="text"
                    placeholder="Enter estimated income"
                    class="border-none text-2xl text-left rounded-md focus:outline-none focus:ring focus:ring-sky-500 focus:border-sky-800 w-32"
                    :class="{ 'text-green-500': totalIncomeEarned >= estimatedIncome }" />
            </div>
        </div>
    </div>
</template>

<script>
export default {
    props: {
        incomeTransactions: {
            type: Array,
            required: true,
        },
        estimatedIncome: {
            type: Number,
            required: true,
        },
    },
    data() {
        return {
            localEstimatedIncome: this.estimatedIncome,
            inputValue: this.formatAmount(this.estimatedIncome),
        };
    },
    computed: {
        totalIncomeEarned() {
            return this.incomeTransactions.reduce((total, transaction) => total + transaction.amount, 0);
        },
    },
    watch: {
        estimatedIncome(newVal) {
            this.localEstimatedIncome = newVal;
            this.inputValue = this.formatAmount(newVal);
        },
    },
    methods: {
        formatAmount(amount) {
            const sign = amount < 0 ? '-' : '';
            const absoluteAmount = Math.abs(amount);
            const formattedAmount = new Intl.NumberFormat('en-US', {
                minimumFractionDigits: 2,
                maximumFractionDigits: 2,
            }).format(absoluteAmount);
            return `${sign}$${formattedAmount}`;
        },
        parseCurrency(value) {
            const sign = value.includes('-') ? -1 : 1;
            const number = parseFloat(value.replace(/[^0-9.]+/g, ''));
            return sign * (isNaN(number) ? 0 : number);
        },
        onInput(event) {
            const rawValue = this.parseCurrency(event.target.value);
            this.localEstimatedIncome = rawValue;
            this.inputValue = event.target.value; // Keep the current input
            this.$emit('update-estimated-income', rawValue);
        },
        onBlur(event) {
            event.target.value = this.formatAmount(this.localEstimatedIncome);
        },
        onFocus(event) {
            event.target.value = this.localEstimatedIncome.toFixed(2);
        },
    },
};
</script>
