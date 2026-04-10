package com.sqmusicplus.v3.utils;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.sqmusicplus.v3.alidrive.entity.AliUploadResult;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 阿里云盘文件上传工具类
 */
@Slf4j
public class AliyunUploadUtils {
    
    /**
     * 基础 URL
     */
    private static final String baseUrl = "openapi.alipan.com";
    
    /**
     * 计算 proof_code（用于秒传）
     * @param accessToken 访问令牌
     * @param fileSize 文件大小
     * @return proof_code
     */
    public static String generateProofCode(String accessToken, long fileSize) throws IOException {
        if (fileSize == 0) {
            return "";
        }
        
        // 1. 获取 access_token 的 MD5
        String tokenMd5 = md5(accessToken);
        
        // 2. 截取前 16 位
        String tmpStr = tokenMd5.substring(0, 16);
        
        // 3. 转换成 unsigned int64
        long tmpInt = new java.math.BigInteger(tmpStr, 16).longValue();
        
        // 4. 对文件大小取模
        long index = Math.abs(tmpInt % fileSize);
        
        // 5. 计算 range: [index, index+8)
        long start = index;
        long end = Math.min(index + 8, fileSize);
        
        // 6. 读取文件指定范围的数据并计算 base64
        // 注意：这里需要传入文件对象，但为了接口简洁，先返回空
        // 实际使用时需要在 uploadFile 方法中实现
        return "";
    }
    
    /**
     * 计算 proof_code（带文件对象）
     * @param accessToken 访问令牌
     * @param file 文件对象
     * @return proof_code
     */
    public static String generateProofCode(String accessToken, File file) throws IOException {
        long fileSize = file.length();
        if (fileSize == 0) {
            return "";
        }
        
        // 1. 获取 access_token 的 MD5
        String tokenMd5 = md5(accessToken);
        
        // 2. 截取前 16 位
        String tmpStr = tokenMd5.substring(0, 16);
        
        // 3. 转换成 unsigned int64（使用 BigInteger 避免溢出）
        java.math.BigInteger tmpBigInt = new java.math.BigInteger(tmpStr, 16);
        
        // 4. 对文件大小取模（确保结果为非负数）
        long index = tmpBigInt.mod(java.math.BigInteger.valueOf(fileSize)).longValue();
        
        // 5. 计算 range: [index, index+8)，确保不超出文件大小
        long start = index;
        long end = Math.min(index + 8, fileSize);
        
        // 6. 读取文件指定范围的数据
        try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
            byte[] buffer = new byte[(int)(end - start)];
            raf.seek(start);
            raf.read(buffer);
            
            // 7. 计算 base64
            return java.util.Base64.getEncoder().encodeToString(buffer);
        }
    }
    
    /**
     * 创建文件（支持秒传和普通上传）
     * API: POST /adrive/v1.0/openFile/create
     * @param accessToken 访问令牌
     * @param driveId 云盘 ID
     * @param parentFileId 父文件夹 ID（根目录为 "root"）
     * @param fileName 文件名
     * @param fileSize 文件大小
     * @param contentHash 文件内容 SHA1 值（秒传时必填）
     * @param preHash 文件前 1KB SHA1 值（大文件预检查时使用）
     * @param proofCode proof_code（秒传时必填）
     * @param checkNameMode 同名处理策略（auto_rename/refuse/ignore）
     * @return 创建结果
     */
    public static JSONObject createFile(
            String accessToken,
            String driveId,
            String parentFileId,
            String fileName,
            long fileSize,
            String contentHash,
            String preHash,
            String proofCode,
            String checkNameMode) {
        
        // 构建请求的 JSON 数据
        JSONObject requestBody = new JSONObject();
        requestBody.put("drive_id", driveId);
        requestBody.put("parent_file_id", parentFileId);
        requestBody.put("name", fileName);
        requestBody.put("type", "file");
        requestBody.put("check_name_mode", checkNameMode != null ? checkNameMode : "refuse");
        
        // 秒传相关参数
        if (contentHash != null && !contentHash.isEmpty()) {
            requestBody.put("content_hash", contentHash);
            requestBody.put("content_hash_name", "sha1");
            requestBody.put("size", fileSize);
        }
        
        // pre_hash 用于大文件预检查
        if (preHash != null && !preHash.isEmpty()) {
            requestBody.put("pre_hash", preHash);
        }
        
        // proof_code 用于秒传验证
        if (proofCode != null && !proofCode.isEmpty()) {
            requestBody.put("proof_code", proofCode);
            requestBody.put("proof_version", "v1");
        }
        
        // 构建 part_info_list（即使秒传成功也需要返回）
        JSONArray partInfoList = new JSONArray();
        if (contentHash == null || contentHash.isEmpty()) {
            // 普通上传模式，需要分片信息
            // 默认分片大小 10MB，最大 5GB
            long partSize = 10 * 1024 * 1024; // 10MB
            int partCount = (int)Math.ceil((double)fileSize / partSize);
            partCount = Math.max(partCount, 1); // 至少一个分片
            
            for (int i = 0; i < partCount; i++) {
                JSONObject partInfo = new JSONObject();
                partInfo.put("part_number", i + 1);
                partInfoList.add(partInfo);
            }
        } else {
            // 秒传模式，只需要一个分片（不会真正上传）
            JSONObject partInfo = new JSONObject();
            partInfo.put("part_number", 1);
            partInfoList.add(partInfo);
        }
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
                // 检查是否是 pre_hash 匹配成功（需要继续秒传）
                if (response.code() == 409) {
                    JSONObject errorBody = result.getJSONObject("error");
                    if (errorBody != null && "PreHashMatched".equals(errorBody.getString("code"))) {
                        log.info("pre_hash 匹配成功，可以继续秒传");
                        return result;
                    }
                }
                log.error("创建文件失败：" + responseBody);
                return null;
            }
            
            return result;
        } catch (IOException e) {
            log.error("创建文件异常：" + e.getMessage(), e);
            return null;
        }
    }
    
    /**
     * 创建文件（简化版，自动计算所需参数）
     * @param accessToken 访问令牌
     * @param driveId 云盘 ID
     * @param parentFileId 父文件夹 ID
     * @param file 本地文件对象
     * @param enableQuickUpload 是否启用秒传
     * @return 创建结果
     */
    public static JSONObject createFile(
            String accessToken,
            String driveId,
            String parentFileId,
            File file,
            boolean enableQuickUpload) throws IOException {
        
        if (!file.exists()) {
            throw new IOException("文件不存在：" + file.getAbsolutePath());
        }
        
        long fileSize = file.length();
        String fileName = file.getName();
        
        if (enableQuickUpload) {
            // 智能判断：小文件直接计算完整 SHA1，大文件使用预检查
            final long SIZE_THRESHOLD = 10 * 1024 * 1024; // 10MB
            
            if (fileSize < SIZE_THRESHOLD) {
                // 小文件：直接计算完整 SHA1
                String contentHash = AliyunDriveUtils.calculateFileSHA1(file);
                String proofCode = generateProofCode(accessToken, file);
                return createFile(
                        accessToken,
                        driveId,
                        parentFileId,
                        fileName,
                        fileSize,
                        contentHash,
                        null,
                        proofCode,
                        "refuse"
                );
            } else {
                // 大文件：先计算 pre_hash 预检查
                String preHash = AliyunDriveUtils.calculateFilePreHash(file);
                return createFile(
                        accessToken,
                        driveId,
                        parentFileId,
                        fileName,
                        fileSize,
                        null,
                        preHash,
                        null,
                        "refuse"
                );
            }
        } else {
            // 不启用秒传，直接普通上传
            return createFile(
                    accessToken,
                    driveId,
                    parentFileId,
                    fileName,
                    fileSize,
                    null,
                    null,
                    null,
                    "refuse"
            );
        }
    }
    
    /**
     * 刷新获取上传地址
     * API: POST /adrive/v1.0/openFile/getUploadUrl
     * @param accessToken 访问令牌
     * @param driveId 云盘 ID
     * @param fileId 文件 ID
     * @param uploadId 上传 ID
     * @param partNumbers 分片编号列表
     * @return 上传地址信息
     */
    public static JSONObject getUploadUrl(
            String accessToken,
            String driveId,
            String fileId,
            String uploadId,
            java.util.List<Integer> partNumbers) {
        
        // 构建请求的 JSON 数据
        JSONObject requestBody = new JSONObject();
        requestBody.put("drive_id", driveId);
        requestBody.put("file_id", fileId);
        requestBody.put("upload_id", uploadId);
        
        JSONArray partInfoList = new JSONArray();
        for (Integer partNumber : partNumbers) {
            JSONObject partInfo = new JSONObject();
            partInfo.put("part_number", partNumber);
            partInfoList.add(partInfo);
        }
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
                .addPathSegment("getUploadUrl")
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
                log.error("刷新上传地址失败：" + responseBody);
                return null;
            }
            
            return JSONObject.parseObject(responseBody);
        } catch (IOException e) {
            log.error("刷新上传地址异常：" + e.getMessage(), e);
            return null;
        }
    }
    
    /**
     * 列举已上传分片
     * API: POST /adrive/v1.0/openFile/listUploadedParts
     * @param accessToken 访问令牌
     * @param driveId 云盘 ID
     * @param fileId 文件 ID
     * @param uploadId 上传 ID
     * @param partNumberMarker 分页标记
     * @return 已上传分片信息
     */
    public static JSONObject listUploadedParts(
            String accessToken,
            String driveId,
            String fileId,
            String uploadId,
            String partNumberMarker) {
        
        // 构建请求的 JSON 数据
        JSONObject requestBody = new JSONObject();
        requestBody.put("drive_id", driveId);
        requestBody.put("file_id", fileId);
        requestBody.put("upload_id", uploadId);
        
        if (partNumberMarker != null && !partNumberMarker.isEmpty()) {
            requestBody.put("part_number_marker", partNumberMarker);
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
                .addPathSegment("listUploadedParts")
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
                log.error("列举已上传分片失败：" + responseBody);
                return null;
            }
            
            return JSONObject.parseObject(responseBody);
        } catch (IOException e) {
            log.error("列举已上传分片异常：" + e.getMessage(), e);
            return null;
        }
    }
    
    /**
     * 标记文件上传完毕
     * API: POST /adrive/v1.0/openFile/complete
     * @param accessToken 访问令牌
     * @param driveId 云盘 ID
     * @param fileId 文件 ID
     * @param uploadId 上传 ID
     * @return 完成结果
     */
    public static JSONObject completeFileUpload(
            String accessToken,
            String driveId,
            String fileId,
            String uploadId) {
        
        // 构建请求的 JSON 数据
        JSONObject requestBody = new JSONObject();
        requestBody.put("drive_id", driveId);
        requestBody.put("file_id", fileId);
        requestBody.put("upload_id", uploadId);
        
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
                .addPathSegment("complete")
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
                log.error("标记文件上传完毕失败：" + responseBody);
                return null;
            }
            
            return JSONObject.parseObject(responseBody);
        } catch (IOException e) {
            log.error("标记文件上传完毕异常：" + e.getMessage(), e);
            return null;
        }
    }
    
    /**
     * 上传单个分片（PUT 请求）
     * @param uploadUrl 上传地址
     * @param fileData 分片数据
     * @return 上传结果
     */
    public static boolean uploadPart(String uploadUrl, byte[] fileData) throws IOException {
        // 注意：不能使用 MediaType，否则 OkHttp 会自动添加 Content-Type 头，导致签名不匹配
        // 阿里云 OSS 预签名 URL 要求 Content-Type 为空
        RequestBody body = RequestBody.create(fileData);
        
        Request request = new Request.Builder()
                .url(uploadUrl)
                .put(body)
                .build();
        
        OkHttpUtils builder = OkHttpUtils.builder(false);
        try (Response response = builder.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String errorBody = "";
                if (response.body() != null) {
                    try {
                        errorBody = response.body().string();
                    } catch (Exception e) {
                        errorBody = "无法读取响应体: " + e.getMessage();
                    }
                }
                log.error("上传分片失败 - URL: {}, 状态码: {}, 响应: {}", 
                    uploadUrl, response.code(), errorBody);
                return false;
            }
            return true;
        }
    }
    
    /**
     * 完整文件上传（自动判断秒传或普通上传）- 内部方法
     * @param accessToken 访问令牌
     * @param driveId 云盘 ID
     * @param parentFileId 父文件夹 ID
     * @param file 本地文件对象
     * @param enableQuickUpload 是否启用秒传
     * @return 上传结果（可能缺少部分字段，需要调用 enrichWithFileDetails 补充）
     */
    private static AliUploadResult doUploadFile(
            String accessToken,
            String driveId,
            String parentFileId,
            File file,
            boolean enableQuickUpload) throws IOException {
        
        if (!file.exists()) {
            throw new IOException("文件不存在：" + file.getAbsolutePath());
        }
        
        // Step 1: 创建文件
        log.info("开始创建文件：{}", file.getName());
        JSONObject createResult = createFile(accessToken, driveId, parentFileId, file, enableQuickUpload);
        
        if (createResult == null) {
            throw new RuntimeException("创建文件失败");
        }
        
        // 检查是否秒传成功
        Boolean rapidUpload = createResult.getBoolean("rapid_upload");
        if (rapidUpload != null && rapidUpload) {
            log.info("✅ 秒传成功！文件 ID: {}", createResult.getString("file_id"));
            return buildUploadResult(createResult, file.getAbsolutePath());
        }
        
        // Step 2: 普通上传流程
        String fileId = createResult.getString("file_id");
        String uploadId = createResult.getString("upload_id");
        JSONArray partInfoList = createResult.getJSONArray("part_info_list");
        
        // 检查是否需要普通上传（某些情况下秒传失败会返回 upload_id 和 part_info_list）
        if (uploadId == null || partInfoList == null || partInfoList.isEmpty()) {
            log.warn("创建文件返回数据：{}", createResult.toJSONString());
            
            // 检查文件是否已存在
            Boolean exist = createResult.getBoolean("exist");
            if (exist != null && exist) {
                log.info("✅ 文件已存在，跳过上传 - 文件ID: {}, 文件名: {}", 
                    createResult.getString("file_id"), file.getName());
                // 文件已存在也算作“秒传”，设置 rapid_upload 为 true
                createResult.put("rapid_upload", true);
                return buildUploadResult(createResult, file.getAbsolutePath());
            }
            
            // 尝试从其他可能的字段获取
            if (uploadId == null) {
                uploadId = createResult.getString("uploadId");
            }
            if (partInfoList == null || partInfoList.isEmpty()) {
                partInfoList = createResult.getJSONArray("partInfoList");
            }
            
            // 如果仍然为空，说明可能是预检查阶段（pre_hash）或秒传失败，需要重新创建文件
            if (uploadId == null || partInfoList == null || partInfoList.isEmpty()) {
                log.info("缺少 upload_id 或 part_info_list，尝试使用完整 SHA1 重新创建文件");
                
                // 计算完整文件的 SHA1
                String contentHash = AliyunDriveUtils.calculateFileSHA1(file);
                String proofCode = generateProofCode(accessToken, file);
                
                log.info("重新创建文件参数 - fileName: {}, fileSize: {}, contentHash: {}, proofCode: {}", 
                    file.getName(), file.length(), contentHash, proofCode);
                
                // 直接调用底层的 createFile 方法，带上完整的 content_hash 和 proof_code
                createResult = createFile(
                        accessToken,
                        driveId,
                        parentFileId,
                        file.getName(),
                        file.length(),
                        contentHash,
                        null,  // preHash 不需要
                        proofCode,
                        "refuse"
                );
                
                log.info("重新创建文件响应：{}", createResult != null ? createResult.toJSONString() : "null");
                
                if (createResult == null) {
                    throw new RuntimeException("重新创建文件失败，响应为 null");
                }
                
                // 再次检查秒传是否成功
                Boolean secondRapidUpload = createResult.getBoolean("rapid_upload");
                if (secondRapidUpload != null && secondRapidUpload) {
                    log.info("✅ 第二次尝试秒传成功！文件 ID: {}", createResult.getString("file_id"));
                    return buildUploadResult(createResult, file.getAbsolutePath());
                }
                
                // 获取新的 upload_id 和 part_info_list
                fileId = createResult.getString("file_id");
                uploadId = createResult.getString("upload_id");
                partInfoList = createResult.getJSONArray("part_info_list");
                
                log.info("重新创建后获取的参数 - fileId: {}, uploadId: {}, partInfoList size: {}", 
                    fileId, uploadId, partInfoList != null ? partInfoList.size() : 0);
                
                if (uploadId == null || partInfoList == null || partInfoList.isEmpty()) {
                    // 检查文件是否已存在
                    Boolean secondExist = createResult.getBoolean("exist");
                    if (secondExist != null && secondExist) {
                        log.warn("⚠️ 文件已存在，跳过上传 - 文件ID: {}, 文件名: {}", 
                            createResult.getString("file_id"), file.getName());
                        // 文件已存在也算作“秒传”，设置 rapid_upload 为 true
                        createResult.put("rapid_upload", true);
                        // 返回已存在文件的信息，标记为秒传（实际上不需要上传）
                        return buildUploadResult(createResult, file.getAbsolutePath());
                    }
                    
                    log.error("重新创建文件后仍然缺少必要参数，响应数据：{}", createResult.toJSONString());
                    throw new RuntimeException("创建文件返回异常，缺少 upload_id 或 part_info_list。请检查：1) access_token 是否有效 2) drive_id 是否正确 3) 文件大小是否为0");
                }
            }
        }
        
        log.info("开始上传文件：{}, 文件 ID: {}, Upload ID: {}", file.getName(), fileId, uploadId);
        
        // Step 3: 分片上传
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] buffer = new byte[10 * 1024 * 1024]; // 10MB 分片
            int partNumber = 0;
            int bytesRead;
            
            while ((bytesRead = fis.read(buffer)) != -1) {
                partNumber++;
                
                // 获取上传地址（如果 part_info_list 中没有或者已过期）
                String uploadUrl = null;
                if (partNumber <= partInfoList.size()) {
                    JSONObject partInfo = partInfoList.getJSONObject(partNumber - 1);
                    uploadUrl = partInfo.getString("upload_url");
                }
                
                if (uploadUrl == null) {
                    // 刷新获取上传地址
                    java.util.List<Integer> partNumbers = new java.util.ArrayList<>();
                    partNumbers.add(partNumber);
                    JSONObject urlResult = getUploadUrl(accessToken, driveId, fileId, uploadId, partNumbers);
                    if (urlResult != null && urlResult.containsKey("part_info_list")) {
                        JSONArray newPartInfoList = urlResult.getJSONArray("part_info_list");
                        if (newPartInfoList != null && !newPartInfoList.isEmpty()) {
                            uploadUrl = newPartInfoList.getJSONObject(0).getString("upload_url");
                        }
                    }
                }
                
                if (uploadUrl == null) {
                    throw new RuntimeException("无法获取上传地址");
                }
                
                // 上传分片
                byte[] partData = (bytesRead == buffer.length) ? buffer : java.util.Arrays.copyOf(buffer, bytesRead);
                boolean uploadSuccess = uploadPart(uploadUrl, partData);
                
                if (!uploadSuccess) {
                    log.error("❌ 上传分片 {} 失败，文件大小: {} bytes, 分片大小: {} bytes", 
                        partNumber, file.length(), partData.length);
                    throw new RuntimeException("上传分片 " + partNumber + " 失败，请查看日志获取详细错误信息");
                }
                
                log.info("上传分片 {}/{} 成功", partNumber, (int)Math.ceil((double)file.length() / buffer.length));
            }
        }
        
        // Step 4: 标记上传完毕
        log.info("标记文件上传完毕：{}", file.getName());
        JSONObject completeResult = completeFileUpload(accessToken, driveId, fileId, uploadId);
        
        if (completeResult == null) {
            throw new RuntimeException("标记文件上传完毕失败");
        }
        
        log.info("✅ 文件上传成功！文件 ID: {}", fileId);
        return buildUploadResult(completeResult, file.getAbsolutePath());
    }
    
    /**
     * 完整文件上传（简化版，默认启用秒传）
     * @param accessToken 访问令牌
     * @param driveId 云盘 ID
     * @param parentFileId 父文件夹 ID
     * @param file 本地文件对象
     * @return 上传结果
     */
    public static AliUploadResult uploadFile(
            String accessToken,
            String driveId,
            String parentFileId,
            File file) throws IOException {
        return uploadFile(accessToken, driveId, parentFileId, file, true);
    }
    
    /**
     * 完整文件上传（公共接口，自动补充文件详情）
     * @param accessToken 访问令牌
     * @param driveId 云盘 ID
     * @param parentFileId 父文件夹 ID
     * @param file 本地文件对象
     * @param enableQuickUpload 是否启用秒传
     * @return 上传结果（包含完整信息）
     */
    public static AliUploadResult uploadFile(
            String accessToken,
            String driveId,
            String parentFileId,
            File file,
            boolean enableQuickUpload) throws IOException {
        
        // 调用内部上传方法
        AliUploadResult result = doUploadFile(accessToken, driveId, parentFileId, file, enableQuickUpload);
        
        // 上传成功后，获取文件详情以补充完整信息
        if (result != null && result.getFileId() != null) {
            log.debug("上传成功，获取文件详情补充信息 - fileId: {}", result.getFileId());
            JSONObject enrichedData = enrichWithFileDetails(
                accessToken, 
                driveId, 
                result.getFileId(), 
                result.getRawData() != null ? result.getRawData() : new JSONObject()
            );
            return buildUploadResult(enrichedData, file.getAbsolutePath());
        }
        
        return result;
    }
    
    /**
     * 通过 getFileDetails API 补充文件详情
     * @param accessToken 访问令牌
     * @param driveId 云盘 ID
     * @param fileId 文件 ID
     * @param originalResult 原始上传结果
     * @return 补充后的文件详情
     */
    private static JSONObject enrichWithFileDetails(String accessToken, String driveId, String fileId, JSONObject originalResult) {
        if (fileId == null || fileId.isEmpty()) {
            log.warn("fileId 为空，跳过获取文件详情");
            return originalResult;
        }
        
        try {
            log.debug("获取文件详情 - fileId: {}, driveId: {}", fileId, driveId);
            JSONObject fileDetails = AliyunDriveUtils.getFileDetails(accessToken, driveId, fileId);
            
            if (fileDetails != null) {
                log.info("成功获取文件详情 - content_hash: {}, id_path: {}, name_path: {}",
                    fileDetails.getString("content_hash"),
                    fileDetails.getString("id_path"),
                    fileDetails.getString("name_path"));
                
                // 合并原始结果和文件详情
                // 优先使用文件详情的字段
                for (String key : fileDetails.keySet()) {
                    if (!originalResult.containsKey(key) || originalResult.get(key) == null) {
                        originalResult.put(key, fileDetails.get(key));
                    }
                }
                
                return originalResult;
            } else {
                log.warn("获取文件详情失败，使用原始上传结果");
                return originalResult;
            }
        } catch (Exception e) {
            log.error("获取文件详情异常，使用原始上传结果: {}", e.getMessage());
            return originalResult;
        }
    }
    
    /**
     * 构建上传结果对象
     * @param jsonData 原始 JSON 数据
     * @param localPath 本地文件路径
     * @return 上传结果实体
     */
    private static AliUploadResult buildUploadResult(JSONObject jsonData, String localPath) {
        // 直接从 API 响应中获取 SHA1，不做任何额外计算
        String sha1 = jsonData.getString("content_hash");
        
        if (sha1 == null || sha1.isEmpty()) {
            log.warn("API 响应中缺少 content_hash - fileId: {}, fileName: {}, rapidUpload: {}, exist: {}", 
                jsonData.getString("file_id"), 
                jsonData.getString("file_name"),
                jsonData.getBoolean("rapid_upload"),
                jsonData.getBoolean("exist"));
        }
        
        // 合并创建文件和上传完毕的返回数据
        return AliUploadResult.builder()
                .fileId(jsonData.getString("file_id"))
                .uploadId(jsonData.getString("upload_id"))
                .fileName(jsonData.getString("name") != null ? jsonData.getString("name") : jsonData.getString("file_name"))
                .fileSize(jsonData.getLong("size"))
                .fileType(jsonData.getString("type"))
                .driveId(jsonData.getString("drive_id"))
                .parentFileId(jsonData.getString("parent_file_id"))
                .createdAt(jsonData.getString("created_at"))
                .updatedAt(jsonData.getString("updated_at"))
                .sha1(sha1)  // 直接使用 API 返回的 SHA1（可能为空）
                .idPath(jsonData.getString("id_path"))  // ID 路径
                .namePath(jsonData.getString("name_path"))  // 名称路径
                .fileExtension(jsonData.getString("file_extension"))
                .category(jsonData.getString("category"))
                .thumbnail(jsonData.getString("thumbnail"))
                .url(jsonData.getString("url"))
                .downloadUrl(jsonData.getString("download_url"))
                .localPath(localPath)
                .rapidUpload(jsonData.getBoolean("rapid_upload"))
                .rawData(jsonData)
                .build();
    }
    
    /**
     * 计算 MD5
     * @param input 输入字符串
     * @return MD5 值（小写十六进制）
     */
    private static String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 算法不支持", e);
        }
    }
}
