package config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * @Author: wkit
 * @Date: 2019-10-14 12:24
 */
@Data
@ConfigurationProperties(prefix = "first-class")
public class FirstClassProperties {
    @NestedConfigurationProperty
    private JdbcProperties jdbc;
    @NestedConfigurationProperty
    private ServiceEngineProperties engine;

}
