package com.lnjoying.justice.cmp.service.easystack.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lnjoying.justice.cmp.common.CloudProduct;
import com.lnjoying.justice.cmp.common.RedisCache;
import com.lnjoying.justice.cmp.common.SyncMsg;
import com.lnjoying.justice.cmp.db.model.*;
import com.lnjoying.justice.cmp.db.repo.*;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.nova.*;
import com.lnjoying.justice.cmp.entity.search.openstack.OSServerSearchCritical;
import com.lnjoying.justice.cmp.service.cloud.CloudService;
import com.lnjoying.justice.cmp.service.easystack.ESServerService;
import com.lnjoying.justice.cmp.service.nextstack.baremetal.BaremetalComputeService;
import com.lnjoying.justice.cmp.service.openstack.ServerService;
import com.lnjoying.justice.cmp.utils.BoolUtil;
import com.lnjoying.justice.cmp.utils.DateUtils;
import com.lnjoying.justice.cmp.utils.osclient.OSClientUtil;
import com.lnjoying.justice.cmp.utils.osclient.OSClientV3;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.common.ErrorLevel;
import com.micro.core.persistence.redis.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.lnjoying.justice.cmp.common.CommonPhaseStatus.REMOVED;
import static java.util.Calendar.HOUR;

@Service
public class ESServerServiceImpl implements ESServerService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(BaremetalComputeService.class);

    private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS").serializeNulls().create();

    @Autowired
    private OSNovaRepository osNovaRepository;

    @Autowired
    private CloudService cloudService;

    @Autowired
    private CloudRepository cloudRepository;

    @Autowired
    private OSCinderRepository osCinderRepository;

    @Autowired
    private OSNeutronRepository osNeutronRepository;

    @Autowired
    private OSGlanceRepository osGlanceRepository;

    @Autowired
    private ServerService osServerService;

    private TblCmpOsInstancesExample getTblCmpOsInstancesExample(String cloudId, OSServerSearchCritical serverSearchCritical, String userId) throws WebSystemException
    {
        TblCmpOsInstancesExample example = new TblCmpOsInstancesExample();
        TblCmpOsInstancesExample.Criteria criteria = example.createCriteria();
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andEeStatusNotEqualTo(REMOVED);

        if (StringUtils.isNotEmpty(serverSearchCritical.getAccessIpV4())) criteria.andAccessIpV4EqualTo(serverSearchCritical.getAccessIpV4());
        if (StringUtils.isNotEmpty(serverSearchCritical.getAccessIpV6())) criteria.andAccessIpV6EqualTo(serverSearchCritical.getAccessIpV6());
        if (StringUtils.isNotEmpty(serverSearchCritical.getAvailabilityZone())) criteria.andAvailabilityZoneEqualTo(serverSearchCritical.getAvailabilityZone());
        if (serverSearchCritical.getDeleted() != null && serverSearchCritical.getDeleted())
        {
            criteria.andStatusEqualTo("SOFT_DELETED");
        }
        else
        {
            criteria.andStatusNotEqualTo("SOFT_DELETED");
        }
        if (StringUtils.isNotEmpty(serverSearchCritical.getDescription())) criteria.andDisplayDescriptionEqualTo(serverSearchCritical.getDescription());
        if (StringUtils.isNotEmpty(serverSearchCritical.getHost())) criteria.andHostEqualTo(serverSearchCritical.getHost());
        if (StringUtils.isNotEmpty(serverSearchCritical.getHostname())) criteria.andHostnameEqualTo(serverSearchCritical.getHostname());
        if (StringUtils.isNotEmpty(serverSearchCritical.getKernelId())) criteria.andKernelIdEqualTo(serverSearchCritical.getKernelId());
        if (StringUtils.isNotEmpty(serverSearchCritical.getKeyName())) criteria.andKeyNameEqualTo(serverSearchCritical.getKeyName());
        if (serverSearchCritical.getLaunchIndex() != null) criteria.andLaunchIndexEqualTo(serverSearchCritical.getLaunchIndex());
        if (StringUtils.isNotEmpty(serverSearchCritical.getLockedBy())) criteria.andLockedByEqualTo(serverSearchCritical.getLockedBy());
        if (StringUtils.isNotEmpty(serverSearchCritical.getName())) criteria.andDisplayNameLike("%" + serverSearchCritical.getName() + "%");
        if (StringUtils.isNotEmpty(serverSearchCritical.getNode())) criteria.andNodeEqualTo(serverSearchCritical.getNode());
        if (serverSearchCritical.getPowerState() != null) criteria.andPowerStateEqualTo(serverSearchCritical.getPowerState());
        if (serverSearchCritical.getProgress() != null) criteria.andProgressEqualTo(serverSearchCritical.getProgress());
        if (StringUtils.isNotEmpty(serverSearchCritical.getProjectId())) criteria.andProjectIdEqualTo(serverSearchCritical.getProjectId());
        if (StringUtils.isNotEmpty(serverSearchCritical.getRamdiskId())) criteria.andRamdiskIdEqualTo(serverSearchCritical.getRamdiskId());
        if (StringUtils.isNotEmpty(serverSearchCritical.getReservationId())) criteria.andReservationIdEqualTo(serverSearchCritical.getReservationId());
        if (StringUtils.isNotEmpty(serverSearchCritical.getRootDeviceName())) criteria.andRootDeviceNameEqualTo(serverSearchCritical.getRootDeviceName());
        if (StringUtils.isNotEmpty(serverSearchCritical.getSortKey()) && StringUtils.isNotEmpty(serverSearchCritical.getSortDir()))
        {
            example.setOrderByClause(String.format("%s %s", serverSearchCritical.getSortKey(), serverSearchCritical.getSortDir()));
        }
        else
        {
            example.setOrderByClause("created_at desc");
        }
        if (StringUtils.isNotEmpty(serverSearchCritical.getStatus())) criteria.andStatusEqualTo(serverSearchCritical.getStatus());
        if (StringUtils.isNotEmpty(serverSearchCritical.getTaskState())) criteria.andTaskStateEqualTo(serverSearchCritical.getTaskState());
        if (StringUtils.isNotEmpty(serverSearchCritical.getUserId())) criteria.andUserIdEqualTo(serverSearchCritical.getUserId());
        if (StringUtils.isNotEmpty(serverSearchCritical.getUuid())) criteria.andUuidEqualTo(serverSearchCritical.getUuid());
        if (StringUtils.isNotEmpty(serverSearchCritical.getVmState())) criteria.andVmStateEqualTo(serverSearchCritical.getVmState());
        if (serverSearchCritical.getLocked() != null) criteria.andLockedEqualTo(BoolUtil.bool2Short(serverSearchCritical.getLocked()));

        if (StringUtils.isNotEmpty(userId)) criteria.andEeUserEqualTo(userId);

        return example;
    }

    private TblCmpOsInstanceFaults getTblCmpOsInstanceFault(String cloudId, String instanceUuid) throws WebSystemException
    {
        return osNovaRepository.getInstanceFaultById(cloudId, instanceUuid);
    }

    private List<TblCmpOsVolumeAttachment> getTblCmpVolumeAttachment(String cloudId, String instanceUuid) throws WebSystemException
    {
        TblCmpOsVolumeAttachmentExample example = new TblCmpOsVolumeAttachmentExample();
        TblCmpOsVolumeAttachmentExample.Criteria criteria = example.createCriteria();
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andEeStatusNotEqualTo(REMOVED);
        criteria.andInstanceUuidEqualTo(instanceUuid);
        return osCinderRepository.getVolumeAttachments(example);
    }

    private TblCmpOsFlavors getTblCmpFlavor(String cloudId, String flavorid)
    {
        return osNovaRepository.getFlavorById(cloudId, flavorid);
    }

    private TblCmpOsImages getTblCmpImage(String cloudId, String imageId)
    {
        return osGlanceRepository.getImagesById(cloudId, imageId);
    }

    private List<TblCmpOsSecuritygroups> getTblCmpSecuritygroups(String cloudId, String instanceUuid)
    {
        return osNeutronRepository.getTblCmpSecuritygroupsByServer(cloudId, instanceUuid);
    }

    public OSExtServersWithDetailsRsp getServersDetailedPage(String cloudId, OSServerSearchCritical serverSearchCritical, String userId)
    {
        OSExtServersWithDetailsRsp serversWithDetailsRsp = new OSExtServersWithDetailsRsp();

        PageHelper.startPage(serverSearchCritical.getPageNum(), serverSearchCritical.getPageSize());
        List<TblCmpOsInstances> servers = osNovaRepository.getInstances(getTblCmpOsInstancesExample(cloudId, serverSearchCritical, userId));
        PageInfo<TblCmpOsInstances> pageInfo = new PageInfo<>(servers);

        if (null == servers) {
            return serversWithDetailsRsp;
        }

        List<OSExtServerInfo> serverInfos = servers.stream().map(tblCmpOsServer -> {
            OSExtServerInfo serverInfo = new OSExtServerInfo();
            serverInfo.setServerInfo(tblCmpOsServer);
            serverInfo.setInstanceFault(getTblCmpOsInstanceFault(cloudId, tblCmpOsServer.getUuid()));
            serverInfo.setVolumeAttachment(getTblCmpVolumeAttachment(cloudId, tblCmpOsServer.getUuid()));
            if (serverInfo.getFlavor() != null && StringUtils.isNotEmpty(serverInfo.getFlavor().getId()))
            {
                serverInfo.setOsFlavor(getTblCmpFlavor(cloudId, serverInfo.getFlavor().getId()));
            }
            if (serverInfo.getImage() != null && StringUtils.isNotEmpty(((OSExtServerInfo.OSImage)serverInfo.getImage()).getId()))
            {
                serverInfo.setOsImage(getTblCmpImage(cloudId, ((OSExtServerInfo.OSImage)serverInfo.getImage()).getId()));
            }
            serverInfo.setSecuritygroup(getTblCmpSecuritygroups(cloudId, tblCmpOsServer.getUuid()));
            return serverInfo;
        }).collect(Collectors.toList());

        serverInfos.forEach(serverInfo -> cloudService.syncSingleData(cloudId, serverInfo.getId(), SyncMsg.OS_INSTANCE));

        serversWithDetailsRsp.setServers(serverInfos);
        serversWithDetailsRsp.setTotalNum(pageInfo.getTotal());
        return serversWithDetailsRsp;
    }

    public ResponseEntity<Object> removeServer(String cloudId, String serverId, String userId) throws WebSystemException
    {
        try
        {
            TblCmpOsInstances tblCmpInstance = osNovaRepository.getInstanceById(cloudId, serverId);
            if (tblCmpInstance == null)
            {
                LOGGER.error("get server null: server id {}", serverId);
                throw new WebSystemException(ErrorCode.INSTANCE_NOT_EXIST, ErrorLevel.INFO);
            }
            if (userId != null && !tblCmpInstance.getEeUser().equals(userId))
            {
                throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
            }

            osServerService.checkServerStatus(tblCmpInstance);

            TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);
            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);
            ResponseEntity response = osClientV3.sendHttpRequestToCloud();
            if (response.getStatusCode() == HttpStatus.NO_CONTENT || response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.ACCEPTED
                    || response.getStatusCode() == HttpStatus.NOT_FOUND || response.getStatusCode() == HttpStatus.CREATED)
            {
                tblCmpInstance.setEeStatus(REMOVED);
                osNovaRepository.updateInstance(tblCmpInstance);
            }
            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("remove server failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }
}
