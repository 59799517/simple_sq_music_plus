package com.sqmusicplus.v3.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.hutool.core.codec.Base64;
import com.sqmusicplus.v3.base.entity.SqConfig;
import com.sqmusicplus.v3.base.enums.DbBooleanConvert;
import com.sqmusicplus.v3.base.enums.SetConfigEnum;
import com.sqmusicplus.v3.config.AjaxResult;
import com.sqmusicplus.v3.config.SqConfigCache;
import com.sqmusicplus.v3.plug.kg.hander.KGHander;
import com.sqmusicplus.v3.plug.netease.hander.NeteaseHander;
import com.sqmusicplus.v3.plug.qq.entity.QQMusicCookieInfo;
import com.sqmusicplus.v3.plug.qq.entity.QQMusicQr;
import com.sqmusicplus.v3.plug.qq.hander.QQHander;
import com.sqmusicplus.v3.plug.qqvip.QQvipHander;
import com.sqmusicplus.v3.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Classname PlugController
 * @Description 插件控制
 * @Version 1.0.0
 * @Date 2025/8/1 17:21
 * @Created by SQ
 */
@Slf4j
@RestController
@RequestMapping("/api/plug")
public class PlugController {
    @Autowired
    private NeteaseHander neteaseHander;
    @Autowired
    private QQvipHander qqvipHander;
    @Autowired
    private QQHander qqHander;
    @Autowired
    private KGHander kGHander;

    //plug签到与登录
    /**
     * 获取酷狗登录二维码
     * @return
     */
    @SaCheckLogin
    @GetMapping("/kg/getQrImage")
    public AjaxResult getKgQrimage(){
        SqConfig kgopenconfigKey = SqConfigCache.getSqConfig(SetConfigEnum.PLUG_KG_OPEN.getKey());
        if (kgopenconfigKey.getConfigValue().equals(DbBooleanConvert.YES.getBooleanValue().toString())){
            return AjaxResult.success("成功", kGHander.getQrImage());
        }
        return AjaxResult.error("酷狗插件未开启");
    }


    /**
     * 获取酷狗扫码信息
     */
    @SaCheckLogin
    @GetMapping("/kg/checkQrCodeStatus")
    public AjaxResult  getKGcheckQrCodeStatus(){
        SqConfig kgopenconfigKey = SqConfigCache.getSqConfig(SetConfigEnum.PLUG_KG_OPEN.getKey());
        if (kgopenconfigKey.getConfigValue().equals(DbBooleanConvert.YES.getBooleanValue().toString())){
            return AjaxResult.success("成功", kGHander.checkQrCodeStatus());
        }
        return AjaxResult.error("酷狗插件未开启");
    }
    /**
     * 微信扫酷狗登录二维码生成
     */
    @SaCheckLogin
    @GetMapping("/kg/getWxQrImage")
    public AjaxResult  getWxQrImage(){
        SqConfig kgopenconfigKey = SqConfigCache.getSqConfig(SetConfigEnum.PLUG_KG_OPEN.getKey());
        if (kgopenconfigKey.getConfigValue().equals(DbBooleanConvert.YES.getBooleanValue().toString())){
            return AjaxResult.success("成功", kGHander.getWxQrImage());
        }
        return AjaxResult.error("酷狗插件未开启");
    }


    /**
     * 微信扫酷狗登录二维码检测
     */
    @SaCheckLogin
    @GetMapping("/kg/checkWxQrCodeStatus")
    public AjaxResult  checkWxQrCodeStatus(){
        SqConfig kgopenconfigKey = SqConfigCache.getSqConfig(SetConfigEnum.PLUG_KG_OPEN.getKey());
        if (kgopenconfigKey.getConfigValue().equals(DbBooleanConvert.YES.getBooleanValue().toString())){
            return AjaxResult.success("成功", kGHander.checkWxQrCodeStatus());
        }
        return AjaxResult.error("酷狗插件未开启");
    }

    /**
     * 刷新酷狗token
     */
    @SaCheckLogin
    @GetMapping("/kg/refreshToken")
    public AjaxResult refreshKGToken(){
        SqConfig kgopenconfigKey = SqConfigCache.getSqConfig(SetConfigEnum.PLUG_KG_OPEN.getKey());
        if (kgopenconfigKey.getConfigValue().equals(DbBooleanConvert.YES.getBooleanValue().toString())){
            boolean b = kGHander.refreshToken();
            return  b?AjaxResult.success("成功"):AjaxResult.error("失败");
        }
        return AjaxResult.error("酷狗插件未开启");
    }

    /**
     * 酷狗签到
     * @return
     */
    @SaCheckLogin
    @GetMapping("/kg/signIn")
    public AjaxResult  kgSignIn(){
        SqConfig kgopenconfigKey = SqConfigCache.getSqConfig(SetConfigEnum.PLUG_KG_OPEN.getKey());
        if (kgopenconfigKey.getConfigValue().equals(DbBooleanConvert.YES.getBooleanValue().toString())){
            boolean b = kGHander.signIn();
            return  b?AjaxResult.success("成功"):AjaxResult.error("失败");
        }
        return AjaxResult.error("酷狗插件未开启");
    }

    /**
     * qqVIP登录相关（QQ二维码）
     * @return
     */
    @SaCheckLogin
    @GetMapping("/qqvip/getQrImage")
    public AjaxResult  getQQvipQrimage(){
            QQMusicQr qqLoginQr = qqHander.getQQLoginQr();
            String image ="data:"+qqLoginQr.getMimeType()+";base64,"+ Base64.encode(qqLoginQr.getData());
            return AjaxResult.success("成功", image);
    }

    /**
     * 微信登录二维码
     * @return
     */
    @SaCheckLogin
    @GetMapping("/qqvip/getWechatQrImage")
    public AjaxResult  getWechatQrImage(){
        QQMusicQr qqLoginQr = qqHander.getWechatLoginQr();
        String image ="data:"+qqLoginQr.getMimeType()+";base64,"+ Base64.encode(qqLoginQr.getData());
        return AjaxResult.success("成功", image);
    }

    /**
     * QQ二维码检测
     */
    @SaCheckLogin
    @GetMapping("/qqvip/checkQrCodeStatus")
    public AjaxResult  getQQvipcheckQrCodeStatus() {
        SqConfig sqConfig = SqConfigCache.getSqConfig(SetConfigEnum.PLUG_QQVIP_OPEN.getKey());
        if (sqConfig.getConfigValue().equals(DbBooleanConvert.YES.getBooleanValue().toString())){
            SqConfig sqConfigCookie = SqConfigCache.getSqConfig(SetConfigEnum.PLUG_QQVIP_COOKIE.getKey());
            if (sqConfigCookie != null && StringUtils.isNotBlank(sqConfigCookie.getConfigValue())) {
                QQMusicCookieInfo qqMusicCookieInfo = qqHander.refreshToken();
                if (qqMusicCookieInfo != null) {
                    return AjaxResult.success("成功", "扫码成功");
                }else{
                    return AjaxResult.error("失败请稍等或者重新扫码");
                }
            } else {
                return AjaxResult.error("失败请稍等或者重新扫码");
            }
        }
        return AjaxResult.error("qqvip插件未开启");
    }
    /**
     * 手动刷新QQ登录cookie
     */
    @SaCheckLogin
    @GetMapping("/qqvip/refreshQQvipCookie")
    public AjaxResult refreshQQvipCookies(){
            QQMusicCookieInfo qqMusicCookieInfo = qqHander.refreshToken();
            if (qqMusicCookieInfo != null) {
                return AjaxResult.success("成功", "刷新成功");
            }
        return AjaxResult.error("登录信息失效请重新扫码登录！");
    }

}
