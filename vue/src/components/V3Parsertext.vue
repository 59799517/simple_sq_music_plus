<script setup>

import {downloadParserText} from "../utils/api.js";


let value=ref('')


let download = ()=>{
  downloadParserText(value.value).then(value=>{
    if (value.data.code === 200) {
      window.$message.success("操作成功："+value.data.msg)
    }else{
      window.$message.error("操作失败："+value.data.msg)
    }
  })
}

</script>

<template>
  <n-space vertical>
    <n-input
        v-model:value="value"
        type="textarea"
        placeholder="请输入解析的歌单信息"
        :autosize="{
           minRows: 20,
           maxRows: 35
        }"
    />
  </n-space>
  <n-divider>注释:转换请参考：https://music.unmeta.cn/</n-divider>
  <div class="page">
    <n-button @click="download">识别并下载</n-button>
  </div>
</template>

<style scoped>
.page{
  display: flex;
  place-items: center;
  flex-direction: row;
  justify-content: center ;
}
</style>