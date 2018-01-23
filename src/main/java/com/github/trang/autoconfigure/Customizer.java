package com.github.trang.autoconfigure;

/**
 * Spring Bean 的回调接口，用来客制化任意 Bean
 *
 * @author trang
 */
public interface Customizer<T> {

    /**
     * 客制化
     *
     * @param bean bean
     */
    void customize(T bean);

}