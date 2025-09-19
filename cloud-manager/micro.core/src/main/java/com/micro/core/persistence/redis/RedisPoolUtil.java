package com.micro.core.persistence.redis;

import com.micro.core.config.RedisConfig;
import com.micro.core.exception.BaseException;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.support.ConnectionPoolSupport;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RedisPoolUtil
{

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisPoolUtil.class);

    static GenericObjectPool<StatefulRedisConnection<String, String>> instance;

    public static void init(RedisConfig redisConfig)
    {
        RedisURI redisUri = RedisURI.builder().withHost(redisConfig.getHost()).withPort(redisConfig.getPort())
                .withPassword(redisConfig.getPassword()).build();
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig<>();
        poolConfig.setMaxTotal(redisConfig.getMaxConnect());
        poolConfig.setMaxIdle(redisConfig.getMaxIdle());
        poolConfig.setMaxWaitMillis(redisConfig.getMaxWaitMillis());
        poolConfig.setTestOnBorrow(true);
        RedisClient redisClient = RedisClient.create(redisUri);
        instance = ConnectionPoolSupport.createGenericObjectPool(redisClient::connect, poolConfig);
    }

    private static GenericObjectPool<StatefulRedisConnection<String, String>> getPoolInstance()
    {
        return instance;
    }

    /**
     * statefull redis connection.
     *
     * @return
     */
    public static StatefulRedisConnection<String, String> getConnection() throws BaseException
    {
        try
        {
            return getPoolInstance().borrowObject();
        }
        catch (Exception e)
        {
            LOGGER.error("can not get redis connection.");
            throw new BaseException("can not get redis connection.");
        }
    }
}
