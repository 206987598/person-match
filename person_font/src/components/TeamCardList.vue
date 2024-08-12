<template>
  <div id="card">
    <van-card v-for="team in props.teamList"
              :title="team.name"
              :desc="team.description"
              :thumb="avator"
    >
      <template #tags>
        <van-tag plain type="primary" style="margin-right: 5px;margin-top: 10px">
          {{
            teamSatusEnum[team.status]
          }}
        </van-tag>
      </template>
      <template #bottom>
        <div>
          最大人数：{{ team.maxNum }}
        </div>
        <div>
          创建时间：{{ team.createTime }}
        </div>
        <div>
          过期时间：{{ team.expireTime }}
        </div>

      </template>
      <template #footer>
        <van-button size="mini" type="primary" @click="doJoinTeam(team.id)">加入队伍</van-button>
      </template>
    </van-card>
  </div>
</template>
<script setup lang="ts">
import {TeamType} from "../models/team";
import {teamSatusEnum} from "../constant/teamSatus";
import MyAxios from "../plugins/myAxios";
import {useRouter} from "vue-router";

const router = useRouter()
interface teamCardList {
  teamList: TeamType[];
}

const avator = "https://img1.baidu.com/it/u=1968668429,2104382916&fm=253&fmt=auto&app=138&f=JPEG?w=507&h=500";
const props = withDefaults(defineProps<teamCardList>(), {
  teamList: []
});
const doJoinTeam = async (id: number) => {
  const res = await MyAxios.post('/team/join', {
    teamId: id,
  });
  //todo 加入有密码的情况
  if (res?.code === 0) {
    alert("加入队伍成功")
    router.replace("/user")
  }else {
    alert("加入队伍失败")
    router.back()
  }

}
</script>
<style scoped>
#card ::v-deep(.van-image__img) {
  height: 120px;

}
</style>
