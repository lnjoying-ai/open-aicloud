package com.lnjoying.justice.cmp.service.openstack.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lnjoying.justice.cmp.common.SyncMsg;
import com.lnjoying.justice.cmp.db.model.*;
import com.lnjoying.justice.cmp.db.repo.CloudRepository;
import com.lnjoying.justice.cmp.db.repo.OSGlanceRepository;
import com.lnjoying.justice.cmp.domain.dto.request.openstack.glance.OSImageCreateReq;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.glance.OSExtImagesWithDetailsRsp;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.glance.OSImageInfo;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.glance.OSImagesWithDetailsRsp;
import com.lnjoying.justice.cmp.entity.search.openstack.OSImageSearchCritical;
import com.lnjoying.justice.cmp.service.nextstack.baremetal.BaremetalComputeService;
import com.lnjoying.justice.cmp.service.cloud.CloudService;
import com.lnjoying.justice.cmp.service.openstack.ImageService;
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

import java.util.List;
import java.util.stream.Collectors;

import static com.lnjoying.justice.cmp.common.CommonPhaseStatus.REMOVED;
import static com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils.isAdmin;

@Service
public class OSImageServiceImpl implements ImageService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(BaremetalComputeService.class);

    private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS").serializeNulls().create();

    @Autowired
    private OSGlanceRepository osGlanceRepository;

    @Autowired
    private CloudRepository cloudRepository;

    @Autowired
    private CloudService cloudService;

    @Autowired
    private ESKSyncDataService eskSyncDataService;

    public ResponseEntity<Object> addImage(String cloudId, OSImageCreateReq imageCreateReq, String bpId, String userId) throws WebSystemException
    {
        try
        {
            TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);
            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);
            ResponseEntity response = osClientV3.sendHttpRequestToCloud(imageCreateReq);
            if (response.getStatusCode() == HttpStatus.ACCEPTED || response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK)
            {
                OSImageInfo osImageInfo = gson.fromJson(gson.toJson(response.getBody()), OSImageInfo.class);

                if (osImageInfo == null)
                {
                    return response;
                }
                eskSyncDataService.updateImageToDB(cloudId, osImageInfo, bpId, userId);
            }
            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("add image failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    public OSImageInfo getImage(String cloudId, String imageId) throws WebSystemException
    {
        TblCmpOsImages tblCmpOsImage = osGlanceRepository.getImagesById(cloudId, imageId);
        if (null == tblCmpOsImage)
        {
            throw new WebSystemException(ErrorCode.IMAGE_NOT_EXIST, ErrorLevel.INFO);
        }

        cloudService.syncSingleData(cloudId, imageId, SyncMsg.OS_IMAGE);

        OSImageInfo osImageInfo = new OSImageInfo();
        osImageInfo.setImageInfo(tblCmpOsImage);
        return osImageInfo;
    }

    public OSImagesWithDetailsRsp getImages(String cloudId, OSImageSearchCritical imageSearchCritical, String userId) throws WebSystemException
    {
        OSImagesWithDetailsRsp imagesWithDetailsRsp = new OSImagesWithDetailsRsp();
        List<TblCmpOsImages> images = osGlanceRepository.getImagess(getTblCmpOsImagesExample(cloudId, imageSearchCritical, userId));
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
        return imagesWithDetailsRsp;
    }

    public ResponseEntity<Object> removeImage(String cloudId, String imageId, String userId) throws WebSystemException
    {
        try
        {
            TblCmpOsImages tblCmpOsImage = osGlanceRepository.getImagesById(cloudId, imageId);
            if (tblCmpOsImage == null)
            {
                LOGGER.error("get image null: image id {}", imageId);
                throw new WebSystemException(ErrorCode.IMAGE_NOT_EXIST, ErrorLevel.INFO);
            }
            if (!isAdmin() && !cloudService.isOwner(cloudId, userId) && !tblCmpOsImage.getEeUser().equals(userId))
            {
                throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
            }

            TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);
            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);
            ResponseEntity response = osClientV3.sendHttpRequestToCloud();
            if (response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.ACCEPTED || response.getStatusCode() == HttpStatus.NO_CONTENT)
            {
                tblCmpOsImage.setEeStatus(REMOVED);
                osGlanceRepository.updateImages(tblCmpOsImage);
            }
            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("remove image failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    private TblCmpOsImagesExample getTblCmpOsImagesExample(String cloudId, OSImageSearchCritical imageSearchCritical, String userId) throws WebSystemException
    {
        TblCmpOsImagesExample example = new TblCmpOsImagesExample();
        TblCmpOsImagesExample.Criteria criteria = example.createCriteria();
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andEeStatusNotEqualTo(REMOVED);

        if (StringUtils.isNotEmpty(imageSearchCritical.getName())) criteria.andNameLike("%" + imageSearchCritical.getName() + "%");
        if (StringUtils.isNotEmpty(imageSearchCritical.getOwner())) criteria.andOwnerEqualTo(imageSearchCritical.getOwner());
        if (imageSearchCritical.getProtect() != null) criteria.andProtectedEqualTo(BoolUtil.bool2Short(imageSearchCritical.getProtect()));
        if (StringUtils.isNotEmpty(imageSearchCritical.getVisibility())) criteria.andVisibilityEqualTo(imageSearchCritical.getVisibility());
        if (imageSearchCritical.getOsHidden() != null) criteria.andOsHiddenEqualTo(BoolUtil.bool2Short(imageSearchCritical.getOsHidden()));
        if (StringUtils.isNotEmpty(imageSearchCritical.getSortKey()) && StringUtils.isNotEmpty(imageSearchCritical.getSortDir()))
        {
            example.setOrderByClause(String.format("%s %s", imageSearchCritical.getSortKey(), imageSearchCritical.getSortDir()));
        }
        else
        {
            example.setOrderByClause("created_at desc");
        }

        if (StringUtils.isNotEmpty(userId)) criteria.andEeUserEqualTo(userId);

        if (StringUtils.isNotEmpty(imageSearchCritical.getImageType()))
        {
            if (imageSearchCritical.getImageType().equalsIgnoreCase("snapshot"))
            {
                criteria.andImageTypeEqualTo("%\"image_type\":\"snapshot\"%");

                if (StringUtils.isNotEmpty(imageSearchCritical.getInstanceId()))
                {
                    criteria.andInstanceIdEqualTo("%\"instance_id\":\"" + imageSearchCritical.getInstanceId() + "\"%");
                }
            }
            else
            {
                criteria.andImageTypeNotEqualTo("%\"image_type\":\"snapshot\"%");
            }
        }
        if (imageSearchCritical.getStatusStr() != null) criteria.andStatusEqualTo(imageSearchCritical.getStatusStr());

        return example;
    }

    public OSExtImagesWithDetailsRsp getImagesPage(String cloudId, OSImageSearchCritical imageSearchCritical, String userId)
    {
        OSExtImagesWithDetailsRsp imagesWithDetailsRsp = new OSExtImagesWithDetailsRsp();

        PageHelper.startPage(imageSearchCritical.getPageNum(), imageSearchCritical.getPageSize());
        List<TblCmpOsImages> images = osGlanceRepository.getImagess(getTblCmpOsImagesExample(cloudId, imageSearchCritical, userId));
        PageInfo<TblCmpOsImages> pageInfo = new PageInfo<>(images);

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
        imagesWithDetailsRsp.setTotalNum(pageInfo.getTotal());
        return imagesWithDetailsRsp;
    }
}
