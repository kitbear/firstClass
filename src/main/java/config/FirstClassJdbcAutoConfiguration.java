package config;

import config.handler.DefaultMetaObjectHandler;
import io.shardingjdbc.core.api.ShardingDataSourceFactory;
import io.shardingjdbc.core.api.config.ShardingRuleConfiguration;
import liquibase.integration.spring.SpringLiquibase;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Author: wkit
 * @Date: 2019-10-14 12:44
 */
@Configuration
@EnableConfigurationProperties(FirstClassProperties.class)
@MapperScan(
        basePackages = {"firstClass.body"},
        sqlSessionFactoryRef = "firstClassSqlSessionFactory"
)
public class FirstClassJdbcAutoConfiguration extends AbstractJdbcAutoConfiguration {

    public FirstClassJdbcAutoConfiguration(FirstClassProperties properties, ResourceLoader resourceLoader) {
        super(properties.getJdbc(), resourceLoader);
    }

    @Override
    protected JdbcFixedProperties initJdbcFixedProperties() {
        JdbcFixedProperties properties = new JdbcFixedProperties();
        properties.setMapperLocation("classpath:firstClass/body/*/*Mapper.xml");
        properties.setTypeEnumsPackage("firstClass.body.**.enumeration");
        properties.setTypeAliasesPackage("firstClass.body.**.entity");
        properties.setObjectHandler(new DefaultMetaObjectHandler());
        properties.setLiquibaseMasterChangeLogFilePath("db/master.xml");
        properties.setLiquibaseTablePrefix("fc");
        properties.setMapperClassBasePackages(Collections.singletonList("firstClass"));
        return properties;
    }

    @Override
    protected DataSource initDataSource() {
        Map<String, DataSource> dataSourceMap = new LinkedHashMap<>();
        dataSourceMap.put("ds0", JdbcBuilder.buildDataSource(this.jdbcProperties.getDataSource()));
        ShardingRuleConfiguration config = new ShardingRuleConfiguration();
        config.setDefaultDataSourceName("ds0");
        try {
            return ShardingDataSourceFactory.createDataSource(dataSourceMap, config, new LinkedHashMap<>(), null);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to init ShardingDataSource", e);
        }
    }

    @Bean(name = "firstClassSqlSessionFactory")
    @Override
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        return super.sqlSessionFactory();
    }

    @Bean(name = "firstClassSpringLiquibase")
    public SpringLiquibase firstClassSpringLiquibase() {
        SpringLiquibase liquibase = super.springLiquibase();
        liquibase.setDataSource(JdbcBuilder.buildDataSource(this.jdbcProperties.getDataSource()));
        return liquibase;
    }

    @Bean(name = "liquibase")
    @Override
    public SpringLiquibase springLiquibase() {
        SpringLiquibase liquibase = super.springLiquibase();
        liquibase.setDataSource(JdbcBuilder.buildDataSource(this.jdbcProperties.getDataSource()));
        return liquibase;
    }

    @Bean(name = "firstClassDataSource")
    public DataSource dataSource() {
        return this.dataSource;
    }
}
