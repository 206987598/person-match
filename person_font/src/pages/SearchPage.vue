<template>
  <form action="/">
    <van-search
        v-model="SearchText"
        show-action
        placeholder="请输入搜索关键词"
        @search="onSearch"
        @cancel="onCancel"
    />
  </form>
  <van-divider content-position="left">已选标签</van-divider>
  <van-row>
    <van-col span="4" v-for="tag in activeIds">
      <van-tag closeable size="medium" type="primary" @close="close(tag)">
        {{ tag }}
      </van-tag>
    </van-col>
  </van-row>
  <van-divider content-position="left">选择标签</van-divider>
  <van-tree-select
      v-model:active-id="activeIds"
      v-model:main-active-index="activeIndex"
      :items="tagList"
  />

</template>
<script setup>
import {ref} from 'vue';
// import {showToast} from 'vant';

const SearchText = ref('');

//移出标签
const close = (tag) => {
  activeIds.value = activeIds.value.filter(item => {
    return item !== tag;
  })
}
const activeIds = ref([]);
const activeIndex = ref();
//定义原始标签列表
const originTagList = [
  {
    text: '性别',
    children: [
      {text: '男', id: '男'},
      {text: '女', id: '女'},
    ],
  },
  {
    text: '年级',
    children: [
      {text: '大一', id: '大一'},
      {text: '大二', id: '大二'},
      {text: '大三', id: '大三'},
      {text: '大四', id: '大四'},
    ],
  },
  {
    text: '职业',
    children: [
      {text: '学生', id: '学生'},
      {text: '实习生', id: '实习生'},
      {text: '在岗工作者', id: '在岗工作者'},
    ],
  },
];
//定义响应式标签列表
const tagList = ref(originTagList)
const onSearch = (val) => {
  tagList.value = originTagList.map(parentTag => {
    const temParent = {...parentTag}
    const temChildren = [...parentTag.children]
    temParent.children = temChildren.filter(item => item.text.includes(SearchText.value))
    return temParent;
  })
  // tagList.forEach(parentTag => parentTag.children=parentTag.children.filter(item => item.text.includes(SearchText.value)))
  // activeIds.value = tagList.flatMap(parent => parent.children)
  //     .filter(item => item.text.includes(SearchText.value));
};
const onCancel = () => {
  SearchText.value = ''
  tagList.value=originTagList
};
</script>

<style scoped>

</style>