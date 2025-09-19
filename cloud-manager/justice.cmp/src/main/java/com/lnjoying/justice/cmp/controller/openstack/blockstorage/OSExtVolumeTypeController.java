package com.lnjoying.justice.cmp.controller.openstack.blockstorage;

import com.lnjoying.justice.cmp.domain.dto.response.openstack.cinder.OSExtVolumeTypesWithDetailsRsp;
import com.lnjoying.justice.cmp.entity.search.openstack.OSVolumeTypeSearchCritical;
import com.lnjoying.justice.cmp.service.openstack.VolumeTypeService;
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

@RestSchema(schemaId = "osextensionvolumetype")
@RequestMapping("/cmp/v1/{vendor}/clouds/{cloud_id}/extension/v3")
@Api(value = "OpenStack Volume Type Controller",tags = "OpenStack Volume Type Controller")
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
        properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "os_ext_volume_type"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "os_ext_volume_types"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "OpenStack卷类型"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "TblCmpOsVolumeTypes")})})
public class OSExtVolumeTypeController extends RestWebController
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    private VolumeTypeService volumeTypeService;

    @GetMapping(value = "/{project_id}/types", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "List all volume types", response = Object.class)
    public ResponseEntity<OSExtVolumeTypesWithDetailsRsp> getVolumeTypesWithDetails(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                                                    @ApiParam(value = "", required = true, name = "project_id") @PathVariable(value = "project_id") String projectId,
                                                                                    @ApiParam(name = "is_public") @RequestParam(required = false, value = "is_public") Boolean isPublic,
                                                                                    @ApiParam(name = "sort") @RequestParam(required = false, value = "sort") String sort,
                                                                                    @ApiParam(name = "limit") @RequestParam(required = false, value = "limit") Integer limit,
                                                                                    @ApiParam(name = "offset") @RequestParam(required = false, value = "offset") Integer offset,
                                                                                    @ApiParam(name = "marker") @RequestParam(required = false, value = "marker") String marker,
                                                                                    @ApiParam(name = "page_size") @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                                                    @ApiParam(name = "page_num") @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                                                                    @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("get volume types. cloud:{}", cloudId);

            OSVolumeTypeSearchCritical volumeTypeSearchCritical = new OSVolumeTypeSearchCritical(projectId, isPublic, sort,
                    limit, offset, marker, pageNum, pageSize);

            OSExtVolumeTypesWithDetailsRsp volumeTypesWithDetails;

            volumeTypesWithDetails = volumeTypeService.getVolumeTypesWithDetailsPage(cloudId, volumeTypeSearchCritical, null);

            return ResponseEntity.ok(volumeTypesWithDetails);
        }
        catch (Exception e)
        {
            LOGGER.error("get volume types failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }
}
