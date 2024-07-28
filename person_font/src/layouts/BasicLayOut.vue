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
  <slot name="content">
    <div>这里写内容</div>

  </slot>
  <van-tabbar v-model="active" @change="onChange">
    <van-tabbar-item icon="home-o" name="index">主页</van-tabbar-item>
    <van-tabbar-item icon="search" name="team">组队</van-tabbar-item>
    <van-tabbar-item icon="friends-o" name="user">个人</van-tabbar-item>
  </van-tabbar>

</template>
<script setup lang="ts">
import {ref} from 'vue';
import {showToast} from 'vant';

const onClickLeft = () => alert('左')
const onClickRight = () => alert('右')
const active = ref("index");
const onChange = (index) => showToast(`标签 ${index}`);
</script>
<style scoped>

</style>