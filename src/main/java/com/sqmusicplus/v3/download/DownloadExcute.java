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
import java.util.concurrent.atomic.AtomicInteger;

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
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    /**
     * 本次执行中QQVIP下载成功计数(线程安全)
     */
    private final AtomicInteger qqVipSuccessCount = new AtomicInteger(0);


    public void getDownloadInfo() {
        // 重置本次计数
        qqVipSuccessCount.set(0);
        
        boolean qqdownload = false;
        //限额数量
        String plug_qqvip_download_daily_limit = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_QQVIP_DOWNLOAD_DAILY_LIMIT);
        String plug_qqvip_download_today = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_QQVIP_DOWNLOAD_TODAY);
        if (StringUtils.isNotBlank(plug_qqvip_download_daily_limit)){
            //有限额
            if (StringUtils.isBlank(plug_qqvip_download_today)){
                SqConfigCache.updateConfigToDb(SetConfigEnum.PLUG_QQVIP_DOWNLOAD_TODAY, "0");
            }
            long QQtoday = Long.parseLong(plug_qqvip_download_today);
            long QQlimit = Long.parseLong(plug_qqvip_download_daily_limit);
            if (QQtoday>=QQlimit){
                //超出限制
                log.debug("QQvip今日下载数量已超出限制,明日在下载！");
                qqdownload=true;
            }
        }



        LambdaQueryWrapper<DownloadInfo> objectLambdaQueryWrapper = new LambdaQueryWrapper<>();
        objectLambdaQueryWrapper.eq(DownloadInfo::getDownloadStatus, DownloadStatus.waiting.value)
                .ne(qqdownload,DownloadInfo::getDownloadPlugName, PlugBrType.QQVIP_Flac_2000.getPlugName());
        long waitsize = downloadInfoService.count(objectLambdaQueryWrapper);

        List<DownloadInfo> records = null;
        if (waitsize>0) {
            String init_download = SqConfigCache.getSqConfigValue(SetConfigEnum.SYSTEM_DOWNLOAD_NUM);
            Long downloadsize = Long.valueOf(init_download);
            LambdaQueryWrapper<DownloadInfo> downloadInfoQueryWrapper = new LambdaQueryWrapper<>();
            downloadInfoQueryWrapper.eq(DownloadInfo::getDownloadStatus, DownloadStatus.loading.value);
            long count = downloadInfoService.count(downloadInfoQueryWrapper);
            log.debug("正在下载任务--->{}个",count);
            if (count-downloadsize<0){
                long l = downloadsize - count;
                Page<DownloadInfo> page = downloadInfoService.page(new Page<>(0, l), objectLambdaQueryWrapper);
                records = page.getRecords();
                log.debug("本次补充--->{}个",records.size());
            }
        }
        if (records != null && records.size() > 0) {
            for (DownloadInfo record : records) {
                threadPoolTaskExecutor.execute(() -> {
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
                                // 如果是QQVIP下载，递增内存计数
                                if (PlugBrType.QQVIP_Flac_2000.getPlugName().equals(record.getDownloadPlugName())) {
                                    qqVipSuccessCount.incrementAndGet();
                                }
                            } catch (IgnoreDownloadException e) {
                                //一般是酷我的歌曲信息获取失败导致的需要从新下载
                                record.setDownloadStatus(DownloadStatus.waiting.getValue());
                                record.setDownloadMsg(e.getMessage());
                                downloadInfoService.updateById(record);
                                return;
                            }catch (Exception e){
                                e.printStackTrace();
                                record.setDownloadStatus(DownloadStatus.error.getValue());
                                record.setDownloadMsg(e.getMessage());
                                downloadInfoService.updateById(record);
                                log.debug("修改错误状态--->{}",record);
                            }
                        }else{
                            try {
                                ReflectUtil.invoke(bean, "dnonloadAndSaveToFile", record, bean);
                                record.setDownloadStatus(DownloadStatus.success.getValue());
                                downloadInfoService.updateById(record);
                                log.debug("修改完成状态--->{}",record);
                                // 如果是QQVIP下载，递增内存计数
                                if (PlugBrType.QQVIP_Flac_2000.getPlugName().equals(record.getDownloadPlugName())) {
                                    qqVipSuccessCount.incrementAndGet();
                                }
                            } catch (IgnoreDownloadException e) {
                                //一般是酷我的歌曲信息获取失败导致的需要从新下载
                                record.setDownloadStatus(DownloadStatus.waiting.getValue());
                                record.setDownloadMsg(e.getMessage());
                                downloadInfoService.updateById(record);
                                return;
                            }catch (Exception e){
                                e.printStackTrace();
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
        
        // 本次执行结束后，统一更新QQVIP下载计数到数据库
        flushQQVipDownloadCount();
    }

    /**
     * 将内存中的QQVIP下载计数刷新到数据库
     * 在每次getDownloadInfo()执行结束时调用，避免频繁IO
     */
    private void flushQQVipDownloadCount() {
        int successCount = qqVipSuccessCount.get();
        if (successCount == 0) {
            return;
        }
        
        try {
            // 获取当前计数
            String todayCountStr = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_QQVIP_DOWNLOAD_TODAY);
            long todayCount = 0;
            if (StringUtils.isNotBlank(todayCountStr)) {
                try {
                    todayCount = Long.parseLong(todayCountStr);
                } catch (NumberFormatException e) {
                    log.warn("解析QQVIP今日下载计数失败，重置为0", e);
                    todayCount = 0;
                }
            }
            
            // 累加本次成功的数量
            todayCount += successCount;
            
            // 一次性更新到数据库和缓存
            SqConfigCache.updateConfigToDb(SetConfigEnum.PLUG_QQVIP_DOWNLOAD_TODAY, String.valueOf(todayCount));
            log.info("本次批量更新QQVIP下载计数: +{}, 总计: {}", successCount, todayCount);
            
        } catch (Exception e) {
            log.error("批量更新QQVIP下载计数失败", e);
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
