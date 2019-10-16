package firstClass.body.utils;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Optional;

/**
 * @Author: wkit
 * @Date: 2019-10-15 21:15
 */
public class CacheUtil {

    private static Cache getCache(String cacheName, CacheManager cacheManager) {
        Cache cache = cacheManager.getCache(cacheName);
        return Optional.ofNullable(cache).orElse(null);
    }

    public static String getCacheName(Class serviceClass) {
        Annotation[] annotations = serviceClass.getAnnotations();
        // Check if @Service
        Service serviceAnnotation = Arrays.stream(annotations)
                .filter(annotation -> annotation instanceof Service).findFirst()
                .map(Service.class::cast).orElse(null);
        Assert.notNull(serviceAnnotation, "Cache should only be applied on @Service classes");
        CacheConfig cacheConfig = Arrays.stream(annotations)
                .filter(annotation -> annotation instanceof CacheConfig).findFirst()
                .map(CacheConfig.class::cast).orElse(null);
        if (cacheConfig == null) {
            return null;
        }
        return Arrays.stream(cacheConfig.cacheNames()).findFirst().orElse(null);
    }

    public static <T extends Object> T getCachedObject(String cacheName, String cacheKey, CacheManager cacheManager, Class<T> objectClass) {
        Cache cache = getCache(cacheName, cacheManager);
        if (cache == null) {
            return null;
        }
        Cache.ValueWrapper valueWrapper = Optional.ofNullable(cache.get(cacheKey)).orElse(null);
        Object object = Optional.ofNullable(valueWrapper).map(Cache.ValueWrapper::get).orElse(null);
        return Optional.ofNullable(object).map(objectClass::cast).orElse(null);
    }

    public static <T extends Object> void putCachedObject(String cacheName, String cacheKey, T cacheObject, CacheManager cacheManager) {
        Cache cache = getCache(cacheName, cacheManager);
        if (cache != null) {
            cache.put(cacheKey, cacheObject);
        }
    }

    public static void evictCachedObject(String cacheName, String cacheKey, CacheManager cacheManager) {
        Cache cache = getCache(cacheName, cacheManager);
        if (cache != null) {
            cache.evict(cacheKey);
        }
    }
}
