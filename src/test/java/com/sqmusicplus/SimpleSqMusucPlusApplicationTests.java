package com.sqmusicplus;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.ListUtil;
import com.alibaba.fastjson2.JSONObject;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sqmusicplus.v3.plug.apple.hander.AppleMusicDRMHandler;
import com.sqmusicplus.v3.plug.entity.Music;
import com.sqmusicplus.v3.plug.qq.entity.QQMusicCookieInfo;
import com.sqmusicplus.v3.plug.qq.entity.QQMusicQr;
import com.sqmusicplus.v3.plug.qq.entity.QQMusicQrEventResult;
import com.sqmusicplus.v3.plug.qq.enums.LoginType;
import com.sqmusicplus.v3.plug.qq.enums.QRCodeLoginEvents;
import com.sqmusicplus.v3.plug.qq.hander.QQHander;
import com.sqmusicplus.v3.plug.qq.util.QQMusicUtil;
import com.sqmusicplus.v3.task.ScanQQVIPLikeMusicTask;
import com.sqmusicplus.v3.utils.*;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.io.IOException;

import java.util.*;



@SpringBootTest
@Slf4j
class SimpleSqMusucPlusApplicationTests {
//    public static final CookieManager manager = new CookieManager();
//    public static final   HttpClient client = HttpClient.newBuilder()
//            .followRedirects(HttpClient.Redirect.NEVER)
//            .cookieHandler(manager)
//            .build();


    private static final String APPLE_MUSIC_HOMEPAGE_URL = "https://beta.music.apple.com";
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private static OkHttpUtils builder = OkHttpUtils.builder();
    private static OkHttpClient client = builder.getOkHttpClient();


    private static final String AppleCookie = "{\"geo\":\"HK\",\"itspod\":\"31\",\"media-user-token\":\"Ati/NdVW8oz+CVO5nVKF2rX/y1XQKh0GOPJbECVL6e+tpTP/OKM3PFwJp9a/EQn/9LWT68PSiYugfjeQwnpuLWfqVc9G2ZfNdWrbq8dMfjenTw4cqdKZzInY7Q8HFygDI/gh8Up7s0nCvmU/RCqabsoKtJhhE1g6+YKUSbDb4gUIFn8yVzBn7dpx19ihAqpHK6FPUCNCmXCLUMGUOrWRcHmyhCa1pl5vOvgC0sCFeoMRcx92+g\",\"itua\":\"CN\",\"pltvcid\":\"99b9bbb002f143b1975483cb225ee0e3031\",\"pldfltcid\":\"f7478cfcaf4f45f7b6f0a3e7454c8ee4031\",\"mut-refresh\":\"1\",\"itre\":\"0\",\"s_fid\":\"62E6144F4247D8C0-15F4D4E2059446B2\",\"s_cc\":\"true\",\"s_vi\":\"[CS]v1|3474DD7FEC3CFE2F-4000019BB8A78FC2[CE]\",\"s_sq\":\"[[B]]\"}";







    /**
     * 获取 Apple Music API 的 Bearer Token
     * @return Bearer Token 字符串
     * @throws IOException 网络请求异常
     */
    public static String getBearerToken() throws IOException {
        // 1. 获取主页内容
        String homePageContent = getHomePage();

        // 2. 从主页中提取 JS 文件路径
        String jsFilePath = extractJsFilePath(homePageContent);

        // 3. 获取 JS 文件内容
        String jsFileContent = getJsFileContent(jsFilePath);

        // 4. 从 JS 文件中提取 Bearer Token
        return extractBearerToken(jsFileContent);
    }



    /**
    }

    /**
     * 获取 Apple Music 主页内容
     * @return 主页 HTML 内容
     * @throws IOException 网络请求异常
     */
    private static String getHomePage() throws IOException {
        Request request = new Request.Builder()
                .url(APPLE_MUSIC_HOMEPAGE_URL)
                .build();


        try (Response response =client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("获取主页失败: " + response);
            }
            return response.body().string();
        }
    }

    /**
     * 从主页内容中提取 JS 文件路径
     * @param homePageContent 主页 HTML 内容
     * @return JS 文件路径
     */
    private static String extractJsFilePath(String homePageContent) {
        Pattern pattern = Pattern.compile("(assets/index-legacy-[^/]+\\.js)");
        Matcher matcher = pattern.matcher(homePageContent);

        if (matcher.find()) {
            return matcher.group(1);
        } else {
            throw new RuntimeException("未找到 JS 文件路径");
        }
    }

    /**
     * 获取 JS 文件内容
     * @param jsFilePath JS 文件路径
     * @return JS 文件内容
     * @throws IOException 网络请求异常
     */
    private static String getJsFileContent(String jsFilePath) throws IOException {
        Request request = new Request.Builder()
                .url(APPLE_MUSIC_HOMEPAGE_URL + "/" + jsFilePath)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("获取 JS 文件失败: " + response);
            }
            return response.body().string();
        }
    }

    /**
     * 从 JS 文件内容中提取 Bearer Token
     * @param jsFileContent JS 文件内容
     * @return Bearer Token
     */
    private static String extractBearerToken(String jsFileContent) {
        Pattern pattern = Pattern.compile("(?=eyJh)(.*?)(?=\")");
        Matcher matcher = pattern.matcher(jsFileContent);

        if (matcher.find()) {
            return matcher.group(1);
        } else {
            throw new RuntimeException("未找到 Bearer Token");
        }
    }

    /**
     * 使用获取到的 Token 发起 API 请求示例
     *
     * @param token          Bearer Token
     * @param mediausertoken
     * @param itua
     * @throws IOException 网络请求异常
     */
    public static void makeApiRequest(String token, String mediausertoken, String itua) throws IOException {
        Request request = new Request.Builder()
                .url("https://amp-api.music.apple.com/v1/catalog/"+itua+"/songs/905206660")
                .addHeader("authorization", "Bearer " + token)
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:95.0) Gecko/20100101 Firefox/95.0")
                .addHeader("Accept", "application/json")
                .addHeader("Accept-Language", "en-US,en;q=0.5")
                // 移除Accept-Encoding，让OkHttp自动处理解压
                .addHeader("content-type", "application/json")
                .addHeader("Media-User-Token", mediausertoken)
                .addHeader("x-apple-renewal", "true")
                .addHeader("DNT", "1")
                .addHeader("Connection", "keep-alive")
                .addHeader("Sec-Fetch-Dest", "empty")
                .addHeader("Sec-Fetch-Mode", "cors")
                .addHeader("Sec-Fetch-Site", "same-site")
                .addHeader("origin", APPLE_MUSIC_HOMEPAGE_URL)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("API 请求失败: " + response);
            }
            
            // 打印响应头信息
            System.out.println("Content-Type: " + response.header("Content-Type"));
            System.out.println("Content-Encoding: " + response.header("Content-Encoding"));
            System.out.println("Content-Length: " + response.header("Content-Length"));
            
            // 检查响应体是否为空
            if (response.body() == null) {
                System.out.println("API 响应为空");
                return;
            }
            
            // 获取响应内容
            String responseBody = response.body().string();
            
            // 检查响应内容是否为空
            if (responseBody.isEmpty()) {
                System.out.println("API 响应内容为空");
                return;
            }
            
            System.out.println("API 响应长度: " + responseBody.length());
            
            // 尝试解析为JSON
            try {
                JSONObject json = JSONObject.parseObject(responseBody);
                System.out.println("API 响应 (JSON格式): " + json.toJSONString());
            } catch (Exception e) {
                // 如果不是有效的JSON，直接打印内容
                System.out.println("API 响应前500字符: " + responseBody.substring(0, Math.min(500, responseBody.length())));
                System.out.println("完整API响应: " + responseBody);
            }
        }
    }


    /**
     * 通过获取用户资料来验证 token（更可靠的验证方式）
     * @param token Bearer Token
     * @return true 表示有效，false 表示无效
     */
    public static boolean isTokenValidByUserProfile(String token,String mediausertoken) {
        if (token == null || token.isEmpty()) {
            return false;
        }

        Request request = new Request.Builder()
                .url("https://api.music.apple.com/v1/me/recommendations")
                .addHeader("authorization", "Bearer " + token)
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:95.0) Gecko/20100101 Firefox/95.0")
                .addHeader("Accept", "application/json")
                .addHeader("Accept-Language", "en-US,en;q=0.5")
                // 移除Accept-Encoding，让OkHttp自动处理解压
                .addHeader("content-type", "application/json")
                .addHeader("Media-User-Token", mediausertoken)
                .addHeader("x-apple-renewal", "true")
                .addHeader("DNT", "1")
                .addHeader("Connection", "keep-alive")
                .addHeader("Sec-Fetch-Dest", "empty")
                .addHeader("Sec-Fetch-Mode", "cors")
                .addHeader("Sec-Fetch-Site", "same-site")
                .addHeader("origin", APPLE_MUSIC_HOMEPAGE_URL)
                .build();

        try (Response response = client.newCall(request).execute()) {
            int code = response.code();
            // 2xx 表示成功，403 表示认证失败
            return code >= 200 && code < 300;
        } catch (IOException e) {
            System.err.println("验证 token 时发生网络错误: " + e.getMessage());
            return false;
        }
    }
    //增加检查token是否失效




    /**
     * 获取车辆钥匙位置在车内还是车外
     * @param keyPosition
     */
    public static void getKeyPosition(KeyPositionDetectionV2 keyPositionDetection, String keyPosition) {
        double rssiValue = 0;

        try {
            rssiValue = Double.parseDouble(keyPosition);
            Integer i = keyPositionDetection.keyPositionDetection_V2(rssiValue);
            System.out.println("位置调用成功："+i);
        } catch (NumberFormatException e) {
            System.out.println("位置调用失败");
        }


    }
    /**
     * 获取蓝牙是靠近还是远离
     */
    public static void getBlePosition(RSSIAnalyzerV2 rssiAnalyzer,String keyPosition) {
        double rssiValue = 0;

        try {
            String data = rssiAnalyzer.getData(keyPosition);
            System.out.println("距离调用成功："+data);
        } catch (NumberFormatException e) {
            System.out.println("距离调用失败");
        }
    }

    public static void main(String[] args) {
        String downloadurl = "http://nmobi.kuwo.cn/mobi.s?f=kuwo&q=";
        String s = "user=0&source=kwplayer_ar_5.0.0.0_B_jiakong_vh.apk&type=convert_url_with_sign&rid=138810&br=2000kflac";

//            s = s.replaceAll("#\\{musicId}","184274130").replaceAll("#\\{brvalue}","2000");
            String encrypt = KuwoDES.encrypt(s);
//            char[] encode = Base64Coder.encode(bytes);
//            String out =  new String(encode);
            downloadurl =  downloadurl+encrypt;
            System.out.println(downloadurl);

//            log.error("获取下载链接失败：{}",e.getMessage());
            return ;
//        String s1 = DownloadUtils.getHttp().sync(downloadurl).get().getBody().toByteString().utf8();
//        System.out.println(s1);
//        downloadurl= s1.split("\n")[2].split("=")[1].split("\r")[0];






//         KeyPositionDetectionV2 keyPositionDetection = new KeyPositionDetectionV2();
//         RSSIAnalyzerV2 rssiAnalyzer = new RSSIAnalyzerV2();
//         //随机生成-55 到-75的数值 生成1000个
//         for (int i = 0; i < 1000; i++) {
//             double rssiValue = (Math.random() * (-55 - (-75)) + (-75));
//             getKeyPosition(keyPositionDetection, String.valueOf(rssiValue));
//             getBlePosition(rssiAnalyzer, String.valueOf(rssiValue));
//         }








//        String url = "https://www.kuwo.cn/album_detail/77598406";
//
//        if (url.contains("c6.y.qq.com")) {
//            System.out.println("QQ音乐");
//        }
//        else if(url.contains("i.y.qq.com")){
//            System.out.println("QQ网页");
//        }
//        else if (url.contains("www.kuwo.cn")) {
//            //酷我的
//            if (url.contains("album")||url.contains("album_detail")) {
//                System.out.println("酷我专辑");
//            }
//            else if (url.contains("playlist")||url.contains("playlist_detail")) {
//                System.out.println("酷我歌单");
//            }
//
//            else if(url.contains("yinyue")||url.contains("play_detail")){
//                System.out.println("酷我单曲");
//
//            }
//            else{
//                throw new RuntimeException("未知的分享类型酷我仅支持 歌单、专辑、单曲");
//            }
//        }
//        else if (url.contains("music.163.com")) {
//            //专辑
//            if (url.contains("album")) {
//                System.out.println("网易专辑");
//            }else if (url.contains("playlist")) {
//                System.out.println("网易歌单");
//            }else if(url.contains("song")){
//                System.out.println("网易单曲");
//            }
//        }
//        else if (url.contains("kugou.com")) {
//            System.out.println("酷狗");
//        }
//        else{
//            throw new RuntimeException("未知的分享类型仅支持QQ、酷狗概念版、酷我、网易云音乐");
//        }








//
//
//       String baseMusicName= "1！2@3#4￥5%6……7&8*9(0)q!w@3#4$5%6%^懂啊将老啊发|||||送、\\、、扥/////收到《》？dsa没了！！";
//                String open_symbol_remove_symbol = "！￥!@#$%^&*()_+(*^&%$~!@|]{}[]、、/。，\\《》？~……";
//                if (StringUtils.isNotBlank(open_symbol_remove_symbol)) {
//                    char[] chars = open_symbol_remove_symbol.toCharArray();
//                    for (char c : chars) {
//                        if (c != ' ') {
//                            // 转义特殊字符以避免正则表达式问题
//                            String escapedSymbol = Pattern.quote(String.valueOf(c));
//                            baseMusicName = baseMusicName.replaceAll(escapedSymbol, "");
//
//                        }
//                    }
//                }
//                System.out.println(baseMusicName);




////获取歌曲基础信息
//        AppleMusicDRMHandler.StreamInfo streamInfoFromM3u8Url = AppleMusicDRMHandler.getStreamInfoFromM3u8Url(
////                "https://aod.itunes.apple.com/itunes-assets/HLSMusic125/v4/7c/21/fd/7c21fd85-4b89-ab25-3eb9-74f1223c662e/P273509279_lossless.m3u8"
//                "https://aod.itunes.apple.com/itunes-assets/HLSMusic221/v4/e5/2a/b4/e52ab494-0dd2-d712-7e70-7cd305cc4b4e/P1031217200_lossless.m3u8"
//        );


//        String decryptionKey = AppleMusicDRMHandler.getDecryptionKey(
//                streamInfoFromM3u8Url.getWidevinePssh(),
////                trackMetadata.getId()
//        );

//        System.out.println(streamInfoFromM3u8Url);

//        String ttml = "<tt xmlns=\"http://www.w3.org/ns/ttml\" xmlns:itunes=\"http://music.apple.com/lyric-ttml-internal\" xmlns:ttm=\"http://www.w3.org/ns/ttml#metadata\" itunes:timing=\"Line\" xml:lang=\"zh-Hant\"><head><metadata><ttm:agent type=\"person\" xml:id=\"v1\"/><iTunesMetadata xmlns=\"http://music.apple.com/lyric-ttml-internal\" leadingSilence=\"0.320\"><translations/><songwriters><songwriter>周杰倫</songwriter></songwriters></iTunesMetadata></metadata></head><body dur=\"4:19.400\"><div begin=\"11.104\" end=\"31.733\" itunes:songPart=\"Intro\"><p begin=\"11.104\" end=\"16.300\" itunes:key=\"L1\" ttm:agent=\"v1\">一步兩步三步四步望著天 手牽手</p><p begin=\"16.469\" end=\"21.558\" itunes:key=\"L2\" ttm:agent=\"v1\">一顆兩顆三顆四顆連成線看星星</p><p begin=\"21.954\" end=\"27.088\" itunes:key=\"L3\" ttm:agent=\"v1\">一步兩步三步四步望著天 手牽手</p><p begin=\"27.215\" end=\"31.733\" itunes:key=\"L4\" ttm:agent=\"v1\">一顆兩顆三顆四顆連成線看星</p></div><div begin=\"33.282\" end=\"52.272\" itunes:songPart=\"Verse\"><p begin=\"33.282\" end=\"37.881\" itunes:key=\"L5\" ttm:agent=\"v1\">乘著風 遊盪在藍天邊</p><p begin=\"38.676\" end=\"43.502\" itunes:key=\"L6\" ttm:agent=\"v1\">一片雲掉落在我面前</p><p begin=\"44.011\" end=\"48.582\" itunes:key=\"L7\" ttm:agent=\"v1\">捏成你的形狀 隨風跟著我</p><p begin=\"48.809\" end=\"52.272\" itunes:key=\"L8\" ttm:agent=\"v1\">一口一口吃掉憂愁</p></div><div begin=\"54.941\" end=\"1:13.882\" itunes:songPart=\"Verse\"><p begin=\"54.941\" end=\"59.428\" itunes:key=\"L9\" ttm:agent=\"v1\">載著你 彷彿載著陽光</p><p begin=\"1:00.382\" end=\"1:05.107\" itunes:key=\"L10\" ttm:agent=\"v1\">不管到哪裡都是晴天</p><p begin=\"1:05.743\" end=\"1:10.158\" itunes:key=\"L11\" ttm:agent=\"v1\">蝴蝶自在飛 花也佈滿天</p><p begin=\"1:10.419\" end=\"1:13.882\" itunes:key=\"L12\" ttm:agent=\"v1\">一朵一朵因你而香</p></div><div begin=\"1:14.737\" end=\"1:35.847\" itunes:songPart=\"PreChorus\"><p begin=\"1:14.737\" end=\"1:19.358\" itunes:key=\"L13\" ttm:agent=\"v1\">試圖讓夕陽飛翔</p><p begin=\"1:19.846\" end=\"1:27.362\" itunes:key=\"L14\" ttm:agent=\"v1\">帶領你我環繞大自然</p><p begin=\"1:28.257\" end=\"1:35.847\" itunes:key=\"L15\" ttm:agent=\"v1\">迎著風 開始共渡每一天</p></div><div begin=\"1:36.291\" end=\"1:56.941\" itunes:songPart=\"Chorus\"><p begin=\"1:36.291\" end=\"1:41.487\" itunes:key=\"L16\" ttm:agent=\"v1\">手牽手 一步兩步三步四步 望著天</p><p begin=\"1:41.726\" end=\"1:46.877\" itunes:key=\"L17\" ttm:agent=\"v1\">看星星 一顆兩顆三顆四顆 連成線</p><p begin=\"1:47.176\" end=\"1:51.543\" itunes:key=\"L18\" ttm:agent=\"v1\">背對背默默許下心願</p><p begin=\"1:51.899\" end=\"1:56.941\" itunes:key=\"L19\" ttm:agent=\"v1\">看遠方的星是否聽得見</p></div><div begin=\"1:57.913\" end=\"2:21.896\" itunes:songPart=\"Chorus\"><p begin=\"1:57.913\" end=\"2:03.109\" itunes:key=\"L20\" ttm:agent=\"v1\">手牽手 一步兩步三步四步 望著天</p><p begin=\"2:03.340\" end=\"2:08.491\" itunes:key=\"L21\" ttm:agent=\"v1\">看星星 一顆兩顆三顆四顆 連成線</p><p begin=\"2:08.715\" end=\"2:13.082\" itunes:key=\"L22\" ttm:agent=\"v1\">背對背默默許下心願</p><p begin=\"2:13.447\" end=\"2:18.354\" itunes:key=\"L23\" ttm:agent=\"v1\">看遠方的星如果聽得見</p><p begin=\"2:18.853\" end=\"2:21.896\" itunes:key=\"L24\" ttm:agent=\"v1\">它一定實現</p></div><div begin=\"2:42.776\" end=\"3:01.820\" itunes:songPart=\"Verse\"><p begin=\"2:42.776\" end=\"2:47.363\" itunes:key=\"L25\" ttm:agent=\"v1\">載著你 彷彿載著陽光</p><p begin=\"2:48.257\" end=\"2:53.082\" itunes:key=\"L26\" ttm:agent=\"v1\">不管到哪裡都是晴天</p><p begin=\"2:53.625\" end=\"2:58.080\" itunes:key=\"L27\" ttm:agent=\"v1\">蝴蝶自在飛 花也佈滿天</p><p begin=\"2:58.357\" end=\"3:01.820\" itunes:key=\"L28\" ttm:agent=\"v1\">一朵一朵因你而香</p></div><div begin=\"3:02.615\" end=\"3:24.032\" itunes:songPart=\"PreChorus\"><p begin=\"3:02.615\" end=\"3:07.236\" itunes:key=\"L29\" ttm:agent=\"v1\">試圖讓夕陽飛翔</p><p begin=\"3:07.839\" end=\"3:15.269\" itunes:key=\"L30\" ttm:agent=\"v1\">帶領你我環繞大自然</p><p begin=\"3:16.219\" end=\"3:24.032\" itunes:key=\"L31\" ttm:agent=\"v1\">迎著風 開始共渡每一天</p></div><div begin=\"3:24.238\" end=\"3:44.881\" itunes:songPart=\"Chorus\"><p begin=\"3:24.238\" end=\"3:29.434\" itunes:key=\"L32\" ttm:agent=\"v1\">手牽手 一步兩步三步四步 望著天</p><p begin=\"3:29.639\" end=\"3:34.790\" itunes:key=\"L33\" ttm:agent=\"v1\">看星星 一顆兩顆三顆四顆 連成線</p><p begin=\"3:35.085\" end=\"3:39.452\" itunes:key=\"L34\" ttm:agent=\"v1\">背對背默默許下心願</p><p begin=\"3:39.839\" end=\"3:44.881\" itunes:key=\"L35\" ttm:agent=\"v1\">看遠方的星是否聽得見</p></div><div begin=\"3:45.863\" end=\"4:10.801\" itunes:songPart=\"Chorus\"><p begin=\"3:45.863\" end=\"3:51.059\" itunes:key=\"L36\" ttm:agent=\"v1\">手牽手 一步兩步三步四步 望著天</p><p begin=\"3:51.258\" end=\"3:56.409\" itunes:key=\"L37\" ttm:agent=\"v1\">看星星 一顆兩顆三顆四顆 連成線</p><p begin=\"3:56.688\" end=\"4:01.055\" itunes:key=\"L38\" ttm:agent=\"v1\">背對背默默許下心願</p><p begin=\"4:01.405\" end=\"4:06.412\" itunes:key=\"L39\" ttm:agent=\"v1\">看遠方的星如果聽得見</p><p begin=\"4:06.803\" end=\"4:10.801\" itunes:key=\"L40\" ttm:agent=\"v1\">它一定實現</p></div></body></tt>";
//        String ttml2="<tt xmlns=\"http://www.w3.org/ns/ttml\" xmlns:itunes=\"http://music.apple.com/lyric-ttml-internal\" xmlns:ttm=\"http://www.w3.org/ns/ttml#metadata\" itunes:timing=\"Word\" xml:lang=\"zh-Hant\"><head><metadata><ttm:agent type=\"person\" xml:id=\"v1\"/><iTunesMetadata xmlns=\"http://music.apple.com/lyric-ttml-internal\" leadingSilence=\"0.340\"><translations><translation type=\"replacement\" xml:lang=\"zh-Hans\"><text for=\"L1\"><span begin=\"17.723\" end=\"18.182\" xmlns=\"http://www.w3.org/ns/ttml\">黑</span><span begin=\"18.182\" end=\"19.022\" xmlns=\"http://www.w3.org/ns/ttml\">暗中</span><span begin=\"19.022\" end=\"20.349\" xmlns=\"http://www.w3.org/ns/ttml\">的我们</span><span begin=\"20.349\" end=\"20.872\" xmlns=\"http://www.w3.org/ns/ttml\">都</span><span begin=\"20.872\" end=\"22.121\" xmlns=\"http://www.w3.org/ns/ttml\">没有说</span><span begin=\"22.121\" end=\"23.093\" xmlns=\"http://www.w3.org/ns/ttml\">话</span></text><text for=\"L2\"><span begin=\"24.119\" end=\"24.695\" xmlns=\"http://www.w3.org/ns/ttml\">你</span><span begin=\"24.695\" end=\"25.751\" xmlns=\"http://www.w3.org/ns/ttml\">只想回</span><span begin=\"25.751\" end=\"26.507\" xmlns=\"http://www.w3.org/ns/ttml\">家</span> <span begin=\"27.840\" end=\"29.011\" xmlns=\"http://www.w3.org/ns/ttml\">不想你</span><span begin=\"29.011\" end=\"29.469\" xmlns=\"http://www.w3.org/ns/ttml\">回</span><span begin=\"29.469\" end=\"30.957\" xmlns=\"http://www.w3.org/ns/ttml\">家</span></text><text for=\"L3\"><span begin=\"32.347\" end=\"32.786\" xmlns=\"http://www.w3.org/ns/ttml\">寂</span><span begin=\"32.786\" end=\"33.638\" xmlns=\"http://www.w3.org/ns/ttml\">寞深</span><span begin=\"33.638\" end=\"34.958\" xmlns=\"http://www.w3.org/ns/ttml\">得像海</span> <span begin=\"34.958\" end=\"36.293\" xmlns=\"http://www.w3.org/ns/ttml\">太让人</span><span begin=\"36.293\" end=\"36.773\" xmlns=\"http://www.w3.org/ns/ttml\">害</span><span begin=\"36.773\" end=\"37.798\" xmlns=\"http://www.w3.org/ns/ttml\">怕</span></text><text for=\"L4\"><span begin=\"38.784\" end=\"39.971\" xmlns=\"http://www.w3.org/ns/ttml\">温柔你</span><span begin=\"39.971\" end=\"40.988\" xmlns=\"http://www.w3.org/ns/ttml\">的手</span> <span begin=\"41.965\" end=\"42.472\" xmlns=\"http://www.w3.org/ns/ttml\">轻轻</span><span begin=\"42.472\" end=\"43.265\" xmlns=\"http://www.w3.org/ns/ttml\">揉着</span><span begin=\"43.265\" end=\"44.034\" xmlns=\"http://www.w3.org/ns/ttml\">我的</span><span begin=\"44.034\" end=\"44.985\" xmlns=\"http://www.w3.org/ns/ttml\">发</span></text><text for=\"L5\"><span begin=\"46.138\" end=\"46.622\" xmlns=\"http://www.w3.org/ns/ttml\">你</span><span begin=\"46.622\" end=\"47.666\" xmlns=\"http://www.w3.org/ns/ttml\">的眉眼</span><span begin=\"47.666\" end=\"48.499\" xmlns=\"http://www.w3.org/ns/ttml\">说</span> <span begin=\"48.884\" end=\"49.424\" xmlns=\"http://www.w3.org/ns/ttml\">你</span><span begin=\"49.424\" end=\"50.508\" xmlns=\"http://www.w3.org/ns/ttml\">好渴望</span><span begin=\"50.508\" end=\"51.341\" xmlns=\"http://www.w3.org/ns/ttml\">我拥</span><span begin=\"51.341\" end=\"52.401\" xmlns=\"http://www.w3.org/ns/ttml\">抱</span></text><text for=\"L6\"><span begin=\"52.970\" end=\"53.388\" xmlns=\"http://www.w3.org/ns/ttml\">你</span><span begin=\"53.388\" end=\"54.253\" xmlns=\"http://www.w3.org/ns/ttml\">身体</span><span begin=\"54.253\" end=\"55.231\" xmlns=\"http://www.w3.org/ns/ttml\">却在</span><span begin=\"55.231\" end=\"56.583\" xmlns=\"http://www.w3.org/ns/ttml\">拼命逃</span> <span begin=\"56.979\" end=\"57.623\" xmlns=\"http://www.w3.org/ns/ttml\">当欲</span><span begin=\"57.623\" end=\"58.856\" xmlns=\"http://www.w3.org/ns/ttml\">望在燃</span><span begin=\"58.856\" end=\"1:02.695\" xmlns=\"http://www.w3.org/ns/ttml\">烧</span></text><text for=\"L7\"><span begin=\"1:05.899\" end=\"1:06.893\" xmlns=\"http://www.w3.org/ns/ttml\">你爱</span><span begin=\"1:07.285\" end=\"1:09.180\" xmlns=\"http://www.w3.org/ns/ttml\">我还是</span><span begin=\"1:09.476\" end=\"1:11.925\" xmlns=\"http://www.w3.org/ns/ttml\">他</span></text><text for=\"L8\"><span begin=\"1:12.656\" end=\"1:13.768\" xmlns=\"http://www.w3.org/ns/ttml\">是不是真</span><span begin=\"1:14.046\" end=\"1:14.905\" xmlns=\"http://www.w3.org/ns/ttml\">的他</span><span begin=\"1:14.905\" end=\"1:16.151\" xmlns=\"http://www.w3.org/ns/ttml\">有比我</span><span begin=\"1:16.151\" end=\"1:16.836\" xmlns=\"http://www.w3.org/ns/ttml\">好</span></text><text for=\"L9\"><span begin=\"1:17.058\" end=\"1:17.707\" xmlns=\"http://www.w3.org/ns/ttml\">你为</span><span begin=\"1:17.707\" end=\"1:18.978\" xmlns=\"http://www.w3.org/ns/ttml\">谁在挣</span><span begin=\"1:18.978\" end=\"1:20.110\" xmlns=\"http://www.w3.org/ns/ttml\">扎</span></text><text for=\"L10\"><span begin=\"1:20.766\" end=\"1:21.444\" xmlns=\"http://www.w3.org/ns/ttml\">你爱</span><span begin=\"1:21.802\" end=\"1:23.757\" xmlns=\"http://www.w3.org/ns/ttml\">我还是</span><span begin=\"1:24.112\" end=\"1:26.937\" xmlns=\"http://www.w3.org/ns/ttml\">他</span></text><text for=\"L11\"><span begin=\"1:27.350\" end=\"1:28.488\" xmlns=\"http://www.w3.org/ns/ttml\">就说出你</span><span begin=\"1:28.671\" end=\"1:29.573\" xmlns=\"http://www.w3.org/ns/ttml\">想说</span><span begin=\"1:29.701\" end=\"1:29.998\" xmlns=\"http://www.w3.org/ns/ttml\">的</span><span begin=\"1:29.998\" end=\"1:30.858\" xmlns=\"http://www.w3.org/ns/ttml\">真心</span><span begin=\"1:30.858\" end=\"1:31.484\" xmlns=\"http://www.w3.org/ns/ttml\">话</span></text><text for=\"L12\"><span begin=\"1:31.801\" end=\"1:32.363\" xmlns=\"http://www.w3.org/ns/ttml\">你到</span><span begin=\"1:32.363\" end=\"1:34.034\" xmlns=\"http://www.w3.org/ns/ttml\">底要跟我</span> <span begin=\"1:34.621\" end=\"1:35.148\" xmlns=\"http://www.w3.org/ns/ttml\">还</span><span begin=\"1:35.148\" end=\"1:35.474\" xmlns=\"http://www.w3.org/ns/ttml\">是</span><span begin=\"1:35.474\" end=\"1:38.828\" xmlns=\"http://www.w3.org/ns/ttml\">他</span></text><text for=\"L13\"><span begin=\"1:42.966\" end=\"1:43.412\" xmlns=\"http://www.w3.org/ns/ttml\">爱</span> <span begin=\"1:43.706\" end=\"1:44.426\" xmlns=\"http://www.w3.org/ns/ttml\">爱</span> <span begin=\"1:44.792\" end=\"1:45.645\" xmlns=\"http://www.w3.org/ns/ttml\">爱</span><span begin=\"1:46.047\" end=\"1:47.499\" xmlns=\"http://www.w3.org/ns/ttml\">他</span></text><text for=\"L14\"><span begin=\"1:51.162\" end=\"1:51.582\" xmlns=\"http://www.w3.org/ns/ttml\">这</span><span begin=\"1:51.582\" end=\"1:52.789\" xmlns=\"http://www.w3.org/ns/ttml\">是不是</span><span begin=\"1:52.789\" end=\"1:53.735\" xmlns=\"http://www.w3.org/ns/ttml\">命运</span><span begin=\"1:53.735\" end=\"1:55.039\" xmlns=\"http://www.w3.org/ns/ttml\">对我的</span><span begin=\"1:55.039\" end=\"1:55.438\" xmlns=\"http://www.w3.org/ns/ttml\">惩</span><span begin=\"1:55.438\" end=\"1:56.581\" xmlns=\"http://www.w3.org/ns/ttml\">罚</span></text><text for=\"L15\"><span begin=\"1:57.247\" end=\"1:57.866\" xmlns=\"http://www.w3.org/ns/ttml\">爱你</span><span begin=\"1:57.866\" end=\"1:59.484\" xmlns=\"http://www.w3.org/ns/ttml\">也没办法</span> <span begin=\"2:00.788\" end=\"2:01.547\" xmlns=\"http://www.w3.org/ns/ttml\">恨你</span><span begin=\"2:01.547\" end=\"2:02.650\" xmlns=\"http://www.w3.org/ns/ttml\">也没办</span><span begin=\"2:02.650\" end=\"2:04.494\" xmlns=\"http://www.w3.org/ns/ttml\">法</span></text><text for=\"L16\"><span begin=\"2:05.456\" end=\"2:06.028\" xmlns=\"http://www.w3.org/ns/ttml\">陷</span><span begin=\"2:06.028\" end=\"2:07.235\" xmlns=\"http://www.w3.org/ns/ttml\">在这个</span><span begin=\"2:07.235\" end=\"2:08.084\" xmlns=\"http://www.w3.org/ns/ttml\">漩涡</span><span begin=\"2:08.091\" end=\"2:09.084\" xmlns=\"http://www.w3.org/ns/ttml\">只想</span><span begin=\"2:09.084\" end=\"2:09.833\" xmlns=\"http://www.w3.org/ns/ttml\">挣脱</span><span begin=\"2:09.833\" end=\"2:10.856\" xmlns=\"http://www.w3.org/ns/ttml\">它</span></text><text for=\"L17\"><span begin=\"2:11.868\" end=\"2:12.305\" xmlns=\"http://www.w3.org/ns/ttml\">拉</span><span begin=\"2:12.305\" end=\"2:13.488\" xmlns=\"http://www.w3.org/ns/ttml\">住你的</span><span begin=\"2:13.488\" end=\"2:14.373\" xmlns=\"http://www.w3.org/ns/ttml\">手</span> <span begin=\"2:15.067\" end=\"2:15.864\" xmlns=\"http://www.w3.org/ns/ttml\">却让我</span><span begin=\"2:15.864\" end=\"2:17.096\" xmlns=\"http://www.w3.org/ns/ttml\">也被拖</span><span begin=\"2:17.096\" end=\"2:18.220\" xmlns=\"http://www.w3.org/ns/ttml\">下</span></text><text for=\"L18\"><span begin=\"2:19.164\" end=\"2:19.624\" xmlns=\"http://www.w3.org/ns/ttml\">你</span><span begin=\"2:19.624\" end=\"2:20.276\" xmlns=\"http://www.w3.org/ns/ttml\">的眉</span><span begin=\"2:20.276\" end=\"2:21.227\" xmlns=\"http://www.w3.org/ns/ttml\">眼说</span> <span begin=\"2:21.858\" end=\"2:22.947\" xmlns=\"http://www.w3.org/ns/ttml\">你不渴</span><span begin=\"2:22.947\" end=\"2:24.239\" xmlns=\"http://www.w3.org/ns/ttml\">望我拥</span><span begin=\"2:24.239\" end=\"2:25.279\" xmlns=\"http://www.w3.org/ns/ttml\">抱</span></text><text for=\"L19\"><span begin=\"2:25.899\" end=\"2:26.811\" xmlns=\"http://www.w3.org/ns/ttml\">每当</span><span begin=\"2:26.811\" end=\"2:28.386\" xmlns=\"http://www.w3.org/ns/ttml\">爱变成了</span><span begin=\"2:28.386\" end=\"2:29.219\" xmlns=\"http://www.w3.org/ns/ttml\">煎熬</span> <span begin=\"2:29.650\" end=\"2:30.353\" xmlns=\"http://www.w3.org/ns/ttml\">你就</span><span begin=\"2:30.353\" end=\"2:31.617\" xmlns=\"http://www.w3.org/ns/ttml\">开始要</span><span begin=\"2:31.617\" end=\"2:34.446\" xmlns=\"http://www.w3.org/ns/ttml\">逃</span></text><text for=\"L20\"><span begin=\"2:35.145\" end=\"2:35.934\" xmlns=\"http://www.w3.org/ns/ttml\">你爱</span><span begin=\"2:36.215\" end=\"2:38.043\" xmlns=\"http://www.w3.org/ns/ttml\">我还是</span><span begin=\"2:38.617\" end=\"2:40.817\" xmlns=\"http://www.w3.org/ns/ttml\">他</span></text><text for=\"L21\"><span begin=\"2:41.627\" end=\"2:42.316\" xmlns=\"http://www.w3.org/ns/ttml\">是不是</span><span begin=\"2:42.316\" end=\"2:43.078\" xmlns=\"http://www.w3.org/ns/ttml\">我</span><span begin=\"2:43.078\" end=\"2:43.694\" xmlns=\"http://www.w3.org/ns/ttml\">可以</span><span begin=\"2:43.945\" end=\"2:44.619\" xmlns=\"http://www.w3.org/ns/ttml\">做得</span><span begin=\"2:44.853\" end=\"2:45.071\" xmlns=\"http://www.w3.org/ns/ttml\">更</span><span begin=\"2:45.071\" end=\"2:45.697\" xmlns=\"http://www.w3.org/ns/ttml\">好</span></text><text for=\"L22\"><span begin=\"2:46.016\" end=\"2:46.607\" xmlns=\"http://www.w3.org/ns/ttml\">让你</span><span begin=\"2:46.722\" end=\"2:47.326\" xmlns=\"http://www.w3.org/ns/ttml\">不再</span><span begin=\"2:47.459\" end=\"2:47.818\" xmlns=\"http://www.w3.org/ns/ttml\">挣</span><span begin=\"2:47.818\" end=\"2:49.140\" xmlns=\"http://www.w3.org/ns/ttml\">扎</span></text><text for=\"L23\"><span begin=\"2:49.523\" end=\"2:50.441\" xmlns=\"http://www.w3.org/ns/ttml\">你爱</span><span begin=\"2:50.669\" end=\"2:51.510\" xmlns=\"http://www.w3.org/ns/ttml\">我</span><span begin=\"2:51.510\" end=\"2:52.773\" xmlns=\"http://www.w3.org/ns/ttml\">还是</span><span begin=\"2:52.773\" end=\"2:55.196\" xmlns=\"http://www.w3.org/ns/ttml\">他</span></text><text for=\"L24\"><span begin=\"2:56.128\" end=\"2:57.042\" xmlns=\"http://www.w3.org/ns/ttml\">我宁愿听</span><span begin=\"2:57.437\" end=\"2:58.062\" xmlns=\"http://www.w3.org/ns/ttml\">到残</span><span begin=\"2:58.307\" end=\"2:59.079\" xmlns=\"http://www.w3.org/ns/ttml\">忍的</span><span begin=\"2:59.079\" end=\"2:59.454\" xmlns=\"http://www.w3.org/ns/ttml\">回</span><span begin=\"2:59.454\" end=\"3:00.178\" xmlns=\"http://www.w3.org/ns/ttml\">答</span></text><text for=\"L25\"><span begin=\"3:00.444\" end=\"3:01.136\" xmlns=\"http://www.w3.org/ns/ttml\">也不</span><span begin=\"3:01.136\" end=\"3:01.806\" xmlns=\"http://www.w3.org/ns/ttml\">要再</span><span begin=\"3:01.931\" end=\"3:02.246\" xmlns=\"http://www.w3.org/ns/ttml\">被</span><span begin=\"3:02.246\" end=\"3:03.633\" xmlns=\"http://www.w3.org/ns/ttml\">耍</span></text><text for=\"L26\"><span begin=\"3:04.064\" end=\"3:04.883\" xmlns=\"http://www.w3.org/ns/ttml\">你爱</span><span begin=\"3:05.164\" end=\"3:06.992\" xmlns=\"http://www.w3.org/ns/ttml\">我还是</span><span begin=\"3:07.308\" end=\"3:10.018\" xmlns=\"http://www.w3.org/ns/ttml\">他</span></text><text for=\"L27\"><span begin=\"3:10.590\" end=\"3:11.442\" xmlns=\"http://www.w3.org/ns/ttml\">我为你找</span><span begin=\"3:11.720\" end=\"3:12.515\" xmlns=\"http://www.w3.org/ns/ttml\">了一</span><span begin=\"3:12.784\" end=\"3:13.398\" xmlns=\"http://www.w3.org/ns/ttml\">百个</span><span begin=\"3:13.585\" end=\"3:13.979\" xmlns=\"http://www.w3.org/ns/ttml\">理</span><span begin=\"3:13.979\" end=\"3:14.585\" xmlns=\"http://www.w3.org/ns/ttml\">由</span></text><text for=\"L28\"><span begin=\"3:14.881\" end=\"3:15.481\" xmlns=\"http://www.w3.org/ns/ttml\">我就</span><span begin=\"3:15.517\" end=\"3:16.702\" xmlns=\"http://www.w3.org/ns/ttml\">是那么</span><span begin=\"3:16.702\" end=\"3:18.171\" xmlns=\"http://www.w3.org/ns/ttml\">傻</span></text><text for=\"L29\"><span begin=\"3:18.508\" end=\"3:19.426\" xmlns=\"http://www.w3.org/ns/ttml\">你爱</span><span begin=\"3:19.654\" end=\"3:20.495\" xmlns=\"http://www.w3.org/ns/ttml\">我</span><span begin=\"3:20.495\" end=\"3:21.758\" xmlns=\"http://www.w3.org/ns/ttml\">还是</span><span begin=\"3:21.758\" end=\"3:24.679\" xmlns=\"http://www.w3.org/ns/ttml\">他</span></text><text for=\"L30\"><span begin=\"3:24.958\" end=\"3:25.992\" xmlns=\"http://www.w3.org/ns/ttml\">是否沉默</span><span begin=\"3:26.362\" end=\"3:26.930\" xmlns=\"http://www.w3.org/ns/ttml\">代替</span><span begin=\"3:27.302\" end=\"3:27.934\" xmlns=\"http://www.w3.org/ns/ttml\">你的</span><span begin=\"3:28.078\" end=\"3:28.393\" xmlns=\"http://www.w3.org/ns/ttml\">回</span><span begin=\"3:28.393\" end=\"3:29.093\" xmlns=\"http://www.w3.org/ns/ttml\">答</span></text><text for=\"L31\"><span begin=\"3:29.344\" end=\"3:29.815\" xmlns=\"http://www.w3.org/ns/ttml\">我应</span><span begin=\"3:29.815\" end=\"3:31.394\" xmlns=\"http://www.w3.org/ns/ttml\">该明白</span><span begin=\"3:31.394\" end=\"3:33.931\" xmlns=\"http://www.w3.org/ns/ttml\">吧</span></text><text for=\"L32\"><span begin=\"4:07.399\" end=\"4:08.204\" xmlns=\"http://www.w3.org/ns/ttml\">爱</span><span begin=\"4:08.204\" end=\"4:09.062\" xmlns=\"http://www.w3.org/ns/ttml\">我</span><span begin=\"4:09.062\" end=\"4:09.919\" xmlns=\"http://www.w3.org/ns/ttml\">还是</span><span begin=\"4:10.390\" end=\"4:11.959\" xmlns=\"http://www.w3.org/ns/ttml\">他</span></text><text for=\"L33\"><span begin=\"4:13.715\" end=\"4:14.399\" xmlns=\"http://www.w3.org/ns/ttml\">你都已</span><span begin=\"4:14.399\" end=\"4:15.067\" xmlns=\"http://www.w3.org/ns/ttml\">看</span><span begin=\"4:15.067\" end=\"4:15.677\" xmlns=\"http://www.w3.org/ns/ttml\">不到</span><span begin=\"4:15.863\" end=\"4:16.617\" xmlns=\"http://www.w3.org/ns/ttml\">我们</span><span begin=\"4:16.973\" end=\"4:17.142\" xmlns=\"http://www.w3.org/ns/ttml\">的</span><span begin=\"4:17.142\" end=\"4:17.686\" xmlns=\"http://www.w3.org/ns/ttml\">好</span></text><text for=\"L34\"><span begin=\"4:18.130\" end=\"4:18.251\" xmlns=\"http://www.w3.org/ns/ttml\">我</span><span begin=\"4:18.251\" end=\"4:18.737\" xmlns=\"http://www.w3.org/ns/ttml\">还</span><span begin=\"4:18.737\" end=\"4:18.908\" xmlns=\"http://www.w3.org/ns/ttml\">为</span><span begin=\"4:18.908\" end=\"4:19.489\" xmlns=\"http://www.w3.org/ns/ttml\">谁</span><span begin=\"4:19.489\" end=\"4:19.857\" xmlns=\"http://www.w3.org/ns/ttml\">牵</span><span begin=\"4:19.857\" end=\"4:20.727\" xmlns=\"http://www.w3.org/ns/ttml\">挂</span></text><text for=\"L35\"><span begin=\"4:21.544\" end=\"4:22.086\" xmlns=\"http://www.w3.org/ns/ttml\">你爱</span><span begin=\"4:22.279\" end=\"4:23.430\" xmlns=\"http://www.w3.org/ns/ttml\">我</span><span begin=\"4:23.430\" end=\"4:24.162\" xmlns=\"http://www.w3.org/ns/ttml\">还是</span><span begin=\"4:24.714\" end=\"4:27.732\" xmlns=\"http://www.w3.org/ns/ttml\">他</span></text><text for=\"L36\"><span begin=\"4:27.943\" end=\"4:28.501\" xmlns=\"http://www.w3.org/ns/ttml\">是否</span><span begin=\"4:28.501\" end=\"4:29.082\" xmlns=\"http://www.w3.org/ns/ttml\">沉默</span><span begin=\"4:29.420\" end=\"4:30.048\" xmlns=\"http://www.w3.org/ns/ttml\">就是</span><span begin=\"4:30.259\" end=\"4:30.895\" xmlns=\"http://www.w3.org/ns/ttml\">你的</span><span begin=\"4:31.120\" end=\"4:31.409\" xmlns=\"http://www.w3.org/ns/ttml\">回</span><span begin=\"4:31.409\" end=\"4:32.076\" xmlns=\"http://www.w3.org/ns/ttml\">答</span></text><text for=\"L37\"><span begin=\"4:32.323\" end=\"4:32.900\" xmlns=\"http://www.w3.org/ns/ttml\">我们</span><span begin=\"4:32.979\" end=\"4:33.749\" xmlns=\"http://www.w3.org/ns/ttml\">都别</span><span begin=\"4:33.921\" end=\"4:34.340\" xmlns=\"http://www.w3.org/ns/ttml\">挣</span><span begin=\"4:34.340\" end=\"4:37.944\" xmlns=\"http://www.w3.org/ns/ttml\">扎</span></text><text for=\"L38\"><span begin=\"4:38.805\" end=\"4:39.260\" xmlns=\"http://www.w3.org/ns/ttml\">去</span><span begin=\"4:39.260\" end=\"4:40.312\" xmlns=\"http://www.w3.org/ns/ttml\">爱</span><span begin=\"4:40.312\" end=\"4:45.522\" xmlns=\"http://www.w3.org/ns/ttml\">他</span></text></translation></translations><songwriters><songwriter>娃娃</songwriter><songwriter>陶喆</songwriter></songwriters></iTunesMetadata></metadata></head><body dur=\"4:52.667\"><div begin=\"17.723\" end=\"44.985\" itunes:songPart=\"Verse\"><p begin=\"17.723\" end=\"23.093\" itunes:key=\"L1\" ttm:agent=\"v1\"><span begin=\"17.723\" end=\"18.182\">黑</span><span begin=\"18.182\" end=\"19.022\">暗中</span><span begin=\"19.022\" end=\"20.349\">的我們</span><span begin=\"20.349\" end=\"20.872\">都</span><span begin=\"20.872\" end=\"22.121\">沒有說</span><span begin=\"22.121\" end=\"23.093\">話</span></p><p begin=\"24.119\" end=\"30.957\" itunes:key=\"L2\" ttm:agent=\"v1\"><span begin=\"24.119\" end=\"24.695\">你</span><span begin=\"24.695\" end=\"25.751\">只想回</span><span begin=\"25.751\" end=\"26.507\">家</span> <span begin=\"27.840\" end=\"29.011\">不想你</span><span begin=\"29.011\" end=\"29.469\">回</span><span begin=\"29.469\" end=\"30.957\">家</span></p><p begin=\"32.347\" end=\"37.798\" itunes:key=\"L3\" ttm:agent=\"v1\"><span begin=\"32.347\" end=\"32.786\">寂</span><span begin=\"32.786\" end=\"33.638\">寞深</span><span begin=\"33.638\" end=\"34.958\">得像海</span> <span begin=\"34.958\" end=\"36.293\">太讓人</span><span begin=\"36.293\" end=\"36.773\">害</span><span begin=\"36.773\" end=\"37.798\">怕</span></p><p begin=\"38.784\" end=\"44.985\" itunes:key=\"L4\" ttm:agent=\"v1\"><span begin=\"38.784\" end=\"39.971\">溫柔你</span><span begin=\"39.971\" end=\"40.988\">的手</span> <span begin=\"41.965\" end=\"42.472\">輕輕</span><span begin=\"42.472\" end=\"43.265\">揉著</span><span begin=\"43.265\" end=\"44.034\">我的</span><span begin=\"44.034\" end=\"44.985\">髮</span></p></div><div begin=\"46.138\" end=\"1:02.695\" itunes:songPart=\"PreChorus\"><p begin=\"46.138\" end=\"52.401\" itunes:key=\"L5\" ttm:agent=\"v1\"><span begin=\"46.138\" end=\"46.622\">你</span><span begin=\"46.622\" end=\"47.666\">的眉眼</span><span begin=\"47.666\" end=\"48.499\">說</span> <span begin=\"48.884\" end=\"49.424\">你</span><span begin=\"49.424\" end=\"50.508\">好渴望</span><span begin=\"50.508\" end=\"51.341\">我擁</span><span begin=\"51.341\" end=\"52.401\">抱</span></p><p begin=\"52.970\" end=\"1:02.695\" itunes:key=\"L6\" ttm:agent=\"v1\"><span begin=\"52.970\" end=\"53.388\">你</span><span begin=\"53.388\" end=\"54.253\">身體</span><span begin=\"54.253\" end=\"55.231\">卻在</span><span begin=\"55.231\" end=\"56.583\">拚命逃</span> <span begin=\"56.979\" end=\"57.623\">當慾</span><span begin=\"57.623\" end=\"58.856\">望在燃</span><span begin=\"58.856\" end=\"1:02.695\">燒</span></p></div><div begin=\"1:05.899\" end=\"1:38.828\" itunes:songPart=\"Chorus\"><p begin=\"1:05.899\" end=\"1:11.925\" itunes:key=\"L7\" ttm:agent=\"v1\"><span begin=\"1:05.899\" end=\"1:06.893\">你愛</span><span begin=\"1:07.285\" end=\"1:09.180\">我還是</span><span begin=\"1:09.476\" end=\"1:11.925\">他</span></p><p begin=\"1:12.656\" end=\"1:16.836\" itunes:key=\"L8\" ttm:agent=\"v1\"><span begin=\"1:12.656\" end=\"1:13.768\">是不是真</span><span begin=\"1:14.046\" end=\"1:14.905\">的他</span><span begin=\"1:14.905\" end=\"1:16.151\">有比我</span><span begin=\"1:16.151\" end=\"1:16.836\">好</span></p><p begin=\"1:17.058\" end=\"1:20.110\" itunes:key=\"L9\" ttm:agent=\"v1\"><span begin=\"1:17.058\" end=\"1:17.707\">你為</span><span begin=\"1:17.707\" end=\"1:18.978\">誰在掙</span><span begin=\"1:18.978\" end=\"1:20.110\">扎</span></p><p begin=\"1:20.766\" end=\"1:26.937\" itunes:key=\"L10\" ttm:agent=\"v1\"><span begin=\"1:20.766\" end=\"1:21.444\">你愛</span><span begin=\"1:21.802\" end=\"1:23.757\">我還是</span><span begin=\"1:24.112\" end=\"1:26.937\">他</span></p><p begin=\"1:27.350\" end=\"1:31.484\" itunes:key=\"L11\" ttm:agent=\"v1\"><span begin=\"1:27.350\" end=\"1:28.488\">就說出你</span><span begin=\"1:28.671\" end=\"1:29.573\">想說</span><span begin=\"1:29.701\" end=\"1:29.998\">的</span><span begin=\"1:29.998\" end=\"1:30.858\">真心</span><span begin=\"1:30.858\" end=\"1:31.484\">話</span></p><p begin=\"1:31.801\" end=\"1:38.828\" itunes:key=\"L12\" ttm:agent=\"v1\"><span begin=\"1:31.801\" end=\"1:32.363\">你到</span><span begin=\"1:32.363\" end=\"1:34.034\">底要跟我</span> <span begin=\"1:34.621\" end=\"1:35.148\">還</span><span begin=\"1:35.148\" end=\"1:35.474\">是</span><span begin=\"1:35.474\" end=\"1:38.828\">他</span></p></div><div begin=\"1:42.966\" end=\"1:47.499\" itunes:songPart=\"Bridge\"><p begin=\"1:42.966\" end=\"1:47.499\" itunes:key=\"L13\" ttm:agent=\"v1\"><span begin=\"1:42.966\" end=\"1:43.412\">愛</span> <span begin=\"1:43.706\" end=\"1:44.426\">愛</span> <span begin=\"1:44.792\" end=\"1:45.645\">愛</span><span begin=\"1:46.047\" end=\"1:47.499\">他</span></p></div><div begin=\"1:51.162\" end=\"2:18.220\" itunes:songPart=\"Verse\"><p begin=\"1:51.162\" end=\"1:56.581\" itunes:key=\"L14\" ttm:agent=\"v1\"><span begin=\"1:51.162\" end=\"1:51.582\">這</span><span begin=\"1:51.582\" end=\"1:52.789\">是不是</span><span begin=\"1:52.789\" end=\"1:53.735\">命運</span><span begin=\"1:53.735\" end=\"1:55.039\">對我的</span><span begin=\"1:55.039\" end=\"1:55.438\">懲</span><span begin=\"1:55.438\" end=\"1:56.581\">罰</span></p><p begin=\"1:57.247\" end=\"2:04.494\" itunes:key=\"L15\" ttm:agent=\"v1\"><span begin=\"1:57.247\" end=\"1:57.866\">愛你</span><span begin=\"1:57.866\" end=\"1:59.484\">也沒辦法</span> <span begin=\"2:00.788\" end=\"2:01.547\">恨你</span><span begin=\"2:01.547\" end=\"2:02.650\">也沒辦</span><span begin=\"2:02.650\" end=\"2:04.494\">法</span></p><p begin=\"2:05.456\" end=\"2:10.856\" itunes:key=\"L16\" ttm:agent=\"v1\"><span begin=\"2:05.456\" end=\"2:06.028\">陷</span><span begin=\"2:06.028\" end=\"2:07.235\">在這個</span><span begin=\"2:07.235\" end=\"2:08.084\">漩渦</span><span begin=\"2:08.091\" end=\"2:09.084\">只想</span><span begin=\"2:09.084\" end=\"2:09.833\">掙脫</span><span begin=\"2:09.833\" end=\"2:10.856\">它</span></p><p begin=\"2:11.868\" end=\"2:18.220\" itunes:key=\"L17\" ttm:agent=\"v1\"><span begin=\"2:11.868\" end=\"2:12.305\">拉</span><span begin=\"2:12.305\" end=\"2:13.488\">住你的</span><span begin=\"2:13.488\" end=\"2:14.373\">手</span> <span begin=\"2:15.067\" end=\"2:15.864\">卻讓我</span><span begin=\"2:15.864\" end=\"2:17.096\">也被拖</span><span begin=\"2:17.096\" end=\"2:18.220\">下</span></p></div><div begin=\"2:19.164\" end=\"2:34.446\" itunes:songPart=\"PreChorus\"><p begin=\"2:19.164\" end=\"2:25.279\" itunes:key=\"L18\" ttm:agent=\"v1\"><span begin=\"2:19.164\" end=\"2:19.624\">你</span><span begin=\"2:19.624\" end=\"2:20.276\">的眉</span><span begin=\"2:20.276\" end=\"2:21.227\">眼說</span> <span begin=\"2:21.858\" end=\"2:22.947\">你不渴</span><span begin=\"2:22.947\" end=\"2:24.239\">望我擁</span><span begin=\"2:24.239\" end=\"2:25.279\">抱</span></p><p begin=\"2:25.899\" end=\"2:34.446\" itunes:key=\"L19\" ttm:agent=\"v1\"><span begin=\"2:25.899\" end=\"2:26.811\">每當</span><span begin=\"2:26.811\" end=\"2:28.386\">愛變成了</span><span begin=\"2:28.386\" end=\"2:29.219\">煎熬</span> <span begin=\"2:29.650\" end=\"2:30.353\">你就</span><span begin=\"2:30.353\" end=\"2:31.617\">開始要</span><span begin=\"2:31.617\" end=\"2:34.446\">逃</span></p></div><div begin=\"2:35.145\" end=\"3:03.633\" itunes:songPart=\"Chorus\"><p begin=\"2:35.145\" end=\"2:40.817\" itunes:key=\"L20\" ttm:agent=\"v1\"><span begin=\"2:35.145\" end=\"2:35.934\">你愛</span><span begin=\"2:36.215\" end=\"2:38.043\">我還是</span><span begin=\"2:38.617\" end=\"2:40.817\">他</span></p><p begin=\"2:41.627\" end=\"2:45.697\" itunes:key=\"L21\" ttm:agent=\"v1\"><span begin=\"2:41.627\" end=\"2:42.316\">是不是</span><span begin=\"2:42.316\" end=\"2:43.078\">我</span><span begin=\"2:43.078\" end=\"2:43.694\">可以</span><span begin=\"2:43.945\" end=\"2:44.619\">做得</span><span begin=\"2:44.853\" end=\"2:45.071\">更</span><span begin=\"2:45.071\" end=\"2:45.697\">好</span></p><p begin=\"2:46.016\" end=\"2:49.140\" itunes:key=\"L22\" ttm:agent=\"v1\"><span begin=\"2:46.016\" end=\"2:46.607\">讓你</span><span begin=\"2:46.722\" end=\"2:47.326\">不再</span><span begin=\"2:47.459\" end=\"2:47.818\">掙</span><span begin=\"2:47.818\" end=\"2:49.140\">扎</span></p><p begin=\"2:49.523\" end=\"2:55.196\" itunes:key=\"L23\" ttm:agent=\"v1\"><span begin=\"2:49.523\" end=\"2:50.441\">你愛</span><span begin=\"2:50.669\" end=\"2:51.510\">我</span><span begin=\"2:51.510\" end=\"2:52.773\">還是</span><span begin=\"2:52.773\" end=\"2:55.196\">他</span></p><p begin=\"2:56.128\" end=\"3:00.178\" itunes:key=\"L24\" ttm:agent=\"v1\"><span begin=\"2:56.128\" end=\"2:57.042\">我寧願聽</span><span begin=\"2:57.437\" end=\"2:58.062\">到殘</span><span begin=\"2:58.307\" end=\"2:59.079\">忍的</span><span begin=\"2:59.079\" end=\"2:59.454\">回</span><span begin=\"2:59.454\" end=\"3:00.178\">答</span></p><p begin=\"3:00.444\" end=\"3:03.633\" itunes:key=\"L25\" ttm:agent=\"v1\"><span begin=\"3:00.444\" end=\"3:01.136\">也不</span><span begin=\"3:01.136\" end=\"3:01.806\">要再</span><span begin=\"3:01.931\" end=\"3:02.246\">被</span><span begin=\"3:02.246\" end=\"3:03.633\">耍</span></p></div><div begin=\"3:04.064\" end=\"3:33.931\" itunes:songPart=\"Chorus\"><p begin=\"3:04.064\" end=\"3:10.018\" itunes:key=\"L26\" ttm:agent=\"v1\"><span begin=\"3:04.064\" end=\"3:04.883\">你愛</span><span begin=\"3:05.164\" end=\"3:06.992\">我還是</span><span begin=\"3:07.308\" end=\"3:10.018\">他</span></p><p begin=\"3:10.590\" end=\"3:14.585\" itunes:key=\"L27\" ttm:agent=\"v1\"><span begin=\"3:10.590\" end=\"3:11.442\">我為你找</span><span begin=\"3:11.720\" end=\"3:12.515\">了一</span><span begin=\"3:12.784\" end=\"3:13.398\">百個</span><span begin=\"3:13.585\" end=\"3:13.979\">理</span><span begin=\"3:13.979\" end=\"3:14.585\">由</span></p><p begin=\"3:14.881\" end=\"3:18.171\" itunes:key=\"L28\" ttm:agent=\"v1\"><span begin=\"3:14.881\" end=\"3:15.481\">我就</span><span begin=\"3:15.517\" end=\"3:16.702\">是那麼</span><span begin=\"3:16.702\" end=\"3:18.171\">傻</span></p><p begin=\"3:18.508\" end=\"3:24.679\" itunes:key=\"L29\" ttm:agent=\"v1\"><span begin=\"3:18.508\" end=\"3:19.426\">你愛</span><span begin=\"3:19.654\" end=\"3:20.495\">我</span><span begin=\"3:20.495\" end=\"3:21.758\">還是</span><span begin=\"3:21.758\" end=\"3:24.679\">他</span></p><p begin=\"3:24.958\" end=\"3:29.093\" itunes:key=\"L30\" ttm:agent=\"v1\"><span begin=\"3:24.958\" end=\"3:25.992\">是否沉默</span><span begin=\"3:26.362\" end=\"3:26.930\">代替</span><span begin=\"3:27.302\" end=\"3:27.934\">你的</span><span begin=\"3:28.078\" end=\"3:28.393\">回</span><span begin=\"3:28.393\" end=\"3:29.093\">答</span></p><p begin=\"3:29.344\" end=\"3:33.931\" itunes:key=\"L31\" ttm:agent=\"v1\"><span begin=\"3:29.344\" end=\"3:29.815\">我應</span><span begin=\"3:29.815\" end=\"3:31.394\">該明白</span><span begin=\"3:31.394\" end=\"3:33.931\">吧</span></p></div><div begin=\"4:07.399\" end=\"4:37.944\" itunes:songPart=\"Chorus\"><p begin=\"4:07.399\" end=\"4:11.959\" itunes:key=\"L32\" ttm:agent=\"v1\"><span begin=\"4:07.399\" end=\"4:08.204\">愛</span><span begin=\"4:08.204\" end=\"4:09.062\">我</span><span begin=\"4:09.062\" end=\"4:09.919\">還是</span><span begin=\"4:10.390\" end=\"4:11.959\">他</span></p><p begin=\"4:13.715\" end=\"4:17.686\" itunes:key=\"L33\" ttm:agent=\"v1\"><span begin=\"4:13.715\" end=\"4:14.399\">你都已</span><span begin=\"4:14.399\" end=\"4:15.067\">看</span><span begin=\"4:15.067\" end=\"4:15.677\">不到</span><span begin=\"4:15.863\" end=\"4:16.617\">我們</span><span begin=\"4:16.973\" end=\"4:17.142\">的</span><span begin=\"4:17.142\" end=\"4:17.686\">好</span></p><p begin=\"4:18.130\" end=\"4:20.727\" itunes:key=\"L34\" ttm:agent=\"v1\"><span begin=\"4:18.130\" end=\"4:18.251\">我</span><span begin=\"4:18.251\" end=\"4:18.737\">還</span><span begin=\"4:18.737\" end=\"4:18.908\">為</span><span begin=\"4:18.908\" end=\"4:19.489\">誰</span><span begin=\"4:19.489\" end=\"4:19.857\">牽</span><span begin=\"4:19.857\" end=\"4:20.727\">掛</span></p><p begin=\"4:21.544\" end=\"4:27.732\" itunes:key=\"L35\" ttm:agent=\"v1\"><span begin=\"4:21.544\" end=\"4:22.086\">你愛</span><span begin=\"4:22.279\" end=\"4:23.430\">我</span><span begin=\"4:23.430\" end=\"4:24.162\">還是</span><span begin=\"4:24.714\" end=\"4:27.732\">他</span></p><p begin=\"4:27.943\" end=\"4:32.076\" itunes:key=\"L36\" ttm:agent=\"v1\"><span begin=\"4:27.943\" end=\"4:28.501\">是否</span><span begin=\"4:28.501\" end=\"4:29.082\">沉默</span><span begin=\"4:29.420\" end=\"4:30.048\">就是</span><span begin=\"4:30.259\" end=\"4:30.895\">你的</span><span begin=\"4:31.120\" end=\"4:31.409\">回</span><span begin=\"4:31.409\" end=\"4:32.076\">答</span></p><p begin=\"4:32.323\" end=\"4:37.944\" itunes:key=\"L37\" ttm:agent=\"v1\"><span begin=\"4:32.323\" end=\"4:32.900\">我們</span><span begin=\"4:32.979\" end=\"4:33.749\">都別</span><span begin=\"4:33.921\" end=\"4:34.340\">掙</span><span begin=\"4:34.340\" end=\"4:37.944\">扎</span></p></div><div begin=\"4:38.805\" end=\"4:45.522\" itunes:songPart=\"Outro\"><p begin=\"4:38.805\" end=\"4:45.522\" itunes:key=\"L38\" ttm:agent=\"v1\"><span begin=\"4:38.805\" end=\"4:39.260\">去</span><span begin=\"4:39.260\" end=\"4:40.312\">愛</span><span begin=\"4:40.312\" end=\"4:45.522\">他</span></p></div></body></tt>";
//        String s = LrcUtils.convertTtmlToLrc(ttml2,"das","231123312","3312");
//        System.out.println(s);

//        try {
//            JSONObject jsonObject = JSONObject.parseObject(AppleCookie);
//            String mediausertoken = jsonObject.getString("media-user-token");
//            String itua = jsonObject.getString("itua");
//            String token = getBearerToken();
////            isTokenValidByUserProfile(token,mediausertoken);
//            makeApiRequest( token,mediausertoken,itua);
//            System.out.println("获取到的 Bearer Token: " + token);
//
//            // 使用获取到的 token 发起 API 请求
//            // fetcher.makeApiRequest(token);
//        } catch (IOException e) {
//            System.err.println("获取 Bearer Token 失败: " + e.getMessage());
//            e.printStackTrace();
//        }
    }


















    public  static String downloadRequestParam(String qq,String musicKey,String loginType ,String filename,String songmid,String songtype) {
//        "QIMEI36": "%s",
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
                          %s
                        ],
                        "guid": "%s",
                        "songmid": [
                          "%s"
                        ],
                        "songtype": [
                          %s
                        ]
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
                songtype
        );
        return format;
    }

//    public static void main(String[] args) {
//
//
//        String input = "RELWORD=星辰大海\r\nSNUM=31774\r\nRNUM=1000\r\nTYPE=0";
//        Pattern pattern = Pattern.compile("RELWORD=([^\\r\\n]*)");
//        Matcher matcher = pattern.matcher(input);
//
//        if (matcher.find()) {
//            String result = matcher.group(1); // 获取第一个捕获组的内容
//            System.out.println(result); // 输出: 星辰大海
//        }
//
//
//
////        String s = String.valueOf(sigHash("MFzb5OFrs0WQflcecG6ILLASd0*UgmQbNAHMTLdWvO4_", 5381));
////        System.out.println(s);
////        System.out.println("1980093307".equals(s));
////
////
////        try {
////            // 1. 获取登录二维码
////            QQMusicQr qr = getQQLoginQr();
////            System.out.println("二维码获取成功，Base64长度: " + getQQMusicQrBase64(qr).length());
////            //转BASE64
////            String qqMusicQrBase64 = getQQMusicQrBase64(qr);
////            System.out.println(qqMusicQrBase64);
////
////            // 2. 轮询检查二维码状态
////            QQMusicQrEventResult status = checkQQQr(qr);
////            while (status.getQrCodeLoginEvents() != QRCodeLoginEvents.DONE) {
////                System.out.println("当前状态: " + status.getQrCodeLoginEvents());
////                Thread.sleep(2000); // 2秒轮询一次
////                status = checkQQQr(qr);
////            }
////
////            // 3. 获取授权码
////            QQMusicQrEventResult authResult = getAuthorizeByQQMusicQrEventResult(status);
////            if (authResult.getQrCodeLoginEvents() == QRCodeLoginEvents.SUCCESS) {
////                System.out.println("登录成功! 授权码: " + authResult.getCode());
////            } else {
////                System.out.println("登录失败: " + authResult.getQrCodeLoginEvents());
////            }
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
//    }


@Autowired
private QQHander qqHander;
    @Autowired
    private ScanQQVIPLikeMusicTask scanQQVIPLikeMusicTask;
    @Test
    public void test()  {
//        scanQQVIPLikeMusicTask.syncalbu();

    }




    //    计算qq的hash值
    private static long sigHash(String qrsig) {
        long hash = 0;
        for (char c : qrsig.toCharArray()) {
            hash = (hash << 5) + hash + c;
        }
        return hash & 0x7FFFFFFF;
    }
    private static long sigHash(String input, long seed) {
        long hash = seed;
        for (char c : input.toCharArray()) {
            hash = (hash << 5) + hash + c;
        }
        return hash & 0x7FFFFFFF;
    }



    /**
     * 获取登录二维码
     * @return
     */
    public static QQMusicQr  getQQLoginQr(){
        //生成随机小数不能大于1
        double random = new Random().nextDouble();

        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host("ssl.ptlogin2.qq.com")
                .addPathSegment("ptqrshow")
                .addQueryParameter("appid", "716027609")
                .addQueryParameter("e", "2")
                .addQueryParameter("l", "M")
                .addQueryParameter("s", "3")
                .addQueryParameter("d", "72")
                .addQueryParameter("v", "4")
                .addQueryParameter("t", random+"")
                .addQueryParameter("daid", "383")
                .addQueryParameter("pt_3rd_aid", "100497308")
                .build();

        QQSession session = QQSession.getCurrentSession();
        Map<String, String> printcookies = session.getCookies();
       //打印
        System.out.println("获取二维码请求-COOKIES:"+JSONObject.toJSONString(printcookies));


        HttpRequest request  = session.buildRequest(url.toString()).GET().build();

        String qrsig = null;
        HttpResponse<byte[]> response=null;

        try {
            response = session.getClient().send(request, HttpResponse.BodyHandlers.ofByteArray());
            System.out.println("获取二维码返回-HEADERS:"+JSONObject.toJSONString(response.headers().map()));
            session.updateCookies(response);
            HttpHeaders headers = response.headers();
            if (headers.map().containsKey("Set-Cookie")) {
                List<String> cookies = headers.map().get("Set-Cookie");
                for (String cookie : cookies) {
                    qrsig = cookie.split(";")[0].split("=")[1];
                    break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


//
//        HttpRequest get = HttpUtil.createGet(url.toString(), false);
//        get.setFollowRedirects(false);
//        HashMap<String, String> headers = new HashMap<>();
//        headers.put("Referer","https://xui.ptlogin2.qq.com/");
//        get.addHeaders(headers);
//        HttpResponse execute = get.execute();
//        Map<String, List<String>> headers1 = execute.headers();
//        String qrsig = null;
//
//        if (headers1.containsKey("Set-Cookie")) {
//            List<String> cookies = headers1.get("Set-Cookie");
//            for (String cookie : cookies) {
//                qrsig = cookie.split(";")[0].split("=")[1];
//            }
//        }
//
//        if (qrsig == null) {
//            return null;
//        }
//        byte[] qrData = null;
//        qrData = execute.bodyBytes();
//
//        return new QQMusicQr(qrData, LoginType.QQ, "image/png", qrsig,0);




//
//
//        OkHttpUtils request  = OkHttpUtils.builder().url(url.toString()).addHeader("Referer", "https://xui.ptlogin2.qq.com/");
//        Response response = request.get().syncReturnResponse();
//        if(response!=null){
//            if (!response.isSuccessful()) {
//                return null;
//            }
//            String qrsig = null;
//            for (String cookie : response.headers("Set-Cookie")) {
//                if (cookie.startsWith("qrsig=")) {
//                    qrsig = cookie.split(";")[0].split("=")[1];
//                    break;
//                }
//            }
            if (qrsig == null) {
                return null;
            }
            byte[] qrData = response.body();

            return new QQMusicQr(qrData, LoginType.QQ, "image/png", qrsig,0);
        }


    //检测QQ二维码状态
    public static QQMusicQrEventResult checkQQQr(QQMusicQr qqMusicQr){
        QQMusicQrEventResult qqMusicQrEventResult = new QQMusicQrEventResult();
        qqMusicQrEventResult.setQqMusicQr(qqMusicQr);
        Integer retryCount = qqMusicQr.getRetryCount();
        //超过100次就停止监听
        if (retryCount > 100) {
            qqMusicQrEventResult.setQrCodeLoginEvents(QRCodeLoginEvents.STOP);
            return qqMusicQrEventResult;
        }
        String qrsig = qqMusicQr.getIdentifier();

        if (qrsig == null || qrsig.isEmpty()) {
            qqMusicQrEventResult.setQrCodeLoginEvents(QRCodeLoginEvents.STOP);
            return qqMusicQrEventResult;
        }
        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host("ssl.ptlogin2.qq.com")
                .addPathSegment("ptqrlogin")
                .addQueryParameter("u1", "https://graph.qq.com/oauth2.0/login_jump")
                .addQueryParameter("ptqrtoken", String.valueOf(sigHash(qrsig)))
                .addQueryParameter("ptredirect", "0")
                .addQueryParameter("h", "1")
                .addQueryParameter("t", "1")
                .addQueryParameter("g", "1")
                .addQueryParameter("from_ui", "1")
                .addQueryParameter("ptlang", "2052")
                .addQueryParameter("action", "0-0-" + System.currentTimeMillis())
                .addQueryParameter("js_ver", "20102616")
                .addQueryParameter("js_type", "1")
                .addQueryParameter("pt_uistyle", "40")
                .addQueryParameter("aid", "716027609")
                .addQueryParameter("daid", "383")
                .addQueryParameter("pt_3rd_aid", "100497308")
                .addQueryParameter("has_onekey", "1")
                .build();
        QQSession session = QQSession.getCurrentSession();
        Map<String, String> printcookies = session.getCookies();
        //打印
        System.out.println("检测二维码请求-COOKIES:"+JSONObject.toJSONString(printcookies));
        HttpRequest request = session.buildRequest(url.toString()).GET().build();

        HttpResponse<String> response=null;

        try {
            response = session.getClient().send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("检测二维码返回-HEADERS:"+JSONObject.toJSONString(response.headers().map()));
            session.updateCookies(response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        String responseBody = response.body();
        Matcher matcher = Pattern.compile("ptuiCB\\((.*?)\\)").matcher(responseBody);

        if (!matcher.find()) {
            qqMusicQrEventResult.setQrCodeLoginEvents(QRCodeLoginEvents.OTHER);
            return qqMusicQrEventResult;

        }
        Integer code = null;
        try {
            String[] data = matcher.group(1).replace("'", "").split(",");
            code = Integer.parseInt(data[0]);
            qqMusicQrEventResult.setUrl(data[2]);

        } catch (NumberFormatException e) {
            qqMusicQrEventResult.setQrCodeLoginEvents(QRCodeLoginEvents.OTHER);
            return qqMusicQrEventResult;
        }
        QRCodeLoginEvents byValue = QRCodeLoginEvents.getByValue(code);
        String sigx = extractValue(responseBody, "&ptsigx=(.+?)&s_url");
        String uin = extractValue(responseBody, "&uin=(.+?)&service");
        qqMusicQrEventResult.setQrCodeLoginEvents(byValue);
        qqMusicQrEventResult.setSigx(sigx);
        qqMusicQrEventResult.setUin(uin);
        return qqMusicQrEventResult;
    }


    //检测微信二维码状态
        private static QQMusicQrEventResult checkWXQR(QQMusicQr qqMusicQr) throws Exception {
           CloseableHttpClient httpClient = HttpClients.createDefault();

            QQMusicQrEventResult qqMusicQrEventResult = new QQMusicQrEventResult();
            qqMusicQrEventResult.setQqMusicQr(qqMusicQr);
            Integer retryCount = qqMusicQr.getRetryCount();
            //超过100次就停止监听1
            if (retryCount > 100) {
                qqMusicQrEventResult.setQrCodeLoginEvents(QRCodeLoginEvents.STOP);
                return qqMusicQrEventResult;
            }
            String qrsig = qqMusicQr.getIdentifier();

            if (qrsig == null || qrsig.isEmpty()) {
                qqMusicQrEventResult.setQrCodeLoginEvents(QRCodeLoginEvents.STOP);
                return qqMusicQrEventResult;
            }

            String uuid = qqMusicQr.getIdentifier();
            int unixTimeStamp = (int) (System.currentTimeMillis() / 1000);
            String url = "https://lp.open.weixin.qq.com/connect/l/qrconnect?uuid=" + uuid + "&_=" + unixTimeStamp;
            HttpGet request = new HttpGet(url);request.setHeader("Referer", "https://open.weixin.qq.com/");


        try (CloseableHttpResponse response = httpClient.execute(request)) {
            String responseText = EntityUtils.toString(response.getEntity());
            Pattern pattern = Pattern.compile("window\\.wx_errcode=(\\d+);window\\.wx_code='([^']*)'");
            Matcher matcher = pattern.matcher(responseText);

            if (!matcher.find()) {
                throw new Exception("获取二维码状态失败");
            }

            String wxErrcode = matcher.group(1);
            if (!wxErrcode.matches("\\d+")) {
                qqMusicQrEventResult.setQrCodeLoginEvents(QRCodeLoginEvents.NOTFOUND);
               return qqMusicQrEventResult;
            }

            QRCodeLoginEvents event = QRCodeLoginEvents.getByValue(Integer.parseInt(wxErrcode));
            qqMusicQrEventResult.setQrCodeLoginEvents(event);
            if (event == QRCodeLoginEvents.DONE) {
                String wxCode = matcher.group(2);
                if (wxCode == null || wxCode.isEmpty()) {
                    throw new Exception("获取code失败");
                }
                qqMusicQrEventResult.setCode(wxCode);
//                authorizeWXQR(wxCode);
            }
            return qqMusicQrEventResult;
        }
    }
    private static void authorizeWXQR(String code) throws Exception {
        String qqwxLoginParam = getQQWXLoginParam(code);
        HttpPost request = new HttpPost("https://u.y.qq.com/cgi-bin/musicu.fcg");
        request.setHeader("Content-Type", "application/json");

        Response referer = OkHttpUtils.builder()
                .url("https://u.y.qq.com/cgi-bin/musicu.fcg")
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Referer", "https://y.qq.com/")
                .addHeader("User-Agent", "QQ%E9%9F%B3%E4%B9%90/73222 CFNetwork/1406.0.3 Darwin/22.4.0")
                .post(true, qqwxLoginParam)
                .syncReturnResponse();
        System.out.println(referer.body());

    }
//    public static QQMusicApi.Credential fromCookies(Map<String, Object> data) {
//        QQMusicApi.Credential credential = new QQMusicApi.Credential();
//        if (data != null) {
//            credential.setMusicid(getStringValue(data, "musicid"));
//            credential.setMusickey(getStringValue(data, "musickey"));
//            credential.setOpenid(getStringValue(data, "openid"));
//            credential.setUnionid(getStringValue(data, "unionid"));
//            credential.setEncryptUin(getStringValue(data, "encrypt_uin"));
//            credential.setLoginType(getIntValue(data, "login_type"));
//            credential.setRefreshKey(getStringValue(data, "refresh_key"));
//            credential.setRefreshToken(getStringValue(data, "refresh_token"));
//        }
//        return credential;
//    }
    // 提取特定值的正则匹配方法
    private static String extractValue(String text, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
    public static  QQMusicQr  getWechatLoginQr(){
        HttpUrl uuidUrl = new HttpUrl.Builder()
                .scheme("https")
                .host("open.weixin.qq.com")
                .addPathSegment("connect")
                .addPathSegment("qrconnect")
                .addQueryParameter("appid", "wx48db31d50e334801")
                .addQueryParameter("redirect_uri", "https://y.qq.com/portal/wx_redirect.html?login_type=2&surl=https://y.qq.com/")
                .addQueryParameter("response_type", "code")
                .addQueryParameter("scope", "snsapi_login")
                .addQueryParameter("state", "STATE")
                .addQueryParameter("href", "https://y.qq.com/mediastyle/music_v17/src/css/popup_wechat.css#wechat_redirect")
                .build();

        OkHttpUtils request  = OkHttpUtils.builder().url(uuidUrl.toString());
        Response uuidResponse = request.get().syncReturnResponse();
        if (!uuidResponse.isSuccessful()) {
        }
        String responseBody = null;
        try {
            responseBody = uuidResponse.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Pattern uuidPattern = Pattern.compile("uuid=(.+?)\"");
        Matcher uuidMatcher = uuidPattern.matcher(responseBody);
        if (!uuidMatcher.find()) {
        }

        String uuid = uuidMatcher.group(1);

        // Step 2: Build the QR Code URL
        HttpUrl qrCodeUrl = new HttpUrl.Builder()
                .scheme("https")
                .host("open.weixin.qq.com")
                .addPathSegment("connect")
                .addPathSegment("qrcode")
                .addPathSegment(uuid)
                .build();
        OkHttpUtils qrCodeRequest = OkHttpUtils.builder().url(qrCodeUrl.toString()).addHeader("Referer", "https://open.weixin.qq.com/connect/qrconnect");
        Response qrCodeResponse = qrCodeRequest.get().syncReturnResponse();
        if (!qrCodeResponse.isSuccessful()) {
        }
        byte[] qrData = null;
        try {
            qrData = qrCodeResponse.body().bytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new QQMusicQr(qrData, LoginType.WECHAT, "image/jpeg", uuid,0);
    }

//    将QQMusicQr转为base64图片并带上mimeType

    public static  String  getQQMusicQrBase64(QQMusicQr qqMusicQr){
        if(qqMusicQr==null){
            return null;
        }
        return  Base64.encode(qqMusicQr.getData());

    }



//@Test
//    void testkg() throws IOException {
//        String keyword = "陈奕迅";
//        int page = 1;
//        int pageSize = 20;
//        String timeStemp = System.currentTimeMillis() + "";
//        ArrayList<String> strings = new ArrayList<>();
//        strings.add("bitrate=0");
//        strings.add("clienttime=" + timeStemp);
//        strings.add("clientver=2000");
//        strings.add("dfid=-");
//        strings.add("inputtype=0");
//        strings.add("iscorrection=1");
//        strings.add("isfuzzy=0");
//        strings.add("keyword=" + keyword);
//        strings.add("mid=" + timeStemp);
//        strings.add("page=" + page);
//        strings.add("pagesize=" + pageSize);
//        strings.add("platform=WebFilter");
//        strings.add("privilege_filter=0");
//        strings.add("srcappid=2919");
//        strings.add("tag=em");
//        strings.add("userid=-1");
//        strings.add("uuid=" + timeStemp);
//        strings.sort(String::compareTo);
//        StringBuilder md5Builder = new StringBuilder();
//        md5Builder.append("NVPh5oo715z5DIWAeQlhMDsWXXQV4hwt");
//        for (String s : strings) {
//            md5Builder.append(s);
//        }
//        md5Builder.append("NVPh5oo715z5DIWAeQlhMDsWXXQV4hwt");
//        StringBuilder stringBuilder = new StringBuilder();
//        for (String s : strings) {
//            stringBuilder.append(s).append("&");
//        }
//
//        String s = DigestUtil.md5Hex(md5Builder.toString());
//        stringBuilder.append("signature=" + s);
//
//        String s1 = DownloadUtils.getHttp().sync("https://complexsearch.kugou.com/v2/search/song?" + stringBuilder.toString()).get().getBody().toByteString().utf8();
//
//        System.out.println(s1);
//    }


//    /**
//     * AES 加密
//     * @param data 需要加密的数据
//     * @param opt 包含key和iv的选项
//     * @return 加密后的字符串或包含加密字符串和临时密钥的Map
//     * @throws Exception 加密过程中可能抛出的异常
//     */
//    public static Map<String, String> cryptoAesEncrypt(String data, Map<String, String> opt) throws Exception {
//        byte[] buffer;
//        if (data == null) {
//            throw new IllegalArgumentException("Data cannot be null");
//        }
//        buffer = data.getBytes(StandardCharsets.UTF_8);
//
//        String key;
//        byte[] iv;
//        String tempKey = "";
//
//        if (opt != null && opt.containsKey("key") && opt.containsKey("iv")) {
//            key = opt.get("key");
//            iv = opt.get("iv").getBytes(StandardCharsets.UTF_8);
//        } else {
//            tempKey = opt != null && opt.containsKey("key") ? opt.get("key") : generateRandomString(16).toLowerCase();
//            key = DigestUtil.md5Hex(tempKey).substring(0, 32);
//            iv = key.substring(key.length() - 16).getBytes(StandardCharsets.UTF_8);
//        }
//
//        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
//        IvParameterSpec ivSpec = new IvParameterSpec(iv);
//
//        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
//        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
//
//        byte[] encrypted = cipher.doFinal(buffer);
//        String encryptedHex = bytesToHex(encrypted);
//        Map<String, String> result = new HashMap<>();
//
//        if (opt != null && opt.containsKey("key")) {
//            result.put("str", encryptedHex);
//        } else {
//            result.put("str", encryptedHex);
//            result.put("key", tempKey);
//        }
//        return result;
//
//    }
//    private static String bytesToHex(byte[] bytes) {
//        StringBuilder hexString = new StringBuilder();
//        for (byte b : bytes) {
//            String hex = Integer.toHexString(0xff & b);
//            if (hex.length() == 1) hexString.append('0');
//            hexString.append(hex);
//        }
//        return hexString.toString();
//    }
//    @Test
//    void trstkglogin() throws Exception {
//
//        String key = "";
//        String timeStemp = System.currentTimeMillis() + "";
//        String userName = "123";
//        String password = "123";
//
//        HashMap<String, String> dataMap  = new HashMap<>();
//        dataMap.put("plat", "1");
//        dataMap.put("support_multi", "1");
//        dataMap.put("clienttime_ms", timeStemp);
//        dataMap.put("t1", "0");
//        dataMap.put("t2", "0");
//        dataMap.put("t3", "MCwwLDAsMCwwLDAsMCwwLDA");
//        dataMap.put("username", userName);
//
//
//        HashMap<String, String> map = new HashMap<>();
//        map.put("pwd", password);
//        map.put("code", "");
//        map.put("clienttime_ms", timeStemp);
//        String data = JSONObject.toJSONString(map);
//
//        Map<String, String> o = CryptoUtil.cryptoAesEncrypt(data, null);
//        String encryptedData = o.get("str");
//        dataMap.put("params", encryptedData);
//        HashMap<String, String> cryptoRSAEncryptMap = new HashMap<>();
//        cryptoRSAEncryptMap.put("clienttime_ms", timeStemp);
//        cryptoRSAEncryptMap.put("key", o.get("key"));
//
//        String s = CryptoUtil.cryptoRSAEncrypt(cryptoRSAEncryptMap, null);
//        dataMap.put("params", encryptedData);
//        dataMap.put("pk", s);
//
//        System.out.println(s);
//
//        String baseyrl = "http://login.user.kugou.com/v9/login_by_pwd";
//        String s1 = DownloadUtils.getHttp().sync(baseyrl).bodyType(OkHttps.JSON)
//                .addHeader("x-router", "login.user.kugou.com")
//                .setBodyPara(dataMap)
//                .post().getBody().toByteString().utf8();
//        System.out.println(s1);
//
//
////        byte[] buffer = data.getBytes(StandardCharsets.UTF_8);
////        AES aes = new AES(Mode.CBC, Padding.PKCS5Padding, key.getBytes(), iv);
////        String encryptedData = aes.decryptStr(buffer);
////        System.out.println(encryptedData);
//
//
//    }

//    private static String generateRandomString(int length) {
//        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
//        Random random = new Random();
//        StringBuilder sb = new StringBuilder(length);
//        for (int i = 0; i < length; i++) {
//            int index = random.nextInt(characters.length());
//            sb.append(characters.charAt(index));
//        }
//        return sb.toString();
//    }

//    @Autowired
//    private QQConfig qqConfig;

//    @Test
//    void contextLoads()  {
//
//        String artistInfoUrl = qqConfig.getArtistInfoUrl();
//        String artistInfoReferer = qqConfig.getArtistInfoReferer();
//        String s = artistInfoUrl.replaceAll("#\\{mid}", "0025NhlN2yWrP4");
//        Mapper mapper1 = DownloadUtils.getHttp().sync(s).bodyType(OkHttps.XML).addHeader("Referer", artistInfoReferer).get().getBody().toMapper();
//        System.out.println(mapper1);
//
//    }
//    @Test
//    public void downloadUrl(){
//        String downloadurl = "http://nmobi.kuwo.cn/mobi.s?f=kuwo&q=";
//        String s = "user=e3cc098fd4c59ce2&android_id=e3cc098fd4c59ce2&prod=kwplayer_ar_9.3.1.3&corp=kuwo&newver=2&vipver=9.3.1.3&source=kwplayer_ar_9.3.1.3_qq.apk&p2p=1&notrace=0&type=convert_url2&br=#{brvalue}&format=flac|mp3|aac&sig=0&rid=#{musicId}&priority=bitrate&loginUid=435947810&network=WIFI&loginSid=1694167478&mode=download&uid=658048466";
//        try {
//            s = s.replaceAll("#\\{musicId}","184274130").replaceAll("#\\{brvalue}","2000");
//            byte[] bytes = KuwoDES.encrypt2(s.getBytes("UTF-8"), s.length(), KuwoDES.SECRET_KEY, KuwoDES.SECRET_KEY_LENG);
//            char[] encode = Base64Coder.encode(bytes);
//            String out =  new String(encode);
//            downloadurl =  downloadurl+out;
//        } catch (UnsupportedEncodingException e) {
////            log.error("获取下载链接失败：{}",e.getMessage());
//            return ;
//        }
//        String s1 = DownloadUtils.getHttp().sync(downloadurl).get().getBody().toByteString().utf8();
//        System.out.println(s1);
////        downloadurl= s1.split("\n")[2].split("=")[1].split("\r")[0];
//    }



    @Test
    public void contextLoads() throws IOException, NoSuchMethodException {

//
//
//        String url = "https://m.lanzouj.com/i6h9c1lbimdc";
//        String downloadUrl = getDownloadUrl(url);
//
//
//        File file = new File("D:\\temp\\sq\\download\\ccc.flac");
////        boolean download = DownloadUtils.download(url, file);
//        HashMap<String, String> stringStringHashMap = new HashMap<>();
//        stringStringHashMap.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
//
////        java.util.function.Consumer<Process> onProcess,Consumer<File> onSuccess,Consumer< Download.Failure> onFailure,Consumer<Download.Status> onComplete
//
//        DownloadUtils.download(downloadUrl, file, stringStringHashMap,onProcess->{
//            long doneBytes = onProcess.getDoneBytes();   // 已下载字节数
//            long totalBytes = onProcess.getTotalBytes(); // 总共的字节数
//            double rate = onProcess.getRate();           // 已下载的比例
//            boolean isDone = onProcess.isDone();         // 是否下载完成
//            System.out.println("下载中");
//            System.out.println("已下载字节数："+doneBytes);
//            System.out.println("总共的字节数"+totalBytes);
//            System.out.println("已下载的比例"+rate);
//            System.out.println("是否下载完成"+isDone);
//
//        },onSuccess->{
//            System.out.println("下载成功");
//
//        },onFailure -> {
//            System.out.println("下载失败:");
//            onFailure.getException().printStackTrace();
//
//        },onComplete->{
//            System.out.println("下载失败XXX:");
//            System.out.println(onComplete);
//        });

//        System.out.println(download);
//        String word1 = "不枉此生(电视剧《雪山飞狐》主题曲)";
//        String word2 = "不枉此生";
//        double cilinSimilarityResult = Similarity.cilinSimilarity(word1, word2);
//        double pinyinSimilarityResult = Similarity.pinyinSimilarity(word1, word2);
//        double conceptSimilarityResult = Similarity.conceptSimilarity(word1, word2);
//        double charBasedSimilarityResult = Similarity.charBasedSimilarity(word1, word2);
//
//        System.out.println(word1 + " vs " + word2 + " 词林相似度值：" + cilinSimilarityResult);
//        System.out.println(word1 + " vs " + word2 + " 拼音相似度值：" + pinyinSimilarityResult);
//        System.out.println(word1 + " vs " + word2 + " 概念相似度值：" + conceptSimilarityResult);
//        System.out.println(word1 + " vs " + word2 + " 字面相似度值：" + charBasedSimilarityResult);


//        ScriptEngineManager manager = new ScriptEngineManager();
//        ScriptEngine engine = manager.getEngineByName("JavaScript");
//
//        Invocable inv = (Invocable) engine;
//
//        String javascriptPath = "C:\\Users\\Administrator\\Desktop\\MyFree.js";
//        engine.eval(new FileReader(javascriptPath));
//        System.out.println(inv.invokeFunction("search", "周杰伦"));

//
//        String downloadUrl = "https://lxmusic.ikunshare.com:9763/url/tx/003KtYhg4frNXC/flac";
//        HTTP http = DownloadUtils.getHttp();
//
//        HttpResult post = http.sync(downloadUrl).addHeader("X-Request-Key","ikunsource")
//                .addHeader("Accept","*/*")
//                .addHeader("Accept-Encoding","gzip, deflate, br")
//                .get();
//        System.out.println(post.getBody().toByteString().utf8());


//        MusicEnum.setBASE_URL_163Music("http://cloud-music.pl-fe.cn");
//        NeteaseCloudMusicInfo neteaseCloudMusicInfo = new NeteaseCloudMusicInfo();
//       neteaseCloudMusicInfo.setCookieString("MUSIC_A_T=1699405917736; Max-Age=2147483647; Expires=Mon, 10 Mar 2092 12:22:21 GMT; Path=/weapi/clientlog;;MUSIC_A_T=1699405917736; Max-Age=2147483647; Expires=Mon, 10 Mar 2092 12:22:21 GMT; Path=/neapi/feedback;;MUSIC_A_T=1699405917736; Max-Age=2147483647; Expires=Mon, 10 Mar 2092 12:22:21 GMT; Path=/weapi/feedback;;MUSIC_A_T=1699405917736; Max-Age=2147483647; Expires=Mon, 10 Mar 2092 12:22:21 GMT; Path=/eapi/feedback;;MUSIC_SNS=; Max-Age=0; Expires=Wed, 21 Feb 2024 09:08:14 GMT; Path=/;MUSIC_R_T=0; Max-Age=2147483647; Expires=Mon, 10 Mar 2092 12:22:21 GMT; Path=/weapi/clientlog;;MUSIC_R_T=0; Max-Age=2147483647; Expires=Mon, 10 Mar 2092 12:22:21 GMT; Path=/wapi/clientlog;;NMTID=00OdwRJrfs-IpN9mkV2q8y5pzJqou8AAAGNyuw2TA; Max-Age=315360000; Expires=Sat, 18 Feb 2034 09:08:14 GMT; Path=/;;MUSIC_R_T=0; Max-Age=2147483647; Expires=Mon, 10 Mar 2092 12:22:21 GMT; Path=/eapi/feedback;;MUSIC_A_T=1699405917736; Max-Age=2147483647; Expires=Mon, 10 Mar 2092 12:22:21 GMT; Path=/api/clientlog;;MUSIC_A_T=1699405917736; Max-Age=2147483647; Expires=Mon, 10 Mar 2092 12:22:21 GMT; Path=/wapi/clientlog;;MUSIC_A_T=1699405917736; Max-Age=2147483647; Expires=Mon, 10 Mar 2092 12:22:21 GMT; Path=/openapi/clientlog;;MUSIC_A_T=1699405917736; Max-Age=2147483647; Expires=Mon, 10 Mar 2092 12:22:21 GMT; Path=/api/feedback;;MUSIC_R_T=0; Max-Age=2147483647; Expires=Mon, 10 Mar 2092 12:22:21 GMT; Path=/wapi/feedback;;MUSIC_A=bf8bfeabb1aa84f9c8c3906c04a04fb864322804c83f5d607e91a04eae463c9436bd1a17ec353cf715bc45df4b3a42a3273c7f3b958a6c67993166e004087dd38107ed2866cbf0ed9de062968295a442f4bf066e6fe094b68be4803e9b31b77d807e650dd04abd3fb8130b7ae43fcc5b; Max-Age=2147483647; Expires=Mon, 10 Mar 2092 12:22:21 GMT; Path=/;;MUSIC_R_T=0; Max-Age=2147483647; Expires=Mon, 10 Mar 2092 12:22:21 GMT; Path=/eapi/clientlog;;MUSIC_R_T=0; Max-Age=2147483647; Expires=Mon, 10 Mar 2092 12:22:21 GMT; Path=/weapi/feedback;;__csrf=0e5ba47686a0f3816c74eb6ed2af3c06; Max-Age=1296010; Expires=Thu, 07 Mar 2024 09:08:24 GMT; Path=/;;MUSIC_R_T=0; Max-Age=2147483647; Expires=Mon, 10 Mar 2092 12:22:21 GMT; Path=/neapi/feedback;;MUSIC_R_T=0; Max-Age=2147483647; Expires=Mon, 10 Mar 2092 12:22:21 GMT; Path=/neapi/clientlog;;MUSIC_R_T=0; Max-Age=2147483647; Expires=Mon, 10 Mar 2092 12:22:21 GMT; Path=/api/clientlog;;MUSIC_R_T=0; Max-Age=2147483647; Expires=Mon, 10 Mar 2092 12:22:21 GMT; Path=/openapi/clientlog;;MUSIC_A_T=1699405917736; Max-Age=2147483647; Expires=Mon, 10 Mar 2092 12:22:21 GMT; Path=/neapi/clientlog;;MUSIC_A_T=1699405917736; Max-Age=2147483647; Expires=Mon, 10 Mar 2092 12:22:21 GMT; Path=/wapi/feedback;;MUSIC_A_T=1699405917736; Max-Age=2147483647; Expires=Mon, 10 Mar 2092 12:22:21 GMT; Path=/eapi/clientlog;;MUSIC_R_T=0; Max-Age=2147483647; Expires=Mon, 10 Mar 2092 12:22:21 GMT; Path=/api/feedback;");
//
//
//        final JSONObject parameter = new JSONObject();// 请求参数
//        parameter.put("keywords", "陶喆");
//        parameter.put("limit", "10");
//        parameter.put("type", "10");
//        JSONObject search = neteaseCloudMusicInfo.cloudsearch(parameter);
//        System.out.println(search);

//        parameter.put("ids","109125");
//        JSONObject jsonObject = neteaseCloudMusicInfo.songDetail(parameter);

//        parameter.put("id","109125");
//        JSONObject jsonObject = neteaseCloudMusicInfo.lyric(parameter);

//        parameter.put("id", "3689");
//        JSONObject jsonObject = neteaseCloudMusicInfo.artistDetail(parameter);


//        parameter.put("id", "10820");
//        JSONObject jsonObject = neteaseCloudMusicInfo.album(parameter);


//        parameter.put("id", 3689);
//        parameter.put("limit", 50);
//        parameter.put("offset", 0);
//        JSONObject jsonObject = neteaseCloudMusicInfo.artistAlbum(parameter);

//        parameter.put("id", 3689);
//
//        JSONObject jsonObject = neteaseCloudMusicInfo.artists(parameter);
//
//                System.out.println(jsonObject);

    }
//    @Test
//    public void contextLoads() throws IOException {
//        String t1_MusicID = "0039wALP1ImfSQ";
//        String platform = "qq";
//        String t2 = "SQ";
//        String device = "MI 14 Pro Max";
//        String osVersion = "13" ;
//         String time = DateUtils.getNowDate().getTime()/1000+"";
//        String  lowerCase = DigestUtil.md5Hex("6d849adb2f3e00d413fe48efbb18d9bb" + time + "6562653262383463363633646364306534333668");
//        String   s6 = "{\\\"method\\\":\\\"GetMusicUrl\\\",\\\"platform\\\":\\\"" + platform + "\\\",\\\"t1\\\":\\\"" + t1_MusicID + "\\\",\\\"t2\\\":\\\"" + t2 + "\\\"}";
//        String s7 = "{\\\"uid\\\":\\\"\\\",\\\"token\\\":\\\"\\\",\\\"deviceid\\\":\\\"84ac82836212e869dbeea73f09ebe52b\\\",\\\"appVersion\\\":\\\"4.1.2\\\",\\\"vercode\\\":\\\"4120\\\",\\\"device\\\":\\\"" + device + "\\\",\\\"osVersion\\\":\\\"" + osVersion + "\\\"}";
//        String  s8 = "{\n\t\"text_1\":\t\"" + s6 + "\",\n\t\"text_2\":\t\"" + s7 + "\",\n\t\"sign_1\":\t\"" + lowerCase + "\",\n\t\"time\":\t\"" + time + "\",\n\t\"sign_2\":\t\"" + DigestUtil.md5Hex(
//                s6.replace("\\", "") + s7.replace("\\", "") + lowerCase + time + "NDRjZGIzNzliNzEe") + "\"\n}" ;
//        byte[] utf8Bytes = s8.getBytes(StandardCharsets.UTF_8);
//        String hexString = ByteArrayUtil.toHexString(utf8Bytes);
//        String upperHexString = hexString.toUpperCase();
//        byte[] encodedBytes = upperHexString.getBytes(StandardCharsets.UTF_8);
//        byte[] compress = ZLibUtils.compress(encodedBytes);
//        HTTP http = DownloadUtils.getHttp();
//        SHttpTask sync = http.sync("http://gcsp.kzti.top:1030/client/cgi-bin/api.fcg");
//        sync.setBodyPara(compress);
//        HttpResult post = sync.post();
//        byte[] decompress = ZLibUtils.decompress(post.getBody().toBytes());
//        String s = new String(decompress);
//        System.out.println(s);
//    }

    @Test
    public void downloadLyric(){
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://api.liumingye.cn/m/api/lyric/id/m4eedd78464c21ce789dea6928415b323-fa4273678781b988a016b813970ee9d7-f89edca7c1ee56f620ab70817d12f739/name/Thinking About You - Jay Sean,Hardwell")
                .get()
                .addHeader("accept", "*/*")
                .addHeader("accept-language", "zh-CN,zh;q=0.9")
                .addHeader("origin", "https://tool.liumingye.cn")
                .addHeader("priority", "u=1, i")
                .addHeader("sec-ch-ua", "\"Not/A)Brand\";v=\"8\", \"Chromium\";v=\"126\", \"Google Chrome\";v=\"126\"")
                .addHeader("sec-ch-ua-mobile", "?0")
                .addHeader("sec-ch-ua-platform", "\"Windows\"")
                .addHeader("sec-fetch-dest", "empty")
                .addHeader("sec-fetch-mode", "cors")
                .addHeader("sec-fetch-site", "same-site")
                .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36")
                .build();

        try {
            Response response = client.newCall(request).execute();
            System.out.println(response.body().string());

        } catch (IOException e) {

        }

    }

    String getDownloadUrl(String musicurl){
        //判断是否是蓝奏地址还是其他地址
        if (musicurl.contains("lanzou")){
            //转直连再下载

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://lz.qaiu.top/json/parser?url="+musicurl)
                    .get()
                    .build();

            try {
                Response response = client.newCall(request).execute();
                String string = response.body().string();
                JSONObject jsonObject = JSONObject.parseObject(string);
                Integer code = jsonObject.getInteger("code");
                if (code==200){
                    String url = jsonObject.getString("data");
                    return url;
                }else{
                    return null;
                }
            } catch (IOException e) {
                return null;
            }


        }else{
            //musicId
            return musicurl;
        }

    }



    public static String getQQWXLoginParam(String code){
        String msg = """
                 {
                                                 "comm": {
                                                     "tmeLoginType": "1",
                                                      "tmeAppID": "qqmusic",
                                                      "g_tk": 5381,
                                                      "platform": "yqq",
                                                      "ct": 24,
                                                      "cv": 0
                                                 },
                                                 "req": {
                                                     "module": "music.login.LoginServer",
                                                     "method": "Login",
                                                     "param": {
                                                         "strAppid": "wx48db31d50e334801",
                                                         "code": "%s"
                
                                                     }
                                                 }
                                             }
          """;

        return String.format(msg,code);
    }


}
