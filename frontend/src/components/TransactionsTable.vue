<template>
    <div>
        <div class="mb-4 flex items-center justify-between">
            <div>
                <label for="month-selector" class="font-sans text-xs font-medium text-gray-800 w-40 mr-3">TIME PERIOD:</label>
                <select
                    id="month-selector"
                    v-model="selectedMonth"
                    @change="filterTransactionsByMonth"
                    class="font-sans flex-grow border-gray-300 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 text-gray-500 text-xs rounded-md">
                    <option v-for="month in availableMonths" :key="month.value" :value="month.value">{{ month.text }}</option>
                </select>
            </div>
            <button @click="openModal" class="bg-green-300 hover:bg-green-500 text-white text-sm font-bold py-2 px-3.5 rounded-full">+</button>
        </div>

        <table class="min-w-full bg-white rounded-lg shadow overflow-hidden">
            <thead>
                <tr class="w-full bg-gray-100 border-b text-left text-xs font-medium text-gray-600 tracking-wider">
                    <th class="pl-4 py-3 cursor-pointer" @click="sortBy('timeOfTransaction')">Date</th>
                    <th class="pl-4 py-3 cursor-pointer" @click="sortBy('merchant')">Merchant</th>
                    <th class="px-4 py-3 cursor-pointer text-gray-100" @click="sortBy('category')">Category</th>
                    <th class="pl-4 py-3 cursor-pointer" @click="sortBy('description')">Description</th>
                    <th class="pl-4 py-3 cursor-pointer" @click="sortBy('amount')">Amount</th>
                    <th class="pl-4 py-3 cursor-pointer" @click="sortBy('balance')">Balance</th>
                    <th class="pl-4 py-3 cursor-pointer" @click="sortBy('isPlanned')">Planned</th>
                    <th class="pl-4 py-3 cursor-pointer" @click="sortBy('plannedAmount')">Planned<br />Amount</th>
                </tr>
            </thead>
            <tbody>
                <TransactionRow v-for="transaction in filteredTransactions" :key="transaction.id" :transaction="transaction" @update-transaction="updateTransaction" />
            </tbody>
        </table>

        <NewTransactionModal v-if="showModal" :show="showModal" :categories="categories" @close="closeModal" @save="addTransaction" />
    </div>
</template>

<script>
import { format, startOfMonth, endOfMonth, parseISO } from 'date-fns';
import TransactionRow from './TransactionRow.vue';
import NewTransactionModal from './NewTransactionModal.vue';

export default {
    components: {
        TransactionRow,
        NewTransactionModal,
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
            showModal: false,
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
        openModal() {
            this.showModal = true;
        },
        closeModal() {
            this.showModal = false;
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
