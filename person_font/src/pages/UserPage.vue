<template>
  <template v-if="user">
    <van-cell title="昵称" is-link to="/user/edit" :value="user.username"
              @click="doEdit('username','昵称',user.username)"/>
    <van-cell title="账号" is-link to="/user/edit" :value="user.userAccount"
              @click="doEdit('userAccount','账号',user.userAccount)"/>
    <van-cell title="头像" is-link to="/user/edit">
      <img :src="user.avatarUrl" width="50" height="50"/>
    </van-cell>
    <van-cell title="性别"  is-link to="/user/edit" :value="user.gender" @click="doEdit('gender','性别',user.gender)">
      {{user.gender==0?'男':user.gender==1?'女':'未知'}}
    </van-cell>


    <van-cell title="电话" is-link to="/user/edit" :value="user.phone" @click="doEdit('phone','电话',user.phone)"/>
    <van-cell title="邮箱" is-link to="/user/edit" :value="user.email" @click="doEdit('email','邮箱',user.email)"/>
    <van-cell title="星球编号" :value="user.planetCode"/>
    <van-cell title="创建时间" :value="user.createTime"/>
  </template>
</template>
<script setup lang="ts">
import {useRouter} from "vue-router";
import {onMounted, ref} from "vue";
import {getCurrentUser} from "../service/User.ts";


// const user = {
//   id: 1,
//   username: 'lack',
//   userAccount: 'lackAdmin',
//   avatarUrl: 'https://img1.baidu.com/it/u=1968668429,2104382916&fm=253&fmt=auto&app=138&f=JPEG?w=507&h=500',
//   gender: '男',
//   phone: '12345678914',
//   email: '206987598@qq.com',
//   createTime: new Date(),
//   planetCode: 16533,
// }
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