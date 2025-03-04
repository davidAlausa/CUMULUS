import { createApp } from 'vue';
import App from './App.vue';
import router from './router'; // Import the router
import './assets/global.css';
createApp(App)
    .use(router) // Add the router to the app
    .mount('#app');

