package com.sqmusicplus.config;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.autoconfigure.DdlApplicationRunner;
import com.baomidou.mybatisplus.extension.ddl.IDdl;
import com.baomidou.mybatisplus.extension.ddl.SimpleDdl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Classname AutoDdlConfig
 * @Description 自动更新维护数据库
 * @Version 1.0.0
 * @Date 2024/8/13 20:06
 * @Created by SQ
 */
@Component
public class AutoDdlConfig   extends SimpleDdl {
    @Override
    public List<String> getSqlFiles() {
        return CollUtil.newArrayList(
                "db/sqmusic.sql"
                ,"db/insert_config.sql"
                ,"db/v2.13.1_updat.sql"
                ,"db/v2.13.3_update.sql"
                 ,"db/v2.15.0_update.sql"
        );
    }

    @Bean("ddlApplicationRunner")
    public DdlApplicationRunner ddlApplicationRunner(@Autowired(required = false) List<IDdl> ddlList) {
        return new SQDdlApplicationRunner(ddlList);
    }
}
