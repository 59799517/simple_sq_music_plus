package com.sqmusicplus.v3.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class JwtUtil {

    @Value("${jwt.secret:sqmusic-secret-key-2025-very-long-string-for-security}")
    private String secret;

    @Value("${jwt.expiration:86400000}")
    private Long expiration;

    // Token 黑名单（内存存储）
    private final Map<String, Long> tokenBlacklist = new ConcurrentHashMap<>();

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 生成 Token
     */
    public String generateToken(Integer userId, String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);
        
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSecretKey())
                .compact();
    }

    /**
     * 从 Token 中获取 Claims
     */
    public Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            log.error("JWT Token 解析失败: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 从 Token 中获取用户 ID
     */
    public Integer getUserIdFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        if (claims != null) {
            return claims.get("userId", Integer.class);
        }
        return null;
    }

    /**
     * 从 Token 中获取用户名
     */
    public String getUsernameFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        if (claims != null) {
            return claims.getSubject();
        }
        return null;
    }

    /**
     * 验证 Token 是否有效
     */
    public boolean validateToken(String token) {
        try {
            // 检查是否在黑名单中
            if (tokenBlacklist.containsKey(token)) {
                log.debug("Token 在黑名单中");
                return false;
            }

            Claims claims = getClaimsFromToken(token);
            if (claims == null) {
                return false;
            }
            
            // 检查是否过期
            Date expiration = claims.getExpiration();
            return !expiration.before(new Date());
        } catch (Exception e) {
            log.error("JWT Token 验证失败: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 将 Token 加入黑名单（登出/踢人时使用）
     */
    public void addToBlacklist(String token) {
        if (token != null && !token.isEmpty()) {
            Claims claims = getClaimsFromToken(token);
            if (claims != null) {
                long expirationTime = claims.getExpiration().getTime();
                tokenBlacklist.put(token, expirationTime);
                log.info("Token 已加入黑名单");
            }
        }
    }

    /**
     * 清理过期的黑名单 Token（定期调用）
     */
    public void cleanupBlacklist() {
        long now = System.currentTimeMillis();
        tokenBlacklist.entrySet().removeIf(entry -> entry.getValue() < now);
        log.debug("清理黑名单，当前黑名单大小: {}", tokenBlacklist.size());
    }

    /**
     * 检查 Token 是否在黑名单中
     */
    public boolean isBlacklisted(String token) {
        return token != null && tokenBlacklist.containsKey(token);
    }
}
