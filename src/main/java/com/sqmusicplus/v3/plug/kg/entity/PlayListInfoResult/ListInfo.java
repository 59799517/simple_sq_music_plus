package com.sqmusicplus.v3.plug.kg.entity.PlayListInfoResult;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

public class ListInfo {

    @JsonProperty("abtags")
    private List<Object> abtags;
    @JsonProperty("tags")
    private String tags;
    @JsonProperty("status")
    private Long status;
    @JsonProperty("create_user_pic")
    private String createUserPic;
    @JsonProperty("is_pri")
    private Long isPri;
    @JsonProperty("pub_new")
    private Long pubNew;
    @JsonProperty("is_drop")
    private Long isDrop;
    @JsonProperty("list_create_userid")
    private Long listCreateUserid;
    @JsonProperty("is_publish")
    private Long isPublish;
    @JsonProperty("musiclib_tags")
    private List<Object> musiclibTags;
    @JsonProperty("pub_type")
    private Long pubType;
    @JsonProperty("is_featured")
    private Long isFeatured;
    @JsonProperty("publish_date")
    private String publishDate;
    @JsonProperty("collect_total")
    private Long collectTotal;
    @JsonProperty("list_ver")
    private Long listVer;
    @JsonProperty("intro")
    private String intro;
    @JsonProperty("type")
    private Long type;
    @JsonProperty("list_create_listid")
    private Long listCreateListid;
    @JsonProperty("radio_id")
    private Long radioId;
    @JsonProperty("source")
    private Long source;
    @JsonProperty("code")
    private Long code;
    @JsonProperty("is_def")
    private Long isDef;
    @JsonProperty("parent_global_collection_id")
    private String parentGlobalCollectionId;
    @JsonProperty("sound_quality")
    private String soundQuality;
    @JsonProperty("per_count")
    private Long perCount;
    @JsonProperty("plist")
    private List<Object> plist;
    @JsonProperty("create_time")
    private Long createTime;
    @JsonProperty("is_per")
    private Long isPer;
    @JsonProperty("is_edit")
    private Long isEdit;
    @JsonProperty("update_time")
    private Long updateTime;
    @JsonProperty("per_num")
    private Long perNum;
    @JsonProperty("count")
    private Long count;
    @JsonProperty("sort")
    private Long sort;
    @JsonProperty("is_mine")
    private Long isMine;
    @JsonProperty("listid")
    private Long listid;
    @JsonProperty("musiclib_id")
    private Long musiclibId;
    @JsonProperty("kq_talent")
    private Long kqTalent;
    @JsonProperty("create_user_gender")
    private Long createUserGender;
    @JsonProperty("pic")
    private String pic;
    @JsonProperty("list_create_username")
    private String listCreateUsername;
    @JsonProperty("name")
    private String name;
    @JsonProperty("is_custom_pic")
    private Long isCustomPic;
    @JsonProperty("global_collection_id")
    private String globalCollectionId;
    @JsonProperty("heat")
    private Long heat;
    @JsonProperty("list_create_gid")
    private String listCreateGid;


    public List<Object> getAbtags(){
        return abtags;
    }

    public void setAbtags(List<Object> abtags){
        this.abtags=abtags;
    }

    public String getTags(){
        return tags;
    }

    public void setTags(String tags){
        this.tags=tags;
    }

    public Long getStatus(){
        return status;
    }

    public void setStatus(Long status){
        this.status=status;
    }

    public String getCreateUserPic(){
        return createUserPic;
    }

    public void setCreateUserPic(String createUserPic){
        this.createUserPic=createUserPic;
    }

    public Long getIsPri(){
        return isPri;
    }

    public void setIsPri(Long isPri){
        this.isPri=isPri;
    }

    public Long getPubNew(){
        return pubNew;
    }

    public void setPubNew(Long pubNew){
        this.pubNew=pubNew;
    }

    public Long getIsDrop(){
        return isDrop;
    }

    public void setIsDrop(Long isDrop){
        this.isDrop=isDrop;
    }

    public Long getListCreateUserid(){
        return listCreateUserid;
    }

    public void setListCreateUserid(Long listCreateUserid){
        this.listCreateUserid=listCreateUserid;
    }

    public Long getIsPublish(){
        return isPublish;
    }

    public void setIsPublish(Long isPublish){
        this.isPublish=isPublish;
    }

    public List<Object> getMusiclibTags(){
        return musiclibTags;
    }

    public void setMusiclibTags(List<Object> musiclibTags){
        this.musiclibTags=musiclibTags;
    }

    public Long getPubType(){
        return pubType;
    }

    public void setPubType(Long pubType){
        this.pubType=pubType;
    }

    public Long getIsFeatured(){
        return isFeatured;
    }

    public void setIsFeatured(Long isFeatured){
        this.isFeatured=isFeatured;
    }

    public String getPublishDate(){
        return publishDate;
    }

    public void setPublishDate(String publishDate){
        this.publishDate=publishDate;
    }

    public Long getCollectTotal(){
        return collectTotal;
    }

    public void setCollectTotal(Long collectTotal){
        this.collectTotal=collectTotal;
    }

    public Long getListVer(){
        return listVer;
    }

    public void setListVer(Long listVer){
        this.listVer=listVer;
    }

    public String getIntro(){
        return intro;
    }

    public void setIntro(String intro){
        this.intro=intro;
    }

    public Long getType(){
        return type;
    }

    public void setType(Long type){
        this.type=type;
    }

    public Long getListCreateListid(){
        return listCreateListid;
    }

    public void setListCreateListid(Long listCreateListid){
        this.listCreateListid=listCreateListid;
    }

    public Long getRadioId(){
        return radioId;
    }

    public void setRadioId(Long radioId){
        this.radioId=radioId;
    }

    public Long getSource(){
        return source;
    }

    public void setSource(Long source){
        this.source=source;
    }

    public Long getCode(){
        return code;
    }

    public void setCode(Long code){
        this.code=code;
    }

    public Long getIsDef(){
        return isDef;
    }

    public void setIsDef(Long isDef){
        this.isDef=isDef;
    }

    public String getParentGlobalCollectionId(){
        return parentGlobalCollectionId;
    }

    public void setParentGlobalCollectionId(String parentGlobalCollectionId){
        this.parentGlobalCollectionId=parentGlobalCollectionId;
    }

    public String getSoundQuality(){
        return soundQuality;
    }

    public void setSoundQuality(String soundQuality){
        this.soundQuality=soundQuality;
    }

    public Long getPerCount(){
        return perCount;
    }

    public void setPerCount(Long perCount){
        this.perCount=perCount;
    }

    public List<Object> getPlist(){
        return plist;
    }

    public void setPlist(List<Object> plist){
        this.plist=plist;
    }

    public Long getCreateTime(){
        return createTime;
    }

    public void setCreateTime(Long createTime){
        this.createTime=createTime;
    }

    public Long getIsPer(){
        return isPer;
    }

    public void setIsPer(Long isPer){
        this.isPer=isPer;
    }

    public Long getIsEdit(){
        return isEdit;
    }

    public void setIsEdit(Long isEdit){
        this.isEdit=isEdit;
    }

    public Long getUpdateTime(){
        return updateTime;
    }

    public void setUpdateTime(Long updateTime){
        this.updateTime=updateTime;
    }

    public Long getPerNum(){
        return perNum;
    }

    public void setPerNum(Long perNum){
        this.perNum=perNum;
    }

    public Long getCount(){
        return count;
    }

    public void setCount(Long count){
        this.count=count;
    }

    public Long getSort(){
        return sort;
    }

    public void setSort(Long sort){
        this.sort=sort;
    }

    public Long getIsMine(){
        return isMine;
    }

    public void setIsMine(Long isMine){
        this.isMine=isMine;
    }

    public Long getListid(){
        return listid;
    }

    public void setListid(Long listid){
        this.listid=listid;
    }

    public Long getMusiclibId(){
        return musiclibId;
    }

    public void setMusiclibId(Long musiclibId){
        this.musiclibId=musiclibId;
    }

    public Long getKqTalent(){
        return kqTalent;
    }

    public void setKqTalent(Long kqTalent){
        this.kqTalent=kqTalent;
    }

    public Long getCreateUserGender(){
        return createUserGender;
    }

    public void setCreateUserGender(Long createUserGender){
        this.createUserGender=createUserGender;
    }

    public String getPic(){
        return pic;
    }

    public void setPic(String pic){
        this.pic=pic;
    }

    public String getListCreateUsername(){
        return listCreateUsername;
    }

    public void setListCreateUsername(String listCreateUsername){
        this.listCreateUsername=listCreateUsername;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name=name;
    }

    public Long getIsCustomPic(){
        return isCustomPic;
    }

    public void setIsCustomPic(Long isCustomPic){
        this.isCustomPic=isCustomPic;
    }

    public String getGlobalCollectionId(){
        return globalCollectionId;
    }

    public void setGlobalCollectionId(String globalCollectionId){
        this.globalCollectionId=globalCollectionId;
    }

    public Long getHeat(){
        return heat;
    }

    public void setHeat(Long heat){
        this.heat=heat;
    }

    public String getListCreateGid(){
        return listCreateGid;
    }

    public void setListCreateGid(String listCreateGid){
        this.listCreateGid=listCreateGid;
    }



    @Override
    public String toString() {
        return "ListInfo{" +
                "abtags=" + abtags +
                ", tags='" + tags + "'" +
                ", status=" + status +
                ", createUserPic='" + createUserPic + "'" +
                ", isPri=" + isPri +
                ", pubNew=" + pubNew +
                ", isDrop=" + isDrop +
                ", listCreateUserid=" + listCreateUserid +
                ", isPublish=" + isPublish +
                ", musiclibTags=" + musiclibTags +
                ", pubType=" + pubType +
                ", isFeatured=" + isFeatured +
                ", publishDate='" + publishDate + "'" +
                ", collectTotal=" + collectTotal +
                ", listVer=" + listVer +
                ", intro='" + intro + "'" +
                ", type=" + type +
                ", listCreateListid=" + listCreateListid +
                ", radioId=" + radioId +
                ", source=" + source +
                ", code=" + code +
                ", isDef=" + isDef +
                ", parentGlobalCollectionId='" + parentGlobalCollectionId + "'" +
                ", soundQuality='" + soundQuality + "'" +
                ", perCount=" + perCount +
                ", plist=" + plist +
                ", createTime=" + createTime +
                ", isPer=" + isPer +
                ", isEdit=" + isEdit +
                ", updateTime=" + updateTime +
                ", perNum=" + perNum +
                ", count=" + count +
                ", sort=" + sort +
                ", isMine=" + isMine +
                ", listid=" + listid +
                ", musiclibId=" + musiclibId +
                ", kqTalent=" + kqTalent +
                ", createUserGender=" + createUserGender +
                ", pic='" + pic + "'" +
                ", listCreateUsername='" + listCreateUsername + "'" +
                ", name='" + name + "'" +
                ", isCustomPic=" + isCustomPic +
                ", globalCollectionId='" + globalCollectionId + "'" +
                ", heat=" + heat +
                ", listCreateGid='" + listCreateGid + "'" +
                "}";
    }


    @Override
    public int hashCode() {
        return Objects.hash(abtags, tags, status, createUserPic, isPri, pubNew, isDrop, listCreateUserid, isPublish, musiclibTags, pubType, isFeatured, publishDate, collectTotal, listVer, intro, type, listCreateListid, radioId, source, code, isDef, parentGlobalCollectionId, soundQuality, perCount, plist, createTime, isPer, isEdit, updateTime, perNum, count, sort, isMine, listid, musiclibId, kqTalent, createUserGender, pic, listCreateUsername, name, isCustomPic, globalCollectionId, heat, listCreateGid);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ListInfo other = (ListInfo) o;
        return Objects.equals(this.abtags, other.abtags) && Objects.equals(this.tags, other.tags) && Objects.equals(this.status, other.status) && Objects.equals(this.createUserPic, other.createUserPic) && Objects.equals(this.isPri, other.isPri) && Objects.equals(this.pubNew, other.pubNew) && Objects.equals(this.isDrop, other.isDrop) && Objects.equals(this.listCreateUserid, other.listCreateUserid) && Objects.equals(this.isPublish, other.isPublish) && Objects.equals(this.musiclibTags, other.musiclibTags) && Objects.equals(this.pubType, other.pubType) && Objects.equals(this.isFeatured, other.isFeatured) && Objects.equals(this.publishDate, other.publishDate) && Objects.equals(this.collectTotal, other.collectTotal) && Objects.equals(this.listVer, other.listVer) && Objects.equals(this.intro, other.intro) && Objects.equals(this.type, other.type) && Objects.equals(this.listCreateListid, other.listCreateListid) && Objects.equals(this.radioId, other.radioId) && Objects.equals(this.source, other.source) && Objects.equals(this.code, other.code) && Objects.equals(this.isDef, other.isDef) && Objects.equals(this.parentGlobalCollectionId, other.parentGlobalCollectionId) && Objects.equals(this.soundQuality, other.soundQuality) && Objects.equals(this.perCount, other.perCount) && Objects.equals(this.plist, other.plist) && Objects.equals(this.createTime, other.createTime) && Objects.equals(this.isPer, other.isPer) && Objects.equals(this.isEdit, other.isEdit) && Objects.equals(this.updateTime, other.updateTime) && Objects.equals(this.perNum, other.perNum) && Objects.equals(this.count, other.count) && Objects.equals(this.sort, other.sort) && Objects.equals(this.isMine, other.isMine) && Objects.equals(this.listid, other.listid) && Objects.equals(this.musiclibId, other.musiclibId) && Objects.equals(this.kqTalent, other.kqTalent) && Objects.equals(this.createUserGender, other.createUserGender) && Objects.equals(this.pic, other.pic) && Objects.equals(this.listCreateUsername, other.listCreateUsername) && Objects.equals(this.name, other.name) && Objects.equals(this.isCustomPic, other.isCustomPic) && Objects.equals(this.globalCollectionId, other.globalCollectionId) && Objects.equals(this.heat, other.heat) && Objects.equals(this.listCreateGid, other.listCreateGid);
    }

}

