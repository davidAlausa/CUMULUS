import { createRouter, createWebHistory } from 'vue-router';


import HomePage from "@/components/HomePage.vue";
import TEST from "@/components/TEST.vue";
import SignUp from "@/components/SignUp.vue";
import ContactUs from "@/components/ContactUs.vue";
import SignIn from "@/components/SignIn.vue";
import ResetPassword from "@/components/ResetPassword.vue";
import UserDashboard from "@/components/UserDashboard.vue";


const routes = [
    { path: '/', component: HomePage }, // Default route (Home page)
    { path: '/test', component: TEST }, // Placeholder route (Test page)
    { path: '/signup', component: SignUp },
    { path: '/signin', component: SignIn },
    { path: '/reset-password', component: ResetPassword },
    { path: '/contactus', component: ContactUs },
    { path: '/dashboard', component: UserDashboard},
    { path: '/:pathMatch(.*)*', redirect: '/' }, // Redirect all other routes to home
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

export default router;
