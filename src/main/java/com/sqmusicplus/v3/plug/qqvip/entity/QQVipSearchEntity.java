package com.sqmusicplus.v3.plug.qqvip.entity;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.sqmusicplus.v3.base.enums.PlugBrType;
import com.sqmusicplus.v3.plug.entity.Music;
import com.sqmusicplus.v3.plug.qq.config.QQConfig;
import com.sqmusicplus.v3.plug.qq.entity.QQSearchEntity;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @Classname QQVipSearchEntity
 * @Description QQVIP的实体转化
 * @Version 1.0.0
 * @Date 2024/7/3 17:27
 * @Created by SQ
 */
@Slf4j
public class QQVipSearchEntity extends QQSearchEntity {
    @Override
    public Music songInfoToMusic(JSONObject jsonObject, QQConfig qqConfig) {

        JSONObject track_info = jsonObject.getJSONObject("songinfo").getJSONObject("data").getJSONObject("track_info");

        String name = track_info.getString("name");
        String mid = track_info.getString("mid");
        String albumid = track_info.getJSONObject("album").getString("mid");
        String albumname = track_info.getJSONObject("album").getString("name");
        String albumpmid = track_info.getJSONObject("album").getString("pmid");
        String albumImageconfig = qqConfig.getAlbumImage();
        String albumImage = albumImageconfig.replaceAll("#\\{pmid}", albumpmid);
        JSONArray jsonArray = track_info.getJSONArray("singer");
        ArrayList<String> singerNames = new ArrayList<>();
        ArrayList<String> singerIds = new ArrayList<>();
        jsonArray.forEach(jdata -> {
            JSONObject e = JSONObject.parseObject(JSONObject.toJSONString(jdata));
            singerNames.add(e.getString("name"));
            singerIds.add(e.getString("mid"));
        });

        Long flac = track_info.getJSONObject("file").getLong("size_flac");
        Long mp3320 = track_info.getJSONObject("file").getLong("size_320mp3");
        Long mp3128 = track_info.getJSONObject("file").getLong("size_128mp3");
//        String mediaMid = mapper1.getMapper("file").getString("media_mid");
        ArrayList<PlugBrType> longs = new ArrayList<>();
        if (flac != null&&flac.longValue()>0){
            longs.add(PlugBrType.QQVIP_Flac_2000);
        }
        if (mp3320 != null&&mp3320.longValue()>0){
            longs.add(PlugBrType.QQVIP_MP3_320);
        }
        if (mp3128 != null&&mp3128.longValue()>0){
            longs.add(PlugBrType.QQVIP_MP3_128);
        }

        String lyricResult = toPlugLyricResult(mid,qqConfig);
        Music music = new Music().setId(mid)
                .setMusicImage(albumImage)
                .setMusicLyric(lyricResult)
                .setMusicAlbum(albumname)
                .setMusicArtists(singerNames)
                .setMusicName(name)
                .setDataInfo(track_info)
                .setAlbumId(albumid)
                .setArtistsIds(singerIds)
                .setBits(longs)
                .setMusicDuration(track_info.getInteger("interval") * 1000L);
        return  music;
    }
}
