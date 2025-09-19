package com.lnjoying.justice.cmp.controller.openstack.compute;

import com.lnjoying.justice.cmp.common.CmpResources;
import com.lnjoying.justice.cmp.db.model.TblCmpOsFlavorsKey;
import com.lnjoying.justice.cmp.domain.dto.request.openstack.nova.OSFlavorCreateReq;
import com.lnjoying.justice.cmp.domain.dto.request.openstack.nova.OSFlavorUpdateReq;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.nova.OSFlavorWithDetailsRsp;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.nova.OSFlavorsRsp;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.nova.OSFlavorsWithDetailsRsp;
import com.lnjoying.justice.cmp.entity.search.openstack.OSFlavorSearchCritical;
import com.lnjoying.justice.cmp.handler.actiondescription.i18n.zh_cn.CmpOpenstackActionDescriptionTemplate;
import com.lnjoying.justice.cmp.service.openstack.FlavorService;
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

@RestSchema(schemaId = "osflavor")
@RequestMapping("/cmp/v1/{vendor}/clouds/{cloud_id}/v2.1/flavors")
@Api(value = "OpenStack Flavor Controller",tags = "OpenStack Flavor Controller")
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
        properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "os_flavor"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "os_flavors"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "OpenStack规格"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "TblCmpOsFlavors")})})
public class OSFlavorController extends RestWebController
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    private FlavorService flavorService;

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "List Flavors", response = Object.class)
    public ResponseEntity<OSFlavorsRsp> getFlavors(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                   @ApiParam(name = "sort_key") @RequestParam(required = false, value = "sort_key") String sortKey,
                                                   @ApiParam(name = "sort_dir") @RequestParam(required = false, value = "sort_dir") String sortDir,
                                                   @ApiParam(name = "limit") @RequestParam(required = false, value = "limit") Integer limit,
                                                   @ApiParam(name = "marker") @RequestParam(required = false, value = "marker") String marker,
                                                   @ApiParam(name = "minDisk") @RequestParam(required = false, value = "minDisk") Integer minDisk,
                                                   @ApiParam(name = "minRam") @RequestParam(required = false, value = "minRam") Integer minRam,
                                                   @ApiParam(name = "is_public") @RequestParam(required = false, value = "is_public") String isPublic,
                                                   @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                                   @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("get flavors. cloud:{}", cloudId);

            OSFlavorSearchCritical flavorSearchCritical = new OSFlavorSearchCritical(sortKey, sortDir, limit, marker, minDisk,
                    minRam, isPublic, null, null);

            OSFlavorsRsp getFlavorsRsp;

            getFlavorsRsp = flavorService.getFlavors(cloudId, flavorSearchCritical, null);

            return ResponseEntity.ok(getFlavorsRsp);
        }
        catch (Exception e)
        {
            LOGGER.error("get flavors failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Create Flavor", response = Object.class)
    @LNJoyAuditLog(value = "Create Flavor",
            resourceMapperId = CmpResources.OS_FLAVOR,
            actionDescriptionTemplate = CmpOpenstackActionDescriptionTemplate.Descriptions.CREATE_FLAVOR,
            associatedResourceCnName = "规格",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.METHOD_RETURN_VALUE,
                    resourceIdParseSPEL = "#obj?.body?.get('flavor')?.get('id')?.toString()", resourcePrimaryKeyClass = TblCmpOsFlavorsKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpOsFlavorsKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> addFlavor(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                            @ApiParam(value = "flavor", required = true, name = "flavor") @RequestBody OSFlavorCreateReq request,
                                            @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                            @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("add flavor, cloud:{}, request:{}", cloudId, request);

            return flavorService.addFlavor(cloudId, request, bpId, userId);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("add flavor failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/detail", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "List Flavors With Details", response = Object.class)
    public ResponseEntity<OSFlavorsWithDetailsRsp> getFlavorsWithDetails(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                                         @ApiParam(name = "sort_key") @RequestParam(required = false, value = "sort_key") String sortKey,
                                                                         @ApiParam(name = "sort_dir") @RequestParam(required = false, value = "sort_dir") String sortDir,
                                                                         @ApiParam(name = "limit") @RequestParam(required = false, value = "limit") Integer limit,
                                                                         @ApiParam(name = "marker") @RequestParam(required = false, value = "marker") String marker,
                                                                         @ApiParam(name = "minDisk") @RequestParam(required = false, value = "minDisk") Integer minDisk,
                                                                         @ApiParam(name = "minRam") @RequestParam(required = false, value = "minRam") Integer minRam,
                                                                         @ApiParam(name = "is_public") @RequestParam(required = false, value = "is_public") String isPublic,
                                                                         @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                                                         @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("get flavors. cloud:{}", cloudId);

            OSFlavorSearchCritical flavorSearchCritical = new OSFlavorSearchCritical(sortKey, sortDir, limit, marker, minDisk,
                    minRam, isPublic, null, null);

            OSFlavorsWithDetailsRsp getFlavorsWithDetailsRsp;

            getFlavorsWithDetailsRsp = flavorService.getFlavorsWithDetails(cloudId, flavorSearchCritical, null);

            return ResponseEntity.ok(getFlavorsWithDetailsRsp);
        }
        catch (Exception e)
        {
            LOGGER.error("get flavors failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/{flavor_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get flavor info", response = Object.class)
    public ResponseEntity<OSFlavorWithDetailsRsp> getFlavor(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                            @ApiParam(value = "flavor_id", required = true, name = "flavor_id") @PathVariable("flavor_id") String flavorId,
                                                            @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                                            @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("get flavor info. cloud:{}, flavor:{}", cloudId, flavorId);

            OSFlavorWithDetailsRsp osFlavorWithDetailsRsp = flavorService.getFlavor(cloudId, flavorId);

            return ResponseEntity.ok(osFlavorWithDetailsRsp);
        }
        catch (Exception e)
        {
            LOGGER.error("get flavor failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @PutMapping(value = "/{flavor_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Update Flavor Description", response = Object.class)
    @LNJoyAuditLog(value = "Update Flavor Description",
            resourceMapperId = CmpResources.OS_FLAVOR,
            actionDescriptionTemplate = CmpOpenstackActionDescriptionTemplate.Descriptions.UPDATE_FLAVOR,
            associatedResourceCnName = "规格",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "flavorId", resourcePrimaryKeyClass = TblCmpOsFlavorsKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpOsFlavorsKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> updateFlavor(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                               @ApiParam(value = "", required = true, name = "flavor_id") @PathVariable(value = "flavor_id") String flavorId,
                                                        @ApiParam(value = "flavor", required = true, name = "flavor") @RequestBody OSFlavorUpdateReq request,
                                                        @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                                        @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("update flavor, cloud:{}, request:{}", cloudId, request);

            return flavorService.updateFlavor(cloudId, flavorId, request);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("update flavor failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @DeleteMapping(value = "/{flavorId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Delete Flavor", response = Object.class)
    @LNJoyAuditLog(value = "Delete Flavor",
            resourceMapperId = CmpResources.OS_FLAVOR,
            actionDescriptionTemplate = CmpOpenstackActionDescriptionTemplate.Descriptions.DELETE_FLAVOR,
            associatedResourceCnName = "规格",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "flavorId", resourcePrimaryKeyClass = TblCmpOsFlavorsKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpOsFlavorsKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> removeFlavor(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                               @ApiParam(value = "flavorId", required = true, name = "flavorId") @PathVariable("flavorId")String flavorId,
                                               @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("remove flavor, cloud:{}, flavor:{}", cloudId, flavorId);

            return flavorService.removeFlavor(cloudId, flavorId, userId);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("remove flavor failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }
}
