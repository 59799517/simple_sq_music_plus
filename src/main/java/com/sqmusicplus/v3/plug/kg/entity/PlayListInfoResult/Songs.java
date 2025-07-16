package com.sqmusicplus.v3.plug.kg.entity.PlayListInfoResult;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

public class Songs {

    @JsonProperty("mvdata")
    private List<Mvdata> mvdata;
    @JsonProperty("hash")
    private String hash;
    @JsonProperty("brief")
    private String brief;
    @JsonProperty("audio_id")
    private Long audioId;
    @JsonProperty("mvtype")
    private Long mvtype;
    @JsonProperty("size")
    private Long size;
    @JsonProperty("publish_date")
    private String publishDate;
    @JsonProperty("name")
    private String name;
    @JsonProperty("mvtrack")
    private Long mvtrack;
    @JsonProperty("bpm_type")
    private String bpmType;
    @JsonProperty("add_mixsongid")
    private Long addMixsongid;
    @JsonProperty("album_id")
    private String albumId;
    @JsonProperty("bpm")
    private Long bpm;
    @JsonProperty("mvhash")
    private String mvhash;
    @JsonProperty("extname")
    private String extname;
    @JsonProperty("language")
    private String language;
    @JsonProperty("collecttime")
    private Long collecttime;
    @JsonProperty("csong")
    private Long csong;
    @JsonProperty("remark")
    private String remark;
    @JsonProperty("level")
    private Long level;
    @JsonProperty("tagmap")
    private Tagmap tagmap;
    @JsonProperty("media_old_cpy")
    private Long mediaOldCpy;
    @JsonProperty("relate_goods")
    private List<RelateGoods> relateGoods;
    @JsonProperty("download")
    private List<Download> download;
    @JsonProperty("rcflag")
    private Long rcflag;
    @JsonProperty("feetype")
    private Long feetype;
    @JsonProperty("has_obbligato")
    private Long hasObbligato;
    @JsonProperty("timelen")
    private Long timelen;
    @JsonProperty("sort")
    private Long sort;
    @JsonProperty("trans_param")
    private TransParam transParam;
    @JsonProperty("medistype")
    private String medistype;
    @JsonProperty("user_id")
    private Long userId;
    @JsonProperty("albuminfo")
    private Albuminfo albuminfo;
    @JsonProperty("bitrate")
    private Long bitrate;
    @JsonProperty("audio_group_id")
    private String audioGroupId;
    @JsonProperty("privilege")
    private Long privilege;
    @JsonProperty("cover")
    private String cover;
    @JsonProperty("mixsongid")
    private Long mixsongid;
    @JsonProperty("fileid")
    private Long fileid;
    @JsonProperty("heat")
    private Long heat;
    @JsonProperty("singerinfo")
    private List<Singerinfo> singerinfo;


    public List<Mvdata> getMvdata(){
        return mvdata;
    }

    public void setMvdata(List<Mvdata> mvdata){
        this.mvdata=mvdata;
    }

    public String getHash(){
        return hash;
    }

    public void setHash(String hash){
        this.hash=hash;
    }

    public String getBrief(){
        return brief;
    }

    public void setBrief(String brief){
        this.brief=brief;
    }

    public Long getAudioId(){
        return audioId;
    }

    public void setAudioId(Long audioId){
        this.audioId=audioId;
    }

    public Long getMvtype(){
        return mvtype;
    }

    public void setMvtype(Long mvtype){
        this.mvtype=mvtype;
    }

    public Long getSize(){
        return size;
    }

    public void setSize(Long size){
        this.size=size;
    }

    public String getPublishDate(){
        return publishDate;
    }

    public void setPublishDate(String publishDate){
        this.publishDate=publishDate;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name=name;
    }

    public Long getMvtrack(){
        return mvtrack;
    }

    public void setMvtrack(Long mvtrack){
        this.mvtrack=mvtrack;
    }

    public String getBpmType(){
        return bpmType;
    }

    public void setBpmType(String bpmType){
        this.bpmType=bpmType;
    }

    public Long getAddMixsongid(){
        return addMixsongid;
    }

    public void setAddMixsongid(Long addMixsongid){
        this.addMixsongid=addMixsongid;
    }

    public String getAlbumId(){
        return albumId;
    }

    public void setAlbumId(String albumId){
        this.albumId=albumId;
    }

    public Long getBpm(){
        return bpm;
    }

    public void setBpm(Long bpm){
        this.bpm=bpm;
    }

    public String getMvhash(){
        return mvhash;
    }

    public void setMvhash(String mvhash){
        this.mvhash=mvhash;
    }

    public String getExtname(){
        return extname;
    }

    public void setExtname(String extname){
        this.extname=extname;
    }

    public String getLanguage(){
        return language;
    }

    public void setLanguage(String language){
        this.language=language;
    }

    public Long getCollecttime(){
        return collecttime;
    }

    public void setCollecttime(Long collecttime){
        this.collecttime=collecttime;
    }

    public Long getCsong(){
        return csong;
    }

    public void setCsong(Long csong){
        this.csong=csong;
    }

    public String getRemark(){
        return remark;
    }

    public void setRemark(String remark){
        this.remark=remark;
    }

    public Long getLevel(){
        return level;
    }

    public void setLevel(Long level){
        this.level=level;
    }

    public Tagmap getTagmap(){
        return tagmap;
    }

    public void setTagmap(Tagmap tagmap){
        this.tagmap=tagmap;
    }

    public Long getMediaOldCpy(){
        return mediaOldCpy;
    }

    public void setMediaOldCpy(Long mediaOldCpy){
        this.mediaOldCpy=mediaOldCpy;
    }

    public List<RelateGoods> getRelateGoods(){
        return relateGoods;
    }

    public void setRelateGoods(List<RelateGoods> relateGoods){
        this.relateGoods=relateGoods;
    }

    public List<Download> getDownload(){
        return download;
    }

    public void setDownload(List<Download> download){
        this.download=download;
    }

    public Long getRcflag(){
        return rcflag;
    }

    public void setRcflag(Long rcflag){
        this.rcflag=rcflag;
    }

    public Long getFeetype(){
        return feetype;
    }

    public void setFeetype(Long feetype){
        this.feetype=feetype;
    }

    public Long getHasObbligato(){
        return hasObbligato;
    }

    public void setHasObbligato(Long hasObbligato){
        this.hasObbligato=hasObbligato;
    }

    public Long getTimelen(){
        return timelen;
    }

    public void setTimelen(Long timelen){
        this.timelen=timelen;
    }

    public Long getSort(){
        return sort;
    }

    public void setSort(Long sort){
        this.sort=sort;
    }

    public TransParam getTransParam(){
        return transParam;
    }

    public void setTransParam(TransParam transParam){
        this.transParam=transParam;
    }

    public String getMedistype(){
        return medistype;
    }

    public void setMedistype(String medistype){
        this.medistype=medistype;
    }

    public Long getUserId(){
        return userId;
    }

    public void setUserId(Long userId){
        this.userId=userId;
    }

    public Albuminfo getAlbuminfo(){
        return albuminfo;
    }

    public void setAlbuminfo(Albuminfo albuminfo){
        this.albuminfo=albuminfo;
    }

    public Long getBitrate(){
        return bitrate;
    }

    public void setBitrate(Long bitrate){
        this.bitrate=bitrate;
    }

    public String getAudioGroupId(){
        return audioGroupId;
    }

    public void setAudioGroupId(String audioGroupId){
        this.audioGroupId=audioGroupId;
    }

    public Long getPrivilege(){
        return privilege;
    }

    public void setPrivilege(Long privilege){
        this.privilege=privilege;
    }

    public String getCover(){
        return cover;
    }

    public void setCover(String cover){
        this.cover=cover;
    }

    public Long getMixsongid(){
        return mixsongid;
    }

    public void setMixsongid(Long mixsongid){
        this.mixsongid=mixsongid;
    }

    public Long getFileid(){
        return fileid;
    }

    public void setFileid(Long fileid){
        this.fileid=fileid;
    }

    public Long getHeat(){
        return heat;
    }

    public void setHeat(Long heat){
        this.heat=heat;
    }

    public List<Singerinfo> getSingerinfo(){
        return singerinfo;
    }

    public void setSingerinfo(List<Singerinfo> singerinfo){
        this.singerinfo=singerinfo;
    }



    @Override
    public String toString() {
        return "Songs{" +
                "mvdata=" + mvdata +
                ", hash='" + hash + "'" +
                ", brief='" + brief + "'" +
                ", audioId=" + audioId +
                ", mvtype=" + mvtype +
                ", size=" + size +
                ", publishDate='" + publishDate + "'" +
                ", name='" + name + "'" +
                ", mvtrack=" + mvtrack +
                ", bpmType='" + bpmType + "'" +
                ", addMixsongid=" + addMixsongid +
                ", albumId='" + albumId + "'" +
                ", bpm=" + bpm +
                ", mvhash='" + mvhash + "'" +
                ", extname='" + extname + "'" +
                ", language='" + language + "'" +
                ", collecttime=" + collecttime +
                ", csong=" + csong +
                ", remark='" + remark + "'" +
                ", level=" + level +
                ", tagmap=" + tagmap +
                ", mediaOldCpy=" + mediaOldCpy +
                ", relateGoods=" + relateGoods +
                ", download=" + download +
                ", rcflag=" + rcflag +
                ", feetype=" + feetype +
                ", hasObbligato=" + hasObbligato +
                ", timelen=" + timelen +
                ", sort=" + sort +
                ", transParam=" + transParam +
                ", medistype='" + medistype + "'" +
                ", userId=" + userId +
                ", albuminfo=" + albuminfo +
                ", bitrate=" + bitrate +
                ", audioGroupId='" + audioGroupId + "'" +
                ", privilege=" + privilege +
                ", cover='" + cover + "'" +
                ", mixsongid=" + mixsongid +
                ", fileid=" + fileid +
                ", heat=" + heat +
                ", singerinfo=" + singerinfo +
                "}";
    }


    @Override
    public int hashCode() {
        return Objects.hash(mvdata, hash, brief, audioId, mvtype, size, publishDate, name, mvtrack, bpmType, addMixsongid, albumId, bpm, mvhash, extname, language, collecttime, csong, remark, level, tagmap, mediaOldCpy, relateGoods, download, rcflag, feetype, hasObbligato, timelen, sort, transParam, medistype, userId, albuminfo, bitrate, audioGroupId, privilege, cover, mixsongid, fileid, heat, singerinfo);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Songs other = (Songs) o;
        return Objects.equals(this.mvdata, other.mvdata) && Objects.equals(this.hash, other.hash) && Objects.equals(this.brief, other.brief) && Objects.equals(this.audioId, other.audioId) && Objects.equals(this.mvtype, other.mvtype) && Objects.equals(this.size, other.size) && Objects.equals(this.publishDate, other.publishDate) && Objects.equals(this.name, other.name) && Objects.equals(this.mvtrack, other.mvtrack) && Objects.equals(this.bpmType, other.bpmType) && Objects.equals(this.addMixsongid, other.addMixsongid) && Objects.equals(this.albumId, other.albumId) && Objects.equals(this.bpm, other.bpm) && Objects.equals(this.mvhash, other.mvhash) && Objects.equals(this.extname, other.extname) && Objects.equals(this.language, other.language) && Objects.equals(this.collecttime, other.collecttime) && Objects.equals(this.csong, other.csong) && Objects.equals(this.remark, other.remark) && Objects.equals(this.level, other.level) && Objects.equals(this.tagmap, other.tagmap) && Objects.equals(this.mediaOldCpy, other.mediaOldCpy) && Objects.equals(this.relateGoods, other.relateGoods) && Objects.equals(this.download, other.download) && Objects.equals(this.rcflag, other.rcflag) && Objects.equals(this.feetype, other.feetype) && Objects.equals(this.hasObbligato, other.hasObbligato) && Objects.equals(this.timelen, other.timelen) && Objects.equals(this.sort, other.sort) && Objects.equals(this.transParam, other.transParam) && Objects.equals(this.medistype, other.medistype) && Objects.equals(this.userId, other.userId) && Objects.equals(this.albuminfo, other.albuminfo) && Objects.equals(this.bitrate, other.bitrate) && Objects.equals(this.audioGroupId, other.audioGroupId) && Objects.equals(this.privilege, other.privilege) && Objects.equals(this.cover, other.cover) && Objects.equals(this.mixsongid, other.mixsongid) && Objects.equals(this.fileid, other.fileid) && Objects.equals(this.heat, other.heat) && Objects.equals(this.singerinfo, other.singerinfo);
    }

}

