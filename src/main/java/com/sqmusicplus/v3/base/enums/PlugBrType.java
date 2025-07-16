package com.sqmusicplus.v3.base.enums;



public enum PlugBrType {

    KW_MP3_128( "128kmp3", "mp3", 128,"kw","nKwSearchHander","kw_mp3_128"),
    KW_MP3_192( "192kmp3", "mp3", 192,"kw","nKwSearchHander","kw_mp3_192"),
    KW_MP3_320( "320kmp3", "mp3", 320,"kw","nKwSearchHander","kw_mp3_320"),
    KW_APE_1000( "1000kape", "ape", 1000,"kw","nKwSearchHander","kw_ape_1000"),
    KW_FLAC_2000( "2000kflac", "flac", 2000,"kw","nKwSearchHander","kw_flac_2000"),
    MG_FLAC_2000("ZQ","flac",2000,"mg","mgHander","mg_flac_2000"),
    MG_M4A_1000("SQ","m4a",1000,"mg","mgHander","mg_m4a_1000"),
    MG_MP3_320("HQ","mp3",320,"mg","mgHander","mg_mp3_320"),
    MG_MP3_128("PQ","mp3",128,"mg","mgHander","mg_mp3_128"),
    MG_MP3_64("LQ","mp3",64,"mg","mgHander","mg_mp3_64"),

    QQ_MP3_128("HQ_M500","mp3",128,"qq","qqHander","qq_mp3_128"),
    QQ_MP3_320("HQ_M800","mp3",320,"qq","qqHander","qq_mp3_320"),
    QQ_Flac_2000("SQ_F000","flac",2000,"qq","qqHander","qq_flac_2000"),
    QQ_Flac_3000("HR_RS01","falc",3000,"qq","qqHander","qq_flac_3000"),
    QQ_Flac_4000("HR_Q000","falc",3000,"qq","qqHander","qq_flac_4000"),
    QQ_Flac_5000("HR_AI00","falc",3000,"qq","qqHander","qq_flac_5000"),

    NETEASE_MP3_128("standard", "mp3", 128,"netease","neteaseHander","netease_mp3_128"),
    NETEASE_MP3_192("higher", "mp3", 192,"netease","neteaseHander","netease_mp3_192"),
    NETEASE_MP3_320("exhigh", "mp3", 320,"netease","neteaseHander","netease_mp3_320"),
    NETEASE_FLAC_2000("lossless", "flac", 2000,"netease","neteaseHander","netease_flac_2000"),
    NETEASE_FLAC_3000("hires", "flac", 3000,"netease","neteaseHander","netease_flac_3000"),



    QQVIP_MP3_128("128","mp3",128,"qqvip","qqvipHander","qqvip_mp3_128"),
    QQVIP_MP3_320("320","mp3",320,"qqvip","qqvipHander","qqvip_mp3_320"),
    QQVIP_Flac_2000("flac","flac",2000,"qqvip","qqvipHander","qqvip_flac_2000"),
    QQVIP_Flac_3000("flac","falc",3000,"qqvip","qqvipHander","qqvip_flac_3000"),
    QQVIP_Flac_4000("flac","falc",4000,"qqvip","qqvipHander","qqvip_flac_4000"),
    QQVIP_Flac_5000("flac","falc",5000,"qqvip","qqvipHander","qqvip_flac_5000"),
    QQVIP_Ape_2000("ape","ape",2000,"qqvip","qqvipHander","qqvip_ape_2000"),
    QQVIP_M4A_2000("m4a","m4a",3000,"qqvip","qqvipHander","qqvip_m4a_2000"),


    Free_Download_2000("flac","flac",2000,"free","freeMp3Hander","free_flac_2000"),

    KG_MP3_128("128", "mp3", 128,"kg","kgHander","kg_mp3_128"),
    KG_MP3_320("320", "mp3", 320,"kg","kgHander","kg_mp3_320"),
    KG_Flac_890("flac", "flac", 890,"kg","kgHander","kg_flac_890"),
    KG_Flac_2000("flac", "flac", 2000,"kg","kgHander","kg_flac_2000"),
    KG_Flac_3000("high", "flac", 3000,"kg","kgHander","kg_flac_3000"),
    KG_Flac_4000("viper_atmos", "flac", 4000,"kg","kgHander","kg_flac_4000"),
    KG_Flac_5000("viper_tape", "flac", 5000,"kg","kgHander","kg_flac_5000");



    String value;
    String type;
    Integer bit;

    String plugName;

    String springName;
    String id;


    PlugBrType(String value, String type, Integer bit, String plugName, String springName, String id) {
        this.value = value;
        this.type = type;
        this.bit = bit;
        this.plugName = plugName;
        this.springName = springName;
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public String getType() {
        return type;
    }

    public Integer getBit() {
        return bit;
    }

    public String getPlugName() {
        return plugName;
    }

    public String getSpringName() {
        return springName;
    }

    public String getId() {
        return id;
    }
    //根据id找出枚举
    public static PlugBrType findById(String id) {
        for (PlugBrType value : PlugBrType.values()) {
            if (value.getId().equals(id)) {
                return value;
            }
        }
        return null;
    }
    //根据plugName和bit找出枚举
    public static PlugBrType findByPlugNameAndBit(String plugName, Integer bit) {
        for (PlugBrType value : PlugBrType.values()) {
            if (value.getPlugName().equals(plugName) && value.getBit().equals(bit)) {
                return value;
            }
        }
        return null;
    }

}
