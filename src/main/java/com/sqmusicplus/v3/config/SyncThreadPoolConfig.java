package com.sqmusicplus.v3.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Classname SyncThreadPoolConfig
 * @Description 多线程池创建配置
 * @Version 1.0.0
 * @Date 2025/2/7 10:00
 * @Created by SQ
 */
@Configuration
public class SyncThreadPoolConfig  {
//    private static final Logger log = LoggerFactory.getLogger(SyncThreadPoolConfig.class);

    /**
     * 酷我二维码线程池 - 使用虚拟线程（Java 21+）
     */
    @Bean(name = "kwQrthreadPoolTaskExecutor")
    public ExecutorService getAsyncExecutor() {
        return Executors.newVirtualThreadPerTaskExecutor();
    }

    /**
     * QQ二维码线程池 - 使用虚拟线程（Java 21+）
     */
    @Bean(name = "qqQrthreadPoolTaskExecutor")
    public ExecutorService getQqAsyncExecutor() {
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
