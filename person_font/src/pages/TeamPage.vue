<template>
  <div id="teamPage">
    <van-button type="primary" @click="doJoinTeam">主要按钮</van-button>
  </div>
  <TeamList :team-list="teamList"></TeamList>
</template>
<script setup>
import {useRouter} from "vue-router";
import TeamList from "../components/TeamCardList.vue";
import MyAxios from "../plugins/myAxios.js";
import {ref,onMounted} from "vue";

const router = useRouter()
const doJoinTeam = () => {
  router.push("/team/add")
}
const teamList =ref([]);
//页面加载是时触发一次
onMounted(async () => {
  const res = await MyAxios.get("/team/list");
  if (res?.code === 0) {
    teamList.value = res.data
  }
})
</script>
<style scoped>
#teamPage {

}

</style>