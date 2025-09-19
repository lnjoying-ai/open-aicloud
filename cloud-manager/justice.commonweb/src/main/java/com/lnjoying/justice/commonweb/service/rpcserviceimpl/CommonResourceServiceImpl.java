package com.lnjoying.justice.commonweb.service.rpcserviceimpl;

import com.lnjoying.justice.commonweb.swagger.ScbSchemaUtils;
import com.lnjoying.justice.commonweb.mapper.CommonResourceCache;
import com.lnjoying.justice.schema.service.iam.CommonResourceService;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.servicecomb.provider.pojo.RpcSchema;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

import static com.lnjoying.justice.schema.service.iam.CommonResourceService.DEFAULT_SCHEMA_ID;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/3/23 19:51
 */

@RpcSchema(schemaId = DEFAULT_SCHEMA_ID)
@Slf4j
public class CommonResourceServiceImpl implements CommonResourceService
{

    @Autowired
    private CommonResourceCache commonResourceCache;

    @Override
    public Map<String, Object> commonResource(@ApiParam(name = "resourceName") String resourceName, @ApiParam(name = "resourceInfo")  Map<String, String> resourceInfo)
    {

        return commonResourceCache.getResourceMap(resourceName, resourceInfo.values().toArray());
    }

    @Override
    public Map<String, String> getSchemaMap()
    {
        return ScbSchemaUtils.getSchemaMap();
    }
}
