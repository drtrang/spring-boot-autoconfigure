package com.github.trang.autoconfigure.condition;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

/**
 * {@link Conditional} that only matches when the specified bean classes and/or names are
 * already contained in the {@link BeanFactory}. When placed on a {@code @Bean} method,
 * the bean class defaults to the return type of the factory method:
 *
 * <pre class="code">
 * &#064;Configuration
 * public class MyAutoConfiguration {
 *
 *     &#064;ConditionalOnBean
 *     &#064;Bean
 *     public MyService myService() {
 *         ...
 *     }
 *
 * }</pre>
 * <p>
 * In the sample above the condition will match if a bean of type {@code MyService} is
 * already contained in the {@link BeanFactory}.
 * <p>
 * The condition can only match the bean definitions that have been processed by the
 * application context so far and, as such, it is strongly recommended to use this
 * condition on auto-configuration classes only. If a candidate bean may be created by
 * another auto-configuration, make sure that the one using this condition runs after.
 *
 * @author trang
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(OnBeansCondition.class)
public @interface ConditionalOnBeans {

    /**
     * The class type of bean that should be checked. The condition matches when any of
     * the classes specified is contained in the {@link ApplicationContext}.
     * @return the class types of beans to check
     */
    Class<?>[] value() default {};

    /**
     * The class type names of bean that should be checked. The condition matches when any
     * of the classes specified is contained in the {@link ApplicationContext}.
     * @return the class type names of beans to check
     */
    String[] type() default {};

    /**
     * The annotation type decorating a bean that should be checked. The condition matches
     * when any of the annotations specified is defined on a bean in the
     * {@link ApplicationContext}.
     * @return the class-level annotation types to check
     */
    Class<? extends Annotation>[] annotation() default {};

    /**
     * The names of beans to check. The condition matches when any of the bean names
     * specified is contained in the {@link ApplicationContext}.
     * @return the name of beans to check
     */
    String[] name() default {};

    /**
     * Strategy to decide if the application context hierarchy (parent contexts) should be
     * considered.
     * @return the search strategy
     */
    SearchStrategy search() default SearchStrategy.ALL;

}