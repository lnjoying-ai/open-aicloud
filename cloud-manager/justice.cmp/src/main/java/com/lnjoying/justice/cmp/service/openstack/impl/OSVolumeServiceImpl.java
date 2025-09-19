package com.lnjoying.justice.cmp.service.openstack.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lnjoying.justice.cmp.common.SyncMsg;
import com.lnjoying.justice.cmp.common.nextstack.VolumeSource;
import com.lnjoying.justice.cmp.db.model.*;
import com.lnjoying.justice.cmp.db.repo.CloudRepository;
import com.lnjoying.justice.cmp.db.repo.OSCinderRepository;
import com.lnjoying.justice.cmp.db.repo.OSNovaRepository;
import com.lnjoying.justice.cmp.domain.dto.request.openstack.cinder.OSVolumeCreateReq;
import com.lnjoying.justice.cmp.domain.dto.request.openstack.cinder.OSVolumeUpdateReq;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.cinder.*;
import com.lnjoying.justice.cmp.entity.search.openstack.OSVolumeSearchCritical;
import com.lnjoying.justice.cmp.service.nextstack.baremetal.BaremetalComputeService;
import com.lnjoying.justice.cmp.service.cloud.CloudService;
import com.lnjoying.justice.cmp.service.openstack.VolumeService;
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

import java.util.*;
import java.util.stream.Collectors;

import static com.lnjoying.justice.cmp.common.CommonPhaseStatus.REMOVED;

@Service
public class OSVolumeServiceImpl implements VolumeService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(BaremetalComputeService.class);

    private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS").serializeNulls().create();

    @Autowired
    private OSCinderRepository osCinderRepository;

    @Autowired
    private CloudRepository cloudRepository;

    @Autowired
    private CloudService cloudService;

    @Autowired
    private OSNovaRepository osNovaRepository;

    @Autowired
    private ESKSyncDataService eskSyncDataService;

    public OSVolumesWithDetailsRsp getVolumesWithDetails(String cloudId, OSVolumeSearchCritical volumeSearchCritical, String userId) throws WebSystemException
    {
        OSVolumesWithDetailsRsp volumesWithDetailsRsp = new OSVolumesWithDetailsRsp();
        List<TblCmpOsVolumes> volumes = osCinderRepository.getVolumes(getTblCmpOsVolumesExample(cloudId, volumeSearchCritical, userId));
        if (null == volumes) {
            return volumesWithDetailsRsp;
        }

        List<OSVolumeInfo> volumeInfos = volumes.stream().map(tblCmpOsVolumes -> {
            OSVolumeInfo volumeInfo = new OSVolumeInfo();
            volumeInfo.setVolumeInfo(tblCmpOsVolumes);
            volumeInfo.setVolumeAttachment(getTblCmpOsVolumeAttachment(cloudId, tblCmpOsVolumes.getId()));
            return volumeInfo;
        }).collect(Collectors.toList());

        volumeInfos.forEach(volumeInfo -> cloudService.syncSingleData(cloudId, volumeInfo.getId(), SyncMsg.OS_VOLUME));

        volumesWithDetailsRsp.setVolumes(volumeInfos);
        return volumesWithDetailsRsp;
    }

    public ResponseEntity<Object> addVolume(String cloudId, OSVolumeCreateReq addVolumeReq, String bpId, String userId) throws WebSystemException
    {
        try
        {
            TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);
            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);
            JsonObject jsonObject = JsonParser.parseString(gson.toJson(addVolumeReq)).getAsJsonObject();
            jsonObject.remove("chargeType");
            jsonObject.remove("priceUnit");
            jsonObject.remove("period");
            ResponseEntity response = osClientV3.sendHttpRequestToCloudWithProject(jsonObject);
            if (response.getStatusCode() == HttpStatus.ACCEPTED || response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK)
            {
                OSVolumeWithDetailsRsp volumeWithDetailsRsp = gson.fromJson(gson.toJson(response.getBody()), OSVolumeWithDetailsRsp.class);
                if (volumeWithDetailsRsp == null || volumeWithDetailsRsp.getVolume() == null)
                {
                    return response;
                }

                eskSyncDataService.updateVolumeToDB(cloudId, volumeWithDetailsRsp.getVolume(), bpId, userId);
            }
            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("add volume failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    public OSVolumesRsp getVolumes(String cloudId, OSVolumeSearchCritical volumeSearchCritical, String userId) throws WebSystemException
    {
        OSVolumesRsp osVolumesRsp = new OSVolumesRsp();
        List<TblCmpOsVolumes> volumes = osCinderRepository.getVolumes(getTblCmpOsVolumesExample(cloudId, volumeSearchCritical, userId));
        if (null == volumes) {
            return osVolumesRsp;
        }

        List<OSVolumeBasicInfo> volumeBasicInfos = volumes.stream().map(tblCmpOsVolumes -> {
            OSVolumeBasicInfo volumeBasicInfo = new OSVolumeBasicInfo();
            volumeBasicInfo.setVolumeBasicInfo(tblCmpOsVolumes);
            return volumeBasicInfo;
        }).collect(Collectors.toList());

        osVolumesRsp.setVolumes(volumeBasicInfos);
        return osVolumesRsp;
    }

    public OSVolumeWithDetailsRsp getVolumeDetails(String cloudId, String volumeId, String userId) throws WebSystemException
    {
        TblCmpOsVolumes tblCmpOsVolumes = osCinderRepository.getVolumeById(cloudId, volumeId);
        if (null == tblCmpOsVolumes || tblCmpOsVolumes.getEeStatus() == REMOVED)
        {
            throw new WebSystemException(ErrorCode.VOLUME_NOT_EXIST, ErrorLevel.INFO);
        }
        if (userId != null && !tblCmpOsVolumes.getEeUser().equals(userId))
        {
            throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
        }
        OSVolumeWithDetailsRsp osVolumeWithDetailsRsp = new OSVolumeWithDetailsRsp();
        OSVolumeInfo volumeInfo = new OSVolumeInfo();
        volumeInfo.setVolumeInfo(tblCmpOsVolumes);
        volumeInfo.setVolumeAttachment(getTblCmpOsVolumeAttachment(cloudId, volumeId));
        osVolumeWithDetailsRsp.setVolume(volumeInfo);

        cloudService.syncSingleData(cloudId, volumeId, SyncMsg.OS_VOLUME);

        return osVolumeWithDetailsRsp;
    }

    public ResponseEntity<Object> updateVolume(String cloudId, String volumeId, OSVolumeUpdateReq volumeUpdateReq, String bpId, String userId) throws WebSystemException
    {
        try
        {
            TblCmpOsVolumes tblCmpOsVolumes = osCinderRepository.getVolumeById(cloudId, volumeId);
            if (null == tblCmpOsVolumes || tblCmpOsVolumes.getEeStatus() == REMOVED)
            {
                throw new WebSystemException(ErrorCode.VOLUME_NOT_EXIST, ErrorLevel.INFO);
            }
            if (userId != null && !tblCmpOsVolumes.getEeUser().equals(userId))
            {
                throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
            }

            checkVolumeStatus(tblCmpOsVolumes);

            TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);
            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);
            ResponseEntity response = osClientV3.sendHttpRequestToCloudWithProject(volumeUpdateReq);
            if (response.getStatusCode() == HttpStatus.ACCEPTED || response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK)
            {
                OSVolumeWithDetailsRsp volumeWithDetailsRsp = gson.fromJson(gson.toJson(response.getBody()), OSVolumeWithDetailsRsp.class);
                if (volumeWithDetailsRsp == null || volumeWithDetailsRsp.getVolume() == null)
                {
                    return response;
                }
                eskSyncDataService.updateVolumeToDB(cloudId, volumeWithDetailsRsp.getVolume(), null, null);
            }
            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("add volume failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    public ResponseEntity<Object> removeVolume(String cloudId, String volumeId, String userId) throws WebSystemException
    {
        try
        {
            TblCmpOsVolumes tblCmpOsVolumes = osCinderRepository.getVolumeById(cloudId, volumeId);
            if (tblCmpOsVolumes == null)
            {
                LOGGER.error("get volume null: volume id {}", volumeId);
                throw new WebSystemException(ErrorCode.VOLUME_NOT_EXIST, ErrorLevel.INFO);
            }
            if (userId != null && !tblCmpOsVolumes.getEeUser().equals(userId))
            {
                throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
            }

            checkVolumeStatus(tblCmpOsVolumes);

            TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);
            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);
            ResponseEntity response = osClientV3.sendHttpRequestToCloudWithProject();
            if (response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.ACCEPTED || response.getStatusCode() == HttpStatus.NO_CONTENT)
            {
                tblCmpOsVolumes.setEeStatus(REMOVED);
                osCinderRepository.updateVolume(tblCmpOsVolumes);
            }
            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("remove volume failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    private TblCmpOsVolumesExample getTblCmpOsVolumesExample(String cloudId, OSVolumeSearchCritical volumeSearchCritical, String userId) throws WebSystemException
    {
        TblCmpOsVolumesExample example = new TblCmpOsVolumesExample();
        TblCmpOsVolumesExample.Criteria criteria = example.createCriteria();
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andEeStatusNotEqualTo(REMOVED);

        example.setOrderByClause("created_at desc");

        if (StringUtils.isNotEmpty(volumeSearchCritical.getName())) criteria.andDisplayNameLike("%" + volumeSearchCritical.getName() + "%");
        if (StringUtils.isNotEmpty(volumeSearchCritical.getStatus())) criteria.andStatusEqualTo(volumeSearchCritical.getStatus());

        if (StringUtils.isNotEmpty(userId)) criteria.andEeUserEqualTo(userId);

        return example;
    }

    private List<TblCmpOsVolumeAttachment> getTblCmpOsVolumeAttachment(String cloudId, String volumeId) throws WebSystemException
    {
        TblCmpOsVolumeAttachmentExample example = new TblCmpOsVolumeAttachmentExample();
        TblCmpOsVolumeAttachmentExample.Criteria criteria = example.createCriteria();
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andEeStatusNotEqualTo(REMOVED);
        criteria.andVolumeIdEqualTo(volumeId);

        return osCinderRepository.getVolumeAttachments(example);
    }

    private TblCmpOsSnapshots getTblCmpOsSnapshot(String cloudId, String snapshotId) throws WebSystemException
    {
        return osCinderRepository.getSnapshotById(cloudId, snapshotId);
    }

    public ResponseEntity actionVolume(String cloudId, String volumeId, Object request, String bpId, String userId)
    {
        try
        {
            TblCmpOsVolumes tblCmpOsVolume = osCinderRepository.getVolumeById(cloudId, volumeId);
            if (tblCmpOsVolume == null)
            {
                LOGGER.error("get volume null: volume id {}", volumeId);
                throw new WebSystemException(ErrorCode.VOLUME_NOT_EXIST, ErrorLevel.INFO);
            }
            if (userId != null && !tblCmpOsVolume.getEeUser().equals(userId))
            {
                throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
            }

            checkVolumeStatus(tblCmpOsVolume);

            TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);
            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);

            Map<String, String> osHeaders = new HashMap<>();
            osHeaders.put("Openstack-Api-Version", "volume 3.59");
            JsonObject jsonObject = JsonParser.parseString(gson.toJson(request)).getAsJsonObject();
            if (jsonObject.keySet().contains("os-set_bootable"))
            {
                osHeaders.remove("Openstack-Api-Version");
            }


            ResponseEntity response = osClientV3.sendHttpRequestToCloudWithProject(osHeaders);
            cloudService.syncSingleData(cloudId, volumeId, SyncMsg.OS_VOLUME);

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

    public OSExtVolumesWithDetailsRsp getVolumesWithDetailsPage(String cloudId, OSVolumeSearchCritical volumeSearchCritical, String userId)
    {
        OSExtVolumesWithDetailsRsp volumesWithDetailsRsp = new OSExtVolumesWithDetailsRsp();

        PageHelper.startPage(volumeSearchCritical.getPageNum(), volumeSearchCritical.getPageSize());
        List<TblCmpOsVolumes> volumes = osCinderRepository.getVolumes(getTblCmpOsVolumesExample(cloudId, volumeSearchCritical, userId));
        PageInfo<TblCmpOsVolumes> pageInfo = new PageInfo<>(volumes);

        if (null == volumes) {
            return volumesWithDetailsRsp;
        }

        List<OSExtVolumeInfo> volumeInfos = volumes.stream().map(tblCmpOsVolumes -> {
            OSExtVolumeInfo volumeInfo = new OSExtVolumeInfo();
            volumeInfo.setVolumeInfo(tblCmpOsVolumes);
            if (volumeInfo.getVolumeTypeId() != null)
            {
                volumeInfo.setVolumeType(getTblCmpOsVolumeTypes(cloudId, volumeInfo.getVolumeTypeId()));
            }
            volumeInfo.setVolumeAttachment(getTblCmpOsVolumeAttachment(cloudId, tblCmpOsVolumes.getId()));
            if (volumeInfo.getAttachments() != null) volumeInfo.getAttachments().forEach(osExtVolumeAttachmentInfo -> {
                TblCmpOsInstances tblCmpOsInstances = getTblCmpOsInstances(cloudId, osExtVolumeAttachmentInfo.getServerId());
                if (tblCmpOsInstances != null) osExtVolumeAttachmentInfo.setServerName(tblCmpOsInstances.getDisplayName());
            });
            if (volumeInfo.getSnapshotId() != null) volumeInfo.setSnapshotName(getTblCmpOsSnapshot(cloudId, tblCmpOsVolumes.getSnapshotId()));

            List<TblCmpOsBlockDeviceMapping> tblCmpOsBlockDeviceMappings = getTblCmpOsBlockDeviceMappings(cloudId, tblCmpOsVolumes.getId());
            if (! CollectionUtils.isEmpty(tblCmpOsBlockDeviceMappings))
            {
                volumeInfo.setSource(VolumeSource.CREATE_WITH_VM);
                volumeInfo.setDeleteOnTermination(BoolUtil.short2Bool(tblCmpOsBlockDeviceMappings.get(0).getDeleteOnTermination()));
            }

            return volumeInfo;
        }).collect(Collectors.toList());

        volumeInfos.forEach(volumeInfo -> cloudService.syncSingleData(cloudId, volumeInfo.getId(), SyncMsg.OS_VOLUME));

        volumesWithDetailsRsp.setVolumes(volumeInfos);
        volumesWithDetailsRsp.setTotalNum(pageInfo.getTotal());
        return volumesWithDetailsRsp;
    }

    private List<TblCmpOsBlockDeviceMapping> getTblCmpOsBlockDeviceMappings(String cloudId, String volumeId) throws WebSystemException
    {
        TblCmpOsBlockDeviceMappingExample example = new TblCmpOsBlockDeviceMappingExample();
        TblCmpOsBlockDeviceMappingExample.Criteria criteria = example.createCriteria();
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andVolumeIdEqualTo(volumeId);
        criteria.andEeStatusNotEqualTo(REMOVED);
        return osNovaRepository.getBlockDeviceMappings(example);
    }

    private TblCmpOsInstances getTblCmpOsInstances(String cloudId, String instanceId) throws WebSystemException
    {
        return osNovaRepository.getInstanceById(cloudId, instanceId);
    }

    private TblCmpOsVolumeTypes getTblCmpOsVolumeTypes(String cloudId, String volumeTypeId) throws WebSystemException
    {
        return osCinderRepository.getVolumeTypeById(cloudId, volumeTypeId);
    }

    public OSExtVolumeWithDetailsRsp getExtVolumeDetails(String cloudId, String volumeId, String userId) throws WebSystemException
    {
        TblCmpOsVolumes tblCmpOsVolumes = osCinderRepository.getVolumeById(cloudId, volumeId);
        if (null == tblCmpOsVolumes || tblCmpOsVolumes.getEeStatus() == REMOVED)
        {
            throw new WebSystemException(ErrorCode.VOLUME_NOT_EXIST, ErrorLevel.INFO);
        }
        if (userId != null && !tblCmpOsVolumes.getEeUser().equals(userId))
        {
            throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
        }

        OSExtVolumeWithDetailsRsp osVolumeWithDetailsRsp = new OSExtVolumeWithDetailsRsp();
        OSExtVolumeInfo volumeInfo = new OSExtVolumeInfo();
        volumeInfo.setVolumeInfo(tblCmpOsVolumes);
        if (volumeInfo.getVolumeTypeId() != null)
        {
            volumeInfo.setVolumeType(getTblCmpOsVolumeTypes(cloudId, volumeInfo.getVolumeTypeId()));
        }
        volumeInfo.setVolumeAttachment(getTblCmpOsVolumeAttachment(cloudId, volumeId));
        if (volumeInfo.getAttachments() != null) volumeInfo.getAttachments().forEach(osExtVolumeAttachmentInfo -> {
            TblCmpOsInstances tblCmpOsInstances = getTblCmpOsInstances(cloudId, osExtVolumeAttachmentInfo.getServerId());
            if (tblCmpOsInstances != null) osExtVolumeAttachmentInfo.setServerName(tblCmpOsInstances.getDisplayName());
        });
        if (volumeInfo.getSnapshotId() != null) volumeInfo.setSnapshotName(getTblCmpOsSnapshot(cloudId, tblCmpOsVolumes.getSnapshotId()));

        TblCmpOsBlockDeviceMappingExample example = new TblCmpOsBlockDeviceMappingExample();
        TblCmpOsBlockDeviceMappingExample.Criteria criteria = example.createCriteria();
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andVolumeIdEqualTo(volumeId);
        criteria.andEeStatusNotEqualTo(REMOVED);
        List<TblCmpOsBlockDeviceMapping> tblCmpOsBlockDeviceMappings = osNovaRepository.getBlockDeviceMappings(example);

        if (! CollectionUtils.isEmpty(tblCmpOsBlockDeviceMappings))
        {
            volumeInfo.setSource(VolumeSource.CREATE_WITH_VM);
            volumeInfo.setDeleteOnTermination(BoolUtil.short2Bool(tblCmpOsBlockDeviceMappings.get(0).getDeleteOnTermination()));
        }

        osVolumeWithDetailsRsp.setVolume(volumeInfo);

        cloudService.syncSingleData(cloudId, volumeId, SyncMsg.OS_VOLUME);

        return osVolumeWithDetailsRsp;
    }

    public void checkVolumeStatus(TblCmpOsVolumes tblCmpOsVolumes)
    {
        if (null == tblCmpOsVolumes || REMOVED == tblCmpOsVolumes.getEeStatus())
        {
            throw new WebSystemException(ErrorCode.VOLUME_NOT_EXIST, ErrorLevel.INFO);
        }
        else if (tblCmpOsVolumes.getStatus().equalsIgnoreCase("creating"))
        {
            throw new WebSystemException(ErrorCode.RESOURCE_IS_CREATING, ErrorLevel.INFO);
        }
    }
}
