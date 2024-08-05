<template>
  <van-form @submit="onSubmit">
    <van-cell-group inset>
      <van-field
          v-model="editUser.currentValue"
          :name="editUser.editKey"
          :label="editUser.editName"
          :placeholder="`请输入${editUser.editName}`"
      />
    </van-cell-group>
    <div style="margin: 16px;">
      <van-button round block type="primary" native-type="submit">
        提交
      </van-button>
    </div>
  </van-form>

</template>
<script setup>
import {useRoute, useRouter} from "vue-router";
import {ref} from "vue";
import MyAxios from "../plugins/myAxios";
import {getCurrentUser} from "../service/User.ts";

const route = useRoute()
const editUser = ref(
    {
      editKey: route.query.editKey,
      editName: route.query.editName,
      currentValue: route.query.currentValue
    }
)


const router = useRouter()
const onSubmit = async () => {
  const currentUser = await getCurrentUser()
  if (!currentUser) {
    return alert('请先登录')
  }
  const res = await MyAxios.post('user/update', {
    'id': currentUser.id,
    [editUser.value.editKey]: editUser.value.currentValue
  })
  console.log("更新请求")
  if (res.code === 0 && res.data > 0) {
    alert('修改成功')
    router.back()
  } else {
    alert('修改失败')
  }
};
</script>
<style scoped>

</style>