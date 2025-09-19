package com.lnjoying.justice.cmp.controller.openstack.blockstorage;

import com.lnjoying.justice.cmp.domain.dto.response.openstack.cinder.*;
import com.lnjoying.justice.cmp.entity.search.openstack.OSVolumeSnapshotSearchCritical;
import com.lnjoying.justice.cmp.service.cloud.CloudService;
import com.lnjoying.justice.cmp.service.openstack.VolumeSnapshotService;
import com.lnjoying.justice.commonweb.controller.RestWebController;
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

@RestSchema(schemaId = "osextensionvolumesnapshot")
@RequestMapping("/cmp/v1/{vendor}/clouds/{cloud_id}/extension/v3")
@Api(value = "OpenStack Volume Snapshot Controller",tags = "OpenStack Volume Snapshot Controller")
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
        properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "os_ext_volume_snapshot"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "os_ext_volume_snapshots"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "OpenStack卷快照"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "TblCmpOsSnapshots")})})
public class OSExtVolumeSnapshotController extends RestWebController
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    private VolumeSnapshotService volumeSnapshotService;

    @Autowired
    private CloudService cloudService;

    @GetMapping(value = "/{project_id}/snapshots/detail", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "List Snapshots With Details", response = Object.class)
    public ResponseEntity<OSExtVolumeSnapshotsWithDetailsRsp> getSnapshotsWithDetails(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                                                      @ApiParam(value = "", required = true, name = "project_id") @PathVariable(value = "project_id") String projectId,
                                                                                      @ApiParam(name = "all_tenants") @RequestParam(required = false, value = "all_tenants") String allTenants,
                                                                                      @ApiParam(name = "sort") @RequestParam(required = false, value = "sort") String sort,
                                                                                      @ApiParam(name = "limit") @RequestParam(required = false, value = "limit") Integer limit,
                                                                                      @ApiParam(name = "offset") @RequestParam(required = false, value = "offset") Integer offset,
                                                                                      @ApiParam(name = "marker") @RequestParam(required = false, value = "marker") String marker,
                                                                                      @ApiParam(name = "with_count") @RequestParam(required = false, value = "with_count") Boolean withCount,
                                                                                      @ApiParam(name = "consumes_quota") @RequestParam(required = false, value = "consumes_quota") Boolean consumesQuota,
                                                                                      @ApiParam(name = "volume_id") @RequestParam(required = false, value = "volume_id") String volumeId,
                                                                                      @ApiParam(name = "page_size") @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                                                      @ApiParam(name = "page_num") @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                                                                      @ApiParam(name = "name") @RequestParam(required = false, value = "name") String name,
                                                                                      @ApiParam(name = "status") @RequestParam(required = false, value = "status") String status,
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
                    allTenants, sort, limit, offset, marker, withCount, consumesQuota, volumeId, pageNum, pageSize, name, status);

            OSExtVolumeSnapshotsWithDetailsRsp volumeSnapshotsWithDetailsRsp = volumeSnapshotService.getSnapshotsWithDetailsPage(cloudId, volumeSnapshotSearchCritical, filterUserId);

            return ResponseEntity.ok(volumeSnapshotsWithDetailsRsp);
        }
        catch (Exception e)
        {
            LOGGER.error("get snapshots details failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/{project_id}/snapshots/{snapshot_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Show Snapshot Details", response = Object.class)
    public ResponseEntity<OSExtVolumeSnapshotWithDetailsRsp> getSnapshotDetails(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
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

            OSExtVolumeSnapshotWithDetailsRsp volumeSnapshotWithDetailsRsp = volumeSnapshotService.getExtSnapshotDetails(cloudId, snapshotId, operUserId);

            return ResponseEntity.ok(volumeSnapshotWithDetailsRsp);
        }
        catch (Exception e)
        {
            LOGGER.error("get snapshot details failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }
}
