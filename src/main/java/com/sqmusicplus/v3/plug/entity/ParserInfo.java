package com.sqmusicplus.v3.plug.entity;


import lombok.Data;

/**
 * @Classname ParserInfo
 * @Description
 * @Version 1.0.0
 * @Date 2026/3/3
 * @Created by SQ
 */
@Data
public class ParserInfo {
    /**
     * 名称
     */
    private String name;
    /**
     * 插件名称
     */
    private String plugNmae;
    /**
     * 唯一id
     */
    private String id;
    /**
     * 链接
     */
    private String url;
    /**
     *   类型
     */
    private String type;
    /**
     * 数量
     */
    private Long count;
    /**
     * 描述
     */
    private String desc;
    /**
     * 封面
     */
    private String cover;


}
