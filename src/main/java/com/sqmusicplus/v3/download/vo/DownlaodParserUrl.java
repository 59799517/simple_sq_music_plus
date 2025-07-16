package com.sqmusicplus.v3.download.vo;

import lombok.Data;

/**
 * @Classname DownlaodAlubm
 * @Description 下载专辑对象
 * @Version 1.0.0
 * @Date 2022/12/14 11:18
 * @Created by shang
 */

@Data
public class DownlaodParserUrl {


    /**
     * 下载的或者解析的url地址
     */
    String url;
    /**
     * 码率
     */
    Integer br;
    /**
     * 是否是书籍类型
     */
    Boolean isAudioBook;
    /**
     * 书籍名称
     */
    String bookName;
    /**
     * 数据作者
     */
    String artist;

    /**
     * 插件名称
     */
    String plugType;

}
