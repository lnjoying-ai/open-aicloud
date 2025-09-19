package com.lnjoying.justice.cis.controller;

import com.lnjoying.justice.cis.common.constant.CisResources;
import com.lnjoying.justice.cis.common.constant.ContainerSearchCritical;
import com.lnjoying.justice.cis.config.DescriptionConfig;
import com.lnjoying.justice.cis.controller.dto.response.DockerContainerDeployInfo;
import com.lnjoying.justice.cis.controller.dto.response.GetContainerDeployInfosRsp;
import com.lnjoying.justice.cis.controller.dto.response.GetContainerInstInfosRsp;
import com.lnjoying.justice.cis.handler.actiondescription.i18n.zh_cn.CisContainerSpecActionDescriptionTemplate;
import com.lnjoying.justice.cis.service.CisManagerService;
import com.lnjoying.justice.commonweb.controller.RestWebController;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.LNJoyAuditLog;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.ResourceIdExtractConfiguration;
import com.lnjoying.justice.commonweb.handler.aspect.enums.ResourceIdLocationType;
import com.lnjoying.justice.schema.constant.UserHeadInfo;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static com.lnjoying.justice.cis.controller.dto.response.DockerContainerDeployInfo.DeployStatus.checkStatus;
import static com.lnjoying.justice.commonweb.common.SwaggerConstants.*;

/**
 * @Description: container deployment handler
 * @Author: Merak
 * @Date: 2022/1/10 10:31
 */

@Controller
@RestSchema(schemaId = "ContainerDeployController")
@RequestMapping("/cis/v1/docker/deployments")
@Api(value = "Container deployments Controller",tags = {"Container deployments Controller"})
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
        properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "container_deploy"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "container_deploys"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "容器部署"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "TblContainerSpecInfo")})})
public class ContainerDeployController extends RestWebController
{

    private static Logger LOGGER = LogManager.getLogger(ContainerDeployController.class);

    @Autowired
    private CisManagerService cisManagerService;

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get container deploy infos", response = Object.class, notes = DescriptionConfig.GET_CONTAIN_INST_INFOS_MSG)
    public ResponseEntity<GetContainerDeployInfosRsp> getContainerDeployInfos(@ApiParam(value = "status", required = false, name = "status") @RequestParam(value="status", required = false) Integer status,
                                                                              @ApiParam(value = "name", required = false, name = "name") @RequestParam(value="name", required = false) String name,
                                                                              @ApiParam(value = "region", required = false, name = "region") @RequestParam(value="region", required = false) String region,
                                                                              @ApiParam(value = "site", required = false, name = "site") @RequestParam(value="site", required = false) String site,
                                                                              @ApiParam(value = "node_id", required = false, name = "node_id") @RequestParam(value= "nodeId", required = false) String nodeId,
                                                                              @ApiParam(value = "user_id",required = false, name = "user_id") @RequestParam(value = "user_id", required = false) String ownerUserId,
                                                                              @ApiParam(value = "page size", required = false, name = "page_size") @RequestParam(value= "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                                                              @ApiParam(value = "page_num", required = false, name = "page_num") @RequestParam(value= "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                                                              @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                                                              @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {
        try
        {

            ContainerSearchCritical critical = new ContainerSearchCritical();

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
                critical.setRegion(region);
            }
            if (StringUtils.isNotBlank(site))
            {
                critical.setSite(site);
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

            LOGGER.info("get container inst list, condition: {}", critical);
            return ResponseEntity.ok().body(cisManagerService.getContainerDeployInfos(critical, ownerUserId));
        }
        catch (Exception e)
        {
            throw throwWebException(e);
        }
    }


    @GetMapping(value = "/{spec_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get container deploy info", response = Object.class, notes = DescriptionConfig.GET_CONTAIN_INST_INFOS_MSG)
    @LNJoyAuditLog(value = "get container deploy info",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH, bindParameterName = "specId"))
    public ResponseEntity<DockerContainerDeployInfo> getContainerDeployInfo(@ApiParam(value = "", required = true, name = "spec_id")@PathVariable("spec_id") String specId,
                                                                              @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                                                              @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {
        try
        {

            LOGGER.debug("get container spec info: {} .", specId);
            return ResponseEntity.ok().body(cisManagerService.getContainerDeployInfo(specId));
        }
        catch (Exception e)
        {
            throw throwWebException(e);
        }
    }


    @GetMapping(value = "/{spec_id}/instances", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get container deploy instances info", response = Object.class, notes = DescriptionConfig.GET_CONTAIN_INST_INFOS_MSG)
    @LNJoyAuditLog(value = "get container deploy instances info",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH, bindParameterName = "specId"))
    public ResponseEntity<GetContainerInstInfosRsp> getContainerDeployInstancesInfo(@ApiParam(value = "", required = true, name = "spec_id")@PathVariable("spec_id") String specId,
                                                                                     @ApiParam(value = "status", required = false, name = "status") @RequestParam(value="status", required = false) Integer status,
                                                                                     @ApiParam(value = "region", required = false, name = "region") @RequestParam(value="region", required = false) String region,
                                                                                     @ApiParam(value = "site", required = false, name = "site") @RequestParam(value="site", required = false) String site,
                                                                                     @ApiParam(value = "node_id", required = false, name = "node_id") @RequestParam(value= "nodeId", required = false) String nodeId,
                                                                                     @ApiParam(value = "page size", required = false, name = "page_size") @RequestParam(value= "pageSize", required = false) Integer pageSize,
                                                                                     @ApiParam(value = "page_num", required = false, name = "page_num") @RequestParam(value= "pageNum", required = false) Integer pageNum,
                                                                                     @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                                                                     @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {
        try
        {

            ContainerSearchCritical critical = new ContainerSearchCritical();

            if (null != status)
            {
                critical.setStatus(status);
            }
            if (StringUtils.isNotBlank(region))
            {
                critical.setRegion(region);
            }
            if (StringUtils.isNotBlank(site))
            {
                critical.setSite(site);
            }
            if (StringUtils.isNotBlank(nodeId))
            {
                critical.setNodeId(nodeId);
            }
            if (StringUtils.isNotBlank(specId))
            {
                critical.setSpecId(specId);
            }
            if (null != pageSize) critical.setPageSize(pageSize);
            if (null != pageNum) critical.setPageNum(pageNum);
            LOGGER.debug("get container spec Instances info: {} .", specId);
            return ResponseEntity.ok().body(cisManagerService.getContainerDeployInstancesInfo(critical));
        }
        catch (Exception e)
        {
            throw throwWebException(e);
        }
    }

    @DeleteMapping(value = "/{spec_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "delete deploy info", response = Object.class)
    @LNJoyAuditLog(value = "delete deploy info",
            resourceMapperId = CisResources.CONTAINER_SPEC_INFO,
            actionDescriptionTemplate = CisContainerSpecActionDescriptionTemplate.Descriptions.DELETE_CONTAINER_DEPLOY,
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH, bindParameterName = "specId"))
    public ResponseEntity<String> deleteContainerDeployInfo(@ApiParam(value = "", required = true, name = "spec_id")@PathVariable("spec_id") String specId,
                                                                            @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                                                            @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {
        try
        {

            LOGGER.debug("delete container spec info: {} .", specId);
            cisManagerService.deleteContainerDeployInfo(specId);
            return ResponseEntity.ok().body(null);
        }
        catch (Exception e)
        {
            throw throwWebException(e);
        }
    }

}
