package config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.injector.AbstractSqlInjector;
import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.baomidou.mybatisplus.extension.injector.LogicSqlInjector;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import liquibase.integration.spring.SpringLiquibase;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ResourceLoader;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Author: wkit
 * @Date: 2019-10-14 15:26
 */
@ComponentScan("firstClass.body")
abstract public class AbstractJdbcAutoConfiguration implements ApplicationContextAware {
    protected final JdbcProperties jdbcProperties;
    protected DataSource dataSource;
    private final ResourceLoader resourceLoader;
    private JdbcFixedProperties jdbcFixedProperties;
    private ApplicationContext applicationContext;

    public AbstractJdbcAutoConfiguration(JdbcProperties jdbcProperties, ResourceLoader resourceLoader) {
        this.jdbcProperties = jdbcProperties;
        this.resourceLoader = resourceLoader;
    }

    @PostConstruct
    public void init() {
        this.jdbcFixedProperties = initJdbcFixedProperties();
        if (this.jdbcFixedProperties != null && this.jdbcFixedProperties.isUseExistingDataSource()) {
            this.dataSource = (DataSource) applicationContext.getBean("dataSource");
        }
        this.dataSource = initDataSource();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public SqlSessionFactory sqlSessionFactory() throws Exception {
        return JdbcBuilder.buildSqlSessionFactory(this.dataSource, jdbcFixedProperties, getInterceptor(), getDefaultInjector());
    }

    public SpringLiquibase springLiquibase() {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setChangeLog("classpath:" + this.jdbcFixedProperties.getLiquibaseMasterChangeLogFilePath());
        liquibase.setDataSource(dataSource);
        liquibase.setResourceLoader(this.resourceLoader);
        liquibase.setContexts(this.jdbcProperties.getLiquibaseContexts());
        liquibase.setDatabaseChangeLogTable(this.jdbcFixedProperties.getLiquibaseTablePrefix() + "_changelog_table");
        liquibase.setDatabaseChangeLogLockTable(this.jdbcFixedProperties.getLiquibaseTablePrefix() + "_changelog_lock_table");
        return liquibase;
    }

    private AbstractSqlInjector getDefaultInjector() {
        return new LogicSqlInjector();
    }


    public Interceptor[] getInterceptor() {
        List<Interceptor> interceptors = new ArrayList<>();
        interceptors.add(getPaginationInterceptor(jdbcProperties.getDbType()));
        interceptors.add(new OptimisticLockerInterceptor());
        List<Interceptor> customInterceptors = getCustomInterceptors();
        if (customInterceptors.size() > 0) {
            interceptors.addAll(customInterceptors);
        }
        Interceptor[] result = new Interceptor[interceptors.size()];
        return interceptors.toArray(result);
    }

    /**
     * 可扩展
     *
     * @return
     */
    private List<Interceptor> getCustomInterceptors() {
        return Collections.emptyList();
    }

    private PaginationInterceptor getPaginationInterceptor(DbType dbType) {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        paginationInterceptor.setDialectClazz(JdbcBuilder.getDialectClass(dbType));
        paginationInterceptor.setSqlParser(getPaginationSqlParser());
        return paginationInterceptor;
    }

    private ISqlParser getPaginationSqlParser() {
        return null;
    }

    protected abstract JdbcFixedProperties initJdbcFixedProperties();

    protected abstract DataSource initDataSource();
}
