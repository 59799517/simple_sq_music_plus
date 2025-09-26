import { defineStore } from "pinia"

const configInfoStore = defineStore('configInfo', {
    state: () => ({
        data: [],
        option: [],
        version: '',
        uiversion: '',
        showPlayButton: false,
        float_bottom: 50,
        float_left:300 ,
        motionPosition: {
            x: 0,
            y: 0
        }

    }),
    getters: {
        getData: (state) => state.data,
        getOption: (state) => state.option
    },
    actions: {
        setData(data) {
            this.data = data
            //找到播放按钮设置
            this.showPlayButton = this.data.find(item => item.configKey === 'system.show.play.url')?.configValue === 'true'
            console.log('播放按钮是否显示',this.showPlayButton)

        },
        setOption(option) {
            this.option = option
        },
        setVersion(version) {
            this.version = version
        },
        setUiVersion(version) {
            this.uiversion = version
        },
        clearData() {
            this.data = []
        },
        clearOption() {
            this.option = []
        },
        clearVersion() {
            this.version = ''
        },
        setMotionPosition(position) {
            this.motionPosition = position
        }
    },
    persist: true,
})

export default configInfoStore