package com.sqmusicplus.v3.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * @Classname SchedulingConfig
 * @Description 定时任务配置
 * @Version 1.0.0
 * @Date 2026/4/27
 * @Created by SQ
 */
@Configuration
public class SchedulingConfig {

    @Bean
    public ThreadPoolTaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        // ScheduledThreadPoolExecutor 内部基于平台线程池，不能用虚拟线程
        scheduler.setPoolSize(30);
        scheduler.setThreadNamePrefix("scheduled-");
        scheduler.setAwaitTerminationSeconds(60);
        scheduler.setWaitForTasksToCompleteOnShutdown(true);
        scheduler.initialize();
        return scheduler;
    }
}
