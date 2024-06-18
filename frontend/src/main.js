import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import store from './store';
import './assets/tailwind.css';
import '@fortawesome/fontawesome-free/css/all.css';
import { VueMasonryPlugin } from 'vue-masonry-css';

const app = createApp(App);

app.use(store);
app.use(router);
app.use(VueMasonryPlugin);

app.mount('#app');
