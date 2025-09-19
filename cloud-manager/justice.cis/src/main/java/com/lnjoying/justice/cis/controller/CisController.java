package com.lnjoying.justice.cis.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lnjoying.justice.cis.common.constant.CisResources;
import com.lnjoying.justice.cis.common.constant.ContainerSearchCritical;
import com.lnjoying.justice.cis.common.constant.InstAction;
import com.lnjoying.justice.cis.config.DescriptionConfig;
import com.lnjoying.justice.cis.controller.dto.request.CreateContainerInstReq;
import com.lnjoying.justice.cis.controller.dto.request.RemoteExecCmdReq;
import com.lnjoying.justice.cis.controller.dto.request.RemoteExecCmdRsp;
import com.lnjoying.justice.cis.controller.dto.response.ContainerOperatorLog;
import com.lnjoying.justice.cis.controller.dto.response.ContainerRunLog;
import com.lnjoying.justice.cis.controller.dto.response.GetContainerInstInfosRsp;
import com.lnjoying.justice.cis.db.model.TblContainerOperatorInfo;
import com.lnjoying.justice.cis.handler.actiondescription.i18n.zh_cn.CisContainerInstActionDescriptionTemplate;
import com.lnjoying.justice.cis.handler.actiondescription.i18n.zh_cn.CisContainerSpecActionDescriptionTemplate;
import com.lnjoying.justice.cis.service.CisManagerService;
import com.lnjoying.justice.commonweb.controller.RestWebController;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.LNJoyAuditLog;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.ResourceIdExtractConfiguration;
import com.lnjoying.justice.commonweb.handler.aspect.enums.ResourceIdLocationType;
import com.lnjoying.justice.commonweb.util.JsonUtils;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.common.ErrorLevel;
import com.lnjoying.justice.schema.constant.ResourceOperationTypeConstants;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

import static com.lnjoying.justice.commonweb.common.SwaggerConstants.*;
import static com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils.*;

@Controller
@RestSchema(schemaId = "cis-manager")
@RequestMapping("/cis/v1/docker/instances")
@Api(value = "Cis Controller",tags = {"Cis Controller"})
@Validated
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
        properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "container"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "containers"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "容器"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "TblContainerSpecInfo,TblContainerInstInfo")})})
public class CisController extends RestWebController
{
    private static Logger LOGGER = LogManager.getLogger(CisController.class);

    @Autowired
    private CisManagerService cisManagerService;

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "create container instance", response = Object.class, notes = DescriptionConfig.CREATE_CONTAIN_INST_MSG)
    @LNJoyAuditLog(value = "create container instance",
            tags = ResourceOperationTypeConstants.RESOURCE_CREATE,
            resourceMapperId = CisResources.CONTAINER_SPEC_INFO,
            actionDescriptionTemplate = CisContainerSpecActionDescriptionTemplate.Descriptions.ADD_CONTAINER_DEPLOY,
            associatedResourceCnName = "容器",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.METHOD_RETURN_VALUE,
                            resourceIdParseSPEL = "#obj?.body?.get('id')?.toString()"))
    public ResponseEntity<Map<String,String>> createContainerInst(@ApiParam(value = "create container inst req", required = true, name = "create container instance req") @RequestBody @Valid CreateContainerInstReq req,
                                                                  @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                                                  @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                                                  @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {
        try
        {
            LOGGER.debug("create container instance. req:{}", req);

            if (StringUtils.isNotBlank(req.getBp_id()) && StringUtils.isNotBlank(req.getUser_id()))
            {
                bpId = req.getBp_id();
                userId = req.getUser_id();
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(cisManagerService.createContainerInst(req, bpId, userId));
        }
        catch (Exception e)
        {
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get container instance infos", response = Object.class, notes = DescriptionConfig.GET_CONTAIN_INST_INFOS_MSG)
    public ResponseEntity<GetContainerInstInfosRsp> getContainerInstInfos(@ApiParam(value = "status", required = false, name = "status") @RequestParam(value="status", required = false) Integer status,
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

            if (null != status) critical.setStatus(status);
            if (null != region) critical.setRegion(region);
            if (null != site) critical.setSite(site);
            if (null != nodeId) critical.setNodeId(nodeId);
            if (null != pageSize) critical.setPageSize(pageSize);
            if (null != pageNum) critical.setPageNum(pageNum);

            LOGGER.info("get container inst list, condition: {}", critical);

            return ResponseEntity.ok().body(cisManagerService.getContainerInstInfos(critical));
        }
        catch (Exception e)
        {
            throw throwWebException(e);
        }
    }

    @PostMapping(value = "/{instanceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "container instance action by instanceId", response = Object.class, notes = DescriptionConfig.CONTAIN_INST_ACTION_MSG)
    @LNJoyAuditLog(value = "container instance action by instanceId",
            resourceMapperId = CisResources.CONTAINER,
            actionDescriptionTemplateClass = CisContainerInstActionDescriptionTemplate.class,
            actionDescriptionField = "action", actionDescriptionFormatString = "control container instance: %s",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH, bindParameterName = "instanceId"))
    @ResponseBody    @ResponseStatus(HttpStatus.OK)
    public Object containerInstAction(@ApiParam(value = "", required = true, name = "instanceId")@PathVariable String instanceId,
                                      @ApiParam(value = "action", required = true, name = "action") @RequestParam(value="action", required = true) String action,
//                                      @ApiParam(value = "remote execute cmd req", required = false, name = "remote execute cmd req") @RequestBody(required = false) RemoteExecCmdReq remoteExecCmdReq,
                                      @ApiParam(value = "cmd req", required = false, name = "cmd req")@RequestBody(required = false)  ObjectNode cmdReqObj,
                                      @ApiParam(value = "", required = false, name = "flow") @RequestParam(value = "flow", required = false, defaultValue = "true") boolean flow,
                                      @ApiParam(value = "", required = false, name = "lines") @RequestParam(value = "lines", required = false, defaultValue = "500") int lines,
                                      @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                      @RequestHeader(name = UserHeadInfo.USERNAME, required = false) String userName,
                                      @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                      @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {

        TblContainerOperatorInfo tblContainerOperatorInfo = new TblContainerOperatorInfo();
        tblContainerOperatorInfo.setOperType(action);
        tblContainerOperatorInfo.setInstId(instanceId);
        tblContainerOperatorInfo.setOperatorId(userId);
        tblContainerOperatorInfo.setOperatorName(userName);
        tblContainerOperatorInfo.setOperResult("success");
        try
        {
            checkAuthorityToOperateInst(authorities, userId, bpId, instanceId);
            if(null == action)
            {
                throw new WebSystemException(ErrorCode.ACTION_NOT_SUPPORT, ErrorLevel.INFO);
            }
            else if(action.equals(InstAction.START.getName())
                    || action.equals(InstAction.STOP.getName())
                    || action.equals(InstAction.RESTART.getName()))
            {
                LOGGER.debug("container inst action, instance id: {}, action: {} .", instanceId, action);
                return cisManagerService.containerLifeMng(instanceId, action);
            }
            else if (action.equals(InstAction.EXECUTE.getName()))
            {
                if (cmdReqObj == null)
                {
                    throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO);
                }

                LOGGER.debug("remote execute command, instance id: {}, action: {} .", instanceId, cmdReqObj);
                RemoteExecCmdReq remoteExecCmdReq = JsonUtils.fromJson(cmdReqObj.toString(), RemoteExecCmdReq.class);
                remoteExecCmdReq.setInstId(instanceId);
                return cisManagerService.remoteExecute(remoteExecCmdReq);
            }
            else if (action.equals(InstAction.LOGS.getName()))
            {
                LOGGER.debug("log inst : {}, flow : {}, lines :{} .", instanceId, flow, lines);
                return cisManagerService.logFollowInst(instanceId, flow, lines);
            }
            else
            {
                throw new WebSystemException(ErrorCode.ACTION_NOT_SUPPORT, ErrorLevel.INFO);
            }
        }
        catch (Exception e)
        {
            tblContainerOperatorInfo.setOperResult(e.getMessage());
            throw throwWebException(e);
        }
        finally
        {
            cisManagerService.insertOperatorInfo(tblContainerOperatorInfo);
        }
    }

    @DeleteMapping(value = "/{instanceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "remove container by instanceId", response = Object.class, notes = DescriptionConfig.DEL_CONTAIN_INST_MSG)
    @LNJoyAuditLog(value = "remove container by instanceId",
            resourceMapperId = CisResources.CONTAINER,
            associatedResourceCnName = "容器",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH, bindParameterName = "instanceId"))
    public ResponseEntity<String> removeContainerInst(@ApiParam(value = "instance id") @PathVariable("instanceId") String instanceId,
                                                      @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                                      @RequestHeader(name = UserHeadInfo.USERNAME, required = false) String userName,
                                                      @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                                      @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {
        checkAuthorityToOperateInst(authorities, userId, bpId, instanceId);

        TblContainerOperatorInfo tblContainerOperatorInfo = new TblContainerOperatorInfo();
        tblContainerOperatorInfo.setOperType(InstAction.REMOVE.getName());
        tblContainerOperatorInfo.setInstId(instanceId);
        tblContainerOperatorInfo.setOperatorId(userId);
        tblContainerOperatorInfo.setOperatorName(userName);
        tblContainerOperatorInfo.setOperResult("success");

        try
        {
            LOGGER.debug("delete container inst: {} .", instanceId);
            cisManagerService.containerLifeMng(instanceId, InstAction.REMOVE.getName());
            return ResponseEntity.ok(null);
        }
        catch (Exception e)
        {
            tblContainerOperatorInfo.setOperResult(e.getMessage());
            throw throwWebException(e);
        }
        finally
        {
            cisManagerService.insertOperatorInfo(tblContainerOperatorInfo);
        }
    }

    @PostMapping(value = "/remote/{instanceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "remote command execute", response = Object.class, notes = DescriptionConfig.REMOTE_COMMAND_EXECUTE)
    @LNJoyAuditLog(value = "remote command execute",
            resourceMapperId = CisResources.CONTAINER,
            actionDescriptionTemplate = CisContainerInstActionDescriptionTemplate.Descriptions.REMOTE_CMD_EXECUTE,
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH, bindParameterName = "instanceId"))
    public ResponseEntity<RemoteExecCmdRsp> remoteExecute(@ApiParam(value = "", required = true, name = "instanceId")@PathVariable String instanceId,
                                                          @ApiParam(value = "remote execute cmd req", required = true, name = "remote execute cmd req") @RequestBody RemoteExecCmdReq req,
                                                          @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                                          @RequestHeader(name = UserHeadInfo.USERNAME, required = false) String userName,
                                                          @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                                          @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {

        checkAuthorityToOperateInst(authorities, userId, bpId, instanceId);

        TblContainerOperatorInfo tblContainerOperatorInfo = new TblContainerOperatorInfo();
        tblContainerOperatorInfo.setOperType(InstAction.REMOVE.getName());
        tblContainerOperatorInfo.setInstId(instanceId);
        tblContainerOperatorInfo.setOperatorId(userId);
        tblContainerOperatorInfo.setOperatorName(userName);
        tblContainerOperatorInfo.setOperResult("success");

        try
        {
            LOGGER.debug("remote execute command, instance id: {}, req: {} .", instanceId, req);
            req.setInstId(instanceId);
            return ResponseEntity.ok().body(cisManagerService.remoteExecute(req));
        }
        catch (Exception e)
        {
            tblContainerOperatorInfo.setOperResult(e.getMessage());
            throw throwWebException(e);
        }
        finally
        {
            cisManagerService.insertOperatorInfo(tblContainerOperatorInfo);
        }
    }

    @GetMapping(value = "/{instanceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get container info by instanceId", response = Object.class, notes = DescriptionConfig.GET_CONTAIN_INST_INFO)
    @LNJoyAuditLog(value = "get container info by instanceId",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH, bindParameterName = "instanceId"))
    @ResponseBody    @ResponseStatus(HttpStatus.OK)
    public Object getContainerInstInfo(@ApiParam(value = "", required = true, name = "instanceId")@PathVariable String instanceId,
                                       @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                       @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                       @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {

        checkAuthorityToOperateInst(authorities, userId, bpId, instanceId);

        try
        {
            LOGGER.debug("get container inst info: {} .", instanceId);
            return cisManagerService.getContainerInstInfo(instanceId);
        }
        catch (Exception e)
        {
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/{instanceId}/stats", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get container stats by instanceId", response = Object.class, notes = DescriptionConfig.GET_CONTAINER_INST_STATS)
    @LNJoyAuditLog(value = "get container stats by instanceId",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH, bindParameterName = "instanceId"))
    @ResponseBody    @ResponseStatus(HttpStatus.OK)
    public Object getContainerInstStats(@ApiParam(value = "", required = true, name = "instanceId") @PathVariable String instanceId,
                                        @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                        @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                        @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {

        checkAuthorityToOperateInst(authorities, userId, bpId, instanceId);

        try
        {
            LOGGER.debug("get container inst stats: {}. ", instanceId);
            return cisManagerService.getContainerInstStats(instanceId);
        }
        catch (Exception e)
        {
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/{instanceId}/oper-logs", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get container oper logs by instanceId", response = Object.class, notes = DescriptionConfig.GET_CONTAIN_INST_OPER_LOGS)
    @LNJoyAuditLog(value = "get container oper logs by instanceId",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH, bindParameterName = "instanceId"))
    @ResponseBody    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<ContainerOperatorLog>> getContainerInstOperLogs(@ApiParam(value = "", required = true, name = "instanceId") @PathVariable String instanceId,
                                                                               @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                                                               @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                                                               @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {

        checkAuthorityToOperateInst(authorities, userId, bpId, instanceId);

        try
        {
            LOGGER.debug("get container inst oper logs: {}. ", instanceId);
            return ResponseEntity.ok().body(cisManagerService.getContainerInstOperLogs(instanceId));
        }
        catch (Exception e)
        {
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/{instanceId}/run-logs", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get container run logs by instanceId", response = Object.class, notes = DescriptionConfig.GET_CONTAIN_INST_RUN_LOGS)
    @LNJoyAuditLog(value = "get container run logs by instanceId",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH, bindParameterName = "instanceId"))
    @ResponseBody    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<ContainerRunLog>> getContainerInstRunLogs(@ApiParam(value = "", required = true, name = "instanceId") @PathVariable String instanceId,
                                                                         @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                                                         @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                                                         @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {

        checkAuthorityToOperateInst(authorities, userId, bpId, instanceId);

        try
        {
            LOGGER.debug("get container inst run logs: {}. ", instanceId);
            return ResponseEntity.ok().body(cisManagerService.getContainerInstRunLogs(instanceId));
        }
        catch (Exception e)
        {
            throw throwWebException(e);
        }
    }

    private void checkAuthorityToOperateInst(String authorities, String userId, String bpId, String instanceId)
    {
        if (StringUtils.isBlank(getKind()))
        {
            throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
        }
        else if (isBpAdmin())
        {
            if (!cisManagerService.isOwnerOfInst(instanceId, null, userId) && !cisManagerService.isOwnerOfInst(instanceId, bpId, null))
            {
                throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
            }
        }
        else if (isBpUser())
        {
            if (!cisManagerService.isOwnerOfInst(instanceId, bpId, userId))
            {
                throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
            }
        }
        else if(! isAdmin())
        {
            throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
        }
    }
}
