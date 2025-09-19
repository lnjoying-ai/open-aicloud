package com.lnjoying.justice.iam.authz.opa.pep.pathMatcher;

import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.parameters.Parameter;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.util.UrlPathHelper;

import java.util.*;
import java.util.stream.Collectors;

import static com.lnjoying.justice.iam.authz.opa.pep.pathMatcher.OperationParamsUtils.buildOperationParams;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/2/28 15:34
 */

public class RequestMappingInfoUtils
{
    private static  RequestMappingInfo.BuilderConfiguration config;

    public static final String PATH_ATTRIBUTE = UrlPathHelper.class.getName() + ".PATH";

    public  final static Set<String> EMPTY_PATH_PATTERN = Collections.singleton("");

    static {
        config = new RequestMappingInfo.BuilderConfiguration();
        //config.setPatternParser(new PathPatternParser());
    }

    public static Map<RequestMethod, Operation> getOperationMap(Path pathItem)
    {
        // 	GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE
        Map<RequestMethod, Operation> operationMap = new HashMap<>();
        operationMap.put(GET, pathItem.getGet());
        operationMap.put(HEAD, pathItem.getHead());
        operationMap.put(POST, pathItem.getPost());
        operationMap.put(PUT, pathItem.getPut());
        operationMap.put(PATCH, pathItem.getPatch());
        operationMap.put(DELETE, pathItem.getDelete());
        operationMap.put(OPTIONS, pathItem.getOptions());
        operationMap.entrySet().removeIf(entry -> Objects.isNull(entry.getValue()));
        return operationMap;
    }

    public static Map<RequestMethod, Operation> getGetOperationMap(Path pathItem)
    {
        // 	GET
        Map<RequestMethod, Operation> operationMap = new HashMap<>();
        operationMap.put(GET, pathItem.getGet());
        operationMap.entrySet().removeIf(entry -> Objects.isNull(entry.getValue()));
        return operationMap;
    }


    public static List<RequestMappingInfo> doMappingBuilder(String path, Path pathItem)
    {
        Map<RequestMethod, Operation> operationMap = getOperationMap(pathItem);
        List<RequestMappingInfo> requestMappingInfoList = operationMap.entrySet().stream().map(entry ->
        {
            // todo fill header params consumers produces
            Operation operation = entry.getValue();
            RequestMappingInfo.Builder builder = RequestMappingInfo
                    .paths(path)
                    .methods(entry.getKey())
                    //.params(buildOperationParams(operation))
                    .params(new String[0])
                    .headers(new String[0])
                    .consumes(null)
                    .produces(null)
                    .mappingName(operation.getOperationId());
            return builder.options(config).build();
        }).collect(Collectors.toList());

        return requestMappingInfoList;
    }

    public static Map<String, Operation> doGetOperationBuilder(String path, Path pathItem)
    {
        Map<String, Operation> result = new HashMap<>();
        Map<RequestMethod, Operation> operationMap = getGetOperationMap(pathItem);
        operationMap.entrySet().stream().forEach(entry ->
        {
            List<Parameter> parameters = entry.getValue().getParameters();
            if (!CollectionUtils.isEmpty(parameters))
            {
                parameters.stream().forEach(parameter -> {
                   if(parameter.getIn().equalsIgnoreCase("path"))
                   {
                       result.put(entry.getValue().getOperationId(), entry.getValue());
                   }
                });
            }
        });

        return result;
    }


}
