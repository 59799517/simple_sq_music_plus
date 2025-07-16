package com.sqmusicplus.v3.plug.kg.entity.PlayListInfoResult;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class TransParam {

    @JsonProperty("ogg_128_hash")
    private String ogg128Hash;
    @JsonProperty("classmap")
    private Classmap classmap;
    @JsonProperty("language")
    private String language;
    @JsonProperty("cpy_attr0")
    private Long cpyAttr0;
    @JsonProperty("musicpack_advance")
    private Long musicpackAdvance;
    @JsonProperty("display")
    private Long display;
    @JsonProperty("display_rate")
    private Long displayRate;
    @JsonProperty("ogg_320_filesize")
    private Long ogg320Filesize;
    @JsonProperty("union_cover")
    private String unionCover;
    @JsonProperty("cpy_grade")
    private Long cpyGrade;
    @JsonProperty("qualitymap")
    private Qualitymap qualitymap;
    @JsonProperty("hash_offset")
    private HashOffset hashOffset;
    @JsonProperty("ipmap")
    private Ipmap ipmap;
    @JsonProperty("cid")
    private Long cid;
    @JsonProperty("ogg_128_filesize")
    private Long ogg128Filesize;
    @JsonProperty("songname_suffix")
    private String songnameSuffix;
    @JsonProperty("hash_multitrack")
    private String hashMultitrack;
    @JsonProperty("appid_block")
    private String appidBlock;
    @JsonProperty("pay_block_tpl")
    private Long payBlockTpl;
    @JsonProperty("ogg_320_hash")
    private String ogg320Hash;
    @JsonProperty("cpy_level")
    private Long cpyLevel;


    public String getOgg128Hash(){
        return ogg128Hash;
    }

    public void setOgg128Hash(String ogg128Hash){
        this.ogg128Hash=ogg128Hash;
    }

    public Classmap getClassmap(){
        return classmap;
    }

    public void setClassmap(Classmap classmap){
        this.classmap=classmap;
    }

    public String getLanguage(){
        return language;
    }

    public void setLanguage(String language){
        this.language=language;
    }

    public Long getCpyAttr0(){
        return cpyAttr0;
    }

    public void setCpyAttr0(Long cpyAttr0){
        this.cpyAttr0=cpyAttr0;
    }

    public Long getMusicpackAdvance(){
        return musicpackAdvance;
    }

    public void setMusicpackAdvance(Long musicpackAdvance){
        this.musicpackAdvance=musicpackAdvance;
    }

    public Long getDisplay(){
        return display;
    }

    public void setDisplay(Long display){
        this.display=display;
    }

    public Long getDisplayRate(){
        return displayRate;
    }

    public void setDisplayRate(Long displayRate){
        this.displayRate=displayRate;
    }

    public Long getOgg320Filesize(){
        return ogg320Filesize;
    }

    public void setOgg320Filesize(Long ogg320Filesize){
        this.ogg320Filesize=ogg320Filesize;
    }

    public String getUnionCover(){
        return unionCover;
    }

    public void setUnionCover(String unionCover){
        this.unionCover=unionCover;
    }

    public Long getCpyGrade(){
        return cpyGrade;
    }

    public void setCpyGrade(Long cpyGrade){
        this.cpyGrade=cpyGrade;
    }

    public Qualitymap getQualitymap(){
        return qualitymap;
    }

    public void setQualitymap(Qualitymap qualitymap){
        this.qualitymap=qualitymap;
    }

    public HashOffset getHashOffset(){
        return hashOffset;
    }

    public void setHashOffset(HashOffset hashOffset){
        this.hashOffset=hashOffset;
    }

    public Ipmap getIpmap(){
        return ipmap;
    }

    public void setIpmap(Ipmap ipmap){
        this.ipmap=ipmap;
    }

    public Long getCid(){
        return cid;
    }

    public void setCid(Long cid){
        this.cid=cid;
    }

    public Long getOgg128Filesize(){
        return ogg128Filesize;
    }

    public void setOgg128Filesize(Long ogg128Filesize){
        this.ogg128Filesize=ogg128Filesize;
    }

    public String getSongnameSuffix(){
        return songnameSuffix;
    }

    public void setSongnameSuffix(String songnameSuffix){
        this.songnameSuffix=songnameSuffix;
    }

    public String getHashMultitrack(){
        return hashMultitrack;
    }

    public void setHashMultitrack(String hashMultitrack){
        this.hashMultitrack=hashMultitrack;
    }

    public String getAppidBlock(){
        return appidBlock;
    }

    public void setAppidBlock(String appidBlock){
        this.appidBlock=appidBlock;
    }

    public Long getPayBlockTpl(){
        return payBlockTpl;
    }

    public void setPayBlockTpl(Long payBlockTpl){
        this.payBlockTpl=payBlockTpl;
    }

    public String getOgg320Hash(){
        return ogg320Hash;
    }

    public void setOgg320Hash(String ogg320Hash){
        this.ogg320Hash=ogg320Hash;
    }

    public Long getCpyLevel(){
        return cpyLevel;
    }

    public void setCpyLevel(Long cpyLevel){
        this.cpyLevel=cpyLevel;
    }



    @Override
    public String toString() {
        return "TransParam{" +
                "ogg128Hash='" + ogg128Hash + "'" +
                ", classmap=" + classmap +
                ", language='" + language + "'" +
                ", cpyAttr0=" + cpyAttr0 +
                ", musicpackAdvance=" + musicpackAdvance +
                ", display=" + display +
                ", displayRate=" + displayRate +
                ", ogg320Filesize=" + ogg320Filesize +
                ", unionCover='" + unionCover + "'" +
                ", cpyGrade=" + cpyGrade +
                ", qualitymap=" + qualitymap +
                ", hashOffset=" + hashOffset +
                ", ipmap=" + ipmap +
                ", cid=" + cid +
                ", ogg128Filesize=" + ogg128Filesize +
                ", songnameSuffix='" + songnameSuffix + "'" +
                ", hashMultitrack='" + hashMultitrack + "'" +
                ", appidBlock='" + appidBlock + "'" +
                ", payBlockTpl=" + payBlockTpl +
                ", ogg320Hash='" + ogg320Hash + "'" +
                ", cpyLevel=" + cpyLevel +
                "}";
    }


    @Override
    public int hashCode() {
        return Objects.hash(ogg128Hash, classmap, language, cpyAttr0, musicpackAdvance, display, displayRate, ogg320Filesize, unionCover, cpyGrade, qualitymap, hashOffset, ipmap, cid, ogg128Filesize, songnameSuffix, hashMultitrack, appidBlock, payBlockTpl, ogg320Hash, cpyLevel);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransParam other = (TransParam) o;
        return Objects.equals(this.ogg128Hash, other.ogg128Hash) && Objects.equals(this.classmap, other.classmap) && Objects.equals(this.language, other.language) && Objects.equals(this.cpyAttr0, other.cpyAttr0) && Objects.equals(this.musicpackAdvance, other.musicpackAdvance) && Objects.equals(this.display, other.display) && Objects.equals(this.displayRate, other.displayRate) && Objects.equals(this.ogg320Filesize, other.ogg320Filesize) && Objects.equals(this.unionCover, other.unionCover) && Objects.equals(this.cpyGrade, other.cpyGrade) && Objects.equals(this.qualitymap, other.qualitymap) && Objects.equals(this.hashOffset, other.hashOffset) && Objects.equals(this.ipmap, other.ipmap) && Objects.equals(this.cid, other.cid) && Objects.equals(this.ogg128Filesize, other.ogg128Filesize) && Objects.equals(this.songnameSuffix, other.songnameSuffix) && Objects.equals(this.hashMultitrack, other.hashMultitrack) && Objects.equals(this.appidBlock, other.appidBlock) && Objects.equals(this.payBlockTpl, other.payBlockTpl) && Objects.equals(this.ogg320Hash, other.ogg320Hash) && Objects.equals(this.cpyLevel, other.cpyLevel);
    }

}

