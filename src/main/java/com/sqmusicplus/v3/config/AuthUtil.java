package com.sqmusicplus.v3.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 认证工具类
 * 提供登录、登出、踢人、检查登录状态等功能
 */
@Slf4j
@Component
public class AuthUtil {

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 生成 Token（登录）
     *
     * @param userId   用户ID
     * @param username 用户名
     * @return JWT Token
     */
    public String login(Integer userId, String username) {
        String token = jwtUtil.generateToken(userId, username);
        log.info("用户 {} (ID: {}) 登录成功", username, userId);
        return token;
    }

    /**
     * 登出（将当前 Token 加入黑名单）
     */
    public void logout() {
        String token = getCurrentToken();
        if (token != null) {
            jwtUtil.addToBlacklist(token);
            log.info("用户登出，Token 已失效");
        }
    }

    /**
     * 踢人下线（将指定用户的 Token 加入黑名单）
     * 注意：由于 JWT 无状态特性，这里只能将当前请求的 Token 加入黑名单
     * 如果要踢掉某个用户的所有设备，需要配合数据库或 Redis 记录用户的所有 Token
     *
     * @param userId 用户ID
     */
    public void kickout(Integer userId) {
        String token = getCurrentToken();
        if (token != null) {
            Integer tokenUserId = jwtUtil.getUserIdFromToken(token);
            if (tokenUserId != null && tokenUserId.equals(userId)) {
                jwtUtil.addToBlacklist(token);
                log.info("用户 {} 被踢下线", userId);
            }
        }
    }

    /**
     * 检查当前用户是否已登录
     *
     * @return true-已登录，false-未登录
     */
    public boolean isLogin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated();
    }

    /**
     * 获取当前登录用户 ID
     *
     * @return 用户ID，未登录返回 null
     */
    public Integer getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Integer) {
            return (Integer) authentication.getPrincipal();
        }
        return null;
    }

    /**
     * 获取当前登录用户名
     *
     * @return 用户名，未登录返回 null
     */
    public String getCurrentUsername() {
        String token = getCurrentToken();
        if (token != null) {
            return jwtUtil.getUsernameFromToken(token);
        }
        return null;
    }

    /**
     * 获取当前请求的 Token
     *
     * @return Token，不存在返回 null
     */
    public String getCurrentToken() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                
                // 优先从 Authorization header 读取 (标准格式: Bearer xxx)
                String bearerToken = request.getHeader("Authorization");
                if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
                    return bearerToken.substring(7);
                }
                
                // 兼容自定义 sqmusic header (不区分大小写)
                String sqmusicToken = request.getHeader("sqmusic");
                if (sqmusicToken != null && !sqmusicToken.isEmpty()) {
                    return sqmusicToken;
                }
            }
        } catch (Exception e) {
            log.error("获取当前 Token 失败", e);
        }
        return null;
    }

    /**
     * 验证 Token 是否有效
     *
     * @param token JWT Token
     * @return true-有效，false-无效
     */
    public boolean validateToken(String token) {
        return jwtUtil.validateToken(token);
    }
}
