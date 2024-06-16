<template>
    <div id="app">
        <navigation />
        <router-view />
    </div>
</template>

<script>
import Navigation from './components/Navigation.vue';
export default {
    components: { Navigation },
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
    color: rgb(14 165 233);
}
</style>
