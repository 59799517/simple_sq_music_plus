package com.sqmusicplus.plug.kg.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * @Classname SearchResult
 * @Description 歌手搜索返回结果
 * @Version 1.0.0
 * @Date 2025/2/6 15:26
 * @Created by SQ
 */

public class SearchArtistResult {


    @JSONField(name = "status")
    private Long status;
    @JSONField(name = "error_code")
    private Long errorCode;
    @JSONField(name = "error_msg")
    private String errorMsg;
    @JSONField(name = "data")
    private DataDTO data;

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Long getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Long errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public DataDTO getData() {
        return data;
    }

    public void setData(DataDTO data) {
        this.data = data;
    }

    public static class DataDTO {
        @JSONField(name = "pagesize")
        private Long pagesize;
        @JSONField(name = "page")
        private Long page;
        @JSONField(name = "from")
        private Long from;
        @JSONField(name = "size")
        private Long size;
        @JSONField(name = "total")
        private Long total;
        @JSONField(name = "correctiontype")
        private Long correctiontype;
        @JSONField(name = "correctionforce")
        private Long correctionforce;
        @JSONField(name = "correctiontip")
        private String correctiontip;
        @JSONField(name = "lists")
        private List<ListsDTO> lists;

        public Long getPagesize() {
            return pagesize;
        }

        public void setPagesize(Long pagesize) {
            this.pagesize = pagesize;
        }

        public Long getPage() {
            return page;
        }

        public void setPage(Long page) {
            this.page = page;
        }

        public Long getFrom() {
            return from;
        }

        public void setFrom(Long from) {
            this.from = from;
        }

        public Long getSize() {
            return size;
        }

        public void setSize(Long size) {
            this.size = size;
        }

        public Long getTotal() {
            return total;
        }

        public void setTotal(Long total) {
            this.total = total;
        }

        public Long getCorrectiontype() {
            return correctiontype;
        }

        public void setCorrectiontype(Long correctiontype) {
            this.correctiontype = correctiontype;
        }

        public Long getCorrectionforce() {
            return correctionforce;
        }

        public void setCorrectionforce(Long correctionforce) {
            this.correctionforce = correctionforce;
        }

        public String getCorrectiontip() {
            return correctiontip;
        }

        public void setCorrectiontip(String correctiontip) {
            this.correctiontip = correctiontip;
        }

        public List<ListsDTO> getLists() {
            return lists;
        }

        public void setLists(List<ListsDTO> lists) {
            this.lists = lists;
        }

        public static class ListsDTO {
            @JSONField(name = "AuthorId")
            private Long authorId;
            @JSONField(name = "AuthorName")
            private String authorName;
            @JSONField(name = "IsSettledAuthor")
            private Long isSettledAuthor;
            @JSONField(name = "Avatar")
            private String avatar;
            @JSONField(name = "Heat")
            private Long heat;
            @JSONField(name = "Auxiliary")
            private String auxiliary;
            @JSONField(name = "AlbumCount")
            private Long albumCount;
            @JSONField(name = "AudioCount")
            private Long audioCount;
            @JSONField(name = "VideoCount")
            private Long videoCount;
            @JSONField(name = "ComplexSongName")
            private String complexSongName;
            @JSONField(name = "FansNum")
            private Long fansNum;
            @JSONField(name = "UserId")
            private Long userId;
            @JSONField(name = "AuthorStatus")
            private Long authorStatus;
            @JSONField(name = "AlgPath")
            private String algPath;

            public Long getAuthorId() {
                return authorId;
            }

            public void setAuthorId(Long authorId) {
                this.authorId = authorId;
            }

            public String getAuthorName() {
                return authorName;
            }

            public void setAuthorName(String authorName) {
                this.authorName = authorName;
            }

            public Long getIsSettledAuthor() {
                return isSettledAuthor;
            }

            public void setIsSettledAuthor(Long isSettledAuthor) {
                this.isSettledAuthor = isSettledAuthor;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public Long getHeat() {
                return heat;
            }

            public void setHeat(Long heat) {
                this.heat = heat;
            }

            public String getAuxiliary() {
                return auxiliary;
            }

            public void setAuxiliary(String auxiliary) {
                this.auxiliary = auxiliary;
            }

            public Long getAlbumCount() {
                return albumCount;
            }

            public void setAlbumCount(Long albumCount) {
                this.albumCount = albumCount;
            }

            public Long getAudioCount() {
                return audioCount;
            }

            public void setAudioCount(Long audioCount) {
                this.audioCount = audioCount;
            }

            public Long getVideoCount() {
                return videoCount;
            }

            public void setVideoCount(Long videoCount) {
                this.videoCount = videoCount;
            }

            public String getComplexSongName() {
                return complexSongName;
            }

            public void setComplexSongName(String complexSongName) {
                this.complexSongName = complexSongName;
            }

            public Long getFansNum() {
                return fansNum;
            }

            public void setFansNum(Long fansNum) {
                this.fansNum = fansNum;
            }

            public Long getUserId() {
                return userId;
            }

            public void setUserId(Long userId) {
                this.userId = userId;
            }

            public Long getAuthorStatus() {
                return authorStatus;
            }

            public void setAuthorStatus(Long authorStatus) {
                this.authorStatus = authorStatus;
            }

            public String getAlgPath() {
                return algPath;
            }

            public void setAlgPath(String algPath) {
                this.algPath = algPath;
            }
        }
    }
}
