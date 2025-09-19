package com.lnjoying.justice.iam.config.wx;

import com.micro.core.persistence.redis.RedisUtil;
import io.lettuce.core.ScriptOutputType;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/10/20 19:56
 */

public class LettuceDistributedLock implements Lock
{

    private static final String DEL_SCRIPT = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";

    @Getter
    private final String key;

    @Getter
    private final int leaseMilliseconds;

    private final ThreadLocal<String> valueThreadLocal = new ThreadLocal<>();

    public LettuceDistributedLock(@NotNull String key, int leaseMilliseconds)
    {
        this.key = "lock:" + UUID.randomUUID().toString();
        this.leaseMilliseconds = leaseMilliseconds;
    }

    @Override
    public void lock()
    {
        while (!tryLock()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // Ignore
            }
        }
    }

    @Override
    public void lockInterruptibly() throws InterruptedException
    {
        while (!tryLock()) {
            Thread.sleep(1000);
        }
    }

    @Override
    public boolean tryLock()
    {
        String value = valueThreadLocal.get();
        if (value == null || value.length() == 0) {
            value = UUID.randomUUID().toString();
            valueThreadLocal.set(value);
        }

        return RedisUtil.setIfPresent(key, value);
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException
    {
        long waitMs = unit.toMillis(time);
        boolean locked = tryLock();
        while (!locked && waitMs > 0) {
            long sleep = waitMs < 1000 ? waitMs : 1000;
            Thread.sleep(sleep);
            waitMs -= sleep;
            locked = tryLock();
        }
        return locked;
    }

    @Override
    public void unlock()
    {
        if (valueThreadLocal.get() != null) {
            RedisUtil.execute(DEL_SCRIPT, ScriptOutputType.INTEGER, new String[]{key}, valueThreadLocal.get());
            valueThreadLocal.remove();
        }
    }

    @Override
    public Condition newCondition()
    {
        throw new UnsupportedOperationException();
    }

    public String getLockSecretValue() {
        return valueThreadLocal.get();
    }
}
