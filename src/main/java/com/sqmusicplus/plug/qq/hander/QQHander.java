package com.sqmusicplus.plug.qq.hander;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ejlchina.data.Array;
import com.ejlchina.data.Mapper;
import com.ejlchina.okhttps.OkHttps;
import com.sqmusicplus.base.entity.*;
import com.sqmusicplus.base.service.SqConfigService;
import com.sqmusicplus.config.GlobalStatic;
import com.sqmusicplus.plug.base.PlugBrType;
import com.sqmusicplus.plug.base.QQSongType;
import com.sqmusicplus.plug.base.hander.SearchHanderAbstract;
import com.sqmusicplus.plug.entity.*;
import com.sqmusicplus.plug.qq.config.QQConfig;
import com.sqmusicplus.plug.qq.entity.*;
import com.sqmusicplus.plug.qq.entity.getfollowsingerlist.GetFollowSingerList;
import com.sqmusicplus.plug.qq.enums.LoginType;
import com.sqmusicplus.plug.qq.enums.QQSearchType;
import com.sqmusicplus.plug.qq.enums.QRCodeLoginEvents;
import com.sqmusicplus.plug.qq.util.QQMusicUtil;
import com.sqmusicplus.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @Classname QQHander
 * @Description TODO
 * @Version 1.0.0
 * @Date 2023/8/25 9:18
 * @Created by Administrator
 */
@Slf4j
@Service("qqHander")
public class QQHander extends SearchHanderAbstract {

    @Autowired
    private QQConfig config;

    @Autowired
    @Qualifier("qqQrthreadPoolTaskExecutor")
    private ThreadPoolExecutor threadPoolExecutor;

    //二维码检查线程
    private Future<QQMusicQrEventResult> qrCodeCheckFuture;


    @Override
    public  QQConfig getConfig() {
        return  config;
    }

    public QQSearchEntity qqSearchEntity = new QQSearchEntity();

    @Override
    public String getPlugName() {
        return "qq";
    }

    public QQSearchEntity getqqSearchEntity() {
        return qqSearchEntity;
    }

    public void setQqSearchEntity(QQSearchEntity qqSearchEntity) {
        this.qqSearchEntity = qqSearchEntity;
    }

    @Override
    public PlugSearchResult<PlugSearchMusicResult> querySongByName(SearchKeyData searchKeyData) {
        String searchUrl = config.getSearchUrl();
        String s = getqqSearchEntity().searchRequestParam(searchKeyData.getSearchkey(), QQSearchType.MUSIC.getValue(), searchKeyData.getPageIndex(), searchKeyData.getPageSize());
        Mapper mapper = DownloadUtils.getHttp().sync(searchUrl).addHeader("Content-Type","json/application;charset=utf-8").addHeader("Referer","https://y.qq.com").addHeader("User-Agent","QQ%E9%9F%B3%E4%B9%90/73222 CFNetwork/1406.0.3 Darwin/22.4.0").bodyType(OkHttps.JSON).setBodyPara(s).post().getBody().toMapper();
        PlugSearchResult<PlugSearchMusicResult> plugSearchResult = getqqSearchEntity().toMusicPlugSearchResult(mapper, config);
        plugSearchResult.setSearchIndex(searchKeyData.getPageIndex());
        plugSearchResult.setSearchSize(searchKeyData.getPageSize());
        plugSearchResult.setSearchTotal(plugSearchResult.getRecords().size());
        plugSearchResult.setSearchKeyWork(searchKeyData.getSearchkey());
        return plugSearchResult;
    }

    @Override
    public PlugSearchResult<PlugSearchArtistResult> queryArtistByName(SearchKeyData searchKeyData) {
        String searchUrl = config.getSearchUrl();
        String s = getqqSearchEntity().searchRequestParam(searchKeyData.getSearchkey(), QQSearchType.ARTIST.getValue(), searchKeyData.getPageIndex(), searchKeyData.getPageSize());
        Mapper mapper = DownloadUtils.getHttp().sync(searchUrl).addHeader("Content-Type","json/application;charset=utf-8").addHeader("Referer","https://y.qq.com").addHeader("User-Agent","QQ%E9%9F%B3%E4%B9%90/73222 CFNetwork/1406.0.3 Darwin/22.4.0").bodyType(OkHttps.JSON).setBodyPara(s).post().getBody().toMapper();
        PlugSearchResult<PlugSearchArtistResult> artistPlugSearchResult = getqqSearchEntity().toArtistPlugSearchResult(mapper);
        artistPlugSearchResult.setSearchIndex(searchKeyData.getPageIndex());
        artistPlugSearchResult.setSearchSize(searchKeyData.getPageSize());
        artistPlugSearchResult.setSearchTotal(artistPlugSearchResult.getRecords().size());
        artistPlugSearchResult.setSearchKeyWork(searchKeyData.getSearchkey());
        return artistPlugSearchResult;
    }

    @Override
    public PlugSearchResult<PlugSearchAlbumResult> queryAlbumByName(SearchKeyData searchKeyData) {
        String searchUrl = config.getSearchUrl();
        String s = getqqSearchEntity().searchRequestParam(searchKeyData.getSearchkey(), QQSearchType.ALBUM.getValue(), searchKeyData.getPageIndex(), searchKeyData.getPageSize());
        Mapper mapper = DownloadUtils.getHttp().sync(searchUrl).addHeader("Content-Type","json/application;charset=utf-8").addHeader("Referer","https://y.qq.com").addHeader("User-Agent","QQ%E9%9F%B3%E4%B9%90/73222 CFNetwork/1406.0.3 Darwin/22.4.0").bodyType(OkHttps.JSON).setBodyPara(s).post().getBody().toMapper();
        PlugSearchResult<PlugSearchAlbumResult> albumPlugSearchResult = getqqSearchEntity().toAlbumPlugSearchResult(mapper);
        albumPlugSearchResult.setSearchIndex(searchKeyData.getPageIndex());
        albumPlugSearchResult.setSearchSize(searchKeyData.getPageSize());
        albumPlugSearchResult.setSearchTotal(albumPlugSearchResult.getRecords().size());
        albumPlugSearchResult.setSearchKeyWork(searchKeyData.getSearchkey());
        return albumPlugSearchResult;
    }

    @Override
    public Music querySongById(String SongId) {
        //查看字符是否包含,
        if (SongId.contains(",")) {
            SongId = SongId.split(",")[0];
        }
        String searchUrl = config.getSearchUrl();
        String s = getqqSearchEntity().musicInfoRequestParam(SongId);
        Mapper mapper = DownloadUtils.getHttp().sync(searchUrl).setBodyPara(s).post().getBody().toMapper();
        return getqqSearchEntity().songInfoToMusic(mapper, config);
    }

    @Override
    public Artists queryArtistById(String artistId) {
        Artists plugArtistResult = getqqSearchEntity().toPlugArtistResult(artistId, config);
        return plugArtistResult;

    }

    @Override
    public Album queryAlbumById(String albumId) {
        String searchUrl = config.getSearchUrl();
        String s = getqqSearchEntity().albumInfoRequestParam(albumId);
        Mapper mapper = DownloadUtils.getHttp().sync(searchUrl).setBodyPara(s).post().getBody().toMapper();
        Album album = getqqSearchEntity().albumInfoToAlbum(mapper, config);
        return album;


    }

    @Override
    public String queryLyric(String SongId) {
        String s = getqqSearchEntity().toPlugLyricResult(SongId,config);
        return s;
    }

    @Override
    public List<Album> getAlbumsByArtist(String artistId, Integer pageIndex, Integer pageSize) {
        String searchUrl = config.getSearchUrl();
        String s = getqqSearchEntity().artistsTransferAlbumParam(artistId);
        Mapper mapper = DownloadUtils.getHttp().sync(searchUrl).setBodyPara(s).post().getBody().toMapper();
        List<Album> albums = getqqSearchEntity().artistsTransferAlbum(mapper, config);
        return albums;
    }

    @Override
    public List<Music> getAlbumSongByAlbumsId(String albumsId) {
        String searchUrl = config.getSearchUrl();
        String s = getqqSearchEntity().albumInfoRequestParam(albumsId);
        Mapper mapper = DownloadUtils.getHttp().sync(searchUrl).setBodyPara(s).post().getBody().toMapper();
        List<Music> albumMusic = getqqSearchEntity().albumInfoToAlbumMusic(mapper, config);
        return albumMusic;


    }

    @Override
    public HashMap<String, String> getDownloadUrl(String musicId, PlugBrType brType) {
        if (musicId.contains(",")){
            musicId = musicId.split(",")[0];
        }
        QQSongType qqSongType=  QQSongType.FLAC;
        String musickey ="";
        String qq ="";
        String loginType ="";
        if (brType.getValue().equalsIgnoreCase("HQ_M500")){
            qqSongType = QQSongType.MP3_128;
        }else  if (brType.getValue().equalsIgnoreCase("HQ_M800")){
            qqSongType = QQSongType.MP3_320;

        }else if (brType.getValue().equalsIgnoreCase("SQ_F000")){
            qqSongType = QQSongType.FLAC;
        }else  if (brType.getValue().equalsIgnoreCase("HR_RS01")){
            qqSongType = QQSongType.FLAC;

        }else  if (brType.getValue().equalsIgnoreCase("HR_Q000")){
            qqSongType = QQSongType.FLAC;
        }else  if (brType.getValue().equalsIgnoreCase("HR_AI00")){
            qqSongType = QQSongType.FLAC;
        }

        SqConfigService configService = getConfigService();
        SqConfig sqConfig = configService.getOne(new QueryWrapper<SqConfig>().eq("config_key", GlobalStatic.QQ_LOGIN_COOKIE_KEY));
        if (sqConfig!=null&&StringUtils.isNotBlank(sqConfig.getConfigValue())){
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
        String s = getqqSearchEntity().downloadRequestParam(qq,musickey,loginType,fileName,musicId);
        String searchUrl = config.getSearchUrl();

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
            //有此参数则需要解密
            ekey = mapper2.getString("ekey");
        }
        if (StringUtils.isNotBlank(url)){
            String baseUrl = "https://isure.stream.qqmusic.qq.com/";
            url = baseUrl +url;
        }else{
            return null;
            //重新生成一个下载
//            PlugBrType qqMp3320 = PlugBrType.QQ_MP3_320;

//            PlugBrType qqMp3320 = PlugBrType.QQ_MP3_320;
//            HashMap<String, String> downloadUrl = getDownloadUrl(musicId, qqMp3320);
//            if (PlugBrType.QQ_MP3_320.getBit().toString().equals(downloadUrl.get("bit"))){
//                return downloadUrl;
//            }
        }
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("url", url);
        stringStringHashMap.put("type", brType.getType());
        stringStringHashMap.put("bit", brType.getBit().toString());
        stringStringHashMap.put("ekey", ekey);
        return stringStringHashMap;











//        HashMap<String, String> stringStringHashMap = new HashMap<>();
//        stringStringHashMap.put("url", "");
//        stringStringHashMap.put("type", brType.getType());
//        stringStringHashMap.put("bit", brType.getBit().toString());
//        // 获取 mediaMid
//        String infoReqBody = String.format("{\"comm\":{\"ct\":\"19\",\"cv\":\"1859\",\"uin\":\"0\"},\"req\":{\"module\":\"music.pf_song_detail_svr\"," +
//                "\"method\":\"get_song_detail_yqq\",\"param\":{\"song_type\":0,\"song_mid\":\"%s\"}}}", musicId);
//        HTTP http = DownloadUtils.getHttp();
//        String sign = QSignHelper.sign(infoReqBody);
//        HttpResult post1 = http.sync("https://u.y.qq.com/cgi-bin/musics.fcg?format=json&sign=" + sign).setBodyPara(infoReqBody).post();
//        JSONObject infoBodyJson = JSONObject.parseObject(post1.getBody().toString());
//
//        if (infoBodyJson.getIntValue("code") != 0 || infoBodyJson.getJSONObject("req").getIntValue("code") != 0){
//
//            return stringStringHashMap;
//        }
//        String mediaMid = infoBodyJson.getJSONObject("req").getJSONObject("data")
//                .getJSONObject("track_info").getJSONObject("file").getString("media_mid");
//        // 获取 url
//        String reqBody = String.format("{\"req_0\":{\"module\":\"vkey.GetVkeyServer\",\"method\":\"CgiGetVkey\",\"param\":{\"filename\":[\"%s\"]," +
//                        "\"guid\":\"%s\",\"songmid\":[\"%s\"],\"songtype\":[0],\"uin\":\"%s\",\"loginflag\":1,\"platform\":\"20\"}}," +
//                        "\"comm\":{\"qq\":\"%s\",\"authst\":\"%s\",\"ct\":\"26\",\"cv\":\"2010101\",\"v\":\"2010101\"}}",
//                brType.getValue().split("_")[1] + mediaMid +brType.getType(), "0", musicId, "0", "", "");
//        String sign2 = QSignHelper.sign(reqBody);
//        HttpResult post2 = http.sync( "https://u.y.qq.com/cgi-bin/musics.fcg?format=json&sign=" + sign2).setBodyPara(reqBody).post();
//
//
//
//        JSONObject urlJson = JSONObject.parseObject(post2.getBody().toString());
//        JSONObject data = urlJson.getJSONObject("req_0").getJSONObject("data");
//        if (urlJson==null||urlJson.isEmpty()){
//
//            return stringStringHashMap;
//        }
//        String sip = data.getJSONArray("sip").getString(0);
//        String url = data.getJSONArray("midurlinfo").getJSONObject(0).getString("purl");
//        String trackUrl = sip + url;
//        stringStringHashMap.put("url", trackUrl);
//
//        return stringStringHashMap;

//
//        HTTP http = DownloadUtils.getHttp();
//        SHttpTask sync = http.sync("https://music-api.gdstudio.xyz/api.php");
//        sync.addUrlPara("types", "url");
//        sync.addUrlPara("source", "tencent");
//
//        sync.addUrlPara("id", musicId);
//
//        Integer bit = brType.getBit();
//        if (bit.intValue()>320){
//            sync.addUrlPara("br", "999");
//        }else{
//            sync.addUrlPara("br", bit.toString());
//        }
//
//
//        HttpResult get = sync.get();
//        Mapper mapper = get.getBody().toMapper();
//        if (StringUtils.isBlank(mapper.getString("url"))){
//            return null;
//        }else{
//            HashMap<String, String> stringStringHashMap = new HashMap<>();
//            stringStringHashMap.put("url", mapper.getString("url"));
//            stringStringHashMap.put("type", brType.getType());
//            stringStringHashMap.put("bit", brType.getBit().toString());
//            return stringStringHashMap;
//        }

//
//        String platform = "qq";
//        String t2 = brType.getValue().split("_")[0];
//        String device = "MI 14 Pro Max";
//        String osVersion = "13" ;
//         String time = DateUtils.getNowDate().getTime()/1000+"";
//        String  lowerCase = DigestUtil.md5Hex("6d849adb2f3e00d413fe48efbb18d9bb" + time + "6562653262383463363633646364306534333668");
//        String   s6 = "{\\\"method\\\":\\\"GetMusicUrl\\\",\\\"platform\\\":\\\"" + platform + "\\\",\\\"t1\\\":\\\"" + musicId + "\\\",\\\"t2\\\":\\\"" + t2 + "\\\"}";
//        String s7 = "{\\\"uid\\\":\\\"\\\",\\\"token\\\":\\\"\\\",\\\"deviceid\\\":\\\"84ac82836212e869dbeea73f09ebe52b\\\",\\\"appVersion\\\":\\\"4.1.2\\\",\\\"vercode\\\":\\\"4120\\\",\\\"device\\\":\\\"" + device + "\\\",\\\"osVersion\\\":\\\"" + osVersion + "\\\"}";
//        String  s8 = "{\n\t\"text_1\":\t\"" + s6 + "\",\n\t\"text_2\":\t\"" + s7 + "\",\n\t\"sign_1\":\t\"" + lowerCase + "\",\n\t\"time\":\t\"" + time + "\",\n\t\"sign_2\":\t\"" + DigestUtil.md5Hex(
//                s6.replace("\\", "") + s7.replace("\\", "") + lowerCase + time + "NDRjZGIzNzliNzEe") + "\"\n}" ;
//        byte[] utf8Bytes = s8.getBytes(StandardCharsets.UTF_8);
//        String hexString = ByteArrayUtil.toHexString(utf8Bytes);
//        String upperHexString = hexString.toUpperCase();
//        byte[] encodedBytes = upperHexString.getBytes(StandardCharsets.UTF_8);
//        byte[] compress = ZLibUtils.compress(encodedBytes);
//        HTTP http = DownloadUtils.getHttp();
////        SHttpTask sync = http.sync(config.getDownloadUrl());
//        SHttpTask sync = http.sync("http://gcsp.kzti.top:1030/client/cgi-bin/api.fcg");
//        sync.setBodyPara(compress);
//        HttpResult post = sync.post();
//        byte[] decompress = ZLibUtils.decompress(post.getBody().toBytes());
//        String s = new String(decompress);
//        JSONObject jsonObject = JSONObject.parseObject(s);
//        String downloadurl = jsonObject.getString("data");
//        HashMap<String, String> stringStringHashMap = new HashMap<>();
//        stringStringHashMap.put("url", downloadurl);
//        stringStringHashMap.put("type", brType.getType());
//        stringStringHashMap.put("bit", brType.getBit().toString());
//        return stringStringHashMap;




//        String deonloadType = "flac";
//        Integer bit = brType.getBit();
//        if (bit == 128) {
//            deonloadType = "128k";
//        } else if (bit == 320) {
//            deonloadType = "320k";
//        } else {
//            deonloadType =  "flac";
//        }
//        HTTP http = DownloadUtils.getHttp();
//        QQConfig config = getConfig();
//        String downloadUrl = config.getDownloadUrl();
//        downloadUrl = downloadUrl.replaceAll("#\\{pmid}", musicId).replaceAll("#\\{brType}", deonloadType);
//        HttpResult post = http.sync(downloadUrl).addHeader("X-Request-Key","ikunsource")
//                .addHeader("Accept","*/*")
//                .addHeader("Accept-Encoding","gzip, deflate, br")
//                .get();
//        Mapper mapper = post.getBody().toMapper();
//        int code = mapper.getInt("code");
//        if (code != 0) {
//            return null;
//        }
//        String downloadurl = mapper.getString("data");
//        HashMap<String, String> stringStringHashMap = new HashMap<>();
//        stringStringHashMap.put("url", downloadurl);
//        stringStringHashMap.put("type", brType.getType());
//        stringStringHashMap.put("bit", brType.getBit().toString());
//        return stringStringHashMap;
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
        if (brType.getValue().equalsIgnoreCase("HQ_M500")){
            qqSongType = QQSongType.MP3_128;
        }else  if (brType.getValue().equalsIgnoreCase("HQ_M800")){
            qqSongType = QQSongType.MP3_320;

        }else if (brType.getValue().equalsIgnoreCase("SQ_F000")){
            qqSongType = QQSongType.FLAC;
        }else  if (brType.getValue().equalsIgnoreCase("HR_RS01")){
            qqSongType = QQSongType.FLAC;

        }else  if (brType.getValue().equalsIgnoreCase("HR_Q000")){
            qqSongType = QQSongType.FLAC;
        }else  if (brType.getValue().equalsIgnoreCase("HR_AI00")){
            qqSongType = QQSongType.FLAC;
        }

        SqConfigService configService = getConfigService();
        SqConfig sqConfig = configService.getOne(new QueryWrapper<SqConfig>().eq("config_key", GlobalStatic.QQ_LOGIN_COOKIE_KEY));
        if (sqConfig!=null&&StringUtils.isNotBlank(sqConfig.getConfigValue())){
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
        String s = getqqSearchEntity().downloadRequestParam(qq,musickey,loginType,fileName,musicId);
        String searchUrl = config.getSearchUrl();

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
            //有此参数则需要解密
            ekey = mapper2.getString("ekey");
        }
        if (StringUtils.isNotBlank(url)){
            String baseUrl = "https://isure.stream.qqmusic.qq.com/";
            url = baseUrl +url;
        }else{
            if (!brType.getValue().equalsIgnoreCase("HQ_M800")){
                downloadEntity.setBrType(PlugBrType.QQ_MP3_320);
                DownloadInfo downloadInfo = MusicUtils.downloadEntitytoDownloadInfoTo(downloadEntity);
                getDownloadInfoService().add(downloadInfo);
            }
            return null;
        }
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("url", url);
        stringStringHashMap.put("type", brType.getType());
        stringStringHashMap.put("bit", brType.getBit().toString());
        stringStringHashMap.put("ekey", ekey);
        return stringStringHashMap;

    }

    @Override
    public DownloadEntity downloadSong(String musicid, PlugBrType brType, String musicname, String artistname, String albumname, Boolean isAudioBook, String addSubsonicPlayListName) {
        Music music = querySongById(musicid);
        DownloadEntity downloadEntity = new DownloadEntity("qqHander",musicid, brType, music.getMusicName(), music.getMusicArtists(), music.getMusicAlbum(), isAudioBook, isAudioBook?addSubsonicPlayListName:null);
        return downloadEntity;
    }

    @Override
    public DownloadEntity downloadSong(Music music, PlugBrType brType, Boolean isAudioBook, String addSubsonicPlayListName) {
        DownloadEntity downloadEntity = new DownloadEntity("qqHander",music.getId(), brType, music.getMusicName(), music.getMusicArtists(), music.getMusicAlbum(), isAudioBook, isAudioBook?addSubsonicPlayListName:null);
        return downloadEntity;
    }

    @Override
    public DownloadEntity downloadSong(Music music, PlugBrType brType, String addSubsonicPlayListName) {
        DownloadEntity downloadEntity = new DownloadEntity("qqHander",music.getId(), brType, music.getMusicName(), music.getMusicArtists(), music.getMusicAlbum(), false, addSubsonicPlayListName);
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
            if (isAudioBook) {
                downloadEntities.add(new DownloadEntity("qqHander",md.getId(), brType, md.getMusicName(), artist, albumName, isAudioBook));
            } else {
                //添加到缓存
                downloadEntities.add(new DownloadEntity("qqHander",md.getId(), brType, md.getMusicName(), change.get(), md.getMusicAlbum()));
            }

        });
        return downloadEntities;

    }

    @Override
    public List<DownloadEntity> downloadArtistAllSong(String artistId, PlugBrType brType, String addSubsonicPlayListName) {
        String searchUrl = config.getSearchUrl();
        String s = getqqSearchEntity().artistsTransferAlbumParam(artistId);
        Mapper mapper = DownloadUtils.getHttp().sync(searchUrl).setBodyPara(s).post().getBody().toMapper();
        List<Album> albums = getqqSearchEntity().artistsTransferAlbum(mapper, config);
        ArrayList<DownloadEntity> downloadEntitys = new ArrayList<>();
        for (Album album : albums) {
            ArrayList<DownloadEntity> downloadEntities = downloadAlbum(album.getAlbumId(), brType, addSubsonicPlayListName, album.getAlbumArtists(), false, album.getAlbumName());
            downloadEntitys.addAll(downloadEntities);
        }
        return downloadEntitys;
    }

    @Override
    public List<DownloadEntity> downloadArtistAllAlbum(String artistId, PlugBrType brType, String addSubsonicPlayListName) {
        String searchUrl = config.getSearchUrl();
        String s = getqqSearchEntity().artistsTransferAlbumParam(artistId);
        Mapper mapper = DownloadUtils.getHttp().sync(searchUrl).setBodyPara(s).post().getBody().toMapper();
        List<Album> albums = getqqSearchEntity().artistsTransferAlbum(mapper, config);
        ArrayList<DownloadEntity> downloadEntitys = new ArrayList<>();
        for (Album album : albums) {
            ArrayList<DownloadEntity> downloadEntities = downloadAlbum(album.getAlbumId(), brType, addSubsonicPlayListName, album.getAlbumArtists(), false, album.getAlbumName());
            downloadEntitys.addAll(downloadEntities);
        }
        return downloadEntitys;
    }






    /**
     * 获取 qq登录二维码
     * @param
     * @return 二维码信息
     */
    public QQMusicQr getQQLoginQr() {
        QQMusicQr qqLoginQr = QQLoginHelp.getQQLoginQr();
        SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
        filter.getExcludes().add("LoginType");
        String jsonString = JSONObject.toJSONString(qqLoginQr, filter);
        getConfigService().remove(Wrappers.<SqConfig>lambdaUpdate().eq(SqConfig::getConfigKey, GlobalStatic.QQ_LOGIN_QR_KEY));
        SqConfig sqConfig = new SqConfig();
        sqConfig.setConfigKey(GlobalStatic.QQ_LOGIN_QR_KEY);
        sqConfig.setConfigValue(jsonString);
        sqConfig.setConfigName("QQ登录二维码key");
        sqConfig.setType("input");
        sqConfig.setConfigShow("N");
        getConfigService().save(sqConfig);
        //异步监听
        syncCheckQrCodeStatus();


        return qqLoginQr;
    }
    /**
     * 获取二维码状态
     */
    public  QQMusicQrEventResult checkQQQr(QQMusicQr qqMusicQr) {
        return QQLoginHelp.checkQQQr(qqMusicQr);
    }

    public  QQMusicQrEventResult checkQQQr() {
        SqConfig sqConfig = getConfigService().selectByKeyAndValue(GlobalStatic.QQ_LOGIN_QR_KEY);
        QQMusicQr qqMusicQr = JSONObject.parseObject(sqConfig.getConfigValue(), QQMusicQr.class);
        qqMusicQr.setQrType(LoginType.getByType(qqMusicQr.getQrTypeStr()));
        QQMusicQrEventResult qqMusicQrEventResult = QQLoginHelp.checkQQQr(qqMusicQr);
        if (qqMusicQrEventResult.getQrCodeLoginEvents() == QRCodeLoginEvents.DONE){
            return qqMusicQrEventResult;
        }
        return null;

    }

    /**
     * 获取授权code
     */
    public  QQMusicQrEventResult getAuthorizeByQQMusicQrEventResult(QQMusicQrEventResult eventResult) {
        return QQLoginHelp.getAuthorizeByQQMusicQrEventResult(eventResult);
    }
    /**
     * 根据code获得cookie
     */
    public QQMusicCookieInfo getCookieByCode(String code) {
        String cookieByCodeParam = qqSearchEntity.getCookieByCodeParam(code);
        String searchUrl = getConfig().getSearchUrl();
        String string = DownloadUtils.getHttp().sync(searchUrl).setBodyPara(cookieByCodeParam).post().getBody().toString();
        QQMusicCookieInfo cookieByCode = qqSearchEntity.getCookieByCode(string);
        if (cookieByCode != null){
            saveCookie(cookieByCode);
        }
        return cookieByCode;
    }
    /**
     * cookies保存数据库
     */
    public void saveCookie(QQMusicCookieInfo qqMusicCookieInfo) {
        getConfigService().remove(new QueryWrapper<SqConfig>().eq("config_key", GlobalStatic.QQ_LOGIN_COOKIE_KEY));
        SqConfig sqConfig = new SqConfig();
        sqConfig.setConfigKey(GlobalStatic.QQ_LOGIN_COOKIE_KEY);
        sqConfig.setConfigValue(JSONObject.toJSONString(qqMusicCookieInfo));
        sqConfig.setConfigName("QQ登录Cookie请勿做任何操作");
        sqConfig.setType("input");
        sqConfig.setConfigShow("N");
        getConfigService().save(sqConfig);
    }




    /**
     * 监听二维码的扫码状态
     */
    public void syncCheckQrCodeStatus() {
        new Thread(() -> {
            // 关闭以前的全部任务
            if (qrCodeCheckFuture != null && !qrCodeCheckFuture.isDone()) {
                qrCodeCheckFuture.cancel(true);
            }

            long startTime = System.currentTimeMillis();
            long timeout = 5 * 60 * 1000; // 5 minutes in milliseconds
            // 异步监控二维码超5分钟自动放弃
            Callable<QQMusicQrEventResult> task = this::checkQQQr;
            qrCodeCheckFuture = threadPoolExecutor.submit(task);
            while (System.currentTimeMillis() - startTime < timeout) {
                try {
                    // 每次等待1秒
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    log.error("线程在等待二维码检查任务时被中断", e);
                    Thread.currentThread().interrupt(); // 恢复中断状态
                    break;
                }

                if (qrCodeCheckFuture.isDone()) {
                    try {
                        QQMusicQrEventResult result = qrCodeCheckFuture.get();
                        if (result != null) {
                            log.info("QQ二维码检查任务成功完成。");
                            //开始获取授权code
                            QQMusicQrEventResult authorizeByQQMusicQrEventResult = getAuthorizeByQQMusicQrEventResult(result);
                            if (authorizeByQQMusicQrEventResult.getQrCodeLoginEvents() == QRCodeLoginEvents.CODE_SUCCESS){
                                getCookieByCode(authorizeByQQMusicQrEventResult.getCode());
                            }
                            break; // 任务成功完成，终止循环
                        }else{
                            // 任务失败，重新提交任务
                            qrCodeCheckFuture = threadPoolExecutor.submit(task);
                        }
                    } catch (InterruptedException | ExecutionException e) {
                        log.error("获取QQ二维码检查任务结果时发生错误", e);
                        Thread.currentThread().interrupt(); // 恢复中断状态
                        break; // 发生错误，终止循环
                    }
                }
            }

            if (!qrCodeCheckFuture.isDone()) {
                qrCodeCheckFuture.cancel(true);
                log.warn("QQ二维码检查任务在5分钟内未完成并已被取消。");
            }

        }).start();
    }
    /**
     * 获取登录状态四否有效
     */
    public Boolean getLoginStatus() {
        SqConfig sqConfig = getConfigService().selectByKeyAndValue(GlobalStatic.QQ_LOGIN_COOKIE_KEY);
        if (sqConfig!=null&&StringUtils.isNotBlank(sqConfig.getConfigValue())){
            QQMusicCookieInfo qqMusicCookieInfo = JSONObject.parseObject(sqConfig.getConfigValue(), QQMusicCookieInfo.class);
            if (qqMusicCookieInfo != null){
                String checkCookieParam = qqSearchEntity.checkCookieParam(qqMusicCookieInfo);
                String searchUrl = getConfig().getSearchUrl();
                String string = DownloadUtils.getHttp().sync(searchUrl).setBodyPara(checkCookieParam).post().getBody().toString();
                QQMuserUserInfo qqMuserUserInfo = qqSearchEntity.checkCookie(string);
                if (qqMuserUserInfo != null&&qqMuserUserInfo.getCode().longValue()==0L){
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * 刷新token
     */
    public QQMusicCookieInfo refreshToken() {
        SqConfig sqConfig = getConfigService().selectByKeyAndValue(GlobalStatic.QQ_LOGIN_COOKIE_KEY);
        if (sqConfig!=null&&StringUtils.isNotBlank(sqConfig.getConfigValue())){
            QQMusicCookieInfo qqMusicCookieInfo = JSONObject.parseObject(sqConfig.getConfigValue(), QQMusicCookieInfo.class);
            if (qqMusicCookieInfo != null){
                String refreshTokenParam = qqSearchEntity.refreshCookieParam(qqMusicCookieInfo);
                String searchUrl = getConfig().getSearchUrl();
                String string = DownloadUtils.getHttp().sync(searchUrl).setBodyPara(refreshTokenParam).post().getBody().toString();
                QQMusicCookieInfo qqMusicCookieInfo1 = qqSearchEntity.refreshCookie(string);
                if (qqMusicCookieInfo1 != null){
                    saveCookie(qqMusicCookieInfo1);
                }
                return qqMusicCookieInfo1;
            }
        }
        return null;

    }
    /**
     * 获取用户收藏歌单
     * 页码从1开始
     */
    public CgiGetPlaylistFavInfo getUserFavSongList(int page) {
        SqConfig sqConfig = getConfigService().selectByKeyAndValue(GlobalStatic.QQ_LOGIN_COOKIE_KEY);
        if (sqConfig!=null&&StringUtils.isNotBlank(sqConfig.getConfigValue())){
            QQMusicCookieInfo qqMusicCookieInfo = JSONObject.parseObject(sqConfig.getConfigValue(), QQMusicCookieInfo.class);
            if (qqMusicCookieInfo != null){
                String musicid = qqMusicCookieInfo.getMusicid();
                String musickey = qqMusicCookieInfo.getMusickey();
                String loginType = qqMusicCookieInfo.getLoginType().toString();
                String encryptUin = qqMusicCookieInfo.getEncryptUin();
                String getUserFavSongListParam = qqSearchEntity.followSongListParam(musicid,musickey,loginType,encryptUin,50,  page);
                String searchUrl = getConfig().getSearchUrl();
                OkHttpUtils builder = OkHttpUtils.builder();
                String sync = builder.url(searchUrl)
                        .post(true, getUserFavSongListParam).sync();
                JSONObject jsonObject = JSONObject.parseObject(sync);
                JSONObject req = jsonObject.getJSONObject("req");
                CgiGetPlaylistFavInfo cgiGetPlaylistFavInfo = qqSearchEntity.followSongList(req);
                return cgiGetPlaylistFavInfo;
            }
        }
        return null;
    }
    /**
     * 获取用户自己窗户建的歌单
     */
    public PlaylistBaseRead getUserSelfSongList() {
        SqConfig sqConfig = getConfigService().selectByKeyAndValue(GlobalStatic.QQ_LOGIN_COOKIE_KEY);
        if (sqConfig!=null&&StringUtils.isNotBlank(sqConfig.getConfigValue())){
            QQMusicCookieInfo qqMusicCookieInfo = JSONObject.parseObject(sqConfig.getConfigValue(), QQMusicCookieInfo.class);
            if (qqMusicCookieInfo != null){
                String musicid = qqMusicCookieInfo.getMusicid();
                String musickey = qqMusicCookieInfo.getMusickey();
                String loginType = qqMusicCookieInfo.getLoginType().toString();
                String s = qqSearchEntity.userSelfSongListParam(musicid, musickey, loginType);
                String searchUrl = getConfig().getSearchUrl();
                OkHttpUtils builder = OkHttpUtils.builder();
                String sync = builder.url(searchUrl)
                        .post(true, s).sync();
                JSONObject jsonObject = JSONObject.parseObject(sync);
                JSONObject req = jsonObject.getJSONObject("req");
                PlaylistBaseRead playlistBaseRead = qqSearchEntity.userSelfSongList(req);
                return playlistBaseRead;
            }
        }
        return null;
    }
    /**
     * 收藏的专辑
     */
    public  CgiGetAlbumFavInfo userALbymList( int page) {
        SqConfig sqConfig = getConfigService().selectByKeyAndValue(GlobalStatic.QQ_LOGIN_COOKIE_KEY);
        if (sqConfig!=null&&StringUtils.isNotBlank(sqConfig.getConfigValue())){
            QQMusicCookieInfo qqMusicCookieInfo = JSONObject.parseObject(sqConfig.getConfigValue(), QQMusicCookieInfo.class);
            if (qqMusicCookieInfo != null){
                String encryptUin = qqMusicCookieInfo.getEncryptUin();
                String s = qqSearchEntity.userALbymListParam(encryptUin, 50, page);
                String searchUrl = getConfig().getSearchUrl();
                OkHttpUtils builder = OkHttpUtils.builder();
                String sync = builder.url(searchUrl)
                        .post(true, s).sync();
                JSONObject jsonObject = JSONObject.parseObject(sync);
                JSONObject req = jsonObject.getJSONObject("req");
                CgiGetAlbumFavInfo albumFavRead = qqSearchEntity.userALbymList(req);
                return albumFavRead;
            }
        }

    return null;
    }
    /**
     * 查询歌单歌曲详情
     */
    public  DissInfo songListInfo(String mid,String dirid,Long page) {
        SqConfig sqConfig = getConfigService().selectByKeyAndValue(GlobalStatic.QQ_LOGIN_COOKIE_KEY);
        if (sqConfig!=null&&StringUtils.isNotBlank(sqConfig.getConfigValue())){
            QQMusicCookieInfo qqMusicCookieInfo = JSONObject.parseObject(sqConfig.getConfigValue(), QQMusicCookieInfo.class);
            if (qqMusicCookieInfo != null){
//                String encryptUin = qqMusicCookieInfo.getEncryptUin();
                String s = qqSearchEntity.songListInfoRequestParam(mid, dirid, page,50L);
                String searchUrl = getConfig().getSearchUrl();
                OkHttpUtils builder = OkHttpUtils.builder();
                String sync = builder.url(searchUrl)
                        .post(true, s).sync();
                JSONObject jsonObject = JSONObject.parseObject(sync);
                JSONObject req = jsonObject.getJSONObject("req");
                DissInfo dissInfo = qqSearchEntity.songListInfo(req);
                return dissInfo;
            }
        }
        return null;
    }

    //用户关注的歌手
    public GetFollowSingerList likeArtists(int page) {
        SqConfig sqConfig = getConfigService().selectByKeyAndValue(GlobalStatic.QQ_LOGIN_COOKIE_KEY);
        if (sqConfig!=null&&StringUtils.isNotBlank(sqConfig.getConfigValue())){
            QQMusicCookieInfo qqMusicCookieInfo = JSONObject.parseObject(sqConfig.getConfigValue(), QQMusicCookieInfo.class);
            if (qqMusicCookieInfo != null){
                String s = qqSearchEntity.followSingerParam(qqMusicCookieInfo.getMusicid(), qqMusicCookieInfo.getMusickey(), qqMusicCookieInfo.getLoginType().toString(), qqMusicCookieInfo.getEncryptUin(),50,page);
                String searchUrl = getConfig().getSearchUrl();
                OkHttpUtils builder = OkHttpUtils.builder();
                String sync = builder.url(searchUrl)
                        .post(true, s).sync();
                JSONObject jsonObject = JSONObject.parseObject(sync);
                JSONObject req = jsonObject.getJSONObject("req");
                GetFollowSingerList followSingerList = qqSearchEntity.followSingerList(req);
                return followSingerList;
            }
        }
        return null;
    }







}
