package com.sqmusicplus.v3.plug.base.hander;

import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import com.alibaba.fastjson2.JSONObject;
import com.sqmusicplus.v3.base.entity.DownloadInfo;
import com.sqmusicplus.v3.plug.entity.Album;
import com.sqmusicplus.v3.plug.entity.Artists;
import com.sqmusicplus.v3.plug.entity.Music;
import com.sqmusicplus.v3.base.enums.DbBooleanConvert;
import com.sqmusicplus.v3.base.enums.PlugBrType;
import com.sqmusicplus.v3.base.enums.SetConfigEnum;
import com.sqmusicplus.v3.base.service.DownloadInfoService;
import com.sqmusicplus.v3.config.SqConfigCache;
import com.sqmusicplus.v3.download.DownloadStatus;
import com.sqmusicplus.v3.download.vo.DownloadUrlResult;
import com.sqmusicplus.v3.plug.entity.PlugSearchMusicResult;
import com.sqmusicplus.v3.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.Serializable;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created with IntelliJ IDEA.
 * User: SQ
 * Date: 2022/11/21
 * Time: 17:48
 * Description: 搜索处理器抽象类
 */
@Service
@Slf4j
public abstract class SearchHanderAbstract implements SearchHander, Serializable {



    @Autowired
    private DownloadInfoService downloadInfoService;

    public DownloadInfoService getDownloadInfoService() {
        return downloadInfoService;
    }


//    @Override
//    public List<String> searchTip(String searchKey) {
//        //搂底使用酷我的
//
//    }

    @Override
    public void dnonloadAndSaveToFile(DownloadInfo downloadInfo, SearchHander searchHander) {
        try {
            Music music = searchHander.querySongById(downloadInfo);
            if (music == null) {
                throw new RuntimeException("下载失败歌曲信息不完整歌曲详情转化歌曲失败:" + JSONObject.toJSONString(downloadInfo));
            }
            String baseMusicName_temp = music.getMusicName().trim();
            String baseMusicArtistName_temp = music.getMusicArtists().stream().map(String::trim).limit(7).collect(Collectors.joining("&"));
            String baseMusicAlbumName_temp = music.getMusicAlbum().trim();
            String baseMusicMainArtistName_temp =  "群星";
            if (StringUtils.isNotBlank( music.getMusicArtists().get(0).trim())){
                baseMusicMainArtistName_temp =  music.getMusicArtists().get(0).trim();
            }


            try {
                String open_symbol_remove = SqConfigCache.getSqConfigValue(SetConfigEnum.SYSTEM_START_FILE_AND_FOLDER_SPECIAL_SYMBOL_REMOVE);
                if (Boolean.valueOf(open_symbol_remove)){
                    String open_symbol_remove_symbol = SqConfigCache.getSqConfigValue(SetConfigEnum.SYSTEM_START_FILE_AND_FOLDER_SPECIAL_SYMBOL_REMOVE_SYMBOL);
                    //移除特殊字符
                    if (StringUtils.isNotBlank(open_symbol_remove_symbol)) {
                        char[] chars = open_symbol_remove_symbol.toCharArray();
                        for (char c : chars) {
                            if (c != ' ') {
                                // 转义特殊字符以避免正则表达式问题
                                String escapedSymbol = Pattern.quote(String.valueOf(c));
                                baseMusicName_temp = baseMusicName_temp.replaceAll(escapedSymbol, "");
                                baseMusicArtistName_temp = baseMusicArtistName_temp.replaceAll(escapedSymbol, "");
                                baseMusicAlbumName_temp = baseMusicAlbumName_temp.replaceAll(escapedSymbol, "");
                            }
                        }
                    }
                }
            } catch (Exception e) {
               log.error("歌曲信息移除特殊字符失败",e);
            }
            final String baseMusicName = baseMusicName_temp;;
            //如果歌手超过7个则只取前7个
            final String baseMusicArtistName = baseMusicArtistName_temp;
//            final String baseMusicArtistName = music.getMusicArtists().stream().map(String::trim).collect(Collectors.joining("&"));
            final String baseMusicAlbumName = baseMusicAlbumName_temp;
            final String baseMusicMainArtistName = baseMusicMainArtistName_temp;

            String baseAlbumID = music.getAlbumId();
            List<String> baseArtistsID = music.getArtistsIds();
            boolean isAudioBook = DbBooleanConvert.findByValue(downloadInfo.getAudioBook());

            String musicPath = SqConfigCache.getSqConfigValue(SetConfigEnum.SYSTEM_DOWNLOAD_PATH);
            //数据库下载路径
            File file = new File(musicPath);

            HashMap<String, Object> pathTemplate = new HashMap<>();
            String music_path_template = SqConfigCache.getSqConfigValue(SetConfigEnum.SYSTEM_DOWNLOAD_FILE_TEMPLATE);
            if (StringUtils.isBlank(music_path_template)){
                music_path_template="${musicName} - ${artists}";
            }

            String artistsId = baseArtistsID.stream().map(String::trim).limit(7).collect(Collectors.joining("&"));
            pathTemplate.put("musicName", baseMusicName);
            pathTemplate.put("artists", baseMusicArtistName);
            pathTemplate.put("artist", baseMusicMainArtistName); //新增主要歌手
            pathTemplate.put("album", baseMusicAlbumName);
            pathTemplate.put("albumId", baseAlbumID);
            pathTemplate.put("artistsId", artistsId);
            String fileName = "";
            try {
                fileName = SpelTemplateUtils.formatTemplateWithDollar(music_path_template, pathTemplate);
            }catch (Exception e){
                fileName = SpelTemplateUtils.formatTemplateWithDollar("${musicName} - ${artists}", pathTemplate);
            }

            //拼接当前路径  歌手/专辑
            String basepath = baseMusicMainArtistName + File.separator + baseMusicAlbumName + File.separator;
            //获取当前文件后缀
            String brType = downloadInfo.getDownloadBrType();
            PlugBrType byId = PlugBrType.findById(brType);
            File type = new File(file, basepath + fileName + "." + byId.getType());
            //过滤掉不下载的格式歌曲
            String downloadFormat = SqConfigCache.getSqConfigValue(SetConfigEnum.SYSTEM_DOWNLOAD_FILE_AUDIO_FORMAT);
            if (!downloadFormat.equals("auto")){
                if (downloadFormat.equals(byId.getType())){
                    log.info("歌曲{}---->因为设置忽略下载此格式音乐{}", baseMusicName, byId.getType());
                    throw new RuntimeException("歌曲"+baseMusicName+"---->因为设置忽略下载此格式音乐("+byId.getType()+"):" + JSONObject.toJSONString(downloadInfo));
                }
            }
            log.debug("开始下载---->{}", baseMusicName);
            //创建任务
            String sqConfigValue = SqConfigCache.getSqConfigValue(SetConfigEnum.SYSTEM_FILE_EXIST_NOT_DOWNLOAD);
            if (Boolean.valueOf(sqConfigValue)) {
                if (type.exists()) {
                    log.info("歌曲{}---->已存在不下载", baseMusicName);
                    return;
                }
            }
            //获取下载链接
            DownloadUrlResult downloadUrlResult = searchHander.getDownloadUrl(downloadInfo);
            if (downloadUrlResult == null || StringUtils.isEmpty(downloadUrlResult.getUrl())) {
                try {
                    throw new RuntimeException(downloadInfo.getDownloadMusicname() + "(未获取到播放链接)下载失败:" + downloadUrlResult.getErrorMsg());
                } catch (RuntimeException e) {
                    throw new RuntimeException(downloadInfo.getDownloadMusicname() + "(未获取到播放链接)下载失败:" + e.getMessage());
                }
            }

            DownloadUtils.download(downloadUrlResult.getUrl(), type, onProcess->{
                log.debug("歌曲：{} 进度：{} , byte信息：{}/{}",music.getMusicName(),onProcess.getProgress(),onProcess.getBytesRead(),onProcess.getTotalBytes());
            },onSuccess ->
            {
                log.debug("歌曲：{} 文件下载完成处理后续步骤",music.getMusicName());
            }, onFailure -> {
                onFailure.printStackTrace();
                log.debug("下载失败(文件写入异常){}", music.getMusicName());
                throw new RuntimeException("下载失败:" + music.getMusicName());
            },onComplete -> {
                Artists artists = searchHander.queryArtistById(baseArtistsID.get(0));
//                String getSearheads = "";
//                try {
//                    getSearheads = ReflectUtil.invoke(searchHander.getConfig(), "getSearheads");
//                } catch (Exception ignored) {
//                }
                //歌手图片地址
                String downloadurl = artists.getMusicArtistsPhoto();
                //歌手图片保存路径
                String downliadpath = musicPath + File.separator + baseMusicMainArtistName;
                //人物图片
                File Artistsfile = FileUtils.findFile(downliadpath + File.separator, "cover");

                if (Artistsfile == null || (!Artistsfile.exists() && !isAudioBook)) {
                    try {
                        DownloadUtils.download(downloadurl, downliadpath,onProcess->{
                            log.debug("歌曲歌手图片：{} 进度：{} , byte信息：{}/{}",music.getMusicName(),onProcess.getProgress(),onProcess.getBytesRead(),onProcess.getTotalBytes());
                        },onSuccess ->
                        {
                            log.debug("歌曲歌手图片：{} 文件下载完成处理后续步骤",music.getMusicName());
                        },onFailure -> {
                            onFailure.printStackTrace();
                            log.debug("歌曲歌手图片下载失败：{}", music.getMusicName());
                        }, onArtistsPhoto -> {
                            try {
                                String suffix = FileTypeUtil.getType(onArtistsPhoto);
                                FileUtil.copy(onArtistsPhoto, new File(downliadpath + File.separator + "cover." + suffix), false);
                                FileUtil.copy(onArtistsPhoto, new File(downliadpath + File.separator + "artist." + suffix), false);
                                //取出文件名后缀
                                FileUtil.copy(onArtistsPhoto, new File(downliadpath + File.separator + "folder." + suffix), true);
                            } catch (Exception e) {
                                SafeFileUtil.safeDelete(onArtistsPhoto);
                            } finally {
                                try {
                                    File parentFile = onArtistsPhoto.getParentFile();
                                    SafeFileUtil.safeDelete(onArtistsPhoto);
                                    boolean dirEmpty = FileUtil.isDirEmpty(parentFile);
                                    if (dirEmpty) {
                                        SafeFileUtil.safeDelete(parentFile);
                                    }

                                } catch (IORuntimeException ignored) {
                                    ignored.printStackTrace();
                                }
                            }
                            artists.setMusicArtistsPhoto("cover");
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                //专辑图片
                Album album = searchHander.queryAlbumById(baseAlbumID.toString());
                String albumImg = album.getAlbumImg();
                if (isAudioBook) {
                    album.setAlbumName(downloadInfo.getDownloadAlbumname());
                }
                Boolean downloadalubimage = true;
                if (StringUtils.isEmpty(albumImg)) {
                    downloadalubimage = false;
                }
                String imagePath = musicPath + File.separator + baseMusicMainArtistName + File.separator + baseMusicAlbumName;
                if (StringUtils.isEmpty(baseMusicAlbumName) && baseMusicAlbumName.equals("other")) {
                    String suffix = FileTypeUtil.getType(Artistsfile);
                    FileUtil.copy(Artistsfile, new File(imagePath + File.separator + "cover." + suffix), true);
                }
                File albumfile = FileUtils.findFile(imagePath + File.separator, "cover");
                //专辑图片下载与标签写入
                if (albumfile == null || (!albumfile.exists() && downloadalubimage)) {
                    try {
                        DownloadUtils.download(albumImg, imagePath, onProcess->{
                            log.debug("歌曲专辑图片：{} 进度：{} , byte信息：{}/{}",music.getMusicName(),onProcess.getProgress(),onProcess.getBytesRead(),onProcess.getTotalBytes());
                        },onSuccess ->
                        {
                            log.debug("歌曲专辑图片：{} 文件下载完成处理后续步骤",music.getMusicName());
                        },onFailure -> {
                            onFailure.printStackTrace();
                            log.debug("歌曲专辑图片下载失败：{}", music.getMusicName());
                        },onAlbumImg -> {
                            File cover = null;
                            try {
                                String suffix = FileTypeUtil.getType(onAlbumImg);
                                onAlbumImg = FileUtil.rename(onAlbumImg, "cover." + suffix, true);
                                FileUtil.copy(onAlbumImg, new File(imagePath + File.separator + "album." + suffix), true);
                                if (isAudioBook) {
                                    FileUtil.copyFile(cover, Artistsfile);
                                }
                            } catch (Exception e) {
                                SafeFileUtil.safeDelete(onAlbumImg);
                            } finally {
                                try {
                                    File parentFile = onAlbumImg.getParentFile();
                                    boolean dirEmpty = FileUtil.isDirEmpty(parentFile);
                                    if (dirEmpty) {
                                        SafeFileUtil.safeDelete(parentFile);
                                    }
                                } catch (IORuntimeException ignored) {

                                }
                            }
                            album.setAlbumImg("cover");
                            extracted(music, onComplete, onAlbumImg, downloadInfo);
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.debug("下载专辑封面失败：{}", downloadInfo.getDownloadMusicname());
                        extracted(music, onComplete, albumfile, downloadInfo);
                    }
                } else {
                    extracted(music, onComplete, albumfile, downloadInfo);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            log.debug("下载失败{}", downloadInfo.getDownloadMusicname());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void dnonloadAndSaveToFile(DownloadInfo downloadInfo, Object searchHander) {
        this.dnonloadAndSaveToFile(downloadInfo, (SearchHander) searchHander);
    }

    @Override
    public DownloadInfo musicToDownloadInfo(Music music, PlugBrType brType, Boolean isAudioBook) {
        List<PlugBrType> bits = music.getBits();
        if (brType==null){
            brType = MusicUtils.getMaxBr(bits);
        }
        String bitsStr = bits.stream().map(plugBrType -> plugBrType.getBit().toString()).collect(Collectors.joining(","));
        String plugBrTypes = bits.stream().map(plugBrType -> plugBrType.getId()).collect(Collectors.joining(","));

        return new DownloadInfo()
                .setDownloadGid(music.getId())
                .setDownloadTime(new Date())
                .setDownloadFile(music.getMusicName()+" - "+String.join("&", music.getMusicArtists()))
                .setDownloadMusicId(music.getId())
                .setDownloadPlugName(brType.getPlugName())
                .setDownloadBrType(brType.getId())
                .setDownloadMusicname(music.getMusicName())
                .setDownloadArtistname(String.join("&", music.getMusicArtists()))
                .setDownloadAlbumname(music.getMusicAlbum())
                .setDownloadMusicInfo(music.getDataInfo().toJSONString())
                .setDownloadStatus(DownloadStatus.waiting.getValue())
                .setSpringName(brType.getSpringName())
                .setAudioBook(isAudioBook? DbBooleanConvert.YES.getValue():DbBooleanConvert.NO.getValue())
                .setDownloadUpdateTime(new Date())
                .setRewriteMp3tag(DbBooleanConvert.YES.getValue())
                .setDownloadBits(bitsStr)
                .setDownloadBrTypes(plugBrTypes);
    }

    @Override
    public DownloadInfo musicToDownloadInfo(PlugSearchMusicResult music, PlugBrType brType, Boolean isAudioBook) {
        List<PlugBrType> bits = music.getBrTypes();
        if (brType==null){
            brType = MusicUtils.getMaxBr(bits);
        }
        String bitsStr = bits.stream().map(plugBrType -> plugBrType.getBit().toString()).collect(Collectors.joining(","));
        String plugBrTypes = bits.stream().map(plugBrType -> plugBrType.getId()).collect(Collectors.joining(","));
        String jsonString ="";
        try {
            jsonString = music.getDataInfo().toJSONString();
        } catch (Exception e) {

        }
        return new DownloadInfo()
                .setDownloadGid(music.getId())
                .setDownloadTime(new Date())
                .setDownloadFile(music.getName()+" - "+String.join("&", music.getArtistName()))
                .setDownloadMusicId(music.getId())
                .setDownloadPlugName(brType.getPlugName())
                .setDownloadBrType(brType.getId())
                .setDownloadMusicname(music.getName())
                .setDownloadArtistname(String.join("&", music.getArtistName()))
                .setDownloadAlbumname(music.getAlbumName())
                .setDownloadMusicInfo(jsonString)
                .setDownloadStatus(DownloadStatus.waiting.getValue())
                .setSpringName(brType.getSpringName())
                .setAudioBook(isAudioBook? DbBooleanConvert.YES.getValue():DbBooleanConvert.NO.getValue())
                .setDownloadUpdateTime(new Date())
                .setRewriteMp3tag(DbBooleanConvert.YES.getValue())
                .setDownloadBits(bitsStr)
                .setDownloadBrTypes(plugBrTypes);
    }

    /**
     * 根据歌曲情况写入到歌曲标签
     *
     * @param music
     * @param onSuccess
     * @param albumfile
     * @param downloadInfo
     */
    private void extracted(Music music, File onSuccess, File albumfile, DownloadInfo downloadInfo) {
        //是否需要标签
        Integer rewriteMp3tag = downloadInfo.getRewriteMp3tag();


        //创建歌词
        try {

            if (StringUtils.isNotEmpty(music.getMusicLyric())) {
                String name = FileUtil.getPrefix(onSuccess);
                log.debug("lrc地址{}", onSuccess.getParentFile() + File.separator + name + ".lrc");
                FileUtil.writeBytes(music.getMusicLyric().getBytes(), onSuccess.getParentFile() + File.separator + name + ".lrc");
            }
        } catch (IORuntimeException e) {
            log.error(e.getMessage());
        }
        //修改文件
        try {
            if (DbBooleanConvert.findByValue(rewriteMp3tag)) {
                MusicUtils.setMediaFileInfo(onSuccess, music.getMusicName(), music.getMusicAlbum(), String.join(";", music.getMusicArtists()), "SqMusic", music.getMusicLyric(), albumfile,music.getMusicArtists().get(0));
                log.debug("标签写入成功{}", music.getMusicName());
            }

        } catch (Exception e) {
            log.debug("下载错误（标签写入错误）{}  ----------> {}", music.getMusicName(), e.getMessage());
            log.error(e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("下载失败（标签写入错误）:" + downloadInfo.getDownloadMusicname() + "------->" + e.getMessage());
        }
    }

    @Override
    public DownloadInfo downloadInfoToDbCheck(DownloadInfo downloadInfo) {
        String trim4 = downloadInfo.getDownloadMusicname().trim();
        if (StringUtils.isBlank(trim4)) {
            throw new RuntimeException("歌曲校验失败：歌曲名称为空");
        } else {
            String s = trim4.replaceAll("<[^>]*>", "");
            downloadInfo.setDownloadMusicname(s);
        }

        String downloadMusicId = downloadInfo.getDownloadMusicId().trim();
        if (StringUtils.isBlank(downloadMusicId)) {
            throw new RuntimeException("歌曲校验失败：歌曲ID为空");
        }
        downloadInfo.setDownloadMusicId(downloadMusicId);

        String downloadArtistname = downloadInfo.getDownloadArtistname().trim();
        if (StringUtils.isBlank(downloadArtistname)) {
            throw new RuntimeException("歌曲校验失败：歌手ID为空");
        }
        downloadInfo.setDownloadArtistname(downloadArtistname);

        String downloadAlbumname = downloadInfo.getDownloadAlbumname().trim();
        if (StringUtils.isBlank(downloadAlbumname)) {
            throw new RuntimeException("歌曲校验失败：专辑ID为空");
        }
        String trim = downloadInfo.getDownloadBrType().trim();
        if (StringUtils.isBlank(trim)) {
            throw new RuntimeException("歌曲校验失败：歌曲br为空");
        }
        downloadInfo.setDownloadBrType(trim);

//        String trim1 = downloadInfo.getDownloadMusicInfo().trim();
//        if (StringUtils.isBlank(trim1)) {
//            throw new RuntimeException("歌曲校验失败：歌曲信息为空");
//        }
        String trim2 = downloadInfo.getDownloadPlugName().trim();
        if (StringUtils.isBlank(trim2)) {
            throw new RuntimeException("歌曲校验失败：歌曲插件名称为空");
        }
        String trim3 = downloadInfo.getSpringName().trim();
        if (StringUtils.isBlank(trim3)) {
            throw new RuntimeException("歌曲校验失败：找不到对应的歌曲下载处理器");
        }
        downloadInfo.setSpringName(trim3);
        Integer audioBook = downloadInfo.getAudioBook();
        if (audioBook == null) {
            throw new RuntimeException("歌曲校验失败：歌曲是否为有声为空");
        }
        Integer rewriteMp3tag = downloadInfo.getRewriteMp3tag();
        if (rewriteMp3tag == null) {
            throw new RuntimeException("歌曲校验失败：歌曲是否重写标签为空");
        }
        String downloadBits = downloadInfo.getDownloadBits().trim();
        if (StringUtils.isBlank(downloadBits)) {
            throw new RuntimeException("歌曲校验失败：歌曲支持码率为空");
        }
        String trim5 = downloadInfo.getDownloadBrTypes().trim();
        if (StringUtils.isBlank(trim5)) {
            throw new RuntimeException("歌曲校验失败：歌曲支持码率类型为空");
        }
        return downloadInfo;
    }

    @Override
    public Music musicIgnoreCheck(Music music){
        if (music==null){
            return null;
        }
        String id = music.getId().trim();

        if (StringUtils.isBlank(id)) {
            return null;
        }
        String ignoreMusicName = SqConfigCache.getSqConfigValue(SetConfigEnum.SYSTEM_IGNORE_ACCOMPANIMENT);
        if (StringUtils.isNotBlank(ignoreMusicName)&&Boolean.parseBoolean(ignoreMusicName)){
            String musicName = music.getMusicName().trim();
            musicName = musicName.replaceAll("（", "(").replaceAll("）", ")");
            ArrayList<String> strings = new ArrayList<>();
            strings.add("(伴奏)");
            strings.add("(录音)");
            strings.add("(录音带伴奏)");
            strings.add("(片段)");
            strings.add("(片段版)");
            for (String s : strings) {
                if (musicName.contains(s)) {
                    log.info("触发伴奏忽略音乐：{}", musicName);
                    return null;
                }
            }
        }
        String ignoreArtist = SqConfigCache.getSqConfigValue(SetConfigEnum.SYSTEM_SYNC_ARTISTS_EXCLUDE);
        List<String> musicArtists = music.getMusicArtists();
        String[] split = ignoreArtist.split("\\|");
        for (String musicArtist : musicArtists) {
            // 忽略的歌手
            for (String s : split) {
                if (StringUtils.isNotBlank(s)&&musicArtist.contains(s)) {
                    log.info("触发歌手忽略音乐：{}--->{}", music.getMusicName(),s);
                    return null;
                }
            }
        }
        //忽略专辑
        String ignoreAlbum = SqConfigCache.getSqConfigValue(SetConfigEnum.SYSTEM_SYNC_ALBUM_EXCLUDE);
        String musicAlbum = music.getMusicAlbum().trim();
        for (String s : ignoreAlbum.split("\\|")) {
            if (StringUtils.isNotBlank(s)&&musicAlbum.contains(s)) {
                log.info("触发专辑忽略音乐：{}--->{}", music.getMusicName(),s);
                return null;
            }
        }
        return music;
    }
    public DownloadInfo musicIgnoreCheck(DownloadInfo downloadInfo) {
        if (downloadInfo==null){
            return null;
        }
        String id = downloadInfo.getDownloadMusicId().trim();

        if (StringUtils.isBlank(id)) {
            return null;
        }
        String ignoreMusicName = SqConfigCache.getSqConfigValue(SetConfigEnum.SYSTEM_IGNORE_ACCOMPANIMENT);
        if (StringUtils.isNotBlank(ignoreMusicName)&&Boolean.parseBoolean(ignoreMusicName)){
            String musicName = downloadInfo.getDownloadMusicname().trim();
            musicName = musicName.replaceAll("（", "(").replaceAll("）", ")");
            ArrayList<String> strings = new ArrayList<>();
            strings.add("(伴奏)");
            strings.add("(录音)");
            strings.add("(录音带伴奏)");
            strings.add("(片段)");
            strings.add("(片段版)");
            for (String s : strings) {
                if (StringUtils.isNotBlank(s)&&musicName.contains(s)) {
                    log.info("触发伴奏忽略音乐：{}", musicName);
                    return null;
                }
            }
        }
        String ignoreArtist = SqConfigCache.getSqConfigValue(SetConfigEnum.SYSTEM_SYNC_ARTISTS_EXCLUDE);
        List<String> musicArtists = Arrays.stream(downloadInfo.getDownloadArtistname().split("&")).toList();
        String[] split = ignoreArtist.split("\\|");
        for (String musicArtist : musicArtists) {
            // 忽略的歌手
            for (String s : split) {
                if (StringUtils.isNotBlank(s)&&musicArtist.contains(s)) {
                    log.info("触发歌手忽略音乐：{}--->{}", downloadInfo.getDownloadMusicname(),s);
                    return null;
                }
            }
        }
        //忽略专辑
        String ignoreAlbum = SqConfigCache.getSqConfigValue(SetConfigEnum.SYSTEM_SYNC_ALBUM_EXCLUDE);
        String musicAlbum = downloadInfo.getDownloadAlbumname().trim();
        for (String s : ignoreAlbum.split("\\|")) {
            if (StringUtils.isNotBlank(s)&&
                    musicAlbum.contains(s)) {
                log.info("触发专辑忽略音乐：{}--->{}", downloadInfo.getDownloadMusicname(),s);
                return null;
            }
        }
        return downloadInfo;
    }


    public List<DownloadInfo> musicIgnoreCheck(List<DownloadInfo> downloadInfos) {
        ArrayList<DownloadInfo> downloadInfos1 = new ArrayList<>();
        downloadInfos.forEach(downloadInfo -> {
            DownloadInfo downloadInfo1 = musicIgnoreCheck(downloadInfo);
            if (downloadInfo1 != null) {
                downloadInfos1.add(downloadInfo1);
            }
        });
        return downloadInfos1;

    }

}