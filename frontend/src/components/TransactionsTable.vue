<template>
    <div>
        <div class="mb-4 flex items-center">
            <label for="month-selector" class="text-sm font-medium text-gray-700 w-40">Time Period</label>
            <select
                id="month-selector"
                v-model="selectedMonth"
                @change="filterTransactionsByMonth"
                class="flex-grow border-gray-300 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm rounded-md">
                <option v-for="month in availableMonths" :key="month.value" :value="month.value">{{ month.text }}</option>
            </select>
        </div>

        <table class="min-w-full bg-white rounded-lg shadow overflow-hidden">
            <thead>
                <tr class="w-full bg-gray-100 border-b">
                    <th class="pl-4 py-3 text-left text-sm font-medium text-gray-600 tracking-wider cursor-pointer" @click="sortBy('timeOfTransaction')">Date</th>
                    <th class="pl-4 py-3 text-left text-sm font-medium text-gray-600 tracking-wider cursor-pointer" @click="sortBy('merchant')">Merchant</th>
                    <th class="pl-4 py-3 text-left text-sm font-medium text-gray-600 tracking-wider cursor-pointer" @click="sortBy('description')">Description</th>
                    <th class="pl-4 py-3 text-left text-sm font-medium text-gray-600 tracking-wider cursor-pointer" @click="sortBy('amount')">Amount</th>
                    <th class="pl-4 py-3 text-left text-sm font-medium text-gray-600 tracking-wider cursor-pointer" @click="sortBy('balance')">Balance</th>
                    <th class="pl-4 py-3 text-left text-sm font-medium text-gray-600 tracking-wider cursor-pointer" @click="sortBy('isPlanned')">Planned</th>
                    <th class="pl-4 py-3 text-left text-sm font-medium text-gray-600 tracking-wider cursor-pointer" @click="sortBy('plannedAmount')">Planned Amount</th>
                    <th class="px-4 py-3 text-left text-sm font-medium text-gray-600 tracking-wider cursor-pointer" @click="sortBy('category')">Category</th>
                </tr>
            </thead>
            <tbody>
                <TransactionRow v-for="transaction in filteredTransactions" :key="transaction.id" :transaction="transaction" @update-transaction="updateTransaction" />
                <NewTransactionForm :categories="categories" @add-transaction="addTransaction" />
            </tbody>
        </table>
    </div>
</template>

<script>
import { format, startOfMonth, endOfMonth, parseISO } from 'date-fns';
import TransactionRow from './TransactionRow.vue';
import NewTransactionForm from './NewTransactionForm.vue';

export default {
    components: {
        TransactionRow,
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
            sortField: '',
            sortOrder: 'asc',
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
    computed: {
        filteredTransactions() {
            const selectedMonth = this.selectedMonth;
            let filteredTransactions = this.transactions;

            if (selectedMonth) {
                const start = startOfMonth(parseISO(`${selectedMonth}-01`));
                const end = endOfMonth(parseISO(`${selectedMonth}-01`));
                filteredTransactions = this.transactions.filter((transaction) => {
                    const transactionDate = parseISO(transaction.timeOfTransaction);
                    return transactionDate >= start && transactionDate <= end;
                });
            }

            // Sort transactions by date in chronological order
            filteredTransactions.sort((a, b) => new Date(a.timeOfTransaction) - new Date(b.timeOfTransaction));

            // Calculate balances
            let balance = 0;
            filteredTransactions = filteredTransactions.map((transaction) => {
                balance += transaction.amount;
                return { ...transaction, balance };
            });

            return filteredTransactions;
        },
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
                this.sortedTransactions = this.transactions.filter((transaction) => {
                    const transactionDate = parseISO(transaction.timeOfTransaction);
                    return transactionDate >= start && transactionDate <= end;
                });
            } else {
                this.sortedTransactions = this.transactions;
            }
        },
        sortBy(field) {
            if (this.sortField === field) {
                this.sortOrder = this.sortOrder === 'asc' ? 'desc' : 'asc';
            } else {
                this.sortField = field;
                this.sortOrder = 'asc';
            }

            // Re-sort transactions by the selected field
            this.filteredTransactions.sort((a, b) => {
                let modifier = this.sortOrder === 'asc' ? 1 : -1;
                if (a[field] < b[field]) return -1 * modifier;
                if (a[field] > b[field]) return 1 * modifier;
                return 0;
            });

            // Re-calculate balances after sorting
            let balance = 0;
            this.filteredTransactions = this.filteredTransactions.map((transaction) => {
                balance += transaction.amount;
                return { ...transaction, balance };
            });
        },
        addTransaction(newTransaction) {
            this.$emit('add-transaction', newTransaction);
        },
        updateTransaction(updatedTransaction) {
            // Handle the update logic here
            this.$emit('update-transaction', updatedTransaction);
        },
    },
};
</script>
