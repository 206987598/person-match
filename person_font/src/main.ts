import {createApp} from 'vue'
import App from './App.vue'
import Vant from "vant";
import "vant/lib/index.css"
import "./global.css"
import * as VueRouter from 'vue-router';
import routes from "./config/route.ts";
import { Toast } from 'vant';


const app = createApp(App)
const Router = VueRouter.createRouter({
    history: VueRouter.createWebHistory(),
    routes,
})
app.use(Vant).use(Router).use(Toast)

app.mount('#app')
