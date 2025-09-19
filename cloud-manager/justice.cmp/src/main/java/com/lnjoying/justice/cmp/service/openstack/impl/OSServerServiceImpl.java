package com.lnjoying.justice.cmp.service.openstack.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lnjoying.justice.cmp.common.RedisCache;
import com.lnjoying.justice.cmp.common.SyncMsg;
import com.lnjoying.justice.cmp.common.nextstack.VolumeSource;
import com.lnjoying.justice.cmp.db.model.*;
import com.lnjoying.justice.cmp.db.repo.*;
import com.lnjoying.justice.cmp.domain.dto.request.openstack.nova.OSBlockDeviceMappingCreate;
import com.lnjoying.justice.cmp.domain.dto.request.openstack.nova.OSExtActionServerReq;
import com.lnjoying.justice.cmp.domain.dto.request.openstack.nova.OSServerCreateReq;
import com.lnjoying.justice.cmp.domain.dto.request.openstack.nova.OSServerUpdateReq;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.cinder.OSExtVolumeInfo;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.glance.OSExtImagesWithDetailsRsp;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.glance.OSImageInfo;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.nova.*;
import com.lnjoying.justice.cmp.domain.model.UserResourceMetrics;
import com.lnjoying.justice.cmp.entity.search.openstack.OSServerSearchCritical;
import com.lnjoying.justice.cmp.service.nextstack.baremetal.BaremetalComputeService;
import com.lnjoying.justice.cmp.service.cloud.CloudService;
import com.lnjoying.justice.cmp.service.openstack.ServerService;
import com.lnjoying.justice.cmp.service.syncdata.ESKSyncDataService;
import com.lnjoying.justice.cmp.utils.BoolUtil;
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
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

import static com.lnjoying.justice.cmp.common.CommonPhaseStatus.REMOVED;

@Service
public class OSServerServiceImpl implements ServerService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(BaremetalComputeService.class);

    private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS").serializeNulls().create();

    @Autowired
    private OSNovaRepository osNovaRepository;

    @Autowired
    private CloudRepository cloudRepository;

    @Autowired
    private CloudService cloudService;

    @Autowired
    private ESKSyncDataService eskSyncDataService;

    @Autowired
    private OSCinderRepository osCinderRepository;

    @Autowired
    private OSNeutronRepository osNeutronRepository;

    @Autowired
    private OSGlanceRepository osGlanceRepository;

    public OSServersRsp getServers(String cloudId, OSServerSearchCritical serverSearchCritical, String userId) throws WebSystemException
    {
        OSServersRsp serversRsp = new OSServersRsp();
        List<TblCmpOsInstances> servers = osNovaRepository.getInstances(getTblCmpOsInstancesExample(cloudId, serverSearchCritical, userId));
        if (null == servers) {
            return serversRsp;
        }

        List<OSServerBasicInfo> serverBasicInfos = servers.stream().map(tblCmpOsInstance -> {
            OSServerBasicInfo serverBasicInfo = new OSServerBasicInfo();
            serverBasicInfo.setServerBasicInfo(tblCmpOsInstance);
            return serverBasicInfo;
        }).collect(Collectors.toList());

        serversRsp.setServers(serverBasicInfos);
        return serversRsp;
    }

    public ResponseEntity<Object> addServer(String cloudId, OSServerCreateReq addServerReq, String bpId, String userId) throws WebSystemException
    {
        try
        {
            TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);

            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);
            Map<String, String> osHeaders = new HashMap<>();
            osHeaders.put("Openstack-Api-Version", "compute 2.67");
            JsonObject jsonObject = JsonParser.parseString(gson.toJson(addServerReq)).getAsJsonObject();
            jsonObject.remove("chargeType");
            jsonObject.remove("priceUnit");
            jsonObject.remove("period");
            ResponseEntity response = osClientV3.sendHttpRequestToCloud(jsonObject, osHeaders);
            if (response.getStatusCode() == HttpStatus.ACCEPTED || response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.CREATED)
            {
                OSServerWithDetailsRsp osServerWithDetailsRsp = gson.fromJson(gson.toJson(response.getBody()), OSServerWithDetailsRsp.class);

                if (osServerWithDetailsRsp == null || osServerWithDetailsRsp.getServer() == null)
                {
                    return response;
                }

                if (osServerWithDetailsRsp.getServer().getImage() == null)
                {
                    addServerReq.getServer().getBlockDeviceMapping().forEach(osBlockDeviceMappingCreate -> {
                        if (osBlockDeviceMappingCreate.getSourceType().equals("image"))
                        {
                            osServerWithDetailsRsp.getServer().setImage(osBlockDeviceMappingCreate.getUuid());
                        }
                    });
                }

                if (osServerWithDetailsRsp.getServer().getName() == null)
                {
                    osServerWithDetailsRsp.getServer().setName(addServerReq.getServer().getName());
                }

                if (osServerWithDetailsRsp.getServer().getFlavor() == null)
                {
                    osServerWithDetailsRsp.getServer().setFlavor(new OSServerInfo.OSFlavor());
                    osServerWithDetailsRsp.getServer().getFlavor().setId(addServerReq.getServer().getFlavorRef());
                }

                eskSyncDataService.updateServerToDB(cloudId, osServerWithDetailsRsp.getServer(), bpId, userId);

                if (! CollectionUtils.isEmpty(addServerReq.getServer().getBlockDeviceMapping()))
                {
                    for (OSBlockDeviceMappingCreate blockDevice : addServerReq.getServer().getBlockDeviceMapping())
                    {
                        eskSyncDataService.updateBlockDeviceMappingToDB(cloudId, blockDevice, osServerWithDetailsRsp.getServer().getId(), bpId, userId);
                    }
                }

                cloudService.syncSingleData(cloudId, osServerWithDetailsRsp.getServer().getId(), SyncMsg.OS_INSTANCE);
            }
            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("add server failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    public OSServersWithDetailsRsp getServersDetailed(String cloudId, OSServerSearchCritical serverSearchCritical, String userId) throws WebSystemException
    {
        OSServersWithDetailsRsp serversWithDetailsRsp = new OSServersWithDetailsRsp();
        List<TblCmpOsInstances> servers = osNovaRepository.getInstances(getTblCmpOsInstancesExample(cloudId, serverSearchCritical, userId));
        if (null == servers) {
            return serversWithDetailsRsp;
        }

        List<OSServerInfo> serverInfos = servers.stream().map(tblCmpOsServer -> {
            OSServerInfo serverInfo = new OSServerInfo();
            serverInfo.setServerInfo(tblCmpOsServer);
            serverInfo.setInstanceFault(getTblCmpOsInstanceFault(cloudId, tblCmpOsServer.getUuid()));
            serverInfo.setVolumeAttachment(getTblCmpVolumeAttachment(cloudId, tblCmpOsServer.getUuid()));
            if (! CollectionUtils.isEmpty(serverInfo.getOsExtendedVolumesAttached()))
            {
                serverInfo.getOsExtendedVolumesAttached().forEach(osExtendedVolumesAttach ->
                {
                    TblCmpOsBlockDeviceMappingExample example = new TblCmpOsBlockDeviceMappingExample();
                    TblCmpOsBlockDeviceMappingExample.Criteria criteria = example.createCriteria();
                    criteria.andCloudIdEqualTo(cloudId);
                    criteria.andVolumeIdEqualTo(osExtendedVolumesAttach.getId());
                    List<TblCmpOsBlockDeviceMapping> tblCmpOsBlockDeviceMappings = osNovaRepository.getBlockDeviceMappings(example);
                    if (! CollectionUtils.isEmpty(tblCmpOsBlockDeviceMappings))
                    {
                        osExtendedVolumesAttach.setDeleteOnTermination(BoolUtil.short2Bool(tblCmpOsBlockDeviceMappings.get(0).getDeleteOnTermination()));
                    }
                });
            }
            if (serverInfo.getFlavor() != null && StringUtils.isNotEmpty(serverInfo.getFlavor().getId()))
            {
                serverInfo.setOsFlavor(getTblCmpFlavor(cloudId, serverInfo.getFlavor().getId()));
            }
            if (serverInfo.getImage() != null && StringUtils.isNotEmpty(((OSServerInfo.OSImage)serverInfo.getImage()).getId()))
            {
                serverInfo.setOsImage(getTblCmpImage(cloudId, ((OSServerInfo.OSImage)serverInfo.getImage()).getId()));
            }
            serverInfo.setSecuritygroup(getTblCmpSecuritygroups(cloudId, tblCmpOsServer.getUuid()));
            return serverInfo;
        }).collect(Collectors.toList());

        serverInfos.forEach(serverInfo -> cloudService.syncSingleData(cloudId, serverInfo.getId(), SyncMsg.OS_INSTANCE));

        serversWithDetailsRsp.setServers(serverInfos);
        return serversWithDetailsRsp;
    }

    public OSServerWithDetailsRsp getServerDetails(String cloudId, String serverId, String userId) throws WebSystemException
    {
        TblCmpOsInstances tblCmpOsInstances = osNovaRepository.getInstanceById(cloudId, serverId);
        if (null == tblCmpOsInstances)
        {
            throw new WebSystemException(ErrorCode.INSTANCE_NOT_EXIST, ErrorLevel.INFO);
        }
        if (userId != null && !tblCmpOsInstances.getEeUser().equals(userId))
        {
            throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
        }
        OSServerWithDetailsRsp serverWithDetailsRsp = new OSServerWithDetailsRsp();
        OSServerInfo serverInfo = new OSServerInfo();
        serverInfo.setServerInfo(tblCmpOsInstances);
        serverInfo.setInstanceFault(getTblCmpOsInstanceFault(cloudId, serverId));
        serverInfo.setVolumeAttachment(getTblCmpVolumeAttachment(cloudId, serverId));
        if (! CollectionUtils.isEmpty(serverInfo.getOsExtendedVolumesAttached()))
        {
            serverInfo.getOsExtendedVolumesAttached().forEach(osExtendedVolumesAttach ->
            {
                TblCmpOsBlockDeviceMappingExample example = new TblCmpOsBlockDeviceMappingExample();
                TblCmpOsBlockDeviceMappingExample.Criteria criteria = example.createCriteria();
                criteria.andCloudIdEqualTo(cloudId);
                criteria.andVolumeIdEqualTo(osExtendedVolumesAttach.getId());
                List<TblCmpOsBlockDeviceMapping> tblCmpOsBlockDeviceMappings = osNovaRepository.getBlockDeviceMappings(example);
                if (! CollectionUtils.isEmpty(tblCmpOsBlockDeviceMappings))
                {
                    osExtendedVolumesAttach.setDeleteOnTermination(BoolUtil.short2Bool(tblCmpOsBlockDeviceMappings.get(0).getDeleteOnTermination()));
                }
            });
        }
        if (serverInfo.getFlavor() != null && StringUtils.isNotEmpty(serverInfo.getFlavor().getId()))
        {
            serverInfo.setOsFlavor(getTblCmpFlavor(cloudId, serverInfo.getFlavor().getId()));
        }
        if (serverInfo.getImage() != null && StringUtils.isNotEmpty(((OSServerInfo.OSImage)serverInfo.getImage()).getId()))
        {
            serverInfo.setOsImage(getTblCmpImage(cloudId, ((OSServerInfo.OSImage)serverInfo.getImage()).getId()));
        }
        serverInfo.setSecuritygroup(getTblCmpSecuritygroups(cloudId, serverId));
        serverWithDetailsRsp.setServer(serverInfo);

        cloudService.syncSingleData(cloudId, serverId, SyncMsg.OS_INSTANCE);

        return serverWithDetailsRsp;
    }

    public ResponseEntity<Object> updateServer(String cloudId, String serverId, OSServerUpdateReq osServerUpdateReq, String bpId, String userId) throws WebSystemException
    {
        try
        {
            TblCmpOsInstances tblCmpOsInstances = osNovaRepository.getInstanceById(cloudId, serverId);
            if (null == tblCmpOsInstances)
            {
                throw new WebSystemException(ErrorCode.INSTANCE_NOT_EXIST, ErrorLevel.INFO);
            }
            if (userId != null && !tblCmpOsInstances.getEeUser().equals(userId))
            {
                throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
            }

            checkServerStatus(tblCmpOsInstances);

            TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);
            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);
            ResponseEntity response = osClientV3.sendHttpRequestToCloud(osServerUpdateReq);
            if (response.getStatusCode() == HttpStatus.ACCEPTED || response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK)
            {
                OSServerWithDetailsRsp osServerWithDetailsRsp = gson.fromJson(gson.toJson(response.getBody()), OSServerWithDetailsRsp.class);

                if (osServerWithDetailsRsp == null || osServerWithDetailsRsp.getServer() == null)
                {
                    return response;
                }
                eskSyncDataService.updateServerToDB(cloudId, osServerWithDetailsRsp.getServer(), null, null);
            }
            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("update server failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
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

            checkServerStatus(tblCmpInstance);

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
        finally
        {
            RedisUtil.delete(RedisCache.USER_METRICS + userId);
        }
    }

    public ResponseEntity<Object> actionServer(String cloudId, String serverId, Object request, String bpId, String userId)
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

            checkServerStatus(tblCmpInstance);

            TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);
            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);
            ResponseEntity response = osClientV3.sendHttpRequestToCloud();

            cloudService.syncSingleData(cloudId, serverId, SyncMsg.OS_INSTANCE);

            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("action flavor failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    public OSServerRemoteRsp remoteServer(String cloudId, String serverId, Object request, String bpId, String userId)
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

            checkServerStatus(tblCmpInstance);

            TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);
            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);

            Map<String, String> osHeaders = new HashMap<>();
            osHeaders.put("Openstack-Api-Version", "compute 2.67");

            ResponseEntity response = osClientV3.sendHttpRequestToCloud(osHeaders);
            if (response.getStatusCode() == HttpStatus.OK)
            {
                OSServerRemoteRsp osServerRemoteRsp = gson.fromJson(gson.toJson(response.getBody()), OSServerRemoteRsp.class);

                if (osServerRemoteRsp != null && osServerRemoteRsp.getRemoteConsole() != null)
                {
                    String urlPattern = "/proxy/%s/clouds/%s%s";
                    if (OSClientUtil.getCloudManagerConfig().getCloudServerUrl() != null)
                    {
                        urlPattern = "https://" + OSClientUtil.getCloudManagerConfig().getCloudServerUrl() + urlPattern;
                    }
                    String url = osServerRemoteRsp.getRemoteConsole().getUrl();
                    if (url.startsWith("http://"))
                    {
                        url = url.substring(url.indexOf("/", 7));
                    }
                    else if (url.startsWith("https://"))
                    {
                        url = url.substring(url.indexOf("/", 8));
                    }
                    else
                    {
                        url = url.substring(url.indexOf("/"));
                    }
                    if (url.contains("?token"))
                    {
                        url = url.replace("?token", String.format("?path=proxy/%s/clouds/%s/websockify?token", osClientV3.getCloudProductShort(), cloudId));
                    }
                    else if (url.contains("%3Ftoken"))
                    {
                        url = url.replace("%3Ftoken", String.format("?path=proxy/%s/clouds/%s/websockify?token", osClientV3.getCloudProductShort(), cloudId));
                    }
                    else
                    {
                        url = url.replace("path=", String.format("path=proxy/%s/clouds/%s/", osClientV3.getCloudProductShort(), cloudId));
                    }
                    osServerRemoteRsp.getRemoteConsole().setUrl(String.format(urlPattern, osClientV3.getCloudProductShort(), cloudId, url));
                    return osServerRemoteRsp;
                }
            }
            ErrorCode errorCode = ErrorCode.CUSTOM_ERROR;
            errorCode.setMessage(gson.toJson(response.getBody()));
            throw new WebSystemException(errorCode, ErrorLevel.INFO);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("action flavor failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    private TblCmpOsInstancesExample getTblCmpOsInstancesExample(String cloudId, OSServerSearchCritical serverSearchCritical, String userId) throws WebSystemException
    {
        TblCmpOsInstancesExample example = new TblCmpOsInstancesExample();
        TblCmpOsInstancesExample.Criteria criteria = example.createCriteria();
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andEeStatusNotEqualTo(REMOVED);

        if (StringUtils.isNotEmpty(serverSearchCritical.getAccessIpV4())) criteria.andAccessIpV4EqualTo(serverSearchCritical.getAccessIpV4());
        if (StringUtils.isNotEmpty(serverSearchCritical.getAccessIpV6())) criteria.andAccessIpV6EqualTo(serverSearchCritical.getAccessIpV6());
        if (StringUtils.isNotEmpty(serverSearchCritical.getAvailabilityZone())) criteria.andAvailabilityZoneEqualTo(serverSearchCritical.getAvailabilityZone());
        if (serverSearchCritical.getDeleted() != null) criteria.andDeletedEqualTo(BoolUtil.bool2Integer(serverSearchCritical.getDeleted()));
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

    private TblCmpOsInstanceExtra getTblCmpOsInstanceExtra(String cloudId, String instanceUuid) throws WebSystemException
    {
        return osNovaRepository.getInstanceExtraById(cloudId, instanceUuid);
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

    public OSServerVolumeAttachmentsRsp getServerVolumeAttachments(String cloudId, String serverId, Integer limit, Integer offset, String userId)
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

        OSServerVolumeAttachmentsRsp serverVolumeAttachmentsRsp = new OSServerVolumeAttachmentsRsp();

        List<TblCmpOsVolumeAttachment> tblCmpOsVolumeAttachments = getTblCmpVolumeAttachment(cloudId, serverId);
        if (null == tblCmpOsVolumeAttachments) {
            return serverVolumeAttachmentsRsp;
        }

        List<OSServerVolumeAttachmentsRsp.OSServerVolumeAttachment> volumeAttachments = tblCmpOsVolumeAttachments.stream().map(tblCmpOsVolumeAttachment -> {
            OSServerVolumeAttachmentsRsp.OSServerVolumeAttachment volumeAttachment = new OSServerVolumeAttachmentsRsp.OSServerVolumeAttachment();
            volumeAttachment.setVolumeAttachment(tblCmpOsVolumeAttachment);
            return volumeAttachment;
        }).collect(Collectors.toList());

        serverVolumeAttachmentsRsp.setVolumeAttachments(volumeAttachments);
        return serverVolumeAttachmentsRsp;
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

    public OSExtServerVolumeAttachmentsRsp getServerVolumeAttachmentsPage(String cloudId, String serverId, Integer limit, Integer offset, String userId, Integer pageSize, Integer pageNum)
    {
        OSExtServerVolumeAttachmentsRsp serverVolumeAttachmentsRsp = new OSExtServerVolumeAttachmentsRsp();

        if (pageSize == null) pageSize = 100;
        if (pageNum == null) pageNum = 1;

        PageHelper.startPage(pageNum, pageSize);
        List<TblCmpOsVolumeAttachment> tblCmpOsVolumeAttachments = getTblCmpVolumeAttachment(cloudId, serverId);
        PageInfo<TblCmpOsVolumeAttachment> pageInfo = new PageInfo<>(tblCmpOsVolumeAttachments);

        if (null == tblCmpOsVolumeAttachments) {
            return serverVolumeAttachmentsRsp;
        }

        List<OSExtServerVolumeAttachmentsRsp.OSServerVolumeAttachment> volumeAttachments = tblCmpOsVolumeAttachments.stream().map(tblCmpOsVolumeAttachment -> {
            OSExtServerVolumeAttachmentsRsp.OSServerVolumeAttachment volumeAttachment = new OSExtServerVolumeAttachmentsRsp.OSServerVolumeAttachment();
            volumeAttachment.setVolumeAttachment(tblCmpOsVolumeAttachment);
            OSExtVolumeInfo volumeInfo = new OSExtVolumeInfo();
            volumeInfo.setVolumeInfo(getTblCmpVolumes(cloudId, volumeAttachment.getVolumeId()));
            if (volumeInfo.getVolumeTypeId() != null)
            {
                volumeInfo.setVolumeType(getTblCmpOsVolumeTypes(cloudId, volumeInfo.getVolumeTypeId()));
            }

            TblCmpOsBlockDeviceMappingExample example = new TblCmpOsBlockDeviceMappingExample();
            TblCmpOsBlockDeviceMappingExample.Criteria criteria = example.createCriteria();
            criteria.andCloudIdEqualTo(cloudId);
            criteria.andInstanceUuidEqualTo(serverId);
            criteria.andVolumeIdEqualTo(volumeAttachment.getVolumeId());
            criteria.andEeStatusNotEqualTo(REMOVED);
            List<TblCmpOsBlockDeviceMapping> tblCmpOsBlockDeviceMappings = osNovaRepository.getBlockDeviceMappings(example);

            if (! CollectionUtils.isEmpty(tblCmpOsBlockDeviceMappings))
            {
                volumeInfo.setSource(VolumeSource.CREATE_WITH_VM);
                volumeInfo.setDeleteOnTermination(BoolUtil.short2Bool(tblCmpOsBlockDeviceMappings.get(0).getDeleteOnTermination()));
            }

            volumeAttachment.setVolume(volumeInfo);
            return volumeAttachment;
        }).collect(Collectors.toList());

        serverVolumeAttachmentsRsp.setVolumeAttachments(volumeAttachments);
        serverVolumeAttachmentsRsp.setTotalNum(pageInfo.getTotal());
        return serverVolumeAttachmentsRsp;
    }

    private TblCmpOsVolumeTypes getTblCmpOsVolumeTypes(String cloudId, String volumeTypeId) throws WebSystemException
    {
        return osCinderRepository.getVolumeTypeById(cloudId, volumeTypeId);
    }

    private TblCmpOsVolumes getTblCmpVolumes(String cloudId, String volumeId) throws WebSystemException
    {
        return osCinderRepository.getVolumeById(cloudId, volumeId);
    }

    public OSExtServerWithDetailsRsp getExtServerDetails(String cloudId, String serverId, String userId) throws WebSystemException
    {
        TblCmpOsInstances tblCmpOsInstances = osNovaRepository.getInstanceById(cloudId, serverId);
        if (null == tblCmpOsInstances)
        {
            throw new WebSystemException(ErrorCode.INSTANCE_NOT_EXIST, ErrorLevel.INFO);
        }
        if (userId != null && !tblCmpOsInstances.getEeUser().equals(userId))
        {
            throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
        }
        OSExtServerWithDetailsRsp serverWithDetailsRsp = new OSExtServerWithDetailsRsp();
        OSExtServerInfo serverInfo = new OSExtServerInfo();
        serverInfo.setServerInfo(tblCmpOsInstances);
        serverInfo.setInstanceFault(getTblCmpOsInstanceFault(cloudId, serverId));
        serverInfo.setVolumeAttachment(getTblCmpVolumeAttachment(cloudId, serverId));
        if (serverInfo.getFlavor() != null && StringUtils.isNotEmpty(serverInfo.getFlavor().getId()))
        {
            serverInfo.setOsFlavor(getTblCmpFlavor(cloudId, serverInfo.getFlavor().getId()));
        }
        if (serverInfo.getImage() != null && StringUtils.isNotEmpty(((OSExtServerInfo.OSImage)serverInfo.getImage()).getId()))
        {
            serverInfo.setOsImage(getTblCmpImage(cloudId, ((OSExtServerInfo.OSImage)serverInfo.getImage()).getId()));
        }
        serverInfo.setSecuritygroup(getTblCmpSecuritygroups(cloudId, serverId));
        if (! CollectionUtils.isEmpty(serverInfo.getOsExtendedVolumesAttached()))
        {
            serverInfo.getOsExtendedVolumesAttached().forEach(osExtendedVolumesAttach -> {
                OSExtVolumeInfo osExtVolumeInfo = new OSExtVolumeInfo();
                osExtVolumeInfo.setVolumeInfo(getTblCmpVolumes(cloudId, osExtendedVolumesAttach.getId()));

                TblCmpOsBlockDeviceMappingExample example = new TblCmpOsBlockDeviceMappingExample();
                TblCmpOsBlockDeviceMappingExample.Criteria criteria = example.createCriteria();
                criteria.andCloudIdEqualTo(cloudId);
                criteria.andInstanceUuidEqualTo(serverId);
                criteria.andVolumeIdEqualTo(osExtendedVolumesAttach.getId());
                criteria.andEeStatusNotEqualTo(REMOVED);
                List<TblCmpOsBlockDeviceMapping> tblCmpOsBlockDeviceMappings = osNovaRepository.getBlockDeviceMappings(example);

                if (! CollectionUtils.isEmpty(tblCmpOsBlockDeviceMappings))
                {
                    osExtVolumeInfo.setSource(VolumeSource.CREATE_WITH_VM);
                    osExtendedVolumesAttach.setDeleteOnTermination(BoolUtil.short2Bool(tblCmpOsBlockDeviceMappings.get(0).getDeleteOnTermination()));
                }

                osExtendedVolumesAttach.setVolume(osExtVolumeInfo);
            });
        }
        serverWithDetailsRsp.setServer(serverInfo);

        cloudService.syncSingleData(cloudId, serverId, SyncMsg.OS_INSTANCE);

        return serverWithDetailsRsp;
    }

    public ResponseEntity<Object> extEctionServer(String cloudId, String serverId, OSExtActionServerReq request, String bpId, String userId)
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

            checkServerStatus(tblCmpInstance);

            TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);
            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);

            ResponseEntity response;
            if (request != null && request.getRevertResize() != null && request.getRevertResize())
            {
                String revertResizeBody = "{\"revertResize\":null}";
                response = osClientV3.sendHttpRequestToCloudWithoutExt(revertResizeBody);
            }
            else if (request != null && request.getConfirmResize() != null && request.getConfirmResize())
            {
                String confirmResizeBody = "{\"confirmResize\":null}";
                response = osClientV3.sendHttpRequestToCloudWithoutExt(confirmResizeBody);
            }
            else
            {
                response = osClientV3.sendHttpRequestToCloudWithoutExt(request);
            }

            if (response.getStatusCode() == HttpStatus.ACCEPTED || response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK)
            {
                cloudService.syncSingleData(cloudId, serverId, SyncMsg.OS_INSTANCE);
            }

            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("action flavor failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    public OSExtServerOsInterfacesRsp getServerOsInterfacesPage(String cloudId, String serverId, String userId, Integer pageSize, Integer pageNum)
    {
        OSExtServerOsInterfacesRsp serverOsInterfacesRsp = new OSExtServerOsInterfacesRsp();

        TblCmpOsInstances tblCmpOsInstances = osNovaRepository.getInstanceById(cloudId, serverId);
        if (null == tblCmpOsInstances)
        {
            throw new WebSystemException(ErrorCode.INSTANCE_NOT_EXIST, ErrorLevel.INFO);
        }

        TblCmpOsPortsExample example = new TblCmpOsPortsExample();
        TblCmpOsPortsExample.Criteria criteria = example.createCriteria();
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andEeStatusNotEqualTo(REMOVED);
        criteria.andDeviceIdEqualTo(serverId);

        List<TblCmpOsPorts> ports = osNeutronRepository.getPorts(example);

        if (CollectionUtils.isEmpty(ports)) return serverOsInterfacesRsp;

        Set<String> subnetIds = osNeutronRepository.getSubnetConnectToRouter(cloudId);

        List<OSExtServerOsInterfacesRsp.OSExtInterfaceAttachment> osExtInterfaceAttachments = ports.stream().map(port -> {
            OSExtServerOsInterfacesRsp.OSExtInterfaceAttachment osExtInterfaceAttachment = new OSExtServerOsInterfacesRsp.OSExtInterfaceAttachment();
            osExtInterfaceAttachment.setPort(port);
            osExtInterfaceAttachment.setFixedIps(getTblCmpOsIpallocatios(cloudId, osExtInterfaceAttachment.getPortId()));
            if (osExtInterfaceAttachment.getNetId() != null) osExtInterfaceAttachment.setNetwork(osNeutronRepository.getNetworkById(cloudId, osExtInterfaceAttachment.getNetId()));
            osExtInterfaceAttachment.getFixedIps().forEach(fixedIp -> {
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
            return osExtInterfaceAttachment;
        }).collect(Collectors.toList());


        serverOsInterfacesRsp.setInterfaceAttachments(osExtInterfaceAttachments);
        return serverOsInterfacesRsp;
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

    public OSExtImagesWithDetailsRsp getRebuildServerImages(String cloudId, String serverId, String userId)
    {
        TblCmpOsInstances tblCmpOsInstances = osNovaRepository.getInstanceById(cloudId, serverId);
        if (null == tblCmpOsInstances)
        {
            throw new WebSystemException(ErrorCode.INSTANCE_NOT_EXIST, ErrorLevel.INFO);
        }
        if (userId != null && !tblCmpOsInstances.getEeUser().equals(userId))
        {
            throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
        }
        TblCmpOsFlavors tblCmpOsFlavor = osNovaRepository.getFlavorById(cloudId, tblCmpOsInstances.getInstanceTypeId());
        if (null == tblCmpOsFlavor)
        {
            throw new WebSystemException(ErrorCode.INSTANCE_NOT_EXIST, ErrorLevel.INFO);
        }

        OSExtImagesWithDetailsRsp imagesWithDetailsRsp = new OSExtImagesWithDetailsRsp();

        TblCmpOsImagesExample example = new TblCmpOsImagesExample();
        TblCmpOsImagesExample.Criteria criteria = example.createCriteria();
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andEeStatusNotEqualTo(REMOVED);
        criteria.andDiskFormatNotEqualTo("iso");
        criteria.andStatusEqualTo("active");
        criteria.andVisibilityEqualTo("public");
        criteria.andImageTypeNotEqualTo("%\"image_type\":\"snapshot\"%");
        criteria.andMinDiskLessThanOrEqualTo(tblCmpOsFlavor.getRootGb());
        criteria.andMinRamLessThanOrEqualTo(tblCmpOsFlavor.getMemoryMb());
        example.setOrderByClause("created_at desc");

        List<TblCmpOsImages> images = osGlanceRepository.getImagess(example);

        if (null == images) {
            return imagesWithDetailsRsp;
        }

        List<OSImageInfo> imageInfos = images.stream().map(tblCmpOsImage -> {
            OSImageInfo imageInfo = new OSImageInfo();
            imageInfo.setImageInfo(tblCmpOsImage);
            return imageInfo;
        }).collect(Collectors.toList());

        imageInfos.forEach(imageInfo -> cloudService.syncSingleData(cloudId, imageInfo.getId(), SyncMsg.OS_IMAGE));

        imagesWithDetailsRsp.setImages(imageInfos);
        imagesWithDetailsRsp.setTotalNum(imageInfos.size());
        return imagesWithDetailsRsp;
    }

    public void checkServerStatus(TblCmpOsInstances tblCmpOsInstances)
    {
        if (null == tblCmpOsInstances || tblCmpOsInstances.getStatus().equalsIgnoreCase("deleted") || REMOVED == tblCmpOsInstances.getEeStatus())
        {
            throw new WebSystemException(ErrorCode.VM_INSTANCE_NOT_EXIST, ErrorLevel.INFO);
        }
        else if (tblCmpOsInstances.getStatus().equalsIgnoreCase("build"))
        {
            throw new WebSystemException(ErrorCode.RESOURCE_IS_CREATING, ErrorLevel.INFO);
        }
    }

    public UserResourceMetrics getUserResourceMetrics(String cloudId, String userId)
    {
        try
        {
            UserResourceMetrics userResourceMetrics = new UserResourceMetrics();
            TblCmpOsInstancesExample tblCmpOsInstancesExample = new TblCmpOsInstancesExample();
            TblCmpOsInstancesExample.Criteria criteria = tblCmpOsInstancesExample.createCriteria();
            criteria.andCloudIdEqualTo(cloudId);
            criteria.andEeStatusNotEqualTo(REMOVED);
            criteria.andEeUserEqualTo(userId);
            long vmNum = osNovaRepository.countInstancesByExample(tblCmpOsInstancesExample);
            userResourceMetrics.setVmNum((int)vmNum);
            return userResourceMetrics;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("get user resources metrics error, {}", e.getMessage());
            return null;
        }
    }
}
