package com.sqmusicplus.v3.base.entity.vo;

import lombok.Data;

/**
 * @Classname SearchMusicByIdParam
 * @Description 搜索ID
 * @Version 1.0.0
 * @Date 2025/7/25 10:03
 * @Created by SQ
 */
@Data
public class SearchMusicByIdParam {
    /**
     * 搜索插件名称
     */
    private String plugName;
    /**
     * 搜索的ID
     */
    private String id;
}
