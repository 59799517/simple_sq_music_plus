package com.sqmusicplus.v3.alidrive.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Classname AliUploadResult
 * @Description 阿里云盘文件上传结果实体
 * @Version 1.0
 * @Date 2026年4月2日
 * @Created by SQ
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AliUploadResult {
    
    /**
     * 文件 ID
     */
    private String fileId;
    
    /**
     * 上传 ID（普通上传时有值，秒传时可能为空）
     */
    private String uploadId;
    
    /**
     * 文件名称
     */
    private String fileName;
    
    /**
     * 文件大小
     */
    private Long fileSize;
    
    /**
     * 文件类型（file/folder）
     */
    private String fileType;
    
    /**
     * 云盘 ID
     */
    private String driveId;
    
    /**
     * 父文件夹 ID
     */
    private String parentFileId;
    
    /**
     * 创建时间
     */
    private String createdAt;
    
    /**
     * 修改时间
     */
    private String updatedAt;
    
    /**
     * SHA1 哈希值（content_hash）
     */
    private String sha1;
    
    /**
     * ID 路径（id_path）
     * 示例：root:/64de0fb2e93179acac1a487389b7768c7c63d36c
     */
    private String idPath;
    
    /**
     * 名称路径（name_path）
     * 示例：root:/Love.Death.and.Robots.S03E02.1080p.VINEnc.mp4
     */
    private String namePath;
    
    /**
     * 文件扩展名
     */
    private String fileExtension;
    
    /**
     * 分类
     */
    private String category;
    
    /**
     * 缩略图
     */
    private String thumbnail;
    
    /**
     * 预览 URL
     */
    private String url;
    
    /**
     * 下载地址
     */
    private String downloadUrl;
    
    /**
     * 本地文件路径
     */
    private String localPath;
    
    /**
     * 阿里云盘路径
     */
    private String aliPath;

    /**
     * 阿里云盘id路径
     */
    private String aliIdPath;

    /**
     * 是否秒传成功
     */
    private Boolean rapidUpload;
    
    /**
     * 原始响应数据（用于获取其他未映射字段）
     */
    private com.alibaba.fastjson2.JSONObject rawData;
}
