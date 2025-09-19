// vite.config.js
import { defineConfig } from "file:///D:/code/vue/sq_music_plus_v2/node_modules/.pnpm/vite@4.5.14_terser@5.43.1/node_modules/vite/dist/node/index.js";
import vue from "file:///D:/code/vue/sq_music_plus_v2/node_modules/.pnpm/@vitejs+plugin-vue@4.6.2_vite@4.5.14_terser@5.43.1__vue@3.5.18/node_modules/@vitejs/plugin-vue/dist/index.mjs";
import AutoImport from "file:///D:/code/vue/sq_music_plus_v2/node_modules/.pnpm/unplugin-auto-import@0.16.7_rollup@2.79.2/node_modules/unplugin-auto-import/dist/vite.js";
import Components from "file:///D:/code/vue/sq_music_plus_v2/node_modules/.pnpm/unplugin-vue-components@0.25.2_@babel+parser@7.28.3_rollup@2.79.2_vue@3.5.18/node_modules/unplugin-vue-components/dist/vite.mjs";
import { NaiveUiResolver } from "file:///D:/code/vue/sq_music_plus_v2/node_modules/.pnpm/unplugin-vue-components@0.25.2_@babel+parser@7.28.3_rollup@2.79.2_vue@3.5.18/node_modules/unplugin-vue-components/dist/resolvers.mjs";
import { VitePWA } from "file:///D:/code/vue/sq_music_plus_v2/node_modules/.pnpm/vite-plugin-pwa@0.16.7_vite@4.5.14_terser@5.43.1__workbox-build@7.3.0_workbox-window@7.3.0/node_modules/vite-plugin-pwa/dist/index.js";
var vite_config_default = defineConfig({
  plugins: [
    vue({
      refTransform: true
    }),
    AutoImport({
      imports: [
        "vue",
        {
          "naive-ui": [
            "useDialog",
            "useMessage",
            "useNotification",
            "useLoadingBar"
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
        description: "SqMusicTool-\u4E0B\u8F7D\u5DE5\u5177",
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
        globPatterns: ["**/*.{js,css,html,ico,png,svg}"]
      },
      devOptions: {
        enabled: true
      }
    })
  ]
});
export {
  vite_config_default as default
};
//# sourceMappingURL=data:application/json;base64,ewogICJ2ZXJzaW9uIjogMywKICAic291cmNlcyI6IFsidml0ZS5jb25maWcuanMiXSwKICAic291cmNlc0NvbnRlbnQiOiBbImNvbnN0IF9fdml0ZV9pbmplY3RlZF9vcmlnaW5hbF9kaXJuYW1lID0gXCJEOlxcXFxjb2RlXFxcXHZ1ZVxcXFxzcV9tdXNpY19wbHVzX3YyXCI7Y29uc3QgX192aXRlX2luamVjdGVkX29yaWdpbmFsX2ZpbGVuYW1lID0gXCJEOlxcXFxjb2RlXFxcXHZ1ZVxcXFxzcV9tdXNpY19wbHVzX3YyXFxcXHZpdGUuY29uZmlnLmpzXCI7Y29uc3QgX192aXRlX2luamVjdGVkX29yaWdpbmFsX2ltcG9ydF9tZXRhX3VybCA9IFwiZmlsZTovLy9EOi9jb2RlL3Z1ZS9zcV9tdXNpY19wbHVzX3YyL3ZpdGUuY29uZmlnLmpzXCI7aW1wb3J0IHsgZGVmaW5lQ29uZmlnIH0gZnJvbSAndml0ZSdcbmltcG9ydCB2dWUgZnJvbSAnQHZpdGVqcy9wbHVnaW4tdnVlJ1xuaW1wb3J0IEF1dG9JbXBvcnQgZnJvbSAndW5wbHVnaW4tYXV0by1pbXBvcnQvdml0ZSdcbmltcG9ydCBDb21wb25lbnRzIGZyb20gJ3VucGx1Z2luLXZ1ZS1jb21wb25lbnRzL3ZpdGUnXG5pbXBvcnQgeyBOYWl2ZVVpUmVzb2x2ZXIgfSBmcm9tICd1bnBsdWdpbi12dWUtY29tcG9uZW50cy9yZXNvbHZlcnMnXG5pbXBvcnQgeyBWaXRlUFdBIH0gZnJvbSAndml0ZS1wbHVnaW4tcHdhJ1xuXG5cbi8vIGh0dHBzOi8vdml0ZWpzLmRldi9jb25maWcvXG5leHBvcnQgZGVmYXVsdCBkZWZpbmVDb25maWcoe1xuICBwbHVnaW5zOiBbXG4gICAgdnVlKHtcbiAgICAgIHJlZlRyYW5zZm9ybTogdHJ1ZVxuICAgIH0pLFxuICAgIEF1dG9JbXBvcnQoe1xuICAgICAgaW1wb3J0czogW1xuICAgICAgICAndnVlJyxcbiAgICAgICAge1xuICAgICAgICAgICduYWl2ZS11aSc6IFtcbiAgICAgICAgICAgICd1c2VEaWFsb2cnLFxuICAgICAgICAgICAgJ3VzZU1lc3NhZ2UnLFxuICAgICAgICAgICAgJ3VzZU5vdGlmaWNhdGlvbicsXG4gICAgICAgICAgICAndXNlTG9hZGluZ0JhcidcbiAgICAgICAgICBdXG4gICAgICAgIH1cbiAgICAgIF1cbiAgICB9KSxcbiAgICBDb21wb25lbnRzKHtcbiAgICAgIHJlc29sdmVyczogW05haXZlVWlSZXNvbHZlcigpXVxuICAgIH0pLFxuICAgIFZpdGVQV0Eoe1xuICAgICAgbWFuaWZlc3Q6IHtcbiAgICAgICAgLy8gXHU1Qjg5XHU4OEM1XHU1RTk0XHU3NTI4XHU1NDBFXHU2NjNFXHU3OTNBXHU3Njg0XHU1RTk0XHU3NTI4XHU1NDBEXG4gICAgICAgIG5hbWU6IFwiU3FNdXNpY1wiLFxuICAgICAgICBkZXNjcmlwdGlvbjogXCJTcU11c2ljVG9vbC1cdTRFMEJcdThGN0RcdTVERTVcdTUxNzdcIixcbiAgICAgICAgLy8gXHU4MUYzXHU1QzExXHU5MTREXHU3RjZFXHU0RTAwXHU0RTJBXHU1NkZFXHU2ODA3XG4gICAgICAgIGljb25zOiBbe1xuICAgICAgICAgIC8vIFx1NkNFOFx1NjEwRlx1NTk4Mlx1Njc5Q1x1NUU5NFx1NzUyOFx1NEUwRFx1NjYyRlx1OTBFOFx1N0Y3Mlx1NTcyOFx1N0FEOVx1NzBCOVx1NjgzOVx1NzZFRVx1NUY1NVx1NTIxOVx1OTcwMFx1ODk4MVx1NzZGOFx1NUJGOVx1OERFRlx1NUY4NFx1RkYwQ1x1NTZGRVx1NzI0N1x1NjU4N1x1NEVGNlx1NjUzRVx1NTcyOFx1OTg3OVx1NzZFRS9wdWJsaWMvcHdhLzE5MngxOTIucG5nXG4gICAgICAgICAgc3JjOiBcIi4vcHdhL2xvZ28ucG5nXCIsXG4gICAgICAgICAgc2l6ZXM6IFwiMTkyeDE5MlwiLFxuICAgICAgICAgIHR5cGU6IFwiaW1hZ2UvcG5nXCJcbiAgICAgICAgfV1cbiAgICAgIH0sXG4gICAgICByZWdpc3RlclR5cGU6IFwiYXV0b1VwZGF0ZVwiLFxuICAgICAgd29ya2JveDoge1xuICAgICAgICAvLyBcdTVCRjlcdTYyNDBcdTY3MDlcdTUzMzlcdTkxNERcdTc2ODRcdTk3NTlcdTYwMDFcdThENDRcdTZFOTBcdThGREJcdTg4NENcdTdGMTNcdTVCNThcbiAgICAgICAgZ2xvYlBhdHRlcm5zOiBbXCIqKi8qLntqcyxjc3MsaHRtbCxpY28scG5nLHN2Z31cIl0sXG4gICAgICB9LFxuICAgICAgZGV2T3B0aW9uczoge1xuICAgICAgICBlbmFibGVkOiB0cnVlXG4gICAgICB9XG5cbiAgICB9KVxuICBdXG59KSJdLAogICJtYXBwaW5ncyI6ICI7QUFBOFEsU0FBUyxvQkFBb0I7QUFDM1MsT0FBTyxTQUFTO0FBQ2hCLE9BQU8sZ0JBQWdCO0FBQ3ZCLE9BQU8sZ0JBQWdCO0FBQ3ZCLFNBQVMsdUJBQXVCO0FBQ2hDLFNBQVMsZUFBZTtBQUl4QixJQUFPLHNCQUFRLGFBQWE7QUFBQSxFQUMxQixTQUFTO0FBQUEsSUFDUCxJQUFJO0FBQUEsTUFDRixjQUFjO0FBQUEsSUFDaEIsQ0FBQztBQUFBLElBQ0QsV0FBVztBQUFBLE1BQ1QsU0FBUztBQUFBLFFBQ1A7QUFBQSxRQUNBO0FBQUEsVUFDRSxZQUFZO0FBQUEsWUFDVjtBQUFBLFlBQ0E7QUFBQSxZQUNBO0FBQUEsWUFDQTtBQUFBLFVBQ0Y7QUFBQSxRQUNGO0FBQUEsTUFDRjtBQUFBLElBQ0YsQ0FBQztBQUFBLElBQ0QsV0FBVztBQUFBLE1BQ1QsV0FBVyxDQUFDLGdCQUFnQixDQUFDO0FBQUEsSUFDL0IsQ0FBQztBQUFBLElBQ0QsUUFBUTtBQUFBLE1BQ04sVUFBVTtBQUFBO0FBQUEsUUFFUixNQUFNO0FBQUEsUUFDTixhQUFhO0FBQUE7QUFBQSxRQUViLE9BQU8sQ0FBQztBQUFBO0FBQUEsVUFFTixLQUFLO0FBQUEsVUFDTCxPQUFPO0FBQUEsVUFDUCxNQUFNO0FBQUEsUUFDUixDQUFDO0FBQUEsTUFDSDtBQUFBLE1BQ0EsY0FBYztBQUFBLE1BQ2QsU0FBUztBQUFBO0FBQUEsUUFFUCxjQUFjLENBQUMsZ0NBQWdDO0FBQUEsTUFDakQ7QUFBQSxNQUNBLFlBQVk7QUFBQSxRQUNWLFNBQVM7QUFBQSxNQUNYO0FBQUEsSUFFRixDQUFDO0FBQUEsRUFDSDtBQUNGLENBQUM7IiwKICAibmFtZXMiOiBbXQp9Cg==
