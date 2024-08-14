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
        <van-button v-if="team.userId === currentUser.id" size="mini" type="primary"
                    @click="doUpdate(team.id)">
          更新队伍
        </van-button>
        <van-button size="mini" type="danger" @click="doQuitTeam(team.id)">退出队伍</van-button>
        <!--todo 队伍页面只有创建者能看到-->
        <van-button size="mini" type="danger" @click="doDeleteTeam(team.id)">解散队伍</van-button>
      </template>
    </van-card>
  </div>
</template>
<script setup lang="ts">
import {TeamType} from "../models/team";
import {teamSatusEnum} from "../constant/teamSatus";
import MyAxios from "../plugins/myAxios";
import {useRouter} from "vue-router";
import {getCurrentUser} from "../service/User.ts";
import {onMounted, ref} from "vue";

const router = useRouter()

interface teamCardList {
  teamList: TeamType[];
}

const currentUser = ref();
onMounted(async () => {
  currentUser.value = await getCurrentUser();
})
const avator = "https://img1.baidu.com/it/u=1968668429,2104382916&fm=253&fmt=auto&app=138&f=JPEG?w=507&h=500";
const props = withDefaults(defineProps<teamCardList>(), {
  teamList: []
});
// 更新队伍
const doUpdate = (id: number) => {
  router.push({
    path: '/team/update',
    query: {
      id
    }
  })

}
// 加入队伍
const doJoinTeam = async (id: number) => {
  const res = await MyAxios.post('/team/join', {
    teamId: id,
  });
  //todo 加入有密码的情况
  if (res?.code === 0) {
    alert("加入队伍成功")
    router.replace("/user")
  } else {
    alert("加入队伍失败")
    router.back()
  }

}
// 退出队伍
const doQuitTeam = async (id: number) => {
  const res = await MyAxios.post('/team/quit', {
    teamId: id
  });
  if (res?.code === 0) {
    alert("退出队伍成功")
     router.replace("/user")
  } else {
    alert("退出队伍失败")
    router.back()
  }
}
// 解散队伍
const doDeleteTeam = async (id: number) => {
  const res = await MyAxios.post('/team/delete', {
    id,
  });
  if (res?.code === 0) {
    alert("解散队伍成功")
     router.replace("/user")
  } else {
    alert("解散队伍失败")
    router.back()
  }
}
</script>
<style scoped>
#card ::v-deep(.van-image__img) {
  height: 120px;

}
</style>
