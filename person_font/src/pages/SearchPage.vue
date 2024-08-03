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
  <div style="padding:20px">
    <van-button type="primary" @click="doSearch" block size="medium" style="margin-top: 20px;">搜索</van-button>
  </div>
</template>
<script setup>
import {ref} from 'vue';
import {useRoute, useRouter} from "vue-router";
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
const router = useRouter();
/**
 * 点击跳转到搜索结果页面
 */
const doSearch = () => {
  router.push(
      {
        path: '/user/list',
        query: {
          tags: activeIds.value
        }
      }
  )
}
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

/**
 * 根据搜索值过滤标签列表
 * @param {string} val - 搜索框的值
 */
const onSearch = (val) => {
  // 遍历原始标签列表，对每个标签及其子标签进行筛选
  tagList.value = originTagList.map(parentTag => {
    // 复制父标签，以免修改原始数据
    const temParent = {...parentTag}
    // 复制子标签列表，以便进行筛选
    const temChildren = [...parentTag.children]
    // 筛选子标签，只保留文本中包含搜索值的标签
    temParent.children = temChildren.filter(item => item.text.includes(SearchText.value))
    // 返回修改后的父标签
    return temParent;
  })

  // tagList.forEach(parentTag => parentTag.children=parentTag.children.filter(item => item.text.includes(SearchText.value)))
  // activeIds.value = tagList.flatMap(parent => parent.children)
  //     .filter(item => item.text.includes(SearchText.value));
};
const onCancel = () => {
  SearchText.value = ''
  tagList.value = originTagList
};
</script>

<style scoped>

</style>