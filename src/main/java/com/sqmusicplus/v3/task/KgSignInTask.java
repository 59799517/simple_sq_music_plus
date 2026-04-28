package com.sqmusicplus.v3.task;

import com.sqmusicplus.v3.base.enums.SetConfigEnum;
import com.sqmusicplus.v3.config.SqConfigCache;
import com.sqmusicplus.v3.plug.kg.hander.KGHander;
import com.sqmusicplus.v3.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Classname KgSignInTask
 * @Description 酷狗自动签到
 * @Version 1.0.0
 * @Date 2025/2/12 16:37
 * @Created by SQ
 */
@Slf4j
@Component
public class KgSignInTask {


    @Autowired
    private KGHander kgHander;
    @Scheduled(cron="1 0 3,6,9,12,15,18,21 * * ? ")
    public void excute() {
        String kgopenconfigKey = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_KG_OPEN);
        try {
            if (StringUtils.isBlank(kgopenconfigKey)|| !Boolean.parseBoolean(kgopenconfigKey)) {
                return;
            }
            String sqConfigValue = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_KG_SIGN_OPEN);
            if (StringUtils.isBlank(sqConfigValue)|| !Boolean.parseBoolean(sqConfigValue)) {
                return;
            }

            boolean login = kgHander.isLogin();
            if (!login){
                log.error("酷狗未开启插件！");
                return;
            }
            kgHander.signIn();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("酷狗签到失败！",e);
        }
    }
}
