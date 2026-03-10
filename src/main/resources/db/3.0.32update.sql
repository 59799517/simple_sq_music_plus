-- 更新网易云接口地址错误的往后移动
UPDATE `sq_config` SET `config_value` = 'https://163api.qijieya.cn;http://dg-t.cn:3000;https://zm.armoe.cn;http://45.152.64.114:3005;https://apis.netstart.cn/music;https://wyy.xhily.com;http://plugin.changsheng.space:3000;' WHERE `config_key` = 'plug.netease.baseurl';
-- 为sq_config表新增options字段（文本类型，允许为空）
ALTER TABLE sq_config
    ADD COLUMN `config_options` TEXT NULL COMMENT '配置选项（自定义参数）' AFTER `config_type`;

-- 增加下载文件音频格式设置
INSERT INTO `sq_config` (`config_id`, `config_name`, `config_value`, `config_key`, `config_type`, `config_options`, `config_show`, `config_remark`, `config_null_check`, `config_disabled`) VALUES (42, '下载文件音频格式', 'auto', 'system.download.file.audio.format', 'select', '[{\"label\":\"自动\",\"value\":\"auto\"},{\"label\":\"只下载flac\",\"value\":\"flac\"},{\"label\":\"只下载mp3\",\"value\":\"mp3\"}]', 1, '自动则是默认下载最高音质，有选择的格式则下载指定的格式', 1, 0);
-- 增加监听表
CREATE TABLE `sq_monitor`  (
    `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
    `plug_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '插件名称',
    `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '监听类型',
    `enabled` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '是否开启1开启0关闭',
    `target_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '目标id',
    `target_name` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '名称',
    `target_desc` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '描述',
    `target_url` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '插入的url',
    `target_count` int(11) NULL DEFAULT NULL COMMENT '数量',
    `target_cover` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '封面图片',
    `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE
    ) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '监听扫扫描歌单' ROW_FORMAT = Dynamic;
