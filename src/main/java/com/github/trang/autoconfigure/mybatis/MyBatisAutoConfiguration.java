package com.github.trang.autoconfigure.mybatis;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
public class MyBatisAutoConfiguration {

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    @ConditionalOnBean(SqlSession.class)
    @ConditionalOnMissingBean
    public SqlMapper sqlMapper(SqlSession sqlSession) {
        return new SqlMapper(sqlSession);
    }

    @Bean
    @ConditionalOnProperty(prefix = "mybatis.configuration", name = "sql-format", havingValue = "true")
    @ConditionalOnMissingBean
    public SqlFormatterPostProcessor sqlFormatterPostProcessor() {
        return new SqlFormatterPostProcessor();
    }

    static class SqlFormatterPostProcessor implements BeanPostProcessor {

        @Override
        public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
            return bean;
        }

        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
            if (bean instanceof SqlSessionFactory) {
                SqlSessionFactory sqlSessionFactory = (SqlSessionFactory) bean;
                sqlSessionFactory.getConfiguration().addInterceptor(new SqlFormatterInterceptor());
            }
            return bean;
        }
    }

}