-- MySQL DDL 会立即提交；迁移失败后重试时保留已经添加的字段。
SET @sqmusic_retry_ddl = IF(
    EXISTS (SELECT 1 FROM information_schema.columns
            WHERE table_schema = DATABASE() AND table_name = 'download_info'
              AND column_name = 'download_retry_num'),
    'SELECT 1',
    'ALTER TABLE `download_info` ADD COLUMN `download_retry_num` int(5) NULL COMMENT ''下载的重试次数'' AFTER `parent_download_id`'
);
PREPARE sqmusic_retry_statement FROM @sqmusic_retry_ddl;
EXECUTE sqmusic_retry_statement;
DEALLOCATE PREPARE sqmusic_retry_statement;

SET @sqmusic_retry_ddl = IF(
    EXISTS (SELECT 1 FROM information_schema.columns
            WHERE table_schema = DATABASE() AND table_name = 'download_info'
              AND column_name = 'download_retry_time'),
    'SELECT 1',
    'ALTER TABLE `download_info` ADD COLUMN `download_retry_time` datetime NULL COMMENT ''最后一次重试的时间'' AFTER `download_retry_num`'
);
PREPARE sqmusic_retry_statement FROM @sqmusic_retry_ddl;
EXECUTE sqmusic_retry_statement;
DEALLOCATE PREPARE sqmusic_retry_statement;

-- 增加设置信息
INSERT INTO `sq_config` (`config_name`, `config_value`, `config_key`, `config_type`, `config_options`, `config_show`, `config_remark`, `config_null_check`, `config_disabled`)
SELECT '下载超时歌曲重试间隔时长（分）', '720', 'system.download.timeout.retry.interval', 'number', NULL, 1, '默认12小时后重试--有些接口调用太多会超时需要间隔时间比较久', 1, 0
WHERE NOT EXISTS (SELECT 1 FROM `sq_config` WHERE `config_key` = 'system.download.timeout.retry.interval');
INSERT INTO `sq_config` (`config_name`, `config_value`, `config_key`, `config_type`, `config_options`, `config_show`, `config_remark`, `config_null_check`, `config_disabled`)
SELECT '插件下载超时重试次数', '3', 'system.download.timeout.retry.num', 'number', NULL, 1, '插件下载超时重试次数', 1, 0
WHERE NOT EXISTS (SELECT 1 FROM `sq_config` WHERE `config_key` = 'system.download.timeout.retry.num');

-- 修改默认网易音乐源
UPDATE `sq_config` SET `config_value` = 'https://zm.wwoyun.cn;https://ncm.landdy.cn' WHERE config_key = 'plug.netease.baseurl';
UPDATE `sq_config` SET `config_disabled` = 0 WHERE `config_key` = 'system.show.traffic.monitoring';
