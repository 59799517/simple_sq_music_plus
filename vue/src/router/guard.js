import { Router } from "vue-router";

export function createRouteGuard(router: Router) {
    router.beforeEach(async () => {
        // 显示加载条
        window.$loadingBar?.start();
    });
    /* eslint-disable */
    router.afterEach((to: any) => {
        window.$loadingBar?.finish();
    })
}