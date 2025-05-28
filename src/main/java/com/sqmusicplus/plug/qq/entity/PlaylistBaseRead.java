package com.sqmusicplus.plug.qq.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @Classname PlaylistBaseRead
 * @Description TODO
 * @Version 1.0.0
 * @Date 2025/5/26 15:07
 * @Created by SQ
 */
@Data
public class PlaylistBaseRead {


    @JsonProperty("code")
    private Long code;
    @JsonProperty("data")
    private DataDTO data;
@Data
    public static class DataDTO {
        @JsonProperty("total")
        private Long total;
        @JsonProperty("v_playlist")
        private List<VPlaylistDTO> vPlaylist;
        @JsonProperty("v_delTid")
        private List<?> vDeltid;
        @JsonProperty("bFinish")
        private Boolean bFinish;

@Data
        public static class VPlaylistDTO {
            @JsonProperty("dirId")
            private Long dirId;
            @JsonProperty("dirName")
            private String dirName;
            @JsonProperty("tid")
            private Long tid;
            @JsonProperty("opType")
            private Long opType;
            @JsonProperty("songNum")
            private Long songNum;
            @JsonProperty("createTime")
            private Long createTime;
            @JsonProperty("updateTime")
            private Long updateTime;
            @JsonProperty("dirShow")
            private Long dirShow;
            @JsonProperty("status")
            private Long status;
            @JsonProperty("picUrl")
            private String picUrl;
            @JsonProperty("bigpicUrl")
            private String bigpicUrl;
            @JsonProperty("uin")
            private String uin;
            @JsonProperty("sortWeight")
            private Long sortWeight;
            @JsonProperty("strTagIdList")
            private String strTagIdList;
            @JsonProperty("invalid")
            private Boolean invalid;
            @JsonProperty("nick")
            private String nick;
            @JsonProperty("albumPicUrl")
            private String albumPicUrl;
            @JsonProperty("desc")
            private String desc;
            @JsonProperty("avatar")
            private String avatar;
            @JsonProperty("identIcon")
            private String identIcon;
            @JsonProperty("layerUrl")
            private String layerUrl;
            @JsonProperty("ext1")
            private String ext1;
            @JsonProperty("ext2")
            private String ext2;
            @JsonProperty("tagNameList")
            private String tagNameList;
            @JsonProperty("ext")
            private Object ext;
        }
    }
}
