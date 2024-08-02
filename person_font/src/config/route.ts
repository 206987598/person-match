import Index from "../pages/Index.vue";
import SearchPage from "../pages/SearchPage.vue";
import UserPage from "../pages/UserPage.vue";
import TeamPage from "../pages/TeamPage.vue";
import UserEdit from "../pages/UserEdit.vue";



const routes = [
    {path: '/', component: Index},
    {path: '/team', component: TeamPage},
    {path: '/user', component: UserPage},
    {path: '/search', component: SearchPage },
    {path: '/user/edit', component: UserEdit },
]
export default routes;