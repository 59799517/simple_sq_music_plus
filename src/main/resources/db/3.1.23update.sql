-- 增加重试次数字段 和 重试最后一次的时间
ALTER TABLE `download_info`
    ADD COLUMN `download_retry_num` int(5) NULL COMMENT '下载的重试次数' AFTER `parent_download_id`,
ADD COLUMN `download_retry_time` datetime NULL COMMENT '最后一次重试的时间' AFTER `download_retry_num`;


-- 增加设置信息
INSERT INTO `sq_config` (`config_name`, `config_value`, `config_key`, `config_type`, `config_options`, `config_show`, `config_remark`, `config_null_check`, `config_disabled`) VALUES ('下载超时歌曲重试间隔时长（分）', '720', 'system.download.timeout.retry.interval', 'number', NULL, 1, '默认12小时后重试--有些接口调用太多会超时需要间隔时间比较久', 1, 0);
INSERT INTO `sq_config` (`config_name`, `config_value`, `config_key`, `config_type`, `config_options`, `config_show`, `config_remark`, `config_null_check`, `config_disabled`) VALUES ( '插件下载超时重试次数', '3', 'system.download.timeout.retry.num', 'number', NULL, 1, '插件下载超时重试次数', 1, 0);

-- 修改默认网易音乐源
UPDATE `sq_config` SET `config_value` = 'https://zm.wwoyun.cn;https://ncm.landdy.cn' WHERE config_key = 'plug.netease.baseurl';
UPDATE `sq_config` SET `config_disabled` = 0 WHERE `config_key` = 'system.show.traffic.monitoring'