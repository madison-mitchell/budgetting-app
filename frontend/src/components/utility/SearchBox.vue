<template>
    <div class="flex items-center w-full max-w-md mx-auto mt-5 text-xs group">
        <input
            v-model="query"
            @input="onInput"
            type="text"
            placeholder="Search..."
            class="w-full px-4 py-2 bg-opacity-50 bg-white border border-gray-300 text-white placeholder-cyan-500 rounded-l-md focus:outline-none group-focus-within:ring group-focus-within:ring-blue-500 group-focus-within:border-blue-800" />
        <button
            @click="onSearch"
            class="px-4 py-2 bg-opacity-50 bg-white border border-gray-300 text-sky-600 rounded-r-md focus:outline-none group-focus-within:ring group-focus-within:ring-blue-500 group-focus-within:border-blue-800">
            <i class="fa-solid fa-magnifying-glass"></i>
        </button>
    </div>
</template>

<script>
export default {
    data() {
        return {
            query: '',
        };
    },
    methods: {
        onSearch() {
            this.$emit('search', this.query);
        },
        onInput() {
            this.$emit('input', this.query);
        },
    },
};
</script>

<style scoped>
button {
    border: none;
}
input {
    border: none;
}
</style>
