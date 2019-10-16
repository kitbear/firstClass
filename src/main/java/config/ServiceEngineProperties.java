package config;

import constants.Environment;
import lombok.Data;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * @Author: wkit
 * @Date: 2019-10-16 11:41
 */
@Data
public class ServiceEngineProperties {
    private String systemName;
    private Environment environment;
    @NestedConfigurationProperty
    private RedisProperties redis;
    @NestedConfigurationProperty
    private Auth auth;

    @Data
    public static class Auth {
        private String clientId;
        private String clientSecret;
        private String tokenUrl;
    }
}
