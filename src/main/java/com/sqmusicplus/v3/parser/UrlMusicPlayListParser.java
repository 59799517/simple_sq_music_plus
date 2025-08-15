package com.sqmusicplus.v3.parser;


import cn.hutool.core.collection.ListUtil;
import com.sqmusicplus.v3.base.entity.DownloadInfo;
import com.sqmusicplus.v3.plug.entity.Music;
import com.sqmusicplus.v3.base.service.DownloadInfoService;
import com.sqmusicplus.v3.download.vo.DownlaodParserUrl;

import com.sqmusicplus.v3.plug.kg.hander.KGHander;
import com.sqmusicplus.v3.plug.kw.hander.NKwSearchHander;
import com.sqmusicplus.v3.plug.netease.hander.NeteaseHander;
import com.sqmusicplus.v3.plug.qq.entity.DissInfo;
import com.sqmusicplus.v3.plug.qqvip.QQvipHander;
import com.sqmusicplus.v3.utils.DownloadUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

/**
 * @Classname TextMusicPlayListParser
 * @Description 文本类型歌单解析
 * @Version 1.0.0
 * @Date 2022/8/10 16:15
 * @Created by SQ
 */
@Component("urlParser")
@Slf4j
public class UrlMusicPlayListParser {

    @Autowired
    QQvipHander qqvipHander;
    @Autowired
    private NKwSearchHander nKwSearchHander;
    @Autowired
    private NeteaseHander neteaseHander;
    @Autowired
    private KGHander kgHander;



    @Autowired
    private DownloadInfoService downloadInfoService;


    public List<Music> parser(DownlaodParserUrl downlaodParserUrl) throws IOException {
        String url = downlaodParserUrl.getUrl();
        //找出url所属的平台
        //找出是不是酷我歌单
        if (url.contains("c6.y.qq.com")) {
            //是QQ的
            //获取url的302来判断是那种类型
            OkHttpClient okHttpClient = DownloadUtils.getOkHttpClient(false);
            Request authRequest = new Request.Builder()
                    .url(url)
                    .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                    .get()
                    .build();
            try (Response authResponse = okHttpClient.newCall(authRequest).execute()){
                String location = authResponse.header("Location");
                if (location == null) {
                    log.error("QQ音乐识别分享类型失败");
                };
                return  QqMusic(url,downlaodParserUrl);
            }
        }
        else if(url.contains("i.y.qq.com")){
            //qq网页分享的
            return  QqMusic(url,downlaodParserUrl);
        }
        else if (url.contains("www.kuwo.cn")) {
            //酷我的
            if (url.contains("album")||url.contains("album_detail")) {
            //专辑
                String[] split = url.split("/");
                String id = split[split.length - 1];
                List<Music> albumSongByAlbumsId = nKwSearchHander.getAlbumSongByAlbumsId(id);
                if (downlaodParserUrl.getIsAudioBook()){
                    for (Music smusic : albumSongByAlbumsId) {
                        smusic.setMusicArtists(ListUtil.of(downlaodParserUrl.getArtist()));
                        smusic.setMusicAlbum(downlaodParserUrl.getArtist());
                    }
                }
                return albumSongByAlbumsId;
            }
            else if (url.contains("playlist")||url.contains("playlist_detail")) {
            //歌单
                String[] split = url.split("/");
                String id = split[split.length - 1];
                List<Music> musics = nKwSearchHander.queryAllPlayInfoList(id, 100, 1);
                if (downlaodParserUrl.getIsAudioBook()){
                    for (Music smusic : musics) {
                        smusic.setMusicArtists(ListUtil.of(downlaodParserUrl.getArtist()));
                        smusic.setMusicAlbum(downlaodParserUrl.getArtist());
                    }
                }
                return musics;
            }

            else if(url.contains("yinyue")||url.contains("play_detail")){
                String[] split = url.split("/");
                String id = split[split.length - 1];
                Music music = nKwSearchHander.querySongById(id);
                if (downlaodParserUrl.getIsAudioBook()){
                    music.setMusicArtists(ListUtil.of(downlaodParserUrl.getArtist()));
                    music.setMusicAlbum(downlaodParserUrl.getArtist());
                }
                return List.of(music);

            }
            else{
                throw new RuntimeException("未知的分享类型酷我仅支持 歌单、专辑、单曲");
            }
        }
        else if (url.contains("music.163.com")) {
            //专辑
            if (url.contains("album")) {
                Map<String, String> urlParams = getUrlParams(url);
                String albumId = urlParams.get("id");
                List<Music> albumSongByAlbumsId = neteaseHander.getAlbumSongByAlbumsId(albumId);
                if (downlaodParserUrl.getIsAudioBook()){
                    for (Music smusic : albumSongByAlbumsId) {
                        smusic.setMusicArtists(ListUtil.of(downlaodParserUrl.getArtist()));
                        smusic.setMusicAlbum(downlaodParserUrl.getArtist());
                    }
                }
                return albumSongByAlbumsId;
            }else if (url.contains("playlist")) {
                Map<String, String> urlParams = getUrlParams(url);
                String playlistId = urlParams.get("id");
                ArrayList<Music> playList = neteaseHander.getPlayList(playlistId);
                if (downlaodParserUrl.getIsAudioBook()){
                    for (Music smusic : playList) {
                        smusic.setMusicArtists(ListUtil.of(downlaodParserUrl.getArtist()));
                        smusic.setMusicAlbum(downlaodParserUrl.getArtist());
                    }
                }
                return playList;
            }else if(url.contains("song")){
                Map<String, String> urlParams = getUrlParams(url);
                String songId = urlParams.get("id");
                Music music = neteaseHander.querySongById(songId);
                if (downlaodParserUrl.getIsAudioBook()){
                    music.setMusicArtists(ListUtil.of(downlaodParserUrl.getArtist()));
                    music.setMusicAlbum(downlaodParserUrl.getArtist());
                }
                return List.of(music);
            }
        }
        else if (url.contains("kugou.com")) {
            OkHttpClient okHttpClient = DownloadUtils.getOkHttpClient(false);
            Request authRequest = new Request.Builder()
                    .url(url)
                    .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                    .get()
                    .build();
            try (Response authResponse = okHttpClient.newCall(authRequest).execute()){
                String location = authResponse.header("Location");
                if (location == null) {
                    log.error("酷狗概念分享类型识别失败！");
                };
                Map<String, String> urlParams = getUrlParams(location);
                // 专辑链接判断
                if (urlParams.containsKey("specialid")) {
                    String specialId = urlParams.get("specialid");
                    List<Music> albumSongByAlbumsId = kgHander.getAlbumSongByAlbumsId(specialId);
                    if (downlaodParserUrl.getIsAudioBook()){
                        for (Music smusic : albumSongByAlbumsId) {
                            smusic.setMusicArtists(ListUtil.of(downlaodParserUrl.getArtist()));
                            smusic.setMusicAlbum(downlaodParserUrl.getArtist());
                        }
                    }
                    return albumSongByAlbumsId;

                }
                // 单曲链接判断
                else if (urlParams.containsKey("album_id") && urlParams.containsKey("album_audio_id")&& urlParams.containsKey("hash")) {
                    String hash = urlParams.get("hash");
                    Music music = kgHander.querySongById(hash);
                    if (downlaodParserUrl.getIsAudioBook()){
                        music.setMusicArtists(ListUtil.of(downlaodParserUrl.getArtist()));
                        music.setMusicAlbum(downlaodParserUrl.getArtist());
                    }
                    return List.of(music);
                }else {
                    throw new RuntimeException("未知的分享类型酷狗概念仅支持 专辑、单曲");
                }
                
            }
        }
        else{
            throw new RuntimeException("未知的分享类型仅支持QQ、酷狗概念版、酷我、网易云音乐");
        }
        return null;
    }



    public List<Music>  QqMusic(String  url,DownlaodParserUrl downlaodParserUrl ) throws MalformedURLException {
        if (url.contains("album.html")) {
            //专辑
//                获取 albumId  https://i.y.qq.com/n2/m/share/details/album.html?ADTAG=pc_v17&albumId=3826322&channelId=10036163&openinqqmusic=1
            Map<String, String> params = getUrlParams(url);
            String albumId = params.get("albumId");
            List<Music> albumSongByAlbumsId = qqvipHander.getAlbumSongByAlbumsId(albumId);
            if (downlaodParserUrl.getIsAudioBook()){
                for (Music smusic : albumSongByAlbumsId) {
                    smusic.setMusicArtists(ListUtil.of(downlaodParserUrl.getArtist()));
                    smusic.setMusicAlbum(downlaodParserUrl.getArtist());
                }
            }
            return albumSongByAlbumsId;
        }
        else if (url.contains("taoge.html")) {
            //歌单
            Map<String, String> params = getUrlParams(url);
            String playListId = params.get("id");
            DissInfo dissInfo = qqvipHander.songListInfo(playListId, "1418", 1);

            Long code = dissInfo.getCode();
            if (code != null && code == 0L) {
                DissInfo.DataDTO data = dissInfo.getData();
                Long totalSongNum = data.getTotalSongNum();
                Long songlistSize = data.getSonglistSize();
                List<DissInfo.DataDTO.SonglistDTO> songlist = data.getSonglist();
                //看总数是否能获取全部的  songlistSize 是每页长度
                if (totalSongNum.longValue() > songlistSize.longValue()) {
                    //计算还需要的页数
                    Long pageNum = totalSongNum.longValue() / songlistSize.longValue();
                    //计算是否有余数
                    if (totalSongNum.longValue() % songlistSize.longValue() > 0) {
                        pageNum++;
                    }
                    for (int i = 2; i <= pageNum; i++) {
                        DissInfo dissInfo1 = qqvipHander.songListInfo(playListId, "1418", Long.parseLong(i + ""));
                        DissInfo.DataDTO data1 = dissInfo1.getData();
                        List<DissInfo.DataDTO.SonglistDTO> songlist1 = data1.getSonglist();
                        songlist.addAll(songlist1);
                    }
                }
                ArrayList<Music> musicList = new ArrayList<>();
                songlist.forEach((item) -> {
                    String songmid = item.getMid();
                    Music music = qqvipHander.querySongById(songmid);
                    if (downlaodParserUrl.getIsAudioBook()){
                        music.setMusicArtists(ListUtil.of(downlaodParserUrl.getArtist()));
                        music.setMusicAlbum(downlaodParserUrl.getArtist());
                    }
                    musicList.add(music);
                });
                return musicList;
            }
            return null;
        }
        else if (url.contains("playlist.html")) {
            //单曲
            Map<String, String> params = getUrlParams(url);
            String songid = params.get("songid");
            //单曲
            ArrayList<Music> musicList = new ArrayList<>();
            Music music = qqvipHander.querySongById(songid);
            musicList.add(music);
            if (downlaodParserUrl.getIsAudioBook()){
                for (Music smusic : musicList) {
                    smusic.setMusicArtists(ListUtil.of(downlaodParserUrl.getArtist()));
                    smusic.setMusicAlbum(downlaodParserUrl.getArtist());
                }
            }
            return musicList;
        }
        else{
            log.error("未知的分享类型qq仅支持 歌单、专辑、单曲");
            throw new RuntimeException("未知的分享类型qq仅支持 歌单、专辑、单曲");
        }
    }

    @NotNull
    private static Map<String, String> getUrlParams(String url) throws MalformedURLException {
        URI uri = null;
        try {
            uri = new URL(url).toURI();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        String query = uri.getQuery();
        Map<String, String> params = new HashMap<>();
        for (String param : query.split("&")) {
            String[] pair = param.split("=");
            params.put(pair[0], pair.length > 1 ? pair[1] : null);
        }
        return params;
    }

}
