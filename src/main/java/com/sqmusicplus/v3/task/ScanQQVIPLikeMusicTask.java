package com.sqmusicplus.v3.task;

import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sqmusicplus.v3.base.entity.DownloadInfo;
import com.sqmusicplus.v3.base.entity.SqSync;
import com.sqmusicplus.v3.plug.entity.Music;
import com.sqmusicplus.v3.base.enums.DbBooleanConvert;
import com.sqmusicplus.v3.base.enums.SetConfigEnum;
import com.sqmusicplus.v3.base.service.DownloadInfoService;
import com.sqmusicplus.v3.base.enums.PlugBrType;
import com.sqmusicplus.v3.base.service.SqSyncService;
import com.sqmusicplus.v3.config.SqConfigCache;
import com.sqmusicplus.v3.plug.qq.entity.CgiGetAlbumFavInfo;
import com.sqmusicplus.v3.plug.qq.entity.CgiGetPlaylistFavInfo;
import com.sqmusicplus.v3.plug.qq.entity.DissInfo;
import com.sqmusicplus.v3.plug.qq.entity.PlaylistBaseRead;
import com.sqmusicplus.v3.plug.qq.entity.getfollowsingerlist.DataVDTO;
import com.sqmusicplus.v3.plug.qq.entity.getfollowsingerlist.GetFollowSingerList;
import com.sqmusicplus.v3.plug.qq.entity.getfollowsingerlist.ListVDTO;
import com.sqmusicplus.v3.plug.qqvip.QQvipHander;
import com.sqmusicplus.v3.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Classname ScanQQVIPLikeMusic
 * @Description 扫描QQVIP喜欢音乐
 * @Version 1.0.0
 * @Date 2024/7/31 10:54
 * @Created by SQ
 */
@Slf4j
@Component
public class ScanQQVIPLikeMusicTask {


    @Autowired
    private QQvipHander qQvipHander;
    @Autowired
    private SqSyncService syncService;

    @Autowired
    private DownloadInfoService downloadInfoService;

    @Scheduled(cron = "0 */10 * * * ? ")
    public void excute() {
        String qqopenconfigKey = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_QQVIP_OPEN);
        String myLikeSongSyncConfig = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_QQVIP_SYNC_MY_LIKE_MUSIC);
        String myLikePlaylistSyncConfig = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_QQVIP_SYNC_MY_LIKE_PLAYLIST);
        String myLikeAlbumSyncConfig = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_QQVIP_SYNC_MY_LIKE_ALBUM);
        String myLikeArtistsSyncConfig = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_QQVIP_SYNC_MY_LIKE_ARTISTS);

            if (StringUtils.isNotBlank(qqopenconfigKey)&& Boolean.parseBoolean(myLikeSongSyncConfig)) {

                if (StringUtils.isNotBlank(myLikeSongSyncConfig)&& Boolean.parseBoolean(myLikeSongSyncConfig)){
                    log.info("扫描QQVIP同步我喜欢单曲");
                    syncLikeSong();
                }

                if (StringUtils.isNotBlank(myLikePlaylistSyncConfig)&& Boolean.parseBoolean(myLikePlaylistSyncConfig)){
                    log.info("扫描QQVIP同步所有歌单");
                    syncplaylist();
                }

                if (StringUtils.isNotBlank(myLikeAlbumSyncConfig)&& Boolean.parseBoolean(myLikeAlbumSyncConfig)){
                    log.info("扫描QQVIP同步所有专辑");
                    syncalbu();
                }
                if (StringUtils.isNotBlank(myLikeArtistsSyncConfig)&& Boolean.parseBoolean(myLikeArtistsSyncConfig)){
                    log.info("扫描QQVIP同步所有关注歌手");
                    syncArtist();
                }
        }
    }
    private void syncArtist() {
        GetFollowSingerList getFollowSingerList = qQvipHander.likeArtists(1);
        Integer code = getFollowSingerList.getCode();
        if (code != null && code.intValue() ==  0) {
            ArrayList<String> exclude = new ArrayList<>();
            DataVDTO data = getFollowSingerList.getData();
            List<ListVDTO> list = data.getList();
            Integer total = data.getTotal();
            Integer totalPage = total % 50 == 0 ? total / 50 : total / 50 + 1;
            for (int i = 2; i <= totalPage; i++) {
                GetFollowSingerList getFollowSingerList1 = qQvipHander.likeArtists(i);
                list.addAll(getFollowSingerList1.getData().getList());
            }
            LambdaQueryWrapper<SqSync> sqQqmusicMyLike = new LambdaQueryWrapper<SqSync>().eq(SqSync::getPlugName, PlugBrType.QQVIP_Flac_2000.getPlugName())
                    .isNotNull(SqSync::getArtistId);

            //已经下载的
            List<SqSync> dbSqSync = syncService.list(sqQqmusicMyLike);
            ArrayList<String> excludeNames = new ArrayList<>();

            if (dbSqSync!=null) {
                List<String> collect = dbSqSync.stream().map(SqSync::getArtistId).collect(Collectors.toList());
                exclude.addAll(collect);
            }
            String sqConfigValue = SqConfigCache.getSqConfigValue(SetConfigEnum.SYSTEM_SYNC_ARTISTS_EXCLUDE);
            //不同步的歌手名称
            if (StringUtils.isNotBlank(sqConfigValue)) {
                String[] split = sqConfigValue.split("\\|");
                if (split != null) {
                    for (String s : split) {
                        excludeNames.add(s.trim());
                    }
                }
            }

            list.forEach(item -> {
                String mid = item.getMid();
                String name = item.getName();
                if (!excludeNames.contains(name)&&!exclude.contains(mid)){
                    List<DownloadInfo> downloadInfos = qQvipHander.downloadArtistAllSong(mid, PlugBrType.QQVIP_Flac_2000);
                    List<DownloadInfo> downloadInfos1 = qQvipHander.musicIgnoreCheck(downloadInfos);

                    downloadInfoService.add(downloadInfos1);
                    SqSync sqSync = new SqSync();
                    sqSync.setPlugName( PlugBrType.QQVIP_Flac_2000.getPlugName());
                    sqSync.setMusicInfo(JSON.toJSONString(item));
                    sqSync.setArtistId(mid);
                    sqSync.setArtistName(name);
                    //添加完成后保存到已经下载的列表中
                    syncService.save(sqSync);


                }else{
                    log.info("已排除歌手或者已经下载过了：{} 不下载",name);
                }

            });



        }

    }


    private void syncalbu() {

        CgiGetAlbumFavInfo cgiGetAlbumFavInfo = qQvipHander.userALbymList(1);
        Long code = cgiGetAlbumFavInfo.getCode();
        if (code != null && code == 0) {
            ArrayList<String> exclude = new ArrayList<>();

            CgiGetAlbumFavInfo.DataDTO data = cgiGetAlbumFavInfo.getData();
            List<CgiGetAlbumFavInfo.DataDTO.VListDTO> vList = data.getVList();
            Long total = data.getTotal();
            Long totalPage = total % 50 == 0 ? total / 50 : total / 50 + 1;
            for (int i = 2; i <= totalPage; i++) {
                CgiGetAlbumFavInfo cgiGetAlbumFavInfo1 = qQvipHander.userALbymList(i);
                vList.addAll(cgiGetAlbumFavInfo1.getData().getVList());
            }
            LambdaQueryWrapper<SqSync> sqQqmusicMyLike = new LambdaQueryWrapper<SqSync>().eq(SqSync::getPlugName, PlugBrType.QQVIP_Flac_2000.getPlugName())
                    .isNotNull(SqSync::getAlbumId);

            //已经下载的
            List<SqSync> dbSqSync = syncService.list(sqQqmusicMyLike);
            if (dbSqSync != null) {
                //已经下载的
                exclude.addAll(dbSqSync.stream().map(SqSync::getAlbumId).toList());
            }
            ArrayList<String> excludeNames = new ArrayList<>();

//           需要排除的
            String sqConfigValue = SqConfigCache.getSqConfigValue(SetConfigEnum.SYSTEM_SYNC_ALBUM_EXCLUDE);

            //不同步的专辑名称
            if (StringUtils.isNotBlank(sqConfigValue)) {
                String[] split = sqConfigValue.split("\\|");
                if (split != null) {
                    for (String s : split) {
                        excludeNames.add(s.trim());
                    }
                }
            }


            vList.forEach((item) -> {
                String albummid = item.getMid();
                List<String> collect = item.getVSinger().stream().map(item1 -> item1.getName()).collect(Collectors.toList());
                String albumname = item.getName();
                if (!excludeNames.contains(albumname)&&!exclude.contains(albummid)) {
                    ArrayList<DownloadInfo> downloadInfos = qQvipHander.downloadAlbum(albummid, PlugBrType.QQVIP_Flac_2000, collect, false, albumname);
                    List<DownloadInfo> downloadInfos1 = qQvipHander.musicIgnoreCheck(downloadInfos);
                    downloadInfoService.add(downloadInfos1);
                    SqSync sqSync = new SqSync();
                    sqSync.setPlugName( PlugBrType.QQVIP_Flac_2000.getPlugName());
                    sqSync.setMusicInfo(JSON.toJSONString(item));
                    sqSync.setAlbumId(albummid);
                    sqSync.setAlbumName(albumname);
                    //添加完成后保存到已经下载的列表中
                    syncService.save(sqSync);
                }
            });


        }


    }

    private void syncplaylist() {
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

        //自己创建的
        PlaylistBaseRead userSelfSongList = qQvipHander.getUserSelfSongList();
        if (userSelfSongList != null && userSelfSongList.getCode() != null && userSelfSongList.getCode() == 0L) {
            PlaylistBaseRead.DataDTO data = userSelfSongList.getData();
            for (PlaylistBaseRead.DataDTO.VPlaylistDTO vPlaylistDTO : data.getVPlaylist()) {
                Long dirId = vPlaylistDTO.getDirId();
                if (dirId != null && dirId != 201L) {
                    Long dirShow = vPlaylistDTO.getDirShow();
                    if (dirShow != null && dirShow == 1) {
                        String diss_name = vPlaylistDTO.getDirName().trim();
                        if (!excludeNames.contains(diss_name)) {
                            log.info("同步我创建的歌单{}的歌曲共找到{}首", diss_name, vPlaylistDTO.getSongNum());
                            //歌单id
                            Long tid = vPlaylistDTO.getTid();
                            syncsonglist(tid.toString(), dirId.toString(), diss_name);
                        } else {
                            log.info("同步我创建的歌单{}设置跳过不进行同步", diss_name);
                        }

                    }
                }
            }
        }

//用户收藏的第一页
        CgiGetPlaylistFavInfo userFavSongList = qQvipHander.getUserFavSongList(1);
        if (userFavSongList != null && userFavSongList.getCode() != null && userFavSongList.getCode() == 0L) {
            CgiGetPlaylistFavInfo.DataDTO data = userFavSongList.getData();
            for (CgiGetPlaylistFavInfo.DataDTO.VListDTO vListDTO : data.getVList()) {
                Long dirId = vListDTO.getDirId();
                if (dirId != null && dirId != 201L) {
                    Long dirShow = vListDTO.getDirShow();
                    if (dirShow != null && dirShow == 1) {
                        String diss_name = vListDTO.getName().trim();
                        if (!excludeNames.contains(diss_name)) {
                            log.info("同步我创建的歌单{}的歌曲共找到{}首", diss_name, vListDTO.getSongnum());
                            //歌单id
                            Long tid = vListDTO.getTid();
                            syncsonglist(tid.toString(), dirId.toString(), diss_name);
                        } else {
                            log.info("同步我创建的歌单{}设置跳过不进行同步", diss_name);
                        }

                    }
                }
            }

//如果收藏的有多余的页码
            Long total = data.getTotal();
            Long number = data.getNumber();
            Long totalPage = total % number == 0 ? total / number : total / number + 1;
            for (int i = 2; i <= totalPage; i++) {
                CgiGetPlaylistFavInfo userFavSongList1 = qQvipHander.getUserFavSongList(i);
                CgiGetPlaylistFavInfo.DataDTO data1 = userFavSongList1.getData();
                for (CgiGetPlaylistFavInfo.DataDTO.VListDTO vListDTO : data1.getVList()) {
                    Long dirId = vListDTO.getDirId();
                    if (dirId != null && dirId != 201L) {
                        Long dirShow = vListDTO.getDirShow();
                        if (dirShow != null && dirShow == 1) {
                            String diss_name = vListDTO.getName().trim();
                            if (!excludeNames.contains(diss_name)) {
                                log.info("同步我创建的歌单{}的歌曲共找到{}首", diss_name, vListDTO.getSongnum());
                                //歌单id
                                Long tid = vListDTO.getTid();
                                syncsonglist(tid.toString(), dirId.toString(), diss_name);
                            } else {
                                log.info("同步我创建的歌单{}设置跳过不进行同步", diss_name);
                            }
                        }
                    }
                }
            }
        }
    }


    /**
     * 我喜欢的歌单（仅仅是我喜欢的单曲）--------已修改
     */
    private void syncLikeSong() {
        PlaylistBaseRead userSelfSongList = qQvipHander.getUserSelfSongList();
        if (userSelfSongList != null && userSelfSongList.getCode() != null && userSelfSongList.getCode() == 0L) {
            PlaylistBaseRead.DataDTO data = userSelfSongList.getData();
            for (PlaylistBaseRead.DataDTO.VPlaylistDTO vPlaylistDTO : data.getVPlaylist()) {
                Long dirId = vPlaylistDTO.getDirId();
                if (dirId != null && dirId == 201L) {
                    Long songNum = vPlaylistDTO.getSongNum();
                    log.info("同步我喜欢的单曲共找到{}首", songNum);
                    //歌单id
                    Long tid = vPlaylistDTO.getTid();
                    String dirName = vPlaylistDTO.getDirName();
                    String string = tid.toString();
                    syncsonglist(string, dirId.toString(), dirName);
                }
            }
        }
    }


    /**
     * 根据歌单id获取歌曲并添加到下载列表
     */
    private void syncsonglist(String tid, String dirid, String dissname) {
        ArrayList<String> songids;
        DissInfo dissInfo = qQvipHander.songListInfo(tid, dirid, 1L);
        Long code = dissInfo.getCode();
        if (code != null && code == 0L) {
            DissInfo.DataDTO data = dissInfo.getData();
            Long totalSongNum = data.getTotalSongNum();
            Long songlistSize = data.getSonglistSize();
            List<DissInfo.DataDTO.SonglistDTO> songlist = data.getSonglist();
            songids = songlist.stream().map(e -> e.getId().toString()).collect(Collectors.toCollection(ArrayList::new));
            //看总数是否能获取全部的  songlistSize 是每页长度
            if (totalSongNum.longValue() > songlistSize.longValue()) {
                //计算还需要的页数
                Long pageNum = totalSongNum.longValue() / songlistSize.longValue();
                //计算是否有余数
                if (totalSongNum.longValue() % songlistSize.longValue() > 0) {
                    pageNum++;
                }
                for (int i = 2; i <= pageNum; i++) {
                    DissInfo dissInfo1 = qQvipHander.songListInfo(tid, dirid, Long.parseLong(i + ""));
                    DissInfo.DataDTO data1 = dissInfo1.getData();
                    List<DissInfo.DataDTO.SonglistDTO> songlist1 = data1.getSonglist();
                    songlist.addAll(songlist1);
                    songids.addAll(songlist1.stream().map(e -> e.getId().toString()).collect(Collectors.toCollection(ArrayList::new)));
                }
            }
            ArrayList<String> addSongIds = new ArrayList<>();
            String playListSha1 = DigestUtil.sha1Hex(dissname);

            LambdaQueryWrapper<SqSync> sqQqmusicMyLike = new LambdaQueryWrapper<SqSync>().eq(SqSync::getPlugName, PlugBrType.QQVIP_Flac_2000.getPlugName())
                    .eq(SqSync::getPlayListId, tid)
                    .eq(SqSync::getPlayListName, dissname);
//                    .eq(SqSync::getPlayListSha1, playListSha1);

            List<SqSync> list = syncService.list(sqQqmusicMyLike);


            if (list.isEmpty()) {
                 list = new ArrayList<>();
            }
                List<String> collect = list.stream().map(SqSync::getMusicId).collect(Collectors.toList());
                //获取上次同步的歌单id
                //通过songids和上次同步的歌单id对比，获取新增的歌单id 使用流处理
                ArrayList<String> finalAddSongIds = new ArrayList<>();
                songids.stream().filter(item -> !collect.contains(item)).forEach(finalAddSongIds::add);
                addSongIds = finalAddSongIds;


            //循环找出歌曲详情
            ArrayList<String> finalAddSongIds1 = addSongIds;
            ArrayList<DownloadInfo> downloadInfos = new ArrayList<>();
            songlist.forEach((item) -> {
                Long id = item.getId();
                if (finalAddSongIds1.contains(id.toString())) {
                    String songmid = item.getMid();
                    Long sizeflac = item.getFile().getSizeFlac();
                    Long size320 = item.getFile().getSize320mp3();
                    Long size128 = item.getFile().getSize128mp3();
                    PlugBrType brType = PlugBrType.QQVIP_Flac_2000;
                    if (sizeflac > 0) {
                        brType = PlugBrType.QQVIP_Flac_2000;
                    } else if (size320 > 0) {
                        brType = PlugBrType.QQVIP_MP3_320;
                    } else if (size128 > 0) {
                        brType = PlugBrType.QQVIP_MP3_128;
                    }
                    Music music = qQvipHander.querySongById(songmid);

                    Music music1 = qQvipHander.musicIgnoreCheck(music);
                    if (music1 == null){
                        log.info("{}:忽略下载",music.getMusicName());
                        return;
                    }

                    DownloadInfo downloadInfo = qQvipHander.musicToDownloadInfo(music, brType, DbBooleanConvert.NO.getBooleanValue());
                    downloadInfos.add(downloadInfo);
                    SqSync sqSync = new SqSync();
                    sqSync.setMusicId(id.toString());
                    sqSync.setPlugName( PlugBrType.QQVIP_Flac_2000.getPlugName());
                    sqSync.setMusicInfo(JSON.toJSONString(music));
                    sqSync.setPlayListName(dissname);
                    sqSync.setPlayListId(tid);
                    sqSync.setDownloadId(downloadInfo.getId());
                    sqSync.setPlayListSha1(playListSha1);

                    //添加完成后保存到已经下载的列表中
                    syncService.save(sqSync);
                }
            });
            log.info("QQVIP同步歌单{}需要同步{}首", dissname, downloadInfos.size());
            if (!downloadInfos.isEmpty()) {
                downloadInfoService.add(downloadInfos);
            }
        }
    }
}




