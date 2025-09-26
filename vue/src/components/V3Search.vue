<script setup lang="js">
import {ref, nextTick, h,defineComponent,inject,watch ,watchEffect  } from 'vue'
import configInfoStore from "../stores/config";
import playListStore from "../stores/playList";
import {
  searchTips,
  musicSearch,
  musicDownload,
  getArtistInfo,
  getAlbumInfo,
  musicDownloadAlbum,
  musicDownloadArtist
} from "../utils/api.js";
import {NButton, NSpace,NTag,NImage} from "naive-ui";
import {storeToRefs} from "pinia";
import ArtistInfo from "./ArtistInfo.vue";
import AlbumInfo from "./AlbumInfo.vue";


const stconfigInfoStore =configInfoStore()
const stplayListStore  = playListStore()





// 搜索关键字
let keyword_value = ref("")
// 每页长度（禁止修改）
let pageSize = ref(10)
// 页码
let pageIndex = ref(1)
// 每次查询数据
let list_data = ref([])
//总条数
let item_total = ref(1);
//搜索结果tips
let search_tips = ref([])

let show_spin = ref(false)
// 显示播放按钮
let showbutton = ref(stconfigInfoStore.showPlayButton)



// 控制歌手信息弹窗显示
let show_artist_modal = ref(false)
// 歌手信息数据
let artist_info_data = ref({

})

// 控制专辑信息弹窗显示
let show_album_modal = ref(false)
// 专辑信息数据
let album_info_data = ref({

})
watch(
    () => stconfigInfoStore.showPlayButton,
    (newValue, oldValue) => {
      console.log("v3搜索按钮显示：", stconfigInfoStore.showPlayButton)
      showbutton.value = stconfigInfoStore.showPlayButton
    }
);



// onMounted(() => {
//
// })



// 播放整张专辑
const playAlbum = async (row) => {
  // 显示加载状态
  show_spin.value = true

  try {
    // 获取专辑详细信息
    const response = await getAlbumInfo(row.albumid, row.plugName)

    if (response.data.code === 200) {
      const albumData = response.data.data

      if (!albumData.musics || albumData.musics.length === 0) {
        window.$message.warning("该专辑暂无歌曲")
        show_spin.value = false
        return
      }

      // 清空当前播放列表
      stplayListStore.clearPlayList()

      // 将专辑中的所有歌曲添加到播放列表
      for (let i = 0; i < albumData.musics.length; i++) {
        const data = albumData.musics[i]
        const songData = {
          id: data.id,
          name: data.musicName,
          artistName: data.musicArtists,
          artistids: data.artistsIds,
          pic: data.musicImage,
          albumName: data.musicAlbum,
          lyric: "",
          lyricId: data.id,
          plugName: row.plugName,
          duration: data.musicDuration,
          brTypes: data.bits
        }

        // 添加到播放列表
        stplayListStore.pushPlayList(songData)
      }

      // 播放第一首歌曲
      if (stplayListStore.playList.length > 0) {
        stplayListStore.setPlayIndex(0)
        window.$message.success("开始播放专辑《" + albumData.albumName + "》")
      }
    } else {
      window.$message.error("获取专辑信息失败：" + response.data.msg)
    }
  } catch (error) {
    console.error("播放专辑时发生错误：", error)
    window.$message.error("播放专辑时发生错误")
  } finally {
    show_spin.value = false
  }
}

//搜索提示每次触发
let search_tips_trigger = (value)=>{
  searchTips(plugType_value.value, value).then(svalue=>{
    nextTick(()=>{
      search_tips.value = svalue.data.data
      search_tips.value.unshift(value)
    })
  })
}


const paginationRef = computed(() => ({
  pageSize: pageSize.value,
  page: pageIndex.value,
  itemCount:item_total.value
}))


let handlePageChange=(curPage)=>{
  pageIndex.value=curPage;
  onSecrch();
}
let pageSizeUpdata=(number)=>{
  pageSizes.value = number
  onSecrch();
}


//点击搜索或者回车的搜索
let onSecrch = ()=>{
  window.$loadingBar.start()
  show_spin.value=true;
  musicSearch(plugType_value.value, shear_select_value.value, keyword_value.value, pageSize.value, pageIndex.value).then(value=>{
    // window.$message.info("搜索成功:"+keyword_value.value+"  searType："+shear_select_value.value+"  plugName："+plugType_value.value)
    if (value.data.code === 200) {
      nextTick(()=>{
        list_data.value = value.data.data.records;
        item_total.value =value.data.data.searchTotal;
        window.$loadingBar.finish()
      })
    }else{
      window.$loadingBar.error()
    }
    show_spin.value=false;
  })
}

//时间格式化
let formateTime = (duration) => {
  var milliseconds = Math.floor((duration % 1000) / 100),
      seconds = Math.floor((duration / 1000) % 60),
      minutes = Math.floor((duration / (1000 * 60)) % 60),
      hours = Math.floor((duration / (1000 * 60 * 60)) % 24);

  // 格式化分钟和秒数，确保至少两位数
  minutes = (minutes < 10) ? "0" + minutes : minutes;
  seconds = (seconds < 10) ? "0" + seconds : seconds;

  // 构建时间字符串
  let timeString = "";

  // 只有当小时大于0时才显示小时部分
  if (hours > 0) {
    timeString += hours + ":";
  }

  timeString += minutes + ":" + seconds;

  // 只有当毫秒大于0时才显示毫秒部分
  if (milliseconds > 0) {
    timeString += "." + milliseconds;
  }

  return timeString;
}


//单曲表格列
let TableColumns = [
  {
    title: '封面',
    key: 'pic',
    maxWidth: 200,
    width: 100,
    minWidth: 100,
    resizable: true,
    align: 'center',
    ellipsis: {
      tooltip: true
    },
    render(row) {
      return h(NImage, {
        src: row.pic,
        lazy: true,
        height: 45,
        width: 45,
        fallbackSrc:"https://h5static.kuwo.cn/upload/image/4f768883f75b17a426c95b93692d98bec7d3ee9240f77f5ea68fc63870fdb050.png",
        onError: () => {
          window.$message.error("图片加载失败")
        }
      });
    }
  },
  {
    title: '歌曲名称',
    key: 'name',
    maxWidth: 300,
    width: 200,
    minWidth: 150,
    resizable: true,
    align: 'center',
    ellipsis: {
      tooltip: true
    }
  },
    {
      title: '歌手',
      key: 'artistName',
      maxWidth: 300,
      width: 250,
      minWidth: 150,
      resizable: true,
      align: 'center',
      ellipsis: {
        tooltip: true
      },
      render(row) {
        const tags = row.artistName.map((tagKey,index) => {
          return h(
              NTag,
              {
                style: {
                  marginRight: '6px',
                  cursor: 'pointer'
                },
                type: 'info',
                bordered: false,
                onClick: () => {
                  // getArtistInfo
                  artist_info_data={"id":row.artistids[index],"plugName":row.plugName}
                  show_artist_modal.value = true
                }

              },
              {
                default: () => tagKey
              }
          )
        })
        return tags
      }
    },
    {
      title: '专辑',
      key: 'albumName',
      maxWidth: 300,
      minWidth: 100,
      resizable: true,
      align: 'center',
      width: 250,
      ellipsis: {
        tooltip: true
      },
      render(row) {
        if (row.albumName){
          return h(NTag,{
            type: 'error',
            bordered: false,
            strong: true,
            style: {
              cursor: 'pointer'
            },
            onClick:() => {
              // getAlbumInfo
              album_info_data={"id":row.albumid,"plugName":row.plugName}
              show_album_modal.value = true
            }
          },{
            default: () => row.albumName
          })
        }

      }

    },
    {
      title: '时长',
      key: 'duration',
      maxWidth: 100,
      minWidth: 50,
      width: 100,
      resizable: true,
      align: 'center',
      ellipsis: {
        tooltip: true
      },
      render(row) {
        return h(NTag,{
          type: 'success',
          bordered: false,
          strong: true,

        },{
          default: () => formateTime(row.duration)
        })
      }
    },
  {
    title: '码率(下载)',
    key: 'brTypes',
    resizable: true,
    align: 'left',
    ellipsis: {
      tooltip: true
    },
    render(row) {
      const tags = row.brTypes.map((tagKey) => {
        return h(
            NButton,
            {
              style: {
                 marginRight: '6px'
              },
              ghost: true,
              type: 'warning',
              onClick: () => {
                musicDownload(row,tagKey).then(value=>{
                  if (value.data.code===200){
                    window.$message.success("开始下载")
                  }else{
                    window.$message.error("操作失败："+value.data.msg)
                  }
                })

              }
            },
            {
              default: () => tagKey
            }
        )
      })
      return tags
    }
  },
]
//专辑表格列表
let album_TableColumns = [
  {
    title: '封面',
    key: 'pic',
    maxWidth: 200,
    width: 100,
    minWidth: 100,
    resizable: true,
    align: 'center',
    ellipsis: {
      tooltip: true
    },
    render(row) {
      return h(NImage, {
        src: row.pic,
        lazy: true,
        height: 45,
        width: 45,
        fallbackSrc:"https://h5static.kuwo.cn/upload/image/4f768883f75b17a426c95b93692d98bec7d3ee9240f77f5ea68fc63870fdb050.png",
        onError: () => {
          window.$message.error("图片加载失败")
        }
      });
    },

  },
  {
    title: '歌手',
    key: 'artistName',
    maxWidth: 300,
    width: 250,
    minWidth: 150,
    resizable: true,
    align: 'center',
    ellipsis: {
      tooltip: true
    },
    render(row) {
        return h(
            NTag,
            {
              style: {
                marginRight: '6px',
                cursor: 'pointer'
              },
              type: 'info',
              bordered: false,
              onClick: () => {
                // getArtistInfo
                artist_info_data={"id":row.artistid,"plugName":row.plugName}
                show_artist_modal.value = true
              }

            },
            {
              default: () => row.artistName
            }
        )

    }
  },
  {
    title: '专辑',
    key: 'albumName',
    maxWidth: 300,
    minWidth: 100,
    resizable: true,
    align: 'center',
    width: 250,
    ellipsis: {
      tooltip: true
    },
    render(row) {
      if (row.albumName){
        return h(NTag,{
          type: 'error',
          bordered: false,
          strong: true,
          style: {
            cursor: 'pointer'
          },
          onClick:() => {
            // getAlbumInfo
            album_info_data={"id":row.albumid,"plugName":row.plugName}
            show_album_modal.value = true
          }
        },{
          default: () => row.albumName
        })
      }

    }

  },

  {
    title: '操作',
    maxWidth: 200,
    width: 150,
    minWidth: 150,
    resizable: true,
    align: 'left',
    ellipsis: {
      tooltip: true
    },
    render(row) {
      if (showbutton.value){
        return [
          h(
              NButton,
              {
                style: {
                  marginRight: '6px'
                },
                ghost: true,
                type: 'success',
                onClick: () => {
                  //一会写下载专辑接口
                  musicDownloadAlbum(row).then(value=>{
                    if (value.data.code===200){
                      window.$message.success("开始下载当前专辑：自动适配最高音质下载")
                    }else{
                      window.$message.error("操作失败："+value.data.msg)
                    }
                  })

                }
              },
              {
                default: () => '下载专辑'
              }
          )
        ]
      }
      return [
        h(
            NButton,
            {
              style: {
                marginRight: '6px'
              },
              ghost: true,
              type: 'primary',
              onClick: () => {
                playAlbum(row)
              }
            },
            {
              default: () => '播放专辑'
            }
        ),




        h(
          NButton,
          {
            style: {
              marginRight: '6px'
            },
            ghost: true,
            type: 'success',
            onClick: () => {
              //一会写下载专辑接口
              musicDownloadAlbum(row).then(value=>{
                if (value.data.code===200){
                  window.$message.success("开始下载当前专辑：自动适配最高音质下载")
                }else{
                  window.$message.error("操作失败："+value.data.msg)
                }
              })

            }
          },
          {
            default: () => '下载专辑'
          }
        )
      ]
    }
  },
]
//歌手表格列
let artist_TableColumns = [
  {
    title: '封面',
    key: 'pic',
    maxWidth: 200,
    width: 100,
    minWidth: 100,
    resizable: true,
    align: 'center',
    ellipsis: {
      tooltip: true
    },
    render(row) {
      return h(NImage, {
        src: row.pic,
        lazy: true,
        height: 45,
        width: 45,
        fallbackSrc:"https://h5static.kuwo.cn/upload/image/4f768883f75b17a426c95b93692d98bec7d3ee9240f77f5ea68fc63870fdb050.png",
        onError: () => {
          window.$message.error("图片加载失败")
        }
      });
    },

  },
  {
    title: '歌手',
    key: 'artistName',
    maxWidth: 300,
    width: 250,
    minWidth: 150,
    resizable: true,
    align: 'center',
    ellipsis: {
      tooltip: true
    },
    render(row) {
        return h(
            NTag,
            {
              style: {
                marginRight: '6px',
                cursor: 'pointer'
              },
              type: 'info',
              bordered: false,
              onClick: () => {
                // getArtistInfo
                artist_info_data={"id":row.artistid,"plugName":row.plugName}
                show_artist_modal.value = true
              }

            },
            {
              default: () => row.artistName
            }
        )

    }
  },
  {
    title: '专辑数',
    key: 'total',
    maxWidth: 100,
    minWidth: 50,
    width: 100,
    resizable: true,
    align: 'center',
    ellipsis: {
      tooltip: true
    }
  },
  {
    title: '操作',
    maxWidth: 200,
    width: 100,
    minWidth: 100,
    resizable: true,
    align: 'left',
    ellipsis: {
      tooltip: true
    },
    render(row) {
      return h(
          NButton,
          {
            style: {
              marginRight: '6px'
            },
            ghost: true,
            type: 'success',
            onClick: () => {
              //一会写下载歌手全部专辑接口
              musicDownloadArtist(row).then(value=>{
                if (value.data.code===200){
                  window.$message.success("开始下载当前歌手：自动适配最高音质下载")
                }else{
                  window.$message.error("操作失败："+value.data.msg)
                }
              })

            }
          },
          {
            default: () => '下载全部专辑'
          }
      )
    }
  },
]

// 根据搜索类型计算应该使用的表格列
const computedTableColumns = computed(() => {
  if (shear_select_value.value === 'album') {
    return album_TableColumns;
  } else if (shear_select_value.value === 'artist') {
    return artist_TableColumns;
  } else {
    if (showbutton.value){
      const baseColumns = [...TableColumns];
      baseColumns.push({
        title: '播放',
        maxWidth: 200,
        width: 100,
        minWidth: 100,
        resizable: true,
        align: 'left',
        ellipsis: {
          tooltip: true
        },
        render(row) {
          return h(
              NButton,
              {
                style: {
                  marginRight: '6px'
                },
                ghost: true,
                type: 'success',
                onClick: () => {
                  stplayListStore.pushPlayListAndPlay(row)
                }
              },
              {
                default: () => '播放'
              }
          )
        }
      });
      return baseColumns;
    }else{
      return TableColumns;
    }
  }
});

//下拉选项默认值
let select_options = ref(stconfigInfoStore.getOption)
// 搜索类型
let plugType_value = ref("kw")
// 二级下拉
let shear_select_options=ref([
  {
    label: "单曲",
    value: "music"
  },
  {
    label: "专辑",
    value: "album"
  },
  {
    label: "歌手",
    value: "artist"
  }
])
let shear_select_value=ref("music")

//清空搜索结果
let update_select_type=(value, option)=>{
  pageIndex.value=1
  list_data.value=[]
}
</script>

<template>

    <n-spin :show="show_spin">
   <div>
    <n-form
      inline
      @keyup.enter.native="onSecrch"
      style="width: 100%;"
  >

    <div class="sqFlex" style="width: 100%;">
      <div class="sqRow" style="width: 100%;">
        <div class="sqRow" style="width: 30%;">
          <n-select
              v-model:value="plugType_value"
              placeholder="Select"
              :options="select_options"
              @update:value="update_select_type"
          />
          <n-select
              v-model:value="shear_select_value"
              placeholder="Select"
              :options="shear_select_options"
              @update:value="update_select_type"
          />
        </div>
        <div  class="sqRow" style="width: 70%;" >
          <n-auto-complete
              v-model:value="keyword_value"
              :loading="false"
              @input="search_tips_trigger"
              :input-props="{
               autocomplete: 'disabled',
              }"
              :options="search_tips"
              placeholder="关键字"
              clearable
          />
          <n-button attr-type="button" @click="onSecrch">
            搜索
          </n-button>
        </div>
      </div>
<!--      <n-divider />-->
      <!--表格数据行-->
      <div class="sqRow" >
        <n-data-table
            :flex-height="false"
            :key="(row) => row.id"
            :bordered="false"
            :single-line="false"
            :columns="computedTableColumns"
            :data="list_data"
            remote
            :on-update:page="handlePageChange"
        />




      </div>
      <n-flex justify="center">
        <n-card title="">
          <n-flex justify="center">
            <n-pagination
                v-model:page="pageIndex"
                :item-count="item_total"
                size="large"
                :page-sizes="[10,20,50,100]"
                show-quick-jumper
                show-size-picker
                @update:page="handlePageChange"
                @update:page-size="pageSizeUpdata"
            >
              <template #goto >
                跳转
              </template>
              <template #suffix>
                页
              </template>

            </n-pagination>
          </n-flex>

        </n-card>
      </n-flex>

    </div>




  </n-form>
   </div>

    </n-spin>




  <!-- 歌手信息弹窗 -->
  <n-modal v-model:show="show_artist_modal" preset="card" style="width: 90%; height: 90%;" :title="artist_info_data.musicArtistsName">
    <ArtistInfo
        :id="artist_info_data.id"
        :plugName="artist_info_data.plugName"
    />
  </n-modal>

  <!-- 专辑信息弹窗 -->
  <n-modal v-model:show="show_album_modal" preset="card" style="width: 90%; height: 90%;" :title="album_info_data.albumName">
    <AlbumInfo
        :id="album_info_data.id"
        :plugName="album_info_data.plugName"
    />
  </n-modal>

</template>

<style scoped>
.headerSearch{
  display: flex;
  flex-direction: row;
  width: 30%;
  justify-content: center;
}
.headerInput{
   display: flex;
   width: 60%;
 }
.sqFlex{
  flex-direction: column;
  justify-content: flex-start;
  display: inline-flex;
}
.sqRow{
  display: inline-flex;
  flex-direction: row;
  justify-content: center;
}
.hover-hand {
  cursor: pointer;
  transition: all 0.3s ease;
}
:deep(.n-data-table__pagination) {
  display: flex !important;
  justify-content: center !important;
}
</style>