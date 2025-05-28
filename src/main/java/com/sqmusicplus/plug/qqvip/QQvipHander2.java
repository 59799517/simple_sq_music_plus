//package com.sqmusicplus.plug.qqvip;
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.ejlchina.data.Mapper;
//import com.sqmusicplus.base.entity.*;
//import com.sqmusicplus.base.service.SqConfigService;
//import com.sqmusicplus.plug.base.PlugBrType;
//import com.sqmusicplus.plug.base.hander.SearchHanderAbstract;
//import com.sqmusicplus.plug.entity.*;
//import com.sqmusicplus.plug.qq.entity.CgiGetAlbumFavInfo;
//import com.sqmusicplus.plug.qq.entity.DissInfo;
//import com.sqmusicplus.plug.qq.entity.PlaylistBaseRead;
//import com.sqmusicplus.plug.qq.hander.QQHander;
//import com.sqmusicplus.plug.qqvip.config.QQVipConfig;
//import com.sqmusicplus.plug.qqvip.entity.QQVipSearchEntity;
//import com.sqmusicplus.utils.DownloadUtils;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.util.ArrayList;
//import java.util.Comparator;
//import java.util.HashMap;
//import java.util.List;
//import java.util.concurrent.atomic.AtomicReference;
//
//
///**
// * @Classname QQvipHander
// * @Description QQVIP处理器
// * @Version 1.0.0
// * @Date 2024/6/27 16:29
// * @Created by SQ
// */
//@Slf4j
//@Component("qqvipHander")
//public class QQvipHander2 extends SearchHanderAbstract {
//
//    @Autowired
//    private QQVipConfig qqvipConfig;
//
//
//    @Autowired
//    private QQHander qqHander;
//
//    @Autowired
//    private SqConfigService configService;
//
//
//
//
//
//    public void initPlug() {
//        QQVipSearchEntity qqSearchEntity = new QQVipSearchEntity();
//        qqSearchEntity.setPlugName("qqvip");
//        qqHander.setQqSearchEntity(qqSearchEntity);
//
//    }
//
//
//    @Override
//    public QQVipConfig getConfig() {
//        return qqvipConfig;
//    }
//
//    @Override
//    public String getPlugName() {
//        return "qqvip";
//    }
//
//    @Override
//    public PlugSearchResult<PlugSearchMusicResult> querySongByName(SearchKeyData searchKeyData) {
//        return qqHander.querySongByName(searchKeyData);
//    }
//
//    @Override
//    public PlugSearchResult<PlugSearchArtistResult> queryArtistByName(SearchKeyData searchKeyData) {
//        return qqHander.queryArtistByName(searchKeyData);
//    }
//
//    @Override
//    public PlugSearchResult<PlugSearchAlbumResult> queryAlbumByName(SearchKeyData searchKeyData) {
//        return qqHander.queryAlbumByName(searchKeyData);
//    }
//
//    @Override
//    public Music querySongById(String SongId) {
////        if (SongId.contains(",")) {
////            SongId = SongId.split(",")[0];
////        }
////        String searchUrl = qqHander.getConfig().getSearchUrl();
////        String s = qqHander.getqqSearchEntity().musicInfoRequestParam(SongId);
////        Mapper mapper = DownloadUtils.getHttp().sync(searchUrl).setBodyPara(s).post().getBody().toMapper();
////        return qqHander.getqqSearchEntity().songInfoToMusic(mapper, qqHander.getConfig());
////
//        return qqHander.querySongById(SongId);
//    }
//
//    @Override
//    public Artists queryArtistById(String artistId) {
//        return qqHander.queryArtistById(artistId);
//    }
//
//    @Override
//    public Album queryAlbumById(String albumId) {
//        return qqHander.queryAlbumById(albumId);
//    }
//
//    @Override
//    public String queryLyric(String SongId) {
//        return qqHander.queryLyric(SongId);
//    }
//
//    @Override
//    public List<Album> getAlbumsByArtist(String artistId, Integer pageIndex, Integer pageSize) {
//        return qqHander.getAlbumsByArtist(artistId, pageIndex, pageSize);
//    }
//
//    @Override
//    public List<Music> getAlbumSongByAlbumsId(String albumsId) {
//        return qqHander.getAlbumSongByAlbumsId(albumsId);
//    }
//
//    @Override
//    public HashMap<String, String> getDownloadUrl(String musicId, PlugBrType brType) {
//        return qqHander.getDownloadUrl(musicId, brType);
////        String type = brType.getValue();
////        JSONObject jsonObject = new JSONObject();
////        String[] split = musicId.split(",");
////        if (musicId.contains(",")) {
////            jsonObject.put("id", split[0]);
////            jsonObject.put("mediaId", split[1]);
////        } else {
////            jsonObject.put("id", musicId);
////        }
////        jsonObject.put("type", type);
////        SqConfig configKey = configService.getOne(new QueryWrapper<SqConfig>().eq("config_key", "plug.qqvip.baseurl"));
////        String baseUrl = configKey.getConfigValue();
////
////        String url = HttpUtil.urlWithForm(baseUrl + "/song/url", jsonObject, Charset.forName("UTF-8"), true);
////        OkHttpClient client = DownloadUtils.getOkHttpClient();
////        try {
////            Request request = new Request.Builder()
////                    .addHeader("Cookie", FreeCookieUtil.getCookieStr())
////                    .url(url)
////                    .get()
////                    .build();
////            Response response = client.newCall(request).execute();
////            String string = response.body().string();
////            JSONObject jsonObject1 = JSONObject.parseObject(string);
////            Integer code = jsonObject1.getInteger("result");
////            if (code != null && code.intValue() == 100) {
////                HashMap<String, String> stringStringHashMap = new HashMap<>();
////                String downloadurl = jsonObject1.getString("data");
////                if (downloadurl.contains("undefined")) {
////                    JSONObject nestedobject = new JSONObject();
////                    nestedobject.put("id", jsonObject.getString("id"));
////                    nestedobject.put("type", type);
////                    String nestedurl = HttpUtil.urlWithForm(baseUrl + "/song/url", nestedobject, Charset.forName("UTF-8"), true);
////                    Request nestedrequest = new Request.Builder()
////                            .addHeader("Cookie", FreeCookieUtil.getCookieStr())
////                            .url(nestedurl)
////                            .get()
////                            .build();
////                    Response nestedresponse = client.newCall(nestedrequest).execute();
////                    String nestedbody = nestedresponse.body().string();
////                    JSONObject nestedjsonObject1 = JSONObject.parseObject(nestedbody);
////                    Integer nestedcode = nestedjsonObject1.getInteger("result");
////
////                    if (nestedcode != null && nestedcode.intValue() == 100) {
////                        downloadurl = nestedjsonObject1.getString("data");
////                    } else {
////                        log.error("获取下载地址失败music地址:{}", musicId);
////                    }
////                }
////                stringStringHashMap.put("url", downloadurl);
////                stringStringHashMap.put("type", brType.getType());
////                stringStringHashMap.put("bit", brType.getBit().toString());
////                return stringStringHashMap;
////            }
////        } catch (IOException e) {
////            e.printStackTrace();
////            log.error("获取下载地址失败music地址:{}", musicId);
////        }
////        return null;
//    }
//
//    @Override
//    public DownloadEntity downloadSong(String musicid, PlugBrType brType, String musicname, String artistname, String albumname, Boolean isAudioBook, String addSubsonicPlayListName) {
//        Music music = querySongById(musicid);
//        DownloadEntity downloadEntity = new DownloadEntity("qqvipHander", musicid, brType, music.getMusicName(), music.getMusicArtists(), music.getMusicAlbum(), isAudioBook, isAudioBook ? addSubsonicPlayListName : null);
//        return downloadEntity;
//    }
//
//    @Override
//    public DownloadEntity downloadSong(Music music, PlugBrType brType, Boolean isAudioBook, String addSubsonicPlayListName) {
//        DownloadEntity downloadEntity = new DownloadEntity("qqvipHander", music.getId(), brType, music.getMusicName(), music.getMusicArtists(), music.getMusicAlbum(), isAudioBook, isAudioBook ? addSubsonicPlayListName : null);
//        return downloadEntity;
//    }
//
//    @Override
//    public DownloadEntity downloadSong(Music music, PlugBrType brType, String addSubsonicPlayListName) {
//        DownloadEntity downloadEntity = new DownloadEntity("qqvipHander", music.getId(), brType, music.getMusicName(), music.getMusicArtists(), music.getMusicAlbum(), false, addSubsonicPlayListName);
//        return downloadEntity;
//    }
//
//    @Override
//    public ArrayList<DownloadEntity> downloadAlbum(String albumsId, PlugBrType brType, String addSubsonicPlayListName, String artist, Boolean isAudioBook, String albumName) {
//        List<Music> musiclist = getAlbumSongByAlbumsId(albumsId);
//        AtomicReference<String> change = new AtomicReference<>(artist);
//        ArrayList<DownloadEntity> downloadEntities = new ArrayList<>();
//
//        SqConfig accompaniment = getConfigService().getOne(new QueryWrapper<SqConfig>().eq("config_key", "music.ignore.accompaniment"));
//        SqConfig matchAlbumSinger = getConfigService().getOne(new QueryWrapper<SqConfig>().eq("config_key", "music.strong.match.album.singer"));
//        SqConfig albumSingerUnity = getConfigService().getOne(new QueryWrapper<SqConfig>().eq("config_key", "music.album.singer.unity"));
//
//        musiclist.forEach(md -> {
//            if (Boolean.getBoolean(accompaniment.getConfigValue())) {
//                if (md.getMusicName().contains("(伴奏)") || md.getMusicName().contains("(试听版)") || md.getMusicName().contains("片段")) {
//                    return;
//                }
//            }
//            if (Boolean.getBoolean(matchAlbumSinger.getConfigValue()) && !isAudioBook) {
//                if (!md.getMusicArtists().contains(change.get())) {
//                    return;
//                }
//            }
//            if (!Boolean.getBoolean(albumSingerUnity.getConfigValue()) && !isAudioBook) {
//                change.set(md.getMusicArtists());
//            }
//            PlugBrType plugBrType = null;
//            if (md.getBits() != null && md.getBits().size() > 0) {
//                ArrayList<PlugBrType> bits = md.getBits();
//                //找出最大的码率
//                plugBrType = bits.stream().max(Comparator.comparing(PlugBrType::getBit)).get();
//            }
//            if (isAudioBook) {
//                downloadEntities.add(new DownloadEntity("qqvipHander", md.getId(), plugBrType == null ? brType : plugBrType, md.getMusicName(), artist, albumName, isAudioBook));
//            } else {
//                //添加到缓存
//                downloadEntities.add(new DownloadEntity("qqvipHander", md.getId(), plugBrType == null ? brType : plugBrType, md.getMusicName(), change.get(), md.getMusicAlbum()));
//            }
//
//        });
//        return downloadEntities;
//    }
//
//    @Override
//    public List<DownloadEntity> downloadArtistAllSong(String artistId, PlugBrType brType, String addSubsonicPlayListName) {
//        String searchUrl = qqHander.getConfig().getSearchUrl();
//        String s = qqHander.getqqSearchEntity().artistsTransferAlbumParam(artistId);
//        Mapper mapper = DownloadUtils.getHttp().sync(searchUrl).setBodyPara(s).post().getBody().toMapper();
//        List<Album> albums = qqHander.getqqSearchEntity().artistsTransferAlbum(mapper, qqHander.getConfig());
//        ArrayList<DownloadEntity> downloadEntitys = new ArrayList<>();
//        for (Album album : albums) {
//            ArrayList<DownloadEntity> downloadEntities = downloadAlbum(album.getAlbumId(), brType, addSubsonicPlayListName, album.getAlbumArtists(), false, album.getAlbumName());
//            downloadEntitys.addAll(downloadEntities);
//        }
//        return downloadEntitys;
//    }
//
//    @Override
//    public List<DownloadEntity> downloadArtistAllAlbum(String artistId, PlugBrType brType, String addSubsonicPlayListName) {
//        String searchUrl = qqHander.getConfig().getSearchUrl();
//        String s = qqHander.getqqSearchEntity().artistsTransferAlbumParam(artistId);
//        Mapper mapper = DownloadUtils.getHttp().sync(searchUrl).setBodyPara(s).post().getBody().toMapper();
//        List<Album> albums = qqHander.getqqSearchEntity().artistsTransferAlbum(mapper, qqHander.getConfig());
//        ArrayList<DownloadEntity> downloadEntitys = new ArrayList<>();
//        for (Album album : albums) {
//            ArrayList<DownloadEntity> downloadEntities = downloadAlbum(album.getAlbumId(), brType, addSubsonicPlayListName, album.getAlbumArtists(), false, album.getAlbumName());
//            downloadEntitys.addAll(downloadEntities);
//        }
//        return downloadEntitys;
//    }
//
//
//    public CgiGetAlbumFavInfo userALbymList(int i) {
//        return qqHander.userALbymList(i);
//    }
//
//    public PlaylistBaseRead getUserSelfSongList() {
//        return qqHander.getUserSelfSongList();
//    }
//
//    public DissInfo songListInfo(String tid, String dirid, long l) {
//
//        return qqHander.songListInfo(tid, dirid, l);
//    }
//}
