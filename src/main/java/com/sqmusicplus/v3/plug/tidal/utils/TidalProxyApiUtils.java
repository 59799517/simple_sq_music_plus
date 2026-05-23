package com.sqmusicplus.v3.plug.tidal.utils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.sqmusicplus.v3.plug.tidal.entity.vo.ManifestResult;
import com.sqmusicplus.v3.plug.tidal.entity.vo.TidalDownloadInfo;
import com.sqmusicplus.v3.utils.OkHttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.bytedeco.javacpp.Loader;

import java.io.*;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Classname TidalProxyApiUtils
 * @Description Tidal 第三方代理 API 工具类（参考 Go 版本实现）
 * @Version 1.0.0
 * @Date 2026/4/29
 * @Created by SQ
 */
@Slf4j
public class TidalProxyApiUtils {

    // 固定的 x-tidal-token（与 Go 版本一致）
    private static final String TIDAL_PUBLIC_TOKEN = "txNoH4kkV41MfH25";

    // Zarz API（优先使用）
    private static final String ZARZ_API_URL = "https://api.zarz.moe/v1/dl/tid2";
    private static final int ZARZ_MAX_RETRIES = 3;
    // Tidal 官方 App User-Agent
    private static final String TIDAL_USER_AGENT = "SpotiFLAC-Mobile/1.0";

    // 代理 API 列表（与 Go 版本一致）
    private static final List<String> PROXY_APIS = new ArrayList<>();
    static {
        PROXY_APIS.add("https://eu-central.monochrome.tf");
        PROXY_APIS.add("https://us-west.monochrome.tf");
        PROXY_APIS.add("https://api.monochrome.tf");
        PROXY_APIS.add("https://monochrome-api.samidy.com");
        PROXY_APIS.add("https://tidal-api.binimum.org");
        PROXY_APIS.add("https://tidal.kinoplus.online");
        PROXY_APIS.add("https://triton.squid.wtf");
        PROXY_APIS.add("https://vogel.qqdl.site");
        PROXY_APIS.add("https://maus.qqdl.site");
        PROXY_APIS.add("https://hund.qqdl.site");
        PROXY_APIS.add("https://katze.qqdl.site");
        PROXY_APIS.add("https://wolf.qqdl.site");
        PROXY_APIS.add("https://hifi-one.spotisaver.net");
        PROXY_APIS.add("https://hifi-two.spotisaver.net");
    }

    // 线程池用于并行请求
    private static final ExecutorService executor = new ThreadPoolExecutor(
            Math.min(PROXY_APIS.size(), 10),
            Math.min(PROXY_APIS.size(), 10),
            60L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(),
            new ThreadPoolExecutor.CallerRunsPolicy()
    );

    // 超时时间（毫秒）
    private static final int TIMEOUT_MS = 10000;

    /**
     * 获取下载链接（优先使用 Zarz API，失败后使用代理 API）
     * 
     * @param trackId 歌曲 ID
     * @param quality 音质（HIGH/LOSSLESS/HI_RES/HI_RES_LOSSLESS）
     * @return TidalDownloadInfo 对象，包含 URL、BitDepth、SampleRate 等
     */
    public static TidalDownloadInfo getDownloadUrl(long trackId, String quality) {
        log.info("========== 开始获取下载链接 ==========");
        log.info("TrackID: {}", trackId);
        log.info("Quality: {}", quality);

        long startTime = System.currentTimeMillis();

        // 1. 优先尝试 Zarz API（重试 3 次）
        log.info("【步骤1】尝试 Zarz API (最多重试 {} 次)...", ZARZ_MAX_RETRIES);
        TidalDownloadInfo zarzResult = tryZarzApi(trackId, quality);
        
        if (zarzResult != null) {
            long totalTime = System.currentTimeMillis() - startTime;
            log.info("✓ Zarz API 成功 (总耗时: {}ms)", totalTime);
            log.info("  BitDepth: {}, SampleRate: {} Hz", 
                zarzResult.getBitDepth(), zarzResult.getSampleRate());
            log.info("========== 下载链接获取完成 ==========\n");
            return zarzResult;
        }
        
        log.warn("✗ Zarz API 失败，切换到代理 API...");

        // 2. Zarz API 失败，使用代理 API
        log.info("【步骤2】尝试代理 API ({} 个)...", PROXY_APIS.size());
        return getDownloadUrlFromProxyApis(trackId, quality, startTime);
    }

    /**
     * 尝试 Zarz API（带重试机制）
     */
    private static TidalDownloadInfo tryZarzApi(long trackId, String quality) {
        for (int attempt = 1; attempt <= ZARZ_MAX_RETRIES; attempt++) {
            try {
                log.info("  Zarz API 第 {}/{} 次尝试...", attempt, ZARZ_MAX_RETRIES);
                
                // 构建 POST 请求参数
                JSONObject requestBody = new JSONObject();
                requestBody.put("id", String.valueOf(trackId));
                requestBody.put("quality", quality);
                
                String response = OkHttpUtils.builder()
                        .url(ZARZ_API_URL)
                        .addHeader("Content-Type", "application/json")
                        .addHeader("User-Agent", TIDAL_USER_AGENT)
                        .addHeader("Accept", "application/json")
                        .post(true,requestBody)
                        .sync();
                
                if (response == null || response.isEmpty()) {
                    log.warn("  Zarz API 返回空响应");
                    continue;
                }
                
                log.debug("  Zarz API 响应长度: {} 字符", response.length());
                
                // 解析响应
                TidalDownloadInfo result = parseZarzResponse(response);
                
                if (result != null && result.getUrl() != null && !result.getUrl().isEmpty()) {
                    log.info("  ✓ Zarz API 第 {} 次尝试成功", attempt);
                    return result;
                } else {
                    log.warn("  ✗ Zarz API 返回数据无效");
                }
                
            } catch (Exception e) {
                log.warn("  ✗ Zarz API 第 {} 次尝试失败: {}", attempt, e.getMessage());
                
                // 如果不是最后一次尝试，等待一下再重试
                if (attempt < ZARZ_MAX_RETRIES) {
                    try {
                        Thread.sleep(1000 * attempt); // 递增等待时间
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
        }
        
        log.error("  ✗ Zarz API 所有 {} 次尝试都失败", ZARZ_MAX_RETRIES);
        return null;
    }

    /**
     * 解析 Zarz API 响应
     */
    private static TidalDownloadInfo parseZarzResponse(String response) {
        try {
            JSONObject json = JSON.parseObject(response);
            
            // 检查 version
            String version = json.getString("version");
            if (version == null) {
                log.warn("Zarz 响应缺少 version 字段");
                return null;
            }
            
            // 获取 data 对象
            JSONObject data = json.getJSONObject("data");
            if (data == null) {
                log.warn("Zarz 响应缺少 data 字段");
                return null;
            }
            
            // 提取字段
            String manifest = data.getString("manifest");
            Integer bitDepth = data.getInteger("bitDepth");
            Integer sampleRate = data.getInteger("sampleRate");
            
            if (manifest == null || manifest.isEmpty()) {
                log.warn("Zarz 响应缺少 manifest 字段");
                return null;
            }
            
            // 构建 TidalDownloadInfo
            TidalDownloadInfo.TidalDownloadInfoBuilder builder = TidalDownloadInfo.builder()
                    .url("MANIFEST:" + manifest);
            
            if (bitDepth != null) {
                builder.bitDepth(bitDepth);
            }
            if (sampleRate != null) {
                builder.sampleRate(sampleRate);
            }
            
            return builder.build();
            
        } catch (Exception e) {
            log.error("解析 Zarz 响应失败: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 从代理 API 获取下载链接（并行请求）
     */
    private static TidalDownloadInfo getDownloadUrlFromProxyApis(long trackId, String quality, long startTime) {
        // 创建 Future 列表
        List<Future<ApiResult>> futures = new ArrayList<>();

        // 并行请求所有 API
        for (String apiUrl : PROXY_APIS) {
            Future<ApiResult> future = executor.submit(() -> {
                long reqStart = System.currentTimeMillis();
                try {
                    TidalDownloadInfo result = fetchFromProxy(apiUrl, trackId, quality);
                    long duration = System.currentTimeMillis() - reqStart;
                    return new ApiResult(apiUrl, result, null, duration);
                } catch (Exception e) {
                    long duration = System.currentTimeMillis() - reqStart;
                    return new ApiResult(apiUrl, null, e, duration);
                }
            });
            futures.add(future);
        }

        // 收集结果，返回第一个成功的响应
        List<String> errors = new ArrayList<>();
        TidalDownloadInfo successResult = null;
        String successApi = null;

        for (int i = 0; i < futures.size(); i++) {
            try {
                ApiResult result = futures.get(i).get(TIMEOUT_MS, TimeUnit.MILLISECONDS);
                
                if (result.exception == null && result.data != null && successResult == null) {
                    // 第一个成功的响应
                    successResult = result.data;
                    successApi = result.apiUrl;
                    
                    log.info("[Parallel] ✓ 从 {} 获取成功 (耗时: {}ms)", 
                        result.apiUrl, result.duration);
                    log.info("[Parallel] BitDepth: {}, SampleRate: {} Hz", 
                        result.data.getBitDepth(),
                        result.data.getSampleRate());
                    
                    // 立即取消其他未完成的请求
                    log.info("[Parallel] 取消剩余 {} 个未完成的请求...", futures.size() - i - 1);
                    cancelRemainingFutures(futures, i);
                    
                    // 不再继续等待其他结果，直接跳出循环
                    break;
                } else if (result.exception != null) {
                    // 失败
                    String errorMsg = result.exception.getMessage();
                    if (errorMsg != null && errorMsg.length() > 100) {
                        errorMsg = errorMsg.substring(0, 100) + "...";
                    }
                    errors.add(result.apiUrl + ": " + (errorMsg != null ? errorMsg : "Unknown error"));
                    log.debug("[Parallel] ✗ {} 失败: {}", result.apiUrl, errorMsg);
                }
            } catch (TimeoutException e) {
                errors.add(PROXY_APIS.get(i) + ": Timeout");
                log.debug("[Parallel] ✗ {} 超时", PROXY_APIS.get(i));
            } catch (Exception e) {
                errors.add(PROXY_APIS.get(i) + ": " + e.getMessage());
                log.debug("[Parallel] ✗ {} 异常: {}", PROXY_APIS.get(i), e.getMessage());
            }
        }

        // 如果找到了成功的结果，立即返回
        if (successResult != null) {
            long totalTime = System.currentTimeMillis() - startTime;
            log.info("[Parallel] 总耗时: {}ms (首个成功: {})", totalTime, successApi);
            log.info("========== 下载链接获取完成 ==========\n");
            return successResult;
        }

        // 所有 API 都失败
        long totalTime = System.currentTimeMillis() - startTime;
        log.error("[Parallel] ✗ 所有 {} 个 API 都失败 (总耗时: {}ms)", PROXY_APIS.size(), totalTime);
        log.error("错误列表:");
        for (String error : errors) {
            log.error("  - {}", error);
        }
        log.info("========== 下载链接获取失败 ==========\n");

        return null;
    }

    /**
     * 从单个代理 API 获取下载链接
     */
    private static TidalDownloadInfo fetchFromProxy(String apiUrl, long trackId, String quality) throws Exception {
        // 构建请求 URL
        String requestUrl = String.format("%s/track/?id=%d&quality=%s", apiUrl, trackId, quality);
        
        log.debug("请求 URL: {}", requestUrl);

        // 发送 HTTP 请求
        String response = OkHttpUtils.builder()
                .url(requestUrl)
                .addHeader("x-tidal-token", TIDAL_PUBLIC_TOKEN)
                .get()
                .sync();

        if (response == null || response.isEmpty()) {
            throw new Exception("Empty response");
        }

        log.debug("响应长度: {} 字符", response.length());

        // 尝试解析 V2 响应（Manifest 格式）
        TidalDownloadInfo v2Result = tryParseV2Response(response);
        if (v2Result != null) {
            return v2Result;
        }

        // 尝试解析 V1 响应（直接 URL）
        TidalDownloadInfo v1Result = tryParseV1Response(response);
        if (v1Result != null) {
            return v1Result;
        }

        throw new Exception("No download URL or manifest in response");
    }

    /**
     * 尝试解析 V2 API 响应（Manifest 格式）
     */
    private static TidalDownloadInfo tryParseV2Response(String responseBody) {
        try {
            JSONObject json = JSON.parseObject(responseBody);

            if (!json.containsKey("data")) {
                return null;
            }

            JSONObject data = json.getJSONObject("data");

            // 检查是否是预览版本
            String assetPresentation = data.getString("assetPresentation");
            if ("PREVIEW".equals(assetPresentation)) {
                return null;
            }

            // 获取 Manifest
            String manifest = data.getString("manifest");
            if (manifest == null || manifest.isEmpty()) {
                return null;
            }

            int bitDepth = data.getIntValue("bitDepth", 0);
            int sampleRate = data.getIntValue("sampleRate", 0);

            // 返回 MANIFEST: 前缀的 URL
            return TidalDownloadInfo.builder()
                    .url("MANIFEST:" + manifest)
                    .bitDepth(bitDepth)
                    .sampleRate(sampleRate)
                    .build();

        } catch (Exception e) {
            log.debug("V2 响应解析失败: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 尝试解析 V1 API 响应（直接 URL）
     */
    private static TidalDownloadInfo tryParseV1Response(String responseBody) {
        try {
            if (!responseBody.trim().startsWith("[")) {
                return null;
            }

            JSONArray jsonArray = JSON.parseArray(responseBody);

            if (jsonArray.isEmpty()) {
                return null;
            }

            JSONObject firstItem = jsonArray.getJSONObject(0);

            String url = firstItem.getString("OriginalTrackUrl");

            if (url == null || url.isEmpty()) {
                return null;
            }

            // V1 格式默认是 16-bit/44100Hz
            return TidalDownloadInfo.builder()
                    .url(url)
                    .bitDepth(16)
                    .sampleRate(44100)
                    .build();

        } catch (Exception e) {
            log.debug("V1 响应解析失败: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 取消剩余的 Future
     */
    private static void cancelRemainingFutures(List<Future<ApiResult>> futures, int currentIndex) {
        for (int i = currentIndex + 1; i < futures.size(); i++) {
            futures.get(i).cancel(true);
        }
    }

    /**
     * 解析 Manifest（Base64 编码）
     * 支持 BTS（JSON）和 DASH（XML/MPD）两种格式
     * 
     * @param manifestBase64 Base64 编码的 Manifest
     * @return ManifestResult 对象
     */
    public static ManifestResult parseManifest(String manifestBase64) throws Exception {
        log.info("========== 开始解析 Manifest ==========");
        log.info("Manifest 长度: {} 字符", manifestBase64.length());

        // Base64 解码
        byte[] manifestBytes = Base64.getDecoder().decode(manifestBase64);
        String manifestStr = new String(manifestBytes, "UTF-8");

        log.info("解码后长度: {} 字符", manifestStr.length());

        ManifestResult result;

        // 判断是 JSON (BTS) 还是 XML (DASH)
        if (manifestStr.trim().startsWith("{")) {
            // BTS 格式 - JSON
            log.info("检测到 BTS 格式 (JSON)");
            result = parseBtsManifest(manifestStr);
        } else {
            // DASH 格式 - XML
            log.info("检测到 DASH 格式 (XML/MPD)");
            result = parseDashManifest(manifestStr);
        }

        log.info("Manifest 解析完成");
        if (result.isBtsFormat()) {
            log.info("  格式: BTS (直接 URL)");
            log.info("  URL: {}", truncateUrl(result.getDirectUrl()));
        } else if (result.isDashFormat()) {
            log.info("  格式: DASH (分段下载)");
            log.info("  初始化段: {}", truncateUrl(result.getInitUrl()));
            log.info("  媒体段数量: {}", result.getSegmentCount());
        }
        log.info("========== Manifest 解析完成 ==========\n");

        return result;
    }

    /**
     * 解析 BTS Manifest (JSON 格式)
     */
    private static ManifestResult parseBtsManifest(String manifestJson) {
        try {
            JSONObject json = JSON.parseObject(manifestJson);

            String directUrl = null;
            String codecs = null;

            if (json.containsKey("urls")) {
                JSONArray urls = json.getJSONArray("urls");
                if (!urls.isEmpty()) {
                    directUrl = urls.getString(0);
                }
            }

            if (json.containsKey("codecs")) {
                codecs = json.getString("codecs");
            }

            return ManifestResult.builder()
                    .directUrl(directUrl)
                    .codecs(codecs)
                    .mediaUrls(new String[0])
                    .build();

        } catch (Exception e) {
            log.error("BTS Manifest 解析失败: {}", e.getMessage(), e);
            return ManifestResult.builder().mediaUrls(new String[0]).build();
        }
    }

    /**
     * 解析 DASH Manifest (XML 格式)
     */
    private static ManifestResult parseDashManifest(String manifestXml) {
        try {
            String initUrl = null;
            String[] mediaUrls = new String[0];
            String codecs = null;

            // 提取 initialization URL
            int initStart = manifestXml.indexOf("initialization=\"");
            if (initStart != -1) {
                initStart += "initialization=\"".length();
                int initEnd = manifestXml.indexOf("\"", initStart);
                if (initEnd != -1) {
                    initUrl = manifestXml.substring(initStart, initEnd)
                            .replace("&amp;", "&");
                }
            }

            // 提取 media 模板
            int mediaStart = manifestXml.indexOf("media=\"");
            if (mediaStart != -1) {
                mediaStart += "media=\"".length();
                int mediaEnd = manifestXml.indexOf("\"", mediaStart);
                if (mediaEnd != -1) {
                    String mediaTemplate = manifestXml.substring(mediaStart, mediaEnd)
                            .replace("&amp;", "&");

                    // 计算分段数量
                    int segmentCount = estimateSegmentCount(manifestXml);

                    // 生成分段 URLs
                    mediaUrls = new String[segmentCount];
                    for (int i = 1; i <= segmentCount; i++) {
                        mediaUrls[i - 1] = mediaTemplate.replace("$Number$", String.valueOf(i));
                    }
                }
            }

            // 提取 codecs 信息
            int codecsStart = manifestXml.indexOf("codecs=\"");
            if (codecsStart != -1) {
                codecsStart += "codecs=\"".length();
                int codecsEnd = manifestXml.indexOf("\"", codecsStart);
                if (codecsEnd != -1) {
                    codecs = manifestXml.substring(codecsStart, codecsEnd);
                }
            }

            return ManifestResult.builder()
                    .initUrl(initUrl)
                    .mediaUrls(mediaUrls)
                    .codecs(codecs)
                    .originalMpdXml(manifestXml)  // 保存原始 MPD
                    .build();

        } catch (Exception e) {
            log.error("DASH Manifest 解析失败: {}", e.getMessage(), e);
            return ManifestResult.builder().mediaUrls(new String[0]).build();
        }
    }

    /**
     * 估算 DASH 分段数量
     */
    private static int estimateSegmentCount(String manifestXml) {
        // 简单统计 <S d="..." r="..."/> 标签
        int count = 0;
        int pos = 0;

        while ((pos = manifestXml.indexOf("<S ", pos)) != -1) {
            int endPos = manifestXml.indexOf("/>", pos);
            if (endPos == -1) break;

            String segmentTag = manifestXml.substring(pos, endPos);

            // 提取 r 属性（repeat 次数）
            int rPos = segmentTag.indexOf("r=\"");
            if (rPos != -1) {
                rPos += 3;
                int rEnd = segmentTag.indexOf("\"", rPos);
                if (rEnd != -1) {
                    try {
                        int repeat = Integer.parseInt(segmentTag.substring(rPos, rEnd));
                        count += repeat + 1;
                    } catch (NumberFormatException e) {
                        count++;
                    }
                } else {
                    count++;
                }
            } else {
                count++;
            }

            pos = endPos + 2;
        }

        return Math.max(count, 1);
    }

    /**
     * 下载直接 URL（BTS 格式）
     * 
     * @param url 下载 URL
     * @param outputPath 输出文件路径
     * @return 是否下载成功
     */
    public static boolean downloadDirectUrl(String url, String outputPath) {
        log.info("========== 开始下载直接 URL ==========");
        log.info("URL: {}", truncateUrl(url));
        log.info("输出文件: {}", outputPath);
        
        try {
            File outputFile = new File(outputPath);
            File parentDir = outputFile.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }
            
            // 使用字节数组下载，避免字符串编码问题
            byte[] response = OkHttpUtils.builder()
                    .url(url)
                    .get()
                    .syncBytes();
            
            if (response == null || response.length == 0) {
                log.error("下载失败: 响应为空");
                return false;
            }
            
            // 写入文件（二进制模式）
            try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                fos.write(response);
                fos.flush();
            }
            
            log.info("✓ 直接 URL 下载成功: {}", outputPath);
            log.info("文件大小: {} bytes ({})", outputFile.length(), formatFileSize(outputFile.length()));
            log.info("========== 直接 URL 下载完成 ==========\n");
            
            return true;
            
        } catch (Exception e) {
            log.error("直接 URL 下载失败", e);
            return false;
        }
    }

    /**
     * 从 MPD XML 下载 DASH 分段并合并为完整文件
     * 
     * @param mpdXml MPD XML 字符串
     * @param outputPath 输出文件路径
     * @return 是否下载成功
     */
    public static boolean downloadDashFromMpdXml(String mpdXml, String outputPath) {
        try {
            // 解析 MPD XML
            ManifestResult manifestResult = parseDashManifest(mpdXml);
            
            if (manifestResult == null || !manifestResult.isDashFormat()) {
                log.error("MPD 解析失败或不是 DASH 格式");
                return false;
            }
            
            // 调用现有的下载方法
            return downloadDashSegments(manifestResult, outputPath);
            
        } catch (Exception e) {
            log.error("从 MPD XML 下载 DASH 失败", e);
            return false;
        }
    }

    /**
     * 下载 DASH 分段并合并为完整文件
     * 
     * @param manifestResult Manifest 解析结果
     * @param outputPath 输出文件路径
     * @return 是否下载成功
     */
    public static boolean downloadDashSegments(ManifestResult manifestResult, String outputPath) {
        if (!manifestResult.isDashFormat()) {
            log.error("不是 DASH 格式，无法下载分段");
            return false;
        }

        log.info("========== 开始下载 DASH 分段 ==========");
        log.info("输出文件: {}", outputPath);
        log.info("初始化段: {}", truncateUrl(manifestResult.getInitUrl()));
        log.info("媒体段数量: {}", manifestResult.getSegmentCount());

        try {
            File outputFile = new File(outputPath);
            File parentDir = outputFile.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }

            // 创建临时目录存放分段
            File tempDir = new File(outputFile.getParent(), "temp_segments_" + System.currentTimeMillis());
            if (!tempDir.exists()) {
                tempDir.mkdirs();
            }

            // 下载初始化段（必须先完成）
            File initFile = new File(tempDir, "init.mp4");
            log.info("[1/{}] 下载初始化段...", manifestResult.getSegmentCount() + 1);
            if (!downloadSingleSegment(manifestResult.getInitUrl(), initFile)) {
                log.error("初始化段下载失败");
                deleteDirectory(tempDir);
                return false;
            }

            // 使用虚拟线程并行下载所有媒体段
            int totalSegments = manifestResult.getMediaUrls().length;
            List<File> segmentFiles = Collections.synchronizedList(new ArrayList<>());
            segmentFiles.add(initFile); // 先添加初始化段
            
            log.info("使用虚拟线程并行下载 {} 个媒体段...", totalSegments);
            
            // 创建虚拟线程列表
            List<Thread> virtualThreads = new ArrayList<>();
            AtomicBoolean hasError = new AtomicBoolean(false);
            
            for (int i = 0; i < totalSegments; i++) {
                final int segmentIndex = i;
                final String segmentUrl = manifestResult.getMediaUrls()[i];
                final File segmentFile = new File(tempDir, String.format("segment_%04d.m4a", i + 1));
                
                // 创建虚拟线程
                Thread virtualThread = Thread.ofVirtual().start(() -> {
                    try {
                        log.debug("[虚拟线程] 开始下载分段 {}/{}", segmentIndex + 1, totalSegments);
                        boolean success = downloadSingleSegment(segmentUrl, segmentFile);
                        
                        if (!success) {
                            log.error("[虚拟线程] 分段 {} 下载失败", segmentIndex + 1);
                            hasError.set(true);
                        } else {
                            segmentFiles.add(segmentFile);
                            log.debug("[虚拟线程] 分段 {}/{} 下载完成", segmentIndex + 1, totalSegments);
                        }
                    } catch (Exception e) {
                        log.error("[虚拟线程] 分段 {} 下载异常: {}", segmentIndex + 1, e.getMessage(), e);
                        hasError.set(true);
                    }
                });
                
                virtualThreads.add(virtualThread);
            }
            
            // 等待所有虚拟线程完成
            log.info("等待 {} 个虚拟线程完成...", virtualThreads.size());
            for (Thread thread : virtualThreads) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    log.error("等待线程被中断", e);
                    Thread.currentThread().interrupt();
                    deleteDirectory(tempDir);
                    return false;
                }
            }
            
            // 检查是否有错误
            if (hasError.get()) {
                log.error("部分分段下载失败");
                deleteDirectory(tempDir);
                return false;
            }
            
            // 按顺序排序分段文件（确保合并顺序正确）
            segmentFiles.sort((f1, f2) -> {
                if (f1.equals(initFile)) return -1;
                if (f2.equals(initFile)) return 1;
                return f1.getName().compareTo(f2.getName());
            });
            
            log.info("所有分段下载完成，共 {} 个文件", segmentFiles.size());

            // 合并所有分段（根据输出文件扩展名自动选择格式）
            log.info("合并 {} 个分段...", segmentFiles.size());
            mergeSegments(segmentFiles, outputFile, null);

            // 清理临时文件
            deleteDirectory(tempDir);

            log.info("✓ DASH 分段下载并合并成功: {}", outputPath);
            log.info("文件大小: {}", formatFileSize(outputFile.length()));
            log.info("========== DASH 下载完成 ==========\n");

            return true;

        } catch (Exception e) {
            log.error("DASH 分段下载失败", e);
            return false;
        }
    }

    /**
     * 下载单个分段
     */
    private static boolean downloadSingleSegment(String url, File outputFile) {
        try {
            // 使用字节数组下载，避免字符串编码问题
            byte[] response = OkHttpUtils.builder()
                    .url(url)
                    .get()
                    .syncBytes();

            if (response == null || response.length == 0) {
                log.error("下载分段失败 [{}]: 响应为空", url);
                return false;
            }

            // 确保父目录存在
            if (outputFile.getParentFile() != null && !outputFile.getParentFile().exists()) {
                outputFile.getParentFile().mkdirs();
            }

            // 写入文件（二进制模式）
            try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                fos.write(response);
                fos.flush();
            }

            log.debug("分段下载成功: {} ({} bytes)", outputFile.getName(), response.length);
            return true;

        } catch (Exception e) {
            log.error("下载分段失败 [{}]: {}", url, e.getMessage(), e);
            return false;
        }
    }

    /**
     * 合并所有分段为一个文件
     * 根据输出文件扩展名自动选择合并策略：
     * - .m4a/.mp4: 二进制拼接（保持原始 DASH 格式）
     * - .flac: 先拼接为临时 m4a，再使用 FFmpeg 转码为 FLAC
     * 
     * @param segmentFiles 分段文件列表
     * @param outputFile 输出文件
     * @param targetFormat 目标格式（null 表示根据文件扩展名自动判断）
     */
    private static void mergeSegments(List<File> segmentFiles, File outputFile, String targetFormat) throws IOException {
        String outputExt = getFileExtension(outputFile.getName()).toLowerCase();
        String format = targetFormat != null ? targetFormat.toLowerCase() : outputExt;
        
        log.info("合并 {} 个分段，目标格式: {}", segmentFiles.size(), format);
        
        // 检查分段文件是否存在且有效
        long totalSize = 0;
        for (File segment : segmentFiles) {
            if (!segment.exists()) {
                throw new IOException("分段文件不存在: " + segment.getAbsolutePath());
            }
            long size = segment.length();
            totalSize += size;
            if (size == 0) {
                log.warn("警告: 分段文件大小为 0: {}", segment.getAbsolutePath());
            } else {
                log.debug("分段文件: {} ({} bytes)", segment.getName(), size);
            }
        }
        log.info("分段文件总大小: {} bytes ({})", totalSize, formatFileSize(totalSize));
        
        if ("flac".equals(format)) {
            // FLAC 格式：先拼接为临时 m4a，再转码
            mergeAndConvertToFlac(segmentFiles, outputFile, totalSize);
        } else {
            // 其他格式（m4a/mp4）：直接二进制拼接
            mergeByBinaryConcat(segmentFiles, outputFile, totalSize);
        }
    }
    
    /**
     * 二进制拼接方式（适用于 m4a/mp4）
     */
    private static void mergeByBinaryConcat(List<File> segmentFiles, File outputFile, long expectedSize) throws IOException {
        log.info("使用二进制拼接方式合并...");
        
        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            
            for (File segment : segmentFiles) {
                try (FileInputStream fis = new FileInputStream(segment)) {
                    while ((bytesRead = fis.read(buffer)) != -1) {
                        fos.write(buffer, 0, bytesRead);
                    }
                }
                log.debug("已拼接: {} ({} bytes)", segment.getName(), segment.length());
            }
            
            fos.flush();
        }
        
        log.info("✓ 二进制拼接成功");
        log.info("输出文件大小: {} bytes ({})", outputFile.length(), formatFileSize(outputFile.length()));
        
        // 验证输出文件
        validateOutputFile(outputFile, expectedSize);
    }
    
    /**
     * 合并并转换为 FLAC 格式
     */
    private static void mergeAndConvertToFlac(List<File> segmentFiles, File outputFile, long expectedSize) throws IOException {
        log.info("使用 FFmpeg 转码方式合并为 FLAC...");
        
        // 1. 先拼接为临时 m4a 文件
        File tempM4a = new File(outputFile.getParentFile() != null ? outputFile.getParentFile() : new File("."), 
            "temp_merge_" + System.currentTimeMillis() + ".m4a");
        
        try {
            log.info("步骤 1/2: 拼接为临时 M4A 文件...");
            mergeByBinaryConcat(segmentFiles, tempM4a, expectedSize);
            
            // 2. 使用 FFmpeg 转码为 FLAC
            log.info("步骤 2/2: 转码为 FLAC 格式...");
            convertToFlac(tempM4a, outputFile);
            
            log.info("✓ FLAC 转码成功");
            log.info("输出文件大小: {} bytes ({})", outputFile.length(), formatFileSize(outputFile.length()));
            
            // 验证输出文件
            validateOutputFile(outputFile, 0); // FLAC 文件大小会不同，不检查
            
        } finally {
            // 删除临时文件
            if (tempM4a.exists()) {
                tempM4a.delete();
                log.debug("已删除临时文件: {}", tempM4a.getName());
            }
        }
    }
    
    /**
     * 使用 FFmpeg 将 M4A 转换为 FLAC
     */
    private static void convertToFlac(File inputFile, File outputFile) throws IOException {
        String ffmpegPath = getJavaCvFfmpegPath();
        
        if (ffmpegPath == null) {
            throw new IOException("无法加载 JavaCV 内置 FFmpeg，请检查 javacv-platform 依赖是否正确添加");
        }
        
        log.info("使用 JavaCV 内置 FFmpeg: {}", ffmpegPath);
        
        // 构建 FFmpeg 命令
        List<String> command = new ArrayList<>();
        command.add(ffmpegPath);
        command.add("-i");
        command.add(inputFile.getAbsolutePath());
        command.add("-c:a");
        command.add("flac");
        command.add("-compression_level");
        command.add("5"); // FLAC 压缩级别 0-8，5 是默认值
        command.add("-y");
        command.add(outputFile.getAbsolutePath());
        
        log.info("执行 FFmpeg 转码命令: {}", String.join(" ", command));
        
        try {
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.redirectErrorStream(true);
            Process process = pb.start();
            
            // 读取输出
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
                log.debug("FFmpeg: {}", line);
            }
            reader.close();
            
            int exitCode = process.waitFor();
            
            if (exitCode != 0) {
                String ffmpegOutput = output.toString();
                log.error("========== FFmpeg 转码失败 ==========");
                log.error("退出代码: {}", exitCode);
                log.error("FFmpeg 输出内容:");
                log.error("{}", ffmpegOutput);
                log.error("=====================================");
                throw new IOException("FFmpeg 转码失败，退出代码: " + exitCode + "\n详细输出:\n" + ffmpegOutput);
            }
            
            log.info("✓ FFmpeg 转码成功");
            
        } catch (Exception e) {
            throw new IOException("FFmpeg 转码失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 验证输出文件
     */
    private static void validateOutputFile(File outputFile, long expectedSize) throws IOException {
        if (outputFile.length() == 0) {
            throw new IOException("输出文件为空，合并失败");
        }
        
        if (expectedSize > 0 && outputFile.length() < expectedSize) {
            log.warn("警告: 输出文件大小 ({}) 小于输入文件总大小 ({})", 
                outputFile.length(), expectedSize);
        }
    }
    
    /**
     * 获取文件扩展名
     */
    private static String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex < fileName.length() - 1) {
            return fileName.substring(lastDotIndex + 1);
        }
        return "";
    }
    
    /**
     * 合并所有分段为一个文件（使用 JavaCV 内置 FFmpeg）
     * 对于 DASH/mp4 分段，使用二进制拼接方式
     * 
     * @deprecated 使用 {@link #mergeSegments(List, File, String)} 代替
     */
    @Deprecated
    private static void mergeSegments(List<File> segmentFiles, File outputFile) throws IOException {
        mergeSegments(segmentFiles, outputFile, null);
    }
    
    /**
     * 获取 JavaCV 内置的 FFmpeg 路径
     * JavaCV 会自动从 JAR 包中提取对应平台的 FFmpeg 到缓存目录
     */
    private static String getJavaCvFfmpegPath() {
        try {
            // 使用 Loader 加载 FFmpeg
            // Loader 会自动解压并返回 FFmpeg 可执行文件的完整路径
            String ffmpegPath = Loader.load(org.bytedeco.ffmpeg.ffmpeg.class);
            
            if (ffmpegPath != null && !ffmpegPath.isEmpty()) {
                log.info("JavaCV FFmpeg 加载成功: {}", ffmpegPath);
                return ffmpegPath;
            }
        } catch (Exception e) {
            log.error("加载 JavaCV FFmpeg 失败: {}", e.getMessage(), e);
        }
        
        return null;
    }

    /**
     * 删除目录及其所有内容
     */
    private static void deleteDirectory(File directory) {
        if (directory == null || !directory.exists()) {
            return;
        }

        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        }
        directory.delete();
    }

    /**
     * 获取下载链接（并行请求多个代理 API）并解析为最终 URL
     * 这是 getDownloadUrl + parseManifest 的便捷方法
     * 
     * @param trackId 歌曲 ID
     * @param quality 音质（HIGH/LOSSLESS/HI_RES）
     * @return ManifestResult 对象，包含解析后的 URL 信息
     */
    public static ManifestResult getDownloadUrlAndParse(long trackId, String quality) {
        log.info("========== 开始获取并解析下载链接 ==========");
        log.info("TrackID: {}", trackId);
        log.info("Quality: {}", quality);
        
        // 1. 获取下载信息
        TidalDownloadInfo downloadInfo = getDownloadUrl(trackId, quality);
        
        if (downloadInfo == null) {
            log.error("❌ 获取下载链接失败");
            return null;
        }
        
        log.info("✓ 获取下载链接成功");
        log.info("  BitDepth: {} bit", downloadInfo.getBitDepth());
        log.info("  SampleRate: {} Hz", downloadInfo.getSampleRate());
        
        // 2. 判断是否为 Manifest 格式
        if (downloadInfo.isManifest()) {
            log.info("检测到 MANIFEST 格式，开始解析...");
            try {
                ManifestResult manifestResult = parseManifest(downloadInfo.getManifestBase64());
                log.info("========== 下载链接解析完成 ==========\n");
                return manifestResult;
            } catch (Exception e) {
                log.error("❌ Manifest 解析失败: {}", e.getMessage(), e);
                return null;
            }
        } else {
            // 直接 URL
            log.info("检测到直接 URL 格式");
            ManifestResult result = ManifestResult.builder()
                    .directUrl(downloadInfo.getUrl())
                    .mediaUrls(new String[0])
                    .build();
            log.info("========== 下载链接解析完成 ==========\n");
            return result;
        }
    }

    /**
     * 获取下载链接并解析为 ManifestResult（支持 PlugBrType）
     * 
     * @param trackId 歌曲 ID
     * @param plugBrType 音质类型
     * @return ManifestResult 对象，包含解析后的 URL 信息和 M3U8 内容
     */
    public static ManifestResult getManifestResult(long trackId, com.sqmusicplus.v3.base.enums.PlugBrType plugBrType) {
        // 根据音质类型选择 Tidal API 的 quality 参数
        String quality;
        if (plugBrType == com.sqmusicplus.v3.base.enums.PlugBrType.TIDAL_M4A_320) {
            quality = "HIGH";
        } else if (plugBrType == com.sqmusicplus.v3.base.enums.PlugBrType.TIDAL_FLAC_LOSSLESS) {
            quality = "LOSSLESS";
        } else if (plugBrType == com.sqmusicplus.v3.base.enums.PlugBrType.TIDAL_HI_FLAC_RES_LOSSLESS) {
            quality = "HI_RES_LOSSLESS";
        } else {
            quality = "HI_RES";
        }
        
        log.info("音质映射: {} -> {}", plugBrType, quality);
        
        // 获取并解析 Manifest
        ManifestResult manifestResult = getDownloadUrlAndParse(trackId, quality);
        
        if (manifestResult == null) {
            return null;
        }
        
        // 如果是 DASH 格式，生成 M3U8 内容
        if (manifestResult.isDashFormat()) {
            log.info("检测到 DASH 格式，生成 M3U8 播放列表...");
            String m3u8Content = generateM3u8Content(manifestResult);
            manifestResult.setM3u8Content(m3u8Content);
            log.info("✓ M3U8 生成成功，长度: {} 字符", m3u8Content.length());
        }
        
        return manifestResult;
    }

    /**
     * 生成 M3U8 播放列表内容（公开方法）
     * 
     * @param manifestResult Manifest 解析结果
     * @return M3U8 格式的字符串
     */
    public static String generateM3u8ContentForPublic(ManifestResult manifestResult) {
        return generateM3u8Content(manifestResult);
    }
    
    /**
     * 生成 M3U8 播放列表内容
     * 
     * @param manifestResult Manifest 解析结果
     * @return M3U8 格式的字符串
     */
    private static String generateM3u8Content(ManifestResult manifestResult) {
        StringBuilder m3u8 = new StringBuilder();
        
        // M3U8 头部 - 使用版本 7 支持 fMP4
        m3u8.append("#EXTM3U\n");
        m3u8.append("#EXT-X-VERSION:7\n");  // 版本 7 支持 CMAF/fMP4
        m3u8.append("#EXT-X-TARGETDURATION:10\n");
        m3u8.append("#EXT-X-MEDIA-SEQUENCE:0\n");
        m3u8.append("#EXT-X-PLAYLIST-TYPE:VOD\n");
        
        // 添加初始化段（带 MIME 类型）
        if (manifestResult.getInitUrl() != null) {
            m3u8.append("#EXT-X-MAP:URI=\"").append(manifestResult.getInitUrl()).append("\",BYTERANGE=\"0@0\"\n");
        }
        
        // 添加媒体分段
        for (String segmentUrl : manifestResult.getMediaUrls()) {
            m3u8.append("#EXTINF:10.000,\n");
            m3u8.append(segmentUrl).append("\n");
        }
        
        // 结束标记
        m3u8.append("#EXT-X-ENDLIST\n");
        
        return m3u8.toString();
    }

    /**
     * 获取下载链接并解析为 ManifestResult
     * 这是最便捷的方法，一步完成获取和解析
     * 
     * @param trackId 歌曲 ID
     * @param quality 音质（HIGH/LOSSLESS/HI_RES）
     * @return ManifestResult 对象，包含解析后的 URL 信息
     */
    public static ManifestResult getDownloadUrlResult(long trackId, String quality) {
        log.info("========== 开始获取下载链接结果 ==========");
        log.info("TrackID: {}", trackId);
        log.info("Quality: {}", quality);
        
        // 获取并解析 Manifest
        ManifestResult manifestResult = getDownloadUrlAndParse(trackId, quality);
        
        if (manifestResult == null) {
            log.error("❌ 获取下载链接失败");
        } else {
            log.info("========== 下载链接结果获取完成 ==========\n");
        }
        
        return manifestResult;
    }

    /**
     * 截断 URL 用于显示
     */
    private static String truncateUrl(String url) {
        if (url == null) {
            return "null";
        }
        if (url.length() <= 80) {
            return url;
        }
        return url.substring(0, 40) + "..." + url.substring(url.length() - 40);
    }

    /**
     * 格式化文件大小
     */
    private static String formatFileSize(long bytes) {
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1024 * 1024) return String.format("%.2f KB", bytes / 1024.0);
        if (bytes < 1024 * 1024 * 1024) return String.format("%.2f MB", bytes / (1024.0 * 1024));
        return String.format("%.2f GB", bytes / (1024.0 * 1024 * 1024));
    }

    /**
     * API 请求结果
     */
    private static class ApiResult {
        String apiUrl;
        TidalDownloadInfo data;
        Exception exception;
        long duration;

        ApiResult(String apiUrl, TidalDownloadInfo data, Exception exception, long duration) {
            this.apiUrl = apiUrl;
            this.data = data;
            this.exception = exception;
            this.duration = duration;
        }
    }

    /**
     * 获取可用的代理 API 列表
     */
    public static List<String> getAvailableApis() {
        return new ArrayList<>(PROXY_APIS);
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        System.out.println("Tidal 代理 API 工具类");
        System.out.println("====================");
        System.out.println("可用 API 数量: " + PROXY_APIS.size());
        System.out.println("\nAPI 列表:");
        for (int i = 0; i < PROXY_APIS.size(); i++) {
            System.out.println((i + 1) + ". " + PROXY_APIS.get(i));
        }
        
        // 示例：获取下载链接
        System.out.println("\n========== 示例用法 ==========");
        System.out.println("// 1. 获取下载链接");
        System.out.println("TidalDownloadInfo downloadInfo = TidalProxyApiUtils.getDownloadUrl(trackId, \"HI_RES\");");
        System.out.println();
        System.out.println("// 2. 判断是否为 Manifest 格式");
        System.out.println("if (downloadInfo.isManifest()) {");
        System.out.println("    // 3. 解析 Manifest");
        System.out.println("    ManifestResult manifestResult = TidalProxyApiUtils.parseManifest(downloadInfo.getManifestBase64());");
        System.out.println();
        System.out.println("    if (manifestResult.isBtsFormat()) {");
        System.out.println("        // BTS 格式：直接下载");
        System.out.println("        String url = manifestResult.getDirectUrl();");
        System.out.println("    } else if (manifestResult.isDashFormat()) {");
        System.out.println("        // DASH 格式：分段下载并合并");
        System.out.println("        TidalProxyApiUtils.downloadDashSegments(manifestResult, \"output.flac\");");
        System.out.println("    }");
        System.out.println("} else {");
        System.out.println("    // 直接 URL：直接下载");
        System.out.println("    String url = downloadInfo.getUrl();");
        System.out.println("}");
    }
}
