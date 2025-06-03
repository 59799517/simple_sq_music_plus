package com.sqmusicplus.plug.kg.hander;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RuntimeUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.sqmusicplus.base.entity.*;
import com.sqmusicplus.base.service.SqConfigService;
import com.sqmusicplus.plug.base.PlugBrType;
import com.sqmusicplus.plug.base.hander.SearchHanderAbstract;
import com.sqmusicplus.plug.entity.*;
import com.sqmusicplus.plug.kg.config.KGConfig;
import com.sqmusicplus.plug.kg.entity.*;
import com.sqmusicplus.plug.kg.enums.KgSearchType;
import com.sqmusicplus.utils.DownloadUtils;
import com.sqmusicplus.utils.OkHttpUtils;
import com.sqmusicplus.utils.StringUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import static com.sqmusicplus.plug.base.PlugBrType.KG_Flac_4000;
import static com.sqmusicplus.plug.base.PlugBrType.KG_Flac_890;


/**
 * @Classname KGHander
 * @Description 酷狗的处理 （图片如果没有默认大小全替换为480）
 * @Version 1.0.0
 * @Date 2025/2/5 15:35
 * @Created by Administrator
 */
@Component("kgHander")
@Slf4j
public class KGHander extends SearchHanderAbstract {

    @Autowired
    private KGConfig config;

    @Autowired
    private SqConfigService configService;

    @Autowired
    @Qualifier("kwQrthreadPoolTaskExecutor")
    private ThreadPoolExecutor threadPoolExecutor;
    //二维码检查线程
    private Future<Boolean> qrCodeCheckFuture;

    private Future<Boolean> wxQrCodeCheckFuture;
    @Override
    public KGConfig getConfig() {
        return config;
    }

    @Override
    public String getPlugName() {
        return "kg";
    }

    public String getBaseURL() {
        SqConfig sqConfig = configService.selectByKeyAndValue("plug.kg.baseurl");
        return sqConfig.getConfigValue();
    }

    @Override
    public PlugSearchResult<PlugSearchMusicResult> querySongByName(SearchKeyData searchKeyData) {

        HashMap<String, String> para = new HashMap<>();
        para.put("page", searchKeyData.getPageIndex()+"");
        para.put("pagesize", searchKeyData.getPageSize().toString());
        para.put("type", KgSearchType.MUSIC.getValue());
        para.put("keywords", searchKeyData.getSearchkey());

        String cooKie = getCooKie();
        if (StringUtils.isNotEmpty(cooKie)){
            para.put("cookie", cooKie);
        }
        OkHttpUtils builder = OkHttpUtils.builder();
        String sync = builder.url(getBaseURL() + getConfig().getSearchUrl())
                .addParam(para)
                .get().sync();
        SearchMusicResult bean = JSONObject.parseObject(sync, SearchMusicResult.class);
        ArrayList<PlugSearchMusicResult> plugSearchMusicResults = new ArrayList<>();
        Long status = bean.getStatus();
        PlugSearchResult<PlugSearchMusicResult> plugSearchResult = new PlugSearchResult<>();

        if (status != 1){
            plugSearchResult.setSearchIndex(searchKeyData.getPageIndex())
                    .setSearchSize(searchKeyData.getPageSize())
                    .setSearchType(getPlugName())
                    .setSearchTotal(0)
                    .setSearchKeyWork(searchKeyData.getSearchkey())
                    .setRecords(plugSearchMusicResults);
            plugSearchResult.setSearchType(getPlugName());
            return plugSearchResult;
        }
        SearchMusicResult.DataDTO data = bean.getData();
        List<SearchMusicResult.DataDTO.ListsDTO> lists = data.getLists();
        for (SearchMusicResult.DataDTO.ListsDTO listsDTO : lists) {
            plugSearchMusicResults.add(         new PlugSearchMusicResult().setAlbumName(listsDTO.getAlbumName())
                    .setAlbumid(listsDTO.getAlbumID())
                    .setArtistName(listsDTO.getSingers().stream().map(e -> e.getName()).collect(Collectors.joining(",")))
                    .setArtistid(listsDTO.getSingers().stream().map(e -> e.getId().toString()).collect(Collectors.joining(",")))
                    .setId(listsDTO.getFileHash())
                    .setSearchType(getPlugName())
                    .setDuration(listsDTO.getDuration().toString())
                    .setName(listsDTO.getSongName()).setPic(listsDTO.getImage().replaceAll("\\{size}",getConfig().getImageSize())));
        }
        plugSearchResult.setSearchIndex(searchKeyData.getPageIndex())
                .setSearchSize(searchKeyData.getPageSize())
                .setSearchType(getPlugName())
                .setSearchTotal(data.getTotal().intValue())
                .setSearchKeyWork(searchKeyData.getSearchkey())
                .setRecords(plugSearchMusicResults);
        return plugSearchResult;
    }

    @Override
    public PlugSearchResult<PlugSearchArtistResult> queryArtistByName(SearchKeyData searchKeyData) {
        HashMap<String, String> para = new HashMap<>();
        para.put("page", searchKeyData.getPageIndex()+"");
        para.put("pagesize", searchKeyData.getPageSize().toString());
        para.put("type", KgSearchType.ARTIST.getValue());
        para.put("keywords", searchKeyData.getSearchkey());
        String cooKie = getCooKie();
        if (StringUtils.isNotEmpty(cooKie)){
            para.put("cookie", cooKie);
        }
        OkHttpUtils builder = OkHttpUtils.builder();
        String sync = builder.url(getBaseURL() + getConfig().getSearchUrl())
                .addParam(para)
                .get().sync();
        SearchArtistResult bean = JSONObject.parseObject(sync, SearchArtistResult.class);
        Long status = bean.getStatus();
        PlugSearchResult<PlugSearchArtistResult> plugSearchResult = new PlugSearchResult();
        ArrayList<PlugSearchArtistResult> plugSearchMusicResults = new ArrayList<>();
        if (status != 1){
            plugSearchResult.setSearchIndex(searchKeyData.getPageIndex())
                    .setSearchSize(searchKeyData.getPageSize())
                    .setSearchType(getPlugName())
                    .setSearchTotal(0)
                    .setSearchKeyWork(searchKeyData.getSearchkey())
                    .setRecords(plugSearchMusicResults);
            plugSearchResult.setSearchType(getPlugName());
            return plugSearchResult;
        }
        SearchArtistResult.DataDTO data = bean.getData();
        List<SearchArtistResult.DataDTO.ListsDTO> lists = data.getLists();
        plugSearchMusicResults = lists.stream().map(listsDTO -> new PlugSearchArtistResult().setArtistName(listsDTO.getAuthorName())
                .setArtistid(listsDTO.getAuthorId().toString())
                .setSearchType(getPlugName())
                .setPic(listsDTO.getAvatar().replaceAll("\\{size}",getConfig().getImageSize()))
                .setTotal(listsDTO.getAlbumCount().toString()))
                .collect(Collectors.toCollection(ArrayList::new));
        plugSearchResult.setSearchIndex(searchKeyData.getPageIndex())
                .setSearchSize(searchKeyData.getPageSize())
                .setSearchType(getPlugName())
                .setSearchTotal(data.getTotal().intValue())
                .setSearchKeyWork(searchKeyData.getSearchkey())
                .setRecords(plugSearchMusicResults);
        plugSearchResult.setSearchType(getPlugName());
        return plugSearchResult;
    }

    @Override
    public PlugSearchResult<PlugSearchAlbumResult> queryAlbumByName(SearchKeyData searchKeyData) {

        HashMap<String, String> para = new HashMap<>();
        para.put("page", searchKeyData.getPageIndex()+"");
        para.put("pagesize", searchKeyData.getPageSize().toString());
        para.put("type", KgSearchType.ALBUM.getValue());
        para.put("keywords", searchKeyData.getSearchkey());
        String cooKie = getCooKie();
        if (StringUtils.isNotEmpty(cooKie)){
            para.put("cookie", cooKie);
        }
        OkHttpUtils builder = OkHttpUtils.builder();
        String sync = builder.url(getBaseURL() + getConfig().getSearchUrl())
                .addParam(para)
                .get().sync();
        SearchAlbumResult bean = JSONObject.parseObject(sync, SearchAlbumResult.class);
        Long status = bean.getStatus();
        PlugSearchResult<PlugSearchAlbumResult> plugSearchResult = new PlugSearchResult();
        ArrayList<PlugSearchAlbumResult> plugSearchMusicResults = new ArrayList<>();

        if (status != 1){
            plugSearchResult.setSearchIndex(searchKeyData.getPageIndex())
                    .setSearchSize(searchKeyData.getPageSize())
                    .setSearchType(getPlugName())
                    .setSearchTotal(0)
                    .setSearchKeyWork(searchKeyData.getSearchkey())
                    .setRecords(plugSearchMusicResults);
            plugSearchResult.setSearchType(getPlugName());
            return plugSearchResult;
        }
        SearchAlbumResult.DataDTO data = bean.getData();
        List<SearchAlbumResult.DataDTO.ListsDTO> lists = data.getLists();
        plugSearchMusicResults = lists.stream().map(listsDTO -> new PlugSearchAlbumResult().setAlbumName(listsDTO.getAlbumname())
                .setAlbumid(listsDTO.getAlbumid().toString())
                .setArtistName(listsDTO.getSingers().stream().map(e -> e.getName()).collect(Collectors.joining(",")))
                .setArtistid(listsDTO.getSingers().stream().map(e -> e.getId().toString()).collect(Collectors.joining(",")))
                .setSearchType(getPlugName())
                .setPic(listsDTO.getImg().replaceAll("\\{size}",getConfig().getImageSize()))
               .setTotal(listsDTO.getSongcount().toString()))
                .collect(Collectors.toCollection(ArrayList::new));
        plugSearchResult.setSearchIndex(searchKeyData.getPageIndex())
                .setSearchSize(searchKeyData.getPageSize())
                .setSearchType(getPlugName())
                .setSearchTotal(data.getTotal().intValue())
                .setSearchKeyWork(searchKeyData.getSearchkey())
                .setRecords(plugSearchMusicResults);
        plugSearchResult.setSearchType(getPlugName());
        return plugSearchResult;
    }

    @Override
    public Music querySongById(String SongId) {
        OkHttpUtils builder = OkHttpUtils.builder();
        HashMap<String, String> para = new HashMap<>();
        para.put("hash", SongId);
        String cooKie = getCooKie();
        if (StringUtils.isNotEmpty(cooKie)){
            para.put("cookie", cooKie);
        }

        String sync = builder.url(getBaseURL() + getConfig().getSonginfoUrl())
                .addParam(para)
                .get().sync();
        SongInfoResult bean = JSONObject.parseObject(sync, SongInfoResult.class);
        if (bean.getStatus() != 1){
            return null;
        }
        List<SongInfoResult.DataDTO> data = bean.getData();
        for (SongInfoResult.DataDTO dataDTO : data) {
            if (dataDTO.getType().equals("audio")){
                //默认的信息
                String id = dataDTO.getHash();
                String musicArtists = dataDTO.getSingername();
                Long authorId = 0L;
                String musicAlbum = dataDTO.getAlbumname();
                String musicName = dataDTO.getName().replaceAll(musicArtists, "").replaceAll("-", "").trim();
                String albumId = dataDTO.getAlbumId();
                SongInfoResult.DataDTO.InfoDTO info = dataDTO.getInfo();
                Long duration = info.getDuration();
                String musicimage = info.getImage().replaceAll("\\{size}",getConfig().getImageSize());
                if (StringUtils.isEmpty(musicimage)){
                    SongInfoResult.DataDTO.TransParamDTO transParam = dataDTO.getTransParam();
                    musicimage = transParam.getUnionCover().replaceAll("\\{size}",getConfig().getImageSize());
                }
                //虽然歌词接口也有歌手信息但是还是用歌曲补充来获取
                String lyric = queryLyric(id);
                HashMap<String, String> audiopara = new HashMap<>();
                audiopara.put("album_audio_id", dataDTO.getAlbumAudioId().toString());
                audiopara.put("fields","base,audio_info,authors.ip,extra,authors.base");
                String syncSonginfoAdd = builder.url(getBaseURL() + getConfig().getSonginfoAddUrl())
                        .addParam(audiopara)
                        .get().sync();
                SongInfoAddResult addsonginfo = JSONObject.parseObject(syncSonginfoAdd, SongInfoAddResult.class);
                        if (addsonginfo.getStatus() == 1){
                            List<SongInfoAddResult.DataDTO> data1 = addsonginfo.getData();
                            for (SongInfoAddResult.DataDTO dataDTO1 : data1) {
                                try {
                                    musicArtists = dataDTO1.getAuthors().stream().map(authorsDTO -> authorsDTO.getBase().getAuthorName()).collect(Collectors.joining(","));
                                } catch (Exception ignored) {
                                }
                                try {
                                    authorId = dataDTO1.getAuthors().get(0).getBase().getAuthorId();
                                } catch (Exception ignored) {
                                }
                                try {
                                    musicName = dataDTO1.getBase().getSongname();
                                } catch (Exception ignored) {
                                }
                                try {
                                    musicAlbum = dataDTO1.getAlbumInfo().getAlbumName();
                                } catch (Exception e) {
                                }
                                try {
                                    albumId = dataDTO1.getAlbumInfo().getAlbumId().toString();
                                } catch (Exception ignored) {
                                }
                            }



                        }

                return new Music()
                        .setId(id)
                        .setMusicName(musicName)
                        .setArtistsId(authorId.toString())
                        .setMusicArtists(musicArtists)
                        .setAlbumId(albumId)
                        .setMusicAlbum(musicAlbum)
                        .setMusicDuration(duration)
                        .setMusicImage(dataDTO.getInfo().getImage());
            }
        }

        return null;
    }

    @Override
    public Artists queryArtistById(String artistId) {
        OkHttpUtils builder = OkHttpUtils.builder();
        HashMap<String, String> para = new HashMap<>();
        para.put("id", artistId);
        String artist = builder.url(getBaseURL() + getConfig().getSingerInfoUrl())
                .addParam(para)
                .get().sync();

        ArtistInfoResult bean = JSONObject.parseObject(artist, ArtistInfoResult.class);
        if (bean.getStatus() != 1){
            return null;
        }
        ArtistInfoResult.DataDTO data = bean.getData();
        return new Artists()
                .setId(data.getAuthorId())
                .setMusicArtistsName(data.getAuthorName())
                .setMusicArtistsPhoto(data.getSizableAvatar())
                .setMusicArtistsDescribe(data.getIntro())
                .setMusicArtistsAlias(data.getPinyinInitial())
                .setMusicArtistsPhoto(data.getSizableAvatar().replaceAll("\\{size}",getConfig().getImageSize()));

    }

    @Override
    public Album queryAlbumById(String albumId) {
        OkHttpUtils builder = OkHttpUtils.builder();
        HashMap<String, String> para = new HashMap<>();
        para.put("album_id", albumId);
        para.put("fields", " trans_param,special_tag,authors,album_name,publish_date,cover,intro,publish_company,type,album_id,language_id,is_publish,heat,grade,quality,exclusive,grade_count,author_name,sizable_cover,language,category");
        String alumbm = builder.url(getBaseURL() + getConfig().getAlbumInfoUrl())
                .addParam(para)
                .get().sync();
        AlbumInfoResult bean = JSONObject.parseObject(alumbm, AlbumInfoResult.class);
        if (bean.getStatus() != 1){
            return null;
        }
        for (AlbumInfoResult.DataDTO dataDTO : bean.getData()) {
            String albumName = dataDTO.getAlbumName();
            String salbumId = dataDTO.getAlbumId();
            String albumImage = dataDTO.getSizableCover().replaceAll("\\{size}",getConfig().getImageSize());
            String albumTime = dataDTO.getPublishDate();
            String albumArtists = dataDTO.getAuthors().stream().map(authorsDTO -> authorsDTO.getAuthorName()).collect(Collectors.joining(","));
            String authorId = dataDTO.getAuthors().get(0).getAuthorId();
            String intro = dataDTO.getIntro();
            List<Music> musics = getAlbumSongByAlbumsId(albumId);
            return new Album()
                    .setMusics(musics)
                    .setAlbumTime(albumTime)
                    .setAlbumArtists(albumArtists)
                    .setAlbumName(albumName)
                    .setAlbumDescribe(intro)
                    .setAlbumImg(albumImage)
                    .setAlbumId(salbumId)
                    .setAlbumArtistId(authorId);
        }
        return null;
    }

    @Override
    public String queryLyric(String SongId) {
        OkHttpUtils builder = OkHttpUtils.builder();
        HashMap<String, String> para = new HashMap<>();
        para.put("hash", SongId);
        String cooKie = getCooKie();
        if (StringUtils.isNotEmpty(cooKie)){
            para.put("cookie", cooKie);
        }
        String sync = builder.url(getBaseURL() + getConfig().getLyricIdUrl())
                .addParam(para)
                .get().sync();
        LyricInfoResult bean = JSONObject.parseObject(sync, LyricInfoResult.class);
        if (bean.getStatus() != 1){
            return "";
        }
        for (LyricInfoResult.CandidatesDTO candidate : bean.getCandidates()) {
            String id = candidate.getId();
            String accesskey = candidate.getAccesskey();
            //找出歌词
            HashMap<String, String> lyricpara = new HashMap<>();
            lyricpara.put("id", id);
            lyricpara.put("accesskey", accesskey);
            lyricpara.put("decode", "true");
            lyricpara.put("fmt", "lrc");
            if (StringUtils.isNotEmpty(cooKie)){
                lyricpara.put("cookie", cooKie);
            }
            String lyricsync = builder.url(getBaseURL() + getConfig().getLyricUrl())
                    .addParam(lyricpara)
                    .get().sync();
            LyricResult lyricResult = JSONObject.parseObject(lyricsync, LyricResult.class);
            if (lyricResult.getStatus() == 200&& lyricResult.getErrorCode() == 0&& lyricResult.getInfo().equals("OK")){
                return lyricResult.getDecodeContent();
            }
        }
        return "";
    }

    @Override
    public List<Album> getAlbumsByArtist(String artistId, Integer pageIndex, Integer pageSize) {
        OkHttpUtils builder = OkHttpUtils.builder();
        HashMap<String, String> para = new HashMap<>();
        para.put("id", artistId);
        para.put("page", "1");
//        应该能获取到全部的了就不循环分页查询了
        para.put("pagesize", "10000");
        para.put("sort", "new");
        String cooKie = getCooKie();
        if (StringUtils.isNotEmpty(cooKie)){
            para.put("cookie", cooKie);
        }
        String sync = builder.url(getBaseURL() + getConfig().getSingerAlbumUrl())
                .addParam(para)
                .get().sync();
        ArtistAlubmResult bean = JSONObject.parseObject(sync, ArtistAlubmResult.class);
        if (bean.getStatus() != 1){
            return new ArrayList<>();
        }
        ArrayList<Album> albums = new ArrayList<>();

        for (ArtistAlubmResult.DataDTO dataDTO : bean.getData()) {
            String albumName = dataDTO.getAlbumName();
            Long salbumId = dataDTO.getAlbumId();
            String albumImage = dataDTO.getSizableCover().replaceAll("\\{size}",getConfig().getImageSize());
            String albumTime = dataDTO.getPublishDate();
            String albumArtists = dataDTO.getAuthors().stream().map(authorsDTO -> authorsDTO.getAuthorName()).collect(Collectors.joining(","));
            Long authorId = dataDTO.getAuthors().get(0).getAuthorId();
            String intro = dataDTO.getIntro();
            albums.add(new Album().setAlbumArtists(albumArtists)
                    .setAlbumArtistId(authorId.toString())
                    .setAlbumTime(albumTime)
                    .setAlbumDescribe(intro)
                    .setAlbumId(salbumId.toString())
                    .setAlbumName(albumName)
                    .setAlbumImg(albumImage)
            );
        }

        return albums;
    }

    @Override
    public List<Music> getAlbumSongByAlbumsId(String albumsId) {
        ArrayList<Music> musics = new ArrayList<>();

        Integer page =1;
        Integer pageSize = 50;
        AlubmSongResult albumSongByPageAndAlbumId = getAlbumSongByPageAndAlbumId(albumsId, page, pageSize);
        if (albumSongByPageAndAlbumId.getStatus() != 1){
            return musics;
        }
        AlubmSongResult.DataDTO data = albumSongByPageAndAlbumId.getData();
        if (data.getTotal()==0){
            return musics;
        }
        Long total = data.getTotal();
        List<AlubmSongResult.DataDTO.SongsDTO> songs = albumSongByPageAndAlbumId.getData().getSongs();
        musics.addAll(alubmSongResultToMusic(songs));

        //计算还需要请求的页数
        Long totalPage = total % pageSize == 0 ? total / pageSize : total / pageSize + 1;
        for (int i = 2; i <= totalPage; i++) {
            AlubmSongResult albumSongByPageAndAlbumId1 = getAlbumSongByPageAndAlbumId(albumsId, i, pageSize);
            List<AlubmSongResult.DataDTO.SongsDTO> songs1 = albumSongByPageAndAlbumId1.getData().getSongs();
            musics.addAll(alubmSongResultToMusic(songs1));
        }
        return musics;
    }

    @Override
    public HashMap<String, String> getDownloadUrl(String musicId, PlugBrType brType) {

//        brType 优化有3000的flac就要哪个3000的
        if (brType.getBit() == 2000){
            brType = PlugBrType.KG_Flac_3000;
        }
        OkHttpUtils builder = OkHttpUtils.builder();
        HashMap<String, String> para = new HashMap<>();
        para.put("hash", musicId);
        para.put("quality", brType.getValue());

        String cooKie = getCooKie();
        if (StringUtils.isNotEmpty(cooKie)){
            para.put("cookie", cooKie);
        }else{
            return new HashMap<>();
        }

        String sync = builder.url(getBaseURL() + getConfig().getDownloadUrl())
                .addParam(para)
                .get().sync();
        DownloadResult bean = JSONObject.parseObject(sync, DownloadResult.class);
        if (bean.getStatus() != 1){
           //降低标准来一次
            brType =  getMaxPlugBrType(brType);
            if (brType==null){
                return new HashMap<>();
            }
            return  getDownloadUrl(musicId,brType);

        }
        String url="" ;
        String type;
        String bit;
        for (String s : bean.getUrl()) {
            if (StringUtils.isNotEmpty(s)){
                url = s;
                break;
            }
        }
        type = bean.getExtName();
        bit = bean.getBitRate().toString();
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("url", url);
        stringStringHashMap.put("type", type);
        stringStringHashMap.put("bit", bit);
        return stringStringHashMap;

    }

    @Override
    public HashMap<String, String> getDownloadUrl(DownloadEntity downloadEntity) {
        return getDownloadUrl(downloadEntity.getMusicid(),downloadEntity.getBrType());
    }

    public PlugBrType getMaxPlugBrType(PlugBrType brType){
        if (brType==PlugBrType.KG_Flac_2000||brType==PlugBrType.KG_Flac_3000||brType== KG_Flac_890) {
            //flac走flac
            if (brType==PlugBrType.KG_Flac_3000){
                return PlugBrType.KG_Flac_2000;
            }
            if (brType== KG_Flac_890){
                return PlugBrType.KG_Flac_2000;
            }
            return PlugBrType.KG_MP3_320;
        }
        if (brType==PlugBrType.KG_Flac_4000||brType==PlugBrType.KG_Flac_5000){
            //特殊的走特殊的没有最优解
            return PlugBrType.KG_Flac_3000;
        }
        if (brType==PlugBrType.KG_MP3_128||brType==PlugBrType.KG_MP3_320){
            if (brType==PlugBrType.KG_MP3_320){
                return PlugBrType.KG_MP3_128;
            }
            return null;
        }
        return brType;
    }
    @Override
    public DownloadEntity downloadSong(String musicid, PlugBrType brType, String musicname, String artistname, String albumname, Boolean isAudioBook, String addSubsonicPlayListName) {
        Music music = querySongById(musicid);
        DownloadEntity downloadEntity = new DownloadEntity("kgHander",musicid, brType, music.getMusicName(), music.getMusicArtists(), music.getMusicAlbum(), isAudioBook, isAudioBook?addSubsonicPlayListName:null);
        return downloadEntity;
    }

    @Override
    public DownloadEntity downloadSong(Music music, PlugBrType brType, Boolean isAudioBook, String addSubsonicPlayListName) {
        DownloadEntity downloadEntity = new DownloadEntity("kgHander",music.getId(), brType, music.getMusicName(), music.getMusicArtists(), music.getMusicAlbum(), isAudioBook, isAudioBook?addSubsonicPlayListName:null);
        return downloadEntity;
    }

    @Override
    public DownloadEntity downloadSong(Music music, PlugBrType brType, String addSubsonicPlayListName) {
        DownloadEntity downloadEntity = new DownloadEntity("kgHander",music.getId(), brType, music.getMusicName(), music.getMusicArtists(), music.getMusicAlbum(), false, addSubsonicPlayListName);
        return downloadEntity;
    }

    @Override
    public ArrayList<DownloadEntity> downloadAlbum(String albumsId, PlugBrType brType, String addSubsonicPlayListName, String artist, Boolean isAudioBook, String albumName) {
        Album album = queryAlbumById(albumsId);
        ArrayList<DownloadEntity> downloadEntities = new ArrayList<>();
        List<Music> musics = album.getMusics();
        AtomicReference<String> change = new AtomicReference<>(artist);

        SqConfig accompaniment = getConfigService().getOne(new QueryWrapper<SqConfig>().eq("config_key", "music.ignore.accompaniment"));
        SqConfig matchAlbumSinger = getConfigService().getOne(new QueryWrapper<SqConfig>().eq("config_key", "music.strong.match.album.singer"));
        SqConfig albumSingerUnity = getConfigService().getOne(new QueryWrapper<SqConfig>().eq("config_key", "music.album.singer.unity"));

        musics.forEach(md -> {
            if (Boolean.getBoolean(accompaniment.getConfigValue())) {
                if (md.getMusicName().contains("(伴奏)") || md.getMusicName().contains("(试听版)") || md.getMusicName().contains("(片段)")) {
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
                downloadEntities.add(new DownloadEntity("nKwSearchHander",md.getId(), brType, md.getMusicName(), artist, albumName, isAudioBook));
            } else {
                //添加到缓存
                downloadEntities.add(new DownloadEntity("nKwSearchHander",md.getId(), brType, md.getMusicName(), change.get(), md.getMusicAlbum()));
            }

        });
        return downloadEntities;
    }

    @Override
    public List<DownloadEntity> downloadArtistAllSong(String artistId, PlugBrType brType, String addSubsonicPlayListName) {
        return downloadArtistAllAlbum(artistId,brType,addSubsonicPlayListName);
    }

    @Override
    public List<DownloadEntity> downloadArtistAllAlbum(String artistId, PlugBrType brType, String addSubsonicPlayListName) {
        ArrayList<DownloadEntity> downloadEntities = new ArrayList<>();
        for (Album album : getAlbumsByArtist(artistId,0,0)) {
            downloadEntities.addAll(downloadAlbum(album.getAlbumId(),brType,addSubsonicPlayListName,album.getAlbumArtists(),false,album.getAlbumName()));
        }
        return downloadEntities;
    }

    /**
     * AlubmSongResult 转 music
     */
    public ArrayList<Music> alubmSongResultToMusic(List<AlubmSongResult.DataDTO.SongsDTO> songs) {
        ArrayList<Music> musics = new ArrayList<>();

        for (AlubmSongResult.DataDTO.SongsDTO song : songs) {
            AlubmSongResult.DataDTO.SongsDTO.AudioInfoDTO audioInfo = song.getAudioInfo();
            AlubmSongResult.DataDTO.SongsDTO.BaseDTO base = song.getBase();
            AlubmSongResult.DataDTO.SongsDTO.AlbumInfoDTO albumInfo = song.getAlbumInfo();
            AlubmSongResult.DataDTO.SongsDTO.TransParamDTO transParam = song.getTransParam();
            List<AlubmSongResult.DataDTO.SongsDTO.AuthorsDTO> authors = song.getAuthors();
            if (audioInfo==null||base==null){
                continue;
            }
            Music music = new Music().setId(audioInfo.getHash())
                    .setMusicName(base.getAudioName())
                    .setMusicDuration(audioInfo.getDuration())
                    .setMusicAlbum(albumInfo.getAlbumName())
                    .setMusicArtists(authors.stream().map(AlubmSongResult.DataDTO.SongsDTO.AuthorsDTO::getAuthorName).collect(Collectors.joining(",")))
                    .setMusicImage(transParam.getUnionCover().replaceAll("\\{size}", "400"))
                    .setAlbumId(base.getAlbumId().toString())
                    .setArtistsId(authors.get(0).getAuthorId().toString());
            musics.add(music);
        }
        return musics;
    }

    /**
     * 发起获取专辑信息请求
     * @param albumId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public AlubmSongResult getAlbumSongByPageAndAlbumId(String albumId, Integer pageIndex, Integer pageSize) {
        OkHttpUtils songbuilder = OkHttpUtils.builder();
        HashMap<String, String> songpara = new HashMap<>();
        songpara.put("id", albumId);
        songpara.put("page",pageIndex.toString());
        songpara.put("page_size",pageSize.toString());

        String sync = songbuilder.url(getBaseURL() + getConfig().getAlbumSongUrl())
                .addParam(songpara)
                .get().sync();
        AlubmSongResult bean1 = JSONObject.parseObject(sync, AlubmSongResult.class);
        return  bean1;
    }

    /**
     * 拼接的cookie字符串
     * @return
     */
    public String getCooKie(){
        SqConfig logininfo = configService.selectByKeyAndValue("plug.kg.logininfo");
        if (logininfo!=null&&StringUtils.isNotEmpty(logininfo.getConfigValue())){
            UserInfoResult userInfoResult = JSONObject.parseObject(logininfo.getConfigValue(), UserInfoResult.class);
            return  "token="+userInfoResult.getToken()+";userid="+userInfoResult.getUserid()+";KUGOU_API_PLATFORM=lite"   ;
        }
        return "";
    }

    /**
     * 签到
     * @return
     */
    public boolean signIn(){
        SqConfig sctotal = configService.getOne(Wrappers.<SqConfig>lambdaQuery().eq(SqConfig::getConfigKey, "plug.kg.signIn.total"));
        SqConfig scdone =  configService.getOne(Wrappers.<SqConfig>lambdaQuery().eq(SqConfig::getConfigKey, "plug.kg.signIn.done"));
        SqConfig scremain =  configService.getOne(Wrappers.<SqConfig>lambdaQuery().eq(SqConfig::getConfigKey, "plug.kg.signIn.remain"));
        SqConfig scvipHour =  configService.getOne(Wrappers.<SqConfig>lambdaQuery().eq(SqConfig::getConfigKey, "plug.kg.signIn.vipHour"));
        Long total = sctotal!=null?Long.parseLong(sctotal.getConfigValue()):8;
        Long done = scdone!=null?Long.parseLong(scdone.getConfigValue()):0;
        Long remain = scremain!=null?Long.parseLong(scremain.getConfigValue()):8;
        Long vipHour = scvipHour!=null?Long.parseLong(scvipHour.getConfigValue()):1;
        String token = getCooKie();
        if (StringUtils.isNotEmpty(token)){
            OkHttpUtils builder = OkHttpUtils.builder();
            String sync = builder.url(getBaseURL() + getConfig().getSignUrl())
                    .addParam("cookie", token)
                    .get().sync();
            if (StringUtils.isNotEmpty(sync)){
                SignResult signResult = JSONObject.parseObject(sync, SignResult.class);
                if (signResult.getStatus() == 1){
                    SignResult.DataDTO data = signResult.getData();
                    vipHour = data.getRemainVipHour();
                    //全部的可签到次数（截止到2025年2月12是每次持续3小时）
                     total = data.getTotal();
                    //已签到次数
                     done = data.getDone();
                    //剩余签到次数
                     remain = data.getRemain();
//                    每次持续时长没啥用
                     Long awardVipHour = data.getAwardVipHour();
                     //保存一下签到次数
                     configService.remove(Wrappers.<SqConfig>lambdaQuery().eq(SqConfig::getConfigKey, "plug.kg.signIn.total"));
                     configService.remove(Wrappers.<SqConfig>lambdaQuery().eq(SqConfig::getConfigKey, "plug.kg.signIn.done"));
                     configService.remove(Wrappers.<SqConfig>lambdaQuery().eq(SqConfig::getConfigKey, "plug.kg.signIn.remain"));
                     configService.remove(Wrappers.<SqConfig>lambdaQuery().eq(SqConfig::getConfigKey, "plug.kg.signIn.vipHour"));
                     //vipHour
                    SqConfig sqConfig = new SqConfig();
                    sqConfig.setConfigName("酷狗签到剩余VIP时间（小时）");
                    sqConfig.setConfigKey("plug.kg.signIn.vipHour");
                    sqConfig.setConfigValue(vipHour.toString());
                    sqConfig.setConfigShow("Y");
                    sqConfig.setType("number");
                    //remain
                    SqConfig sqConfig1 = new SqConfig();
                    sqConfig1.setConfigName("酷狗签到剩余次数");
                    sqConfig1.setConfigKey("plug.kg.signIn.remain");
                    sqConfig1.setConfigShow("Y");
                    sqConfig1.setType("number");
                    sqConfig1.setConfigValue(remain.toString());
                    //done
                    SqConfig sqConfig2 = new SqConfig();
                    sqConfig2.setConfigName("酷狗签到已签到次数");
                    sqConfig2.setConfigKey("plug.kg.signIn.done");
                    sqConfig2.setConfigShow("Y");
                    sqConfig2.setType("number");
                    sqConfig2.setConfigValue(done.toString());
                    //total
                    SqConfig sqConfig3 = new SqConfig();
                    sqConfig3.setConfigName("酷狗签到全部签到次数");
                    sqConfig3.setConfigKey("plug.kg.signIn.total");
                    sqConfig3.setConfigShow("Y");
                    sqConfig3.setType("number");
                    sqConfig3.setConfigValue(total.toString());
                    configService.save(sqConfig);
                    configService.save(sqConfig1);
                    configService.save(sqConfig2);
                    configService.save(sqConfig3);
                }
            }

        }
        return false;
    }


    /**
     * 刷新token
     * @return
     */
    public boolean refreshToken(){
       return isLogin();
    }

    /**
     * 判断登录并且刷新token
     * @return
     */
    public boolean isLogin(){
        try {
            SqConfig configKey = configService.getOne(Wrappers.<SqConfig>lambdaQuery().eq(SqConfig::getConfigKey, "plug.kg.open"));
            if (configKey != null && Boolean.parseBoolean(configKey.getConfigValue())) {
                SqConfig logininfo = configService.getOne(Wrappers.<SqConfig>lambdaQuery().eq(SqConfig::getConfigKey, "plug.kg.logininfo"));
                if (StringUtils.isNotEmpty(logininfo.getConfigValue())){

                    UserInfoResult userInfoResult = JSONObject.parseObject(logininfo.getConfigValue(), UserInfoResult.class);
                    //使用URL校验

                    OkHttpUtils builder = OkHttpUtils.builder();
                    HashMap<String, String> para = new HashMap<>();
                    para.put("token", userInfoResult.getToken());
                    para.put("userid",userInfoResult.getUserid().toString());
                    String sync = builder.url(getBaseURL() + getConfig().getRefreshTokenUrl())
                            .addParam(para)
                            .get().sync();
                    JSONObject body = JSONObject.parseObject(sync);
                    int status = body.getInteger("status");
                    if (status == 1){
                        String data = body.getString("data");
                        configService.update(Wrappers.<SqConfig>lambdaUpdate().eq(SqConfig::getConfigKey, "plug.kg.logininfo").set(SqConfig::getConfigValue, data));
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            log.error("获取KG登录信息异常请检查相关API配置");
        }
        configService.update(Wrappers.<SqConfig>lambdaUpdate().eq(SqConfig::getConfigKey, "plug.kg.open").set(SqConfig::getConfigValue, "false"));
        return false;
    }

    /**
     * 获取二维码图片
     * @return
     */
    public String getQrImage(){
        JSONObject body = DownloadUtils.getToJsonObject(getBaseURL() + getConfig().getQrCodeKeyUrl());
        int status = body.getInteger("status");
        if (status != 1){
            return "";
        }
        JSONObject mapper = body.getJSONObject("data");
        String key = mapper.getString("qrcode");
        String img = mapper.getString("qrcode_img");
        configService.remove(Wrappers.<SqConfig>lambdaUpdate().eq(SqConfig::getConfigKey, "plug.kg.qrcode"));
        SqConfig sqConfig = new SqConfig();
        sqConfig.setConfigKey("plug.kg.qrcode");
        sqConfig.setConfigValue(key);
        sqConfig.setConfigName("酷狗二维码key");
        sqConfig.setType("input");
        sqConfig.setConfigShow("N");
        configService.save(sqConfig);
        //异步监听
        syncCheckQrCodeStatus();
        return img;
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
          Callable<Boolean> task = this::checkQrCodeStatus;
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
                  Boolean result =false;
                  try {
                      result = qrCodeCheckFuture.get();
                      if (result != null && result) {
                          log.info("二维码检查任务成功完成。");
                          break; // 任务成功完成，终止循环
                      }else{
                          // 任务失败，重新提交任务
                          qrCodeCheckFuture = threadPoolExecutor.submit(task);
                      }
                  } catch (InterruptedException | ExecutionException e) {
                      log.error("获取二维码检查任务结果时发生错误", e);
                      Thread.currentThread().interrupt(); // 恢复中断状态
                      break; // 发生错误，终止循环
                  }
              }
          }

          if (!qrCodeCheckFuture.isDone()) {
              qrCodeCheckFuture.cancel(true);
              log.warn("二维码检查任务在5分钟内未完成并已被取消。");
          }

        }).start();

    }

    /**
     * 单次获取二维码状态
     * @return
     */
    public boolean checkQrCodeStatus(){
        SqConfig configKey = configService.getOne(Wrappers.<SqConfig>lambdaQuery().eq(SqConfig::getConfigKey, "plug.kg.qrcode"));
        if (configKey != null && StringUtils.isNotEmpty(configKey.getConfigValue())) {
            OkHttpUtils builder = OkHttpUtils.builder();
            HashMap<String, String> para = new HashMap<>();
            para.put("key", configKey.getConfigValue());
            String sync = builder.url(getBaseURL() + getConfig().getQrCodeCheckUrl())
                    .addParam(para)
                    .get().sync();
            JSONObject body = JSONObject.parseObject(sync);
            if (body.getInteger("status") == 1) {
                JSONObject mapper = body.getJSONObject("data");
                int qrstatus = mapper.getInteger("status");
                if (qrstatus==4){
                    String token = mapper.getString("token");
                    String userid = mapper.getString("userid");
                    //使用URL校验
                    HashMap<String, String> params = new HashMap<>();
                    params.put("token", token);
                    params.put("userid", userid);
                    JSONObject userInfoBody = DownloadUtils.getToJsonObject(getBaseURL() + getConfig().getRefreshTokenUrl(), params);

                    int status = userInfoBody.getInteger("status");
                    if (status == 1){
                        String data = userInfoBody.getString("data");
                        configService.update(Wrappers.<SqConfig>lambdaUpdate().eq(SqConfig::getConfigKey, "plug.kg.logininfo").set(SqConfig::getConfigValue, data));
                        return true;
                    }
                }
            }
        }
        configService.update(Wrappers.<SqConfig>lambdaUpdate().eq(SqConfig::getConfigKey, "plug.kg.logininfo").set(SqConfig::getConfigValue, ""));
        return false;
    }

    public String getWxQrImage(){
        JSONObject body = DownloadUtils.getToJsonObject(getBaseURL() + getConfig().getWxQrCodeUrl());
        int status = body.getInteger("errcode");
        if (status != 0){
            return "";
        }
        String key = body.getString("uuid");
        String img = body.getJSONObject("qrcode").getString("qrcodebase64");


        configService.remove(Wrappers.<SqConfig>lambdaUpdate().eq(SqConfig::getConfigKey, "plug.kg.wx.qrcode"));
        SqConfig sqConfig = new SqConfig();
        sqConfig.setConfigKey("plug.kg.wx.qrcode");
        sqConfig.setConfigValue(key);
        sqConfig.setConfigName("酷狗微信二维码key");
        sqConfig.setType("input");
        sqConfig.setConfigShow("N");
        configService.save(sqConfig);
        //异步监听
        syncCheckWxQrCodeStatus();
        return "data:image/jpge;base64,"+img;
    }

    /**
     * 单次获取微信二维码状态
     * @return
     */
    public boolean checkWxQrCodeStatus(){
        SqConfig configKey = configService.getOne(Wrappers.<SqConfig>lambdaQuery().eq(SqConfig::getConfigKey, "plug.kg.wx.qrcode"));
        if (configKey != null && StringUtils.isNotEmpty(configKey.getConfigValue())) {
            OkHttpUtils builder = OkHttpUtils.builder();
            HashMap<String, String> para = new HashMap<>();
            para.put("uuid", configKey.getConfigValue());
            para.put("timestamp",  DateUtil.currentSeconds()+"");
            String sync = builder.url(getBaseURL() + getConfig().getWxQrCodeCheckUrl())
                    .addParam(para)
                    .get().sync();
            JSONObject body = JSONObject.parseObject(sync);
            if (StringUtils.isNotEmpty(body.getString("wx_code"))) {
                String wx_code  = body.getString("wx_code");
                    //使用URL校验
                HashMap<String, String> params = new HashMap<>();
                params.put("code", wx_code);
                JSONObject userInfoBody = DownloadUtils.getToJsonObject(getBaseURL() + getConfig().getWxQropenplatUrl(), params);
                int status = userInfoBody.getInteger("status");
                    if (status == 1){
                        String data = userInfoBody.getString("data");
                        configService.update(Wrappers.<SqConfig>lambdaUpdate().eq(SqConfig::getConfigKey, "plug.kg.logininfo").set(SqConfig::getConfigValue, data));
                        return true;
                    }
            }
        }
        configService.update(Wrappers.<SqConfig>lambdaUpdate().eq(SqConfig::getConfigKey, "plug.kg.logininfo").set(SqConfig::getConfigValue, ""));
        return false;
    }

    /**
     * 监听微信二维码的扫码状态
     */
    public void syncCheckWxQrCodeStatus() {
        new Thread(() -> {
            // 关闭以前的全部任务
            if (wxQrCodeCheckFuture != null && !wxQrCodeCheckFuture.isDone()) {
                wxQrCodeCheckFuture.cancel(true);
            }

            long startTime = System.currentTimeMillis();
            long timeout = 5 * 60 * 1000; // 5 minutes in milliseconds
            // 异步监控二维码超5分钟自动放弃
            Callable<Boolean> task = this::checkWxQrCodeStatus;
            wxQrCodeCheckFuture = threadPoolExecutor.submit(task);
            while (System.currentTimeMillis() - startTime < timeout) {
                try {
                    // 每次等待1秒
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    log.error("线程在等待二维码检查任务时被中断", e);
                    Thread.currentThread().interrupt(); // 恢复中断状态
                    break;
                }

                if (wxQrCodeCheckFuture.isDone()) {
                    Boolean result =false;
                    try {
                        result = wxQrCodeCheckFuture.get();
                        if (result != null && result) {
                            log.info("二维码检查任务成功完成。");
                            break; // 任务成功完成，终止循环
                        }else{
                            // 任务失败，重新提交任务
                            wxQrCodeCheckFuture = threadPoolExecutor.submit(task);
                        }
                    } catch (InterruptedException | ExecutionException e) {
                        log.error("获取二维码检查任务结果时发生错误", e);
                        Thread.currentThread().interrupt(); // 恢复中断状态
                        break; // 发生错误，终止循环
                    }
                }
            }

            if (!wxQrCodeCheckFuture.isDone()) {
                wxQrCodeCheckFuture.cancel(true);
                log.warn("二维码检查任务在5分钟内未完成并已被取消。");
            }

        }).start();

    }

}
