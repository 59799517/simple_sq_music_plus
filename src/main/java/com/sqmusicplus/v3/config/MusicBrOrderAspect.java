package com.sqmusicplus.v3.config;

import com.sqmusicplus.v3.plug.entity.*;
import com.sqmusicplus.v3.base.enums.PlugBrType;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.List;

/**
 * @Classname MusicBrOrder
 * @Description TODO
 * @Version 1.0.0
 * @Date 2025/9/17 17:39
 * @Created by Administrator
 */
@Aspect
@Component
public class MusicBrOrderAspect {

    //    所有的SearchHanderAbstract具体实现类的querySongByName方法
    @Pointcut("execution(* com.sqmusicplus.v3.plug.base.hander.*.querySongByName(..))")
    public void orderPointCut(){}

    // 修改方法的返回值
    @Around("orderPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        // 执行原方法
        Object result = point.proceed();
        // 如果返回值是PlugSearchResult类型，则可以进行修改
        if (result instanceof PlugSearchResult) {
            PlugSearchResult<PlugSearchMusicResult> searchResult = (PlugSearchResult) result;
            // 可以在这里对返回值进行修改
            List<PlugSearchMusicResult> records = searchResult.getRecords();
            if (records != null) {
                for (PlugSearchMusicResult record : records) {
                    List<PlugBrType> brTypes = record.getBrTypes();
                    if (brTypes != null && !brTypes.isEmpty()) {
                        // 按bit从大到小排序
                        brTypes.sort(Comparator.comparing(PlugBrType::getBit).reversed());
                    }
                }
            }
        }
        
        return result;
    }

    @Pointcut("execution(* com.sqmusicplus.v3.plug.base.hander.*.queryAlbumById(..))")
    public void orderAlubPointCut(){}

    @Around("orderAlubPointCut()")
    public Object aroundAlub(ProceedingJoinPoint point) throws Throwable {
        // 执行原方法
        Object result = point.proceed();
        // 如果返回值是Album类型，则可以进行修改
        if (result instanceof Album) {
            Album album = (Album) result;
            // 可以在这里对返回值进行修改
            List<Music> musics = album.getMusics();
            if (musics != null) {
                for (Music record : musics) {
                    List<PlugBrType> brTypes = record.getBits();
                    if (brTypes != null && !brTypes.isEmpty()) {
                        // 按bit从大到小排序
                        brTypes.sort(Comparator.comparing(PlugBrType::getBit).reversed());
                    }
                }
            }
        }
        return result;
    }
}