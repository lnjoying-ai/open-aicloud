package com.lnjoying.justice.cmp.controller.openstack.blockstorage;

import com.lnjoying.justice.cmp.common.CmpResources;
import com.lnjoying.justice.cmp.db.model.TblCmpOsVolumesKey;
import com.lnjoying.justice.cmp.domain.dto.request.openstack.cinder.OSVolumeCreateReq;
import com.lnjoying.justice.cmp.domain.dto.request.openstack.cinder.OSVolumeUpdateReq;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.cinder.OSVolumeWithDetailsRsp;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.cinder.OSVolumesRsp;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.cinder.OSVolumesWithDetailsRsp;
import com.lnjoying.justice.cmp.entity.search.openstack.OSVolumeSearchCritical;
import com.lnjoying.justice.cmp.handler.actiondescription.i18n.zh_cn.CmpOpenstackActionDescriptionTemplate;
import com.lnjoying.justice.cmp.service.cloud.CloudService;
import com.lnjoying.justice.cmp.service.openstack.VolumeService;
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

@RestSchema(schemaId = "osvolume")
@RequestMapping("/cmp/v1/{vendor}/clouds/{cloud_id}/v3")
@Api(value = "OpenStack Volume Controller",tags = "OpenStack Volume Controller")
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
        properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "os_volume"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "os_volumes"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "OpenStack卷"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "TblCmpOsVolumes")})})
public class OSVolumeController extends RestWebController
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    private VolumeService volumeService;

    @Autowired
    private CloudService cloudService;

    @GetMapping(value = "/{project_id}/volumes/detail", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "List Volumes With Details", response = Object.class)
    public ResponseEntity<OSVolumesWithDetailsRsp> getVolumesWithDetails(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                                         @ApiParam(value = "", required = true, name = "project_id") @PathVariable(value = "project_id") String projectId,
                                                                         @ApiParam(name = "all_tenants") @RequestParam(required = false, value = "all_tenants") String allTenants,
                                                                         @ApiParam(name = "sort") @RequestParam(required = false, value = "sort") String sort,
                                                                         @ApiParam(name = "limit") @RequestParam(required = false, value = "limit") Integer limit,
                                                                         @ApiParam(name = "offset") @RequestParam(required = false, value = "offset") Integer offset,
                                                                         @ApiParam(name = "marker") @RequestParam(required = false, value = "marker") String marker,
                                                                         @ApiParam(name = "with_count") @RequestParam(required = false, value = "with_count") Boolean withCount,
                                                                         @ApiParam(name = "created_at") @RequestParam(required = false, value = "created_at") String createdAt,
                                                                         @ApiParam(name = "updated_at") @RequestParam(required = false, value = "updated_at") String updatedAt,
                                                                         @ApiParam(name = "consumes_quota") @RequestParam(required = false, value = "consumes_quota") Boolean consumesQuota,
                                                                         @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("get volumes. cloud:{}", cloudId);

            String filterUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                filterUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, true);
            }

            OSVolumeSearchCritical volumeSearchCritical = new OSVolumeSearchCritical(projectId, allTenants, sort, limit,
                    offset, marker, withCount, createdAt, updatedAt, consumesQuota, null, null, null, null);

            OSVolumesWithDetailsRsp volumesWithDetails = volumeService.getVolumesWithDetails(cloudId, volumeSearchCritical, filterUserId);

            return ResponseEntity.ok(volumesWithDetails);
        }
        catch (Exception e)
        {
            LOGGER.error("get volumes failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @PostMapping(value = "/{project_id}/volumes", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Create Volume", response = Object.class)
    @LNJoyAuditLog(value = "Create Volume",
            resourceMapperId = CmpResources.OS_VOLUME,
            actionDescriptionTemplate = CmpOpenstackActionDescriptionTemplate.Descriptions.CREATE_VOLUME,
            associatedResourceCnName = "磁盘",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.METHOD_RETURN_VALUE,
                    resourceIdParseSPEL = "#obj?.body.get('volume')?.get('id')?.toString()", resourcePrimaryKeyClass = TblCmpOsVolumesKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpOsVolumesKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> addVolume(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                            @ApiParam(value = "", required = true, name = "project_id") @PathVariable(value = "project_id") String projectId,
                                            @ApiParam(value = "volume", required = true, name = "volume") @RequestBody OSVolumeCreateReq request,
                                            @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                            @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("add volume, cloud:{}, request:{}", cloudId, request);

            if (! isAdmin() && ! cloudService.isOwner(cloudId, userId))
            {
                cloudService.checkCloudStatus(cloudId, false);
            }

            return volumeService.addVolume(cloudId, request, bpId, userId);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("add volume failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/{project_id}/volumes", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "List Volumes", response = Object.class)
    public ResponseEntity<OSVolumesRsp> getVolumes(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                   @ApiParam(value = "", required = true, name = "project_id") @PathVariable(value = "project_id") String projectId,
                                                   @ApiParam(name = "all_tenants") @RequestParam(required = false, value = "all_tenants") String allTenants,
                                                   @ApiParam(name = "sort") @RequestParam(required = false, value = "sort") String sort,
                                                   @ApiParam(name = "limit") @RequestParam(required = false, value = "limit") Integer limit,
                                                   @ApiParam(name = "offset") @RequestParam(required = false, value = "offset") Integer offset,
                                                   @ApiParam(name = "marker") @RequestParam(required = false, value = "marker") String marker,
                                                   @ApiParam(name = "with_count") @RequestParam(required = false, value = "with_count") Boolean withCount,
                                                   @ApiParam(name = "created_at") @RequestParam(required = false, value = "created_at") String createdAt,
                                                   @ApiParam(name = "updated_at") @RequestParam(required = false, value = "updated_at") String updatedAt,
                                                   @ApiParam(name = "consumes_quota") @RequestParam(required = false, value = "consumes_quota") Boolean consumesQuota,
                                                   @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("get volumes. cloud:{}", cloudId);

            String filterUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                filterUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, true);
            }

            OSVolumeSearchCritical volumeSearchCritical = new OSVolumeSearchCritical(projectId, allTenants, sort, limit,
                    offset, marker, withCount, createdAt, updatedAt, consumesQuota, null, null, null, null);

            OSVolumesRsp volumesRsp = volumeService.getVolumes(cloudId, volumeSearchCritical, filterUserId);

            return ResponseEntity.ok(volumesRsp);
        }
        catch (Exception e)
        {
            LOGGER.error("get volumes failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/{project_id}/volumes/{volume_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Show Volume Details", response = Object.class)
    public ResponseEntity<OSVolumeWithDetailsRsp> getVolumeDetails(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                                   @ApiParam(value = "", required = true, name = "project_id") @PathVariable(value = "project_id") String projectId,
                                                                   @ApiParam(value = "volume_id", required = true, name = "volume_id") @PathVariable("volume_id") String volumeId,
                                                                   @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("get volume details. cloud:{}, flavor:{}", cloudId, volumeId);

            String operUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, true);
            }

            OSVolumeWithDetailsRsp volumeDetails = volumeService.getVolumeDetails(cloudId, volumeId, operUserId);

            return ResponseEntity.ok(volumeDetails);
        }
        catch (Exception e)
        {
            LOGGER.error("get volume details failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @PutMapping(value = "/{project_id}/volumes/{volume_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Update Volume", response = Object.class)
    @LNJoyAuditLog(value = "Update Volume",
            resourceMapperId = CmpResources.OS_VOLUME,
            actionDescriptionTemplate = CmpOpenstackActionDescriptionTemplate.Descriptions.UPDATE_VOLUME,
            associatedResourceCnName = "磁盘",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "volumeId", resourcePrimaryKeyClass = TblCmpOsVolumesKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpOsVolumesKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> updateVolume(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                               @ApiParam(value = "", required = true, name = "project_id") @PathVariable(value = "project_id") String projectId,
                                               @ApiParam(value = "", required = true, name = "volume_id") @PathVariable(value = "volume_id") String volumeId,
                                                        @ApiParam(value = "volume", required = true, name = "volume") @RequestBody OSVolumeUpdateReq request,
                                                        @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                                        @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("update volume, cloud:{}, request:{}", cloudId, request);

            String operUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, false);
            }

            return volumeService.updateVolume(cloudId, volumeId, request, bpId, operUserId);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("update volume failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @DeleteMapping(value = "/{project_id}/volumes/{volume_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Delete Volume", response = Object.class)
    @LNJoyAuditLog(value = "Delete Volume",
            resourceMapperId = CmpResources.OS_VOLUME,
            actionDescriptionTemplate = CmpOpenstackActionDescriptionTemplate.Descriptions.DELETE_VOLUME,
            associatedResourceCnName = "磁盘",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "volumeId", resourcePrimaryKeyClass = TblCmpOsVolumesKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpOsVolumesKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> removeVolume(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                               @ApiParam(value = "", required = true, name = "project_id") @PathVariable(value = "project_id") String projectId,
                                               @ApiParam(value = "volume_id", required = true, name = "volume_id") @PathVariable("volume_id")String volumeId,
                                               @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("remove volume, cloud:{}, volume_id:{}", cloudId, volumeId);

            String operUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, false);
            }

            return volumeService.removeVolume(cloudId, volumeId, operUserId);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("remove volume failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @PostMapping(value = "/{project_id}/volumes/{volume_id}/action", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Action volume", response = Object.class)
    @LNJoyAuditLog(value = "Action volume",
            resourceMapperId = CmpResources.OS_VOLUME,
            actionDescriptionTemplate = CmpOpenstackActionDescriptionTemplate.Descriptions.ACTION_VOLUME,
            associatedResourceCnName = "磁盘",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "volumeId", resourcePrimaryKeyClass = TblCmpOsVolumesKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpOsVolumesKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> actionVolume(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                               @ApiParam(value = "", required = true, name = "project_id") @PathVariable(value = "project_id") String projectId,
                                               @ApiParam(value = "", required = true, name = "volume_id") @PathVariable(value = "volume_id") String volumeId,
                                               @ApiParam(value = "action", required = true, name = "action") @RequestBody Object request,
                                               @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                               @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("action volume, cloud:{}, request:{}", cloudId, request);

            String operUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, false);
            }

            return volumeService.actionVolume(cloudId, volumeId, request, bpId, operUserId);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("update volume failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }
}
