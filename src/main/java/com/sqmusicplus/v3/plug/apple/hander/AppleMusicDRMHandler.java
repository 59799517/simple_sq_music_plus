package com.sqmusicplus.v3.plug.apple.hander;

import com.alibaba.fastjson.JSONObject;
import com.sqmusicplus.v3.plug.apple.config.AppleConfig;
import com.sqmusicplus.v3.utils.OkHttpUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Classname AppleMusicDRMHandler
 * @Description 处理Apple Music歌曲的DRM解密和音频处理流程
 * @Version 1.0.0
 * @Date 2025/10/18 10:00
 * @Created by SQ
 */
@Component
@Slf4j
public class AppleMusicDRMHandler {

    @Autowired
    private AppleConfig appleConfig;

    /**
     * 步骤1：从m3u8播放列表中提取DRM信息
     * 查找data_id为"com.apple.hls.AudioSessionKeyInfo"的会话数据，然后对value字段进行base64解码并解析为JSON格式
     * 
     * @param m3u8Content m3u8播放列表内容
     * @return DRM信息对象，如果未找到则返回null
     */
    public static JSONObject getDrmInfos(String m3u8Content) {
        // 1.1 在m3u8内容中查找EXT-X-SESSION-DATA标签，其中包含com.apple.hls.AudioSessionKeyInfo
        // 匹配格式: #EXT-X-SESSION-DATA:DATA-ID="com.apple.hls.AudioSessionKeyInfo",VALUE="base64data"
        // 或者: #EXT-X-SESSION-DATA:DATA-ID="com.apple.hls.AudioSessionKeyInfo",URI="url"
        Pattern pattern = Pattern.compile("#EXT-X-SESSION-DATA:DATA-ID=\"com\\.apple\\.hls\\.AudioSessionKeyInfo\"[^,]*?,VALUE=\"(.*?)\"", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(m3u8Content);
        
        if (matcher.find()) {
            // 1.2 获取base64编码的DRM信息
            String base64Data = matcher.group(1);
            try {
                // 1.3 清理base64数据中的换行符和空格
                base64Data = base64Data.replaceAll("\\s+", "");
                // 1.4 解码base64编码的DRM信息
                byte[] decodedBytes = Base64.getDecoder().decode(base64Data);
                String decodedString = new String(decodedBytes, "UTF-8");
                // 1.5 解析为JSON对象并返回
                return JSONObject.parseObject(decodedString);
            } catch (Exception e) {
                log.error("解码DRM信息失败: base64Data=" + base64Data, e);
                return null;
            }
        } else {
            log.warn("在m3u8内容中未找到com.apple.hls.AudioSessionKeyInfo会话数据");
        }
        return null;
    }

    /**
     * 步骤2：从m3u8播放列表中获取资源信息
     * 查找data_id为"com.apple.hls.audioAssetMetadata"的会话数据，然后对value字段进行base64解码并解析为JSON格式
     * 
     * @param m3u8Content m3u8播放列表内容
     * @return 资源信息对象
     */
    public static JSONObject getAssetInfos(String m3u8Content) {
        // 2.1 在m3u8内容中查找EXT-X-SESSION-DATA标签，其中包含com.apple.hls.audioAssetMetadata
        Pattern pattern = Pattern.compile("#EXT-X-SESSION-DATA:DATA-ID=\"com\\.apple\\.hls\\.audioAssetMetadata\"[^,]*?,VALUE=\"(.*?)\"", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(m3u8Content);
        
        if (matcher.find()) {
            // 2.2 获取base64编码的资源信息
            String base64Data = matcher.group(1);
            try {
                // 2.3 清理base64数据中的换行符和空格
                base64Data = base64Data.replaceAll("\\s+", "");
                // 2.4 解码base64编码的资源信息
                byte[] decodedBytes = Base64.getDecoder().decode(base64Data);
                String decodedString = new String(decodedBytes, "UTF-8");
                // 2.5 解析为JSON对象并返回
                return JSONObject.parseObject(decodedString);
            } catch (Exception e) {
                log.error("解码资源信息失败: base64Data=" + base64Data, e);
                return null;
            }
        } else {
            log.warn("在m3u8内容中未找到com.apple.hls.audioAssetMetadata会话数据");
        }
        return null;
    }

    /**
     * 解析m3u8内容并提取播放列表信息
     * 
     * @param m3u8Content m3u8内容
     * @param baseUrl 基础URL
     * @return 播放列表列表
     */
    public static List<Map<String, Object>> parseM3u8Playlists(String m3u8Content, String baseUrl) {
        List<Map<String, Object>> playlists = new ArrayList<>();
        String[] lines = m3u8Content.split("\n");
        Map<String, Object> currentPlaylist = null;
        
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].trim();
            
            if (line.startsWith("#EXT-X-STREAM-INF:")) {
                currentPlaylist = new HashMap<>();
                // 解析流信息参数
                String streamInfo = line.substring("#EXT-X-STREAM-INF:".length());
                String[] params = streamInfo.split(",");
                
                for (String param : params) {
                    String[] keyValue = param.split("=", 2);
                    if (keyValue.length == 2) {
                        String key = keyValue[0].trim();
                        String value = keyValue[1].trim().replaceAll("\"", "");
                        
                        switch (key) {
                            case "AUDIO":
                                currentPlaylist.put("audio", value);
                                break;
                            case "AVERAGE-BANDWIDTH":
                                currentPlaylist.put("average_bandwidth", Integer.parseInt(value));
                                break;
                            case "CODECS":
                                currentPlaylist.put("codecs", value);
                                break;
                            case "STABLE-VARIANT-ID":
                                currentPlaylist.put("stable_variant_id", value);
                                break;
                        }
                    }
                }
            } else if (!line.startsWith("#") && !line.isEmpty() && currentPlaylist != null) {
                // 这是URI行
                String uri = line;
                if (!uri.startsWith("http")) {
                    // 处理相对URL
                    uri = baseUrl.substring(0, baseUrl.lastIndexOf("/") + 1) + uri;
                }
                currentPlaylist.put("uri", uri);
                playlists.add(currentPlaylist);
                currentPlaylist = null;
            }
        }
        
        return playlists;
    }

    /**
     * 根据编解码器类型从播放列表中选择合适的播放列表
     * 
     * @param playlists 播放列表列表
     * @param codec 编解码器类型
     * @return 匹配的播放列表，如果没有找到则返回null
     */
    public static Map<String, Object> getPlaylistFromCodec(List<Map<String, Object>> playlists, String codec) {
        List<Map<String, Object>> matchedPlaylists = new ArrayList<>();
        
        // 根据编解码器类型筛选播放列表
        for (Map<String, Object> playlist : playlists) {
            String audioCodec = (String) playlist.get("audio");
            if (audioCodec != null && matchCodec(audioCodec, codec)) {
                matchedPlaylists.add(playlist);
            }
        }
        
        if (matchedPlaylists.isEmpty()) {
            return null;
        }
        
        // 按平均带宽排序并返回最高质量的播放列表
        Collections.sort(matchedPlaylists, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> p1, Map<String, Object> p2) {
                Integer bw1 = (Integer) p1.get("average_bandwidth");
                Integer bw2 = (Integer) p2.get("average_bandwidth");
                if (bw1 == null) bw1 = 0;
                if (bw2 == null) bw2 = 0;
                return bw2.compareTo(bw1); // 降序排列
            }
        });
        
        return matchedPlaylists.get(0);
    }

    /**
     * 检查音频编解码器是否与指定的编解码器类型匹配
     * 
     * @param audioCodec 音频编解码器标识
     * @param codec 目标编解码器类型
     * @return 是否匹配
     */
    private static boolean matchCodec(String audioCodec, String codec) {
        // 编解码器匹配规则
        switch (codec) {
            case "aac-legacy":
                return audioCodec.matches("aac[-_]legacy");
            case "aac-he-legacy":
                return audioCodec.matches("aac[-_]he[-_]legacy");
            case "aac":
                return audioCodec.equals("aac");
            case "aac-he":
                return audioCodec.matches("aac[-_]he");
            case "aac-binaural":
                return audioCodec.matches("aac[-_]binaural");
            case "aac-downmix":
                return audioCodec.matches("aac[-_]downmix");
            case "aac-he-binaural":
                return audioCodec.matches("aac[-_]he[-_]binaural");
            case "aac-he-downmix":
                return audioCodec.matches("aac[-_]he[-_]downmix");
            case "atmos":
                return audioCodec.equals("atmos");
            case "ac3":
                return audioCodec.equals("ac3");
            case "alac":
                return audioCodec.equals("alac");
            default:
                return audioCodec.equals(codec);
        }
    }

    /**
     * 步骤3：通过m3u8地址获取流信息
     * 
     * @param m3u8Url m3u8播放列表地址
     * @return 流信息对象
     */
    public static StreamInfo getStreamInfoFromM3u8Url(String m3u8Url) {
        StreamInfo streamInfo = new StreamInfo();
        
        try {
            // 3.1 构建请求并获取m3u8数据
            Request request = new Request.Builder()
                    .url(m3u8Url)
                    .addHeader("authorization", "Bearer eyJhbGciOiJFUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6IldlYlBsYXlLaWQifQ.eyJpc3MiOiJBTVBXZWJQbGF5IiwiaWF0IjoxNzU3NjM4NTkzLCJleHAiOjE3NjQ4OTYxOTMsInJvb3RfaHR0cHNfb3JpZ2luIjpbImFwcGxlLmNvbSJdfQ.ojmBjfRBGXX3gj4MYpajgRBt8PCN-NQniQOnqflBHXyvaTICY5o7Tz64iW6r-coq1jf290wUGmpk4IhW8Ksaeg")
                    .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:95.0) Gecko/20100101 Firefox/95.0")
                    .addHeader("Accept", "application/json")
                    .addHeader("Accept-Language", "en-US,en;q=0.5")
                    // 移除Accept-Encoding，让OkHttp自动处理解压
                    .addHeader("content-type", "application/json")
                    .addHeader("Media-User-Token", "Ati/NdVW8oz+CVO5nVKF2rX/y1XQKh0GOPJbECVL6e+tpTP/OKM3PFwJp9a/EQn/9LWT68PSiYugfjeQwnpuLWfqVc9G2ZfNdWrbq8dMfjenTw4cqdKZzInY7Q8HFygDI/gh8Up7s0nCvmU/RCqabsoKtJhhE1g6+YKUSbDb4gUIFn8yVzBn7dpx19ihAqpHK6FPUCNCmXCLUMGUOrWRcHmyhCa1pl5vOvgC0sCFeoMRcx92+g")
                    .addHeader("x-apple-renewal", "true")
                    .addHeader("DNT", "1")
                    .addHeader("Connection", "keep-alive")
                    .addHeader("Sec-Fetch-Dest", "empty")
                    .addHeader("Sec-Fetch-Mode", "cors")
                    .addHeader("Sec-Fetch-Site", "same-site")
                    .addHeader("origin", "https://beta.music.apple.com")
                    .build();
            
            try (Response response = OkHttpUtils.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    String m3u8Content = response.body().string();
                    
                    // 3.2 提取DRM信息
                    JSONObject drmInfos = getDrmInfos(m3u8Content);
                    if (drmInfos == null) {
                        log.warn("未找到DRM信息");
                        return streamInfo;
                    }
                    
                    // 3.3 提取资源信息
                    JSONObject assetInfos = getAssetInfos(m3u8Content);
                    if (assetInfos == null) {
                        log.warn("未找到资源信息");
                        return streamInfo;
                    }
                    
                    // 3.4 解析播放列表获取流URL和其他信息
                    List<Map<String, Object>> playlists = parseM3u8Playlists(m3u8Content, m3u8Url);
                    
                    // 选择默认编解码器的播放列表（这里使用aac作为示例）
                    Map<String, Object> selectedPlaylist = getPlaylistFromCodec(playlists, "aac");
                    if (selectedPlaylist != null) {
                        streamInfo.setStreamUrl((String) selectedPlaylist.get("uri"));
                        
                        // 获取编解码器信息
                        streamInfo.setCodec((String) selectedPlaylist.get("codecs"));
                        
                        // 获取DRM密钥信息（简化处理）
                        streamInfo.setWidevinePssh("widevine_pssh_placeholder");
                        streamInfo.setPlayreadyPssh("playready_pssh_placeholder");
                        streamInfo.setFairplayKey("fairplay_key_placeholder");
                    } else {
                        log.warn("未找到合适的播放列表");
                        // 回退到原来的简单提取方式
                        streamInfo.setStreamUrl(extractStreamUrl(m3u8Content, m3u8Url));
                        streamInfo.setCodec("codec_placeholder");
                        streamInfo.setWidevinePssh("widevine_pssh_placeholder");
                        streamInfo.setPlayreadyPssh("playready_pssh_placeholder");
                        streamInfo.setFairplayKey("fairplay_key_placeholder");
                    }
                }
            }
        } catch (IOException e) {
            log.error("获取流信息失败", e);
        }
        
        return streamInfo;
    }

    /**
     * 步骤4：从m3u8内容中提取流URL
     * 
     * @param m3u8Content m3u8内容
     * @param baseUrl 基础URL
     * @return 流URL
     */
    private static String extractStreamUrl(String m3u8Content, String baseUrl) {
        // 查找包含URI的行
        String[] lines = m3u8Content.split("\n");
        for (String line : lines) {
            if (!line.startsWith("#") && line.contains(".m3u8")) {
                // 如果是相对路径，需要拼接基础URL
                if (line.startsWith("http")) {
                    return line.trim();
                } else {
                    // 处理相对路径
                    String base = baseUrl.substring(0, baseUrl.lastIndexOf("/") + 1);
                    return base + line.trim();
                }
            }
        }
        return "";
    }

    /**
     * 步骤5：从跟踪元数据获取流信息
     * 
     * @param trackMetadata 跟踪元数据
     * @return 流信息对象
     */
    public static StreamInfo getStreamInfo(JSONObject trackMetadata) {
        StreamInfo streamInfo = new StreamInfo();
        
        // 5.1 从元数据中获取m3u8 URL
        JSONObject extendedAssetUrls = trackMetadata.getJSONObject("attributes").getJSONObject("extendedAssetUrls");
        String m3u8Url = extendedAssetUrls.getString("enhancedHls");
        
        if (m3u8Url == null || m3u8Url.isEmpty()) {
            log.warn("未找到增强型HLS URL");
            return streamInfo;
        }
        
        // 5.2 调用通过URL获取流信息的方法
        return getStreamInfoFromM3u8Url(m3u8Url);
    }

    /**
     * 步骤6：获取解密密钥
     * 
     * @param songId 歌曲ID
     * @param appleMusicAPI Apple Music API实例
     * @param widevineClient Widevine客户端实例
     * @return 解密密钥
     */
    public static String getDecryptionKey(String songId, AppleMusicAPI appleMusicAPI, WidevineClient widevineClient) {
        // 6.1 获取webplayback信息
        JSONObject webplayback = appleMusicAPI.getWebplayback(songId);
        if (webplayback == null) {
            log.error("获取webplayback信息失败");
            return null;
        }
        
        // 6.2 获取跟踪URI
        String trackUri = webplayback.getJSONArray("assets").getJSONObject(0).getString("URL");
        
        // 6.3 获取Widevine许可证
        JSONObject keyInfo = appleMusicAPI.getWidevineLicense(
            songId,
            trackUri,
            Base64.getEncoder().encodeToString(
                widevineClient.getChallenge(false)
            )
        );
        if (keyInfo == null) {
            log.error("获取Widevine许可证失败");
            return null;
        }
        
        // 6.4 提供许可证并获取密钥
        widevineClient.provideLicense(keyInfo);
        return widevineClient.getKey();
    }

    /**
     * 步骤7：解密文件
     * 
     * @param encryptedPath 加密文件路径
     * @param decryptedPath 解密文件路径
     * @param decryptionKey 解密密钥
     * @param mp4DecryptPath mp4decrypt工具路径
     */
    public static void decrypt(String encryptedPath, String decryptedPath, String decryptionKey, String mp4DecryptPath) {
        // 7.1 修复密钥ID
        fixKeyId(encryptedPath);
        
        // 7.2 调用mp4decrypt工具进行解密
        // 实际实现需要调用外部工具或库进行解密
        log.info("解密文件: {} -> {}, 使用密钥: {}", encryptedPath, decryptedPath, decryptionKey);
        log.info("使用解密工具: {}", mp4DecryptPath);
    }

    /**
     * 步骤8：修复密钥ID
     * 
     * @param encryptedPath 加密文件路径
     */
    private static void fixKeyId(String encryptedPath) {
        // 这里需要处理加密文件中的密钥ID
        // 实际实现需要根据具体文件格式进行调整
        log.info("修复密钥ID: {}", encryptedPath);
    }

    /**
     * 步骤9：重新封装文件
     * 
     * @param decryptedPath 解密文件路径
     * @param remuxedPath 重新封装文件路径
     * @param codec 编解码器
     * @param remuxMode 重新封装模式
     * @param mp4boxPath MP4Box工具路径
     * @param ffmpegPath FFmpeg工具路径
     */
    public static void remux(String decryptedPath, String remuxedPath, String codec, RemuxMode remuxMode, String mp4boxPath, String ffmpegPath) {
        // 9.1 根据配置选择重新封装方式
        if (remuxMode == RemuxMode.MP4BOX) {
            // 使用MP4Box重新封装
            log.info("使用MP4Box重新封装文件: {} -> {}, 工具路径: {}", decryptedPath, remuxedPath, mp4boxPath);
        } else if (remuxMode == RemuxMode.FFMPEG) {
            // 使用FFmpeg重新封装
            log.info("使用FFmpeg重新封装文件: {} -> {}, 工具路径: {}", decryptedPath, remuxedPath, ffmpegPath);
        }
        log.info("使用的编解码器: {}", codec);
    }

    /**
     * 流信息类
     */
    public static class StreamInfo {
        private String streamUrl;
        private String widevinePssh;
        private String playreadyPssh;
        private String fairplayKey;
        private String codec;

        // Getters and Setters
        public String getStreamUrl() {
            return streamUrl;
        }

        public void setStreamUrl(String streamUrl) {
            this.streamUrl = streamUrl;
        }

        public String getWidevinePssh() {
            return widevinePssh;
        }

        public void setWidevinePssh(String widevinePssh) {
            this.widevinePssh = widevinePssh;
        }

        public String getPlayreadyPssh() {
            return playreadyPssh;
        }

        public void setPlayreadyPssh(String playreadyPssh) {
            this.playreadyPssh = playreadyPssh;
        }

        public String getFairplayKey() {
            return fairplayKey;
        }

        public void setFairplayKey(String fairplayKey) {
            this.fairplayKey = fairplayKey;
        }

        public String getCodec() {
            return codec;
        }

        public void setCodec(String codec) {
            this.codec = codec;
        }
    }
    
    /**
     * 重新封装模式枚举
     */
    public enum RemuxMode {
        MP4BOX,
        FFMPEG
    }
    
    /**
     * Apple Music API接口（占位符）
     */
    public static class AppleMusicAPI {
        public JSONObject getWebplayback(String songId) {
            // 实际实现需要调用Apple Music API获取webplayback信息
            return new JSONObject();
        }
        
        public JSONObject getWidevineLicense(String songId, String trackUri, String challenge) {
            // 实际实现需要调用Apple Music API获取Widevine许可证
            return new JSONObject();
        }
    }
    
    /**
     * Widevine客户端（使用JNI实现）
     */
    public static class WidevineClient {
        private WidevineJNIWrapper jniWrapper;
        
        public WidevineClient() {
            this.jniWrapper = WidevineJNIWrapper.getInstance();
        }
        
        public byte[] getChallenge(boolean isFirefox) {
            // 实际实现需要与Widevine客户端交互获取挑战
            return jniWrapper.getChallenge(isFirefox, null);
        }
        
        public void provideLicense(JSONObject keyInfo) {
            // 实际实现需要与Widevine客户端交互提供许可证
            // 这里需要将JSONObject转换为字节数组
            // 简化处理，实际需要解析keyInfo中的许可证数据
            jniWrapper.provideLicense(keyInfo.toJSONString().getBytes());
        }
        
        public String getKey() {
            // 实际实现需要从Widevine客户端获取解密密钥
            // 这里需要指定具体的密钥ID
            // 简化处理，返回占位符
            return "decryption_key_placeholder";
        }
    }
}