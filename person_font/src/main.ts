import {createApp} from 'vue'
import App from './App.vue'
import {Button,NavBar,Icon,Tabbar,TabbarItem} from "vant";

const app = createApp(App)
app.use(Button).use(NavBar).use(Icon).use(Tabbar).use(TabbarItem)

createApp(App).mount('#app')
