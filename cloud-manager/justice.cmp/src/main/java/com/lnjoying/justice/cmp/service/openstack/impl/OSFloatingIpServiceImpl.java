package com.lnjoying.justice.cmp.service.openstack.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lnjoying.justice.cmp.common.SyncMsg;
import com.lnjoying.justice.cmp.db.model.*;
import com.lnjoying.justice.cmp.db.repo.CloudRepository;
import com.lnjoying.justice.cmp.db.repo.OSNeutronRepository;
import com.lnjoying.justice.cmp.domain.dto.request.openstack.neutron.OSFloatingIPCreateReq;
import com.lnjoying.justice.cmp.domain.dto.request.openstack.neutron.OSFloatingIPUpdateReq;
import com.lnjoying.justice.cmp.domain.dto.request.openstack.neutron.OSPortForwardingCreateReq;
import com.lnjoying.justice.cmp.domain.dto.request.openstack.neutron.OSPortForwardingUpdateReq;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.neutron.*;
import com.lnjoying.justice.cmp.entity.search.openstack.OSFloatingIPSearchCritical;
import com.lnjoying.justice.cmp.entity.search.openstack.OSPortForwardingSearchCritical;
import com.lnjoying.justice.cmp.service.nextstack.baremetal.BaremetalComputeService;
import com.lnjoying.justice.cmp.service.cloud.CloudService;
import com.lnjoying.justice.cmp.service.openstack.FloatingIPService;
import com.lnjoying.justice.cmp.service.syncdata.ESKSyncDataService;
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

import java.util.List;
import java.util.stream.Collectors;

import static com.lnjoying.justice.cmp.common.CommonPhaseStatus.REMOVED;

@Service
public class OSFloatingIpServiceImpl implements FloatingIPService
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
    private ESKSyncDataService eskSyncDataService;

    public OSFloatingIpsWithDetailsRsp getFloatingIPs(String cloudId, OSFloatingIPSearchCritical floatingIPSearchCritical, String userId) throws WebSystemException
    {
        OSFloatingIpsWithDetailsRsp floatingIpsWithDetails = new OSFloatingIpsWithDetailsRsp();
        List<TblCmpOsFloatingips> floatingips = osNeutronRepository.getFloatingips(getTblCmpOsFloatingipsExample(cloudId, floatingIPSearchCritical, userId));
        if (null == floatingips) {
            return floatingIpsWithDetails;
        }

        List<OSFloatingIpInfo> floatingIpInfos = floatingips.stream().map(tblCmpOsFloatingip -> {
            OSFloatingIpInfo floatingIpInfo = new OSFloatingIpInfo();
            floatingIpInfo.setFloatingIpInfo(tblCmpOsFloatingip);
            floatingIpInfo.setNetwork(getTblCmpOsNetwork(cloudId, tblCmpOsFloatingip.getFloatingNetworkId()));
            return floatingIpInfo;
        }).collect(Collectors.toList());

        floatingIpInfos.forEach(floatingIpInfo -> cloudService.syncSingleData(cloudId, floatingIpInfo.getId(), SyncMsg.OS_FLOATING_IP));

        floatingIpsWithDetails.setFloatingips(floatingIpInfos);
        return floatingIpsWithDetails;
    }

    public ResponseEntity<Object> addFloatingIP(String cloudId, OSFloatingIPCreateReq addFloatingIPReq, String bpId, String userId) throws WebSystemException
    {
        try
        {
            TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);
            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);
            addFloatingIPReq.getFloatingip().setProjectId(OSClientUtil.getOSProject(osClientV3));
            ResponseEntity response = osClientV3.sendHttpRequestToCloud(addFloatingIPReq);
            if (response.getStatusCode() == HttpStatus.ACCEPTED || response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK)
            {
                OSFloatingIpWithDetailsRsp osFloatingIpWithDetailsRsp = gson.fromJson(gson.toJson(response.getBody()), OSFloatingIpWithDetailsRsp.class);
                if (osFloatingIpWithDetailsRsp == null || osFloatingIpWithDetailsRsp.getFloatingip() == null)
                {
                    return response;
                }
                eskSyncDataService.updateFloatingIpToDB(cloudId, osFloatingIpWithDetailsRsp.getFloatingip(), bpId, userId);
            }
            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("add floatingIP failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    public OSFloatingIpWithDetailsRsp getFloatingIPDetails(String cloudId, String floatingipId, String userId) throws WebSystemException
    {
        TblCmpOsFloatingips tblCmpOsFloatingips = osNeutronRepository.getFloatingipById(cloudId, floatingipId);
        if (null == tblCmpOsFloatingips)
        {
            throw new WebSystemException(ErrorCode.FLOATINGIP_NOT_EXISTS, ErrorLevel.INFO);
        }
        if (userId != null && !tblCmpOsFloatingips.getEeUser().equals(userId))
        {
            throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
        }
        OSFloatingIpWithDetailsRsp osFloatingIpWithDetailsRsp = new OSFloatingIpWithDetailsRsp();
        OSFloatingIpInfo floatingIpInfo = new OSFloatingIpInfo();
        floatingIpInfo.setFloatingIpInfo(tblCmpOsFloatingips);
        floatingIpInfo.setNetwork(getTblCmpOsNetwork(cloudId, tblCmpOsFloatingips.getFloatingNetworkId()));
        osFloatingIpWithDetailsRsp.setFloatingip(floatingIpInfo);

        cloudService.syncSingleData(cloudId, floatingipId, SyncMsg.OS_FLOATING_IP);

        return osFloatingIpWithDetailsRsp;
    }

    public ResponseEntity<Object> updateFloatingIP(String cloudId, String floatingIpId, OSFloatingIPUpdateReq floatingIPUpdateReq, String bpId, String userId) throws WebSystemException
    {
        try
        {
            TblCmpOsFloatingips tblCmpOsFloatingips = osNeutronRepository.getFloatingipById(cloudId, floatingIpId);
            if (null == tblCmpOsFloatingips)
            {
                throw new WebSystemException(ErrorCode.FLOATINGIP_NOT_EXISTS, ErrorLevel.INFO);
            }
            if (userId != null && !tblCmpOsFloatingips.getEeUser().equals(userId))
            {
                throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
            }

            TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);
            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);
            ResponseEntity response = osClientV3.sendHttpRequestToCloud(floatingIPUpdateReq);
            if (response.getStatusCode() == HttpStatus.ACCEPTED || response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK)
            {
                OSFloatingIpWithDetailsRsp osFloatingIpWithDetailsRsp = gson.fromJson(gson.toJson(response.getBody()), OSFloatingIpWithDetailsRsp.class);
                if (osFloatingIpWithDetailsRsp == null || osFloatingIpWithDetailsRsp.getFloatingip() == null)
                {
                    return response;
                }
                eskSyncDataService.updateFloatingIpToDB(cloudId, osFloatingIpWithDetailsRsp.getFloatingip(), null, null);
            }
            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("update floatingIP failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    public ResponseEntity<Object> removeFloatingIP(String cloudId, String floatingipId, String userId) throws WebSystemException
    {
        try
        {
            TblCmpOsFloatingips tblCmpOsFloatingips = osNeutronRepository.getFloatingipById(cloudId, floatingipId);
            if (tblCmpOsFloatingips == null)
            {
                LOGGER.error("get floatingIP null: floatingIP id {}", floatingipId);
                throw new WebSystemException(ErrorCode.FLOATINGIP_NOT_EXISTS, ErrorLevel.INFO);
            }
            if (userId != null && !tblCmpOsFloatingips.getEeUser().equals(userId))
            {
                throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
            }

            TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);
            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);
            ResponseEntity response = osClientV3.sendHttpRequestToCloud();
            if (response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.ACCEPTED || response.getStatusCode() == HttpStatus.NO_CONTENT)
            {
                tblCmpOsFloatingips.setEeStatus(REMOVED);
                osNeutronRepository.updateFloatingip(tblCmpOsFloatingips);
            }
            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("remove floatingIP failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    public OSPortForwardingWithDetailsRsp getPortForwarding(String cloudId, String floatingIpId, String portForwardingId, String fields, String userId) throws WebSystemException
    {
        TblCmpOsPortforwardings portforwardings = osNeutronRepository.getPortforwardingById(cloudId, portForwardingId);
        if (null == portforwardings)
        {
            throw new WebSystemException(ErrorCode.PORTFORWARDING_NOT_EXISTS, ErrorLevel.INFO);
        }
        if (userId != null && !portforwardings.getEeUser().equals(userId))
        {
            throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
        }
        OSPortForwardingWithDetailsRsp portForwardingWithDetails = new OSPortForwardingWithDetailsRsp();
        OSPortForwardingInfo portForwardingInfo = new OSPortForwardingInfo();
        portForwardingInfo.setForwardingInfo(portforwardings);
        portForwardingWithDetails.setPortForwarding(portForwardingInfo);
        return portForwardingWithDetails;
    }

    public ResponseEntity<Object> updatePortForwarding(String cloudId, String floatingipId, String portForwardingId, OSPortForwardingUpdateReq request, String userId)
    {
        try
        {
            TblCmpOsFloatingips tblCmpOsFloatingips = osNeutronRepository.getFloatingipById(cloudId, floatingipId);
            if (tblCmpOsFloatingips == null)
            {
                LOGGER.error("get floatingIP null: floatingIP id {}", floatingipId);
                throw new WebSystemException(ErrorCode.FLOATINGIP_NOT_EXISTS, ErrorLevel.INFO);
            }
            if (userId != null && !tblCmpOsFloatingips.getEeUser().equals(userId))
            {
                throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
            }

            TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);
            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);
            ResponseEntity response = osClientV3.sendHttpRequestToCloud(request);
            if (response.getStatusCode() == HttpStatus.ACCEPTED || response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK)
            {
                OSPortForwardingWithDetailsRsp osPortForwardingWithDetailsRsp = gson.fromJson(gson.toJson(response.getBody()), OSPortForwardingWithDetailsRsp.class);
                if (osPortForwardingWithDetailsRsp == null || osPortForwardingWithDetailsRsp.getPortForwarding() == null)
                {
                    return response;
                }
                eskSyncDataService.updatePortforwardingToDB(cloudId, osPortForwardingWithDetailsRsp.getPortForwarding(), null, null);
            }
            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("update port forwarding failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    public ResponseEntity<Object> removePortForwarding(String cloudId, String floatingipId, String portForwardingId, String userId)
    {
        try
        {
            TblCmpOsPortforwardings portforwardings = osNeutronRepository.getPortforwardingById(cloudId, portForwardingId);
            if (portforwardings == null)
            {
                LOGGER.error("get port forwarding null: port forwarding id {}", portForwardingId);
                throw new WebSystemException(ErrorCode.PORTFORWARDING_NOT_EXISTS, ErrorLevel.INFO);
            }
            if (userId != null && !portforwardings.getEeUser().equals(userId))
            {
                throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
            }

            TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);
            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);
            ResponseEntity response = osClientV3.sendHttpRequestToCloud();
            if (response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.ACCEPTED)
            {
                portforwardings.setEeStatus(REMOVED);
                osNeutronRepository.updatePortforwarding(portforwardings);
            }
            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("remove port forwarding failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    public OSPortForwardingsWithDetailsRsp getPortForwardings(String cloudId, OSPortForwardingSearchCritical portForwardingSearchCritical, String userId)
    {
        OSPortForwardingsWithDetailsRsp portForwardingWithDetails = new OSPortForwardingsWithDetailsRsp();
        List<TblCmpOsPortforwardings> portforwardings = getTblCmpOsPortforwardings(cloudId, portForwardingSearchCritical, userId);
        if (null == portforwardings) {
            return portForwardingWithDetails;
        }

        List<OSPortForwardingInfo> portForwardingInfos = portforwardings.stream().map(tblCmpOsPortforwardings -> {
            OSPortForwardingInfo portForwardingInfo = new OSPortForwardingInfo();
            portForwardingInfo.setForwardingInfo(tblCmpOsPortforwardings);
            return portForwardingInfo;
        }).collect(Collectors.toList());

        portForwardingWithDetails.setPortForwardings(portForwardingInfos);
        return portForwardingWithDetails;
    }

    public ResponseEntity<Object> addPortForwarding(String cloudId, String floatingipId, OSPortForwardingCreateReq addPortForwardingReq, String bpId, String userId)
    {
        try
        {
            TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);
            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);
            ResponseEntity response = osClientV3.sendHttpRequestToCloud(addPortForwardingReq);
            if (response.getStatusCode() == HttpStatus.ACCEPTED || response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK)
            {
                OSPortForwardingWithDetailsRsp osPortForwardingWithDetailsRsp = gson.fromJson(gson.toJson(response.getBody()), OSPortForwardingWithDetailsRsp.class);
                if (osPortForwardingWithDetailsRsp == null || osPortForwardingWithDetailsRsp.getPortForwarding() == null)
                {
                    return response;
                }
                eskSyncDataService.updatePortforwardingToDB(cloudId, osPortForwardingWithDetailsRsp.getPortForwarding(), bpId, userId);
            }
            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("add port forwarding failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    private TblCmpOsFloatingipsExample getTblCmpOsFloatingipsExample(String cloudId, OSFloatingIPSearchCritical floatingIPSearchCritical, String userId) throws WebSystemException
    {
        TblCmpOsFloatingipsExample example = new TblCmpOsFloatingipsExample();
        TblCmpOsFloatingipsExample.Criteria criteria = example.createCriteria();
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andEeStatusNotEqualTo(REMOVED);

        if (StringUtils.isNotEmpty(floatingIPSearchCritical.getId())) criteria.andIdEqualTo(floatingIPSearchCritical.getId());
        if (StringUtils.isNotEmpty(floatingIPSearchCritical.getRouterId())) criteria.andRouterIdEqualTo(floatingIPSearchCritical.getRouterId());
        if (StringUtils.isNotEmpty(floatingIPSearchCritical.getStatus())) criteria.andStatusEqualTo(floatingIPSearchCritical.getStatus());
        if (StringUtils.isNotEmpty(floatingIPSearchCritical.getTenantId()))
        {
            criteria.andProjectIdEqualTo(floatingIPSearchCritical.getTenantId());
        }
        else if (StringUtils.isNotEmpty(floatingIPSearchCritical.getProjectId()))
        {
            criteria.andProjectIdEqualTo(floatingIPSearchCritical.getProjectId());
        }
        if (floatingIPSearchCritical.getRevisionNumber() != null) criteria.andRevisionNumberEqualTo(floatingIPSearchCritical.getRevisionNumber().longValue());
        if (StringUtils.isNotEmpty(floatingIPSearchCritical.getDescription())) criteria.andDescriptionEqualTo(floatingIPSearchCritical.getDescription());
        if (StringUtils.isNotEmpty(floatingIPSearchCritical.getFloatingNetworkId())) criteria.andFloatingNetworkIdEqualTo(floatingIPSearchCritical.getFloatingNetworkId());
        if (StringUtils.isNotEmpty(floatingIPSearchCritical.getFixedIpAddress())) criteria.andFixedIpAddressEqualTo(floatingIPSearchCritical.getFixedIpAddress());
        if (StringUtils.isNotEmpty(floatingIPSearchCritical.getFloatingIpAddress())) criteria.andFloatingIpAddressEqualTo(floatingIPSearchCritical.getFloatingIpAddress());
        if (StringUtils.isNotEmpty(floatingIPSearchCritical.getSortKey()) && StringUtils.isNotEmpty(floatingIPSearchCritical.getSortDir()))
        {
            example.setOrderByClause(String.format("%s %s", floatingIPSearchCritical.getSortKey(), floatingIPSearchCritical.getSortDir()));
        }
        else
        {
            example.setOrderByClause("created_at desc, id desc");
        }

        if (StringUtils.isNotEmpty(userId)) criteria.andEeUserEqualTo(userId);

        return example;
    }

    private List<TblCmpOsPortforwardings> getTblCmpOsPortforwardings(String cloudId, OSPortForwardingSearchCritical portForwardingSearchCritical, String userId) throws WebSystemException
    {
        TblCmpOsPortforwardingsExample example = new TblCmpOsPortforwardingsExample();
        TblCmpOsPortforwardingsExample.Criteria criteria = example.createCriteria();
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andEeStatusNotEqualTo(REMOVED);

        if (StringUtils.isNotEmpty(portForwardingSearchCritical.getId())) criteria.andIdEqualTo(portForwardingSearchCritical.getId());
        if (portForwardingSearchCritical.getExternalPort() != null) criteria.andExternalPortEqualTo(portForwardingSearchCritical.getExternalPort());
        if (StringUtils.isNotEmpty(portForwardingSearchCritical.getProtocol())) criteria.andProtocolEqualTo(portForwardingSearchCritical.getProtocol());
        if (StringUtils.isNotEmpty(portForwardingSearchCritical.getSortKey()) && StringUtils.isNotEmpty(portForwardingSearchCritical.getSortDir()))
        {
            example.setOrderByClause(String.format("%s %s", portForwardingSearchCritical.getSortKey(), portForwardingSearchCritical.getSortDir()));
        }

        if (StringUtils.isNotEmpty(userId)) criteria.andEeUserEqualTo(userId);

        List<TblCmpOsPortforwardings> portforwardings = osNeutronRepository.getPortforwardings(example);
        return portforwardings;
    }

    private TblCmpOsNetworks getTblCmpOsNetwork(String cloudId, String networkId) throws WebSystemException
    {
        return osNeutronRepository.getNetworkById(cloudId, networkId);
    }

    public OSExtFloatingIpsWithDetailsRsp getFloatingIPsPage(String cloudId, OSFloatingIPSearchCritical floatingIPSearchCritical, String userId)
    {
        OSExtFloatingIpsWithDetailsRsp floatingIpsWithDetails = new OSExtFloatingIpsWithDetailsRsp();

        PageHelper.startPage(floatingIPSearchCritical.getPageNum(), floatingIPSearchCritical.getPageSize());
        List<TblCmpOsFloatingips> floatingips = osNeutronRepository.getFloatingips(getTblCmpOsFloatingipsExample(cloudId, floatingIPSearchCritical, userId));
        PageInfo<TblCmpOsFloatingips> pageInfo = new PageInfo<>(floatingips);

        if (null == floatingips) {
            return floatingIpsWithDetails;
        }

        List<OSExtFloatingIpInfo> floatingIpInfos = floatingips.stream().map(tblCmpOsFloatingip -> {
            OSExtFloatingIpInfo floatingIpInfo = new OSExtFloatingIpInfo();
            floatingIpInfo.setFloatingIpInfo(tblCmpOsFloatingip);
            floatingIpInfo.setNetwork(getTblCmpOsNetwork(cloudId, tblCmpOsFloatingip.getFloatingNetworkId()));
            floatingIpInfo.setRouter(getTblCmpOsRouter(cloudId, tblCmpOsFloatingip.getRouterId()));
            return floatingIpInfo;
        }).collect(Collectors.toList());

        floatingIpInfos.forEach(floatingIpInfo -> cloudService.syncSingleData(cloudId, floatingIpInfo.getId(), SyncMsg.OS_FLOATING_IP));

        floatingIpsWithDetails.setFloatingips(floatingIpInfos);
        floatingIpsWithDetails.setTotalNum(pageInfo.getTotal());
        return floatingIpsWithDetails;
    }

    public OSExtFloatingIpWithDetailsRsp getExtFloatingIPDetails(String cloudId, String floatingipId, String userId) throws WebSystemException
    {
        TblCmpOsFloatingips tblCmpOsFloatingips = osNeutronRepository.getFloatingipById(cloudId, floatingipId);
        if (null == tblCmpOsFloatingips)
        {
            throw new WebSystemException(ErrorCode.FLOATINGIP_NOT_EXISTS, ErrorLevel.INFO);
        }
        if (userId != null && !tblCmpOsFloatingips.getEeUser().equals(userId))
        {
            throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
        }
        OSExtFloatingIpWithDetailsRsp osFloatingIpWithDetailsRsp = new OSExtFloatingIpWithDetailsRsp();
        OSExtFloatingIpInfo floatingIpInfo = new OSExtFloatingIpInfo();
        floatingIpInfo.setFloatingIpInfo(tblCmpOsFloatingips);
        floatingIpInfo.setNetwork(getTblCmpOsNetwork(cloudId, tblCmpOsFloatingips.getFloatingNetworkId()));
        floatingIpInfo.setRouter(getTblCmpOsRouter(cloudId, tblCmpOsFloatingips.getRouterId()));
        osFloatingIpWithDetailsRsp.setFloatingip(floatingIpInfo);

        cloudService.syncSingleData(cloudId, floatingipId, SyncMsg.OS_FLOATING_IP);

        return osFloatingIpWithDetailsRsp;
    }

    private TblCmpOsRouters getTblCmpOsRouter(String cloudId, String routerId) throws WebSystemException
    {
        return osNeutronRepository.getRouterById(cloudId, routerId);
    }
}
