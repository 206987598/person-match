import {createApp} from 'vue'
import App from './App.vue'
import {Button, NavBar, Icon, Tabbar, TabbarItem} from "vant";
import * as VueRouter from 'vue-router';
import routes from "./config/route.ts";


const app = createApp(App)
const Router = VueRouter.createRouter({
    history: VueRouter.createWebHashHistory(),
    routes,
})
app.use(Button).use(NavBar).use(Icon).use(Tabbar).use(TabbarItem).use(Router)

app.mount('#app')
