<script setup>
import {useRouter } from 'vue-router'
import {login,getAllSet,getAllOption,getversion} from "../utils/api.js";
import userInfoStore from "../stores/user";
import configInfoStore from "../stores/config";
import {inject, ref, computed} from "vue";

const stuserInfoStore = userInfoStore();
const stconfigInfoStore =configInfoStore()

const router = useRouter()

// 从App.vue中注入主题切换函数，获取当前主题
const changetheme = inject("changetheme", null);
const savedTheme = localStorage.getItem('theme');
const isDarkTheme = ref(savedTheme !== 'light');

// 计算标题和标签颜色
const titleColor = computed(() => {
  return isDarkTheme.value ? 'rgba(255, 255, 255, 0.9)' : 'rgba(0, 0, 0, 0.9)';
});

// 计算盒子样式
const boxStyle = computed(() => {
  return {
    display: 'flex',
    flexDirection: 'column',
    justifyContent: 'center',
    alignItems: 'center',
    width: '380px',
    height: '540px',
    border: isDarkTheme.value ? '1px solid rgba(255, 255, 255, 0.5)' : '1px solid rgba(0, 0, 0, 0.2)',
    background: isDarkTheme.value ? 'rgba(0, 0, 0, 0.3)' : 'rgba(255, 255, 255, 0.7)',
    backdropFilter: 'blur(10px)',
    borderRadius: '15px'
  };
});

let loginp =()=>{
  console.log("开始登录")
  loginWeb(loginUsername.value,loginPassword.value)
}
let loginUsername = ref("");
let loginPassword = ref("");
onBeforeMount(()=>{
  //校验有token参数没  没有直接跳转到登录页清除全部缓存
  if (!router.currentRoute.value.query.token) {
    // 清除用户信息缓存
    stuserInfoStore.clearUserInfo();
    // 清除配置信息缓存
    stconfigInfoStore.clearData();
    stconfigInfoStore.clearOption();
    stconfigInfoStore.clearVersion();
    // 清除播放列表缓存（从 localStorage 中移除）
    localStorage.removeItem('playList');
    // 跳转到登录页
    router.replace({path :"/login"}).catch(err => {
      console.error('路由跳转失败:', err);
    });
    return; // 直接返回，不再执行后续的登录状态检查
  }

  stuserInfoStore.isLogin().then((value)=>{
    if (value.data.code===200){
      // 已登录，跳转到首页
      router.replace({path :"/home"}).catch(err => {
        console.error('路由跳转失败:', err);
      });
    }else{
      // 未登录或 token 超时，停留在登录页
      router.replace({path :"/login"}).catch(err => {
        console.error('路由跳转失败:', err);
      });
      console.log('用户未登录或登录已过期');
    }
  }).catch((error)=>{
    router.replace({path :"/login"}).catch(err => {
      console.error('路由跳转失败:', err);
    });
    // 请求失败或网络错误
    console.error('检查登录状态失败:', error);
  })
})

/**
 * 登录
 */
let loginWeb = (username,password)=>{
  console.log("开始登录")
  login(username,password).then((value)=>{
    if(value.data.code===200){
      console.log("登录token："+value.data.data.tokenValue)
      console.log("登录Device："+value.data.data.loginDevice)
      stuserInfoStore.setToken(value.data.data.tokenValue)
      stuserInfoStore.setLoginDevice(value.data.data.loginDevice)
      stuserInfoStore.setUserName(username)
      getAllSet().then((value)=>{
        console.log("获取全部设置:"+value.data)
        if (value.data.code===200){
          stconfigInfoStore.setData(value.data.data)
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
      getversion().then(value=>{
        stconfigInfoStore.setVersion(value.data.data);
      })
      window.$message.success("登录成功！");
      router.replace({path :"/home"}).catch(err => {
        console.error('路由跳转失败:', err);
      });
    }else{
      window.$message.error(value.data.msg)

    }
  })
}

// 切换主题函数
const toggleTheme = () => {
  // 调用App.vue提供的主题切换函数
  if (changetheme) {
    changetheme();
  }
  // 切换本地主题状态
  isDarkTheme.value = !isDarkTheme.value;
  // 保存到localStorage
  localStorage.setItem('theme', isDarkTheme.value ? 'dark' : 'light');
}
</script>

<template>
<n-form @keyup.enter.native="loginp">
<div class="cbody">
  <!-- 将切换主题按钮放置在右上角 -->
  <div class="theme-toggle-button">
    <n-button @click="toggleTheme" circle>
      {{ isDarkTheme ? '🌞' : '🌙' }}
    </n-button>
  </div>
  
  <div class="box" :style="boxStyle">
    <h2 :style="{ color: titleColor }">SqMusic</h2>
    <div class="input-box">
      <label :style="{ color: titleColor }">账号</label>
      <n-input 
        type="text" 
        placeholder="请输入用户名" 
        v-model:value="loginUsername"
      />
    </div>
    <div class="input-box">
      <label :style="{ color: titleColor }">密码</label>
      <n-input 
        type="password"  
        v-model:value="loginPassword" 
        placeholder="请输入密码"
      />
    </div>
    <div class="btn-box">
      <div>
        <n-button @click="loginp" :keyboard="true">登录</n-button>
      </div>
    </div>
  </div>
</div>
</n-form>

  <n-message-provider>
    <content />
  </n-message-provider>
</template>

<style scoped>
* {
  margin: 0;
  padding: 0;
}
a {
  text-decoration: none;
}
.cbody {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 100vh;
  position: relative;
}
.box {
  transition: all 0.3s ease;
}
.box > h2 {
  margin-bottom: 50px;
}
.box .input-box {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}
.box .input-box > label {
  margin-top: 4px;
  font-size: 14px;
  margin-bottom: 10px;
}
.box a {
  display: flex;
  width: 250px;
  flex-direction: column;
  margin-top: 10px;
  font-size: 14px;
  text-align: end;
}
.box .btn-box > div {
  margin-top: 20px;
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: start;
}

.box .btn-box > div > button {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 120px;
  height: 34px;
  border-radius: 3px;
  border: 1px solid rgba(58, 58, 58, 0.3);
  transition: all 0.3s ease;
}
.box .btn-box > div > button:hover {
  border: 1px solid rgba(59, 47, 49, 0.5);
}

/* 右上角主题切换按钮样式 */
.theme-toggle-button {
  position: absolute;
  top: 20px;
  right: 20px;
}
</style>