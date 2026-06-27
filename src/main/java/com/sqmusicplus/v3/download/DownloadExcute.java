package com.sqmusicplus.v3.download;

import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqmusicplus.v3.base.entity.DownloadInfo;
import com.sqmusicplus.v3.base.enums.PlugBrType;
import com.sqmusicplus.v3.base.enums.SetConfigEnum;
import com.sqmusicplus.v3.base.service.DownloadInfoService;
import com.sqmusicplus.v3.config.SqConfigCache;
import com.sqmusicplus.v3.config.exception.IgnoreDownloadException;
import com.sqmusicplus.v3.download.DownloadRetryService;
import com.sqmusicplus.v3.plug.base.hander.SearchHander;
import com.sqmusicplus.v3.utils.SpringContextUtil;
import com.sqmusicplus.v3.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * @Classname DownloadExcute
 * @Description 下载执行器
 * @Version 1.0.0
 * @Date 2023/8/23 14:31
 * @Created by SQ
 */

@Slf4j
@Service
@Lazy
public class DownloadExcute {

    @Autowired
    private DownloadInfoService downloadInfoService;
    @Autowired
    @Qualifier("threadPoolTaskExecutor")
    private ExecutorService executorService;

    @Autowired
    private DownloadRetryService downloadRetryService;


    public void getDownloadInfo() {
        boolean qqdownload = false;
        //限额数量
        String plug_qqvip_download_daily_limit = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_QQVIP_DOWNLOAD_DAILY_LIMIT);
        if (StringUtils.isNotBlank(plug_qqvip_download_daily_limit)){
            try {
                long QQlimit = Long.parseLong(plug_qqvip_download_daily_limit);
                // 0或小于0表示不限制下载数量
                if (QQlimit > 0) {
                    long QQtoday = getQQVipTodayCountFromDb();
                    if (QQtoday >= QQlimit) {
                        //超出限制
                        log.debug("QQvip今日下载数量已超出限制{},明日在下载！", QQlimit);
                        qqdownload = true;
                    }
                }
            } catch (NumberFormatException e) {
                log.warn("解析QQVIP每日下载限额失败: {}", plug_qqvip_download_daily_limit, e);
            }
        }



        LambdaQueryWrapper<DownloadInfo> objectLambdaQueryWrapper = new LambdaQueryWrapper<>();
        objectLambdaQueryWrapper.eq(DownloadInfo::getDownloadStatus, DownloadStatus.waiting.value)
                .ne(qqdownload,DownloadInfo::getDownloadPlugName, PlugBrType.QQVIP_Flac_2000.getPlugName());
        long waitsize = downloadInfoService.count(objectLambdaQueryWrapper);

        List<DownloadInfo> records = null;
        if (waitsize>0) {
            String init_download = SqConfigCache.getSqConfigValue(SetConfigEnum.SYSTEM_DOWNLOAD_NUM);
            if (StringUtils.isBlank(init_download)) {
                log.warn("SYSTEM_DOWNLOAD_NUM 未配置，默认使用 5");
                init_download = "5";
            }
            Long downloadsize = Long.valueOf(init_download);
            LambdaQueryWrapper<DownloadInfo> downloadInfoQueryWrapper = new LambdaQueryWrapper<>();
            downloadInfoQueryWrapper.eq(DownloadInfo::getDownloadStatus, DownloadStatus.loading.value);
            long count = downloadInfoService.count(downloadInfoQueryWrapper);
            log.info("正在下载任务--->{}个",count);
            if (count-downloadsize<0){
                long l = downloadsize - count;
                Page<DownloadInfo> page = downloadInfoService.page(new Page<>(0, l), objectLambdaQueryWrapper);
                records = page.getRecords();
                log.info("本次补充--->{}个",records.size());
            }
        }
        if (records != null && records.size() > 0) {
            for (DownloadInfo record : records) {
                executorService.execute(() -> {
                    try {
                        record.setDownloadStatus(DownloadStatus.loading.getValue());
                        downloadInfoService.updateById(record);
                        log.debug("修改进行中状态--->{}",record);
//                        DownloadEntity downloadEntity = MusicUtils.downloadInfoToDownloadEntity(record);
                        Object bean = SpringContextUtil.getBean(record.getSpringName());
                        if (bean instanceof SearchHander){
                            SearchHander searchHander = (SearchHander) bean;
                            try {
                                searchHander.dnonloadAndSaveToFile(record, searchHander);
                                //捕获内容
                                record.setDownloadStatus(DownloadStatus.success.getValue());
                                downloadInfoService.updateById(record);
                                log.debug("修改完成状态--->{}",record);
                                // QQVIP下载成功后立即更新今日下载计数到DB
                                if (PlugBrType.QQVIP_Flac_2000.getPlugName().equals(record.getDownloadPlugName())) {
                                    incrementQQVipDownloadCount();
                                }
                            } catch (IgnoreDownloadException e) {
                                //一般是酷我的歌曲信息获取失败导致的需要从新下载
                                record.setDownloadStatus(DownloadStatus.waiting.getValue());
                                record.setDownloadMsg(e.getMessage());
                                downloadInfoService.updateById(record);
                            }catch (Exception e){
                                e.printStackTrace();
                                // 下载失败，尝试使用其他插件重试
                                boolean b = downloadRetryService.retryWithOtherPlugin(record);
                                if(!b){
                                    // 原记录始终标记为 error 避免阻塞下载队列
                                    record.setDownloadStatus(DownloadStatus.error.getValue());
                                    record.setDownloadMsg(e.getMessage());
                                    downloadInfoService.updateById(record);
                                    log.debug("修改错误状态--->{}",record);
                                }

                            }
                        }else{
                            try {
                                ReflectUtil.invoke(bean, "dnonloadAndSaveToFile", record, bean);
                                record.setDownloadStatus(DownloadStatus.success.getValue());
                                downloadInfoService.updateById(record);
                                log.debug("修改完成状态--->{}",record);
                                // QQVIP下载成功后立即更新今日下载计数到DB
                                if (PlugBrType.QQVIP_Flac_2000.getPlugName().equals(record.getDownloadPlugName())) {
                                    incrementQQVipDownloadCount();
                                }
                            } catch (IgnoreDownloadException e) {
                                //一般是酷我的歌曲信息获取失败导致的需要从新下载
                                record.setDownloadStatus(DownloadStatus.waiting.getValue());
                                record.setDownloadMsg(e.getMessage());
                                downloadInfoService.updateById(record);
                            }catch (Exception e){
                                e.printStackTrace();
                                // 下载失败，尝试使用其他插件重试
                                downloadRetryService.retryWithOtherPlugin(record);
                                // 原记录始终标记为 error 避免阻塞下载队列
                                record.setDownloadStatus(DownloadStatus.error.getValue());
                                record.setDownloadMsg(e.getMessage());
                                downloadInfoService.updateById(record);
                                log.debug("修改错误状态--->{}",record);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        record.setDownloadStatus(DownloadStatus.error.getValue());
                        record.setDownloadMsg(e.getMessage());
                        downloadInfoService.updateById(record);
                        log.debug("修改错误状态--->{}",record);
                    }
                });
            }
        }
        
    }

    /**
     * 从DB读取今日下载计数
     */
    private long getQQVipTodayCountFromDb() {
        try {
            String currentCountStr = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_QQVIP_DOWNLOAD_TODAY);
            if (StringUtils.isNotBlank(currentCountStr)) {
                return Long.parseLong(currentCountStr);
            }
        } catch (NumberFormatException e) {
            log.warn("解析QQVIP今日下载计数失败", e);
        }
        return 0;
    }

    /**
     * QQVIP下载成功后立即更新今日下载计数到DB
     * 从DB读取当前值，加1后更新回去
     */
    private void incrementQQVipDownloadCount() {
        try {
            long currentCount = getQQVipTodayCountFromDb();
            long newCount = currentCount + 1;
            SqConfigCache.updateConfigToDb(SetConfigEnum.PLUG_QQVIP_DOWNLOAD_TODAY, String.valueOf(newCount));
            log.debug("QQVIP今日下载计数更新: {} -> {}", currentCount, newCount);
        } catch (Exception e) {
            log.error("更新QQVIP下载计数失败", e);
        }
    }




//    public DownloadEntity addSubsonicPlayList(DownloadEntity downloadEntity) {
//        String addSubsonicPlayListName = downloadEntity.getAddSubsonicPlayListName();
//            if (StringUtils.isNotEmpty(addSubsonicPlayListName)) {
//                log.debug("需要添加到第三方中--->{}",downloadEntity);
//                SyncTask syncTask = SpringContextUtil.getBean(SyncTask.class);
//                syncTask.excute(downloadEntity);
//            }
//
//        return downloadEntity;
//    }




}
