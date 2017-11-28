package com.github.trang.autoconfigure.context;

import com.github.trang.autoconfigure.context.SpringProfileAutoConfiguration.SpringProfileImportSelector;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;
import java.util.Objects;

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
public class SpringProfileAutoConfiguration {

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