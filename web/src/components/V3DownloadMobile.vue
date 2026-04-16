<script setup>
import { NButton, NTag, useMessage, NSpace, NList, NListItem, NThing, NCard, NDivider, NCollapse, NCollapseItem, NTooltip } from 'naive-ui'
import { h, ref, onBeforeMount } from 'vue'
import configInfoStore from "../stores/config";

import {
  againTask,
  delErrorTask,
  delSuccessTask,
  delWaitingTask,
  postDownloadInfo,
  refreshTask,
  delDownloadInfo,
  refreshStatus
} from "../utils/api.js";

const stconfigInfoStore = configInfoStore()

// 页面数据
let pageSizes = ref(10)
let page_index = ref(1);
let item_total = ref(1);
let page_data = ref([]);

// 搜索关键字
let keyword_music_value = ref("")
let keyword_artis_value = ref("")
let keyword_album_value = ref("")
let download_type_value = ref("")
let download_status_value = ref("")

// 时间范围
let download_time_start_value = ''
let download_time_end_value = ''

// 加载状态
let show_spin = ref(false)

// 是否显示高级功能
let show_advanced = ref(false)

// 下载状态列表
let select_download_status_options = ref([
  {
    label: "全部",
    value: ""
  },
  {
    label: "等待下载",
    value: "waiting"
  },
  {
    label: "下载中",
    value: "loading"
  },
  {
    label: "下载成功",
    value: "success"
  },
  {
    label: "下载失败",
    value: "error"
  }
])

// 下载类型选项
let select_download_type_options = ref([
  {
    label: "全部",
    value: ""
  }
])

let shortcuts = {
  昨天: () => new Date().getTime() - 24 * 60 * 60 * 1000,
  近2小时: () => {
    const cur = new Date().getTime();
    return [cur - 2 * 60 * 60 * 1000, cur];
  }
}

// 初始化
onBeforeMount(() => {
  select_download_type_options.value.push(...stconfigInfoStore.getOption)
  getDownloadData()
})

let getDownloadData = () => {
  window.$loadingBar.start()
  show_spin.value = true;
  postDownloadInfo(
    keyword_music_value.value,
    keyword_artis_value.value,
    keyword_album_value.value,
    download_type_value.value,
    download_status_value.value,
    download_time_start_value,
    download_time_end_value,
    pageSizes.value,
    page_index.value
  ).then(value => {
    show_spin.value = false;
    item_total.value = value.data.data.total
    page_data.value = value.data.data.records;
    window.$loadingBar.finish()
  })
}

// 按钮功能
let delErrorTask_b = () => {
  window.$dialog.warning({
    title: '警告',
    content: '确定执行此操作？',
    positiveText: '确定',
    negativeText: '取消',
    onPositiveClick: () => {
      delErrorTask().then(value => {
        if (value.data.code === 200) {
          window.$message.success("操作成功")
        } else {
          window.$message.error("操作失败：" + value.data.msg)
        }
      })
    },
    onNegativeClick: () => { }
  })
}

let delSuccessTask_b = () => {
  window.$dialog.warning({
    title: '警告',
    content: '确定执行此操作？',
    positiveText: '确定',
    negativeText: '取消',
    onPositiveClick: () => {
      delSuccessTask().then(value => {
        if (value.data.code === 200) {
          window.$message.success("操作成功")
        } else {
          window.$message.error("操作失败：" + value.data.msg)
        }
      })
    },
    onNegativeClick: () => { }
  })
}

let delWaitingTask_b = () => {
  window.$dialog.warning({
    title: '警告',
    content: '确定执行此操作？',
    positiveText: '确定',
    negativeText: '取消',
    onPositiveClick: () => {
      delWaitingTask().then(value => {
        if (value.data.code === 200) {
          window.$message.success("操作成功")
        } else {
          window.$message.error("操作失败：" + value.data.msg)
        }
      })
    },
    onNegativeClick: () => { }
  })
}

let refreshTask_b = () => {
  window.$dialog.warning({
    title: '警告',
    content: '确定执行此操作？',
    positiveText: '确定',
    negativeText: '取消',
    onPositiveClick: () => {
      refreshTask().then(value => {
        if (value.data.code === 200) {
          window.$message.success("操作成功")
        } else {
          window.$message.error("操作失败：" + value.data.msg)
        }
      })
    },
    onNegativeClick: () => { }
  })
}

let pageUpdata = (number) => {
  page_index.value = number
  getDownloadData();
}

let pageSizeUpdata = (number) => {
  pageSizes.value = number
  getDownloadData();
}

// 下拉选择
let update_download_type = (value, option) => {
  download_type_value.value = value
}

let update_download_status = (value, option) => {
  download_status_value.value = value
}

// 删除下载信息
let handleDelete = (id) => {
  window.$dialog.warning({
    title: '警告',
    content: '确定删除此条记录？',
    positiveText: '确定',
    negativeText: '取消',
    onPositiveClick: () => {
      delDownloadInfo(id).then(value => {
        if (value.data.code === 200) {
          window.$message.success("操作成功")
          getDownloadData()
        } else {
          window.$message.error("操作失败：" + value.data.msg)
        }
      })
    }
  })
}

// 重新下载
let handleRefresh = (id) => {
  refreshStatus(id).then(value => {
    if (value.data.code === 200) {
      window.$message.success("操作成功")
      getDownloadData()
    } else {
      window.$message.error("操作失败：" + value.data.msg)
    }
  })
}

// 获取状态标签
let getStatusTag = (status) => {
  switch (status) {
    case "waiting":
      return h(NTag, {
        type: 'info',
        style: { marginRight: '5px' }
      }, () => '等待下载')
    case "loading":
      return h(NTag, {
        type: 'info',
        style: { marginRight: '5px' }
      }, () => '下载中')
    case "success":
      return h(NTag, {
        type: 'success',
        style: { marginRight: '5px' }
      }, () => '下载成功')
    case "error":
      return h(NTag, {
        type: 'error',
        style: { marginRight: '5px' }
      }, () => '下载失败')
    default:
      return h(NTag, { type: 'default' }, () => status)
  }
}

// 获取音频书籍标签
let getAudioBookTag = (isAudioBook) => {
  return h(NTag, {
    type: isAudioBook === 1 ? 'success' : 'default',
    style: { marginRight: '5px' }
  }, () => isAudioBook === 1 ? '是' : '否')
}

// 获取下载类型标签
let getDownloadTypeTag = (type) => {
  return h(NTag, {
    type: 'success',
    style: { marginRight: '5px' }
  }, () => type)
}
</script>

<template>
  <n-spin :show="show_spin">
    <n-card title="下载管理" style="margin: 10px;">
      <!-- 搜索表单 -->
      <n-form @submit.prevent="getDownloadData">
        <n-form-item label="歌名">
          <n-input v-model:value="keyword_music_value" clearable placeholder="输入歌名" />
        </n-form-item>
        <n-form-item label="歌手">
          <n-input v-model:value="keyword_artis_value" clearable placeholder="歌手名称" />
        </n-form-item>
        <n-form-item label="专辑">
          <n-input v-model:value="keyword_album_value" clearable placeholder="专辑名称" />
        </n-form-item>
        <n-form-item label="数据来源">
          <n-select v-model:value="download_type_value" placeholder="选择数据来源" clearable
            :options="select_download_type_options" @update:value="update_download_type" />
        </n-form-item>
        <n-form-item label="下载状态">
          <n-select v-model:value="download_status_value" placeholder="选择下载状态" clearable
            :options="select_download_status_options" @update:value="update_download_status" />
        </n-form-item>
        <n-form-item label="下载时间">
          <n-date-picker :input-readonly="true" type="datetimerange" :shortcuts="shortcuts" :clearable="true"
            :update-value-on-close="true" :on-update-formatted-value="(value) => {
              download_time_start_value = value[0]
              download_time_end_value = value[1]
            }" />
        </n-form-item>
        <n-form-item>
          <n-button attr-type="button" @click="getDownloadData" type="primary" block>
            搜索
          </n-button>
        </n-form-item>
      </n-form>

      <!-- 高级功能 -->
      <n-button @click="show_advanced = !show_advanced" style="margin-bottom: 10px;" block>
        {{ show_advanced ? '收起高级功能' : '展开高级功能' }}
      </n-button>

      <n-collapse v-if="show_advanced" style="margin-bottom: 10px;">
        <n-collapse-item title="待下载高级操作" name="1">
          <n-button @click="delWaitingTask_b" style="margin-bottom: 5px;" block>删除待下载</n-button>
        </n-collapse-item>
        <n-collapse-item title="下载中高级操作" name="2">
          <n-button @click="refreshTask_b" style="margin-bottom: 5px;" block>重新下载</n-button>
        </n-collapse-item>
        <n-collapse-item title="下载成功高级操作" name="3">
          <n-button @click="delSuccessTask_b" style="margin-bottom: 5px;" block>删除完成</n-button>
        </n-collapse-item>
        <n-collapse-item title="下载错误高级操作" name="4">
          <n-button @click="delErrorTask_b" style="margin-bottom: 5px;" block>删除错误</n-button>
          <n-button @click="refreshTask_b" block>重新下载错误</n-button>
        </n-collapse-item>
      </n-collapse>

      <n-divider />

      <!-- 下载列表 -->
      <n-list style="margin-bottom: 10px;">
        <n-list-item v-for="item in page_data" :key="item.id">
          <n-thing>
            <template #header>
              {{ item.downloadMusicname || '未知歌曲' }}
            </template>
            <template #description>
              <div style="display: flex; flex-direction: column; gap: 5px;">
                <div>
                  <span style="font-weight: bold;">歌手：</span>
                  <span>{{ item.downloadArtistname || '未知歌手' }}</span>
                </div>
                <div>
                  <span style="font-weight: bold;">专辑：</span>
                  <span>{{ item.downloadAlbumname || '未知专辑' }}</span>
                </div>
                <div>
                  <span style="font-weight: bold;">数据来源：</span>
                  <span>{{ item.downloadPlugName || '未知来源' }}</span>
                </div>
                <div>
                  <span style="font-weight: bold;">是否书籍：</span>
                  <component :is="getAudioBookTag(item.audioBook)" />
                </div>
                <div>
                  <span style="font-weight: bold;">下载类型：</span>
                  <component :is="getDownloadTypeTag(item.downloadBrType)" />
                </div>
                <div>
                  <span style="font-weight: bold;">状态：</span>
                  <component :is="getStatusTag(item.downloadStatus)" />
                </div>
                <div>
                  <span style="font-weight: bold;">下载时间：</span>
                  <span>{{ item.downloadTime || '未知时间' }}</span>
                </div>
                <div v-if="item.downloadUpdateTime">
                  <span style="font-weight: bold;">更新时间：</span>
                  <span>{{ item.downloadUpdateTime }}</span>
                </div>
                <div v-if="item.downloadMsg">
                  <span style="font-weight: bold;">下载消息：</span>
                  <span>{{ item.downloadMsg }}</span>
                </div>
              </div>
            </template>
          </n-thing>
          
          <template #suffix>
            <div style="display: flex; flex-direction: column; gap: 5px;">
              <n-button @click="handleDelete(item.id)" size="small" type="error">
                删除
              </n-button>
              <n-button v-if="item.downloadStatus === 'error'" @click="handleRefresh(item.id)" size="small" type="info"
                style="margin-top: 5px;">
                重新下载
              </n-button>
            </div>
          </template>
        </n-list-item>
      </n-list>

      <!-- 分页 -->
      <div style="margin-top: 15px;">
        <n-pagination 
          v-model:page="page_index" 
          :item-count="item_total" 
          :page-size="pageSizes"
          :page-sizes="[10, 20, 50, 100]"
          show-size-picker 
          show-quick-jumper
          @update:page="pageUpdata" 
          @update:page-size="pageSizeUpdata"
        >
          <template #prefix="{ itemCount }">
            共 {{ itemCount }} 项
          </template>
        </n-pagination>
      </div>
    </n-card>

    <n-back-top :right="20" :bottom="20" />
  </n-spin>
</template>

<style scoped>
.page {
  display: flex;
  place-items: center;
  flex-direction: row;
  justify-content: center;
}

.operat {
  display: flex;
  flex-direction: row;
  justify-content: space-around;
}

.from {
  display: flex;
  align-items: center;
  flex-direction: row;
  align-content: center;
  justify-content: space-around;
  flex-wrap: wrap;
}

.box {
  display: flex;
  align-items: center;
  flex-direction: row;
  align-content: center;
  justify-content: center;
  flex-wrap: wrap;
}

/* 移动端分页优化 */
:deep(.n-pagination) {
  flex-wrap: wrap;
  gap: 5px;
}

:deep(.n-pagination .n-pagination-item) {
  min-width: 30px;
  padding: 0 5px;
}

@media (max-width: 768px) {
  :deep(.n-pagination .n-pagination-prefix) {
    font-size: 12px;
  }
  
  :deep(.n-pagination .n-pagination-quick-jumper) {
    font-size: 12px;
  }
  
  :deep(.n-pagination .n-pagination-quick-jumper input) {
    width: 50px;
    padding: 0 5px;
  }
}
</style>