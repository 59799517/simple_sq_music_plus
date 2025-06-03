package com.sqmusicplus.plug.qqvip;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ejlchina.data.Mapper;
import com.sqmusicplus.base.entity.*;
import com.sqmusicplus.plug.base.PlugBrType;
import com.sqmusicplus.plug.base.hander.SearchHanderAbstract;
import com.sqmusicplus.plug.entity.*;
import com.sqmusicplus.plug.qq.entity.CgiGetAlbumFavInfo;
import com.sqmusicplus.plug.qq.entity.CgiGetPlaylistFavInfo;
import com.sqmusicplus.plug.qq.entity.DissInfo;
import com.sqmusicplus.plug.qq.entity.PlaylistBaseRead;
import com.sqmusicplus.plug.qq.entity.getfollowsingerlist.GetFollowSingerList;
import com.sqmusicplus.plug.qq.hander.QQHander;
import com.sqmusicplus.plug.qqvip.config.QQVipConfig;
import com.sqmusicplus.plug.qqvip.entity.QQVipSearchEntity;
import com.sqmusicplus.utils.DownloadUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @Classname QQvipHander1
 * @Description TODO
 * @Version 1.0.0
 * @Date 2025/5/26 17:58
 * @Created by SQ
 */
@Slf4j
@Component("qqvipHander")
public class QQvipHander extends SearchHanderAbstract {
    @Autowired
    private QQHander qqHander;
    @Autowired
     private QQVipConfig qqVipConfig;
    public void initPlug() {
        QQVipSearchEntity qqSearchEntity = new QQVipSearchEntity();
        qqSearchEntity.setPlugName("qqvip");
        qqHander.setQqSearchEntity(qqSearchEntity);
    }

    @Override
    public QQVipConfig  getConfig() {
        return qqVipConfig;
    }

    @Override
    public String getPlugName() {
        return "qqvip";
    }

    @Override
    public PlugSearchResult<PlugSearchMusicResult> querySongByName(SearchKeyData searchKeyData) {
       return qqHander.querySongByName(searchKeyData);
    }

    @Override
    public PlugSearchResult<PlugSearchArtistResult> queryArtistByName(SearchKeyData searchKeyData) {
        return qqHander.queryArtistByName(searchKeyData);
    }

    @Override
    public PlugSearchResult<PlugSearchAlbumResult> queryAlbumByName(SearchKeyData searchKeyData) {
        return qqHander.queryAlbumByName(searchKeyData);
    }

    @Override
    public Music querySongById(String SongId) {
        return qqHander.querySongById(SongId);
    }

    @Override
    public Artists queryArtistById(String artistId) {
        return qqHander.queryArtistById(artistId);
    }

    @Override
    public Album queryAlbumById(String albumId) {
        return qqHander.queryAlbumById(albumId);
    }

    @Override
    public String queryLyric(String SongId) {
        return qqHander.queryLyric(SongId);
    }

    @Override
    public List<Album> getAlbumsByArtist(String artistId, Integer pageIndex, Integer pageSize) {
        return qqHander.getAlbumsByArtist(artistId, pageIndex, pageSize);
    }

    @Override
    public List<Music> getAlbumSongByAlbumsId(String albumsId) {
        return qqHander.getAlbumSongByAlbumsId(albumsId);
    }

    @Override
    public HashMap<String, String> getDownloadUrl(String musicId, PlugBrType brType) {
        return qqHander.getDownloadUrl(musicId, brType);
    }

    @Override
    public HashMap<String, String> getDownloadUrl(DownloadEntity downloadEntity) {
        return qqHander.getDownloadUrl(downloadEntity);
    }

    @Override
    public DownloadEntity downloadSong(String musicid, PlugBrType brType, String musicname, String artistname, String albumname, Boolean isAudioBook, String addSubsonicPlayListName) {
        Music music = querySongById(musicid);
        DownloadEntity downloadEntity = new DownloadEntity("qqvipHander", musicid, brType, music.getMusicName(), music.getMusicArtists(), music.getMusicAlbum(), isAudioBook, isAudioBook ? addSubsonicPlayListName : null);
        return downloadEntity;
    }

    @Override
    public DownloadEntity downloadSong(Music music, PlugBrType brType, Boolean isAudioBook, String addSubsonicPlayListName) {
        DownloadEntity downloadEntity = new DownloadEntity("qqvipHander", music.getId(), brType, music.getMusicName(), music.getMusicArtists(), music.getMusicAlbum(), isAudioBook, isAudioBook ? addSubsonicPlayListName : null);
        return downloadEntity;
    }

    @Override
    public DownloadEntity downloadSong(Music music, PlugBrType brType, String addSubsonicPlayListName) {
        DownloadEntity downloadEntity = new DownloadEntity("qqvipHander", music.getId(), brType, music.getMusicName(), music.getMusicArtists(), music.getMusicAlbum(), false, addSubsonicPlayListName);
        return downloadEntity;
    }

    @Override
    public ArrayList<DownloadEntity> downloadAlbum(String albumsId, PlugBrType brType, String addSubsonicPlayListName, String artist, Boolean isAudioBook, String albumName) {
        List<Music> musiclist = getAlbumSongByAlbumsId(albumsId);
        AtomicReference<String> change = new AtomicReference<>(artist);
        ArrayList<DownloadEntity> downloadEntities = new ArrayList<>();

        SqConfig accompaniment = getConfigService().getOne(new QueryWrapper<SqConfig>().eq("config_key", "music.ignore.accompaniment"));
        SqConfig matchAlbumSinger = getConfigService().getOne(new QueryWrapper<SqConfig>().eq("config_key", "music.strong.match.album.singer"));
        SqConfig albumSingerUnity = getConfigService().getOne(new QueryWrapper<SqConfig>().eq("config_key", "music.album.singer.unity"));

        musiclist.forEach(md -> {
            if (Boolean.getBoolean(accompaniment.getConfigValue())) {
                if (md.getMusicName().contains("(伴奏)") || md.getMusicName().contains("(试听版)") || md.getMusicName().contains("片段")) {
                    return;
                }
            }
            if (Boolean.getBoolean(matchAlbumSinger.getConfigValue()) && !isAudioBook) {
                if (!md.getMusicArtists().contains(change.get())) {
                    return;
                }
            }
            if (!Boolean.getBoolean(albumSingerUnity.getConfigValue()) && !isAudioBook) {
                change.set(md.getMusicArtists());
            }
            PlugBrType plugBrType = null;
            if (md.getBits() != null && md.getBits().size() > 0) {
                ArrayList<PlugBrType> bits = md.getBits();
                //找出最大的码率
                plugBrType = bits.stream().max(Comparator.comparing(PlugBrType::getBit)).get();
            }
            if (isAudioBook) {
                downloadEntities.add(new DownloadEntity("qqvipHander", md.getId(), plugBrType == null ? brType : plugBrType, md.getMusicName(), artist, albumName, isAudioBook));
            } else {
                downloadEntities.add(new DownloadEntity("qqvipHander", md.getId(), plugBrType == null ? brType : plugBrType, md.getMusicName(), change.get(), md.getMusicAlbum()));
            }

        });
        return downloadEntities;
    }

    @Override
    public List<DownloadEntity> downloadArtistAllSong(String artistId, PlugBrType brType, String addSubsonicPlayListName) {
        String searchUrl = qqHander.getConfig().getSearchUrl();
        String s = qqHander.getqqSearchEntity().artistsTransferAlbumParam(artistId);
        Mapper mapper = DownloadUtils.getHttp().sync(searchUrl).setBodyPara(s).post().getBody().toMapper();
        List<Album> albums = qqHander.getqqSearchEntity().artistsTransferAlbum(mapper, qqHander.getConfig());
        ArrayList<DownloadEntity> downloadEntitys = new ArrayList<>();
        for (Album album : albums) {
            ArrayList<DownloadEntity> downloadEntities = downloadAlbum(album.getAlbumId(), brType, addSubsonicPlayListName, album.getAlbumArtists(), false, album.getAlbumName());
            downloadEntitys.addAll(downloadEntities);
        }
        return downloadEntitys;
    }

    @Override
    public List<DownloadEntity> downloadArtistAllAlbum(String artistId, PlugBrType brType, String addSubsonicPlayListName) {
        String searchUrl = qqHander.getConfig().getSearchUrl();
        String s = qqHander.getqqSearchEntity().artistsTransferAlbumParam(artistId);
        Mapper mapper = DownloadUtils.getHttp().sync(searchUrl).setBodyPara(s).post().getBody().toMapper();
        List<Album> albums = qqHander.getqqSearchEntity().artistsTransferAlbum(mapper, qqHander.getConfig());
        ArrayList<DownloadEntity> downloadEntitys = new ArrayList<>();
        for (Album album : albums) {
            ArrayList<DownloadEntity> downloadEntities = downloadAlbum(album.getAlbumId(), brType, addSubsonicPlayListName, album.getAlbumArtists(), false, album.getAlbumName());
            downloadEntitys.addAll(downloadEntities);
        }
        return downloadEntitys;
    }

    public GetFollowSingerList likeArtists(int i) {
        return qqHander.likeArtists(i);
    }

    public CgiGetAlbumFavInfo userALbymList(int i) {
        return qqHander.userALbymList(i);
    }

    public PlaylistBaseRead getUserSelfSongList() {
        return qqHander.getUserSelfSongList();
    }

    public CgiGetPlaylistFavInfo getUserFavSongList(int i) {
        return qqHander.getUserFavSongList(i);
    }

    public DissInfo songListInfo(String tid, String dirid, long l) {
        return qqHander.songListInfo(tid, dirid, l);
    }
}
