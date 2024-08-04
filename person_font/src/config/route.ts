import Index from "../pages/Index.vue";
import SearchPage from "../pages/SearchPage.vue";
import UserPage from "../pages/UserPage.vue";
import TeamPage from "../pages/TeamPage.vue";
import UserEdit from "../pages/UserEdit.vue";
import SearchResultPage from "../pages/SearchResultPage.vue";
import userLogin from "../pages/UserLogin.vue";



const routes = [
    {path: '/', component: Index},
    {path: '/team', component: TeamPage},
    {path: '/user', component: UserPage},
    {path: '/search', component: SearchPage },
    {path: '/user/edit', component: UserEdit },
    {path: '/user/list', component: SearchResultPage },
    {path: '/login', component: userLogin },
]
export default routes;