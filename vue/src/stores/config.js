import { defineStore } from "pinia"

const configInfoStore = defineStore('configInfo', {
    state: () => ({
        data: [],
        option: [],
        version: '',
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
        },
        setOption(option) {
            this.option = option
        },
        setVersion(version) {
            this.version = version
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