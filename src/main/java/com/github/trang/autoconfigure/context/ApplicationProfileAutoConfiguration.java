package com.github.trang.autoconfigure.context;

import com.github.trang.autoconfigure.context.ApplicationProfileAutoConfiguration.SpringProfileImportSelector;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

import java.util.*;

/**
 * Spring Profile 的自动配置
 *
 * 用于需要区分运行环境的配置，做到声明的配置只在当前环境生效
 *
 * @author trang
 */
@Configuration
@Import(SpringProfileImportSelector.class)
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
public class ApplicationProfileAutoConfiguration {

    static class SpringProfileImportSelector implements ImportSelector, EnvironmentAware {

        private static final String PREFIX = "spring.profiles.configure.includes";

        private Set<String> configurations;

        @Override
        public String[] selectImports(AnnotationMetadata metadata) {
            return configurations.stream()
                    .filter(Objects::nonNull)
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .toArray(String[]::new);
        }

        @Override
        public void setEnvironment(Environment environment) {
            this.configurations = Binder.get(environment)
                    .bind(PREFIX, Bindable.setOf(String.class))
                    .orElseGet(HashSet::new);
        }

    }

}