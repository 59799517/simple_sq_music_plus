// vite.config.js
import { defineConfig } from "file:///D:/code/node/simple_sq_music_plus_web/node_modules/.pnpm/vite@4.5.14_terser@5.46.0/node_modules/vite/dist/node/index.js";
import vue from "file:///D:/code/node/simple_sq_music_plus_web/node_modules/.pnpm/@vitejs+plugin-vue@4.6.2_vi_346c6894a89ed9483741408d73e621e7/node_modules/@vitejs/plugin-vue/dist/index.mjs";
import AutoImport from "file:///D:/code/node/simple_sq_music_plus_web/node_modules/.pnpm/unplugin-auto-import@0.16.7_03842f8a28a252d194e4c294c0cc70a1/node_modules/unplugin-auto-import/dist/vite.js";
import Components from "file:///D:/code/node/simple_sq_music_plus_web/node_modules/.pnpm/unplugin-vue-components@0.2_e4803f57b5d60d9cd1b78d373e934300/node_modules/unplugin-vue-components/dist/vite.mjs";
import { NaiveUiResolver } from "file:///D:/code/node/simple_sq_music_plus_web/node_modules/.pnpm/unplugin-vue-components@0.2_e4803f57b5d60d9cd1b78d373e934300/node_modules/unplugin-vue-components/dist/resolvers.mjs";
import { VitePWA } from "file:///D:/code/node/simple_sq_music_plus_web/node_modules/.pnpm/vite-plugin-pwa@0.17.5_vite_329ad219bd417cb9a8a7ab0f8faa61b4/node_modules/vite-plugin-pwa/dist/index.js";
import MotionResolver from "file:///D:/code/node/simple_sq_music_plus_web/node_modules/.pnpm/motion-v@1.10.3_@vueuse+core@14.2.1_vue@3.5.28__vue@3.5.28/node_modules/motion-v/dist/resolver/index.mjs";
var vite_config_default = defineConfig({
  define: {
    __APP_VERSION__: JSON.stringify(process.env.npm_package_version)
  },
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
      resolvers: [NaiveUiResolver(), MotionResolver()]
    }),
    VitePWA({
      strategies: "generateSW",
      manifest: {
        // 安装应用后显示的应用名
        name: "SqMusic",
        short_name: "SqMusic",
        description: "SqMusicTool-\u4E0B\u8F7D\u5DE5\u5177",
        theme_color: "#000000",
        background_color: "#ffffff",
        display: "standalone",
        start_url: "/",
        // 至少配置一个图标
        icons: [{
          // 注意如果应用不是部署在站点根目录则需要相对路径，图片文件放在项目/public/pwa/192x192.png
          src: "/pwa/logo.png",
          sizes: "192x192",
          type: "image/png"
        }, {
          src: "/pwa/logo.png",
          sizes: "512x512",
          type: "image/png"
        }]
      },
      registerType: "autoUpdate",
      workbox: {
        // 对所有匹配的静态资源进行缓存
        globPatterns: ["**/*.{js,css,html,ico,png,svg,webmanifest}"]
      },
      devOptions: {
        enabled: false,
        type: "module"
      }
    })
  ]
});
export {
  vite_config_default as default
};
//# sourceMappingURL=data:application/json;base64,ewogICJ2ZXJzaW9uIjogMywKICAic291cmNlcyI6IFsidml0ZS5jb25maWcuanMiXSwKICAic291cmNlc0NvbnRlbnQiOiBbImNvbnN0IF9fdml0ZV9pbmplY3RlZF9vcmlnaW5hbF9kaXJuYW1lID0gXCJEOlxcXFxjb2RlXFxcXG5vZGVcXFxcc2ltcGxlX3NxX211c2ljX3BsdXNfd2ViXCI7Y29uc3QgX192aXRlX2luamVjdGVkX29yaWdpbmFsX2ZpbGVuYW1lID0gXCJEOlxcXFxjb2RlXFxcXG5vZGVcXFxcc2ltcGxlX3NxX211c2ljX3BsdXNfd2ViXFxcXHZpdGUuY29uZmlnLmpzXCI7Y29uc3QgX192aXRlX2luamVjdGVkX29yaWdpbmFsX2ltcG9ydF9tZXRhX3VybCA9IFwiZmlsZTovLy9EOi9jb2RlL25vZGUvc2ltcGxlX3NxX211c2ljX3BsdXNfd2ViL3ZpdGUuY29uZmlnLmpzXCI7aW1wb3J0IHsgZGVmaW5lQ29uZmlnIH0gZnJvbSAndml0ZSdcclxuaW1wb3J0IHZ1ZSBmcm9tICdAdml0ZWpzL3BsdWdpbi12dWUnXHJcbmltcG9ydCBBdXRvSW1wb3J0IGZyb20gJ3VucGx1Z2luLWF1dG8taW1wb3J0L3ZpdGUnXHJcbmltcG9ydCBDb21wb25lbnRzIGZyb20gJ3VucGx1Z2luLXZ1ZS1jb21wb25lbnRzL3ZpdGUnXHJcbmltcG9ydCB7IE5haXZlVWlSZXNvbHZlciB9IGZyb20gJ3VucGx1Z2luLXZ1ZS1jb21wb25lbnRzL3Jlc29sdmVycydcclxuaW1wb3J0IHsgVml0ZVBXQSB9IGZyb20gJ3ZpdGUtcGx1Z2luLXB3YSdcclxuaW1wb3J0IE1vdGlvblJlc29sdmVyIGZyb20gJ21vdGlvbi12L3Jlc29sdmVyJ1xyXG5cclxuXHJcbi8vIGh0dHBzOi8vdml0ZWpzLmRldi9jb25maWcvXHJcbmV4cG9ydCBkZWZhdWx0IGRlZmluZUNvbmZpZyh7XHJcbiAgZGVmaW5lOiB7XHJcbiAgICBfX0FQUF9WRVJTSU9OX186IEpTT04uc3RyaW5naWZ5KHByb2Nlc3MuZW52Lm5wbV9wYWNrYWdlX3ZlcnNpb24pXHJcbiAgfSxcclxuICBwbHVnaW5zOiBbXHJcbiAgICB2dWUoe1xyXG4gICAgICByZWZUcmFuc2Zvcm06IHRydWVcclxuICAgIH0pLFxyXG4gICAgQXV0b0ltcG9ydCh7XHJcbiAgICAgIGltcG9ydHM6IFtcclxuICAgICAgICAndnVlJyxcclxuICAgICAgICB7XHJcbiAgICAgICAgICAnbmFpdmUtdWknOiBbXHJcbiAgICAgICAgICAgICd1c2VEaWFsb2cnLFxyXG4gICAgICAgICAgICAndXNlTWVzc2FnZScsXHJcbiAgICAgICAgICAgICd1c2VOb3RpZmljYXRpb24nLFxyXG4gICAgICAgICAgICAndXNlTG9hZGluZ0JhcidcclxuICAgICAgICAgIF1cclxuICAgICAgICB9XHJcbiAgICAgIF1cclxuICAgIH0pLFxyXG4gICAgQ29tcG9uZW50cyh7XHJcbiAgICAgIHJlc29sdmVyczogW05haXZlVWlSZXNvbHZlcigpLCBNb3Rpb25SZXNvbHZlcigpXVxyXG4gICAgfSksXHJcbiAgICBWaXRlUFdBKHtcclxuICAgICAgc3RyYXRlZ2llczogJ2dlbmVyYXRlU1cnLFxyXG4gICAgICBtYW5pZmVzdDoge1xyXG4gICAgICAgIC8vIFx1NUI4OVx1ODhDNVx1NUU5NFx1NzUyOFx1NTQwRVx1NjYzRVx1NzkzQVx1NzY4NFx1NUU5NFx1NzUyOFx1NTQwRFxyXG4gICAgICAgIG5hbWU6IFwiU3FNdXNpY1wiLFxyXG4gICAgICAgIHNob3J0X25hbWU6IFwiU3FNdXNpY1wiLFxyXG4gICAgICAgIGRlc2NyaXB0aW9uOiBcIlNxTXVzaWNUb29sLVx1NEUwQlx1OEY3RFx1NURFNVx1NTE3N1wiLFxyXG4gICAgICAgIHRoZW1lX2NvbG9yOiBcIiMwMDAwMDBcIixcclxuICAgICAgICBiYWNrZ3JvdW5kX2NvbG9yOiBcIiNmZmZmZmZcIixcclxuICAgICAgICBkaXNwbGF5OiBcInN0YW5kYWxvbmVcIixcclxuICAgICAgICBzdGFydF91cmw6IFwiL1wiLFxyXG4gICAgICAgIC8vIFx1ODFGM1x1NUMxMVx1OTE0RFx1N0Y2RVx1NEUwMFx1NEUyQVx1NTZGRVx1NjgwN1xyXG4gICAgICAgIGljb25zOiBbe1xyXG4gICAgICAgICAgLy8gXHU2Q0U4XHU2MTBGXHU1OTgyXHU2NzlDXHU1RTk0XHU3NTI4XHU0RTBEXHU2NjJGXHU5MEU4XHU3RjcyXHU1NzI4XHU3QUQ5XHU3MEI5XHU2ODM5XHU3NkVFXHU1RjU1XHU1MjE5XHU5NzAwXHU4OTgxXHU3NkY4XHU1QkY5XHU4REVGXHU1Rjg0XHVGRjBDXHU1NkZFXHU3MjQ3XHU2NTg3XHU0RUY2XHU2NTNFXHU1NzI4XHU5ODc5XHU3NkVFL3B1YmxpYy9wd2EvMTkyeDE5Mi5wbmdcclxuICAgICAgICAgIHNyYzogXCIvcHdhL2xvZ28ucG5nXCIsXHJcbiAgICAgICAgICBzaXplczogXCIxOTJ4MTkyXCIsXHJcbiAgICAgICAgICB0eXBlOiBcImltYWdlL3BuZ1wiXHJcbiAgICAgICAgfSwge1xyXG4gICAgICAgICAgc3JjOiBcIi9wd2EvbG9nby5wbmdcIixcclxuICAgICAgICAgIHNpemVzOiBcIjUxMng1MTJcIixcclxuICAgICAgICAgIHR5cGU6IFwiaW1hZ2UvcG5nXCJcclxuICAgICAgICB9XVxyXG4gICAgICB9LFxyXG4gICAgICByZWdpc3RlclR5cGU6IFwiYXV0b1VwZGF0ZVwiLFxyXG4gICAgICB3b3JrYm94OiB7XHJcbiAgICAgICAgLy8gXHU1QkY5XHU2MjQwXHU2NzA5XHU1MzM5XHU5MTREXHU3Njg0XHU5NzU5XHU2MDAxXHU4RDQ0XHU2RTkwXHU4RkRCXHU4ODRDXHU3RjEzXHU1QjU4XHJcbiAgICAgICAgZ2xvYlBhdHRlcm5zOiBbXCIqKi8qLntqcyxjc3MsaHRtbCxpY28scG5nLHN2Zyx3ZWJtYW5pZmVzdH1cIl0sXHJcbiAgICAgIH0sXHJcbiAgICAgIGRldk9wdGlvbnM6IHtcclxuICAgICAgICBlbmFibGVkOiBmYWxzZSxcclxuICAgICAgICB0eXBlOiBcIm1vZHVsZVwiXHJcbiAgICAgIH1cclxuXHJcbiAgICB9KVxyXG4gIF1cclxufSkiXSwKICAibWFwcGluZ3MiOiAiO0FBQXlTLFNBQVMsb0JBQW9CO0FBQ3RVLE9BQU8sU0FBUztBQUNoQixPQUFPLGdCQUFnQjtBQUN2QixPQUFPLGdCQUFnQjtBQUN2QixTQUFTLHVCQUF1QjtBQUNoQyxTQUFTLGVBQWU7QUFDeEIsT0FBTyxvQkFBb0I7QUFJM0IsSUFBTyxzQkFBUSxhQUFhO0FBQUEsRUFDMUIsUUFBUTtBQUFBLElBQ04saUJBQWlCLEtBQUssVUFBVSxRQUFRLElBQUksbUJBQW1CO0FBQUEsRUFDakU7QUFBQSxFQUNBLFNBQVM7QUFBQSxJQUNQLElBQUk7QUFBQSxNQUNGLGNBQWM7QUFBQSxJQUNoQixDQUFDO0FBQUEsSUFDRCxXQUFXO0FBQUEsTUFDVCxTQUFTO0FBQUEsUUFDUDtBQUFBLFFBQ0E7QUFBQSxVQUNFLFlBQVk7QUFBQSxZQUNWO0FBQUEsWUFDQTtBQUFBLFlBQ0E7QUFBQSxZQUNBO0FBQUEsVUFDRjtBQUFBLFFBQ0Y7QUFBQSxNQUNGO0FBQUEsSUFDRixDQUFDO0FBQUEsSUFDRCxXQUFXO0FBQUEsTUFDVCxXQUFXLENBQUMsZ0JBQWdCLEdBQUcsZUFBZSxDQUFDO0FBQUEsSUFDakQsQ0FBQztBQUFBLElBQ0QsUUFBUTtBQUFBLE1BQ04sWUFBWTtBQUFBLE1BQ1osVUFBVTtBQUFBO0FBQUEsUUFFUixNQUFNO0FBQUEsUUFDTixZQUFZO0FBQUEsUUFDWixhQUFhO0FBQUEsUUFDYixhQUFhO0FBQUEsUUFDYixrQkFBa0I7QUFBQSxRQUNsQixTQUFTO0FBQUEsUUFDVCxXQUFXO0FBQUE7QUFBQSxRQUVYLE9BQU8sQ0FBQztBQUFBO0FBQUEsVUFFTixLQUFLO0FBQUEsVUFDTCxPQUFPO0FBQUEsVUFDUCxNQUFNO0FBQUEsUUFDUixHQUFHO0FBQUEsVUFDRCxLQUFLO0FBQUEsVUFDTCxPQUFPO0FBQUEsVUFDUCxNQUFNO0FBQUEsUUFDUixDQUFDO0FBQUEsTUFDSDtBQUFBLE1BQ0EsY0FBYztBQUFBLE1BQ2QsU0FBUztBQUFBO0FBQUEsUUFFUCxjQUFjLENBQUMsNENBQTRDO0FBQUEsTUFDN0Q7QUFBQSxNQUNBLFlBQVk7QUFBQSxRQUNWLFNBQVM7QUFBQSxRQUNULE1BQU07QUFBQSxNQUNSO0FBQUEsSUFFRixDQUFDO0FBQUEsRUFDSDtBQUNGLENBQUM7IiwKICAibmFtZXMiOiBbXQp9Cg==
