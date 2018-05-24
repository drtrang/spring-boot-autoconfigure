package com.github.trang.autoconfigure.mybatis;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;

/**
 * MyBaits 自定义工具的自动配置
 *
 * @author trang
 */
@Configuration
@ConditionalOnClass(SqlSessionFactory.class)
@AutoConfigureBefore(name = "com.github.pagehelper.autoconfigure.PageHelperAutoConfiguration")
@AutoConfigureAfter(name = "org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration")
@Import(SqlFormatterConfiguration.class)
public class MyBatisAutoConfiguration {

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    @ConditionalOnMissingBean(SqlMapper.class)
    @ConditionalOnBean(SqlSession.class)
    public SqlMapper sqlMapper(SqlSession sqlSession) {
        return new SqlMapper(sqlSession);
    }

}