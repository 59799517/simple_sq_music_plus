-- PostgreSQL 版本的数据库初始化脚本

-- download_info 表定义
CREATE TABLE download_info (
    id SERIAL PRIMARY KEY,
    download_gid VARCHAR(255) DEFAULT NULL,
    download_time TIMESTAMP DEFAULT NULL,
    download_file VARCHAR(1000) DEFAULT NULL,
    download_music_id VARCHAR(255) NOT NULL,
    download_plug_name VARCHAR(255) DEFAULT NULL,
    download_br_type VARCHAR(255) DEFAULT NULL,
    download_musicname VARCHAR(255) DEFAULT NULL,
    download_artistname VARCHAR(255) DEFAULT NULL,
    download_albumname VARCHAR(255) DEFAULT NULL,
    download_msg VARCHAR(255) DEFAULT NULL,
    version VARCHAR(255) DEFAULT NULL,
    download_music_info TEXT DEFAULT NULL,
    download_status VARCHAR(255) DEFAULT NULL,
    spring_name VARCHAR(255) DEFAULT NULL,
    audio_book INTEGER DEFAULT NULL,
    download_update_time TIMESTAMP DEFAULT NULL,
    revision INTEGER DEFAULT NULL,
    rewrite_mp3tag INTEGER DEFAULT NULL,
    download_bits VARCHAR(255) DEFAULT NULL,
    download_br_types VARCHAR(255) DEFAULT NULL
);

-- 创建索引
CREATE INDEX idx_download_albumname ON download_info(download_albumname);
CREATE INDEX idx_download_artistname ON download_info(download_artistname);
CREATE INDEX idx_download_musicname ON download_info(download_musicname);

-- sq_config 表定义
CREATE TABLE sq_config (
    config_id SERIAL PRIMARY KEY,
    config_name VARCHAR(255) DEFAULT NULL,
    config_value TEXT DEFAULT NULL,
    config_key VARCHAR(255) DEFAULT NULL,
    config_type VARCHAR(255) DEFAULT NULL,
    config_show INTEGER DEFAULT NULL,
    config_remark VARCHAR(255) DEFAULT NULL,
    config_null_check INTEGER DEFAULT NULL,
    config_disabled INTEGER DEFAULT NULL
);

-- sq_sync 表定义
CREATE TABLE sq_sync (
    id SERIAL PRIMARY KEY,
    plug_name VARCHAR(255) DEFAULT NULL,
    music_id VARCHAR(255) DEFAULT NULL,
    music_info TEXT DEFAULT NULL,
    download_id INTEGER DEFAULT NULL,
    play_list_name VARCHAR(255) DEFAULT NULL,
    play_list_sha1 VARCHAR(255) DEFAULT NULL,
    play_list_id VARCHAR(255) DEFAULT NULL,
    artist_id VARCHAR(255) DEFAULT NULL,
    artist_name VARCHAR(255) DEFAULT NULL,
    album_id VARCHAR(255) DEFAULT NULL,
    album_name VARCHAR(255) DEFAULT NULL,
    music_name VARCHAR(500) DEFAULT NULL
);