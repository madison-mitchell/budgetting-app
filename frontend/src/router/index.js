import { createRouter, createWebHistory } from 'vue-router';
import Home from '../views/HomeView.vue';
import Login from '../components/Login.vue';
import BankAccounts from '../components/BankAccounts.vue';
import authService from '../services/authService';

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
        component: BankAccounts,
        meta: { requiresAuth: true },
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
