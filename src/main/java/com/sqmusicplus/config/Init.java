package com.sqmusicplus.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.sqmusicplus.base.entity.SqConfig;
import com.sqmusicplus.download.DownloadExcute;
import com.sqmusicplus.plug.kg.hander.KGHander;
import com.sqmusicplus.plug.netease.hander.NeteaseHander;
import com.sqmusicplus.plug.qqvip.QQvipHander;
import com.sqmusicplus.base.service.SqConfigService;
import com.sqmusicplus.plug.utils.FreeCookieUtil;
import com.sqmusicplus.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.ArrayList;

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
        String qqurl = System.getProperty("init.qqurl");

        String kgurl = System.getProperty("init.kgurl");

        if (StringUtils.isNotEmpty(qqurl)){
            //设置参数
            configService.update(new UpdateWrapper<SqConfig>().eq("config_key", "plug.qqvip.baseurl").set("config_value", qqurl));
            log.info("设置QQVIP地址为：{}", qqurl);
        }
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
        qqvipHander.initPlug();
        log.info("当前服务版本->{}", version);
       SqConfig qqopenconfigKey = configService.getOne(new QueryWrapper<SqConfig>().eq("config_key", "plug.qqvip.open"));
       SqConfig configKey = configService.getOne(new QueryWrapper<SqConfig>().eq("config_key", "plug.qqvip.baseurl"));
       SqConfig qqconfigKey = configService.getOne(new QueryWrapper<SqConfig>().eq("config_key", "plug.qqvip.qq"));
        if (qqconfigKey != null && configKey != null && qqopenconfigKey != null && qqopenconfigKey.getConfigValue() != null &&Boolean.parseBoolean(qqopenconfigKey.getConfigValue())&& configKey.getConfigValue() != null && qqconfigKey.getConfigValue() != null) {
            try {
                FreeCookieUtil.refreshCookies(qqconfigKey.getConfigValue(), configKey.getConfigValue());
            } catch (Exception e) {
                configService.update(new UpdateWrapper<SqConfig>().eq("config_key", "plug.qqvip.open").set("config_value", "false"));
                log.error("获取QQvip失败请检查ip和qq是否准确已自动关闭该插件");
            }
        }
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
