package com.sqmusicplus.v3.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.sqmusicplus.v3.base.entity.SqConfig;
import com.sqmusicplus.v3.base.enums.DbBooleanConvert;
import com.sqmusicplus.v3.base.enums.SetConfigEnum;
import com.sqmusicplus.v3.config.AjaxResult;
import com.sqmusicplus.v3.config.SqConfigCache;
import com.sqmusicplus.v3.plug.kg.hander.KGHander;
import com.sqmusicplus.v3.plug.netease.hander.NeteaseHander;
import com.sqmusicplus.v3.plug.qq.hander.QQHander;
import com.sqmusicplus.v3.plug.qqvip.QQvipHander;
import com.sqmusicplus.v3.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

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
    @GetMapping("/updateConfig")
    public AjaxResult updateConfig(String configKey, String configValue) {
        SqConfig sqConfig = SqConfigCache.getSqConfig(configKey);
        if (sqConfig.getConfigDisabled()== DbBooleanConvert.YES.getValue().intValue()){
            return AjaxResult.error("该设置已禁用不允许修改");
        }
        if (sqConfig.getConfigNullCheck()== DbBooleanConvert.YES.getValue().intValue()){
            if (StringUtils.isBlank(configValue)){
                return AjaxResult.error("该设置不允许为空");
            }
        }
        if(!configCheck(sqConfig,configValue)){
            return AjaxResult.error("设置参数填写异常");

        }
        if (StringUtils.isBlank(configValue)){
            configValue="";
        }
        if (sqConfig.getType().equals("boolean")){
            if (configValue.equals(DbBooleanConvert.YES.getBooleanValue().toString())) {
                configValue = DbBooleanConvert.YES.getValue().toString();
            }else{
                configValue = DbBooleanConvert.NO.getValue().toString();
            }
        }
        SqConfigCache.updateConfigToDb(configKey,configValue);
        //根据key设置一些特殊配置
        specialPlugConfigUpdate(configKey,configValue);
        return AjaxResult.success();
    }
    /**
     * 获取启动的查询件
     */
    @SaCheckLogin
    @GetMapping("getSelectOption")
    public AjaxResult getSelectOption() {
        return AjaxResult.success(SqConfigCache.PlugOptions);
    }







    /**
     * 校验设置类型结果是否准确
     * @param sqConfig 设置信息
     * @param configValue 设置的值
     * @return
     */
    private boolean configCheck(SqConfig sqConfig, String configValue){
        String type = sqConfig.getType();
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
}
