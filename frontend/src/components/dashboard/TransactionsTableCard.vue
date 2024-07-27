<!-- src/components/dashboard/TransactionsTableCard.vue -->
<template>
    <div class="flex flex-col h-full">
        <h3 class="text-lg font-semibold text-gray-800 mt-4">Transactions to Review</h3>
        <div class="p-6 h-full flex flex-col justify-evenly">
            <table v-if="sortedTransactions.length > 0" class="w-full">
                <TransactionRow v-for="transaction in sortedTransactions" :key="transaction.id" :transaction="transaction" />
            </table>
            <p v-else class="text-sm text-sky-500 font-medium italic mb-10">No transactions to review.</p>
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
