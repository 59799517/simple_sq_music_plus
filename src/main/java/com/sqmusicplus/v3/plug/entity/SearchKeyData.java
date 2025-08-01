package com.sqmusicplus.v3.plug.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created with IntelliJ IDEA.
 * User: SQ
 * Date: 2022/11/21
 * Time: 17:04
 * Description: 搜索关键字对象
 */
@Data
@Accessors(chain = true)
public class SearchKeyData {

    /**
     * 搜索关键字
     */
    String searchkey;
    /**
     * 插件名称
     */
    String plugName;
    /**
     * 页码
     */
    Integer pageIndex;
    /**
     * 每页长度
     */
    Integer pageSize;
}
