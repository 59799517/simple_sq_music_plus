package com.sqmusicplus.v3.controller;


import cn.dev33.satoken.annotation.SaCheckLogin;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.sqmusicplus.v3.alidrive.hander.AliHander;
import com.sqmusicplus.v3.alidrive.entity.SqAliSync;
import com.sqmusicplus.v3.alidrive.service.SqAliSyncService;
import com.sqmusicplus.v3.base.entity.SqConfig;
import com.sqmusicplus.v3.base.enums.SetConfigEnum;
import com.sqmusicplus.v3.base.service.SqConfigService;
import com.sqmusicplus.v3.config.AjaxResult;
import com.sqmusicplus.v3.config.SqConfigCache;
import com.sqmusicplus.v3.utils.AliyunDriveUtils;
import com.sqmusicplus.v3.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Classname ExpandController
 * @Description 阿里云盘控制器
 * @Version 1.0.0
 * @Date 2026年3月28日
 * @Created by SQ
 */
@Slf4j
@RestController
@RequestMapping("/api/expand/ali")
public class ExpandController {

    @Autowired
    private AliHander aliHander;
    @Autowired
    private SqConfigService configService;
    @Autowired
    private SqAliSyncService sqAliSyncService;
    /**
     * 获取阿里云盘授权码url
     */
    @SaCheckLogin
    @PostMapping("/getAuthorizationCode")
    public AjaxResult getAuthorizationCode() {
        String appid = SqConfigCache.getSqConfigValue(SetConfigEnum.EXPAND_ALIYUN_APPID);
        if (StringUtils.isBlank(appid)){
            return AjaxResult.error("没有发现appId请先填写APPID");
        }

        JSONObject authCodeUrl = aliHander.getAuthCodeUrl(appid);
        return authCodeUrl==null?AjaxResult.error("获取阿里云盘授权码url失败"):AjaxResult.success(authCodeUrl);
    }
    /**
     * 获取确认授权码
     */
    @SaCheckLogin
    @PostMapping("/getConfirmCode")
    public AjaxResult getConfirmCode(@RequestBody HashMap<String, String> param ) {
        String appid = SqConfigCache.getSqConfigValue(SetConfigEnum.EXPAND_ALIYUN_APPID);
        if (StringUtils.isBlank(appid)){
            return AjaxResult.error("没有发现appId请先填写APPID");
        }

        String code = param.get("code");
        String code_verifier = param.get("code_verifier");
        if (StringUtils.isBlank(code)|| StringUtils.isBlank(code_verifier)){
            return AjaxResult.error("参数错误");
        }
        JSONObject authConfirm = aliHander.getAuthConfirm(appid,code,code_verifier);
        if (authConfirm!=null){
            LambdaUpdateWrapper<SqConfig> sqConfigLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            sqConfigLambdaUpdateWrapper.eq(SqConfig::getConfigKey, SetConfigEnum.EXPAND_ALIYUN_ACCESS_TOKEN.getKey())
                    .set(SqConfig::getConfigValue, authConfirm.getString("access_token"));
            boolean update = configService.update(sqConfigLambdaUpdateWrapper);
            if (update){
                SqConfigCache.updateConfigToDb(SetConfigEnum.EXPAND_ALIYUN_ACCESS_TOKEN.getKey(), authConfirm.getString("access_token"));
                String expiresIn = authConfirm.getString("expiration_time_string");
                SqConfigCache.updateConfigToDb(SetConfigEnum.EXPAND_ALIYUN_ACCESS_TOKEN_EXPIRE_TIME.getKey(), expiresIn);
                return AjaxResult.success("授权成功");
            }
        }
        return authConfirm==null?AjaxResult.error("获取阿里云盘确认授权码失败"):AjaxResult.success(authConfirm);
    }
    /**
     * 校验access_token是由有效
     */
    @SaCheckLogin
    @RequestMapping("/checkAccessToken")
    public AjaxResult checkAccessToken() {
        String access_token = SqConfigCache.getSqConfigValue(SetConfigEnum.EXPAND_ALIYUN_ACCESS_TOKEN);
        if (StringUtils.isBlank(access_token)){
            return AjaxResult.error("暂无授权信息请重新授权");
        }
        Boolean b = aliHander.checkAccessToken(access_token);
        return b==null?AjaxResult.error("校验阿里云盘授权码失败,重试授权一下！"):b?AjaxResult.success("授权成功"):AjaxResult.error("授权已过期请重新授权");
    }
    /**
     * 获取阿里云盘用户信息以及用户信息
     */
    @SaCheckLogin
    @RequestMapping("/getAndSetUserInfo")
    public AjaxResult getAndSetUserInfo() {
        String access_token = SqConfigCache.getSqConfigValue(SetConfigEnum.EXPAND_ALIYUN_ACCESS_TOKEN);
        if (StringUtils.isBlank(access_token)){
            return AjaxResult.error("暂无授权信息请重新授权");
        }
        JSONObject userInfo = AliyunDriveUtils.getDriveInfo(access_token);
        if (userInfo!=null){
            SqConfigCache.updateConfigToDb(SetConfigEnum.EXPAND_ALIYUN_BACKUP_DRIVE_ID.getKey(), userInfo.getString("default_drive_id"));
            SqConfigCache.updateConfigToDb(SetConfigEnum.EXPAND_ALIYUN_RESOURCE_DRIVE_ID.getKey(), userInfo.getString("resource_drive_id"));
            SqConfigCache.updateConfigToDb(SetConfigEnum.EXPAND_ALIYUN_USER_NAME.getKey(), userInfo.getString("name"));
            SqConfigCache.updateConfigToDb(SetConfigEnum.EXPAND_ALIYUN_USER_ID.getKey(), userInfo.getString("user_id"));
            SqConfigCache.updateConfigToDb(SetConfigEnum.EXPAND_ALIYUN_AVATAR.getKey(), userInfo.getString("avatar"));
            SqConfigCache.updateConfigToDb(SetConfigEnum.EXPAND_ALIYUN_USER_INFO_NAME.getKey(), userInfo.getString("user_name"));
            SqConfigCache.updateConfigToDb(SetConfigEnum.EXPAND_ALIYUN_NICK_NAME.getKey(), userInfo.getString("nick_name"));
            return AjaxResult.success("获取阿里云盘用户信息成功");
        }
        return userInfo==null?AjaxResult.error("获取阿里云盘用户信息失败"):AjaxResult.success(userInfo);
    }

    /**
     * 校验文件夹是否存在
     */
    @SaCheckLogin
    @PostMapping("/checkFolder")
    public AjaxResult checkFolder(@RequestBody HashMap<String, String> param) {
        String path = param.get("path");
        if (StringUtils.isBlank(path)){
            return AjaxResult.error("空值不允许设置，可以获取默认保存位置！");
        }
        String access_token = SqConfigCache.getSqConfigValue(SetConfigEnum.EXPAND_ALIYUN_ACCESS_TOKEN);
        if (StringUtils.isBlank(access_token)){
            return AjaxResult.error("暂无授权信息请重新授权");
        }
        Boolean b = aliHander.checkFolder(path,false);
        return b==null?AjaxResult.error("校验阿里云盘文件夹失败,是否需要使用默认目录！"):b?AjaxResult.success("检测通过",true):AjaxResult.error("文件夹不存在",false);
    }
    /**
     * 获取默认保存位置
     */
    @SaCheckLogin
    @RequestMapping("/getDefaultSavePath")
    public AjaxResult getDefaultSavePath() {
        String path = aliHander.getDefaultFilePath();
        if (StringUtils.isBlank(path)){
            return AjaxResult.error("暂无默认保存位置请设置！");
        }
        return AjaxResult.success("获取默认保存位置成功",path);
    }

    /**
     * 根据路径自动创建阿里云文件夹
     */
    @SaCheckLogin
    @PostMapping("/autoCreateFolder")
    public AjaxResult autoCreateFolder(@RequestBody HashMap<String, String> param) {
        String path = param.get("path");
        if (StringUtils.isBlank(path)){
            return AjaxResult.error("空值不允许设置，可以获取默认保存位置！");
        }
        Boolean b = aliHander.createFolder(path);
        return b==null?AjaxResult.error("创建阿里云盘文件夹失败,是否需要使用默认目录！"):b?AjaxResult.success("创建成功"):AjaxResult.error("创建失败");
    }
    /**
     * 手动同步一次（全量）
     */
    @SaCheckLogin
    @RequestMapping("/syncOnce")
    public AjaxResult syncOnce() {
        new Thread(()->{
            try {
                LambdaQueryWrapper<SqAliSync> notNull = new LambdaQueryWrapper<SqAliSync>().isNotNull(SqAliSync::getId);
                sqAliSyncService.remove(notNull);
                aliHander.uploadFile(true);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("同步失败：{}",e.getMessage());
            }
        }).start();
        return AjaxResult.success("正在后台同步！");
    }
    
    /**
     * 增量同步（只上传新增或修改的文件）
     */
    @SaCheckLogin
    @RequestMapping("/incrementalSync")
    public AjaxResult incrementalSync() {
        new Thread(()->{
            try {
                List<SqAliSync> sqAliSyncs = aliHander.uploadFile(true, true);
                if (!sqAliSyncs.isEmpty()) {
                    log.info("增量同步完成，新上传 {} 个文件", sqAliSyncs.size());
                } else {
                    log.info("无需同步，所有文件已上传");
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error("增量同步失败：{}",e.getMessage());
            }
        }).start();
        return AjaxResult.success("正在后台增量同步！");
    }


    /**
     * 查询所有已经上传的的文件列表
     */
    @SaCheckLogin
    @RequestMapping("/queryAllUploadFile")
    public AjaxResult queryAllUploadFile() {
        List<SqAliSync> sqAliSyncs = sqAliSyncService.list();
        return AjaxResult.success("查询成功",sqAliSyncs);
    }
    /**
     * 查询所有已经上传的的文件列表树状展示
     */
    @SaCheckLogin
    @RequestMapping("/queryAllUploadFileTree")
    public AjaxResult queryAllUploadFileTree() {
        List<SqAliSync> sqAliSyncs = sqAliSyncService.list();
        Map<String, Object> treeRoot = buildFileTree(sqAliSyncs);
        return AjaxResult.success("查询成功", treeRoot.get("children"));
    }
    
    /**
     * 构建文件树
     * @param files 文件列表
     * @return 树状结构
     */
    private Map<String, Object> buildFileTree(List<SqAliSync> files) {
        Map<String, Object> root = new HashMap<>();
        root.put("name", "根目录");
        root.put("type", "folder");
        root.put("children", new ArrayList<Map<String, Object>>());
        
        for (SqAliSync file : files) {
            String path = file.getPath();
            if (path == null || path.isEmpty()) {
                continue;
            }
            // 按路径分隔符拆分（支持 / 和 \）
            String[] parts = path.split("[/\\\\]");
            // 递归添加到树中
            addToTree(root, parts, file, 0);
        }
        
        return root;
    }
    
    /**
     * 递归添加节点到树中
     * @param parentNode 父节点
     * @param parts 路径部分数组
     * @param file 文件对象（仅在叶子节点使用）
     * @param index 当前处理的路径索引
     */
    @SuppressWarnings("unchecked")
    private void addToTree(Map<String, Object> parentNode, String[] parts, SqAliSync file, int index) {
        if (index >= parts.length) {
            return;
        }
        
        String currentName = parts[index];
        boolean isLastLevel = (index == parts.length - 1);
        
        List<Map<String, Object>> children = (List<Map<String, Object>>) parentNode.get("children");
        
        // 查找是否已存在该节点
        Map<String, Object> existingNode = null;
        for (Map<String, Object> child : children) {
            if (child.get("name").equals(currentName)) {
                existingNode = child;
                break;
            }
        }
        
        if (existingNode == null) {
            // 创建新节点
            Map<String, Object> newNode = new HashMap<>();
            newNode.put("name", currentName);
            
            if (isLastLevel) {
                // 叶子节点（文件）
                newNode.put("type", "file");
                newNode.put("fileInfo", file);
            } else {
                // 文件夹节点
                newNode.put("type", "folder");
                newNode.put("children", new ArrayList<Map<String, Object>>());
            }
            
            children.add(newNode);
            existingNode = newNode;
        }
        
        // 如果不是最后一层，继续递归
        if (!isLastLevel) {
            addToTree(existingNode, parts, file, index + 1);
        }
    }






}
