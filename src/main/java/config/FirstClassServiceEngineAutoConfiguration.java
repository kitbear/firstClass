package config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @Author: wkit
 * @Date: 2019-10-16 13:06
 */
@Slf4j
@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
@ComponentScan("config")
public class FirstClassServiceEngineAutoConfiguration {
}
