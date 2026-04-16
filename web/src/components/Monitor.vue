<script setup>

import {getMonitor, parserUrlInfo,addMonitor,delListen} from "../utils/api.js";
import {ref,onMounted} from "vue";


let addMonitorRequest = ()=>{
  // 构建符合 SqMonitor 实体类的数据对象
  const monitorData = {
    plugName: parserUrlInfoData.value.plugName || "netease", // 默认网易云
    type: parserUrlInfoData.value.type,
    enabled: "1",
    targetId: parserUrlInfoData.value.id,
    targetName: parserUrlInfoData.value.name,
    targetUrl: parserUrlInfoData.value.url,
    targetCount: parserUrlInfoData.value.count,
    targetDesc: parserUrlInfoData.value.desc,
    targetCover: parserUrlInfoData.value.cover
  }

  console.log("提交数据:", monitorData)
  addMonitor(monitorData).then((value)=>{
    if (value.data.code !== 200) {
      window.$message.error(value.data.msg || "请求失败")
      return;
    }
    window.$message.success("添加成功")
    closeModal()
    getMonitorRequest() // 刷新列表
  }).catch((error)=>{
    console.error('添加监听错误:', error)
    window.$message.error("添加失败，请稍后重试")
  })
}

/**
 * 全部监听列表查询
 * @type {Promise<void>}
 */
let getMonitorRequest = ()=>{
  getMonitor().then((value)=>{
    getMonitorData.value = value.data.data
  })
}

// 页面加载时获取一次数据
onMounted(() => {
  getMonitorRequest()
})
/**
 * 获取歌单信息
 * @type {Promise<axios.AxiosResponse<any>>}
 */
let parserUrlInfoRequest=()=>{
  //判断 URL 是否是空
  if (!addMonitorUrl.value || addMonitorUrl.value.trim() === '') {
    window.$message.error("请输入URL")
    return;
  }
  space.value=true;
  parserUrlInfo(addMonitorUrl.value).then((value)=>{
    space.value=false;
    // 检查返回码是否为 200
    if (value.data.code !== 200) {
      window.$message.error(value.data.msg || "请求失败")
      return;
    }
    parserUrlInfoData.value = value.data.data;
  }).catch((error)=>{
    space.value=false;
    console.error('解析 URL 错误:', error)
    window.$message.error("网络错误，请稍后重试")
  })
}

let getMonitorData = ref("")
/**
 * 显示新增弹窗
 */
let showModal = ref(false)
/**
 * 需要新增的URL
 */
let addMonitorUrl = ref("")
/**
 * 点击分析的space
 */
let space = ref(false)
/**
 * 获取到的歌单分析数据
 */
let parserUrlInfoData = ref("")

/**
 * 显示详情弹窗的状态
 */
let showDetailModal = ref(false)
/**
 * 当前选中的监听项
 */
let currentSelectedItem = ref(null)

/**
 * 关闭弹窗并清空数据
 */
const closeModal = () => {
  showModal.value = false
  parserUrlInfoData.value = ""
  addMonitorUrl.value = ""
}

/**
 * 处理卡片点击事件
 */
const handleCardClick = (item) => {
  console.log('点击卡片:', item)
  currentSelectedItem.value = item
  showDetailModal.value = true
}

/**
 * 关闭详情弹窗
 */
const closeDetailModal = () => {
  showDetailModal.value = false
  currentSelectedItem.value = null
}

/**
 * 处理删除按钮点击事件
 */
const handleDelete = (item) => {
  console.log('删除:', item)
  delListen(item.id).then((value) => {
    if (value.data.code !== 200) {
      window.$message.error(value.data.msg || "删除失败")
      return;
    }
    window.$message.success("删除成功")
    getMonitorRequest() // 刷新列表
  }).catch((error) => {
    console.error('删除错误:', error)
    window.$message.error("删除失败，请稍后重试")
  })
}

</script>

<template>
  <n-flex wrap>
    <n-card style="width: 10rem; cursor: pointer;" title="新增监听" @click="showModal = true">
      <n-space justify="center" align="center" style="height: calc(100% - 4rem); min-height: 7rem;">
        <n-icon size="48" color="#909090">
          <svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" viewBox="0 0 24 24"><path d="M19 13h-6v6h-2v-6H5v-2h6V5h2v6h6v2z" fill="currentColor"></path></svg>
        </n-icon>
      </n-space>
    </n-card>
    <n-card 
      v-for="(item, index) in getMonitorData" 
      :key="index"
      style="width: 10rem; cursor: pointer;"
      @click="handleCardClick(item)"
    >
      <template #header>
        <n-space align="center">
          <n-tag size="large" strong type="success"> 来源 :{{ item.plugName }}</n-tag>
        </n-space>
      </template>
      <n-space vertical>
        <n-image
          :src="item.targetCover"
          style="width: 100%; height: 7rem; object-fit: cover; pointer-events: none;"
        />
        <span style="font-size: 0.85rem; margin-top: 0.5rem;">{{ item.targetName }}</span>
        <n-flex justify="end" style="margin-top: 0.25rem;">
          <n-popconfirm @positive-click.stop="() => handleDelete(item)">
            <template #trigger>
              <n-button type="error" size="small" ghost @click.stop>删除</n-button>
            </template>
            确定要删除这个监听吗？
          </n-popconfirm>
        </n-flex>
      </n-space>
    </n-card>
  </n-flex>



  <n-modal v-model:show="showModal" @update:show="(val) => { if (!val) closeModal() }">
    <n-card
        style="width: 600px"
        title="请输入URL"
        :bordered="false"
        size="huge"
        role="dialog"
        aria-modal="true"
    >
      <n-input v-model:value="addMonitorUrl" type="text" placeholder="目前仅支持网易云歌单分享监听" />
      <n-divider />
      <n-spin :show="space">
        <n-card title="歌单信息" v-if="parserUrlInfoData && !space">
          <n-space :size="16" style="display: flex; flex-wrap: nowrap;">
            <n-image
                :src="parserUrlInfoData.cover"
                style="width: 100px; height: 100px; object-fit: cover;"
            />
            <n-space vertical style="flex: 1; min-width: 0;">
              <n-text style="display: block; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;">歌单名称：{{ parserUrlInfoData.name }}</n-text>
              <n-text>歌单 ID：{{ parserUrlInfoData.id }}</n-text>
              <n-text style="display: block; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;">歌单 url：{{ parserUrlInfoData.url }}</n-text>
              <n-text>当前歌曲数量：{{ parserUrlInfoData.count }}</n-text>
            </n-space>
          </n-space>
          <n-divider />
          <n-text>歌单描述：{{ parserUrlInfoData.desc }}</n-text>
        </n-card>
      </n-spin>
      <template #footer>
        <n-flex justify="end">
          <n-button type="primary" @click="parserUrlInfoRequest" v-if="!parserUrlInfoData">分析</n-button>
          <n-button type="success" @click="addMonitorRequest" v-else>添加</n-button>
          <n-button @click="closeModal">取消</n-button>
        </n-flex>
      </template>
    </n-card>
  </n-modal>

  <!-- 详情弹窗 -->
  <n-modal v-model:show="showDetailModal" @update:show="(val) => { if (!val) closeDetailModal() }">
    <n-card
        style="width: 600px"
        title="监听详情"
        :bordered="false"
        size="large"
        role="dialog"
        aria-modal="true"
    >
      <n-space :size="16" style="display: flex; flex-wrap: wrap; justify-content: center;" v-if="currentSelectedItem">
        <n-image
            :src="currentSelectedItem.targetCover"
            style="width: 300px; height: 300px; object-fit: cover;"
        />
        <n-space vertical style="flex: 1; min-width: 0;">
          <n-text style="display: block; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;">歌单名称：{{ currentSelectedItem.targetName }}</n-text>
          <n-text>歌单ID：{{ currentSelectedItem.targetId }}</n-text>
          <n-text>插件名称：{{ currentSelectedItem.plugName }}</n-text>
          <n-text style="display: block; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;">目标 URL：{{ currentSelectedItem.targetUrl }}</n-text>
          <n-text>歌曲数量：{{ currentSelectedItem.targetCount }}</n-text>
          <n-text>创建时间：{{ currentSelectedItem.createTime }}</n-text>
          <n-text>更新时间：{{ currentSelectedItem.updateTime }}</n-text>
        </n-space>
      </n-space>
      <n-divider />
      <n-text>歌单描述：{{ currentSelectedItem?.targetDesc || '无描述' }}</n-text>
      <template #footer>
        <n-flex justify="end">
          <n-button @click="closeDetailModal">关闭</n-button>
        </n-flex>
      </template>
    </n-card>
  </n-modal>
</template>

<style scoped>

</style>