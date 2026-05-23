package com.sqmusicplus.v3.plug.tidal.utils;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * @Classname TidalManifestUtils
 * @Description Tidal Manifest 解密工具类
 * @Version 1.0.0
 * @Date 2026/4/29
 * @Created by SQ
 */
@Slf4j
public class TidalManifestUtils {

    // 固定解密密钥，和 SpotiFLAC 源码完全一致
    private static final String TIDAL_DECRYPT_KEY = "UIlTTEMmmLfGowo/UC60xw==";

    /**
     * 解密 Tidal Manifest 获取下载链接
     * 
     * @param encryptedManifestBase64 加密的 manifest（Base64编码）
     * @return 解密后的下载链接
     * @throws Exception 解密失败时抛出异常
     */
    public static String decryptManifest(String encryptedManifestBase64) throws Exception {
        if (encryptedManifestBase64 == null || encryptedManifestBase64.isEmpty()) {
            throw new IllegalArgumentException("Manifest 不能为空");
        }

        log.info("========== 开始解密 Manifest ==========");
        log.info("加密数据长度: {} 字符", encryptedManifestBase64.length());
        log.info("加密数据前缀: {}", encryptedManifestBase64.substring(0, Math.min(50, encryptedManifestBase64.length())) + "...");

        try {
            // 1. Base64解码密钥和加密内容
            byte[] keyBytes = Base64.getDecoder().decode(TIDAL_DECRYPT_KEY);
            byte[] encryptedData = Base64.getDecoder().decode(encryptedManifestBase64);

            log.debug("密钥长度: {} bytes", keyBytes.length);
            log.debug("加密数据长度: {} bytes", encryptedData.length);

            // 2. 初始化AES解密器
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, keySpec);

            log.info("步骤1: AES 解密...");
            
            // 3. 执行解密
            byte[] decryptedData = cipher.doFinal(encryptedData);
            String decryptedJson = new String(decryptedData);

            log.info("✓ AES 解密成功");
            log.info("解密后 JSON 长度: {} 字符", decryptedJson.length());
            log.info("解密后 JSON 前缀: {}", decryptedJson.length() > 200 ? decryptedJson.substring(0, 200) + "..." : decryptedJson);
            
            // 判断 JSON 类型
            if (decryptedJson.startsWith("{")) {
                log.info("JSON 类型: BTS 格式（直接 URL）");
            } else if (decryptedJson.startsWith("<")) {
                log.info("JSON 类型: MPD 格式（分段下载）");
            } else {
                log.warn("JSON 类型: 未知格式");
            }

            // 4. 提取最终下载链接
            log.info("步骤2: 提取下载链接...");
            String downloadUrl = extractDownloadUrl(decryptedJson);
            
            if (downloadUrl == null || downloadUrl.isEmpty()) {
                log.error("❌ 无法从解密后的 JSON 中提取下载链接");
                log.error("完整解密内容: {}", decryptedJson);
                throw new RuntimeException("无法提取下载链接");
            }

            log.info("✓ 成功提取下载链接");
            log.info("  URL 长度: {} 字符", downloadUrl.length());
            log.info("  URL 前缀: {}", downloadUrl.substring(0, Math.min(100, downloadUrl.length())) + "...");
            log.info("========== Manifest 解密完成 ==========\n");
            
            return downloadUrl;

        } catch (Exception e) {
            log.error("❌ Manifest 解密失败", e);
            log.error("错误类型: {}", e.getClass().getName());
            log.error("错误消息: {}", e.getMessage());
            throw new RuntimeException("Manifest 解密失败: " + e.getMessage(), e);
        }
    }

    /**
     * 从解密后的 JSON 中提取下载链接
     * 
     * @param decryptedJson 解密后的 JSON 字符串
     * @return 下载链接
     */
    private static String extractDownloadUrl(String decryptedJson) {
        try {
            // 方法1: 使用字符串提取（简单快速）
            int urlStart = decryptedJson.indexOf("\"urls\":[\"") + 8;
            if (urlStart < 8) {
                log.warn("未找到 \"urls\":[\" 标记，尝试其他方法");
                return extractUrlByAlternativeMethods(decryptedJson);
            }
            
            int urlEnd = decryptedJson.indexOf("\"", urlStart);
            if (urlEnd < urlStart) {
                log.warn("URL 结束标记未找到");
                return null;
            }
            
            String url = decryptedJson.substring(urlStart, urlEnd);
            log.debug("通过字符串提取获得 URL: {}...", url.substring(0, Math.min(50, url.length())));
            return url;

        } catch (Exception e) {
            log.error("提取下载链接失败", e);
            return extractUrlByAlternativeMethods(decryptedJson);
        }
    }

    /**
     * 备用方法提取 URL
     * 
     * @param decryptedJson 解密后的 JSON
     * @return 下载链接
     */
    private static String extractUrlByAlternativeMethods(String decryptedJson) {
        try {
            // 方法2: 查找 http:// 或 https://
            int httpIndex = decryptedJson.indexOf("http");
            if (httpIndex >= 0) {
                int urlEnd = decryptedJson.indexOf("\"", httpIndex);
                if (urlEnd < 0) {
                    urlEnd = decryptedJson.indexOf(",", httpIndex);
                }
                if (urlEnd < 0) {
                    urlEnd = decryptedJson.indexOf("}", httpIndex);
                }
                if (urlEnd > httpIndex) {
                    String url = decryptedJson.substring(httpIndex, urlEnd);
                    log.debug("通过 HTTP 标记提取 URL: {}...", url.substring(0, Math.min(50, url.length())));
                    return url;
                }
            }
            
            log.warn("所有提取方法都失败");
            return null;
            
        } catch (Exception e) {
            log.error("备用提取方法失败", e);
            return null;
        }
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        System.out.println("Tidal Manifest 解密工具类");
        System.out.println("========================");
        System.out.println("解密密钥: " + TIDAL_DECRYPT_KEY);
        System.out.println("\n使用方法:");
        System.out.println("String downloadUrl = TidalManifestUtils.decryptManifest(manifest);");
    }
}
