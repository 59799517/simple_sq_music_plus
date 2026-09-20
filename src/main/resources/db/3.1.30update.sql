-- 【仅首次执行】给 config_key 添加唯一索引
ALTER TABLE `sq_config` ADD UNIQUE INDEX `uk_sq_config_config_key` (`config_key`);

INSERT INTO `sq_config` (`config_name`, `config_value`, `config_key`, `config_type`, `config_options`, `config_show`, `config_remark`, `config_null_check`, `config_disabled`)
VALUES ('歌曲tag标签数据来源', 'auto', 'plug.qqvip.tag.source', 'select', '[{"label":"当前插件","value":"auto"},{"label":"qq音乐（推荐）","value":"qq"},{"label":"网易云音乐","value":"netease"},{"label":"咪咕","value":"mg"},{"label":"酷我（不推荐 没有CD标识 全部歌按顺序排序）","value":"kw"},{"label":"酷狗（需要开启插件并且登录）","value":"kg"}]', 1, '歌曲tag标签数据来源默认跟随自身插件', 1, 0)
    ON DUPLICATE KEY UPDATE `config_key` = `config_key`;

INSERT INTO `sq_config` (`config_name`, `config_value`, `config_key`, `config_type`, `config_options`, `config_show`, `config_remark`, `config_null_check`, `config_disabled`)
VALUES ('歌曲tag标签数据来源', 'auto', 'plug.mg.tag.source', 'select', '[{"label":"当前插件","value":"auto"},{"label":"qq音乐（推荐）","value":"qq"},{"label":"网易云音乐","value":"netease"},{"label":"咪咕","value":"mg"},{"label":"酷我（不推荐 没有CD标识 全部歌按顺序排序）","value":"kw"},{"label":"酷狗（需要开启插件并且登录）","value":"kg"}]', 1, '歌曲tag标签数据来源默认跟随自身插件', 1, 0)
    ON DUPLICATE KEY UPDATE `config_key` = `config_key`;

INSERT INTO `sq_config` (`config_name`, `config_value`, `config_key`, `config_type`, `config_options`, `config_show`, `config_remark`, `config_null_check`, `config_disabled`)
VALUES ('歌曲tag标签数据来源', 'auto', 'plug.kg.tag.source', 'select', '[{"label":"当前插件","value":"auto"},{"label":"qq音乐（推荐）","value":"qq"},{"label":"网易云音乐","value":"netease"},{"label":"咪咕","value":"mg"},{"label":"酷我（不推荐 没有CD标识 全部歌按顺序排序）","value":"kw"},{"label":"酷狗（需要开启插件并且登录）","value":"kg"}]', 1, '歌曲tag标签数据来源默认跟随自身插件', 1, 0)
    ON DUPLICATE KEY UPDATE `config_key` = `config_key`;

INSERT INTO `sq_config` (`config_name`, `config_value`, `config_key`, `config_type`, `config_options`, `config_show`, `config_remark`, `config_null_check`, `config_disabled`)
VALUES ('歌曲tag标签数据来源', 'qq', 'plug.kw.tag.source', 'select', '[{"label":"当前插件","value":"auto"},{"label":"qq音乐（推荐）","value":"qq"},{"label":"网易云音乐","value":"netease"},{"label":"咪咕","value":"mg"},{"label":"酷我（不推荐 没有CD标识 全部歌按顺序排序）","value":"kw"},{"label":"酷狗（需要开启插件并且登录）","value":"kg"}]', 1, '歌曲tag标签数据来源默认跟随自身插件', 1, 0)
    ON DUPLICATE KEY UPDATE `config_key` = `config_key`;

INSERT INTO `sq_config` (`config_name`, `config_value`, `config_key`, `config_type`, `config_options`, `config_show`, `config_remark`, `config_null_check`, `config_disabled`)
VALUES ('歌曲tag标签数据来源', 'auto', 'plug.netease.tag.source', 'select', '[{"label":"当前插件","value":"auto"},{"label":"qq音乐（推荐）","value":"qq"},{"label":"网易云音乐","value":"netease"},{"label":"咪咕","value":"mg"},{"label":"酷我（不推荐 没有CD标识 全部歌按顺序排序）","value":"kw"},{"label":"酷狗（需要开启插件并且登录）","value":"kg"}]', 1, '歌曲tag标签数据来源默认跟随自身插件', 1, 0)
    ON DUPLICATE KEY UPDATE `config_key` = `config_key`;
