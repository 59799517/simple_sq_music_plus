package com.sqmusicplus.v3.plug.kg.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * @Classname ArtistAlubmResult
 * @Description 歌手专辑返回值
 * @Version 1.0.0
 * @Date 2025/2/12 11:19
 * @Created by SQ
 */

public class ArtistAlubmResult {


    @JSONField(name = "total")
    private Long total;
    @JSONField(name = "error_code")
    private Long errorCode;
    @JSONField(name = "data")
    private List<DataDTO> data;
    @JSONField(name = "extra")
    private ExtraDTO extra;
    @JSONField(name = "status")
    private Long status;
    @JSONField(name = "errmsg")
    private String errmsg;

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Long getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Long errorCode) {
        this.errorCode = errorCode;
    }

    public List<DataDTO> getData() {
        return data;
    }

    public void setData(List<DataDTO> data) {
        this.data = data;
    }

    public ExtraDTO getExtra() {
        return extra;
    }

    public void setExtra(ExtraDTO extra) {
        this.extra = extra;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public static class ExtraDTO {
        @JSONField(name = "page_total")
        private Long pageTotal;

        public Long getPageTotal() {
            return pageTotal;
        }

        public void setPageTotal(Long pageTotal) {
            this.pageTotal = pageTotal;
        }
    }

    public static class DataDTO {
        @JSONField(name = "publish_company")
        private String publishCompany;
        @JSONField(name = "author_name")
        private String authorName;
        @JSONField(name = "sizable_cover")
        private String sizableCover;
        @JSONField(name = "category")
        private Long category;
        @JSONField(name = "grade_count")
        private Long gradeCount;
        @JSONField(name = "special_tag")
        private String specialTag;
        @JSONField(name = "privilege_download")
        private PrivilegeDownloadDTO privilegeDownload;
        @JSONField(name = "album_name")
        private String albumName;
        @JSONField(name = "is_publish")
        private Long isPublish;
        @JSONField(name = "goods_info")
        private GoodsInfoDTO goodsInfo;
        @JSONField(name = "grade")
        private Long grade;
        @JSONField(name = "heat")
        private Long heat;
        @JSONField(name = "exclusive")
        private Long exclusive;
        @JSONField(name = "sum_ownercount")
        private Long sumOwnercount;
        @JSONField(name = "language_id")
        private Long languageId;
        @JSONField(name = "quality")
        private Long quality;
        @JSONField(name = "publish_date")
        private String publishDate;
        @JSONField(name = "album_id")
        private Long albumId;
        @JSONField(name = "cover")
        private String cover;
        @JSONField(name = "authors")
        private List<AuthorsDTO> authors;
        @JSONField(name = "language")
        private String language;
        @JSONField(name = "intro")
        private String intro;
        @JSONField(name = "type")
        private String type;

        public String getPublishCompany() {
            return publishCompany;
        }

        public void setPublishCompany(String publishCompany) {
            this.publishCompany = publishCompany;
        }

        public String getAuthorName() {
            return authorName;
        }

        public void setAuthorName(String authorName) {
            this.authorName = authorName;
        }

        public String getSizableCover() {
            return sizableCover;
        }

        public void setSizableCover(String sizableCover) {
            this.sizableCover = sizableCover;
        }

        public Long getCategory() {
            return category;
        }

        public void setCategory(Long category) {
            this.category = category;
        }

        public Long getGradeCount() {
            return gradeCount;
        }

        public void setGradeCount(Long gradeCount) {
            this.gradeCount = gradeCount;
        }

        public String getSpecialTag() {
            return specialTag;
        }

        public void setSpecialTag(String specialTag) {
            this.specialTag = specialTag;
        }

        public PrivilegeDownloadDTO getPrivilegeDownload() {
            return privilegeDownload;
        }

        public void setPrivilegeDownload(PrivilegeDownloadDTO privilegeDownload) {
            this.privilegeDownload = privilegeDownload;
        }

        public String getAlbumName() {
            return albumName;
        }

        public void setAlbumName(String albumName) {
            this.albumName = albumName;
        }

        public Long getIsPublish() {
            return isPublish;
        }

        public void setIsPublish(Long isPublish) {
            this.isPublish = isPublish;
        }

        public GoodsInfoDTO getGoodsInfo() {
            return goodsInfo;
        }

        public void setGoodsInfo(GoodsInfoDTO goodsInfo) {
            this.goodsInfo = goodsInfo;
        }

        public Long getGrade() {
            return grade;
        }

        public void setGrade(Long grade) {
            this.grade = grade;
        }

        public Long getHeat() {
            return heat;
        }

        public void setHeat(Long heat) {
            this.heat = heat;
        }

        public Long getExclusive() {
            return exclusive;
        }

        public void setExclusive(Long exclusive) {
            this.exclusive = exclusive;
        }

        public Long getSumOwnercount() {
            return sumOwnercount;
        }

        public void setSumOwnercount(Long sumOwnercount) {
            this.sumOwnercount = sumOwnercount;
        }

        public Long getLanguageId() {
            return languageId;
        }

        public void setLanguageId(Long languageId) {
            this.languageId = languageId;
        }

        public Long getQuality() {
            return quality;
        }

        public void setQuality(Long quality) {
            this.quality = quality;
        }

        public String getPublishDate() {
            return publishDate;
        }

        public void setPublishDate(String publishDate) {
            this.publishDate = publishDate;
        }

        public Long getAlbumId() {
            return albumId;
        }

        public void setAlbumId(Long albumId) {
            this.albumId = albumId;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public List<AuthorsDTO> getAuthors() {
            return authors;
        }

        public void setAuthors(List<AuthorsDTO> authors) {
            this.authors = authors;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public String getIntro() {
            return intro;
        }

        public void setIntro(String intro) {
            this.intro = intro;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public static class PrivilegeDownloadDTO {
            @JSONField(name = "privilege")
            private Long privilege;
            @JSONField(name = "fail_process")
            private Long failProcess;

            public Long getPrivilege() {
                return privilege;
            }

            public void setPrivilege(Long privilege) {
                this.privilege = privilege;
            }

            public Long getFailProcess() {
                return failProcess;
            }

            public void setFailProcess(Long failProcess) {
                this.failProcess = failProcess;
            }
        }

        public static class GoodsInfoDTO {
            @JSONField(name = "could_buy_album")
            private String couldBuyAlbum;
            @JSONField(name = "album_pay_type")
            private Long albumPayType;
            @JSONField(name = "album_sale_url")
            private String albumSaleUrl;
            @JSONField(name = "album_price")
            private Long albumPrice;

            public String getCouldBuyAlbum() {
                return couldBuyAlbum;
            }

            public void setCouldBuyAlbum(String couldBuyAlbum) {
                this.couldBuyAlbum = couldBuyAlbum;
            }

            public Long getAlbumPayType() {
                return albumPayType;
            }

            public void setAlbumPayType(Long albumPayType) {
                this.albumPayType = albumPayType;
            }

            public String getAlbumSaleUrl() {
                return albumSaleUrl;
            }

            public void setAlbumSaleUrl(String albumSaleUrl) {
                this.albumSaleUrl = albumSaleUrl;
            }

            public Long getAlbumPrice() {
                return albumPrice;
            }

            public void setAlbumPrice(Long albumPrice) {
                this.albumPrice = albumPrice;
            }
        }

        public static class AuthorsDTO {
            @JSONField(name = "author_name")
            private String authorName;
            @JSONField(name = "author_id")
            private Long authorId;

            public String getAuthorName() {
                return authorName;
            }

            public void setAuthorName(String authorName) {
                this.authorName = authorName;
            }

            public Long getAuthorId() {
                return authorId;
            }

            public void setAuthorId(Long authorId) {
                this.authorId = authorId;
            }
        }
    }
}
