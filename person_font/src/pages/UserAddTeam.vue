<template>
  <van-form @submit="onSubmit">
    <van-cell-group inset>
      <van-field
          v-model="formData.name"
          name="name"
          label="队伍名称"
          placeholder="请输入队伍名称"
          :rules="[{ required: true, message: '请填写队伍名称' }]"
      />
      <van-field
          v-model="formData.description"
          name="description"
          label="队伍描述"
          placeholder="请输入队伍描述"
          type="textarea"
          rows="2"
          autosize
      />
      <van-field
          v-model="result"
          is-link
          readonly
          name="datePicker"
          label="过期时间选择"
          placeholder="点击选择时间"
          @click="showPicker = true"
      />
      <van-popup v-model:show="showPicker" position="bottom">
        <van-date-picker
            type="datetime"
            title="请选择过期选择日期"
            @confirm="onConfirm"
            @cancel="showPicker = false"
            :min-date="minDate"/>
      </van-popup>
      <van-field name="stepper" label="请选择人数">
        <template #input>
          <van-stepper v-model="formData.maxNum" :max="10" :min="3"/>
        </template>
      </van-field>
      <van-field name="radio" label="单选框">
        <template #input>
          <van-radio-group v-model="formData.status" direction="horizontal">
            <van-radio name="0">公开</van-radio>
            <van-radio name="1">私有</van-radio>
            <van-radio name="2">加密</van-radio>
          </van-radio-group>
        </template>
      </van-field>

      <van-field
          v-if="Number(formData.status)===2"
          v-model="formData.password"
          type="password"
          name="userPassword"
          label="密码"
          placeholder="请输入密码"
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
<script setup>
import {ref} from "vue";
import MyAxios from "../plugins/myAxios.js";
import {useRouter} from "vue-router";


const router = useRouter()
const onSubmit = async () => {
  console.log('开始向后台传参数');
// todo 前段参数验证
  const postData = {
    ...formData.value,
    status: Number(formData.value.status),
  }
  const res = await MyAxios.post("/team/save", postData);
  if (res?.code == 0 && res.data) {
    console.log("看到这里就已经执行成功了")
    showToast('创建队伍成功');
    router.push({
      path: '/team',
      replace: true
    })
  } else {
    showToast('创建队伍失败');
  }

}
const result = ref('');
const showPicker = ref(false);
const onConfirm = ({selectedValues}) => {
  result.value = selectedValues.join('-');
  formData.value.expireTime = result;
  showPicker.value = false;
};
const minDate = ref(new Date());
const initFormDate = {
  "name": "",
  "description": "",
  "maxNum": 3,
  "status": 0,
  "expireTime": null,
  "password": "",
}
const formData = ref({...initFormDate})


</script>

<style scoped>

</style>