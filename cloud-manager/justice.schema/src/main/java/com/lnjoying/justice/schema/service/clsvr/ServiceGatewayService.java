package com.lnjoying.justice.schema.service.clsvr;

import com.lnjoying.justice.schema.entity.servicemanager.PortStatus;
import com.lnjoying.justice.schema.entity.servicemanager.RpcCreatePortReq;
import io.swagger.annotations.ApiParam;

import java.util.List;

public interface ServiceGatewayService
{
    int createPort(@ApiParam(name = "rpcCreatePortReq") RpcCreatePortReq rpcCreatePortReq);

    int releasePort(@ApiParam(name = "targetPortId")String targetPortId, @ApiParam(name = "internalIp")String internalIp, @ApiParam(name = "listenPort")Integer listenPort);

    List<PortStatus> getPortStatus();
}
