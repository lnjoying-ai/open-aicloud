package com.lnjoying.justice.cmp.service.nextstack.repo;

import com.lnjoying.justice.cmp.common.nextstack.PhaseStatus;
import com.lnjoying.justice.cmp.common.SyncMsg;
import com.lnjoying.justice.cmp.db.model.*;
import com.lnjoying.justice.cmp.db.model.search.ImageSearchCritical;
import com.lnjoying.justice.cmp.db.repo.ImageRepository;
import com.lnjoying.justice.cmp.domain.dto.request.nextstack.repo.ImageCreateReq;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.repo.ImageDetailInfoRsp;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.repo.ImagesRsp;
import com.lnjoying.justice.cmp.service.UserService;
import com.lnjoying.justice.cmp.service.cloud.CloudService;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.common.ErrorLevel;
import com.micro.core.common.Utils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.lnjoying.justice.cmp.common.CommonPhaseStatus.SYNCING;
import static com.lnjoying.justice.cmp.common.CommonPhaseStatus.REMOVED;

@Service
public class ImageServiceBiz
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageServiceBiz.class);

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private CloudService cloudService;

    @Autowired
    private UserService userService;

    public ImagesRsp getImages(String cloudId, ImageSearchCritical imageSearchCritical, String userId, Boolean visibility) throws WebSystemException
    {
        try
        {
            TblCmpImageExample example = new TblCmpImageExample();
            TblCmpImageExample.Criteria criteria = example.createCriteria();
            criteria.andCloudIdEqualTo(cloudId);
            criteria.andEeStatusNotEqualTo(REMOVED);
            criteria.andPhaseStatusNotEqualTo((short)REMOVED);
            if (!StringUtils.isBlank(imageSearchCritical.getImageName()))
            {
                criteria.andImageNameLike("%" + imageSearchCritical.getImageName() + "%");
            }
            if (null != imageSearchCritical.getImageOsType())
            {
                criteria.andImageOsTypeEqualTo(imageSearchCritical.getImageOsType());
            }
            if (null != imageSearchCritical.getIsOk())
            {
                criteria.andPhaseStatusEqualTo((short) PhaseStatus.ADDED);
            }
            if (null != imageSearchCritical.getImageOsVendor())
            {
                criteria.andImageOsVendorEqualTo(imageSearchCritical.getImageOsVendor());
            }
            if (null != imageSearchCritical.getIsVm() && imageSearchCritical.getIsVm())
            {
                criteria.andFileIdFromAgentIsNull();
            }
            else if (null != imageSearchCritical.getIsVm() && !imageSearchCritical.getIsVm())
            {
                criteria.andFileIdFromAgentIsNotNull();
            }
            if (imageSearchCritical.getIsPublic())
            {
                criteria.andIsPublicEqualTo(true);
                if (visibility != null)
                {
                    criteria.andEeVisibilityIsNullOrEqualTo(visibility);
                }
            }
            else
            {
                criteria.andIsPublicEqualTo(false);
                if (!StringUtils.isBlank(userId))
                {
                    criteria.andEeUserEqualTo(userId);
                }
            }
            if (null != imageSearchCritical.getIsGpu())
            {
                if (imageSearchCritical.getIsGpu())
                {
                    criteria.andImageNameLike("%GPU%");
                }
                else
                {
                    criteria.andImageNameNotLike("%GPU%");
                }
            }

            ImagesRsp getImagesRsp = new ImagesRsp();

            long totalNum = imageRepository.countImageByExample(example);
            getImagesRsp.setTotalNum(totalNum);
            if (totalNum < 1) {
                return getImagesRsp;
            }

            int begin = ((imageSearchCritical.getPageNum() - 1) * imageSearchCritical.getPageSize());
            example.setOrderByClause("create_time desc, image_name desc");

            example.setStartRow(begin);
            example.setPageSize(imageSearchCritical.getPageSize());

            List<TblCmpImage> tblCmpImagesList = imageRepository.getImages(example);
            if (null == tblCmpImagesList)
            {
                return getImagesRsp;
            }

            List<ImageDetailInfoRsp> imageInfos = tblCmpImagesList.stream().map(tblRsImage -> {
                ImageDetailInfoRsp imageInfo = new ImageDetailInfoRsp();
                imageInfo.setImageDetailInfoRsp(tblRsImage);
                if (StringUtils.isNotEmpty(imageInfo.getEeBp())) imageInfo.setEeBpName(userService.getBpName(imageInfo.getEeBp()));
                if (StringUtils.isNotEmpty(imageInfo.getEeUser())) imageInfo.setEeUserName(userService.getUserName(imageInfo.getEeUser()));
                return imageInfo;
            }).collect(Collectors.toList());

            getImagesRsp.setImages(imageInfos);

            tblCmpImagesList.forEach(image -> cloudService.syncSingleData(cloudId, image.getImageId(), SyncMsg.NS_IMAGE));

            return getImagesRsp;
        }
        catch (Exception exception)
        {
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.ERROR);
        }
    }

    public ImageDetailInfoRsp getImage(String cloudId, String imageId, String userId) throws WebSystemException
    {
        TblCmpImage image = imageRepository.getImageById(cloudId, imageId);
        if (null == image || REMOVED == image.getPhaseStatus() || REMOVED == image.getEeStatus())
        {
            throw new WebSystemException(ErrorCode.IMAGE_NOT_EXIST, ErrorLevel.INFO);
        }
        if (!StringUtils.isBlank(userId)&&!image.getIsPublic() && !userId.equals(image.getEeUser()))
        {
            throw new WebSystemException(ErrorCode.InvalidAuthority, ErrorLevel.INFO);
        }
        ImageDetailInfoRsp imageDetailInfoRsp = new ImageDetailInfoRsp();
        imageDetailInfoRsp.setImageDetailInfoRsp(image);

        if (StringUtils.isNotEmpty(imageDetailInfoRsp.getEeBp())) imageDetailInfoRsp.setEeBpName(userService.getBpName(imageDetailInfoRsp.getEeBp()));
        if (StringUtils.isNotEmpty(imageDetailInfoRsp.getEeUser())) imageDetailInfoRsp.setEeUserName(userService.getUserName(imageDetailInfoRsp.getEeUser()));

        cloudService.syncSingleData(cloudId, imageId, SyncMsg.NS_IMAGE);

        return imageDetailInfoRsp;
    }

    public ResponseEntity removeImage(String cloudId, String imageId, String userId) throws WebSystemException
    {
        try
        {
            TblCmpImage tblCmpImage = imageRepository.getImageById(cloudId, imageId);

            if (tblCmpImage == null)
            {
                LOGGER.error("get image null: image id {}", imageId);
                throw new WebSystemException(ErrorCode.IMAGE_NOT_EXIST, ErrorLevel.INFO);
            }
            if (userId != null && !tblCmpImage.getEeUser().equals(userId))
            {
                throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
            }

            ResponseEntity response = cloudService.sendHttpRequestToCloud(cloudId);

            if (response.getStatusCode() == HttpStatus.NO_CONTENT || response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.ACCEPTED
                    || response.getStatusCode() == HttpStatus.NOT_FOUND || response.getStatusCode() == HttpStatus.CREATED)
            {
                tblCmpImage.setEeStatus(REMOVED);
                tblCmpImage.setUpdateTime(Utils.buildDate(System.currentTimeMillis()));
                imageRepository.updateImage(tblCmpImage);
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

    public int updateImageVisibility(String cloudId, String imageId, Boolean visibility) throws WebSystemException
    {
        try
        {
            TblCmpImage tblCmpImage = imageRepository.getImageById(cloudId, imageId);

            if (tblCmpImage == null)
            {
                LOGGER.error("get image null: image id {}", imageId);
                throw new WebSystemException(ErrorCode.IMAGE_NOT_EXIST, ErrorLevel.INFO);
            }

            tblCmpImage.setEeVisibility(visibility);
            return imageRepository.updateImage(tblCmpImage);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("update image visibility failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    public ResponseEntity createImage(String cloudId, ImageCreateReq imageInfo, String bpId, String userId) throws WebSystemException
    {
        try
        {
            ResponseEntity response = cloudService.sendHttpRequestToCloud(cloudId);
            if (response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK)
            {
                Map resultMap = (Map) response.getBody();
                if (null == resultMap)
                {
                    LOGGER.error("add image error");
                    throw new WebSystemException(ErrorCode.SystemError, ErrorLevel.INFO);
                }
                else
                {
                    String uuid = (String) resultMap.get("uuid");
                    if (StringUtils.isEmpty(uuid))
                    {
                        LOGGER.error("add image error");
                        throw new WebSystemException(ErrorCode.SystemError, ErrorLevel.INFO);
                    }
                    else
                    {
                        TblCmpImage tblCmpImage = new TblCmpImage();
                        tblCmpImage.setImageId(uuid);
                        tblCmpImage.setCloudId(cloudId);
                        tblCmpImage.setEeBp(bpId);
                        tblCmpImage.setEeUser(userId);
                        tblCmpImage.setEeStatus(SYNCING);

                        tblCmpImage.setImageName(imageInfo.getImageName());
                        tblCmpImage.setImageFormat(imageInfo.getImageFormat());
                        tblCmpImage.setImageOsType(imageInfo.getImageOsType());
                        tblCmpImage.setImageOsVendor(imageInfo.getImageOsVendor());
                        tblCmpImage.setImageOsVersion(imageInfo.getImageOsVersion());
                        tblCmpImage.setPhaseStatus((short)PhaseStatus.ADDED);
                        try
                        {
                            imageRepository.createImage(tblCmpImage);
                        }
                        catch (DuplicateKeyException e)
                        {
                            TblCmpImage updateTblCmpImage = imageRepository.getImageById(cloudId, uuid);
                            updateTblCmpImage.setEeBp(bpId);
                            updateTblCmpImage.setEeUser(userId);
                            updateTblCmpImage.setEeStatus(SYNCING);
                            imageRepository.updateImage(updateTblCmpImage);
                        }

                        cloudService.syncSingleData(cloudId, uuid, SyncMsg.NS_IMAGE);
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
            LOGGER.error("add image failed, message:{}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    public void checkImageUser(String cloudId, String imageId, String userId) throws WebSystemException
    {
        TblCmpImage tblCmpImage = imageRepository.getImageById(cloudId, imageId);
        if (tblCmpImage == null)
        {
            LOGGER.error("get image null: volume id {}", imageId);
            throw new WebSystemException(ErrorCode.IMAGE_NOT_EXIST, ErrorLevel.INFO);
        }
        if (userId != null && !tblCmpImage.getEeUser().equals(userId))
        {
            throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
        }
    }
}
