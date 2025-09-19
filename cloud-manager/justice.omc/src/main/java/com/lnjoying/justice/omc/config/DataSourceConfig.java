package com.lnjoying.justice.omc.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.lnjoying.justice.commonweb.util.AESUtils;
import com.micro.core.config.JdbcConfig;
import com.micro.core.config.RedisConfig;
import com.micro.core.persistence.redis.RedisUtil;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.PostConstruct;
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
    static final String PACKAGE = "com.lnjoying.justice.omc.db";
    static final String MAPPER_LOCATION = "classpath:mybatis/sql/*.xml";
    public static final String TYPE_HANDLERS_PACKAGE = "com.lnjoying.justice.omc.converter";

    @Autowired
    private JdbcConfig jdbcConfig;
    @Autowired
    private RedisConfig redisConfig;

    @Bean
    JdbcConfig createJdbc()
    {
        return new JdbcConfig();
    }

    @Bean
    RedisConfig createRedisConfig()
    {
        return new RedisConfig();
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

    @PostConstruct
    void start()
    {
        try
        {
            LOGGER.info("connect redis");

            if (SecurityModeConfig.securityMode)
            {
                redisConfig.setPassword(AESUtils.cbcDecrypt(redisConfig.getPassword(), SecurityModeConfig.aesCbcKey, SecurityModeConfig.aesCbcIv));
            }

            RedisUtil.init(redisConfig);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

}
