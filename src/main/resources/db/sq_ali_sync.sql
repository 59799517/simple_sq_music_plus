create table sqmusicv3.sq_ali_sync
(
    id            int auto_increment
        primary key,
    sha1          varchar(255) charset utf8mb4 null comment 'sha1',
    md5           varchar(255) charset utf8mb4 null comment 'MD5',
    sharding_sha1 varchar(255) charset utf8mb4 null comment '分片1ksha1',
    path          text charset utf8mb4         null comment '文件路径',
    ali_id_path   text charset utf8mb4         null comment '阿里云盘id路径',
    ali_path      text charset utf8mb4         null comment '阿里云盘路径',
    name          varchar(255) charset utf8mb4 null comment '文件名称',
    music_name    varchar(255) charset utf8mb4 null comment '歌曲名称',
    music_artist  varchar(255) charset utf8mb4 null comment '歌手',
    music_album   varchar(255) charset utf8mb4 null comment '专辑名称',
    suffix        varchar(255) charset utf8mb4 null comment '文件后缀',
    upload_time   datetime                     null comment '上传时间',
    result        varchar(255) charset utf8mb4 null comment '结果',
    rapid         int(1)                       null comment '是否秒传1是0否',
    download_id   int(50)                      null comment '下载表id'
)
    charset = utf8;

