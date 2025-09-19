package com.lnjoying.justice.iam.config.wx;

import com.micro.core.persistence.redis.RedisUtil;
import me.chanjar.weixin.common.redis.WxRedisOps;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/10/20 19:20
 */

public class LettuceWxRedisOps implements WxRedisOps
{

    @Override
    public String getValue(String key)
    {
        return RedisUtil.get(key);
    }

    @Override
    public void setValue(String key, String value, int expire, TimeUnit timeUnit)
    {
        if (expire < 0)
        {
            RedisUtil.set(key, value);
        }
        else
        {
            RedisUtil.set(key, value, (int)timeUnit.toSeconds(expire));
        }
    }

    @Override
    public Long getExpire(String key)
    {
        return RedisUtil.ttl(key);
    }

    @Override
    public void expire(String key, int expire, TimeUnit timeUnit)
    {
        RedisUtil.expire(key, timeUnit.toSeconds(expire));
    }

    @Override
    public Lock getLock(String key)
    {
        return new LettuceDistributedLock(key, 60 * 1000);
    }
}
