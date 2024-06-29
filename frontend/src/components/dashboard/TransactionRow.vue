<!-- src/components/dashboard/TransactionRow.vue -->
<template>
    <tr class="border-b hover:bg-gray-50 text-left text-xs">
        <td class="pl-4 py-4 whitespace-nowrap">{{ formatDate(transaction.timeOfTransaction) }}</td>
        <td class="pl-4 py-4 whitespace-nowrap">{{ transaction.merchant }}</td>
        <td class="pl-4 py-4 whitespace-nowrap" :class="{ 'text-green-600': transaction.amount > 0 }">{{ formatBalance(transaction.amount) }}</td>
        <td class="pl-4 py-4 whitespace-nowrap text-center">
            <input type="checkbox" v-model="transaction.reviewed" class="form-checkbox h-4 w-4 text-green-600 transition duration-150 ease-in-out" />
        </td>
    </tr>
</template>

<script>
export default {
    props: {
        transaction: {
            type: Object,
            required: true,
        },
    },
    methods: {
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
    },
};
</script>

<style scoped>
/* Add any scoped styles here if needed */
</style>
