package com.lnjoying.justice.cmp.controller.nextstack.repo;

import com.lnjoying.justice.cmp.common.CmpResources;
import com.lnjoying.justice.cmp.db.model.TblCmpFlavorKey;
import com.lnjoying.justice.cmp.domain.dto.request.nextstack.repo.FlavorCreateReq;
import com.lnjoying.justice.cmp.domain.dto.request.nextstack.repo.FlavorUpdateReq;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.repo.FlavorDetailInfoRsp;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.repo.FlavorsRsp;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.vm.RpcGpuFlavorInfo;
import com.lnjoying.justice.cmp.entity.search.nextstack.repo.FlavorSearchCritical;
import com.lnjoying.justice.cmp.handler.actiondescription.i18n.zh_cn.CmpNextStackActionDescriptionTemplate;
import com.lnjoying.justice.cmp.service.nextstack.repo.FlavorServiceBiz;
import com.lnjoying.justice.cmp.service.cloud.CloudService;
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

import javax.validation.Valid;

import java.util.List;

import static com.lnjoying.justice.commonweb.common.SwaggerConstants.*;

@RestSchema(schemaId = "flavor")
@RequestMapping("/cmp/v1/{vendor}/clouds/{cloud_id}/api/repo/v1/flavors")
@Api(value = "Flavors Controller",tags = "Flavors Controller")
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
        properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "ns_flavor"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "ns_flavors"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "规格"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "TblCmpFlavor")})})
public class FlavorController extends RestWebController
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    private FlavorServiceBiz flavorService;

    @Autowired
    private CloudService cloudService;

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "create flavor", response = Object.class)
    @LNJoyAuditLog(value = "create flavor",
            resourceMapperId = CmpResources.NS_FLAVOR,
            actionDescriptionTemplate = CmpNextStackActionDescriptionTemplate.Descriptions.ADD_FLAVOR,
            associatedResourceCnName = "规格",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.METHOD_RETURN_VALUE,
                    resourceIdParseSPEL = "#obj?.body?.get('flavorId')?.toString()", resourcePrimaryKeyClass = TblCmpFlavorKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpFlavorKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> createFlavor(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                               @ApiParam(value = "FlavorCreateReq", required = true, name = "FlavorCreateReq") @RequestBody FlavorCreateReq request,
                                               @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                               @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("create flavor, cloud:{}, request:{}", cloudId, request);

            return flavorService.createFlavor(cloudId, request, bpId, userId);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("create flavor failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/gpus", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get available gpu", response = Object.class)
    public ResponseEntity<List<RpcGpuFlavorInfo>> getGpuFlavors(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                                @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            return ResponseEntity.ok(flavorService.getAvailableGpu(cloudId));
        }
        catch (Exception e)
        {
            LOGGER.error("get available gpu failed: {}", e.getMessage());
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get flavors", response = Object.class)
    public ResponseEntity<FlavorsRsp> getFlavors(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                 @ApiParam(name = "name") @RequestParam(required = false) String name,
                                                 @ApiParam(value = "type", name = "type") @RequestParam(required=true, value = "type") Integer flavorType,
                                                 @ApiParam(value = "gpu", name = "gpu") @RequestParam(required=false,value = "gpu") Boolean gpu,
                                                 @ApiParam(name = "page_size") @RequestParam(required = false, value = "page_size") Integer pageSize,
                                                 @ApiParam(name = "page_num") @RequestParam(required = false, value = "page_num") Integer pageNum,
                                                 @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("get flavors. cloud:{}", cloudId);

            FlavorSearchCritical flavorSearchCritical = new FlavorSearchCritical();
            flavorSearchCritical.setFlavorName(name);
            flavorSearchCritical.setFlavorType(flavorType);
            if (gpu != null) flavorSearchCritical.setGpu(gpu);
            if (pageNum != null) flavorSearchCritical.setPageNum(pageNum);
            if (pageSize != null)flavorSearchCritical.setPageSize(pageSize);

            FlavorsRsp getFlavorsRsp = flavorService.getFlavors(cloudId, flavorSearchCritical);

            return ResponseEntity.ok(getFlavorsRsp);
        }
        catch (Exception e)
        {
            LOGGER.error("get flavors failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @PutMapping(value = "/flavors/{flavor_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "update flavor", response = Object.class)
    @LNJoyAuditLog(value = "update flavor",
            resourceMapperId = CmpResources.NS_FLAVOR,
            actionDescriptionTemplate = CmpNextStackActionDescriptionTemplate.Descriptions.UPDATE_FLAVOR,
            associatedResourceCnName = "规格",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "flavorId", resourcePrimaryKeyClass = TblCmpFlavorKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpFlavorKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> updateFlavorName(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                   @ApiParam(value = "flavor_id", required = true, name = "flavor_id") @PathVariable("flavor_id")String flavorId,
                                                   @ApiParam(value = "FlavorUpdateReq", required = true, name = "FlavorUpdateReq") @RequestBody @Valid FlavorUpdateReq request)
    {
        try
        {
            LOGGER.info("update flavor: flavorId:{} name:{}", flavorId, request.getName());

            return cloudService.sendHttpRequestToCloud(cloudId);
        }
        catch (Exception e)
        {
            LOGGER.error("update  flavor failed: {}", e.getMessage());

            throw throwWebException(e);
        }
    }

    @DeleteMapping(value = "/{flavorId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "remove flavor", response = Object.class)
    @LNJoyAuditLog(value = "remove flavor",
            resourceMapperId = CmpResources.NS_FLAVOR,
            actionDescriptionTemplate = CmpNextStackActionDescriptionTemplate.Descriptions.DELETE_FLAVOR,
            associatedResourceCnName = "规格",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "flavorId", resourcePrimaryKeyClass = TblCmpFlavorKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpFlavorKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
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

    @GetMapping(value = "/{flavorId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get flavor info", response = Object.class)
    public ResponseEntity<FlavorDetailInfoRsp> getFlavor(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                         @ApiParam(value = "flavorId", required = true, name = "flavorId") @PathVariable("flavorId") String flavorId)
    {
        try
        {
            LOGGER.info("get flavor info. cloud:{}, flavor:{}", cloudId, flavorId);

            FlavorDetailInfoRsp tblCmpFlavor = flavorService.getFlavor(cloudId, flavorId);
            return ResponseEntity.ok(tblCmpFlavor);
        }
        catch (Exception e)
        {
            LOGGER.error("get flavor failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/max_info", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get max flavor info", response = Object.class)
    public ResponseEntity<Object> getMaxFlavorInfo(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId)
    {
        try
        {
            return cloudService.sendHttpRequestToCloud(cloudId);
        }
        catch (Exception e)
        {
            LOGGER.error("get max flavor failed: {}", e.getMessage());
            throw throwWebException(e);
        }
    }
}
