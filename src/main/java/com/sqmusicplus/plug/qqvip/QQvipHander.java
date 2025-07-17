package com.sqmusicplus.plug.qqvip;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ejlchina.data.Array;
import com.ejlchina.data.Mapper;
import com.sqmusicplus.base.entity.*;
import com.sqmusicplus.base.service.SqConfigService;
import com.sqmusicplus.config.GlobalStatic;
import com.sqmusicplus.plug.base.PlugBrType;
import com.sqmusicplus.plug.base.QQSongType;
import com.sqmusicplus.plug.base.hander.SearchHanderAbstract;
import com.sqmusicplus.plug.entity.*;
import com.sqmusicplus.plug.qq.entity.*;
import com.sqmusicplus.plug.qq.entity.getfollowsingerlist.GetFollowSingerList;
import com.sqmusicplus.plug.qq.hander.QQHander;
import com.sqmusicplus.plug.qq.util.QQMusicUtil;
import com.sqmusicplus.plug.qqvip.config.QQVipConfig;
import com.sqmusicplus.plug.qqvip.entity.QQVipSearchEntity;
import com.sqmusicplus.utils.DownloadUtils;
import com.sqmusicplus.utils.MusicUtils;
import com.sqmusicplus.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
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
        String musicId = downloadEntity.getMusicid();
        PlugBrType brType = downloadEntity.getBrType();
        if (musicId.contains(",")){
            musicId = musicId.split(",")[0];
        }
        QQSongType qqSongType=  QQSongType.FLAC;
        String musickey ="";
        String qq ="";
        String loginType ="";
        if (brType.getBit().intValue()==128){
            qqSongType = QQSongType.MP3_128;
        }else  if (brType.getBit().intValue()==320){
            qqSongType = QQSongType.MP3_320;
        }else if (brType.getValue().equals("flac")){
            if(brType.getBit().intValue()==2000||brType.getBit().intValue()==3000||brType.getBit().intValue()==4000||brType.getBit().intValue()==5000){
                qqSongType = QQSongType.FLAC;
            }
        }else  {
            qqSongType = QQSongType.FLAC;
        }

        SqConfigService configService = getConfigService();
        SqConfig sqConfig = configService.getOne(new QueryWrapper<SqConfig>().eq("config_key", GlobalStatic.QQ_LOGIN_COOKIE_KEY));
        if (sqConfig!=null&& StringUtils.isNotBlank(sqConfig.getConfigValue())){
            QQMusicCookieInfo qqMusicCookieInfo = JSONObject.parseObject(sqConfig.getConfigValue(), QQMusicCookieInfo.class);
            if (qqMusicCookieInfo != null){
                musickey= qqMusicCookieInfo.getMusickey();
                qq= qqMusicCookieInfo.getMusicid();
                loginType = qqMusicCookieInfo.getLoginType().toString();
            }

        }
        if (StringUtils.isBlank(musickey)||StringUtils.isBlank(qq)||StringUtils.isBlank(loginType)||StringUtils.isBlank(musicId)){
            return null;
        }
        String fileName = qqSongType.getPrefix()+musicId+musicId+"."+qqSongType.getSuffix();


        String  url= "";
        String ekey="";
        String vkey = "";
        String s = qqHander.getqqSearchEntity().downloadRequestParam(qq,musickey,loginType,fileName,musicId);
        String searchUrl = qqHander.getConfig().getSearchUrl();

        try {
            String  sign =  QQMusicUtil.sign(s);
            searchUrl= searchUrl+"?sign="+sign+"&signature="+sign;
        } catch (Exception e) {
        }


        Mapper mapper = DownloadUtils.getHttp().sync(searchUrl).setBodyPara(s).post().getBody().toMapper();

        Mapper mapper1 = mapper.getMapper("music.vkey.GetVkey.UrlGetVkey");
        long code = mapper1.getLong("code");
        if (code != 0) {
            return null;
        }
        Array array = mapper1.getMapper("data").getArray("midurlinfo");
        for (int i = 0; i < array.size(); i++) {
            Mapper mapper2 = array.getMapper(i);
            url = mapper2.getString("wifiurl");
            if (StringUtils.isBlank(url)){
                url= mapper2.getString("purl");
            }
            //有此参数则需要解密
            ekey = mapper2.getString("ekey");
            vkey = mapper2.getString("vkey");
        }

        String baseUrl = "https://isure.stream.qqmusic.qq.com/";
        Array sipArray = mapper1.getMapper("data").getArray("sip");
        ArrayList<String> baseurls = new ArrayList<>();
        if (sipArray!=null&&sipArray.size()>0){
            //循环找出sip
            for (int i = 0; i < sipArray.size(); i++) {
                //找出全部的
                if (StringUtils.isNotBlank(sipArray.getString(i))){
                    baseurls.add(sipArray.getString(i));
                }
            }
        }
        if (sipArray!=null&&sipArray.size()>0){
            //随机从baseurls抽取一个
            baseUrl = baseurls.get(new Random().nextInt(baseurls.size()));
        }


        if (StringUtils.isNotBlank(url)){
            url = baseUrl +url;
        }else{
            // if (brType.getBit().intValue()!=320){
            //     downloadEntity.setBrType(PlugBrType.QQVIP_MP3_320);
            //     DownloadInfo downloadInfo = MusicUtils.downloadEntitytoDownloadInfoTo(downloadEntity);
            //     getDownloadInfoService().add(downloadInfo);
            // }
            return null;
        }
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("url", url);
        stringStringHashMap.put("type", brType.getType());
        stringStringHashMap.put("bit", brType.getBit().toString());
        stringStringHashMap.put("ekey", ekey);
        stringStringHashMap.put("vkey", vkey);
        return stringStringHashMap;

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
