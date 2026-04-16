import { Router } from "vue-router";

// 检测是否为移动设备
const isMobile = () => {
  return /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent);
}

export function createRouteGuard(router) {
    router.beforeEach(async (to, from, next) => {
        // 显示加载条
        window.$loadingBar?.start();
        
        // 特殊处理下载页面，根据设备类型选择合适的组件
        if (to.path === '/V3Download') {
            const isMobileDevice = isMobile();
            // 检查当前路由组件是否与设备类型匹配
            const currentComponent = router.options.routes.find(r => r.path === '/home')
                ?.children?.find(c => c.path === '/V3Download')?.component;
            
            // 如果当前组件与设备类型不匹配，则重新加载对应组件的路由
            if ((isMobileDevice && currentComponent?.name !== 'V3DownloadMobile') || 
                (!isMobileDevice && currentComponent?.name !== 'V3Download')) {
                // 这里可以添加重新加载路由的逻辑，但通常不需要
            }
        }
        
        next();
    });
    
    router.afterEach((to) => {
        window.$loadingBar?.finish();
    })
}