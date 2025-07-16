package com.sqmusicplus.v3.plug.kg.entity.PlayListInfoResult;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

public class Data {

    @JsonProperty("begin_idx")
    private Long beginIdx;
    @JsonProperty("pagesize")
    private Long pagesize;
    @JsonProperty("count")
    private Long count;
    @JsonProperty("popularization")
    private Popularization popularization;
    @JsonProperty("userid")
    private Long userid;
    @JsonProperty("songs")
    private List<Songs> songs;
    @JsonProperty("list_info")
    private ListInfo listInfo;


    public Long getBeginIdx(){
        return beginIdx;
    }

    public void setBeginIdx(Long beginIdx){
        this.beginIdx=beginIdx;
    }

    public Long getPagesize(){
        return pagesize;
    }

    public void setPagesize(Long pagesize){
        this.pagesize=pagesize;
    }

    public Long getCount(){
        return count;
    }

    public void setCount(Long count){
        this.count=count;
    }

    public Popularization getPopularization(){
        return popularization;
    }

    public void setPopularization(Popularization popularization){
        this.popularization=popularization;
    }

    public Long getUserid(){
        return userid;
    }

    public void setUserid(Long userid){
        this.userid=userid;
    }

    public List<Songs> getSongs(){
        return songs;
    }

    public void setSongs(List<Songs> songs){
        this.songs=songs;
    }

    public ListInfo getListInfo(){
        return listInfo;
    }

    public void setListInfo(ListInfo listInfo){
        this.listInfo=listInfo;
    }



    @Override
    public String toString() {
        return "Data{" +
                "beginIdx=" + beginIdx +
                ", pagesize=" + pagesize +
                ", count=" + count +
                ", popularization=" + popularization +
                ", userid=" + userid +
                ", songs=" + songs +
                ", listInfo=" + listInfo +
                "}";
    }


    @Override
    public int hashCode() {
        return Objects.hash(beginIdx, pagesize, count, popularization, userid, songs, listInfo);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Data other = (Data) o;
        return Objects.equals(this.beginIdx, other.beginIdx) && Objects.equals(this.pagesize, other.pagesize) && Objects.equals(this.count, other.count) && Objects.equals(this.popularization, other.popularization) && Objects.equals(this.userid, other.userid) && Objects.equals(this.songs, other.songs) && Objects.equals(this.listInfo, other.listInfo);
    }

}

