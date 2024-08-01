import {createApp} from 'vue'
import App from './App.vue'
import Vant from "vant";
import "vant/lib/index.css"
import * as VueRouter from 'vue-router';
import routes from "./config/route.ts";


const app = createApp(App)
const Router = VueRouter.createRouter({
    history: VueRouter.createWebHashHistory(),
    routes,
})
app.use(Vant).use(Router)

app.mount('#app')
