package com.lnjoying.justice.schema.service.iam;

import io.swagger.annotations.ApiParam;

import java.util.Map;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/3/23 19:49
 */

public interface CommonResourceService
{
    String DEFAULT_SCHEMA_ID = "commonResourceService";

    Map<String, Object> commonResource(@ApiParam(name = "resourceName") String resourceName, @ApiParam(name = "resourceInfo")  Map<String, String> resourceInfo);


    Map<String, String> getSchemaMap();
}
