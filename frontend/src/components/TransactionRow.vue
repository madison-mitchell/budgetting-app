<template>
    <template v-if="transaction.hasSplit">
        <tr v-for="(split, index) in transaction.splits" :key="split.id" class="border-b hover:bg-gray-50 text-left text-sm">
            <td class="pl-4 py-4 whitespace-nowrap">{{ formatDate(transaction.timeOfTransaction) }}</td>
            <td class="pl-4 py-4 whitespace-nowrap">
                <span
                    class="font-mono text-xs border border-1 border-gray-400 px-1 mr-2 rounded-md cursor-default"
                    :class="{ 'text-green-500 border-green-500': transaction.type === 'Income', 'text-gray-400': true }"
                    title="Transaction Type: Regular, Income, Transfer">
                    {{ transaction.type.substring(0, 1) }}
                </span>
                <span class="font-mono text-sky-500 text-xs border border-1 border-sky-500 px-1 mr-2 rounded-md" title="Split Transaction">S</span>
                {{ transaction.merchant }}
            </td>
            <td class="pl-4 py-4 whitespace-nowrap">{{ transaction.description }}</td>
            <td class="pl-4 py-4 whitespace-nowrap" :class="{ 'text-green-600': split.amount > 0 }">{{ formatBalance(split.amount) }}</td>
            <td class="pl-4 py-4 whitespace-nowrap" :class="{ 'text-red-500': transaction.balance < 0 }">{{ formatBalance(calculateSplitBalance(index, transaction)) }}</td>
            <td class="pl-4 py-4 whitespace-nowrap text-center">
                <input type="checkbox" v-model="split.planned" @change="updateTransaction(split)" class="form-checkbox h-4 w-5 text-green-600 transition duration-150 ease-in-out" />
            </td>
            <td class="pl-4 py-4 whitespace-nowrap">{{ formatBalance(split.plannedAmount) }}</td>
            <td class="px-4 py-4 whitespace-nowrap flex items-center">
                <i :class="`fa ${getCategoryIcon(split.categoryId.childCategory.name)} text-lg`" class="mr-2"></i>
                {{ split.categoryId.childCategory.name }}
                <i class="fa-solid fa-circle text-supersmall text-sky-500 ml-2" title="Split Transaction"></i>
            </td>
        </tr>
    </template>
    <tr v-else class="border-b hover:bg-gray-50 text-left text-sm">
        <td class="pl-4 py-4 whitespace-nowrap">{{ formatDate(transaction.timeOfTransaction) }}</td>
        <td class="pl-4 py-4 whitespace-nowrap">
            <span
                class="font-mono text-xs border border-1 border-gray-400 px-1 mr-2 rounded-md cursor-default"
                :class="{ 'text-green-500 border-green-500': transaction.type === 'Income', 'text-gray-400': true }">
                {{ transaction.type.substring(0, 1) }}
            </span>
            {{ transaction.merchant }}
        </td>
        <td class="pl-4 py-4 whitespace-nowrap">{{ transaction.description }}</td>
        <td class="pl-4 py-4 whitespace-nowrap" :class="{ 'text-green-600': transaction.amount > 0 }">{{ formatBalance(transaction.amount) }}</td>
        <td class="pl-4 py-4 whitespace-nowrap" :class="{ 'text-red-500': transaction.balance < 0 }">{{ formatBalance(transaction.balance) }}</td>
        <td class="pl-4 py-4 whitespace-nowrap text-center">
            <input type="checkbox" v-model="transaction.planned" @change="updateTransaction(transaction)" class="form-checkbox h-4 w-5 text-green-600 transition duration-150 ease-in-out" />
        </td>
        <td class="pl-4 py-4 whitespace-nowrap">{{ formatBalance(transaction.plannedAmount) }}</td>
        <td class="px-4 py-4 whitespace-nowrap flex items-center">
            <i :class="`fa ${getCategoryIcon(transaction.categoryId.childCategory.name)}  text-lg`" class="mr-2" :title="transaction.categoryId.childCategory.name"></i>
            {{ transaction.categoryId.childCategory.name }}
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
                Cosmetics: 'fa-makeup text-teal-600',
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
    },
};
</script>

<style scoped>
.text-supersmall {
    font-size: 4pt;
}
</style>
