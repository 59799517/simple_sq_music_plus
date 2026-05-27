package com.sqmusicplus.v3.task;


import com.sqmusicplus.v3.base.enums.SetConfigEnum;
import com.sqmusicplus.v3.config.SqConfigCache;
import com.sqmusicplus.v3.plug.qq.hander.QQHander;
import com.sqmusicplus.v3.utils.StringUtils;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Classname QQSigninTask
 * @Description qq签到任务
 * @Version 1.0.0
 * @Date 2025/5/28 09:18
 * @Created by SQ
 */
@Slf4j
@Component
public class QQSigninTask {

    @Autowired
    private QQHander qqHander;

    @PostConstruct
    public void init() {
        log.info("QQSigninTask QQ签到任务已注册, cron=1 0 0,4,8,12,16,20,23 * * ? (每4小时)");
    }

    @Scheduled(cron="1 0 0,4,8,12,16,20,23 * * ? ")
    public void excute() {
        try {
            String qqopenconfigKey = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_QQVIP_OPEN);
            String qqautologin = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_QQVIP_AUTO_REFRESH_LOGIN);
            if (StringUtils.isNotBlank(qqopenconfigKey)&& Boolean.parseBoolean(qqopenconfigKey) && Boolean.parseBoolean(qqautologin)) {
                log.info("QQ音乐插件已开启，开始自动刷新登录信息");
                qqHander.refreshToken();
            }
        } catch (Throwable t) {
            log.error("QQ音乐插件自动刷新登录信息异常！", t);
        }


    }
}
