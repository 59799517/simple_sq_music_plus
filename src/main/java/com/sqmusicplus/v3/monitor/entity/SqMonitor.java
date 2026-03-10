package com.sqmusicplus.v3.monitor.entity;

import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Classname SqMonitor
 * @Description
 * @Version 1.0.0
 * @Date 2026/3/2
 * @Created by SQ
 */
@Data
@TableName(value = "sq_monitor")
public class SqMonitor implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 插件名称
     */
    @TableField(value = "plug_name")
    private String plugName;
    /**
     * 监控类型
     */
    @TableField(value = "type")
    private String type;
    /**
     * 是否启用
     */
    @TableField(value = "enabled")
    private String enabled;
    /**
     * 目标id
     */
    @TableField(value = "target_id")
    private String targetId;
    /**
     * 创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_time")
    private Date createTime;
    /**
     * 更新时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 名称
     */
    @TableField(value = "target_name")
    private String targetName;


    /**
     * 链接
     */
    @TableField(value = "target_url")
    private String targetUrl;

    /**
     * 数量
     */
    @TableField(value = "target_count")
    private Long targetCount;
    /**
     * 描述
     */
    @TableField(value = "target_desc")
    private String targetDesc;
    /**
     * 封面
     */
    @TableField(value = "target_cover")
    private String targetCover;

}
