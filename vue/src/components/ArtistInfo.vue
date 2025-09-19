<script setup lang="js">
import { ref, onMounted } from 'vue'
import { NImage, NGrid, NGridItem, NCard, NSpace, NTag, NThing, NAvatar, NEmpty, NSpin, NModal, NButton, NMessageProvider } from 'naive-ui'
import { getArtistInfo, musicDownloadArtist } from '../utils/api.js'
import AlbumInfo from './AlbumInfo.vue'

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

const artistData = ref({
})

const loading = ref(true)
const showModal = ref(false)
const selectedAlbumId = ref('')

// 解码函数，支持多种编码格式
const decodeText = (text) => {
  if (!text) return text;

  let decodedText = text;

  // HTML 实体字符映射表
  const entities = {
    '&nbsp;': ' ',
    '&amp;': '&',
    '&lt;': '<',
    '&gt;': '>',
    '&quot;': '"',
    '&apos;': "'",
    '&cent;': '¢',
    '&pound;': '£',
    '&yen;': '¥',
    '&euro;': '€',
    '&copy;': '©',
    '&reg;': '®'
  };

  // 替换已知的 HTML 实体字符
  for (const [entity, char] of Object.entries(entities)) {
    decodedText = decodedText.replace(new RegExp(entity, 'g'), char);
  }

  // 处理数字实体 &#123; 或 &#x123;
  decodedText = decodedText.replace(/&#(\d+);/g, (match, dec) => {
    return String.fromCharCode(dec);
  });

  decodedText = decodedText.replace(/&#x([0-9a-fA-F]+);/g, (match, hex) => {
    return String.fromCharCode(parseInt(hex, 16));
  });

  return decodedText;
}

// 打开专辑弹出层
const openAlbumModal = (albumId) => {
  selectedAlbumId.value = albumId
  showModal.value = true
}

// 下载歌手全部专辑
const downloadAllAlbums = () => {
  if (!artistData.value || !artistData.value.albums || artistData.value.albums.length === 0) {
    window.$message.warning("该歌手暂无专辑可下载")
    return
  }

  const data = {
    artistid: props.id,
    artistName: artistData.musicArtistsName,
    plugName: props.plugName
  }

  musicDownloadArtist(data).then(response => {
    if (response.data.code === 200) {
      window.$message.success("开始下载当前歌手：自动适配最高音质下载")
    } else {
      window.$message.error("下载失败: " + response.data.msg)
    }
  }).catch(error => {
    console.error("下载歌手全部专辑失败：", error)
    window.$message.error("下载请求失败")
  })
}

onMounted(() => {
  console.log("当前歌手参数是：id=" + props.id + ", plugName=" + props.plugName)
  getArtistInfo(props.id, props.plugName).then(response => {
    console.log("当前歌手数据是：" + JSON.stringify(response.data.data))
    artistData.value = response.data.data
    loading.value = false
  }).catch(error => {
    console.error("获取歌手信息失败：", error)
    loading.value = false
  })
})
</script>

<template>
  <div>
    <n-spin :show="loading">
      <n-card v-if="artistData.musicArtistsName" :title="decodeText(artistData.musicArtistsName)">
      <n-flex justify="space-between">
        <n-space align="center">
          <n-avatar
              :src="artistData.musicArtistsPhoto"
              :size="80"
              fallback-src="https://h5static.kuwo.cn/upload/image/4f768883f75b17a426c95b93692d98bec7d3ee9240f77f5ea68fc63870fdb050.png"
          />
          <div>
            <n-ellipsis style="max-width: 240px">
              <div style="font-size: 18px; font-weight: bold;">{{ decodeText(artistData.musicArtistsName) }}</div>

            </n-ellipsis>
            <div v-if="artistData.musicArtistsAlias && artistData.musicArtistsAlias.length > 0">
              <n-tag  type="info" style="margin-right: 5px;">
                {{ decodeText(artistData.musicArtistsAlias) }}
              </n-tag>
            </div>
          </div>
        </n-space>
        <n-button :on-click="downloadAllAlbums">
          下载歌手全部专辑
        </n-button>
      </n-flex>




      </n-card>

      <n-card title="专辑列表">
        <n-grid v-if="artistData.albums && artistData.albums.length > 0" responsive="screen" :x-gap="12" :y-gap="12" :cols="2">
          <n-gi  v-for="(album, index) in artistData.albums" :key="index">
            <n-card hoverable :bordered="false" @click="openAlbumModal(album.albumId)" style="cursor: pointer;">

              <n-flex>


                <n-image
                    :src="album.albumImg"
                    :width="100"
                    :height="100"
                    fallback-src="https://h5static.kuwo.cn/upload/image/4f768883f75b17a426c95b93692d98bec7d3ee9240f77f5ea68fc63870fdb050.png"
                    style="border-radius: 4px;"
                    preview-disabled
                />
                <n-flex vertical>
                  <n-ellipsis style="max-width: 180px">
                  <n-gradient-text :size="18" type="warning">
                    {{album.albumName}}
                  </n-gradient-text>
                  </n-ellipsis>
                  <div style="font-size: 12px; color: #999;">{{ decodeText(album.albumTime) }}</div>
  <!--                <n-button>-->
  <!--                  查看-->
  <!--                </n-button>-->

                </n-flex>
              </n-flex>

            </n-card>

          </n-gi>

        </n-grid>
        <n-empty v-else description="暂无专辑数据" />
      </n-card>
    </n-spin>

    <!-- 专辑详情弹出层 -->
    <n-modal v-model:show="showModal" preset="card" style="width: 90%; height: 90%;" :bordered="false">
      <AlbumInfo v-if="showModal" :id="selectedAlbumId" :plugName="plugName" />
    </n-modal>
  </div>
</template>

<style scoped>
</style>