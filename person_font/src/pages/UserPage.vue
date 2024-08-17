<template>
  <template v-if="user">
    <div style="text-align: center;">
      <img :src="user.avatarUrl" width="100" height="100" style="border-radius: 45%"/>
    </div>
    <van-cell title="当前用户" :value="user?.username"/>
    <van-cell title="我的信息" is-link to="/user/update"/>
    <van-cell title="我加入的队伍" is-link to="/user/join"/>
    <van-cell title="我创建的队伍" is-link to="/user/create"/>
  </template>
  <van-button type="danger"  block @click="logOut" style="margin-bottom: 10px">退出登录</van-button>
</template>
<script setup lang="ts">
import {useRouter} from "vue-router";
import {onMounted, ref} from "vue";
import {getCurrentUser} from "../service/User.ts";
import MyAxios from "../plugins/myAxios";


// 定义一个响应式引用，用于存储当前用户信息
const user = ref()
/**
 * 在组件挂载后执行异步操作，获取当前用户信息
 * 使用async/await语法以处理异步获取的用户信息
 */
onMounted(async () => {
  user.value = await getCurrentUser()
})
// 获取路由实例，用于后续的路由跳转
const Router = useRouter()
/**
 * 编辑用户信息的函数
 * @param {string} editKey - 需要编辑的用户信息的键值
 * @param {string} editName - 需要编辑的用户信息的名称
 * @param {string} currentValue - 当前编辑字段的值
 * 路由跳转到编辑页面，并传递编辑信息作为查询参数
 */
const logOut = async () => {
  const res = await MyAxios.post("/user/logout");
  if (res.code === 0){
    Router.replace("/login");
  }
}
</script>
<style scoped>

</style>