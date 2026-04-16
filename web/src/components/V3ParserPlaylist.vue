<script setup>

import {parserUrlAndDownload} from "../utils/api.js";
import {ref} from "vue";



let value=ref('')
let active =ref(false)
let bookName = ref('')
let artist = ref('')
const data_list= ref([])
let download = ()=>{
  parserUrlAndDownload(value.value,active.value,bookName.value,artist.value).then(value=>{
    if (value.data.code === 200) {
      window.$message.success("操作成功")
      data_list.value = value.data.data
    }else{
      window.$message.error("操作失败："+value.data.msg)
    }
  })
}
</script>

<template>
  <br/>
  <br/>
  <br/>
  <br/>
  <n-space vertical>
    <n-input
        v-model:value="value"
        placeholder="请输入歌单url：qq链接必须扫码登陆后才能使用！"
    />
  </n-space>
  <n-divider>
      异步任务解析下载后续请自行查看下载列表
  </n-divider>
  <div v-show="active">
    <n-input placeholder="书名：" v-model:value="bookName"></n-input>
    <n-input placeholder="作者:" v-model:value="artist" ></n-input>
  </div>
  <br>
  <div class="page">
    是否自定义歌单信息(主要用于有声书下载方便整理)：
    <n-switch v-model:value="active" />
  </div>
  <br/>
  <br/>
<div class="page">
  <n-button @click="download">解析并下载</n-button>
</div>

  <n-divider>
    已经识别歌曲信息：
  </n-divider>

  <div v-if="data_list.length>0">
    <n-infinite-scroll style="height: 480px" :distance="10">
      <n-list hoverable clickable>
        <n-list-item v-for="i in data_list">
          <n-gradient-text :size="24" type="warning">
            {{ i.downloadMusicname }}
          </n-gradient-text>
          <n-thing content-style="margin-top: 10px;">

            <template #description>
              <n-space size="small" style="margin-top: 4px">
                <n-gradient-text type="warning">
                  歌手：
                </n-gradient-text>
                <n-tag :bordered="false" type="warning" size="small">
                  {{ i.downloadArtistname }}
                </n-tag>
                <br>
                <n-gradient-text type="warning">
                  专辑：
                </n-gradient-text>
                <n-tag :bordered="false" type="warning" size="small">
                  {{ i.downloadAlbumname }}
                </n-tag>

                <n-gradient-text type="warning">
                  当前下载音质：
                </n-gradient-text>
                <n-tag :bordered="false" type="warning" size="small">
                  {{i.downloadBrType}}
                </n-tag>
              </n-space>
            </template>
          </n-thing>
        </n-list-item>
      </n-list>
    </n-infinite-scroll>
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