package com.sqmusicplus.plug.kg.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * @Classname ArtistInfoResult
 * @Description 歌手详情
 * @Version 1.0.0
 * @Date 2025/2/11 16:51
 * @Created by SQ
 */

public class ArtistInfoResult {


    @JSONField(name = "data")
    private DataDTO data;
    @JSONField(name = "msg")
    private String msg;
    @JSONField(name = "status")
    private Long status;
    @JSONField(name = "error_code")
    private Long errorCode;

    public DataDTO getData() {
        return data;
    }

    public void setData(DataDTO data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

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

    public static class DataDTO {
        @JSONField(name = "birthday")
        private String birthday;
        @JSONField(name = "mv_count")
        private Long mvCount;
        @JSONField(name = "pinyin_initial")
        private String pinyinInitial;
        @JSONField(name = "author_name")
        private String authorName;
        @JSONField(name = "sizable_avatar")
        private String sizableAvatar;
        @JSONField(name = "is_publish")
        private Long isPublish;
        @JSONField(name = "author_id")
        private String authorId;
        @JSONField(name = "album_count")
        private Long albumCount;
        @JSONField(name = "fansnums")
        private Long fansnums;
        @JSONField(name = "long_intro")
        private List<LongIntroDTO> longIntro;
        @JSONField(name = "area_id")
        private String areaId;
        @JSONField(name = "song_count")
        private Long songCount;
        @JSONField(name = "intro")
        private String intro;
        @JSONField(name = "user_status")
        private Long userStatus;

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public Long getMvCount() {
            return mvCount;
        }

        public void setMvCount(Long mvCount) {
            this.mvCount = mvCount;
        }

        public String getPinyinInitial() {
            return pinyinInitial;
        }

        public void setPinyinInitial(String pinyinInitial) {
            this.pinyinInitial = pinyinInitial;
        }

        public String getAuthorName() {
            return authorName;
        }

        public void setAuthorName(String authorName) {
            this.authorName = authorName;
        }

        public String getSizableAvatar() {
            return sizableAvatar;
        }

        public void setSizableAvatar(String sizableAvatar) {
            this.sizableAvatar = sizableAvatar;
        }

        public Long getIsPublish() {
            return isPublish;
        }

        public void setIsPublish(Long isPublish) {
            this.isPublish = isPublish;
        }

        public String getAuthorId() {
            return authorId;
        }

        public void setAuthorId(String authorId) {
            this.authorId = authorId;
        }

        public Long getAlbumCount() {
            return albumCount;
        }

        public void setAlbumCount(Long albumCount) {
            this.albumCount = albumCount;
        }

        public Long getFansnums() {
            return fansnums;
        }

        public void setFansnums(Long fansnums) {
            this.fansnums = fansnums;
        }

        public List<LongIntroDTO> getLongIntro() {
            return longIntro;
        }

        public void setLongIntro(List<LongIntroDTO> longIntro) {
            this.longIntro = longIntro;
        }

        public String getAreaId() {
            return areaId;
        }

        public void setAreaId(String areaId) {
            this.areaId = areaId;
        }

        public Long getSongCount() {
            return songCount;
        }

        public void setSongCount(Long songCount) {
            this.songCount = songCount;
        }

        public String getIntro() {
            return intro;
        }

        public void setIntro(String intro) {
            this.intro = intro;
        }

        public Long getUserStatus() {
            return userStatus;
        }

        public void setUserStatus(Long userStatus) {
            this.userStatus = userStatus;
        }

        public static class LongIntroDTO {
            @JSONField(name = "content")
            private String content;
            @JSONField(name = "title")
            private String title;

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }
        }
    }
}
