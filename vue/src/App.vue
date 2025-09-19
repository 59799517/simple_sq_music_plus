<script setup>


import { darkTheme,NMessageProvider, zhCN, dateZhCN } from "naive-ui";
import { provide, ref } from 'vue'
import MessageApi from "./components/message-api.vue";
import Login from "./components/Login.vue";
import configInfoStore from "./stores/config";
import { getversion } from "./utils/api.js";


let theme=ref(darkTheme);
let name=ref('dark');
const changetheme = () => {

  if (name.value === 'dark') {
    theme.value = null;
    name.value='';
  }else{
    theme.value =darkTheme;
    name.value='dark';
  }
};
provide("changetheme",changetheme);

// 获取后端版本号
const configStore = configInfoStore();
getversion().then(res => {
  if (res.code === 200) {
    configStore.setBackendVersion(res.data);
  }
}).catch(err => {
  console.error("获取后端版本号失败:", err);
});
</script>
<template>
  <n-config-provider :theme="theme" :locale="zhCN" :date-locale="dateZhCN">
    <n-dialog-provider>
      <n-loading-bar-provider>
      <n-message-provider placement="bottom">
      <MessageApi />
    </n-message-provider>
      </n-loading-bar-provider>
    </n-dialog-provider>
    <n-global-style />
<!--    <Login/>-->
    <router-view></router-view>
  </n-config-provider>

</template>

<style scoped>

</style>
