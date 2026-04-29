UPDATE `sq_config` SET `config_value` = '*?:|\"\\/<>' WHERE `config_key` = 'system.start.file.and.folder.special.symbol.remove.symbol';
UPDATE `sq_config` SET `config_value` = 'true',`config_remark` = '开启后配置需要移除的特护符号(建议开启部分歌曲歌手专辑名称有系统无法识别的字符，关闭有可能部分歌曲无法下载！)' WHERE `config_key` = 'system.start.file.and.folder.special.symbol.remove';
