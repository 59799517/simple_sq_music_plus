package com.sqmusicplus.v3.task;

import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sqmusicplus.v3.base.entity.DownloadInfo;
import com.sqmusicplus.v3.base.entity.SqSync;
import com.sqmusicplus.v3.base.enums.DbBooleanConvert;
import com.sqmusicplus.v3.base.enums.PlugBrType;
import com.sqmusicplus.v3.base.enums.SetConfigEnum;
import com.sqmusicplus.v3.base.service.DownloadInfoService;
import com.sqmusicplus.v3.base.service.SqSyncService;
import com.sqmusicplus.v3.config.AjaxResult;
import com.sqmusicplus.v3.config.SqConfigCache;
import com.sqmusicplus.v3.monitor.entity.SqMonitor;
import com.sqmusicplus.v3.monitor.enums.MonitorType;
import com.sqmusicplus.v3.monitor.service.SqMonitorService;
import com.sqmusicplus.v3.plug.entity.Music;
import com.sqmusicplus.v3.plug.netease.entity.PlaylistTrackAllResult;
import com.sqmusicplus.v3.plug.netease.hander.NeteaseHander;
import com.sqmusicplus.v3.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Classname ScanNeteasePlayList
 * @Description 扫描网易云歌单
 * @Version 1.0.0
 * @Date 2026/3/2
 * @Created by SQ
 */
@Slf4j
@Component
public class ScanNeteasePlayList {

    @Autowired
    private NeteaseHander neteaseHander;

    @Autowired
    private SqMonitorService monitorService;

    @Autowired
    private DownloadInfoService downloadInfoService;

    @Autowired
    private SqSyncService syncService;


    @Scheduled(cron = "10 */1 * * * ? ")
    public void excute() {
        try {
            String netopen = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_NETEASE_OPEN);
            if (StringUtils.isNotBlank(netopen)) {
                log.info("开始扫描网易云歌单");
                queryAndDownloadPlayList();
            }
        } catch (Exception e) { // 必须捕获 Throwable，避免OOM等严重异常也漏抓
            e.printStackTrace();
            log.error("网易云歌单扫描定时任务执行异常，等待重试！", e);
        }
    }
    //查询需要扫描的歌单

    private void queryAndDownloadPlayList() {
        ArrayList<String> excludeArtistNames = new ArrayList<>();
        ArrayList<String> excludeAlbumNames = new ArrayList<>();
        LambdaQueryWrapper<SqMonitor> sqMonitorLambdaQueryWrapper = new LambdaQueryWrapper<SqMonitor>()
                .eq(SqMonitor::getPlugName, neteaseHander.getPlugName())
                        .eq(SqMonitor::getEnabled, DbBooleanConvert.YES.getValue().intValue())
                                .eq(SqMonitor::getType, MonitorType.PLAYLIST.getCode());
        List<SqMonitor> list = monitorService.list(sqMonitorLambdaQueryWrapper);
        if (!list.isEmpty()){
            //忽略专辑
            String excludeAlbum = SqConfigCache.getSqConfigValue(SetConfigEnum.SYSTEM_SYNC_ALBUM_EXCLUDE);
            //忽略歌手
            String excludeArtists = SqConfigCache.getSqConfigValue(SetConfigEnum.SYSTEM_SYNC_ARTISTS_EXCLUDE);
            //不同步的歌手名称
            if (StringUtils.isNotBlank(excludeArtists)) {
                String[] split = excludeArtists.split("\\|");
                if (split != null) {
                    for (String s : split) {
                        excludeArtistNames.add(s.trim());
                    }
                }
            }
//            不同步的专辑
            if (StringUtils.isNotBlank(excludeAlbum)) {
                String[] split = excludeAlbum.split("\\|");
                if (split != null) {
                    for (String s : split) {
                        excludeAlbumNames.add(s.trim());
                    }
                }
            }

            for (SqMonitor sqMonitor : list) {
                String targetId = sqMonitor.getTargetId();
                try {
                    PlaylistTrackAllResult playListInfo = neteaseHander.getPlayListInfo(targetId);
                    Long trackCount = playListInfo.getPlaylist().getTrackCount();
                    sqMonitor.setUpdateTime(new Date());
                    sqMonitor.setTargetCount(trackCount);
                    if (StringUtils.isNotBlank(playListInfo.getPlaylist().getCoverImgUrl())){
                        sqMonitor.setTargetCover(playListInfo.getPlaylist().getCoverImgUrl());
                    }
                    monitorService.updateById(sqMonitor);
                } catch (Exception e) {
                    log.error("监听网易云歌单信息失败: targetId=" + targetId, e);
                }


                ArrayList<SqSync> sqSyncs = new ArrayList<>();

                ArrayList<Music> playList = neteaseHander.getPlayList(targetId);
                if (playList != null){
                    //获取已经下载的歌曲
                    LambdaQueryWrapper<SqSync> sqQqmusicMyLike = new LambdaQueryWrapper<SqSync>().eq(SqSync::getPlugName, neteaseHander.getPlugName())
                            .eq(SqSync::getPlayListId, targetId);
                    
                    List<SqSync> dbSqSync = syncService.list(sqQqmusicMyLike);
                    Set<String> downloadedMusicIds = dbSqSync.stream()
                            .map(SqSync::getMusicId)
                            .collect(Collectors.toSet());
                                        
                    ArrayList<DownloadInfo> downloadInfos = new ArrayList<>();
                    for (Music music : playList) {
                        // 跳过已经下载过的歌曲，只处理未下载的新歌曲
                        if (downloadedMusicIds.contains(music.getId())){
                            continue;
                        }
                        if (excludeAlbumNames.contains(music.getMusicAlbum())) {
                            continue;
                        }
                        List<String> musicArtists = music.getMusicArtists();
                        boolean needExclude = checkNeedExclude(excludeArtistNames, musicArtists);
                        if (needExclude) {
                            continue;
                        }
                        DownloadInfo downloadInfo = neteaseHander.musicToDownloadInfo(music, null, false);
                        downloadInfos.add(downloadInfo);
                        SqSync sqSync = new SqSync();
                        sqSync.setMusicId(music.getId());
                        sqSync.setPlugName( neteaseHander.getPlugName());
                        sqSync.setMusicInfo(JSON.toJSONString(music));
                        sqSync.setPlayListName(sqMonitor.getTargetName());
                        sqSync.setPlayListId(targetId);
                        sqSync.setDownloadId(downloadInfo.getId());
                        String playListSha1 = DigestUtil.sha1Hex(sqMonitor.getTargetName());
                        sqSync.setPlayListSha1(playListSha1);
                        sqSyncs.add(sqSync);
                    }
                    downloadInfoService.add(downloadInfos);
                    syncService.saveBatch(sqSyncs);

                }
            }
        }
    }
    /**
     * 判断当前歌手列表是否包含需要忽略的歌手，决定是否忽略当前歌曲
     * @param excludeArtistNames 需要忽略的歌手列表
     * @param musicArtists 当前歌曲的歌手列表
     * @return 包含则返回true（需要忽略），否则返回false
     */
    public static boolean checkNeedExclude(List<String> excludeArtistNames, List<String> musicArtists) {
        if (CollectionUtils.isEmpty(excludeArtistNames) || CollectionUtils.isEmpty(musicArtists)) {
            return false;
        }
        Set<String> musicArtistsSet = new HashSet<>(musicArtists);
        return excludeArtistNames.stream().anyMatch(musicArtistsSet::contains);
    }
}
