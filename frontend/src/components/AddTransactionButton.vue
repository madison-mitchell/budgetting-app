<template>
    <div>
        <button
            class="bg-white shadow-md border border-grey-50 rounded-full p-6 w-24 m-10 flex flex-col justify-center items-center cursor-pointer text-gray-400 hover:text-sky-500"
            @click="showModal = true">
            <span class="text-4xl">+</span>
        </button>
        <AddTransactionModal v-if="showModal" @close="showModal = false" @add-transaction="handleAddTransaction" />
    </div>
</template>

<script>
import AddTransactionModal from './AddTransactionModal.vue';

export default {
    components: {
        AddTransactionModal,
    },
    data() {
        return {
            showModal: false,
        };
    },
    methods: {
        handleAddTransaction(newTransaction) {
            this.$emit('add-transaction', newTransaction);
            this.showModal = false;
        },
    },
};
</script>

<style scoped>
/* Add any scoped styles here if needed */
</style>
