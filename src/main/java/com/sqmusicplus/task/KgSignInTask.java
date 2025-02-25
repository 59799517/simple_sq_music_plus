package com.sqmusicplus.task;

import com.sqmusicplus.base.service.SqConfigService;
import com.sqmusicplus.plug.kg.hander.KGHander;
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
    @Scheduled(cron="1 * 0,3,6,9,12,15,18,21 * * ? ")
    public void excuteSignIn() {
        boolean login = kgHander.isLogin();
        if (!login){
            log.error("酷狗未开启插件！");
            return;
        }
        kgHander.signIn();


    }
}
