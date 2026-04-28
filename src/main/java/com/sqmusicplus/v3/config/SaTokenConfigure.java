package com.sqmusicplus.v3.config;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.filter.SaServletFilter;
import cn.dev33.satoken.jwt.StpLogicJwtForStateless;
import cn.dev33.satoken.router.SaHttpMethod;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.spring.pathmatch.SaPathMatcherHolder;
import cn.dev33.satoken.spring.pathmatch.SaPatternsRequestConditionHolder;
import cn.dev33.satoken.stp.StpLogic;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.strategy.SaStrategy;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Created with IntelliJ IDEA.
 * User: SQ
 * Date: 2022/8/6
 * Time: 17:33
 * Description: Sa-Token 配置类
 */
@Configuration
public class SaTokenConfigure implements WebMvcConfigurer {

//    使用jwt记录
    @Bean
    public StpLogic getStpLogicJwt() {
        return new StpLogicJwtForStateless();
    }

    /** 注册 [Sa-Token全局过滤器] */
    @Bean
    public SaServletFilter getSaServletFilter() {
        return new SaServletFilter()
                .addInclude("/**")
                .addExclude("/**/*.webmanifest","/**/*.md","/**/*.svg","/**/*.json","/**/*.js","/","/**/*.html","/**/*.css","/**/*.png","/**/*.jpg","/**/*.otf","/**/*.ttf","/**/*.wasm","/login", "/favicon.ico","/isLogin","/set/selectOption","/set/getSetList","/set/getSearchType","/set/getSearchTypeBrType","/set/getSetListByType","/set/getSetListByTypeBrType","/set/getSetListByTypeBrTypeId","/set/getSetListByTypeId","/set/getSetListByTypeIdBrType","/set/getSetListByTypeIdBrTypeId","/set/getSetListByTypeIdBrTypeIdId","/set/check","/download/freemp3")
                .addExclude("/api/**")
                .setBeforeAuth(obj -> {
                    // ---------- 设置跨域响应头 ----------
                    SaHolder.getResponse()
                            // 允许指定域访问跨域资源
                            .setHeader("Access-Control-Allow-Origin", "*")
                            // 允许所有请求方式
                            .setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE")
                            // 有效时间
                            .setHeader("Access-Control-Max-Age", "3600")
                            // 允许的header参数
                            .setHeader("Access-Control-Allow-Headers", "*");

                    // 如果是预检请求，则立即返回到前端
                    SaRouter.match(SaHttpMethod.OPTIONS)
                            .back();
                })

                .setAuth(obj -> {
                    if(SaHolder.getRequest().getMethod().equals("OPTIONS")){
                        SaHolder.getResponse().setStatus(200);
                        SaHolder.getResponse().addHeader("Content-Type","application/json;charset=UTF-8");
                        SaHolder.getResponse().addHeader("Access-Control-Allow-Headers","appId");
                        SaHolder.getResponse().addHeader("Access-Control-Allow-Methods","POST, GET, OPTIONS");
                        SaHolder.getResponse().addHeader("Access-Control-Allow-Origin","*");
                        SaRouter.back();
                    }
                    if(!StpUtil.isLogin()) {
                            SaHolder.getResponse().setStatus(401);
                        SaRouter.back();
                    }
                });
    }
}
