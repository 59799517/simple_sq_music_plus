<script setup lang="js">
import {ref, nextTick, h,defineComponent,inject,watch ,watchEffect, computed, onMounted, onBeforeUnmount } from 'vue'
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
import {NButton, NSpace,NTag,NImage, NGrid, NGridItem, NCard, NAvatar} from "naive-ui";
import {storeToRefs} from "pinia";
import ArtistInfo from "./ArtistInfo.vue";
import AlbumInfo from "./AlbumInfo.vue";


const stconfigInfoStore =configInfoStore()
const stplayListStore  = playListStore()

// 检测是否为移动设备
const isMobile = computed(() => {
  return /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent)
})

// 回到顶部按钮显示控制
let showBackTop = ref(false)

// 监听滚动事件
let handleScroll = () => {
  showBackTop.value = window.scrollY > 300
}

// 回到顶部方法
let backToTop = () => {
  window.scrollTo({
    top: 0,
    behavior: 'smooth'
  })
}

onMounted(() => {
  window.addEventListener('scroll', handleScroll)
})

onBeforeUnmount(() => {
  window.removeEventListener('scroll', handleScroll)
})

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
        },
        style: {
          borderRadius: '4px'
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
                 marginRight: '6px',
                 marginTop: '4px'
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
      return h('div', {
        style: {
          display: 'flex',
          flexWrap: 'wrap'
        }
      }, [
        h('span', {
          style: {
            display: 'inline-block',
            minWidth: '40px',
            marginRight: '6px'
          }
        }, '码率: '),
        h('div', {
          style: {
            display: 'inline-flex',
            flexWrap: 'wrap',
            maxWidth: 'calc(100% - 50px)'
          }
        }, tags)
      ])
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
        },
        style: {
          borderRadius: '4px'
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
        },
        style: {
          borderRadius: '50%'
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

// 渲染单曲卡片
const renderMusicCard = (item) => {
  return h(NCard, {
    style: {
      marginBottom: '16px'
    }
  }, {
    default: () => [
      // 第一行：图片
      h('div', { 
        style: { 
          display: 'flex',
          justifyContent: 'center',
          marginBottom: '12px'
        } 
      }, [
        h(NImage, {
          src: item.pic,
          fallbackSrc: "https://h5static.kuwo.cn/upload/image/4f768883f75b17a426c95b93692d98bec7d3ee9240f77f5ea68fc63870fdb050.png",
          style: {
            width: '100%',
            height: '150px',
            borderRadius: '8px',
            objectFit: 'cover'
          }
        })
      ]),
      
      // 第二行：歌曲名称
      h('div', { 
        style: { 
          fontWeight: 'bold', 
          fontSize: '16px',
          marginBottom: '8px',
          textAlign: 'left'
        } 
      }, item.name),
      
      // 第三行：歌手（可以点击）
      h('div', { 
        style: { 
          marginBottom: '8px'
        } 
      }, [
        h('span', { 
          style: { 
            color: '#666', 
            fontSize: '14px',
            marginRight: '6px'
          } 
        }, '歌手: '),
        ...item.artistName.map((artist, index) => 
          h(NTag, {
            style: {
              marginRight: '6px',
              marginTop: '4px',
              cursor: 'pointer'
            },
            type: 'info',
            bordered: false,
            onClick: () => {
              artist_info_data.value = { "id": item.artistids[index], "plugName": item.plugName }
              show_artist_modal.value = true
            }
          }, { default: () => artist })
        )
      ]),
      
      // 第四行：专辑信息（可以点击）
      h('div', { 
        style: { 
          marginBottom: '8px'
        } 
      }, [
        h('span', { 
          style: { 
            color: '#666', 
            fontSize: '14px',
            marginRight: '6px'
          } 
        }, '专辑: '),
        h(NTag, {
          type: 'error',
          bordered: false,
          strong: true,
          style: {
            cursor: 'pointer',
            marginTop: '4px'
          },
          onClick: () => {
            album_info_data.value = { "id": item.albumid, "plugName": item.plugName }
            show_album_modal.value = true
          }
        }, { default: () => item.albumName })
      ]),
      
      // 第五行：时长信息
      h('div', { 
        style: { 
          marginBottom: '8px'
        } 
      }, [
        h('span', { 
          style: { 
            color: '#666', 
            fontSize: '14px',
            marginRight: '6px'
          } 
        }, '时长: '),
        h(NTag, {
          type: 'success',
          bordered: false,
          strong: true,
          style: {
            marginTop: '4px'
          }
        }, { default: () => formateTime(item.duration) })
      ]),
      
      // 第六行：码率
      h('div', { 
        style: { 
          marginBottom: '12px'
        } 
      }, [
        h('div', {
          style: {
            display: 'flex',
            alignItems: 'flex-start'
          }
        }, [
          h('span', { 
            style: { 
              color: '#666', 
              fontSize: '14px', 
              marginRight: '6px',
              flexShrink: 0
            } 
          }, '码率: '),
          h('div', {
            style: {
              display: 'flex',
              flexWrap: 'wrap',
              flex: 1
            }
          }, [
            ...item.brTypes.map(tagKey => 
              h(NButton, {
                style: {
                  marginRight: '6px',
                  marginTop: '4px'
                },
                size: 'small',
                ghost: true,
                type: 'warning',
                onClick: () => {
                  musicDownload(item, tagKey).then(value => {
                    if (value.data.code === 200) {
                      window.$message.success("开始下载")
                    } else {
                      window.$message.error("操作失败：" + value.data.msg)
                    }
                  })
                }
              }, { default: () => tagKey })
            )
          ])
        ])
      ]),
      
      // 第七行：播放按钮
      showbutton.value ? 
        h('div', {
          style: {
            display: 'flex',
            justifyContent: 'flex-start'
          }
        }, [
          h(NButton, {
            style: {
              marginRight: '6px'
            },
            ghost: true,
            type: 'success',
            onClick: () => {
              stplayListStore.pushPlayListAndPlay(item)
            }
          }, { default: () => '播放' })
        ]) : 
        null
    ]
  })
}

// 渲染专辑卡片
const renderAlbumCard = (item) => {
  return h(NCard, {
    style: {
      marginBottom: '16px'
    }
  }, {
    cover: () => h(NImage, {
      src: item.pic,
      lazy: true,
      height: 150,
      width: '100%',
      fallbackSrc: "https://h5static.kuwo.cn/upload/image/4f768883f75b17a426c95b93692d98bec7d3ee9240f77f5ea68fc63870fdb050.png",
      onError: () => {
        window.$message.error("图片加载失败")
      },
      style: {
        objectFit: 'cover',
        borderRadius: '8px'
      }
    }),
    default: () => [
      h('div', { style: { fontWeight: 'bold', fontSize: '16px', marginBottom: '8px' } }, item.albumName),
      h('div', { style: { marginBottom: '8px' } }, [
        h('span', { style: { color: '#666', fontSize: '14px' } }, '歌手: '),
        h(NTag, {
          style: {
            marginRight: '6px',
            marginTop: '4px',
            cursor: 'pointer'
          },
          type: 'info',
          bordered: false,
          onClick: () => {
            artist_info_data.value = { "id": item.artistid, "plugName": item.plugName }
            show_artist_modal.value = true
          }
        }, { default: () => item.artistName })
      ]),
      h(NSpace, { style: { marginTop: '12px' } }, () => [
        h(NButton, {
          style: {
            marginRight: '6px'
          },
          ghost: true,
          type: 'primary',
          onClick: () => {
            playAlbum(item)
          }
        }, { default: () => '播放专辑' }),
        h(NButton, {
          style: {
            marginRight: '6px'
          },
          ghost: true,
          type: 'success',
          onClick: () => {
            musicDownloadAlbum(item).then(value => {
              if (value.data.code === 200) {
                window.$message.success("开始下载当前专辑：自动适配最高音质下载")
              } else {
                window.$message.error("操作失败：" + value.data.msg)
              }
            })
          }
        }, { default: () => '下载专辑' })
      ])
    ]
  })
}

// 渲染歌手卡片
const renderArtistCard = (item) => {
  return h(NCard, {
    style: {
      marginBottom: '16px'
    }
  }, {
    cover: () => h(NAvatar, {
      src: item.pic,
      lazy: true,
      round: false,
      height: 150,
      width: '100%',
      fallbackSrc: "https://h5static.kuwo.cn/upload/image/4f768883f75b17a426c95b93692d98bec7d3ee9240f77f5ea68fc63870fdb050.png",
      onError: () => {
        window.$message.error("图片加载失败")
      },
      style: {
        objectFit: 'cover',
        borderRadius: '8px'
      }
    }),
    default: () => [
      h('div', { style: { fontWeight: 'bold', fontSize: '16px', marginBottom: '8px' } }, item.artistName),
      h('div', { style: { marginBottom: '8px' } }, [
        h('span', { style: { color: '#666', fontSize: '14px' } }, '专辑数: '),
        h(NTag, {
          type: 'info',
          bordered: false,
          strong: true,
          style: {
            marginTop: '4px'
          }
        }, { default: () => item.total })
      ]),
      h(NButton, {
        style: {
          marginTop: '12px'
        },
        ghost: true,
        type: 'success',
        onClick: () => {
          musicDownloadArtist(item).then(value => {
            if (value.data.code === 200) {
              window.$message.success("开始下载当前歌手：自动适配最高音质下载")
            } else {
              window.$message.error("操作失败：" + value.data.msg)
            }
          })
        }
      }, { default: () => '下载全部专辑' })
    ]
  })
}

// 根据搜索类型渲染对应的卡片
const renderCard = (item) => {
  if (shear_select_value.value === 'album') {
    return renderAlbumCard(item)
  } else if (shear_select_value.value === 'artist') {
    return renderArtistCard(item)
  } else {
    return renderMusicCard(item)
  }
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
        <!-- 桌面端显示表格 -->
        <n-data-table
            v-if="!isMobile"
            :flex-height="false"
            :key="(row) => row.id"
            :bordered="false"
            :single-line="false"
            :columns="computedTableColumns"
            :data="list_data"
            remote
            :on-update:page="handlePageChange"
        />
        
        <!-- 移动端显示卡片 -->
        <div v-else>
          <n-grid :cols="1" responsive="screen">
            <n-grid-item v-for="item in list_data" :key="item.id">
              <component :is="renderCard(item)" />
            </n-grid-item>
          </n-grid>
        </div>
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

    <!-- 回到顶部按钮 -->
    <transition name="fade">
      <div v-if="showBackTop" class="back-top" @click="backToTop">
        <n-button circle type="primary" size="large">
          <template #icon>
            <n-icon>
              <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor">
                <path d="M12 4l-8 8h6v8h4v-8h6l-8-8z"/>
              </svg>
            </n-icon>
          </template>
        </n-button>
      </div>
    </transition>
  
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

/* 移动端适配样式 */
@media (max-width: 768px) {
  .sqRow {
    flex-direction: column;
    width: 100% !important;
  }
  
  .sqRow > div {
    width: 100% !important;
    margin-bottom: 10px;
  }
  
  .n-pagination {
    flex-wrap: wrap;
  }
  
  /* 优化移动端卡片样式 */
  :deep(.n-card) {
    border-radius: 8px;
    overflow: hidden;
  }
  
  /* 优化移动端图片展示 */
  :deep(.n-image img) {
    border-radius: 8px;
    transition: transform 0.3s ease;
  }
  
  :deep(.n-image img:hover) {
    transform: scale(1.02);
  }
  
  /* 优化移动端标签显示 */
  :deep(.n-tag) {
    margin-top: 4px;
    margin-bottom: 4px;
  }
  
  /* 优化按钮在小屏幕上的显示 */
  :deep(.n-button) {
    margin-top: 4px;
    margin-bottom: 4px;
  }
  
  /* 确保码率按钮不会超出容器 */
  :deep(.n-button) {
    white-space: nowrap;
  }
}

/* 回到顶部按钮样式 */
.back-top {
  position: fixed;
  bottom: 40px;
  right: 40px;
  z-index: 999;
}

/* 淡入淡出动画 */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>