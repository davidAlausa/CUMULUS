import { createRouter, createWebHistory } from 'vue-router';


import HomePage from "@/components/HomePage.vue";
import TEST from "@/components/TEST.vue";
import SignUp from "@/components/SignUp.vue";
import ContactUs from "@/components/ContactUs.vue";
import SignIn from "@/components/SignIn.vue";
import ResetPassword from "@/components/ResetPassword.vue";
import UserDashboard from "@/components/UserDashboard.vue";
import AssessWorkstream from "@/components/AssessWorkstream.vue";
import ProcessWorkstream from "@/components/ProcessWorkstream.vue";
import AssessmentModule from "@/components/AssessmentModule.vue";
import FailedProcess from "@/components/FailedProcess.vue";


const routes = [
    { path: '/', component: HomePage },
    { path: '/test', component: TEST },
    { path: '/signup', component: SignUp },
    { path: '/signin', component: SignIn },
    { path: '/reset-password', component: ResetPassword },
    { path: '/contactus', component: ContactUs },
    { path: '/dashboard', component: UserDashboard},
    { path: '/assess-workstream', component: AssessWorkstream},
    { path: '/process-workstream', name: 'ProcessWorkstream', component: ProcessWorkstream },
    { path: '/assessment-module', name: 'AssessmentModule', component: AssessmentModule},
    { path: '/failed-process', name: 'FailedProcess', component: FailedProcess},
    { path: '/:pathMatch(.*)*', redirect: '/' },
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

export default router;
