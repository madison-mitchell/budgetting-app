<!-- src/components/dashboard/AccountsOverviewCard.vue -->
<template>
    <div class="bg-white shadow-md border border-grey-50 rounded-lg p-6 flex flex-col justify-between">
        <div>
            <h3 class="text-lg font-semibold text-gray-800 mb-4">Bank Accounts</h3>

            <table class="table-auto w-full">
                <tbody>
                    <tr v-for="account in accounts" :key="account.id">
                        <td class="text-left px-4 w-full">
                            {{ account.bankName }}
                            <span class="text-gray-300 text-xs ml-2">**** {{ getLastFourDigits(account.accountNumber) }}</span>
                        </td>
                        <td :class="{ 'text-red-600': account.currentBalance < 0, 'text-right tracking-wide px-4': true }" class="text-right w-40">
                            {{ formatBalance(account.currentBalance) }}
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
        <h3 class="text-lg font-semibold mt-4">Total Balance: {{ formatBalance(totalBalance) }}</h3>
    </div>
</template>

<script>
export default {
    props: {
        accounts: {
            type: Array,
            required: true,
        },
        totalBalance: {
            type: Number,
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
        getLastFourDigits(accountNumber) {
            return accountNumber.slice(-4);
        },
    },
};
</script>

<style scoped>
/* Add any scoped styles here if needed */
</style>
