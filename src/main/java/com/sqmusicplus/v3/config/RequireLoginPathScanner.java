package com.sqmusicplus.v3.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 自动扫描 @RequireLogin 注解，收集不需要登录的路径
 */
@Slf4j
@Component
public class RequireLoginPathScanner {

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 获取所有标记为 @RequireLogin(false) 的路径
     *
     * @return 不需要登录的路径集合
     */
    public Set<String> getPublicPaths() {
        Set<String> publicPaths = new HashSet<>();
        
        try {
            RequestMappingHandlerMapping handlerMapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
            Map<RequestMappingInfo, HandlerMethod> handlerMethods = handlerMapping.getHandlerMethods();
            
            log.info("开始扫描 @RequireLogin 注解，共 {} 个 Handler", handlerMethods.size());

            for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
                HandlerMethod handlerMethod = entry.getValue();
                RequestMappingInfo requestMappingInfo = entry.getKey();
                
                String beanName = handlerMethod.getBeanType().getSimpleName();
                String methodName = handlerMethod.getMethod().getName();
                boolean isPublic = false;

                // 检查方法上是否有 @RequireLogin(false)
                RequireLogin methodAnnotation = handlerMethod.getMethodAnnotation(RequireLogin.class);
                if (methodAnnotation != null && !methodAnnotation.value()) {
                    isPublic = true;
                    log.debug("检测到 @RequireLogin 注解: {}.{} value={}", beanName, methodName, methodAnnotation.value());
                }

                // 检查类上是否有 @RequireLogin(false)
                if (!isPublic) {
                    RequireLogin classAnnotation = handlerMethod.getBeanType().getAnnotation(RequireLogin.class);
                    if (classAnnotation != null && !classAnnotation.value()) {
                        isPublic = true;
                        log.debug("检测到类级别 @RequireLogin 注解: {} value={}", beanName, classAnnotation.value());
                    }
                }
                
                // 如果是公开接口，提取路径
                if (isPublic) {
                    // 尝试多种方式获取路径
                    Set<String> patterns = new HashSet<>();
                    
                    // 方式1: getPathPatternsCondition (Spring Boot 2.6+)
                    if (requestMappingInfo.getPathPatternsCondition() != null) {
                        patterns.addAll(requestMappingInfo.getPathPatternsCondition().getPatternValues());
                    }
                    
                    // 方式2: getPatternsCondition (旧版本)
                    if (patterns.isEmpty() && requestMappingInfo.getPatternsCondition() != null) {
                        patterns.addAll(requestMappingInfo.getPatternsCondition().getPatterns());
                    }
                    
                    // 添加路径到结果集
                    for (String pattern : patterns) {
                        String path = normalizePath(pattern);
                        publicPaths.add(path);
                        log.debug("✅ 发现公开接口: {} -> {}", beanName + "." + methodName, path);
                    }
                    
                    // 如果还是没有路径，记录警告
                    if (patterns.isEmpty()) {
                        log.warn("⚠️ 无法提取路径: {}.{}", beanName, methodName);
                    }
                }
            }

            log.debug("🎯 共扫描到 {} 个公开接口（无需登录）: {}", publicPaths.size(), publicPaths);
        } catch (Exception e) {
            log.error("❌ 扫描 @RequireLogin 注解失败", e);
        }

        return publicPaths;
    }

    /**
     * 标准化路径，将 Ant 风格路径转换为 Spring Security 兼容的格式
     */
    private String normalizePath(String pattern) {
        // 确保路径以 / 开头
        if (!pattern.startsWith("/")) {
            pattern = "/" + pattern;
        }
        return pattern;
    }
}
