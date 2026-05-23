package com.sqmusicplus;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

/**
 * @Classname TidalDirectLinkFetcher
 * @Description TODO
 * @Date 2026/4/30 10:36
 * @Created by SQ
 */
public class TidalDownloadTest {
    // ====================== 代理配置（和你的浏览器代理一致） ======================
    private static final String PROXY_HOST = "127.0.0.1";
    private static final int PROXY_PORT = 7897;
    private static final boolean ENABLE_PROXY = true; // 开启/关闭代理

    private static final String TIDAL_TRACK_ID = "337502043"; // 替换为你的 Tidal Track ID
    private static final String QUALITY = "HI_RES_LOSSLESS"; // 音质：LOSSLESS, HI_RES, HIGH

    private final OkHttpClient httpClient;
    // Tidal API 镜像列表（来自 go_backend/tidal.go 第 715-732 行 GetAvailableAPIs）
    private static final String[] TIDAL_APIS = {
            "https://eu-central.monochrome.tf",
            "https://us-west.monochrome.tf",
            "https://api.monochrome.tf",
            "https://monochrome-api.samidy.com",
            "https://tidal-api.binimum.org",
            "https://tidal.kinoplus.online",
            "https://triton.squid.wtf",
            "https://vogel.qqdl.site",
            "https://maus.qqdl.site",
            "https://hund.qqdl.site",
            "https://katze.qqdl.site",
            "https://wolf.qqdl.site",
            "https://hifi-one.spotisaver.net",
            "https://hifi-two.spotisaver.net"
    };

    public TidalDownloadTest() {
        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(25, TimeUnit.SECONDS)
                .readTimeout(25, TimeUnit.SECONDS)
                .writeTimeout(25, TimeUnit.SECONDS)
                .build();
    }

    /**
     * 主测试方法 - 执行完整的 Tidal 下载流程
     */
    public void testTidalDownload() {
        System.out.println("=== Tidal 下载链接获取测试 ===\n");

        try {
            // ========== 第一阶段：获取下载信息 ==========
            // 对应源码：go_backend/tidal.go 第 1357-1369 行 GetDownloadURL
            System.out.println("[阶段1] 获取 Tidal 下载 URL...");
            TidalDownloadInfo downloadInfo = getDownloadURL(Long.parseLong(TIDAL_TRACK_ID), QUALITY);

            System.out.println("✓ 获取成功!");
            System.out.println("  URL 类型: " + (downloadInfo.url.startsWith("MANIFEST:") ? "MANIFEST (DASH/BTS)" : "Direct URL"));
            System.out.println("  位深度: " + downloadInfo.bitDepth + "-bit");
            System.out.println("  采样率: " + downloadInfo.sampleRate + " Hz");
            System.out.println();

            // ========== 第二阶段：解析 Manifest（如果需要）==========
            String finalDownloadUrl = downloadInfo.url;

            if (downloadInfo.url.startsWith("MANIFEST:")) {
                // 对应源码：go_backend/tidal.go 第 1371-1462 行 parseManifest
                System.out.println("[阶段2] 解析 Manifest...");
                String manifestBase64 = downloadInfo.url.substring("MANIFEST:".length());
                ManifestResult manifestResult = parseManifest(manifestBase64);

                if (manifestResult.directUrl != null && !manifestResult.directUrl.isEmpty()) {
                    // BTS 格式：直接下载 URL
                    finalDownloadUrl = manifestResult.directUrl;
                    System.out.println("✓ Manifest 解析成功 (BTS 格式)");
                    System.out.println("  直接下载 URL: " + truncateUrl(finalDownloadUrl));
                } else if (manifestResult.initUrl != null && !manifestResult.initUrl.isEmpty()) {
                    // DASH 格式：分段下载
                    System.out.println("✓ Manifest 解析成功 (DASH 格式)");
                    System.out.println("  初始化段 URL: " + truncateUrl(manifestResult.initUrl));
                    System.out.println("  媒体段数量: " + manifestResult.mediaUrls.length);
                    System.out.println("  注意: DASH 格式需要下载并拼接所有分段");
                    // 这里我们只获取第一个分段作为示例
                    if (manifestResult.mediaUrls.length > 0) {
                        finalDownloadUrl = manifestResult.mediaUrls[0];
                        System.out.println("  示例分段 URL: " + truncateUrl(finalDownloadUrl));
                    }
                }
                System.out.println();
            }

            // ========== 第三阶段：验证下载链接 ==========
            System.out.println("[阶段3] 验证下载链接...");
            boolean isValid = verifyDownloadUrl(finalDownloadUrl);

            if (isValid) {
                System.out.println("✓ 下载链接有效!");
            } else {
                System.out.println("✗ 下载链接无效或不可访问");
            }
            System.out.println();

            // ========== 第四阶段：下载文件（可选）==========
            // 对应源码：go_backend/tidal.go 第 1464-1554 行 DownloadFile
            System.out.println("[阶段4] 下载文件示例...");
            
            // 根据音质信息确定文件扩展名
            String fileExtension = getFileExtension(downloadInfo);
            String outputPath = "tidal_test_" + TIDAL_TRACK_ID + fileExtension;
            
            System.out.println("  音质信息: " + downloadInfo.bitDepth + "-bit / " + downloadInfo.sampleRate + " Hz");
            System.out.println("  文件格式: " + fileExtension.substring(1).toUpperCase());
            System.out.println("  保存位置: " + outputPath);
            
            boolean downloadSuccess = downloadFile(finalDownloadUrl, outputPath);

            if (downloadSuccess) {
                System.out.println("✓ 文件下载成功: " + outputPath);
            } else {
                System.out.println("✗ 文件下载失败");
            }

        } catch (Exception e) {
            System.err.println("✗ 测试失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 阶段1: 获取下载 URL
     *
     * 对应源码：
     * - go_backend/tidal.go 第 1357-1369 行 GetDownloadURL
     * - go_backend/tidal.go 第 1306-1355 行 getDownloadURLParallel
     * - go_backend/tidal.go 第 1209-1304 行 fetchTidalURLWithRetry
     */
    private TidalDownloadInfo getDownloadURL(long trackId, String quality) throws Exception {
        System.out.println("  正在并行请求 " + TIDAL_APIS.length + " 个 API...");

        // 使用线程池并行请求所有 API（对应源码的并发逻辑）
        java.util.concurrent.ExecutorService executor =
                java.util.concurrent.Executors.newFixedThreadPool(TIDAL_APIS.length);
        java.util.concurrent.CompletableFuture<TidalDownloadInfo>[] futures = new java.util.concurrent.CompletableFuture[TIDAL_APIS.length];

        for (int i = 0; i < TIDAL_APIS.length; i++) {
            final String api = TIDAL_APIS[i];
            final int index = i;

            futures[i] = java.util.concurrent.CompletableFuture.supplyAsync(() -> {
                try {
                    return fetchTidalUrlWithRetry(api, trackId, quality, index);
                } catch (Exception e) {
                    System.err.println("  API [" + index + "] " + api + " 失败: " + e.getMessage());
                    return null;
                }
            }, executor);
        }

        // 等待第一个成功的结果
        TidalDownloadInfo result = null;
        for (java.util.concurrent.CompletableFuture<TidalDownloadInfo> future : futures) {
            try {
                result = future.get(30, java.util.concurrent.TimeUnit.SECONDS);
                if (result != null) {
                    break;
                }
            } catch (Exception e) {
                // 忽略超时或取消的任务
            }
        }

        executor.shutdownNow();

        if (result == null) {
            throw new Exception("所有 Tidal API 都失败了");
        }

        return result;
    }

    /**
     * 带重试机制的请求单个 API
     *
     * 对应源码：go_backend/tidal.go 第 1209-1304 行 fetchTidalURLWithRetry
     */
    private TidalDownloadInfo fetchTidalUrlWithRetry(String api, long trackId, String quality, int apiIndex)
            throws Exception {
        int maxRetries = 10;
        long retryDelay = 100; // 毫秒

        for (int attempt = 0; attempt <= maxRetries; attempt++) {
            if (attempt > 0) {
                System.out.println("  API [" + apiIndex + "] 重试 " + attempt + "/" + maxRetries);
                Thread.sleep(retryDelay);
                retryDelay *= 2; // 指数退避
            }

            try {
                // 构建请求 URL
                // 对应源码：go_backend/tidal.go 第 1221 行
                String requestUrl = String.format("%s/track/?id=%d&quality=%s", api, trackId, quality);
                System.out.println("  请求 URL: " + requestUrl);

                Request request = new Request.Builder()
                        .url(requestUrl)
                        .get()
                        .build();

                Response response = httpClient.newCall(request).execute();

                if (!response.isSuccessful()) {
                    response.close();

                    // 服务器错误，重试
                    if (response.code() >= 500 || response.code() == 429) {
                        continue;
                    }
                    throw new IOException("HTTP " + response.code());
                }

                String responseBody = response.body().string();
                response.close();

                // 尝试解析 V2 响应（Manifest 格式）
                // 对应源码：go_backend/tidal.go 第 1269-1280 行
                TidalDownloadInfo v2Result = tryParseV2Response(responseBody);
                if (v2Result != null) {
                    System.out.println("  ✓ API [" + apiIndex + "] " + api + " 成功 (V2 Manifest)");
                    return v2Result;
                }

                // 尝试解析 V1 响应（直接 URL）
                // 对应源码：go_backend/tidal.go 第 1282-1295 行
                TidalDownloadInfo v1Result = tryParseV1Response(responseBody);
                if (v1Result != null) {
                    System.out.println("  ✓ API [" + apiIndex + "] " + api + " 成功 (V1 Direct)");
                    return v1Result;
                }

                throw new Exception("无法解析响应");

            } catch (IOException e) {
                String errorMsg = e.getMessage().toLowerCase();

                // 网络错误，重试
                if (errorMsg.contains("timeout") ||
                        errorMsg.contains("reset") ||
                        errorMsg.contains("connection refused") ||
                        errorMsg.contains("eof")) {
                    if (attempt == maxRetries) {
                        throw e;
                    }
                    continue;
                }
                throw e;
            }
        }

        throw new Exception("所有重试都失败了");
    }

    /**
     * 尝试解析 V2 API 响应（Manifest 格式）
     *
     * 对应源码：go_backend/tidal.go 第 1269-1280 行
     */
    private TidalDownloadInfo tryParseV2Response(String responseBody) {
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

            int bitDepth = data.getIntValue("bitDepth");
            int sampleRate = data.getIntValue("sampleRate");

            // 返回 MANIFEST: 前缀的 URL
            return new TidalDownloadInfo("MANIFEST:" + manifest, bitDepth, sampleRate);

        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 尝试解析 V1 API 响应（直接 URL）
     *
     * 对应源码：go_backend/tidal.go 第 1282-1295 行
     */
    private TidalDownloadInfo tryParseV1Response(String responseBody) {
        try {
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
            return new TidalDownloadInfo(url, 16, 44100);

        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 阶段2: 解析 Manifest
     *
     * 对应源码：go_backend/tidal.go 第 1371-1462 行 parseManifest
     */
    private ManifestResult parseManifest(String manifestBase64) throws Exception {
        // Base64 解码
        byte[] manifestBytes = Base64.getDecoder().decode(manifestBase64);
        String manifestStr = new String(manifestBytes, "UTF-8");

        ManifestResult result = new ManifestResult();

        // 判断是 JSON (BTS) 还是 XML (DASH)
        if (manifestStr.trim().startsWith("{")) {
            // BTS 格式 - JSON
            System.out.println("  检测到 BTS 格式 (JSON)");
            result = parseBtsManifest(manifestStr);
        } else {
            // DASH 格式 - XML
            System.out.println("  检测到 DASH 格式 (XML/MPD)");
            result = parseDashManifest(manifestStr);
        }

        return result;
    }

    /**
     * 解析 BTS Manifest (JSON 格式)
     *
     * 对应源码：go_backend/tidal.go 第 1385-1396 行
     */
    private ManifestResult parseBtsManifest(String manifestJson) {
        ManifestResult result = new ManifestResult();

        try {
            JSONObject json = JSON.parseObject(manifestJson);

            if (json.containsKey("urls")) {
                JSONArray urls = json.getJSONArray("urls");

                if (!urls.isEmpty()) {
                    result.directUrl = urls.getString(0);
                }
            }

        } catch (Exception e) {
            System.err.println("  BTS Manifest 解析失败: " + e.getMessage());
        }

        return result;
    }

    /**
     * 解析 DASH Manifest (XML 格式)
     *
     * 对应源码：go_backend/tidal.go 第 1398-1461 行
     */
    private ManifestResult parseDashManifest(String manifestXml) {
        ManifestResult result = new ManifestResult();

        try {
            // 简化的 XML 解析（生产环境建议使用 proper XML parser）

            // 提取 initialization URL
            int initStart = manifestXml.indexOf("initialization=\"");
            if (initStart != -1) {
                initStart += "initialization=\"".length();
                int initEnd = manifestXml.indexOf("\"", initStart);
                if (initEnd != -1) {
                    result.initUrl = manifestXml.substring(initStart, initEnd)
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

                    // 计算分段数量（简化版，实际应该解析 SegmentTimeline）
                    int segmentCount = estimateSegmentCount(manifestXml);

                    // 生成分段 URLs
                    result.mediaUrls = new String[segmentCount];
                    for (int i = 1; i <= segmentCount; i++) {
                        result.mediaUrls[i - 1] = mediaTemplate.replace("$Number$", String.valueOf(i));
                    }
                }
            }

        } catch (Exception e) {
            System.err.println("  DASH Manifest 解析失败: " + e.getMessage());
        }

        return result;
    }

    /**
     * 估算分段数量（简化实现）
     *
     * 对应源码：go_backend/tidal.go 第 1426-1450 行
     */
    private int estimateSegmentCount(String manifestXml) {
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
     * 阶段3: 验证下载链接
     */
    private boolean verifyDownloadUrl(String url) {
        try {
            Request request = new Request.Builder()
                    .url(url)
                    .head() // 使用 HEAD 请求只获取头部
                    .build();

            Response response = httpClient.newCall(request).execute();
            boolean isValid = response.isSuccessful();
            response.close();

            return isValid;

        } catch (Exception e) {
            System.err.println("  验证失败: " + e.getMessage());
            return false;
        }
    }

    /**
     * 阶段4: 下载文件
     *
     * 对应源码：go_backend/tidal.go 第 1464-1554 行 DownloadFile
     */
    private boolean downloadFile(String url, String outputPath) {
        try {
            System.out.println("  开始下载...");
            System.out.println("  保存位置: " + outputPath);
            
            // 获取绝对路径
            java.io.File file = new java.io.File(outputPath);
            System.out.println("  绝对路径: " + file.getAbsolutePath());
            System.out.println("  父目录: " + (file.getParentFile() != null ? file.getParentFile().getAbsolutePath() : "当前目录"));

            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();

            Response response = httpClient.newCall(request).execute();

            if (!response.isSuccessful()) {
                System.err.println("  下载失败: HTTP " + response.code());
                response.close();
                return false;
            }

            // 获取文件大小
            long contentLength = response.body().contentLength();
            System.out.println("  文件大小: " + formatFileSize(contentLength));

            // 保存到文件
            java.io.InputStream inputStream = response.body().byteStream();
            java.io.FileOutputStream outputStream = new java.io.FileOutputStream(outputPath);

            byte[] buffer = new byte[256 * 1024]; // 256KB 缓冲区
            long bytesRead = 0;
            int read;

            while ((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
                bytesRead += read;

                // 显示进度
                if (contentLength > 0) {
                    int progress = (int) ((bytesRead * 100) / contentLength);
                    System.out.print("\r  下载进度: " + progress + "%");
                }
            }

            inputStream.close();
            outputStream.close();
            response.close();

            System.out.println();
            return true;

        } catch (Exception e) {
            System.err.println("  下载异常: " + e.getMessage());
            return false;
        }
    }

    // ==================== 辅助方法 ====================

    /**
     * 截断 URL 用于显示
     */
    private String truncateUrl(String url) {
        if (url.length() <= 80) {
            return url;
        }
        return url.substring(0, 40) + "..." + url.substring(url.length() - 40);
    }

    /**
     * 格式化文件大小
     */
    private String formatFileSize(long bytes) {
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1024 * 1024) return String.format("%.2f KB", bytes / 1024.0);
        if (bytes < 1024 * 1024 * 1024) return String.format("%.2f MB", bytes / (1024.0 * 1024));
        return String.format("%.2f GB", bytes / (1024.0 * 1024 * 1024));
    }

    /**
     * 根据音质信息获取文件扩展名
     * 
     * @param downloadInfo 下载信息
     * @return 文件扩展名（如 .flac, .m4a, .mp3）
     */
    private String getFileExtension(TidalDownloadInfo downloadInfo) {
        // 根据位深度和采样率判断格式
        int bitDepth = downloadInfo.bitDepth;
        int sampleRate = downloadInfo.sampleRate;
        
        // Hi-Res: 24-bit/96kHz 或更高 -> FLAC
        if (bitDepth >= 24 && sampleRate >= 96000) {
            return ".flac";
        }
        
        // Lossless: 16-bit/44.1kHz 或 24-bit/44.1kHz -> FLAC
        if (bitDepth >= 16 && sampleRate >= 44100) {
            return ".flac";
        }
        
        // High: MP3/AAC -> M4A (AAC)
        if (bitDepth == 0 || sampleRate == 0) {
            // 如果位深度和采样率为 0，可能是 AAC 格式
            return ".m4a";
        }
        
        // 默认使用 FLAC
        return ".flac";
    }

    // ==================== 数据类 ====================

    /**
     * Tidal 下载信息
     *
     * 对应源码：go_backend/tidal.go 第 1190-1194 行 TidalDownloadInfo
     */
    static class TidalDownloadInfo {
        String url;
        int bitDepth;
        int sampleRate;

        TidalDownloadInfo(String url, int bitDepth, int sampleRate) {
            this.url = url;
            this.bitDepth = bitDepth;
            this.sampleRate = sampleRate;
        }
    }

    /**
     * Manifest 解析结果
     *
     * 对应源码：go_backend/tidal.go 第 1371 行 parseManifest 返回值
     */
    static class ManifestResult {
        String directUrl;      // BTS 格式的直接 URL
        String initUrl;        // DASH 格式的初始化段 URL
        String[] mediaUrls;    // DASH 格式的媒体段 URLs

        ManifestResult() {
            this.directUrl = null;
            this.initUrl = null;
            this.mediaUrls = new String[0];
        }
    }

    // ==================== 主函数 ====================

    public static void main(String[] args) {
        TidalDownloadTest test = new TidalDownloadTest();
        test.testTidalDownload();
    }
}
