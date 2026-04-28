package com.sqmusicplus.v3.config;

import org.springframework.boot.task.ThreadPoolTaskSchedulerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * @Classname SchedulingConfig
 * @Description 定时任务配置 - 使用虚拟线程
 * @Version 1.0.0
 * @Date 2026/4/27
 * @Created by SQ
 */
@Configuration
@EnableScheduling
public class SchedulingConfig {


    @Bean
    public ThreadPoolTaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        // 关键：使用虚拟线程工厂
        scheduler.setThreadFactory(Thread.ofVirtual()
                .name("scheduled-virtual-", 1)
                .factory());

        // 虚拟线程下大小无所谓
        scheduler.setPoolSize(1);
        scheduler.initialize();
        return scheduler;
    }
}
