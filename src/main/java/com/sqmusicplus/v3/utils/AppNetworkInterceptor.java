package com.sqmusicplus.v3.utils;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * OkHttp 网络流量拦截器
 * 自动统计通过 OkHttp 发起的所有请求的流量，实现精确的应用程序流量监控
 */
public class AppNetworkInterceptor implements Interceptor {
    
//    private static final Logger log = LoggerFactory.getLogger(AppNetworkInterceptor.class);
    
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        
        // 记录请求体大小（上传流量）
        long requestBodySize = 0;
        if (request.body() != null) {
            requestBodySize = request.body().contentLength();
            if (requestBodySize > 0) {
                SystemUtils.recordAppUpload(requestBodySize);
//                log.debug("记录HTTP请求上传: {} bytes, URL: {}", requestBodySize, request.url());
            }
        }
        
//        long startTime = System.currentTimeMillis();
        
        // 执行请求
        Response response = chain.proceed(request);
        
        // 记录响应体大小（下载流量）
        long responseBodySize = response.body().contentLength();
        if (responseBodySize > 0) {
            SystemUtils.recordAppDownload(responseBodySize);
//            log.debug("记录HTTP响应下载: {} bytes, URL: {}", responseBodySize, request.url());
        }
        
//        long duration = System.currentTimeMillis() - startTime;
        
//        log.debug("HTTP {} {} - 请求: {} bytes, 响应: {} bytes, 耗时: {}ms",
//                 request.method(), request.url(), requestBodySize, responseBodySize, duration);
        
        return response;
    }
}