package com.sqmusicplus.v3.task;

import com.sqmusicplus.v3.alidrive.hander.AliHander;
import com.sqmusicplus.v3.alidrive.entity.SqAliSync;
import com.sqmusicplus.v3.alidrive.service.SqAliSyncService;
import com.sqmusicplus.v3.base.enums.SetConfigEnum;
import com.sqmusicplus.v3.config.SqConfigCache;
import com.sqmusicplus.v3.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Classname AliDriceSyncTask
 * @Description TODO
 * @Version 1.0
 * @Date 2026年4月2日
 * @Created by SQ
 */
@Slf4j
@Component
public class AliDriceSyncTask {

    @Autowired
    private AliHander aliHander;
    @Autowired
    private SqAliSyncService sqAliSyncService;
    @Scheduled(cron="0 0 0/1 * * ? ")
    public void excute(){
        String aliyun_sync_mode = SqConfigCache.getSqConfigValue(SetConfigEnum.EXPAND_ALIYUN_SYNC_MODE);
        if (StringUtils.isBlank(aliyun_sync_mode)){
            return;
        }
        if (!aliyun_sync_mode.equals("scheduled")){
            return;
        }
        log.info("=============开始同步阿里云盘===============");
        List<SqAliSync> sqAliSyncs = aliHander.uploadFile(true,true);
        log.info("=============同步阿里云盘{}共计：{}首歌曲===============", !sqAliSyncs.isEmpty() ?"成功":"失败",sqAliSyncs.size());
    }
    //每天 0:00 执行 检查 token 是否失效
    @Scheduled(cron="0 0 0 * * ? ")
    public void checkToken(){
        String aliyun_open = SqConfigCache.getSqConfigValue(SetConfigEnum.EXPAND_ALIYUN_OPEN);
        if (StringUtils.isBlank(aliyun_open)){
            return;
        }
        if (!aliyun_open.equals("true")){
            return;
        }
        
        log.info("=============检查阿里云盘 Token===============");
        String token_expire_time = SqConfigCache.getSqConfigValue(SetConfigEnum.EXPAND_ALIYUN_ACCESS_TOKEN_EXPIRE_TIME);
        
        // 判断是否过期（忽略时分秒，只比较日期）
        if (isTokenExpired(token_expire_time)) {
            log.info("Token 已过期，清空配置");
            clearAliyunConfig();
            return;
        }
    
        String aliyun_access_token = SqConfigCache.getSqConfigValue(SetConfigEnum.EXPAND_ALIYUN_ACCESS_TOKEN);
    
        Boolean b = aliHander.checkAccessToken(aliyun_access_token);
        if (!b){
            log.info("Token 验证失败，清空配置");
            clearAliyunConfig();
        }
    }
        
    /**
     * 清空阿里云盘配置
     */
    private void clearAliyunConfig() {
        SqConfigCache.updateConfigToDb(SetConfigEnum.EXPAND_ALIYUN_OPEN,"false");
        SqConfigCache.updateConfigToDb(SetConfigEnum.EXPAND_ALIYUN_ACCESS_TOKEN_EXPIRE_TIME,"");
        SqConfigCache.updateConfigToDb(SetConfigEnum.EXPAND_ALIYUN_ACCESS_TOKEN,"");
        SqConfigCache.updateConfigToDb(SetConfigEnum.EXPAND_ALIYUN_CODE,"");
        SqConfigCache.updateConfigToDb(SetConfigEnum.EXPAND_ALIYUN_CODE_VERIFIER,"");
        SqConfigCache.updateConfigToDb(SetConfigEnum.EXPAND_ALIYUN_USER_NAME,"");
        SqConfigCache.updateConfigToDb(SetConfigEnum.EXPAND_ALIYUN_AVATAR,"");
        SqConfigCache.updateConfigToDb(SetConfigEnum.EXPAND_ALIYUN_USER_ID,"");
        SqConfigCache.updateConfigToDb(SetConfigEnum.EXPAND_ALIYUN_USER_INFO_NAME,"");
        SqConfigCache.updateConfigToDb(SetConfigEnum.EXPAND_ALIYUN_NICK_NAME,"");
        SqConfigCache.updateConfigToDb(SetConfigEnum.EXPAND_ALIYUN_BACKUP_DRIVE_ID,"");
        SqConfigCache.updateConfigToDb(SetConfigEnum.EXPAND_ALIYUN_RESOURCE_DRIVE_ID,"");
    }
    
    /**
     * 判断 Token 是否过期（忽略时分秒，只比较日期）
     * @param expireTimeStr 过期时间字符串（格式：yyyy-MM-dd HH:mm:ss）
     * @return true-已过期，false-未过期
     */
    private boolean isTokenExpired(String expireTimeStr) {
        if (StringUtils.isBlank(expireTimeStr)) {
            return true;
        }
        
        try {
            // 解析过期时间
            java.time.LocalDateTime expireTime = java.time.LocalDateTime.parse(expireTimeStr, 
                java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            // 获取当前时间
            java.time.LocalDateTime now = java.time.LocalDateTime.now();
            
            // 只比较日期部分（忽略时分秒）
            java.time.LocalDate expireDate = expireTime.toLocalDate();
            java.time.LocalDate today = now.toLocalDate();
            
            // 如果是同一天，说明已过期（因为今天是最后有效日）
            return !expireDate.isAfter(today);
        } catch (Exception e) {
            log.error("解析过期时间失败：{}", expireTimeStr, e);
            return true;
        }
    }

}
