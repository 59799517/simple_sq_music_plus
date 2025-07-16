package com.sqmusicplus.v3.plug.kg.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Classname UserPlayList
 * @Description 用户收藏的歌单
 * @Version 1.0.0
 * @Date 2025/7/10 15:45
 * @Created by SQ
 */

@NoArgsConstructor
@Data
public class UserPlayListResult {


    @JsonProperty("status")
    private Integer status;
    @JsonProperty("error_code")
    private Integer errorCode;
    @JsonProperty("data")
    private DataDTO data;

    @NoArgsConstructor
    @Data
    public static class DataDTO {
        @JsonProperty("userid")
        private Long userid;
        @JsonProperty("info")
        private List<InfoDTO> info;
        @JsonProperty("total_ver")
        private Integer totalVer;
        @JsonProperty("phone_flag")
        private Integer phoneFlag;
        @JsonProperty("album_count")
        private Integer albumCount;
        @JsonProperty("list_count")
        private Integer listCount;
        @JsonProperty("collect_count")
        private Integer collectCount;

        @NoArgsConstructor
        @Data
        public static class InfoDTO {
            @JsonProperty("global_collection_id")
            private String globalCollectionId;
            @JsonProperty("per_num")
            private Integer perNum;
            @JsonProperty("is_drop")
            private Integer isDrop;
            @JsonProperty("is_pri")
            private Integer isPri;
            @JsonProperty("source")
            private Integer source;
            @JsonProperty("list_create_listid")
            private Integer listCreateListid;
            @JsonProperty("pub_time")
            private Integer pubTime;
            @JsonProperty("is_def")
            private Integer isDef;
            @JsonProperty("sound_quality")
            private String soundQuality;
            @JsonProperty("pub_type")
            private Integer pubType;
            @JsonProperty("list_ver")
            private Integer listVer;
            @JsonProperty("is_custom_pic")
            private Integer isCustomPic;
            @JsonProperty("update_time")
            private Integer updateTime;
            @JsonProperty("intro")
            private String intro;
            @JsonProperty("musiclib_tags")
            private List<?> musiclibTags;
            @JsonProperty("is_mine")
            private Integer isMine;
            @JsonProperty("is_del")
            private Integer isDel;
            @JsonProperty("kq_talent")
            private Integer kqTalent;
            @JsonProperty("per_count")
            private Integer perCount;
            @JsonProperty("is_edit")
            private Integer isEdit;
            @JsonProperty("is_publish")
            private Integer isPublish;
            @JsonProperty("m_count")
            private Integer mCount;
            @JsonProperty("list_create_gid")
            private String listCreateGid;
            @JsonProperty("create_user_pic")
            private String createUserPic;
            @JsonProperty("radio_id")
            private Integer radioId;
            @JsonProperty("create_time")
            private Integer createTime;
            @JsonProperty("tags")
            private String tags;
            @JsonProperty("status")
            private Integer status;
            @JsonProperty("name")
            private String name;
            @JsonProperty("count")
            private Integer count;
            @JsonProperty("sort")
            private Integer sort;
            @JsonProperty("pic")
            private String pic;
            @JsonProperty("listid")
            private Integer listid;
            @JsonProperty("pub_new")
            private Integer pubNew;
            @JsonProperty("list_create_userid")
            private Long listCreateUserid;
            @JsonProperty("type")
            private Integer type;
            @JsonProperty("is_per")
            private Integer isPer;
            @JsonProperty("is_featured")
            private Integer isFeatured;
            @JsonProperty("list_create_username")
            private String listCreateUsername;
        }
    }
}
