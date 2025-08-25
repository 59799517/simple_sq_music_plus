<script setup>

import {useRouter } from 'vue-router'


import {login,getAllSet,getAllOption,getversion} from "../utils/api.js"; //引入仓库
import userInfoStore from "../stores/user";
import configInfoStore from "../stores/config";
const stuserInfoStore = userInfoStore();
const stconfigInfoStore =configInfoStore()

const router = useRouter()





// //全局监听回车按钮
// document.onkeydown = function (e) {
//   if ( e.code === 'Enter') {
//     loginp()
//   }
// }

let loginp =()=>{
  console.log("开始登录")
  loginWeb(loginUsername.value,loginPassword.value)
}
let loginUsername = ref("");
let loginPassword = ref("");
onBeforeMount(()=>{
  stuserInfoStore.isLogin().then((value)=>{
    if (value.data.code===200){
      router.replace({path :"/home"}).catch(err => {
        console.error('路由跳转失败:', err);
      });
    }
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

</script>

<template>
<!--  <n-modal :show="showModal">-->
<!--    <n-card style="width: 600px" title="登录成功" size="huge" :bordered="false" role="dialog" aria-modal="true">-->
<!--      返回-->
<!--    </n-card>-->
<!--  </n-modal>-->
<n-form @keyup.enter.native="loginp">
<div class="cbody">



  <div class="box" >
    <h2>SqMusic</h2>
    <div class="input-box">
      <label>账号</label>
      <n-input type="text" placeholder="请输入用户名" v-model:value="loginUsername" />
    </div>
    <div class="input-box">
      <label>密码</label>
      <n-input type="password"  v-model:value="loginPassword" placeholder="请输入密码" />
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
  color: black;
}
.cbody {
  display: flex;
  flex-direction: center;
  justify-content: center;
  align-items: center;
  height: 100vh;
  //background-image: url("../../public/qysv.jpg"); /* 在这里插入一张你想要设置的背景图片 */

  /* 背景图垂直、水平均居中 */
  background-position:center center;
  /* 背景图不平铺 */
  background-repeat:no-repeat;
  /* 当内容高度大于图片高度时，背景图像的位置相对于viewport固定 */
  background-attachment:fixed;
  /* 让背景图基于容器大小伸缩 */
  background-size:cover;

}
.box {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  width: 380px;
  height: 540px;
  border: 1px solid rgb(255, 255, 255, 0.5);
  backdrop-filter: blur(10px);
  border-radius: 15px;
}
.box > h2 {
  color: rgb(255, 255, 255, 0.9);
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
  color: rgb(255, 255, 255, 0.9);
}
.input-box input {
  width: 250px;
  height: 34px;
  color: black(255, 255, 255, 0.9);
  background: rgb(255, 255, 255, 0.3);
  border: 1px solid rgb(255, 255, 255, 0.5);
  backdrop-filter: blur(10px);
  border-radius: 5px;
  transition: 0.2s;
  padding: 0 10px;
  outline: none;
}
.input-box input :focus {
  border: 1px solid rgb(255, 255, 255, 0.8);
}
.box a {
  display: flex;
  width: 250px;
  flex-direction: column;
  color: white;
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
  align-items: start;
  width: 120px;
  height: 34px;
  align-items: center;
  /*   color: rgb(255, 255, 255, 0.9);*/
   border-radius: 3px;
   border: 1px solid rgb(58, 58, 58, 0.3); /* 这里是对两个按钮设置的背景颜色修改不同图片时候可以按照你的喜好插入 */
  /*  background: rgb(58, 58, 58, 0.3);  */
 /* transition: 0.2s; */
}
/* 这里是鼠标移动上去的变色同理按你的喜好设置 */
.box .btn-box > div > button:hover {
  /* border: 1px solid rgb(59, 47, 49, 0.3); */
  /*background: rgb(59, 47, 49, 0.9);*/
}
.box .btn-box > div > button:nth-child(2) {
  margin-left: 20px;
}
</style>
