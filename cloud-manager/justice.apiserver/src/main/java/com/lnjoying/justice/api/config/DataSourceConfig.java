package com.lnjoying.justice.api.config;

import com.lnjoying.justice.commonweb.util.AESUtils;
import com.micro.core.config.RedisConfig;
import com.micro.core.persistence.redis.RedisUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.annotation.PostConstruct;

@Configuration
@PropertySource(value = "classpath:application.properties", encoding="utf-8")
public class DataSourceConfig
{
    private static Logger LOGGER = LogManager.getLogger();


    @Autowired
    private RedisConfig redisConfig;

    @Bean
    RedisConfig createRedisConfig()
    {
        return new RedisConfig();
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
