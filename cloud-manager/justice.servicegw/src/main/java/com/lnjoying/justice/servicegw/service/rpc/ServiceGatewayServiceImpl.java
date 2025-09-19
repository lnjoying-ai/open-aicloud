package com.lnjoying.justice.servicegw.service.rpc;

import com.lnjoying.justice.schema.entity.servicemanager.PortStatus;
import com.lnjoying.justice.schema.entity.servicemanager.RpcCreatePortReq;
import com.lnjoying.justice.schema.service.clsvr.ServiceGatewayService;
import com.lnjoying.justice.servicegw.service.ServicePortService;
import io.swagger.annotations.ApiParam;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.servicecomb.provider.pojo.RpcSchema;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@RpcSchema(schemaId = "serviceGateway")
public class ServiceGatewayServiceImpl implements ServiceGatewayService
{
    private static Logger LOGGER = LogManager.getLogger();
    
    @Autowired
    ServicePortService servicePortService;
    
    @Override
    public int createPort(@ApiParam(name = "rpcCreatePortReq") RpcCreatePortReq rpcCreatePortReq)
    {
        return servicePortService.createPort(rpcCreatePortReq);
    }

    @Override
    public int releasePort(@ApiParam(name = "targetPortId")String targetPortId, @ApiParam(name = "internalIp")String internalIp, @ApiParam(name = "listenPort")Integer listenPort)
    {
        return servicePortService.releasePort(targetPortId, internalIp, listenPort);
    }

    @Override
    public List<PortStatus> getPortStatus()
    {
        return servicePortService.getPortStatus();
    }
}
