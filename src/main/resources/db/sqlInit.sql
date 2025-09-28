-- sqmusicv3.download_info definition

CREATE TABLE `download_info` (
                                 `id` int(50) NOT NULL AUTO_INCREMENT,
                                 `download_gid` varchar(255) DEFAULT NULL COMMENT '全局id',
                                 `download_time` datetime DEFAULT NULL COMMENT '下载插入时间',
                                 `download_file` varchar(1000) DEFAULT NULL COMMENT '下载文件地址',
                                 `download_music_id` varchar(255) NOT NULL COMMENT '下载歌曲id',
                                 `download_plug_name` varchar(255) DEFAULT NULL COMMENT '插件名称',
                                 `download_br_type` varchar(255) DEFAULT NULL COMMENT '下载音质类型',
                                 `download_musicname` varchar(255) DEFAULT NULL COMMENT '歌曲名称',
                                 `download_artistname` varchar(255) DEFAULT NULL COMMENT '歌手名称',
                                 `download_albumname` varchar(255) DEFAULT NULL COMMENT '专辑名称',
                                 `download_msg` varchar(255) DEFAULT NULL COMMENT '下载信息错误信息',
                                 `version` varchar(255) DEFAULT NULL COMMENT '不需要',
                                 `download_music_info` longtext COMMENT '下载歌曲详细信息',
                                 `download_status` varchar(255) DEFAULT NULL COMMENT '下载状态',
                                 `spring_name` varchar(255) DEFAULT NULL COMMENT '下载使用的spring处理器',
                                 `audio_book` int(4) DEFAULT NULL COMMENT '是否是音频书1 是 0 否',
                                 `download_update_time` datetime DEFAULT NULL COMMENT '修改时间（多次下载后的时间）',
                                 `revision` int(3) DEFAULT NULL COMMENT '乐观锁',
                                 `rewrite_mp3tag` int(4) DEFAULT NULL COMMENT '是否重写MP3tag 1是 0否',
                                 `download_bits` varchar(255) DEFAULT NULL COMMENT '支持的bit 多个,分割',
                                 `download_br_types` varchar(255) DEFAULT NULL COMMENT '支持的类型列表多个,分割',
                                 PRIMARY KEY (`id`) USING BTREE,
                                 KEY `pk_download_albumname` (`download_albumname`) USING BTREE,
                                 KEY `pk_download_artistname` (`download_artistname`) USING BTREE,
                                 KEY `pk_download_musicname` (`download_musicname`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;


-- sqmusicv3.sq_config definition

CREATE TABLE `sq_config` (
                             `config_id` int(11) NOT NULL AUTO_INCREMENT,
                             `config_name` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '配置名称',
                             `config_value` longtext CHARACTER SET utf8 COMMENT '配置值',
                             `config_key` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '配置标识',
                             `config_type` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '类型',
                             `config_show` int(255) DEFAULT NULL COMMENT '是否显示1 是0 否',
                             `config_remark` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '配置备注',
                             `config_null_check` int(11) DEFAULT NULL COMMENT '开启空值检查 1 是 0 否',
                             `config_disabled` int(11) DEFAULT NULL COMMENT '禁止修改 1 是 0 否',
                             PRIMARY KEY (`config_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_croatian_ci ROW_FORMAT=DYNAMIC;


-- sqmusicv3.sq_sync definition

CREATE TABLE `sq_sync` (
                           `id` int(50) NOT NULL AUTO_INCREMENT COMMENT '主键',
                           `plug_name` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '插件名称',
                           `music_id` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '歌曲ID',
                           `music_info` longtext CHARACTER SET utf8 COMMENT '歌曲信息（禁止搜索）',
                           `download_id` int(50) DEFAULT NULL COMMENT '下载id',
                           `play_list_name` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '歌单名称最大50个字',
                           `play_list_sha1` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '歌单名称sha1避免歌单名称过长',
                           `play_list_id` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '歌单id',
                           `artist_id` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '歌手ID',
                           `artist_name` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '歌手名称',
                           `album_id` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '专辑ID',
                           `album_name` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '专辑名称',
                           `music_name` varchar(500) COLLATE utf8_croatian_ci DEFAULT NULL COMMENT '歌曲名称',
                           PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_croatian_ci ROW_FORMAT=DYNAMIC;