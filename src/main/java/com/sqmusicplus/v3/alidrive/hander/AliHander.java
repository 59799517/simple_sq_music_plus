package com.sqmusicplus.v3.alidrive.hander;

import com.alibaba.fastjson2.JSONObject;
import com.sqmusicplus.v3.alidrive.entity.AliUploadResult;
import com.sqmusicplus.v3.alidrive.entity.SqAliSync;
import com.sqmusicplus.v3.alidrive.service.SqAliSyncService;
import com.sqmusicplus.v3.base.enums.SetConfigEnum;
import com.sqmusicplus.v3.config.SqConfigCache;
import com.sqmusicplus.v3.config.exception.SQException;
import com.sqmusicplus.v3.plug.entity.Music;
import com.sqmusicplus.v3.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;


/**
 * SQ
 * 阿里云盘处理类
 * 2026年3月28日
 */
@Slf4j
@Service
public class AliHander {

    @Autowired
    private SqAliSyncService sqAliSyncService;

    String DEFAULT_FOLDER_NAME = "SqMusic";
    /**
     * 获取阿里云盘授权码url
     */

    public  JSONObject getAuthCodeUrl(String appId) {
       return AliyunDriveUtils.getAuthorizationCode(appId);
    }
    /**
     * 获取阿里云盘确认
     */
    public  JSONObject getAuthConfirm(String appId,String code,String code_verifier) {
        return AliyunDriveUtils.getAccessToken(appId,code,code_verifier);
    }
    /**
     * 校验网盘access_token是由有效
     */
    public  Boolean checkAccessToken(String access_token) {
        return AliyunDriveUtils.checkAccessToken(access_token);
    }
    /**
     * 校验网盘的文件夹是否可用
     * path: 文件夹路径
     * autoCreate: 是否自动创建
     */
    public  Boolean checkFolder(String path,boolean autoCreate) {
//        使用/分割
        String[] split = path.split("/");
        String sp_default_drive_name = split[0];
        if (StringUtils.isBlank(sp_default_drive_name)){
            return false;
        }
        //查看是资源库还是备份盘
        String drive_id = "";
        if (sp_default_drive_name.equals("资源文件")){
            drive_id = SqConfigCache.getSqConfigValue(SetConfigEnum.EXPAND_ALIYUN_RESOURCE_DRIVE_ID);

        }else if (sp_default_drive_name.equals("备份文件")){
            drive_id = SqConfigCache.getSqConfigValue(SetConfigEnum.EXPAND_ALIYUN_BACKUP_DRIVE_ID);
        }
        if (StringUtils.isBlank(drive_id)){
            return false;
        }
        String sqConfigValue = SqConfigCache.getSqConfigValue(SetConfigEnum.EXPAND_ALIYUN_ACCESS_TOKEN);
        if (StringUtils.isBlank(sqConfigValue)){
            return false;
        }

//        剔除 split 的第 0 个
        String[] split1 = new String[split.length-1];
        System.arraycopy(split, 1, split1, 0, split.length - 1);
        //剩余的搜索路径
        String defaultDriveId = drive_id;
        String defaultFolderID="root";

        for (String s : split1) {
            //检查第二级以后的文件夹是否存在
            JSONObject searchResult1 = AliyunDriveUtils.searchFiles(
                    sqConfigValue,
                    defaultDriveId,
                    "type = 'folder' and name ='"+s+"' and parent_file_id = '"+defaultFolderID+"'"
            );
            Long totalCount = searchResult1.getLong("total_count");
            if (totalCount!=null&&totalCount > 0){
                defaultFolderID = searchResult1.getJSONArray("items").getJSONObject(0).getString("file_id");
            }else{
                // 如果文件夹不存在且 autoCreate 为 true，则创建
                if (autoCreate) {
                    JSONObject folder = AliyunDriveUtils.createFolder(sqConfigValue, drive_id, defaultFolderID, s);
                    if (folder != null && folder.containsKey("file_id")) {
                        defaultFolderID = folder.getString("file_id");
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        }
        return true;
    }
    /**
     * 根据设置路径获取阿里云盘路径
     */
    public  String getDefaultFileIdPath() {
        String idPath = "";
        String aliyun_folder_path = SqConfigCache.getSqConfigValue(SetConfigEnum.EXPAND_ALIYUN_FOLDER_PATH);
        if (StringUtils.isBlank(aliyun_folder_path)){
           throw new SQException("无阿里云盘默认保存位置！");
        }
        //        使用/分割
        String[] split = aliyun_folder_path.split("/");
        String sp_default_drive_name = split[0];
        if (StringUtils.isBlank(sp_default_drive_name)){
            throw new SQException("无阿里云盘默认保存位置是空的！");
        }
        //查看是资源库还是备份盘
        String drive_id = "";
        if (sp_default_drive_name.equals("资源文件")){
            drive_id = SqConfigCache.getSqConfigValue(SetConfigEnum.EXPAND_ALIYUN_RESOURCE_DRIVE_ID);

        }else if (sp_default_drive_name.equals("备份文件")){
            drive_id = SqConfigCache.getSqConfigValue(SetConfigEnum.EXPAND_ALIYUN_BACKUP_DRIVE_ID);
        }
        if (StringUtils.isBlank(drive_id)){
            throw new SQException("无阿里云盘默认保存位置设置有误！");
        }
        idPath+=drive_id;
        String sqConfigValue = SqConfigCache.getSqConfigValue(SetConfigEnum.EXPAND_ALIYUN_ACCESS_TOKEN);
        if (StringUtils.isBlank(sqConfigValue)){
            throw new SQException("无阿里云授权token");
        }

//        剔除 split的第0个
        String[] split1 = new String[split.length-1];
        System.arraycopy(split, 1, split1, 0, split.length - 1);
        //剩余的搜索路径
        String defaultDriveId = drive_id;
        String defaultFolderID="root";

        for (String s : split1) {
            //检查第二级以后的文件夹是否存在
            JSONObject searchResult1 = AliyunDriveUtils.searchFiles(
                    sqConfigValue,
                    defaultDriveId,
                    "type = 'folder' and name ='"+s+"' and parent_file_id = '"+defaultFolderID+"'"
            );
            Long totalCount = searchResult1.getLong("total_count");
            if (totalCount!=null&&totalCount > 0){
                defaultFolderID = searchResult1.getJSONArray("items").getJSONObject(0).getString("file_id");
                idPath+="/"+defaultFolderID;
            }
        }
        return idPath;


    }
    /**
     * 获取默认的文件夹
     * 优先默认 DEFAULT_DRIVE_ID 然后是  RESOURCE_DRIVE_ID 然后是  DRIVE_ID
     */
    public  String getDefaultFilePath() {
        return "备份文件/"+DEFAULT_FOLDER_NAME;

    }
    /**
     * 根据path获取真的id path
     */
    public  String getFileIdByPath(String path) {
        String[] split = path.split("/");
        String sp_default_drive_name = split[0];
        StringBuilder idPath = new StringBuilder();
        //查看是资源库还是备份盘
        String drive_id = "";
        if (sp_default_drive_name.equals("资源文件")){
            drive_id = SqConfigCache.getSqConfigValue(SetConfigEnum.EXPAND_ALIYUN_RESOURCE_DRIVE_ID);

        }else if (sp_default_drive_name.equals("备份文件")){
            drive_id = SqConfigCache.getSqConfigValue(SetConfigEnum.EXPAND_ALIYUN_BACKUP_DRIVE_ID);
        }
        String sqConfigValue = SqConfigCache.getSqConfigValue(SetConfigEnum.EXPAND_ALIYUN_ACCESS_TOKEN);

//        剔除 split的第0个
        String[] split1 = new String[split.length-1];
        System.arraycopy(split, 1, split1, 0, split.length - 1);
        //剩余的搜索路径
        String defaultDriveId = drive_id;
        String defaultFolderID="root";
        idPath.append(drive_id);
        for (String s : split1) {
            //检查第二级以后的文件夹是否存在
            JSONObject searchResult1 = AliyunDriveUtils.searchFiles(
                    sqConfigValue,
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
    /**
     * 根据path创建文件夹
     */
    public  boolean createFolder(String path) {
        try {
            String[] split = path.split("/");
            String sp_default_drive_name = split[0];
            StringBuilder idPath = new StringBuilder();
            //查看是资源库还是备份盘
            String drive_id = "";
            if (sp_default_drive_name.equals("资源文件")){
                drive_id = SqConfigCache.getSqConfigValue(SetConfigEnum.EXPAND_ALIYUN_RESOURCE_DRIVE_ID);

            }else if (sp_default_drive_name.equals("备份文件")){
                drive_id = SqConfigCache.getSqConfigValue(SetConfigEnum.EXPAND_ALIYUN_BACKUP_DRIVE_ID);
            }
            String sqConfigValue = SqConfigCache.getSqConfigValue(SetConfigEnum.EXPAND_ALIYUN_ACCESS_TOKEN);

            String defaultFolderID="root";
            //        剔除 split的第0个
            String[] split1 = new String[split.length-1];
            System.arraycopy(split, 1, split1, 0, split.length - 1);
            for (String s : split1) {
                JSONObject folder = AliyunDriveUtils.createFolder(sqConfigValue, drive_id, defaultFolderID, s);
                defaultFolderID = folder.getString("file_id");
            }
        } catch (Exception e) {
            log.error("创建阿里云文件夹失败路径是{}:{}",path,e);
            e.printStackTrace();
            return false;
        }
        return true;
    }


    /**
     * 判断相对路径下的阿里云盘文件夹是否存在
     * @param baseId 根目录 ID
     * @param path 相对路径（格式：文件夹 1/文件夹2/文件夹3）
     * @param autoCreate 是否自动创建不存在的文件夹
     * @return 文件夹 ID 路径（用/分隔），如果失败返回 null
     */
    public  String checkRelativeFolder(String baseId,String path,boolean autoCreate) {
        String[] split = path.split("/");
        String access_token = SqConfigCache.getSqConfigValue(SetConfigEnum.EXPAND_ALIYUN_ACCESS_TOKEN);
                
        if (StringUtils.isBlank(access_token) || StringUtils.isBlank(baseId)) {
            return null;
        }
        
        StringBuilder idPath = new StringBuilder();
        String currentParentId = baseId;
        String[] currentParentIds = baseId.split("/");
        idPath.append(baseId);
        String searchCurrentParentId = currentParentId.substring(currentParentId.lastIndexOf("/") + 1);


        for (String s : split) {
            // 跳过空字符串
            if (StringUtils.isBlank(s)) {
                continue;
            }
            // 检查文件夹是否存在
            // 对文件夹名称进行转义，防止特殊字符破坏查询语法
            String escapedFolderName = escapeQuerySpecialChars(s);
            String query = String.format(
                "type = 'folder' and name = '%s' and parent_file_id = '%s'",
                escapedFolderName,
                searchCurrentParentId
            );
            
            JSONObject searchResult1 = AliyunDriveUtils.searchFiles(
                    access_token,
                    currentParentIds[0],
                    query
            );
            
            // 检查搜索结果是否为 null
            if (searchResult1 == null) {
                log.error("搜索文件夹失败，返回 null - 文件夹名: {}, parentFileId: {}", s, searchCurrentParentId);
                return null;
            }
            
            Long totalCount = searchResult1.getLong("total_count");
                   
            if (totalCount != null && totalCount > 0) {
                // 文件夹存在，更新父文件夹 ID
                searchCurrentParentId = searchResult1.getJSONArray("items").getJSONObject(0).getString("file_id");
                idPath.append("/").append(searchCurrentParentId);
            } else {
                // 文件夹不存在
                if (autoCreate) {
                    // 自动创建文件夹
                    JSONObject folder = AliyunDriveUtils.createFolder(access_token, currentParentIds[0], searchCurrentParentId, s);
                    if (folder != null && folder.containsKey("file_id")) {
                        searchCurrentParentId = folder.getString("file_id");
                        idPath.append("/").append(searchCurrentParentId);
                    } else {
                        return null;
                    }
                } else {
                    // 不自动创建，直接返回 null
                    return null;
                }
            }
        }
        return idPath.toString();
    }

    /**
     * 上传单个文件
     */
    public  SqAliSync uploadFile(File file, String musicName, String musicArtist, String musicAlbum,Integer downloadId) {
        String access_token = SqConfigCache.getSqConfigValue(SetConfigEnum.EXPAND_ALIYUN_ACCESS_TOKEN);
        if (StringUtils.isBlank(access_token)){
            throw new SQException("无阿里云授权token");
        }
        String aliyun_folder_path = SqConfigCache.getSqConfigValue(SetConfigEnum.EXPAND_ALIYUN_FOLDER_PATH);
        if (StringUtils.isBlank(aliyun_folder_path)){
            throw new SQException("无阿里云盘默认保存位置！");
        }
        String defaultFileIdPath = getDefaultFileIdPath();
        String[] split = defaultFileIdPath.split("/");
        String defaultFileId = getFileIdByPath(aliyun_folder_path);
        String drive_id = split[0];
        String musicPath = SqConfigCache.getSqConfigValue(SetConfigEnum.SYSTEM_DOWNLOAD_PATH);
        //检测是否是个目录或者文件 路径是否存在
        if (!file.exists()) {
           return null;
        }
        String relativePath;
        try {
            // 使用 Paths.relativize 正确处理跨平台路径
            java.nio.file.Path musicRootPath = java.nio.file.Paths.get(musicPath);
            java.nio.file.Path filePath = file.toPath();
            java.nio.file.Path relative = musicRootPath.relativize(filePath);
            // 只保留目录路径，不包含文件名
            java.nio.file.Path parentDir = relative.getParent();
            relativePath = parentDir != null ? parentDir.toString().replace("\\", "/") : "";
        } catch (Exception e) {
            log.error("计算相对路径失败: musicPath={}, file={}", musicPath, file.getAbsolutePath(), e);
            return null;
        }
        if (relativePath.startsWith("/")) {
            relativePath = relativePath.substring(1);
        }
        try {
            String uploadpaths = checkRelativeFolder(defaultFileId, relativePath, true);
            if (uploadpaths==null){
               return null;
            }
            String suffix = FileUtils.getFileSuffix(file.getName());
            String[] split1 = uploadpaths.split("/");
            String fileId = split1[split1.length-1];
            AliUploadResult aliUploadResult = AliyunUploadUtils.uploadFile(access_token, drive_id, fileId, file);
            log.info("上传文件成功:{} - sha1:{}, namePath:{}, idPath:{}", 
                file.getName(), 
                aliUploadResult.getSha1(),
                aliUploadResult.getNamePath(),
                aliUploadResult.getIdPath());
            SqAliSync sqAliSync = new SqAliSync();
            sqAliSync.setSha1(aliUploadResult.getSha1());
            sqAliSync.setPath(relativePath != null ? relativePath : "");
            sqAliSync.setAliPath(aliUploadResult.getNamePath() != null ? aliUploadResult.getNamePath() : "");
            sqAliSync.setAliIdPath(aliUploadResult.getIdPath() != null ? aliUploadResult.getIdPath() : "");
            sqAliSync.setName(aliUploadResult.getFileName());
            sqAliSync.setSuffix(suffix != null ? suffix : "");
            sqAliSync.setUploadTime(DateUtils.getNowDate());
            // 处理 rapidUpload 可能为 null 的情况
            Boolean rapidUpload = aliUploadResult.getRapidUpload();
            sqAliSync.setRapid(rapidUpload != null && rapidUpload ? 1 : 0);
            sqAliSync.setMusicName(musicName != null ? musicName : "");
            sqAliSync.setMusicArtist(musicArtist != null ? musicArtist : "");
            sqAliSync.setMusicAlbum(musicAlbum != null ? musicAlbum : "");
            sqAliSync.setDownloadId(downloadId);
            sqAliSyncService.save(sqAliSync);
            return sqAliSync;
        } catch (IOException e) {
           return null;
        }

    }


    /**
     * 根据文件夹上传文件并返回 SqAliSync
     */
    public List<SqAliSync> uploadFile(boolean verify) {
        return uploadFile(verify, false);
    }
    
    /**
     * 根据文件夹上传文件并返回 SqAliSync
     * @param verify 是否验证文件类型
     * @param incremental 是否增量上传（只上传新增或修改的文件）
     */
    public List<SqAliSync> uploadFile(boolean verify, boolean incremental) {
        String access_token = SqConfigCache.getSqConfigValue(SetConfigEnum.EXPAND_ALIYUN_ACCESS_TOKEN);
        if (StringUtils.isBlank(access_token)){
            throw new SQException("无阿里云授权token");
        }
        String aliyun_folder_path = SqConfigCache.getSqConfigValue(SetConfigEnum.EXPAND_ALIYUN_FOLDER_PATH);
        if (StringUtils.isBlank(aliyun_folder_path)){
            throw new SQException("无阿里云盘默认保存位置！");
        }
        String defaultFileIdPath = getDefaultFileIdPath();
        String[] split = defaultFileIdPath.split("/");
        String defaultFileId = getFileIdByPath(aliyun_folder_path);
        String drive_id = split[0];
        ArrayList<File> files = new ArrayList<>();
        String musicPath = SqConfigCache.getSqConfigValue(SetConfigEnum.SYSTEM_DOWNLOAD_PATH);
        //数据库下载路径
        File file = new File(musicPath);
        //检测是否是个目录或者文件 路径是否存在
        if (!file.exists()) {
           throw  new SQException("文件或者文件夹不存在");
        }
        //递归查询所有音频和歌词文件
         collectFilesRecursive(file, files,verify);

        // 增量上传：查询数据库中已上传的文件，对比 path 和 suffix 判断是否需要重新上传
        Set<String> uploadedFileSet = new HashSet<>();
        if (incremental) {
            // 查询需要的字段（path 和 suffix），减少数据传输
            List<SqAliSync> uploadedRecords = sqAliSyncService.list(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<SqAliSync>()
                    .select(SqAliSync::getPath, SqAliSync::getSuffix)
            );
            for (SqAliSync record : uploadedRecords) {
                // 将 path 和 suffix 组合作为唯一标识
                String uniqueKey = record.getPath() + "::" + record.getSuffix();
                uploadedFileSet.add(uniqueKey);
            }
        }
        
        List<SqAliSync> sqAliSyncs = new ArrayList<>();

        // 直接遍历文件列表，每个文件都处理
        for (File currentFile : files) {
            String fileName = currentFile.getName();
            try {
                // 计算文件相对于 musicPath 的相对路径（包含文件名）
                String relativePath;
                try {
                    java.nio.file.Path musicRootPath = java.nio.file.Paths.get(musicPath);
                    java.nio.file.Path filePath = currentFile.toPath();
                    java.nio.file.Path relative = musicRootPath.relativize(filePath);
                    relativePath = relative.toString().replace("\\", "/");
                } catch (Exception e) {
                    log.error("计算相对路径失败: musicPath={}, file={}", musicPath, currentFile.getAbsolutePath(), e);
                    continue;
                }
                
                // 获取文件后缀
                String suffix = FileUtils.getFileSuffix(fileName);
                
                // 增量上传：检查文件是否已存在（path 和 suffix 都相同）
                if (incremental) {
                    String uniqueKey = relativePath + "::" + suffix;
                    if (uploadedFileSet.contains(uniqueKey)) {
                        log.debug("文件已上传，跳过：{} [{}]", relativePath, suffix);
                        continue;
                    }
                }
                
                // 提取文件的父目录路径（用于创建文件夹）
                String parentDirPath = "";
                int lastSlashIndex = relativePath.lastIndexOf("/");
                if (lastSlashIndex > 0) {
                    parentDirPath = relativePath.substring(0, lastSlashIndex);
                }
                
                // 创建或获取父目录的 folder_id
                String uploadpaths = checkRelativeFolder(defaultFileId, parentDirPath, true);
                if (uploadpaths == null) {
                    throw new SQException("创建文件夹失败: " + parentDirPath);
                }
                
                String[] split1 = uploadpaths.split("/");
                String fileId = split1[split1.length - 1];
                
                // 上传文件
                AliUploadResult aliUploadResult = AliyunUploadUtils.uploadFile(access_token, drive_id, fileId, currentFile);
                log.info("上传文件成功:{} - sha1:{}, namePath:{}, idPath:{}", 
                    fileName, 
                    aliUploadResult.getSha1(),
                    aliUploadResult.getNamePath(),
                    aliUploadResult.getIdPath());
                
                // 保存同步记录
                SqAliSync sqAliSync = new SqAliSync();
                sqAliSync.setSha1(aliUploadResult.getSha1());
                sqAliSync.setPath(relativePath != null ? relativePath : "");
                sqAliSync.setAliPath(aliUploadResult.getNamePath() != null ? aliUploadResult.getNamePath() : "");
                sqAliSync.setAliIdPath(aliUploadResult.getIdPath() != null ? aliUploadResult.getIdPath() : "");
                sqAliSync.setName(aliUploadResult.getFileName());
                sqAliSync.setSuffix(suffix != null ? suffix : "");
                sqAliSync.setUploadTime(DateUtils.getNowDate());
                // 处理 rapidUpload 可能为 null 的情况
                Boolean rapidUpload = aliUploadResult.getRapidUpload();
                sqAliSync.setRapid(rapidUpload != null && rapidUpload ? 1 : 0);
                // 批量上传时这些字段为空，设置为空字符串
                sqAliSync.setMusicName("");
                sqAliSync.setMusicArtist("");
                sqAliSync.setMusicAlbum("");
                sqAliSync.setDownloadId(null);
                sqAliSyncService.save(sqAliSync);
            } catch (Exception e) {
                // 捕获所有异常，记录错误但继续处理下一个文件
                log.error("上传文件失败，跳过：{} - 错误信息：{}", fileName, e.getMessage());
                log.debug("详细错误堆栈：", e);
                // 不抛出异常，继续处理下一个文件
            }
        }
        return sqAliSyncs;
    }






    /**
     * 递归收集目录下的所有音频和歌词文件
     */
    private void collectFilesRecursive(File dir, ArrayList<File> files,boolean verify) {
        File[] listFiles = dir.listFiles();
        if (listFiles == null) {
            return;
        }
        for (File f : listFiles) {
            if (f.isDirectory()) {
                // 递归子目录
                collectFilesRecursive(f, files,verify);
            } else if (f.isFile()) {
                // verify=true: 检查是否符合过滤规则（音频、歌词、图片）
                // verify=false: 不检查，添加所有文件
                if (!verify || FileTypeUtils.isValidAudioFile(f) || FileTypeUtils.isValidLrcFile(f) || FileTypeUtils.isValidImageFile(f)) {
                    files.add(f);
                }
            }
        }
    }

    /**
     * 转义阿里云盘查询语法中的特殊字符
     * 需要转义的字符: * / : < > " ' | ? \ 空格
     * @param input 原始字符串
     * @return 转义后的字符串
     */
    private String escapeQuerySpecialChars(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        
        // 按顺序转义特殊字符
        // 注意：必须先转义反斜杠，否则会影响其他转义
        return input
                .replace("\\", "\\\\")   // \ -> \\
                .replace("*", "\\*")     // * -> \*
                .replace("/", "\\/")     // / -> \/
                .replace(":", "\\:")     // : -> \:
                .replace("<", "\\<")     // < -> \<
                .replace(">", "\\>")     // > -> \>
                .replace("\"", "\\\"")  // " -> \"
                .replace("'", "\\'")     // ' -> \'
                .replace("|", "\\|")     // | -> \|
                .replace("?", "\\?")     // ? -> \?
                .replace(" ", "\\ ");    // 空格 -> \ 
    }

}
