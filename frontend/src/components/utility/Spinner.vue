<template>
    <transition name="fade" mode="out-in">
        <!-- <div v-if="visible" class="fixed inset-0 flex items-center justify-center bg-sky-500 bg-opacity-40 z-250"> -->
        <div v-if="visible" class="fixed inset-0 flex items-center justify-center">
            <div class="animate-spin rounded-full h-20 w-20 border-t-4 border-b-4 border-sky-500"></div>
        </div>
    </transition>
</template>

<script>
export default {
    props: {
        visible: {
            type: Boolean,
            default: false,
        },
    },
};
</script>

<style scoped>
.fade-enter-active,
.fade-leave-active {
    transition: opacity 0.5s ease;
}
.fade-enter-from,
.fade-leave-to {
    opacity: 0;
}
</style>
