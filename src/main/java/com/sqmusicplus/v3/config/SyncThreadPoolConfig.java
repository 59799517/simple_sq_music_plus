package com.sqmusicplus.v3.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Classname SyncThreadPoolConfig
 * @Description 虚拟线程池配置 - JDK 21
 * @Version 2.0.0
 * @Date 2025/2/7 10:00
 * @Created by SQ
 */
@Configuration
public class SyncThreadPoolConfig {
//    private static final Logger log = LoggerFactory.getLogger(SyncThreadPoolConfig.class);

    @Bean(name = "kwQrthreadPoolTaskExecutor")
    public ExecutorService getAsyncExecutor() {
        // 使用虚拟线程执行器
        return Executors.newVirtualThreadPerTaskExecutor();
    }
    
    @Bean(name = "qqQrthreadPoolTaskExecutor")
    public ExecutorService getQqAsyncExecutor() {
        // 使用虚拟线程执行器
        return Executors.newVirtualThreadPerTaskExecutor();
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
