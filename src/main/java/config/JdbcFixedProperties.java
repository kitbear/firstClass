package config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.Data;

import java.util.List;

/**
 * @Author: wkit
 * @Date: 2019-10-14 15:41
 */
@Data
public class JdbcFixedProperties {
    /**
     * Mybatis Mapper Location
     */
    private String mapperLocation;

    /**
     * Mybatis TypeEnums Packages
     */
    private String typeEnumsPackage;

    /**
     * Mybatis TypeHandler Packages
     */
    private String typeHandlersPackage;

    /**
     * Mybatis TypeAliases Packages
     */
    private String typeAliasesPackage;

    /**
     * Mybatis Plus MetaObjectHandler
     */
    private MetaObjectHandler objectHandler;

    /**
     * Liquibase changeLog meta tables name prefix
     */
    private String liquibaseTablePrefix;

    /**
     * Liquibase master changelog path
     */
    private String liquibaseMasterChangeLogFilePath;

    /**
     * Mapper interfaces basePackages
     */
    private List<String> mapperClassBasePackages;

    /**
     * reuse existing configured datasource
     */
    private boolean useExistingDataSource;
}
