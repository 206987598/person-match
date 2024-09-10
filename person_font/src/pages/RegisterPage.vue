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
      <van-field
          v-model="checkPassword"
          type="password"
          name="checkPassword"
          label="确认密码"
          placeholder="确认密码"
          :rules="[{ required: true, message: '请再次填写密码' }]"
      />
    </van-cell-group>
    <div style="margin: 16px;">
      <van-button round block type="primary" native-type="submit">
        注册
      </van-button>
    </div>
  </van-form>
</template>
<script setup>
import {ref,} from 'vue';
import MyAxios from "../plugins/myAxios";

const userAccount = ref('')
const userPassword = ref('')
const checkPassword = ref('')
const onSubmit = async () => {
  const res = await MyAxios.post('/user/register', {
    userAccount: userAccount.value,
    userPassword: userPassword.value,
    checkPassword: checkPassword.value
  })
  if (res.code === 0 && res.data) {
    showToast('注册成功')
    window.location.href = '/login'
  } else {
    showToast('注册失败')
  }
}

</script>

<style scoped>

</style>