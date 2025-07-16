package com.sqmusicplus.task;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.sqmusicplus.base.entity.Music;
import com.sqmusicplus.base.entity.SqConfig;
import com.sqmusicplus.base.service.SqConfigService;
import com.sqmusicplus.v3.base.enums.PlugBrType;
import com.sqmusicplus.plug.kg.entity.PlayListInfoResult.*;
import com.sqmusicplus.plug.kg.entity.UserPlayListResult;
import com.sqmusicplus.plug.kg.hander.KGHander;
import com.sqmusicplus.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    private SqConfigService configService;
    @Autowired
    private KGHander kgHander;

    public void excute()
    {
        log.info("开始执行酷狗歌单任务");
        //获取是否开启自动同步
        SqConfig openconfig = configService.getOne(Wrappers.<SqConfig>lambdaQuery().eq(SqConfig::getConfigKey, "plug.kg.open"));
        SqConfig syncconfig = configService.getOne(Wrappers.<SqConfig>lambdaQuery().eq(SqConfig::getConfigKey, "plug.kg.sync"));
        if (syncconfig != null && Boolean.parseBoolean(syncconfig.getConfigValue()) && Boolean.parseBoolean(openconfig.getConfigValue())) {
            //获取用户全部歌单
            UserPlayListResult userPlayList = kgHander.getUserPlayList();
            if (userPlayList != null && userPlayList.getStatus()==1 && userPlayList.getData() != null && userPlayList.getData().getInfo() != null) {
                //找出每一个歌单的ID
                for (UserPlayListResult.DataDTO.InfoDTO infoDTO : userPlayList.getData().getInfo()) {

                    String globalCollectionId = infoDTO.getGlobalCollectionId();

                    RootBean playListInfo = kgHander.getPlayListInfo(globalCollectionId);
                    if (playListInfo != null && playListInfo.getStatus()==1 && playListInfo.getData() != null && playListInfo.getData().getSongs() != null) {
                        List<Songs> songs = playListInfo.getData().getSongs();
                        for (Songs song : songs) {
                            String id = song.getHash();
                            String musicArtists = song.getSingerinfo().stream().map(Singerinfo::getName).collect(Collectors.joining(","));
                            Long authorId =  song.getSingerinfo().get(0).getId();
                            String musicAlbum = song.getAlbuminfo().getName();
                            String musicName = song.getName().replaceAll(musicArtists, "").replaceAll("-", "").trim();
                            String albumId = song.getAlbumId();
//                            SongInfoResult.DataDTO.InfoDTO info = dataDTO.getInfo();
//                            Long duration = info.getDuration();
                            String musicimage = song.getCover().replaceAll("\\{size}",kgHander.getConfig().getImageSize());
                            if (StringUtils.isEmpty(musicimage)){
                                TransParam transParam = song.getTransParam();
                                musicimage = transParam.getUnionCover().replaceAll("\\{size}",kgHander.getConfig().getImageSize());
                            }

                            try {
                                String hash = song.getHash();
                                if (StringUtils.isNotEmpty(hash)){
                                    plugBrTypes.add(PlugBrType.KG_MP3_128);
                                }
                            } catch (Exception e) {
                            }
                            try {
                                String hash320 = dataDTO1.getAudioInfo().getHash320();
                                if (StringUtils.isNotEmpty(hash320)){
                                    plugBrTypes.add(PlugBrType.KG_MP3_320);
                                }
                            } catch (Exception e) {
                            }
                            try {
                                String hashFlac = dataDTO1.getAudioInfo().getHashFlac();

                                if (StringUtils.isNotEmpty(hashFlac)){
                                    plugBrTypes.add(PlugBrType.KG_Flac_2000);
                                }
                            } catch (Exception e) {
                            }

                            Music music = new Music()
                                    .setId(song.getHash())
                                    .setMusicName(musicName)
                                    .setArtistsId(authorId.toString())
                                    .setMusicArtists(musicArtists)
                                    .setAlbumId(albumId)
                                    .setMusicAlbum(musicAlbum)
//                                    .setMusicLyric(lyric)
//                                    .setMusicDuration(duration)
                                    .setMusicImage(musicimage);
                        }

                    }
                }

            }


        }


        }

}
