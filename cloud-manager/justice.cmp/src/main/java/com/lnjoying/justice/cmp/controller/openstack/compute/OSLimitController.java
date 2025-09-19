package com.lnjoying.justice.cmp.controller.openstack.compute;

import com.lnjoying.justice.cmp.domain.dto.response.openstack.nova.OSFlavorsRsp;
import com.lnjoying.justice.cmp.entity.search.openstack.OSFlavorSearchCritical;
import com.lnjoying.justice.cmp.service.openstack.LimitService;
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

@RestSchema(schemaId = "oslimit")
@RequestMapping("/cmp/v1/{vendor}/clouds/{cloud_id}/v2.1/limits")
@Api(value = "OpenStack Limits Controller",tags = "OpenStack Limits Controller")
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
        properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "os_limit"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "os_limits"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "OpenStack配额"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "TblCmpFlavor")})})
public class OSLimitController extends RestWebController
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    private LimitService limitService;

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Show Rate And Absolute Limits", response = Object.class)
    public ResponseEntity<OSFlavorsRsp> getLimits(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                  @ApiParam(name = "reserved") @RequestParam(required = false, value = "reserved") Integer reserved,
                                                  @ApiParam(name = "tenant_id") @RequestParam(required = false, value = "tenant_id") String tenantId,
                                                  @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("get Limits. cloud:{}", cloudId);

            OSFlavorSearchCritical flavorSearchCritical = new OSFlavorSearchCritical();

            OSFlavorsRsp getFlavorsRsp = limitService.getLimits(cloudId, flavorSearchCritical, userId);

            return ResponseEntity.ok(getFlavorsRsp);
        }
        catch (Exception e)
        {
            LOGGER.error("get Limits failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }
}
