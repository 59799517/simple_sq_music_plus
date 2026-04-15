package com.sqmusicplus.v3.config;

import com.baomidou.mybatisplus.autoconfigure.DdlApplicationRunner;
import com.baomidou.mybatisplus.autoconfigure.DdlAutoConfiguration;
import com.baomidou.mybatisplus.extension.ddl.IDdl;
import com.baomidou.mybatisplus.extension.ddl.SimpleDdl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

/**
 * @Classname SQDdlApplicationRunner
 * @Description ddl自定义执行器（H2 兼容版本）
 * @Version 1.0.0
 * @Date 2024/8/14 9:36
 * @Created by SQ
 */
@Configuration
@Order(1)
public class SQDdlApplicationRunner extends DdlApplicationRunner {

    public SQDdlApplicationRunner(List<IDdl> ddlList) {
        super(ddlList);
    }
}
