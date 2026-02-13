package com.sqmusicplus.v3.plug.apple.config;

import com.sqmusicplus.v3.plug.apple.enums.SongCodec;

import java.util.HashMap;
import java.util.Map;

/**
 * @Classname SongCodecConstants
 * @Description TODO
 * @Version 1.0.0
 * @Date 2025/10/17 16:37
 * @Created by SQ
 */

public class SongCodecConstants {
    // 编解码器正则表达式映射（与Python中的SONG_CODEC_REGEX_MAP对应）
    public static final Map<SongCodec, String> SONG_CODEC_REGEX_MAP = new HashMap<>();

    static {
        SONG_CODEC_REGEX_MAP.put(SongCodec.AAC, "audio-stereo-\\d+");
        SONG_CODEC_REGEX_MAP.put(SongCodec.AAC_HE, "audio-HE-stereo-\\d+");
        SONG_CODEC_REGEX_MAP.put(SongCodec.AAC_BINAURAL, "audio-stereo-\\d+-binaural");
        SONG_CODEC_REGEX_MAP.put(SongCodec.AAC_DOWNMIX, "audio-stereo-\\d+-downmix");
        SONG_CODEC_REGEX_MAP.put(SongCodec.AAC_HE_BINAURAL, "audio-HE-stereo-\\d+-binaural");
        SONG_CODEC_REGEX_MAP.put(SongCodec.AAC_HE_DOWNMIX, "audio-HE-stereo-\\d+-downmix");
        SONG_CODEC_REGEX_MAP.put(SongCodec.ATMOS, "audio-atmos-.*");
        SONG_CODEC_REGEX_MAP.put(SongCodec.AC3, "audio-ac3-.*");
        SONG_CODEC_REGEX_MAP.put(SongCodec.ALAC, "audio-alac-.*");
    }
}
