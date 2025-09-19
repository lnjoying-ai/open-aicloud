package com.lnjoying.justice.iam.authz.opa.pep.pathMatcher;

import io.swagger.models.Swagger;
import org.apache.servicecomb.swagger.SwaggerUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/2/28 20:04
 */


public class DefaultSwaggerLoader implements SwaggerLoader
{
    private List<Swagger> swaggerCache = new CopyOnWriteArrayList<>();

    @Override
    public Swagger loadSwagger(String swaggerContext)
    {
        Swagger swagger = SwaggerUtils.parseSwagger(swaggerContext);
        swaggerCache.add(swagger);
       return swagger;
    }

    @Override
    public List<Swagger> getSwaggers()
    {
        return swaggerCache;
    }


}
