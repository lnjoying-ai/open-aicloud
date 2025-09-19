package com.lnjoying.justice.commonweb.handler.aspect.rpcproxy;

import com.lnjoying.justice.commonweb.handler.aspect.annotation.ResourceInstanceNameFromRpcServiceConfig;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.servicecomb.provider.pojo.Invoker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * rpc服务代理，负责远程服务调用
 *
 * @author: Robin
 * @date: 2024年01月24日 16:49
 */
@Component("rpcServiceProxy")
public class RpcServiceProxy
{
    private ConcurrentHashMap<String, RpcServiceProxyEntry> rpcServiceMethodProxyMap = new ConcurrentHashMap<>();

    private Logger LOGGER = LoggerFactory.getLogger(RpcServiceProxy.class);

    public Optional<Object> invoke(ResourceInstanceNameFromRpcServiceConfig config, Object[] args)
    {
        try
        {
            if (config == null ||
                    config.microserviceName() == null ||
                    config.serviceSchemaId() == null ||
                    config.serviceInterfaceClass() == null ||
                    config.parameterClasses() == null)
            {
                return Optional.empty();
            }

            Optional<String> proxyServiceMethodId = registerProxyServiceMethod(config);
            if (!proxyServiceMethodId.isPresent())
            {
                return Optional.empty();
            }

            RpcServiceProxyEntry rpcServiceProxyEntry = rpcServiceMethodProxyMap.get(proxyServiceMethodId.get());
            Object result = rpcServiceProxyEntry.getProxyServiceMethod().invoke(rpcServiceProxyEntry.getProxyServiceInstance(), args);
            return Optional.ofNullable(result);
        } catch (Exception e)
        {
            List<String> parameterTypeList = Arrays.stream(config.parameterClasses()).map(x -> x.getSimpleName()).collect(Collectors.toList());
            String[] parameterTypeArray = parameterTypeList.toArray(new String[parameterTypeList.size()]);
            LOGGER.error("调用 {}.{}.{}({}) 失败! stackTrace:{}, errorMessage:{}, 参数: {}", config.microserviceName(), config.serviceSchemaId(), config.serviceMethodName(),
                    String.join(",", parameterTypeArray), ExceptionUtils.getStackTrace(e), e.getMessage(), args);
        }

        return Optional.empty();
    }

    private Optional<String> registerProxyServiceMethod(ResourceInstanceNameFromRpcServiceConfig config) throws NoSuchMethodException
    {
        String proxyServiceMethodId = String.format("%s.%s.%s", config.microserviceName(), config.serviceSchemaId(), config.serviceMethodName());
        if (!rpcServiceMethodProxyMap.containsKey(proxyServiceMethodId))
        {
            Object rpcServiceInstance = Invoker.createProxy(config.microserviceName(), config.serviceSchemaId(), config.serviceInterfaceClass());
            if (rpcServiceInstance != null)
            {
                Class<?> rpcServiceClass = rpcServiceInstance.getClass();

                Method targetMethod = rpcServiceClass.getDeclaredMethod(config.serviceMethodName(), config.parameterClasses());
                rpcServiceMethodProxyMap.putIfAbsent(proxyServiceMethodId, RpcServiceProxyEntry.builder()
                        .proxyServiceMethod(targetMethod)
                        .proxyServiceInstance(rpcServiceInstance)
                        .build());
            }
        }

        if (!rpcServiceMethodProxyMap.containsKey(proxyServiceMethodId))
        {
            return Optional.empty();
        }

        return Optional.of(proxyServiceMethodId);
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RpcServiceProxyEntry
    {
        private Method proxyServiceMethod;
        private Object proxyServiceInstance;
    }
}
