package com.sqmusicplus.plug.qq.entity;

import cn.hutool.core.codec.Base64;
import com.alibaba.fastjson.JSONObject;
import com.ejlchina.data.Array;
import com.ejlchina.data.Mapper;
import com.sqmusicplus.base.entity.Album;
import com.sqmusicplus.base.entity.Artists;
import com.sqmusicplus.base.entity.Music;
import com.sqmusicplus.plug.base.PlugBrType;
import com.sqmusicplus.plug.entity.PlugSearchAlbumResult;
import com.sqmusicplus.plug.entity.PlugSearchArtistResult;
import com.sqmusicplus.plug.entity.PlugSearchMusicResult;
import com.sqmusicplus.plug.entity.PlugSearchResult;
import com.sqmusicplus.plug.qq.config.QQConfig;
import com.sqmusicplus.plug.qq.entity.getfollowsingerlist.GetFollowSingerList;
import com.sqmusicplus.plug.qq.util.QQMusicUtil;
import com.sqmusicplus.utils.DownloadUtils;
import com.sqmusicplus.utils.StringUtils;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @Classname QQSearchEntity
 * @Description TODO
 * @Version 1.0.0
 * @Date 2023/8/24 17:09
 * @Created by Administrator
 */

@NoArgsConstructor
@Data
public class QQSearchEntity {


    private  String plugName = "qq";

    public void setPlugName(String plugName) {
        this.plugName = plugName;
    }

    public  String getPlugName() {
        return plugName;
    }


    /**
     * 用code换cookies
     * @param code
     * @return
     */

    public String getCookieByCodeParam(String  code){
        String   msg = """
                {
                           "comm": {
                               "g_tk": 5381,
                               "platform": "yqq",
                               "ct": 24,
                               "cv": 0
                           },
                           "req": {
                               "module": "QQConnectLogin.LoginServer",
                               "method": "QQLogin",
                               "param": {
                                   "code": "%s"
                               }
                           }
                       }
                """;

        String format = String.format(msg, code);
        return format;
    }

    /**
     * 获取下载链接
     */
    public  String downloadRequestParam(String qq,String musicKey,String loginType ,String filename,String songmid) {
//        "QIMEI36": "%s",
        String msg = """
                {
                    "comm": {
                      "cv": 13020508,
                      "v": 13020508,
                      "ct": "24",
                      "tmeAppID": "qqmusic",
                      "format": "json",
                      "inCharset": "utf-8",
                      "outCharset": "utf-8",
                      "uid": "3931641530",
                      "qq": "%s",
                      "authst": "%s",
                      "tmeLoginType": "%s"
                    },
                    "music.vkey.GetVkey.UrlGetVkey": {
                      "module": "music.vkey.GetVkey",
                      "method": "UrlGetVkey",
                      "param": {
                        "filename": [
                          "%s"
                        ],
                        "guid": "%s",
                        "songmid": [
                          "%s"
                        ],
                        "songtype": [
                          0
                        ],
                        "uin": "%s",
                        "loginflag": 1,
                        "platform": "20"
                      }
                    }
                  }
               """;
        String format = String.format(msg,
                qq,
                musicKey,
                loginType,
                filename,
                QQMusicUtil.getGuid(),
                songmid,
                qq
        );
        return format;
    }

    /**
     * 检测cookie是否有效
     */
    public  String checkCookieParam(QQMusicCookieInfo cookie){
        String msg = """
              {
                       	"comm": {
                       		"cv": 13020508,
                           "v": 13020508,
                           "ct": "11",
                           "tmeAppID": "qqmusic",
                           "format": "json",
                           "inCharset": "utf-8",
                           "outCharset": "utf-8",
                           "qq": "%s",
                           "authst": "%s",
                           "tmeLoginType": "%s"
                       	},
                       	"req": {
                       		"module": "music.UserInfo.userInfoServer",
                       		"method": "GetLoginUserInfo",
                       		"param": {}
                       	}
                       }
          """;
        String musicid = cookie.getMusicid();
        String musickey = cookie.getMusickey();
        String loginType = cookie.getLoginType().toString();
        return String.format(msg,musicid,musickey,loginType);
    }
    /**
     * 刷新token
     */
    public  String refreshCookieParam(QQMusicCookieInfo cookie){
        String msg = """
                {
                             "comm": {
                                 "fPersonality": "0",
                                 "tmeLoginType": "%s",
                                 "qq": "%s",
                                 "authst": "%s",
                                 "ct": "11",
                                 "cv": "12080008",
                                 "v": "12080008",
                                 "tmeAppID": "qqmusic"
                             },
                             "req1": {
                                 "module": "music.login.LoginServer",
                                 "method": "Login",
                                 "param": {
                                     "str_musicid": "%s",
                                     "musickey": "%s",
                                     "refresh_key":"%s"
                                 }
                             }
                         }
          """;
        return String.format(msg,  cookie.getLoginType().toString(),
                cookie.getStrMusicid(),
                cookie.getMusickey(),
                cookie.getStrMusicid(),
                cookie.getMusickey(),
                cookie.getRefreshKey());
    }

    /**
     * 喜欢的歌手
     * @param qq
     * @param musicKey
     * @param loginType
     * @param encryptUin
     * @param size 每页长度
     * @param page 页码从1开始
     * @return
     */
    public  String followSingerParam(String qq,String musicKey,String loginType,String encryptUin,int  size, int page) {

        String   msg = """
                {
                    "comm": {
                      "cv": 13020508,
                      "v": 13020508,
                      "ct": "11",
                      "tmeAppID": "qqmusic",
                      "format": "json",
                      "inCharset": "utf-8",
                      "outCharset": "utf-8",
                      "qq": "%s",
                      "authst": "%s",
                      "tmeLoginType": "%s"
                    },
                    "req": {
                        "module": "music.concern.RelationList",
                        "method": "GetFollowSingerList",
                        "param": {
                            "HostUin": "%s",
                            "From": %s,
                            "Size": %s
                        }
                    }
                }""";
        page --;
        String format = String.format(msg,
                qq,
                musicKey,
                loginType,
                encryptUin,
                page,
                size

        );
        return format;
    }



    /**
     * 搜索请求参数
     * @param query
     * @param search_type
     * @param page_num
     * @param num_per_page
     * @return
     */


    public  String searchRequestParam(String query,String search_type ,Integer page_num,Integer num_per_page) {

     String   msg = """
                {
                        "comm": {
                            "ct": "19",
                            "cv": "1859",
                            "uin": "0"
                        },
                        "req": {
                            "method": "DoSearchForQQMusicDesktop",
                            "module": "music.search.SearchCgiService",
                            "param": {
                            "search_type": %s,
                            "query": "%s",
                               "page_num": %s,
                                    " num_per_page": %s,
                                "grp": 1  
                            }
                        }
                    }
                """;

        String format = String.format(msg, search_type, query, page_num, num_per_page);
        return format;
    }

    /**
     * 歌词参数
     * @param mid
     * @return
     */
    public  String lyricRequestParam(String mid) {
        String msg = """
                {
                           "music.musichallSong.PlayLyricInfo.GetPlayLyricInfo": {
                               "module": "music.musichallSong.PlayLyricInfo",
                               "method": "GetPlayLyricInfo",
                               "param": {
                                   "trans_t": 0,
                                   "roma_t": 0,
                                   "crypt": 0, 
                                   "lrc_t": 0,
                                   "interval": 208,
                                   "trans": 1,
                                   "ct": 6,
                                   "singerName": "", 
                                   "type": 0,
                                   "qrc_t": 0,
                                   "cv": 80600,
                                   "roma": 1,
                                    "songMID": "%s",
                                   "qrc": 0,
                                   "albumName": "",
                                   "songName": "" 
                               }
                           },
                           "comm": {
                               "wid": "",
                               "tmeAppID": "qqmusic",
                               "authst": "",
                               "uid": "",
                               "gray": "0",
                               "OpenUDID": "",
                               "ct": "6",
                               "patch": "2",
                               "psrf_qqopenid": "",
                               "sid": "",
                               "psrf_access_token_expiresAt": "",
                               "cv": "80600",
                               "gzip": "0",
                               "qq": "",
                               "nettype": "2",
                               "psrf_qqunionid": "",
                               "psrf_qqaccess_token": "",
                               "tmeLoginType": "2"
                           }
                       }           
                """;
        String format = String.format(msg, mid);
        return format;
    }

    /**
     * 单曲详情参数
     * @param mid
     * @return
     */
    public  String musicInfoRequestParam(String mid) {
        String msg = """
                {
                        "songinfo": {
                          "method": "get_song_detail_yqq",
                          "module": "music.pf_song_detail_svr",
                          "param": {
                                "song_type": "0",
                            "song_mid": "%s"
                          }
                        }
                      }
                """;
        String format = String.format(msg, mid);
        return format;
    }

    /**
     * 专辑详情参数
     * @param albummid
     * @return
     */
    public  String albumInfoRequestParam(String albummid) {
        String msg = """
                {
                           "AlbumSongList": {
                               "module": "music.musichallAlbum.AlbumSongList",
                               "method": "GetAlbumSongList",
                               "param": {"albumMid": "%s", "begin": 0, "num": 100, "order": 2}
                           },
                           "comm": {
                               "g_tk": 0,
                               "uin": "",
                               "format": "json",
                               "ct": 6,
                               "cv": 80600,
                               "platform": "wk_v17",
                               "uid": ""
                           }
                       }
                """;
        String format = String.format(msg, albummid);
        return format;
    }


    /**
     * 歌手全部歌曲参数
     * @param albummid
     * @return
     */
    public  String artistsTransferAlbumParam(String albummid) {

        String msg = """
                {
                                  "comm": {
                                    "ct": 24,
                                    "cv": 0
                                  },
                                  "singerAlbum": {
                                    "method": "get_singer_album",
                                    "param": {
                                      "singermid":"%s",
                                      "order": "time",
                                      "begin": 0,
                                      "num": 1000,
                                      "exstatus": 1
                                    },
                                    "module": "music.web_singer_info_svr"
                                  }
                                }
                        
                """;
        String format = String.format(msg, albummid);
        return format;
    }
    /**
     * 收藏歌单参数
     */
    public  String userSelfSongListParam(String qq,String musicKey,String loginType) {
        String msg = """
                {
                    "comm": {
                        "cv": 13020508,
                        "v": 13020508,
                        "ct": "11",
                        "tmeAppID": "qqmusic",
                        "format": "json",
                        "inCharset": "utf-8",
                        "outCharset": "utf-8",
                        "qq": "%s",
                        "authst": "%s",
                        "tmeLoginType": "%s"
                    },
                    "req": {
                        "module": "music.musicasset.PlaylistBaseRead",
                        "method": "GetPlaylistByUin",
                        "param": {
                          "uni":"%s"
                        }
                    }
                }
               """;
        String format = String.format(msg, qq,musicKey,loginType,qq);
        return format;
    }

    /**
     * 收藏的专辑
     */
    public  String userALbymListParam(String encryptUin,int  size, int page) {
        String msg = """
               
                {
                    "comm": {
                      "cv": 13020508,
                      "v": 13020508,
                      "ct": "11",
                      "tmeAppID": "qqmusic",
                      "format": "json",
                      "inCharset": "utf-8",
                      "outCharset": "utf-8"
                    },
                    "req": {
                      "module": "music.musicasset.AlbumFavRead",
                      "method": "CgiGetAlbumFavInfo",
                      "param": {
                        "euin": "%s",
                        "offset": 0,
                        "size": 10
                      }
                    }
                  }
               """;
        page--;
        String format = String.format(msg, encryptUin,page,size);
        return format;

}

    /**
     * 获取用户收藏歌单
     * @param musicid
     * @param musickey
     * @param loginType
     * @param encryptUin
     * @param size
     * @param page
     * @return
     */
    public String followSongListParam(String musicid, String musickey, String loginType, String encryptUin, int size, int page) {
        String format = """
                {
                    "comm": {
                        "cv": 13020508,
                        "v": 13020508,
                        "ct": "11",
                        "tmeAppID": "qqmusic",
                        "format": "json",
                        "inCharset": "utf-8",
                        "outCharset": "utf-8",
                        "qq": "%s",
                        "authst": "%s",
                        "tmeLoginType": "%s"
                    },
                    "req": {
                        "module": "music.musicasset.PlaylistFavRead",
                        "method": "CgiGetPlaylistFavInfo",
                        "param": {
                            "uin": "%s",
                            "offset": %s,
                            "size": %s
                        }
                    }
                }
               """;
        page --;
        return String.format(format, musicid,musickey,loginType,encryptUin,page,size);
    }

    /**
     * 获取 歌单详情
     */
    public  String songListInfoRequestParam(String mid,String dirid,Long page,Long size) {
        String msg = """
                {
                  "comm": {
                    "cv": 13020508,
                    "v": 13020508,
                    "ct": "11",
                    "tmeAppID": "qqmusic",
                    "format": "json",
                    "inCharset": "utf-8",
                    "outCharset": "utf-8"
                  },
                  "req": {
                    "module": "music.srfDissInfo.DissInfo",
                    "method": "CgiGetDiss",
                    "param": {
                      "disstid": %s,
                      "dirid": %s,
                      "tag": 1,
                      "song_begin": %s,
                      "song_num": %s,
                      "userinfo": 1,
                      "orderlist": 1,
                      "onlysonglist": 1
                    }
                  }
                }
                """;
        long song_begin = size * (page - 1);
        return String.format(msg, mid,dirid,song_begin,size);

    }


//    /**
//     * 获取全部关注歌手信息
//     */
//    public  String likeArtistsParam(String qq,String musicKey,String loginType,String encryptUin,int  size, int page) {
//        String msg = """
//                {
//                    "comm": {
//                        "cv": 13020508,
//                        "v": 13020508,
//                        "ct": "11",
//                        "tmeAppID": "qqmusic",
//                        "format": "json",
//                        "inCharset": "utf-8",
//                        "outCharset": "utf-8",
//                        "qq": "%s",
//                        "authst": "%s",
//                        "tmeLoginType": "%s"
//                    },
//                    "req": {
//                        "module": "music.concern.RelationList",
//                        "method": "GetFollowSingerList",
//                        "param": {
//                            "HostUin": "%s",
//                            "From": %s,
//                            "Size": %s
//                        }
//                    }
//                }
//                    """;
//        page --;
//        String format = String.format(msg,
//                qq,
//                musicKey,
//                loginType,
//                encryptUin,
//                page,
//                size);
//        return format;
//}


    /**
     * 单曲搜索结果转换
     * @param mapper
     * @param qqConfig
     * @return
     */
    public  PlugSearchResult<PlugSearchMusicResult> toMusicPlugSearchResult(Mapper mapper, QQConfig qqConfig) {
        ArrayList<PlugSearchMusicResult> plugSearchMusicResults = new ArrayList<>();
        Array array = mapper.getMapper("req").getMapper("data")
                .getMapper("body").getMapper("song").getArray("list");
        array.forEach((i,e)-> {
            String mid = e.toMapper().getString("mid");

            String name = e.toMapper().getString("name");
            String artistName = "";
            ArrayList<String> strings = new ArrayList<>();
            AtomicReference<String> artistid = new AtomicReference<>("");
            e.toMapper().getArray("singer").forEach((i1,e1)->{
                strings.add(e1.toMapper().getString("name"));
                if (StringUtils.isEmpty(artistid.get())){
                    artistid.set(e1.toMapper().getString("mid"));
                }
            });
            artistName=String.join(",",strings);
            String album = e.toMapper().getMapper("album").getString("name");
            String albumId = e.toMapper().getMapper("album").getString("mid");
            String pmid = e.toMapper().getMapper("album").getString("pmid");
            String albumImageconfig = qqConfig.getAlbumImage();
            String albumImage = albumImageconfig.replaceAll("#\\{pmid}", pmid);
            String lyricResult = toPlugLyricResult(mid,qqConfig);
            PlugSearchMusicResult plugSearchMusicResult = new PlugSearchMusicResult();
            plugSearchMusicResult.setSearchType(getPlugName());
            plugSearchMusicResult.setId(mid);
            plugSearchMusicResult.setName(name);
            plugSearchMusicResult.setArtistName(artistName);
            plugSearchMusicResult.setArtistid(artistid.get());
            plugSearchMusicResult.setPic(albumImage);
            plugSearchMusicResult.setAlbumName(album);
            plugSearchMusicResult.setAlbumid(albumId);
            plugSearchMusicResult.setLyricId(mid);
            plugSearchMusicResult.setLyric(lyricResult);
            int i1 = e.toMapper().getInt("interval") * 1000;
            plugSearchMusicResult.setDuration(i1+"");
//            plugSearchMusicResult.setOter(e.toString());
            plugSearchMusicResults.add(plugSearchMusicResult);

        });
        PlugSearchResult<PlugSearchMusicResult> plugSearchResult = new PlugSearchResult<>();
        plugSearchResult.setSearchType(getPlugName());
        plugSearchResult.setRecords(plugSearchMusicResults);
        return plugSearchResult;
    }

    /**
     * 歌手搜索转换
     * @param mapper
     * @return
     */
    public  PlugSearchResult<PlugSearchArtistResult> toArtistPlugSearchResult(Mapper mapper) {
        ArrayList<PlugSearchArtistResult> plugSearchArtistResults = new ArrayList<>();
        Array array = mapper.getMapper("req").getMapper("data")
                .getMapper("body").getMapper("singer").getArray("list");

        array.forEach((i,e)-> {
            String singerName = e.toMapper().getString("singerName");
            String singerID = e.toMapper().getString("singerMID");
            String singerPic = e.toMapper().getString("singerPic");
            String string = e.toMapper().getString("albumNum");
            PlugSearchArtistResult plugSearchArtistResult = new PlugSearchArtistResult();
            plugSearchArtistResult.setSearchType(getPlugName());
            plugSearchArtistResult.setArtistName(singerName);
            plugSearchArtistResult.setTotal(string);
            plugSearchArtistResult.setArtistid(singerID);
            plugSearchArtistResult.setPic(singerPic);
            plugSearchArtistResult.setOter(e.toString());
            plugSearchArtistResults.add(plugSearchArtistResult);

        });
        PlugSearchResult<PlugSearchArtistResult> plugSearchResult = new PlugSearchResult<>();
        plugSearchResult.setSearchType(getPlugName());
        plugSearchResult.setRecords(plugSearchArtistResults);
        return plugSearchResult;
    }


    /**
     * 单曲转音乐
     * @param mapper
     * @param qqConfig
     * @return
     */
    public  Music songInfoToMusic(Mapper mapper,QQConfig qqConfig) {
        Mapper mapper1 = mapper.getMapper("songinfo").getMapper("data").getMapper("track_info");
        String name = mapper1.getString("name");
        String mid = mapper1.getString("mid");
        String albumid = mapper1.getMapper("album").getString("mid");
        String albumname = mapper1.getMapper("album").getString("name");
        String albumpmid = mapper1.getMapper("album").getString("pmid");
        String albumImageconfig = qqConfig.getAlbumImage();
        String albumImage = albumImageconfig.replaceAll("#\\{pmid}", albumpmid);
        String artistId = mapper1.getArray("singer").getMapper(0).getString("mid");
        Array singer = mapper1.getArray("singer");
        Long flac = mapper1.getMapper("file").getLong("size_flac");
        Long mp3320 = mapper1.getMapper("file").getLong("size_320mp3");
        Long mp3128 = mapper1.getMapper("file").getLong("size_128mp3");
//        String mediaMid = mapper1.getMapper("file").getString("media_mid");
        ArrayList<PlugBrType> longs = new ArrayList<>();
        if (flac != null&&flac.longValue()>0){
            longs.add(PlugBrType.QQVIP_Flac_2000);
        }
        if (mp3320 != null&&mp3320.longValue()>0){
            longs.add(PlugBrType.QQVIP_MP3_320);
        }
        if (mp3128 != null&&mp3128.longValue()>0){
            longs.add(PlugBrType.QQVIP_MP3_128);
        }
        ArrayList<String> strings = new ArrayList<>();
        singer.forEach((i,e)->{
            String string = e.toMapper().getString("name");
            strings.add(string);
        });
        String lyricResult = toPlugLyricResult(mid,qqConfig);

//        if (StringUtils.isNotEmpty(mediaMid)){
//            mid=mid+","+mediaMid;
//        }
        Music music = new Music().setId(mid)
                .setMusicImage(albumImage)
                .setMusicLyric(lyricResult)
                .setMusicAlbum(albumname)
                .setMusicArtists(StringUtils.join(strings,"&"))
                .setMusicName(name).
                setOther(JSONObject.parseObject(mapper1.toString()))
                .setAlbumId(albumid)
                .setArtistsId(artistId)
                .setBits(longs)
                .setMusicDuration(mapper1.getInt("interval") * 1000L);
        return  music;
    }

    /**
     * 专辑详情转专辑对象
     * @param mapper
     * @param qqConfig
     * @return
     */
    public  Album albumInfoToAlbum(Mapper mapper, QQConfig qqConfig) {

        Array array = mapper.getMapper("AlbumSongList").getMapper("data").getArray("songList");

        AtomicReference<String> albumName = new AtomicReference<>("");
        AtomicReference<String> albumid= new AtomicReference<>("");
        AtomicReference<String> alubimage= new AtomicReference<>("");
        AtomicReference<String> artistid= new AtomicReference<>("");
        AtomicReference<String> artist= new AtomicReference<>("");
        AtomicReference<String> albumTime= new AtomicReference<>("");
        ArrayList<Music> collect = new ArrayList<>();
        array.forEach((i,e)->{
            if (StringUtils.isEmpty(albumName.get())){
                String pmid = e.toMapper().getMapper("songInfo").getMapper("album").getString("pmid");
                albumName.set(e.toMapper().getMapper("songInfo").getMapper("album").getString("name"));
                albumid.set(e.toMapper().getMapper("songInfo").getMapper("album").getString("mid"));
                albumTime.set(e.toMapper().getMapper("songInfo").getMapper("album").getString("time_public"));
                String albumImageconfig = qqConfig.getAlbumImage();
                alubimage.set(albumImageconfig.replaceAll("#\\{pmid}", pmid));
                artistid.set(e.toMapper().getMapper("songInfo").getArray("singer").getMapper(0).getString("mid"));
                artist.set(e.toMapper().getMapper("songInfo").getArray("singer").getMapper(0).getString("name"));
            }
            String string = e.toMapper().getMapper("songInfo").getString("name");
            String albumname =  e.toMapper().getMapper("songInfo").getMapper("album").getString("name");
            String aartist = e.toMapper().getMapper("songInfo").getArray("singer").getMapper(0).getString("name");
            String albumImageconfig = qqConfig.getAlbumImage();
            String url =  albumImageconfig.replaceAll("#\\{pmid}", e.toMapper().getMapper("songInfo").getMapper("album").getString("pmid"));
            Music music = new Music().setMusicName(string).setMusicAlbum(albumname).setMusicArtists(aartist).setMusicImage(url).setOther(JSONObject.parseObject(e.toString()));
            collect.add(music);
        });

        return new Album().
                 setMusics(collect)
                 .setAlbumArtists(artist.get())
                 .setAlbumName(albumName.get())
                .setAlbumTime(albumTime.get())
                 .setAlbumDescribe("无")
                 .setAlbumImg(alubimage.get())
                 .setAlbumId(albumid.get())
                 .setAlbumArtistId(artistid.get());
    }
    public  List<Music> albumInfoToAlbumMusic(Mapper mapper, QQConfig qqConfig) {

        Array array = mapper.getMapper("AlbumSongList").getMapper("data").getArray("songList");

        ArrayList<Music> collect = new ArrayList<>();
        array.forEach((i,e)->{
            String string = e.toMapper().getMapper("songInfo").getString("name");
            String mid = e.toMapper().getMapper("songInfo").getString("mid");
            String albumname =  e.toMapper().getMapper("songInfo").getMapper("album").getString("name");
//            String aartist = e.toMapper().getMapper("songInfo").getArray("singer").getMapper(0).getString("name");
            Array singer = e.toMapper().getMapper("songInfo").getArray("singer");
            ArrayList<String> strings = new ArrayList<>();
            singer.forEach((c,x)->{
                String name = x.toMapper().getString("name");
                strings.add(name);
            });
            Long flac = e.toMapper().getMapper("songInfo").getMapper("file").getLong("size_flac");
            Long mp3320 = e.toMapper().getMapper("songInfo").getMapper("file").getLong("size_320mp3");
            Long mp3128 = e.toMapper().getMapper("songInfo").getMapper("file").getLong("size_128mp3");
            String media_mid = e.toMapper().getMapper("songInfo").getMapper("file").getString("media_mid");

            ArrayList<PlugBrType> longs = new ArrayList<>();
            if (flac != null&&flac.longValue()>0){
                longs.add(PlugBrType.QQVIP_Flac_2000);
            }
            if (mp3320 != null&&mp3320.longValue()>0){
                longs.add(PlugBrType.QQVIP_MP3_320);
            }
            if (mp3128 != null&&mp3128.longValue()>0){
                longs.add(PlugBrType.QQVIP_MP3_128);
            }

            String albumImageconfig = qqConfig.getAlbumImage();
            String url =  albumImageconfig.replaceAll("#\\{pmid}", e.toMapper().getMapper("songInfo").getMapper("album").getString("pmid"));
           if (StringUtils.isNotEmpty(media_mid)){
               mid = mid+","+media_mid;
           }
            Music music = new Music().setId(mid).setMusicName(string).setMusicAlbum(albumname).setMusicArtists(StringUtils.join(strings,"&")).setMusicImage(url).setOther(JSONObject.parseObject(e.toString())).setBits(longs);
            collect.add(music);
        });
        return collect;

    }
    /**
     * 专辑搜索转换
     * @param mapper
     * @return
     */
    public  PlugSearchResult<PlugSearchAlbumResult> toAlbumPlugSearchResult(Mapper mapper) {
        ArrayList<PlugSearchAlbumResult> plugSearchAlbumResults = new ArrayList<>();
        Array array = mapper.getMapper("req").getMapper("data")
                .getMapper("body").getMapper("album").getArray("list");
        array.forEach((i,e)-> {
            String albumID = e.toMapper().getString("albumMID");
            String albumName = e.toMapper().getString("albumName");
            String singerName = e.toMapper().getString("singerName");
            String singerID = e.toMapper().getString("singerID");
            String albumPic = e.toMapper().getString("albumPic");
            PlugSearchAlbumResult plugSearchAlbumResult = new PlugSearchAlbumResult();
            plugSearchAlbumResult.setSearchType(getPlugName());
            plugSearchAlbumResult.setAlbumName(albumName);
            plugSearchAlbumResult.setAlbumid(albumID);
            plugSearchAlbumResult.setArtistName(singerName);
            plugSearchAlbumResult.setArtistid(singerID);
            plugSearchAlbumResult.setPic(albumPic);
            plugSearchAlbumResults.add(plugSearchAlbumResult);

        });
        PlugSearchResult<PlugSearchAlbumResult> plugSearchResult = new PlugSearchResult<>();
        plugSearchResult.setSearchType(getPlugName());
        plugSearchResult.setRecords(plugSearchAlbumResults);
        return plugSearchResult;
    }

    /**
     * 获取歌词（会发送请求）
     * @param musicId
     * @param qqConfig
     * @return
     */
    //歌词
    public  String toPlugLyricResult(String musicId,QQConfig qqConfig){
        String s = lyricRequestParam(musicId);
        String searchUrl = qqConfig.getSearchUrl();
        Mapper mapper = DownloadUtils.getHttp().sync(searchUrl).setBodyPara(s).post().getBody().toMapper();
        Mapper mapper1 = mapper.getMapper("music.musichallSong.PlayLyricInfo.GetPlayLyricInfo");
        String lyric = mapper1.getMapper("data").getString("lyric");
        String s1 = Base64.decodeStr(lyric);
        return s1;
    }

    /**
     * 获取歌手信息
     * @param artistId
     * @param qqConfig
     * @return
     */
    public  Artists toPlugArtistResult(String artistId,QQConfig qqConfig){
        String artistImage = qqConfig.getArtistImage();
        String pic = artistImage.replaceAll("#\\{pmid}", artistId);
        Artists artists = new Artists();
        artists.setId(artistId);
        artists.setMusicArtistsPhoto(pic);
        return artists;
    }

    /**
     * 歌手全部专辑转换
     * @param mapper
     * @param qqConfig
     * @return
     */
    public  List<Album> artistsTransferAlbum (Mapper mapper, QQConfig qqConfig){
        ArrayList<Album> albums = new ArrayList<>();
        Array array = mapper.getMapper("singerAlbum").getMapper("data").getArray("list");
        array.forEach((i,e)-> {
            String album_mid = e.toMapper().getString("album_mid");
            String pub_time = e.toMapper().getString("pub_time");
            String singer_mid = e.toMapper().getString("singer_mid");
            String singer_name = e.toMapper().getString("singer_name");
            String desc = e.toMapper().getString("desc");
            String album_name = e.toMapper().getString("album_name");
            String albumImageconfig = qqConfig.getAlbumImage();
            String image = albumImageconfig.replaceAll("#\\{pmid}", album_mid);
            albums.add(new Album().setAlbumArtists(singer_name)
                    .setAlbumArtistId(singer_mid)
                    .setAlbumTime(pub_time)
                    .setAlbumDescribe(desc)
                    .setAlbumId(album_mid)
                    .setAlbumName(album_name)
                    .setAlbumImg(image)
                    .setOther(e.toString()));
        });
        return albums;
    }

    /**
     * 获取cookie信息
     * @param data 返回的字符串信息
     * @return
     */
    public QQMusicCookieInfo getCookieByCode(String data){
        QQMusicCookie qqMusicCookie = JSONObject.parseObject(data, QQMusicCookie.class);
        if (qqMusicCookie.getCode()==0){
            QQMusicCookie.ReqDTO req = qqMusicCookie.getReq();
            if (req.getCode()==0){
                return req.getData();
            }
        }
    return null;
    }

    /**
     * 检查cookie是否过期
     */
    public  QQMuserUserInfo checkCookie(String data){
        QQMuserUserInfo qqMuserUserInfo = JSONObject.parseObject(data, QQMuserUserInfo.class);
        if (qqMuserUserInfo==null){
            return null;
        }
        if (qqMuserUserInfo.getCode()==0){
            QQMuserUserInfo.ReqDTO req = qqMuserUserInfo.getReq();
            if (req.getCode()==0){
                return qqMuserUserInfo;
            }
        }
        return null;
    }
    /**
     * 刷新token
     */
    public QQMusicCookieInfo refreshCookie(String data){
        QQMusicCookie qqMusicCookie = JSONObject.parseObject(data, QQMusicCookie.class);
        if (qqMusicCookie.getCode()==0){
            QQMusicCookie.ReqDTO req1 = qqMusicCookie.getReq1();
            if (req1.getCode()==0){
                return req1.getData();
            }
        }
        return null;
    }


    /**
     * 获取收藏歌单
     */
    public CgiGetPlaylistFavInfo followSongList(JSONObject req) {
        String jsonString = req.toJSONString();
        return JSONObject.parseObject(jsonString, CgiGetPlaylistFavInfo.class);
    }

    /**
     * 获取用具歌单 id 201是我喜欢
     * @param req
     * @return
     */

    public PlaylistBaseRead userSelfSongList(JSONObject req) {
        String jsonString = req.toJSONString();
        return JSONObject.parseObject(jsonString, PlaylistBaseRead.class);
    }


    public CgiGetAlbumFavInfo userALbymList(JSONObject req) {
        String jsonString = req.toJSONString();
        return JSONObject.parseObject(jsonString, CgiGetAlbumFavInfo.class);
    }

    public DissInfo songListInfo(JSONObject req) {
        String jsonString = req.toJSONString();
        return JSONObject.parseObject(jsonString, DissInfo.class);
    }

    public GetFollowSingerList followSingerList(JSONObject req) {
        String jsonString = req.toJSONString();
        return JSONObject.parseObject(jsonString, GetFollowSingerList.class);

    }
}
