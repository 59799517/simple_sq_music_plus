package com.sqmusicplus.v3.parser;


import com.sqmusicplus.v3.plug.entity.Music;
import com.sqmusicplus.v3.download.vo.DownlaodParserUrl;

import java.io.IOException;
import java.util.List;

/**
 * @Classname TextParser
 * @Description 文本解析
 * @Version 1.0.0
 * @Date 2022/8/29 10:20
 * @Created by SQ
 */

public abstract class TextParser implements MusicPlayListParser {
    @Override
    public List<Music> parser(DownlaodParserUrl downlaodParserUrl) throws IOException {
        return null;
    }
}
