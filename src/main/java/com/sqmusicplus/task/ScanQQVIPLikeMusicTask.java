package com.sqmusicplus.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.sqmusicplus.base.entity.DownloadEntity;
import com.sqmusicplus.base.entity.DownloadInfo;
import com.sqmusicplus.base.entity.Music;
import com.sqmusicplus.base.entity.SqConfig;
import com.sqmusicplus.base.service.DownloadInfoService;
import com.sqmusicplus.base.service.SqConfigService;
import com.sqmusicplus.plug.base.PlugBrType;
import com.sqmusicplus.plug.qq.entity.CgiGetAlbumFavInfo;
import com.sqmusicplus.plug.qq.entity.CgiGetPlaylistFavInfo;
import com.sqmusicplus.plug.qq.entity.DissInfo;
import com.sqmusicplus.plug.qq.entity.PlaylistBaseRead;
import com.sqmusicplus.plug.qq.entity.getfollowsingerlist.DataVDTO;
import com.sqmusicplus.plug.qq.entity.getfollowsingerlist.GetFollowSingerList;
import com.sqmusicplus.plug.qq.entity.getfollowsingerlist.ListVDTO;
import com.sqmusicplus.plug.qq.hander.QQHander;
import com.sqmusicplus.plug.qqvip.QQvipHander;
import com.sqmusicplus.utils.MusicUtils;
import com.sqmusicplus.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
    private SqConfigService configService;

    @Autowired
    private QQvipHander qQvipHander;

    @Autowired
    private DownloadInfoService downloadInfoService;

    @Scheduled(cron = "0 0/10 * * * ? ")
    public void excute() {
        SqConfig qqopenconfigKey = configService.getOne(new QueryWrapper<SqConfig>().eq("config_key", "plug.qqvip.open"));

        SqConfig syncConfig = configService.getOne(new QueryWrapper<SqConfig>().eq("config_key", "plug.qqvip.sync"));

        if (qqopenconfigKey != null && Boolean.parseBoolean(qqopenconfigKey.getConfigValue()) && Boolean.parseBoolean(syncConfig.getConfigValue())) {

//
//            喜欢单曲
            SqConfig syncLikeSongConfig = configService.getOne(new QueryWrapper<SqConfig>().eq("config_key", "plug.qqvip.synclikesong"));
            if (syncLikeSongConfig!=null&&Boolean.parseBoolean(syncLikeSongConfig.getConfigValue())) {
                log.info("扫描QQVIP同步我喜欢单曲");
                syncLikeSong();
            }
            //所有歌单
            SqConfig syncplaylist = configService.getOne(new QueryWrapper<SqConfig>().eq("config_key", "plug.qqvip.syncplaylist"));
            if (syncplaylist!=null&&Boolean.parseBoolean(syncplaylist.getConfigValue())) {
                log.info("扫描QQVIP同步所有歌单");
                syncplaylist();
            }
            //专辑
            SqConfig synclikealbum = configService.getOne(new QueryWrapper<SqConfig>().eq("config_key", "plug.qqvip.synclikealbum"));
            if (synclikealbum!=null&&Boolean.parseBoolean(synclikealbum.getConfigValue())) {
                log.info("扫描QQVIP同步所有专辑");
                syncalbu();
            }
            //关注的歌手
            SqConfig syncLikeArtist = configService.getOne(new QueryWrapper<SqConfig>().eq("config_key", "plug.qqvip.synclikeartist"));
            if (syncLikeArtist!=null&&Boolean.parseBoolean(syncLikeArtist.getConfigValue())) {
                log.info("扫描QQVIP同步所有关注歌手");
                syncArtist();
            }
        }
    }
//    plug.qqvip.likeArtistids
    private void syncArtist() {
        GetFollowSingerList getFollowSingerList = qQvipHander.likeArtists(1);
        Integer code = getFollowSingerList.getCode();
        if (code != null && code.intValue() ==  0) {
            ArrayList<String> artistids = new ArrayList<>();
            ArrayList<String> exclude = new ArrayList<>();
            DataVDTO data = getFollowSingerList.getData();
            List<ListVDTO> list = data.getList();
            Integer total = data.getTotal();
            Integer totalPage = total % 50 == 0 ? total / 50 : total / 50 + 1;
            for (int i = 2; i <= totalPage; i++) {
                GetFollowSingerList getFollowSingerList1 = qQvipHander.likeArtists(i);
                list.addAll(getFollowSingerList1.getData().getList());
            }
            for (ListVDTO listDTO : list) {
                String mid = listDTO.getMid();
                artistids.add(mid);
            }
            String allartistids = StringUtils.join(artistids, ",");
            SqConfig configKey = configService.getOne(new QueryWrapper<SqConfig>().eq("config_key", "plug.qqvip.likeArtistids"));
            if (configKey!=null){
                String configValue = configKey.getConfigValue();
                //已经下载的
                String[] split = configValue.split(",");
                if (split != null) {
                    Collections.addAll(exclude, split);
                }
                configService.update(new UpdateWrapper<SqConfig>().eq("config_key", "plug.qqvip.likeArtistids").set("config_value", allartistids));
            }else{
                SqConfig sqConfig = new SqConfig();
                sqConfig.setConfigKey("plug.qqvip.likeArtistids");
                sqConfig.setConfigValue(allartistids);
                sqConfig.setConfigName("QQVIP插件同步我关注的歌手同步专辑");
                sqConfig.setType("input");
                sqConfig.setConfigShow("N");
                configService.save(sqConfig);
            }
            ArrayList<String> excludeNames = new ArrayList<>();
            //不同步的歌单名称
            SqConfig syncplaylistexclude = configService.getOne(new QueryWrapper<SqConfig>().eq("config_key", "plug.qqvip.syncartistexclude"));
            if (StringUtils.isNotBlank(syncplaylistexclude.getConfigValue())) {
                String[] split = syncplaylistexclude.getConfigValue().split(",");
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
                    List<DownloadEntity> downloadEntities = qQvipHander.downloadArtistAllSong(mid, PlugBrType.QQVIP_Flac_2000, null);
                    List<DownloadInfo> downloadInfos = MusicUtils.downloadEntitytoDownloadInfoTo(downloadEntities);
                    downloadInfoService.add(downloadInfos);
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
            ArrayList<String> albummids = new ArrayList<>();
            ArrayList<String> exclude = new ArrayList<>();

            CgiGetAlbumFavInfo.DataDTO data = cgiGetAlbumFavInfo.getData();
            List<CgiGetAlbumFavInfo.DataDTO.VListDTO> vList = data.getVList();
            Long total = data.getTotal();
            Long totalPage = total % 50 == 0 ? total / 50 : total / 50 + 1;
            for (int i = 2; i <= totalPage; i++) {
                CgiGetAlbumFavInfo cgiGetAlbumFavInfo1 = qQvipHander.userALbymList(i);
                vList.addAll(cgiGetAlbumFavInfo1.getData().getVList());
            }
            for (CgiGetAlbumFavInfo.DataDTO.VListDTO vListDTO : vList) {
                albummids.add(vListDTO.getMid());
            }
            String allalbummids = StringUtils.join(albummids, ",");
            SqConfig configKey = configService.getOne(new QueryWrapper<SqConfig>().eq("config_key", "plug.qqvip.likeAlubids"));
            if (configKey != null) {
                String configValue = configKey.getConfigValue();
                //已经下载的
                String[] split = configValue.split(",");
                if (split != null) {
                    Collections.addAll(exclude, split);
                }
                configService.update(new UpdateWrapper<SqConfig>().eq("config_key", "plug.qqvip.likeAlubids").set("config_value", allalbummids));
            } else {
                SqConfig sqConfig = new SqConfig();
                sqConfig.setConfigKey("plug.qqvip.likeAlubids");
                sqConfig.setConfigValue(allalbummids);
                sqConfig.setConfigName("QQVIP已经同步过的专辑id（删除则重新同步一次）");
                sqConfig.setType("input");
                sqConfig.setConfigShow("N");
                configService.save(sqConfig);
            }
            vList.forEach((item) -> {
                String albummid = item.getMid();
                String singername = item.getVSinger().stream().map(item1 -> item1.getName()).collect(Collectors.joining(","));
                String albumname = item.getName();
                if (!exclude.contains(albummid)) {
                    ArrayList<DownloadEntity> downloadEntities = qQvipHander.downloadAlbum(albummid, PlugBrType.QQVIP_Flac_2000, null, singername, false, albumname);
                    List<DownloadInfo> downloadInfos = MusicUtils.downloadEntitytoDownloadInfoTo(downloadEntities);
                    downloadInfoService.add(downloadInfos);
                }
            });


        }


    }

    private void syncplaylist() {
        ArrayList<String> excludeNames = new ArrayList<>();
        //不同步的歌单名称
        SqConfig syncplaylistexclude = configService.getOne(new QueryWrapper<SqConfig>().eq("config_key", "plug.qqvip.syncplaylistexclude"));
        if (StringUtils.isNotBlank(syncplaylistexclude.getConfigValue())) {
            String[] split = syncplaylistexclude.getConfigValue().split(",");
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
                    songids.addAll(songlist1.stream().map(e -> e.getId().toString()).collect(Collectors.toCollection(ArrayList::new)));
                }
            }
            ArrayList<String> addSongIds = new ArrayList<>();
            SqConfig configKey = configService.getOne(new QueryWrapper<SqConfig>().eq("config_key", "plug.qqvip.songlistid." + tid));
            if (configKey != null) {
                //获取上次同步的歌单id
                String configValue = configKey.getConfigValue();
                List<String> targetList = Arrays.asList(configValue.split(","));
                //通过songids和上次同步的歌单id对比，获取新增的歌单id 使用流处理
                ArrayList<String> finalAddSongIds = new ArrayList<>();
                songids.stream().filter(item -> !targetList.contains(item)).forEach(item -> {
                    finalAddSongIds.add(item);
                });
                addSongIds = finalAddSongIds;
                configService.update(new UpdateWrapper<SqConfig>().eq("config_key", "plug.qqvip.songlistid." + tid).set("config_value", String.join(",", songids)));
            } else {
                addSongIds = new ArrayList<>(songids);
                SqConfig sqConfig = new SqConfig();
                sqConfig.setConfigKey("plug.qqvip.songlistid." + tid);
                sqConfig.setConfigValue(String.join(",", songids));
                sqConfig.setType("input");
                sqConfig.setConfigName("qqvip(" + dissname + ")歌单已下载id缓存 删除则回重新同步数据");
                sqConfig.setConfigShow("N");
                configService.save(sqConfig);
            }

            //循环找出歌曲详情
            ArrayList<String> finalAddSongIds1 = addSongIds;
            ArrayList<DownloadInfo> downloadInfos = new ArrayList<>();
            songlist.forEach((item) -> {
                String songid = item.getMid();
                if (finalAddSongIds1.contains(songid)) {
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
                    DownloadEntity downloadEntity = qQvipHander.downloadSong(music, brType, null);
                    DownloadInfo downloadInfo = MusicUtils.downloadEntitytoDownloadInfoTo(downloadEntity);
                    downloadInfos.add(downloadInfo);
                }
            });
            log.info("QQVIP同步歌单{}需要同步{}首", dissname, downloadInfos.size());
            if (!downloadInfos.isEmpty()) {
                downloadInfoService.add(downloadInfos);
            }
        }
    }
}




