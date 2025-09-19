package com.sqmusicplus.v3.config;

import cn.hutool.core.util.ReflectUtil;
import com.sqmusicplus.v3.base.entity.DownloadInfo;
import com.sqmusicplus.v3.plug.base.hander.SearchHander;
import com.sqmusicplus.v3.utils.SpringContextUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @Classname SaveMusicCheckAspect
 * @Description 保存到数据库歌曲切面
 * @Version 1.0.0
 * @Date 2025/7/14 11:57
 * @Created by SQ
 */
@Aspect  // 使用@Aspect注解声明一个切面
@Component
public class SaveMusicCheckAspect {


    //创建切面
    @Pointcut("execution(* com.sqmusicplus.v3.base.service.impl.DownloadInfoServiceImpl.add(..))")
    public void logPointCut() {}

    /**
     * 辅助校验添加到数据库的数据是否合法
     * @param point
     * @return
     * @throws Throwable
     */
    @Before("logPointCut()")
    public void before(JoinPoint point) throws Throwable {
        try {
            Object[] args = point.getArgs();
            if (args!=null&&args.length>0){
                Object arg = args[0];
                if (arg != null){
                    if (arg instanceof DownloadInfo) {
                        DownloadInfo downloadInfo = (DownloadInfo)arg;
                        Object bean = SpringContextUtil.getBean(downloadInfo.getSpringName());
                        if (bean instanceof SearchHander){
                            SearchHander searchHander = (SearchHander) bean;
                            searchHander.downloadInfoToDbCheck(downloadInfo);
                        }else{
                            ReflectUtil.invoke(bean, "downloadInfoToDbCheck", downloadInfo);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }

    }


}
