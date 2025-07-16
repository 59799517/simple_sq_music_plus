package com.sqmusicplus.v3.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqmusicplus.v3.base.entity.DownloadInfo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author sq
 * @since 2023-08-23
 */
public interface DownloadInfoService extends IService<DownloadInfo> {


     Boolean add(DownloadInfo downloadInfo);
     Boolean add(List<DownloadInfo> downloadInfo);
     boolean updateById(DownloadInfo entity);


}
