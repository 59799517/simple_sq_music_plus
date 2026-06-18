-- 为监听表新增歌单更新时间戳字段，用于增量同步判断
ALTER TABLE sq_monitor
    ADD COLUMN `target_update_time` bigint(20) NULL DEFAULT NULL COMMENT '歌单更新时间戳（网易云trackUpdateTime），用于增量同步判断' AFTER `target_cover`;


UPDATE `sq_config` SET  `config_options` = '[{\"label\":\"自动\",\"value\":\"auto\"},{\"label\":\"只下载flac\",\"value\":\"flac\"},{\"label\":\"只下载mp3\",\"value\":\"mp3\"},{\"label\":\"只下载ape\",\"value\":\"ape\"},{\"label\":\"只下载wav\",\"value\":\"wav\"},{\"label\":\"只下载m4a\",\"value\":\"m4a\"},{\"label\":\"只下载ogg\",\"value\":\"ogg\"},{\"label\":\"只下载aac\",\"value\":\"aac\"}]',  `config_remark` = '自动则是默认下载最高音质，有选择的格式则下载指定的格式\r\n支持情况(flac和mp3通用):\r\n-kw mp3,ape,flac-mg mp3-qqvip m4a,mp3,ape,flac-kg mp3-apple(目前未上线) aac,ogg,m4a,mp3,wav,flac,alac-tidal mp3,flac-qobuz(国外综合，目前未上线) mp3,flac' WHERE `config_key` = 'system.download.file.audio.format';
