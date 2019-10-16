package config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

/**
 * @Author: wkit
 * @Date: 2019-10-16 11:10
 */
@Configuration
@EnableConfigurationProperties(FirstClassProperties.class)
public class FirstClassRedisCacheAutoConfiguration extends AbstractRedisCacheAutoConfiguration {
    public FirstClassRedisCacheAutoConfiguration(FirstClassProperties properties, RedisConnectionFactory connectionFactory) {
        super(properties.getEngine(), connectionFactory);
    }

    @Bean("firstClassCacheManager")
    @Primary
    @Override
    public RedisCacheManager redisCacheManager() {
        return super.redisCacheManager();
    }

    @Override
    protected String getMouleName() {
        return "FIRST_CLASS_SERVICE";
    }
}
