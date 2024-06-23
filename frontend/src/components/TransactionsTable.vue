<template>
    <div class="overflow-x-auto">
        <div class="mb-4">
            <label for="month-selector" class="block text-sm font-medium text-gray-700">Select Month</label>
            <select
                id="month-selector"
                v-model="selectedMonth"
                @change="filterTransactionsByMonth"
                class="mt-1 block w-full pl-3 pr-10 py-2 text-base border-gray-300 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm rounded-md">
                <option v-for="month in availableMonths" :key="month.value" :value="month.value">{{ month.text }}</option>
            </select>
        </div>
        <table class="min-w-full bg-white rounded-lg shadow overflow-hidden">
            <thead>
                <tr class="w-full bg-gray-100 border-b">
                    <th class="px-6 py-3 text-left text-sm font-medium text-gray-600 tracking-wider cursor-pointer" @click="sortBy('timeOfTransaction')">Date</th>
                    <th class="px-6 py-3 text-left text-sm font-medium text-gray-600 tracking-wider cursor-pointer" @click="sortBy('merchant')">Merchant</th>
                    <th class="px-6 py-3 text-left text-sm font-medium text-gray-600 tracking-wider cursor-pointer" @click="sortBy('description')">Description</th>
                    <th class="px-6 py-3 text-left text-sm font-medium text-gray-600 tracking-wider cursor-pointer" @click="sortBy('amount')">Amount</th>
                    <th class="px-6 py-3 text-left text-sm font-medium text-gray-600 tracking-wider cursor-pointer" @click="sortBy('balance')">Balance</th>
                    <th class="px-6 py-3 text-left text-sm font-medium text-gray-600 tracking-wider cursor-pointer" @click="sortBy('type')">Type</th>
                    <th class="px-6 py-3 text-left text-sm font-medium text-gray-600 tracking-wider cursor-pointer" @click="sortBy('isPlanned')">Planned</th>
                    <th class="px-6 py-3 text-left text-sm font-medium text-gray-600 tracking-wider cursor-pointer" @click="sortBy('plannedAmount')">Planned Amount</th>
                    <th class="px-6 py-3 text-left text-sm font-medium text-gray-600 tracking-wider cursor-pointer" @click="sortBy('category')">Category</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="transaction in filteredTransactions" :key="transaction.id" class="border-b hover:bg-gray-50 text-left text-sm">
                    <td class="px-6 py-4 whitespace-nowrap">{{ formatDate(transaction.timeOfTransaction) }}</td>
                    <td class="px-6 py-4 whitespace-nowrap">{{ transaction.merchant }}</td>
                    <td class="px-6 py-4 whitespace-nowrap">{{ transaction.description }}</td>
                    <td class="px-6 py-4 whitespace-nowrap" :class="{ 'text-green-600': transaction.amount > 0 }">{{ formatBalance(transaction.amount) }}</td>
                    <td class="px-6 py-4 whitespace-nowrap" :class="{ 'text-red-500': transaction.balance < 0 }">{{ formatBalance(transaction.balance) }}</td>
                    <td class="px-6 py-4 whitespace-nowrap">{{ transaction.type }}</td>
                    <td class="px-6 py-4 whitespace-nowrap text-center">
                        <input
                            type="checkbox"
                            v-model="transaction.planned"
                            @change="updateTransaction(transaction)"
                            class="form-checkbox h-4 w-5 text-green-600 transition duration-150 ease-in-out" />
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap">{{ formatBalance(transaction.plannedAmount) }}</td>
                    <td class="px-6 py-4 whitespace-nowrap">{{ transaction.categoryId.childCategory.name }}</td>
                </tr>
                <tr>
                    <td colspan="9">
                        <new-transaction-form :categories="categories" @add-transaction="addTransaction" />
                    </td>
                </tr>
            </tbody>
        </table>
    </div>
</template>

<script>
import { format, startOfMonth, endOfMonth, parseISO } from 'date-fns';
import NewTransactionForm from './NewTransactionForm.vue';

export default {
    components: {
        NewTransactionForm,
    },
    props: {
        transactions: {
            type: Array,
            required: true,
        },
        categories: {
            type: Array,
            required: true,
        },
    },
    data() {
        return {
            selectedMonth: '',
            availableMonths: [],
            filteredTransactions: [],
        };
    },
    watch: {
        transactions: {
            handler(newTransactions) {
                this.setupAvailableMonths();
                this.filterTransactionsByMonth();
            },
            deep: true,
        },
    },
    mounted() {
        this.setupAvailableMonths();
        this.filterTransactionsByMonth();
    },
    methods: {
        setupAvailableMonths() {
            const monthsSet = new Set();
            this.transactions.forEach((transaction) => {
                const transactionMonth = format(parseISO(transaction.timeOfTransaction), 'yyyy-MM');
                monthsSet.add(transactionMonth);
            });

            this.availableMonths = [...monthsSet]
                .sort()
                .reverse()
                .map((month) => {
                    return {
                        value: month,
                        text: format(parseISO(`${month}-01`), 'MMMM yyyy'),
                    };
                });

            if (!this.selectedMonth && this.availableMonths.length > 0) {
                this.selectedMonth = this.availableMonths[0].value;
            }
        },
        filterTransactionsByMonth() {
            const selectedMonth = this.selectedMonth;
            if (selectedMonth) {
                const start = startOfMonth(parseISO(`${selectedMonth}-01`));
                const end = endOfMonth(parseISO(`${selectedMonth}-01`));
                this.filteredTransactions = this.transactions.filter((transaction) => {
                    const transactionDate = parseISO(transaction.timeOfTransaction);
                    return transactionDate >= start && transactionDate <= end;
                });
            } else {
                this.filteredTransactions = this.transactions;
            }

            // Sort the filtered transactions in chronological order
            this.filteredTransactions.sort((a, b) => new Date(a.timeOfTransaction) - new Date(b.timeOfTransaction));

            // Calculate the balance
            this.calculateBalances();

            console.log('filteredTransactions: ', this.filteredTransactions);
        },
        formatDate(date) {
            const options = { year: 'numeric', month: 'short', day: 'numeric' };
            return new Date(date).toLocaleDateString(undefined, options);
        },
        formatBalance(balance) {
            if (balance === null || balance === undefined) {
                return '$0.00';
            }
            const formattedBalance = balance.toFixed(2).replace(/\d(?=(\d{3})+\.)/g, '$&,');
            return balance < 0 ? `-$${formattedBalance.slice(1)}` : `$${formattedBalance}`;
        },
        calculateBalances() {
            let balance = 0;
            this.filteredTransactions = this.filteredTransactions.map((transaction) => {
                balance += transaction.amount;
                return { ...transaction, balance };
            });
        },
        updateTransaction(transaction) {
            this.$emit('update-transaction', transaction);
        },
        addTransaction(newTransaction) {
            this.$emit('add-transaction', newTransaction);
        },
    },
};
</script>
