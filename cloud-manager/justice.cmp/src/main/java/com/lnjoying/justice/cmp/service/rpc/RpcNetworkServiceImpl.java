package com.lnjoying.justice.cmp.service.rpc;

import com.lnjoying.justice.cmp.common.PortType;
import com.lnjoying.justice.cmp.common.nextstack.AgentConstant;
import com.lnjoying.justice.cmp.db.model.*;
import com.lnjoying.justice.cmp.db.repo.NetworkRepository;
import com.lnjoying.justice.cmp.domain.dto.AddressesRef;
import com.lnjoying.justice.cmp.domain.dto.request.nextstack.network.NetworkDetailInfoReq;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.network.RpcNetworkDetailInfo;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.network.RpcSecurityGroupRule;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.network.RpcSgInfo;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.network.RpcTenantNetworkPort;
import com.lnjoying.justice.cmp.service.nextstack.repo.ImageServiceBiz;
import com.micro.core.common.Utils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.lnjoying.justice.cmp.common.CommonPhaseStatus.REMOVED;

@Service
public class RpcNetworkServiceImpl
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageServiceBiz.class);

    @Autowired
    private NetworkRepository networkRepository;

    public List<RpcNetworkDetailInfo> getBatchNetworkInfos(String cloudId, List<NetworkDetailInfoReq> networkInfoList) {
        return networkInfoList.stream().map(networkDetailInfoReq -> getNetworkDetailInfo(cloudId, networkDetailInfoReq)).collect(Collectors.toList());
    }

    public RpcNetworkDetailInfo getNetworkDetailInfo(String cloudId, NetworkDetailInfoReq networkDetailInfoReq){
        TblCmpVpc vpc = networkRepository.getVpcById(cloudId, networkDetailInfoReq.getVpcId());
        TblCmpSubnet subnet = networkRepository.getSubnetById(cloudId, networkDetailInfoReq.getSubnetId());
        RpcNetworkDetailInfo networkDetailInfo = new RpcNetworkDetailInfo();
        TblCmpPort port = networkRepository.getPortById(cloudId, networkDetailInfoReq.getPortId());
        if (vpc != null && null != vpc.getPhaseStatus() && REMOVED != vpc.getPhaseStatus() ) {
            networkDetailInfo.setVpcName(vpc.getName());
            networkDetailInfo.setVpcId(vpc.getVpcId());
            networkDetailInfo.setVpcCidr(vpc.getVpcCidr());
        }
        if (subnet != null && null != subnet.getPhaseStatus() && REMOVED != subnet.getPhaseStatus()) {
            networkDetailInfo.setSubnetId(subnet.getSubnetId());
            networkDetailInfo.setSubnetCidr(subnet.getSubnetCidr());
            networkDetailInfo.setSubnetName(subnet.getName());
        }
        if (port != null) {
            networkDetailInfo.setPortId(port.getPortId());
            networkDetailInfo.setIpAddress(port.getIpAddress());
            networkDetailInfo.setIsVip(false);
            if (port.getType() != null && PortType.vip == port.getType())
            {
                networkDetailInfo.setIsVip(true);
            }
            if (StringUtils.isNotBlank(port.getEipId()))
            {
                TblCmpEip tblCmpEip = networkRepository.getEipById(cloudId, port.getEipId());
                networkDetailInfo.setEipId(port.getEipId());
                networkDetailInfo.setEip(tblCmpEip.getIpaddr());
                networkDetailInfo.setPublicIp(tblCmpEip.getPublicIp());
                networkDetailInfo.setBoundPhaseStatus(port.getEipPhaseStatus());
                networkDetailInfo.setBoundType(AgentConstant.PORT_BOUND);
            }
            else
            {
                TblCmpEipMap eipMap = getEipIdFromPortMap(cloudId, port.getPortId());
                if (null != eipMap)
                {
                    TblCmpEip tblCmpEip = networkRepository.getEipById(cloudId, eipMap.getEipId());
                    networkDetailInfo.setEipId(tblCmpEip.getEipId());
                    networkDetailInfo.setEip(tblCmpEip.getIpaddr());
                    networkDetailInfo.setPublicIp(tblCmpEip.getPublicIp());
                    networkDetailInfo.setBoundPhaseStatus(eipMap.getStatus() == null ? null : eipMap.getStatus().intValue());
                    networkDetailInfo.setBoundType(AgentConstant.NAT_BOUND);
                }
            }
        }
        return networkDetailInfo;
    }

    TblCmpEipMap getEipIdFromPortMap(String cloudId, String portId)
    {
        TblCmpEipMapExample eipMapExample = new TblCmpEipMapExample();
        TblCmpEipMapExample.Criteria criteria = eipMapExample.createCriteria();
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andEeStatusNotEqualTo(REMOVED);
        criteria.andPortIdEqualTo(portId);
        criteria.andStatusNotEqualTo((short)REMOVED);
        if (networkRepository.countEipMapByExample(eipMapExample) == 0)
        {
            return null;
        }
        List<TblCmpEipMap> tblCmpEipMaps = networkRepository.getEipMaps(eipMapExample);
        return tblCmpEipMaps == null ? null : tblCmpEipMaps.get(0);
    }

    public List<String> getPorts(String cloudId, String subnetId)
    {
        TblCmpEipMapExample eipMapExample = new TblCmpEipMapExample();
        TblCmpEipMapExample.Criteria eipMapCriteria = eipMapExample.createCriteria();
        eipMapCriteria.andSubnetIdEqualTo(subnetId);
        eipMapCriteria.andPortIdIsNotNull();
        eipMapCriteria.andStatusNotEqualTo((short)REMOVED);
        eipMapCriteria.andCloudIdEqualTo(cloudId);
        eipMapCriteria.andEeStatusNotEqualTo(REMOVED);
        List<String> portIds = networkRepository.getEipMaps(eipMapExample).stream().map(TblCmpEipMap::getPortId).collect(Collectors.toList());

        TblCmpPortExample portExample = new TblCmpPortExample();
        TblCmpPortExample.Criteria portCriteria = portExample.createCriteria();
        portCriteria.andSubnetIdEqualTo(subnetId);
        portCriteria.andEipIdIsNotNull();
        portCriteria.andPhaseStatusNotEqualTo((short)REMOVED);
        portCriteria.andCloudIdEqualTo(cloudId);
        portCriteria.andEeStatusNotEqualTo(REMOVED);
        portIds.addAll(networkRepository.getPorts(portExample).stream().map(TblCmpPort::getPortId).collect(Collectors.toList()));

        return portIds;
    }

    public  List<RpcSgInfo> getSgInfos(String cloudId, String instanceId)
    {
        if (null != instanceId && !instanceId.isEmpty())
        {
            TblCmpSgVmInstanceExample example = new TblCmpSgVmInstanceExample();
            TblCmpSgVmInstanceExample.Criteria criteria = example.createCriteria();
            criteria.andInstanceIdEqualTo(instanceId);
            criteria.andCloudIdEqualTo(cloudId);
            criteria.andEeStatusNotEqualTo(REMOVED);
            example.setOrderByClause("create_time desc");

            List<TblCmpSgVmInstance> tblCmpSgVmInstances = networkRepository.getSgVmInstances(example);
            List<TblCmpSecurityGroup> tblCmpSecurityGroups = tblCmpSgVmInstances.stream().map(
                    tblSgVmInstance->networkRepository.getSecurityGroupById(cloudId, tblSgVmInstance.getSgId())).collect(Collectors.toList());

            return tblCmpSecurityGroups.stream().map(
                    tblSecurityGroup -> {
                        RpcSgInfo sgInfo = new RpcSgInfo();
                        sgInfo.setDescription(tblSecurityGroup.getDescription());
                        sgInfo.setSgId(tblSecurityGroup.getSgId());
                        sgInfo.setSgName(tblSecurityGroup.getName());
                        TblCmpSecurityGroupRuleExample ruleExample = new TblCmpSecurityGroupRuleExample();
                        TblCmpSecurityGroupRuleExample.Criteria ruleCriteria = ruleExample.createCriteria();
                        ruleCriteria.andSgIdEqualTo(tblSecurityGroup.getSgId());
                        ruleCriteria.andCloudIdEqualTo(cloudId);
                        ruleCriteria.andEeStatusNotEqualTo(REMOVED);
                        List<TblCmpSecurityGroupRule> rules =  networkRepository.getSecurityGroupRules(ruleExample);
                        if (rules.size() > 0)
                        {
                            List<RpcSecurityGroupRule> sgRules= rules.stream().map(
                                    tblSecurityGroupRule -> {
                                        RpcSecurityGroupRule sgRule = new RpcSecurityGroupRule();
                                        AddressesRef addressesRef = new AddressesRef();
                                        if (null != tblSecurityGroupRule.getCidr() && !tblSecurityGroupRule.getCidr().isEmpty())
                                        {
                                            addressesRef.setCidr(tblSecurityGroupRule.getCidr());
                                            sgRule.setAddressRef(addressesRef);
                                        }
                                        else if (null != tblSecurityGroupRule.getSgIdReference() && !tblSecurityGroupRule.getSgIdReference().isEmpty())
                                        {
                                            addressesRef.setSgId(tblSecurityGroupRule.getSgIdReference());
                                            sgRule.setAddressRef(addressesRef);
                                        }
                                        sgRule.setAction(tblSecurityGroupRule.getAction() == null ? null : tblSecurityGroupRule.getAction().intValue());
                                        sgRule.setRuleId(tblSecurityGroupRule.getRuleId());
                                        sgRule.setAddressType(tblSecurityGroupRule.getAddressType() == null ? null : tblSecurityGroupRule.getAddressType().intValue());
                                        sgRule.setDescription(tblSecurityGroupRule.getDescription());
                                        sgRule.setPort(tblSecurityGroupRule.getPort());
                                        sgRule.setPriority(tblSecurityGroupRule.getPriority() == null ? null : tblSecurityGroupRule.getPriority().intValue());
                                        sgRule.setProtocol(tblSecurityGroupRule.getProtocol() == null ? null : tblSecurityGroupRule.getProtocol().intValue());
                                        sgRule.setDirection(tblSecurityGroupRule.getDirection() == null ? null : tblSecurityGroupRule.getDirection().intValue());
                                        sgRule.setUpdateTime(Utils.formatDate(tblSecurityGroupRule.getUpdateTime()));
                                        sgRule.setCreateTime(Utils.formatDate(tblSecurityGroupRule.getCreateTime()));
                                        return sgRule;
                                    }
                            ).collect(Collectors.toList());
                            sgInfo.setRules(sgRules);
                        }
                        return sgInfo;
                    }).collect(Collectors.toList());
        }
        return null;
    }

    public RpcTenantNetworkPort getTenantNetworkPort(String cloudId,  String portId) {
        TblCmpPort tblCmpPort = networkRepository.getPortById(cloudId, portId);
        if (null == tblCmpPort || REMOVED == tblCmpPort.getEeStatus()) {
            return null;
        }
        RpcTenantNetworkPort tenantNetworkPort = new RpcTenantNetworkPort();
        tenantNetworkPort.setMacAddress(tblCmpPort.getMacAddress());
        tenantNetworkPort.setPortId(portId);
        if (null != tblCmpPort.getOfPort())
        {
            tenantNetworkPort.setOfport(tblCmpPort.getOfPort().toString());
        }
        tenantNetworkPort.setIpAddress(tblCmpPort.getIpAddress());
        tenantNetworkPort.setPhaseStatus(tblCmpPort.getPhaseStatus() == null ? null : tblCmpPort.getPhaseStatus().intValue());
        TblCmpSubnet tblSubnet = networkRepository.getSubnetById(cloudId, tblCmpPort.getSubnetId());
        if (tblSubnet != null)
        {
            tenantNetworkPort.setSubnetCidr(tblSubnet.getSubnetCidr());
            tenantNetworkPort.setSubnetName(tblSubnet.getName());
            TblCmpVpc tblVpc = networkRepository.getVpcById(cloudId, tblSubnet.getVpcId());
            if (tblVpc != null)
            {
                tenantNetworkPort.setVpcCidr(tblVpc.getVpcCidr());
                tenantNetworkPort.setVlanId(tblVpc.getVlanId() == null ? null : tblVpc.getVlanId().toString());
                tenantNetworkPort.setVpcName(tblVpc.getName());
            }
        }
        return  tenantNetworkPort;
    }
}
