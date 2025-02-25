package com.sqmusicplus.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ejlchina.data.Array;
import com.ejlchina.data.Mapper;
import com.ejlchina.okhttps.HTTP;
import com.sqmusicplus.base.entity.DownloadEntity;
import com.sqmusicplus.base.entity.DownloadInfo;
import com.sqmusicplus.base.entity.Music;
import com.sqmusicplus.base.entity.SqConfig;
import com.sqmusicplus.base.service.DownloadInfoService;
import com.sqmusicplus.base.service.SqConfigService;
import com.sqmusicplus.plug.base.PlugBrType;
import com.sqmusicplus.plug.qqvip.QQvipHander;
import com.sqmusicplus.plug.utils.FreeCookieUtil;
import com.sqmusicplus.utils.DownloadUtils;
import com.sqmusicplus.utils.MusicUtils;
import com.sqmusicplus.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Classname ScanQQVIPLikeMusic
 * @Description 扫描QQVIP喜欢音乐
 * @Version 1.0.0
 * @Date 2024/7/31 10:54
 * @Created by SQ
 */
@Slf4j
@Component
public class ScanQQVIPLikeMusicTask {

    @Autowired
    private SqConfigService configService;

    @Autowired
    private QQvipHander qqvipHander;

    @Autowired
    private DownloadInfoService downloadInfoService;
    @Scheduled(cron="0 0/10 * * * ? ")
    public void excute(){
        SqConfig qqopenconfigKey = configService.getOne(new QueryWrapper<SqConfig>().eq("config_key", "plug.qqvip.open"));

        SqConfig syncConfig = configService.getOne(new QueryWrapper<SqConfig>().eq("config_key", "plug.qqvip.sync"));

        if (qqopenconfigKey!=null&&Boolean.parseBoolean(qqopenconfigKey.getConfigValue())&&Boolean.parseBoolean(syncConfig.getConfigValue())){


            SqConfig urlconfig = configService.getOne(new QueryWrapper<SqConfig>().eq(SqConfig.COL_CONFIG_KEY, "plug.qqvip.baseurl"));
            SqConfig qqconfig = configService.getOne(new QueryWrapper<SqConfig>().eq(SqConfig.COL_CONFIG_KEY, "plug.qqvip.qq"));

            try {
                FreeCookieUtil.refreshCookies(qqconfig.getConfigValue(), urlconfig.getConfigValue());
            } catch (Exception e) {
                configService.update(new UpdateWrapper<SqConfig>().eq("config_key", "plug.qqvip.open").set("config_value", "false"));
                log.error("获取QQvip失败请检查ip和qq是否准确已自动关闭该插件");
            }


            //喜欢单曲
            SqConfig syncLikeSongConfig = configService.getOne(new QueryWrapper<SqConfig>().eq("config_key", "plug.qqvip.synclikesong"));
            if (Boolean.parseBoolean(syncLikeSongConfig.getConfigValue())){
                log.info("扫描QQVIP同步我喜欢单曲");
                syncLikeSong(qqconfig.getConfigValue(),urlconfig.getConfigValue());
            }
            //所有歌单
            SqConfig syncplaylist = configService.getOne(new QueryWrapper<SqConfig>().eq("config_key", "plug.qqvip.syncplaylist"));
            if (Boolean.parseBoolean(syncplaylist.getConfigValue())){
                log.info("扫描QQVIP同步所有歌单");
                syncplaylist(qqconfig.getConfigValue(),urlconfig.getConfigValue());
            }
            //专辑
            SqConfig synclikealbum = configService.getOne(new QueryWrapper<SqConfig>().eq("config_key", "plug.qqvip.synclikealbum"));
            if (Boolean.parseBoolean(synclikealbum.getConfigValue())){
                log.info("扫描QQVIP同步所有专辑");
                syncalbu(qqconfig.getConfigValue(),urlconfig.getConfigValue());
            }



        }
    }



    private void syncalbu(String qq,String url){
        HTTP http = DownloadUtils.getHttp();
        Mapper mapper = http.sync(url + "/user/collect/album?id=" + qq+"&pageSize=100").addHeader("Cookie", FreeCookieUtil.getCookieStr()).addHeader("Accept", "application/xml;version=1").get().getBody().toMapper();
        int result = mapper.getInt("result");
        if (result == 100){
            Mapper data = mapper.getMapper("data");
            Array list = data.getArray("list");
            ArrayList<String> exclude = new ArrayList<>();
            ArrayList<String> albummids = new ArrayList<>();
            list.forEach((index, item) -> {
                String albummid = item.toMapper().getString("albummid");
                albummids.add(albummid);
            });
            String allalbummids = StringUtils.join(albummids, ",");
            SqConfig configKey = configService.getOne(new QueryWrapper<SqConfig>().eq("config_key", "plug.qqvip.likeAlubids"));
            if (configKey!=null){
                String configValue = configKey.getConfigValue();
                //已经下载的
                String[] split = configValue.split(",");
                if (split!=null&&split.length>0){
                    for (String s : split) {
                        exclude.add(s);
                    }
                }
                configService.update(new UpdateWrapper<SqConfig>().eq("config_key", "plug.qqvip.likeAlubids").set("config_value",allalbummids));
            }else{
                SqConfig sqConfig = new SqConfig();
                sqConfig.setConfigKey("plug.qqvip.likeAlubids");
                sqConfig.setConfigValue(allalbummids);
                sqConfig.setConfigName("QQVIP已经同步过的专辑id（删除则重新同步一次）");
                sqConfig.setType("input");
                sqConfig.setConfigShow("N");
                configService.save(sqConfig);

            }

            list.forEach((index, item) -> {
                String albummid = item.toMapper().getString("albummid");
                String singername = item.toMapper().getString("singername");
                String albumname = item.toMapper().getString("albumname");
                if (!exclude.contains(albummid)){
                    ArrayList<DownloadEntity> downloadEntities = qqvipHander.downloadAlbum(albummid, PlugBrType.QQVIP_Flac_2000, null, singername, false, albumname);
                    List<DownloadInfo> downloadInfos = MusicUtils.downloadEntitytoDownloadInfoTo(downloadEntities);
                    downloadInfoService.add(downloadInfos);
                }

            });
        }

    }

    private void syncplaylist(String qq , String url){
        ArrayList<String> excludeNames = new ArrayList<>();
        //不同步的歌单名称
        SqConfig syncplaylistexclude = configService.getOne(new QueryWrapper<SqConfig>().eq("config_key", "plug.qqvip.syncplaylistexclude"));
        if (StringUtils.isNotBlank(syncplaylistexclude.getConfigValue())) {
            String[] split = syncplaylistexclude.getConfigValue().split(",");
            if (split!=null&&split.length>0) {
                for (String s : split) {
                    excludeNames.add(s.trim());
                }

            }
        }
        HTTP http = DownloadUtils.getHttp();
        Mapper mapper = http.sync(url + "/user/songlist?id=" + qq).addHeader("Cookie", FreeCookieUtil.getCookieStr()).get().getBody().toMapper();
        int result = mapper.getInt("result");
        if (result == 100){
            Mapper data = mapper.getMapper("data");
            Array list = data.getArray("list");
            list.forEach((index, item) -> {
                int dirid = item.toMapper().getInt("dir_show");
                if(dirid == 1){
                    String diss_name = item.toMapper().getString("diss_name").trim();

                    if (!excludeNames.contains(diss_name)){
                        log.info("同步我创建的歌单{}的歌曲共找到{}首",diss_name,item.toMapper().getLong("song_cnt"));
                        //歌单id
                        String string = item.toMapper().getString("tid");
                        syncsonglist(url,string);
                    }else{
                        log.info("同步我创建的歌单{}设置跳过不进行同步",diss_name);
                    }
                }
            });
        }else {
            log.info("QQVIP同步喜欢音乐失败获取歌单失败");
        }

        Mapper collectmapper = http.sync(url + "/user/collect/songlist?id="+ qq+"&pageSize=100").addHeader("Cookie", FreeCookieUtil.getCookieStr()).get().getBody().toMapper();
        int collectresult = collectmapper.getInt("result");
        if (collectresult == 100){
            Mapper collectdata = collectmapper.getMapper("data");
            Array list = collectdata.getArray("list");
            if (list==null|| list.isEmpty()){
                log.info("QQVIP同步收藏歌单失败获取歌单失败(未找到歌曲信息)");
                return;
            }
            list.forEach((index, item) -> {
                    String diss_name = item.toMapper().getString("dissname");
                    if (!excludeNames.contains(diss_name)){
                        log.info("同步我收藏的的歌单{}的歌曲共找到{}首",diss_name,item.toMapper().getLong("songnum"));
                        //歌单id
                        String string = item.toMapper().getString("dissid");
                        syncsonglist(url,string);
                    }else{
                        log.info("同步我收藏的歌单{}设置跳过不进行同步",diss_name);
                    }

            });

        }else{
            log.info("QQVIP同步收藏歌单失败失败");
        }





    }


    /**
     * 我喜欢的歌单（仅仅是我喜欢的单曲）
     * @param qq
     * @param url
     */
    private void syncLikeSong(String qq , String url){
        HTTP http = DownloadUtils.getHttp();
        Mapper mapper = http.sync(url + "/user/songlist?id=" + qq).addHeader("Cookie", FreeCookieUtil.getCookieStr()).get().getBody().toMapper();
        int result = mapper.getInt("result");
        if (result == 100){
            Mapper data = mapper.getMapper("data");
            Array list = data.getArray("list");

            list.forEach((index, item) -> {
                int dirid = item.toMapper().getInt("dirid");
                if(dirid == 201){
                    log.info("同步我喜欢的单曲共找到{}首",item.toMapper().getLong("song_cnt"));
                    //歌单id
                    String string = item.toMapper().getString("tid");
                    syncsonglist(url,string);
                }
            });
        }else {
            log.info("QQVIP同步喜欢音乐失败获取歌单失败");
        }
    }

    /**
     * 根据歌单id获取歌曲并添加到下载列表
     * @param url
     * @param tid
     */
    private void syncsonglist(String url,String tid){
        HTTP http = DownloadUtils.getHttp();
        Mapper mapper = http.sync(url + "/songlist?id=" + tid).addHeader("Cookie", FreeCookieUtil.getCookieStr()).get().getBody().toMapper();
        int result = mapper.getInt("result");
        if (result == 100){
            Mapper data = mapper.getMapper("data");
            //本次包含的所有歌曲id
            String string = data.getString("songids");
            //歌单名称
            String dissname = data.getString("dissname");

            List<String> soutIdList = Arrays.asList(string.split(","));
            ArrayList<String> addSongIds = new ArrayList<>();

            SqConfig configKey = configService.getOne(new QueryWrapper<SqConfig>().eq("config_key", "plug.qqvip.songlistid." + tid));
            if (configKey!=null){
                //获取上次同步的歌单id
                String configValue = configKey.getConfigValue();
                List<String> targetList = Arrays.asList(configValue.split(","));
                //通过songids和上次同步的歌单id对比，获取新增的歌单id 使用流处理
                ArrayList<String> finalAddSongIds = addSongIds;
                soutIdList.stream().filter(item -> !targetList.contains(item)).forEach(item -> {
                    finalAddSongIds.add(item);
                });
                addSongIds = finalAddSongIds;
                configService.update(new UpdateWrapper<SqConfig>().eq("config_key", "plug.qqvip.songlistid." + tid).set("config_value", string));

            }else{
                addSongIds = new ArrayList<>(soutIdList);
                SqConfig sqConfig = new SqConfig();
                sqConfig.setConfigKey("plug.qqvip.songlistid." + tid);
                sqConfig.setConfigValue(string);
                sqConfig.setType("input");
                sqConfig.setConfigName("qqvip("+dissname+")歌单已下载id缓存 删除则回重新同步数据");
                sqConfig.setConfigShow("N");
                configService.save(sqConfig);
            }
            //循环找出歌曲详情
            Array songlist = data.getArray("songlist");
            ArrayList<String> finalAddSongIds1 = addSongIds;
            ArrayList<DownloadInfo> downloadInfos = new ArrayList<>();
            songlist.forEach((index, item) -> {
                String songid = item.toMapper().getString("songid");
                if (finalAddSongIds1.contains(songid)){
                    String songmid = item.toMapper().getString("songmid");
                    String strMediaMid = item.toMapper().getString("strMediaMid");

                    int sizeflac = item.toMapper().getInt("sizeflac");
                    int size320 = item.toMapper().getInt("size320");
                    int size128 = item.toMapper().getInt("size128");
                    PlugBrType brType = PlugBrType.QQVIP_Flac_2000;
                    if (sizeflac > 0){
                        brType = PlugBrType.QQVIP_Flac_2000;
                    }else if (size320>0){
                        brType = PlugBrType.QQVIP_MP3_320;
                    }else if (size128>0){
                        brType = PlugBrType.QQVIP_MP3_128;
                    }
                    Music music = qqvipHander.querySongById(songmid);

                    DownloadEntity downloadEntity = qqvipHander.downloadSong(music, brType, null);
                    DownloadInfo downloadInfo = MusicUtils.downloadEntitytoDownloadInfoTo(downloadEntity);

                    downloadInfos.add(downloadInfo);
                }
            });
            log.info("QQVIP同步歌单{}需要同步{}首",dissname,downloadInfos.size());

            if (!downloadInfos.isEmpty()){
                downloadInfoService.add(downloadInfos);
            }
            //插入数据库
        }
    }



}




