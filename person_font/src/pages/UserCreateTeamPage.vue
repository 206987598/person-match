<template>
  <div id="teamPage">
    <van-search v-model="searchText" placeholder="请输入搜索关键词" @search="doSearch"/>
  </div>
  <van-empty v-if="teamList?.length < 1" description="暂无符合描述队伍"/>
  <TeamList :team-list="teamList"></TeamList>
</template>
<script setup>
import {useRouter} from "vue-router";
import TeamList from "../components/TeamCardList.vue";
import MyAxios from "../plugins/myAxios.js";
import {ref, onMounted} from "vue";

const router = useRouter()
const teamList = ref([]);
const searchText = ref("");
const SearchList = async (val) => {
  const res = await MyAxios.get("/team/list/myCreateTeam", {
    params: {
      searchText: val
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
</script>
<style scoped>
#teamPage {

}

</style>