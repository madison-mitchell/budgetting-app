<template>
    <div class="flex min-h-full flex-col justify-center px-6 py-12 lg:px-8">
        <div class="sm:mx-auto sm:w-full sm:max-w-sm">
            <h2 class="mt-10 text-center text-2xl font-bold leading-9 tracking-tight text-gray-900">Sign in to your account</h2>
        </div>

        <div class="mt-10 sm:mx-auto sm:w-full sm:max-w-sm">
            <form class="space-y-6" @submit.prevent="handleLogin">
                <div>
                    <label for="username" class="flex text-sm font-medium leading-6 text-gray-900">Username</label>
                    <div class="mt-2">
                        <input
                            id="username"
                            name="username"
                            type="text"
                            v-model="username"
                            required
                            class="block w-full rounded-md border-0 p-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-sky-600 sm:text-sm sm:leading-6" />
                    </div>
                </div>

                <div>
                    <div class="flex items-center justify-between">
                        <label for="password" class="flex text-sm font-medium leading-6 text-gray-900">Password</label>
                        <div class="text-sm">
                            <a href="#" class="font-semibold text-sky-600 hover:text-sky-500">Forgot password?</a>
                        </div>
                    </div>
                    <div class="mt-2">
                        <input
                            id="password"
                            name="password"
                            type="password"
                            v-model="password"
                            autocomplete="current-password"
                            required
                            class="block w-full rounded-md border-0 p-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-sky-600 sm:text-sm sm:leading-6" />
                    </div>
                </div>

                <div>
                    <button
                        type="submit"
                        class="flex w-full justify-center rounded-md bg-sky-600 px-3 py-1.5 text-sm font-semibold leading-6 text-white shadow-sm hover:bg-sky-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-sky-600">
                        Sign in
                    </button>
                </div>
            </form>

            <p v-if="message">{{ message }}</p>
        </div>
    </div>
</template>

<script>
import authService from '@/services/authService';

export default {
    data() {
        return {
            username: '',
            password: '',
            message: '',
        };
    },
    methods: {
        handleLogin() {
            authService
                .login({ username: this.username, password: this.password })
                .then(() => {
                    this.$router.push('/dashboard'); // Redirect to Dashboard view
                })
                .catch(() => {
                    this.message = 'Failed to login. Please check your credentials.';
                });
        },
    },
};
</script>

<style scoped>
/* Add any scoped styles here if needed */
</style>
