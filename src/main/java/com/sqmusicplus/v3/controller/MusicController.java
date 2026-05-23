package com.sqmusicplus.v3.controller;

import com.sqmusicplus.v3.base.entity.DownloadInfo;
import com.sqmusicplus.v3.base.entity.vo.DownloadMusicParam;
import com.sqmusicplus.v3.base.entity.vo.SearchMusicByIdParam;
import com.sqmusicplus.v3.base.enums.PlugBrType;
import com.sqmusicplus.v3.download.vo.DownloadUrlResult;
import com.sqmusicplus.v3.plug.entity.*;
import com.sqmusicplus.v3.base.entity.vo.SearchMusicParam;
import com.sqmusicplus.v3.config.AjaxResult;
import com.sqmusicplus.v3.config.exception.SQException;
import com.sqmusicplus.v3.plug.base.hander.SearchHanderAbstract;
import com.sqmusicplus.v3.utils.MusicUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Classname MusicController
 * @Description 音频控制类主要是搜索和下载
 * @Version 1.0.0
 * @Date 2025/7/24 17:33
 * @Created by SQ
 */
@Slf4j
@RestController
@RequestMapping("/api/music")
public class MusicController {

    @Autowired
    List<SearchHanderAbstract> searchHanderAbstractList;

    /**
     * 搜索提示词
     * @param param
     * @return
     */
    @GetMapping("/searchTips")
    public AjaxResult searchTips(SearchMusicParam param) {
        SearchHanderAbstract plugHander = MusicUtils.getPlugHander(param.getPlugName(),searchHanderAbstractList);
        List<String> strings = plugHander.searchTip(param.getKeyword());
        return AjaxResult.success(strings);
    }


    /**
     * 搜索单曲
     * @param param  搜索条件
     * @return
     */
    @GetMapping("/searchSong")
    public AjaxResult searchSong(SearchMusicParam param){
        SearchHanderAbstract plugHander = MusicUtils.getPlugHander(param.getPlugName(),searchHanderAbstractList);
        SearchKeyData searchKeyData = new SearchKeyData().setPageIndex(param.getPageIndex()).setPageSize(param.getPageSize()).setSearchkey(param.getKeyword()).setPlugName(param.getPlugName());
        PlugSearchResult<PlugSearchMusicResult> plugSearchMusicResultPlugSearchResult = plugHander.querySongByName(searchKeyData);
        return AjaxResult.success(plugSearchMusicResultPlugSearchResult);
    }

    /**
     * 获取单曲信息 根据id
     * @param param 搜索条件
     * @return
     */
    @GetMapping("/SongInfoById")
    public AjaxResult SongInfoById(SearchMusicByIdParam param){
        SearchHanderAbstract plugHander = MusicUtils.getPlugHander(param.getPlugName(),searchHanderAbstractList);
        Music music = plugHander.querySongById(param.getId());
        return AjaxResult.success(music);
    }

    /**
     * 搜索歌手
     * @param param 搜索条件
     * @return
     */
    @GetMapping("/searchArtist")
    public AjaxResult searchArtist(SearchMusicParam param){
        SearchHanderAbstract plugHander = MusicUtils.getPlugHander(param.getPlugName(),searchHanderAbstractList);
        SearchKeyData searchKeyData = new SearchKeyData().setSearchkey(param.getKeyword()).setPageSize(param.getPageSize()).setPageIndex(param.getPageIndex()).setPlugName(param.getPlugName());
        PlugSearchResult<PlugSearchArtistResult> plugSearchArtistResultPlugSearchResult = plugHander.queryArtistByName(searchKeyData);
        return AjaxResult.success(plugSearchArtistResultPlugSearchResult);
    }


    /**
     * 根据歌手ID查询歌手全部专辑
     */
    @GetMapping("/artistAlbumById")
    public AjaxResult ArtistAlbumById(SearchMusicByIdParam param){
        SearchHanderAbstract plugHander = MusicUtils.getPlugHander(param.getPlugName(),searchHanderAbstractList);
        Artists artists = plugHander.queryArtistById(param.getId());
        List<Album> albumsByArtist = plugHander.getAlbumsByArtist(param.getId());
        artists.setAlbums(albumsByArtist);
        return AjaxResult.success(artists);
    }


    /**
     * 搜索专辑
     * @param param 搜索条件
     * @return
     */
    @GetMapping("/searchAlbum")
    public AjaxResult searchAlbum(SearchMusicParam param){
        SearchHanderAbstract plugHander = MusicUtils.getPlugHander(param.getPlugName(),searchHanderAbstractList);
        SearchKeyData searchKeyData = new SearchKeyData().setSearchkey(param.getKeyword()).setPageSize(param.getPageSize()).setPageIndex(param.getPageIndex()).setPlugName(param.getPlugName());
        PlugSearchResult<PlugSearchAlbumResult> plugSearchAlbumResultPlugSearchResult = plugHander.queryAlbumByName(searchKeyData);
        return AjaxResult.success(plugSearchAlbumResultPlugSearchResult);
    }
    /**
     * 获取专辑信息 根据id
     */

    @GetMapping("/albumInfoById")
    public AjaxResult albumInfoById(SearchMusicByIdParam param){
        SearchHanderAbstract plugHander = MusicUtils.getPlugHander(param.getPlugName(),searchHanderAbstractList);
        Album album = plugHander.queryAlbumById(param.getId());
        return AjaxResult.success(album);
    }


    /**
     *获取歌词
     */
    @PostMapping("/getLyric")
    public AjaxResult getLyric(@RequestBody SearchMusicByIdParam param){
        SearchHanderAbstract plugHander = MusicUtils.getPlugHander(param.getPlugName(),searchHanderAbstractList);
        String lyric = plugHander.queryLyric(param.getId());
        return AjaxResult.success(lyric);
    }

    /**
     * 获取下载链接(播放链接)
     * @param param 搜索条件
     * @return 下载信息
     */
    @PostMapping("/getDownloadUrl")
    public AjaxResult getDownloadUrl(@RequestBody PlugDownloadSongParam param){
        SearchHanderAbstract plugHander = MusicUtils.getPlugHander(param.getPlugName(),searchHanderAbstractList);
        PlugBrType brType = param.getBrType();
        if (brType==null){
            throw new SQException("未知的音频质量");
        }
        Music music = plugHander.querySongById(param.getId());
        music.setBits(param.getBrTypes());
        DownloadInfo downloadInfo = plugHander.musicToDownloadInfo(music, brType, false);
        DownloadUrlResult downloadUrl = plugHander.getDownloadUrl(downloadInfo);
        return AjaxResult.success(downloadUrl);
    }




}
