package com.lnjoying.justice.aos.controller;

import com.lnjoying.justice.aos.common.AosResources;
import com.lnjoying.justice.aos.common.StackSearchCritical;
import com.lnjoying.justice.aos.config.DescriptionConfig;
import com.lnjoying.justice.aos.domain.dto.rsp.GetStackDeployListRsp;
import com.lnjoying.justice.aos.domain.dto.rsp.GetStackListRsp;
import com.lnjoying.justice.aos.domain.model.StackDeployInfo;
import com.lnjoying.justice.aos.facade.StackServiceFacade;
import com.lnjoying.justice.aos.handler.actiondescription.i18n.zh_cn.StackDeployActionDescriptionTemplate;
import com.lnjoying.justice.commonweb.controller.RestWebController;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.LNJoyAuditLog;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.ResourceIdExtractConfiguration;
import com.lnjoying.justice.commonweb.handler.aspect.enums.ResourceIdLocationType;
import com.lnjoying.justice.commonweb.util.CollectionUtils;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.common.ErrorLevel;
import com.lnjoying.justice.schema.constant.UserHeadInfo;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static com.lnjoying.justice.aos.domain.model.StackDeployInfo.DeployStatus.checkStatus;
import static com.lnjoying.justice.commonweb.common.SwaggerConstants.*;
import static com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils.*;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/1/13 21:10
 */

@Controller
@RestSchema(schemaId = "StackDeployController")
@RequestMapping("/aos/v1/docker/deployments")
@Api(value = "stack deployments Controller",tags = {"stack deployments Controller"})
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
        properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "stack_deploy"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "stack_deploys"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "应用部署"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "TblStackSpecInfo")})})
public class StackDeployController extends RestWebController
{

    private static Logger LOGGER = LogManager.getLogger(StackDeployController.class);

    @Autowired
    private StackServiceFacade stackFacade;

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get stack deploy infos", response = Object.class)
    public ResponseEntity<GetStackDeployListRsp> getStackDeployInfos(@ApiParam(value = "status", required = false, name = "status") @RequestParam(value="status", required = false) Integer status,
                                                                         @ApiParam(value = "name", required = false, name = "name") @RequestParam(value="name", required = false) String name,
                                                                         @ApiParam(value = "region", required = false, name = "region") @RequestParam(value="region", required = false) String region,
                                                                         @ApiParam(value = "site", required = false, name = "site") @RequestParam(value="site", required = false) String site,
                                                                         @ApiParam(value = "node_id", required = false, name = "node_id") @RequestParam(value= "nodeId", required = false) String nodeId,
                                                                         @ApiParam(value = "user_id",required = false, name = "user_id") @RequestParam(value = "user_id", required = false) String ownerUserId,
                                                                         @ApiParam(value = "page size", required = false, name = "page_size") @RequestParam(value= "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                                                         @ApiParam(value = "page_num", required = false, name = "page_num") @RequestParam(value= "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                                                         @ApiParam(value = "stack_type", required = false, name = "stack_type") @RequestParam(value= "stackType", required = false, defaultValue = "") String stackType,
                                                                     @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                                                         @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {
        try
        {
            StackSearchCritical critical = new StackSearchCritical();

            boolean queryOwner = StringUtils.isNotBlank(ownerUserId) && (isAdmin() || isBpAdmin());
            if (queryOwner)
            {
                critical.setUserId(ownerUserId);
            }
            else
            {
                critical.setUserId(queryUserId());
                critical.setBpId(queryBpId());
            }

            if (null != status)
            {
                checkStatus(status);
                critical.setStatus(status);
            }
            if (StringUtils.isNotBlank(name))
            {
                critical.setName(name);
            }
            if (StringUtils.isNotBlank(region))
            {
                critical.setRegionId(region);
            }
            if (StringUtils.isNotBlank(site))
            {
                critical.setSiteId(site);
            }
            if (StringUtils.isNotBlank(nodeId))
            {
                critical.setNodeId(nodeId);
            }
            if (null != pageSize)
            {
                critical.setPageSize(pageSize);
            }
            if (null != pageNum)
            {
                critical.setPageNum(pageNum);
            }
            
            if (CollectionUtils.hasContent(stackType))
            {
                critical.setStackType(stackType);
            }

            LOGGER.info("get stack spec list, condition: {}", critical);
            return ResponseEntity.ok().body(stackFacade.getStackDeployInfos(critical));
        }
        catch (Exception e)
        {
            LOGGER.error("get stack deploy list info error:{}", e);
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/{spec_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get stack deploy info", response = Object.class)
    @LNJoyAuditLog(value = "get stack deploy info",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH, bindParameterName = "specId"))
    public ResponseEntity<StackDeployInfo> getStackDeployInfo(@ApiParam(value = "", required = true, name = "spec_id")@PathVariable("spec_id") String specId,
                                                                  @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                                                  @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {
        try
        {
            checkRoleAosTenantOrAdmin(authorities);
            LOGGER.debug("get stack spec info: {} .", specId);
            return ResponseEntity.ok().body(stackFacade.getStackDeployInfo(specId));
        }
        catch (Exception e)
        {
            LOGGER.error("get stack deploy  info error:{}", e);
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/{spec_id}/stacks", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get stack deploy info", response = Object.class)
    @LNJoyAuditLog(value = "get stack deploy info",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH, bindParameterName = "specId"))
    public ResponseEntity<GetStackListRsp> getStackDeployInstancesInfo(@ApiParam(value = "", required = true, name = "spec_id")@PathVariable("spec_id") String specId,
                                                                        @ApiParam(value = "status", required = false, name = "status") @RequestParam(value="status", required = false) Integer status,
                                                                        @ApiParam(value = "region", required = false, name = "region") @RequestParam(value="region", required = false) String region,
                                                                        @ApiParam(value = "site", required = false, name = "site") @RequestParam(value="site", required = false) String site,
                                                                        @ApiParam(value = "node_id", required = false, name = "node_id") @RequestParam(value= "nodeId", required = false) String nodeId,
                                                                        @ApiParam(name = "page_size") @RequestParam(value = "page_size", required = false) Integer pageSize,
                                                                        @ApiParam(name = "page_num") @RequestParam(value = "page_num", required = false)  Integer pageNum,
                                                                        @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                                                        @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {
        try
        {
            LOGGER.debug("get container spec Instances info: {} .", specId);
            StackSearchCritical critical = new StackSearchCritical();

            critical.setUserId(queryUserId());
            critical.setBpId(queryBpId());
            critical.setStatus(status);
            if (pageNum != null) critical.setPageNum(pageNum);
            if (pageSize != null) critical.setPageSize(pageSize);
            critical.setRegionId(region);
            critical.setSiteId(site);
            critical.setNodeId(nodeId);
            critical.setSpecId(specId);

            return ResponseEntity.ok().body(stackFacade.getStackDeployInstancesInfo(critical));
        }
        catch (Exception e)
        {
            LOGGER.error("get stack deploy instances info error:{}", e);
            throw throwWebException(e);
        }
    }

    @DeleteMapping(value = "/{spec_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "delete stack deploy info",notes = DescriptionConfig.DELETE_STACK_MSG)
    @LNJoyAuditLog(value = "delete stack deploy info",
            resourceMapperId = AosResources.STACK_SPEC,
            actionDescriptionTemplate = StackDeployActionDescriptionTemplate.Descriptions.DELETE_STACK_DEPLOY,
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH, bindParameterName = "specId"))
    public ResponseEntity<String>  deleteStackDeployInfo(@ApiParam(value = "", required = true, name = "spec_id")@PathVariable(value = "spec_id") String specId,
                                               @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                               @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                               @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {
        try
        {
            LOGGER.info("delete stack spec info: {}", specId);
            checkRoleAosTenantOrAdmin(authorities);
            String ownerUserId = getUserAttributes().getRight();
            stackFacade.deleteStackDeployInfo(specId, ownerUserId);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("delete stack spec info error: {}", e.getMessage());
            throw throwWebException(e);
        }
    }

    protected void checkRoleAosTenantOrAdmin(String authorities)
    {
        if (!(isBpAdmin() || isBpUser() || isAdmin()))
        {
            throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
        }
    }
}
