package com.sqmusicplus;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.sqmusicplus.v3.base.enums.SetConfigEnum;
import com.sqmusicplus.v3.config.SqConfigCache;
import com.sqmusicplus.v3.utils.AliyunDriveUtils;
import com.sqmusicplus.v3.utils.StringUtils;

import java.io.File;

public class AliyunTest {

    public static String token ="eyJraWQiOiJLcU8iLCJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI2ODdkNTQzNGNlMTk0Y2YzYmJjNDRhMmNhNTM2YWM3ZiIsImF1ZCI6IjQxYWRhNTU3ZmQ4MjQ4ODlhMmI2M2FiZjFhZjYzOTg2IiwicyI6ImNkYSIsImQiOiIxNzg2MDYsODkyMjQ2MjgyIiwiaXNzIjoiYWxpcGFuIiwiZXhwIjoxNzc3NDY0NDk5LCJpYXQiOjE3NzQ4NzI0OTYsImp0aSI6IjI1MjNhMzQ5MjhjODQ3YTU5NTQ1OWNkY2NkMTVmNjA0In0.5amPvKdqFRv3rohyq-avIwmVBnl_4LWnSWmENc8YoLQ";
    public static String drive_id = "178606";
    public static   Boolean checkFolder(String path) {
//        使用/分割
        String[] split = path.split("/");


//        剔除 split的第0个
        String[] split1 = new String[split.length-1];
        System.arraycopy(split, 1, split1, 0, split.length - 1);
        //剩余的搜索路径
        String defaultDriveId = "178606";
        String defaultFolderID="root";
        for (String s : split1) {
            //检查第二级以后的文件夹是否存在
            JSONObject searchResult1 = AliyunDriveUtils.searchFiles(
                    "eyJraWQiOiJLcU8iLCJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI2ODdkNTQzNGNlMTk0Y2YzYmJjNDRhMmNhNTM2YWM3ZiIsImF1ZCI6IjQxYWRhNTU3ZmQ4MjQ4ODlhMmI2M2FiZjFhZjYzOTg2IiwicyI6ImNkYSIsImQiOiIxNzg2MDYsODkyMjQ2MjgyIiwiaXNzIjoiYWxpcGFuIiwiZXhwIjoxNzc3NDY0NDk5LCJpYXQiOjE3NzQ4NzI0OTYsImp0aSI6IjI1MjNhMzQ5MjhjODQ3YTU5NTQ1OWNkY2NkMTVmNjA0In0.5amPvKdqFRv3rohyq-avIwmVBnl_4LWnSWmENc8YoLQ",
                    defaultDriveId,
                    "type = 'folder' and name ='"+s+"' and parent_file_id = '"+defaultFolderID+"'"
            );
            Long totalCount = searchResult1.getLong("total_count");
            if (totalCount!=null&&totalCount > 0){
                defaultFolderID = searchResult1.getJSONArray("items").getJSONObject(0).getString("file_id");
            }else{
                return false;
            }
        }
        return true;
    }



    public static String getFileIdByPath(String path) {
        String[] split = path.split("/");
//        String sp_default_drive_name = split[0];
        StringBuilder idPath = new StringBuilder();


//        剔除 split的第0个
        String[] split1 = new String[split.length-1];
        System.arraycopy(split, 1, split1, 0, split.length - 1);
        //剩余的搜索路径
        String defaultDriveId = "178606";
        String defaultFolderID="root";
        idPath.append(defaultDriveId);
        for (String s : split1) {
            //检查第二级以后的文件夹是否存在
            JSONObject searchResult1 = AliyunDriveUtils.searchFiles(
                    token,
                    defaultDriveId,
                    "type = 'folder' and name ='"+s+"' and parent_file_id = '"+defaultFolderID+"'"
            );
            Long totalCount = searchResult1.getLong("total_count");
            if (totalCount!=null&&totalCount > 0){
                idPath.append("/");
                defaultFolderID = searchResult1.getJSONArray("items").getJSONObject(0).getString("file_id");
                idPath.append(defaultFolderID);
            }
        }

        return idPath.toString();
    }
    public static void main(String[] args){
//                    JSONObject searchResult1 = AliyunDriveUtils.searchFiles(
//                    "eyJraWQiOiJLcU8iLCJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI2ODdkNTQzNGNlMTk0Y2YzYmJjNDRhMmNhNTM2YWM3ZiIsImF1ZCI6IjQxYWRhNTU3ZmQ4MjQ4ODlhMmI2M2FiZjFhZjYzOTg2IiwicyI6ImNkYSIsImQiOiIxNzg2MDYsODkyMjQ2MjgyIiwiaXNzIjoiYWxpcGFuIiwiZXhwIjoxNzc3NDY0NDk5LCJpYXQiOjE3NzQ4NzI0OTYsImp0aSI6IjI1MjNhMzQ5MjhjODQ3YTU5NTQ1OWNkY2NkMTVmNjA0In0.5amPvKdqFRv3rohyq-avIwmVBnl_4LWnSWmENc8YoLQ",
//                    "178606",
//                    "type = 'folder' and name ='SQ'"
//            );
//                    printSearchResult(searchResult1);
//        Boolean b = checkFolder("备份盘/SQ/sqmusic/sqmusic");
//        System.out.println(b);
//        String fileIdByPath = getFileIdByPath("备份盘/SQ/sqmusic/sqmusic");
//        System.out.println(fileIdByPath);


//        JSONObject result = AliyunDriveUtils.createFolder(
//                "178606",
//                "root",
//                "SQ测试创建文件夹"
//        );

        String path = "备份文件/SQ测试创建文件夹/sqmusic/sqmusic";
        String[] split = path.split("/");

        String defaultFolderID="root";
        //        剔除 split的第0个
        String[] split1 = new String[split.length-1];
        System.arraycopy(split, 1, split1, 0, split.length - 1);
        for (String s : split1) {
            JSONObject folder = AliyunDriveUtils.createFolder(token, drive_id, defaultFolderID, s);
            defaultFolderID = folder.getString("file_id");
        }



    }
//    public static void main(String[] args) {
//        try {
//            // ========== 步骤 1: 获取 Access Token ==========
////            System.out.println("=== 获取 Access Token ===");
////            JSONObject authResult = AliyunDriveUtils.getAuthorizationCode();
////            String codeVerifier = authResult.getString("code_verifier");
////
////            // 假设已经从回调 URL 提取了 authorization_code
////            String authorizationCode = extractCodeFromLocation(authResult.getString("location"));
////
////            JSONObject tokenResult = AliyunDriveUtils.getAccessToken(
////                    authorizationCode,
////                    codeVerifier
////            );
//
//            String accessToken = "eyJraWQiOiJLcU8iLCJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI2ODdkNTQzNGNlMTk0Y2YzYmJjNDRhMmNhNTM2YWM3ZiIsImF1ZCI6IjQxYWRhNTU3ZmQ4MjQ4ODlhMmI2M2FiZjFhZjYzOTg2IiwicyI6ImNkYSIsImQiOiIxNzg2MDYsODkyMjQ2MjgyIiwiaXNzIjoiYWxpcGFuIiwiZXhwIjoxNzc3MjcxODMzLCJpYXQiOjE3NzQ2Nzk4MzAsImp0aSI6ImNlM2Q1MjhmMjIwZjQ0YmY4M2I2YWQ4YTdmOGU0ZjI0In0.yAU29z8_7i6kBP_Y8r6SukujHBvYdw8xPxTMxJImkOQ";
//            System.out.println("Access Token: " + accessToken);
//
//            // ========== 步骤 2: 获取用户基本信息 ==========
//            System.out.println("\n=== 用户基本信息 ===");
//            JSONObject userInfo = AliyunDriveUtils.getUserInfo(accessToken);
//            System.out.println("用户 ID: " + userInfo.getString("id"));
//            System.out.println("昵称：" + userInfo.getString("name"));
//            System.out.println("头像：" + userInfo.getString("avatar"));
//
//            // ========== 步骤 3: 获取 Drive 信息（重要！）==========
//            System.out.println("\n=== Drive 信息 ===");
//            JSONObject driveInfo = AliyunDriveUtils.getDriveInfo(accessToken);
//            String defaultDriveId = driveInfo.getString("default_drive_id");
//            System.out.println("默认云盘 ID: " + defaultDriveId);
//            System.out.println("昵称：" + driveInfo.getString("name"));
//            System.out.println("头像：" + driveInfo.getString("avatar"));
//
//            // 如果有资源库
//            if (driveInfo.containsKey("resource_drive_id")) {
//                System.out.println("资源库 ID: " + driveInfo.getString("resource_drive_id"));
//            }
//
//            // ========== 步骤 4: 获取空间信息 ==========
//            System.out.println("\n=== 空间信息 ===");
//            JSONObject spaceInfo = AliyunDriveUtils.getSpaceInfo(accessToken);
//            JSONObject personalSpace = spaceInfo.getJSONObject("personal_space_info");
//
//            if (personalSpace == null) {
//                System.out.println("无法获取空间信息");
//            } else {
//                Long usedSizeObj = personalSpace.getLong("used_size");
//                Long totalSizeObj = personalSpace.getLong("total_size");
//
//                long usedSize = (usedSizeObj != null) ? usedSizeObj : 0;
//                long totalSize = (totalSizeObj != null) ? totalSizeObj : 0;
//
//                System.out.println("已使用：" + formatFileSize(usedSize));
//                System.out.println("总容量：" + formatFileSize(totalSize));
//
//                if (totalSize > 0) {
//                    System.out.println("剩余：" + formatFileSize(totalSize - usedSize));
//                    System.out.println("使用率：" + String.format("%.2f%%", (double)usedSize/totalSize*100));
//                } else {
//                    System.out.println("剩余：未知");
//                    System.out.println("使用率：未知");
//                }
//            }
//
//            // ========== 步骤 5: 获取 VIP 信息 ==========
//            System.out.println("\n=== VIP 信息 ===");
//            JSONObject vipInfo = AliyunDriveUtils.getVipInfo(accessToken);
//            String identity = vipInfo.getString("identity");
//            String level = vipInfo.getString("level");
//            Long expireTimeObj = vipInfo.getLong("expire");
//            long expireTime = (expireTimeObj != null) ? expireTimeObj : 0;
//
//            System.out.println("会员类型：" + getVipTypeName(identity));
//            System.out.println("等级：" + (level != null ? level : "未知"));
//            if (expireTimeObj != null) {
//                System.out.println("过期时间：" + formatTimestamp(expireTime));
//            } else {
//                System.out.println("过期时间：永久或未知");
//            }
//
//            Boolean thirdPartyVipObj = vipInfo.getBoolean("thirdPartyVip");
//            boolean thirdPartyVip = (thirdPartyVipObj != null) ? thirdPartyVipObj : false;
//            System.out.println("三方权益：" + (thirdPartyVip ? "已生效" : "未生效"));
//
//            // ========== 步骤 6: 获取用户权限 ==========
//            System.out.println("\n=== 用户权限 ===");
//            JSONObject scopesInfo = AliyunDriveUtils.getUserScopes(accessToken);
//            System.out.println("用户 ID: " + scopesInfo.getString("id"));
//
//            JSONArray scopes = scopesInfo.getJSONArray("scopes");
//            if (scopes == null || scopes.isEmpty()) {
//                System.out.println("暂无权限");
//            } else {
//                System.out.println("已授权权限:");
//                for (int i = 0; i < scopes.size(); i++) {
//                    JSONObject scope = scopes.getJSONObject(i);
//                    System.out.println("  - " + scope.getString("scope"));
//                }
//            }
//
//            // ========== 步骤 7: 文件秒传测试 ==========
//            System.out.println("\n=== 文件秒传测试 ===");
//            File musicFile = new File("/music/测试歌曲.mp3");
//
//            JSONObject uploadResult = AliyunDriveUtils.quickUploadFile(
//                    accessToken,
//                    defaultDriveId,  // 使用上面获取的 default_drive_id
//                    "root",
//                    musicFile
//            );
//
//            if (uploadResult.getBooleanValue("quick_upload")) {
//                System.out.println("✅ 秒传成功！");
//                System.out.println("文件 ID: " + uploadResult.getString("file_id"));
//            } else {
//                System.out.println("⚠️ 需要普通上传");
//                System.out.println("Upload ID: " + uploadResult.getString("upload_id"));
//            }
//
//            // ========== 步骤 8: 获取文件列表测试 ==========
//            System.out.println("\n=== 获取文件列表测试 ===");
//
//            // 测试 1: 获取根目录文件列表（简化版）
//            System.out.println("\n【根目录文件列表】");
//            JSONObject fileListSimple = AliyunDriveUtils.getFileList(
//                    accessToken,
//                    defaultDriveId,
//                    "root"
//            );
//            printFileList(fileListSimple);
//
//            // 测试 2: 获取根目录文件列表（带参数）
//            System.out.println("\n【根目录文件列表 - 带排序和过滤】");
//            JSONObject fileListFull = AliyunDriveUtils.getFileList(
//                    accessToken,
//                    defaultDriveId,
//                    "root",
//                    20,              // limit: 返回 20 个文件
//                    null,            // marker: 不分页
//                    "name",          // orderBy: 按名称排序
//                    "ASC",           // orderDirection: 升序
//                    null,            // category: 不限制分类
//                    "all",           // type: 所有类型
//                    "*"              // fields: 返回所有字段
//            );
//            printFileList(fileListFull);
//
//            // 测试 3: 只获取音频文件
//            System.out.println("\n【音频文件列表】");
//            JSONObject audioFiles = AliyunDriveUtils.getFileList(
//                    accessToken,
//                    defaultDriveId,
//                    "root",
//                    50,
//                    null,
//                    "name_enhanced",  // 对数字编号友好的排序
//                    "ASC",
//                    "audio",          // 只查询音频文件
//                    "file",           // 只查询文件，不包含文件夹
//                    "*"
//            );
//            printFileList(audioFiles);
//
//            // 测试 4: 只获取文件夹
//            System.out.println("\n【文件夹列表】");
//            JSONObject folderFiles = AliyunDriveUtils.getFileList(
//                    accessToken,
//                    defaultDriveId,
//                    "root",
//                    null,
//                    null,
//                    "name",
//                    "ASC",
//                    null,
//                    "folder",         // 只查询文件夹
//                    "*"
//            );
//            printFileList(folderFiles);
//
//            // ========== 步骤 9: 获取文件详情测试 ==========
//            System.out.println("\n=== 获取文件详情测试 ===");
//
//            // 先从文件列表中获取一个文件 ID
//            if (fileListSimple != null && fileListSimple.containsKey("items")) {
//                JSONArray items = fileListSimple.getJSONArray("items");
//                if (items != null && !items.isEmpty()) {
//                    // 获取第一个文件的详情
//                    JSONObject firstFile = items.getJSONObject(0);
//                    String fileId = firstFile.getString("file_id");
//                    String fileName = firstFile.getString("name");
//
//                    System.out.println("\n【获取文件详情：" + fileName + "】");
//                    JSONObject fileDetails = AliyunDriveUtils.getFileDetails(
//                            accessToken,
//                            defaultDriveId,
//                            fileId
//                    );
//
//                    if (fileDetails != null) {
//                        System.out.println("文件 ID: " + fileDetails.getString("file_id"));
//                        System.out.println("文件名：" + fileDetails.getString("name"));
//                        System.out.println("文件大小：" + formatFileSize(fileDetails.getLongValue("size")));
//                        System.out.println("文件类型：" + fileDetails.getString("type"));
//                        System.out.println("文件分类：" + fileDetails.getString("category"));
//                        System.out.println("文件后缀：" + fileDetails.getString("file_extension"));
//                        System.out.println("创建时间：" + formatTimestamp(fileDetails.getLongValue("created_at")));
//                        System.out.println("更新时间：" + formatTimestamp(fileDetails.getLongValue("updated_at")));
//
//                        // 如果是音频文件，显示额外信息
//                        if ("audio".equals(fileDetails.getString("category"))) {
//                            System.out.println("⚠️ 这是一个音频文件");
//                        }
//
//                        // 如果是视频文件，显示视频元数据
//                        if ("video".equals(fileDetails.getString("category"))) {
//                            JSONObject videoMetadata = fileDetails.getJSONObject("video_media_metadata");
//                            if (videoMetadata != null) {
//                                System.out.println("视频时长：" + videoMetadata.getInteger("duration") + "ms");
//                                System.out.println("视频分辨率：" + videoMetadata.getInteger("width") + "x" + videoMetadata.getInteger("height"));
//                            }
//                        }
//                    } else {
//                        System.out.println("❌ 获取文件详情失败");
//                    }
//                } else {
//                    System.out.println("⚠️ 根目录没有文件，跳过文件详情测试");
//                }
//            }
//
//            // ========== 步骤 10: 文件搜索测试 ==========
//            System.out.println("\n=== 文件搜索测试 ===");
//
//            // 测试 1: 简单搜索 - 搜索根目录下的文件
//            System.out.println("\n【搜索根目录下的所有文件】");
//            JSONObject searchResult1 = AliyunDriveUtils.searchFiles(
//                    accessToken,
//                    defaultDriveId,
//                    "parent_file_id = 'root'"
//            );
//            printSearchResult(searchResult1);
//
//            // 测试 2: 精确搜索 - 搜索特定文件名的文件
//            System.out.println("\n【精确搜索文件名包含'test'的文件】");
//            JSONObject searchResult2 = AliyunDriveUtils.searchFiles(
//                    accessToken,
//                    defaultDriveId,
//                    "name match \"test\""
//            );
//            printSearchResult(searchResult2);
//
//            // 测试 3: 组合搜索 - 搜索根目录下的音频文件
//            System.out.println("\n【搜索根目录下的音频文件】");
//            JSONObject searchResult3 = AliyunDriveUtils.searchFiles(
//                    accessToken,
//                    defaultDriveId,
//                    "parent_file_id = 'root' and category = 'audio'",
//                    50,              // limit
//                    null,            // marker
//                    "name ASC",      // orderBy
//                    true             // return_total_count
//            );
//            printSearchResult(searchResult3);
//
//            // 测试 4: 搜索文件夹
//            System.out.println("\n【搜索所有文件夹】");
//            JSONObject searchResult4 = AliyunDriveUtils.searchFiles(
//                    accessToken,
//                    defaultDriveId,
//                    "type = 'folder'",
//                    100,
//                    null,
//                    "name ASC",
//                    true
//            );
//            printSearchResult(searchResult4);
//
//            // 测试 5: 范围查询 - 搜索最近创建的文件
//            System.out.println("\n【搜索最近创建的文件】");
//            long now = System.currentTimeMillis() / 1000;
//            long oneDayAgo = now - 86400; // 一天前的时间戳
//            JSONObject searchResult5 = AliyunDriveUtils.searchFiles(
//                    accessToken,
//                    defaultDriveId,
//                    "created_at > '" + oneDayAgo + "'",
//                    50,
//                    null,
//                    "created_at DESC",
//                    true
//            );
//            printSearchResult(searchResult5);
//
//        } catch (Exception e) {
//            System.err.println("❌ 错误：" + e.getMessage());
//            e.printStackTrace();
//        }
//    }

    // ========== 辅助方法 ==========

    private static String extractCodeFromLocation(String location) {
        // TODO: 实现从回调 URL 提取 code
        return "your_authorization_code";
    }

    private static String formatFileSize(long size) {
        if (size < 1024) return size + " B";
        if (size < 1024 * 1024) return String.format("%.2f KB", size / 1024.0);
        if (size < 1024 * 1024 * 1024) return String.format("%.2f MB", size / (1024.0 * 1024.0));
        if (size < 1024L * 1024 * 1024 * 1024) return String.format("%.2f GB", size / (1024.0 * 1024.0 * 1024.0));
        return String.format("%.2f TB", size / (1024.0 * 1024.0 * 1024.0 * 1024.0));
    }

    private static String getVipTypeName(String identity) {
        switch (identity) {
            case "member": return "普通会员";
            case "vip": return "超级会员";
            case "svip": return "超级会员 SVIP";
            default: return "未知";
        }
    }

    private static String formatTimestamp(long timestamp) {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new java.util.Date(timestamp * 1000));
    }
    
    // ========== 打印辅助方法 ==========
    
    /**
     * 打印文件列表结果
     */
    private static void printFileList(JSONObject fileList) {
        if (fileList == null) {
            System.out.println("❌ 获取文件列表失败");
            return;
        }
        
        JSONArray items = fileList.getJSONArray("items");
        String nextMarker = fileList.getString("next_marker");
        
        if (items == null || items.isEmpty()) {
            System.out.println("📂 没有找到文件");
        } else {
            System.out.println("📂 共找到 " + items.size() + " 个文件/文件夹");
            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            System.out.printf("%-5s %-10s %-30s %-15s %-10s%n", "序号", "类型", "文件名", "大小", "分类");
            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            
            for (int i = 0; i < items.size(); i++) {
                JSONObject item = items.getJSONObject(i);
                String type = item.getString("type");
                String name = item.getString("name");
                long size = item.getLongValue("size");
                String category = item.getString("category");
                String icon = "file".equals(type) ? "📄" : "📁";
                
                System.out.printf("%-5d %-10s %-30s %-15s %-10s%n", 
                    i + 1, 
                    icon + " " + type, 
                    name.length() > 30 ? name.substring(0, 27) + "..." : name,
                    "file".equals(type) ? formatFileSize(size) : "-",
                    category != null ? category : "-");
            }
            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            
            if (nextMarker != null && !nextMarker.isEmpty()) {
                System.out.println("⚠️ 还有更多文件，next_marker: " + nextMarker);
            }
        }
    }
    
    /**
     * 打印搜索结果
     */
    private static void printSearchResult(JSONObject searchResult) {
        if (searchResult == null) {
            System.out.println("❌ 搜索失败");
            return;
        }
        
        JSONArray items = searchResult.getJSONArray("items");
        Long totalCount = searchResult.getLong("total_count");
        String nextMarker = searchResult.getString("next_marker");
        
        if (items == null || items.isEmpty()) {
            System.out.println("🔍 没有找到匹配的文件");
        } else {
            System.out.println("🔍 共找到 " + items.size() + " 个文件" + 
                (totalCount != null ? " (总数：" + totalCount + ")" : ""));
            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            System.out.printf("%-5s %-30s %-15s %-10s %-20s%n", "序号", "文件名", "大小", "分类", "路径");
            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            
            for (int i = 0; i < items.size(); i++) {
                JSONObject item = items.getJSONObject(i);
                String name = item.getString("name");
                long size = item.getLongValue("size");
                String category = item.getString("category");
                String parentFileId = item.getString("parent_file_id");
                
                System.out.printf("%-5d %-30s %-15s %-10s %-20s%n", 
                    i + 1, 
                    name.length() > 30 ? name.substring(0, 27) + "..." : name,
                    formatFileSize(size),
                    category != null ? category : "-",
                    "root".equals(parentFileId) ? "根目录" : parentFileId);
            }
            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            
            if (nextMarker != null && !nextMarker.isEmpty()) {
                System.out.println("⚠️ 还有更多结果，next_marker: " + nextMarker);
            }
        }
    }
}