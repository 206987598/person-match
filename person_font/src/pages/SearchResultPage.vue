<template>
  <UserCardList :userList="userList"></UserCardList>
  <van-empty v-if=" ! userList||userList.length===0" description="暂无数据"/>

</template>
<script setup>
import {useRoute} from "vue-router";
import {ref, onMounted} from "vue";
import MyAxios from "../plugins/myAxios.js";
import qs from "qs"
import UserCardList from "../components/UserCardList.vue";


const route = useRoute()
const {tags} = route.query
const userList = ref([])
onMounted(async () => {
  const userListData = await MyAxios.get('/user/search/tags',
      {
        params: {
          tagNameList: tags,
        },
        paramsSerializer: function (params) {
          return qs.stringify(params, {indices: false})
        }
      }
  )
      .then(function (response) {
        console.log(response)

        return response?.data
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