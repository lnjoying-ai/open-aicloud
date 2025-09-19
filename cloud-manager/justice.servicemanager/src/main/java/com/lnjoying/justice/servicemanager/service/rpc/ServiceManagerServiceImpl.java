package com.lnjoying.justice.servicemanager.service.rpc;

import com.google.gson.Gson;
import com.lnjoying.justice.commonweb.util.CollectionUtils;
import com.lnjoying.justice.schema.common.CombRetPacket;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.entity.servicemanager.RegisterServiceGatewayRsp;
import com.lnjoying.justice.schema.entity.servicemanager.RpcAddServicePortReq;
import com.lnjoying.justice.schema.entity.servicemanager.RpcCreatePortReq;
import com.lnjoying.justice.schema.msg.MessagePack;
import com.lnjoying.justice.schema.service.servicemanager.ServiceManagerService;
import com.lnjoying.justice.servicemanager.common.*;
import com.lnjoying.justice.servicemanager.db.model.*;
import com.lnjoying.justice.servicemanager.db.repo.ServiceGatewayRepository;
import com.lnjoying.justice.servicemanager.db.repo.ServicePortRepository;
import com.lnjoying.justice.servicemanager.service.ServiceGatewayService;
import com.lnjoying.justice.servicemanager.service.process.ServiceManagerMsgProcessStrategy;
import com.micro.core.common.Utils;
import io.swagger.annotations.ApiParam;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.servicecomb.provider.pojo.RpcSchema;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@RpcSchema(schemaId = "serviceManagerService")
public class ServiceManagerServiceImpl implements ServiceManagerService
{
    private static Logger LOGGER = LogManager.getLogger();
    
    @Autowired
    private ServiceGatewayRepository serviceGatewayRepository;

    @Autowired
    private ServicePortRepository servicePortRepository;

    @Autowired
    private ServiceGatewayService serviceGatewayService;

    @Autowired
    private ServiceManagerMsgProcessStrategy serviceManagerMsgProcessStrategy;

    private static Gson gson = new Gson();

    @Override
    public RegisterServiceGatewayRsp registerServiceGateway(@ApiParam(name = "endpoint") String endpoint, @ApiParam(name = "instanceId") String instanceId)
    {
        RegisterServiceGatewayRsp registerServiceGatewayRsp = new RegisterServiceGatewayRsp();
        try
        {
            TblServiceGatewayInfoExample example = new TblServiceGatewayInfoExample();
            TblServiceGatewayInfoExample.Criteria criteria = example.createCriteria();
            criteria.andEndpointEqualTo(endpoint);
            criteria.andStatusNotEqualTo(ServiceGatewayStatus.REMOVED.getCode());
            example.setOrderByClause("status asc");
            List<TblServiceGatewayInfo> tblServiceGatewayInfos = serviceGatewayRepository.getServiceGatewaysByExample(example);

            if (CollectionUtils.isEmpty(tblServiceGatewayInfos))
            {
                TblServiceGatewayInfo tblServiceGatewayInfo = new TblServiceGatewayInfo();
                tblServiceGatewayInfo.setId(Utils.buildStr("JST_SGW_", Utils.assignUUId()));
                tblServiceGatewayInfo.setEndpoint(endpoint);
                tblServiceGatewayInfo.setInstanceId(instanceId);
                tblServiceGatewayInfo.setCreateTime(new Date());
                tblServiceGatewayInfo.setUpdateTime(tblServiceGatewayInfo.getCreateTime());
                tblServiceGatewayInfo.setStatus(ServiceGatewayStatus.UP.getCode());

                serviceGatewayRepository.insertServiceGateway(tblServiceGatewayInfo);

                TblServiceGatewayPortInfo tblServiceGatewayPortInfo = new TblServiceGatewayPortInfo();
                tblServiceGatewayPortInfo.setId(Utils.buildStr("JST_SGW_PR_", Utils.assignUUId()));
                tblServiceGatewayPortInfo.setServiceGatewayId(tblServiceGatewayInfo.getId());
                tblServiceGatewayPortInfo.setPortRangeMin(50000);
                tblServiceGatewayPortInfo.setPortRangeMax(60000);
                tblServiceGatewayPortInfo.setListenPortRangeMin(50000);
                tblServiceGatewayPortInfo.setListenPortRangeMax(60000);
                String tmpEndpoint = endpoint.replace("rest://", "");
                tblServiceGatewayPortInfo.setInternalIp(tmpEndpoint.substring(0, tmpEndpoint.indexOf(":")));
                tblServiceGatewayPortInfo.setExternalIp(tblServiceGatewayPortInfo.getExternalIp());
                tblServiceGatewayPortInfo.setTotal(tblServiceGatewayPortInfo.getPortRangeMax() - tblServiceGatewayPortInfo.getPortRangeMin() + 1);
                tblServiceGatewayPortInfo.setStatus(CommonPhaseStatus.DEFAULT);
                tblServiceGatewayPortInfo.setCreateTime(new Date());
                tblServiceGatewayPortInfo.setUpdateTime(tblServiceGatewayPortInfo.getCreateTime());
                serviceGatewayRepository.insertServiceGatewayPort(tblServiceGatewayPortInfo);

                return null;
            }
            else
            {
                for (int i = 0; i < tblServiceGatewayInfos.size(); i++)
                {
                    TblServiceGatewayInfo tblServiceGatewayInfo = tblServiceGatewayInfos.get(i);
                    if (i == 0)
                    {
                        tblServiceGatewayInfo.setStatus(ServiceGatewayStatus.UP.getCode());
                    }
                    else
                    {
                        tblServiceGatewayInfo.setStatus(ServiceGatewayStatus.REMOVED.getCode());
                    }
                    tblServiceGatewayInfo.setInstanceId(instanceId);
                    tblServiceGatewayInfo.setUpdateTime(new Date());
                    serviceGatewayRepository.updateServiceGateway(tblServiceGatewayInfo);
                }

                List<TblServiceGatewayPortAllocation> tblServiceGatewayPortAllocations = serviceGatewayRepository.getServiceGatewayPortAllocationsByServiceGatewayId(tblServiceGatewayInfos.get(0).getId());
                if (CollectionUtils.isEmpty(tblServiceGatewayPortAllocations))
                {
                    return registerServiceGatewayRsp;
                }

                List<RpcCreatePortReq> rpcCreatePortReqs = new ArrayList<>();

                for (TblServiceGatewayPortAllocation tblServiceGatewayPortAllocation : tblServiceGatewayPortAllocations)
                {
                    if (tblServiceGatewayPortAllocation.getStatus() == PortAllocationStatus.RELEASING.getCode()) continue;
                    TblServicePortTargetServiceInfo tblServicePortTargetServiceInfo = servicePortRepository.getServicePortTargetService(tblServiceGatewayPortAllocation.getServicePortTargetServiceId());
                    if (tblServicePortTargetServiceInfo.getStatus() == TargetServiceStatus.REMOVED.getCode())
                    {
                        tblServiceGatewayPortAllocation.setStatus(PortAllocationStatus.RELEASED.getCode());
                        serviceGatewayRepository.updateServiceGatewayPortAllocation(tblServiceGatewayPortAllocation);
                        continue;
                    }
                    else if (tblServicePortTargetServiceInfo.getStatus() == TargetServiceStatus.REMOVING.getCode())
                    {
                        continue;
                    }
                    TblServicePortInfo tblServicePortInfo = servicePortRepository.getServicePort(tblServicePortTargetServiceInfo.getServicePortId());
                    if (tblServicePortInfo.getStatus() == ServicePortStatus.REMOVED.getCode())
                    {
                        tblServiceGatewayPortAllocation.setStatus(PortAllocationStatus.RELEASED.getCode());
                        serviceGatewayRepository.updateServiceGatewayPortAllocation(tblServiceGatewayPortAllocation);
                        continue;
                    }
                    else if (tblServicePortInfo.getStatus() == ServicePortStatus.REMOVING.getCode())
                    {
                        continue;
                    }
                    RpcCreatePortReq rpcCreatePortReq = serviceGatewayService.genRpcCreatePortReq(tblServicePortInfo, tblServicePortTargetServiceInfo, tblServiceGatewayPortAllocation);
                    rpcCreatePortReqs.add(rpcCreatePortReq);
                }
                registerServiceGatewayRsp.setPorts(rpcCreatePortReqs);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("register service gateway error, {}", e.getMessage());
        }

        return registerServiceGatewayRsp;
    }

    @Override
    public CombRetPacket addServicePort(@ApiParam(name = "rpcAddServicePortReq") RpcAddServicePortReq rpcAddServicePortReq)
    {
        CombRetPacket retPacket = new CombRetPacket();

        TblServicePortInfoExample example = new TblServicePortInfoExample();
        TblServicePortInfoExample.Criteria criteria = example.createCriteria();
        criteria.andStatusNotEqualTo(TargetServiceStatus.REMOVED.getCode());
        criteria.andReqIdEqualTo(rpcAddServicePortReq.getReqId());
        criteria.andMicroServiceEqualTo(rpcAddServicePortReq.getMicroServiceName());
        criteria.andTargetTypeEqualTo(rpcAddServicePortReq.getTargetType());
        criteria.andPurposeEqualTo(rpcAddServicePortReq.getPurpose());
        criteria.andDeploymentEqualTo(rpcAddServicePortReq.getDeployment());


        List<TblServicePortInfo> tblServicePortInfos = servicePortRepository.getServicePortsByExample(example);

        if (! CollectionUtils.isEmpty(tblServicePortInfos))
        {
            for (TblServicePortInfo tblServicePortInfo : tblServicePortInfos)
            {
                if (tblServicePortInfo != null && tblServicePortInfo.getStatus() != ServicePortStatus.REMOVED.getCode())
                {
                    retPacket.setStatus(ErrorCode.SUCCESS.getCode());
                    if (tblServicePortInfo.getStatus() == ServicePortStatus.READY.getCode())
                    {
                        MessagePack messagePack = new MessagePack();
                        messagePack.setMessageObj(tblServicePortInfo.getId());
                        messagePack.setMsgType(ServiceManagerMsg.ADD_SERVICE_PORT_CALLBACK);
                        serviceManagerMsgProcessStrategy.sendMessageIfNotInQueue(messagePack, ProcessorName.SERVICE_MANAGER_MSG);
                    }
                    else if (tblServicePortInfo.getStatus() == ServicePortStatus.PARTIAL_READY.getCode()
                            || tblServicePortInfo.getStatus() == ServicePortStatus.FILED.getCode())
                    {
                        List<TblServicePortTargetServiceInfo> tblServicePortTargetServiceInfos = servicePortRepository.getServicePortTargetServicesByServicePort(tblServicePortInfo.getId());

                        tblServicePortTargetServiceInfos.forEach(tblServicePortTargetServiceInfo -> {
                            if (tblServicePortTargetServiceInfo.getStatus() == TargetServiceStatus.NO_RESOURCE.getCode()
                                    || tblServicePortTargetServiceInfo.getStatus() == TargetServiceStatus.NOT_SATISFIED.getCode()
                                    || tblServicePortTargetServiceInfo.getStatus() == TargetServiceStatus.CREATE_FILED.getCode())
                            {
                                tblServicePortTargetServiceInfo.setStatus(TargetServiceStatus.UPDATING.getCode());
                                tblServicePortTargetServiceInfo.setUpdateTime(new Date());
                                servicePortRepository.updateServicePortTargetService(tblServicePortTargetServiceInfo);
                            }
                                });

                        tblServicePortInfo.setStatus(ServicePortStatus.UPDATING.getCode());
                        tblServicePortInfo.setUpdateTime(new Date());
                        servicePortRepository.updateServicePortSelective(tblServicePortInfo);
                    }
                    retPacket.setObj(tblServicePortInfo.getId());
                    return retPacket;
                }
            }
        }

        TblServicePortInfo tblServicePortInfo = new TblServicePortInfo();
        tblServicePortInfo.setId(Utils.buildStr("JST_SP_", Utils.assignUUId()));
        tblServicePortInfo.setName(String.format("%s_%s_%s", rpcAddServicePortReq.getMicroServiceName(), rpcAddServicePortReq.getPurpose(), rpcAddServicePortReq.getDeployment()));

        List<String> tags = rpcAddServicePortReq.getTags();
        if (tags == null) tags = new ArrayList<>();
        tags.add(String.format("microServiceName:%s", rpcAddServicePortReq.getMicroServiceName()));
        tags.add(String.format("reqId:%s", rpcAddServicePortReq.getReqId()));
        tblServicePortInfo.setTags(gson.toJson(tags));
        tblServicePortInfo.setPurpose(rpcAddServicePortReq.getPurpose());
        tblServicePortInfo.setTargetType(rpcAddServicePortReq.getTargetType());
        tblServicePortInfo.setDeployment(rpcAddServicePortReq.getDeployment());
        tblServicePortInfo.setStatus(ServicePortStatus.CREATING.getCode());
        tblServicePortInfo.setDescription(String.format("%s_%s_%s", rpcAddServicePortReq.getMicroServiceName(), rpcAddServicePortReq.getPurpose(), rpcAddServicePortReq.getDeployment()));
        tblServicePortInfo.setCreateTime(new Date());
        tblServicePortInfo.setUpdateTime(tblServicePortInfo.getCreateTime());
        servicePortRepository.insertServicePort(tblServicePortInfo);

        rpcAddServicePortReq.getTargetServices().forEach(targetService -> {
            TblServicePortTargetServiceInfo tblServicePortTargetServiceInfo = new TblServicePortTargetServiceInfo();
            tblServicePortTargetServiceInfo.setServicePortId(tblServicePortInfo.getId());
            tblServicePortTargetServiceInfo.setId(Utils.buildStr("JST_SP_TS_", Utils.assignUUId()));
            tblServicePortTargetServiceInfo.setDescription(null);
            tblServicePortTargetServiceInfo.setTargetSvcNode(null);
            tblServicePortTargetServiceInfo.setTargetSvcPort(null);
            tblServicePortTargetServiceInfo.setSvcNode(null);
            tblServicePortTargetServiceInfo.setSvcIp(null);
            tblServicePortTargetServiceInfo.setSvcPort(null);
            tblServicePortTargetServiceInfo.setProtocol(targetService.getProtocol());
            tblServicePortTargetServiceInfo.setSite(null);
            tblServicePortTargetServiceInfo.setService(targetService.getService());
            tblServicePortTargetServiceInfo.setTargetIp(targetService.getTargetIp());
            tblServicePortTargetServiceInfo.setTargetPort(targetService.getTargetPort());
            tblServicePortTargetServiceInfo.setCert(targetService.getCert());
            tblServicePortTargetServiceInfo.setStatus(TargetServiceStatus.CREATING.getCode());
            tblServicePortTargetServiceInfo.setCreateTime(tblServicePortInfo.getCreateTime());
            tblServicePortTargetServiceInfo.setUpdateTime(tblServicePortInfo.getCreateTime());
            servicePortRepository.insertServicePortTargetService(tblServicePortTargetServiceInfo);
        });

        retPacket.setStatus(ErrorCode.SUCCESS.getCode());
        retPacket.setObj(tblServicePortInfo.getId());
        return retPacket;
    }

    @Override
    public CombRetPacket deleteServicePort(@ApiParam(name = "reqId")String reqId)
    {
        CombRetPacket retPacket = new CombRetPacket();
        retPacket.setStatus(ErrorCode.SUCCESS.getCode());

        TblServicePortInfoExample example = new TblServicePortInfoExample();
        TblServicePortInfoExample.Criteria criteria = example.createCriteria();
        criteria.andReqIdEqualTo(reqId);
        criteria.andStatusNotEqualTo(TargetServiceStatus.REMOVED.getCode());
        List<TblServicePortInfo> tblServicePortInfos = servicePortRepository.getServicePortsByExample(example);

        tblServicePortInfos.forEach(tblServicePortInfo -> {
            if (tblServicePortInfo != null && tblServicePortInfo.getStatus() != ServicePortStatus.REMOVED.getCode())
            {
                tblServicePortInfo.setStatus(ServicePortStatus.REMOVING.getCode());
                servicePortRepository.updateServicePort(tblServicePortInfo);
            }
        });

        return retPacket;
    }

    @Override
    public RpcCreatePortReq getRpcCreatePortReq(@ApiParam(name = "targetServiceId")String targetServiceId)
    {
        RpcCreatePortReq rpcCreatePortReq = null;
        TblServicePortTargetServiceInfo tblServicePortTargetServiceInfo = servicePortRepository.getServicePortTargetService(targetServiceId);
        if (tblServicePortTargetServiceInfo == null || tblServicePortTargetServiceInfo.getStatus() == TargetServiceStatus.REMOVED.getCode())
        {
            return rpcCreatePortReq;
        }
        TblServicePortInfo tblServicePortInfo = servicePortRepository.getServicePort(tblServicePortTargetServiceInfo.getServicePortId());
        if (tblServicePortInfo == null || tblServicePortInfo.getStatus() == ServicePortStatus.REMOVED.getCode())
        {
            tblServicePortTargetServiceInfo.setStatus(TargetServiceStatus.REMOVED.getCode());
            tblServicePortTargetServiceInfo.setUpdateTime(new Date());
            servicePortRepository.updateServicePortTargetService(tblServicePortTargetServiceInfo);
            return rpcCreatePortReq;
        }
        TblServiceGatewayPortAllocationExample example = new TblServiceGatewayPortAllocationExample();
        TblServiceGatewayPortAllocationExample.Criteria criteria = example.createCriteria();
        criteria.andStatusNotEqualTo(PortAllocationStatus.RELEASED.getCode());
        criteria.andServicePortTargetServiceIdEqualTo(targetServiceId);
        List<TblServiceGatewayPortAllocation> tblServiceGatewayPortAllocations = serviceGatewayRepository.getServiceGatewayPortAllocationsByExample(example);
        if (CollectionUtils.isEmpty(tblServiceGatewayPortAllocations))
        {
            tblServicePortTargetServiceInfo.setStatus(TargetServiceStatus.REMOVED.getCode());
            tblServicePortTargetServiceInfo.setUpdateTime(new Date());
            servicePortRepository.updateServicePortTargetService(tblServicePortTargetServiceInfo);
            return rpcCreatePortReq;
        }
        rpcCreatePortReq = serviceGatewayService.genRpcCreatePortReq(tblServicePortInfo, tblServicePortTargetServiceInfo, tblServiceGatewayPortAllocations.get(0));

        return rpcCreatePortReq;
    }
}
