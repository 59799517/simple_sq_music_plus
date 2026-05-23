package com.sqmusicplus.v3.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * 登录认证检查器
 * 检查接口是否有 @RequireLogin 注解，并验证用户是否登录
 */
@Slf4j
@Component
public class LoginCheckInterceptor {

    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 检查当前请求是否需要登录
     *
     * @param request HTTP 请求
     * @return true-需要登录且已登录，false-不需要登录或未登录
     */
    public boolean checkLogin(HttpServletRequest request) {
        try {
            // 获取 handler
            HandlerExecutionChain handlerChain = requestMappingHandlerMapping.getHandler(request);
            if (handlerChain == null) {
                return true; // 没有找到 handler，放行
            }

            Object handler = handlerChain.getHandler();
            if (!(handler instanceof HandlerMethod)) {
                return true; // 不是方法 handler，放行
            }

            HandlerMethod handlerMethod = (HandlerMethod) handler;

            // 检查方法上是否有 @RequireLogin 注解
            RequireLogin methodAnnotation = handlerMethod.getMethodAnnotation(RequireLogin.class);
            
            // 检查类上是否有 @RequireLogin 注解
            RequireLogin classAnnotation = handlerMethod.getBeanType().getAnnotation(RequireLogin.class);

            // 如果方法和类上都没有注解，默认需要登录（根据 SecurityConfig 的配置）
            if (methodAnnotation == null && classAnnotation == null) {
                return true; // 使用 Spring Security 的默认配置
            }

            // 获取注解的值（优先使用方法上的注解）
            boolean requireLogin = methodAnnotation != null ? methodAnnotation.value() : classAnnotation.value();

            if (!requireLogin) {
                // 不需要登录，直接放行
                return true;
            }

            // 需要登录，检查用户是否已登录
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                log.warn("未登录用户尝试访问需要认证的接口: {}", request.getRequestURI());
                return false;
            }

            // 检查 Token 是否在黑名单中
            String token = getTokenFromRequest(request);
            if (token != null && jwtUtil.isBlacklisted(token)) {
                log.warn("黑名单 Token 尝试访问接口: {}", request.getRequestURI());
                return false;
            }

            return true;

        } catch (Exception e) {
            log.error("登录检查失败", e);
            return true; // 出错时放行，避免影响正常请求
        }
    }

    /**
     * 从请求中获取 Token
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
