package com.lnjoying.justice.cmp.controller.openstack.compute;

import com.lnjoying.justice.cmp.domain.dto.response.openstack.nova.OSExtFlavorsWithDetailsRsp;
import com.lnjoying.justice.cmp.entity.search.openstack.OSFlavorSearchCritical;
import com.lnjoying.justice.cmp.service.openstack.FlavorService;
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

@RestSchema(schemaId = "osextensionflavor")
@RequestMapping("/cmp/v1/{vendor}/clouds/{cloud_id}/extension/v2.1/flavors")
@Api(value = "OpenStack Flavor Controller",tags = "OpenStack Flavor Controller")
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
        properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "os_ext_flavor"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "os_ext_flavors"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "OpenStack规格"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "TblCmpOsFlavors")})})
public class OSExtFlavorController extends RestWebController
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    private FlavorService flavorService;

    @GetMapping(value = "/detail", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "List Flavors With Details", response = Object.class)
    public ResponseEntity<OSExtFlavorsWithDetailsRsp> getFlavorsWithDetails(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                                            @ApiParam(name = "sort_key") @RequestParam(required = false, value = "sort_key") String sortKey,
                                                                            @ApiParam(name = "sort_dir") @RequestParam(required = false, value = "sort_dir") String sortDir,
                                                                            @ApiParam(name = "limit") @RequestParam(required = false, value = "limit") Integer limit,
                                                                            @ApiParam(name = "marker") @RequestParam(required = false, value = "marker") String marker,
                                                                            @ApiParam(name = "minDisk") @RequestParam(required = false, value = "minDisk") Integer minDisk,
                                                                            @ApiParam(name = "minRam") @RequestParam(required = false, value = "minRam") Integer minRam,
                                                                            @ApiParam(name = "is_public") @RequestParam(required = false, value = "is_public") String isPublic,
                                                                            @ApiParam(name = "page_size") @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                                            @ApiParam(name = "page_num") @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                                                            @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                                                            @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("get flavors. cloud:{}", cloudId);

            OSFlavorSearchCritical flavorSearchCritical = new OSFlavorSearchCritical(sortKey, sortDir, limit, marker, minDisk,
                    minRam, isPublic, pageNum, pageSize);

            OSExtFlavorsWithDetailsRsp getFlavorsWithDetailsRsp;

            getFlavorsWithDetailsRsp = flavorService.getFlavorsWithDetailsPage(cloudId, flavorSearchCritical, null);

            return ResponseEntity.ok(getFlavorsWithDetailsRsp);
        }
        catch (Exception e)
        {
            LOGGER.error("get flavors failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }
}
