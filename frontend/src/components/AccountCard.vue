<template>
    <div class="bg-white shadow-md border border-grey-50 rounded-lg p-6 flex flex-col justify-between">
        <div>
            <h3 class="text-lg font-semibold text-gray-800">{{ account.name }}</h3>
            <p
                class="text-lg font-semibold mt-4"
                :class="{
                    'text-red-600': account.balance < 0,
                    'text-orange-500': account.balance >= 0 && account.balance < 50,
                    'text-sky-500': account.balance >= 50,
                }">
                {{ formatBalance(account.balance) }}
            </p>
        </div>
        <div class="flex justify-between items-center mt-4">
            <p class="text-sm text-gray-600">{{ account.bankName }}</p>
            <p class="text-sm text-gray-600">**** {{ getLastFourDigits(account.accountNumber) }}</p>
        </div>
    </div>
</template>

<script>
export default {
    name: 'AccountCard',
    props: {
        account: {
            type: Object,
            required: true,
        },
    },
    methods: {
        formatBalance(balance) {
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
