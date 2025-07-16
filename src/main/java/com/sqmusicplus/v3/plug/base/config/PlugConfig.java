package com.sqmusicplus.v3.plug.base.config;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: SQ
 * Date: 2022/11/22
 * Time: 10:10
 * Description: 插件规范
 */
@Data
public abstract class PlugConfig {
    /**
     * 标识
     */
    @NotNull
    private String id;
    /**
     * 插件名称
     */
    @NotNull
    private String name;

    /**
     * 插件版本
     */
    @NotNull
    private String version;
    /**
     * 该插件是否需要登录
     */
    private Boolean islogin;


}
