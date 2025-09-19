import { defineStore } from "pinia"
import {inject,watch } from 'vue'
import {getLyric, getMusicUrl} from "../utils/api.js";


import configInfoStore from "./config.js";


let playerData = inject("playerData");

export const usePlayListStore = defineStore("playList", {
    persist: {
        key: "playList",
        storage: localStorage,
        paths: [
            "playList",
            "playIndex",
            "albumName",
            "albumid",
            "artistName",
            "artistids",
            "brTypes",
            "duration",
            "id",
            "lyric",
            "lyricId",
            "name",
            "pic",
            "plugName",
            "isPlaying",
            "currentTime",
            "totalTime",
            "musicUrl",
            "volume",
            "playbackRate"
            // 注意：audioElement 不应该被持久化
        ]
    },
    state: () => ({
    playList: [],
    playIndex:0,
    albumName: "",
    albumid: "",
    artistName: [],
    artistids: [],
    brTypes: [],
    duration: "",
    id: "",
    lyric: "",
    lyricId: "",
    name: "",
    pic: "",
    plugName: "",
    isPlaying: false, // 是否正在播放
    currentTime: 0,   // 当前播放时间
    totalTime: 0,     // 总时间
    musicUrl: "",     // 当前歌曲的播放链接
    volume: 0.7,      // 音量 (0-1)
    playbackRate: 1,  // 播放速度
    shouldAutoPlay: false // 是否应该自动播放（用于组件挂载后播放）
  }),
    getters: {
    getPlayId() {
        return this.playList[this.playIndex].id
    },
    },
    actions: {
        pushPlayList(data) {
            //找到历史播放歌曲是否有一样的如果有则删除掉
            let isHave = this.playList.findIndex(item => item.id === data.id)
            if (isHave !== -1) {
                this.playList.splice(isHave, 1)
            }
            this.playList.push(data)
            this.playIndex = this.playList.length-1
        },
        async pushPlayListAndPlay(data) {
            // 先尝试获取播放链接，如果失败则不加入播放队列
            try {
                const response = await getMusicUrl(data, data.brTypes[0]);
                if (response.data.code === 200) {
                    this.pushPlayList(data)
                    this.playIndex = this.playList.length-1
                    this.setPlayIndex(this.playIndex)
                } else {
                    console.error("获取播放链接失败:", response.data.msg);
                    window.$message.error("获取播放链接失败: " + response.data.msg);
                    return false; // 返回失败状态
                }
            } catch (error) {
                console.error("获取播放链接时发生错误:", error);
                window.$message.error("获取播放链接时发生错误");
                return false; // 返回失败状态
            }
        },
        clearPlayList() {
            this.playList = []
            this.playIndex = 0
        },
        setPlayIndex(index) {
            //判断索引是否存在
            if (this.playList[index]) {
                this.playIndex = index
                let nowPlay = this.playList[index];
                this.albumName = nowPlay.albumName;
                this.albumid = nowPlay.albumid;
                this.artistName = nowPlay.artistName;
                this.artistids = nowPlay.artistids;
                this.brTypes = nowPlay.brTypes;
                this.duration = nowPlay.duration;
                this.id = nowPlay.id;
                this.lyric = nowPlay.lyric;
                this.lyricId = nowPlay.lyricId;
                this.name = nowPlay.name;
                this.pic = nowPlay.pic;
                this.plugName = nowPlay.plugName;

                // 获取播放链接
                this.fetchMusicUrl(nowPlay);
            }
        },
        // 设置播放状态
        setIsPlaying(status) {
            this.isPlaying = status;
        },
        // 设置当前播放时间
        setCurrentTime(time) {
            this.currentTime = time;
        },
        // 设置总时间
        setTotalTime(time) {
            this.totalTime = time;
        },
        // 播放上一曲
        playPrevious() {
            if (this.playList.length > 0) {
                let newIndex = this.playIndex - 1;
                if (newIndex < 0) {
                    newIndex = this.playList.length - 1; // 循环到最后一首
                }
                this.setPlayIndex(newIndex);
            }
        },
        // 播放下一曲
        playNext() {
            if (this.playList.length > 0) {
                let newIndex = this.playIndex + 1;
                if (newIndex >= this.playList.length) {
                    newIndex = 0; // 循环到第一首
                }
                this.setPlayIndex(newIndex);
            }
        },

        // 获取歌曲播放链接
        async fetchMusicUrl(songData) {
            try {
                // 使用歌曲数据和插件名称调用getMusicUrl
                // 修复参数传递，确保传入正确的参数
                const response = await getMusicUrl(songData, songData.brTypes[0]);
                if (response.data.code === 200) {
                    this.musicUrl = response.data.data.url;
                    console.log("获取播放链接成功:", this.musicUrl);
                    // 找出当前歌曲的索引
                    getLyric(songData.id, songData.plugName).then(getLyricdata => {
                        if (getLyricdata.data.code === 200){
                            console.log("歌词获取成功");
                            console.log(getLyricdata.data.msg)
                            const index = this.playList.findIndex(item => item.id === songData.id);
                            if (index !== -1) {
                                this.playList[index].lyric = getLyricdata.data.msg;
                            }
                            if (this.id ===songData.id){
                                this.lyric = getLyricdata.data.msg;
                            }
                        }
                    });
                    // 设置自动播放标志，但不立即初始化音频
                    this.shouldAutoPlay = true;
                } else {
                    console.error("获取播放链接失败:", response.data.msg);
                    window.$message.error("获取播放链接失败: " + response.data.msg);
                }
            } catch (error) {
                console.error("获取播放链接时发生错误:", error);
                window.$message.error("获取播放链接时发生错误");
            }
        },
        // 播放（现在由Home.vue控制）
        play() {
            this.isPlaying = true;
        },
        // 暂停（现在由Home.vue控制）
        pause() {
            this.isPlaying = false;
        },
        // 切换播放/暂停状态（现在由Home.vue控制）
        togglePlay() {
            this.isPlaying = !this.isPlaying;
        },
        // 设置音量
        setVolume(volume) {
            this.volume = volume;
        },
        // 设置播放速度
        setPlaybackRate(rate) {
            this.playbackRate = rate;
        },
        // 跳转到指定时间（现在由Home.vue控制）
        seekTo(time) {
            this.currentTime = time;
        },
        // 清理播放器资源（不再需要）
        destroy() {
            // 不再需要清理音频元素
        }
      },
})

export default usePlayListStore