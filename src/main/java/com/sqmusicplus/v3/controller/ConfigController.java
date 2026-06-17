package com.sqmusicplus.v3.controller;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.sqmusicplus.v3.base.entity.SqConfig;
import com.sqmusicplus.v3.base.entity.SqSync;
import com.sqmusicplus.v3.base.enums.DbBooleanConvert;
import com.sqmusicplus.v3.base.enums.PlugBrType;
import com.sqmusicplus.v3.base.enums.SetConfigEnum;
import com.sqmusicplus.v3.base.service.SqConfigService;
import com.sqmusicplus.v3.base.service.SqSyncService;
import com.sqmusicplus.v3.config.*;
import com.sqmusicplus.v3.config.exception.SQException;
import com.sqmusicplus.v3.plug.kg.hander.KGHander;
import com.sqmusicplus.v3.plug.netease.hander.NeteaseHander;
import com.sqmusicplus.v3.plug.qq.hander.QQHander;
import com.sqmusicplus.v3.plug.qqvip.QQvipHander;
import com.sqmusicplus.v3.utils.StringUtils;
import com.sqmusicplus.v3.utils.SystemUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
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

    @Autowired
    private SqConfigService configService;
    @Autowired
    private AuthUtil authUtil;
    @Value("${jwt.expiration:604800000}")
    private Long jwtExpirationMs; // JWT 过期时间（毫秒）

    /**
     * 登录
     *
     * @param data
     * @return
     */
    @RequireLogin(value = false)
    @PostMapping("/login")
    public AjaxResult login(@RequestBody HashMap<String, String> data) {
        String username = data.get("username");
        String password = data.get("password");
        String device = data.get("device");
        if (StringUtils.isEmpty(device)) {
            return AjaxResult.error("请填写登录设备类型");
        }
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return AjaxResult.error("登录失败");
        }
        String dbname = SqConfigCache.getSqConfigValue(SetConfigEnum.SYSTEM_LOGIN_ACCOUNT);
        String dbpwd = SqConfigCache.getSqConfigValue(SetConfigEnum.SYSTEM_LOGIN_PASSWORD);
        if (StringUtils.isBlank(dbname)) {
            return AjaxResult.error("请先设置登录用户");
        }
        if (username.equals(dbname) && password.equals(dbpwd)) {
            // 生成 Token
            String token = authUtil.login(9527, username);
            
            // 计算 Token 过期时间（秒），从配置文件读取
            Long tokenTimeout = jwtExpirationMs / 1000; // 毫秒转秒
            
            // 创建登录响应
            LoginResponse loginResponse = new LoginResponse(token, 9527, tokenTimeout, device);
            return AjaxResult.success(loginResponse);
        } else {
            return AjaxResult.error("账号密码错误");
        }
    }

    /**
     * 登出
     *
     * @return
     */
    @PostMapping("/logout")
    public AjaxResult logout(HttpServletRequest request) {
        authUtil.logout();
        return AjaxResult.success();
    }

    /**
     * 用户是否登录
     *
     * @return
     */
    @RequestMapping(value = "isLogin")
    public AjaxResult isLogin(Authentication authentication) {
        boolean isLogin = authUtil.isLogin();
//        boolean isLogin = authentication != null && authentication.isAuthenticated();
        return isLogin ? AjaxResult.success("登录有效", true) : AjaxResult.error("过期", false);
    }

    /**
     * 获取全部设置
     *
     * @return
     */
    @RequireLogin(value = false)
    @GetMapping("/getConfigList")
    public AjaxResult getConfigList() {
        return AjaxResult.success(SqConfigCache.getAllConfig());
    }

    /**
     * 修改设置
     */
    @PostMapping("/updateConfig")
    public AjaxResult updateConfig(@RequestBody SqConfig data) {
        SqConfig sqConfig = SqConfigCache.getSqConfig(data.getConfigKey());
        if (sqConfig.getConfigDisabled() == DbBooleanConvert.YES.getValue().intValue()) {
            return AjaxResult.error("该设置已禁用不允许修改");
        }
        if (sqConfig.getConfigNullCheck() == DbBooleanConvert.YES.getValue().intValue()) {
            if (StringUtils.isBlank(data.getConfigValue())) {
                return AjaxResult.error("该设置不允许为空");
            }
        }
        if (!configCheck(sqConfig, data.getConfigValue())) {
            return AjaxResult.error("设置参数填写异常");

        }
        if (StringUtils.isBlank(data.getConfigValue())) {
            data.setConfigValue("");
        }
        if (sqConfig.getConfigType().equals("boolean")) {
            if (data.getConfigValue().equals(DbBooleanConvert.YES.getBooleanValue().toString())) {
                data.setConfigValue(DbBooleanConvert.YES.getBooleanValue().toString());
            } else {
                data.setConfigValue(DbBooleanConvert.NO.getBooleanValue().toString());
            }
        }
        LambdaUpdateWrapper<SqConfig> sqConfigLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        sqConfigLambdaUpdateWrapper.eq(SqConfig::getConfigKey, data.getConfigKey())
                        .set(SqConfig::getConfigValue, data.getConfigValue());
        configService.update(sqConfigLambdaUpdateWrapper);
        List<SqConfig> list = configService.list();
        SqConfigCache.setSqConfigMap(list);
        //根据key设置一些特殊配置
        specialPlugConfigUpdate(data.getConfigKey(), data.getConfigValue());
        return AjaxResult.success();
    }
    //获取当前网络使用情况

    @RequireLogin(value = false)
    @GetMapping("/getCurrentNetwork")
    public AjaxResult getCurrentNetwork() {
        return AjaxResult.success(SystemUtils.getNetworkSpeedReport());
    }
    /**
     * 获取启动的查询件
     */
    @RequireLogin(value = false)
    @GetMapping("/getOption")
    public AjaxResult getSelectOption() {
        return AjaxResult.success(SqConfigCache.PlugOptions);
    }

    /**
     * 获取版本信息
     *
     * @return
     */
    @GetMapping("/version")
    public AjaxResult getVersion() {
        return AjaxResult.success("成功", version);
    }


    /**
     * 校验设置类型结果是否准确
     *
     * @param sqConfig    设置信息
     * @param configValue 设置的值
     * @return
     */
    private boolean configCheck(SqConfig sqConfig, String configValue) {
        String type = sqConfig.getConfigType();
        switch (type) {
            case "number":
                if (!StringUtils.isNumeric(configValue)) {
                    throw new RuntimeException("请输入数字");
                }
                return true;
            case "input", "path", "password":
                return true;
            case "boolean":
                if (!configValue.equals("true") && !configValue.equals("false")) {
                    throw new RuntimeException("请输入布尔值");
                }
                return true;
            case "select":
                if (!sqConfig.getConfigOptions().contains(configValue)) {
                    throw new RuntimeException("请输入正确的选项");
                }
                String configOptions = sqConfig.getConfigOptions();
                JSONArray objects = JSONArray.parseArray(configOptions);
                for (Object object : objects) {
                    JSONObject jsonObject = (JSONObject) object;
                    if (jsonObject.getString("value").equals(configValue)) {
                        return true;
                    }
                }
                throw new RuntimeException("请输入正确的选项");
            default:
                throw new RuntimeException("请输入正确的类型");
        }

    }


    /**
     * 特殊修改设置与配置
     *
     * @param configKey
     * @param configValue
     */
    private void specialPlugConfigUpdate(String configKey, String configValue) {

        //        酷我
        if (configKey.equals(SetConfigEnum.PLUG_KW_OPEN.getKey())) {
            if (configValue.equals(DbBooleanConvert.YES.getBooleanValue().toString())) {
                HashMap<String, String> kwoption = new HashMap<>();
                kwoption.put("value", "kw");
                kwoption.put("label", "某我(无需登录支持flac)");
                //修改酷狗插打开插件功能
                SqConfigCache.updatePlugOptions(kwoption);
            } else {
                SqConfigCache.removePlugOptions("kw");
            }
        }
        //        qqvip
        if (configKey.equals(SetConfigEnum.PLUG_QQVIP_OPEN.getKey())) {
            if (configValue.equals(DbBooleanConvert.YES.getBooleanValue().toString())) {
                HashMap<String, String> QQVIPoption = new HashMap<>();
                QQVIPoption.put("value", "qqvip");
                QQVIPoption.put("label","鹅厂VIP下载（登录支持flac，自动同步喜欢的去设置开启）");
                //修改酷狗插打开插件功能
                SqConfigCache.updatePlugOptions(QQVIPoption);
                qqvipHander.initPlug();
            } else {
                SqConfigCache.removePlugOptions("qqvip");
            }
        }
// 网易
        if (configKey.equals(SetConfigEnum.PLUG_NETEASE_OPEN.getKey())) {
            if (configValue.equals(DbBooleanConvert.YES.getBooleanValue().toString())) {

                boolean b = neteaseHander.initPlug();
                if (b) {
                    HashMap<String, String> neteaseoption = new HashMap<>();
                    neteaseoption.put("value", "netease");
                    neteaseoption.put("label", "猪厂(无需登录支持flac)");
                    //修改酷狗插打开插件功能
                    SqConfigCache.updatePlugOptions(neteaseoption);
                }else{
                    throw new SQException("访问接口失败,请检查网易云音乐是否正常!");
                }
            } else {
                SqConfigCache.removePlugOptions("netease");
            }
        }

        if (configKey.equals(SetConfigEnum.PLUG_MG_OPEN.getKey())) {
            if (configValue.equals(DbBooleanConvert.YES.getBooleanValue().toString())) {
                HashMap<String, String> kwoption = new HashMap<>();
                kwoption.put("value","mg");
                kwoption.put("label","移动(无需登录支持320Mp3)");
                SqConfigCache.addPlugOptions(kwoption);
            } else {
                SqConfigCache.removePlugOptions("mg");
            }
        }
        //        酷狗
        if (configKey.equals(SetConfigEnum.PLUG_KG_OPEN.getKey())) {
            if (configValue.equals(DbBooleanConvert.YES.getBooleanValue().toString())) {
                HashMap<String, String> KGoption = new HashMap<>();
                KGoption.put("value", "kg");
                KGoption.put("label","某狗-概念版（签到只支持128Mp3）");
                //修改酷狗插打开插件功能
                SqConfigCache.updatePlugOptions(KGoption);
            } else {
                SqConfigCache.removePlugOptions("kg");
            }
        }

        //网易API地址修改
        if (configKey.equals(SetConfigEnum.PLUG_NETEASE_BASEURL.getKey())) {
            if (StringUtils.isNotBlank(configValue)) {
                boolean b = neteaseHander.initPlug();
                if (!b) {
                 throw   new SQException("访问接口失败,请检查网易云音乐是否正常!");
                }

            }
        }

        if (configKey.equals(SetConfigEnum.PLUG_TIDAL_OPEN.getKey())) {
            if (configValue.equals(DbBooleanConvert.YES.getBooleanValue().toString())) {
                HashMap<String, String> TIDALoption = new HashMap<>();
                TIDALoption.put("value", "tidal");
                TIDALoption.put("label","Tidal(无需登录支持真flac无损)");
                //修改酷狗插打开插件功能
                SqConfigCache.updatePlugOptions(TIDALoption);
            } else {
                SqConfigCache.removePlugOptions("tidal");
            }
        }
        //修改密码后退出登录
        if (configKey.equals(SetConfigEnum.SYSTEM_LOGIN_PASSWORD.getKey())||configKey.equals(SetConfigEnum.SYSTEM_LOGIN_ACCOUNT.getKey())) {
            authUtil.kickout(9527);
            authUtil.logout();
        }
    }


    /**
     * 导入V2.x版本歌单配置
     * 上传json文件 文件名称为file
     * 需要校验数据后缀是否是json
     */
    @PostMapping("/importSongList")
    public AjaxResult importSongList(@RequestParam("file") MultipartFile file) {
        String json = null;
        try {
            InputStream inputStream = file.getInputStream();
            json = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            JSONArray array = JSONArray.parseArray(json);


            log.info("=======本次共计导入歌单设置：{}条===========", array.size());
            for (int i = 0; i < array.size(); i++) {

                JSONObject jsonObject = array.getJSONObject(i);
                String importType = jsonObject.getString("importType");
                if (StringUtils.isBlank(importType) || importType.equalsIgnoreCase("playList")) {
                    String songlistname = jsonObject.getString("songlistname");
                    String plugTpye = jsonObject.getString("plugTpye");
                    String songlistid = jsonObject.getString("songlistid");
                    JSONArray jsonArray = jsonObject.getJSONArray("songlistids");
                    //本次歌单已经存在的 歌曲
                    List<SqSync> PlayListData = syncService.list(new LambdaQueryWrapper<SqSync>()
                            .eq(SqSync::getPlayListId, songlistid));
                    Set<String> collect = PlayListData.stream().map(SqSync::getMusicId).collect(Collectors.toSet());


                    log.info("导入识别已下载歌单类型{}名称：{}({})歌曲个数：{}", plugTpye, songlistname, songlistid, jsonArray.size());
                    ArrayList<SqSync> sqSyncs = new ArrayList<>();

                    for (int i1 = 0; i1 < jsonArray.size(); i1++) {
                        String string = jsonArray.getString(i1);
                        // 检查歌曲是否已经存在于数据库中
                        if (collect.contains(string)) {
                            continue;
                        }
                        SqSync sqSync = new SqSync();
                        sqSync.setPlugName(plugTpye);
                        sqSync.setPlayListName(songlistname);
                        sqSync.setPlayListId(songlistid);
                        sqSync.setMusicId(string);
                        sqSyncs.add(sqSync);
                    }
                    log.info("本次共计导入{}首歌曲", sqSyncs.size());
                    // 批量插入歌曲数据，忽略重复冲突
                    saveBatchIgnoreDuplicates(sqSyncs);

                } else if (importType.equalsIgnoreCase("likeAlubids")) {
                    //找到专辑信息
                    JSONArray jsonArray = jsonObject.getJSONArray("alubids");
                    Set<String> collect1 = jsonArray.stream().map(Object::toString).collect(Collectors.toSet());
                    List<SqSync> PlayListData = syncService.list(new LambdaQueryWrapper<SqSync>()
                            .in(SqSync::getAlbumId, collect1));
                    Set<String> collect = PlayListData.stream().map(SqSync::getMusicId).collect(Collectors.toSet());
                    ArrayList<SqSync> sqSyncsAlub = new ArrayList<>();

                    for (int i1 = 0; i1 < jsonArray.size(); i1++) {
                        String albumId = jsonArray.getString(i1);
                        // 检查专辑是否已经存在于数据库中
                        if (collect.contains(albumId)) {
                            continue;
                        }
                        SqSync sqSync = new SqSync();
                        sqSync.setPlugName(PlugBrType.QQVIP_Flac_2000.getPlugName());
                        sqSync.setAlbumId(albumId);
                        sqSyncsAlub.add(sqSync);
                    }
                    log.info("本次共计导入{}张专辑", sqSyncsAlub.size());

                    // 扫量插入专辑数据，忽略重复冲突
                    saveBatchIgnoreDuplicates(sqSyncsAlub);

                } else if (importType.equalsIgnoreCase("likeArtistids")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("artistids");
                    //找到专辑信息
                    Set<String> collect1 = jsonArray.stream().map(Object::toString).collect(Collectors.toSet());
                    List<SqSync> PlayListData = syncService.list(new LambdaQueryWrapper<SqSync>()
                            .in(SqSync::getArtistId, collect1));
                    Set<String> collect = PlayListData.stream().map(SqSync::getMusicId).collect(Collectors.toSet());
                    ArrayList<SqSync> sqSyncsArt = new ArrayList<>();
                    for (int i1 = 0; i1 < jsonArray.size(); i1++) {
                        String artistId = jsonArray.getString(i1);
                        // 检查艺术家是否已经存在于数据库中
                        if (collect.contains(artistId)) {
                            continue;
                        }
                        SqSync sqSync = new SqSync();
                        sqSync.setPlugName(PlugBrType.QQVIP_Flac_2000.getPlugName());
                        sqSync.setArtistId(artistId);
                        sqSyncsArt.add(sqSync);
                    }
                    log.info("本次共计导入{}位歌手", sqSyncsArt.size());

                    // 批量插入艺术家数据，忽略重复冲突
                    saveBatchIgnoreDuplicates(sqSyncsArt);

                }
            }

        } catch (IOException e) {
            log.error("导入歌单失败", e);
            return AjaxResult.error("导入失败: " + e.getMessage());
        }
        log.info("==========本次歌单导入完成==============");
        return AjaxResult.success("导入成功");
    }

    /**
     * 批量保存数据，忽略重复冲突
     *
     * @param syncs 数据列表
     */
    private void saveBatchIgnoreDuplicates(List<SqSync> syncs) {
        if (syncs.isEmpty()) {
            return;
        }

        // 每300条分割插入数据库
        try {
            syncService.saveBatch(syncs);
        } catch (Exception e) {
            // 如果整批保存失败，则逐条保存，忽略任何错误
            log.warn("批量保存失败，尝试逐条保存: {}", e.getMessage());
            int successCount = 0;
            for (SqSync sync : syncs) {
                try {
                    syncService.save(sync);
                } catch (Exception innerE) {
                    // 忽略所有类型的错误，继续处理下一条记录
                    log.debug("忽略保存错误: {}", innerE.getMessage());
                }
                successCount++;
            }
            log.info("批量保存中成功插入 {} 条记录", successCount);
        }
    }

}
