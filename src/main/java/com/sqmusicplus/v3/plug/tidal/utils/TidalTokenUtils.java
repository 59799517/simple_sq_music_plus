package com.sqmusicplus.v3.plug.tidal.utils;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.sqmusicplus.v3.utils.OkHttpUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @Classname TidalTokenUtils
 * @Description Tidal Token管理工具类
 * @Version 1.0.0
 * @Date 2026/4/29
 * @Created by SQ
 */
@Slf4j
public class TidalTokenUtils {

    // 固定凭证，和SpotiFLAC源码完全一致
    private static final String CLIENT_ID = "6BDSRdpK9hqEBTgU";
    private static final String CLIENT_SECRET = "xeuPmY7nbpZ9IIbLAcQ93shka1VNheUAqN6IcszjTG8=";
    private static final String TOKEN_URL = "https://auth.tidal.com/v1/oauth2/token";

    // Token存储
    private static volatile String accessToken = null;
    private static volatile long expireTime = 0;
    
    // 读写锁，保证线程安全
    private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    /**
     * 获取Access Token（带自动刷新）
     * @return Access Token
     */
    public static String getAccessToken() {
        // 先读锁检查token是否有效
        lock.readLock().lock();
        try {
            if (accessToken != null && System.currentTimeMillis() < expireTime - 60000) {
                // Token还有效（提前1分钟刷新）
                return accessToken;
            }
        } finally {
            lock.readLock().unlock();
        }

        // 需要刷新token，使用写锁
        lock.writeLock().lock();
        try {
            // 双重检查
            if (accessToken != null && System.currentTimeMillis() < expireTime - 60000) {
                return accessToken;
            }
            
            // 刷新token
            refreshAccessToken();
            return accessToken;
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * 刷新Access Token
     */
    public static void refreshAccessToken() {
        log.info("开始刷新Tidal Access Token...");
        
        try {
            // 使用OkHttpUtils.builder()确保OkHttpClient已初始化
            // 构建表单参数
            String requestBody = "grant_type=client_credentials" +
                    "&client_id=" + CLIENT_ID +
                    "&client_secret=" + CLIENT_SECRET;
            
            // 使用builder().sync()获取响应字符串
            String responseBody = OkHttpUtils.builder()
                    .url(TOKEN_URL)
                    .post(false, requestBody)  // false表示表单提交
                    .sync();
            
            // 检查响应是否成功
            if (responseBody == null || responseBody.startsWith("请求失败：")) {
                throw new RuntimeException("获取Token失败：" + responseBody);
            }
            
            JSONObject jsonNode = JSON.parseObject(responseBody);
            
            String newToken = jsonNode.getString("access_token");
            Integer expiresIn = jsonNode.getInteger("expires_in");
            
            if (StrUtil.isBlank(newToken)) {
                throw new RuntimeException("获取Token失败：返回数据中缺少access_token");
            }
            
            // 更新token
            accessToken = newToken;
            // 设置过期时间（提前60秒刷新）
            expireTime = System.currentTimeMillis() + (expiresIn - 60) * 1000L;
            
            log.info("Tidal Access Token刷新成功，有效期: {} 秒", expiresIn);
            log.debug("新Token: {}", maskToken(newToken));
        } catch (Exception e) {
            log.error("刷新Tidal Access Token失败", e);
            throw new RuntimeException("刷新Tidal Access Token失败: " + e.getMessage(), e);
        }
    }

    /**
     * 强制刷新Token（忽略缓存）
     */
    public static String forceRefreshToken() {
        lock.writeLock().lock();
        try {
            refreshAccessToken();
            return accessToken;
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * 检查Token是否有效
     * @return true-有效，false-无效
     */
    public static boolean isTokenValid() {
        lock.readLock().lock();
        try {
            return accessToken != null && System.currentTimeMillis() < expireTime - 60000;
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * 获取Token剩余有效时间（秒）
     * @return 剩余秒数，如果token无效返回0
     */
    public static long getTokenRemainingSeconds() {
        lock.readLock().lock();
        try {
            if (accessToken == null) {
                return 0;
            }
            long remaining = (expireTime - System.currentTimeMillis()) / 1000;
            return Math.max(0, remaining);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * 清除Token缓存
     */
    public static void clearToken() {
        lock.writeLock().lock();
        try {
            accessToken = null;
            expireTime = 0;
            log.info("Tidal Token缓存已清除");
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * 掩码显示Token（用于日志）
     */
    private static String maskToken(String token) {
        if (token == null || token.length() < 20) {
            return "***";
        }
        return token.substring(0, 10) + "..." + token.substring(token.length() - 10);
    }

    /**
     * 获取Client ID（用于调试）
     */
    public static String getClientId() {
        return CLIENT_ID;
    }
}
