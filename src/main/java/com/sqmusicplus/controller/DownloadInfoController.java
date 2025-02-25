package com.sqmusicplus.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqmusicplus.base.entity.DownloadInfo;
import com.sqmusicplus.base.entity.vo.DownloadInfoSearch;
import com.sqmusicplus.base.service.DownloadInfoService;
import com.sqmusicplus.config.AjaxResult;
import com.sqmusicplus.download.DownloadStatus;
import com.sqmusicplus.task.ScanQQVIPLikeMusicTask;
import com.sqmusicplus.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author sq
 * @since 2023-08-23
 */
@RestController
@RequestMapping("/downloadInfo")
public class DownloadInfoController {

    @Autowired
    private ScanQQVIPLikeMusicTask scanQQVIPLikeMusic;


    @Autowired
    private DownloadInfoService downloadInfoService;

    @GetMapping("/getDownloadInfo/{type}/{pageSize}/{pageIndex}")
    public AjaxResult getDownloadInfo(@PathVariable("type") String type, @PathVariable("pageSize") Integer pageSize, @PathVariable("pageIndex") Integer pageIndex){
        LambdaQueryWrapper<DownloadInfo> downloadInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        downloadInfoLambdaQueryWrapper.eq(DownloadInfo::getStatus, type);
        downloadInfoLambdaQueryWrapper.orderByDesc(DownloadInfo::getDownloadTime);
        Page<DownloadInfo> page = downloadInfoService.page(new Page<>(pageIndex, pageSize),downloadInfoLambdaQueryWrapper);
        return AjaxResult.success(page);
    }

    @PostMapping("/getDownloadInfo/search")
    public AjaxResult getDownloadInfo(@RequestBody DownloadInfoSearch downloadInfo){
        LambdaQueryWrapper<DownloadInfo> downloadInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        downloadInfoLambdaQueryWrapper.eq(StringUtils.isNotEmpty(downloadInfo.getStatus()),DownloadInfo::getStatus, downloadInfo.getStatus());
        downloadInfoLambdaQueryWrapper.between(downloadInfo.getDownloadTimeStart()!=null&&downloadInfo.getDownloadTimeEnd()!=null,DownloadInfo::getDownloadTime, downloadInfo.getDownloadTimeStart(), downloadInfo.getDownloadTimeEnd());
        downloadInfoLambdaQueryWrapper.like(StringUtils.isNotEmpty(downloadInfo.getDownloadMusicname()),DownloadInfo::getDownloadMusicname, downloadInfo.getDownloadMusicname());
        downloadInfoLambdaQueryWrapper.like(StringUtils.isNotEmpty(downloadInfo.getDownloadArtistname()),DownloadInfo::getDownloadArtistname, downloadInfo.getDownloadArtistname());
        downloadInfoLambdaQueryWrapper.like(StringUtils.isNotEmpty(downloadInfo.getDownloadAlbumname()),DownloadInfo::getDownloadAlbumname, downloadInfo.getDownloadAlbumname());
        downloadInfoLambdaQueryWrapper.eq(StringUtils.isNotEmpty(downloadInfo.getDownloadType()),DownloadInfo::getDownloadType, downloadInfo.getDownloadType());
        downloadInfoLambdaQueryWrapper.eq(StringUtils.isNotEmpty(downloadInfo.getAudioBook()),DownloadInfo::getAudioBook, downloadInfo.getAudioBook());
        downloadInfoLambdaQueryWrapper.orderByDesc(DownloadInfo::getDownloadTime);
        Page<DownloadInfo> page = downloadInfoService.page(new Page<>(downloadInfo.getPageIndex(), downloadInfo.getPageSize()),downloadInfoLambdaQueryWrapper);
        return AjaxResult.success(page);
    }

    /**
     * 删除任务
     * @param downloadInfo
     * @return
     */
    @PostMapping("/deleteDownloadInfo")
    public AjaxResult deleteDownloadInfo(@RequestBody DownloadInfo downloadInfo){
        Integer id = downloadInfo.getId();
        if (id!=null){
            downloadInfoService.removeById(id);
            return AjaxResult.success();
        }
        return AjaxResult.error();

    }
    @PostMapping("/refresh/status")
    public AjaxResult updateDownloadInfo(@RequestBody DownloadInfo downloadInfo){
        Integer id = downloadInfo.getId();
        if (id!=null){
            DownloadInfo updownloadInfo = new DownloadInfo();
            updownloadInfo.setStatus(DownloadStatus.waiting.getValue());
            updownloadInfo.setId(id);
            downloadInfoService.updateById(updownloadInfo);
            return AjaxResult.success();
        }
        return AjaxResult.error();
    }



    @GetMapping("/delAllTask")
    public AjaxResult delAllTask(){
        QueryWrapper<DownloadInfo> downloadInfoQueryWrapper = new QueryWrapper<>();
        downloadInfoQueryWrapper.eq(DownloadStatus.error.getValue(),1);
        downloadInfoService.remove(downloadInfoQueryWrapper);
        return AjaxResult.success();
    }


    @SaCheckLogin
    @GetMapping("/delErrorTask")
    public AjaxResult delErrorTask(){
        LambdaQueryWrapper<DownloadInfo> downloadInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        downloadInfoLambdaQueryWrapper.eq(DownloadInfo::getStatus, DownloadStatus.error.getValue());
        downloadInfoService.remove(downloadInfoLambdaQueryWrapper);
        return AjaxResult.success();
    }
    @SaCheckLogin
    @GetMapping("/delSuccessTask")
    public AjaxResult delSuccessTask(){
        LambdaQueryWrapper<DownloadInfo> downloadInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        downloadInfoLambdaQueryWrapper.eq(DownloadInfo::getStatus, DownloadStatus.success.getValue());
        downloadInfoService.remove(downloadInfoLambdaQueryWrapper);
        return AjaxResult.success();
    }
    @SaCheckLogin
    @GetMapping("/delWaitingTask")
    public AjaxResult delWaitingTask(){
        LambdaQueryWrapper<DownloadInfo> downloadInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        downloadInfoLambdaQueryWrapper.eq(DownloadInfo::getStatus, DownloadStatus.waiting.getValue());
        downloadInfoService.remove(downloadInfoLambdaQueryWrapper);
        return AjaxResult.success();
    }

    /**
     * 重新下载错误任务
     * @return
     */
    @SaCheckLogin
    @GetMapping("/againTask")
    public AjaxResult againTask(){
        LambdaUpdateWrapper<DownloadInfo> downloadInfoLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        downloadInfoLambdaUpdateWrapper.eq(DownloadInfo::getStatus, DownloadStatus.error.getValue())
                        .set(DownloadInfo::getStatus, DownloadStatus.waiting.getValue());
        downloadInfoService.update(downloadInfoLambdaUpdateWrapper);
        return AjaxResult.success();
    }
    @SaCheckLogin
    @GetMapping("/refreshTask")
    public AjaxResult refreshTask(){
           LambdaUpdateWrapper<DownloadInfo> downloadInfoLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
           downloadInfoLambdaUpdateWrapper.eq(DownloadInfo::getStatus, DownloadStatus.loading.getValue())
                   .set(DownloadInfo::getStatus, DownloadStatus.waiting.getValue());
           downloadInfoService.update(downloadInfoLambdaUpdateWrapper);
           return AjaxResult.success();
    }


    /**
     * 刷新QQvip下载配置遵循数据库配置
     * @return
     */
    @SaCheckLogin
    @GetMapping("/refreshQQvipTask")
    public AjaxResult refreshQQvipTask(){
        scanQQVIPLikeMusic.excute();
        return AjaxResult.success();
    }
}
