package com.github.trang.autoconfigure.context;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring 自定义工具的自动配置
 *
 * @author trang
 */
@Configuration
@AutoConfigureOrder
@Slf4j
public class ApplicationContextAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "spring.context-holder", name = "enabled", havingValue = "true", matchIfMissing = true)
    public SpringContextHolder springContextHolder() {
        log.warn("SpringContextHolder class is deprecated, please replace it with ApplicationContextHolder.");
        return new SpringContextHolder();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "spring.context-holder", name = "enabled", havingValue = "true", matchIfMissing = true)
    public ApplicationContextHolder applicationContextHolder() {
        return new ApplicationContextHolder();
    }

}