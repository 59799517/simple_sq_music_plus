import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import { NaiveUiResolver } from 'unplugin-vue-components/resolvers'
import { VitePWA } from 'vite-plugin-pwa'


// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    vue({
      refTransform: true
    }),
    AutoImport({
      imports: [
        'vue',
        {
          'naive-ui': [
            'useDialog',
            'useMessage',
            'useNotification',
            'useLoadingBar'
          ]
        }
      ]
    }),
    Components({
      resolvers: [NaiveUiResolver()]
    }),
    VitePWA({
      manifest: {
        // 安装应用后显示的应用名
        name: "SqMusic",
        description: "SqMusicTool-下载工具",
        // 至少配置一个图标
        icons: [{
          // 注意如果应用不是部署在站点根目录则需要相对路径，图片文件放在项目/public/pwa/192x192.png
          src: "./pwa/logo.png",
          sizes: "192x192",
          type: "image/png"
        }]
      },
      registerType: "autoUpdate",
      workbox: {
        // 对所有匹配的静态资源进行缓存
        globPatterns: ["**/*.{js,css,html,ico,png,svg}"],
      },
      devOptions: {
        enabled: true
      }

    })
  ]
})