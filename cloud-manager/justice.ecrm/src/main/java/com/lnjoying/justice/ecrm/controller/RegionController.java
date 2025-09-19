package com.lnjoying.justice.ecrm.controller;

import com.lnjoying.justice.commonweb.controller.RestWebController;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.LNJoyAuditLog;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.ResourceIdExtractConfiguration;
import com.lnjoying.justice.commonweb.handler.aspect.enums.ResourceIdLocationType;
import com.lnjoying.justice.ecrm.common.constant.EcrmResources;
import com.lnjoying.justice.ecrm.config.DescriptionConfig;
import com.lnjoying.justice.ecrm.domain.dto.request.RegionInputReq;
import com.lnjoying.justice.ecrm.domain.dto.response.QueryRegionRsp;
import com.lnjoying.justice.ecrm.facade.RegionServiceFacade;
import com.lnjoying.justice.ecrm.handler.actiondescription.i18n.zh_cn.RegionActionDescriptionTemplate;
import com.lnjoying.justice.schema.constant.ResourceOperationTypeConstants;
import com.lnjoying.justice.schema.constant.UserHeadInfo;
import io.swagger.annotations.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.lnjoying.justice.commonweb.common.SwaggerConstants.*;


@RestSchema(schemaId = "region")
@RequestMapping("/ecrm/v1/regions")
@Api(value = "Region Controller",tags = "Region Controller")
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
        properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "region"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "regions"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "区域"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "TblRegionInfo")})})
public class RegionController extends RestWebController
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    RegionServiceFacade regionServiceFacade;

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "add region",notes = DescriptionConfig.ADD_REGION_MSG)
    @LNJoyAuditLog(value = "add region",
            tags = ResourceOperationTypeConstants.RESOURCE_CREATE,
            actionDescriptionTemplate = RegionActionDescriptionTemplate.Descriptions.ADD_REGION,
            isResourceInstanceNameFromArgs = true,
            resourceInstanceNameFromArgsParseSpEl = "#obj?.get('name')?.toString()",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.METHOD_RETURN_VALUE,
                            resourceIdParseSPEL = "#obj?.body?.toString()"))
    public ResponseEntity<String> addRegion(@ApiParam(value = "", required = true, name = "regionIputInfo")@RequestBody RegionInputReq regionInputReq,
                                            @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                            @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {
        try
        {

            regionServiceFacade.addRegion(regionInputReq);
            return ResponseEntity.status(HttpStatus.CREATED).body(regionInputReq.getId());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("create region error. {}", e.getMessage());
            throw throwWebException(e);
        }
    }

    @PutMapping(value = "/{region_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "update region",notes = DescriptionConfig.UPDATE_REGION_MSG)
    @LNJoyAuditLog(value = "update region",
            actionDescriptionTemplate = RegionActionDescriptionTemplate.Descriptions.UPDATE_REGION,
            resourceMapperId = EcrmResources.REGION,
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "region_id"))
    public ResponseEntity<String> updateRegion(@ApiParam(value = "region_id", required = true, name = "region_id")@PathVariable String region_id,
                                               @ApiParam(value = "", required = true, name = "regionIputInfo")@RequestBody RegionInputReq regionInputReq,
                                               @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                               @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {
        try
        {

            regionServiceFacade.updateRegion(region_id, regionInputReq);
            return ResponseEntity.ok(null);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("update region error. {}", e.getMessage());
            throw throwWebException(e);
        }
    }


    @DeleteMapping(value = "/{region_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "delete region",notes = DescriptionConfig.DELETE_REGION_MSG)
    @LNJoyAuditLog(value = "delete region",
            resourceMapperId = EcrmResources.REGION,
            actionDescriptionTemplate = RegionActionDescriptionTemplate.Descriptions.DELETE_REGION,
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "region_id"))
    public ResponseEntity<String> deleteRegion(@ApiParam(value = "region_id", required = true, name = "region_id")@PathVariable(value = "region_id") String region_id,
                                               @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                               @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {
        try
        {

            regionServiceFacade.deleteRegion(region_id);
            return ResponseEntity.ok(null);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("delete region error. {}", e.getMessage());
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get region list",notes = DescriptionConfig.REGION_LIST_MSG)
    public ResponseEntity<QueryRegionRsp> getRegions(@ApiParam(value = "orchestration", required = false, name = "orchestration") @RequestParam(required = false) String orchestration,
                                                     @ApiParam(value = "name", required = false, name = "name") @RequestParam(required = false) String name,
                                                     @ApiParam(name = "page_size") @RequestParam(value = "page_size", required = false, defaultValue = Integer.MAX_VALUE + "") Integer pageSize,
                                                     @ApiParam(name = "page_num") @RequestParam(value = "page_num", required = false, defaultValue = "1") Integer pageNum,
                                                     @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                                     @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {

        try
        {
            return ResponseEntity.ok().body(regionServiceFacade.getPageableRegions(orchestration, name, pageNum, pageSize));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("delete region error. {}", e.getMessage());
            throw throwWebException(e);
        }
    }
}
