package com.sqmusicplus.download;

import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.lang.reflect.MethodHandleUtil;
import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqmusicplus.base.entity.DownloadEntity;
import com.sqmusicplus.base.entity.DownloadInfo;
import com.sqmusicplus.base.entity.SqConfig;
import com.sqmusicplus.base.service.DownloadInfoService;
import com.sqmusicplus.base.service.SqConfigService;
import com.sqmusicplus.config.GlobalStatic;
import com.sqmusicplus.config.IgnoreDownloadException;
import com.sqmusicplus.plug.base.PlugBrType;
import com.sqmusicplus.plug.base.hander.SearchHander;
import com.sqmusicplus.plug.utils.TypeUtils;
import com.sqmusicplus.utils.MusicUtils;
import com.sqmusicplus.utils.SpringContextUtil;
import com.sqmusicplus.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.Date;
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
        objectLambdaQueryWrapper.eq(DownloadInfo::getStatus, DownloadStatus.waiting.value);
        long waitsize = downloadInfoService.count(objectLambdaQueryWrapper);

        List<DownloadInfo> records = null;
        if (waitsize>0) {
            SqConfig init_download = configService.getOne(new QueryWrapper<SqConfig>().eq("config_key", "music.download.size"));
            String configValue = init_download.getConfigValue();
            Long downloadsize = Long.valueOf(configValue);
            LambdaQueryWrapper<DownloadInfo> downloadInfoQueryWrapper = new LambdaQueryWrapper<>();
            downloadInfoQueryWrapper.eq(DownloadInfo::getStatus, DownloadStatus.loading.value);
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
                        record.setStatus(DownloadStatus.loading.getValue());
                        downloadInfoService.updateById(record);
                        log.debug("修改进行中状态--->{}",record);
                        DownloadEntity downloadEntity = MusicUtils.downloadInfoToDownloadEntity(record);
                        Object bean = SpringContextUtil.getBean(record.getSpringName());
                        if (bean instanceof SearchHander){
                            SearchHander searchHander = (SearchHander) bean;
                                searchHander.dnonloadAndSaveToFile(downloadEntity, searchHander);
                                //捕获内容
                        }else{
                                ReflectUtil.invoke(bean, "dnonloadAndSaveToFile", downloadEntity, bean);
                        }
                        record.setStatus(DownloadStatus.success.getValue());
                        downloadInfoService.updateById(record);
                        log.debug("修改完成状态--->{}",record);
                    }catch (IgnoreDownloadException e) {
                        //一般是酷我的歌曲信息获取失败导致的需要从新下载
                        record.setStatus(DownloadStatus.waiting.getValue());
                        record.setDownloadMsg(e.getMessage());
                        downloadInfoService.updateById(record);
                        log.debug("修改错误状态--->{}",record);
                    } catch (Exception e) {
                        e.printStackTrace();
                        record.setStatus(DownloadStatus.error.getValue());
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
