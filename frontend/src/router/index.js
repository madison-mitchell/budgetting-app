import { createRouter, createWebHistory } from 'vue-router';
import HomeView from '../views/HomeView.vue';
import Login from '../components/Login.vue';
import BankAccountsView from '../views/BankAccountsView.vue';
import TransactionsView from '../views/TransactionsView.vue';
import ExpensesView from '../views/ExpensesView.vue';
import DashboardView from '../views/DashboardView.vue';
import ProfileView from '../views/ProfileView.vue'; // Ensure ProfileView is imported
import authService from '../services/authService';

const routes = [
    { path: '/', name: 'Home', component: HomeView, meta: { requiresAuth: true } },
    { path: '/login', name: 'Login', component: Login },
    { path: '/dashboard', name: 'Dashboard', component: DashboardView, meta: { requiresAuth: true } },
    { path: '/bank-accounts', name: 'BankAccounts', component: BankAccountsView, meta: { requiresAuth: true } },
    { path: '/transactions', name: 'Transactions', component: TransactionsView, meta: { requiresAuth: true } },
    { path: '/expenses', name: 'Expenses', component: ExpensesView, meta: { requiresAuth: true } },
    { path: '/profile', name: 'Profile', component: ProfileView, meta: { requiresAuth: true } },
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
