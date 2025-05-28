package com.sqmusicplus.plug.base;

/**
 * @Classname QQSongType
 * @Description qq歌曲类型
 * @Version 1.0.0
 * @Date 2025/4/30 09:45
 * @Created by S
 */

public enum QQSongType {
    //不加密文件类型
//     MASTER: 臻品母带2.0,24Bit 192kHz,size_new[0]
//     ATMOS_2: 臻品全景声2.0,16Bit 44.1kHz,size_new[1]
//     ATMOS_51: 臻品音质2.0,16Bit 44.1kHz,size_new[2]
//     FLAC: flac 格式,16Bit 44.1kHz~24Bit 48kHz,size_flac
//     OGG_640: ogg 格式,640kbps,size_new[5]
//     OGG_320: ogg 格式,320kbps,size_new[3]
//     OGG_192: ogg 格式,192kbps,size_192ogg
//     OGG_96: ogg 格式,96kbps,size_96ogg
//     MP3_320: mp3 格式,320kbps,size_320mp3
//     MP3_128: mp3 格式,128kbps,size_128mp3
//     ACC_192: m4a 格式,192kbps,size_192aac
//     ACC_96: m4a 格式,96kbps,size_96aac
//     ACC_48: m4a 格式,48kbps,size_48aac

    /// /     加密歌曲文件类型
//
//    MASTER1: 臻品母带2.0,24Bit 192kHz,size_new[0]
//    ATMOS_2: 臻品全景声2.0,16Bit 44.1kHz,size_new[1]
//    ATMOS_51: 臻品音质2.0,16Bit 44.1kHz,size_new[2]
//    FLAC: mflac 格式,16Bit 44.1kHz~24Bit 48kHz,size_flac
//    OGG_640: mgg 格式,640kbps,size_new[5]
//    OGG_320: mgg 格式,320kbps,size_new[3]
//    OGG_192: mgg 格式,192kbps,size_192ogg
//    OGG_96: mgg 格式,96kbps,size_96ogg

    MASTER("MASTER","AI00","mp3",false),

    ATMOS_2("ATMOS_2","Q000","mp3",false),

    ATMOS_51("ATMOS_51","Q001","mp3",false),

    FLAC("FLAC","F000","flac",false),

    OGG_640("OGG_640","O801","ogg",false),

    OGG_320("OGG_320","O800","ogg",false),

    OGG_192("OGG_192","O600","ogg",false),

    OGG_96("OGG_96","O400","ogg",false),

    MP3_320("MP3_320","M800","mp3",false),

    MP3_128("MP3_128","M500","mp3",false),

    ACC_192("ACC_192","C600","m4a",false),

    ACC_96("ACC_96","C400","m4a",false),

    ACC_48("ACC_48","C200","m4a",false),

    MASTER_ENCRYPT("MASTER1","AIM0","mp3",true),

    ATMOS_ENCRYPT_2("ATMOS_2","Q0M0","mp3",true),

    ATMOS_ENCRYPT_51("ATMOS_51","Q0M1","mp3",true),

    FLAC_ENCRYPT("FLAC","F0M0","mflac",true),

    OGG_ENCRYPT_640("OGG_640","O801","mgg",true),

    OGG_ENCRYPT_320("OGG_320","O800","mgg",true),

    OGG_ENCRYPT_192("OGG_192","O6M0","mgg",true),

    OGG_ENCRYPT_96("OGG_96","O4M0","mgg",true);


    private final String name;

    /**
     * 前缀
     */
    private final String prefix;

    /**
     * 文件后缀
     */
    private final String suffix;

    /**
     * 是否加密类型
     */
    private final Boolean encrypt;

    QQSongType(String name, String prefix, String suffix, Boolean encrypt) {
        this.name = name;
        this.prefix = prefix;
        this.suffix = suffix;
        this.encrypt = encrypt;
    }

    public String getName() {
        return name;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public Boolean getEncrypt() {
        return encrypt;
    }
}
