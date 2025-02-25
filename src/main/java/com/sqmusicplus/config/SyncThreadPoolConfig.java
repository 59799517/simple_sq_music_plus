package com.sqmusicplus.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.reflect.Method;
import java.util.concurrent.*;

/**
 * @Classname SyncThreadPoolConfig
 * @Description TODO
 * @Version 1.0.0
 * @Date 2025/2/7 10:00
 * @Created by Administrator
 */
@Configuration
public class SyncThreadPoolConfig  {
//    private static final Logger log = LoggerFactory.getLogger(SyncThreadPoolConfig.class);

    @Bean(name = "kwQrthreadPoolTaskExecutor")
    public ThreadPoolExecutor getAsyncExecutor() {


        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                10, // 核心线程数
                15, // 最大线程数
                60*5, // 线程最大空闲时间
                TimeUnit.SECONDS, // 时间单位
                new LinkedBlockingQueue<Runnable>(20), // 任务队列
                Executors.defaultThreadFactory(), // 线程工厂
                new ThreadPoolExecutor.CallerRunsPolicy() // 拒绝策略
        );

//        ThreadPoolExecutor taskExecutor = new ThreadPoolExecutor();
//        taskExecutor.setCorePoolSize(10);
//        taskExecutor.setMaxPoolSize(20);
//        taskExecutor.setQueueCapacity(50);
//        taskExecutor.setThreadNamePrefix("kgsync-");
//        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
////        等待时长强制销毁 5分钟
//        taskExecutor.setAwaitTerminationSeconds(60 * 5);
//        taskExecutor.initialize();
        return executor;
    }
//
//    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
//
//        return new MyAsyncExceptionHandler();
//    }
//
//    class MyAsyncExceptionHandler implements AsyncUncaughtExceptionHandler{
//        @Override
//        public void handleUncaughtException(Throwable throwable, Method method, Object... params) {
//            log.error("--------------------------------");
//            log.error("异步任务异常信息：Exception message - " + throwable.getMessage());
//            log.error("异步任务异常信息： Method name - " + method.getName());
//            for (Object param : params) {
//                log.error("异步任务异常信息：Parameter value - " + param);
//            }
//            log.error("--------------------------------");
//        }
//    }


}
