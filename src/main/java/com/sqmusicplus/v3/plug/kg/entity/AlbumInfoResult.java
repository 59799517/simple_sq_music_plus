package com.sqmusicplus.v3.plug.kg.entity;

import com.alibaba.fastjson2.annotation.JSONField;

import java.util.List;

/**
 * @Classname AlbumInfoResult
 * @Description 专辑信息返回值（不包含歌曲信息）
 * @Version 1.0.0
 * @Date 2025/2/11 17:56
 * @Created by SQ
 */

public class AlbumInfoResult {


    @JSONField(name = "status")
    private Long status;
    @JSONField(name = "error_code")
    private Long errorCode;
    @JSONField(name = "errmsg")
    private String errmsg;
    @JSONField(name = "data")
    private List<DataDTO> data;

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

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public List<DataDTO> getData() {
        return data;
    }

    public void setData(List<DataDTO> data) {
        this.data = data;
    }

    public static class DataDTO {
        @JSONField(name = "trans_param")
        private TransParamDTO transParam;
        @JSONField(name = "language")
        private String language;
        @JSONField(name = "grade_count")
        private String gradeCount;
        @JSONField(name = "intro")
        private String intro;
        @JSONField(name = "heat")
        private String heat;
        @JSONField(name = "category")
        private String category;
        @JSONField(name = "sizable_cover")
        private String sizableCover;
        @JSONField(name = "cover")
        private String cover;
        @JSONField(name = "album_name")
        private String albumName;
        @JSONField(name = "type")
        private String type;
        @JSONField(name = "quality")
        private String quality;
        @JSONField(name = "publish_company")
        private String publishCompany;
        @JSONField(name = "grade")
        private String grade;
        @JSONField(name = "special_tag")
        private String specialTag;
        @JSONField(name = "author_name")
        private String authorName;
        @JSONField(name = "publish_date")
        private String publishDate;
        @JSONField(name = "language_id")
        private String languageId;
        @JSONField(name = "album_id")
        private String albumId;
        @JSONField(name = "exclusive")
        private String exclusive;
        @JSONField(name = "is_publish")
        private String isPublish;
        @JSONField(name = "authors")
        private List<AuthorsDTO> authors;

        public TransParamDTO getTransParam() {
            return transParam;
        }

        public void setTransParam(TransParamDTO transParam) {
            this.transParam = transParam;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public String getGradeCount() {
            return gradeCount;
        }

        public void setGradeCount(String gradeCount) {
            this.gradeCount = gradeCount;
        }

        public String getIntro() {
            return intro;
        }

        public void setIntro(String intro) {
            this.intro = intro;
        }

        public String getHeat() {
            return heat;
        }

        public void setHeat(String heat) {
            this.heat = heat;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getSizableCover() {
            return sizableCover;
        }

        public void setSizableCover(String sizableCover) {
            this.sizableCover = sizableCover;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public String getAlbumName() {
            return albumName;
        }

        public void setAlbumName(String albumName) {
            this.albumName = albumName;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getQuality() {
            return quality;
        }

        public void setQuality(String quality) {
            this.quality = quality;
        }

        public String getPublishCompany() {
            return publishCompany;
        }

        public void setPublishCompany(String publishCompany) {
            this.publishCompany = publishCompany;
        }

        public String getGrade() {
            return grade;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }

        public String getSpecialTag() {
            return specialTag;
        }

        public void setSpecialTag(String specialTag) {
            this.specialTag = specialTag;
        }

        public String getAuthorName() {
            return authorName;
        }

        public void setAuthorName(String authorName) {
            this.authorName = authorName;
        }

        public String getPublishDate() {
            return publishDate;
        }

        public void setPublishDate(String publishDate) {
            this.publishDate = publishDate;
        }

        public String getLanguageId() {
            return languageId;
        }

        public void setLanguageId(String languageId) {
            this.languageId = languageId;
        }

        public String getAlbumId() {
            return albumId;
        }

        public void setAlbumId(String albumId) {
            this.albumId = albumId;
        }

        public String getExclusive() {
            return exclusive;
        }

        public void setExclusive(String exclusive) {
            this.exclusive = exclusive;
        }

        public String getIsPublish() {
            return isPublish;
        }

        public void setIsPublish(String isPublish) {
            this.isPublish = isPublish;
        }

        public List<AuthorsDTO> getAuthors() {
            return authors;
        }

        public void setAuthors(List<AuthorsDTO> authors) {
            this.authors = authors;
        }

        public static class TransParamDTO {
            @JSONField(name = "special_tag")
            private String specialTag;

            public String getSpecialTag() {
                return specialTag;
            }

            public void setSpecialTag(String specialTag) {
                this.specialTag = specialTag;
            }
        }

        public static class AuthorsDTO {
            @JSONField(name = "author_id")
            private String authorId;
            @JSONField(name = "author_name")
            private String authorName;

            public String getAuthorId() {
                return authorId;
            }

            public void setAuthorId(String authorId) {
                this.authorId = authorId;
            }

            public String getAuthorName() {
                return authorName;
            }

            public void setAuthorName(String authorName) {
                this.authorName = authorName;
            }
        }
    }
}
