package config;

import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Author: wkit
 * @Date: 2019-10-16 11:11
 */
abstract public class AbstractRedisCacheAutoConfiguration {
    private final String systemName;
    private final RedisConnectionFactory connectionFactory;

    private RedisCacheManager redisCacheManager;
    protected final Map<String, Duration> cacheExpirationSetting = new LinkedHashMap<>();

    public AbstractRedisCacheAutoConfiguration(ServiceEngineProperties properties, RedisConnectionFactory connectionFactory) {
        this.systemName = properties.getSystemName();
        this.connectionFactory = connectionFactory;
    }

    @PostConstruct
    protected void initCacheManeger() {
        this.initCacheExpirationSetting();

        RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager.RedisCacheManagerBuilder
                .fromConnectionFactory(this.connectionFactory)
                .cacheDefaults(getDefaultCacheConfig());
        if (this.cacheExpirationSetting.size() > 0) {
            for (String cacheName : cacheExpirationSetting.keySet()) {
                RedisCacheConfiguration cacheConfiguration = getDefaultCacheConfig();
                cacheConfiguration.entryTtl(cacheExpirationSetting.get(cacheName));
            }
        }
        this.redisCacheManager = builder.build();
    }

    protected RedisCacheConfiguration getDefaultCacheConfig() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .computePrefixWith((cacheName -> this.systemName + ":" + getMouleName() + ":" + cacheName + "::"))
                .entryTtl(Duration.ofMillis(30));

    }

    protected abstract String getMouleName();


    protected void initCacheExpirationSetting() {

    }

    public RedisCacheManager redisCacheManager() {
        return this.redisCacheManager;
    }
}
