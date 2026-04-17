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
 * 总线程池配置与下载线程池
 *
 * @author SQ
 **/
@Configuration
public class ThreadPoolConfig
{
    // 核心线程池大小
    private int corePoolSize = 20;

    // 最大可创建的线程数
//    private int maxPoolSize = 80;
//
//    // 队列最大长度
//    private int queueCapacity = 500;
//
//    // 线程池维护线程所允许的空闲时间
//    private int keepAliveSeconds = 300;

    /**
     * 通用线程池 - 使用虚拟线程（Java 21+）
     * 适合 I/O 密集型任务
     */
    @Bean(name = "threadPoolTaskExecutor")
    public ExecutorService threadPoolTaskExecutor()
    {
        return Executors.newVirtualThreadPerTaskExecutor();
    }

    /**
     * 下载线程池 - 使用虚拟线程（Java 21+）
     * 适合网络下载等 I/O 密集型任务
     */
    @Bean(name = "downloadThreadPool")
    public ExecutorService downloadThreadPool()
    {
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
