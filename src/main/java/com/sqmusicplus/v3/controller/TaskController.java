package com.sqmusicplus.v3.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqmusicplus.v3.base.entity.DownloadInfo;
import com.sqmusicplus.v3.base.entity.DownloadInfoOperation;
import com.sqmusicplus.v3.base.entity.SqSync;
import com.sqmusicplus.v3.base.service.DownloadInfoService;
import com.sqmusicplus.v3.base.service.SqSyncService;
import com.sqmusicplus.v3.config.AjaxResult;
import com.sqmusicplus.v3.download.DownloadStatus;
import com.sqmusicplus.v3.download.vo.DownloadInfoSearch;
import com.sqmusicplus.v3.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Classname TaskController
 * @Description 下载任务
 * @Version 1.0.0
 * @Date 2025/8/15 15:29
 * @Created by SQ
 */
@Slf4j
@RestController
@RequestMapping("/api/task")
public class TaskController {
    @Autowired
    private DownloadInfoService downloadInfoService;
    @Autowired
    private SqSyncService syncService;

    /**
     * 获取任务列表
     * @param downloadInfo
     * @return
     */
    @PostMapping("/list")
    public AjaxResult list(@RequestBody DownloadInfoSearch downloadInfo){
        LambdaQueryWrapper<DownloadInfo> downloadInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        downloadInfoLambdaQueryWrapper.eq(StringUtils.isNotEmpty(downloadInfo.getDownloadStatus()),DownloadInfo::getDownloadStatus, downloadInfo.getDownloadStatus());
        downloadInfoLambdaQueryWrapper.between(downloadInfo.getDownloadTimeStart()!=null&&downloadInfo.getDownloadTimeEnd()!=null,DownloadInfo::getDownloadTime, downloadInfo.getDownloadTimeStart(), downloadInfo.getDownloadTimeEnd());
        downloadInfoLambdaQueryWrapper.like(StringUtils.isNotEmpty(downloadInfo.getDownloadMusicname()),DownloadInfo::getDownloadMusicname, downloadInfo.getDownloadMusicname());
        downloadInfoLambdaQueryWrapper.like(StringUtils.isNotEmpty(downloadInfo.getDownloadArtistname()),DownloadInfo::getDownloadArtistname, downloadInfo.getDownloadArtistname());
        downloadInfoLambdaQueryWrapper.like(StringUtils.isNotEmpty(downloadInfo.getDownloadAlbumname()),DownloadInfo::getDownloadAlbumname, downloadInfo.getDownloadAlbumname());
        downloadInfoLambdaQueryWrapper.eq(StringUtils.isNotEmpty(downloadInfo.getDownloadPlugName()),DownloadInfo::getDownloadPlugName, downloadInfo.getDownloadPlugName());
        downloadInfoLambdaQueryWrapper.eq(downloadInfo.getAudioBook()!=null,DownloadInfo::getAudioBook, downloadInfo.getAudioBook());
        downloadInfoLambdaQueryWrapper.ne(DownloadInfo::getDownloadMusicId, "0");
        downloadInfoLambdaQueryWrapper.isNotNull(DownloadInfo::getDownloadPlugName);
        downloadInfoLambdaQueryWrapper.isNull(DownloadInfo::getParentDownloadId);
        downloadInfoLambdaQueryWrapper.orderByDesc(DownloadInfo::getDownloadUpdateTime);
        Page<DownloadInfo> page = downloadInfoService.page(new Page<>(downloadInfo.getPageIndex(), downloadInfo.getPageSize()),downloadInfoLambdaQueryWrapper);
        return AjaxResult.success(page);
    }

    /**
     * 删除任务
     * @param downloadInfo
     * @return
     */
    @PostMapping("/del")
    public AjaxResult deleteDownloadInfo(@RequestBody DownloadInfo downloadInfo){
        Integer id = downloadInfo.getId();
        if (id!=null){
            LambdaQueryWrapper<DownloadInfo> eq = new LambdaQueryWrapper<DownloadInfo>()
                    .eq(DownloadInfo::getId, id)
                    .or().eq(DownloadInfo::getParentDownloadId, id);
            downloadInfoService.remove(eq);
            return AjaxResult.success();
        }
        try {
            //同时删除同步任务的数据
            DownloadInfo byId = downloadInfoService.getById(id);
            syncService.remove(new LambdaQueryWrapper<SqSync>().eq(SqSync::getMusicId, byId.getDownloadMusicId()));
        } catch (Exception ignored) {
        }

        return AjaxResult.error();

    }

    /**
     * 重新下载任务
     * @param downloadInfo
     * @return
     */
    @PostMapping("/refreshTask")
    public AjaxResult updateDownloadInfo(@RequestBody DownloadInfo downloadInfo){
        Integer id = downloadInfo.getId();
        if (id!=null){
            DownloadInfo updownloadInfo = new DownloadInfo();
            updownloadInfo.setDownloadStatus(DownloadStatus.waiting.getValue());
            updownloadInfo.setId(id);
            downloadInfoService.updateById(updownloadInfo);
            return AjaxResult.success();
        }
        try {
            //同时删除同步任务的数据
            DownloadInfo byId = downloadInfoService.getById(id);
            syncService.remove(new LambdaQueryWrapper<SqSync>().eq(SqSync::getMusicId, byId.getDownloadMusicId()));
        } catch (Exception ignored) {
        }
        return AjaxResult.error();
    }


    /**
     * 重新下载错误任务
     * @return
     */
    @GetMapping("/againTask")
    public AjaxResult againTask(){
        LambdaUpdateWrapper<DownloadInfo> downloadInfoLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        downloadInfoLambdaUpdateWrapper.eq(DownloadInfo::getDownloadStatus, DownloadStatus.error.getValue())
                .ne(DownloadInfo::getDownloadMusicId, "0")
                .isNotNull(DownloadInfo::getDownloadPlugName)
                .set(DownloadInfo::getDownloadStatus, DownloadStatus.waiting.getValue());
        downloadInfoService.update(downloadInfoLambdaUpdateWrapper);

        try {
            ArrayList<String> delIds = new ArrayList<>();
            //同时删除同步任务的数据
            LambdaQueryWrapper<DownloadInfo> downloadInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
            downloadInfoLambdaQueryWrapper.eq(DownloadInfo::getDownloadStatus, DownloadStatus.error.getValue());
            List<DownloadInfo> list = downloadInfoService.list(downloadInfoLambdaQueryWrapper);
            list.stream().forEach(downloadInfo -> {
                delIds.add(downloadInfo.getId().toString());
            });
            syncService.remove(new LambdaQueryWrapper<SqSync>().in(SqSync::getMusicId, delIds));
        } catch (Exception ignored) {
        }

        return AjaxResult.success();
    }

    /**
     * 刷新正在下载的任务（重新下载正在下载的任务）
     * @return
     */
    @GetMapping("/refreshTask")
    public AjaxResult refreshTask(){
        LambdaUpdateWrapper<DownloadInfo> downloadInfoLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        downloadInfoLambdaUpdateWrapper.eq(DownloadInfo::getDownloadStatus, DownloadStatus.loading.getValue())
                .set(DownloadInfo::getDownloadStatus, DownloadStatus.waiting.getValue());
        downloadInfoService.update(downloadInfoLambdaUpdateWrapper);
        return AjaxResult.success();
    }


    /**
     * 删除所有错误任务
     * @return
     */
    @GetMapping("/delErrorTask")
    public AjaxResult delErrorTask(){
        LambdaQueryWrapper<DownloadInfo> downloadInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        downloadInfoLambdaQueryWrapper.eq(DownloadInfo::getDownloadStatus, DownloadStatus.error.getValue());
        try {
            ArrayList<String> delIds = new ArrayList<>();
            //同时删除同步任务的数据
            List<DownloadInfo> list = downloadInfoService.list(downloadInfoLambdaQueryWrapper);
            list.stream().forEach(downloadInfo -> {
                delIds.add(downloadInfo.getId().toString());
            });
            syncService.remove(new LambdaQueryWrapper<SqSync>().in(SqSync::getMusicId, delIds));
        } catch (Exception ignored) {
        }


        downloadInfoService.remove(downloadInfoLambdaQueryWrapper);

        return AjaxResult.success();
    }

    /**
     * 删除成功任务
     * @return
     */
    @GetMapping("/delSuccessTask")
    public AjaxResult delSuccessTask(){
        LambdaQueryWrapper<DownloadInfo> downloadInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        downloadInfoLambdaQueryWrapper.eq(DownloadInfo::getDownloadStatus, DownloadStatus.success.getValue());
        try {
            ArrayList<String> delIds = new ArrayList<>();
            //同时删除同步任务的数据
            List<DownloadInfo> list = downloadInfoService.list(downloadInfoLambdaQueryWrapper);
            list.stream().forEach(downloadInfo -> {
                delIds.add(downloadInfo.getId().toString());
            });
            syncService.remove(new LambdaQueryWrapper<SqSync>().in(SqSync::getMusicId, delIds));
        } catch (Exception ignored) {
        }
        downloadInfoService.remove(downloadInfoLambdaQueryWrapper);

        return AjaxResult.success();
    }

    /**
     * 删除正在等待任务
     * @return
     */
    @GetMapping("/delWaitingTask")
    public AjaxResult delWaitingTask(){
        LambdaQueryWrapper<DownloadInfo> downloadInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        downloadInfoLambdaQueryWrapper.eq(DownloadInfo::getDownloadStatus, DownloadStatus.waiting.getValue());
        downloadInfoService.remove(downloadInfoLambdaQueryWrapper);
        return AjaxResult.success();
    }

    /**
     * 查看下载错误的任务的重新下载子任务信息
     */
    @PostMapping("/errorTaskRetry")
    public AjaxResult errorTaskRetry(@RequestBody DownloadInfo downloadInfo){
        if (downloadInfo.getId()==null){
            return AjaxResult.error("这次下载无自动下载任务或者下载任务是空!");
        }
        LambdaQueryWrapper<DownloadInfo> downloadInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        downloadInfoLambdaQueryWrapper.eq(DownloadInfo::getParentDownloadId, downloadInfo.getId());
        List<DownloadInfo> list = downloadInfoService.list(downloadInfoLambdaQueryWrapper);
        return AjaxResult.success(list);
    }
    /**
     * 高级任务处理
     */
    @PostMapping("/advancedTask")
    public AjaxResult advancedTask(@RequestBody DownloadInfoOperation downloadInfo){
            if (StringUtils.isBlank(downloadInfo.getOperationType())){
            return AjaxResult.error("需要选择操作类型！");
        }
        if (downloadInfo.getOperationType().equals("delete")){
            LambdaQueryWrapper<DownloadInfo> downloadInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
            // //支持根据下载时间  范围内 删除，重试
            downloadInfoLambdaQueryWrapper.between(downloadInfo.getDownloadCreateTimeStart()!=null&&downloadInfo.getDownloadCreateTimeEnd()!=null, DownloadInfo::getDownloadUpdateTime, downloadInfo.getDownloadCreateTimeStart() ,downloadInfo.getDownloadCreateTimeEnd());
            //支持根据插件名称删除，重试
            downloadInfoLambdaQueryWrapper.eq(StringUtils.isNotBlank(downloadInfo.getDownloadPlugName()), DownloadInfo::getDownloadPlugName, downloadInfo.getDownloadPlugName());
            //支持根据download_br_type删除，重试
            downloadInfoLambdaQueryWrapper.eq(StringUtils.isNotBlank(downloadInfo.getDownloadBrType()), DownloadInfo::getDownloadBrType, downloadInfo.getDownloadBrType());
            //支持根据download_musicname（相等）删除，重试
            downloadInfoLambdaQueryWrapper.eq(StringUtils.isNotBlank(downloadInfo.getDownloadMusicname()), DownloadInfo::getDownloadMusicname, downloadInfo.getDownloadMusicname());
            //支持根据download_artistname（相等）删除，重试
            downloadInfoLambdaQueryWrapper.eq(StringUtils.isNotBlank(downloadInfo.getDownloadArtistname()), DownloadInfo::getDownloadArtistname, downloadInfo.getDownloadArtistname());
            //支持根据download_albumname（相等）删除，重试
            downloadInfoLambdaQueryWrapper.eq(StringUtils.isNotBlank(downloadInfo.getDownloadAlbumname()), DownloadInfo::getDownloadAlbumname, downloadInfo.getDownloadAlbumname());
            //支持根据download_status（相等）删除，重试
            downloadInfoLambdaQueryWrapper.eq(StringUtils.isNotBlank(downloadInfo.getDownloadStatus()), DownloadInfo::getDownloadStatus, downloadInfo.getDownloadStatus());
            //支持根据download_update_time 范围内 删除，重试
            downloadInfoLambdaQueryWrapper.between(downloadInfo.getDownloadUpdateTimeStart()!=null && downloadInfo.getDownloadUpdateTimeEnd()!=null, DownloadInfo::getDownloadUpdateTime, downloadInfo.getDownloadUpdateTimeStart(), downloadInfo.getDownloadUpdateTimeEnd());
            boolean remove = downloadInfoService.remove(downloadInfoLambdaQueryWrapper);
            return remove?AjaxResult.success():AjaxResult.error("删除失败！");
        }else if (downloadInfo.getOperationType().equals("rewrite")){
            LambdaUpdateWrapper<DownloadInfo> downloadInfoLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            // //支持根据下载时间  范围内 删除，重试
            downloadInfoLambdaUpdateWrapper.between(downloadInfo.getDownloadCreateTimeStart()!=null&&downloadInfo.getDownloadCreateTimeEnd()!=null, DownloadInfo::getDownloadUpdateTime, downloadInfo.getDownloadCreateTimeStart() ,downloadInfo.getDownloadCreateTimeEnd());
            //支持根据插件名称删除，重试
            downloadInfoLambdaUpdateWrapper.eq(StringUtils.isNotBlank(downloadInfo.getDownloadPlugName()), DownloadInfo::getDownloadPlugName, downloadInfo.getDownloadPlugName());
            //支持根据download_br_type删除，重试
            downloadInfoLambdaUpdateWrapper.eq(StringUtils.isNotBlank(downloadInfo.getDownloadBrType()), DownloadInfo::getDownloadBrType, downloadInfo.getDownloadBrType());
            //支持根据download_musicname（相等）删除，重试
            downloadInfoLambdaUpdateWrapper.eq(StringUtils.isNotBlank(downloadInfo.getDownloadMusicname()), DownloadInfo::getDownloadMusicname, downloadInfo.getDownloadMusicname());
            //支持根据download_artistname（相等）删除，重试
            downloadInfoLambdaUpdateWrapper.eq(StringUtils.isNotBlank(downloadInfo.getDownloadArtistname()), DownloadInfo::getDownloadArtistname, downloadInfo.getDownloadArtistname());
            //支持根据download_albumname（相等）删除，重试
            downloadInfoLambdaUpdateWrapper.eq(StringUtils.isNotBlank(downloadInfo.getDownloadAlbumname()), DownloadInfo::getDownloadAlbumname, downloadInfo.getDownloadAlbumname());
            //支持根据download_status（相等）删除，重试
            downloadInfoLambdaUpdateWrapper.eq(StringUtils.isNotBlank(downloadInfo.getDownloadStatus()), DownloadInfo::getDownloadStatus, downloadInfo.getDownloadStatus());
            //支持根据download_update_time 范围内 删除，重试
            downloadInfoLambdaUpdateWrapper.between(downloadInfo.getDownloadUpdateTimeStart()!=null && downloadInfo.getDownloadUpdateTimeEnd()!=null, DownloadInfo::getDownloadUpdateTime, downloadInfo.getDownloadUpdateTimeStart(), downloadInfo.getDownloadUpdateTimeEnd());
            downloadInfoLambdaUpdateWrapper.set(DownloadInfo::getDownloadStatus, DownloadStatus.waiting.getValue());
            downloadInfoLambdaUpdateWrapper.set(DownloadInfo::getDownloadRetryNum, 0);
            downloadInfoLambdaUpdateWrapper.set(DownloadInfo::getDownloadRetryTime, null);
            downloadInfoLambdaUpdateWrapper.set(DownloadInfo::getDownloadUpdateTime, new Date());
            boolean update = downloadInfoService.update(downloadInfoLambdaUpdateWrapper);
            return update?AjaxResult.success("重试成功！") : AjaxResult.error("重试失败！");
        }
        return AjaxResult.error("操作失败！（操作类型不在支持的范围内，目前支持删除、重试）");
    }


}
