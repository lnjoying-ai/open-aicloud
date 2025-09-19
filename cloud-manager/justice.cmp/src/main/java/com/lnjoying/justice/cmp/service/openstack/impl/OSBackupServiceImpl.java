package com.lnjoying.justice.cmp.service.openstack.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lnjoying.justice.cmp.common.SyncMsg;
import com.lnjoying.justice.cmp.db.model.*;
import com.lnjoying.justice.cmp.db.repo.CloudRepository;
import com.lnjoying.justice.cmp.db.repo.OSCinderRepository;
import com.lnjoying.justice.cmp.domain.dto.request.openstack.cinder.*;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.cinder.*;
import com.lnjoying.justice.cmp.entity.search.openstack.OSVolumeBackupSearchCritical;
import com.lnjoying.justice.cmp.service.nextstack.baremetal.BaremetalComputeService;
import com.lnjoying.justice.cmp.service.cloud.CloudService;
import com.lnjoying.justice.cmp.service.openstack.BackupService;
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
public class OSBackupServiceImpl implements BackupService
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

    public OSVolumeBackupsWithDetailsRsp getBackupsWithDetails(String cloudId, OSVolumeBackupSearchCritical osVolumeBackupSearchCritical, String userId) throws WebSystemException
    {
        OSVolumeBackupsWithDetailsRsp volumeBackupsWithDetailsRsp = new OSVolumeBackupsWithDetailsRsp();
        List<TblCmpOsBackups> backups = osCinderRepository.getBackups(getTblCmpOsBackupsExample(cloudId, osVolumeBackupSearchCritical, userId));
        if (null == backups) {
            return volumeBackupsWithDetailsRsp;
        }

        List<OSVolumeBackupInfo> backupInfos = backups.stream().map(tblCmpOsBackup -> {
            OSVolumeBackupInfo backupInfo = new OSVolumeBackupInfo();
            backupInfo.setBackupInfo(tblCmpOsBackup);
            return backupInfo;
        }).collect(Collectors.toList());

        backupInfos.forEach(backupInfo -> cloudService.syncSingleData(cloudId, backupInfo.getId(), SyncMsg.OS_BACKUP));

        volumeBackupsWithDetailsRsp.setBackups(backupInfos);
        return volumeBackupsWithDetailsRsp;
    }

    public OSBackupWithDetailsRsp getBackupDetails(String cloudId, String backupId, String userId) throws WebSystemException
    {
        TblCmpOsBackups tblCmpOsBackup = osCinderRepository.getBackupById(cloudId, backupId);
        if (null == tblCmpOsBackup)
        {
            throw new WebSystemException(ErrorCode.VOLUME_BACKUP_NOT_EXIST, ErrorLevel.INFO);
        }
        if (userId != null && !tblCmpOsBackup.getEeUser().equals(userId))
        {
            throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
        }
        OSBackupWithDetailsRsp osBackupWithDetailsRsp = new OSBackupWithDetailsRsp();
        OSVolumeBackupInfo backupInfo = new OSVolumeBackupInfo();
        backupInfo.setBackupInfo(tblCmpOsBackup);
        osBackupWithDetailsRsp.setBackup(backupInfo);

        cloudService.syncSingleData(cloudId, backupId, SyncMsg.OS_BACKUP);

        return osBackupWithDetailsRsp;
    }

    public ResponseEntity removeBackup(String cloudId, String backupId, String userId) throws WebSystemException
    {
        try
        {
            TblCmpOsBackups tblCmpOsBackup = osCinderRepository.getBackupById(cloudId, backupId);
            if (tblCmpOsBackup == null)
            {
                LOGGER.error("get backup null: backup id {}", backupId);
                throw new WebSystemException(ErrorCode.VOLUME_BACKUP_NOT_EXIST, ErrorLevel.INFO);
            }
            if (userId != null && !tblCmpOsBackup.getEeUser().equals(userId))
            {
                throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
            }

            TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);
            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);
            ResponseEntity response = osClientV3.sendHttpRequestToCloudWithProject();
            if (response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.ACCEPTED || response.getStatusCode() == HttpStatus.NO_CONTENT)
            {
                tblCmpOsBackup.setEeStatus(REMOVED);
                osCinderRepository.updateBackup(tblCmpOsBackup);
            }
            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("remove backup failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    public ResponseEntity<Object> restoreBackup(String cloudId, String backupId, OSBackupRestoreReq backupRestoreReq, String bpId, String userId) throws WebSystemException
    {
        try
        {
            TblCmpOsBackups tblCmpOsBackup = osCinderRepository.getBackupById(cloudId, backupId);
            if (tblCmpOsBackup == null)
            {
                LOGGER.error("get backup null: backup id {}", backupId);
                throw new WebSystemException(ErrorCode.VOLUME_BACKUP_NOT_EXIST, ErrorLevel.INFO);
            }
            if (userId != null && !tblCmpOsBackup.getEeUser().equals(userId))
            {
                throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
            }

            TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);
            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);
            ResponseEntity response = osClientV3.sendHttpRequestToCloudWithProject(backupRestoreReq);
            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("restore backup failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    public ResponseEntity<Object> addBackup(String cloudId, OSBackupCreateReq backupCreateReq, String bpId, String userId) throws WebSystemException
    {
        try
        {
            TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);
            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);
            ResponseEntity response = osClientV3.sendHttpRequestToCloudWithProject(backupCreateReq);
            if (response.getStatusCode() == HttpStatus.ACCEPTED || response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK)
            {
                OSBackupWithDetailsRsp backupWithDetailsRsp = gson.fromJson(gson.toJson(response.getBody()), OSBackupWithDetailsRsp.class);
                if (backupWithDetailsRsp == null || backupWithDetailsRsp.getBackup() == null)
                {
                    return response;
                }
                eskSyncDataService.updateBackupToDB(cloudId, backupWithDetailsRsp.getBackup(), bpId, userId);
            }
            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("add backup failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    public ResponseEntity<Object> updateBackup(String cloudId, String backupId, OSBackupUpdateReq request, String bpId, String userId) throws WebSystemException
    {
        try
        {
            TblCmpOsBackups tblCmpOsBackup = osCinderRepository.getBackupById(cloudId, backupId);
            if (tblCmpOsBackup == null)
            {
                LOGGER.error("get backup null: backup id {}", backupId);
                throw new WebSystemException(ErrorCode.VOLUME_BACKUP_NOT_EXIST, ErrorLevel.INFO);
            }
            if (userId != null && !tblCmpOsBackup.getEeUser().equals(userId))
            {
                throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
            }

            TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);
            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);
            Map<String, String> osHeaders = new HashMap<>();
            osHeaders.put("Openstack-Api-Version", "volume 3.59");
            ResponseEntity response = osClientV3.sendHttpRequestToCloudWithProject(request, osHeaders);
            if (response.getStatusCode() == HttpStatus.ACCEPTED || response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK)
            {
                OSBackupWithDetailsRsp backupWithDetailsRsp = gson.fromJson(gson.toJson(response.getBody()), OSBackupWithDetailsRsp.class);
                if (backupWithDetailsRsp == null || backupWithDetailsRsp.getBackup() == null)
                {
                    return response;
                }
                eskSyncDataService.updateBackupToDB(cloudId, backupWithDetailsRsp.getBackup(), null, null);
            }
            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("add backup failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    public OSBackupsRsp getBackups(String cloudId, OSVolumeBackupSearchCritical osVolumeBackupSearchCritical, String userId) throws WebSystemException
    {
        OSBackupsRsp osBackupsRsp = new OSBackupsRsp();
        List<TblCmpOsBackups> backups = osCinderRepository.getBackups(getTblCmpOsBackupsExample(cloudId, osVolumeBackupSearchCritical, userId));
        if (null == backups) {
            return osBackupsRsp;
        }

        List<OSBackupBasicInfo> backupBasicInfos = backups.stream().map(tblCmpOsVolumes -> {
            OSBackupBasicInfo backupBasicInfo = new OSBackupBasicInfo();
            backupBasicInfo.setVolumeBasicInfo(tblCmpOsVolumes);
            return backupBasicInfo;
        }).collect(Collectors.toList());

        osBackupsRsp.setBackups(backupBasicInfos);
        return osBackupsRsp;
    }

    private TblCmpOsBackupsExample getTblCmpOsBackupsExample(String cloudId, OSVolumeBackupSearchCritical osVolumeBackupSearchCritical, String userId) throws WebSystemException
    {
        TblCmpOsBackupsExample example = new TblCmpOsBackupsExample();
        TblCmpOsBackupsExample.Criteria criteria = example.createCriteria();
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andEeStatusNotEqualTo(REMOVED);

        example.setOrderByClause("created_at desc");

        if (StringUtils.isNotEmpty(osVolumeBackupSearchCritical.getVolumeId())) criteria.andVolumeIdEqualTo(osVolumeBackupSearchCritical.getVolumeId());

        if (StringUtils.isNotEmpty(osVolumeBackupSearchCritical.getName())) criteria.andDisplayNameLike("%" + osVolumeBackupSearchCritical.getName() + "%");

        if (StringUtils.isNotEmpty(userId)) criteria.andEeUserEqualTo(userId);

        return example;
    }

    private TblCmpOsVolumes getTblCmpOsVolume(String cloudId, String volumeId) throws WebSystemException
    {
        return osCinderRepository.getVolumeById(cloudId, volumeId);
    }

    public OSExtVolumeBackupsWithDetailsRsp getBackupsWithDetailsPage(String cloudId, OSVolumeBackupSearchCritical osVolumeBackupSearchCritical, String userId)
    {
        OSExtVolumeBackupsWithDetailsRsp volumeBackupsWithDetailsRsp = new OSExtVolumeBackupsWithDetailsRsp();

        PageHelper.startPage(osVolumeBackupSearchCritical.getPageNum(), osVolumeBackupSearchCritical.getPageSize());
        List<TblCmpOsBackups> backups = osCinderRepository.getBackups(getTblCmpOsBackupsExample(cloudId, osVolumeBackupSearchCritical, userId));
        PageInfo<TblCmpOsBackups> pageInfo = new PageInfo<>(backups);

        if (null == backups) {
            return volumeBackupsWithDetailsRsp;
        }

        List<OSExtVolumeBackupInfo> backupInfos = backups.stream().map(tblCmpOsBackup -> {
            OSExtVolumeBackupInfo backupInfo = new OSExtVolumeBackupInfo();
            backupInfo.setBackupInfo(tblCmpOsBackup);
            backupInfo.setVolumeInfo(getTblCmpOsVolume(cloudId, backupInfo.getVolumeId()));
            return backupInfo;
        }).collect(Collectors.toList());

        backupInfos.forEach(backupInfo -> cloudService.syncSingleData(cloudId, backupInfo.getId(), SyncMsg.OS_BACKUP));

        volumeBackupsWithDetailsRsp.setBackups(backupInfos);
        volumeBackupsWithDetailsRsp.setTotalNum(pageInfo.getTotal());
        return volumeBackupsWithDetailsRsp;
    }

    public OSExtBackupWithDetailsRsp getExtBackupDetails(String cloudId, String backupId, String userId)
    {
        TblCmpOsBackups tblCmpOsBackup = osCinderRepository.getBackupById(cloudId, backupId);
        if (null == tblCmpOsBackup)
        {
            throw new WebSystemException(ErrorCode.VOLUME_BACKUP_NOT_EXIST, ErrorLevel.INFO);
        }
        if (userId != null && !tblCmpOsBackup.getEeUser().equals(userId))
        {
            throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
        }
        OSExtBackupWithDetailsRsp osBackupWithDetailsRsp = new OSExtBackupWithDetailsRsp();
        OSExtVolumeBackupInfo backupInfo = new OSExtVolumeBackupInfo();
        backupInfo.setBackupInfo(tblCmpOsBackup);
        backupInfo.setVolumeInfo(getTblCmpOsVolume(cloudId, backupInfo.getVolumeId()));
        osBackupWithDetailsRsp.setBackup(backupInfo);

        cloudService.syncSingleData(cloudId, backupId, SyncMsg.OS_BACKUP);

        return osBackupWithDetailsRsp;
    }
}
