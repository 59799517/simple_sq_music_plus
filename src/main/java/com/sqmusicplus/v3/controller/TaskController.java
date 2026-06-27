package com.sqmusicplus.v3.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqmusicplus.v3.base.entity.DownloadInfo;
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
        downloadInfoLambdaQueryWrapper.orderByDesc(DownloadInfo::getDownloadTime);
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


}
