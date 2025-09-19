package com.lnjoying.justice.cmp.service.openstack.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lnjoying.justice.cmp.common.SyncMsg;
import com.lnjoying.justice.cmp.db.model.*;
import com.lnjoying.justice.cmp.db.repo.CloudRepository;
import com.lnjoying.justice.cmp.db.repo.OSNeutronRepository;
import com.lnjoying.justice.cmp.db.repo.OSNovaRepository;
import com.lnjoying.justice.cmp.domain.dto.request.openstack.neutron.OSPortCreateReq;
import com.lnjoying.justice.cmp.domain.dto.request.openstack.neutron.OSPortUpdateReq;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.neutron.*;
import com.lnjoying.justice.cmp.entity.search.openstack.OSPortSearchCritical;
import com.lnjoying.justice.cmp.service.nextstack.baremetal.BaremetalComputeService;
import com.lnjoying.justice.cmp.service.cloud.CloudService;
import com.lnjoying.justice.cmp.service.openstack.PortService;
import com.lnjoying.justice.cmp.service.syncdata.ESKSyncDataService;
import com.lnjoying.justice.cmp.utils.BoolUtil;
import com.lnjoying.justice.cmp.utils.osclient.OSClientUtil;
import com.lnjoying.justice.cmp.utils.osclient.OSClientV3;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.common.ErrorLevel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.lnjoying.justice.cmp.common.CommonPhaseStatus.REMOVED;

@Service
public class OSPortServiceImpl implements PortService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(BaremetalComputeService.class);

    private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS").serializeNulls().create();

    @Autowired
    private OSNeutronRepository osNeutronRepository;

    @Autowired
    private CloudRepository cloudRepository;

    @Autowired
    private CloudService cloudService;

    @Autowired
    private OSNovaRepository osNovaRepository;

    @Autowired
    private ESKSyncDataService eskSyncDataService;

    public OSPortWithDetailsRsp getPortDetails(String cloudId, String portId, String fields, String userId) throws WebSystemException
    {
        TblCmpOsPorts tblCmpOsPort = osNeutronRepository.getPortById(cloudId, portId);
        if (null == tblCmpOsPort)
        {
            throw new WebSystemException(ErrorCode.PORT_NOT_EXIST, ErrorLevel.INFO);
        }
        if (userId != null && !tblCmpOsPort.getEeUser().equals(userId))
        {
            throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
        }
        OSPortWithDetailsRsp osPortWithDetailsRsp = new OSPortWithDetailsRsp();
        OSPortInfo portInfo = new OSPortInfo();
        portInfo.setPortInfo(tblCmpOsPort);
        portInfo.setAllowedaddresspair(getTblCmpOsAllowedaddresspairs(cloudId, portId));
        portInfo.setSecuritygroupportbinding(getTblCmpOsSecuritygroupportbindings(cloudId, portId));
        portInfo.setExtradhcpopt(getTblCmpOsExtradhcpopts(cloudId, portId));
        portInfo.setMl2PortBinding(getTblCmpOsPortBinding(cloudId, portId));
        portInfo.setIpallocation(getTblCmpOsIpallocatios(cloudId, portId));
        portInfo.setNetwork(getTblCmpOsNetwork(cloudId, portInfo.getNetworkId()));
        osPortWithDetailsRsp.setPort(portInfo);

        cloudService.syncSingleData(cloudId, portId, SyncMsg.OS_PORT);

        return osPortWithDetailsRsp;
    }

    public ResponseEntity<Object> updatePort(String cloudId, String portId, OSPortUpdateReq portUpdateReq, String bpId, String userId) throws WebSystemException
    {
        try
        {
            TblCmpOsPorts tblCmpOsPort = osNeutronRepository.getPortById(cloudId, portId);
            if (null == tblCmpOsPort)
            {
                throw new WebSystemException(ErrorCode.PORT_NOT_EXIST, ErrorLevel.INFO);
            }
            if (userId != null && !tblCmpOsPort.getEeUser().equals(userId))
            {
                throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
            }

            TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);
            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);
            ResponseEntity response = osClientV3.sendHttpRequestToCloud(portUpdateReq);
            if (response.getStatusCode() == HttpStatus.ACCEPTED || response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK)
            {
                OSPortWithDetailsRsp portWithDetailsRsp = gson.fromJson(gson.toJson(response.getBody()), OSPortWithDetailsRsp.class);
                if (portWithDetailsRsp == null || portWithDetailsRsp.getPort() == null)
                {
                    return response;
                }
                eskSyncDataService.updatePortToDB(cloudId, portWithDetailsRsp.getPort(), null, null);
            }
            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("update port failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    public ResponseEntity<Object> removePort(String cloudId, String portId, String userId) throws WebSystemException
    {
        try
        {
            TblCmpOsPorts tblCmpOsPort = osNeutronRepository.getPortById(cloudId, portId);
            if (tblCmpOsPort == null)
            {
                LOGGER.error("get port null: port id {}", portId);
                throw new WebSystemException(ErrorCode.PORT_NOT_EXIST, ErrorLevel.INFO);
            }
            if (userId != null && !tblCmpOsPort.getEeUser().equals(userId))
            {
                throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
            }

            TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);
            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);
            ResponseEntity response = osClientV3.sendHttpRequestToCloud();
            if (response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.ACCEPTED || response.getStatusCode() == HttpStatus.NO_CONTENT)
            {
                tblCmpOsPort.setEeStatus(REMOVED);
                osNeutronRepository.updatePort(tblCmpOsPort);
            }
            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("remove port failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    public OSPortsWithDetailsRsp getPorts(String cloudId, OSPortSearchCritical portSearchCritical, String userId) throws WebSystemException
    {
        OSPortsWithDetailsRsp portsWithDetails = new OSPortsWithDetailsRsp();
        List<TblCmpOsPorts> ports = osNeutronRepository.getPorts(getTblCmpOsPortsExample(cloudId, portSearchCritical, userId));
        if (null == ports) {
            return portsWithDetails;
        }

        List<OSPortInfo> portInfos = ports.stream().map(tblCmpOsPort -> {
            OSPortInfo portInfo = new OSPortInfo();
            portInfo.setPortInfo(tblCmpOsPort);
            portInfo.setAllowedaddresspair(getTblCmpOsAllowedaddresspairs(cloudId, portInfo.getId()));
            portInfo.setSecuritygroupportbinding(getTblCmpOsSecuritygroupportbindings(cloudId, portInfo.getId()));
            portInfo.setExtradhcpopt(getTblCmpOsExtradhcpopts(cloudId, portInfo.getId()));
            portInfo.setMl2PortBinding(getTblCmpOsPortBinding(cloudId, portInfo.getId()));
            portInfo.setIpallocation(getTblCmpOsIpallocatios(cloudId, portInfo.getId()));
            portInfo.setNetwork(getTblCmpOsNetwork(cloudId, portInfo.getNetworkId()));
            return portInfo;
        }).collect(Collectors.toList());

        portInfos.forEach(portInfo -> cloudService.syncSingleData(cloudId, portInfo.getId(), SyncMsg.OS_PORT));

        portsWithDetails.setPorts(portInfos);
        return portsWithDetails;
    }

    public ResponseEntity<Object> addPort(String cloudId, OSPortCreateReq addPortReq, String bpId, String userId) throws WebSystemException
    {
        try
        {
            TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);
            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);
            ResponseEntity response = osClientV3.sendHttpRequestToCloud(addPortReq);
            if (response.getStatusCode() == HttpStatus.ACCEPTED || response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK)
            {
                OSPortWithDetailsRsp portWithDetailsRsp = gson.fromJson(gson.toJson(response.getBody()), OSPortWithDetailsRsp.class);
                if (portWithDetailsRsp == null || portWithDetailsRsp.getPort() == null)
                {
                    return response;
                }
                eskSyncDataService.updatePortToDB(cloudId, portWithDetailsRsp.getPort(), bpId, userId);
            }
            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("add port failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    private TblCmpOsPortsExample getTblCmpOsPortsExample(String cloudId, OSPortSearchCritical portSearchCritical, String userId) throws WebSystemException
    {
        TblCmpOsPortsExample example = new TblCmpOsPortsExample();
        TblCmpOsPortsExample.Criteria criteria = example.createCriteria();
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andEeStatusNotEqualTo(REMOVED);

        if (portSearchCritical.getAdminStateUp() != null) criteria.andAdminStateUpEqualTo(BoolUtil.bool2Short(portSearchCritical.getAdminStateUp()));
        if (StringUtils.isNotEmpty(portSearchCritical.getDescription())) criteria.andDescriptionEqualTo(portSearchCritical.getDescription());
        if (StringUtils.isNotEmpty(portSearchCritical.getDeviceId())) criteria.andDeviceIdEqualTo(portSearchCritical.getDeviceId());
        if (StringUtils.isNotEmpty(portSearchCritical.getDeviceOwner())) criteria.andDeviceOwnerEqualTo(portSearchCritical.getDeviceOwner());
        if (StringUtils.isNotEmpty(portSearchCritical.getId())) criteria.andIdEqualTo(portSearchCritical.getId());
        if (StringUtils.isNotEmpty(portSearchCritical.getIpAllocation())) criteria.andIpAllocationEqualTo(portSearchCritical.getIpAllocation());
        if (StringUtils.isNotEmpty(portSearchCritical.getMacAddress())) criteria.andMacAddressEqualTo(portSearchCritical.getMacAddress());
        if (StringUtils.isNotEmpty(portSearchCritical.getName())) criteria.andNameLike("%" + portSearchCritical.getName() + "%");
        if (StringUtils.isNotEmpty(portSearchCritical.getNetworkId())) criteria.andNetworkIdEqualTo(portSearchCritical.getNetworkId());
        if (StringUtils.isNotEmpty(portSearchCritical.getTenantId()))
        {
            criteria.andProjectIdEqualTo(portSearchCritical.getTenantId());
        }
        else if (StringUtils.isNotEmpty(portSearchCritical.getProjectId()))
        {
            criteria.andProjectIdEqualTo(portSearchCritical.getProjectId());
        }
        if (portSearchCritical.getRevisionNumber() != null) criteria.andRevisionNumberEqualTo(portSearchCritical.getRevisionNumber().longValue());
        if (StringUtils.isNotEmpty(portSearchCritical.getSortKey()) && StringUtils.isNotEmpty(portSearchCritical.getSortDir()))
        {
            example.setOrderByClause(String.format("%s %s", portSearchCritical.getSortKey(), portSearchCritical.getSortDir()));
        }
        else
        {
            example.setOrderByClause("created_at desc, id desc");
        }
        if (StringUtils.isNotEmpty(portSearchCritical.getStatus())) criteria.andStatusEqualTo(portSearchCritical.getStatus());
        if (portSearchCritical.getMacLearningEnabled() != null) criteria.andMacLearningEnabledEqualTo(BoolUtil.bool2Short(portSearchCritical.getMacLearningEnabled()));

        if (StringUtils.isNotEmpty(userId)) criteria.andEeUserEqualTo(userId);

        return example;
    }

    private List<TblCmpOsAllowedaddresspairs> getTblCmpOsAllowedaddresspairs(String cloudId, String portId) throws WebSystemException
    {
        TblCmpOsAllowedaddresspairsExample example = new TblCmpOsAllowedaddresspairsExample();
        TblCmpOsAllowedaddresspairsExample.Criteria criteria = example.createCriteria();
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andEeStatusNotEqualTo(REMOVED);
        criteria.andPortIdEqualTo(portId);

        List<TblCmpOsAllowedaddresspairs> allowedaddresspairs = osNeutronRepository.getAllowedaddresspairs(example);
        return allowedaddresspairs;
    }

    private List<TblCmpOsExtradhcpopts> getTblCmpOsExtradhcpopts(String cloudId, String portId) throws WebSystemException
    {
        TblCmpOsExtradhcpoptsExample example = new TblCmpOsExtradhcpoptsExample();
        TblCmpOsExtradhcpoptsExample.Criteria criteria = example.createCriteria();
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andEeStatusNotEqualTo(REMOVED);
        criteria.andPortIdEqualTo(portId);

        List<TblCmpOsExtradhcpopts> extradhcpopts = osNeutronRepository.getExtradhcpopts(example);
        return extradhcpopts;
    }

    private List<TblCmpOsSecuritygroupportbindings> getTblCmpOsSecuritygroupportbindings(String cloudId, String portId) throws WebSystemException
    {
        TblCmpOsSecuritygroupportbindingsExample example = new TblCmpOsSecuritygroupportbindingsExample();
        TblCmpOsSecuritygroupportbindingsExample.Criteria criteria = example.createCriteria();
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andEeStatusNotEqualTo(REMOVED);
        criteria.andPortIdEqualTo(portId);

        List<TblCmpOsSecuritygroupportbindings> securitygroupportbindings = osNeutronRepository.getSecuritygroupportbindings(example);
        return securitygroupportbindings;
    }

    private TblCmpOsMl2PortBindings getTblCmpOsPortBinding(String cloudId, String portId) throws WebSystemException
    {
        TblCmpOsMl2PortBindingsExample example = new TblCmpOsMl2PortBindingsExample();
        TblCmpOsMl2PortBindingsExample.Criteria criteria = example.createCriteria();
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andEeStatusNotEqualTo(REMOVED);
        criteria.andPortIdEqualTo(portId);

        List<TblCmpOsMl2PortBindings> ml2PortBindings = osNeutronRepository.getMl2PortBindings(example);

        return CollectionUtils.isEmpty(ml2PortBindings) ? null : ml2PortBindings.get(0);
    }

    private List<TblCmpOsIpallocations> getTblCmpOsIpallocatios(String cloudId, String portId) throws WebSystemException
    {
        TblCmpOsIpallocationsExample example = new TblCmpOsIpallocationsExample();
        TblCmpOsIpallocationsExample.Criteria criteria = example.createCriteria();
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andEeStatusNotEqualTo(REMOVED);
        criteria.andPortIdEqualTo(portId);

        List<TblCmpOsIpallocations> ipallocations = osNeutronRepository.getIpallocations(example);

        return ipallocations;
    }

    private TblCmpOsNetworks getTblCmpOsNetwork(String cloudId, String networkId) throws WebSystemException
    {
        return osNeutronRepository.getNetworkById(cloudId, networkId);
    }

    private TblCmpOsSubnets getTblCmpOsSubnet(String cloudId, String subnetId) throws WebSystemException
    {
        return osNeutronRepository.getSubnetById(cloudId, subnetId);
    }

    public OSExtPortsWithDetailsRsp getPortsPage(String cloudId, OSPortSearchCritical portSearchCritical, String userId)
    {
        OSExtPortsWithDetailsRsp portsWithDetails = new OSExtPortsWithDetailsRsp();

        PageHelper.startPage(portSearchCritical.getPageNum(), portSearchCritical.getPageSize());
        List<TblCmpOsPorts> ports = osNeutronRepository.getPortsWithShared(getTblCmpOsPortsExample(cloudId, portSearchCritical, userId), cloudId, OSClientUtil.getOSProject(cloudId), userId);
        PageInfo<TblCmpOsPorts> pageInfo = new PageInfo<>(ports);

        if (null == ports) {
            return portsWithDetails;
        }

        List<OSExtPortInfo> portInfos = ports.stream().map(tblCmpOsPort -> {
            OSExtPortInfo portInfo = new OSExtPortInfo();
            portInfo.setPortInfo(tblCmpOsPort);
            portInfo.setAllowedaddresspair(getTblCmpOsAllowedaddresspairs(cloudId, portInfo.getId()));
            portInfo.setSecuritygroupportbinding(getTblCmpOsSecuritygroupportbindings(cloudId, portInfo.getId()));
            portInfo.setExtradhcpopt(getTblCmpOsExtradhcpopts(cloudId, portInfo.getId()));
            portInfo.setMl2PortBinding(getTblCmpOsPortBinding(cloudId, portInfo.getId()));
            portInfo.setIpallocation(getTblCmpOsIpallocatios(cloudId, portInfo.getId()));
            portInfo.setNetwork(getTblCmpOsNetwork(cloudId, portInfo.getNetworkId()));

            if (! CollectionUtils.isEmpty(portInfo.getFixedIps()))
            {
                for (OSExtIP osExtIP : portInfo.getFixedIps())
                {
                    portInfo.setSubnet(getTblCmpOsSubnet(cloudId, osExtIP.getSubnetId()));
                    portInfo.setIpallocationpools(getTblCmpOsIpallocationpools(cloudId, osExtIP.getSubnetId()));
                }
            }

            return portInfo;
        }).collect(Collectors.toList());

        portInfos.forEach(portInfo -> cloudService.syncSingleData(cloudId, portInfo.getId(), SyncMsg.OS_PORT));

        portsWithDetails.setPorts(portInfos);
        portsWithDetails.setTotalNum(pageInfo.getTotal());
        return portsWithDetails;
    }

    private TblCmpOsInstances getTblCmpOsInstance(String cloudId, String instanceId) throws WebSystemException
    {
        return osNovaRepository.getInstanceById(cloudId, instanceId);
    }

    public OSExtPortWithDetailsRsp getExtPortDetails(String cloudId, String portId, String fields, String userId) throws WebSystemException
    {
        TblCmpOsPorts tblCmpOsPort = osNeutronRepository.getPortById(cloudId, portId);
        if (null == tblCmpOsPort)
        {
            throw new WebSystemException(ErrorCode.PORT_NOT_EXIST, ErrorLevel.INFO);
        }
        if (userId != null && !tblCmpOsPort.getEeUser().equals(userId))
        {
            throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
        }
        OSExtPortWithDetailsRsp osPortWithDetailsRsp = new OSExtPortWithDetailsRsp();
        OSExtPortInfo portInfo = new OSExtPortInfo();
        portInfo.setPortInfo(tblCmpOsPort);
        portInfo.setAllowedaddresspair(getTblCmpOsAllowedaddresspairs(cloudId, portId));
        portInfo.setSecuritygroupportbinding(getTblCmpOsSecuritygroupportbindings(cloudId, portId));
        portInfo.setExtradhcpopt(getTblCmpOsExtradhcpopts(cloudId, portId));
        portInfo.setMl2PortBinding(getTblCmpOsPortBinding(cloudId, portId));
        portInfo.setIpallocation(getTblCmpOsIpallocatios(cloudId, portId));
        portInfo.setNetwork(getTblCmpOsNetwork(cloudId, portInfo.getNetworkId()));
        Set<String> subnetIds = osNeutronRepository.getSubnetConnectToRouter(cloudId);
        portInfo.getFixedIps().forEach(fixedIp -> {
            if (fixedIp.getSubnetId() != null) fixedIp.setSubnet(osNeutronRepository.getSubnetById(cloudId, fixedIp.getSubnetId()));
            if (fixedIp.getRouterExternal() == null)
            {
                fixedIp.setFloatingIp(getTblCmpOsFloatingips(cloudId, fixedIp.getIpAddress()));
                if (fixedIp.getFloatingIpAddress() == null && !subnetIds.contains(fixedIp.getSubnetId()))
                {
                    fixedIp.setUnreachable(true);
                }
            }
        });

        osPortWithDetailsRsp.setPort(portInfo);

        cloudService.syncSingleData(cloudId, portId, SyncMsg.OS_PORT);

        return osPortWithDetailsRsp;
    }

    private List<TblCmpOsFloatingips> getTblCmpOsFloatingips(String cloudId, String fixedIpAddress) throws WebSystemException
    {
        TblCmpOsFloatingipsExample example = new TblCmpOsFloatingipsExample();
        TblCmpOsFloatingipsExample.Criteria criteria = example.createCriteria();
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andEeStatusNotEqualTo(REMOVED);
        criteria.andFixedIpAddressEqualTo(fixedIpAddress);

        List<TblCmpOsFloatingips> floatingips = osNeutronRepository.getFloatingips(example);

        return floatingips;
    }

    private List<TblCmpOsIpallocationpools> getTblCmpOsIpallocationpools(String cloudId, String subnetId) throws WebSystemException
    {
        TblCmpOsIpallocationpoolsExample example = new TblCmpOsIpallocationpoolsExample();
        TblCmpOsIpallocationpoolsExample.Criteria criteria = example.createCriteria();
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andEeStatusNotEqualTo(REMOVED);
        criteria.andSubnetIdEqualTo(subnetId);

        List<TblCmpOsIpallocationpools> ipallocationpools = osNeutronRepository.getIpallocationpools(example);

        return ipallocationpools;
    }
}
