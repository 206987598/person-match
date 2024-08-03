<template>
  <van-card v-for="user in userList"
            :title="user.username"
            :desc="user.profile"
            :thumb="user.avatarUrl"
  >
    <template #tags>
      <van-tag plain type="primary" v-for="(tag,index) in user.tags" style="margin-right: 5px;margin-top: 10px">{{ tag }}</van-tag>
    </template>
    <template #footer>
      <van-button size="mini">联系我</van-button>
    </template>
  </van-card>

</template>
<script setup>
import {useRoute} from "vue-router";
import {ref} from "vue";

const route = useRoute()
const {tags} = route.query
const textUser = {
  id: 1,
  username: 'lack',
  userAccount: 'lackAdmin',
  avatarUrl: 'https://tse1-mm.cn.bing.net/th/id/OIP-C.tDbFzIxORkvKYnfklqqA6QHaHa?w=222&h=220&c=7&r=0&o=5&dpr=1.3&pid=1.7',
  profile: '一个正在努力成为全栈开发的菜鸟程序员',
  tags: ['JAVA', '男', '前端', '后端'],
}
const userList = ref([textUser])
</script>

<style scoped>

</style>