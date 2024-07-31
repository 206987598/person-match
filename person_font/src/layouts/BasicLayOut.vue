<template>
  <van-nav-bar
      title="标题"
      left-arrow
      @click-left="onClickLeft"
      @click-right="onClickRight">
    <template #right>
      <van-icon name="search" size="18"/>
    </template>
  </van-nav-bar>
  <div id="content">
    <router-view/>
  </div>
<!--  <router-link to="/">主页</router-link>-->
<!--  <router-link to="/team">队伍</router-link>-->
  <van-tabbar route>
    <van-tabbar-item to="/" icon="home-o" >主页</van-tabbar-item>
    <van-tabbar-item to="/team" icon="search" >组队</van-tabbar-item>
    <van-tabbar-item  to="/user"  icon="friends-o" >个人</van-tabbar-item>
  </van-tabbar>

</template>
<script setup lang="ts">
// import {ref} from 'vue';
// import {showToast} from 'vant';


const onClickLeft = () => alert('左')
const onClickRight = () => alert('右')
// const active = ref("index");
// const onChange = (index) => showToast(`标签 ${index}`);
</script>
<style scoped>

</style>