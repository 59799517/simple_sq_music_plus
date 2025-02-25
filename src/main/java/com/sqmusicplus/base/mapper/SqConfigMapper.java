package com.sqmusicplus.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqmusicplus.base.entity.SqConfig;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


/**
 * @Classname SqConfigMapper
 * @Description 配置mapper
 * @Version 1.0.0
 * @Date 2022/10/21 10:45
 * @Created by SQ
 */
@Mapper
@CacheNamespace(blocking = false)
public interface SqConfigMapper extends BaseMapper<SqConfig> {

    @Select("select * from sq_config where config_key = #{key}")
    SqConfig selectByKeyAndValue(@Param("key") String key);
}
