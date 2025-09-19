package com.lnjoying.justice.ims.controller;

import com.lnjoying.justice.commonweb.handler.aspect.annotation.LNJoyAuditLog;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.ResourceIdExtractConfiguration;
import com.lnjoying.justice.commonweb.handler.aspect.enums.ResourceIdLocationType;
import com.lnjoying.justice.ims.common.ImsResources;
import com.lnjoying.justice.ims.db.model.TblImsRegistryProject;
import com.lnjoying.justice.ims.domain.dto.req.AddRegistryProjectReq;
import com.lnjoying.justice.ims.domain.dto.req.UpdateRegistryProjectReq;
import com.lnjoying.justice.ims.domain.dto.rsp.RegistryProjectsRsp;
import com.lnjoying.justice.ims.domain.model.ImsRegistryProject;
import com.lnjoying.justice.ims.exception.ImsWebSystemException;
import com.lnjoying.justice.ims.facade.ImsRegistryProjectFacade;
import com.lnjoying.justice.ims.handler.actiondescription.RegistryProjectActionDescriptionTemplate;
import com.lnjoying.justice.schema.common.ErrorLevel;
import com.lnjoying.justice.schema.constant.ResourceOperationTypeConstants;
import com.lnjoying.justice.schema.constant.UserHeadInfo;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Extension;
import io.swagger.annotations.ExtensionProperty;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

import static com.lnjoying.justice.commonweb.common.SwaggerConstants.*;
import static com.lnjoying.justice.schema.common.ErrorCode.BP_VISIBLE_PROJECT_NEED_TO_HAVE_BPID;
import static com.lnjoying.justice.schema.common.ErrorCode.INCORRECT_USER_ID;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.ResponseEntity.status;

/**
 * project handler
 *
 * @author merak
 **/

@RestSchema(schemaId = "imsRegistryProjectController")
@RequestMapping(path = "/ims/v1/docker/registries/{registry_id}/projects")
@Slf4j
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
        properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "project"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "projects"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "项目"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "TblImsRegistryProject")})})
public class ImsRegistryProjectController extends ImsWebController
{

    @Autowired
    private ImsRegistryProjectFacade imsRegistryProjectFacade;

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "add Registry project")
    @LNJoyAuditLog(value = "add Registry project",
            tags = ResourceOperationTypeConstants.RESOURCE_CREATE,
            actionDescriptionTemplate = RegistryProjectActionDescriptionTemplate.Descriptions.ADD_REGISTRY_PROJECT,
            isResourceInstanceNameFromArgs = true,
            resourceInstanceNameFromArgsParseSpEl = "#obj?.get('projectName')?.toString()",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.METHOD_RETURN_VALUE,
                            resourceIdParseSPEL = "#obj?.body?.toString()"))
    public ResponseEntity<String> addRegistryProject(@ApiParam(required = true, name = "registry_id") @PathVariable String registryId,
                                                     @ApiParam(required = true, name = "registryProjectReq") @RequestBody @Valid AddRegistryProjectReq req,
                                                     @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                                     @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                                     @RequestHeader(name = UserHeadInfo.USERNAME, required = false) String userName,
                                                     @RequestHeader(name = UserHeadInfo.BpName, required = false) String bpName,
                                                     @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {
        
        log.info("add registry project. req: {}, userId: {}", req, userId);
        
        setBaseReq(req, userId, bpId, userName, bpName);
        req.setRegistryId(registryId);

        // bp visible project need to have bpid
        if (StringUtils.isBlank(req.getBpId()) && req.getScope().equals(TblImsRegistryProject.ProjectScope.BP))
        {
            throw new ImsWebSystemException(BP_VISIBLE_PROJECT_NEED_TO_HAVE_BPID, ErrorLevel.INFO);
        }

        return status(CREATED).body(imsRegistryProjectFacade.addRegistryProject(req));
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get registry project list")
    public ResponseEntity<RegistryProjectsRsp> getRegistryProjectList(@ApiParam(required = false, name = "registry_id") @PathVariable(required = false) String registryId,
                                                                      @ApiParam(name = "project_name") @RequestParam(value = "project_name", required = false) String projectName,
                                                                      @ApiParam(name = "scope") @RequestParam(value = "scope", required = false) Integer scope,
                                                                      @ApiParam(name = "user_id") @RequestParam(value = "user_id", required = false) String ownerUserId,
                                                                      @ApiParam(name = "bp_id") @RequestParam(value = "bp_id", required = false) String ownerBpId,
                                                                      @ApiParam(name = "page_size") @RequestParam(value = "page_size", required = false, defaultValue = Integer.MAX_VALUE + "") Integer pageSize,
                                                                      @ApiParam(name = "page_num") @RequestParam(value = "page_num", required = false, defaultValue = "1") Integer pageNum,
                                                                      @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                                                      @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                                                      @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {
        
        RegistryProjectsRsp res = RegistryProjectsRsp.builder().build();

        if (isRoleImsAdmin(authorities))
        {
            res = imsRegistryProjectFacade.getProjectList(registryId, projectName, scope, ownerUserId, ownerBpId, pageNum, pageSize, true, false);
        }
        else if (isRoleImsTenantAdmin(authorities))
        {
            if (StringUtils.isNotBlank(ownerUserId))
            {
                res = imsRegistryProjectFacade.getProjectList(registryId, projectName, scope, ownerUserId, bpId, pageNum, pageSize, false, true);
            }
            else
            {
                res = imsRegistryProjectFacade.getProjectList(registryId, projectName, scope, ownerUserId, bpId, pageNum, pageSize, false, false);
            }
        }
        else if (isRoleImsTenant(authorities))
        {
            if (StringUtils.isNotBlank(ownerUserId))
            {
                if (!userId.equals(ownerUserId))
                {
                    throw new ImsWebSystemException(INCORRECT_USER_ID, ErrorLevel.ERROR);
                }
                // ownerUserId is not blank means that only the projects created by the owner user are displayed, excluding BP projects and other public projects
                res = imsRegistryProjectFacade.getProjectList(registryId, projectName, scope, userId, bpId, pageNum, pageSize, false, true);
            }
            else
            {
                res = imsRegistryProjectFacade.getProjectList(registryId, projectName, scope, userId, bpId, pageNum, pageSize, false, false);
            }
        }

        return status(OK).body(res);
    }

    @PutMapping(value = "/{project_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "update registry project")
    @LNJoyAuditLog(value = "update registry project",
            actionDescriptionTemplate = RegistryProjectActionDescriptionTemplate.Descriptions.UPDATE_REGISTRY_PROJECT,
            resourceMapperId = ImsResources.PROJECT,
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "projectId"))
    public ResponseEntity<Map<String, String>> updateRegistryProject(@ApiParam(required = false, name = "registry_id") @PathVariable(required = false) String registryId,
                                                                     @ApiParam(required = true, name = "project_id") @PathVariable("project_id") String projectId,
                                                                     @ApiParam(required = true, name = "req") @RequestBody UpdateRegistryProjectReq req,
                                                                     @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                                                     @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                                                     @RequestHeader(name = UserHeadInfo.USERNAME, required = false) String userName,
                                                                     @RequestHeader(name = UserHeadInfo.BpName, required = false) String bpName,
                                                                     @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {
        
        log.info("update registry project. req: {}, userId: {}", req, userId);

        setBaseReq(req, userId, bpId, userName, bpName);
        req.setProjectId(projectId);
        imsRegistryProjectFacade.updateRegistryProject(req);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @DeleteMapping(value = "/{project_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "delete registry project")
    @LNJoyAuditLog(value = "delete registry project",
            actionDescriptionTemplate = RegistryProjectActionDescriptionTemplate.Descriptions.DELETE_REGISTRY_PROJECT,
            resourceMapperId = ImsResources.PROJECT,
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "projectId"))
    public ResponseEntity<String> deleteRegistryProject(@ApiParam(required = false, name = "registry_id") @PathVariable(required = false) String registryId,
                                                        @ApiParam(required = true, name = "project_id") @PathVariable("project_id") String projectId,
                                                        @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                                        @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                                        @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {
        
        log.info("delete registry project: {}, userId: {}", projectId, userId);

        if (isRoleImsAdmin(authorities))
        {
            imsRegistryProjectFacade.deleteRegistryProject(projectId, null, null);
        }
        else if (isRoleImsTenantAdmin(authorities))
        {
            imsRegistryProjectFacade.deleteRegistryProject(projectId, bpId, null);
        }
        else if (isRoleImsTenant(authorities))
        {
            imsRegistryProjectFacade.deleteRegistryProject(projectId, null, userId);
        }
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping(value = "/{project_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get registry project info")
    @LNJoyAuditLog(value = "get registry project info",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "projectId"))
    public ResponseEntity<ImsRegistryProject> getRegistryProject(@ApiParam(required = false, name = "registry_id") @PathVariable(required = false) String registryId,
                                                                 @ApiParam(required = true, name = "project_id") @PathVariable("project_id") String projectId,
                                                                 @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                                                 @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                                                 @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {

        if (isRoleImsAdmin(authorities))
        {
            return status(OK).body(imsRegistryProjectFacade.getRegistryProject(projectId, null, null));
        }
        else if (isRoleImsTenantAdmin(authorities))
        {
            return status(OK).body(imsRegistryProjectFacade.getRegistryProject(projectId, bpId, null));
        }
        return status(OK).body(imsRegistryProjectFacade.getRegistryProject(projectId, bpId, userId));
    }
}
