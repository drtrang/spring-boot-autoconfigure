package com.github.trang.autoconfigure.mybatis;

import com.alibaba.druid.sql.SQLUtils.FormatOption;
import com.alibaba.druid.sql.visitor.VisitorFeature;
import com.github.trang.autoconfigure.mybatis.MyBatisAutoConfiguration.DbTypeProperties;
import com.github.trang.autoconfigure.mybatis.MyBatisAutoConfiguration.SqlFormatProperties;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
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
@EnableConfigurationProperties({SqlFormatProperties.class, DbTypeProperties.class})
public class MyBatisAutoConfiguration {

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    @ConditionalOnMissingBean
    @ConditionalOnBean(SqlSession.class)
    public SqlMapper sqlMapper(SqlSession sqlSession) {
        return new SqlMapper(sqlSession);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "mybatis.configuration", name = "sql-format", havingValue = "true")
    public SqlSessionFactoryBeanPostProcessor sqlSessionFactoryBeanPostProcessor() {
        return new SqlSessionFactoryBeanPostProcessor();
    }

    @ConfigurationProperties(prefix = "mybatis.configuration")
    @Getter @Setter
    static class SqlFormatProperties {
        @NestedConfigurationProperty
        private FormatOption sqlFormatOption = new FormatOption(VisitorFeature.OutputUCase);
    }

    @ConfigurationProperties(prefix = "mybatis.configuration.sql-format-option")
    @Getter @Setter
    static class DbTypeProperties {
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