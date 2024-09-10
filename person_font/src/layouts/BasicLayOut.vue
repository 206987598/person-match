<template id="basic-layout">
  <van-nav-bar
      :title="title"
      left-arrow
      @click-left="onClickLeft"
      @click-right="onClickRight">
    <template #right>
      <van-icon name="search" size="18"/>
    </template>
  </van-nav-bar>
  <div id="content" style="padding-bottom: 50px">
    <router-view/>
  </div>
  <van-tabbar route>
    <van-tabbar-item to="/" icon="home-o">主页</van-tabbar-item>
    <van-tabbar-item to="/team" icon="search">组队</van-tabbar-item>
    <van-tabbar-item to="/user" icon="friends-o">个人</van-tabbar-item>
  </van-tabbar>

</template>

<script setup lang="ts">

import {useRouter} from 'vue-router'
import {ref} from "vue";
import routes from "../config/route.ts";

const router = useRouter()

const DEFAULT_TITLE = '搭子匹配'
const title = ref(DEFAULT_TITLE)
/**
 * 在路由跳转之前执行的钩子函数，用于设置页面标题
 * 根据即将跳转的目标路径，查找对应的路由配置并设置页面标题
 *
 * @param {Object} to - 即将跳转的目标路由对象
 * @param {Object} from - 当前路由对象，即将离开的路由
 */
router.beforeEach((to) => {
  // 获取目标路由的路径
  const toPath = to.path;
  // 通过路径查找对应的路由配置
  const route = routes.find((route) => {
    return route.path === toPath;
  })
  // 设置页面标题为找到的路由配置中的title，如果没有则使用默认标题
  title.value = route?.title ?? DEFAULT_TITLE
})
const onClickLeft = () => {
  router.back()
  // router.push(('/'))
}
const onClickRight = () => {
  router.push(('/search'))
}

</script>
<style scoped>

</style>