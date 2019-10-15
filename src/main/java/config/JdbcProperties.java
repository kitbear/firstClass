package config;

import com.baomidou.mybatisplus.annotation.DbType;
import lombok.Data;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * @Author: wkit
 * @Date: 2019-10-14 12:50
 */
@Data
public class JdbcProperties {
    private DbType dbType;
    @NestedConfigurationProperty
    private DataSourceProperties dataSource;
    private String liquibaseContexts;
}
