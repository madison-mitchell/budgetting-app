<!-- src/components/dashboard/TransactionRow.vue -->
<template>
    <tr class="border-b border-gray-100 hover:bg-gray-50 text-left text-xs">
        <td class="pt-1 whitespace-nowrap text-center flex items-center justify-center">
            <!-- <input type="checkbox" class="h-3 w-3" /> -->
            <input type="checkbox" v-model="transaction.reviewed" class="h-3 w-3" />
        </td>
        <td class="tracking-tighter w-8 text-gray-400 pt-1 whitespace-nowrap">{{ formatDayWithOrdinal(transaction.timeOfTransaction) }}</td>
        <td class="pt-1 whitespace-nowrap">
            <span
                class="font-mono text-xxs border border-1 px-1 mr-2 rounded cursor-default"
                :class="{
                    'text-green-500 border-green-500': transaction.type === 'Income',
                    'text-gray-400 border-gray-400': transaction.type === 'Transfer',
                    'text-sky-400 border-sky-400': transaction.type === 'Expense',
                    // 'text-sky-400 border-sky-400': transaction.type === 'Regular',
                }"
                >{{ transaction.type.substring(0, 1) }}</span
            >
            {{ transaction.merchant }}
        </td>
        <td class="pt-1 whitespace-nowrap text-right" :class="{ 'text-green-600': transaction.amount > 0 }">{{ formatBalance(transaction.amount) }}</td>
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
        formatBalance(balance) {
            if (balance === null || balance === undefined) {
                return '$0.00';
            }
            const formattedBalance = balance.toFixed(2).replace(/\d(?=(\d{3})+\.)/g, '$&,');
            return balance < 0 ? `-$${formattedBalance.slice(1)}` : `$${formattedBalance}`;
        },
        formatDayWithOrdinal(dateTimeString) {
            const date = new Date(dateTimeString);
            const day = date.getDate();
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
};
</script>

<style scoped>
.text-xxs {
    font-size: 0.6rem;
}
</style>
