<template>
    <div class="bg-white shadow-md border border-grey-50 rounded-lg p-4">
        <h2 class="text-lg font-semibold mb-4">Income</h2>
        <div class="mb-4">
            <div class="flex justify-between w-full items-center mb-4">
                <h2 class="text-sm text-left w-20" :class="{ 'text-green-500': totalIncomeEarned >= estimatedIncome }">{{ formatAmount(totalIncomeEarned) }}</h2>
                <div class="relative">
                    <div
                        class="overflow-hidden h-3 text-xs flex rounded-xl border w-32 mx-auto bg-sky-200"
                        :class="{ 'border-green-500': progressPercentage >= 100, 'border-sky-500': progressPercentage < 100 }">
                        <div
                            :style="{ width: progressPercentage + '%' }"
                            :class="{ 'bg-green-500': progressPercentage >= 100, 'bg-sky-500': progressPercentage < 100 }"
                            class="shadow-none flex flex-col text-center text-gray-800 whitespace-nowrap justify-center"></div>
                    </div>
                </div>
                <div class="relative flex items-center">
                    <input
                        :value="inputValue"
                        @input="onInput"
                        @blur="onBlur"
                        @focus="onFocus"
                        type="text"
                        placeholder="Enter estimated income"
                        class="border-none text-sm text-right w-20 rounded-md focus:outline-none focus:ring focus:ring-sky-500 focus:border-sky-800"
                        :class="{ 'text-green-500': totalIncomeEarned >= estimatedIncome }"
                        :style="{ width: inputWidth + 'px' }" />
                    <span ref="textWidthHelper" class="absolute invisible whitespace-nowrap">{{ inputValue }}</span>
                </div>
            </div>

            <table v-if="incomeTransactions.length > 0" class="min-w-full bg-white rounded-lg overflow-hidden">
                <tbody>
                    <tr v-for="transaction in incomeTransactions" :key="transaction.id" class="hover:bg-gray-50 rounded text-left text-xs flex justify-between py-1">
                        <td class="whitespace-nowrap">{{ formatDate(transaction.timeOfTransaction) }}</td>
                        <td class="w-40 whitespace-nowrap">
                            <span
                                class="font-mono text-xs border border-1 px-1 mr-2 rounded-md cursor-default"
                                :class="{ 'text-green-500 border-green-500': transaction.type === 'Income', 'text-sky-400 border-sky-400': transaction.type !== 'Income' }">
                                {{ transaction.type.substring(0, 1) }}
                            </span>
                            {{ transaction.merchant }}
                        </td>
                        <td class="whitespace-nowrap" :class="{ 'text-green-600': transaction.amount > 0 }">{{ formatAmount(transaction.amount) }}</td>
                    </tr>
                </tbody>
            </table>

            <p v-else class="text-green-700 font-medium italic mt-2">No transactions to review.</p>
        </div>
    </div>
</template>

<script>
import TransactionRow from '@/components/dashboard/TransactionRow.vue';

export default {
    components: {
        TransactionRow,
    },
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
            inputWidth: 0,
        };
    },
    computed: {
        totalIncomeEarned() {
            return this.incomeTransactions.reduce((total, transaction) => total + transaction.amount, 0);
        },
        progressPercentage() {
            if (this.localEstimatedIncome === 0) {
                return 0;
            }
            return Math.min((this.totalIncomeEarned / this.localEstimatedIncome) * 100, 100).toFixed(2);
        },
    },
    watch: {
        estimatedIncome(newVal) {
            this.localEstimatedIncome = newVal;
            this.inputValue = this.formatAmount(newVal);
            this.updateInputWidth();
        },
        inputValue() {
            this.$nextTick(this.updateInputWidth);
        },
    },
    mounted() {
        this.updateInputWidth();
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
        updateInputWidth() {
            if (this.$refs.textWidthHelper) {
                this.inputWidth = this.$refs.textWidthHelper.offsetWidth;
            }
        },
        formatDate(date) {
            const options = {
                month: 'short',
                day: 'numeric',
            };
            return new Date(date).toLocaleDateString(undefined, options);
        },
    },
};
</script>
