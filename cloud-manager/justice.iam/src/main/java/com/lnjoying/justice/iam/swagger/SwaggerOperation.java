package com.lnjoying.justice.iam.swagger;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import io.swagger.models.HttpMethod;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.Swagger;
import io.swagger.util.Yaml;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.servicecomb.swagger.SwaggerUtils;
import org.springframework.web.bind.annotation.RequestMethod.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static io.swagger.models.HttpMethod.*;


/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/3/13 16:18
 */

@Slf4j
public class SwaggerOperation
{

    /**
     * Extract the operation in the swaggeContext and convert it to action.
     * @param serviceName service iam code
     * @param swaggerContext
     * @return
     */
    public Map<String, String> getOperationApis(String serviceName, String swaggerContext)
    {

        Map<String, String> operationApis = new HashMap<>();
        Swagger swagger = SwaggerUtils.parseSwagger(swaggerContext);
        Swagger clone = SwaggerUtils.parseSwagger(swaggerContext);

        clone.getPaths().forEach((k, v) -> {
            v.setGet(null);
            v.setPut(null);
            v.setPost(null);
            v.setDelete(null);
            v.patch(null);
            v.head(null);
            v.options(null);
        });


        swagger.getPaths().forEach((k, v) -> {
            //String operationId = v.getGet().getOperationId();
            clone.getPaths().entrySet().stream().filter(entry -> entry.getKey().equals(k)).findFirst().ifPresent(entry ->
            {
                getOperationMap(v).forEach((method, operation) ->
                {
                    if (Objects.nonNull(operation))
                    {
                        try
                        {
                            String fullOperationId = StringUtils.join(Lists.newArrayList(serviceName, operation.getOperationId()), ":");
                            operation.setOperationId(fullOperationId);
                            entry.getValue().set(method.name().toLowerCase(), operation);
                            String operationYaml =  Yaml.mapper().writeValueAsString(clone);
                            operationApis.put(fullOperationId, operationYaml);
                            entry.getValue().set(method.name().toLowerCase(), null);
                        }
                        catch (JsonProcessingException e)
                        {
                            log.error("parse swagger yaml failed:{}", e);
                        }

                    }
                });
            });
        });

        return operationApis;
    }


    public static Map<HttpMethod, Operation> getOperationMap(Path pathItem)
    {
        // 	GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE
        Map<HttpMethod, Operation> operationMap = new HashMap<>();
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
}
