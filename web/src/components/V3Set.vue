<script setup>
import {
  getAllSet,
  updateConfig,
  logout,
  getQQVipQrCode,
  getKgQrCode,
  getKgWxQrCode,
  refreshQQvipCookie,
  getKgWxQrCodeStatus,
  getKgQrCodeStatus,
  kgRefreshToken,
  kgSign,
  getQQVipQrCodeStatus,
  getUploadJsonFileUrl,
  getAllOption,
  getQQVipWechatQrCode
} from "../utils/api.js"; //引入仓库
import Cookies from 'js-cookie';
import { inject, ref, onMounted, computed } from 'vue';

import userInfoStore from "../stores/user";
import configInfoStore from "../stores/config";
import {storeToRefs} from "pinia";
const stuserInfoStore = userInfoStore();
const stconfigInfoStore =configInfoStore()
const { loginDevice,username, token } = storeToRefs(stuserInfoStore); // 响应式

// 注入切换主题方法
const changetheme = inject("changetheme");

// PWA安装相关
const deferredPrompt = ref(null);
const showInstallPrompt = ref(false);

// 检查是否支持PWA安装
const checkPWAInstallSupport = () => {
  // 检查浏览器是否支持PWA
  if (typeof navigator !== 'undefined' && 'serviceWorker' in navigator) {
    // 监听 beforeinstallprompt 事件
    const beforeInstallPromptHandler = (e) => {
      // 阻止默认的安装提示
      e.preventDefault();
      // 保存事件以便稍后使用
      deferredPrompt.value = e;
      // 显示安装按钮
      showInstallPrompt.value = true;
    };
    
    window.addEventListener('beforeinstallprompt', beforeInstallPromptHandler);
    
    // 返回清理函数
    return () => {
      window.removeEventListener('beforeinstallprompt', beforeInstallPromptHandler);
    };
  }
  
  return () => {};
};

// 安装PWA
const installPWA = () => {
  if (deferredPrompt.value) {
    // 显示安装提示
    deferredPrompt.value.prompt();
    // 等待用户响应
    deferredPrompt.value.userChoice.then((choiceResult) => {
      if (choiceResult.outcome === 'accepted') {
        console.log('用户接受了PWA安装');
      } else {
        console.log('用户拒绝了PWA安装');
      }
      // 重置 deferredPrompt 变量
      deferredPrompt.value = null;
      // 隐藏安装提示
      showInstallPrompt.value = false;
    });
  }
};

// 检查是否支持PWA功能
const isPWASupported = () => {
  return typeof navigator !== 'undefined' && 'serviceWorker' in navigator;
};

// 页面加载时检查PWA安装支持
onMounted(() => {
  const cleanup = checkPWAInstallSupport();
  
  // 在组件卸载时清理事件监听器
  // 这里我们不需要显式调用cleanup，因为Vue会在组件卸载时自动清理
});

//设置分组
let configData = ref([]);
let systemConfig = ref([]);
let plugConfig = ref({});
// 插件二维码设置

let qrTitle = ref("注意区分手机QQ登录二维码和微信登录二维码");

let qqvipPlugopen=ref(false)
let kgPlugopen=ref(false)

//qq插件二维码信息
let qqqr = ref("");
//酷狗插件二维码信息
let kgqr = ref("");
// 酷狗签到的开始结束时间
let kgbeginendtime = ref("")

// 刷新页面设置数据
let refreshData= () => {

  configData.value=[]
  systemConfig.value = []
  plugConfig.value = {};
  getAllSet().then((value)=>{
    console.log("获取全部设置:"+value.data)
    if (value.data.code===200){
      stconfigInfoStore.setData(value.data.data)
      configData.value= stconfigInfoStore.data
      //根据configData 的 configKey分组
      configData.value.forEach(item => {
        let stringsKey = item.configKey.split(".");
        
        // 跳过 expand.aliyun 开头的配置项（不显示但保留数据）
        if (item.configKey.startsWith('expand.aliyun')) {
          return;
        }
        
        if (stringsKey[0]==="system") {
          systemConfig.value.push(item)
        }else if (stringsKey[0]==="plug"){

          let tempplugset = plugConfig.value[stringsKey[1]];
          if (tempplugset===undefined||tempplugset===null||tempplugset.length===0){
            plugConfig.value[stringsKey[1]] = [item]
          }else{
            tempplugset.push(item)
            plugConfig.value[stringsKey[1]]=tempplugset;
          }
          if (item.configKey==="plug.qqvip.open"){
            qqvipPlugopen.value = item.configValue === "true";
            console.log("plug.qqvip.open:"+item.configValue)
          }
          if (item.configKey==="plug.kg.open"){
            kgPlugopen.value = item.configValue === "true";
            console.log("plug.kg.open:"+item.configValue)
          }
          if (item.configKey==="plug.kg.sign.begin-end.time"){
            kgbeginendtime.value = item.configValue;
            console.log("plug.kg.sign.begin-end.time:"+item.configValue)
          }
        }
        else{
          systemConfig.value.push( item)
        }
      });
    }else{
      window.$message.error("错误信息："+"获取整体设置失败检查服务器是否异常！")
    }
  })
  getAllOption().then((value)=>{
    console.log("获取的设置是：",value.data)
    if (value.data.code===200){
      stconfigInfoStore.setOption(value.data.data)
    }else{
      window.$message.error("错误信息："+"获取参数信息失败检查服务器是否异常！")
    }
  })
}
//刷新数据
refreshData();

// 设置弹出框
let showModal = ref(false)
let tempModalTile = ref("")
let tempModalRemark = ref("")
let tempModalType = ref("")
let tempModalValue = ref("")
let tempModalKey = ref("")
let tempModalKeyNullCheck = ref(0)
let tempModalDisabled = ref(0)
let tempOptions = ref("")

//关闭弹窗
let  closeDialog = ()=>{
  showModal.value=false
  tempModalTile.value= ''
  tempModalRemark.value=''
  tempModalType.value=''
  tempModalValue.value=''
  tempModalKey.value=''
  tempModalKeyNullCheck.value=0
  tempModalDisabled.value=0
  tempOptions.value=''
}

//打开弹窗
let openModal =(item)=>{
  showModal.value=true
  // console.log("item:"+JSON.stringify(item))
  tempModalTile.value = item.configName;
  tempModalRemark.value = item.configRemark;
  tempModalType.value= item.configType;
  if(item.configType==="number"){
    tempModalValue =ref(Number(item.configValue));
  }else if(item.configType==="boolean"){
    tempModalValue =ref(JSON.parse(item.configValue));
  }else if(item.configType==="select"){
    tempModalValue =ref(item.configValue);
    // 解析 configOptions JSON 字符串为数组
    try {
      tempOptions.value = JSON.parse(item.configOptions);
    } catch (e) {
      console.error('解析 configOptions 失败:', e);
      tempOptions.value = [];
    }
  }
  else{
    tempModalValue =ref(item.configValue);
  }
  tempModalKeyNullCheck.value = item.configNullCheck;
  tempModalDisabled.value=item.configDisabled;
  tempModalKey.value=item.configKey;
}
//保存设置
let setConfig = ()=>{
if (tempModalKeyNullCheck.value===1){
  if (tempModalType==='boolean'){
    if (tempModalValue.value===null||tempModalValue.value===undefined){
      window.$message.error("请填写内容是或者否")
      return;
    }
  }
  if (tempModalType==='number'){
    if (tempModalValue.value>=0){
      window.$message.error("数值类型必须大于等于 0")
      return;
    }
  }
  if (tempModalType==='input'||tempModalType==='path'||tempModalType==='password'){
    if (tempModalValue.value===''||tempModalValue.value===null||tempModalValue.value===undefined){
      window.$message.error("请填写内容")
      return;
    }
  }
}
// 校验 select 类型的值是否在选项列表内
if (tempModalType==='select'){
  if (!tempOptions.value || tempOptions.value.length === 0) {
    window.$message.error("该配置项没有有效的选项列表，请检查数据")
    return;
  }
  // 提取所有选项的 value 值
  const validValues = tempOptions.value.map(opt => opt.value);
  if (!validValues.includes(tempModalValue.value)) {
    window.$message.error(`选择的值 "${tempModalValue.value}" 不在允许的选项中，请重新选择`)
    return;
  }
}
  updateConfig(tempModalKey.value,tempModalValue.value).then(value => {
    if(value.data.code===200){
      window.$message.success("修改成功!")
      getAllSet().then((value)=>{
        // console.log("获取全部设置:"+value.data)
        if (value.data.code===200){
          stconfigInfoStore.clearData()
          stconfigInfoStore.setData(value.data.data)
          // configData.value=value.data.data

          refreshData()
        }else{
          window.$message.error("错误信息："+"获取整体设置失败检查服务器是否异常！")
        }
      })
      closeDialog()
    }else{
      window.$message.error("修改失败："+value.data.msg)
    }
  })
}
// 退出
let clogout=() => {
  logout(stuserInfoStore.loginDevice).then(value => {
    if(value.data.code===200){
      window.$message.success("退出成功")
      stuserInfoStore.clearUserInfo()
      stconfigInfoStore.clearData()
      // 清空cookie
      console.log("Cookies.get('sqmusic'):"+Cookies.get('sqmusic'))
      console.log("Cookies.get('token'):"+Cookies.get('token'))
      Cookies.set('sqmusic', '');
      Cookies.set('token', '');
      window.location.href="/"
    }else{
      window.$message.error("退出失败："+value.data.msg)
    }
  })
}

let getqqvipWechatQr=()=>{
  getQQVipWechatQrCode().then((value)=>{
    qqqr.value = value.data.data
    qrTitle.value = "↓↓↓↓↓↓↓↓↓↓使用微信扫码（成功后等待5-10秒点击下方检测二维码状态）↓↓↓↓↓↓↓↓↓↓↓↓"
    refreshData()
  })
}

// 获取QQVIP登录二维码
let getqqvipqqQr=()=>{
  getQQVipQrCode().then((value)=>{
    qqqr.value = value.data.data
    qrTitle.value = "↓↓↓↓↓↓↓↓↓↓使用手机QQ扫码（成功后等待5-10秒点击下方检测二维码状态）↓↓↓↓↓↓↓↓↓↓↓↓"
    refreshData()
  })
}
/**
 * 获取二维码状态
 */
let getQQVipQrCodeStatusc=()=>{
  getQQVipQrCodeStatus().then((value)=>{
    console.log("获取的状态是：",value)
    if (value.data.code===200){
      window.$message.success("扫码成功（已获取到用户信息）")
      qqqr.value="";
      qrTitle.value="扫码成功了去搜索看看！";
    }else{
      window.$message.error(value.data.msg)
      qrTitle.value="扫码有点问题，重试一下或者检查下网络啥问题！";
    }
    refreshData()
  })
}
//刷新二维码
let refreshQQvipCookiec=()=>{
  refreshQQvipCookie().then(value => {
    if (value.data.code===200){
      window.$message.success("刷新成功")
    }else{
      window.$message.error(value.data.msg)
    }
    refreshData()

  })
}
// 获取酷狗登录二维码
let getKgQr=()=>{
  getKgQrCode().then((value)=>{
    kgqr.value=value.data.data
    refreshData()
  })
}
// 获得酷狗微信等枯二维码
let fgetKgWxQrCode = ()=>{
  getKgWxQrCode().then((value)=>{
    kgqr.value=value.data.data
    window.$message.error(value.data.msg)
  })
}
//获得酷狗微信二维码登录状态
let fgetKgWxQrCodeStatus = ()=>{
  getKgWxQrCodeStatus().then((value)=>{
    if (value.data.code===200&&value.data){
      kgqr.value="";
      window.$message.success("扫码成功")
    }else{
      window.$message.error("未获得扫码结果（长时间未获得请重新获取并扫码）")
    }
    refreshData()
  })
}
//获得酷狗二维码登录状态
let fgetKgQrCodeStatus = ()=>{
  getKgQrCodeStatus().then((value)=>{
    if (value.data.code===200&&value.data.data){
      kgqr.value="";
      window.$message.success("扫码成功")
    }else{
      window.$message.error("未获得扫码结果（长时间未获得请重新获取并扫码）")
    }
    refreshData()
  })
}

// 刷新酷狗登录状态
let fkgRefreshToken = ()=>{
  kgRefreshToken().then((value)=>{
    if (value.data.code===200){
      window.$message.success("刷新成功")
    }else{
      window.$message.error("刷新失败")
    }
    refreshData()
  })
}
// 酷狗手动签到
let fkgSign = ()=>{
  kgSign().then((value)=>{
    if (value.data.code===200){
      window.$message.success("签到成功")
    }else{
      window.$message.error("签到失败")
    }
    refreshData()
  })
}

let  handleFinish = ({
                       file,
                       event
                     })=>{
  // console.log("file:"+file.status)
  // console.log("event:"+JSON.stringify(event))
  // console.log("file:"+JSON.stringify(file))
  //
  if (file.status=='finished'){
    window.$message.success("配置导入成功!")
  }else{
    window.$message.error("配置导入失败!")
  }
}
</script>

<template>
  <n-card title="系统设置">
    <n-list v-for="(item, index) in systemConfig">
      <n-list-item v-if="item.configShow===1">
        <n-popover trigger="hover">
          <template #trigger>
            <n-thing :title="item.configName" :description="item.configValue" />
          </template>
          <span>{{item.configRemark}}</span>
        </n-popover>
        <template #suffix>
          <n-button @click="openModal(item)" > 修改</n-button>
        </template>
      </n-list-item>
    </n-list>
  </n-card>
  
  <n-card title="插件登录" v-if="qqvipPlugopen||kgPlugopen">
    <n-tabs type="line"  animated>
      <n-tab-pane display-directive="if" name="qqvip" tab="qqvip" v-if="qqvipPlugopen">
        <n-flex justify="space-around" >
          <p>{{qrTitle}}</p>
          <n-image :alt="qrTitle"  width="200" v-if="qqqr" :src="qqqr"></n-image>
        </n-flex>
        <n-divider>
        </n-divider>
        <n-flex vertical>
          <n-flex  inline justify="space-around" size="large">
            <n-button @click="getqqvipqqQr">
              ①获手机QQ二维码
            </n-button>
            <n-button @click="getqqvipWechatQr">
              ①微信二维码
            </n-button>
          </n-flex>
          <n-flex vertical justify="space-around" size="large">

          </n-flex>

        <n-button @click="getQQVipQrCodeStatusc">
          ② 检查二维码状态
        </n-button>
        <n-divider>
          辅助功能
        </n-divider>
        <n-button @click="refreshQQvipCookiec">
          刷新当前登录cookie
        </n-button>
        </n-flex>
      </n-tab-pane>
      
      <n-tab-pane display-directive="if" name="酷狗概念" tab="酷狗概念" v-if="kgPlugopen">
        <n-image v-if="kgqr" :src="kgqr"></n-image>
        <n-divider v-if="kgLastTime!==''">
          酷狗签到领取VIP信息：
          <br>
          {{kgbeginendtime}}
        </n-divider>
        <n-divider v-else>
          暂无签到信息请下方手动签到
        </n-divider>
        <n-flex vertical>
          <n-button @click="fgetKgWxQrCode">
            ①获取酷狗微信二维码
          </n-button>
          <n-button @click="fgetKgWxQrCodeStatus">
            ②获取酷狗微信二维码扫码状态(扫完后等一小会然后点)
          </n-button>
        </n-flex>
        <n-divider>
          辅助功能（请勿频繁手动签到）每次签到获得一天vip
        </n-divider>
        <n-flex vertical>
          <n-button @click="fkgRefreshToken">
            刷新登录信息
          </n-button>
          <n-button @click="fkgSign">
            酷狗手动签到
          </n-button>
        </n-flex>
      </n-tab-pane>
    </n-tabs>
  </n-card>
  
  <n-card title="插件设置" style="margin-bottom: 16px">
    <n-tabs type="line" animated>
      <n-tab-pane
          display-directive="if"
          default-value="kw"
          v-for="(configItems, pluginName) in plugConfig"
          :key="pluginName"
          :name="pluginName"
          :tab="pluginName"
      >
        <n-list>
          <div v-for="(config, index) in configItems" :key="config.configKey || index">
            <n-list-item  v-if="config.configShow===1">
              <n-popover trigger="hover" >
                <template #trigger>
                  <n-thing :title="config.configName" :description="config.configValue" />
                </template>
                <span>{{config.configRemark}}</span>
              </n-popover>
              <template #suffix>
                <n-button size="small" @click="openModal(config)">
                  修改
                </n-button>
              </template>
            </n-list-item>
          </div>
        </n-list>
      </n-tab-pane>
      <n-tab-pane name="no-plugins" tab="无插件" v-if="Object.keys(plugConfig).length === 0">
        <n-empty description="暂无插件配置" />
      </n-tab-pane>
    </n-tabs>
  </n-card>
  
  <n-card title="登录信息">
    <div style="display: flex;flex-direction: column;width: 100%" >
      <n-p>token：{{token}}</n-p>
      <n-p>username：{{username}}</n-p>
      <n-button @click="changetheme">切换主题</n-button>
      
      <!-- PWA安装按钮移到这里 -->
      <n-button 
        v-if="isPWASupported()" 
        @click="installPWA" 
        style="margin-top: 10px;"
        :disabled="!showInstallPrompt"
        :title="!showInstallPrompt ? '暂不支持安装或已安装' : '安装应用到桌面'"
      >
        {{ showInstallPrompt ? '安装应用' : '安装应用（不可用）' }}
      </n-button>

      <div style="width: 100%; display: flex; flex-direction: column;">
        <n-upload
            style="width: 100%;"
            :action=getUploadJsonFileUrl()
            accept=".json"
            @finish="handleFinish"
            :headers="{
          'sqmusic': token,
        }"
        >
          <n-button style="margin-top: 10px; width: 100%" >导入歌单数据</n-button>
        </n-upload>
      </div>

      <n-button @click="clogout" style="margin-top: 10px;">退出</n-button>
    </div>
  </n-card>

<!--  弹出框-->
  <n-modal
      v-model:show="showModal"
      :title="tempModalTile"
      :draggable="{ bounds: 'none' }"
      :style="{ width: '800px' }"
  >
    <n-card
        :title="tempModalTile"
        :bordered="false"
        size="huge"
        role="card"
        aria-modal="true"
    >
      描述：{{tempModalRemark}}
      <template #footer>
        <div v-if="tempModalType==='input'" >
          <n-input v-model:value="tempModalValue" :disabled="tempModalDisabled===1" />
        </div>
        <div v-if="tempModalType==='path'" >
          <n-input v-model:value="tempModalValue" :disabled="tempModalDisabled===1" />
        </div>
        <div v-if="tempModalType==='password'" >
          <n-input
              type="password"
              show-password-on="mousedown"
              placeholder="密码"
              v-model:value="tempModalValue"
              :disabled="tempModalDisabled===1"
          />
        </div>
        <div v-if="tempModalType==='boolean'" >
          <n-switch v-model:value="tempModalValue" :disabled="tempModalDisabled===1"/>
        </div>
        <div v-if="tempModalType==='number'" >
          <n-input-number v-model:value="tempModalValue"  :disabled="tempModalDisabled===1"  />
        </div>
        <div v-if="tempModalType==='select'" >
          <n-select
            v-model:value="tempModalValue"
            :options="tempOptions"
            :disabled="tempModalDisabled===1"
            placeholder="请选择"
            :multiple="false"
          />
        </div>
        <div v-if="tempModalType===''||tempModalType===null" >
          <n-input v-model:value="tempModalValue" :disabled="tempModalDisabled===1" />
        </div>

        <br>
        <br>
        <div style="display: flex;flex-direction: row;justify-content: flex-end">
          <n-button @click="setConfig">确定</n-button>
          <div style="width: 30px;"></div>
          <n-button @click="closeDialog">取消</n-button>
        </div>
      </template>
    </n-card>
  </n-modal>
</template>

<style >

</style>