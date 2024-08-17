<template>
  <van-cell center :title="title">
    <template #right-icon>
      <van-switch v-model="isMatchMode" :loading="loading"/>
    </template>
  </van-cell>
  <UserCardList :userList="userList"></UserCardList>
  <van-empty v-if=" ! userList||userList.length===0" description="数据出错了"/>
</template>
<script setup lang="ts">
import {useRouter} from "vue-router";
import {ref, watchEffect} from "vue";
import MyAxios from "../plugins/myAxios.js";
import UserCardList from "../components/UserCardList.vue";
import {UserType} from "../models/user";

const router = useRouter()

const userList = ref([])

const isMatchMode = ref<boolean>(false)

const loading = ref<boolean>(false)

let title = '';

const loadData = async () => {
  let userListData = [];
  if (isMatchMode.value === true) {
    title = '搭子模式'
    loading.value = true

    const num = 10;
    userListData = await MyAxios.get('/user/match',
        {
          params: {
            num,
          },
        }
    )
        .then(function (response) {
          console.log(response)
          return response.data
        }).catch(function (error) {
          console.log(error)
        })
  } else {
    title = '普通模式'
    userListData = await MyAxios.get('/user/recommend',
        {
          params: {
            pageSize: 10,
            pageNum: 1
          },
        }
    )
        .then(function (response) {
          console.log(response)
          return response.data?.records
        }).catch(function (error) {
          console.log(error)
        })
  }
  if (userListData) {
    userListData.forEach((user: UserType) => {
      if (user.tags) {
        user.tags = JSON.parse(user.tags)
      }
    })
    userList.value = userListData;
    console.log(userListData)
    loading.value = false
  }

}
watchEffect(() => {
  loadData()
})

</script>

<style scoped>

</style>