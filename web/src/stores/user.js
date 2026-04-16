import { defineStore } from "pinia"
import {getAllSet, isLogin, login} from "../utils/api.js";
import { useRoute, useRouter } from 'vue-router'
const router = useRouter()



const userInfoStore = defineStore('userInfo', {
    persist: true,

    state: () => ({
        username: '',
        token: '',
        loginDevice:'web',
        device:'web'
    }),
    actions: {
        setUserName(username){
            this.username = username
        },
        setToken(token){
            this.token = token
        },
        setLoginDevice(loginDevice){
            this.loginDevice = loginDevice
        },
        isLogin() {
            return isLogin()
        },
        clearUserInfo() {
            this.username = ''
            this.token = ''
            this.loginDevice = ''
        },
    }
})

export default userInfoStore