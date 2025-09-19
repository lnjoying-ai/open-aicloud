package com.lnjoying.justice.cmp.service.openstack.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lnjoying.justice.cmp.common.SyncMsg;
import com.lnjoying.justice.cmp.db.model.*;
import com.lnjoying.justice.cmp.db.repo.CloudRepository;
import com.lnjoying.justice.cmp.db.repo.OSCinderRepository;
import com.lnjoying.justice.cmp.domain.dto.request.openstack.cinder.OSVolumeSnapshotCreateReq;
import com.lnjoying.justice.cmp.domain.dto.request.openstack.cinder.OSVolumeSnapshotUpdateReq;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.cinder.*;
import com.lnjoying.justice.cmp.entity.search.openstack.OSVolumeSnapshotSearchCritical;
import com.lnjoying.justice.cmp.service.nextstack.baremetal.BaremetalComputeService;
import com.lnjoying.justice.cmp.service.cloud.CloudService;
import com.lnjoying.justice.cmp.service.openstack.VolumeSnapshotService;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.lnjoying.justice.cmp.common.CommonPhaseStatus.REMOVED;

@Service
public class OSVolumeSnapshotServiceImpl implements VolumeSnapshotService
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
    private ESKSyncDataService eskSyncDataService;

    public OSVolumeSnapshotsWithDetailsRsp getSnapshotsWithDetails(String cloudId, OSVolumeSnapshotSearchCritical volumeSnapshotSearchCritical, String userId) throws WebSystemException
    {
        OSVolumeSnapshotsWithDetailsRsp snapshotsWithDetailsRsp = new OSVolumeSnapshotsWithDetailsRsp();
        List<TblCmpOsSnapshots> snapshots = osCinderRepository.getSnapshots(getTblCmpOsSnapshotsExample(cloudId, volumeSnapshotSearchCritical, userId));
        if (null == snapshots) {
            return snapshotsWithDetailsRsp;
        }

        List<OSVolumeSnapshotInfo> volumeSnapshotInfos = snapshots.stream().map(tblCmpOsSnapshots -> {
            OSVolumeSnapshotInfo volumeSnapshotInfo = new OSVolumeSnapshotInfo();
            volumeSnapshotInfo.setSnapshotInfo(tblCmpOsSnapshots);
            return volumeSnapshotInfo;
        }).collect(Collectors.toList());

        volumeSnapshotInfos.forEach(volumeSnapshotInfo -> cloudService.syncSingleData(cloudId, volumeSnapshotInfo.getId(), SyncMsg.OS_SNAPSHOT));

        snapshotsWithDetailsRsp.setSnapshots(volumeSnapshotInfos);
        return snapshotsWithDetailsRsp;
    }

    public ResponseEntity<Object> addSnapshot(String cloudId, OSVolumeSnapshotCreateReq addVolumeSnapshotReq, String bpId, String userId) throws WebSystemException
    {
        try
        {
            TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);
            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);
            ResponseEntity response = osClientV3.sendHttpRequestToCloudWithProject(addVolumeSnapshotReq);
            if (response.getStatusCode() == HttpStatus.ACCEPTED || response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK)
            {
                OSVolumeSnapshotWithDetailsRsp volumeSnapshotWithDetailsRsp =  gson.fromJson(gson.toJson(response.getBody()), OSVolumeSnapshotWithDetailsRsp.class);

                if (volumeSnapshotWithDetailsRsp == null || volumeSnapshotWithDetailsRsp.getSnapshot() == null)
                {
                    return response;
                }
                eskSyncDataService.updateSnapshotToDB(cloudId, volumeSnapshotWithDetailsRsp.getSnapshot(), bpId, userId);
            }
            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("add snapshot failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    public OSVolumeSnapshotsRsp getSnapshots(String cloudId, OSVolumeSnapshotSearchCritical volumeSnapshotSearchCritical, String userId) throws WebSystemException
    {
        OSVolumeSnapshotsRsp osVolumeSnapshotsRsp = new OSVolumeSnapshotsRsp();
        List<TblCmpOsSnapshots> snapshots = osCinderRepository.getSnapshots(getTblCmpOsSnapshotsExample(cloudId, volumeSnapshotSearchCritical, userId));
        if (null == snapshots) {
            return osVolumeSnapshotsRsp;
        }

        List<OSVolumeSnapshotBasicInfo> volumeSnapshotBasicInfos = snapshots.stream().map(tblCmpOsSnapshots -> {
            OSVolumeSnapshotBasicInfo volumeSnapshotBasicInfo = new OSVolumeSnapshotBasicInfo();
            volumeSnapshotBasicInfo.setSnapshotBasicInfo(tblCmpOsSnapshots);
            return volumeSnapshotBasicInfo;
        }).collect(Collectors.toList());

        osVolumeSnapshotsRsp.setSnapshots(volumeSnapshotBasicInfos);
        return osVolumeSnapshotsRsp;
    }

    public OSVolumeSnapshotMetadata getSnapshotMetadata(String cloudId, String snapshotId, String userId)
    {
        TblCmpOsSnapshots tblCmpOsSnapshot = osCinderRepository.getSnapshotById(cloudId, snapshotId);
        if (tblCmpOsSnapshot == null)
        {
            LOGGER.error("get snapshot null: snapshot id {}", snapshotId);
            throw new WebSystemException(ErrorCode.VOLUME_SNAP_NOT_EXIST, ErrorLevel.INFO);
        }
        if (userId != null && !tblCmpOsSnapshot.getEeUser().equals(userId))
        {
            throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
        }

        OSVolumeSnapshotMetadata volumeSnapshotMetadata = new OSVolumeSnapshotMetadata();
        List<TblCmpOsSnapshotMetadata> metadatas = getTblCmpOsSnapshotMetadatas(cloudId, snapshotId);
        if (null == metadatas) {
            return volumeSnapshotMetadata;
        }

        volumeSnapshotMetadata.setMetadata(new HashMap<>());

        for (TblCmpOsSnapshotMetadata metadata : metadatas)
        {
            volumeSnapshotMetadata.getMetadata().put(metadata.getKey(), metadata.getValue());
        }

        return volumeSnapshotMetadata;
    }

    public ResponseEntity addSnapshotMetadata(String cloudId, String snapshotId, Map<String, String> metadata, String bpId, String userId)
    {
        return null;
    }

    public ResponseEntity updateSnapshotMetadata(String cloudId, String snapshotId, Map<String, String> metadata, String bpId, String userId)
    {
        return null;
    }

    public OSVolumeSnapshotWithDetailsRsp getSnapshotDetails(String cloudId, String snapshotId, String userId) throws WebSystemException
    {
        TblCmpOsSnapshots tblCmpOsSnapshots = osCinderRepository.getSnapshotById(cloudId, snapshotId);
        if (null == tblCmpOsSnapshots)
        {
            throw new WebSystemException(ErrorCode.VOLUME_SNAP_NOT_EXIST, ErrorLevel.INFO);
        }
        if (userId != null && !tblCmpOsSnapshots.getEeUser().equals(userId))
        {
            throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
        }
        OSVolumeSnapshotWithDetailsRsp osVolumeSnapshotWithDetailsRsp = new OSVolumeSnapshotWithDetailsRsp();
        OSVolumeSnapshotInfo snapshotInfo = new OSVolumeSnapshotInfo();
        snapshotInfo.setSnapshotInfo(tblCmpOsSnapshots);
        osVolumeSnapshotWithDetailsRsp.setSnapshot(snapshotInfo);

        cloudService.syncSingleData(cloudId, snapshotId, SyncMsg.OS_SNAPSHOT);

        return osVolumeSnapshotWithDetailsRsp;
    }

    public ResponseEntity<Object> updateSnapshot(String cloudId, String snapshotId, OSVolumeSnapshotUpdateReq volumeSnapshotUpdateReq, String bpId, String userId) throws WebSystemException
    {
        try
        {
            TblCmpOsSnapshots tblCmpOsSnapshots = osCinderRepository.getSnapshotById(cloudId, snapshotId);
            if (null == tblCmpOsSnapshots)
            {
                throw new WebSystemException(ErrorCode.VOLUME_SNAP_NOT_EXIST, ErrorLevel.INFO);
            }
            if (userId != null && !tblCmpOsSnapshots.getEeUser().equals(userId))
            {
                throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
            }

            TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);
            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);
            ResponseEntity response = osClientV3.sendHttpRequestToCloudWithProject(volumeSnapshotUpdateReq);
            if (response.getStatusCode() == HttpStatus.ACCEPTED || response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK)
            {
                OSVolumeSnapshotWithDetailsRsp volumeSnapshotWithDetailsRsp = gson.fromJson(gson.toJson(response.getBody()), OSVolumeSnapshotWithDetailsRsp.class);

                if (volumeSnapshotWithDetailsRsp == null || volumeSnapshotWithDetailsRsp.getSnapshot() == null)
                {
                    return response;
                }
                eskSyncDataService.updateSnapshotToDB(cloudId, volumeSnapshotWithDetailsRsp.getSnapshot(), null, null);
            }
            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("add snapshot failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    public ResponseEntity<Object> removeSnapshot(String cloudId, String snapshotId, String userId) throws WebSystemException
    {
        try
        {
            TblCmpOsSnapshots tblCmpOsSnapshot = osCinderRepository.getSnapshotById(cloudId, snapshotId);
            if (tblCmpOsSnapshot == null)
            {
                LOGGER.error("get snapshot null: snapshot id {}", snapshotId);
                throw new WebSystemException(ErrorCode.VOLUME_SNAP_NOT_EXIST, ErrorLevel.INFO);
            }
            if (userId != null && !tblCmpOsSnapshot.getEeUser().equals(userId))
            {
                throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
            }

            TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);
            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);
            ResponseEntity response = osClientV3.sendHttpRequestToCloudWithProject();
            if (response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.ACCEPTED || response.getStatusCode() == HttpStatus.NO_CONTENT)
            {
                tblCmpOsSnapshot.setEeStatus(REMOVED);
                osCinderRepository.updateSnapshot(tblCmpOsSnapshot);
            }
            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("remove snapshot failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    private TblCmpOsSnapshotsExample getTblCmpOsSnapshotsExample(String cloudId, OSVolumeSnapshotSearchCritical volumeSnapshotSearchCritical, String userId) throws WebSystemException
    {
        TblCmpOsSnapshotsExample example = new TblCmpOsSnapshotsExample();
        TblCmpOsSnapshotsExample.Criteria criteria = example.createCriteria();
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andEeStatusNotEqualTo(REMOVED);

        example.setOrderByClause("created_at desc");

        if (StringUtils.isNotEmpty(volumeSnapshotSearchCritical.getVolumeId())) criteria.andVolumeIdEqualTo(volumeSnapshotSearchCritical.getVolumeId());

        if (StringUtils.isNotEmpty(volumeSnapshotSearchCritical.getName())) criteria.andDisplayNameLike("%" + volumeSnapshotSearchCritical.getName() + "%");
        if (StringUtils.isNotEmpty(volumeSnapshotSearchCritical.getStatus())) criteria.andStatusEqualTo(volumeSnapshotSearchCritical.getStatus());

        if (StringUtils.isNotEmpty(userId)) criteria.andEeUserEqualTo(userId);

        return example;
    }

    private List<TblCmpOsSnapshotMetadata> getTblCmpOsSnapshotMetadatas(String cloudId, String snapshotId)
    {
        TblCmpOsSnapshotMetadataExample example = new TblCmpOsSnapshotMetadataExample();
        TblCmpOsSnapshotMetadataExample.Criteria criteria = example.createCriteria();
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andEeStatusNotEqualTo(REMOVED);
        criteria.andSnapshotIdEqualTo(snapshotId);
        List<TblCmpOsSnapshotMetadata> metadatas = osCinderRepository.getSnapshotMetadatas(example);
        return metadatas;
    }

    private TblCmpOsVolumes getTblCmpOsVolume(String cloudId, String volumeId) throws WebSystemException
    {
        return osCinderRepository.getVolumeById(cloudId, volumeId);
    }

    public OSExtVolumeSnapshotsWithDetailsRsp getSnapshotsWithDetailsPage(String cloudId, OSVolumeSnapshotSearchCritical volumeSnapshotSearchCritical, String userId)
    {
        OSExtVolumeSnapshotsWithDetailsRsp snapshotsWithDetailsRsp = new OSExtVolumeSnapshotsWithDetailsRsp();

        PageHelper.startPage(volumeSnapshotSearchCritical.getPageNum(), volumeSnapshotSearchCritical.getPageSize());
        List<TblCmpOsSnapshots> snapshots = osCinderRepository.getSnapshots(getTblCmpOsSnapshotsExample(cloudId, volumeSnapshotSearchCritical, userId));
        PageInfo<TblCmpOsSnapshots> pageInfo = new PageInfo<>(snapshots);

        if (null == snapshots) {
            return snapshotsWithDetailsRsp;
        }

        List<OSExtVolumeSnapshotInfo> volumeSnapshotInfos = snapshots.stream().map(tblCmpOsSnapshots -> {
            OSExtVolumeSnapshotInfo volumeSnapshotInfo = new OSExtVolumeSnapshotInfo();
            volumeSnapshotInfo.setSnapshotInfo(tblCmpOsSnapshots);
            volumeSnapshotInfo.setVolumeInfo(getTblCmpOsVolume(cloudId, volumeSnapshotInfo.getVolumeId()));
            return volumeSnapshotInfo;
        }).collect(Collectors.toList());

        volumeSnapshotInfos.forEach(volumeSnapshotInfo -> cloudService.syncSingleData(cloudId, volumeSnapshotInfo.getId(), SyncMsg.OS_SNAPSHOT));

        snapshotsWithDetailsRsp.setSnapshots(volumeSnapshotInfos);
        snapshotsWithDetailsRsp.setTotalNum(pageInfo.getTotal());
        return snapshotsWithDetailsRsp;
    }

    public OSExtVolumeSnapshotWithDetailsRsp getExtSnapshotDetails(String cloudId, String snapshotId, String userId)
    {
        TblCmpOsSnapshots tblCmpOsSnapshots = osCinderRepository.getSnapshotById(cloudId, snapshotId);
        if (null == tblCmpOsSnapshots)
        {
            throw new WebSystemException(ErrorCode.VOLUME_SNAP_NOT_EXIST, ErrorLevel.INFO);
        }
        if (userId != null && !tblCmpOsSnapshots.getEeUser().equals(userId))
        {
            throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
        }
        OSExtVolumeSnapshotWithDetailsRsp osVolumeSnapshotWithDetailsRsp = new OSExtVolumeSnapshotWithDetailsRsp();
        OSExtVolumeSnapshotInfo snapshotInfo = new OSExtVolumeSnapshotInfo();
        snapshotInfo.setSnapshotInfo(tblCmpOsSnapshots);
        snapshotInfo.setVolumeInfo(getTblCmpOsVolume(cloudId, snapshotInfo.getVolumeId()));
        osVolumeSnapshotWithDetailsRsp.setSnapshot(snapshotInfo);

        cloudService.syncSingleData(cloudId, snapshotId, SyncMsg.OS_SNAPSHOT);

        return osVolumeSnapshotWithDetailsRsp;
    }
}
