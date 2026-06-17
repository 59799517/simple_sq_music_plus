-- 为监听表新增歌单更新时间戳字段，用于增量同步判断
ALTER TABLE sq_monitor
    ADD COLUMN `target_update_time` bigint(20) NULL DEFAULT NULL COMMENT '歌单更新时间戳（网易云trackUpdateTime），用于增量同步判断' AFTER `target_cover`;
