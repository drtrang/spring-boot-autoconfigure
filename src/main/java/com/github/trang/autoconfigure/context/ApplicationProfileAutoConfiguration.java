package com.github.trang.autoconfigure.context;

import static java.util.Collections.emptySet;

import java.util.Objects;
import java.util.Set;

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

import com.github.trang.autoconfigure.context.ApplicationProfileAutoConfiguration.SpringProfileImportSelector;

/**
 * Spring Profile 的自动配置，默认自动加载
 * <p>
 * 用于需要区分运行环境的配置，做到声明的配置只在当前环境生效
 * <p>
 * 其实最好是用 exclude 的方式，但是目前还没有想好实现方式，等什么时候把 OverrideAutoConfiguration 的原理看懂了就换了它！
 *
 * @author trang
 */
@Configuration
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@Import(SpringProfileImportSelector.class)
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
                    .orElse(emptySet());
        }

    }

}