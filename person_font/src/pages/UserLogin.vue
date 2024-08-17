<template>
  <van-form @submit="onSubmit">
    <van-cell-group inset>
      <van-field
          v-model="userAccount"
          name="userAccount"
          label="账号"
          placeholder="账号"
          :rules="[{ required: true, message: '请填写账号' }]"
      />
      <van-field
          v-model="userPassword"
          type="password"
          name="userPassword"
          label="密码"
          placeholder="密码"
          :rules="[{ required: true, message: '请填写密码' }]"
      />
    </van-cell-group>
    <div style="margin: 16px;">
      <van-button round block type="primary" native-type="submit">
        提交
      </van-button>
    </div>
  </van-form>

</template>
<script setup lang="ts">
import {useRoute, useRouter} from "vue-router";
import {ref} from "vue";
import MyAxios from "../plugins/myAxios";


const router = useRouter()
const route = useRoute()
const userAccount = ref('');
const userPassword = ref('');
const onSubmit = async () => {
  const res = await MyAxios.post('/user/login', {
    userAccount: userAccount.value,
    userPassword: userPassword.value
  })
  if (res.code === 0 && res.data) {
    showToast('登录成功')
    const redirectUrl = route.query?.redirect as string ?? '/';
    window.location.href = redirectUrl
    // router.replace('/');
  } else {
    showToast('登录失败')
  }
};
</script>
<style scoped>

</style>