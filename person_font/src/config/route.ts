import Index from "../pages/Index.vue";
import SearchPage from "../pages/SearchPage.vue";
import UserPage from "../pages/UserPage.vue";
import TeamPage from "../pages/TeamPage.vue";
import UserEdit from "../pages/UserEdit.vue";
import SearchResultPage from "../pages/SearchResultPage.vue";
import UserLogin from "../pages/UserLogin.vue";
import UserAddTeam from "../pages/UserAddTeam.vue";
import UserUpdateTeam from "../pages/UserUpdateTeam.vue";
import UserCreateTeamPage from "../pages/UserCreateTeamPage.vue";
import UserJoinTeamPage from "../pages/UserJoinTeamPage.vue";
import UserUpdatePage from "../pages/UserUpdatePage.vue";


const routes = [
    {path: '/',component: Index},
    {path: '/team',title: '队伍', component: TeamPage},
    {path: '/user', title: '用户',component: UserPage},
    {path: '/search',title: '搜索', component: SearchPage},
    {path: '/user/edit',title: '编辑', component: UserEdit},
    {path: '/user/list',title: '搜索结果', component: SearchResultPage},
    {path: '/user/login', title: '登录',component: UserLogin},
    {path: '/team/add', title: '新建队伍',component: UserAddTeam},
    {path: '/team/update',title: '更新队伍', component: UserUpdateTeam},
    {path: '/user/create',title: '创建的队伍', component: UserCreateTeamPage},
    {path: '/user/join',title: '加入的队伍', component: UserJoinTeamPage},
    {path: '/user/update',title: '用户信息', component: UserUpdatePage},
]
export default routes;