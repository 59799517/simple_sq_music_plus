package com.sqmusicplus.v3.task;

import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson2.JSON;
import com.sqmusicplus.v3.base.entity.DownloadInfo;
import com.sqmusicplus.v3.base.entity.SqSync;
import com.sqmusicplus.v3.plug.entity.Music;
import com.sqmusicplus.v3.base.enums.DbBooleanConvert;
import com.sqmusicplus.v3.base.enums.PlugBrType;
import com.sqmusicplus.v3.base.enums.SetConfigEnum;
import com.sqmusicplus.v3.base.service.SqSyncService;
import com.sqmusicplus.v3.config.SqConfigCache;
import com.sqmusicplus.v3.plug.kg.entity.PlayListInfoResult.*;
import com.sqmusicplus.v3.plug.kg.entity.UserPlayListResult;
import com.sqmusicplus.v3.plug.kg.hander.KGHander;
import com.sqmusicplus.v3.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Classname KGPlayListTask
 * @Description 酷狗歌单任务
 * @Version 1.0.0
 * @Date 2025/7/10 15:35
 * @Created by SQ
 */
@Slf4j
@Component
public class KGPlayListTask {
    @Autowired
    private KGHander kgHander;
    @Autowired
    private SqSyncService syncService;

    @Scheduled(cron = "15 */1 * * * ? ")
    public void excute()
    {
        try {
            String kgopenconfigKey = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_KG_OPEN);
            String mycollectconfigKey = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_KG_SYNC_MY_COLLECT_PLAYLIST);

            //获取是否开启自动同步
            if (kgopenconfigKey != null && Boolean.parseBoolean(kgopenconfigKey) && Boolean.parseBoolean(mycollectconfigKey)) {
                log.info("开始执行酷狗歌单任务");
                ArrayList<String> excludeNames = new ArrayList<>();
                //不同步的歌单名称
                String sqConfigValue = SqConfigCache.getSqConfigValue(SetConfigEnum.SYSTEM_SYNC_PLAYLIST_EXCLUDE);

                if (StringUtils.isNotBlank(sqConfigValue)) {
                    String[] split = sqConfigValue.split("\\|");
                    if (split != null) {
                        for (String s : split) {
                            excludeNames.add(s.trim());
                        }
                    }
                }


                ArrayList<DownloadInfo> downloadInfos = new ArrayList<>();

                List<PlugBrType> plugBrTypes = new ArrayList<>();
                //获取用户全部歌单
                UserPlayListResult userPlayList = kgHander.getUserPlayList();
                if (userPlayList != null && userPlayList.getStatus()==1 && userPlayList.getData() != null && userPlayList.getData().getInfo() != null) {
                    //找出每一个歌单的名称
                    for (UserPlayListResult.DataDTO.InfoDTO infoDTO : userPlayList.getData().getInfo()) {
                        String name = infoDTO.getName();
                        if (excludeNames.contains(name)) {
                            continue;
                        }
                        String globalCollectionId = infoDTO.getGlobalCollectionId();

                        RootBean playListInfo = kgHander.getPlayListInfo(globalCollectionId);
                        if (playListInfo != null && playListInfo.getStatus()==1 && playListInfo.getData() != null && playListInfo.getData().getSongs() != null) {
                            List<Songs> songs = playListInfo.getData().getSongs();
                            for (Songs song : songs) {
                                String musicArtists = song.getSingerinfo().stream().map(Singerinfo::getName).collect(Collectors.joining(","));
                                List<String> authorIds = song.getSingerinfo().stream().map(e->e.getId().toString()).collect(Collectors.toList());
                                List<String> authorNames = song.getSingerinfo().stream().map(Singerinfo::getName).collect(Collectors.toList());

                                String musicAlbum = song.getAlbuminfo().getName();
                                String musicName = song.getName().replaceAll(musicArtists, "").replaceAll("-", "").trim();
                                String albumId = song.getAlbumId();
                                String musicimage = song.getCover().replaceAll("\\{size}",kgHander.getConfig().getImageSize());
                                if (StringUtils.isEmpty(musicimage)){
                                    TransParam transParam = song.getTransParam();
                                    musicimage = transParam.getUnionCover().replaceAll("\\{size}",kgHander.getConfig().getImageSize());
                                }
                                List<RelateGoods> relateGoods = song.getRelateGoods();
                                for (RelateGoods relat : relateGoods) {
                                    if (relat.getBitrate() == 128) {
                                        plugBrTypes.add(PlugBrType.KG_MP3_128);
                                    }
    //                                else if (relat.getBitrate() == 320) {
    //                                    plugBrTypes.add(PlugBrType.KG_MP3_320);
    //                                } else {
    //                                    plugBrTypes.add(PlugBrType.KG_Flac_2000);
    //                                }
                                }
    //                           plugBrTypes 去重
                                plugBrTypes = plugBrTypes.stream().distinct().collect(Collectors.toList());
                                //找到bit最大的
                                PlugBrType brType = plugBrTypes.stream().max(PlugBrType::compareTo).get();


                                String playListSha1 = DigestUtil.sha1Hex(playListInfo.getData().getListInfo().getName());

                                Music music = new Music()
                                        .setId(song.getHash())
                                        .setMusicName(musicName)
                                        .setArtistsIds(authorIds)
                                        .setMusicArtists(authorNames)
                                        .setAlbumId(albumId)
                                        .setMusicAlbum(musicAlbum)
                                        .setBits(plugBrTypes)
                                        .setMusicImage(musicimage);

                                Music music1 = kgHander.musicIgnoreCheck(music);
                                if (music1 == null){
                                    log.info("{}:忽略下载",music.getMusicName());
                                    continue;
                                }


                                DownloadInfo downloadInfo = kgHander.musicToDownloadInfo(music, brType, DbBooleanConvert.NO.getBooleanValue());
                                downloadInfos.add(downloadInfo);
                                SqSync sqSync = new SqSync();
                                sqSync.setMusicId(song.getHash());
                                sqSync.setPlugName( PlugBrType.KG_MP3_128.getPlugName());
                                sqSync.setMusicInfo(JSON.toJSONString(music));
                                sqSync.setPlayListName(playListInfo.getData().getListInfo().getName());
                                sqSync.setPlayListId(playListInfo.getData().getListInfo().getParentGlobalCollectionId());
                                sqSync.setDownloadId(downloadInfo.getId());
                                sqSync.setPlayListSha1(playListSha1);
                                //添加完成后保存到已经下载的列表中
                                syncService.save(sqSync);
                            }

                        }
                    }

                }


            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("扫描酷狗歌单失败: {}", e.getMessage());


        }


    }

}
