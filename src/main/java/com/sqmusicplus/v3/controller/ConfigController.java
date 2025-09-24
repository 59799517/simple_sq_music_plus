package com.sqmusicplus.v3.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.SaLoginConfig;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.ListUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sqmusicplus.v3.base.entity.DownloadInfo;
import com.sqmusicplus.v3.base.entity.SqConfig;
import com.sqmusicplus.v3.base.entity.SqSync;
import com.sqmusicplus.v3.base.enums.DbBooleanConvert;
import com.sqmusicplus.v3.base.enums.PlugBrType;
import com.sqmusicplus.v3.base.enums.SetConfigEnum;
import com.sqmusicplus.v3.base.service.SqSyncService;
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
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Classname ConfigController
 * @Description 设置控制器
 * @Version 1.0.0
 * @Date 2025/8/1 11:12
 * @Created by SQ
 */
@Slf4j
@RestController
@RequestMapping("/api/config")
public class ConfigController {
    @Autowired
    private NeteaseHander neteaseHander;
    @Autowired
    private QQvipHander qqvipHander;
    @Autowired
    private QQHander qqHander;
    @Autowired
    private KGHander kGHander;
    @Value("${version}")
    private String version;
    @Autowired
    private SqSyncService syncService;

    /**
     * 登录
     * @param data
     * @return
     */
    @PostMapping("/login")
    public AjaxResult login(@RequestBody HashMap<String,String> data )  {
        String username = data.get("username");
        String password = data.get("password");
        String device = data.get("device");
        if (StringUtils.isEmpty(device)){
            return AjaxResult.error("请填写登录设备类型");
        }
        if (StringUtils.isEmpty(username)||StringUtils.isEmpty(password)){
            return AjaxResult.error("登录失败");
        }
        String dbname = SqConfigCache.getSqConfigValue(SetConfigEnum.SYSTEM_LOGIN_ACCOUNT);
        String dbpwd = SqConfigCache.getSqConfigValue(SetConfigEnum.SYSTEM_LOGIN_PASSWORD);
        if (StringUtils.isBlank(dbname)){
            return AjaxResult.error("请先设置登录用户");
        }
        if (username.equals(dbname) && password.equals(dbpwd)) {
            StpUtil.login(9527, SaLoginConfig.setExtra("device", device).setIsLastingCookie(false));
            SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
            return AjaxResult.success(tokenInfo);
        }else{
            return   AjaxResult.error("账号密码错误");
        }
    }

    /**
     * jwt模式无需退出前段清楚token即可
     * @return
     */
    @SaCheckLogin
    @PostMapping("/logout")
    public AjaxResult logout() {
//        StpUtil.logout(9527,device);
        return AjaxResult.success();
    }

    /**
     * 用户是否登上
     * @return
     */
    @RequestMapping(value = "isLogin")
    public AjaxResult isLogin() {
        return  StpUtil.isLogin()?AjaxResult.success("登录有效",true):AjaxResult.error("过期",false);
    }
    /**
     * 获取全部设置
     * @return
     */
    @SaCheckLogin
    @GetMapping("/getConfigList")
    public AjaxResult getConfigList() {
        return AjaxResult.success(SqConfigCache.getAllConfig());
    }
    /**
     * 修改设置
     */
    @SaCheckLogin
    @PostMapping("/updateConfig")
    public AjaxResult updateConfig(@RequestBody SqConfig data) {
        SqConfig sqConfig = SqConfigCache.getSqConfig(data.getConfigKey());
        if (sqConfig.getConfigDisabled()== DbBooleanConvert.YES.getValue().intValue()){
            return AjaxResult.error("该设置已禁用不允许修改");
        }
        if (sqConfig.getConfigNullCheck()== DbBooleanConvert.YES.getValue().intValue()){
            if (StringUtils.isBlank(data.getConfigValue())){
                return AjaxResult.error("该设置不允许为空");
            }
        }
        if(!configCheck(sqConfig,data.getConfigValue())){
            return AjaxResult.error("设置参数填写异常");

        }
        if (StringUtils.isBlank(data.getConfigValue())){
            data.setConfigValue("");
        }
        if (sqConfig.getConfigType().equals("boolean")){
            if (data.getConfigValue().equals(DbBooleanConvert.YES.getBooleanValue().toString())) {
                data.setConfigValue(DbBooleanConvert.YES.getBooleanValue().toString());
            }else{
                data.setConfigValue(DbBooleanConvert.NO.getBooleanValue().toString());
            }
        }
        SqConfigCache.updateConfigToDb(data.getConfigKey(),data.getConfigValue());
        //根据key设置一些特殊配置
        specialPlugConfigUpdate(data.getConfigKey(), data.getConfigValue());
        return AjaxResult.success();
    }
    /**
     * 获取启动的查询件
     */
    @SaCheckLogin
    @GetMapping("/getOption")
    public AjaxResult getSelectOption() {
        return AjaxResult.success(SqConfigCache.PlugOptions);
    }

    /**
     * 获取版本信息
     * @return
     */
    @GetMapping("/version")
    public AjaxResult getVersion(){
        return AjaxResult.success("成功", version);
    }



    /**
     * 校验设置类型结果是否准确
     * @param sqConfig 设置信息
     * @param configValue 设置的值
     * @return
     */
    private boolean configCheck(SqConfig sqConfig, String configValue){
        String type = sqConfig.getConfigType();
        switch (type){
            case "number":
                if (!StringUtils.isNumeric(configValue)){
                    throw new RuntimeException("请输入数字");
                }
                return true;
            case "input", "path":
                return true;
            case "boolean":
                if (!configValue.equals("true")&&!configValue.equals("false")){
                    throw new RuntimeException("请输入布尔值");
                }
                return true;
            default:
                throw new RuntimeException("请输入正确的类型");
        }

    }


    /**
     *  特殊修改设置与配置
     * @param configKey
     * @param configValue
     */
    private void specialPlugConfigUpdate(String configKey, String configValue){
//        酷狗
        if (configKey.equals(SetConfigEnum.PLUG_KG_OPEN.getKey())){
            if (configValue.equals(DbBooleanConvert.YES.getValue().toString())){
                HashMap<String, String> KGoption = new HashMap<>();
                KGoption.put("value","kg");
                KGoption.put("label","某狗-概念版");
                //修改酷狗插打开插件功能
                SqConfigCache.updatePlugOptions(KGoption);
            }else{
                SqConfigCache.removePlugOptions("kg");
            }
        }
        //        酷我
        if (configKey.equals(SetConfigEnum.PLUG_KW_OPEN.getKey())){
            if (configValue.equals(DbBooleanConvert.YES.getValue().toString())){
                HashMap<String, String> kwoption = new HashMap<>();
                kwoption.put("value","kw");
                kwoption.put("label","某我");
                //修改酷狗插打开插件功能
                SqConfigCache.updatePlugOptions(kwoption);
            }else{
                SqConfigCache.removePlugOptions("kw");
            }
        }
        //        酷我
        if (configKey.equals(SetConfigEnum.PLUG_QQVIP_OPEN.getKey())){
            if (configValue.equals(DbBooleanConvert.YES.getValue().toString())){
                HashMap<String, String> QQVIPoption = new HashMap<>();
                QQVIPoption.put("value","qqvip");
                QQVIPoption.put("label","鹅厂VIP下载（自动同步喜欢的去设置开启）");
                //修改酷狗插打开插件功能
                SqConfigCache.updatePlugOptions(QQVIPoption);
                qqvipHander.initPlug();
            }else{
                SqConfigCache.removePlugOptions("qqvip");
            }
        }
// 网易
        if (configKey.equals(SetConfigEnum.PLUG_NETEASE_OPEN.getKey())){
            if (configValue.equals(DbBooleanConvert.YES.getValue().toString())){
                HashMap<String, String> neteaseoption = new HashMap<>();
                neteaseoption.put("value","netease");
                neteaseoption.put("label","猪厂");
                //修改酷狗插打开插件功能
                SqConfigCache.updatePlugOptions(neteaseoption);
                neteaseHander.initPlug();
            }else{
                SqConfigCache.removePlugOptions("netease");
            }
        }
        //网易API地址修改
        if (configKey.equals(SetConfigEnum.PLUG_NETEASE_BASEURL.getKey())){
            if (StringUtils.isNotBlank(configValue)){
                neteaseHander.initPlug();
            }
        }
    }


    /**
     * 导入V2.x版本歌单配置
     * 上传json文件 文件名称为file
     *需要校验数据后缀是否是json
     *
     *
     */
    @PostMapping("/importSongList")
    public AjaxResult importSongList(@RequestParam("file") MultipartFile file) {
//       获取文件中的内容
        String json = null;
        try {
            InputStream inputStream = file.getInputStream();
            json = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            JSONArray array = JSONArray.parseArray(json);
            ArrayList<SqSync> sqSyncs = new ArrayList<>();
            ArrayList<SqSync> sqSyncsAlub = new ArrayList<>();
            ArrayList<SqSync> sqSyncsArt = new ArrayList<>();



            log.info("=======本次共计导入歌单设置：{}条===========", array.size());
            for (int i = 0; i < array.size(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                String importType = jsonObject.getString("importType");
                if (StringUtils.isBlank(importType)||importType.equalsIgnoreCase("playList")){

                    String songlistname = jsonObject.getString("songlistname");
                    String plugTpye = jsonObject.getString("plugTpye");
                    String songlistid = jsonObject.getString("songlistid");
                    JSONArray jsonArray = jsonObject.getJSONArray("songlistids");
                    log.info("导入识别已下载歌单类型{}名称：{}({})歌曲个数：{}",plugTpye,songlistname,songlistid, jsonArray.size());
                    //已经存在的歌曲信息
                    List<String> dbMusicIds = new ArrayList<>();
                    try {
                        //找到每个歌单已经存在的信息
                        LambdaQueryWrapper<SqSync> sqSyncsLambdaQueryWrapper = new LambdaQueryWrapper<>();
                        sqSyncsLambdaQueryWrapper.eq(SqSync::getPlayListName, songlistname);
                        sqSyncsLambdaQueryWrapper.eq(SqSync::getPlugName, plugTpye);
                        List<SqSync> sqSyncs1 = syncService.list(sqSyncsLambdaQueryWrapper);
                        //已经存在的歌曲信息
                        dbMusicIds = sqSyncs1.stream().map(SqSync::getMusicId).toList();
                    } catch (Exception e) {

                    }
                    for (int i1 = 0; i1 < jsonArray.size(); i1++) {
                        String string = jsonArray.getString(i1);
                        if (dbMusicIds.contains(string)){
                            continue;
                        }
                        SqSync sqSync = new SqSync();
                        sqSync.setPlugName(plugTpye);
                        sqSync.setPlayListName(songlistname);
                        sqSync.setPlayListId(songlistid);
                        sqSync.setMusicId(string);
                        sqSyncs.add(sqSync);
                    }
                    log.info("本次共计导入{}首歌曲",sqSyncs.size());
                    //sqSyncs 每300条分割插入数据库
                    for (List<SqSync> syncs : ListUtil.partition(sqSyncs, 300)) {
                        syncService.saveBatch(syncs);
                    }
                }else if(importType.equalsIgnoreCase("likeAlubids")){
                    JSONArray jsonArray = jsonObject.getJSONArray("alubids");
                    for (int i1 = 0; i1 < jsonArray.size(); i1++) {
                        SqSync sqSync = new SqSync();
                        sqSync.setPlugName( PlugBrType.QQVIP_Flac_2000.getPlugName());
                        sqSync.setAlbumId(jsonArray.getString(i1));
                        sqSyncsAlub.add(sqSync);
                    }
                    log.info("本次共计导入{}张专辑",sqSyncsAlub.size());
                    for (List<SqSync> syncs : ListUtil.partition(sqSyncsAlub, 300)) {
                        syncService.saveBatch(syncs);
                    }


                }else if(importType.equalsIgnoreCase("likeArtistids")){
                    JSONArray jsonArray = jsonObject.getJSONArray("artistids");
                    for (int i1 = 0; i1 < jsonArray.size(); i1++) {
                        SqSync sqSync = new SqSync();
                        sqSync.setPlugName(PlugBrType.QQVIP_Flac_2000.getPlugName());
                        sqSync.setArtistId(jsonArray.getString(i1));
                        sqSyncsArt.add(sqSync);
                    }
                    log.info("本次共计导入{}位歌手",sqSyncsArt.size());
                    for (List<SqSync> syncs : ListUtil.partition(sqSyncsArt, 300)) {
                        syncService.saveBatch(syncs);
                    }
                }

                }


        } catch (IOException e) {
//            throw new RuntimeException(e);
            return AjaxResult.error("导入失败");
        }
        log.info("==========本次歌单导入完成==============");
        return AjaxResult.success("导入成功");

    }




}
