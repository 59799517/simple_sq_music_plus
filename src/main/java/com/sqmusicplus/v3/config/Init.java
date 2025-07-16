package com.sqmusicplus.v3.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.sqmusicplus.download.DownloadExcute;
import com.sqmusicplus.plug.kg.hander.KGHander;
import com.sqmusicplus.plug.netease.hander.NeteaseHander;
import com.sqmusicplus.plug.qq.hander.QQHander;
import com.sqmusicplus.plug.qqvip.QQvipHander;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * @Classname Init
 * @Description 初始化
 * @Version 1.0.0
 * @Date 2022/8/3 16:52
 * @Created by SQ
 */
@Slf4j
@Configuration
@Order(100)

public class Init implements ApplicationRunner {
    @Autowired
    private SqConfigService configService;
    @Autowired
    private QQvipHander qqvipHander;
    @Autowired
    private QQHander qqHander;
    @Value("${server.port}")
    private String port;
    @Value("${version}")
    private String version;
    @Autowired
    private DownloadExcute downloadExcute;
    @Autowired
    private NeteaseHander neteaseHander;
    @Autowired
    private KGHander kgHander;



    @Override
    public void run(ApplicationArguments args) throws Exception {
        //查看本次 系统变量带的值是否有

        String kgurl = System.getProperty("init.kgurl");

        if (StringUtils.isNotEmpty(kgurl)){
            //设置参数
            configService.update(new UpdateWrapper<SqConfig>().eq("config_key", "plug.kg.baseurl").set("config_value", kgurl));
            log.info("设置KG地址为：{}", kgurl);
        }


        SqConfig init_download = configService.getOne(new QueryWrapper<SqConfig>().eq("config_key", "music.init.download"));
        log.info("启动完毕：http://localhost:{}", port);

        if (Boolean.parseBoolean(init_download.getConfigValue())){
            downloadExcute.getDownloadInfo();
        }

        try {
            neteaseHander.initPlug();
        } catch (Exception e) {
            log.error("网易未开启插件！:{}", e.getMessage());
        }
        log.info("当前服务版本->{}", version);
        qqvipHander.initPlug();
       SqConfig qqopenconfigKey = configService.getOne(new QueryWrapper<SqConfig>().eq("config_key", "plug.qqvip.open"));

       if (qqopenconfigKey.getConfigValue() != null &&Boolean.parseBoolean(qqopenconfigKey.getConfigValue())){
           Boolean loginStatus = qqHander.getLoginStatus();
           if (!loginStatus){
               log.error("登录已失效需要重新登录！");
           }else {
               qqHander.refreshToken();
               log.info("求缓存cookie正常使用！");
           }
       }

//        if (qqconfigKey != null && configKey != null && qqopenconfigKey != null && qqopenconfigKey.getConfigValue() != null &&Boolean.parseBoolean(qqopenconfigKey.getConfigValue())&& configKey.getConfigValue() != null && qqconfigKey.getConfigValue() != null) {
//            try {
//                FreeCookieUtil.refreshCookies(qqconfigKey.getConfigValue(), configKey.getConfigValue());
//            } catch (Exception e) {
//                configService.update(new UpdateWrapper<SqConfig>().eq("config_key", "plug.qqvip.open").set("config_value", "false"));
//                log.error("获取QQvip失败请检查ip和qq是否准确已自动关闭该插件");
//            }
//        }
        boolean login = false;
        try {
            login = kgHander.isLogin();
        } catch (Exception e) {
            log.error("酷狗未开启插件！:{}", e.getMessage());
        }
        if (!login){
            log.error("酷狗未开启插件！");
        }else {
            log.info("酷狗插件已开启！");
        }


    }



}
