package com.sqmusicplus.v3.download;

import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqmusicplus.base.entity.DownloadEntity;
import com.sqmusicplus.base.entity.SqConfig;
import com.sqmusicplus.base.service.SqConfigService;
import com.sqmusicplus.plug.base.hander.SearchHander;
import com.sqmusicplus.v3.base.entity.DownloadInfo;
import com.sqmusicplus.v3.base.enums.SetConfigEnum;
import com.sqmusicplus.v3.base.service.DownloadInfoService;
import com.sqmusicplus.v3.utils.MusicUtils;
import com.sqmusicplus.v3.utils.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.List;

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
    private SqConfigService configService;
    @Autowired
    private DownloadInfoService downloadInfoService;
    @Autowired
    @Qualifier("threadPoolTaskExecutor")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;


    public void getDownloadInfo() {




        LambdaQueryWrapper<DownloadInfo> objectLambdaQueryWrapper = new LambdaQueryWrapper<>();
        objectLambdaQueryWrapper.eq(DownloadInfo::getDownloadStatus, DownloadStatus.waiting.value);
        long waitsize = downloadInfoService.count(objectLambdaQueryWrapper);

        List<DownloadInfo> records = null;
        if (waitsize>0) {
            LambdaQueryWrapper<SqConfig> downloadSizeQueryWrapper = new LambdaQueryWrapper<>();
            downloadSizeQueryWrapper.eq(SqConfig::getConfigKey, SetConfigEnum.SYSTEM_DOWNLOAD_NUM.getKey());
            SqConfig init_download = configService.getOne(downloadSizeQueryWrapper);
            String configValue = init_download.getConfigValue();
            Long downloadsize = Long.valueOf(configValue);
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
                            searchHander.dnonloadAndSaveToFile(record, searchHander);
                        }else{
                            ReflectUtil.invoke(bean, "dnonloadAndSaveToFile", record, bean);
                        }
                        record.setDownloadStatus(DownloadStatus.success.getValue());
                        downloadInfoService.updateById(record);
                        log.debug("修改完成状态--->{}",record);
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
