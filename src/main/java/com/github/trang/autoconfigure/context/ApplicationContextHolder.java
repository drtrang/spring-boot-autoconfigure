package com.github.trang.autoconfigure.context;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.core.ResolvableType;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;

/**
 * 以静态变量保存 ApplicationContext，供容器启动后使用
 *
 * @author trang
 */
public class ApplicationContextHolder implements ApplicationContextAware {

    /** ApplicationContext */
    private volatile static ApplicationContext context;

    /**
     * 实现 ApplicationContextAware 接口的回调方法，设置上下文环境
     *
     * @param applicationContext ApplicationContext
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        if (context == null) {
            synchronized (ApplicationContextHolder.class) {
                if (context == null) {
                    ApplicationContextHolder.context = applicationContext;
                }
            }
        }
    }

    public static ApplicationContext getContext() {
        return context;
    }

    public static <T> T getBean(Class<T> requiredType) {
        return context.getBean(requiredType);
    }

    public static <T> T getBean(Class<T> requiredType, Object... args) {
        return context.getBean(requiredType, args);
    }

    public static Object getBean(String name) {
        return context.getBean(name);
    }

    public static <T> T getBean(String name, Class<T> requiredType) {
        return context.getBean(name, requiredType);
    }

    public static Object getBean(String name, Object... args) {
        return context.getBean(name, args);
    }

    public static boolean containsBean(String name) {
        return context.containsBean(name);
    }

    public static boolean isSingleton(String name) {
        return context.isSingleton(name);
    }

    public static boolean isPrototype(String name) {
        return context.isPrototype(name);
    }

    public static boolean isTypeMatch(String name, ResolvableType typeToMatch) {
        return context.isTypeMatch(name, typeToMatch);
    }

    public static boolean isTypeMatch(String name, Class<?> typeToMatch) {
        return context.isTypeMatch(name, typeToMatch);
    }

    public static Class<?> getType(String name) {
        return context.getType(name);
    }

    public static String[] getAliases(String name) {
        return context.getAliases(name);
    }

    public static <T> Map<String, T> getBeansOfType(Class<T> type) {
        return context.getBeansOfType(type);
    }

    public static <T> Map<String, T> getBeansOfType(Class<T> type, boolean includeNonSingletons, boolean allowEagerInit) {
        return context.getBeansOfType(type, includeNonSingletons, allowEagerInit);
    }

    public static Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> annotationType) {
        return context.getBeansWithAnnotation(annotationType);
    }

    public static <A extends Annotation> A findAnnotationOnBean(String beanName, Class<A> annotationType) {
        return context.findAnnotationOnBean(beanName, annotationType);
    }

    public static String[] getBeanNamesForType(Class<?> type) {
        return context.getBeanNamesForType(type);
    }

    public static String[] getBeanNamesForType(Class<?> type, boolean includeNonSingletons, boolean allowEagerInit) {
        return context.getBeanNamesForType(type, includeNonSingletons, allowEagerInit);
    }

    public static String[] getBeanNamesForAnnotation(Class<? extends Annotation> annotationType) {
        return context.getBeanNamesForAnnotation(annotationType);
    }

    public static boolean containsBeanDefinition(String beanName) {
        return context.containsBeanDefinition(beanName);
    }

    public static int getBeanDefinitionCount() {
        return context.getBeanDefinitionCount();
    }

    public static String[] getBeanDefinitionNames() {
        return context.getBeanDefinitionNames();
    }

    public static String[] getBeanNamesForType(ResolvableType type) {
        return context.getBeanNamesForType(type);
    }

    public static String getId() {
        return context.getId();
    }

    public static String getApplicationName() {
        return context.getApplicationName();
    }

    public static String getDisplayName() {
        return context.getDisplayName();
    }

    public static long getStartupDate() {
        return context.getStartupDate();
    }

    public static boolean containsLocalBean(String name) {
        return context.containsLocalBean(name);
    }

    public static void publishEvent(ApplicationEvent event) {
        context.publishEvent(event);
    }

    public static void publishEvent(Object event) {
        context.publishEvent(event);
    }

    public static Environment getEnvironment() {
        return context.getEnvironment();
    }

    public static Resource[] getResources(String locationPattern) throws IOException {
        return context.getResources(locationPattern);
    }

    public static Resource getResource(String location) {
        return context.getResource(location);
    }

}