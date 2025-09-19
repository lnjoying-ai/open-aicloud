package com.lnjoying.justice.schema.service.iam;

import com.lnjoying.justice.schema.entity.RpcResult;
import io.swagger.annotations.ApiParam;

import java.util.*;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/5/6 16:15
 */

public interface CommonResourceFeederService
{
    RpcResult resourceFeeder(@ApiParam(name = "iamCode") String iamCode, @ApiParam(name = "schemaMap")Map<String, String> schemaMap);

    RpcResult<HashMap<String /* serviceEnName:resourceEnName */, String /* serviceCnName:resourceCnName */>> getServiceResourceDisplayInfo(@ApiParam(name = "serviceResources") Set<String /* serviceEnName:resourceEnName */> serviceResources);

    public enum ResultCode
    {

        SUCCESS(0),

        SYSTEM_ERROR(9999),

        SERVICE_NOT_EXIST(9998),

        MISSING_PARAMETER(9997),

        SERVICE_NOT_UP(9996);

        private int code;

        ResultCode(int code)
        {
            this.code = code;
        }

        public int getCode()
        {
            return code;
        }

        }
}
