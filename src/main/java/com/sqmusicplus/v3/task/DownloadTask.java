package com.sqmusicplus.v3.task;

import com.sqmusicplus.v3.download.DownloadExcute;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @Classname DownloadTask
 * @Description 下载定时任务
 * @Version 1.0.0
 * @Date 2023/8/24 13:42
 * @Created by sq
 */
@Slf4j
@Component
public class DownloadTask {

    @Autowired
    private DownloadExcute downloadExcute;
    
    @PostConstruct
    public void init() {
        log.info("DownloadTask 下载定时任务已注册, cron=*/10 * * * * ? (每10秒执行)");
    }

    @Scheduled(cron="*/10 * * * * ? ")
    public void excute(){
        try {
            log.info("=============开始检测下载===============");
            downloadExcute.getDownloadInfo();
        } catch (Throwable t) {
            log.error("=============下载任务执行失败(严重异常)===============", t);
        }
    }

}