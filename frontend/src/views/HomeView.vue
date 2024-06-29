<template>
    <div>
        <login />
    </div>
</template>

<script>
import Login from '@/components/Login.vue';
import authService from '@/services/authService';

export default {
    components: { Login },
    name: 'HomeView',
};
</script>
