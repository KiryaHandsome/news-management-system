package ru.clevertec.news.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import ru.clevertec.news.cache.annotation.EnableCache;
import ru.clevertec.news.cache.provider.api.CacheProvider;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * Custom bean post processor for creating
 * proxy for classes that have {@link EnableCache } annotation
 */
@Component
@RequiredArgsConstructor
public class CacheBeanPostProcessor implements BeanPostProcessor {

    private final Map<String, Object> annotatedServices = new HashMap<>();
    private final CacheProvider<String> cacheProvider;
    private final ApplicationContext applicationContext;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> clazz = bean.getClass();
        if (clazz.isAnnotationPresent(EnableCache.class)) {
            annotatedServices.put(beanName, bean);
        }
        return bean;
    }

    /**
     * Checks whether annotation {@link EnableCache} is present on class:
     * if yes it returns proxy with {@link CacheInvocationHandler} as invocation handler,
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Object target = annotatedServices.get(beanName);
        if (target != null) {
            Class<?> clazz = target.getClass();
            CacheInvocationHandler handler = applicationContext.getBean(
                    CacheInvocationHandler.class,
                    cacheProvider
            );
            handler.setTarget(target);
            return Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(), handler);
        }
        return bean;
    }
}
