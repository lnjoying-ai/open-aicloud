package com.micro.core.persistence.redis;

import com.micro.core.config.RedisConfig;
import com.micro.core.exception.BaseException;
import io.lettuce.core.Range;
import io.lettuce.core.ScriptOutputType;
import io.lettuce.core.SetArgs;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Set;

public class RedisUtil
{
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisUtil.class);

    private RedisUtil()
    {
    }

    public static void init(RedisConfig redisConfig) throws InterruptedException
    {
        RedisPoolUtil.init(redisConfig);
    }

    public static void set(String key, String value)
    {
        try (StatefulRedisConnection<String, String> connection = RedisPoolUtil.getConnection())
        {
            RedisCommands<String, String> commands = connection.sync();
            commands.set(key, value);
        }
        catch (BaseException e)
        {
            LOGGER.error("failed to connect redis.");
        }
    }

    public static void set(String mkey, String skey, String value)
    {
        try (StatefulRedisConnection<String, String> connection = RedisPoolUtil.getConnection())
        {
            RedisCommands<String, String> commands = connection.sync();
            commands.set(mkey + skey, value);
        }
        catch (BaseException e)
        {
            LOGGER.error("failed to connect redis.");
        }
    }

    public static void set(String key, String value, int extime)
    {
        try (StatefulRedisConnection<String, String> connection = RedisPoolUtil.getConnection())
        {
            RedisCommands<String, String> commands = connection.sync();
            commands.set(key, value);
            commands.expire(key, extime);
        }
        catch (BaseException e)
        {
            LOGGER.error("failed to connect redis.");
        }
    }

    public static void set(String mkey, String skey, String value, int extime)
    {
        try (StatefulRedisConnection<String, String> connection = RedisPoolUtil.getConnection())
        {
            RedisCommands<String, String> commands = connection.sync();
            commands.set(mkey + skey, value);
            commands.expire(mkey + skey, extime);
        }
        catch (BaseException e)
        {
            LOGGER.error("failed to connect redis.");
        }
    }

    public static void oset(String mkey, String skey, Object value)
    {
        try (StatefulRedisConnection<String, String> connection = RedisPoolUtil.getConnection())
        {
            RedisCommands<String, String> commands = connection.sync();
            commands.set(mkey + skey,SerializeUtil.serializeStr(value));
        }
        catch (BaseException e)
        {
            LOGGER.error("failed to connect redis.");
        }
    }

    public static void oset(String mkey, String skey, Object value, int extime)
    {
        try (StatefulRedisConnection<String, String> connection = RedisPoolUtil.getConnection())
        {
            RedisCommands<String, String> commands = connection.sync();
            commands.set(mkey + skey,SerializeUtil.serializeStr(value));
            commands.expire(mkey + skey, extime);
        }
        catch (BaseException e)
        {
            LOGGER.error("failed to connect redis.");
        }
    }


    public static  Object oget(String mkey, String skey)
    {
        try (StatefulRedisConnection<String, String> connection = RedisPoolUtil.getConnection())
        {
            RedisCommands<String, String> commands = connection.sync();
            String value = commands.get(mkey + skey);
            return SerializeUtil.unserializeStr(value);
        }
        catch (BaseException e)
        {
            LOGGER.error("failed to connect redis.");
            return null;
        }
    }

    public static void delete(String... key)
    {
        try (StatefulRedisConnection<String, String> connection = RedisPoolUtil.getConnection())
        {
            connection.sync().del( key);
        }
        catch (BaseException e)
        {
            LOGGER.error("failed to connect redis.");
        }
    }

    public static void odel(String mkey, String skey)
    {
        String key = mkey + skey;
        delete(key);
    }

    public static void hset(String mkey, String skey, String value)
    {
        try (StatefulRedisConnection<String, String> connection = RedisPoolUtil.getConnection())
        {
            RedisHashCommands<String, String> commands = connection.sync();
            commands.hset(mkey,skey,value);
        }
        catch (BaseException e)
        {
            LOGGER.error("failed to connect redis.");
        }
    }


    public static void hdel(String mkey, String ...skey)
    {
        try (StatefulRedisConnection<String, String> connection = RedisPoolUtil.getConnection())
        {
            RedisHashCommands<String, String> commands = connection.sync();
            commands.hdel(mkey,skey);
        }
        catch (BaseException e)
        {
            LOGGER.error("failed to connect redis.");
        }
    }

    public static String hget(String mkey, String skey)
    {
        try (StatefulRedisConnection<String, String> connection = RedisPoolUtil.getConnection())
        {
            RedisHashCommands<String, String> commands = connection.sync();
            return commands.hget(mkey,skey);
        }
        catch (BaseException e)
        {
            LOGGER.error("failed to connect redis.");
            return null;
        }
    }

    public static void delete (List<String> keys)
    {
        for (String key : keys)
        {
            delete(key);
        }
    }

    public static Long llen(String mkey, String skey)
    {
        try (StatefulRedisConnection<String, String> connection = RedisPoolUtil.getConnection())
        {
            RedisListCommands<String, String> commands = connection.sync();
            return commands.llen(mkey+skey);
        }
        catch (BaseException e)
        {
            LOGGER.error("failed to connect redis.");
        }
        return 0L;
    }

    public static List<String> lrange(String mkey, String skey, int start, int end)
    {
        try (StatefulRedisConnection<String, String> connection = RedisPoolUtil.getConnection())
        {
            RedisListCommands<String, String> commands = connection.sync();

            return commands.lrange(mkey+skey, start, end);
        }
        catch (BaseException e)
        {
            LOGGER.error("failed to connect redis.");
            return null;
        }
    }

    public static List<String> lrangeAll(String mkey, String skey)
    {
        try (StatefulRedisConnection<String, String> connection = RedisPoolUtil.getConnection())
        {
            RedisListCommands<String, String> commands = connection.sync();
            Long al = llen(mkey, skey);
            if (al == null || al < 1)
            {
                return null;
            }

           return commands.lrange(mkey+skey, 0L, al);
        }
        catch (BaseException e)
        {
            LOGGER.error("failed to connect redis.");
            return null;
        }
    }
    
    public static long lrem(String mkey, String skey, String value)
    {
        try (StatefulRedisConnection<String, String> connection = RedisPoolUtil.getConnection())
        {
            RedisListCommands<String, String> commands = connection.sync();
            
            return commands.lrem(mkey+skey, 0L, value);
        }
        catch (BaseException e)
        {
            LOGGER.error("failed to connect redis.");
            return 0;
        }
    }

    public static long lpush(String mkey, String skey, String value)
    {
        try (StatefulRedisConnection<String, String> connection = RedisPoolUtil.getConnection())
        {
            RedisListCommands<String, String> commands = connection.sync();
            List<String> values = lrangeAll(mkey, skey);
            if (values != null && ! values.isEmpty())
            {
                for (String v : values)
                {
                    if (v.equals(value))
                    {
                        LOGGER.info("{}{} !!!!!have value:{}", mkey, skey, value);
                        return values.size();
                    }
                }
            }
            return  commands.lpush(mkey + skey, value);
        }
        catch (BaseException e)
        {
            LOGGER.error("failed to connect redis.");
            return -1;
        }
    }

    public static Long scard(String mkey, String skey)
    {
        return scard(mkey+skey);
    }

    public static Long scard(String key)
    {
        try (StatefulRedisConnection<String, String> connection = RedisPoolUtil.getConnection())
        {
            RedisSetCommands<String, String> commands = connection.sync();
            return commands.scard(key);
        }
        catch (BaseException e)
        {
            LOGGER.error("failed to connect redis.");
            return null;
        }
    }

    public static long sadd(String mkey, String skey, String ... value)
    {
        try (StatefulRedisConnection<String, String> connection = RedisPoolUtil.getConnection())
        {
            RedisSetCommands<String, String> commands = connection.sync();
            return commands.sadd(mkey + skey, value);
        }
        catch (BaseException e)
        {
            LOGGER.error("failed to connect redis.");
            return -1;
        }
    }

    public static boolean sismember(String mkey, String skey, String value)
    {
        LOGGER.info("judge setmember key: {}{} value: {}", mkey, skey, value);
        try (StatefulRedisConnection<String, String> connection = RedisPoolUtil.getConnection())
        {
            RedisSetCommands<String, String> commands = connection.sync();
            return commands.sismember(mkey+skey, value);
        }
        catch (BaseException e)
        {
            LOGGER.error("failed to connect redis.");
            return false;
        }
    }

    public static long srem(String mkey, String skey, String value)
    {
        LOGGER.info("judge setmember key: {}{} value: {}", mkey, skey, value);
        try (StatefulRedisConnection<String, String> connection = RedisPoolUtil.getConnection())
        {
            RedisSetCommands<String, String> commands = connection.sync();
            return commands.srem(mkey+skey, value);
        }
        catch (BaseException e)
        {
            LOGGER.error("failed to connect redis.");
            return -1;
        }
    }


    public static Set<String> smembers(String mkey)
    {
        try (StatefulRedisConnection<String, String> connection = RedisPoolUtil.getConnection())
        {
            RedisSetCommands<String, String> commands = connection.sync();
            return commands.smembers(mkey);
        }
        catch (BaseException e)
        {
            LOGGER.error("failed to connect redis.");
            return null;
        }
    }

    public static Set<String> smembers(String mkey, String skey)
    {
        try (StatefulRedisConnection<String, String> connection = RedisPoolUtil.getConnection())
        {
            RedisSetCommands<String, String> commands = connection.sync();
            return commands.smembers(mkey+skey);
        }
        catch (BaseException e)
        {
            LOGGER.error("failed to connect redis.");
            return null;
        }
    }

    public static Long sdiffstore(String key, String ... key1)
    {
        try (StatefulRedisConnection<String, String> connection = RedisPoolUtil.getConnection())
        {
            RedisSetCommands<String, String> commands = connection.sync();
            return commands.sdiffstore(key, key1);
        }
        catch (BaseException e)
        {
            LOGGER.error("failed to connect redis.");
            return -1L;
        }
    }

    public static Set<String> sdiff(String ... key)
    {
        try (StatefulRedisConnection<String, String> connection = RedisPoolUtil.getConnection())
        {
            RedisSetCommands<String, String> commands = connection.sync();
            return commands.sdiff(key);
        }
        catch (BaseException e)
        {
            LOGGER.error("failed to connect redis.");
            return null;
        }
    }


    public static long lpushMany(String mkey, String skey, String ...value)
    {
        try (StatefulRedisConnection<String, String> connection = RedisPoolUtil.getConnection())
        {
            RedisListCommands<String, String> commands = connection.sync();
            return commands.lpush(mkey + skey, value);
        }
        catch (BaseException e)
        {
            LOGGER.error("failed to connect redis.");
            return -1;
        }
    }

    public static String lpop(String mkey, String skey)
    {

        try (StatefulRedisConnection<String, String> connection = RedisPoolUtil.getConnection())
        {
            RedisListCommands<String, String> commands = connection.sync();
            return commands.lpop(mkey + skey);
        }
        catch (BaseException e)
        {
            LOGGER.error("failed to connect redis.");
            return null;
        }
    }

    public static String spop(String mkey, String skey)
    {
        try (StatefulRedisConnection<String, String> connection = RedisPoolUtil.getConnection())
        {
            RedisSetCommands<String, String> commands = connection.sync();

            return commands.spop(mkey + skey);
        }
        catch (BaseException e)
        {
            LOGGER.error("failed to connect redis.");
            return null;
        }
    }

    public static String spop(String key)
    {
        try (StatefulRedisConnection<String, String> connection = RedisPoolUtil.getConnection())
        {
            RedisSetCommands<String, String> commands = connection.sync();

            return commands.spop(key);
        }
        catch (BaseException e)
        {
            LOGGER.error("failed to connect redis.");
            return null;
        }
    }


    public static List<String> keys(String master)
    {
        try (StatefulRedisConnection<String, String> connection = RedisPoolUtil.getConnection())
        {
            RedisCommands<String, String> commands = connection.sync();
            return commands.keys(master);
        }
        catch (BaseException e)
        {
            LOGGER.error("failed to connect redis.");
            return null;
        }
    }

    public static String get(String mkey,String skey)
    {
        String key = mkey + skey;
        return get(key);
    }

    public static String get(String key)
    {
        try (StatefulRedisConnection<String, String> connection = RedisPoolUtil.getConnection())
        {
            RedisCommands<String, String> commands = connection.sync();
            return commands.get(key);
        }
        catch (BaseException e)
        {
            LOGGER.error("failed to connect redis.");
            return null;
        }
    }

    public static Long exists(String key)
    {
        try (StatefulRedisConnection<String, String> connection = RedisPoolUtil.getConnection())
        {
            RedisCommands<String, String> commands = connection.sync();
            return commands.exists(key);
        }
        catch (BaseException e)
        {
            LOGGER.error("failed to connect redis.");
            return null;
        }
    }

    public static Long sunionstore(String key, String ... key1)
    {
        try (StatefulRedisConnection<String, String> connection = RedisPoolUtil.getConnection())
        {
            RedisSetCommands<String, String> commands = connection.sync();
            return commands.sunionstore(key, key1);
        }
        catch (BaseException e)
        {
            LOGGER.error("failed to connect redis.");
            return -1L;
        }
    }

    public static Set<String> sunion(String ... key)
    {
        try (StatefulRedisConnection<String, String> connection = RedisPoolUtil.getConnection())
        {
            RedisSetCommands<String, String> commands = connection.sync();
            return commands.sunion(key);
        }
        catch (BaseException e)
        {
            LOGGER.error("failed to connect redis.");
            return null;
        }
    }

    public static Long sinterstore(String key, String ... key1)
    {
        try (StatefulRedisConnection<String, String> connection = RedisPoolUtil.getConnection())
        {
            RedisSetCommands<String, String> commands = connection.sync();
            return commands.sinterstore(key, key1);
        }
        catch (BaseException e)
        {
            LOGGER.error("failed to connect redis.");
            return -1L;
        }
    }

    public static Set<String> sinter(String ... key)
    {
        try (StatefulRedisConnection<String, String> connection = RedisPoolUtil.getConnection())
        {
            RedisSetCommands<String, String> commands = connection.sync();
            return commands.sinter(key);
        }
        catch (BaseException e)
        {
            LOGGER.error("failed to connect redis.");
            return null;
        }
    }

    public static Long zadd(String key, String value, double score)
    {
        try (StatefulRedisConnection<String, String> connection = RedisPoolUtil.getConnection())
        {
            RedisSortedSetCommands<String, String> commands = connection.sync();
            return commands.zadd(key, score, value);
        }
        catch (BaseException e)
        {
            LOGGER.error("failed to connect redis.");
            return null;
        }
    }

    public static Long zrem(String key, String... values)
    {
        try (StatefulRedisConnection<String, String> connection = RedisPoolUtil.getConnection())
        {
            RedisSortedSetCommands<String, String> commands = connection.sync();
            return commands.zrem(key, values);
        }
        catch (BaseException e)
        {
            LOGGER.error("failed to connect redis.");
            return null;
        }
    }

    public static Double zscore(String key, String value)
    {
        try (StatefulRedisConnection<String, String> connection = RedisPoolUtil.getConnection())
        {
            RedisSortedSetCommands<String, String> commands = connection.sync();
            return commands.zscore(key, value);
        }
        catch (BaseException e)
        {
            LOGGER.error("failed to connect redis.");
            return null;
        }
    }

    public static List<String> zrangebyscore(String key, double minScore, double maxScore)
    {
        try (StatefulRedisConnection<String, String> connection = RedisPoolUtil.getConnection())
        {
            RedisSortedSetCommands<String, String> commands = connection.sync();
            return commands.zrangebyscore(key, Range.create(minScore, maxScore));
        }
        catch (BaseException e)
        {
            LOGGER.error("failed to connect redis.");
            return null;
        }
    }

    public static Double zincrby(String key, double score, String value)
    {
        try (StatefulRedisConnection<String, String> connection = RedisPoolUtil.getConnection())
        {
            RedisSortedSetCommands<String, String> commands = connection.sync();
            return commands.zincrby(key, score, value);
        }
        catch (BaseException e)
        {
            LOGGER.error("failed to connect redis.");
            return null;
        }
    }

    public static Long incrByWithTtl(String key, long amount, long seconds)
    {
        try (StatefulRedisConnection<String, String> connection = RedisPoolUtil.getConnection())
        {
            RedisCommands<String, String> command = connection.sync();
            Long result = command.incrby(key, amount);
            command.expire(key, seconds);
            return result;
        } catch (BaseException e)
        {
            LOGGER.error("failed to connect redis.");
            return null;
        }
    }

    public static Long zcard(String key)
    {
        try (StatefulRedisConnection<String, String> connection = RedisPoolUtil.getConnection())
        {
            RedisSortedSetCommands<String, String> commands = connection.sync();
            return commands.zcard(key);
        }
        catch (BaseException e)
        {
            LOGGER.error("failed to connect redis.");
            return null;
        }
    }

    public static Long ttl(String key)
    {
        try (StatefulRedisConnection<String, String> connection = RedisPoolUtil.getConnection())
        {
            RedisCommands<String, String> commands = connection.sync();
            return commands.ttl(key);
        }
        catch (BaseException e)
        {
            LOGGER.error("failed to connect redis.");
            return null;
        }
    }

    public static Boolean expire(String key, long seconds)
    {
        try (StatefulRedisConnection<String, String> connection = RedisPoolUtil.getConnection())
        {
            RedisCommands<String, String> commands = connection.sync();
            return commands.expire(key, seconds);
        }
        catch (BaseException e)
        {
            LOGGER.error("failed to connect redis.");
            return null;
        }
    }

    public static Boolean setIfPresent(String key, String value)
    {
        try (StatefulRedisConnection<String, String> connection = RedisPoolUtil.getConnection())
        {
            RedisCommands<String, String> commands = connection.sync();
            String res = commands.set(key, value);
            return ObjectUtils.nullSafeEquals("OK", res);
        }
        catch (BaseException e)
        {
            LOGGER.error("failed to connect redis.");
        }

        return false;
    }

    public static void execute(String script, ScriptOutputType type, String[] key, String... value)
    {
        try (StatefulRedisConnection<String, String> connection = RedisPoolUtil.getConnection())
        {
            RedisCommands<String, String> commands = connection.sync();
            commands.eval(script, type, key, value);
        }
        catch (BaseException e)
        {
            LOGGER.error("failed to connect redis.");
        }

    }

    public static boolean tryLock(String key, String value, long seconds)
    {
        boolean lockStatus = false;
        try (StatefulRedisConnection<String, String> connection = RedisPoolUtil.getConnection())
        {
            RedisCommands<String, String> syncCommand = connection.sync();
            SetArgs setArgs = SetArgs.Builder.nx().ex(seconds);
            int waitForNextTryMilliseconds = 100;//100ms
            final int MAX_RETRIES = 3;
            for (int i = 0; i < MAX_RETRIES; i++)
            {
                if (syncCommand.set(key, value, setArgs) != null)
                {
                    lockStatus = true;
                    break;
                }

                try
                {
                    Thread.sleep(waitForNextTryMilliseconds);
                    waitForNextTryMilliseconds *= 2;
                } catch (InterruptedException e)
                {
                }
            }

            return lockStatus;
        } catch (BaseException e)
        {
            LOGGER.error("failed to connect redis.");
            return false;
        }
    }

    public static void unLock(String key, String expectedValue)
    {
        try (StatefulRedisConnection<String, String> connection = RedisPoolUtil.getConnection())
        {
            RedisCommands<String, String> syncCommand = connection.sync();
            String atomicDelScript = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
            syncCommand.eval(atomicDelScript, ScriptOutputType.INTEGER, new String[]{key}, expectedValue);
        } catch (BaseException e)
        {
            LOGGER.error("failed to connect redis.");
        }
    }
}
