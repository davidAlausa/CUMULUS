import { createRouter, createWebHistory } from 'vue-router';


import HomePage from "@/components/HomePage.vue";
import TEST from "@/components/TEST.vue";


const routes = [
    { path: '/', component: HomePage }, // Default route (Home page)
    { path: '/test', component: TEST }, // Placeholder route (Test page)
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

export default router;
