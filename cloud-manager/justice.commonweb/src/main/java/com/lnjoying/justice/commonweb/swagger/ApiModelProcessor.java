package com.lnjoying.justice.commonweb.swagger;

import io.swagger.annotations.ApiModel;
import org.apache.servicecomb.swagger.SwaggerUtils;
import org.apache.servicecomb.swagger.generator.ClassAnnotationProcessor;
import org.apache.servicecomb.swagger.generator.SwaggerGenerator;

import java.lang.reflect.Type;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/2/21 11:07
 */

public class ApiModelProcessor implements ClassAnnotationProcessor<ApiModel>
{
    @Override
    public Type getProcessType()
    {
        return ApiModel.class;
    }

    @Override
    public void process(SwaggerGenerator swaggerGenerator, ApiModel apiModel)
    {
        String value = apiModel.value();
        SwaggerUtils.addDefinitions(swaggerGenerator.getSwagger(), null);
    }
}
