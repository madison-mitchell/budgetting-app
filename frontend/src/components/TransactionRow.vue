<template>
    <tr class="border-b hover:bg-gray-50 text-left text-sm">
        <td class="pl-4 py-4 whitespace-nowrap">{{ formatDate(transaction.timeOfTransaction) }}</td>
        <td class="pl-4 py-4 whitespace-nowrap">
            <span class="font-mono text-xs border border-1 border-gray-400 px-1 mr-2" :class="{ 'text-green-500 border-green-500': transaction.type === 'Income', 'text-gray-400': true }">{{
                transaction.type.substring(0, 1)
            }}</span>
            {{ transaction.merchant }}
        </td>
        <td class="pl-4 py-4 whitespace-nowrap">{{ transaction.description }}</td>
        <td class="pl-4 py-4 whitespace-nowrap" :class="{ 'text-green-600': transaction.amount > 0 }">{{ formatBalance(transaction.amount) }}</td>
        <td class="pl-4 py-4 whitespace-nowrap" :class="{ 'text-red-500': transaction.balance < 0 }">{{ formatBalance(transaction.balance) }}</td>
        <!-- <td class="pl-4 py-4 whitespace-nowrap">{{ transaction.type }}</td> -->
        <td class="pl-4 py-4 whitespace-nowrap text-center">
            <input type="checkbox" v-model="transaction.planned" @change="updateTransaction" class="form-checkbox h-4 w-5 text-green-600 transition duration-150 ease-in-out" />
        </td>
        <td class="pl-4 py-4 whitespace-nowrap">{{ formatBalance(transaction.plannedAmount) }}</td>
        <td class="px-4 py-4 whitespace-nowrap">{{ transaction.categoryId.childCategory.name }}</td>
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
        updateTransaction() {
            this.$emit('update-transaction', this.transaction);
        },
    },
};
</script>
