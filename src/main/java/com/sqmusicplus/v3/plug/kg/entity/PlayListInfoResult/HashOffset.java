package com.sqmusicplus.v3.plug.kg.entity.PlayListInfoResult;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class HashOffset {

    @JsonProperty("clip_hash")
    private String clipHash;
    @JsonProperty("start_byte")
    private Long startByte;
    @JsonProperty("file_type")
    private Long fileType;
    @JsonProperty("end_byte")
    private Long endByte;
    @JsonProperty("end_ms")
    private Long endMs;
    @JsonProperty("start_ms")
    private Long startMs;
    @JsonProperty("offset_hash")
    private String offsetHash;


    public String getClipHash(){
        return clipHash;
    }

    public void setClipHash(String clipHash){
        this.clipHash=clipHash;
    }

    public Long getStartByte(){
        return startByte;
    }

    public void setStartByte(Long startByte){
        this.startByte=startByte;
    }

    public Long getFileType(){
        return fileType;
    }

    public void setFileType(Long fileType){
        this.fileType=fileType;
    }

    public Long getEndByte(){
        return endByte;
    }

    public void setEndByte(Long endByte){
        this.endByte=endByte;
    }

    public Long getEndMs(){
        return endMs;
    }

    public void setEndMs(Long endMs){
        this.endMs=endMs;
    }

    public Long getStartMs(){
        return startMs;
    }

    public void setStartMs(Long startMs){
        this.startMs=startMs;
    }

    public String getOffsetHash(){
        return offsetHash;
    }

    public void setOffsetHash(String offsetHash){
        this.offsetHash=offsetHash;
    }



    @Override
    public String toString() {
        return "HashOffset{" +
                "clipHash='" + clipHash + "'" +
                ", startByte=" + startByte +
                ", fileType=" + fileType +
                ", endByte=" + endByte +
                ", endMs=" + endMs +
                ", startMs=" + startMs +
                ", offsetHash='" + offsetHash + "'" +
                "}";
    }


    @Override
    public int hashCode() {
        return Objects.hash(clipHash, startByte, fileType, endByte, endMs, startMs, offsetHash);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HashOffset other = (HashOffset) o;
        return Objects.equals(this.clipHash, other.clipHash) && Objects.equals(this.startByte, other.startByte) && Objects.equals(this.fileType, other.fileType) && Objects.equals(this.endByte, other.endByte) && Objects.equals(this.endMs, other.endMs) && Objects.equals(this.startMs, other.startMs) && Objects.equals(this.offsetHash, other.offsetHash);
    }

}

