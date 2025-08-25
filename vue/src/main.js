import { createApp } from 'vue'
import './style.css'
import App from './App.vue'
import {router} from './router/index.js'
import pinia from './stores'  //引入


createApp(App).use(pinia).use(router).mount('#app')
