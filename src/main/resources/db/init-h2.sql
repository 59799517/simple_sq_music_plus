-- ============================================
-- H2 数据库初始化脚本（MySQL 兼容模式）
-- 合并所有建表和初始数据脚本
-- ============================================

-- ============================================
-- 1. 创建基础表结构
-- ============================================

-- download_info 表
CREATE TABLE IF NOT EXISTS download_info (
                                             id INT AUTO_INCREMENT PRIMARY KEY,
                                             download_gid VARCHAR(255),
    download_time TIMESTAMP,
    download_file VARCHAR(1000),
    download_music_id VARCHAR(255) NOT NULL,
    download_plug_name VARCHAR(255),
    download_br_type VARCHAR(255),
    download_musicname VARCHAR(255),
    download_artistname VARCHAR(255),
    download_albumname VARCHAR(255),
    download_msg VARCHAR(255),
    version VARCHAR(255),
    download_music_info CLOB,
    download_status VARCHAR(255),
    spring_name VARCHAR(255),
    audio_book INT,
    download_update_time TIMESTAMP,
    revision INT,
    rewrite_mp3tag INT,
    download_bits VARCHAR(255),
    download_br_types VARCHAR(255)
    );

CREATE INDEX IF NOT EXISTS idx_download_albumname ON download_info(download_albumname);
CREATE INDEX IF NOT EXISTS idx_download_artistname ON download_info(download_artistname);
CREATE INDEX IF NOT EXISTS idx_download_musicname ON download_info(download_musicname);

-- sq_config 表
CREATE TABLE IF NOT EXISTS sq_config (
                                         config_id INT AUTO_INCREMENT PRIMARY KEY,
                                         config_name VARCHAR(255),
    config_value CLOB,
    config_key VARCHAR(255),
    config_type VARCHAR(255),
    config_options CLOB,
    config_show INT,
    config_remark VARCHAR(255),
    config_null_check INT,
    config_disabled INT
    );

-- sq_sync 表
CREATE TABLE IF NOT EXISTS sq_sync (
                                       id INT AUTO_INCREMENT PRIMARY KEY,
                                       plug_name VARCHAR(255),
    music_id VARCHAR(255),
    music_info CLOB,
    download_id INT,
    play_list_name VARCHAR(255),
    play_list_sha1 VARCHAR(255),
    play_list_id VARCHAR(255),
    artist_id VARCHAR(255),
    artist_name VARCHAR(255),
    album_id VARCHAR(255),
    album_name VARCHAR(255),
    music_name VARCHAR(500)
    );

-- sq_ali_sync 表
CREATE TABLE IF NOT EXISTS sq_ali_sync (
                                           id INT AUTO_INCREMENT PRIMARY KEY,
                                           sha1 VARCHAR(255),
    md5 VARCHAR(255),
    sharding_sha1 VARCHAR(255),
    path CLOB,
    ali_id_path CLOB,
    ali_path CLOB,
    name VARCHAR(255),
    music_name VARCHAR(255),
    music_artist VARCHAR(255),
    music_album VARCHAR(255),
    suffix VARCHAR(255),
    upload_time TIMESTAMP,
    result VARCHAR(255),
    rapid INT,
    download_id INT
    );

-- sq_monitor 表
CREATE TABLE IF NOT EXISTS sq_monitor (
                                          id INT AUTO_INCREMENT PRIMARY KEY,
                                          plug_name VARCHAR(255),
    type VARCHAR(255),
    enabled VARCHAR(255),
    target_id VARCHAR(255),
    target_name CLOB,
    target_desc CLOB,
    target_url CLOB,
    target_count INT,
    target_cover CLOB,
    create_time TIMESTAMP,
    update_time TIMESTAMP
    );

-- ============================================
-- 2. 插入基础配置数据
-- ============================================

-- 系统基础配置
INSERT INTO sq_config (config_name, config_value, config_key, config_type, config_show, config_remark, config_null_check, config_disabled)
VALUES ('音乐下载路径', '/music', 'system.download.path', 'path', 1, '音乐下载路径', 1, 0);

INSERT INTO sq_config (config_name, config_value, config_key, config_type, config_show, config_remark, config_null_check, config_disabled)
VALUES ('是否忽略伴奏片段等格式', 'true', 'system.ignore.accompaniment', 'boolean', 1, '例如 伴奏，片段等', 1, 0);

INSERT INTO sq_config (config_name, config_value, config_key, config_type, config_show, config_remark, config_null_check, config_disabled)
VALUES ('文件存在时不下载', 'true', 'system.file.exist.not.download', 'boolean', 1, '文件存在时不下载', 1, 0);

INSERT INTO sq_config (config_name, config_value, config_key, config_type, config_show, config_remark, config_null_check, config_disabled)
VALUES ('同时下载的数量', '8', 'system.download.num', 'number', 1, '允许同时下载歌的数量（最小是1）越大消耗的资源越多也有可能碰到封IP', 1, 0);

INSERT INTO sq_config (config_name, config_value, config_key, config_type, config_show, config_remark, config_null_check, config_disabled)
VALUES ('登录账号', 'admin', 'system.login.account', 'input', 1, '登录账号', 1, 0);

INSERT INTO sq_config (config_name, config_value, config_key, config_type, config_show, config_remark, config_null_check, config_disabled)
VALUES ('登录密码', 'admin', 'system.login.password', 'password', 1, '登录密码', 1, 0);

INSERT INTO sq_config (config_name, config_value, config_key, config_type, config_show, config_remark, config_null_check, config_disabled)
VALUES ('开启降级下载', 'false', 'system.download.degrade', 'boolean', 0, '暂时未开发功能后续准备', 1, 1);

INSERT INTO sq_config (config_name, config_value, config_key, config_type, config_show, config_remark, config_null_check, config_disabled)
VALUES ('排除的歌单名称', '示例1|示例2', 'system.sync.playlist.exclude', 'input', 1, '排除的歌单名称多个|（管道符）分割', 0, 0);

INSERT INTO sq_config (config_name, config_value, config_key, config_type, config_show, config_remark, config_null_check, config_disabled)
VALUES ('排除的专辑名称', '示例1|示例2', 'system.sync.album.exclude', 'input', 1, '排除的专辑名称多个|（管道符）分割', 0, 0);

INSERT INTO sq_config (config_name, config_value, config_key, config_type, config_show, config_remark, config_null_check, config_disabled)
VALUES ('排除的歌手名称', '示例1|示例2', 'system.sync.artists.exclude', 'input', 1, '排除的歌手名称多个|（管道符）分割', 0, 0);

INSERT INTO sq_config (config_name, config_value, config_key, config_type, config_show, config_remark, config_null_check, config_disabled)
VALUES ('是否显示播放连接', 'true', 'system.show.play.url', 'boolean', 1, '是否显示播放连接（下载链接）', 1, 0);

INSERT INTO sq_config (config_name, config_value, config_key, config_type, config_show, config_remark, config_null_check, config_disabled)
VALUES ('同步补充同步歌单信息', 'false', 'system.sync.update.playlist.info', 'boolean', 1, '2.0版本导入的歌单信息缺失歌手信息和专辑信息如果碰到下载相同的歌曲时候补充信息，如果是没有历史歌单导入或者3.0版本开始使用则不用开启', 1, 1);

-- QQ音乐插件配置
INSERT INTO sq_config (config_name, config_value, config_key, config_type, config_show, config_remark, config_null_check, config_disabled)
VALUES ('QQ音乐插件启用', 'false', 'plug.qqvip.open', 'boolean', 1, '开启QQvip插件类型', 1, 0);

INSERT INTO sq_config (config_name, config_value, config_key, config_type, config_show, config_remark, config_null_check, config_disabled)
VALUES ('QQ音乐自动刷新登录', 'false', 'plug.qqvip.auto.refresh.login', 'boolean', 1, '是否开启QQ音乐自动刷新登录信息', 1, 0);

INSERT INTO sq_config (config_name, config_value, config_key, config_type, config_show, config_remark, config_null_check, config_disabled)
VALUES ('QQ音乐自动下载我喜欢的歌曲', 'false', 'plug.qqvip.sync.my.like.music', 'boolean', 1, 'QQ音乐自动下载我喜欢音乐内容', 1, 0);

INSERT INTO sq_config (config_name, config_value, config_key, config_type, config_show, config_remark, config_null_check, config_disabled)
VALUES ('QQ音乐自动下载我收藏专辑', 'false', 'plug.qqvip.sync.my.like.album', 'boolean', 1, '忽略专辑可在排除的专辑名称设置', 1, 0);

INSERT INTO sq_config (config_name, config_value, config_key, config_type, config_show, config_remark, config_null_check, config_disabled)
VALUES ('QQ音乐自动下载我关注的歌手', 'false', 'plug.qqvip.sync.my.like.artists', 'boolean', 1, '忽略歌手可在排除的歌手名称设置', 1, 0);

INSERT INTO sq_config (config_name, config_value, config_key, config_type, config_show, config_remark, config_null_check, config_disabled)
VALUES ('QQ音乐自动下载我收藏歌单内容', 'false', 'plug.qqvip.sync.my.like.playlist', 'boolean', 1, '忽略歌单名称可以通过排除歌单名称设置', 1, 0);

INSERT INTO sq_config (config_name, config_value, config_key, config_type, config_show, config_remark, config_null_check, config_disabled)
VALUES ('QQ音乐cookie', '', 'plug.qqvip.cookie', 'input', 0, '看看就行尽量别改', 1, 1);

INSERT INTO sq_config (config_name, config_value, config_key, config_type, config_show, config_remark, config_null_check, config_disabled)
VALUES ('QQ音乐qrcode', '', 'plug.qqvip.qrcode', 'input', 0, '看看就行尽量别改', 1, 1);

INSERT INTO sq_config (config_name, config_value, config_key, config_type, config_show, config_remark, config_null_check, config_disabled)
VALUES ('QQ音乐每日最大限额', '2000', 'plug.qqvip.download.daily.limit', 'number', 1, '每天下载超过这个数值就会暂停等待第二天下载(获取下载链接的次数防止被封号)', 1, 0);

INSERT INTO sq_config (config_name, config_value, config_key, config_type, config_show, config_remark, config_null_check, config_disabled)
VALUES ('QQ音乐今日下载', '0', 'plug.qqvip.download.today', 'number', 1, '今日下载数量10点整刷新', 1, 1);

-- 网易云音乐插件配置
INSERT INTO sq_config (config_name, config_value, config_key, config_type, config_show, config_remark, config_null_check, config_disabled)
VALUES ('开启网易云音乐插件', 'true', 'plug.netease.open', 'boolean', 1, '开启网易云音乐插件', 1, 0);

INSERT INTO sq_config (config_name, config_value, config_key, config_type, config_show, config_remark, config_null_check, config_disabled)
VALUES ('网易云音乐API地址', 'https://163api.qijieya.cn;http://dg-t.cn:3000;https://zm.armoe.cn;http://45.152.64.114:3005;https://apis.netstart.cn/music;https://wyy.xhily.com;http://plugin.changsheng.space:3000;', 'plug.netease.baseurl', 'input', 1, '类似http://xxx.xxxx.com(后边不要带/)多个;分割', 1, 0);

INSERT INTO sq_config (config_name, config_value, config_key, config_type, config_show, config_remark, config_null_check, config_disabled)
VALUES ('网易云音乐AP匿名登录地址', '/register/anonimous', 'plug.netease.cookieurl', 'input', 1, '一般是API地址加上/register/anonimous', 1, 0);

INSERT INTO sq_config (config_name, config_value, config_key, config_type, config_show, config_remark, config_null_check, config_disabled)
VALUES ('网易云音乐用户信息', '', 'plug.netease.cookie', 'input', 1, '网易云音乐的cookie', 1, 0);

INSERT INTO sq_config (config_name, config_value, config_key, config_type, config_show, config_remark, config_null_check, config_disabled)
VALUES ('是否使用扩展下载', 'true', 'plug.netease.extend.download', 'boolean', 1, '扩展可下载VIP（如果自己的cookie是VIP也可以设置为否）', 1, 0);

INSERT INTO sq_config (config_name, config_value, config_key, config_type, config_show, config_remark, config_null_check, config_disabled)
VALUES ('网易云音乐扩展下载地址', 'https://music-api.gdstudio.xyz/api.php', 'plug.netease.extend.download.url', 'input', 1, '网易云音乐扩展下载地址', 1, 0);

-- 酷狗音乐插件配置
INSERT INTO sq_config (config_name, config_value, config_key, config_type, config_show, config_remark, config_null_check, config_disabled)
VALUES ('开启酷狗插件', 'false', 'plug.kg.open', 'boolean', 1, '开启酷狗插件', 1, 0);

INSERT INTO sq_config (config_name, config_value, config_key, config_type, config_show, config_remark, config_null_check, config_disabled)
VALUES ('酷狗概念API地址', '', 'plug.kg.baseurl', 'input', 1, 'GIT:https://github.com/MakcRe/KuGouMusicApi 部署此项目(1.4.0版本)', 1, 0);

INSERT INTO sq_config (config_name, config_value, config_key, config_type, config_show, config_remark, config_null_check, config_disabled)
VALUES ('酷狗用户信息', '', 'plug.kg.user.info', 'input', 1, '酷狗用户信息', 1, 0);

INSERT INTO sq_config (config_name, config_value, config_key, config_type, config_show, config_remark, config_null_check, config_disabled)
VALUES ('酷狗二维码信息', '', 'plug.kg.qrcode.code', 'input', 0, '酷狗二维码信息', 1, 1);

INSERT INTO sq_config (config_name, config_value, config_key, config_type, config_show, config_remark, config_null_check, config_disabled)
VALUES ('酷狗微信二维码信息', '', 'plug.kg.qrcode.wx.code', 'input', 0, '酷狗微信二维码信息', 1, 1);

INSERT INTO sq_config (config_name, config_value, config_key, config_type, config_show, config_remark, config_null_check, config_disabled)
VALUES ('酷狗开启自动下载我收藏的歌单', 'true', 'plug.kg.sync.my.collect.playlist', 'boolean', 1, '忽略歌单名称可以通过排除歌单名称设置', 1, 0);

INSERT INTO sq_config (config_name, config_value, config_key, config_type, config_show, config_remark, config_null_check, config_disabled)
VALUES ('开启自动签到', 'true', 'plug.kg.sign.open', 'boolean', 1, '定时自动获得VIP', 1, 0);

INSERT INTO sq_config (config_name, config_value, config_key, config_type, config_show, config_remark, config_null_check, config_disabled)
VALUES ('酷狗最后一次自动签到时间', '', 'plug.kg.sign.last.time', 'input', 1, '酷狗最后一次自动签到时间', 1, 1);

INSERT INTO sq_config (config_name, config_value, config_key, config_type, config_show, config_remark, config_null_check, config_disabled)
VALUES ('酷狗概念签到到期信息', '', 'plug.kg.sign.begin-end.time', 'input', 1, '酷狗概念最后一次签到指示的开始与结束时间', 0, 1);

-- 酷我音乐插件配置
INSERT INTO sq_config (config_name, config_value, config_key, config_type, config_show, config_remark, config_null_check, config_disabled)
VALUES ('开启酷我插件', 'true', 'plug.kw.open', 'boolean', 1, '开启酷我插件', 1, 1);

-- 阿里云盘扩展配置
INSERT INTO sq_config (config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled)
VALUES ('阿里云同步扩展', NULL, 'expand.aliyun.open', 'boolean', NULL, 1, '打开则开启自动同步歌曲到阿里云音乐，同步模式在同步模式中选择', 1, 0);

INSERT INTO sq_config (config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled)
VALUES ('阿里云盘Appid', NULL, 'expand.aliyun.appid', 'input', NULL, 1, '阿里云盘开发者的appid', 1, 0);

INSERT INTO sq_config (config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled)
VALUES ('阿里云盘secret', NULL, 'expand.aliyun.appsecret', 'input', NULL, 1, '目前用的无后台模式暂时用不到', 1, 1);

INSERT INTO sq_config (config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled)
VALUES ('阿里云盘刷新token', NULL, 'expand.aliyun.refresh_token', 'input', NULL, 1, '目前用的无后台模式暂时用不到', 1, 1);

INSERT INTO sq_config (config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled)
VALUES ('阿里云盘的backup_drive_id', NULL, 'expand.aliyun.backup.drive_id', 'input', NULL, 1, '资源文件夹的ID', 1, 1);

INSERT INTO sq_config (config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled)
VALUES ('阿里云盘的资源drive_id', NULL, 'expand.aliyun.resource.drive_id', 'input', NULL, 1, '资源文件夹的ID', 1, 1);

INSERT INTO sq_config (config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled)
VALUES ('阿里云盘的请求token', NULL, 'expand.aliyun.access_token', 'input', NULL, 1, 'access_token 用户信息token', 1, 1);

INSERT INTO sq_config (config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled)
VALUES ('阿里云盘的请求token有效时长', NULL, 'expand.aliyun.code_verifier', 'input', NULL, 1, '单位是秒（无后端服务情况下只支持30天有效）', 1, 1);

INSERT INTO sq_config (config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled)
VALUES ('阿里云盘的授权码', NULL, 'expand.aliyun.code', 'input', NULL, 1, '授权码 获取到请求token后会很快失效过度使用', 1, 1);

INSERT INTO sq_config (config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled)
VALUES ('阿里云盘用户名称', NULL, 'expand.aliyun.user.name', 'input', NULL, 1, '阿里云盘用户名称', 1, 1);

INSERT INTO sq_config (config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled)
VALUES ('阿里云盘头像url', NULL, 'expand.aliyun.avatar', 'input', NULL, 1, '阿里云盘头像url', 1, 1);

INSERT INTO sq_config (config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled)
VALUES ('阿里云用户id', NULL, 'expand.aliyun.user.id', 'input', NULL, 1, '阿里云用户id', 1, 1);

INSERT INTO sq_config (config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled)
VALUES ('阿里云盘的账号名称', NULL, 'expand.aliyun.user.info.name', 'input', NULL, 1, '阿里云盘的账号名称', 1, 1);

INSERT INTO sq_config (config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled)
VALUES ('阿里云盘昵称', NULL, 'expand.aliyun.nick.name', 'input', NULL, 1, '阿里云盘的昵称', 1, 1);

INSERT INTO sq_config (config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled)
VALUES ('存储到阿里云盘的目录位置', NULL, 'expand.aliyun.folder.path', 'input', NULL, 1, '存储到阿里云盘的目录位置（默认在 备份文件/SqMusic文件夹）', 1, 0);

INSERT INTO sq_config (config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled)
VALUES ('同步模式', NULL, 'expand.aliyun.sync.mode', 'select', '[{"label":"定时同步和下载后自动同步","value":"all"},{"label":"定时同步到云盘","value":"scheduled"},{"label":"歌曲下载完成后同步","value":"download"}]', 1, '1.支持定时扫描下载文件夹内的全部歌曲同步上传到阿里云
2.支持歌曲下载完成后同步到阿里云盘内', 1, 0);

INSERT INTO sq_config (config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled)
VALUES ('阿里云盘access_token到期时间', NULL, 'expand.aliyun.access_token.expire.time', 'input', NULL, 1, '阿里云盘access_token到期时间', 1, 1);

INSERT INTO sq_config (config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled)
VALUES ('显示阿里云盘扩展', 'false', 'plug.aliyun.show', 'boolean', NULL, 1, '控制显示和隐藏阿里云扩展', 1, 0);

-- 下载相关配置
INSERT INTO sq_config (config_name, config_value, config_key, config_type, config_show, config_remark, config_null_check, config_disabled)
VALUES ('下载歌曲文件名模板', '${musicName} - ${artists}', 'system.download.file.template', 'input', 1, '目前支持
1.${musicName}---歌曲名称
2.${artists}---歌手名称
3.${album}---专辑名称
4.${albumId}---专辑ID
5.${artistsId}---歌手id
默认为${musicName} - ${artists}---歌曲名称 - 歌手名称 （高级支持SpEL表达式具体百度）', 1, 0);

INSERT INTO sq_config (config_name, config_value, config_key, config_type, config_show, config_remark, config_null_check, config_disabled)
VALUES ('开启特殊字符移除', 'false', 'system.start.file.and.folder.special.symbol.remove', 'boolean', 1, '开启后配置需要移除的特护符号', 1, 0);

INSERT INTO sq_config (config_name, config_value, config_key, config_type, config_show, config_remark, config_null_check, config_disabled)
VALUES ('特殊符号配置', '*?:|？：.<>', 'system.start.file.and.folder.special.symbol.remove.symbol', 'input', 1, '开启特殊字符移除后生效', 1, 0);

INSERT INTO sq_config (config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled)
VALUES ('下载文件音频格式', 'auto', 'system.download.file.audio.format', 'select', '[{"label":"自动","value":"auto"},{"label":"只下载flac","value":"flac"},{"label":"只下载mp3","value":"mp3"}]', 1, '自动则是默认下载最高音质，有选择的格式则下载指定的格式', 1, 0);
