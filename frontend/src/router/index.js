import { createRouter, createWebHistory } from 'vue-router';
import Home from '../views/HomeView.vue';
import authService from '../services/authService';
import Login from '../components/Login.vue';
import BankAccountsView from '../views/BankAccountsView.vue';
import TransactionsView from '../views/TransactionsView.vue';
import ExpensesView from '../views/ExpensesView.vue';
import DashboardView from '../views/DashboardView.vue';

const routes = [
    {
        path: '/',
        component: Home,
        meta: { requiresAuth: true },
    },
    {
        path: '/login',
        component: Login,
    },
    {
        path: '/bank-accounts',
        name: 'bank-accounts',
        component: BankAccountsView,
        meta: { requiresAuth: true },
    },
    {
        path: '/transactions',
        name: 'Transactions',
        component: TransactionsView,
    },
    {
        path: '/expenses',
        name: 'Expenses',
        component: ExpensesView,
    },
    {
        path: '/dashboard',
        name: 'Dashboard',
        component: DashboardView,
    },
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

router.beforeEach((to, from, next) => {
    const requiresAuth = to.matched.some((record) => record.meta.requiresAuth);
    const currentUser = authService.getCurrentUser();

    if (requiresAuth && !currentUser) {
        next('/login');
    } else {
        next();
    }
});

export default router;
