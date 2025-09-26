import { createApp, reactive } from 'vue'
import './style.css'
import App from './App.vue'
import {router} from './router/index.js'
import pinia from './stores'  //引入



const playerData = reactive({
})

// 创建应用实例
const app = createApp(App)

// 先初始化 Pinia
app.use(pinia)

// 然后设置前端版本号
import configInfoStore from './stores/config'
const configStore = configInfoStore()
configStore.setUiVersion('v' + __APP_VERSION__)

// 继续其他初始化
app.use(router).provide('playerData', playerData).mount('#app')
