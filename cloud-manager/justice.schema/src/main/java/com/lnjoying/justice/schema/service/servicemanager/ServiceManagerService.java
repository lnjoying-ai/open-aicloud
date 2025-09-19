package com.lnjoying.justice.schema.service.servicemanager;

import com.lnjoying.justice.schema.common.CombRetPacket;
import com.lnjoying.justice.schema.entity.servicemanager.RegisterServiceGatewayRsp;
import com.lnjoying.justice.schema.entity.servicemanager.RpcAddServicePortReq;
import com.lnjoying.justice.schema.entity.servicemanager.RpcCreatePortReq;
import io.swagger.annotations.ApiParam;

public interface ServiceManagerService
{
    RegisterServiceGatewayRsp registerServiceGateway(@ApiParam(name = "endpoint")String endpoint, @ApiParam(name = "instanceId")String instanceId);

    CombRetPacket addServicePort(@ApiParam(name = "rpcAddServicePortReq") RpcAddServicePortReq rpcAddServicePortReq);

    CombRetPacket deleteServicePort(@ApiParam(name = "reqId")String reqId);

    RpcCreatePortReq getRpcCreatePortReq(@ApiParam(name = "targetServiceId")String targetServiceId);
}
