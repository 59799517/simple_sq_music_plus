package com.sqmusicplus.v3.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sqmusicplus.v3.config.AjaxResult;
import com.sqmusicplus.v3.monitor.entity.SqMonitor;
import com.sqmusicplus.v3.monitor.service.SqMonitorService;
import com.sqmusicplus.v3.plug.entity.ParserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * @Classname MonitorController
 * @Description
 * @Version 1.0.0
 * @Date 2026/3/2
 * @Created by SQ
 */
@Slf4j
@RestController
@RequestMapping("/api/monitor")
public class MonitorController {
    @Autowired
    private SqMonitorService monitorService;
    @RequestMapping("/list")
    public AjaxResult list(){
        List<SqMonitor> list = monitorService.list();
        return AjaxResult.success(list);
    }
    @RequestMapping("/add")
    public AjaxResult add(@RequestBody SqMonitor sqMonitor){
        sqMonitor.setCreateTime(new Date());
        sqMonitor.setUpdateTime(new Date());
        String plugName = sqMonitor.getPlugName();
        String targetId = sqMonitor.getTargetId();
        LambdaQueryWrapper<SqMonitor> sqMonitorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<SqMonitor> wrapper = sqMonitorLambdaQueryWrapper.eq(SqMonitor::getPlugName, plugName)
                .eq(SqMonitor::getTargetId, targetId);
        long count = monitorService.count(wrapper);
        if (count > 0){
            return AjaxResult.error("已存在,不要重复添加");
        }
        boolean save = monitorService.save(sqMonitor);
        return AjaxResult.success(save);
    }
    @RequestMapping("/delete")
    public AjaxResult delete(@RequestBody SqMonitor sqMonitor){
        boolean delete = monitorService.removeById(sqMonitor.getId());
        return AjaxResult.success(delete);
    }


}
