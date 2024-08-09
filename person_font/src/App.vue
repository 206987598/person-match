<template>
  <BasicLayOut/>
</template>
<script setup lang="ts">
import BasicLayOut from "./layouts/BasicLayOut.vue";
import {useRoute, useRouter} from "vue-router";
import {onMounted} from "vue";

onMounted(() => {
  console.log(useRoute().path)
  if (useRoute().path === "/") {
    useRouter().replace("/login")
  }
})
</script>

<style scoped>

</style>
