package com.lnjoying.justice.cmp.controller.openstack.blockstorage;

import com.lnjoying.justice.cmp.common.CmpResources;
import com.lnjoying.justice.cmp.db.model.TblCmpOsSnapshotsKey;
import com.lnjoying.justice.cmp.domain.dto.request.openstack.cinder.OSVolumeSnapshotCreateReq;
import com.lnjoying.justice.cmp.domain.dto.request.openstack.cinder.OSVolumeSnapshotUpdateReq;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.cinder.OSVolumeSnapshotMetadata;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.cinder.OSVolumeSnapshotWithDetailsRsp;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.cinder.OSVolumeSnapshotsRsp;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.cinder.OSVolumeSnapshotsWithDetailsRsp;
import com.lnjoying.justice.cmp.entity.search.openstack.OSVolumeSnapshotSearchCritical;
import com.lnjoying.justice.cmp.handler.actiondescription.i18n.zh_cn.CmpOpenstackActionDescriptionTemplate;
import com.lnjoying.justice.cmp.service.cloud.CloudService;
import com.lnjoying.justice.cmp.service.openstack.VolumeSnapshotService;
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

import java.util.Map;

import static com.lnjoying.justice.commonweb.common.SwaggerConstants.*;
import static com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils.isAdmin;

@RestSchema(schemaId = "osvolumesnapshot")
@RequestMapping("/cmp/v1/{vendor}/clouds/{cloud_id}/v3")
@Api(value = "OpenStack Volume Snapshot Controller",tags = "OpenStack Volume Snapshot Controller")
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
        properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "os_volume_snapshot"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "os_volume_snapshots"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "OpenStack卷快照"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "TblCmpOsSnapshots")})})
public class OSVolumeSnapshotController extends RestWebController
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    private VolumeSnapshotService volumeSnapshotService;

    @Autowired
    private CloudService cloudService;

    @GetMapping(value = "/{project_id}/snapshots/detail", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "List Snapshots With Details", response = Object.class)
    public ResponseEntity<OSVolumeSnapshotsWithDetailsRsp> getSnapshotsWithDetails(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                                                   @ApiParam(value = "", required = true, name = "project_id") @PathVariable(value = "project_id") String projectId,
                                                                                   @ApiParam(name = "all_tenants") @RequestParam(required = false, value = "all_tenants") String allTenants,
                                                                                   @ApiParam(name = "sort") @RequestParam(required = false, value = "sort") String sort,
                                                                                   @ApiParam(name = "limit") @RequestParam(required = false, value = "limit") Integer limit,
                                                                                   @ApiParam(name = "offset") @RequestParam(required = false, value = "offset") Integer offset,
                                                                                   @ApiParam(name = "marker") @RequestParam(required = false, value = "marker") String marker,
                                                                                   @ApiParam(name = "with_count") @RequestParam(required = false, value = "with_count") Boolean withCount,
                                                                                   @ApiParam(name = "consumes_quota") @RequestParam(required = false, value = "consumes_quota") Boolean consumesQuota,
                                                                                   @ApiParam(name = "volume_id") @RequestParam(required = false, value = "volume_id") String volumeId,
                                                                                   @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("get snapshots details. cloud:{}", cloudId);

            String filterUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                filterUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, true);
            }

            OSVolumeSnapshotSearchCritical volumeSnapshotSearchCritical = new OSVolumeSnapshotSearchCritical(projectId,
                    allTenants, sort, limit, offset, marker, withCount, consumesQuota, volumeId, null, null, null, null);

            OSVolumeSnapshotsWithDetailsRsp volumeSnapshotsWithDetailsRsp = volumeSnapshotService.getSnapshotsWithDetails(cloudId, volumeSnapshotSearchCritical, filterUserId);

            return ResponseEntity.ok(volumeSnapshotsWithDetailsRsp);
        }
        catch (Exception e)
        {
            LOGGER.error("get snapshots details failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @PostMapping(value = "/{project_id}/snapshots", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Create Snapshot", response = Object.class)
    @LNJoyAuditLog(value = "Create Snapshot",
            resourceMapperId = CmpResources.OS_VOLUME_SNAPSHOT,
            actionDescriptionTemplate = CmpOpenstackActionDescriptionTemplate.Descriptions.CREATE_SNAPSHOT,
            associatedResourceCnName = "磁盘快照",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.METHOD_RETURN_VALUE,
                    resourceIdParseSPEL = "#obj?.body.get('snapshot')?.get('id')?.toString()", resourcePrimaryKeyClass = TblCmpOsSnapshotsKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpOsSnapshotsKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> addSnapshot(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                            @ApiParam(value = "snapshot", required = true, name = "snapshot") @RequestBody OSVolumeSnapshotCreateReq request,
                                            @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                            @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("add snapshot, cloud:{}, request:{}", cloudId, request);

            if (! isAdmin() && ! cloudService.isOwner(cloudId, userId))
            {
                cloudService.checkCloudStatus(cloudId, false);
            }

            return volumeSnapshotService.addSnapshot(cloudId, request, bpId, userId);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("add snapshot failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/{project_id}/snapshots", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "List Snapshots", response = Object.class)
    public ResponseEntity<OSVolumeSnapshotsRsp> getSnapshots(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                             @ApiParam(value = "", required = true, name = "project_id") @PathVariable(value = "project_id") String projectId,
                                                             @ApiParam(name = "all_tenants") @RequestParam(required = false, value = "all_tenants") String allTenants,
                                                             @ApiParam(name = "sort") @RequestParam(required = false, value = "sort") String sort,
                                                             @ApiParam(name = "limit") @RequestParam(required = false, value = "limit") Integer limit,
                                                             @ApiParam(name = "offset") @RequestParam(required = false, value = "offset") Integer offset,
                                                             @ApiParam(name = "marker") @RequestParam(required = false, value = "marker") String marker,
                                                             @ApiParam(name = "with_count") @RequestParam(required = false, value = "with_count") Boolean withCount,
                                                             @ApiParam(name = "consumes_quota") @RequestParam(required = false, value = "consumes_quota") Boolean consumesQuota,
                                                             @ApiParam(name = "volume_id") @RequestParam(required = false, value = "volume_id") String volumeId,
                                                             @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("get snapshots. cloud:{}", cloudId);

            String filterUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                filterUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, true);
            }

            OSVolumeSnapshotSearchCritical volumeSnapshotSearchCritical = new OSVolumeSnapshotSearchCritical(projectId,
                    allTenants, sort, limit, offset, marker, withCount, consumesQuota, volumeId, null, null, null, null);

            OSVolumeSnapshotsRsp volumeSnapshotsRsp = volumeSnapshotService.getSnapshots(cloudId, volumeSnapshotSearchCritical, filterUserId);

            return ResponseEntity.ok(volumeSnapshotsRsp);
        }
        catch (Exception e)
        {
            LOGGER.error("get snapshots failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/{project_id}/snapshots/{snapshot_id}/metadata", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Show snapshot metadata", response = Object.class)
    public ResponseEntity<OSVolumeSnapshotMetadata> getSnapshotMetadata(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                                        @ApiParam(value = "", required = true, name = "project_id") @PathVariable(value = "project_id") String projectId,
                                                                        @ApiParam(value = "snapshot_id", required = true, name = "snapshot_id") @PathVariable("snapshot_id") String snapshotId,
                                                                        @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("get snapshot metadata. cloud:{}, flavor:{}", cloudId, snapshotId);

            String operUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, true);
            }

            OSVolumeSnapshotMetadata volumeSnapshotMetadataRsp = volumeSnapshotService.getSnapshotMetadata(cloudId, snapshotId, operUserId);

            return ResponseEntity.ok(volumeSnapshotMetadataRsp);
        }
        catch (Exception e)
        {
            LOGGER.error("get snapshot metadata failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @PostMapping(value = "/{project_id}/snapshots/{snapshot_id}/metadata", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Create Snapshot Metadata", response = Object.class)
    @LNJoyAuditLog(value = "Create Snapshot Metadata",
            resourceMapperId = CmpResources.OS_VOLUME_SNAPSHOT,
            actionDescriptionTemplate = CmpOpenstackActionDescriptionTemplate.Descriptions.CREATE_SNAPSHOT_METADATA,
            associatedResourceCnName = "磁盘快照",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "snapshotId", resourcePrimaryKeyClass = TblCmpOsSnapshotsKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpOsSnapshotsKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> addSnapshotMetadata(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                      @ApiParam(value = "snapshot_id", required = true, name = "snapshot_id") @PathVariable("snapshot_id") String snapshotId,
                                                      @ApiParam(value = "metadata", required = true, name = "metadata") @RequestBody Map<String, String> metadata,
                                                      @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                                      @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("add snapshot metadata, cloud:{}, request:{}", cloudId, metadata);

            if (! isAdmin() && ! cloudService.isOwner(cloudId, userId))
            {
                cloudService.checkCloudStatus(cloudId, false);
            }

            return volumeSnapshotService.addSnapshotMetadata(cloudId, snapshotId, metadata, bpId, userId);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("add snapshot metadata failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @PutMapping(value = "/{project_id}/snapshots/{snapshot_id}/metadata", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Update Snapshot Metadata", response = Object.class)
    @LNJoyAuditLog(value = "Update Snapshot Metadata",
            resourceMapperId = CmpResources.OS_VOLUME_SNAPSHOT,
            actionDescriptionTemplate = CmpOpenstackActionDescriptionTemplate.Descriptions.UPDATE_SNAPSHOT_METADATA,
            associatedResourceCnName = "磁盘快照",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "snapshotId", resourcePrimaryKeyClass = TblCmpOsSnapshotsKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpOsSnapshotsKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> updateSnapshotMetadata(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                         @ApiParam(value = "snapshot_id", required = true, name = "snapshot_id") @PathVariable("snapshot_id") String snapshotId,
                                                         @ApiParam(value = "metadata", required = true, name = "metadata") @RequestBody Map<String, String> metadata,
                                                         @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                                         @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("update snapshot metadata, cloud:{}, request:{}", cloudId, metadata);

            String operUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, false);
            }

            return volumeSnapshotService.updateSnapshotMetadata(cloudId, snapshotId, metadata, bpId, operUserId);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("update snapshot metadata failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/{project_id}/snapshots/{snapshot_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Show Snapshot Details", response = Object.class)
    public ResponseEntity<OSVolumeSnapshotWithDetailsRsp> getSnapshotDetails(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                                             @ApiParam(value = "snapshot_id", required = true, name = "snapshot_id") @PathVariable("snapshot_id") String snapshotId,
                                                                             @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("get snapshot details. cloud:{}, snapshotId:{}", cloudId, snapshotId);

            String operUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, true);
            }

            OSVolumeSnapshotWithDetailsRsp volumeSnapshotWithDetailsRsp = volumeSnapshotService.getSnapshotDetails(cloudId, snapshotId, operUserId);

            return ResponseEntity.ok(volumeSnapshotWithDetailsRsp);
        }
        catch (Exception e)
        {
            LOGGER.error("get snapshot details failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @PutMapping(value = "/{project_id}/snapshots/{snapshot_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Update Snapshot", response = Object.class)
    @LNJoyAuditLog(value = "Update Snapshot",
            resourceMapperId = CmpResources.OS_VOLUME_SNAPSHOT,
            actionDescriptionTemplate = CmpOpenstackActionDescriptionTemplate.Descriptions.UPDATE_SNAPSHOT,
            associatedResourceCnName = "磁盘快照",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "snapshotId", resourcePrimaryKeyClass = TblCmpOsSnapshotsKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpOsSnapshotsKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> updateSnapshot(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                 @ApiParam(value = "snapshot_id", required = true, name = "snapshot_id") @PathVariable("snapshot_id") String snapshotId,
                                                 @ApiParam(value = "snapshot", required = true, name = "snapshot") @RequestBody OSVolumeSnapshotUpdateReq request,
                                                 @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                                 @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("update snapshot, cloud:{}, request:{}", cloudId, request);

            String operUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, false);
            }

            return volumeSnapshotService.updateSnapshot(cloudId, snapshotId, request, bpId, operUserId);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("update snapshot failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @DeleteMapping(value = "/{project_id}/snapshots/{snapshot_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Delete Snapshot", response = Object.class)
    @LNJoyAuditLog(value = "Delete Snapshot",
            resourceMapperId = CmpResources.OS_VOLUME_SNAPSHOT,
            actionDescriptionTemplate = CmpOpenstackActionDescriptionTemplate.Descriptions.DELETE_SNAPSHOT,
            associatedResourceCnName = "磁盘快照",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "snapshotId", resourcePrimaryKeyClass = TblCmpOsSnapshotsKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpOsSnapshotsKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> removeSnapshot(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                 @ApiParam(value = "snapshot_id", required = true, name = "snapshot_id") @PathVariable("snapshot_id") String snapshotId,
                                                 @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("remove snapshot, cloud:{}, snapshotId:{}", cloudId, snapshotId);

            String operUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, false);
            }

            return volumeSnapshotService.removeSnapshot(cloudId, snapshotId, operUserId);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("remove snapshot failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }
}
