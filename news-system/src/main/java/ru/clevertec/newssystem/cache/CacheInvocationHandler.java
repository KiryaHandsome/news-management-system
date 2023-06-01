package ru.clevertec.newssystem.cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.clevertec.newssystem.cache.annotation.CacheGet;
import ru.clevertec.newssystem.cache.annotation.CacheRemove;
import ru.clevertec.newssystem.cache.annotation.CacheSave;
import ru.clevertec.newssystem.cache.provider.api.CacheProvider;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@Scope("prototype")
@RequiredArgsConstructor
public class CacheInvocationHandler implements InvocationHandler {

    private final CacheProvider<String> cacheProvider;
    private final Map<String, Method> methods = new HashMap<>();
    private Object target;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();
        Method currentMethod = methods.get(methodName);
        try {
            if (currentMethod.isAnnotationPresent(CacheRemove.class)) {
                return remove(currentMethod, args);
            }
            if (currentMethod.isAnnotationPresent(CacheGet.class)) {
                return get(currentMethod, args);
            }
            if (currentMethod.isAnnotationPresent(CacheSave.class)) {
                return save(currentMethod, args);
            }
            return currentMethod.invoke(target, args);
        } catch (InvocationTargetException ex) {
            throw ex.getCause();
        }
    }

    /**
     * The method removes value from cache by entity id.
     *
     * @param method source method
     * @param args   method arguments
     * @return value returned from source method
     */
    private Object remove(Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
        Object returnValue = method.invoke(target, args);
        Integer id = (Integer) args[0];
        CacheRemove annotation = method.getAnnotation(CacheRemove.class);
        String cacheName = annotation.value();
        if (annotation.clearCache()) {
            cacheProvider.clear();
            log.info("Clear cache");
        } else {
            String cacheKey = generateKey(cacheName, id);
            cacheProvider.delete(cacheKey);
            log.info("Remove value from cache by id: " + id);
        }
        return returnValue;
    }

    /**
     * The method serves to get cached object.
     * If object with passed id is present it's returned,
     * otherwise source method is invoked and returned
     * value is put in cache
     *
     * @param method source method
     * @param args   method arguments
     * @return object from cache or db
     */
    private Object get(Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
        Integer id = (Integer) args[0];
        String cacheName = method.getAnnotation(CacheGet.class).value();
        String cacheKey = generateKey(cacheName, id);
        Object cachedValue = cacheProvider.get(cacheKey);
        if (cachedValue != null) {
            log.info("Get value from cache with cacheKey = " + cacheKey);
            return cachedValue;
        }
        Object returnValue = method.invoke(target, args);
        cacheProvider.put(cacheKey, returnValue);
        return returnValue;
    }


    /**
     * The method saves(or updates value if it's already present)
     * in cache value returned after invoking method
     *
     * @param method source method
     * @param args   method arguments
     * @return value returned from source method
     */
    private Object save(Method method, Object[] args) throws Exception {
        Object returnValue = method.invoke(target, args);
        Integer id = retrieveId(returnValue);
        String cacheName = method.getAnnotation(CacheSave.class).value();
        String cacheKey = generateKey(cacheName, id);
        cacheProvider.put(cacheKey, returnValue);
        log.info("Save value in cache with cacheKey = " + cacheKey);
        return returnValue;
    }

    /**
     * This method serves for retrieving value
     * of integer id field from object
     *
     * @param object target instance
     * @return retrieved id
     */
    private static Integer retrieveId(Object object) throws Exception {
        Field idField = object.getClass().getDeclaredField("id");
        idField.setAccessible(true);
        return (Integer) idField.get(object);
    }

    /**
     * Sets target and save method's names from target class
     *
     * @param target instance of target class
     */
    public void setTarget(Object target) {
        this.target = target;
        for (Method method : target.getClass().getDeclaredMethods()) {
            this.methods.put(method.getName(), method);
        }
    }

    private static String generateKey(String cacheName, Integer id) {
        return cacheName + id;
    }
}
