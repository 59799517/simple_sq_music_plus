package com.sqmusicplus.v3.task;

import com.sqmusicplus.v3.plug.tidal.utils.TidalTokenUtils;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Classname TidalTokenRefreshTask
 * @Description Tidal Token自动刷新定时任务
 * @Version 1.0.0
 * @Date 2026/4/29
 * @Created by SQ
 */
@Slf4j
@Component
public class TidalTokenRefreshTask {

    @PostConstruct
    public void init() {
        log.info("TidalTokenRefreshTask 已注册, fixedRate=1800000ms (每30分钟)");
    }

    /**
     * 每30分钟检查并刷新Tidal Token
     * Tidal Token默认有效期为1小时，提前刷新确保不会过期
     */
    @Scheduled(fixedRate = 1800000) // 30分钟 = 1800000毫秒
    public void refreshTidalToken() {
        try {
            log.info("========== Tidal Token定时刷新任务开始 ==========");
            
            // 检查token是否需要刷新
            if (!TidalTokenUtils.isTokenValid()) {
                log.info("Tidal Token已过期或即将过期，开始刷新...");
                String newToken = TidalTokenUtils.forceRefreshToken();
                log.info("Tidal Token刷新成功");
            } else {
                long remainingSeconds = TidalTokenUtils.getTokenRemainingSeconds();
                log.debug("Tidal Token仍然有效，剩余时间: {} 秒 ({} 分钟)", 
                    remainingSeconds, remainingSeconds / 60);
            }
            
            log.info("========== Tidal Token定时刷新任务完成 ==========");
        } catch (Throwable t) {
            log.error("Tidal Token定时刷新任务执行失败", t);
        }
    }

    /**
     * 应用启动时立即刷新一次Token
     * 使用@Scheduled的initialDelay=0确保启动后立即执行
     */
    @Scheduled(initialDelay = 1000, fixedRate = Long.MAX_VALUE)
    public void initTidalToken() {
        try {
            log.info("========== Tidal Token初始化开始 ==========");
            TidalTokenUtils.forceRefreshToken();
            log.info("========== Tidal Token初始化完成 ==========");
        } catch (Throwable t) {
            log.error("Tidal Token初始化失败", t);
        }
    }
}
