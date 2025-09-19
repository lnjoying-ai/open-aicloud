package com.lnjoying.justice.commonweb.mapper;

import com.lnjoying.justice.commonweb.util.BeanMapTool;
import com.lnjoying.justice.commonweb.util.FileUtils;
import com.micro.core.config.JdbcConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.ibatis.binding.MapperRegistry;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/4/6 20:35
 */

@Component()
@Slf4j
public class CommonResourceCache
{
    /**
     * key: resource name, value: mapper object
     */
    private Map<String, Object> resourceMap = new ConcurrentHashMap<>(16);

    /**
     * key: resource name, value: method for get resource info
     */
    private Map<String, Method> resourceMethodMap = new ConcurrentHashMap<>(16);

    @Autowired(required = false)
    private SqlSessionFactory sqlSessionFactory;


    @PostConstruct
    public void init()
    {
        if (sqlSessionFactory != null)
        {
            Configuration configuration = sqlSessionFactory.getConfiguration();
            SqlSession sqlSession = sqlSessionFactory.openSession();
            MapperRegistry mapperRegistry = configuration.getMapperRegistry();
            Collection<Class<?>> mappers = mapperRegistry.getMappers();
            mappers.forEach(mapper ->
            {
                if (mapper.isAnnotationPresent(MapperModel.class))
                {
                    MapperModel annotation = mapper.getAnnotation(MapperModel.class);
                    String resourceName = annotation.value();
                    if (StringUtils.isNotBlank(resourceName))
                    {
                        resourceMap.put(resourceName, sqlSession.getMapper(mapper));
                    }

                    String methodName = annotation.method();
                    if (StringUtils.isNotBlank(methodName))
                    {
                        Method[] declaredMethods = mapper.getDeclaredMethods();
                        Optional<Method> defaultMethod = Arrays.stream(declaredMethods).filter(method -> method.getName().equals(methodName) || method.getName().equals("DEFAULT_METHOD")).findFirst();
                        defaultMethod.ifPresent(method ->
                        {
                            resourceMethodMap.put(resourceName, method);
                        });
                    }
                }

            });

            log.info("init resource mapper cache finish");
        }

    }

    public Map<String, Object> getResourceMap(String resourceName, Object... args)
    {
        if (!CollectionUtils.isEmpty(resourceMethodMap) && !CollectionUtils.isEmpty(resourceMap))
        {
            Object mapper = resourceMap.get(resourceName);
            Method method = resourceMethodMap.get(resourceName);
            if (Objects.nonNull(mapper) && Objects.nonNull(method) && method.getParameterCount() == args.length)
            {
                try
                {
                    Object invoke = method.invoke(mapper, args);
                    return BeanMapTool.beanToMap(invoke);
                }
                catch (Exception e)
                {
                    log.error("get resource info,resourceName:{}, args:{}, error:{} ", e);
                }
            }
        }
        return Collections.emptyMap();
    }

    public Optional<Object> getResourceInstance(String resourceName, String methodName, Object... primaryKey)
    {
        if (CollectionUtils.isEmpty(resourceMethodMap) || CollectionUtils.isEmpty(resourceMap))
        {
            log.error("resourceMethodMap or resourceMap is empty, query resource instance failed!");
            return Optional.empty();
        }

        if (resourceName == null)
        {
            log.warn("resourceName is empty, query resource instance failed!");
            return Optional.empty();
        }

        Object resourceQueryMapper = resourceMap.get(resourceName);
        if (resourceQueryMapper == null)
        {
            log.error("There is no QueryMapper for '{}'", resourceName);
            return Optional.empty();
        }

        Method queryMethod = null;
        if (!org.springframework.util.StringUtils.hasText(methodName))
        {
            queryMethod = resourceMethodMap.get(resourceName);
        } else
        {
            try
            {
                if (primaryKey == null || primaryKey.length == 0)
                {
                    return Optional.empty();
                }

                List<? extends Class<?>> primaryKeyClassList = Arrays.stream(primaryKey).map(x -> x.getClass()).collect(Collectors.toList());
                Class<?>[] primaryKeyClassArray = primaryKeyClassList.toArray(new Class<?>[primaryKeyClassList.size()]);
                queryMethod = resourceQueryMapper.getClass().getDeclaredMethod(methodName, primaryKeyClassArray);
            } catch (NoSuchMethodException e)
            {

            }
        }
        if (queryMethod == null)
        {
            log.error("There is no expected query method found in QueryMapper for '{}' ", resourceName);
            return Optional.empty();
        }

        try
        {
            Object resourceInstance = queryMethod.invoke(resourceQueryMapper, primaryKey);
            return Optional.ofNullable(resourceInstance);
        } catch (Exception e)
        {
            log.error("get resource instance failed! resourceName:{}, primaryKey:{}, stackTrace:{}, errorMessage:{} ",
                    resourceName, primaryKey, ExceptionUtils.getStackTrace(e), e.getMessage());
        }

        return Optional.empty();
    }


}
