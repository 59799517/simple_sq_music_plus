package com.sqmusicplus.v3.plug.qqvip.entity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sqmusicplus.v3.base.entity.vo.Music;
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
        try {
            JSONObject mapper1 = null;
            try {
                mapper1 = jsonObject.getJSONObject("songinfo").getJSONObject("data").getJSONObject("track_info");
            } catch (Exception e) {
                log.error("QQVIP歌曲信息转换失败：{}------------------------>配置：{}",jsonObject.toJSONString(),JSONObject.toJSON(qqConfig));
                e.printStackTrace();
                return null;
            }
            String name = mapper1.getString("name");
            String mid = mapper1.getString("mid");
            String albumid = mapper1.getJSONObject("album").getString("mid");
            String albumname = mapper1.getJSONObject("album").getString("name");
            String albumpmid = mapper1.getJSONObject("album").getString("pmid");
            String albumImageconfig = qqConfig.getAlbumImage();
            String albumImage = albumImageconfig.replaceAll("#\\{pmid}", albumpmid);
            List<String> artistIds = new ArrayList<>();
            List<String> artistNames = new ArrayList<>();
            try {
                JSONArray jsonArray = mapper1.getJSONArray("singer");
                for (Object o : jsonArray) {
                    JSONObject jdata = JSONObject.parseObject(JSONObject.toJSONString(o));
                    artistIds.add(jdata.getString("mid"));
                    artistNames.add(jdata.getString("name"));
                }
            } catch (Exception e) {
                log.error("QQVIP歌曲信息转换失败：{}------------------------>配置：{}",jsonObject.toJSONString(),JSONObject.toJSON(qqConfig));
                e.printStackTrace();
                artistIds.add("0");
            }

//            JSONObject filemapper = mapper1.getJSONObject("file");
//            String mediaMid = filemapper.getString("media_mid");
            String lyricResult = toPlugLyricResult(mid,qqConfig);
//            if (StringUtils.isNotBlank(mediaMid)) {
//                mid  = mid+","+mediaMid;
//            }
            Music music = new Music().setId(mid)
                    .setMusicImage(albumImage)
                    .setMusicLyric(lyricResult)
                    .setMusicAlbum(albumname)
                    .setMusicArtists(artistNames)
                    .setMusicName(name)
                    .setDataInfo(mapper1)
                    .setAlbumId(albumid)
                    .setArtistsIds(artistIds);
            return  music;
        } catch (Exception e) {
            log.error("QQVIP歌曲信息转换失败：{}------------------------>配置：{}",jsonObject.toJSONString(),JSONObject.toJSON(qqConfig));
            e.printStackTrace();
            return null;
        }
    }
}
