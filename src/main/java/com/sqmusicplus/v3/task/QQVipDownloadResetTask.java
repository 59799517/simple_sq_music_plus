package com.sqmusicplus.v3.task;

import com.sqmusicplus.v3.base.enums.SetConfigEnum;
import com.sqmusicplus.v3.config.SqConfigCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Classname QQVipDownloadResetTask
 * @Description QQVIP每日下载计数重置任务
 * @Version 1.0.0
 * @Date 2026/4/10
 * @Created by SQ
 */
@Slf4j
@Component
public class QQVipDownloadResetTask {

    /**
     * 每天凌晨0点重置QQVIP下载计数
     */
    @Scheduled(cron = "0 0 10 * * ?")
    public void resetDailyCount() {
        try {
            log.info("开始执行QQVIP每日下载计数重置任务");
            
            // 重置今日下载计数为0
            SqConfigCache.updateConfigToDb(SetConfigEnum.PLUG_QQVIP_DOWNLOAD_TODAY, "0");
            
            log.info("QQVIP每日下载计数已重置为0");
        } catch (Exception e) {
            log.error("重置QQVIP每日下载计数失败", e);
        }
    }
}
