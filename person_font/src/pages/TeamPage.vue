<template>
  <div id="teamPage">
    <van-search v-model="searchText" placeholder="请输入搜索关键词" @search="doSearch"/>
    <van-tabs v-model:active="active" type="card" @change="doChange">
      <van-tab title="公开队伍" name='PUBLIC'/>
      <van-tab title="加密队伍" name="SECRET"/>
    </van-tabs>
  </div>
  <div style="margin-bottom: 10px"></div>
  <van-empty v-if="teamList?.length < 1" description="暂无符合描述队伍"/>
  <TeamList :team-list="teamList"></TeamList>
  <van-button id="add-button" icon="plus" type="primary" @click="doCreateTeam"></van-button>
</template>
<script setup>

import {useRouter} from "vue-router";
import TeamList from "../components/TeamCardList.vue";
import MyAxios from "../plugins/myAxios.js";
import {ref, onMounted} from "vue";

const router = useRouter()
const doCreateTeam = () => {
  router.push("/team/add")
}
const teamList = ref([]);
const searchText = ref("");
const SearchList = async (val, status = 0) => {
  const res = await MyAxios.get("/team/list", {
    params: {
      searchText: val,
      status,
    }
  });
  if (res?.code === 0) {
    teamList.value = res.data
  }
}
//页面加载是时触发一次
onMounted(() => {
  SearchList('')
})
//执行搜索方法
const doSearch = () => {
  SearchList(searchText.value)
}
const active = ref('PUBLIC')
const doChange = (name) => {
  if (name === 'PUBLIC') {
    SearchList(searchText.value, 0)
  } else if (name === 'SECRET') {
    SearchList(searchText.value, 2)
  }
}
</script>
<style scoped>

#teamPage {

}

</style>