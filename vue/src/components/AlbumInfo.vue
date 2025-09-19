<script setup lang="js">
import { ref, onMounted } from 'vue'
import { NImage, NGrid, NGridItem, NCard, NSpace, NTag, NAvatar, NEmpty, NSpin, NThing, NGradientText, NEllipsis, NFlex, NButton } from 'naive-ui'
import {getAlbumInfo, musicDownload, musicDownloadAlbum} from '../utils/api.js'
import playListStore from '../stores/playList.js'
import { storeToRefs } from 'pinia'

const stplayListStore = playListStore()

const props = defineProps({
  id: {
    type: String,
    required: true
  },
  plugName: {
    type: String,
    required: true
  }
})

const albumData = ref({
  albumImg: '',
  albumName: '',
  albumSinger: '',
  albumSongCount: '',
  albumTime: '',
  albumDes: '',
  songs: []
})

const loading = ref(true)

onMounted(() => {
  console.log("当前专辑参数是：id=" + props.id + ", plugName=" + props.plugName)
  getAlbumInfo(props.id, props.plugName).then(response => {
    console.log("当前专辑数据是：" + JSON.stringify(response.data.data))
    albumData.value = response.data.data
    loading.value = false
  }).catch(error => {
    console.error("获取专辑信息失败：", error)
    loading.value = false
  })
})

// 播放歌曲
const playSong = (data) => {
  // 构造歌曲数据对象
  const songData = {
    id: data.id,
    name: data.musicName,
    artistName:data.musicArtists,
    artistids: data.artistsIds,
    pic: data.musicImage,
    albumName:data.musicAlbum,
    lyric:"",
    lyricId:data.id,
    plugName: props.plugName,
    duration: data.musicDuration,
    brTypes: data.bits
  }

  // 添加到播放列表并播放
  stplayListStore.pushPlayListAndPlay(songData)
}

// 播放整张专辑
const playAlbum = async () => {
  if (!albumData.value || !albumData.value.musics || albumData.value.musics.length === 0) {
    window.$message.warning("该专辑暂无歌曲")
    return
  }

  // 清空当前播放列表
  stplayListStore.clearPlayList()

  // 将专辑中的所有歌曲添加到播放列表
  for (let i = 0; i < albumData.value.musics.length; i++) {
    const data = albumData.value.musics[i]
    const songData = {
      id: data.id,
      name: data.musicName,
      artistName: data.musicArtists,
      artistids: data.artistsIds,
      pic: data.musicImage,
      albumName: data.musicAlbum,
      lyric: "",
      lyricId: data.id,
      plugName: props.plugName,
      duration: data.musicDuration,
      brTypes: data.bits
    }

    // 添加到播放列表
    stplayListStore.pushPlayList(songData)
  }

  // 播放第一首歌曲
  if (stplayListStore.playList.length > 0) {
    stplayListStore.setPlayIndex(0)
    window.$message.success("开始播放专辑《" + albumData.value.albumName + "》")
  }
}

// 按指定音质播放歌曲
const download = (data, quality) => {
  // 构造歌曲数据对象
  const songData = {
    id: data.id,
    name: data.musicName,
    artistName:data.musicArtists,
    artistids: data.artistsIds,
    pic: data.musicImage,
    albumName:data.musicAlbum,
    lyric:"",
    lyricId:data.id,
    plugName: props.plugName,
    duration: data.musicDuration,
    brTypes: data.bits,
  }
  musicDownload(songData,quality).then(value=>{
    if (value.data.code===200){
      window.$message.success("开始下载")
    }else{
      window.$message.error("操作失败："+value.data.msg)
    }
  })

}

const downloadAlbum = () => {
  if (!albumData.value || !albumData.value.musics || albumData.value.musics.length === 0) {
    window.$message.warning("该专辑暂无歌曲可下载")
    return
  }


  const data = {
    albumid: props.id,
    albumName: albumData.albumName,
    plugName: props.plugName
  }
  musicDownloadAlbum(data).then(value=>{
    if (value.data.code===200){
      window.$message.success("开始下载当前专辑：自动适配最高音质下载")
    }else{
      window.$message.error("操作失败："+value.data.msg)
    }
  })
}

</script>

<template>
  <div>
    <n-spin :show="loading">
      <n-card v-if="albumData.albumName" :title="albumData.albumName" style="margin-bottom: 20px;">
        <n-flex justify="space-between">
          <n-space align="center">
            <n-image
                :src="albumData.albumImg"
                :width="150"
                :height="150"
                fallback-src="https://h5static.kuwo.cn/upload/image/4f768883f75b17a426c95b93692d98bec7d3ee9240f77f5ea68fc63870fdb050.png"
                style="border-radius: 8px;"
            />
            <div>
              <div style="font-size: 20px; font-weight: bold; margin-bottom: 8px;">{{ albumData.albumName }}</div>
              <div style="margin-bottom: 8px;">
                <n-tag type="info" style="margin-right: 5px;">歌手</n-tag>
                <n-ellipsis style="max-width: 540px">
                  {{ albumData.albumArtist }}
                </n-ellipsis>
              </div>
              <div style="margin-bottom: 8px;">
                <n-tag type="info" style="margin-right: 5px;">发行时间</n-tag>
                {{ albumData.albumTime }}
              </div>
              <div style="margin-bottom: 8px;">
                <n-tag type="info" style="margin-right: 5px;">歌曲数</n-tag>
                {{ albumData.musics.length }}
              </div>
            </div>
          </n-space>
          <n-space>
            <n-button @click="playAlbum">
              播放整张专辑
            </n-button>
            <n-button @click="downloadAlbum">
              下载专辑
            </n-button>
          </n-space>
        </n-flex>


        <div v-if="albumData.albumDescribe" style="margin-top: 20px;">
          <div style="font-weight: bold; margin-bottom: 8px;">专辑介绍</div>
          <n-ellipsis :line-clamp="5">
            <div>{{ albumData.albumDescribe }}</div>

          </n-ellipsis>
        </div>
      </n-card>

      <n-card title="歌曲列表">
        <n-grid v-if="albumData.musics && albumData.musics.length > 0" responsive="screen" :x-gap="12" :y-gap="12" cols="1">
          <n-gi v-for="(song, index) in albumData.musics" :key="index">
            <n-card hoverable :bordered="false">
              <n-flex align="center">
                <div style="font-size: 16px; font-weight: bold; min-width: 20px; margin-right: 10px;">
                  {{ index + 1 }}
                </div>
                <n-image
                  :src="song.musicImage"
                  :width="75"
                  :height="75"
                  fallback-src="https://h5static.kuwo.cn/upload/image/4f768883f75b17a426c95b93692d98bec7d3ee9240f77f5ea68fc63870fdb050.png"
                  style="border-radius: 4px; margin-right: 10px;"
                />
                <n-flex vertical style="flex: 1;">
                  <n-ellipsis style="max-width: 90%">
                    <n-gradient-text :size="16" type="warning">
                      {{ song.musicName }}
                    </n-gradient-text>
                  </n-ellipsis>
                  <div style="font-size: 12px; color: #999;">
                    <n-tag v-for="(alias, index) in song.musicArtists" :key="index" style="margin-right: 5px;">
                      {{ alias }}
                    </n-tag>
                  </div>

                </n-flex>
                <n-flex vertical>
                  <n-tag v-if="song.bits.length>0" v-for="(bits, index) in song.bits"  type="info" @click="download(song, bits)" style="cursor: pointer;">
                    {{ bits }}
                  </n-tag>
                </n-flex>
                <n-button v-if="song.bits.length>0"
                  type="success"
                  ghost
                  size="small"
                  @click="playSong(song)"
                  style="margin-left: 10px;"
                >
                  播放
                </n-button>
              </n-flex>
            </n-card>
          </n-gi>
        </n-grid>
        <n-empty v-else description="暂无歌曲数据" />
      </n-card>
    </n-spin>
  </div>
</template>

<style scoped>
</style>