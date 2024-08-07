<template>
<UserCardList :userList="userList"></UserCardList>
  <van-empty v-if=" ! userList||userList.length===0" description="数据出错了"/>

</template>
<script setup>
import {useRoute} from "vue-router";
import {ref, onMounted} from "vue";
import MyAxios from "../plugins/myAxios.js";
import UserCardList from "../components/UserCardList.vue";


const route = useRoute()
const {tags} = route.query
const userList = ref([])
onMounted(async () => {
  const userListData = await MyAxios.get('/user/recommend',
      {
        params: {
          pageSize: 10,
          pageNum:1
        },
      }
  )
      .then(function (response) {
        console.log(response)
        return response.data?.records
      }).catch(function (error) {
        console.log(error)
      })
  if (userListData) {
    userListData.forEach((user) => {
      if (user.tags) {
        user.tags = JSON.parse(user.tags)
      }
    })
    userList.value = userListData;
    console.log(userListData)
  }
})
</script>

<style scoped>

</style>