package com.sqmusicplus.v3.config;

import com.baomidou.mybatisplus.autoconfigure.DdlApplicationRunner;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AutoDdlConfigTest {
    @Test
    void componentScanningRegistersOneDdlRunner() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext()) {
            context.registerBean(DataSource.class, () -> new DriverManagerDataSource());
            ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(context);
            scanner.setResourcePattern("*Ddl*.class");
            scanner.scan(AutoDdlConfig.class.getPackageName());
            context.refresh();

            assertEquals(1, context.getBeansOfType(DdlApplicationRunner.class).size());
            assertEquals(SQDdlApplicationRunner.class, context.getBean("ddlApplicationRunner").getClass());
        }
    }
}
