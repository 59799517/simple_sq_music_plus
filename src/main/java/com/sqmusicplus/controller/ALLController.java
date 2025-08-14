package com.sqmusicplus.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sqmusicplus.base.entity.*;
import com.sqmusicplus.base.entity.vo.*;
import com.sqmusicplus.base.service.DownloadInfoService;
import com.sqmusicplus.config.AjaxResult;
import com.sqmusicplus.config.IgnoreDownloadException;
import com.sqmusicplus.parser.TextMusicPlayListParser;
import com.sqmusicplus.parser.UrlMusicPlayListParser;
import com.sqmusicplus.plug.base.PlugBrType;
import com.sqmusicplus.plug.base.hander.SearchHanderAbstract;
import com.sqmusicplus.plug.entity.*;
import com.sqmusicplus.plug.freemp3.hander.FreeMp3Hander;
import com.sqmusicplus.plug.kw.hander.NKwSearchHander;
import com.sqmusicplus.plug.mg.hander.MgHander;
import com.sqmusicplus.plug.netease.hander.NeteaseHander;
import com.sqmusicplus.plug.qq.hander.QQHander;
import com.sqmusicplus.plug.utils.TypeUtils;
import com.sqmusicplus.base.service.SqConfigService;
import com.sqmusicplus.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created with IntelliJ IDEA.
 * User: SQ
 * Date: 2022/7/24
 * Time: 13:40
 * Description: 接口总出口
 */
@Slf4j
@RestController
@RequestMapping()
public class ALLController {
    @Autowired
    @Qualifier("threadPoolTaskExecutor")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Autowired
    private TextMusicPlayListParser textMusicPlayListParser;
    @Autowired
    private UrlMusicPlayListParser urlMusicPlayListParser;
    @Autowired
    private SqConfigService configService;

    @Autowired
    private DownloadInfoService downloadInfoService;
    @Autowired
    List<SearchHanderAbstract> searchHanderAbstractList;
    @Autowired
    private FreeMp3Hander freeMp3Hander;






    /**
     * 搜索单曲
     * @param keyword 关键字
     * @param pageSize 每页长度 最大50
     * @param pageIndex 页码 从1开始
     * @return
     */
    @SaCheckLogin
    @GetMapping("/searchMusic/{searchType}/{keyword}/{pageSize}/{pageIndex}")
    public AjaxResult searchMusic(@PathVariable("searchType") String searchType,@PathVariable("keyword") String keyword,@PathVariable("pageSize") Integer pageSize,@PathVariable("pageIndex") Integer pageIndex ){

        SearchHanderAbstract searchHanderAbstract = null;
        for (SearchHanderAbstract item : searchHanderAbstractList) {
            if(item.getPlugName().equals(searchType)){
                searchHanderAbstract = item;
            }
        }
        if (searchHanderAbstract==null){
            return AjaxResult.error("未知的搜索类型");
        }
        SearchKeyData searchKeyData = new SearchKeyData().setPageIndex(pageIndex).setPageSize(pageSize).setSearchkey(keyword).setSearchType(searchType);
        PlugSearchResult<PlugSearchMusicResult> plugSearchMusicResultPlugSearchResult = searchHanderAbstract.querySongByName(searchKeyData);
        return AjaxResult.success(plugSearchMusicResultPlugSearchResult);

    }
    @SaCheckLogin
    @GetMapping("/musicUrl/{searchType}/{id}")
    public AjaxResult getPlayMusicUrl(@PathVariable("searchType") String searchType,@PathVariable("id")String id){
        SearchHanderAbstract searchHanderAbstract = null;
        for (SearchHanderAbstract item : searchHanderAbstractList) {
            if(item.getPlugName().equals(searchType)){
                searchHanderAbstract = item;
            }
        }
        if (searchHanderAbstract==null){
            return AjaxResult.error("未知的搜索类型");
        }

        PlugBrType plugType;
        if (StringUtils.isEmpty(searchType)){
            plugType = TypeUtils.getPlugType(searchType);
        }else{
            plugType = TypeUtils.getPlugType(searchType);
        }
        PlugBrType finalPlugType = plugType;

        HashMap<String, String> downloadUrl = searchHanderAbstract.getDownloadUrl(id, finalPlugType);
        return AjaxResult.success(downloadUrl);

    }
    @SaCheckLogin
    @GetMapping("/SongInfoById/{searchType}/{id}")
    public AjaxResult SongInfoById(@PathVariable("searchType") String searchType,@PathVariable("id")String id){
        SearchHanderAbstract searchHanderAbstract = null;
        for (SearchHanderAbstract item : searchHanderAbstractList) {
            if(item.getPlugName().equals(searchType)){
                searchHanderAbstract = item;
            }
        }
        if (searchHanderAbstract==null){
            return AjaxResult.error("未知的搜索类型");
        }
        Music music = null;
        try {
            music = searchHanderAbstract.querySongById(id);
        } catch (IgnoreDownloadException e) {
            int i = 0;
            while (music==null){
                log.info("歌曲(酷我id){}---->获取歌曲详情歌词信息失败正在重试", id);
                try {
                    music = searchHanderAbstract.querySongById(id);
                } catch (Exception ex) {
                }
                i++;
                if (i>10){
                    break;
                }
            }
        }


        return AjaxResult.success(music);

    }



    /**
     *
     * @param downloadSong
     * @return
     */
    @SaCheckLogin
    @PostMapping("/musicDownload")
    public AjaxResult musicDownload( @RequestBody DownloadSongEntity downloadSong) {
        SearchHanderAbstract searchHanderAbstract = null;
        for (SearchHanderAbstract item : searchHanderAbstractList) {
            if(item.getPlugName().equals(downloadSong.getPlugType())){
                searchHanderAbstract = item;
            }
        }
        if (searchHanderAbstract==null){
            return AjaxResult.error("未知的搜索类型");
        }
        String searchType = downloadSong.getPlugType();
        PlugBrType plugType;
        if (StringUtils.isEmpty(downloadSong.getPlugTypeValue())){
            plugType = TypeUtils.getPlugType(downloadSong.getPlugType(), downloadSong.getBr());
        }else{
            plugType = TypeUtils.getPlugType(downloadSong.getPlugType(), downloadSong.getPlugTypeValue());
        }
        PlugBrType finalPlugType = plugType;
        Music music =null;
        try {
            music = searchHanderAbstract.querySongById(downloadSong.getId());
        } catch (IgnoreDownloadException e) {
            int i = 0;
            while (music==null){
                log.info("歌曲(酷我id){}---->获取歌曲详情歌词信息失败正在重试", downloadSong.getId());
                try {
                    music = searchHanderAbstract.querySongById(downloadSong.getId());
                } catch (Exception ex) {
                }
                i++;
                if (i>10){
                    break;
                }
            }
        }




        ArrayList<PlugBrType> bits = music.getBits();
        //找出bit最大的
        if (bits!=null&&bits.size()>0){
            finalPlugType = bits.stream().max(Comparator.comparing(PlugBrType::getBit)).get();
        }
        Music finalMusic = music;
            DownloadInfo downloadInfo = new DownloadInfo().setDownloadMusicId(finalMusic.getId())
                    .setDownloadBrType(finalPlugType.getValue())
                    .setDownloadMusicname(finalMusic.getMusicName())
                    .setDownloadArtistname(finalMusic.getMusicArtists())
                    .setDownloadAlbumname(finalMusic.getMusicAlbum())
                    .setAudioBook("false")
                    .setAddSubsonicPlayListName(downloadSong.getSubsonicPlayListName())
                    .setSpringName(finalPlugType.getSpringName())
                    .setDownloadType(searchType)
                    .setDownloadTime(new Date());
            Boolean add = downloadInfoService.add(downloadInfo);
        return AjaxResult.success(add);




    }

    /**
     * 搜索歌手
     * @param keyword 关键字
     * @param pageSize 每页长度 最大50
     * @param pageIndex 页码 从1开始
     * @returnparserUrlAndDownload
     */
    @SaCheckLogin
    @GetMapping("/searchArtist/{searchType}/{keyword}/{pageSize}/{pageIndex}")
    public AjaxResult searchArtist(@PathVariable("searchType") String searchType,@PathVariable("keyword") String keyword,@PathVariable("pageSize") Integer pageSize,@PathVariable("pageIndex") Integer pageIndex ){
        SearchHanderAbstract searchHanderAbstract = null;
        for (SearchHanderAbstract item : searchHanderAbstractList) {
            if(item.getPlugName().equals(searchType)){
                searchHanderAbstract = item;
            }
        }
        if (searchHanderAbstract==null){
            return AjaxResult.error("未知的搜索类型");
        }

        SearchKeyData searchKeyData = new SearchKeyData().setSearchkey(keyword).setPageSize(pageSize).setPageIndex(pageIndex).setSearchType(searchType);
        PlugSearchResult<PlugSearchArtistResult> plugSearchArtistResultPlugSearchResult = searchHanderAbstract.queryArtistByName(searchKeyData);
        return AjaxResult.success(plugSearchArtistResultPlugSearchResult);
    }
    /**
     * 下载歌手全部专辑歌曲到服务器
     * @param downlaodAlubm 下载信息
     * @return
     */
    @SaCheckLogin
    @PostMapping("/ArtistDownload")
    public AjaxResult ArtistDownload(@RequestBody DownlaodArtis downlaodAlubm){
        SearchHanderAbstract searchHanderAbstract = null;
        for (SearchHanderAbstract item : searchHanderAbstractList) {
            if(item.getPlugName().equals(downlaodAlubm.getPlugType())){
                searchHanderAbstract = item;
            }
        }
        if (searchHanderAbstract==null){
            return AjaxResult.error("未知的搜索类型");
        }
        SearchHanderAbstract finalSearchHanderAbstract = searchHanderAbstract;
        threadPoolTaskExecutor.execute(()->{
            PlugBrType plugType;
            if (StringUtils.isEmpty(downlaodAlubm.getPlugTypeValue())){
                plugType = TypeUtils.getPlugType(downlaodAlubm.getPlugType(), downlaodAlubm.getBr());
            }else{
                plugType = TypeUtils.getPlugType(downlaodAlubm.getPlugType(), downlaodAlubm.getPlugTypeValue());
            }
            PlugBrType finalPlugType = plugType;
            List<DownloadEntity> downloadEntities =null;
            downloadEntities =  finalSearchHanderAbstract.downloadArtistAllAlbum(downlaodAlubm.getId(), finalPlugType, null);
            List<DownloadInfo> downloadInfos = MusicUtils.downloadEntitytoDownloadInfoTo(downloadEntities);
            downloadInfoService.add(downloadInfos);
        });

        return AjaxResult.success(true);
    }
    /**
     * 搜索专辑
     * @param keyword 关键字
     * @param pageSize 每页长度 最大50
     * @param pageIndex 页码 从1开始
     * @return
     */
    @SaCheckLogin
    @GetMapping("/searchAlbum/{searchType}/{keyword}/{pageSize}/{pageIndex}")
    public AjaxResult searchAlbum(@PathVariable("searchType") String searchType,@PathVariable("keyword") String keyword,@PathVariable("pageSize") Integer pageSize,@PathVariable("pageIndex") Integer pageIndex ){
        SearchHanderAbstract searchHanderAbstract = null;
        for (SearchHanderAbstract item : searchHanderAbstractList) {
            if(item.getPlugName().equals(searchType)){
                searchHanderAbstract = item;
            }
        }
        if (searchHanderAbstract==null){
            return AjaxResult.error("未知的搜索类型");
        }
        SearchKeyData searchKeyData = new SearchKeyData().setSearchkey(keyword).setPageSize(pageSize).setPageIndex(pageIndex - 1).setSearchType(searchType);
        searchKeyData = new SearchKeyData().setSearchkey(keyword).setPageSize(pageSize).setPageIndex(pageIndex).setSearchType(searchType);
        PlugSearchResult<PlugSearchAlbumResult> plugSearchAlbumResultPlugSearchResult = searchHanderAbstract.queryAlbumByName(searchKeyData);
        return AjaxResult.success(plugSearchAlbumResultPlugSearchResult);

    }
    /**
     * 下载歌手全部歌曲到服务器
     * @param downlaodAlubm 下载信息
     * @return
     */
    @SaCheckLogin
    @PostMapping("/AlbumDownload")
    public AjaxResult AlbumDownload(@RequestBody DownlaodAlubm downlaodAlubm){
        SearchHanderAbstract searchHanderAbstract = null;
        for (SearchHanderAbstract item : searchHanderAbstractList) {
            if(item.getPlugName().equals(downlaodAlubm.getPlugType())){
                searchHanderAbstract = item;
            }
        }
        if (searchHanderAbstract==null){
            return AjaxResult.error("未知的搜索类型");
        }

        PlugBrType plugType;
        if (StringUtils.isEmpty(downlaodAlubm.getPlugTypeValue())){
            plugType = TypeUtils.getPlugType(downlaodAlubm.getPlugType(), downlaodAlubm.getBr());
        }else{
            plugType = TypeUtils.getPlugType(downlaodAlubm.getPlugType(), downlaodAlubm.getPlugTypeValue());
        }
        PlugBrType finalPlugType = plugType;
        ArrayList<DownloadEntity> downloadEntities = null;
        downloadEntities =  searchHanderAbstract.downloadAlbum(downlaodAlubm.getId(), finalPlugType, downlaodAlubm.getSubsonicPlayListName(), "", false, "");
        List<DownloadInfo> downloadInfos = MusicUtils.downloadEntitytoDownloadInfoTo(downloadEntities);
        downloadInfoService.add(downloadInfos);
        return AjaxResult.success(true);
    }


    /**
     * 解析单单
     * @param text
     * @return
     */
    @SaCheckLogin
    @GetMapping("/parserText")
    public List<ParserEntity> parserText(String text) throws IOException {
        List<ParserEntity> parser = textMusicPlayListParser.parser(text);
        return parser;
    }

    /**
     * 解析单单
     *
     * @param data
     * @return
     */
    @SaCheckLogin
    @PostMapping("/parserUrlAndDownload")
    public AjaxResult parserUrl( @RequestBody DownlaodParserUrl data) throws IOException {
        threadPoolTaskExecutor.execute(() -> {
            try {
                urlMusicPlayListParser.parser(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        return AjaxResult.success(true);
    }

    /**
     * 解析单单
     *
     * @param data { text：“内容”，plugType：“插件类型”，br：“品质”，subsonicPlayListName：“歌单名称”}
     * @return
     * 以后在整理逻辑将就用
     */
    @SaCheckLogin
    @PostMapping("/downloadParser")
    public AjaxResult downloadParser(@RequestBody DownlaodParserText data) throws IOException {

        SearchHanderAbstract searchHanderAbstract = null;
        for (SearchHanderAbstract item : searchHanderAbstractList) {
            if (StringUtils.isEmpty(data.getPlugType())) {
                data.setPlugType("kw");
            }
            if(item.getPlugName().equals(data.getPlugType())){
                searchHanderAbstract = item;
            }
        }
        if (searchHanderAbstract==null){
            return AjaxResult.error("未知的搜索类型");
        }

        String text = data.getText();

        String subsonicPlayListName = data.getSubsonicPlayListName();
        List<ParserEntity> parser = textMusicPlayListParser.parser(text);
        SearchHanderAbstract finalSearchHanderAbstract = searchHanderAbstract;
        threadPoolTaskExecutor.execute(()->{
            for (ParserEntity parserEntity : parser) {
                try {
                PlugSearchResult<PlugSearchMusicResult> plugSearchMusicResultPlugSearchResult = null;
                    plugSearchMusicResultPlugSearchResult = finalSearchHanderAbstract.querySongByName(new SearchKeyData().setSearchkey(parserEntity.getSongName() + " " + parserEntity.getArtistsName()).setPageIndex(0).setPageSize(5));
                    String id = "";
                    Music music =null;
                    List<PlugSearchMusicResult> records = plugSearchMusicResultPlugSearchResult.getRecords();
                    for (PlugSearchMusicResult record : records) {
                        if (parserEntity.getArtistsName().trim().equals(record.getArtistName().trim())){
                             id = record.getId();
                            try {
                                music = finalSearchHanderAbstract.querySongById(id);
                            } catch (IgnoreDownloadException e) {
                            }
                            int i = 0;
                            while (music==null){
                                log.info("歌曲(酷我id){}---->获取歌曲详情歌词信息失败正在重试", id);
                                try {
                                    music = finalSearchHanderAbstract.querySongById(id);
                                } catch (Exception e) {
                                }
                                i++;
                                if (i>10){
                                    break;
                                }
                            }
                             break;
                        }
                    }

                    if (music==null){
                        List<SearchHanderAbstract> searchHanderAbstracts = sortHander(searchHanderAbstractList);
                        for (SearchHanderAbstract handerAbstract : searchHanderAbstracts) {
                            plugSearchMusicResultPlugSearchResult = handerAbstract.querySongByName(new SearchKeyData().setSearchkey(parserEntity.getSongName() + " " + parserEntity.getArtistsName()).setPageIndex(0).setPageSize(5));

                            List<PlugSearchMusicResult> records1 = plugSearchMusicResultPlugSearchResult.getRecords();
                            for (PlugSearchMusicResult record : records1) {
                                if (parserEntity.getArtistsName().trim().equals(record.getArtistName().trim())){
                                    id = record.getId();

                                        try {
                                            music = finalSearchHanderAbstract.querySongById(id);
                                        } catch (IgnoreDownloadException e) {
                                            int i = 0;
                                            while (music==null){
                                                log.info("歌曲(酷我id){}---->获取歌曲详情歌词信息失败正在重试", id);
                                                try {
                                                    music = finalSearchHanderAbstract.querySongById(id);
                                                } catch (Exception ex) {
                                                }
                                                i++;
                                                if (i>10){
                                                    break;
                                                }
                                            }
                                        }

                                    break;
                                }
                            }

                        }
                        if (music==null){
                            return;
                        }


                    }else{
                        //成功了
                        Music finalMusic = music;
                        music.setMusicArtists(parserEntity.getArtistsName());
                        DownloadInfo downloadInfo = new DownloadInfo().setDownloadMusicId(finalMusic.getId())
                                .setDownloadBrType(PlugBrType.KW_FLAC_2000.getValue())
                                .setDownloadMusicname(finalMusic.getMusicName())
                                .setDownloadArtistname(finalMusic.getMusicArtists())
                                .setDownloadAlbumname(finalMusic.getMusicAlbum())
                                .setAudioBook("false")
                                .setAddSubsonicPlayListName(subsonicPlayListName)
                                .setSpringName(PlugBrType.KW_FLAC_2000.getSpringName())
                                .setDownloadType(PlugBrType.KW_FLAC_2000.getPlugName())
                                .setDownloadTime(new Date());
                        Boolean add = downloadInfoService.add(downloadInfo);
                    }
                } catch (Exception e) {
                    log.error("没有查询出错：" + parserEntity+"  "+e.getMessage());
                    continue;
                }
            }
        });
        return AjaxResult.success(true);
    }

    @SaCheckLogin
    @PostMapping("/ArtistSongList")
    public AjaxResult ArtistSongList(@RequestBody DownlaodArtis downlaodArtis) {

        SearchHanderAbstract searchHanderAbstract = null;
        for (SearchHanderAbstract item : searchHanderAbstractList) {
            if(item.getPlugName().equals(downlaodArtis.getPlugType())){
                searchHanderAbstract = item;
            }
        }
        if (searchHanderAbstract==null){
            return AjaxResult.error("未知的搜索类型");
        }
        SqConfig accompaniment = configService.getOne(new QueryWrapper<SqConfig>().eq("config_key", "music.ignore.accompaniment"));
        List<Music> musics = searchHanderAbstract.getAlbumSongByAlbumsId(downlaodArtis.getId());
        ArrayList<DownloadInfo> downloadInfos = new ArrayList<>();
        for (Music music : musics) {
            if (Boolean.getBoolean(accompaniment.getConfigValue())) {
                if (music.getMusicName().contains("(伴奏)") || music.getMusicName().contains("(试听版)") || music.getMusicName().contains("(片段)")) {
                    continue;
                }
            }
            PlugBrType plugType;
            if (StringUtils.isEmpty(downlaodArtis.getPlugTypeValue())){
                plugType = TypeUtils.getPlugType(downlaodArtis.getPlugType(), downlaodArtis.getBr());
            }else{
                plugType = TypeUtils.getPlugType(downlaodArtis.getPlugType(), downlaodArtis.getPlugTypeValue());
            }
            DownloadEntity downloadEntity = searchHanderAbstract.downloadSong(music, plugType, "");
            DownloadInfo downloadInfo = MusicUtils.downloadEntitytoDownloadInfoTo(downloadEntity);
            downloadInfos.add(downloadInfo);
        }
        downloadInfoService.add(downloadInfos);
        return AjaxResult.success(true);
    }




    @RequestMapping(value = "login",method = RequestMethod.POST)
    public AjaxResult login(@RequestBody HashMap<String,String> data )  {
        String username = data.get("username");
        String password = data.get("password");
        if (StringUtils.isEmpty(username)||StringUtils.isEmpty(password)){
            return AjaxResult.error("登录失败");
        }
        SqConfig suser = configService.getOne(new QueryWrapper<SqConfig>().eq("config_key", "system.username"));
        SqConfig spwd = configService.getOne(new QueryWrapper<SqConfig>().eq("config_key", "system.password"));
        if (suser.getConfigValue().equals(username) && spwd.getConfigValue().equals(password)) {
            StpUtil.login(9527);
            SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
           return AjaxResult.success(tokenInfo);
        }else{
            return   AjaxResult.error("账号密码错误");
        }
    }


    @RequestMapping(value = "logout",method = RequestMethod.POST)
    public AjaxResult logout()  {
         StpUtil.logout(9527);
         return AjaxResult.success("已退出");

    }
    @RequestMapping(value = "isLogin")
    public AjaxResult isLogin() {
        return  StpUtil.isLogin()?AjaxResult.success("登录有效",true):AjaxResult.error("过期",false);
    }


    public static List<SearchHanderAbstract>  sortHander( List<SearchHanderAbstract> list) {
        List<String> targetList = Arrays.asList("kw", "netease", "qq", "qqvip");
        List<SearchHanderAbstract> clone = ObjectUtil.clone(list);
        // 按照 list 里的 name 来排序 targetList
        Collections.sort(clone, ((o1, o2) -> {
            int io1 = targetList.indexOf(o1.getPlugName());
            int io2 = targetList.indexOf(o2.getPlugName());

            if (io1 != -1) {
                io1 = targetList.size() - io1;
            }
            if (io2 != -1) {
                io2 = targetList.size() - io2;
            }

            return io2 - io1;
        }));
        return clone ;
    }


    @SaCheckLogin
    @PostMapping("/download/freemp3")
    public String freemp3Download(@Valid  @RequestBody PlugFreeMp3DownloadEntity data) throws Exception {
        String musicname = data.getMusicname();
        String musicartistName = data.getMusicartistName();
        String musicalbumName = data.getMusicalbumName();
        String downloadurl = data.getDownloadurl();
        String musiclyrc = data.getMusiclyrc();
        String musicimage = data.getMusicimage();


        String[] split = musicname.split("-");
        String musicName = null;
        try {
            musicName = split[0].trim();
        } catch (Exception e) {
            musicName = musicname;
        }

        String artistName = null;
        if (StringUtils.isBlank(musicartistName)){
            try {
                artistName = split[1].trim();
            } catch (Exception e) {
                artistName="";
            }
        }else{
            artistName = musicartistName;
        }
        if (StringUtils.isBlank(musicalbumName)){
            //网易喜欢搞这种的  酷我是无专辑大户
            musicalbumName = musicName;
        }

        String finalMusicName = musicName;
        String finalArtistName = artistName;
        String finalMusicalbumName = musicalbumName;
        threadPoolTaskExecutor.execute(() -> {
            String redirectUrl = null;
            try {
                redirectUrl = OkHttpUtils.getRedirectUrl(downloadurl);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            DownloadEntity download = freeMp3Hander.download(finalMusicName, finalArtistName, finalMusicalbumName, musiclyrc, musicimage, redirectUrl);
            downloadInfoService.add(MusicUtils.downloadEntitytoDownloadInfoTo(download));
        });

        return "ok";
    }


    @GetMapping(value = "/download/freemp3",produces = MediaType.TEXT_HTML_VALUE)
    public String freemp3DownloadGet(@RequestParam String data) throws Exception {

        byte[] receive = PakoUtil.receive(data);
        byte[] decompress = ZLibUtils.decompress(receive);
        String s = new String(decompress);
        JSONObject jsonObject = JSONObject.parseObject(s);
        String musicname = jsonObject.getString("musicname");
        String musicartistName = jsonObject.getString("musicartistName");
        String musicalbumName = jsonObject.getString("musicalbumName");
        String downloadurl = jsonObject.getString("downloadurl");
        String musicimage = jsonObject.getString("musicimage");
        String musiclyrc = jsonObject.getString("musiclyrc");
        String sqmusictoken = jsonObject.getString("sqmusictoken");


        Object loginIdByToken = StpUtil.getLoginIdByToken(sqmusictoken);
        if(loginIdByToken == null){
            return AjaxResult.error("登录过期").toString();
        }
        String[] split = musicname.split("-");
        String musicName = null;
        try {
            musicName = split[0].trim();
        } catch (Exception e) {
            musicName = musicname;
        }

        String artistName = null;
        if (StringUtils.isBlank(musicartistName)){
            try {
                artistName = split[1].trim();
            } catch (Exception e) {
                artistName="";
            }
        }else{
            artistName = musicartistName;
        }
        if (StringUtils.isBlank(musicalbumName)){
            //网易喜欢搞这种的  酷我是无专辑大户
            musicalbumName = musicName;
        }

        String finalMusicName = musicName;
        String finalArtistName = artistName;
        String finalMusicalbumName = musicalbumName;
        threadPoolTaskExecutor.execute(() -> {
            String redirectUrl = null;
            try {
                redirectUrl = OkHttpUtils.getRedirectUrl(downloadurl);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            DownloadEntity download = freeMp3Hander.download(finalMusicName, finalArtistName, finalMusicalbumName, musiclyrc, musicimage, redirectUrl);
            downloadInfoService.add(MusicUtils.downloadEntitytoDownloadInfoTo(download));
        });


        return "<html>" +
                "<header><title>Welcome</title></header>" +
                "<body>" +
                "即将自动关闭" +
                "<a href=\"javascript:window.opener=null;window.open('','_self');window.close();\">手动关闭</a>" +
                "</body>" +
                "<script>" +
                "function closeWindow(){\n" +
                "\tvar userAgent = navigator.userAgent;\n" +
                "\tif (userAgent.indexOf(\"Firefox\") != -1 || userAgent.indexOf(\"Chrome\") !=-1) {\n" +
                "\t\t\twindow.location.href=\"about:blank\";\n" +
                "\t\t\twindow.close();\n" +
                "\t} else {\n" +
                "\t\t\twindow.opener = null;\n" +
                "\t\t\twindow.open(\"\", \"_self\");\n" +
                "\t\t\twindow.close();\n" +
                "\t}\n" +
                "}"+
                "closeWindow()"+
                "</script>"+
                "</html>";
    }



}



