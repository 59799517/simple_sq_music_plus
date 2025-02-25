package com.sqmusicplus.plug.kg.entity;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @Classname LyricResult
 * @Description 歌词
 * @Version 1.0.0
 * @Date 2025/2/11 16:05
 * @Created by SQ
 */

public class LyricResult {


    @JSONField(name = "status")
    private Long status;
    @JSONField(name = "info")
    private String info;
    @JSONField(name = "error_code")
    private Long errorCode;
    @JSONField(name = "fmt")
    private String fmt;
    @JSONField(name = "contenttype")
    private Long contenttype;
    @JSONField(name = "_source")
    private String source;
    @JSONField(name = "charset")
    private String charset;
    @JSONField(name = "content")
    private String content;
    @JSONField(name = "id")
    private String id;
    @JSONField(name = "decodeContent")
    private String decodeContent;

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Long getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Long errorCode) {
        this.errorCode = errorCode;
    }

    public String getFmt() {
        return fmt;
    }

    public void setFmt(String fmt) {
        this.fmt = fmt;
    }

    public Long getContenttype() {
        return contenttype;
    }

    public void setContenttype(Long contenttype) {
        this.contenttype = contenttype;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDecodeContent() {
        return decodeContent;
    }

    public void setDecodeContent(String decodeContent) {
        this.decodeContent = decodeContent;
    }
}
