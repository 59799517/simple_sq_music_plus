-- 3.0.21更新脚本
INSERT INTO `sq_config` (`config_name`, `config_value`, `config_key`, `config_type`, `config_show`, `config_remark`, `config_null_check`, `config_disabled`) VALUES ('开启特殊字符移除', 'false', 'system.start.file.and.folder.special.symbol.remove', 'boolean', 1, '开启后配置需要移除的特护符号', 1, 0);
INSERT INTO `sq_config` (`config_name`, `config_value`, `config_key`, `config_type`, `config_show`, `config_remark`, `config_null_check`, `config_disabled`) VALUES ('特殊符号配置', '*?:|？：.<>', 'system.start.file.and.folder.special.symbol.remove.symbol', 'input', 1, '开启特殊字符移除后生效', 1, 0);

