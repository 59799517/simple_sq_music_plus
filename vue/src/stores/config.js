import { defineStore } from "pinia"

const configInfoStore = defineStore('configInfo', {
    state: () => ({
        data: [],
        option: [],
        version: '',
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
    },
    persist: true,
})




export default configInfoStore