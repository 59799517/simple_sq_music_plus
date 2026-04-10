package com.sqmusicplus;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.sqmusicplus.v3.alidrive.entity.AliUploadResult;
import com.sqmusicplus.v3.utils.AliyunDriveUtils;
import com.sqmusicplus.v3.utils.AliyunUploadUtils;

import java.io.File;
import java.io.IOException;

/**
 * 阿里云盘文件上传功能测试
 */
public class AliyunUploadTest {
    public static void main(String[] args) {
        try {
            // ========== 初始化配置 ==========
            String accessToken = "eyJraWQiOiJLcU8iLCJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI2ODdkNTQzNGNlMTk0Y2YzYmJjNDRhMmNhNTM2YWM3ZiIsImF1ZCI6IjQxYWRhNTU3ZmQ4MjQ4ODlhMmI2M2FiZjFhZjYzOTg2IiwicyI6ImNkYSIsImQiOiIxNzg2MDYsODkyMjQ2MjgyIiwiaXNzIjoiYWxpcGFuIiwiZXhwIjoxNzc3MjcxODMzLCJpYXQiOjE3NzQ2Nzk4MzAsImp0aSI6ImNlM2Q1MjhmMjIwZjQ0YmY4M2I2YWQ4YTdmOGU0ZjI0In0.yAU29z8_7i6kBP_Y8r6SukujHBvYdw8xPxTMxJImkOQ";
            
            System.out.println("Access Token: " + accessToken);
            
            // 获取 Drive ID
            JSONObject driveInfo = AliyunDriveUtils.getDriveInfo(accessToken);
            String defaultDriveId = driveInfo.getString("default_drive_id");
            System.out.println("默认云盘 ID: " + defaultDriveId);
            
            // ========== 步骤 1: proof_code 计算测试 ==========
            System.out.println("\n=== 测试 proof_code 计算 ===");
            File testFile = new File("/music/test.mp3");
            if (testFile.exists()) {
                try {
                    String proofCode = AliyunUploadUtils.generateProofCode(accessToken, testFile);
                    System.out.println("proof_code: " + proofCode);
                    System.out.println("✅ proof_code 计算成功");
                } catch (IOException e) {
                    System.out.println("❌ proof_code 计算失败：" + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                System.out.println("⚠️ 测试文件不存在，跳过 proof_code 计算测试");
            }
            
            // ========== 步骤 2: 小文件秒传测试（< 10MB）==========
            System.out.println("\n=== 测试小文件秒传 ===");
            File smallFile = new File("/music/小文件测试.txt");
            if (smallFile.exists()) {
                try {
                    System.out.println("文件大小：" + formatFileSize(smallFile.length()));
                    System.out.println("开始上传...");
                    
                    AliUploadResult uploadResult = AliyunUploadUtils.uploadFile(
                            accessToken,
                            defaultDriveId,
                            "root",
                            smallFile,
                            true  // 启用秒传
                    );
                    
                    if (uploadResult != null) {
                        Boolean rapidUpload = uploadResult.getRapidUpload();
                        if (rapidUpload != null && rapidUpload) {
                            System.out.println("✅ 小文件秒传成功！");
                            System.out.println("文件 ID: " + uploadResult.getFileId());
                            System.out.println("文件名：" + uploadResult.getFileName());
                            System.out.println("文件大小：" + formatFileSize(uploadResult.getFileSize()));
                            System.out.println("文件 Hash: " + uploadResult.getSha1());
                        } else {
                            System.out.println("⚠️ 小文件需要普通上传");
                            System.out.println("Upload ID: " + uploadResult.getUploadId());
                        }
                    } else {
                        System.out.println("❌ 小文件上传失败");
                    }
                } catch (Exception e) {
                    System.out.println("❌ 小文件上传异常：" + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                System.out.println("⚠️ 测试文件不存在，跳过小文件秒传测试");
            }
            
            // ========== 步骤 3: 文件创建测试（不上传）==========
            System.out.println("\n=== 测试文件创建 ===");
            try {
                JSONObject createResult = AliyunUploadUtils.createFile(
                        accessToken,
                        defaultDriveId,
                        "root",
                        "测试文件_未上传.txt",
                        1024,  // 1KB
                        null,  // content_hash
                        null,  // pre_hash
                        null,  // proof_code
                        "auto_rename"
                );
                
                if (createResult != null) {
                    System.out.println("✅ 文件创建成功！");
                    System.out.println("文件 ID: " + createResult.getString("file_id"));
                    System.out.println("Upload ID: " + createResult.getString("upload_id"));
                    System.out.println("文件名：" + createResult.getString("name"));
                    
                    // 检查是否返回了上传地址
                    JSONArray partInfoList = createResult.getJSONArray("part_info_list");
                    if (partInfoList != null && !partInfoList.isEmpty()) {
                        System.out.println("分片数量：" + partInfoList.size());
                        for (int i = 0; i < partInfoList.size(); i++) {
                            JSONObject partInfo = partInfoList.getJSONObject(i);
                            System.out.println("  分片 " + (i+1) + ": 编号=" + partInfo.getInteger("part_number"));
                        }
                    }
                } else {
                    System.out.println("❌ 文件创建失败");
                }
            } catch (Exception e) {
                System.out.println("❌ 文件创建异常：" + e.getMessage());
                e.printStackTrace();
            }
            
            // ========== 步骤 3.5: 创建文件夹测试 ==========
            System.out.println("\n=== 测试创建文件夹 ===");
            try {
                // 在根目录创建文件夹
                JSONObject folderResult = AliyunDriveUtils.createFolder(
                        accessToken,
                        defaultDriveId,
                        "root",
                        "测试文件夹"
                );
                
                if (folderResult != null) {
                    System.out.println("✅ 文件夹创建成功！");
                    System.out.println("文件夹 ID: " + folderResult.getString("file_id"));
                    System.out.println("文件夹名称：" + folderResult.getString("name"));
                    System.out.println("父文件夹 ID: " + folderResult.getString("parent_file_id"));
                    System.out.println("创建时间：" + formatTimestamp(folderResult.getLongValue("created_at")));
                    
                    // 在刚创建的文件夹中再创建一个子文件夹
                    String parentFolderId = folderResult.getString("file_id");
                    JSONObject subFolderResult = AliyunDriveUtils.createFolder(
                            accessToken,
                            defaultDriveId,
                            parentFolderId,
                            "子文件夹测试"
                    );
                    
                    if (subFolderResult != null) {
                        System.out.println("\n✅ 子文件夹创建成功！");
                        System.out.println("子文件夹 ID: " + subFolderResult.getString("file_id"));
                        System.out.println("子文件夹名称：" + subFolderResult.getString("name"));
                        System.out.println("父文件夹 ID: " + subFolderResult.getString("parent_file_id"));
                    }
                } else {
                    System.out.println("❌ 文件夹创建失败");
                }
            } catch (Exception e) {
                System.out.println("❌ 文件夹创建异常：" + e.getMessage());
                e.printStackTrace();
            }
            
            // ========== 步骤 4: 大文件预检查测试（使用 pre_hash）==========
            System.out.println("\n=== 测试大文件预检查（pre_hash）===");
            File largeFile = new File("/music/大文件测试.iso");
            if (largeFile.exists() && largeFile.length() > 10 * 1024 * 1024) {
                try {
                    System.out.println("文件大小：" + formatFileSize(largeFile.length()));
                    System.out.println("正在计算 pre_hash...");
                    
                    long startTime = System.currentTimeMillis();
                    String preHash = AliyunDriveUtils.calculateFilePreHash(largeFile);
                    long endTime = System.currentTimeMillis();
                    
                    System.out.println("pre_hash: " + preHash);
                    System.out.println("计算耗时：" + (endTime - startTime) + "ms");
                    
                    // 创建文件（使用 pre_hash）
                    System.out.println("正在创建文件...");
                    JSONObject createWithPreHash = AliyunUploadUtils.createFile(
                            accessToken,
                            defaultDriveId,
                            "root",
                            largeFile.getName(),
                            largeFile.length(),
                            null,  // content_hash（暂不计算）
                            preHash,  // pre_hash
                            null,  // proof_code
                            "auto_rename"
                    );
                    
                    if (createWithPreHash != null) {
                        // 检查是否是 PreHashMatched
                        Boolean rapidUpload = createWithPreHash.getBoolean("rapid_upload");
                        if (rapidUpload != null && rapidUpload) {
                            System.out.println("✅ pre_hash 匹配成功，秒传成功！");
                            System.out.println("文件 ID: " + createWithPreHash.getString("file_id"));
                            System.out.println("文件名：" + createWithPreHash.getString("name"));
                            System.out.println("文件大小：" + formatFileSize(createWithPreHash.getLongValue("size")));
                        } else {
                            System.out.println("ℹ️ pre_hash 未匹配或需要进一步验证");
                            System.out.println("需要计算完整 SHA1 或进行普通上传");
                            System.out.println("Upload ID: " + createWithPreHash.getString("upload_id"));
                        }
                    } else {
                        System.out.println("❌ 文件创建失败");
                    }
                } catch (Exception e) {
                    System.out.println("❌ 大文件预检查异常：" + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                System.out.println("⚠️ 没有找到大于 10MB 的测试文件，已跳过");
            }
            
            // ========== 步骤 5: 完整的文件上传流程测试 ==========
            System.out.println("\n=== 测试完整文件上传流程 ===");
            File uploadTestFile = new File("/music/上传测试文件.mp3");
            if (uploadTestFile.exists()) {
                try {
                    System.out.println("文件：" + uploadTestFile.getName());
                    System.out.println("大小：" + formatFileSize(uploadTestFile.length()));
                    System.out.println("开始上传...");
                    
                    long startTime = System.currentTimeMillis();

                    AliUploadResult uploadResult = AliyunUploadUtils.uploadFile(
                            accessToken,
                            defaultDriveId,
                            "root",
                            uploadTestFile,
                            true  // 启用秒传
                    );

                    long endTime = System.currentTimeMillis();
                    long duration = endTime - startTime;
                    
                    if (uploadResult != null) {
                        System.out.println("✅ 文件上传成功！");
                        System.out.println("总耗时：" + duration + "ms (" + String.format("%.2f", duration/1000.0) + "秒)");
                        System.out.println("文件 ID: " + uploadResult.getFileId());
                        System.out.println("文件名：" + uploadResult.getFileName());
                        System.out.println("文件大小：" + formatFileSize(uploadResult.getFileSize()));
                        System.out.println("文件 Hash: " + uploadResult.getSha1());
                        
                        // 尝试获取文件详情
                        System.out.println("\n等待数据同步...");
                        Thread.sleep(1000);
                        
                        JSONObject fileDetails = AliyunDriveUtils.getFileDetails(
                                accessToken,
                                defaultDriveId,
                                uploadResult.getFileId()
                        );
                        
                        if (fileDetails != null) {
                            System.out.println("\n📄 文件详情:");
                            System.out.println("  分类：" + fileDetails.getString("category"));
                            System.out.println("  类型：" + fileDetails.getString("type"));
                            System.out.println("  文件后缀：" + fileDetails.getString("file_extension"));
                            System.out.println("  创建时间：" + formatTimestamp(fileDetails.getLongValue("created_at")));
                            System.out.println("  更新时间：" + formatTimestamp(fileDetails.getLongValue("updated_at")));
                            
                            // 如果是音频文件，显示额外信息
                            if ("audio".equals(fileDetails.getString("category"))) {
                                System.out.println("  ⚠️ 这是一个音频文件");
                            }
                            
                            // 如果是视频文件，显示视频元数据
                            if ("video".equals(fileDetails.getString("category"))) {
                                JSONObject videoMetadata = fileDetails.getJSONObject("video_media_metadata");
                                if (videoMetadata != null) {
                                    System.out.println("  视频时长：" + videoMetadata.getInteger("duration") + "ms");
                                    System.out.println("  视频分辨率：" + videoMetadata.getInteger("width") + "x" + videoMetadata.getInteger("height"));
                                }
                            }
                        }
                        
                        // 验证秒传是否正确
                        System.out.println("\n=== 验证秒传功能 ===");
                        Boolean wasRapidUpload = uploadResult.getRapidUpload();
                        if (wasRapidUpload != null && wasRapidUpload) {
                            System.out.println("✅ 验证成功：该文件是通过秒传上传的");
                        } else {
                            System.out.println("ℹ️ 该文件是通过普通上传完成的");
                        }
                        
                    } else {
                        System.out.println("❌ 文件上传失败");
                    }
                } catch (Exception e) {
                    System.out.println("❌ 文件上传异常：" + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                System.out.println("⚠️ 上传测试文件不存在，已跳过");
            }
            
            System.out.println("\n=== 所有测试完成 ===");
            
        } catch (Exception e) {
            System.err.println("❌ 错误：" + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // ========== 辅助方法 ==========
    
    private static String formatFileSize(long size) {
        if (size < 1024) return size + " B";
        if (size < 1024 * 1024) return String.format("%.2f KB", size / 1024.0);
        if (size < 1024 * 1024 * 1024) return String.format("%.2f MB", size / (1024.0 * 1024.0));
        if (size < 1024L * 1024 * 1024 * 1024) return String.format("%.2f GB", size / (1024.0 * 1024.0 * 1024.0));
        return String.format("%.2f TB", size / (1024.0 * 1024.0 * 1024.0 * 1024.0));
    }
    
    private static String formatTimestamp(long timestamp) {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new java.util.Date(timestamp * 1000));
    }
}
