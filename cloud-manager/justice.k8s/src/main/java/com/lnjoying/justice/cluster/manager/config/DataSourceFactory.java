package com.lnjoying.justice.cluster.manager.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.pagehelper.PageInterceptor;
import com.micro.core.config.JdbcConfig;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Properties;

public class DataSourceFactory
{
    private static Logger LOGGER = LogManager.getLogger();
    public static final String TYPE_HANDLERS_PACKAGE = "com.lnjoying.justice.cluster.manager.converter";

    public static DruidDataSource createDruidDataSource(JdbcConfig jdbcConfig)
    {
        LOGGER.info("create druid data source pool");
        final DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUrl(jdbcConfig.getUrl());
        druidDataSource.setUsername(jdbcConfig.getUserName());
        druidDataSource.setPassword(jdbcConfig.getPassword());
        druidDataSource.setInitialSize(jdbcConfig.getInitialSize());
        druidDataSource.setMinIdle(jdbcConfig.getMinIdle());
        druidDataSource.setMaxActive(jdbcConfig.getMaxActive());
        druidDataSource.setTimeBetweenEvictionRunsMillis(jdbcConfig.getTimeBetweenEvictionRunsMillis());
        druidDataSource.setMinEvictableIdleTimeMillis(jdbcConfig.getMinEvictableIdleTimeMillis());
        druidDataSource.setValidationQuery(jdbcConfig.getValidationQuery());
        druidDataSource.setTestWhileIdle(jdbcConfig.isTestWhileIdle());
        druidDataSource.setTestOnBorrow(jdbcConfig.isTestOnBorrow());
        druidDataSource.setTestOnReturn(jdbcConfig.isTestOnReturn());
        druidDataSource.setMaxPoolPreparedStatementPerConnectionSize(jdbcConfig.getMaxPoolPreparedStatementPerConnectionSize());
        try
        {
            druidDataSource.setFilters(jdbcConfig.getFilters());
        }
        catch (SQLException e)
        {
            if (LOGGER.isInfoEnabled())
            {
                LOGGER.info(e.getMessage(), e);
            }
        }

        return druidDataSource;
    }

    public static DataSourceTransactionManager createTransactionManager(DataSource dataSource) throws Exception
    {
        LOGGER.info("create transaction manager");
        final DataSourceTransactionManager sourceTransactionManager = new DataSourceTransactionManager();
        sourceTransactionManager.setDataSource(dataSource);
        sourceTransactionManager.setTransactionSynchronization(1);
        return sourceTransactionManager;
    }
    
    public static SqlSessionFactory createSqlSessionFactory(DataSource dataSource, String map_location) throws Exception
    {
        LOGGER.info("create sql session factory");
        
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setTypeAliasesPackage("com.lnjoying.justice.cluster.manager.db.mapper");
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources(map_location));
        sessionFactory.setTypeHandlersPackage(TYPE_HANDLERS_PACKAGE);
        Interceptor[] plugins =  new Interceptor[]{pageHelper()};
        
        sessionFactory.setPlugins(plugins);
        
        return sessionFactory.getObject();
    }
    
    public static PageInterceptor pageHelper()
    {
        PageInterceptor pageHelper = new PageInterceptor();
        Properties p = new Properties();
        p.setProperty("offsetAsPageNum", "true");
        p.setProperty("rowBoundsWithCount", "true");
        p.setProperty("reasonable", "false");
        pageHelper.setProperties(p);
        return pageHelper;
        
    }
}
