package com.sqmusicplus.v3.base.entity.vo;

import lombok.Data;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Classname SearchMusicParam
 * @Description 搜索参数
 * @Version 1.0.0
 * @Date 2025/7/25 09:57
 * @Created by SQ
 */
@Data
public class SearchMusicParam {
    /**
     * 搜索插件名称
     */
    private String plugName;
    /**
     * 搜索平关键字
     */
    private String keyword;
    /**
     * 每页数量
     */
    private  Integer pageSize;
    /**
     * 页码
     */
    private Integer pageIndex;


}
