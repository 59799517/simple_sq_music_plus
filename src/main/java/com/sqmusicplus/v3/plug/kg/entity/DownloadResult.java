package com.sqmusicplus.v3.plug.kg.entity;

import com.alibaba.fastjson2.annotation.JSONField;

import java.util.List;

/**
 * @Classname DownloadResult
 * @Description 下载链接返回值
 * @Version 1.0.0
 * @Date 2025/2/12 14:12
 * @Created by SQ
 */

public class DownloadResult {


    @JSONField(name = "extName")
    private String extName;
    @JSONField(name = "classmap")
    private ClassmapDTO classmap;
    @JSONField(name = "status")
    private Long status;
    @JSONField(name = "volume")
    private Double volume;
    @JSONField(name = "std_hash_time")
    private Long stdHashTime;
    @JSONField(name = "backupUrl")
    private List<String> backupUrl;
    @JSONField(name = "url")
    private List<String> url;
    @JSONField(name = "std_hash")
    private String stdHash;
    @JSONField(name = "trans_param")
    private TransParamDTO transParam;
    @JSONField(name = "fileHead")
    private Long fileHead;
    @JSONField(name = "timeLength")
    private Long timeLength;
    @JSONField(name = "bitRate")
    private Long bitRate;
    @JSONField(name = "priv_status")
    private Long privStatus;
    @JSONField(name = "volume_peak")
    private Double volumePeak;
    @JSONField(name = "volume_gain")
    private Long volumeGain;
    @JSONField(name = "q")
    private Long q;
    @JSONField(name = "fileName")
    private String fileName;
    @JSONField(name = "fileSize")
    private Long fileSize;
    @JSONField(name = "hash")
    private String hash;

    public String getExtName() {
        return extName;
    }

    public void setExtName(String extName) {
        this.extName = extName;
    }

    public ClassmapDTO getClassmap() {
        return classmap;
    }

    public void setClassmap(ClassmapDTO classmap) {
        this.classmap = classmap;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    public Long getStdHashTime() {
        return stdHashTime;
    }

    public void setStdHashTime(Long stdHashTime) {
        this.stdHashTime = stdHashTime;
    }

    public List<String> getBackupUrl() {
        return backupUrl;
    }

    public void setBackupUrl(List<String> backupUrl) {
        this.backupUrl = backupUrl;
    }

    public List<String> getUrl() {
        return url;
    }

    public void setUrl(List<String> url) {
        this.url = url;
    }

    public String getStdHash() {
        return stdHash;
    }

    public void setStdHash(String stdHash) {
        this.stdHash = stdHash;
    }

    public TransParamDTO getTransParam() {
        return transParam;
    }

    public void setTransParam(TransParamDTO transParam) {
        this.transParam = transParam;
    }

    public Long getFileHead() {
        return fileHead;
    }

    public void setFileHead(Long fileHead) {
        this.fileHead = fileHead;
    }

    public Long getTimeLength() {
        return timeLength;
    }

    public void setTimeLength(Long timeLength) {
        this.timeLength = timeLength;
    }

    public Long getBitRate() {
        return bitRate;
    }

    public void setBitRate(Long bitRate) {
        this.bitRate = bitRate;
    }

    public Long getPrivStatus() {
        return privStatus;
    }

    public void setPrivStatus(Long privStatus) {
        this.privStatus = privStatus;
    }

    public Double getVolumePeak() {
        return volumePeak;
    }

    public void setVolumePeak(Double volumePeak) {
        this.volumePeak = volumePeak;
    }

    public Long getVolumeGain() {
        return volumeGain;
    }

    public void setVolumeGain(Long volumeGain) {
        this.volumeGain = volumeGain;
    }

    public Long getQ() {
        return q;
    }

    public void setQ(Long q) {
        this.q = q;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public static class ClassmapDTO {
        @JSONField(name = "attr0")
        private Long attr0;

        public Long getAttr0() {
            return attr0;
        }

        public void setAttr0(Long attr0) {
            this.attr0 = attr0;
        }
    }

    public static class TransParamDTO {
        @JSONField(name = "classmap")
        private ClassmapDTO classmap;
        @JSONField(name = "qualitymap")
        private QualitymapDTO qualitymap;
        @JSONField(name = "display")
        private Long display;
        @JSONField(name = "display_rate")
        private Long displayRate;

        public ClassmapDTO getClassmap() {
            return classmap;
        }

        public void setClassmap(ClassmapDTO classmap) {
            this.classmap = classmap;
        }

        public QualitymapDTO getQualitymap() {
            return qualitymap;
        }

        public void setQualitymap(QualitymapDTO qualitymap) {
            this.qualitymap = qualitymap;
        }

        public Long getDisplay() {
            return display;
        }

        public void setDisplay(Long display) {
            this.display = display;
        }

        public Long getDisplayRate() {
            return displayRate;
        }

        public void setDisplayRate(Long displayRate) {
            this.displayRate = displayRate;
        }

        public static class ClassmapDTO {
            @JSONField(name = "attr0")
            private Long attr0;

            public Long getAttr0() {
                return attr0;
            }

            public void setAttr0(Long attr0) {
                this.attr0 = attr0;
            }
        }

        public static class QualitymapDTO {
            @JSONField(name = "attr0")
            private Long attr0;
            @JSONField(name = "attr1")
            private Long attr1;

            public Long getAttr0() {
                return attr0;
            }

            public void setAttr0(Long attr0) {
                this.attr0 = attr0;
            }

            public Long getAttr1() {
                return attr1;
            }

            public void setAttr1(Long attr1) {
                this.attr1 = attr1;
            }
        }
    }
}
