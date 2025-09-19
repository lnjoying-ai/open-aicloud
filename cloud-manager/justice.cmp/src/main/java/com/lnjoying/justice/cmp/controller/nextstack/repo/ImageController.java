package com.lnjoying.justice.cmp.controller.nextstack.repo;

import com.lnjoying.justice.cmp.common.CmpResources;
import com.lnjoying.justice.cmp.config.DescriptionConfig;
import com.lnjoying.justice.cmp.db.model.TblCmpImageKey;
import com.lnjoying.justice.cmp.db.model.search.ImageSearchCritical;
import com.lnjoying.justice.cmp.domain.dto.request.nextstack.baremetal.CommonReq;
import com.lnjoying.justice.cmp.domain.dto.request.nextstack.repo.ImageCreateReq;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.BaseRsp;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.repo.ImageDetailInfoRsp;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.repo.ImagesRsp;
import com.lnjoying.justice.cmp.handler.actiondescription.i18n.zh_cn.CmpNextStackActionDescriptionTemplate;
import com.lnjoying.justice.cmp.service.cloud.CloudService;
import com.lnjoying.justice.cmp.service.nextstack.repo.ImageServiceBiz;
import com.lnjoying.justice.commonweb.controller.RestWebController;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.LNJoyAuditLog;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.ResourceIdExtractConfiguration;
import com.lnjoying.justice.commonweb.handler.aspect.enums.ResourceIdLocationType;
import com.lnjoying.justice.schema.constant.UserHeadInfo;
import io.swagger.annotations.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.io.IOException;

import static com.lnjoying.justice.commonweb.common.SwaggerConstants.*;
import static com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils.isAdmin;

@RestSchema(schemaId = "image")
@RequestMapping("/cmp/v1/{vendor}/clouds/{cloud_id}/api/repo/v1")
@Api(value = "Image Controller",tags = "Image Controller")
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
        properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "ns_image"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "ns_images"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "镜像"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "TblCmpImage")})})
public class ImageController extends RestWebController
{
    private static Logger LOGGER = LogManager.getLogger();

    private static final String REG_UUID = "[0-9a-f-]{32}";

    @Autowired
    private ImageServiceBiz imageService;

    @Autowired
    private CloudService cloudService;

    @GetMapping(value = "/images", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get images", response = ImagesRsp.class, notes = DescriptionConfig.GET_IMAGES)
    public Object getImages(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                            @ApiParam(name = "name") @RequestParam(required = false, value = "name") String imageName,
                            @ApiParam(name = "image_os_type") @RequestParam(required = false, value = "image_os_type") Short imageOsType,
                            @ApiParam(name = "image_os_vendor") @RequestParam(required = false, value = "image_os_vendor") Short imageOsVendor,
                            @ApiParam(name = "is_vm") @RequestParam(required = false, value = "is_vm") Boolean isVm,
                            @ApiParam(name = "is_gpu") @RequestParam(required = false, value = "is_gpu") Boolean isGpu,
                            @ApiParam(name = "is_ok") @RequestParam(required = false, value = "is_ok") Boolean isOk,
                            @ApiParam(name = "is_public") @RequestParam(required = false, value = "is_public") Boolean isPublic,
                            @ApiParam(name = "page_size") @RequestParam(required = false, value = "page_size")  Integer page_size,
                            @ApiParam(name = "page_num") @RequestParam(required = false, value = "page_num")  Integer page_num,
                            @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.debug("get image list, cloud:{}", cloudId);

            String filterUserId = userId;
            Boolean visibility = null;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                filterUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, true);
                visibility = true;
            }

            ImageSearchCritical pageSearchCritical = new ImageSearchCritical();
            pageSearchCritical.setImageName(imageName);
            pageSearchCritical.setImageOsType(imageOsType);
            pageSearchCritical.setImageOsVendor(imageOsVendor);
            pageSearchCritical.setIsVm(isVm);
            if (isPublic == null) isPublic = true;
            pageSearchCritical.setIsPublic(isPublic);
            if (null != isOk) {
                pageSearchCritical.setIsOk(isOk);
            }
            if (null != isGpu) {
                pageSearchCritical.setIsGpu(isGpu);
            }

            if (page_num != null) pageSearchCritical.setPageNum(page_num);
            if (page_size != null) pageSearchCritical.setPageSize(page_size);

            return imageService.getImages(cloudId, pageSearchCritical, filterUserId, visibility);
        }
        catch (Exception e)
        {
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/images/{imageId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get image detail info", response = ImageDetailInfoRsp.class, notes = DescriptionConfig.GET_IMAGE)
    public Object getImage(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                           @ApiParam(value = "imageId", required = true, name = "imageId") @PathVariable("imageId") @Pattern(regexp = REG_UUID) String imageId,
                           @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId) throws IOException
    {
        try
        {
            LOGGER.debug("get image detail info, cloud:{}, imageId: {}", cloudId, imageId);

            String operUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, true);
            }

            return imageService.getImage(cloudId, imageId, operUserId);
        }
        catch (Exception e)
        {
            throw throwWebException(e);
        }
    }

    @PostMapping(value = "/images", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "create new image",response = BaseRsp.class, notes = DescriptionConfig.POST_IMAGE)
    @LNJoyAuditLog(value = "create new image",
            resourceMapperId = CmpResources.NS_IMAGE,
            actionDescriptionTemplate = CmpNextStackActionDescriptionTemplate.Descriptions.ADD_IMAGE,
            associatedResourceCnName = "镜像",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.METHOD_RETURN_VALUE,
                    resourceIdParseSPEL = "#obj?.body?.get('uuid')?.toString()", resourcePrimaryKeyClass = TblCmpImageKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpImageKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> createImage(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                              @ApiParam(value = "imageCreateReq", required = true, name = "imageCreateReq")@RequestBody @Valid ImageCreateReq image,
                                              @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                              @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("create image, cloud:{}, request:{}", cloudId, image);

            if (! isAdmin() && ! cloudService.isOwner(cloudId, userId))
            {
                cloudService.checkCloudStatus(cloudId, false);
            }

            return imageService.createImage(cloudId, image, bpId, userId);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw throwWebException(e);
        }
    }

    @DeleteMapping(value = "/images/{imageId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "remove image",response = BaseRsp.class, notes = DescriptionConfig.DEL_IMAGE)
    @LNJoyAuditLog(value = "remove image",
            resourceMapperId = CmpResources.NS_IMAGE,
            actionDescriptionTemplate = CmpNextStackActionDescriptionTemplate.Descriptions.DELETE_IMAGE,
            associatedResourceCnName = "镜像",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "imageId", resourcePrimaryKeyClass = TblCmpImageKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpImageKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> removeImage(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                              @ApiParam(value = "imageId", required = true, name = "imageId")@PathVariable("imageId") @Pattern(regexp = REG_UUID) String imageId,
                                              @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.debug("remove image, cloud:{}, imageId:{}", cloudId, imageId);

            String operUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, false);
            }

            return imageService.removeImage(cloudId, imageId, operUserId);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw throwWebException(e);
        }
    }

    @PutMapping(value = "/images/{image_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "update image",response = Object.class)
    @LNJoyAuditLog(value = "update image",
            resourceMapperId = CmpResources.NS_IMAGE,
            actionDescriptionTemplate = CmpNextStackActionDescriptionTemplate.Descriptions.UPDATE_IMAGE,
            associatedResourceCnName = "镜像",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "imageId", resourcePrimaryKeyClass = TblCmpImageKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpImageKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public Object updateImage(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                              @ApiParam(value = "image_id", required = true, name = "image_id")@PathVariable("image_id") @Pattern(regexp = REG_UUID) String imageId,
                              @ApiParam(value = "CommonReq", required = true, name = "CommonReq")@RequestBody @Valid CommonReq commonReq,
                              @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("update image, imageId: {}", imageId);

            if (! isAdmin() && ! cloudService.isOwner(cloudId, userId))
            {
                cloudService.checkCloudStatus(cloudId, false);
                imageService.checkImageUser(cloudId, imageId, userId);
            }

            return cloudService.sendHttpRequestToCloud(cloudId);
        }
        catch (Exception e)
        {
            LOGGER.error("update image error: {}",e.getMessage());
            throw throwWebException(e);
        }
    }

    @PutMapping(value = "/images/{image_id}/visibility", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "update image visibility",response = Object.class)
    @LNJoyAuditLog(value = "update image visibility",
            resourceMapperId = CmpResources.NS_IMAGE,
            actionDescriptionTemplate = CmpNextStackActionDescriptionTemplate.Descriptions.UPDATE_IMAGE_VISIBILITY,
            associatedResourceCnName = "镜像",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "imageId", resourcePrimaryKeyClass = TblCmpImageKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpImageKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public Object updateImageVisibility(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                        @ApiParam(value = "image_id", required = true, name = "image_id")@PathVariable("image_id") @Pattern(regexp = REG_UUID) String imageId,
                                        @ApiParam(name = "visibility") @RequestParam(required = true, value = "visibility") Boolean visibility,
                                        @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("update image visibility, imageId: {}, visibility: {}", imageId, visibility);

            if (! isAdmin() && ! cloudService.isOwner(cloudId, userId))
            {
                imageService.checkImageUser(cloudId, imageId, userId);
            }

            return imageService.updateImageVisibility(cloudId, imageId, visibility);
        }
        catch (Exception e)
        {
            LOGGER.error("update image error: {}",e.getMessage());
            throw throwWebException(e);
        }
    }
}
