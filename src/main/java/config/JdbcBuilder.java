package config;

import ch.qos.logback.core.db.dialect.MySQLDialect;
import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.injector.AbstractSqlInjector;
import com.baomidou.mybatisplus.extension.handlers.EnumTypeHandler;
import com.baomidou.mybatisplus.extension.plugins.pagination.dialects.H2Dialect;
import com.baomidou.mybatisplus.extension.plugins.pagination.dialects.PostgreDialect;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;

import javax.sql.DataSource;

/**
 * @Author: wkit
 * @Date: 2019-10-14 16:29
 */
public class JdbcBuilder {

    public static DataSource buildDataSource(DataSourceProperties properties) {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUrl(properties.getUrl());
        druidDataSource.setUsername(properties.getUsername());
        druidDataSource.setPassword(properties.getPassword());
        druidDataSource.setValidationQuery("SELECT 1");
        druidDataSource.setTestWhileIdle(true);
        return druidDataSource;
    }


    public static String getDialectClass(DbType dbType) {
        switch (dbType) {
            case H2:
                return H2Dialect.class.getName();
            case MYSQL:
                return MySQLDialect.class.getName();
            case POSTGRE_SQL:
                return PostgreDialect.class.getName();
            default:
                return null;
        }
    }

    public static SqlSessionFactory buildSqlSessionFactory(DataSource dataSource, JdbcFixedProperties jdbcFixedProperties, Interceptor[] interceptors, AbstractSqlInjector injector) throws Exception {
        MybatisSqlSessionFactoryBean factoryBean = new MybatisSqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setVfs(SpringBootVFS.class);
        factoryBean.setPlugins(interceptors);

        //Mybatis Configuration
        MybatisConfiguration mybatisConfiguration = new MybatisConfiguration();
        mybatisConfiguration.setMapUnderscoreToCamelCase(true);
        mybatisConfiguration.setDefaultEnumTypeHandler(EnumTypeHandler.class);
        factoryBean.setConfiguration(mybatisConfiguration);

        // Global Configuration
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setSqlInjector(injector);
        globalConfig.setMetaObjectHandler(jdbcFixedProperties.getObjectHandler());

        GlobalConfig.DbConfig dbConfig = new GlobalConfig.DbConfig();
        dbConfig.setIdType(IdType.ID_WORKER);
        dbConfig.setTableUnderline(true);
        dbConfig.setLogicDeleteValue("1");
        dbConfig.setLogicNotDeleteValue("0");
        globalConfig.setDbConfig(dbConfig);
        factoryBean.setGlobalConfig(globalConfig);
        return factoryBean.getObject();
    }
}
