package com.github.trang.autoconfigure.context;

import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

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
    public ApplicationContextHolder applicationContextHolder() {
        return new ApplicationContextHolder();
    }

}