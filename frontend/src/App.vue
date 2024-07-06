<template>
    <div id="app" class="flex">
        <LeftSidebar />
        <div class="flex-1 ml-60">
            <Navigation />
            <router-view />
        </div>
    </div>
</template>

<script>
import Navigation from '@/components/utility/Navigation.vue';
import LeftSidebar from '@/components/utility/LeftSidebar.vue';

export default {
    components: { Navigation, LeftSidebar },
    name: 'App',
};
</script>

<style>
#app {
    font-family: Avenir, Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    text-align: center;
    color: #2c3e50;
}

nav {
    padding: 30px;
}

nav a {
    font-weight: bold;
    color: #2c3e50;
}

nav a.router-link-exact-active {
    color: rgb(2 132 199);
}
</style>
