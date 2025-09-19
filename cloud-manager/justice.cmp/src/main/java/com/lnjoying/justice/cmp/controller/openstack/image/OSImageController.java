package com.lnjoying.justice.cmp.controller.openstack.image;

import com.lnjoying.justice.cmp.common.CmpResources;
import com.lnjoying.justice.cmp.db.model.TblCmpOsImagesKey;
import com.lnjoying.justice.cmp.domain.dto.request.openstack.glance.OSImageCreateReq;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.glance.OSImageInfo;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.glance.OSImagesWithDetailsRsp;
import com.lnjoying.justice.cmp.entity.search.openstack.OSImageSearchCritical;
import com.lnjoying.justice.cmp.handler.actiondescription.i18n.zh_cn.CmpOpenstackActionDescriptionTemplate;
import com.lnjoying.justice.cmp.service.cloud.CloudService;
import com.lnjoying.justice.cmp.service.openstack.ImageService;
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

import static com.lnjoying.justice.commonweb.common.SwaggerConstants.*;
import static com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils.isAdmin;

@RestSchema(schemaId = "osimage")
@RequestMapping("/cmp/v1/{vendor}/clouds/{cloud_id}/v2/images")
@Api(value = "OpenStack Image Controller",tags = "OpenStack Image Controller")
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
        properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "os_image"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "os_images"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "OpenStack镜像"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "TblCmpOsImages")})})
public class OSImageController extends RestWebController
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    private ImageService imageService;

    @Autowired
    private CloudService cloudService;

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Create image", response = Object.class)
    @LNJoyAuditLog(value = "Create image",
            resourceMapperId = CmpResources.OS_IMAGE,
            actionDescriptionTemplate = CmpOpenstackActionDescriptionTemplate.Descriptions.CREATE_IMAGE,
            associatedResourceCnName = "镜像",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.METHOD_RETURN_VALUE,
                    resourceIdParseSPEL = "#obj?.body?.get('id')?.toString()", resourcePrimaryKeyClass = TblCmpOsImagesKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpOsImagesKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> addImage(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                            @ApiParam(value = "image", required = true, name = "image") @RequestBody OSImageCreateReq request,
                                            @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                            @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("add image, cloud:{}, request:{}", cloudId, request);

            if (! isAdmin() && ! cloudService.isOwner(cloudId, userId))
            {
                cloudService.checkCloudStatus(cloudId, false);
            }

            return imageService.addImage(cloudId, request, bpId, userId);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("add image failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/{image_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Show image", response = Object.class)
    public ResponseEntity<OSImageInfo> getImage(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                @ApiParam(value = "image_id", required = true, name = "image_id") @PathVariable("image_id") String imageId,
                                                @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                                @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("get image info. cloud:{}, flavor:{}", cloudId, imageId);

            OSImageInfo osImageInfo = imageService.getImage(cloudId, imageId);

            return ResponseEntity.ok(osImageInfo);
        }
        catch (Exception e)
        {
            LOGGER.error("get image failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "List images", response = Object.class)
    public ResponseEntity<OSImagesWithDetailsRsp> getImages(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                            @ApiParam(name = "limit") @RequestParam(required = false, value = "limit") Integer limit,
                                                            @ApiParam(name = "marker") @RequestParam(required = false, value = "marker") String marker,
                                                            @ApiParam(name = "name") @RequestParam(required = false, value = "name") String name,
                                                            @ApiParam(name = "owner") @RequestParam(required = false, value = "owner") String owner,
                                                            @ApiParam(name = "protected") @RequestParam(required = false, value = "protected") Boolean protect,
                                                            @ApiParam(name = "status") @RequestParam(required = false, value = "status") Integer status,
                                                            @ApiParam(name = "tag") @RequestParam(required = false, value = "tag") String tag,
                                                            @ApiParam(name = "visibility") @RequestParam(required = false, value = "visibility") String visibility,
                                                            @ApiParam(name = "os_hidden") @RequestParam(required = false, value = "os_hidden") Boolean osHidden,
                                                            @ApiParam(name = "member_status") @RequestParam(required = false, value = "member_status") String memberStatus,
                                                            @ApiParam(name = "size_max") @RequestParam(required = false, value = "size_max") String sizeMax,
                                                            @ApiParam(name = "size_min") @RequestParam(required = false, value = "size_min") String sizeMin,
                                                            @ApiParam(name = "created_at") @RequestParam(required = false, value = "created_at") String createdAt,
                                                            @ApiParam(name = "updated_at") @RequestParam(required = false, value = "updated_at") String updatedAt,
                                                            @ApiParam(name = "sort_dir") @RequestParam(required = false, value = "sort_dir") String sortDir,
                                                            @ApiParam(name = "sort_key") @RequestParam(required = false, value = "sort_key") String sortKey,
                                                            @ApiParam(name = "sort") @RequestParam(required = false, value = "sort") String sort,
                                                            @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                                            @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("get images. cloud:{}", cloudId);

            OSImageSearchCritical imageSearchCritical = new OSImageSearchCritical(limit, marker, name, owner, protect, status,
                    tag, visibility, osHidden, memberStatus, sizeMax, sizeMin, createdAt, updatedAt, sortDir, sortKey, sort,
                    null, null, null, null, null);

            OSImagesWithDetailsRsp getImagesRsp;

            getImagesRsp = imageService.getImages(cloudId, imageSearchCritical, null);

            return ResponseEntity.ok(getImagesRsp);
        }
        catch (Exception e)
        {
            LOGGER.error("get images failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @DeleteMapping(value = "/{image_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Delete image", response = Object.class)
    @LNJoyAuditLog(value = "Delete image",
            resourceMapperId = CmpResources.OS_IMAGE,
            actionDescriptionTemplate = CmpOpenstackActionDescriptionTemplate.Descriptions.DELETE_IMAGE,
            associatedResourceCnName = "镜像",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "imageId", resourcePrimaryKeyClass = TblCmpOsImagesKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpOsImagesKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> removeImage(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                               @ApiParam(value = "image_id", required = true, name = "image_id") @PathVariable("image_id")String imageId,
                                               @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("remove image, cloud:{}, image:{}", cloudId, imageId);

            return imageService.removeImage(cloudId, imageId, userId);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("remove image failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }
}
