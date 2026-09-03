package com.sqmusicplus.v3.config;

import com.sqmusicplus.v3.base.entity.SqConfig;
import com.sqmusicplus.v3.base.enums.SetConfigEnum;
import com.sqmusicplus.v3.base.service.SqConfigService;
import com.sqmusicplus.v3.plug.netease.hander.NeteaseHander;
import com.sqmusicplus.v3.plug.qq.hander.QQHander;
import com.sqmusicplus.v3.plug.qqvip.QQvipHander;
import com.sqmusicplus.v3.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

/**
 * @Classname InitV3
 * @Description v3版本初始化
 * @Version 1.0.0
 * @Date 2025/7/15 09:51
 * @Created by SQ
 */
@Slf4j
@Configuration
@Order(101)
public class InitV3  implements ApplicationRunner {

    @Autowired
    private SqConfigService configService;


    @Value("${server.port}")
    private String port;
    @Value("${version}")
    private String version;



    @Autowired
    private NeteaseHander neteaseHander;

    @Autowired
    private QQvipHander qqvipHander;
    @Autowired
    private QQHander qqHander;


    @Override
    public void run(ApplicationArguments args) throws Exception {

        log.info("================服务开始启动====================");
        //加载数据库设置
        List<SqConfig> list = configService.list();
        //加入缓存
        SqConfigCache.setSqConfigMap(list);
        log.info("================缓存设置成功====================");
        log.info("初始化插件");

//        ------------------------酷我-----------------------------

        String kwsqConfigvalue = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_KW_OPEN);
        if (Boolean.valueOf(kwsqConfigvalue)) {
            HashMap<String, String> kwoption = new HashMap<>();
            kwoption.put("value","kw");
            kwoption.put("label","某我");
            kwoption.put("desc","无需登录支持flac");
            SqConfigCache.addPlugOptions(kwoption);
            log.info("酷我插件开启成功！");
        }else{
            log.error("酷我未开启插件！");
        }

        //------------------------网易云音乐-----------------------------
        String  netsqConfigvalue = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_NETEASE_OPEN);
        if (Boolean.valueOf(netsqConfigvalue)) {
            // 检测自建 neteasecloudmusicapi 服务，若可用则将其放到 baseUrl 首位
            String neteaseBuiltinUrl = "http://neteasecloudmusicapi:3000";
//            String neteaseBuiltinUrl = "http://127.0.0.1:3000";
            if (checkServiceAvailable(neteaseBuiltinUrl)) {
                log.info("✓ 检测到自建网易云API服务: {}", neteaseBuiltinUrl);
                String neteaseBaseUrl = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_NETEASE_BASEURL);
                boolean needUpdate = false;
                if (StringUtils.isNotBlank(neteaseBaseUrl)) {
                    // 取出第一个非空地址，判断是否为内置URL
                    String[] urls = neteaseBaseUrl.split(";");
                    String firstUrl = "";
                    for (String url : urls) {
                        if (StringUtils.isNotBlank(url)) {
                            firstUrl = url.trim();
                            break;
                        }
                    }
                    if (!neteaseBuiltinUrl.equals(firstUrl)) {
                        neteaseBaseUrl = neteaseBuiltinUrl + ";" + neteaseBaseUrl;
                        needUpdate = true;
                    }
                } else {
                    neteaseBaseUrl = neteaseBuiltinUrl + ";";
                    needUpdate = true;
                }
                if (needUpdate) {
                    SqConfigCache.updateConfigToDb(SetConfigEnum.PLUG_NETEASE_BASEURL, neteaseBaseUrl);
                    log.info("已将自建网易云API地址添加到baseUrl首位: {}", neteaseBaseUrl);
                } else {
                    log.info("自建网易云API地址已在baseUrl首位，无需更新");
                }
            } else {
                log.error("未检测到自建网易云API服务: {}, 使用配置的公共API", neteaseBuiltinUrl);
            }

            String sqConfigValue = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_NETEASE_COOKIE);
            if (StringUtils.isNotEmpty(sqConfigValue)){
               log.info("发现网易云音cookie使用cookie登陆");
            }else{
                log.info("未发现网易云cookie使用匿名登陆");
            }
            boolean b = neteaseHander.initPlug();
            if ( b){
                HashMap<String, String> neteaseoption = new HashMap<>();
                neteaseoption.put("value","netease");
                neteaseoption.put("label","猪厂");
                neteaseoption.put("desc","无需登录支持flac");
                SqConfigCache.addPlugOptions(neteaseoption);
                log.info("网易云音乐插件开启成功！");
            }else{
                log.error("网易云未开启插件！");
            }

        }else{
            log.error("网易云未开启插件！");
        }


        //------------------------QQvip音乐-----------------------------
        String  qqvipsqConfigvalue = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_QQVIP_OPEN);
        if (Boolean.valueOf(qqvipsqConfigvalue)) {
            HashMap<String, String> QQVIPoption = new HashMap<>();
            QQVIPoption.put("value","qqvip");
            QQVIPoption.put("label","鹅厂VIP下");
            QQVIPoption.put("desc","需要登录，支持flac，自动同步喜欢的去设置开启");

            SqConfigCache.addPlugOptions(QQVIPoption);
            log.info("QQvip插件开启成功！");
            qqvipHander.initPlug();
            Boolean loginStatus = qqHander.getLoginStatus();
            if (!loginStatus){
                log.error("登录已失效需要重新登录！");
            }else {
                qqHander.refreshToken();
                log.info("qq缓存cookie正常使用无需刷新！");
            }

        }else{
            log.error("QQvip未开启插件！");
        }

        //------------------------咪咕音乐----------------------------

        String mgsqConfigvalue = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_MG_OPEN);
        if (Boolean.valueOf(mgsqConfigvalue)) {
            HashMap<String, String> kwoption = new HashMap<>();
            kwoption.put("value","mg");
            kwoption.put("label","移动");
            kwoption.put("desc","无需登录支持320Mp3");
            SqConfigCache.addPlugOptions(kwoption);
            log.info("咪咕（移动）插件开启成功！");
        }else{
            log.error("咪咕（移动）酷我未开启插件！");
        }


        //------------------------酷狗-----------------------------
        String  kgsqConfigvalue = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_KG_OPEN);
        if (Boolean.valueOf(kgsqConfigvalue)) {
            String configValue = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_KG_BASEURL);
// 检测kugoumusicapi服务
            String dockerApiUrl = "http://kugoumusicapi:3000";
//            String dockerApiUrl = "http://192.168.31.70:3000";
            if(StringUtils.isNotBlank(configValue)){
                dockerApiUrl= configValue;
            }
            if (checkServiceAvailable(dockerApiUrl)) {
                log.info("✓ 检测到KuGou服务: {}", dockerApiUrl);
                // 更新缓存
                SqConfigCache.updateConfigToDb(SetConfigEnum.PLUG_KG_BASEURL, dockerApiUrl);
            }else{
                log.error("未检测到KuGouAPI服务，或者服务不可用，请检查！");
            }

            if (StringUtils.isNotEmpty(dockerApiUrl)){
                HashMap<String, String> KGoption = new HashMap<>();
                KGoption.put("value","kg");
                KGoption.put("label","某狗-概念版");
                KGoption.put("desc","需要登录签到，支持128Mp3");
                SqConfigCache.addPlugOptions(KGoption);
                log.info("酷狗插件开启成功！");

            }else{
                log.info("酷狗插件请填写API地址！");
            }
        }else{
            log.error("酷狗未开启插件！");
        }


//        ------------------------Tidal-----------------------------
        String  tidalsqConfigvalue = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_TIDAL_OPEN);
        if (Boolean.valueOf(tidalsqConfigvalue)) {
            HashMap<String, String> Tidaloption = new HashMap<>();
            Tidaloption.put("value","tidal");
            Tidaloption.put("label","Tidal");
            Tidaloption.put("desc","无需登录支持真flac无损-下载错误多试几次");
            SqConfigCache.addPlugOptions(Tidaloption);
            log.info("Tidal插件开启成功！");
        }else {
            log.error("Tidal未开启插件！");
        }




        //------------------------其他-----------------------------
//        HashMap<String, String> QQoption = new HashMap<>();
//        QQoption.put("value","qq");
//        QQoption.put("label","鹅厂(不要太过频繁否则无法下载)");
////        HashMap<String, String> MGoption = new HashMap<>();
//////        MGoption.put("value","mg");
//////        MGoption.put("label","10086(有问题暂停使用)");
//////        MGoption.put("disabled","true");
//        log.info("默认打开QQ插件！");
//        SqConfigCache.addPlugOptions(QQoption);
//        SqConfigCache.addPlugOptions(MGoption);
        log.info("启动完毕：http://localhost:{}", port);
        log.info("当前服务版本->{}", version);
        GlobalStatic.IS_INIT = true;

    }
    
    /**
     * 检查服务是否可用
     */
    private boolean checkServiceAvailable(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(3000);
            connection.setReadTimeout(3000);
            int responseCode = connection.getResponseCode();
            connection.disconnect();
            return responseCode >= 200 && responseCode < 400;
        } catch (IOException e) {
            return false;
        }
    }
}
