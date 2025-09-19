package com.lnjoying.justice.schema.service.servicemanager;

import com.lnjoying.justice.schema.common.CombRetPacket;
import com.lnjoying.justice.schema.entity.servicemanager.RpcPortMappingResult;
import io.swagger.annotations.ApiParam;

public interface ServicePortCallback
{
    CombRetPacket addServicePortCallback(@ApiParam(name = "portMappingResult") RpcPortMappingResult portMappingResult);
}
