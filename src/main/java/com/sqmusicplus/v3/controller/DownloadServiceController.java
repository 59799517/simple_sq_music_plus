package com.sqmusicplus.v3.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.sqmusicplus.v3.base.entity.DownloadInfo;
import com.sqmusicplus.v3.base.entity.vo.ParserEntity;
import com.sqmusicplus.v3.base.enums.PlugBrType;
import com.sqmusicplus.v3.base.service.DownloadInfoService;
import com.sqmusicplus.v3.config.AjaxResult;
import com.sqmusicplus.v3.config.exception.SQException;
import com.sqmusicplus.v3.download.vo.DownlaodParserUrl;
import com.sqmusicplus.v3.download.vo.ParserTextParam;
import com.sqmusicplus.v3.parser.TextMusicPlayListParser;
import com.sqmusicplus.v3.parser.UrlMusicPlayListParser;
import com.sqmusicplus.v3.plug.base.hander.SearchHanderAbstract;
import com.sqmusicplus.v3.plug.entity.*;
import com.sqmusicplus.v3.utils.MusicUtils;
import com.sqmusicplus.v3.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @Classname DownloadServiceController
 * @Description 下载到服务器控制接口
 * @Version 1.0.0
 * @Date 2025/7/25 10:29
 * @Created by SQ
 */
@Slf4j
@RestController
@RequestMapping("/api/download")
public class DownloadServiceController {

    @Autowired
    List<SearchHanderAbstract> searchHanderAbstractList;

    @Autowired
    private DownloadInfoService downloadInfoService;


    @Autowired
    private UrlMusicPlayListParser urlMusicPlayListParser;
    @Autowired
    private TextMusicPlayListParser textMusicPlayListParser;

    /**
     * 下载单曲
     * @param downloadSongParam
     * @return
     */
    @SaCheckLogin
    @PostMapping("/downloadSong")
   public AjaxResult downloadSong(@RequestBody PlugDownloadSongParam downloadSongParam) {
        SearchHanderAbstract plugHander = MusicUtils.getPlugHander(downloadSongParam.getPlugName(), searchHanderAbstractList);
        List<PlugBrType> brTypes = downloadSongParam.getBrTypes();
        if (brTypes==null|| brTypes.isEmpty()){
            throw new SQException("未找到可供下载的bit");
        }
        PlugBrType maxBr = MusicUtils.getMaxBr(brTypes);
        if (downloadSongParam.getBrType()!=null){
            maxBr = downloadSongParam.getBrType();
        }
        DownloadInfo downloadInfo = plugHander.musicToDownloadInfo(downloadSongParam, maxBr, false);
        Boolean add = downloadInfoService.add(downloadInfo);
        if (add){
            return AjaxResult.success("下载成功",downloadInfo);
        }
        return AjaxResult.error("下载失败");
    }

    /**
     * 下载歌手的全部专辑
     * @param plugDownloadArtisParam
     * @return
     */
    @SaCheckLogin
    @PostMapping("/downloadArtistAlbum")
    public AjaxResult downloadArtistAlbum(@RequestBody PlugDownloadArtisParam plugDownloadArtisParam) {
        SearchHanderAbstract plugHander = MusicUtils.getPlugHander(plugDownloadArtisParam.getPlugName(), searchHanderAbstractList);
        PlugBrType maxBr= null;
        if (plugDownloadArtisParam.getBit()!=null){
            maxBr = PlugBrType.findByPlugNameAndBit(plugDownloadArtisParam.getPlugName(), plugDownloadArtisParam.getBit());
        }
        List<DownloadInfo> downloadInfos = plugHander.downloadArtistAllAlbum(plugDownloadArtisParam.getArtistid(), maxBr);
        Boolean add = downloadInfoService.add(downloadInfos);
        if (add){
            return AjaxResult.success("下载成功",downloadInfos);
        }
        return AjaxResult.error("下载失败");
    }

    /**
     * 下载专辑
     * @param plugDownloadAlbumParam
     * @return
     */
    @SaCheckLogin
    @PostMapping("/downloadAlbum")
    public AjaxResult downloadAlbum(@RequestBody PlugDownloadAlbumParam plugDownloadAlbumParam){
        SearchHanderAbstract plugHander = MusicUtils.getPlugHander(plugDownloadAlbumParam.getPlugName(), searchHanderAbstractList);
        PlugBrType maxBr= null;
        if (plugDownloadAlbumParam.getBit()!=null){
            maxBr = PlugBrType.findByPlugNameAndBit(plugDownloadAlbumParam.getPlugName(), plugDownloadAlbumParam.getBit());
        }
        List<String> artistNameList = null;
        String albumid = plugDownloadAlbumParam.getAlbumid();
        if (StringUtils.isNotBlank(plugDownloadAlbumParam.getArtistName())){
            String[] split = plugDownloadAlbumParam.getArtistName().split("&");
            artistNameList= new ArrayList<>();
            for (String s : split) {
                artistNameList.add(s.trim());
            }
        }
        ArrayList<DownloadInfo> downloadInfos = plugHander.downloadAlbum(albumid, maxBr, artistNameList, false, plugDownloadAlbumParam.getAlbumName());
        Boolean add = downloadInfoService.add(downloadInfos);
        if (add){
            return AjaxResult.success("下载成功",downloadInfos);
        }
        return AjaxResult.error("下载失败");
    }

    /**
     * 下载解析的URL歌曲
     * @param downlaodParserUrl 解析的URL
     * @return
     */
    @SaCheckLogin
    @PostMapping("/downloadParserUrl")
    public AjaxResult downloadParserUrl(@RequestBody DownlaodParserUrl downlaodParserUrl) {
        // Validate URL format first (quick check before going async)
        String url = downlaodParserUrl.getUrl();
        if (StringUtils.isBlank(url)) {
            return AjaxResult.error("请输入要解析的URL");
        }
        if (!url.contains("y.qq.com") && !url.contains("www.kuwo.cn")
                && !url.contains("music.163.com") && !url.contains("kugou.com")) {
            return AjaxResult.error("解析失败 仅支持qq 酷我 酷狗概念 网易云");
        }
        // Run parsing and downloading asynchronously to avoid nginx proxy timeout
        Thread thread = new Thread(() -> {
            try {
                List<Music> parser = urlMusicPlayListParser.parser(downlaodParserUrl);
                if (parser == null || parser.isEmpty()) {
                    log.warn("URL parser: no songs parsed from URL: {}", url);
                    return;
                }
                ArrayList<DownloadInfo> downloadInfos = new ArrayList<>();
                for (Music music : parser) {
                    try {
                        SearchHanderAbstract plugHander = MusicUtils.getPlugHander(music.getPlugName(), searchHanderAbstractList);
                        DownloadInfo downloadInfo = plugHander.musicToDownloadInfo(music, null, false);
                        downloadInfos.add(downloadInfo);
                    } catch (Exception e) {
                        log.error("URL parser: failed to create download info for song: {}, error: {}", music.getMusicName(), e.getMessage());
                    }
                }
                if (!downloadInfos.isEmpty()) {
                    Boolean add = downloadInfoService.add(downloadInfos);
                    if (add) {
                        log.info("URL parser: successfully added {} download tasks from URL: {}", downloadInfos.size(), url);
                    }
                } else {
                    log.warn("URL parser: no download tasks created from URL: {}", url);
                }
            } catch (Exception e) {
                log.error("URL parser: failed to parse URL: {}", url, e);
            }
        });
        thread.start();
        return AjaxResult.success("解析成功，稍后在下载处查看");
    }


    /**
     * 下载解析的URL歌曲（替代解析方法）
     * @param musicList
     * @return
     */
    @SaCheckLogin
    @PostMapping("/downloadParserUrlResult")
    public AjaxResult downloadParserUrlResult(@RequestBody List<Music> musicList) {
        ArrayList<DownloadInfo> downloadInfos = new ArrayList<>();
        for (Music music : musicList) {
            SearchHanderAbstract plugHander = MusicUtils.getPlugHander(music.getPlugName(), searchHanderAbstractList);
            DownloadInfo downloadInfo = plugHander.musicToDownloadInfo(music, null, false);
            downloadInfos.add(downloadInfo);
        }
        Boolean add = downloadInfoService.add(downloadInfos);
        if (add){
            return AjaxResult.success("下载成功",downloadInfos);
        }
        return AjaxResult.error("下载失败");
    }

    /**
     * 下载解析的文本歌曲(替代解析方法)
     * @param param
     * @return
     */
    @SaCheckLogin
    @PostMapping("/downloadParserText")
    public AjaxResult downloadParserText(@RequestBody ParserTextParam param) {
        if (StringUtils.isBlank(param.getText())) {
            return AjaxResult.error("请输入要解析的文本");
        }
        Thread thread = new Thread(() -> {
            try {

                List<ParserEntity> parser = textMusicPlayListParser.parser(param.getText());
                List<ParserEntity> parserEntities = textMusicPlayListParser.parserParserEntity(parser);

                if (parserEntities != null) {
                    ArrayList<DownloadInfo> downloadInfos = new ArrayList<>();
                    for (ParserEntity parserEntity : parserEntities) {
                        // Skip entries that were not matched to any song
                        if (parserEntity.getIsDetection() == null || !parserEntity.getIsDetection()) {
                            log.warn("Text parser: song not matched, skipping: {} - {}", parserEntity.getSongName(), parserEntity.getArtistsName());
                            continue;
                        }
                        PlugSearchMusicResult plugSearchMusicResult = parserEntity.getPlugSearchMusicResult();
                        if (plugSearchMusicResult == null) {
                            log.warn("Text parser: plugSearchMusicResult is null, skipping: {}", parserEntity.getSongName());
                            continue;
                        }
                        try {
                            SearchHanderAbstract plugHander = MusicUtils.getPlugHander(plugSearchMusicResult.getPlugName(), searchHanderAbstractList);
                            DownloadInfo downloadInfo = plugHander.musicToDownloadInfo(plugSearchMusicResult, null, false);
                            downloadInfos.add(downloadInfo);
                        } catch (Exception e) {
                            log.error("Text parser: failed to create download info for song: {}, error: {}", parserEntity.getSongName(), e.getMessage());
                        }
                    }
                    if (!downloadInfos.isEmpty()) {
                        Boolean add = downloadInfoService.add(downloadInfos);
                        if (add) {
                            log.info("Text parser: successfully added {} download tasks", downloadInfos.size());
                            return;
                        }
                    } else {
                        log.warn("Text parser: no songs matched, nothing to download");
                    }
                }
//                return ;

            } catch (Exception e) {
                log.error("解析失败", e);
//                return ;
            }

        });
        thread.start();
        return AjaxResult.success("开始解析并下载，稍后在下载中查看！");
    }

    /**
     * 批量下载解析的文本歌曲 替代解析方法
     * @param parserEntities
     * @return
     */
    @SaCheckLogin
    @PostMapping("/downloadParserTextResult")
    public AjaxResult downloadParserTextResult(@RequestBody List<ParserEntity> parserEntities) {
        ArrayList<DownloadInfo> downloadInfos = new ArrayList<>();
        for (ParserEntity parserEntity : parserEntities) {
            PlugSearchMusicResult plugSearchMusicResult = parserEntity.getPlugSearchMusicResult();
            SearchHanderAbstract plugHander = MusicUtils.getPlugHander(plugSearchMusicResult.getPlugName(), searchHanderAbstractList);
            DownloadInfo downloadInfo = plugHander.musicToDownloadInfo(plugSearchMusicResult, null, false);
            downloadInfos.add(downloadInfo);
        }
        Boolean add = downloadInfoService.add(downloadInfos);
        if (add){
            return AjaxResult.success("下载成功",downloadInfos);
        }
        return AjaxResult.error("下载失败");
    }







}
