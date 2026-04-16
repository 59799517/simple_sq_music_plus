import { createRouter,createWebHashHistory} from "vue-router";

const home = () => import("../components/Home.vue")
const login = () => import("../components/Login.vue")

const V3Parsertext = () => import("../components/V3Parsertext.vue")
const V3Search = () => import("../components/V3Search.vue")
const V3Set = () => import("../components/V3Set.vue")
const V3Download = () => import("../components/V3Download.vue")
const V3DownloadMobile = () => import("../components/V3DownloadMobile.vue")
const V3ParserPlaylist = () => import("../components/V3ParserPlaylist.vue")
const AliyunSync = () => import("../components/AliyunSync.vue")

const  Monitor = () => import("../components/Monitor.vue")

// 检测是否为移动设备
const isMobile = () => {
  return /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent);
}

const routes = [
    { path: "/", redirect: "/login" },
    {
        path: "/login",
        name: "login",
        component: login
    },
    {
        path: "/home",
        name: "home",
        component: home,
        children: [
            {
                path: "",
                redirect: "/v3search"
            },
            {
                path: "/v3search",
                name: "v3search",
                component: V3Search
            },
            {
                path: "/V3Download",
                name: "V3Download",
                component: () => isMobile() ? V3DownloadMobile() : V3Download()
            },
            {
                path: "/v3set",
                name: "v3set",
                component: V3Set
            },
            {
                path: "/V3ParserPlaylist",
                name: "V3ParserPlaylist",
                component: V3ParserPlaylist
            },
            {
                path: "/V3Parsertext",
                name: "V3Parsertext",
                component: V3Parsertext
            },
            {
                path: "/Monitor",
                name: "Monitor",
                component: Monitor
            },
            {
                path: "/AliyunSync",
                name: "AliyunSync",
                component: AliyunSync
            }
        ]
    }
    // 移除移动端播放页面独立路由
]

export const router = createRouter({
    history: createWebHashHistory(),
    routes: routes
})