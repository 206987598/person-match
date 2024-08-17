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
          {{ `已加人数：${team.hasJoinNum}/${team.maxNum}` }}
        </div>
        <div>
          创建时间：{{ team.createTime }}
        </div>
        <div>
          过期时间：{{ team.expireTime }}
        </div>
      </template>
      <template #footer>
        <van-button size="mini" type="primary" v-if="team.userId !== currentUser.id && team.hasJoin==false"
                    @click="(preJoinTeam(team))">加入队伍
        </van-button>
        <van-button v-if="team.userId === currentUser.id" size="mini" type="primary"
                    @click="doUpdate(team.id)">
          更新队伍
        </van-button>
        <van-button size="mini" type="danger" @click="doQuitTeam(team.id)" v-if="team.hasJoin===true">退出队伍
        </van-button>
        <van-button size="mini" type="danger" @click="doDeleteTeam(team.id)" v-if="team.userId === currentUser.id">
          解散队伍
        </van-button>
      </template>
    </van-card>
  </div>
  <van-dialog v-model:show="showPassword" title="请输入密码" show-cancel-button @confirm="doJoinTeam"
              @cancel="doJoinCancel">
    <van-field v-model="password" placeholder="请输入密码" type="password"/>
  </van-dialog>
</template>
<script setup lang="ts">
import {TeamType} from "../models/team";
import {teamSatusEnum} from "../constant/teamSatus";
import MyAxios from "../plugins/myAxios";
import {useRouter} from "vue-router";
import {getCurrentUser} from "../service/User.ts";
import {onMounted, ref} from "vue";
import {showToast} from "vant";

const router = useRouter()
const showPassword = ref(false)
const password = ref('')
const JoinTeamId = ref()

interface teamCardList {
  teamList: TeamType[];
}

const avator = "https://img1.baidu.com/it/u=1968668429,2104382916&fm=253&fmt=auto&app=138&f=JPEG?w=507&h=500";
const currentUser = ref();
onMounted(async () => {
  currentUser.value = await getCurrentUser();
})
const doJoinCancel = () => {
  password.value = ''
  JoinTeamId.value = ''
}
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
// 预览加入队伍
const preJoinTeam = (team: TeamType) => {
  JoinTeamId.value = (team.id)
  if (team.status === 0) {
    doJoinTeam()
  } else {
    showPassword.value = true
  }
}
// 加入队伍
const doJoinTeam = async () => {

  const res = await MyAxios.post('/team/join', {
    teamId: JoinTeamId.value,
    password: password.value,
  });
  if (res?.code === 0) {
    showToast("加入队伍成功")

    router.replace("/team")
  } else {
    showToast("加入队伍失败")
    // router.back()
  }
  window.location.reload()
  doJoinCancel()
}
// 退出队伍
const doQuitTeam = async (id: number) => {
  const res = await MyAxios.post('/team/quit', {
    teamId: id
  });
  if (res?.code === 0) {
    showToast("退出队伍成功")
    router.replace("/team")
  } else {
    showToast("退出队伍失败")
    router.back()
  }
  window.location.reload()
}
// 解散队伍
const doDeleteTeam = async (id: number) => {
  const res = await MyAxios.post('/team/delete', {
    id,
  });
  if (res?.code === 0) {
    showToast("解散队伍成功")
    router.replace("/team")
  } else {
    showToast("解散队伍失败")
    router.back()
  }
  window.location.reload()
}
</script>
<style scoped>
#card ::v-deep(.van-image__img) {
  height: 120px;

}
</style>
