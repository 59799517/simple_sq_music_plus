package com.sqmusicplus.v3.base.service.impl;


import com.sqmusicplus.v3.base.entity.DownloadInfo;
import com.sqmusicplus.v3.base.mapper.DownloadInfoMapper;
import com.sqmusicplus.v3.base.service.DownloadInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqmusicplus.v3.download.DownloadStatus;
import com.sqmusicplus.v3.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author sq
 * @since 2023-08-23
 */
@Service
public class DownloadInfoServiceImpl extends ServiceImpl<DownloadInfoMapper, DownloadInfo> implements DownloadInfoService {


    @Autowired
    private  DownloadInfoServiceImpl downloadInfoService;



    @Override
    public Boolean add(DownloadInfo downloadInfo) {
        downloadInfo.setDownloadStatus(DownloadStatus.waiting.getValue());
        boolean save = downloadInfoService.save(downloadInfo);
        return  save;
    }

    @Override
    public Boolean add(List<DownloadInfo> downloadInfo) {
        downloadInfo.forEach(e->e.setDownloadStatus(DownloadStatus.waiting.getValue()));
        boolean save = downloadInfoService.saveBatch(downloadInfo);
        return  save;
    }

    @Override
    public synchronized boolean updateById(DownloadInfo entity) {
        entity.setDownloadUpdateTime(DateUtils.getNowDate());
        return super.updateById(entity);
    }
}
