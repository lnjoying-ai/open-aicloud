package com.lnjoying.justice.cmp.controller.openstack.blockstorage;

import com.lnjoying.justice.cmp.domain.dto.response.openstack.cinder.OSExtVolumeWithDetailsRsp;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.cinder.OSExtVolumesWithDetailsRsp;
import com.lnjoying.justice.cmp.entity.search.openstack.OSVolumeSearchCritical;
import com.lnjoying.justice.cmp.service.cloud.CloudService;
import com.lnjoying.justice.cmp.service.openstack.VolumeService;
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

@RestSchema(schemaId = "osextensionvolume")
@RequestMapping("/cmp/v1/{vendor}/clouds/{cloud_id}/extension/v3")
@Api(value = "OpenStack Volume Controller",tags = "OpenStack Volume Controller")
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
        properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "os_ext_volume"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "os_ext_volumes"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "OpenStack卷"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "TblCmpOsVolumes")})})
public class OSExtVolumeController extends RestWebController
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    private VolumeService volumeService;

    @Autowired
    private CloudService cloudService;

    @GetMapping(value = "/{project_id}/volumes/detail", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "List Volumes With Details", response = Object.class)
    public ResponseEntity<OSExtVolumesWithDetailsRsp> getVolumesWithDetails(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
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
                                                                            @ApiParam(name = "page_size") @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                                            @ApiParam(name = "page_num") @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                                                            @ApiParam(name = "name") @RequestParam(required = false, value = "name") String name,
                                                                            @ApiParam(name = "status") @RequestParam(required = false, value = "status") String status,
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
                    offset, marker, withCount, createdAt, updatedAt, consumesQuota, pageNum, pageSize, name, status);

            OSExtVolumesWithDetailsRsp volumesWithDetails = volumeService.getVolumesWithDetailsPage(cloudId, volumeSearchCritical, filterUserId);

            return ResponseEntity.ok(volumesWithDetails);
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
    public ResponseEntity<OSExtVolumeWithDetailsRsp> getVolumeDetails(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
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

            OSExtVolumeWithDetailsRsp volumeDetails = volumeService.getExtVolumeDetails(cloudId, volumeId, operUserId);

            return ResponseEntity.ok(volumeDetails);
        }
        catch (Exception e)
        {
            LOGGER.error("get volume details failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }
}
