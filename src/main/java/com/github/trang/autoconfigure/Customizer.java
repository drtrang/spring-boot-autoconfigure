package com.github.trang.autoconfigure;

/**
 * Spring Bean 初始化的回调接口，用来自定义 Bean
 *
 * @author trang
 */
public interface Customizer<T> {

    /**
     * 自定义方法
     *
     * @param bean bean
     */
    void customize(T bean);

}