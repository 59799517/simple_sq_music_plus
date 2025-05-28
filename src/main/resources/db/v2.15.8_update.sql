--删除不用的设置
delete from sq_config where config_key='plug.qqvip.baseurl';
delete from sq_config where config_key='plug.qqvip.qq';
--增加新的设置项
INSERT INTO `sq_config` (`config_name`, `config_value`, `config_key`, `type`, `config_show`) VALUES ('自动同步关注的歌手所有专辑', 'false', 'plug.qqvip.synclikeartist', 'switch', 'Y');
INSERT INTO `sq_config` (`config_name`, `config_value`, `config_key`, `type`, `config_show`) VALUES ('同步排除的歌手（歌手名称）多个,（半角,）分割', '例如：讨这,周桀綸', 'plug.qqvip.syncartistexclude', 'input', 'Y');


