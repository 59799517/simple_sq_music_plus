package com.sqmusicplus.v3.utils;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.sqmusicplus.v3.alidrive.entity.ShareInfo;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * 阿里云盘工具
 */
@Slf4j
public class AliyunDriveUtils {
    /**
     * 基础 URL
     */
    static final String baseUrl = "openapi.alipan.com";

    /**
     * 回调地址 五后台模式不需要回调
     */
    static final String redirectUri = "oob";
    /**
     * 获取权限（只要用户基本信息和 文件读写权限）
     */
    static final String scope = "user:base,file:all:read,file:all:write";
    /**
     * 请求类型 支持code
     */
    static final String responseType = "code";



    private static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .hostnameVerifier((hostName, session) -> true)
            .protocols(Arrays.asList(Protocol.HTTP_2, Protocol.HTTP_1_1))
            .retryOnConnectionFailure(true)
            .followRedirects(false)
            .followSslRedirects(false)
            .build();




    /**
     * 获取授权码
     * @return 授权码 JSON 对象 code_verifier 和 location
     *
     */
    public static JSONObject getAuthorizationCode(String appId) {
        //生成一个长度 43 - 128 的随机字符串作为 code_verifier
        String code_verifier = RandomUtil.randomString(125);
        
        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host(baseUrl)
                .addPathSegment("oauth")
                .addPathSegment("authorize")
                .addQueryParameter("client_id", appId)
                .addQueryParameter("redirect_uri", redirectUri)
                .addQueryParameter("scope", scope)
                .addQueryParameter("response_type", responseType)
                .addQueryParameter("code_challenge_method", "plain")
                .addQueryParameter("code_challenge", code_verifier)  // plain 模式下 code_challenge = code_verifier
                .build();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();




        try (Response response = okHttpClient.newCall(request).execute()) {
            if (response.code() != 302) return null;
            // 提取 location
            String location = response.header("location");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code_verifier", code_verifier);  // 保存 code_verifier 供后续使用
            jsonObject.put("location", location);
            return jsonObject;

        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取授权码异常：{}", e);
            return null;
        }
    }


    /**
     * 根据授权码获取令牌（PKCE 模式）
     * @param authorizationCode 授权码（code 10 分钟内有效，只能用一次）
     * @param code_verifier 第一步生成的 code_verifier（必须与第一步的 code_challenge 原始值一致）
     */
    public static JSONObject getAccessToken(String appId,String authorizationCode, String code_verifier) {
        // 构建请求的 JSON 数据
        JSONObject requestBody = new JSONObject();
        requestBody.put("grant_type", "authorization_code");
        requestBody.put("code", authorizationCode);
        requestBody.put("client_id", appId);
        requestBody.put("code_verifier", code_verifier);  // PKCE 模式必填，使用 code_challenge 的原始值
        
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                requestBody.toJSONString()
        );

        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host(baseUrl)
                .addPathSegment("oauth")
                .addPathSegment("access_token")
                .build();
        
        OkHttpUtils builder = OkHttpUtils.builder();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = builder.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                log.error("获取 Access Token 失败：" + response.body().string());
                return null;
            }
            JSONObject result = JSONObject.parseObject(response.body().string());
            
            // 设置过期时间：当前时间加上 expiresIn 秒转为时间字符串并打印
            Long expiresIn = result.getLong("expires_in");
            if (expiresIn != null) {
                long expirationTime = System.currentTimeMillis() + expiresIn * 1000;
                String expirationTimeString = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date(expirationTime));
                log.info("Access Token 获取成功，过期时间：{} ({} 秒后过期)", expirationTimeString, expiresIn);
                result.put("expiration_time", expirationTime);
                result.put("expiration_time_string", expirationTimeString);
            }
            
            return result;
        } catch (IOException e) {
            log.error("获取 Access Token 异常：" + e.getMessage(), e);
            return null;
        }
    }
    
    /**
     * 使用 refresh_token 刷新 access_token
     * @param refreshToken 刷新 token（单次请求有效）
     */
    public static JSONObject refreshAccessToken(String appId,String refreshToken) {
        // 构建请求的 JSON 数据
        JSONObject requestBody = new JSONObject();
        requestBody.put("grant_type", "refresh_token");
        requestBody.put("refresh_token", refreshToken);
        requestBody.put("client_id", appId);
        
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                requestBody.toJSONString()
        );

        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host(baseUrl)
                .addPathSegment("oauth")
                .addPathSegment("access_token")
                .build();
        
        OkHttpUtils builder = OkHttpUtils.builder();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = builder.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException("刷新 Access Token 失败：" + response.body().string());
            }
            JSONObject result = JSONObject.parseObject(response.body().string());
            
            // 设置过期时间：当前时间加上 expiresIn 秒转为时间字符串并打印
            Long expiresIn = result.getLong("expires_in");
            if (expiresIn != null) {
                long expirationTime = System.currentTimeMillis() + expiresIn * 1000;
                String expirationTimeString = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date(expirationTime));
                log.info("Access Token 刷新成功，过期时间：{} ({} 秒后过期)", expirationTimeString, expiresIn);
                result.put("expiration_time", expirationTime);
                result.put("expiration_time_string", expirationTimeString);
            }
            
            return result;
        } catch (IOException e) {
            throw new RuntimeException("刷新 Access Token 异常：" + e.getMessage(), e);
        }
    }
    
    /**
     * 获取用户信息
     * API: GET /oauth/users/info
     * @param accessToken 访问令牌
     * @return 用户信息 JSON 对象
     */
    public static JSONObject getUserInfo(String accessToken) {
        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host(baseUrl)
                .addPathSegment("oauth")
                .addPathSegment("users")
                .addPathSegment("info")
                .build();
        
        OkHttpUtils builder = OkHttpUtils.builder();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Authorization", "Bearer " + accessToken)
                .build();
        
        try (Response response = builder.newCall(request).execute()) {
            String responseBody = response.body().string();
            
            if (!response.isSuccessful()) {
                log.error("获取用户信息失败：" + responseBody);
                return null;
            }
            
            return JSONObject.parseObject(responseBody);
        } catch (IOException e) {
            log.error("获取用户信息异常：" + e.getMessage(), e);
            return null;
        }
    }
    
    /**
     * 获取用户 Drive 信息
     * API: POST /adrive/v1.0/user/getDriveInfo
     * @param accessToken 访问令牌
     * @return 用户 Drive 信息 JSON 对象
     */
    public static JSONObject getDriveInfo(String accessToken) {
        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host(baseUrl)
                .addPathSegment("adrive")
                .addPathSegment("v1.0")
                .addPathSegment("user")
                .addPathSegment("getDriveInfo")
                .build();
        
        OkHttpUtils builder = OkHttpUtils.builder();
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), "{}"))
                .addHeader("Authorization", "Bearer " + accessToken)
                .build();
        
        try (Response response = builder.newCall(request).execute()) {
            String responseBody = response.body().string();
            
            if (!response.isSuccessful()) {
                throw new RuntimeException("获取 Drive 信息失败：" + responseBody);
            }
            
            return JSONObject.parseObject(responseBody);
        } catch (IOException e) {
            throw new RuntimeException("获取 Drive 信息异常：" + e.getMessage(), e);
        }
    }
    
    /**
     * 获取用户空间信息
     * API: POST /adrive/v1.0/user/getSpaceInfo
     * @param accessToken 访问令牌
     * @return 用户空间信息 JSON 对象
     */
    public static JSONObject getSpaceInfo(String accessToken) {
        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host(baseUrl)
                .addPathSegment("adrive")
                .addPathSegment("v1.0")
                .addPathSegment("user")
                .addPathSegment("getSpaceInfo")
                .build();
        
        OkHttpUtils builder = OkHttpUtils.builder();
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), "{}"))
                .addHeader("Authorization", "Bearer " + accessToken)
                .build();
        
        try (Response response = builder.newCall(request).execute()) {
            String responseBody = response.body().string();
            
            if (!response.isSuccessful()) {
                throw new RuntimeException("获取空间信息失败：" + responseBody);
            }
            
            return JSONObject.parseObject(responseBody);
        } catch (IOException e) {
            throw new RuntimeException("获取空间信息异常：" + e.getMessage(), e);
        }
    }
    
    /**
     * 获取用户 VIP 信息
     * API: POST /business/v1.0/user/getVipInfo
     * @param accessToken 访问令牌
     * @return 用户 VIP 信息 JSON 对象
     */
    public static JSONObject getVipInfo(String accessToken) {
        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host(baseUrl)
                .addPathSegment("business")
                .addPathSegment("v1.0")
                .addPathSegment("user")
                .addPathSegment("getVipInfo")
                .build();
        
        OkHttpUtils builder = OkHttpUtils.builder();
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), "{}"))
                .addHeader("Authorization", "Bearer " + accessToken)
                .build();
        
        try (Response response = builder.newCall(request).execute()) {
            String responseBody = response.body().string();
            
            if (!response.isSuccessful()) {
                throw new RuntimeException("获取 VIP 信息失败：" + responseBody);
            }
            
            return JSONObject.parseObject(responseBody);
        } catch (IOException e) {
            throw new RuntimeException("获取 VIP 信息异常：" + e.getMessage(), e);
        }
    }
    
    /**
     * 获取用户权限信息
     * API: GET /oauth/users/scopes
     * @param accessToken 访问令牌
     * @return 用户权限信息 JSON 对象
     */
    public static JSONObject getUserScopes(String accessToken) {
        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host(baseUrl)
                .addPathSegment("oauth")
                .addPathSegment("users")
                .addPathSegment("scopes")
                .build();
        
        OkHttpUtils builder = OkHttpUtils.builder();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Authorization", "Bearer " + accessToken)
                .build();
        
        try (Response response = builder.newCall(request).execute()) {
            String responseBody = response.body().string();
            
            if (!response.isSuccessful()) {
                throw new RuntimeException("获取用户权限失败：" + responseBody);
            }
            
            return JSONObject.parseObject(responseBody);
        } catch (IOException e) {
            throw new RuntimeException("获取用户权限异常：" + e.getMessage(), e);
        }
    }
    
    /**
     * 计算文件的 SHA1 值（用于秒传）
     * @param file 文件对象
     * @return SHA1 值（大写十六进制字符串）
     */
    public static String calculateFileSHA1(File file) throws IOException {
        return calculateFileSHA1(file, -1);
    }
    
    /**
     * 计算文件指定长度的 SHA1 值
     * @param file 文件对象
     * @param length 要读取的字节数，-1 表示读取整个文件
     * @return SHA1 值（大写十六进制字符串）
     */
    public static String calculateFileSHA1(File file, long length) throws IOException {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] buffer = new byte[8192];
            try (FileInputStream is = new FileInputStream(file)) {
                long remaining = length;
                int len;
                while ((len = is.read(buffer)) != -1) {
                    if (remaining != -1) {
                        // 如果指定了长度，只读取指定字节
                        if (remaining <= 0) break;
                        if (len > remaining) {
                            len = (int) remaining;
                        }
                        remaining -= len;
                    }
                    digest.update(buffer, 0, len);
                }
            }
            byte[] sha1Bytes = digest.digest();
            return bytesToHex(sha1Bytes).toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-1 算法不支持", e);
        }
    }
    
    /**
     * 计算文件前 1KB 的 SHA1 值（用于预秒传检查）
     * @param file 文件对象
     * @return 前 1KB 数据的 SHA1 值（大写十六进制字符串）
     */
    public static String calculateFilePreHash(File file) throws IOException {
        if (!file.exists()) {
            throw new IOException("文件不存在：" + file.getAbsolutePath());
        }
        // 如果文件小于 1KB，则计算整个文件
        long readLength = Math.min(1024, file.length());
        return calculateFileSHA1(file, readLength);
    }
    
    /**
     * 字节数组转十六进制字符串
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
    
    /**
     * 文件秒传（通过 content_hash 实现）
     * @param accessToken 访问令牌
     * @param driveId 云盘 ID（通常为个人云盘 ID）
     * @param parentFileId 父文件夹 ID（根目录为 "root"）
     * @param fileName 文件名
     * @param fileSize 文件大小（字节）
     * @param contentHash 文件内容的 SHA1 值（大写）
     * @return 创建结果
     */
    public static JSONObject createFileWithQuickUpload(
            String accessToken,
            String driveId,
            String parentFileId,
            String fileName,
            long fileSize,
            String contentHash) {
        
        // 构建请求的 JSON 数据
        JSONObject requestBody = new JSONObject();
        requestBody.put("drive_id", driveId);
        requestBody.put("parent_file_id", parentFileId);
        requestBody.put("name", fileName);
        requestBody.put("type", "file");
        requestBody.put("size", fileSize);
        requestBody.put("content_hash", contentHash);  // 文件完整 SHA1 值
        requestBody.put("content_hash_name", "sha1");  // 固定为 sha1
        requestBody.put("check_name_mode", "auto_rename");  // 自动重命名同名文件
        
        // 即使秒传成功，也需要返回 part_info_list（虽然不会真正上传）
        JSONObject partInfo = new JSONObject();
        partInfo.put("part_number", 1);
        JSONArray partInfoList = new JSONArray();
        partInfoList.add(partInfo);
        requestBody.put("part_info_list", partInfoList);
        
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                requestBody.toJSONString()
        );

        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host(baseUrl)
                .addPathSegment("adrive")
                .addPathSegment("v1.0")
                .addPathSegment("openFile")
                .addPathSegment("create")
                .build();
        
        OkHttpUtils builder = OkHttpUtils.builder();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Authorization", "Bearer " + accessToken)
                .addHeader("Content-Type", "application/json")
                .build();
        
        try (Response response = builder.newCall(request).execute()) {
            String responseBody = response.body().string();
            JSONObject result = JSONObject.parseObject(responseBody);
            
            if (!response.isSuccessful()) {
                throw new RuntimeException("文件秒传失败：" + responseBody);
            }
            
            return result;
        } catch (IOException e) {
            throw new RuntimeException("文件秒传异常：" + e.getMessage(), e);
        }
    }
    
    /**
     * 文件秒传（便捷方法，从 File 对象自动计算 SHA1，带智能预检查优化）
     * 自动根据文件大小决定是否使用预检查：
     * - 文件 < 10MB：直接计算完整 SHA1（避免二次计算开销）
     * - 文件 >= 10MB：先计算前 1KB pre_hash 探测
     * @param accessToken 访问令牌
     * @param driveId 云盘 ID
     * @param parentFileId 父文件夹 ID
     * @param file 本地文件对象
     * @return 创建结果
     */
    public static JSONObject quickUploadFile(
            String accessToken,
            String driveId,
            String parentFileId,
            File file) throws IOException {
        
        if (!file.exists()) {
            throw new IOException("文件不存在：" + file.getAbsolutePath());
        }
        
        long fileSize = file.length();
        String fileName = file.getName();
        
        // 智能判断：小文件直接计算完整 SHA1，大文件使用预检查
        // 阈值设定为 10MB（30MB 的文件计算两次反而更慢）
        final long SIZE_THRESHOLD = 10 * 1024 * 1024; // 10MB
        
        if (fileSize < SIZE_THRESHOLD) {
            // 小文件：直接计算完整 SHA1（一次完成）
            String contentHash = calculateFileSHA1(file);
            return createFileWithQuickUpload(
                    accessToken,
                    driveId,
                    parentFileId,
                    fileName,
                    fileSize,
                    contentHash
            );
        } else {
            // 大文件：使用预检查优化
            String preHash = calculateFilePreHash(file);
            return quickUploadFileWithPreCheck(
                    accessToken,
                    driveId,
                    parentFileId,
                    fileName,
                    fileSize,
                    preHash,
                    file
            );
        }
    }
    
    /**
     * 文件秒传（带预检查优化）
     * 先通过 pre_hash 检查云端是否有匹配，避免无效的大文件 SHA1 计算
     * @param accessToken 访问令牌
     * @param driveId 云盘 ID
     * @param parentFileId 父文件夹 ID
     * @param fileName 文件名
     * @param fileSize 文件大小
     * @param preHash 文件前 1KB 的 SHA1 值
     * @param file 本地文件对象（用于后续计算完整 SHA1）
     * @return 创建结果
     */
    public static JSONObject quickUploadFileWithPreCheck(
            String accessToken,
            String driveId,
            String parentFileId,
            String fileName,
            long fileSize,
            String preHash,
            File file) throws IOException {
        
        // Step 1: 先用 pre_hash 试探性请求（不传完整 content_hash）
        JSONObject probeRequestBody = new JSONObject();
        probeRequestBody.put("drive_id", driveId);
        probeRequestBody.put("parent_file_id", parentFileId);
        probeRequestBody.put("name", fileName);
        probeRequestBody.put("type", "file");
        probeRequestBody.put("size", fileSize);
        probeRequestBody.put("pre_hash", preHash);  // 只传前 1KB 的 SHA1
        probeRequestBody.put("content_hash_name", "sha1");
        probeRequestBody.put("check_name_mode", "auto_rename");
        
        RequestBody probeBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                probeRequestBody.toJSONString()
        );

        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host(baseUrl)
                .addPathSegment("adrive")
                .addPathSegment("v1.0")
                .addPathSegment("openFile")
                .addPathSegment("create")
                .build();
        
        OkHttpUtils builder = OkHttpUtils.builder();
        Request probeRequest = new Request.Builder()
                .url(url)
                .post(probeBody)
                .addHeader("Authorization", "Bearer " + accessToken)
                .addHeader("Content-Type", "application/json")
                .build();
        
        // Step 2: 发送探测请求
        try (Response probeResponse = builder.newCall(probeRequest).execute()) {
            String probeResponseBody = probeResponse.body().string();
            JSONObject probeResult = JSONObject.parseObject(probeResponseBody);
            
            // Step 3: 检查返回结果
            String uploadId = probeResult.getString("upload_id");
            
            // 如果返回了 uploadId，说明 pre_hash 没有匹配，需要普通上传
            if (uploadId != null && !uploadId.isEmpty()) {
                // pre_hash 不匹配，可以选择：
                // 方案 A: 直接返回失败（最省时间）
                // throw new IOException("预秒传检查失败，文件不存在于云端，无需计算完整 SHA1");
                
                // 方案 B: 继续计算完整 SHA1 尝试秒传（更保险）
                String contentHash = calculateFileSHA1(file);
                return createFileWithQuickUpload(
                        accessToken,
                        driveId,
                        parentFileId,
                        fileName,
                        fileSize,
                        contentHash
                );
            }
            
            // 如果没有返回 uploadId，说明 pre_hash 匹配成功，直接返回成功结果
            return probeResult;
            
        } catch (IOException e) {
            throw new RuntimeException("文件秒传异常：" + e.getMessage(), e);
        }
    }
    
    /**
     * 生成秒传分享信息
     * @param fileName 文件名
     * @param fileSize 文件大小
     * @param contentHash 文件 SHA1 值
     * @param driveId 云盘 ID（可选）
     * @param parentFileId 父文件夹 ID（可选，默认 root）
     * @param musicName 音乐名称（可选）
     * @param musicArtist 音乐艺术家（可选）
     * @param musicAlbum 音乐专辑（可选）
     * @param suffix 文件后缀（可选）
     * @return 秒传分享信息
     */
    public static ShareInfo generateShareInfo(
            String fileName,
            long fileSize,
            String contentHash,
            String driveId,
            String parentFileId,
            String musicName,
            String musicArtist,
            String musicAlbum,
            String suffix) {
        
        ShareInfo shareInfo = new ShareInfo();
        shareInfo.setSha1(contentHash);
        shareInfo.setSize(String.valueOf(fileSize));
        shareInfo.setName(fileName);
        shareInfo.setMusicName(musicName);
        shareInfo.setMusicArtist(musicArtist);
        shareInfo.setMusicAlbum(musicAlbum);
        shareInfo.setSuffix(suffix);
        
        return shareInfo;
    }
    
    /**
     * 生成秒传分享信息（简化版，只需要核心字段）
     * @param fileName 文件名
     * @param fileSize 文件大小
     * @param contentHash 文件 SHA1 值
     * @return 秒传分享 JSON
     */
    public static JSONObject generateSimpleShareInfo(
            String fileName,
            long fileSize,
            String contentHash) {
        
        JSONObject shareInfo = new JSONObject();
        shareInfo.put("content_hash", contentHash);
        shareInfo.put("size", fileSize);
        shareInfo.put("file_name", fileName);
        
        return shareInfo;
    }
    
    /**
     * 从秒传分享信息解析为秒传参数
     * @param shareInfo 秒传分享 JSON
     * @return 解析后的参数对象
     */
    public static JSONObject parseShareInfo(JSONObject shareInfo) {
        JSONObject result = new JSONObject();
        
        String contentHash = shareInfo.getString("content_hash");
        Long size = shareInfo.getLong("size");
        String fileName = shareInfo.getString("file_name");
        
        if (contentHash == null || size == null || fileName == null) {
            throw new IllegalArgumentException("秒传信息缺少必要字段：content_hash, size, file_name");
        }
        
        result.put("content_hash", contentHash);
        result.put("size", size);
        result.put("file_name", fileName);
        result.put("drive_id", shareInfo.getString("drive_id"));
        result.put("parent_file_id", shareInfo.getString("parent_file_id"));
        result.put("check_name_mode", shareInfo.getString("check_name_mode"));
        
        return result;
    }

    /**
     *  检测 access_token 是否有效（获取一次用户信息）
     * @param accessToken
     * @return
     */
    public static Boolean checkAccessToken(String accessToken) {
        JSONObject userInfo = AliyunDriveUtils.getUserInfo(accessToken);
        return userInfo != null;
    }
    
    /**
     * 获取文件列表
     * API: POST /adrive/v1.0/openFile/list
     * @param accessToken 访问令牌
     * @param driveId 云盘 ID
     * @param parentFileId 父文件夹 ID（根目录为 "root"）
     * @param limit 返回文件数量（默认 50，最大 100）
     * @param marker 分页标记
     * @param orderBy 排序字段（created_at | updated_at | name | size | name_enhanced）
     * @param orderDirection 排序方向（DESC | ASC）
     * @param category 分类（video | doc | audio | zip | others | image，可逗号组合）
     * @param type 类型（all | file | folder）
     * @param fields 返回字段（* 表示所有字段，或逗号分隔的字段名）
     * @return 文件列表 JSON 对象
     */
    public static JSONObject getFileList(
            String accessToken,
            String driveId,
            String parentFileId,
            Integer limit,
            String marker,
            String orderBy,
            String orderDirection,
            String category,
            String type,
            String fields) {
        
        // 构建请求的 JSON 数据
        JSONObject requestBody = new JSONObject();
        requestBody.put("drive_id", driveId);
        requestBody.put("parent_file_id", parentFileId);
        
        if (limit != null) {
            requestBody.put("limit", limit);
        }
        if (marker != null && !marker.isEmpty()) {
            requestBody.put("marker", marker);
        }
        if (orderBy != null && !orderBy.isEmpty()) {
            requestBody.put("order_by", orderBy);
        }
        if (orderDirection != null && !orderDirection.isEmpty()) {
            requestBody.put("order_direction", orderDirection);
        }
        if (category != null && !category.isEmpty()) {
            requestBody.put("category", category);
        }
        if (type != null && !type.isEmpty()) {
            requestBody.put("type", type);
        }
        if (fields != null && !fields.isEmpty()) {
            requestBody.put("fields", fields);
        }
        
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                requestBody.toJSONString()
        );

        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host(baseUrl)
                .addPathSegment("adrive")
                .addPathSegment("v1.0")
                .addPathSegment("openFile")
                .addPathSegment("list")
                .build();
        
        OkHttpUtils builder = OkHttpUtils.builder();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Authorization", "Bearer " + accessToken)
                .addHeader("Content-Type", "application/json")
                .build();
        
        try (Response response = builder.newCall(request).execute()) {
            String responseBody = response.body().string();
            
            if (!response.isSuccessful()) {
                log.error("获取文件列表失败：" + responseBody);
                return null;
            }
            
            return JSONObject.parseObject(responseBody);
        } catch (IOException e) {
            log.error("获取文件列表异常：" + e.getMessage(), e);
            return null;
        }
    }
    
    /**
     * 获取文件列表（简化版，只传必填参数）
     * @param accessToken 访问令牌
     * @param driveId 云盘 ID
     * @param parentFileId 父文件夹 ID（根目录为 "root"）
     * @return 文件列表 JSON 对象
     */
    public static JSONObject getFileList(
            String accessToken,
            String driveId,
            String parentFileId) {
        return getFileList(accessToken, driveId, parentFileId, null, null, null, null, null, null, null);
    }
    
    /**
     * 获取文件详情
     * API: POST /adrive/v1.0/openFile/get
     * @param accessToken 访问令牌
     * @param driveId 云盘 ID
     * @param fileId 文件 ID
     * @return 文件详情 JSON 对象
     */
    public static JSONObject getFileDetails(
            String accessToken,
            String driveId,
            String fileId) {
        
        // 构建请求的 JSON 数据
        JSONObject requestBody = new JSONObject();
        requestBody.put("drive_id", driveId);
        requestBody.put("file_id", fileId);
        // 指定返回 id_path 和 name_path 字段
        requestBody.put("fields", "id_path,name_path");
        
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                requestBody.toJSONString()
        );

        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host(baseUrl)
                .addPathSegment("adrive")
                .addPathSegment("v1.0")
                .addPathSegment("openFile")
                .addPathSegment("get")
                .build();
        
        OkHttpUtils builder = OkHttpUtils.builder();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Authorization", "Bearer " + accessToken)
                .addHeader("Content-Type", "application/json")
                .build();
        
        try (Response response = builder.newCall(request).execute()) {
            String responseBody = response.body().string();
            
            if (!response.isSuccessful()) {
                log.error("获取文件详情失败：" + responseBody);
                return null;
            }
            
            return JSONObject.parseObject(responseBody);
        } catch (IOException e) {
            log.error("获取文件详情异常：" + e.getMessage(), e);
            return null;
        }
    }
    
    /**
     * 文件搜索
     * API: POST /adrive/v1.0/openFile/search
     * @param accessToken 访问令牌
     * @param driveId 云盘 ID
     * @param query 查询语句（示例：parent_file_id = 'root' and name = '123'）
     * @param limit 返回文件数量（默认 100，最大 100）
     * @param marker 分页标记
     * @param orderBy 排序字段（created_at | updated_at | name | size + ASC|DESC）
     * @param returnTotalCount 是否返回总数
     * @return 搜索结果 JSON 对象
     */
    public static JSONObject searchFiles(
            String accessToken,
            String driveId,
            String query,
            Integer limit,
            String marker,
            String orderBy,
            Boolean returnTotalCount) {
        
        // 构建请求的 JSON 数据
        JSONObject requestBody = new JSONObject();
        requestBody.put("drive_id", driveId);
        requestBody.put("query", query);
        
        if (limit != null) {
            requestBody.put("limit", limit);
        }
        if (marker != null && !marker.isEmpty()) {
            requestBody.put("marker", marker);
        }
        if (orderBy != null && !orderBy.isEmpty()) {
            requestBody.put("order_by", orderBy);
        }
        if (returnTotalCount != null) {
            requestBody.put("return_total_count", returnTotalCount);
        }
        
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                requestBody.toJSONString()
        );

        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host(baseUrl)
                .addPathSegment("adrive")
                .addPathSegment("v1.0")
                .addPathSegment("openFile")
                .addPathSegment("search")
                .build();
        
        OkHttpUtils builder = OkHttpUtils.builder();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Authorization", "Bearer " + accessToken)
                .addHeader("Content-Type", "application/json")
                .build();
        
        try (Response response = builder.newCall(request).execute()) {
            String responseBody = response.body().string();
            
            if (!response.isSuccessful()) {
                log.error("文件搜索失败：" + responseBody);
                return null;
            }
            
            return JSONObject.parseObject(responseBody);
        } catch (IOException e) {
            log.error("文件搜索异常：" + e.getMessage(), e);
            return null;
        }
    }
    
    /**
     * 文件搜索（简化版，只传必填参数）
     * @param accessToken 访问令牌
     * @param driveId 云盘 ID
     * @param query 查询语句
     * @return 搜索结果 JSON 对象
     */
    public static JSONObject searchFiles(
            String accessToken,
            String driveId,
            String query) {
        return searchFiles(accessToken, driveId, query, null, null, null, true);
    }
    
    /**
     * 创建文件夹
     * API: POST /adrive/v1.0/openFile/create
     * @param accessToken 访问令牌
     * @param driveId 云盘 ID
     * @param parentFileId 父文件夹 ID（根目录为 "root"）
     * @param folderName 文件夹名称
     * @param checkNameMode 同名处理策略（auto_rename/refuse/ignore）
     * @return 创建结果
     */
    public static JSONObject createFolder(
            String accessToken,
            String driveId,
            String parentFileId,
            String folderName,
            String checkNameMode) {
        
        // 构建请求的 JSON 数据
        JSONObject requestBody = new JSONObject();
        requestBody.put("drive_id", driveId);
        requestBody.put("parent_file_id", parentFileId);
        requestBody.put("name", folderName);
        requestBody.put("type", "folder");
        requestBody.put("check_name_mode", checkNameMode != null ? checkNameMode : "auto_rename");
        
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                requestBody.toJSONString()
        );

        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host(baseUrl)
                .addPathSegment("adrive")
                .addPathSegment("v1.0")
                .addPathSegment("openFile")
                .addPathSegment("create")
                .build();
        
        OkHttpUtils builder = OkHttpUtils.builder();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Authorization", "Bearer " + accessToken)
                .addHeader("Content-Type", "application/json")
                .build();
        
        try (Response response = builder.newCall(request).execute()) {
            String responseBody = response.body().string();
            JSONObject result = JSONObject.parseObject(responseBody);
            
            if (!response.isSuccessful()) {
                log.error("创建文件夹失败：" + responseBody);
                return null;
            }
            
            return result;
        } catch (IOException e) {
            log.error("创建文件夹异常：" + e.getMessage(), e);
            return null;
        }
    }
    
    /**
     * 创建文件夹（简化版，默认 auto_rename）
     * @param accessToken 访问令牌
     * @param driveId 云盘 ID
     * @param parentFileId 父文件夹 ID
     * @param folderName 文件夹名称
     * @return 创建结果
     */
    public static JSONObject createFolder(
            String accessToken,
            String driveId,
            String parentFileId,
            String folderName) {
        return createFolder(accessToken, driveId, parentFileId, folderName, "refuse");
    }
}
