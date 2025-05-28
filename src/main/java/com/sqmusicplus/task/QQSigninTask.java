package com.sqmusicplus.task;

import com.sqmusicplus.plug.kg.hander.KGHander;
import com.sqmusicplus.plug.qq.hander.QQHander;
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
    @Scheduled(cron="1 0 0,4,8,12,16,20,23 * * ? ")
    public void excuteSignIn() {
        qqHander.refreshToken();
    }
}
