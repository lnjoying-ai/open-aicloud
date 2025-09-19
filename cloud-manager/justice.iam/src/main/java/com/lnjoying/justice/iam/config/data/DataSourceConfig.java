package com.lnjoying.justice.iam.config.data;

import com.lnjoying.justice.commonweb.util.AESUtils;
import com.lnjoying.justice.iam.config.SecurityModeConfig;
import com.micro.core.config.JdbcConfig;
import com.micro.core.config.RedisConfig;
import com.micro.core.persistence.redis.RedisUtil;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;


@Configuration
@PropertySource(value = "classpath:application.properties")
public class
DataSourceConfig
{
    private static Logger LOGGER = LogManager.getLogger();

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

    @Bean
    public DataSource dataSource(){
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(jdbcConfig.getUrl());
        if (SecurityModeConfig.securityMode)
        {
            hikariConfig.setUsername(AESUtils.cbcDecrypt(jdbcConfig.getUserName(), SecurityModeConfig.aesCbcKey, SecurityModeConfig.aesCbcIv));
            hikariConfig.setPassword(AESUtils.cbcDecrypt(jdbcConfig.getPassword(), SecurityModeConfig.aesCbcKey, SecurityModeConfig.aesCbcIv));
        }
        else
        {
            hikariConfig.setUsername(jdbcConfig.getUserName());
            hikariConfig.setPassword(jdbcConfig.getPassword());
        }
        hikariConfig.setDriverClassName(jdbcConfig.getDriverClass());
        return new HikariDataSource(hikariConfig);
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
