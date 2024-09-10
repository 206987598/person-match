<template>
  <van-card v-for="user in props.userList"
            :title="user.username"
            :thumb="user.avatarUrl"
  >
    <template #tags>
      <van-tag plain type="primary" v-for="tag in user.tags" style="margin-right: 5px;margin-top: 10px">
        {{ tag }}
      </van-tag>
    </template>
    <template #footer>
      <van-button size="mini">联系我</van-button>
    </template>
  </van-card>
</template>
<script setup lang="ts">
import {UserType} from "../models/user";

interface userCardList {
  userList: UserType[];
}

const props = withDefaults(defineProps<userCardList>(), {
  userList: []
});

</script>
<style scoped>

</style>
