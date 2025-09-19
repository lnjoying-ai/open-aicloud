package com.lnjoying.justice.ims.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.lnjoying.justice.commonweb.util.AESUtils;
import com.micro.core.config.JdbcConfig;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;


@Configuration
@PropertySource(value = "classpath:application.properties")
@MapperScan(basePackages = DataSourceConfig.PACKAGE, sqlSessionFactoryRef = "sqlSessionFactory")
//@AutoConfigureBefore(PageHelperAutoConfiguration.class)
@EnableTransactionManagement
@EnableAspectJAutoProxy
public class DataSourceConfig
{
    private static Logger LOGGER = LogManager.getLogger();
    static final String PACKAGE = "com.lnjoying.justice.ims.db";
    static final String MAPPER_LOCATION = "classpath:mybatis/sql/*.xml";
    public static final String TYPE_HANDLERS_PACKAGE = "com.lnjoying.justice.ims.converter";

    @Autowired
    private JdbcConfig jdbcConfig;

    @Bean
    JdbcConfig createJdbc()
    {
        return new JdbcConfig();
    }

    @Bean(initMethod = "init", destroyMethod = "close")
    @Primary
    public DruidDataSource dataSource()
    {
        LOGGER.info("create druid data source pool");

        if (SecurityModeConfig.securityMode)
        {
            jdbcConfig.setUserName(AESUtils.cbcDecrypt(jdbcConfig.getUserName(), SecurityModeConfig.aesCbcKey, SecurityModeConfig.aesCbcIv));
            jdbcConfig.setPassword(AESUtils.cbcDecrypt(jdbcConfig.getPassword(), SecurityModeConfig.aesCbcKey, SecurityModeConfig.aesCbcIv));
        }

        DruidDataSource druidDataSource = DataSourceFactory.createDruidDataSource(jdbcConfig);
        return druidDataSource;
    }


    @Bean(name = "transactionManager")
    @Primary
    public DataSourceTransactionManager createTransactionManager(@Qualifier("dataSource") DataSource dataSource) throws Exception
    {
        final DataSourceTransactionManager sourceTransactionManager = DataSourceFactory.createTransactionManager(dataSource);
        return sourceTransactionManager;
    }

    @Bean(name = "sqlSessionFactory")
    @Primary
    public SqlSessionFactory createSqlSessionFactory(@Qualifier("dataSource") DataSource dataSource) throws Exception
    {
        final SqlSessionFactory sessionFactory = DataSourceFactory.createSqlSessionFactory(dataSource, DataSourceConfig.MAPPER_LOCATION);
        return sessionFactory;
    }

}
