<script setup lang="js">
import { defineStore } from 'pinia'
import Set from "./V3Set.vue";


import configInfoStore from "../stores/config";
const stconfigInfoStore =configInfoStore()


// 设置参数
const active = ref(false);
const placement = ref("right");
const activate = (place) => {
  active.value = true;
  placement.value = place;
};



</script>

<template>
  <div>
    <div class="header">
      <div class="box"  >
        <h2>SqMusic</h2>
        <p>
          <n-gradient-text :size="12" type="success" >
            &nbsp;{{stconfigInfoStore.version}}
          </n-gradient-text>
        </p>
      </div>
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
      </div>
      <div class="box">
<!--        <router-link active-class="active" to="/V3Set">-->
          <n-button  size="large" quaternary @click="activate('right')">
            设置
          </n-button>
<!--        </router-link>-->
<!--        <n-button @click="clockTheam" quaternary>-->
<!--          切换主题-->
<!--        </n-button>-->
<!--        <n-button @click="logout_b">设置</n-button>-->
      </div>
    </div>
  </div>
<!--设置弹出框-->
  <n-drawer v-model:show="active" :width="502" :placement="placement">
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