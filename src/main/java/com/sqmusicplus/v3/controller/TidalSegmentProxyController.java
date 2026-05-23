package com.sqmusicplus.v3.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMethod;

import jakarta.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Tidal CDN 分段代理（轻量级）
 * 仅转发单个分段请求，解决浏览器 CORS 问题
 * 无缓存、流式转发、极低负载
 */
@RestController
@RequestMapping("/api/proxy/tidal")
@Slf4j
public class TidalSegmentProxyController {

    /**
     * 处理 OPTIONS 预检请求
     */
    @RequestMapping(method = RequestMethod.OPTIONS)
    public void handleOptions(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, HEAD, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Range, Accept, Origin");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    /**
     * 代理 Tidal 音频分段请求
     * 支持两种模式：
     * 1. 直接代理完整 URL: /tidal/segment/direct?url=xxx
     * 2. 模板代理（推荐）: /tidal/segment/template?baseurl=xxx&number=1
     */
    @GetMapping("/direct")
    public void proxyDirectSegment(
            @RequestParam String url,
            HttpServletResponse response) {
        proxySegmentInternal(url, response);
    }
    
    @GetMapping
    public void proxySegment(
            @RequestParam(required = false) String url,
            @RequestParam(required = false) String baseurl,
            @RequestParam(required = false) Integer number,
            HttpServletResponse response) {
        
        // 如果提供了 baseurl 和 number，构造实际 URL
        if (baseurl != null && number != null) {
            url = baseurl.replace("$Number$", String.valueOf(number));
        }
        
        if (url == null || url.isEmpty()) {
            try {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Missing url parameter");
            } catch (Exception e) {
                log.error("写入错误响应失败", e);
            }
            return;
        }
        
        proxySegmentInternal(url, response);
    }
    
    private void proxySegmentInternal(String url, HttpServletResponse response) {
        
        HttpURLConnection conn = null;
        try {
            String decodedUrl = java.net.URLDecoder.decode(url, java.nio.charset.StandardCharsets.UTF_8);
            
            log.info("代理请求: {}", decodedUrl.length() > 150 ? decodedUrl.substring(0, 150) + "..." : decodedUrl);
            
            conn = (HttpURLConnection) new URL(decodedUrl).openConnection();
            conn.setRequestMethod("GET");
            conn.setInstanceFollowRedirects(true);
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(60000);
            
            // 设置请求头（模拟正常请求）
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36");
            conn.setRequestProperty("Accept", "*/*");
            conn.setRequestProperty("Accept-Encoding", "identity");
            
            int responseCode = conn.getResponseCode();
            log.info("CDN 响应码: {}", responseCode);
            
            if (responseCode != HttpURLConnection.HTTP_OK && 
                responseCode != HttpURLConnection.HTTP_PARTIAL) {
                log.error("CDN 返回错误码: {} for URL: {}", responseCode, decodedUrl.substring(0, Math.min(100, decodedUrl.length())));
                response.setStatus(HttpServletResponse.SC_BAD_GATEWAY);
                response.setContentType("text/plain");
                response.getWriter().write("CDN Error: " + responseCode);
                return;
            }
            
            // 转发响应
            response.setStatus(responseCode);
            String contentType = conn.getContentType();
            response.setContentType(contentType != null ? contentType : "audio/mp4");
            
            // 转发关键头
            String contentLength = conn.getHeaderField("Content-Length");
            String contentRange = conn.getHeaderField("Content-Range");
            String acceptRanges = conn.getHeaderField("Accept-Ranges");
            String cacheControl = conn.getHeaderField("Cache-Control");
            String etag = conn.getHeaderField("ETag");
            String lastModified = conn.getHeaderField("Last-Modified");
            
            if (contentLength != null) response.setHeader("Content-Length", contentLength);
            if (contentRange != null) response.setHeader("Content-Range", contentRange);
            if (acceptRanges != null) response.setHeader("Accept-Ranges", acceptRanges);
            if (cacheControl != null) response.setHeader("Cache-Control", cacheControl);
            if (etag != null) response.setHeader("ETag", etag);
            if (lastModified != null) response.setHeader("Last-Modified", lastModified);
            
            // CORS 头
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Methods", "GET, HEAD, OPTIONS");
            response.setHeader("Access-Control-Allow-Headers", "Range, Accept, Origin");
            response.setHeader("Access-Control-Expose-Headers", "Content-Length, Content-Range, Accept-Ranges, ETag, Last-Modified");
            
            log.debug("开始流式转发, Content-Length: {}", contentLength);
            
            // 流式转发（64KB 缓冲）
            try (InputStream is = conn.getInputStream();
                 OutputStream os = response.getOutputStream()) {
                
                byte[] buffer = new byte[65536];
                int len;
                long totalBytes = 0;
                while ((len = is.read(buffer)) != -1) {
                    os.write(buffer, 0, len);
                    totalBytes += len;
                }
                os.flush();
                
                log.debug("转发完成, 总字节数: {}", totalBytes);
            }
            
        } catch (Exception e) {
            log.error("分段代理失败, URL: {}", url != null ? url.substring(0, Math.min(100, url.length())) : "null", e);
            try {
                response.setStatus(HttpServletResponse.SC_BAD_GATEWAY);
                response.setContentType("text/plain");
                response.getWriter().write("Proxy Error: " + e.getMessage());
            } catch (Exception ex) {
                log.error("写入错误响应失败", ex);
            }
        } finally {
            if (conn != null) conn.disconnect();
        }
    }
}
