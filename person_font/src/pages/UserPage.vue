<template>
  <van-cell title="昵称" is-link to="/user/edit" :value="user.username" @click="doEdit('username','昵称',user.username)"/>
  <van-cell title="账号" is-link to="/user/edit" :value="user.userAccount" @click="doEdit('userAccount','账号',user.userAccount)"/>
  <van-cell title="头像" is-link to="/user/edit">
    <img :src="user.avatarUrl" width="50" height="50"/>
  </van-cell>
  <van-cell title="性别" is-link to="/user/edit" :value="user.gender" @click="doEdit('gender','性别',user.gender)"/>
  <van-cell title="电话" is-link to="/user/edit" :value="user.phone" @click="doEdit('phone','电话',user.phone)"/>
  <van-cell title="邮箱" is-link to="/user/edit" :value="user.email" @click="doEdit('email','邮箱',user.email)"/>
  <van-cell title="星球编号" :value="user.planetCode"/>
  <van-cell title="创建时间" :value="user.createTime.toLocaleDateString()"/>
</template>
<script setup lang="ts">
import {useRouter} from "vue-router";

const user = {
  id: 1,
  username: 'lack',
  userAccount: 'lackAdmin',
  avatarUrl: 'https://img1.baidu.com/it/u=1968668429,2104382916&fm=253&fmt=auto&app=138&f=JPEG?w=507&h=500',
  gender: '男',
  phone: '12345678914',
  email: '206987598@qq.com',
  createTime: new Date(),
  planetCode: 16533,
}
const Router = useRouter()
const doEdit = (editKey: string, editName: string, currentValue: string) => {
  Router.push({
    path: '/user/edit',
    query: {
      editKey,
      editName,
      currentValue
    }
  })
}
</script>
<style scoped>

</style>