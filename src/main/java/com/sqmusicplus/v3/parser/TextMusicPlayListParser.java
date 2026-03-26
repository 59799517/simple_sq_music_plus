package com.sqmusicplus.v3.parser;

import com.sqmusicplus.v3.base.entity.SqConfig;
import com.sqmusicplus.v3.base.entity.vo.ParserEntity;
import com.sqmusicplus.v3.base.enums.DbBooleanConvert;
import com.sqmusicplus.v3.base.enums.SetConfigEnum;
import com.sqmusicplus.v3.config.SqConfigCache;
import com.sqmusicplus.v3.plug.base.hander.SearchHanderAbstract;
import com.sqmusicplus.v3.plug.entity.Music;
import com.sqmusicplus.v3.plug.entity.PlugSearchMusicResult;
import com.sqmusicplus.v3.plug.entity.PlugSearchResult;
import com.sqmusicplus.v3.plug.entity.SearchKeyData;
import com.sqmusicplus.v3.plug.kg.hander.KGHander;
import com.sqmusicplus.v3.plug.kw.hander.NKwSearchHander;
import com.sqmusicplus.v3.plug.netease.hander.NeteaseHander;
import com.sqmusicplus.v3.plug.qq.hander.QQHander;
import com.sqmusicplus.v3.plug.qqvip.QQvipHander;
import com.sqmusicplus.v3.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Classname TextMusicPlayListParser
 * @Description 文本类型歌单解析
 * @Version 1.0.0
 * @Date 2022/8/10 16:15
 * @Created by SQ
 */
@Slf4j
@Component("textParser")
public class TextMusicPlayListParser {

    @Autowired
    private NeteaseHander neteaseHander;
    @Autowired
    private QQvipHander qqvipHander;
    @Autowired
    private QQHander qqHander;
    @Autowired
    private KGHander kGHander;
    @Autowired
    private NKwSearchHander nKwHander;

    public List<ParserEntity> parser(String msg) throws IOException {
        String[] split = msg.split("\n");
        return Arrays.stream(split)
                .filter(s -> StringUtils.isNotBlank(s.trim())) // 过滤掉空字符串
                .map(m -> {
                    String[] sa = m.split("-");
                    try {
                        return new ParserEntity().setSongName(sa[0].trim()).setArtistsName(sa[1].trim());
                    } catch (ArrayIndexOutOfBoundsException e) {
                        return  new ParserEntity().setSongName(m.trim()).setArtistsName("");
                    }
                }).collect(Collectors.toList());

    }


    public List<ParserEntity> parserParserEntity(List<ParserEntity> parserEntities) throws IOException {
        //        酷我-网易-qqvip-酷狗-qq
        SqConfig sqConfig = SqConfigCache.getSqConfig(SetConfigEnum.PLUG_KW_OPEN);
        SqConfig netOpen = SqConfigCache.getSqConfig(SetConfigEnum.PLUG_NETEASE_OPEN);
        SqConfig qqvipOpen = SqConfigCache.getSqConfig(SetConfigEnum.PLUG_QQVIP_OPEN);
        SqConfig kgOpen = SqConfigCache.getSqConfig(SetConfigEnum.PLUG_KG_OPEN);
        for (ParserEntity parserEntity : parserEntities) {
            //每一个处理增加500 ms间隔
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
            }

            try {
                //组合搜索条件
                if (StringUtils.isBlank(parserEntity.getSongName())) {
                    parserEntity.setIsDetection(false);
                    continue;
                }
                String searchKey = parserEntity.getSongName().trim();
                if (StringUtils.isNotBlank(parserEntity.getArtistsName())){
                    // 如果艺术家名字包含分隔符（/、& 或 ;），则分割并取第一个非空的艺术家名
                    if (parserEntity.getArtistsName().contains("/") || parserEntity.getArtistsName().contains("&") || parserEntity.getArtistsName().contains(";")) {
                        String[] split = parserEntity.getArtistsName().split("[/&;]");
                        // 遍历分割结果，取第一个非空的值
                        for (String artist : split) {
                            if (StringUtils.isNotBlank(artist.trim())) {
                                parserEntity.setArtistsName(artist.trim());
                                break;
                            }
                        }
                    }
                    searchKey += " " + parserEntity.getArtistsName().trim();
                }
                SearchKeyData searchKeyData = new SearchKeyData();
                searchKeyData.setSearchkey(searchKey).setPageIndex(1).setPageSize(20);

                if (sqConfig.getConfigValue().equals(DbBooleanConvert.YES.getBooleanValue().toString())) {

                    PlugSearchResult<PlugSearchMusicResult> plugSearchMusicResultPlugSearchResult = nKwHander.querySongByName(searchKeyData);
                    //找出匹配的
                    extracted(parserEntity, plugSearchMusicResultPlugSearchResult);
                }


                if ( parserEntity.getIsDetection() == null || !parserEntity.getIsDetection()) {
                    if (netOpen.getConfigValue().equals(DbBooleanConvert.YES.getBooleanValue().toString())) {
                        PlugSearchResult<PlugSearchMusicResult> plugSearchMusicResultPlugSearchResult = neteaseHander.querySongByName(searchKeyData);
                        extracted(parserEntity, plugSearchMusicResultPlugSearchResult);
                    }
                }
                if (parserEntity.getIsDetection() == null || !parserEntity.getIsDetection()) {
                    if (qqvipOpen.getConfigValue().equals(DbBooleanConvert.YES.getBooleanValue().toString())) {
                        PlugSearchResult<PlugSearchMusicResult> plugSearchMusicResultPlugSearchResult = qqvipHander.querySongByName(searchKeyData);
                        extracted(parserEntity, plugSearchMusicResultPlugSearchResult);
                    }
                }
                if (parserEntity.getIsDetection() == null || !parserEntity.getIsDetection()) {
                    if (kgOpen.getConfigValue().equals(DbBooleanConvert.YES.getBooleanValue().toString())) {
                        PlugSearchResult<PlugSearchMusicResult> plugSearchMusicResultPlugSearchResult = kGHander.querySongByName(searchKeyData);
                        extracted(parserEntity, plugSearchMusicResultPlugSearchResult);
                    }
                }
            } catch (Exception e) {
                log.error("解析失败:{}",parserEntity);
            }

        }
        return parserEntities;
    }

    private static void extracted(ParserEntity parserEntity, PlugSearchResult<PlugSearchMusicResult> plugSearchMusicResultPlugSearchResult) {
        if (!plugSearchMusicResultPlugSearchResult.getRecords().isEmpty()) {
            List<PlugSearchMusicResult> records = plugSearchMusicResultPlugSearchResult.getRecords();
            for (PlugSearchMusicResult record : records) {
                if (record.getName().trim().equals(parserEntity.getSongName().trim())){
                    //匹配成功歌曲名称
                    if (StringUtils.isNotBlank(parserEntity.getArtistsName())){
                        if (record.getArtistName().contains(parserEntity.getArtistsName().trim())){
                            parserEntity.setIsDetection(true);
                            parserEntity.setPlugSearchMusicResult(record);
                            parserEntity.setPlugName(plugSearchMusicResultPlugSearchResult.getPlugName());
                            break;
                        }else{
                            parserEntity.setIsDetection(false);
                        }
                    }else{
                        parserEntity.setIsDetection(true);
                        parserEntity.setPlugSearchMusicResult(record);
                        parserEntity.setPlugName(plugSearchMusicResultPlugSearchResult.getPlugName());
                        break;
                    }
                }
            }


        }
    }

}
