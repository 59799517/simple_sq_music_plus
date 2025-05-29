


预计更新时间不定 按照优先级排序
2. 酷狗概念喜欢自动下载
3. docker-compose方便一键部署               


## 更新参考
2.15.10
1. QQ音乐碰到没高音质的情况下自动下滑到mp3 320音质

2.15.8
1. QQ音乐取消外部接口（不用配置https://github.com/jsososo/QQMusicApi 服务）全内置
2. 增加QQ音乐登录信息自动刷新（再也不用隔段时间登陆一次了）  
3. 下载失败的错误提示修正
4. qq在设置里边扫码登录，也可以在设置里边刷新登录信息QQ登录信息如果不刷新一般能坚持2-3天

2.15.6
1. 解析下载有声书默认使用128 mp3格式
解决有ogg aac mp3格式混乱

2.15.3(4)
1. 修复酷我专辑下载优遗漏问题
2. QQ使用music.gdstudio.xyz获取下载地址但是此接口有限制不一定能拿到（基本不能用全靠运气）
3. 修复网易下载问题现在支持flac下载使用  music.gdstudio.xyz接口
4. 关闭一些不需要的日志

2.15.2 
1. 修复网易接口失效问题（只是不报错不影响启动 后续我修复功能）

2.15.1
1.修复设置不显示酷狗概念设置url地址


2.15.0
1. 增加酷狗概念版支持
2. 修复框架导致的下载失败问题
3. 修复酷狗下载部分异常问题
### 酷狗概念使用方法：

1. 设置页面开启酷狗插件（需要设置酷狗插件的Url地址）
2. 顶部扫码（微信-扫码）
3. 扫完后点结果查询即可

2.12.14.2
1. 修复酷我超长专辑下载不全问题（主要是有声书下载不全问题）


2.12.14.1
1. docker和其他服务受防火墙影响太严重了有的jar加速都下载不下来 制作了sh脚本能够一键部署docker
2. 做了新版本带上上传自动部署脚本

2.13.x
1. 将数据库配置改为mysql
2. 修改自动脚本
3. 自动打包jar
4. 在线预览增加设置
5. 重写下载管理功能页面


2.12.x
## 更新
1. 增加QQvip自动同步 我喜欢的歌单收藏专辑等功能需要启动后再设置中开启
2. 增加自动同步配置参数需要修改文设置 （设置中进行开启）参考下图 插件开启  定时开启  然后开启需要的功能 qq和url必须写

![10.png](img%2F10.png)

2.11.x
## 更新
修复QQvip下载部分问题
（2.15.8版本内置不用配置了）
~~1.增加QQvip下载需要买了vip并且部署了 https://github.com/jsososo/QQMusicApi 服务 登录账号密码后使用application-qqvip.yml配置url即可（如没配置请不要在网页中使用此功能！）~~

网站关闭了 用不到了
2.增加freeMp3的插件下载支持  
使用方法
1. 部署此项目
2. 安装插件（在free_plug中有安装教程）
3. 项目搜索选择freeMP3根据配置设置（url写你服务器地址   http://{你的服务器IP:端口}）
4. 打开https://tool.liumingye.cn/music/#/
5. 在下载页面下方出现红色的 根据自身情况点击推送即可
   token获取：~~
6.
![free3.png](img%2Ffree3.png)
## 感谢https://lz.qaiu.top/提供的蓝奏云转直连


[updateLog.md](updateLog.md)
## 项目描述

下载音乐工具，可以当普通的音乐下载工具使用，支持，flac，ape，mp3等格式的下载（根据码率不同）， 下载的歌曲目录结构支持emby 与 subsonic 类的服务，下载文件支持文件标签识别，歌词下载。

\Music \Artist Name \Album Name 1- Song.mp3 2- Song.mp3

emby识别请参考如下配置
https://support.emby.media/support/solutions/articles/44001159113-music-naming

## 整体配置

1. 登录用户名密码（默认账号 admin 密码 admin）
2. 启动数据库后自动创建数据库等
3. 如需哦修改配置则去项目的设置中修改



自己打包java 运行 更方便 
咱最新的桌面播放器 也支持此服务 双击右下方版本号可进入插件设置使用此服务

播放器：
https://github.com/59799517/sq_subsonic_desktop

## 使用可（java -jar ./simple-MusicServer-0.0.1-SNAPSHOT.jar

1. 安装java17
2. 运行jar包即）


## 打包docker方法

1. 删除旧的包 docker rmi sqmusicplusserver
2. 打包新的 docker build -t sqmusicplusserver .
3. 运行 docker run -d --name="sqmusicplusserver"   -p {你需要的端口号}:8083 -v /mnt/user/media/newmusic:/music sqmusicplusserver

### 运行图：

![](img/1.png)
![](img/2.png)
![](img/3.png)
![](img/4.png)
![](img/5.png)
![](img/6.png)
![](img/7.png)
![](img/8.png)
![](img/9.png)

## Star History

[![Star History Chart](https://api.star-history.com/svg?repos=59799517/simple_sq_musuc_plus&type=Date)](https://star-history.com/#59799517/simple_sq_musuc_plus&Date)
![Alt](https://repobeats.axiom.co/api/embed/ca2487298cfad03b4fb6cf106afe371ff18a5bd1.svg "Repobeats analytics image")








