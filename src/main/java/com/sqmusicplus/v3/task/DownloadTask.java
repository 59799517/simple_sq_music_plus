package com.sqmusicplus.v3.task;

import com.sqmusicplus.v3.download.DownloadExcute;
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
    


    @Scheduled(cron="*/10 * * * * ? ")
    public void excute(){
        try {
            log.debug("=============开始检测下载===============");
            downloadExcute.getDownloadInfo();
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("=============下载任务执行失败===============");
        }
    }

}