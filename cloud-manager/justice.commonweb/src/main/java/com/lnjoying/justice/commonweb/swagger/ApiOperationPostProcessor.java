package com.lnjoying.justice.commonweb.swagger;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.util.BaseReaderUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.servicecomb.swagger.SwaggerUtils;
import org.apache.servicecomb.swagger.generator.OperationPostProcessor;
import org.apache.servicecomb.swagger.generator.core.AbstractOperationGenerator;
import org.apache.servicecomb.swagger.generator.core.AbstractSwaggerGenerator;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

import static com.lnjoying.justice.commonweb.common.SwaggerConstants.SWAGGER_RESOURCE_MODEL;
import static com.lnjoying.justice.commonweb.common.SwaggerConstants.SWAGGER_X_RESOURCE;

/**
 * @Description: For issues where fix @ApiOperation does not work on class
 * @Author: Merak
 * @Date: 2023/2/20 10:43
 */

public class ApiOperationPostProcessor implements OperationPostProcessor
{

    @Override
    public boolean shouldProcess(AbstractSwaggerGenerator swaggerGenerator, AbstractOperationGenerator operationGenerator)
    {
        return true;
    }

    @Override
    public void process(AbstractSwaggerGenerator swaggerGenerator, AbstractOperationGenerator operationGenerator)
    {
        ApiModelScanner apiModelScanner = new ApiModelScanner();
        Set<Class<?>> types = apiModelScanner.findTypes();

        ApiOperation annotation = swaggerGenerator.getClazz().getAnnotation(ApiOperation.class);
        if (Objects.nonNull(annotation) && ArrayUtils.isNotEmpty(annotation.extensions()))
        {
            Map<String, Object> extensions = BaseReaderUtils.parseExtensions(annotation.extensions());
            // todo The vendor extensions was not used during the display.
            Map<String, Object> vendorExtensions = swaggerGenerator.getSwagger().getVendorExtensions();
            if (CollectionUtils.isEmpty(vendorExtensions))
            {
               swaggerGenerator.getSwagger().setVendorExtensions(extensions);
            }
            else
            {
                vendorExtensions.putAll(extensions);
            }

            operationGenerator.getSwaggerOperation().getVendorExtensions().putAll(extensions);
            Set<String> xResourceSet = extensions.keySet().stream().filter(extension -> extension.startsWith(SWAGGER_X_RESOURCE)).collect(Collectors.toSet());
            if (!CollectionUtils.isEmpty(xResourceSet))
            {
                xResourceSet.forEach(xResource -> {
                    HashMap<String, String> map = (HashMap<String, String>) extensions.get(xResource);

                    if (CollectionUtils.isEmpty(types))
                    {
                        return;
                    }
                    if (!CollectionUtils.isEmpty(map))
                    {
                        String model = map.get(SWAGGER_RESOURCE_MODEL);
                        if (StringUtils.isNotBlank(model))
                        {
                            String[] modelArray = StringUtils.split(model, ",");
                            Arrays.stream(modelArray).forEach(m -> {
                                types.stream().forEach(type -> {
                                    ApiModel apiModel = type.getAnnotation(ApiModel.class);
                                    if (Objects.nonNull(apiModel))
                                    {
                                        if (apiModel.value().equalsIgnoreCase(StringUtils.trim(m)))
                                        {
                                            SwaggerUtils.addDefinitions(swaggerGenerator.getSwagger(), type);
                                        }
                                    }
                                });
                            });
                        }
                    }
                });
            }
        }

    }
}
