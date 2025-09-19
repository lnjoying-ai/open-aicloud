package com.lnjoying.justice.cmp.service.nextstack.repo;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lnjoying.justice.cmp.common.CreateResourceMsg;
import com.lnjoying.justice.cmp.common.SyncMsg;
import com.lnjoying.justice.cmp.common.constant.VolumeType;
import com.lnjoying.justice.cmp.common.nextstack.NSVolumeStatus;
import com.lnjoying.justice.cmp.db.model.*;
import com.lnjoying.justice.cmp.db.repo.ImageRepository;
import com.lnjoying.justice.cmp.db.repo.RepoRepository;
import com.lnjoying.justice.cmp.db.repo.VmComputeRepository;
import com.lnjoying.justice.cmp.domain.dto.request.nextstack.repo.VolumeCreateReq;
import com.lnjoying.justice.cmp.domain.dto.request.nextstack.repo.VolumeExportReq;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.repo.*;
import com.lnjoying.justice.cmp.domain.model.CreateResourceInfo;
import com.lnjoying.justice.cmp.entity.search.nextstack.repo.VolumeSearchCritical;
import com.lnjoying.justice.cmp.service.UserService;
import com.lnjoying.justice.cmp.service.cloud.CloudService;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.commonweb.util.JsonUtils;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.common.ErrorLevel;
import com.micro.core.common.Utils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.lnjoying.justice.cmp.common.CommonPhaseStatus.*;

@Service("volumeServiceBiz")
public class VolumeServiceBiz
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    private RepoRepository repoRepository;

    @Autowired
    private CloudService cloudService;

    @Autowired
    private VmComputeRepository vmComputeRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private UserService userService;

    public VolumesRsp getVolumes(String cloudId, VolumeSearchCritical volumeSearchCritical, String userId) throws WebSystemException
    {
        TblCmpVolumeExample example = new TblCmpVolumeExample();
        TblCmpVolumeExample.Criteria criteria = example.createCriteria();
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andEeStatusNotEqualTo(REMOVED);
        criteria.andPhaseStatusNotEqualTo(REMOVED);
        if (!StringUtils.isBlank(volumeSearchCritical.getVolumeName()))
        {
            criteria.andNameLike("%" + volumeSearchCritical.getVolumeName() + "%");
        }
        if (!StringUtils.isBlank(volumeSearchCritical.getPoolId()) )
        {
            criteria.andStoragePoolIdEqualTo(volumeSearchCritical.getPoolId());
        }
        if (null != volumeSearchCritical.getPhaseStatus() && volumeSearchCritical.getIsEqPhaseStatus())
        {
            criteria.andPhaseStatusEqualTo(volumeSearchCritical.getPhaseStatus());
        }
        else if (null != volumeSearchCritical.getPhaseStatus() && !volumeSearchCritical.getIsEqPhaseStatus() )
        {
            criteria.andPhaseStatusNotEqualTo(volumeSearchCritical.getPhaseStatus());
        }
        if (volumeSearchCritical.getIsRoot())
        {
            criteria.andTypeEqualTo(VolumeType.ROOT_DISK);
        }
        else
        {
            criteria.andTypeEqualTo(VolumeType.DATA_DISK);
        }

        if (!StringUtils.isBlank(userId))
        {
            criteria.andEeUserEqualTo(userId);
        }

        VolumesRsp getVolumesRsp = new VolumesRsp();

        long totalNum = repoRepository.countVolumes(example);

        getVolumesRsp.setTotalNum(totalNum);
        if (totalNum < 1)
        {
            return getVolumesRsp;
        }

        int begin = ((volumeSearchCritical.getPageNum() - 1) * volumeSearchCritical.getPageSize());
        example.setOrderByClause("create_time desc, volume_id desc");

        example.setStartRow(begin);
        example.setPageSize(volumeSearchCritical.getPageSize());
        List<VolumeVo> volumes = repoRepository.selectVolumes(example);

        getVolumesRsp.setVolumes(volumes);

        volumes.forEach(volumeVo -> {
            volumeVo.setEeBpName(userService.getBpName(volumeVo.getEeBp()));
            volumeVo.setEeUserName(userService.getUserName(volumeVo.getEeUser()));
        });

        volumes.forEach(volume -> cloudService.syncSingleData(cloudId, volume.getVolumeId(), SyncMsg.NS_VOLUME));

        return getVolumesRsp;
    }

    public RootVolumesRsp getRootVolumes(String cloudId, VolumeSearchCritical volumeSearchCritical, String userId) throws WebSystemException
    {
        TblCmpVolumeExample example = new TblCmpVolumeExample();
        TblCmpVolumeExample.Criteria criteria = example.createCriteria();
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andEeStatusNotEqualTo(REMOVED);
        criteria.andPhaseStatusNotEqualTo(REMOVED);
        criteria.andTypeEqualTo(VolumeType.ROOT_DISK);
        if (!StringUtils.isBlank(volumeSearchCritical.getVolumeName()))
        {
            criteria.andNameLike("%" + volumeSearchCritical.getVolumeName() + "%");
        }
        if (!StringUtils.isBlank(volumeSearchCritical.getPoolId()) )
        {
            criteria.andStoragePoolIdEqualTo(volumeSearchCritical.getPoolId());
        }
        if (null != volumeSearchCritical.getPhaseStatus() && volumeSearchCritical.getIsEqPhaseStatus())
        {
            criteria.andPhaseStatusEqualTo(volumeSearchCritical.getPhaseStatus());
        }
        else if (null != volumeSearchCritical.getPhaseStatus() && !volumeSearchCritical.getIsEqPhaseStatus() )
        {
            criteria.andPhaseStatusNotEqualTo(volumeSearchCritical.getPhaseStatus());
        }

        if (!StringUtils.isBlank(userId))
        {
            criteria.andEeUserEqualTo(userId);
        }

        RootVolumesRsp getVolumesRsp = new RootVolumesRsp();

        long totalNum = repoRepository.countRootVolumes(cloudId, example);

        getVolumesRsp.setTotalNum(totalNum);
        if (totalNum < 1)
        {
            return getVolumesRsp;
        }

        int begin = ((volumeSearchCritical.getPageNum() - 1) * volumeSearchCritical.getPageSize());
        example.setOrderByClause("create_time desc, volume_id desc");

        example.setStartRow(begin);
        example.setPageSize(volumeSearchCritical.getPageSize());
        List<RootVolumeVo> volumes = repoRepository.selectRootVolumes(cloudId, example);

        getVolumesRsp.setVolumes(volumes);

        volumes.forEach(volumeVo -> {
            volumeVo.setEeBpName(userService.getBpName(volumeVo.getEeBp()));
            volumeVo.setEeUserName(userService.getUserName(volumeVo.getEeUser()));
        });

        volumes.forEach(volume -> cloudService.syncSingleData(cloudId, volume.getVolumeId(), SyncMsg.NS_VOLUME));

        return getVolumesRsp;
    }

    public VolumeDetailInfoRsp getVolume(String cloudId, String volumeId, String userId) throws WebSystemException
    {
        TblCmpVolume volume = repoRepository.getVolumeById(cloudId, volumeId);
        if (null == volume || REMOVED == volume.getPhaseStatus() || REMOVED == volume.getEeStatus())
        {
            throw new WebSystemException(ErrorCode.VOLUME_NOT_EXIST, ErrorLevel.INFO);
        }
        if (!StringUtils.isBlank(userId) && !userId.equals(volume.getEeUser()))
        {
            throw new WebSystemException(ErrorCode.InvalidAuthority, ErrorLevel.INFO);
        }
        VolumeDetailInfoRsp volumeDetailInfoRsp = new VolumeDetailInfoRsp();
        volumeDetailInfoRsp.setVolumeDetailInfoRsp(volume);
        volumeDetailInfoRsp.setEeBpName(userService.getBpName(volume.getEeBp()));
        volumeDetailInfoRsp.setEeUserName(userService.getUserName(volume.getEeUser()));
        TblCmpStoragePool storagePool = repoRepository.getStoragePoolById(cloudId, volume.getStoragePoolId());
        if (null != storagePool)
        {
            volumeDetailInfoRsp.setStoragePoolName(storagePool.getName());
            volumeDetailInfoRsp.setStoragePoolId(storagePool.getStoragePoolId());
        }

        if (VolumeType.ROOT_DISK == volume.getType())
        {
            Integer imageOsType = vmComputeRepository.getImageOsTypeByVolumeId(cloudId, volumeId);
            volumeDetailInfoRsp.setImageOsType(imageOsType);
            if (StringUtils.isNotBlank(volume.getImageId()))
            {
                TblCmpImage tblCmpImage = imageRepository.getImageById(cloudId, volume.getImageId());
                if (null != tblCmpImage)
                {
                    volumeDetailInfoRsp.setImageName(tblCmpImage.getImageName());
                    volumeDetailInfoRsp.setImageOsVendor(tblCmpImage.getImageOsVendor() == null ? null : tblCmpImage.getImageOsVendor().intValue());

                    if (imageOsType == null)
                    {
                        volumeDetailInfoRsp.setImageOsType(tblCmpImage.getImageOsType() == null ? null : tblCmpImage.getImageOsType().intValue());
                    }
                }
            }
        }

        if (StringUtils.isNotEmpty(volumeDetailInfoRsp.getVmInstanceId()))
        {
            TblCmpVmInstance tblCmpVmInstance = vmComputeRepository.getVmInstanceById(cloudId, volumeDetailInfoRsp.getVmInstanceId());
            if (tblCmpVmInstance != null)
            {
                volumeDetailInfoRsp.setVmName(tblCmpVmInstance.getName());
            }
        }

        cloudService.syncSingleData(cloudId, volumeId, SyncMsg.NS_VOLUME);

        return volumeDetailInfoRsp;
    }

    public ResponseEntity removeVolume(String cloudId, String volumeId, String userId) throws WebSystemException
    {
        try
        {
            TblCmpVolume tblCmpVolume = repoRepository.getVolumeById(cloudId, volumeId);
            if (tblCmpVolume == null)
            {
                LOGGER.error("get volume null: volume id {}", volumeId);
                throw new WebSystemException(ErrorCode.VOLUME_NOT_EXIST, ErrorLevel.INFO);
            }
            if (userId != null && !tblCmpVolume.getEeUser().equals(userId))
            {
                throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
            }
            if (tblCmpVolume.getVolumeId().equals(tblCmpVolume.getEeId()) && tblCmpVolume.getPhaseStatus() == NSVolumeStatus.ADD_FAILED.getCode())
            {
                tblCmpVolume.setEeStatus(REMOVED);
                repoRepository.updateVolume(tblCmpVolume);
                return ResponseEntity.status(HttpStatus.OK).body(null);
            }

            checkVolumeStatus(tblCmpVolume);

            ResponseEntity response = cloudService.sendHttpRequestToCloud(cloudId);

            if (response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.ACCEPTED || response.getStatusCode() == HttpStatus.NO_CONTENT)
            {
                tblCmpVolume.setEeStatus(REMOVED);
                tblCmpVolume.setUpdateTime(Utils.buildDate(System.currentTimeMillis()));
                repoRepository.updateVolume(tblCmpVolume);
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

    public ResponseEntity createVolume(String cloudId, VolumeCreateReq volumeInfo, String bpId, String userId, boolean isRootDisk, String nodeIp, String vmId, String imageId) throws WebSystemException
    {
        try
        {

            TblCmpVolume tblCmpVolume = new TblCmpVolume();
            tblCmpVolume.setVolumeId(Utils.buildStr("EE_", Utils.assignUUId()));
            tblCmpVolume.setEeId(tblCmpVolume.getVolumeId());
            tblCmpVolume.setCloudId(cloudId);
            tblCmpVolume.setEeBp(bpId);
            tblCmpVolume.setEeUser(userId);
            tblCmpVolume.setEeStatus(WAIT_CREATE);
            tblCmpVolume.setPhaseStatus(NSVolumeStatus.ADDING.getCode());
            tblCmpVolume.setType(2);
            tblCmpVolume.setSize(volumeInfo.getSize());

            tblCmpVolume.setName(volumeInfo.getName());
            tblCmpVolume.setDescription(volumeInfo.getDescription());
            tblCmpVolume.setStoragePoolId(volumeInfo.getStoragePoolId());
            repoRepository.insertVolume(tblCmpVolume);

            cloudService.createResource(cloudId, tblCmpVolume.getEeId(), bpId, userId, CreateResourceMsg.NS_VOLUME, volumeInfo);

            return ResponseEntity.status(HttpStatus.CREATED).body(tblCmpVolume.getEeId());
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("add volume failed, message:{}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    public void createVolume(CreateResourceInfo createResourceInfo)
    {
        try
        {
            VolumeCreateReq volumeInfo = (VolumeCreateReq) createResourceInfo.getObject();
            String cloudId = createResourceInfo.getCloudId();
            String bpId = createResourceInfo.getBpId();
            String userId = createResourceInfo.getUserId();
            String eeId = createResourceInfo.getEeId();

            JsonObject jsonObject = JsonParser.parseString(JsonUtils.toJson(volumeInfo)).getAsJsonObject();
            jsonObject.remove("chargeType");
            jsonObject.remove("priceUnit");
            jsonObject.remove("period");

            String url = "/api/repo/v1/volumes";
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json");
            ResponseEntity response = cloudService.sendHttpRequestToCloud(cloudId, url, HttpMethod.POST, jsonObject, headers);
            if (response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK)
            {
                Map resultMap = (Map) response.getBody();
                if (null == resultMap)
                {
                    LOGGER.error("add volume error");
                    throw new WebSystemException(ErrorCode.SystemError, ErrorLevel.INFO);
                }
                else
                {
                    String volumeId = (String) resultMap.get("volumeId");
                    if (StringUtils.isEmpty(volumeId))
                    {
                        LOGGER.error("add volume error");
                        throw new WebSystemException(ErrorCode.SystemError, ErrorLevel.INFO);
                    }
                    else
                    {
                        TblCmpVolume tblCmpVolume = new TblCmpVolume();
                        tblCmpVolume.setVolumeId(volumeId);
                        tblCmpVolume.setCloudId(cloudId);
                        tblCmpVolume.setEeStatus(SYNCING);
                        tblCmpVolume.setEeId(eeId);
                        try
                        {
                            repoRepository.updateVolumeByEeIdSelective(tblCmpVolume);
                        }
                        catch (DuplicateKeyException e)
                        {
                            TblCmpVolume oldTblCmpVolume = repoRepository.getVolumeByEeId(cloudId, eeId);
                            TblCmpVolume updateTblCmpVolume = repoRepository.getVolumeById(cloudId, volumeId);

                            updateTblCmpVolume.setEeBp(bpId);
                            updateTblCmpVolume.setEeUser(userId);
                            updateTblCmpVolume.setEeStatus(SYNCING);
                            updateTblCmpVolume.setEeId(eeId);
                            updateTblCmpVolume.setChargeType(oldTblCmpVolume.getChargeType());
                            updateTblCmpVolume.setPeriod(oldTblCmpVolume.getPeriod());
                            updateTblCmpVolume.setPriceUnit(oldTblCmpVolume.getPriceUnit());
                            repoRepository.updateVolume(updateTblCmpVolume);

                            oldTblCmpVolume.setEeStatus(REMOVED);
                            repoRepository.updateVolume(oldTblCmpVolume);
                        }

                        cloudService.syncSingleData(cloudId, volumeId, SyncMsg.NS_VOLUME);
                    }
                }
            }
            else
            {
                TblCmpVolume tblCmpVolume = repoRepository.getVolumeByEeId(cloudId, eeId);
                tblCmpVolume.setPhaseStatus(NSVolumeStatus.ADD_FAILED.getCode());
                repoRepository.updateVolume(tblCmpVolume);
            }
        }
        catch (Exception e)
        {
            LOGGER.error("add volume failed, message:{}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    public ResponseEntity exportVolume(String cloudId, VolumeExportReq volumeExportReq, String bpId, String userId) throws WebSystemException
    {
        try
        {

            ResponseEntity response = cloudService.sendHttpRequestToCloud(cloudId);
            if (response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK)
            {
                Map resultMap = (Map) response.getBody();
                if (null == resultMap)
                {
                    LOGGER.error("export volume error");
                    throw new WebSystemException(ErrorCode.SystemError, ErrorLevel.INFO);
                }
                else
                {
                    String imageId = (String) resultMap.get("imageId");
                    if (StringUtils.isEmpty(imageId))
                    {
                        LOGGER.error("export volume error");
                        throw new WebSystemException(ErrorCode.SystemError, ErrorLevel.INFO);
                    }
                    else
                    {
                        TblCmpImage tblCmpImage = new TblCmpImage();
                        tblCmpImage.setImageId(imageId);
                        tblCmpImage.setCloudId(cloudId);
                        tblCmpImage.setEeBp(bpId);
                        tblCmpImage.setEeUser(userId);
                        tblCmpImage.setEeStatus(SYNCING);
                        try
                        {
                            imageRepository.createImage(tblCmpImage);
                        }
                        catch (DuplicateKeyException e)
                        {
                            TblCmpImage updateTblCmpImage = imageRepository.getImageById(cloudId, imageId);
                            updateTblCmpImage.setEeBp(bpId);
                            updateTblCmpImage.setEeUser(userId);
                            updateTblCmpImage.setEeStatus(SYNCING);
                            imageRepository.updateImage(updateTblCmpImage);
                        }

                        cloudService.syncSingleData(cloudId, imageId, SyncMsg.NS_IMAGE);
                    }
                }
            }
            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("export volume failed, message:{}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    public void checkVolumeStatus(TblCmpVolume tblCmpVolume)
    {
        if (null == tblCmpVolume || REMOVED == tblCmpVolume.getPhaseStatus() || REMOVED == tblCmpVolume.getEeStatus())
        {
            throw new WebSystemException(ErrorCode.VOLUME_NOT_EXIST, ErrorLevel.INFO);
        }
        else if (NSVolumeStatus.CREATING_STATUS.contains(tblCmpVolume.getPhaseStatus()))
        {
            throw new WebSystemException(ErrorCode.RESOURCE_IS_CREATING, ErrorLevel.INFO);
        }
    }

    public void checkVolumeUser(String cloudId, String volumeId, String userId) throws WebSystemException
    {
        TblCmpVolume tblCmpVolume = repoRepository.getVolumeById(cloudId, volumeId);
        if (tblCmpVolume == null)
        {
            LOGGER.error("get volume null: volume id {}", volumeId);
            throw new WebSystemException(ErrorCode.VOLUME_NOT_EXIST, ErrorLevel.INFO);
        }
        if (userId != null && !tblCmpVolume.getEeUser().equals(userId))
        {
            throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
        }
    }
}
