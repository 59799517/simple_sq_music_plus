<script setup lang="js">
import {ref, computed } from 'vue'

import Set from "./V3Set.vue";
import {
  getNetSpeed, getQQVipQrCodeStatus
} from "../utils/api.js";

import configInfoStore from "../stores/config";
const stconfigInfoStore =configInfoStore()

const uploadSpeed = ref("0.00 B/s");
const downloadSpeed = ref("0.00 B/s");

// 检查是否显示阿里云盘菜单
const showAliyunSync = computed(() => {
  const aliyunShowConfig = stconfigInfoStore.data?.find(
    item => item.configKey === 'plug.aliyun.show'
  );
  console.log('阿里云盘显示配置:', aliyunShowConfig);
  // 如果配置不存在，默认显示；如果配置值为 false（字符串或布尔），则隐藏
  if (!aliyunShowConfig) return true;
  const value = aliyunShowConfig.configValue;
  return value !== 'false' && value !== false;
});
// 检测是否为移动设备
const isMobile = () => {
  return /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent);
}

// 设置参数
const active = ref(false);
const placement = ref("right");
const activate = (place) => {
  // 移动端从底部弹出，桌面端从右侧弹出
  if (isMobile()) {
    placement.value = 'bottom';
  } else {
    placement.value = place;
  }
  active.value = true;
};

/**
 * 每间隔 10 秒执行一次 获取当前网速
 */
setInterval(()=>{
  getNetSpeed()
  .then((value)=>{
    uploadSpeed.value = value.data.data.uploadSpeedFormatted+"p"
    downloadSpeed.value = value.data.data.downloadSpeedFormatted
  })
  .catch((error)=>{
    // console.error('getNetSpeed 错误:', error)
  })
},1000)

</script>

<template>
  <div>
    <div class="header">
      <div class="box"  >
        <n-popover trigger="hover">
          <template #trigger>
            <div>
              <n-flex justify= "center" align = "center">
                <h2>SqMusic</h2>
                <n-gradient-text :size="12" type="success" >
                  上传：{{uploadSpeed}}
                  下载：{{downloadSpeed}}
                </n-gradient-text>
              </n-flex>

            </div>

          </template>
          <p>
            <n-gradient-text :size="12" type="success" >
              &nbsp;前端版本：{{stconfigInfoStore.uiversion}}
            </n-gradient-text>
          </p>
          <p v-if="stconfigInfoStore.version !== stconfigInfoStore.backendVersion">
            <n-gradient-text :size="12" type="info" >
              &nbsp;后端版本：{{stconfigInfoStore.version}}
            </n-gradient-text>
          </p>
        </n-popover>

      </div>
      
      <!-- PC 端布局 -->
      <template v-if="!isMobile()">
        <div class="box">
          <!-- 修改路径，添加 /home 前缀 -->
          <router-link active-class="active" to="/v3search">
            <n-button size="large" quaternary>
              搜索
            </n-button>
          </router-link>
          <router-link active-class="active" to="/V3Download">
            <n-button size="large" quaternary>
              下载
            </n-button>
          </router-link>
          <router-link active-class="active" to="/V3Parsertext">
            <n-button size="large" quaternary>
              解析文本
            </n-button>
          </router-link>
          <router-link active-class="active" to="/V3ParserPlaylist">
            <n-button size="large" quaternary>
              解析歌单
            </n-button>
          </router-link>
          <router-link active-class="active" to="/Monitor">
            <n-button size="large" quaternary>
              监听下载
            </n-button>
          </router-link>
          <router-link v-if="showAliyunSync" active-class="active" to="/AliyunSync">
            <n-button size="large" quaternary>
              阿里云盘
            </n-button>
          </router-link>
        </div>
        <div class="box">
            <n-button  size="large" quaternary @click="activate('right')">
              设置
            </n-button>
        </div>
      </template>

      <!-- 移动端布局 -->
      <template v-else>
        <div class="box mobile-nav">
          <router-link active-class="active" to="/v3search">
            <n-button size="large" quaternary>
              搜索
            </n-button>
          </router-link>
          <router-link active-class="active" to="/V3Download">
            <n-button size="large" quaternary>
              下载
            </n-button>
          </router-link>
          <router-link active-class="active" to="/V3Parsertext">
            <n-button size="large" quaternary>
              解析文本
            </n-button>
          </router-link>
          <router-link active-class="active" to="/V3ParserPlaylist">
            <n-button size="large" quaternary>
              解析歌单
            </n-button>
          </router-link>
          <router-link active-class="active" to="/Monitor">
            <n-button size="large" quaternary>
              监听下载
            </n-button>
          </router-link>
          <router-link v-if="showAliyunSync" active-class="active" to="/AliyunSync">
            <n-button size="large" quaternary>
              阿里云盘
            </n-button>
          </router-link>
          <n-button size="large" quaternary @click="activate('right')">
            设置
          </n-button>
        </div>
      </template>
    </div>
  </div>
<!--设置弹出框-->
  <n-drawer v-model:show="active" :width="isMobile() ? '100%' : 502" :height="isMobile() ? '80%' : '100%'" :placement="placement">
    <n-drawer-content title="设置">
    <Set></Set>
    </n-drawer-content>
  </n-drawer>



</template>

<style scoped>
.header{
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  justify-content: space-between;

}
.box{
  display: flex;
  align-items: center;
  flex-direction:row;
  align-content: center;
  justify-content: center;
  flex-wrap: wrap;
}

/* 移动端导航样式 */
.mobile-nav {
  width: 100%;
  justify-content: space-around;
  margin-top: 10px;
}

.mobile-nav a,
.mobile-nav button {
  flex: 1;
  text-align: center;
  min-width: 0;
}

.mobile-nav a .n-button,
.mobile-nav button {
  width: 100%;
  font-size: 14px;
  padding: 0 5px;
}

img{
  width: 40px;
  height: 40px;
}

nav {
  display: flex;
  align-items: center;
  margin: 10px 90px;
  font:16px Arial, Helvetica, sans-serif;
}


nav a:hover {
  opacity: 1;
}

.active {
  //color: #608bd2;
  pointer-events: none;
  opacity: 1;
}

/*搜索框*/

.text{
  height: 22px;
  font-size: 14px;
  //border: 1px solid #ccc;
  padding: 3px 16px;
  border-bottom-left-radius: 20px;
  border-top-left-radius: 20px;
}
.text:focus{
  outline: none;
  //border-color: rgba(82, 168, 236, 0.8);
  //box-shadow: inset 0 2px 2px rgba(0, 0, 0, 0.075), 0 0 8px rgba(82, 168, 236, 0.6);
}
.button{
  width: 60px;
  height: 30px;
  font-size: 14px;
  margin-right: 35px;
  //border: 1px solid #608bd2;
  //background-color: #608bd2;
  border-top-right-radius: 20px;
  border-bottom-right-radius: 20px;
}

.contents{
  display: flex;
  justify-content: center;
}
.content{
  display: flex;
  width: 1400px;
  height: 1400px;
  /*background-color: #f0f2f3;*/
}
a{
  text-decoration: none;

  color:#000000;

  font-family:sans-serif;

  font-size: 12px;

}
</style>
