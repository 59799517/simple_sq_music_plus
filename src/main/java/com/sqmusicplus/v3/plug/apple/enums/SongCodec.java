package com.sqmusicplus.v3.plug.apple.enums;


/**
 * @Classname SongCodec
 * @Version 1.0.0
 * @Date 2025/10/17 16:17
 * @Created by SQ
 * 歌曲编码格式枚举类
 * 定义音频文件支持的编码格式选项
 */
public enum SongCodec {
    /** AAC传统编码 */
    AAC_LEGACY("aac-legacy"),
    /** AAC高效率传统编码 */
    AAC_HE_LEGACY("aac-he-legacy"),
    /** AAC编码 */
    AAC("aac"),
    /** AAC高效率编码 */
    AAC_HE("aac-he"),
    /** AAC双耳音效编码 */
    AAC_BINAURAL("aac-binaural"),
    /** AAC缩混编码 */
    AAC_DOWNMIX("aac-downmix"),
    /** AAC高效率双耳音效编码 */
    AAC_HE_BINAURAL("aac-he-binaural"),
    /** AAC高效率缩混编码 */
    AAC_HE_DOWNMIX("aac-he-downmix"),
    /** Atmos环绕声编码 */
    ATMOS("atmos"),
    /** AC3编码 */
    AC3("ac3"),
    /** ALAC无损编码 */
    ALAC("alac"),
    /** 询问用户选择 */
    ASK("ask");

    private final String value;

    /**
     * 构造函数
     * @param value 枚举对应的字符串值
     */
    SongCodec(String value) {
        this.value = value;
    }

    /**
     * 获取枚举值
     * @return 对应的字符串值
     */
    public String getValue() {
        return value;
    }
}