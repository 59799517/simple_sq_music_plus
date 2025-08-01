package com.sqmusicplus.v3.base.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;


/**
 * @Classname SqConfig
 * @Description 系统设置
 * @Version 1.0.0
 * @Date 2022/10/21 10:45
 * @Update 2025年7月15
 * @Created by SQ
 */
@Data
@TableName(value = "sq_config")
public class SqConfig implements Serializable {
    public static final String COL_CONFIG_ID = "config_id";
    public static final String COL_CONFIG_NAME = "config_name";
    public static final String COL_CONFIG_VALUE = "config_value";
    public static final String COL_CONFIG_KEY = "config_key";
    public static final String COL_CONFIG_TYPE = "config_type";
    public static final String COL_CONFIG_SHOW = "config_show";
    public static final String COL_CONFIG_REMARK = "config_remark";
    public static final String COL_CONFIG_NULL_CHECK = "config_null_check";
    public static final String COL_CONFIG_DISABLED = "config_disabled";
    @TableId(value = "config_id", type = IdType.AUTO)
    private Integer configId;
    @TableField(value = "config_name")
    private String configName;
    @TableField(value = "config_value")
    private String configValue;
    @TableField(value = "config_key")
    private String configKey;
    @TableField(value = "config_type")
    private String type;
    @TableField(value = "config_show")
    private Integer configShow;
    @TableField(value = "config_remark")
    private String configRemark;
    @TableField(value = "config_null_check")
    private Integer configNullCheck;
    @TableField(value = "config_disabled")
    private Integer configDisabled;
}
