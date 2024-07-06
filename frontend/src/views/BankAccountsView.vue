<template>
    <div class="relative max-w-7xl mx-auto p-12">
        <div class="flex justify-between items-center mb-6">
            <h2 class="text-2xl font-semibold text-gray-900">Bank Accounts</h2>
            <AddButton @click="showModal = true" />
        </div>
        <div v-if="errorMessage" class="text-red-500">{{ errorMessage }}</div>
        <div v-else class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            <AccountCard
                v-for="account in bankAccounts"
                :key="account.id"
                :account="account"
                @click="selectAccount(account)"
                :class="{ 'bg-gradient-to-br from-sky-50 to-sky-200': selectedAccount && selectedAccount.id === account.id, 'cursor-pointer': true }">
                <span class="text-blue-500 hover:underline cursor-pointer">View Details</span>
            </AccountCard>
        </div>

        <div v-if="selectedAccount" class="mt-6">
            <!-- <div v-if="loadingTransactions" class="text-center"></div> -->
            <Spinner :visible="loadingTransactions" />
            <div v-if="!loadingTransactions" class="mt-10">
                <div class="text-xl">{{ selectedAccount.bankName }}</div>
                <div class="grid grid-cols-1 md:grid-cols-2 gap-4 text-left border border-1 border-gray-300 rounded-lg shadow-md my-4 text-sm">
                    <div class="text-right text-gray-800 rounded-r-lg p-4">
                        <p>Name</p>
                        <p>Type</p>
                        <p>Account Number</p>
                        <p>Payment Method</p>
                        <p>Notes</p>
                    </div>
                    <div class="bg-gray-50 text-gray-800 p-4 rounded-r-lg">
                        <p>{{ selectedAccount.name }}</p>
                        <p>{{ selectedAccount.accountType }}</p>
                        <p>{{ selectedAccount.accountNumber }}</p>
                        <p>{{ selectedAccount.paymentMethod }}</p>
                        <p>{{ selectedAccount.notes }}</p>
                    </div>
                </div>

                <TransactionsTable
                    :transactions="filteredTransactions"
                    :categories="categories"
                    :selected-month="selectedMonth"
                    :available-months="availableMonths"
                    class="mt-10"
                    @add-transaction="handleAddTransaction"
                    @update-transaction="updateTransaction"
                    @change-month="handleMonthChange" />
            </div>
        </div>

        <AddItemModal v-if="showModal" :show="showModal" itemType="Account" :fields="accountFields" @close="showModal = false" @add-item="handleAddAccount" />
    </div>
</template>

<script>
import axios from 'axios';
import authService from '@/services/authService';
import AccountCard from '../components/accounts/AccountCard.vue';
import AddButton from '../components/AddButton.vue';
import AddItemModal from '../components/AddItemModal.vue';
import TransactionsTable from '../components/transactions/TransactionsTable.vue';
import NewTransactionModal from '../components/transactions/NewTransactionModal.vue';
import Spinner from '../components/utility/Spinner.vue';
import { format, parseISO, startOfMonth, endOfMonth } from 'date-fns';

export default {
    components: {
        AccountCard,
        AddButton,
        AddItemModal,
        TransactionsTable,
        NewTransactionModal,
        Spinner,
    },
    data() {
        return {
            bankAccounts: [],
            accountOrder: {},
            showModal: false,
            showTransactionModal: false,
            errorMessage: '',
            accountFields: [
                { name: 'name', label: 'Name', type: 'text', required: true },
                { name: 'bankName', label: 'Bank Name', type: 'text', required: true },
                { name: 'accountNumber', label: 'Account Number', type: 'text', required: true },
                { name: 'balance', label: 'Balance', type: 'number', required: true },
                { name: 'accountType', label: 'Account Type', type: 'text', required: true },
                { name: 'paymentMethod', label: 'Payment Method', type: 'text', required: true },
            ],
            selectedAccount: null,
            transactions: [],
            categories: [],
            loadingTransactions: false,
            selectedMonth: format(new Date(), 'yyyy-MM'),
            availableMonths: [],
        };
    },
    computed: {
        filteredTransactions() {
            return this.filterTransactionsByMonth();
        },
    },
    mounted() {
        console.log('------------------ BankAccountView.vue --------------------');
        this.fetchBankAccounts();
    },
    methods: {
        fetchBankAccounts() {
            axios
                .get('http://localhost:8080/api/bank-accounts', {
                    headers: {
                        Authorization: 'Bearer ' + authService.getCurrentUser().jwt,
                    },
                })
                .then((response) => {
                    // Sort the accounts by their ID and add an order property
                    this.bankAccounts = response.data
                        .sort((a, b) => a.id - b.id)
                        .map((account, index) => {
                            return { ...account, order: index + 1 };
                        });
                })
                .catch((error) => {
                    this.errorMessage = 'Failed to fetch bank accounts.';
                    console.error(error);
                });
        },
        handleAddAccount(newAccount) {
            this.bankAccounts.push(newAccount);
            this.showModal = false;
        },
        handleAddTransaction(newTransaction) {
            console.log('New transaction in BankAccountsView:', newTransaction);
            console.log('account_id: this.selectedAccount.id:', this.selectedAccount.id);

            const transaction = {
                ...newTransaction,
                accountId: this.selectedAccount.id,
                //bankName: this.selectedAccount.bankName,
            };

            console.log('Transaction in BankAccountsView:', transaction);

            axios
                .post('http://localhost:8080/api/transactions', transaction, {
                    headers: {
                        Authorization: 'Bearer ' + authService.getCurrentUser().jwt,
                    },
                })
                .then((response) => {
                    this.transactions.push(response.data);
                    this.setupAvailableMonths();
                    this.filterTransactionsByMonth();
                })
                .catch((error) => {
                    console.error('Failed to add transaction:', error);
                });
        },
        selectAccount(account) {
            this.selectedAccount = account;
            this.fetchTransactions(account.id);
            this.fetchCategories();
        },
        fetchTransactions(accountId) {
            this.loadingTransactions = true;
            axios
                .get(`http://localhost:8080/api/transactions/account/${accountId}`, {
                    headers: {
                        Authorization: 'Bearer ' + authService.getCurrentUser().jwt,
                    },
                })
                .then((response) => {
                    this.transactions = response.data;
                    this.setupAvailableMonths();
                })
                .catch((error) => {
                    console.error('Failed to fetch transactions:', error);
                    this.loadingTransactions = false;
                })
                .finally(() => {
                    setTimeout(() => {
                        this.loadingTransactions = false;
                    }, 500);
                });
        },
        fetchCategories() {
            axios
                .get('http://localhost:8080/api/categories/relation', {
                    headers: {
                        Authorization: 'Bearer ' + authService.getCurrentUser().jwt,
                    },
                })
                .then((response) => {
                    this.categories = response.data;
                })
                .catch((error) => {
                    console.error('Failed to fetch categories:', error);
                });
        },
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

            const currentMonth = format(new Date(), 'yyyy-MM');
            if (this.availableMonths.some((month) => month.value === currentMonth)) {
                this.selectedMonth = currentMonth;
            } else if (!this.selectedMonth && this.availableMonths.length > 0) {
                this.selectedMonth = this.availableMonths[0].value;
            }
        },
        handleMonthChange(newMonth) {
            this.selectedMonth = newMonth;
            this.filterTransactionsByMonth();
        },
        filterTransactionsByMonth() {
            const selectedMonth = this.selectedMonth;
            if (selectedMonth) {
                const start = startOfMonth(parseISO(`${selectedMonth}-01`));
                const end = endOfMonth(parseISO(`${selectedMonth}-01`));
                return this.transactions.filter((transaction) => {
                    try {
                        if (!transaction.timeOfTransaction) {
                            console.error(`Transaction missing timeOfTransaction: ${JSON.stringify(transaction)}`);
                            return false;
                        }

                        const transactionDate = parseISO(transaction.timeOfTransaction);
                        if (isNaN(transactionDate)) {
                            console.error(`Invalid date format for transaction: ${transaction.timeOfTransaction}`);
                            return false;
                        }

                        return transactionDate >= start && transactionDate <= end;
                    } catch (error) {
                        console.error(`Error parsing date for transaction: ${transaction.timeOfTransaction}`, error);
                        return false;
                    }
                });
            } else {
                return this.transactions;
            }
        },
        updateTransaction(updatedTransaction) {
            const index = this.transactions.findIndex((t) => t.id === updatedTransaction.id);
            if (index !== -1) {
                this.transactions.splice(index, 1, updatedTransaction);
                this.setupAvailableMonths();
            }
        },
    },
};
</script>

<style scoped>
/* Add any scoped styles here if needed */
</style>
