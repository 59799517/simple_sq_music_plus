package com.sqmusicplus.v3.monitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqmusicplus.v3.monitor.entity.SqMonitor;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Classname SqMonitorMapper
 * @Description
 * @Version 1.0.0
 * @Date 2026/3/2
 * @Created by SQ
 */
@Mapper
@CacheNamespace(blocking = false)
public interface SqMonitorMapper  extends BaseMapper<SqMonitor> {
}
