package com.lnjoying.justice.ecrm.controller;

import com.lnjoying.justice.commonweb.controller.RestWebController;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.LNJoyAuditLog;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.ResourceIdExtractConfiguration;
import com.lnjoying.justice.commonweb.handler.aspect.enums.ResourceIdLocationType;
import com.lnjoying.justice.ecrm.config.DescriptionConfig;
import com.lnjoying.justice.ecrm.domain.dto.request.GwInputReq;
import com.lnjoying.justice.ecrm.domain.dto.response.QueryGwRsp;
import com.lnjoying.justice.ecrm.domain.model.search.GwSearchCritical;
import com.lnjoying.justice.ecrm.facade.GwServiceFacade;
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

import javax.validation.Valid;
import java.util.Map;

import static com.lnjoying.justice.commonweb.common.SwaggerConstants.*;

@RestSchema(schemaId = "gws")
@RequestMapping("/ecrm/v1/gws")
@Api(value = "GW Controller",tags = "GW Controller")
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
        properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "gw"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "gws"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "网关"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "TblEdgeGwInfo")})})
public class GwController extends RestWebController
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    GwServiceFacade gwServiceFacade;

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "add node",notes = DescriptionConfig.ADD_GW_NODE_MSG,
            tags = ResourceOperationTypeConstants.RESOURCE_CREATE)
    public ResponseEntity<Map<String,String>> genAddCmd(@ApiParam(value = "", required = true, name = "gwInputInfo")@RequestBody @Valid GwInputReq gwInputReq,
                                                        @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                                        @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {
        try
        {

            return ResponseEntity.status(HttpStatus.CREATED).body(gwServiceFacade.genAddCmd(gwInputReq));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("gen gw cmd error. {}", e.getMessage());
            throw throwWebException(e);
        }
    }

    @PutMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "update node",notes = DescriptionConfig.UPDATE_GW_NODE_MSG)
    @LNJoyAuditLog(value = "update node",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "gw_id"))
    public ResponseEntity<String> updateNode(@ApiParam(value = "regionId", required = true, name = "regionId")@PathVariable String gw_id,
                                             @ApiParam(value = "", required = true, name = "gwInputInfo")@RequestBody GwInputReq gwInputReq,
                                             @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                             @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {
        try
        {

            gwServiceFacade.updateNode(gw_id, gwInputReq);
            return ResponseEntity.ok(null);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("update gw error. {}", e.getMessage());
            throw throwWebException(e);
        }
    }

    @DeleteMapping(value = "/{node_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "delete node",notes = DescriptionConfig.DELETE_GW_NODE_MSG)
    @LNJoyAuditLog(value = "delete node",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "node_id"))
    public ResponseEntity<String> deleteNode(@ApiParam(value = "", required = true, name = "node_id")@PathVariable String node_id,
                                             @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                             @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {
        try
        {

            gwServiceFacade.deleteNode(node_id);
            return ResponseEntity.ok(null);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("delete gw error. {}", e.getMessage());
            throw throwWebException(e);
        }
    }

    @PostMapping(value = "/{node_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "action node",notes = DescriptionConfig.ACTION_GW_NODE_MSG)
    @LNJoyAuditLog(value = "action node",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "node_id"),
            actionDescriptionField = "action",
            actionDescriptionValueSpEl = "#obj",
            actionDescriptionFormatString = "gw node action: %s")
    public ResponseEntity<String> ctrlNode(@ApiParam(value = "", required = true, name = "node_id")@PathVariable String node_id,
                                           @ApiParam(value = "", required = true, name = "action")@RequestParam String action,
                                           @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                           @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {
        try
        {

            gwServiceFacade.ctrlNode(node_id, action);
            return ResponseEntity.ok(null);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("ctrl gw node error. {}", e.getMessage());
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get node list",notes = DescriptionConfig.GW_NODE_LIST_MSG)
    public ResponseEntity<QueryGwRsp> getNodes(@ApiParam(name = "active_status") @RequestParam(value = "active_status", required = false) Integer active_status,
                                               @ApiParam(name = "online_status") @RequestParam(value = "online_status", required = false)  Integer online_status,
                                               @ApiParam(name = "region") @RequestParam(value = "region", required = false)  String region,
                                               @ApiParam(name = "page_size") @RequestParam(value = "page_size", required = false) Integer page_size,
                                               @ApiParam(name = "page_num") @RequestParam(value = "page_num", required = false)  Integer page_num,
                                               @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                               @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {

        try
        {
            GwSearchCritical gwSearchCritical = new GwSearchCritical();
            gwSearchCritical.setRegion(region);
            gwSearchCritical.setActive_status(active_status);
            gwSearchCritical.setOnline_status(online_status);
            if (page_num  != null) gwSearchCritical.setPageNum(page_num);
            if (page_size != null) gwSearchCritical.setPageSize(page_size);
            return ResponseEntity.ok().body(gwServiceFacade.getNodes(gwSearchCritical));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("get gw node error. {}", e.getMessage());
            throw throwWebException(e);
        }
    }
}
