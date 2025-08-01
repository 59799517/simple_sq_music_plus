package com.sqmusicplus.v3.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.sqmusicplus.v3.base.entity.vo.ParserEntity;
import com.sqmusicplus.v3.config.AjaxResult;
import com.sqmusicplus.v3.download.vo.DownlaodParserUrl;
import com.sqmusicplus.v3.parser.TextMusicPlayListParser;
import com.sqmusicplus.v3.parser.UrlMusicPlayListParser;
import com.sqmusicplus.v3.plug.entity.Music;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Classname ParserController
 * @Description 解析控制器
 * @Version 1.0.0
 * @Date 2025/8/1 17:32
 * @Created by SQ
 */
@Slf4j
@RestController
@RequestMapping("/api/parser")
public class ParserController {


    @Autowired
    private UrlMusicPlayListParser urlMusicPlayListParser;
    @Autowired
    private TextMusicPlayListParser textMusicPlayListParser;
    @SaCheckLogin
    @PostMapping("/parserUrl")
    public AjaxResult parserUrl(DownlaodParserUrl  downlaodParserUrl) {
        try {
            List<Music> parser = urlMusicPlayListParser.parser(downlaodParserUrl);
            if (parser != null){
                return AjaxResult.success(parser);
            }
            return AjaxResult.error("解析失败 仅支持qq 酷我 酷狗概念 网易云");

        } catch (Exception e) {
            log.error("解析失败",e);
            return AjaxResult.error("解析失败 仅支持qq 酷我 酷狗概念 网易云");
        }
    }
    @SaCheckLogin
    @PostMapping("/parserText")
    public AjaxResult parserText(String text) {
        try {
            List<ParserEntity> parser = textMusicPlayListParser.parser(text);
            if (parser != null){
                return AjaxResult.success(parser);
            }
            return AjaxResult.error("解析失败 仅支持qq 酷我 酷狗概念 网易云");

        } catch (Exception e) {
            log.error("解析失败",e);
            return AjaxResult.error("解析失败 仅支持qq 酷我 酷狗概念 网易云");
        }
    }

}
