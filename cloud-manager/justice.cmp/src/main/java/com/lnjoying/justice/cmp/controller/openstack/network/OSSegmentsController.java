package com.lnjoying.justice.cmp.controller.openstack.network;

import com.lnjoying.justice.cmp.domain.dto.response.openstack.neutron.OSSegmentsWithDetailsRsp;
import com.lnjoying.justice.cmp.entity.search.openstack.OSSegmentSearchCritical;
import com.lnjoying.justice.cmp.service.cloud.CloudService;
import com.lnjoying.justice.cmp.service.openstack.SegmentService;
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

@RestSchema(schemaId = "ossegment")
@RequestMapping("/cmp/v1/{vendor}/clouds/{cloud_id}/v2.0/segments")
@Api(value = "OpenStack Segment Controller",tags = "OpenStack Segment Controller")
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
        properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "os_segment"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "os_segments"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "OpenStack网络segments"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "TblCmpOsNetworksegments")})})
public class OSSegmentsController extends RestWebController
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    private SegmentService segmentService;

    @Autowired
    private CloudService cloudService;

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "List Segments", response = Object.class)
    public ResponseEntity<OSSegmentsWithDetailsRsp> getSegments(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                                @ApiParam(name = "id") @RequestParam(required = false, value = "id") String id,
                                                                @ApiParam(name = "network_id") @RequestParam(required = false, value = "network_id") String networkId,
                                                                @ApiParam(name = "physical_network") @RequestParam(required = false, value = "physical_network") String physicalNetwork,
                                                                @ApiParam(name = "network_type") @RequestParam(required = false, value = "network_type") String networkType,
                                                                @ApiParam(name = "revision_number") @RequestParam(required = false, value = "revision_number") Integer revisionNumber,
                                                                @ApiParam(name = "segmentation_id") @RequestParam(required = false, value = "segmentation_id") Integer segmentationId,
                                                                @ApiParam(name = "name") @RequestParam(required = false, value = "name") String name,
                                                                @ApiParam(name = "description") @RequestParam(required = false, value = "description") String description,
                                                                @ApiParam(name = "sort_dir") @RequestParam(required = false, value = "sort_dir") String sortDir,
                                                                @ApiParam(name = "sort_key") @RequestParam(required = false, value = "sort_key") String sortKey,
                                                                @ApiParam(name = "fields") @RequestParam(required = false, value = "fields") String fields,
                                                                @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("get segments. cloud:{}", cloudId);

            String filterUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                filterUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, true);
            }

            OSSegmentSearchCritical segmentSearchCritical = new OSSegmentSearchCritical(id, networkId, physicalNetwork,
                    networkType, revisionNumber, segmentationId, name, description, sortDir, sortKey, fields);

            OSSegmentsWithDetailsRsp segmentsWithDetails = segmentService.getSegments(cloudId, segmentSearchCritical, filterUserId);

            return ResponseEntity.ok(segmentsWithDetails);
        }
        catch (Exception e)
        {
            LOGGER.error("get segments failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }
}
