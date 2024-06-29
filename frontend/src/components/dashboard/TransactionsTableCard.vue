<!-- src/components/dashboard/TransactionsTableCard.vue -->
<template>
    <div>
        <h3 class="text-lg font-semibold text-gray-800 mb-4">Unreviewed Transactions</h3>
        <table class="min-w-full bg-white rounded-lg shadow overflow-hidden">
            <thead>
                <tr class="w-full bg-gray-100 border-b text-left text-xs font-medium text-gray-600 tracking-wider">
                    <th class="pl-4 py-3 cursor-pointer" @click="sortBy('timeOfTransaction')">Date</th>
                    <th class="pl-4 py-3 cursor-pointer" @click="sortBy('merchant')">Merchant</th>
                    <th class="pl-4 py-3 cursor-pointer" @click="sortBy('amount')">Amount</th>
                    <th class="pl-4 py-3">Reviewed</th>
                </tr>
            </thead>
            <tbody>
                <TransactionRow v-for="transaction in sortedTransactions" :key="transaction.id" :transaction="transaction" />
            </tbody>
        </table>
    </div>
</template>

<script>
import TransactionRow from '@/components/dashboard/TransactionRow.vue';

export default {
    components: {
        TransactionRow,
    },
    props: {
        transactions: {
            type: Array,
            required: true,
        },
    },
    data() {
        return {
            sortField: 'timeOfTransaction',
            sortOrder: 'asc',
        };
    },
    computed: {
        sortedTransactions() {
            let filteredTransactions = this.transactions.filter((transaction) => !transaction.reviewed);

            // Sort transactions by the selected field
            filteredTransactions.sort((a, b) => {
                let modifier = this.sortOrder === 'asc' ? 1 : -1;
                if (a[this.sortField] < b[this.sortField]) return -1 * modifier;
                if (a[this.sortField] > b[this.sortField]) return 1 * modifier;
                return 0;
            });

            return filteredTransactions;
        },
    },
    methods: {
        sortBy(field) {
            if (this.sortField === field) {
                this.sortOrder = this.sortOrder === 'asc' ? 'desc' : 'asc';
            } else {
                this.sortField = field;
                this.sortOrder = 'asc';
            }
        },
    },
};
</script>

<style scoped>
/* Add any scoped styles here if needed */
</style>
