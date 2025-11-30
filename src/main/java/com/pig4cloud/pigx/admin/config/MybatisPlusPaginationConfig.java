package com.pig4cloud.pigx.admin.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class MybatisPlusPaginationConfig implements BeanPostProcessor {

    private static final long MAX_LIMIT = 10000L;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof MybatisPlusInterceptor interceptor) {
            interceptor.getInterceptors().stream()
                    .filter(PaginationInnerInterceptor.class::isInstance)
                    .map(PaginationInnerInterceptor.class::cast)
                    .filter(inner -> !Objects.equals(inner.getMaxLimit(), MAX_LIMIT))
                    .forEach(inner -> inner.setMaxLimit(MAX_LIMIT));
        }
        return bean;
    }
}
