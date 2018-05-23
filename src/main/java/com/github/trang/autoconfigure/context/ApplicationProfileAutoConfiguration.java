package com.github.trang.autoconfigure.context;

import java.util.Map;
import java.util.Objects;

import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

import com.github.trang.autoconfigure.context.ApplicationProfileAutoConfiguration.SpringProfileImportSelector;

/**
 * Spring Profile 的自动配置
 * <p>
 * 用于需要区分运行环境的配置，做到声明的配置只在当前环境生效
 *
 * @author trang
 */
@Configuration
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@Import(SpringProfileImportSelector.class)
public class ApplicationProfileAutoConfiguration {

    static class SpringProfileImportSelector implements ImportSelector, EnvironmentAware {

        private static final String CONFIGURE = "spring.profiles.configure.includes";

        private RelaxedPropertyResolver resolver;

        @Override
        public String[] selectImports(AnnotationMetadata metadata) {
            Map<String, Object> properties = resolver.getSubProperties("");
            if (properties == null || properties.isEmpty()) {
                return new String[0];
            }
            return properties.values().stream()
                    .filter(Objects::nonNull)
                    .map(Objects::toString)
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .toArray(String[]::new);
        }

        @Override
        public void setEnvironment(Environment environment) {
            this.resolver = new RelaxedPropertyResolver(environment, CONFIGURE);
        }

    }

}