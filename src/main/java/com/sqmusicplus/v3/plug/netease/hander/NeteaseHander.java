package com.sqmusicplus.v3.plug.netease.hander;

import cn.hutool.core.collection.ListUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sqmusicplus.v3.base.entity.DownloadInfo;
import com.sqmusicplus.v3.plug.entity.Album;
import com.sqmusicplus.v3.plug.entity.Artists;
import com.sqmusicplus.v3.plug.entity.Music;
import com.sqmusicplus.v3.base.enums.PlugBrType;
import com.sqmusicplus.v3.base.enums.SetConfigEnum;
import com.sqmusicplus.v3.config.SqConfigCache;
import com.sqmusicplus.v3.download.vo.DownloadUrlResult;
import com.sqmusicplus.v3.plug.base.hander.SearchHanderAbstract;
import com.sqmusicplus.v3.plug.entity.*;
import com.sqmusicplus.v3.plug.netease.entity.*;
import com.sqmusicplus.v3.plug.netease.enums.SearchEnums;
import com.sqmusicplus.v3.utils.OkHttpUtils;
import com.sqmusicplus.v3.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.yumbo.util.music.MusicEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @Classname NeteaseHander
 * @Description  网易云音乐
 * @Version 1.0.0
 * @Date 2024/2/21 14:49
 * @Created by SQ
 *
 */
@Slf4j
@Component("neteaseHander")
public class NeteaseHander extends SearchHanderAbstract {



    public SQNeteaseCloudMusicInfo neteaseCloudMusicInfo = new SQNeteaseCloudMusicInfo();

    private static final long serialVersionUID = 1L;



    public void initPlug(){
        // 设置网易云音乐的地址
        String baseUrl = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_NETEASE_BASEURL);

        for (String s : baseUrl.split(";")) {
            MusicEnum.setBASE_URL_163Music(s);
            String cookieUrl = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_NETEASE_COOKIEURL);
            if (StringUtils.isNotEmpty(s)&&StringUtils.isNotEmpty(cookieUrl)){
                JSONObject jsonObject = null;
                try {
                    String data = OkHttpUtils.builder()
                            .url(s+cookieUrl)
                            .addHeader("Accept", "application/xml;version=1")
                            .get()
                            .sync();
                    jsonObject = JSONObject.parseObject(data);
                    if(jsonObject==null){
                        log.error("netease使用{}匿名登录失败",s);
                        continue;
                    }
                } catch (Exception e) {
                    log.error("netease使用{}匿名登录失败",s);
                    continue;
                }
                if(jsonObject.getInteger("code")==200){
                    neteaseCloudMusicInfo.setCookieString(jsonObject.getString("cookie"));
                    log.info("netease匿名登录成功使用：{}",s);
                    break;
                }else{
                    log.error("netease使用{}匿名登录失败",s);
                    continue;
                }
            }else{
                String cookie = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_NETEASE_COOKIE);

                if (StringUtils.isNotEmpty(cookie)){
                    neteaseCloudMusicInfo.setCookieString(cookie);
                }
            }
        }



    }


    @Override
    public <C> C getConfig() {
        return null;
    }

    @Override
    public String getPlugName() {
        return "netease";
    }

    @Override
    public List<String> searchTip(String searchKey) {
        ArrayList<String> tips = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("keywords", searchKey);
            JSONObject jsonObject1 = neteaseCloudMusicInfo.searchSuggest(jsonObject);
            JSONObject result = jsonObject1.getJSONObject("result");
            JSONArray jsonArray = result.getJSONArray("songs");
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                String tip = jsonObject2.getString("name");
                JSONArray jsonArray1 = jsonObject2.getJSONArray("artists");
                if (jsonArray1!=null&&jsonArray1.size()>0){
                    tip+=" "+jsonArray1.getJSONObject(0).getString("name");
                }
                tips.add(tip);
            }
        } catch (Exception e) {
        }
        return tips;



    }

    @Override
    public PlugSearchResult<PlugSearchMusicResult> querySongByName(SearchKeyData searchKeyData) {
        JSONObject parameter = new JSONObject();// 请求参数
        parameter.put("keywords", searchKeyData.getSearchkey());
        parameter.put("limit", searchKeyData.getPageSize());
        parameter.put("type", SearchEnums.SONG.getValue());
        parameter.put("offset", ((searchKeyData.getPageIndex())-1)*searchKeyData.getPageSize());
        JSONObject cloudsearch = neteaseCloudMusicInfo.cloudsearch(parameter);
        SearchMusicNeteaseResult searchMusicResult = cloudsearch.toJavaObject(SearchMusicNeteaseResult.class);
        PlugSearchResult<PlugSearchMusicResult> plugSearchResult = new PlugSearchResult<>();
        ArrayList<PlugSearchMusicResult> plugSearchMusicResults = new ArrayList<>();
        if (searchMusicResult.getCode()==200) {
            List<SearchMusicNeteaseResult.ResultDTO.SongsDTO> songs = searchMusicResult.getResult().getSongs();
            songs.forEach(songsDTO -> {
                ArrayList<String> artists = new ArrayList<>();
                ArrayList<String> artistids = new ArrayList<>();
                ArrayList<PlugBrType> brTypes = new ArrayList<>();
                SearchMusicNeteaseResult.ResultDTO.SongsDTO.LDTO l = songsDTO.getL();
                if (l!=null&&l.getBr()==128000&&l.getSize()>0) {
                    brTypes.add(PlugBrType.NETEASE_MP3_128);
                }
                SearchMusicNeteaseResult.ResultDTO.SongsDTO.MDTO m = songsDTO.getM();

                if (m!=null&&m.getBr()==192000&&m.getSize()>0) {
                    brTypes.add(PlugBrType.NETEASE_MP3_192);
                }
                SearchMusicNeteaseResult.ResultDTO.SongsDTO.HDTO h = songsDTO.getH();
                if (h!=null&&h.getBr()==320000&&h.getSize()>0) {
                    brTypes.add(PlugBrType.NETEASE_MP3_320);
                }
                SearchMusicNeteaseResult.ResultDTO.SongsDTO.SqDTO sq = songsDTO.getSq();
                if (sq!=null&&sq.getSize()>0) {
                    brTypes.add(PlugBrType.NETEASE_FLAC_2000);
                }
                SearchMusicNeteaseResult.ResultDTO.SongsDTO.HrDTO hr = songsDTO.getHr();
                if (hr!=null&&hr.getSize()>0) {
                    brTypes.add(PlugBrType.NETEASE_FLAC_3000);
                }



                List<SearchMusicNeteaseResult.ResultDTO.SongsDTO.ArDTO> collect = songsDTO.getAr().stream().toList();
                for (SearchMusicNeteaseResult.ResultDTO.SongsDTO.ArDTO arDTO : collect) {
                    artists.add(arDTO.getName());
                    artistids.add(arDTO.getId().toString());
                }
                PlugSearchMusicResult plugSearchMusicResult = new PlugSearchMusicResult().setArtistName(artists)
                        .setAlbumName(songsDTO.getAl().getName())
                        .setDuration(songsDTO.getDt().toString())
                        .setPic(songsDTO.getAl().getPicUrl())
                        .setArtistids(artistids)
                        .setAlbumid(songsDTO.getAl().getId().toString())
                        .setId(songsDTO.getId().toString())
                        .setPlugName(getPlugName())
                        .setBrTypes(brTypes)
                        .setLyricId(songsDTO.getId().toString())
                        .setName(songsDTO.getName())
                        .setDataInfo(JSONObject.parseObject(JSONObject.toJSONString(songsDTO)));
                plugSearchMusicResults.add(plugSearchMusicResult);
            });
        }
        plugSearchResult.setSearchIndex(searchKeyData.getPageIndex())
                .setSearchSize(searchKeyData.getPageSize())
                .setPlugName(getPlugName())
                .setSearchTotal( searchMusicResult.getResult().getSongCount().intValue())
                .setSearchKeyWork(searchKeyData.getSearchkey())
                .setRecords(plugSearchMusicResults);
        plugSearchResult.setPlugName(getPlugName());
        return plugSearchResult;
    }

    @Override
    public PlugSearchResult<PlugSearchArtistResult> queryArtistByName(SearchKeyData searchKeyData) {
        JSONObject parameter = new JSONObject();// 请求参数
        parameter.put("keywords", searchKeyData.getSearchkey());
        parameter.put("limit", searchKeyData.getPageSize());
        parameter.put("type", SearchEnums.ARTIST.getValue());
        parameter.put("offset", ((searchKeyData.getPageIndex())-1)*searchKeyData.getPageSize());
        JSONObject cloudsearch = neteaseCloudMusicInfo.cloudsearch(parameter);
        ArrayList<PlugSearchArtistResult> plugSearchArtistResults = new ArrayList<>();
        PlugSearchResult<PlugSearchArtistResult> plugSearchResult = new PlugSearchResult<>();
        SearchArtistNeteaseResult artistNeteaseResult = cloudsearch.toJavaObject(SearchArtistNeteaseResult.class);
        if (artistNeteaseResult.getCode()==200) {
            List<SearchArtistNeteaseResult.ResultDTO.ArtistsDTO> artists = artistNeteaseResult.getResult().getArtists();
            artists.forEach(artistsDTO -> {
                PlugSearchArtistResult plugSearchArtistResult = new PlugSearchArtistResult()
                        .setArtistName(artistsDTO.getName())
                        .setArtistid(artistsDTO.getId().toString())
                        .setPlugName(getPlugName())
                        .setPic(artistsDTO.getPicUrl())
                        .setDataInfo(JSONObject.parseObject(JSONObject.toJSONString(artistsDTO)))
                        .setTotal(artistsDTO.getAlbumSize().toString());
                plugSearchArtistResults.add(plugSearchArtistResult);
            });
        }
        plugSearchResult.setSearchIndex(searchKeyData.getPageIndex())
                .setSearchSize(searchKeyData.getPageSize())
                .setPlugName(getPlugName())
                .setSearchTotal(artistNeteaseResult.getResult().getArtistCount())
                .setSearchKeyWork(searchKeyData.getSearchkey())
                .setRecords(plugSearchArtistResults);
        plugSearchResult.setPlugName(getPlugName());
        return plugSearchResult;

    }

    @Override
    public PlugSearchResult<PlugSearchAlbumResult> queryAlbumByName(SearchKeyData searchKeyData) {
        JSONObject parameter = new JSONObject();// 请求参数
        parameter.put("keywords", searchKeyData.getSearchkey());
        parameter.put("limit", searchKeyData.getPageSize());
        parameter.put("type", SearchEnums.ALBUM.getValue());
        parameter.put("offset", ((searchKeyData.getPageIndex())-1)*searchKeyData.getPageSize());
        ArrayList<PlugSearchAlbumResult> plugSearchAlbumResults = new ArrayList<>();
        PlugSearchResult<PlugSearchAlbumResult> plugSearchResult = new PlugSearchResult<>();
        JSONObject cloudsearch = neteaseCloudMusicInfo.cloudsearch(parameter);
        SearchAlbumsNeteaseResult albumsNeteaseResult = cloudsearch.toJavaObject(SearchAlbumsNeteaseResult.class);
        if (albumsNeteaseResult.getCode()==200) {
            List<SearchAlbumsNeteaseResult.ResultDTO.AlbumsDTO> albums = albumsNeteaseResult.getResult().getAlbums();
            albums.forEach(albumsDTO -> {
                PlugSearchAlbumResult plugSearchAlbumResult = new PlugSearchAlbumResult()
                        .setAlbumName(albumsDTO.getName())
                        .setAlbumid(albumsDTO.getId().toString())
                        .setArtistName(albumsDTO.getArtist().getName())
                        .setArtistid(albumsDTO.getArtist().getId().toString())
                        .setPlugName(getPlugName())
                        .setDataInfo(JSONObject.parseObject(JSONObject.toJSONString(albumsDTO)))
                        .setPic(albumsDTO.getPicUrl());
                plugSearchAlbumResults.add(plugSearchAlbumResult);
            });
        }
        plugSearchResult.setSearchIndex(searchKeyData.getPageIndex())
                .setSearchSize(searchKeyData.getPageSize())
                .setPlugName(getPlugName())
                .setSearchTotal(albumsNeteaseResult.getResult().getAlbumCount())
                .setSearchKeyWork(searchKeyData.getSearchkey())
                .setRecords(plugSearchAlbumResults);
        return plugSearchResult;

    }

    @Override
    public Music querySongById(String SongId) {
        JSONObject parameter = new JSONObject();// 请求参数
        parameter.put("ids", SongId);
        JSONObject jsonObject = neteaseCloudMusicInfo.songDetail(parameter);
        MusicInfoNeteaseResult javaObject = jsonObject.toJavaObject(MusicInfoNeteaseResult.class);

        Music music = new Music();
        if (javaObject.getCode()==200) {
            MusicInfoNeteaseResult.SongsDTO songsDTO = javaObject.getSongs().get(0);
            MusicInfoNeteaseResult.SongsDTO.HDTO h = songsDTO.getH();
            MusicInfoNeteaseResult.SongsDTO.MDTO m = songsDTO.getM();
            MusicInfoNeteaseResult.SongsDTO.LDTO l = songsDTO.getL();
            MusicInfoNeteaseResult.SongsDTO.SqDTO sq = songsDTO.getSq();
            MusicInfoNeteaseResult.SongsDTO.HDTO hr = songsDTO.getHr();
            ArrayList<PlugBrType> plugBrTypes = new ArrayList<>();
            if (h!=null&&h.getBr()!=null){
                plugBrTypes.add(PlugBrType.NETEASE_MP3_320);
            }
            if (m!=null&&m.getBr()!=null){
                plugBrTypes.add(PlugBrType.NETEASE_MP3_192);
            }
            if (l!=null&&l.getBr()!=null){
                plugBrTypes.add(PlugBrType.NETEASE_MP3_128);
            }
            if (sq!=null&&sq.getBr()!=null){
                plugBrTypes.add(PlugBrType.NETEASE_FLAC_2000);
            }
            if (hr!=null&&hr.getBr()!=null){
                plugBrTypes.add(PlugBrType.NETEASE_FLAC_3000);
            }
            List<String> artists = songsDTO.getAr().stream().map(e -> e.getName()).collect(Collectors.toList());

            List<String> artistsIds = songsDTO.getAr().stream().map(e -> e.getId().toString()).collect(Collectors.toList());
            music.setId(songsDTO.getId().toString())
                    .setMusicImage(songsDTO.getAl().getPicUrl())
                    .setMusicLyric(queryLyric(SongId))
                    .setMusicAlbum(songsDTO.getAl().getName())
                    .setMusicArtists(artists)
                    .setMusicName(songsDTO.getName())
                    .setMusicDuration(songsDTO.getDt())
                    .setAlbumId(songsDTO.getAl().getId().toString())
                    .setBits(plugBrTypes)
                    .setDataInfo(JSONObject.parseObject(JSONObject.toJSONString(songsDTO)))
                    .setArtistsIds(artistsIds);
        }
        return music;


    }

    @Override
    public Music querySongById(DownloadInfo downloadInfo) {
        return querySongById(downloadInfo.getDownloadMusicId());
    }

    @Override
    public Artists queryArtistById(String artistId) {
        JSONObject parameter = new JSONObject();// 请求参数
        parameter.put("id", artistId);
        JSONObject jsonObject = neteaseCloudMusicInfo.artistDetail(parameter);
        ArtistInfoNeteaseResult infoNeteaseResult = jsonObject.toJavaObject(ArtistInfoNeteaseResult.class);
        Artists artists = new Artists();
        if (infoNeteaseResult.getCode()==200){
            ArtistInfoNeteaseResult.DataDTO data = infoNeteaseResult.getData();
            artists.setMusicArtistsName(data.getArtist().getName())
                    .setMusicArtistsAlias(data.getArtist().getAlias().stream().collect(Collectors.joining(",")))
                    .setMusicArtistsPhoto(data.getArtist().getCover())
                    .setMusicArtistsDescribe(data.getArtist().getBriefDesc());
        }
        return artists;
    }

    @Override
    public Album queryAlbumById(String albumId) {
        JSONObject parameter = new JSONObject();// 请求参数
        parameter.put("id", albumId);
        JSONObject jsonObject = neteaseCloudMusicInfo.album(parameter);
        AlbumInfoNeteaseResult albumInfoNeteaseResult = jsonObject.toJavaObject(AlbumInfoNeteaseResult.class);
        Album album = new Album();
        if (albumInfoNeteaseResult.getCode()==200){
            AlbumInfoNeteaseResult.AlbumDTO albumDTO = albumInfoNeteaseResult.getAlbum();
            List<AlbumInfoNeteaseResult.SongsDTO> songs = albumInfoNeteaseResult.getSongs();
            ArrayList<Music> collect = new ArrayList<>();
            songs.forEach(songsInfoDTO -> {

                AlbumInfoNeteaseResult.SongsDTO songsDTO = songsInfoDTO;
                AlbumInfoNeteaseResult.SongsDTO.HDTO h = songsDTO.getH();
                AlbumInfoNeteaseResult.SongsDTO.MDTO m = songsDTO.getM();
                AlbumInfoNeteaseResult.SongsDTO.LDTO l = songsDTO.getL();
                AlbumInfoNeteaseResult.SongsDTO.SqDTO sq = songsDTO.getSq();
                AlbumInfoNeteaseResult.SongsDTO.HrDTO hr = songsDTO.getHr();
                ArrayList<PlugBrType> plugBrTypes = new ArrayList<>();
                if (h!=null&&h.getBr()!=null){
                    plugBrTypes.add(PlugBrType.NETEASE_MP3_320);
                }
                if (m!=null&&m.getBr()!=null){
                    plugBrTypes.add(PlugBrType.NETEASE_MP3_192);
                }
                if (l!=null&&l.getBr()!=null){
                    plugBrTypes.add(PlugBrType.NETEASE_MP3_128);
                }
                if (sq!=null&&sq.getBr()!=null){
                    plugBrTypes.add(PlugBrType.NETEASE_FLAC_2000);
                }
                if (hr!=null&&hr.getBr()!=null){
                    plugBrTypes.add(PlugBrType.NETEASE_FLAC_3000);
                }

                Music music = new Music();
                music.setId(songsInfoDTO.getId().toString())
                        .setMusicName(songsInfoDTO.getName())
                        .setBits(plugBrTypes)
                        .setMusicDuration(songsInfoDTO.getDt())
                        .setMusicAlbum(songsInfoDTO.getAl().getName())
                        .setMusicArtists(songsInfoDTO.getAr().stream().map(e -> e.getName()).collect(Collectors.toList()))
                        .setMusicImage(albumDTO.getPicUrl())
                        .setAlbumId(albumDTO.getId().toString())
                        .setPlugName(getPlugName())
                        .setDataInfo(JSONObject.parseObject(JSONObject.toJSONString(songsInfoDTO)))
                        .setArtistsIds(songsInfoDTO.getAr().stream().map(e -> e.getId().toString()).collect(Collectors.toList()));
                collect.add(music);
            });

            album.setMusics(collect)
                     .setAlbumTime(albumDTO.getPublishTime().toString())
                     .setAlbumArtist(albumDTO.getArtist().getName())
                     .setAlbumName(albumDTO.getName())
                     .setAlbumDescribe(albumDTO.getDescription())
                     .setAlbumImg(albumDTO.getPicUrl())
                     .setAlbumId(albumDTO.getId().toString())
                     .setAlbumArtistId(albumDTO.getArtist().getId().toString());
        }
        return album;

    }

    @Override
    public String queryLyric(String SongId) {

        JSONObject parameter = new JSONObject();// 请求参数
        parameter.put("id", SongId);
        JSONObject jsonObject = neteaseCloudMusicInfo.lyric(parameter);
        Integer code = jsonObject.getInteger("code");
        if (code==200){
            JSONObject lrc = jsonObject.getJSONObject("lrc");
            String string = lrc.getString("lyric");
            return  string;
        }else{
            return "";
        }
    }

    @Override
    public List<Album> getAlbumsByArtist(String artistId) {
        int pageSize = 50;
        int pageIndex = 1;
        boolean more = true;
        ArrayList<Album> resultAlbum = new ArrayList<>();

        while ( more){
            JSONObject parameter = new JSONObject();// 请求参数
            parameter.put("id", artistId);
            parameter.put("limit", pageSize);
            parameter.put("offset", ((pageIndex-1)*pageSize));
            JSONObject jsonObject = neteaseCloudMusicInfo.artistAlbum(parameter);
            ArtistAllAlubuminNeteaseResult artistAllAlubuminNeteaseResult = jsonObject.toJavaObject(ArtistAllAlubuminNeteaseResult.class);
            List<ArtistAllAlubuminNeteaseResult.HotAlbumsDTO> hotAlbums = artistAllAlubuminNeteaseResult.getHotAlbums();
            List<Album> albums = hotAlbums.stream().map(e -> {
                Album album = new Album();
                album.setAlbumName(e.getName())
                        .setAlbumId(e.getId().toString())
                        .setAlbumImg(e.getPicUrl())
                        .setAlbumDescribe(e.getDescription())
                        .setAlbumTime(e.getPublishTime().toString())
                        .setAlbumArtistId(e.getArtist().getId().toString())
                        .setDataInfo(JSONObject.parseObject(JSONObject.toJSONString(e)))
                        .setAlbumArtist(e.getArtist().getName());
                return album;
            }).collect(Collectors.toList());
            resultAlbum.addAll(albums);
            more = artistAllAlubuminNeteaseResult.getMore();
            if (!more){
                pageIndex++;
            }
        }
        return resultAlbum;
    }

    @Override
    public List<Music> getAlbumSongByAlbumsId(String albumsId) {
        JSONObject parameter = new JSONObject();// 请求参数
        parameter.put("id", albumsId);
        JSONObject jsonObject = neteaseCloudMusicInfo.album(parameter);
        AlbumInfoNeteaseResult albumInfoNeteaseResult = jsonObject.toJavaObject(AlbumInfoNeteaseResult.class);
        ArrayList<Music> collect = new ArrayList<>();

        if (albumInfoNeteaseResult.getCode()==200){
            AlbumInfoNeteaseResult.AlbumDTO albumDTO = albumInfoNeteaseResult.getAlbum();
            List<AlbumInfoNeteaseResult.SongsDTO> songs = albumInfoNeteaseResult.getSongs();
            songs.forEach(songsInfoDTO -> {

                AlbumInfoNeteaseResult.SongsDTO songsDTO = songsInfoDTO;
                AlbumInfoNeteaseResult.SongsDTO.HDTO h = songsDTO.getH();
                AlbumInfoNeteaseResult.SongsDTO.MDTO m = songsDTO.getM();
                AlbumInfoNeteaseResult.SongsDTO.LDTO l = songsDTO.getL();
                AlbumInfoNeteaseResult.SongsDTO.SqDTO sq = songsDTO.getSq();
                AlbumInfoNeteaseResult.SongsDTO.HrDTO hr = songsDTO.getHr();
                ArrayList<PlugBrType> plugBrTypes = new ArrayList<>();
                if (h!=null&&h.getBr()!=null){
                    plugBrTypes.add(PlugBrType.NETEASE_MP3_320);
                }
                if (m!=null&&m.getBr()!=null){
                    plugBrTypes.add(PlugBrType.NETEASE_MP3_192);
                }
                if (l!=null&&l.getBr()!=null){
                    plugBrTypes.add(PlugBrType.NETEASE_MP3_128);
                }
                if (sq!=null&&sq.getBr()!=null){
                    plugBrTypes.add(PlugBrType.NETEASE_FLAC_2000);
                }
                if (hr!=null&&hr.getBr()!=null){
                    plugBrTypes.add(PlugBrType.NETEASE_FLAC_3000);
                }

                Music music = new Music();
                music.setId(songsInfoDTO.getId().toString())
                        .setMusicName(songsInfoDTO.getName())
                        .setBits(plugBrTypes)
                        .setMusicDuration(songsInfoDTO.getDt())
                        .setMusicAlbum(songsInfoDTO.getAl().getName())
                        .setMusicArtists(songsInfoDTO.getAr().stream().map(e -> e.getName()).collect(Collectors.toList()))
                        .setMusicImage(songsInfoDTO.getAl().getPicUrl())
                        .setAlbumId(albumDTO.getId().toString())
                        .setDataInfo(JSONObject.parseObject(JSONObject.toJSONString(songsInfoDTO)))
                        .setArtistsIds(songsInfoDTO.getAr().stream().map(e -> e.getId().toString()).collect(Collectors.toList()));
                collect.add(music);
            });
        }
        return collect;




    }

    @Override
    public DownloadUrlResult getDownloadUrl(DownloadInfo downloadInfo) {
        String downloadMusicId = downloadInfo.getDownloadMusicId();
        String brType = downloadInfo.getDownloadBrType();
        PlugBrType plugBrType = PlugBrType.findById(brType);

        String sqConfigValue = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_NETEASE_EXTEND_DOWNLOAD);
        if (Boolean.valueOf(sqConfigValue)){
            String DOWNLOAD_URL = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_NETEASE_EXTEND_DOWNLOAD_URL);
            String url = DOWNLOAD_URL+"?types=url&source=netease&id="+downloadMusicId+"&";


            Integer bit = plugBrType.getBit();
            if (bit.intValue()>320){
                url+="br=999";
            }else{
                url+="br="+bit.toString();
            }

            String data = OkHttpUtils.builder()
                    .url(url)
                    .addHeader("User-Agent","QQ%E9%9F%B3%E4%B9%90/73222 CFNetwork/1406.0.3 Darwin/22.4.0")
                    .get()
                    .sync();
            JSONObject jsonObject = JSONObject.parseObject(data);


            if (StringUtils.isBlank(jsonObject.getString("url"))){
                return null;
            }else{
                DownloadUrlResult downloadUrlResult = new DownloadUrlResult();
                downloadUrlResult.setUrl(jsonObject.getString("url"));
                downloadUrlResult.setPlugBrTypeId(brType);
                downloadUrlResult.setBit(plugBrType.getBit().toString());
                return downloadUrlResult;
            }

        }else{
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("id", downloadInfo.getDownloadMusicId());
            int i = plugBrType.getBit() * 1000;
            if (plugBrType.getType().equals("flac")){
                jsonObject1.put("type","flac");
            }else{
                jsonObject1.put("br",999000);

            }
            JSONObject jsonObject2 = neteaseCloudMusicInfo.songDownloadUrl(jsonObject1);
            String string = null;
            try {
                string = jsonObject2.getJSONObject("data").getString("url");
            } catch (Exception e) {
                return null;
            }
            if (string != null){
                DownloadUrlResult downloadUrlResult = new DownloadUrlResult();
                downloadUrlResult.setUrl(string);
                downloadUrlResult.setPlugBrTypeId(brType);
                downloadUrlResult.setBit(plugBrType.getBit().toString());
                return downloadUrlResult;
            }else{
                return null;
            }

        }
    }

    @Override
    public ArrayList<DownloadInfo> downloadAlbum(String albumsId, PlugBrType brType, List<String> artists, Boolean isAudioBook, String albumName) {

        List<Music> musiclist = getAlbumSongByAlbumsId(albumsId);
        ArrayList<DownloadInfo> downloadInfos = new ArrayList<>();


        musiclist.forEach(md -> {
            if (isAudioBook) {
                md.setMusicAlbum(albumName).setMusicArtists(artists);
            }
            DownloadInfo downloadInfo = super.musicToDownloadInfo(md, brType, isAudioBook);
            downloadInfos.add(downloadInfo);
        });
        return downloadInfos;
    }

    @Override
    public List<DownloadInfo> downloadArtistAllSong(String artistId, PlugBrType brType) {
        return downloadArtistAllAlbum(artistId, brType);
    }

    @Override
    public List<DownloadInfo> downloadArtistAllAlbum(String artistId, PlugBrType brType) {
        int page = 1;
        JSONObject parameter = new JSONObject();// 请求参数
        parameter.put("id", artistId);
        parameter.put("limit", "50");
        parameter.put("offset", (page - 1)*50);
        JSONObject jsonObject = neteaseCloudMusicInfo.artistAlbum(parameter);
        ArtistAllAlubuminNeteaseResult artistAllAlubuminNeteaseResult = jsonObject.toJavaObject(ArtistAllAlubuminNeteaseResult.class);
        List<ArtistAllAlubuminNeteaseResult.HotAlbumsDTO> hotAlbums = artistAllAlubuminNeteaseResult.getHotAlbums();
        Boolean more = artistAllAlubuminNeteaseResult.getMore();
        try {
            while (more) {
                page++;
                //继续补充
                parameter.put("id", artistId);
                parameter.put("limit", "50");
                parameter.put("offset", (page - 1)*50);
                JSONObject jsonObjectmore = neteaseCloudMusicInfo.artistAlbum(parameter);
                ArtistAllAlubuminNeteaseResult alummore = jsonObjectmore.toJavaObject(ArtistAllAlubuminNeteaseResult.class);
                hotAlbums.addAll(alummore.getHotAlbums());
            }
        } catch (Exception e) {
            more=false;
        }
        ArrayList<DownloadInfo> downloadInfos = new ArrayList<>();
        for (ArtistAllAlubuminNeteaseResult.HotAlbumsDTO album : hotAlbums) {
            ArrayList<String> artists = new ArrayList<>();
            artists.add(album.getArtist().getName());
            ArrayList<DownloadInfo> downloadEntities = downloadAlbum(album.getId().toString(), brType,artists , false, album.getName());
            downloadInfos.addAll(downloadEntities);
        }
        return downloadInfos;
    }




    public ArrayList<Music>  getPlayList(String playlistId){
        ArrayList<Music> musics = new ArrayList<>();

        int page = 1;
        JSONObject parameter = new JSONObject();// 请求参数
        parameter.put("id", playlistId);
        parameter.put("limit", "50");
        parameter.put("offset", (page - 1)*50);
        boolean more = true;
        JSONObject jsonObject = neteaseCloudMusicInfo.playlistDetail(parameter);
        PlaylistTrackAllResult playlistTrackAllResult = jsonObject.toJavaObject(PlaylistTrackAllResult.class);
        List<PlaylistTrackAllResult.SongsDTO> songs = playlistTrackAllResult.getSongs();
        if (songs==null|| songs.isEmpty()){
            more=false;
            return musics;
        }
        try {
            while (more) {
                page++;
                //继续补充
                parameter.put("id", playlistId);
                parameter.put("limit", "50");
                parameter.put("offset", (page - 1)*50);
                JSONObject jsonObject1 = neteaseCloudMusicInfo.playlistDetail(parameter);
                PlaylistTrackAllResult javaObject = jsonObject1.toJavaObject(PlaylistTrackAllResult.class);
                List<PlaylistTrackAllResult.SongsDTO> songs1 = javaObject.getSongs();
                if (songs1==null|| songs1.isEmpty()){
                    more=false;
                    break;
                }
                songs.addAll(songs1);
            }
        } catch (Exception e) {
            more=false;
        }
        //处理歌曲
        songs.forEach(songsInfoDTO -> {
            PlaylistTrackAllResult.SongsDTO songsDTO = songsInfoDTO;
            PlaylistTrackAllResult.SongsDTO.HDTO h = songsDTO.getH();
            PlaylistTrackAllResult.SongsDTO.MDTO m = songsDTO.getM();
            PlaylistTrackAllResult.SongsDTO.LDTO l = songsDTO.getL();
            PlaylistTrackAllResult.SongsDTO.SqDTO sq = songsDTO.getSq();
            PlaylistTrackAllResult.SongsDTO.SqDTO hr = songsDTO.getHr();
            ArrayList<PlugBrType> plugBrTypes = new ArrayList<>();
            if (h!=null&&h.getBr()!=null){
                plugBrTypes.add(PlugBrType.NETEASE_MP3_320);
            }
            if (m!=null&&m.getBr()!=null){
                plugBrTypes.add(PlugBrType.NETEASE_MP3_192);
            }
            if (l!=null&&l.getBr()!=null){
                plugBrTypes.add(PlugBrType.NETEASE_MP3_128);
            }
            if (sq!=null&&sq.getBr()!=null){
                plugBrTypes.add(PlugBrType.NETEASE_FLAC_2000);
            }
            if (hr!=null&&hr.getBr()!=null){
                plugBrTypes.add(PlugBrType.NETEASE_FLAC_3000);
            }
            Music music = new Music();
            music.setId(songsInfoDTO.getId().toString())
                    .setMusicName(songsInfoDTO.getName())
                    .setMusicDuration(songsInfoDTO.getDt())
                    .setMusicAlbum(songsInfoDTO.getAl().getName())
                    .setMusicArtists(songsInfoDTO.getAr().stream().map(e -> e.getName()).collect(Collectors.toList()))
                    .setMusicImage(songsInfoDTO.getAl().getPicUrl())
                    .setAlbumId(songsInfoDTO.getAl().getId().toString())
                    .setDataInfo(JSONObject.parseObject(JSONObject.toJSONString(songsInfoDTO)))
                    .setArtistsIds(songsInfoDTO.getAr().stream().map(e -> e.getId().toString()).collect(Collectors.toList()));
            musics.add(music);
        });
        return musics;
    }


    /**
     * 获取歌单详情
     */
    public PlaylistTrackAllResult  getPlayListInfo(String playlistId) {
        JSONObject parameter = new JSONObject();// 请求参数
        parameter.put("id", playlistId);
        JSONObject jsonObject1 = neteaseCloudMusicInfo.playlistDetail(parameter);
        PlaylistTrackAllResult javaObject = jsonObject1.toJavaObject(PlaylistTrackAllResult.class);
        return javaObject;
    }




}
