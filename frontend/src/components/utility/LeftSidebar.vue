<template>
    <div v-if="isAuthenticated" class="fixed h-full w-60 bg-gradient-to-br from-cyan-300 to-blue-500 text-gray-700 capitalize p-5 rounded-r-md flex flex-col">
        <SearchBox />
        <div class="flex-grow mt-10 overflow-auto">
            <div class="text-left">
                <h2>MY ACCOUNTS</h2>
                <div v-if="creditCards.length > 0">
                    <h3 class="mt-4 text-sm">Credit Cards</h3>
                    <div v-for="account in creditCards" :key="account.id" class="ml-2 text-xs flex justify-between">
                        <div>{{ account.name }}</div>
                        <div class="text-right">{{ formatBalance(account.currentBalance) }}</div>
                    </div>
                </div>
                <div v-if="depositories.length > 0">
                    <h3 class="mt-4 text-sm">Depository</h3>
                    <div v-for="account in depositories" :key="account.id" class="ml-2 text-xs flex justify-between">
                        <div>{{ account.name }}</div>
                        <div class="text-right">{{ formatBalance(account.currentBalance) }}</div>
                    </div>
                </div>
                <div v-if="investments.length > 0">
                    <h3 class="mt-4 text-sm">Investments</h3>
                    <!-- Add investment accounts here if needed -->
                </div>
                <div v-if="loans.length > 0">
                    <h3 class="mt-4 text-sm">Loans</h3>
                    <!-- Add loan accounts here if needed -->
                </div>
                <div v-if="realEstate.length > 0">
                    <h3 class="mt-4 text-sm">Real Estate</h3>
                    <!-- Add real estate accounts here if needed -->
                </div>
                <div v-if="other.length > 0">
                    <h3 class="mt-4 text-sm">Other</h3>
                    <!-- Add other accounts here if needed -->
                </div>
                <div class="mt-4 flex justify-between text-md">
                    <h3>Net Worth</h3>
                    <div>{{ formatBalance(netWorth) }}</div>
                </div>
            </div>
        </div>
        <div class="flex justify-evenly mt-4">
            <router-link v-if="!isAuthenticated" to="/login" class="px-4 py-2 hover:text-sky-300">LOGIN</router-link>
            <router-link v-else to="/profile" class="py-2 hover:text-sky-600">PROFILE</router-link>
            <button v-if="isAuthenticated" @click="logout" class="py-2 hover:text-sky-300">
                <i class="fa-solid fa-right-from-bracket"></i>
            </button>
        </div>
    </div>
</template>

<script>
import axios from 'axios';
import authService from '@/services/authService';
import SearchBox from '@/components/utility/SearchBox.vue';

export default {
    components: {
        SearchBox,
    },
    data() {
        return {
            isAuthenticated: !!authService.getCurrentUser(),
            bankAccounts: [],
            creditCards: [],
            depositories: [],
            investments: [],
            loans: [],
            realEstate: [],
            other: [],
        };
    },
    computed: {
        netWorth() {
            let netWorth = 0;
            this.depositories.forEach((account) => {
                netWorth += account.currentBalance;
            });

            this.creditCards.forEach((card) => {
                netWorth += -card.currentBalance;
            });
            return netWorth;
        },
    },
    methods: {
        logout() {
            authService.logout();
            this.isAuthenticated = false;
            this.$router.push('/login');
        },
        fetchBankAccounts() {
            axios
                .get('http://localhost:8080/api/bank-accounts', {
                    headers: {
                        Authorization: 'Bearer ' + authService.getCurrentUser().jwt,
                    },
                })
                .then((response) => {
                    // console.log('Bank Accounts: ', response.data);
                    // Sort the accounts by their ID and add an order property
                    this.bankAccounts = response.data
                        .sort((a, b) => a.id - b.id)
                        .map((account, index) => {
                            return { ...account, order: index + 1 };
                        });
                    this.filterAccountByType();
                })
                .catch((error) => {
                    this.errorMessage = 'Failed to fetch bank accounts.';
                    console.error(error);
                });
        },
        filterAccountByType() {
            this.creditCards = this.bankAccounts.filter((account) => account.accountType === 'Credit Card');
            // console.log('Credit Cards: ', this.creditCards);
            this.depositories = this.bankAccounts.filter((account) => account.accountType === 'Checking' || account.accountType === 'Savings');
            // console.log('Depositories: ', this.depositories);
        },
        formatBalance(balance) {
            if (balance === null || balance === undefined) {
                return '$0.00';
            }
            const numBalance = Number(balance);
            if (isNaN(numBalance)) {
                return '$0.00';
            }
            const formattedBalance = numBalance.toFixed(2).replace(/\d(?=(\d{3})+\.)/g, '$&,');
            return numBalance < 0 ? `-$${formattedBalance.slice(1)}` : `$${formattedBalance}`;
        },
    },
    mounted() {
        // console.log('------------------ LeftSidebar.vue --------------------');
        if (this.isAuthenticated) {
            this.fetchBankAccounts();
        }
    },
    watch: {
        $route() {
            this.isAuthenticated = !!authService.getCurrentUser();
            if (this.isAuthenticated) {
                this.fetchBankAccounts();
            }
        },
    },
};
</script>

<style scoped>
/* Optional: Add any additional scoped styles here if needed */
</style>
