package com.sqmusicplus.v3.config;

import com.sqmusicplus.v3.utils.Threads;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 总线程池配置与下载线程池 - JDK 21 虚拟线程版本
 *
 * @author SQ
 **/
@Configuration
public class ThreadPoolConfig
{
    // 核心线程池大小（用于定时任务）
    private int corePoolSize = 20;

    @Bean(name = "threadPoolTaskExecutor")
    public ExecutorService threadPoolTaskExecutor()
    {
        // 使用虚拟线程执行器
        return Executors.newVirtualThreadPerTaskExecutor();
    }
    
    @Bean(name = "downloadThreadPool")
    public ExecutorService downloadThreadPool()
    {
        // 使用虚拟线程执行器
        return Executors.newVirtualThreadPerTaskExecutor();
    }
    /**
     * 执行周期性或定时任务
     */
    @Bean(name = "scheduledExecutorService")
    protected ScheduledExecutorService scheduledExecutorService()
    {
        return new ScheduledThreadPoolExecutor(corePoolSize,
                new BasicThreadFactory.Builder().namingPattern("schedule-pool-%d").daemon(true).build(),
                new ThreadPoolExecutor.CallerRunsPolicy())
        {
            @Override
            protected void afterExecute(Runnable r, Throwable t)
            {
                super.afterExecute(r, t);
                Threads.printException(r, t);
            }
        };
    }
}
