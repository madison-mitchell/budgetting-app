<template>
    <template v-if="transaction.hasSplit">
        <tr v-for="(split, index) in transaction.splits" :key="split.id" class="border-b hover:bg-gray-50 text-left text-xxs">
            <td class="pl-4 py-4 whitespace-nowrap">{{ formatDate(transaction.timeOfTransaction) }}</td>
            <td class="pl-4 py-4 whitespace-nowrap">
                <span
                    class="font-mono text-xs border border-1 px-1 mr-2 rounded-md cursor-default"
                    :class="{ 'text-green-500 border-green-500': transaction.type === 'Income', 'text-sky-400 border-sky-400': true }"
                    title="Transaction Type: Regular, Income, Transfer">
                    {{ transaction.type.substring(0, 1) }}
                </span>
                <span class="font-mono text-sky-500 text-xs border border-1 border-sky-500 px-1 mr-2 rounded-md" title="Split Transaction">S</span>
                {{ transaction.merchant }}
            </td>
            <td class="pl-4 py-4 whitespace-nowrap">{{ transaction.description }}</td>
            <td class="pl-4 py-4 whitespace-nowrap" :class="{ 'text-green-600': split.amount > 0 }">{{ formatBalance(split.amount) }}</td>
            <td class="pl-4 py-4 whitespace-nowrap" :class="{ 'text-red-500': transaction.accountBalance < 0 }">{{ formatBalance(calculateSplitBalance(index, transaction)) }}</td>
            <td class="pl-4 py-4 whitespace-nowrap text-center">
                <input type="checkbox" v-model="split.planned" @change="updateTransaction(split)" class="form-checkbox h-4 w-5 text-green-600 transition duration-150 ease-in-out" />
            </td>
            <td class="pl-4 py-4 whitespace-nowrap">{{ formatBalance(split.plannedAmount) }}</td>
            <td class="px-4 py-4 whitespace-nowrap flex items-center">
                <div :class="`${getCategoryBgColor(transaction.categoryId.childCategory.name)} px-2 py-0.5 rounded-xl`">
                    <i :class="`fa ${getCategoryIcon(split.categoryId.childCategory.name)} text-xxs`" class="mr-2"></i>
                    {{ split.categoryId.childCategory.name }}
                    <i class="fa-solid fa-circle text-xxxs text-sky-500 ml-2" title="Split Transaction"></i>
                </div>
            </td>
        </tr>
    </template>
    <tr v-else class="border-b hover:bg-gray-50 text-left text-xxs">
        <td class="pl-4 py-4 whitespace-nowrap">{{ formatDate(transaction.timeOfTransaction) }}</td>
        <td class="pl-4 py-4 whitespace-nowrap">
            <span
                class="font-mono text-xs border border-1 px-1 mr-2 rounded-md cursor-default"
                :class="{ 'text-green-500 border-green-500': true, 'text-sky-400 border-sky-400': transaction.type !== 'Income' }">
                {{ transaction.type.substring(0, 1) }}
            </span>
            {{ transaction.merchant }}
            <span class="text-gray-400 ml-2">{{ transaction.accountId.bankName }}</span>
        </td>
        <td class="px-4 py-4 whitespace-nowrap flex justify-end">
            <div :class="`${getCategoryBgColor(transaction.categoryId.childCategory.name)} px-2 py-0.5 rounded-xl`">
                <i :class="`fa ${getCategoryIcon(transaction.categoryId.childCategory.name)}  text-xxs`" class="mr-2" :title="transaction.categoryId.childCategory.name"></i>
                {{ transaction.categoryId.childCategory.name }}
            </div>
        </td>
        <td class="pl-4 py-4 whitespace-nowrap">{{ transaction.description }}</td>
        <td class="pl-4 py-4 whitespace-nowrap" :class="{ 'text-green-600': transaction.amount > 0 }">{{ formatBalance(transaction.amount) }}</td>
        <td class="pl-4 py-4 whitespace-nowrap" :class="{ 'text-red-500': transaction.accountBalance < 0 }">{{ formatBalance(transaction.accountBalance) }}</td>
        <td class="py-4 whitespace-nowrap flex justify-around">
            <input type="checkbox" v-model="transaction.planned" @change="updateTransaction(transaction)" class="form-checkbox h-4 w-5 text-green-600 transition duration-150 ease-in-out" />
        </td>
        <td class="pl-4 py-4 whitespace-nowrap">{{ formatBalance(transaction.plannedAmount) }}</td>
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
    emits: ['update-transaction'],
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
        calculateSplitBalance(index, transaction) {
            let cumulativeBalance = transaction.balance - transaction.amount;
            for (let i = 0; i <= index; i++) {
                cumulativeBalance += transaction.splits[i].amount;
            }
            return cumulativeBalance;
        },
        updateTransaction(row) {
            this.$emit('update-transaction', row);
        },
        getCategoryIcon(categoryName) {
            const categoryIcons = {
                // Dining
                Restaurants: 'fa-utensils text-indigo-600',
                Groceries: 'fa-shopping-basket text-indigo-600',
                'Fast Food': 'fa-hamburger text-indigo-600',
                // Education
                'Online Courses': 'fa-laptop text-amber-600',
                Books: 'fa-book text-amber-600',
                Tuition: 'fa-graduation-cap text-amber-600',
                Supplies: 'fa-pencil-alt text-amber-600',
                // Entertainment
                Hobbies: 'fa-paint-brush text-purple-600',
                Subscription: 'fa-newspaper text-purple-600',
                Concerts: 'fa-music text-purple-600',
                Movies: 'fa-film text-purple-600',
                Games: 'fa-gamepad text-purple-600',
                // Flight
                Materials: 'fa-box text-sky-600',
                'Flight Time': 'fa-plane text-sky-600',
                'Instructor Time': 'fa-chalkboard-teacher text-sky-600',
                // Healthcare
                Dental: 'fa-tooth text-cyan-600',
                'Medical Bills': 'fa-file-medical text-cyan-600',
                Prescriptions: 'fa-prescription-bottle-alt text-cyan-600',
                Vision: 'fa-eye text-cyan-600',
                'Health Insurance': 'fa-heartbeat text-cyan-600',
                // Housing
                Rent: 'fa-home text-orange-600',
                //Income
                Salary: 'fa-money-bill-wave text-green-600',
                // Miscellaneous
                Other: 'fa-ellipsis-h text-gray-600',
                Donations: 'fa-hand-holding-heart text-gray-600',
                // Personal Care
                Spa: 'fa-spa text-teal-600',
                Cosmetics: 'fa-spa text-teal-600',
                Haircuts: 'fa-cut text-teal-600',
                'Gym Membership': 'fa-dumbbell text-teal-600',
                // Shopping
                Clothing: 'fa-tshirt text-rose-600',
                Electronics: 'fa-tv text-rose-600',
                'Home Goods': 'fa-couch text-rose-600',
                Gifts: 'fa-gift text-rose-600',
                Toys: 'fa-puzzle-piece text-rose-600',
                // Split Transaction
                'Split Transaction': 'fa-random text-slate-600',
                // Transportation
                'Car Insurance': 'fa-shield-alt text-red-600',
                'Car Payment': 'fa-car text-red-600',
                Fuel: 'fa-gas-pump text-red-600',
                Repairs: 'fa-wrench text-red-600',
                'Public Transport': 'fa-bus text-red-600',
            };
            return categoryIcons[categoryName] || 'fa-circle'; // Default icon if category not found
        },
        getCategoryBgColor(categoryName) {
            const categoryBgColor = {
                // Dining
                Restaurants: 'text-indigo-700 bg-indigo-200',
                Groceries: 'text-indigo-700 bg-indigo-200',
                'Fast Food': 'text-indigo-700 bg-indigo-200',
                // Education
                'Online Courses': 'text-amber-700 bg-amber-200',
                Books: 'text-amber-700 bg-amber-200',
                Tuition: 'text-amber-700 bg-amber-200',
                Supplies: 'text-amber-700 bg-amber-200',
                // Entertainment
                Hobbies: 'text-purple-700 bg-purple-200',
                Subscription: 'text-purple-700 bg-purple-200',
                Concerts: 'text-purple-700 bg-purple-200',
                Movies: 'text-purple-700 bg-purple-200',
                Games: 'text-purple-700 bg-purple-200',
                // Flight
                Materials: 'text-sky-700 fa-box bg-sky-200',
                'Flight Time': 'text-sky-700 bg-sky-200',
                'Instructor Time': 'text-sky-700 bg-sky-200',
                // Healthcare
                Dental: 'text-cyan-700 bg-cyan-200',
                'Medical Bills': 'text-cyan-700 bg-cyan-200',
                Prescriptions: 'text-cyan-700 bg-cyan-200',
                Vision: 'text-cyan-700 bg-cyan-200',
                'Health Insurance': 'text-cyan-700 bg-cyan-200',
                // Housing
                Rent: 'text-orange-700 bg-orange-200',
                //Income
                Salary: 'text-green-700 bg-green-200',
                // Miscellaneous
                Other: 'text-gray-700 bg-gray-200',
                Donations: 'text-gray-700 bg-gray-200',
                // Personal Care
                Spa: 'text-teal-700 bg-teal-200',
                Cosmetics: 'text-teal-700 bg-teal-200',
                Haircuts: 'text-teal-700 bg-teal-200',
                'Gym Membership': 'text-teal-700 bg-teal-200',
                // Shopping
                Clothing: 'text-rose-700 bg-rose-200',
                Electronics: 'text-rose-700 bg-rose-200',
                'Home Goods': 'text-rose-700 bg-rose-200',
                Gifts: 'text-rose-700 bg-rose-200',
                Toys: 'text-rose-700 bg-rose-200',
                // Split Transaction
                'Split Transaction': 'text-slate-700 bg-slate-200',
                // Transportation
                'Car Insurance': 'text-red-700 bg-red-200',
                'Car Payment': 'text-red-700 bg-red-200',
                Fuel: 'text-red-700 bg-red-200',
                Repairs: 'text-red-700 bg-red-200',
                'Public Transport': 'text-red-700 bg-red-200',
            };
            return categoryBgColor[categoryName] || '';
        },
    },
};
</script>

<style scoped>
.text-xxxs {
    font-size: 4pt;
}

.text-xxs {
    font-size: 9pt;
}
</style>
