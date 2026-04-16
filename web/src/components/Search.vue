<!--<script setup>-->
<!--import {ref,nextTick } from 'vue'-->
<!--import {AlbumDownload, ArtistDownload, getPlayUrL, musicDownload, selectmusic} from "../utils/api.js";-->
<!--import axios from 'axios'-->
<!--import TopWitge from "./TopWitge.vue";-->





<!--// 搜索类型-->
<!--let plugType_value = ref("kw")-->

<!--let shear_select_value=ref("music")-->
<!--// 搜索关键字-->
<!--let keyword_value = ref("")-->
<!--// 每页长度（禁止修改）-->
<!--let pageSize = ref(20)-->
<!--// 页码-->
<!--let pageIndex = ref(1)-->
<!--// 每次查询数据-->
<!--let list_data = ref([])-->


<!--let is_show_play = ref(false);-->

<!--let getShowPlay=()=>{-->
<!--  //找到configKey是music.show.play的数据-->
<!--  let setinfodata = window.localStorage.getItem("setinfo");-->
<!--  let setinfo = ref(JSON.parse(setinfodata));-->


<!--  for (let setinfoElement of setinfo.value) {-->
<!--    if (setinfoElement["configKey"] == "music.show.play"&&setinfoElement["configValue"] == "true") {-->
<!--      return true;-->
<!--    }-->
<!--  }-->

<!--  return false;-->
<!--}-->



<!--// 搜索类型选项-->
<!--let select_options=ref([-->
<!--  {-->
<!--    label: "某我",-->
<!--    value: "kw"-->
<!--  },-->
<!--  {-->
<!--    label: "鹅厂",-->
<!--    value: "qq"-->
<!--  },-->
<!--  {-->
<!--    label: "10086",-->
<!--    value: "mg",-->
<!--    disabled: true-->
<!--  }-->
<!--])-->

<!--let shear_select_options=ref([-->
<!--      {-->
<!--        label: "单曲",-->
<!--        value: "music"-->
<!--      },-->
<!--      {-->
<!--        label: "专辑",-->
<!--        value: "album"-->
<!--      },-->
<!--      {-->
<!--        label: "歌手（全部专辑）",-->
<!--        value: "artist"-->
<!--      },-->
<!--      {-->
<!--        label: "歌手（全部歌曲）-不分接口失效",-->
<!--        value: "artistAllSong"-->
<!--      }-->
<!--    ])-->

<!--let update_select_type=(value, option)=>{-->
<!--  pageIndex.value=1-->
<!--  list_data.value=[]-->
<!--}-->


<!--    // @update:value="handleUpdateValue"-->
<!--/**-->
<!-- * 初始化搜索类型选项-->
<!-- */-->
<!--onBeforeMount(()=>{-->
<!--  // selectOption().then(value => {-->
<!--  //   console.log(value)-->
<!--  //   nextTick(()=>{-->
<!--  //     select_options.value = value.data.data;-->
<!--  //   })-->
<!--  // })-->

<!--  is_show_play.value = getShowPlay();-->
<!--})-->

<!--/**-->
<!-- * 搜索-->
<!-- */-->
<!--let d_selectmusic= ()=>{-->
<!--  selectmusic(plugType_value.value,shear_select_value.value,keyword_value.value,pageSize.value,pageIndex.value).then(value=>{-->
<!--    if (value.data.code === 200) {-->
<!--      nextTick(()=>{-->
<!--        list_data.value = value.data.data.records;-->
<!--      })-->
<!--    }-->
<!--  })-->
<!--}-->

<!--// 单曲下载-->
<!--let b_musicDownload=(id)=> {-->
<!--  musicDownload(id, plugType_value.value, 2000).then(value => {-->
<!--    if (value.data.code === 200) {-->
<!--      window.$message.success("提交成功加入待下载")-->
<!--    } else {-->
<!--      window.$message.error("提交失败：" + value.data.msg)-->
<!--    }-->
<!--  })-->
<!--}-->

<!--  let b_musicPlayerURl=(id)=> {-->
<!--    getPlayUrL(id, plugType_value.value).then(value => {-->
<!--      if (value.data.code === 200) {-->
<!--        window.open(value.data.data.url, '_blank');-->
<!--      } else {-->
<!--        window.$message.error("好像没获取到数据：" + value.data.msg)-->
<!--      }-->
<!--    })-->

<!--  }-->

<!--    let b_ArtistDownload=(id)=>{-->
<!--  ArtistDownload(id,plugType_value.value,2000).then(value=>{-->
<!--    if (value.data.code===200){-->
<!--      window.$message.success("提交成功加入待下载")-->
<!--    }else{-->
<!--      window.$message.error("提交失败："+value.data.msg)-->
<!--    }-->
<!--  })-->
<!--}-->

<!--let b_AlbumDownload=(id)=>{-->
<!--  AlbumDownload(id,plugType_value.value,2000).then(value=>{-->
<!--    if (value.data.code===200){-->
<!--      window.$message.success("提交成功加入待下载")-->
<!--    }else{-->
<!--      window.$message.error("提交失败："+value.data.msg)-->
<!--    }-->
<!--  })-->
<!--}-->


<!--/**-->
<!-- * 下一页-->
<!-- */-->
<!--let nextPage=()=>{-->
<!--  nextTick(()=>{-->
<!--    pageIndex.value=pageIndex.value+1;-->
<!--    d_selectmusic()-->
<!--  })-->

<!--}-->
<!--let PreviousPage =()=>{-->
<!--  nextTick(()=>{-->
<!--    pageIndex.value=pageIndex.value-1;-->
<!--    if (pageIndex.value<1){-->
<!--      pageIndex.value=1;-->
<!--      window.$message.error("第一页就别点了吧")-->
<!--    }-->
<!--    d_selectmusic()-->
<!--  })-->
<!--}-->
<!--</script>-->
<!--<template>-->
<!--  <TopWitge/>-->
<!--  <n-form-->
<!--      inline-->
<!--      @keyup.enter.native="d_selectmusic"-->
<!--  >-->
<!--    <n-select-->
<!--        v-model:value="plugType_value"-->
<!--        placeholder="Select"-->
<!--        :options="select_options"-->
<!--        @update:value="update_select_type"-->
<!--    />-->
<!--    <n-select-->
<!--        v-model:value="shear_select_value"-->
<!--        placeholder="Select"-->
<!--        :options="shear_select_options"-->
<!--        @update:value="update_select_type"-->
<!--    />-->
<!--      <n-input placeholder="关键字" v-model:value="keyword_value" />-->
<!--      <n-button attr-type="button" @click="d_selectmusic">-->
<!--        搜索-->
<!--      </n-button>-->
<!--  </n-form>-->
<!--  <br>-->
<!--  <br>-->
<!--  <div  id="addlist">-->
<!--    <div id = "musicshowdiv" v-if="shear_select_value==='music'">-->
<!--      <n-list v-for="(item, index) in list_data">-->
<!--        <n-list-item>-->
<!--          <template #prefix>-->
<!--            <n-avatar-->
<!--                :size="48"-->
<!--                :src="item.pic"-->
<!--            />-->
<!--          </template>-->
<!--          <n-thing :title="item.name" :description="item.artistName" />-->

<!--          <template #suffix>-->
<!--            <n-button @click="b_musicDownload(item.id)"> 下载</n-button>-->
<!--            <n-button v-if="is_show_play" @click="b_musicPlayerURl(item.id)"> 播放</n-button>-->
<!--          </template>-->
<!--        </n-list-item>-->
<!--      </n-list>-->
<!--    </div>-->

<!--    <div id = "albumshowdiv" v-if="shear_select_value==='album'">-->
<!--      <n-list v-for="(item, index) in list_data">-->
<!--        <n-list-item>-->
<!--          <template #prefix>-->
<!--            <n-avatar-->
<!--                :size="48"-->
<!--                :src="item.pic"-->
<!--            />-->
<!--          </template>-->
<!--          <n-thing :title="item.albumName" :description="item.artistName" />-->
<!--          <template #suffix>-->
<!--            <n-button @click="b_AlbumDownload(item.albumid)"> 下载该专辑全部歌曲</n-button>-->
<!--          </template>-->
<!--        </n-list-item>-->
<!--      </n-list>-->
<!--    </div>-->

<!--    <div id = "artistshowdiv" v-if="shear_select_value==='artist'||shear_select_value==='artistAllSong'">-->
<!--      <n-list v-for="(item, index) in list_data">-->
<!--        <n-list-item>-->
<!--          <template #prefix>-->
<!--            <n-avatar-->
<!--                :size="48"-->
<!--                :src="item.pic"-->
<!--            />-->
<!--          </template>-->
<!--          <n-thing :title="item.artistName" :description="item.total+'张专辑'" />-->
<!--          <template #suffix>-->
<!--            <n-button @click="b_ArtistDownload(item.artistid)"> 下载全部专辑歌曲</n-button>-->

<!--            <n-tooltip :show-arrow="false" trigger="hover">-->
<!--              <template #trigger>-->
<!--                <n-button @click="b_ArtistDownload(item.artistid)"> 下载全部歌曲</n-button>-->
<!--              </template>-->
<!--              部分歌曲不在专辑中此功能也可下载（部分不支持比如鹅厂）-->
<!--            </n-tooltip>-->

<!--          </template>-->
<!--        </n-list-item>-->
<!--      </n-list>-->
<!--    </div>-->
<!--  <div class="page">-->
<!--    <n-button @click="PreviousPage">上一页</n-button>-->
<!--    <div><h3>&nbsp;&nbsp;&nbsp;当前第： {{pageIndex}}页&nbsp;&nbsp;&nbsp;</h3></div>-->
<!--    <n-button @click="nextPage">下一页</n-button>-->
<!--  </div>-->

<!--  </div>-->
<!--</template>-->

<!--<style scoped>-->
<!--.page{-->
<!--  display: flex;-->
<!--  place-items: center;-->
<!--  flex-direction: row;-->
<!--  justify-content: center ;-->
<!--}-->

<!--.headerSarch{-->
<!--  display: flex;-->
<!--}-->
<!--</style>-->
