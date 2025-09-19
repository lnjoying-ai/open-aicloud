package com.lnjoying.justice.ecrm.controller;

import com.lnjoying.justice.commonweb.controller.RestWebController;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.LNJoyAuditLog;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.ResourceIdExtractConfiguration;
import com.lnjoying.justice.commonweb.handler.aspect.enums.ResourceIdLocationType;
import com.lnjoying.justice.ecrm.config.DescriptionConfig;
import com.lnjoying.justice.ecrm.domain.dto.request.AddServiceAgentReq;
import com.lnjoying.justice.ecrm.domain.dto.response.GetServiceAgentsRsp;
import com.lnjoying.justice.ecrm.domain.model.search.ServiceAgentSearchCritical;
import com.lnjoying.justice.ecrm.facade.ServiceAgentService;
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

@RestSchema(schemaId = "serviceagent")
@RequestMapping("/ecrm/v1/service-agents")
@Api(value = "Service Agent Controller",tags = "Service Agent Controller")
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
        properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "serviceagent"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "serviceagents"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "服务Agent"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "TblRegionInfo")})})
public class ServiceAgentController extends RestWebController
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    ServiceAgentService serviceAgentService;

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "add service agent",notes = DescriptionConfig.ADD_REGION_MSG,
            tags = ResourceOperationTypeConstants.RESOURCE_CREATE)
    public ResponseEntity<String> addServiceAgent(@ApiParam(value = "", required = true, name = "addServiceAgentReq")@RequestBody AddServiceAgentReq addServiceAgentReq,
                                            @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                            @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {
        try
        {

            serviceAgentService.addServiceAgent(addServiceAgentReq);
            return ResponseEntity.status(HttpStatus.CREATED).body(null);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("add service agent error. {}", e.getMessage());
            throw throwWebException(e);
        }
    }

    @DeleteMapping(value = "/{agent_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "delete service agent",notes = DescriptionConfig.DELETE_REGION_MSG)
    @LNJoyAuditLog(value = "delete service agent",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "agentId"))
    public ResponseEntity<String> deleteServiceAgent(@ApiParam(value = "region_id", required = true, name = "agent_id")@PathVariable(value = "agent_id") String agentId,
                                               @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                               @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {
        try
        {

            serviceAgentService.deleteServiceAgent(agentId);
            return ResponseEntity.ok(null);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("delete service agent error. {}", e.getMessage());
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get service agent list",notes = DescriptionConfig.REGION_LIST_MSG)
    public ResponseEntity<GetServiceAgentsRsp> getServiceAgents(@ApiParam(value = "region_id", required = false, name = "region_id") @RequestParam(required = false) String regionId,
                                                                @ApiParam(value = "site_id", required = false, name = "site_id") @RequestParam(required = false) String siteId,
                                                                @ApiParam(value = "node_id", required = false, name = "node_id") @RequestParam(required = false) String nodeId,
                                                                @ApiParam(value = "agent_id", required = false, name = "agent_id") @RequestParam(required = false) String agentId,
                                                                @ApiParam(name = "page_size") @RequestParam(value = "page_size", required = false, defaultValue = Integer.MAX_VALUE + "") Integer pageSize,
                                                                @ApiParam(name = "page_num") @RequestParam(value = "page_num", required = false, defaultValue = "1") Integer pageNum,
                                                                @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                                                @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {

        try
        {
            ServiceAgentSearchCritical serviceAgentSearchCritical = new ServiceAgentSearchCritical();
            serviceAgentSearchCritical.setRegionId(regionId);
            serviceAgentSearchCritical.setSiteId(siteId);
            serviceAgentSearchCritical.setNodeId(nodeId);
            serviceAgentSearchCritical.setAgentId(agentId);
            if (pageNum != null) serviceAgentSearchCritical.setPageNum(pageNum);
            if (pageSize != null) serviceAgentSearchCritical.setPageSize(pageSize);
            return ResponseEntity.ok().body(serviceAgentService.getServiceAgents(serviceAgentSearchCritical));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("get service agents error. {}", e.getMessage());
            throw throwWebException(e);
        }
    }
}
