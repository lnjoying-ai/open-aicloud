package com.lnjoying.justice.cluster.manager.util.template;

import com.lnjoying.justice.cluster.manager.util.K8sRedisField;
import com.lnjoying.justice.commonweb.util.JsonUtils;
import com.micro.core.persistence.redis.RedisUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class RedisTemplateStorage
{
    private static Logger LOGGER = LogManager.getLogger();
    /**
     * @description  save template to redis
     * @author Regulus
     * @date 11/26/21 9:15 PM
     * @param name
     * @param content 
     * @return java.lang.String
     */
    public String saveTemplate(String name, String content)
    {
        String key = getRedisKey(name);
        LOGGER.info("save template name:{}, Key:{}", name, key);
        RedisUtil.set(key, content);
        updateTimestamp(name);
        return content;
    }
    
    /**
     * @description get template
     * @author Regulus
     * @date 11/26/21 9:16 PM
     * @param name 
     * @return java.lang.String
     */
    public String getTemplate(String name)
    {
        String key = getRedisKey(name);
        String result = RedisUtil.get(key);
        LOGGER.info("get template name:{}, Key:{}, value:{} ", name, key, result);
        return result;
    }
    
    /**
     * @description get all template name
     * @author Regulus
     * @date 11/26/21 9:16 PM
     * @param  
     * @return java.lang.String
     */
    public String getAllTemplateNames()
    {
        List<String> keys = RedisUtil.keys(K8sRedisField.ADDONFTL_KEY_PREFIX + "*");
        Set<String> trimmedKeys = new HashSet<String>();
        for (String k : keys)
        {
            trimmedKeys.add(k.substring(K8sRedisField.ADDONFTL_KEY_PREFIX.length()));
        }
        return JsonUtils.toJson(trimmedKeys);
    }
    
    /**
     * @description delete template by name
     * @author Regulus
     * @date 11/26/21 9:17 PM
     * @param name
     * @return java.lang.Boolean
     */
    public Boolean deleteTemplate(String name)
    {
        RedisUtil.delete(getRedisKey(name));
        RedisUtil.delete(getTimestampRedisKey(name));
        return true;
    }
    
    /**
     * @description get template time stamp
     * @author Regulus
     * @date 11/26/21 9:17 PM
     * @param name
     * @return java.lang.Long
     */
    public Long getTemplateTimestamp(String name)
    {
        String ts = RedisUtil.get(getTimestampRedisKey(name));
        return Long.parseLong(ts);
    }
    
    /**
     * @description update template time stamp
     * @author Regulus
     * @date 11/26/21 9:17 PM
     * @param name 
     * @return void
     */
    private void updateTimestamp(String name)
    {
        RedisUtil.set(getTimestampRedisKey(name), new Date().getTime() + "");
    }
    
    /**
     * @description delete template timestamp
     * @author Regulus
     * @date 11/26/21 9:18 PM
     * @param name 
     * @return void
     */
    private void deleteTimestamp(String name)
    {
        RedisUtil.delete(getTimestampRedisKey(name));
    }
    
    /**
     * @description get redis key by name
     * @author Regulus
     * @date 11/26/21 9:18 PM
     * @param name 
     * @return java.lang.String
     */
    public String getRedisKey(String name)
    {
        return K8sRedisField.ADDONFTL_KEY_PREFIX + name;
    }
    
    /**
     * @description get template timestamp redis key
     * @author Regulus
     * @date 11/26/21 9:18 PM
     * @param name 
     * @return java.lang.String
     */
    private String getTimestampRedisKey(String name)
    {
        return K8sRedisField.ADDONFTL_TS_PREFIX + name;
    }
}