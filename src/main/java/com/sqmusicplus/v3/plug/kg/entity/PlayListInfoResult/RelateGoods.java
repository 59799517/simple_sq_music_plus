package com.sqmusicplus.v3.plug.kg.entity.PlayListInfoResult;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class RelateGoods {

    @JsonProperty("size")
    private Long size;
    @JsonProperty("hash")
    private String hash;
    @JsonProperty("level")
    private Long level;
    @JsonProperty("privilege")
    private Long privilege;
    @JsonProperty("bitrate")
    private Long bitrate;


    public Long getSize(){
        return size;
    }

    public void setSize(Long size){
        this.size=size;
    }

    public String getHash(){
        return hash;
    }

    public void setHash(String hash){
        this.hash=hash;
    }

    public Long getLevel(){
        return level;
    }

    public void setLevel(Long level){
        this.level=level;
    }

    public Long getPrivilege(){
        return privilege;
    }

    public void setPrivilege(Long privilege){
        this.privilege=privilege;
    }

    public Long getBitrate(){
        return bitrate;
    }

    public void setBitrate(Long bitrate){
        this.bitrate=bitrate;
    }



    @Override
    public String toString() {
        return "RelateGoods{" +
                "size=" + size +
                ", hash='" + hash + "'" +
                ", level=" + level +
                ", privilege=" + privilege +
                ", bitrate=" + bitrate +
                "}";
    }


    @Override
    public int hashCode() {
        return Objects.hash(size, hash, level, privilege, bitrate);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RelateGoods other = (RelateGoods) o;
        return Objects.equals(this.size, other.size) && Objects.equals(this.hash, other.hash) && Objects.equals(this.level, other.level) && Objects.equals(this.privilege, other.privilege) && Objects.equals(this.bitrate, other.bitrate);
    }

}

