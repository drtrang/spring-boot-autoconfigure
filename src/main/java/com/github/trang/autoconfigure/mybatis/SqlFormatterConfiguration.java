package com.github.trang.autoconfigure.mybatis;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Bean;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.sql.SQLUtils.FormatOption;
import com.alibaba.druid.sql.visitor.VisitorFeature;
import com.github.trang.autoconfigure.mybatis.SqlFormatterConfiguration.DbTypeProperties;
import com.github.trang.autoconfigure.mybatis.SqlFormatterConfiguration.SqlFormatProperties;

import lombok.Getter;
import lombok.Setter;

/**
 * SqlFormatterConfiguration
 * <p>
 * Write the code. Change the world.
 *
 * @author trang
 * @date 2018/5/24
 */
@ConditionalOnClass(DruidDataSource.class)
@ConditionalOnProperty(prefix = "mybatis.configuration", name = "sql-format", havingValue = "true")
@EnableConfigurationProperties({ SqlFormatProperties.class, DbTypeProperties.class })
public class SqlFormatterConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public SqlSessionFactoryBeanPostProcessor sqlSessionFactoryBeanPostProcessor() {
        return new SqlSessionFactoryBeanPostProcessor();
    }

    @ConfigurationProperties(prefix = "mybatis.configuration")
    @Getter
    @Setter
    static class SqlFormatProperties {
        /** 格式化参数 */
        @NestedConfigurationProperty
        private FormatOption sqlFormatOption = new FormatOption(VisitorFeature.OutputUCase);
    }

    @ConfigurationProperties(prefix = "mybatis.configuration.sql-format-option")
    @Getter
    @Setter
    static class DbTypeProperties {
        /** 数据库类型，默认值：mysql */
        private String dbType = "mysql";
    }

    static class SqlSessionFactoryBeanPostProcessor implements BeanPostProcessor {

        @Autowired
        private SqlFormatProperties sqlFormatProperties;
        @Autowired
        private DbTypeProperties dbTypeProperties;

        @Override
        public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
            return bean;
        }

        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
            if (bean instanceof SqlSessionFactory) {
                SqlFormatterInterceptor sqlFormatterInterceptor =
                        new SqlFormatterInterceptor(dbTypeProperties.getDbType(), sqlFormatProperties.getSqlFormatOption());
                SqlSessionFactory sqlSessionFactory = (SqlSessionFactory) bean;
                sqlSessionFactory.getConfiguration().addInterceptor(sqlFormatterInterceptor);
            }
            return bean;
        }
    }

}