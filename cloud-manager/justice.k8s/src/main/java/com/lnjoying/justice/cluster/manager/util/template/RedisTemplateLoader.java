package com.lnjoying.justice.cluster.manager.util.template;

import freemarker.cache.TemplateLoader;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Reader;
import java.io.StringReader;

@Component
public class RedisTemplateLoader implements TemplateLoader
{
    private static Logger LOGGER = LogManager.getLogger();
    
    @Autowired
    private RedisTemplateStorage redisTemplateStorage;
    
    @Override
    public Object findTemplateSource(String name)
    {
        try
        {
            if (name.endsWith("_"))
            {
                name = name.substring(0, name.length()-1);
            }
            String tpl = redisTemplateStorage.getTemplate(name);
//            LOGGER.info("Template name:[{}], content:[{}]", name, tpl);
            if (StringUtils.isEmpty(tpl))
            {
                LOGGER.info("can't find templlate for {}", name);
                return null;
            }
            Long ts = redisTemplateStorage.getTemplateTimestamp(name);
//            LOGGER.info("Template name:[{}], content:[{}], ts:[{}]", name, tpl, ts);
            return new StringTemplateSource(name, tpl, ts);
        }
        catch (Exception e)
        {
            LOGGER.error("Failed to get template {}, error:{}", name, e.getMessage());
            return null;
        }
    }
    
    @Override
    public long getLastModified(Object templateSource)
    {
        return ((StringTemplateSource) templateSource).lastModified;
    }
    
    @Override
    public Reader getReader(Object templateSource, String encoding)
    {
        return new StringReader(((StringTemplateSource) templateSource).source);
    }
    
    @Override
    public void closeTemplateSource(Object templateSource)
    {
        // do nothing
    }
    
    private static class StringTemplateSource
    {
        private final String name;
        private final String source;
        private final long lastModified;
        
        StringTemplateSource(String name, String source, long lastModified)
        {
            if (name == null)
            {
                throw new IllegalArgumentException("name == null");
            }
            
            if (source == null)
            {
                throw new IllegalArgumentException("source == null");
            }
            
            if (lastModified < -1L)
            {
                throw new IllegalArgumentException("lastModified < -1L");
            }
            
            this.name = name;
            this.source = source;
            this.lastModified = lastModified;
        }
        
        public boolean equals(Object obj)
        {
            if (obj instanceof StringTemplateSource)
            {
                return name.equals(((StringTemplateSource) obj).name);
            }
            
            return false;
        }
        
        public int hashCode()
        {
            return name.hashCode();
        }
    }
    
    @Override
    public String toString()
    {
        return "RedisTemplateLoader";
    }
}
