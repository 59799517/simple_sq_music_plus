package com.sqmusicplus.v3.plug.kg.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Classname SearchResult
 * @Description 歌手搜索返回结果
 * @Version 1.0.0
 * @Date 2025/2/6 15:26
 * @Created by SQ
 */

@NoArgsConstructor
@Data
public class SearchArtistResult {


    @JsonProperty("status")
    private Long status;
    @JsonProperty("error_code")
    private Long errorCode;
    @JsonProperty("error_msg")
    private String errorMsg;
    @JsonProperty("data")
    private DataDTO data;

    @NoArgsConstructor
    @Data
    public static class DataDTO {
        @JsonProperty("pagesize")
        private Long pagesize;
        @JsonProperty("page")
        private Long page;
        @JsonProperty("from")
        private Long from;
        @JsonProperty("size")
        private Long size;
        @JsonProperty("total")
        private Long total;
        @JsonProperty("correctiontype")
        private Long correctiontype;
        @JsonProperty("correctionforce")
        private Long correctionforce;
        @JsonProperty("correctiontip")
        private String correctiontip;
        @JsonProperty("lists")
        private List<ListsDTO> lists;

        @NoArgsConstructor
        @Data
        public static class ListsDTO {
            @JsonProperty("AuthorId")
            private Long authorId;
            @JsonProperty("AuthorName")
            private String authorName;
            @JsonProperty("IsSettledAuthor")
            private Long isSettledAuthor;
            @JsonProperty("Avatar")
            private String avatar;
            @JsonProperty("Heat")
            private Long heat;
            @JsonProperty("Auxiliary")
            private String auxiliary;
            @JsonProperty("AlbumCount")
            private Long albumCount;
            @JsonProperty("AudioCount")
            private Long audioCount;
            @JsonProperty("VideoCount")
            private Long videoCount;
            @JsonProperty("LyricistCount")
            private Long lyricistCount;
            @JsonProperty("ComposerCount")
            private Long composerCount;
            @JsonProperty("Identity")
            private Long identity;
            @JsonProperty("ComplexSongName")
            private String complexSongName;
            @JsonProperty("FansNum")
            private Long fansNum;
            @JsonProperty("UserId")
            private Long userId;
            @JsonProperty("AuthorStatus")
            private Long authorStatus;
            @JsonProperty("AlgPath")
            private String algPath;
        }
    }
}
