<script setup lang="js">
import {ref, nextTick, h,defineComponent} from 'vue'
import configInfoStore from "../stores/config";
import {searchTips, musicSearch, musicDownload} from "../utils/api.js";
import {NButton, NSpace,NTag,NImage} from "naive-ui";


const stconfigInfoStore =configInfoStore()


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


//点击搜索或者回车的搜索
let onSecrch = ()=>{
  window.$loadingBar.start()
  show_spin.value=true;
  musicSearch(plugType_value.value, shear_select_value.value, keyword_value.value, pageSize.value, pageIndex.value).then(value=>{
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


//表格列
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
        const tags = row.artistName.map((tagKey) => {
          return h(
              NTag,
              {
                style: {
                  marginRight: '6px'
                },
                type: 'info',
                bordered: false
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
          strong: true
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
                console.log("点击下载按钮："+JSON.stringify(row))
                musicDownload(row,tagKey).then(value=>{
                  if (value.data.code===200){
                    window.$message.success("操作成功")
                  }else{
                    window.$message.error("操作失败："+value.data.msg)
                  }
                })
                // delDownloadInfo(row.id).then(value=>{
                //   if (value.data.code===200){
                    window.$message.success("操作成功点击了："+tagKey)
                //   }else{
                //     window.$message.error("操作失败："+value.data.msg)
                //   }
                // })
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
              console.log("点击下载按钮："+JSON.stringify(row))
              // delDownloadInfo(row.id).then(value=>{
              //   if (value.data.code===200){
              window.$message.success("操作成功点击了："+"播放")
              //   }else{
              //     window.$message.error("操作失败："+value.data.msg)
              //   }
              // })
            }
          },
          {
            default: () => '播放'
          }
      )
    }
  },
]






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
  <div style="width: 100%;
" ref="loadingBarTargetRef">
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
      <n-divider />

      <!--表格数据行-->
      <div class="sqRow" v-if="list_data.length>0">
        <n-data-table


            :key="(row) => row.id"
            :bordered="false"
            :single-line="false"
            :columns="TableColumns"
            :data="list_data"
            remote
            :pagination="paginationRef"
            :on-update:page="handlePageChange"
        />




      </div>

    </div>




  </n-form>

  </div>

    </n-spin>
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
</style>