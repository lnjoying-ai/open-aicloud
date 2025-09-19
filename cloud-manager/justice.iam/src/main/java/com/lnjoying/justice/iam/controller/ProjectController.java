package com.lnjoying.justice.iam.controller;

import com.lnjoying.justice.commonweb.handler.aspect.annotation.LNJoyAuditLog;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.ResourceIdExtractConfiguration;
import com.lnjoying.justice.commonweb.handler.aspect.enums.ResourceIdLocationType;
import com.lnjoying.justice.iam.common.constant.IamResources;
import com.lnjoying.justice.iam.domain.dto.request.project.ProjectGroupsReq;
import com.lnjoying.justice.iam.domain.dto.request.project.AddProjectReq;
import com.lnjoying.justice.iam.domain.dto.request.project.ProjectUsersReq;
import com.lnjoying.justice.iam.domain.dto.request.project.UpdateProjectReq;
import com.lnjoying.justice.iam.domain.dto.response.project.ProjectsRsp;
import com.lnjoying.justice.iam.domain.model.IamProject;
import com.lnjoying.justice.iam.handler.actiondescription.i18n.zh_cn.ProjectActionDescriptionTemplate;
import com.lnjoying.justice.iam.handler.actiondescription.i18n.zh_cn.ProjectUserActionDescriptionTemplate;
import com.lnjoying.justice.iam.handler.actiondescription.i18n.zh_cn.ProjectUserGroupActionDescriptionTemplate;
import com.lnjoying.justice.iam.service.ProjectService;
import com.lnjoying.justice.iam.utils.tree.Tree;
import com.lnjoying.justice.schema.constant.ResourceOperationTypeConstants;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import java.util.List;
import java.util.Map;

import static com.lnjoying.justice.commonweb.common.SwaggerConstants.*;
import static com.lnjoying.justice.iam.utils.ServiceCombRequestUtils.*;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.ResponseEntity.status;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/2/9 14:25
 */

@RestSchema(schemaId = "projects-manager")
@RequestMapping("/api/iam/v1/projects")
@Controller
@Api(value = "project Controller",tags = {"project Controller"})
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
        properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "project"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "projects"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "项目"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "tbl_iam_project")})})
@Slf4j
public class ProjectController  extends IamRestWebController
{

    @Autowired
    private ProjectService projectService;

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "add project")
    @LNJoyAuditLog(value = "add project",
            tags = ResourceOperationTypeConstants.RESOURCE_CREATE,
            actionDescriptionTemplateClass = ProjectActionDescriptionTemplate.class,
            isResourceInstanceNameFromArgs = true,
            resourceInstanceNameFromArgsParseSpEl = "#obj?.get('displayName')?.toString()",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.METHOD_RETURN_VALUE,
                            resourceIdParseSPEL = "#obj?.body?.toString()"))
    public ResponseEntity<Object> addProject(@ApiParam(required = true, name = "projectReq") @RequestBody @Valid AddProjectReq projectReq)
    {
        String bpId = getBpId();
        String userId = getUserId();
        log.info("add project. req: {}, userId: {}", projectReq, userId);

        setBaseReq(projectReq, bpId, userId);
        return status(CREATED).body(projectService.addProject(projectReq));
    }

    @PutMapping(value = "/{project_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "update project")
    @LNJoyAuditLog(value = "update project",
            resourceMapperId = IamResources.PROJECT,
            actionDescriptionTemplateClass = ProjectActionDescriptionTemplate.class,
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "projectId"))
    public ResponseEntity<Map<String, String>> updateProject(@ApiParam(required = true, name = "project_id") @PathVariable String projectId,
                                                              @ApiParam(required = true, name = "req") @RequestBody @Valid UpdateProjectReq req)
    {
        String bpId = getBpId();
        String userId = getUserId();
        log.info("update project. req: {}, userId: {}", req, userId);

        // update userId and bpId
        setBaseReq(req, bpId, userId);
        projectService.updateProject(projectId, req);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PatchMapping(value = "/{project_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "enable project or disable project")
    @LNJoyAuditLog(value = "enable project or disable project",
            actionDescriptionTemplateClass = ProjectActionDescriptionTemplate.class,
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "projectId"),
            actionDescriptionField = "action", actionDescriptionValueSpEl = "#obj", actionDescriptionFormatString = "project action: %s")
    public ResponseEntity<String> action(@ApiParam(required = true, name = "project_id") @PathVariable String projectId,
                                         @ApiParam(required = true, name = "action") @RequestParam(required = true) @Valid @Pattern(regexp = "(?i)deactive|(?i)active") String action)
    {
        String bpId = getBpId();
        String userId = getUserId();
        projectService.action(projectId, userId, bpId, action);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get project list")
    public ResponseEntity<Object> getProjectList(@ApiParam(name = "name") @RequestParam(value = "name", required = false) String name,
                                                  @ApiParam(name = "page_size") @RequestParam(value = "page_size", required = false, defaultValue = Integer.MAX_VALUE + "") Integer pageSize,
                                                  @ApiParam(name = "page_num") @RequestParam(value = "page_num", required = false, defaultValue = "1") Integer pageNum,
                                                 @ApiParam(name = "display_type") @RequestParam(value = "display_type", required = false, defaultValue = "tree") String displayType,
                                                  @ApiParam(name = "bp_id") @RequestParam(value = "bp_id", required = false) String queryBpId,
                                                  @ApiParam(name = "status") @RequestParam(value = "status", required = false) Integer status)
    {

        if (!isAdmin())
        {
            queryBpId = getUserAttributes().getLeft();
        }

        if (DisplayType.LIST.name().equalsIgnoreCase(displayType))
        {
            ProjectsRsp projectList = projectService.getProjectList(name, queryBpId, status, pageSize, pageNum);

            return status(OK).body(projectList);
        }
        else
        {
            ProjectsRsp projectTree = projectService.getProjectTree(name, queryBpId, status, pageSize, pageNum);
            return status(OK).body(projectTree);
        }

    }

    @GetMapping(value = "/{project_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get project info")
    @LNJoyAuditLog(value = "get project info",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "projectId"))
    public ResponseEntity<Object> getProject(@ApiParam(required = true, name = "project_id") @PathVariable String projectId)
    {
        String queryBpId = "";
        if (!isAdmin())
        {
            queryBpId = getUserAttributes().getLeft();
        }
        return status(OK).body(projectService.getProject(projectId, queryBpId));

    }

    @DeleteMapping(value = "/{project_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "delete project")
    @LNJoyAuditLog(value = "delete project",
            resourceMapperId = IamResources.PROJECT,
            actionDescriptionTemplateClass = ProjectActionDescriptionTemplate.class,
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "projectId"))
    public ResponseEntity<String> deleteProject(@ApiParam(required = true, name = "project_id") @PathVariable String projectId)
    {

        log.info("delete project: {}, userId: {}", projectId, getUserId());

        String queryBpId = "";
        if (!isAdmin())
        {
            queryBpId = getUserAttributes().getLeft();
        }
        projectService.deleteProject(projectId, queryBpId);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PostMapping(value = "/{project_id}/users", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "add users to project")
    @LNJoyAuditLog(value = "add users to project",
            tags = ResourceOperationTypeConstants.RESOURCE_CREATE,
            resourceMapperId = IamResources.PROJECT,
            actionDescriptionTemplateClass = ProjectUserActionDescriptionTemplate.class,
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "projectId"))
    public ResponseEntity<String> addUsersToProject(@ApiParam(required = true, name = "project_id") @PathVariable String projectId,
                                                    @ApiParam(required = true, name = "req") @RequestBody @Valid ProjectUsersReq req)
    {


        projectService.addUsersToProject(projectId, req);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping(value = "/{project_id}/users", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get users in project")
    @LNJoyAuditLog(value = "get users in project",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "projectId"))
    public ResponseEntity<Object> getUsersInProject(@ApiParam(required = true, name = "project_id") @PathVariable String projectId)
    {

        return ResponseEntity.status(HttpStatus.OK).body( projectService.getUsersInProject(projectId));
    }

    @DeleteMapping(value = "/{project_id}/users", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "delete users from project")
    @LNJoyAuditLog(value = "delete users from project",
            resourceMapperId = IamResources.PROJECT,
            actionDescriptionTemplateClass = ProjectUserActionDescriptionTemplate.class,
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "projectId"))
    public ResponseEntity<String> deleteUsersFromProject(@ApiParam(required = true, name = "project_id") @PathVariable String projectId,
                                                    @ApiParam(required = true, name = "req") @RequestBody @Valid ProjectUsersReq req)
    {


        projectService.deleteUsersFromProject(projectId, req);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }


    @GetMapping(value = "/{project_id}/groups", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get groups in project")
    @LNJoyAuditLog(value = "get groups in project",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "projectId"))
    public ResponseEntity<Object> getGroupsInProject(@ApiParam(required = true, name = "project_id") @PathVariable String projectId)
    {

        return ResponseEntity.status(HttpStatus.OK).body(projectService.getGroupsInProject(projectId));
    }

    @PostMapping(value = "/{project_id}/groups", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "add groups to project")
    @LNJoyAuditLog(value = "add groups to project",
            tags = ResourceOperationTypeConstants.RESOURCE_CREATE,
            resourceMapperId = IamResources.PROJECT,
            actionDescriptionTemplateClass = ProjectUserGroupActionDescriptionTemplate.class,
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "projectId"))
    public ResponseEntity<String> addGroupsToProject(@ApiParam(required = true, name = "project_id") @PathVariable String projectId,
                                                    @ApiParam(required = true, name = "req") @RequestBody @Valid ProjectGroupsReq req)
    {


        projectService.addGroupsToProject(projectId, req);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @DeleteMapping(value = "/{project_id}/groups", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "delete groups from project")
    @LNJoyAuditLog(value = "delete groups from project",
            resourceMapperId = IamResources.PROJECT,
            actionDescriptionTemplateClass = ProjectUserGroupActionDescriptionTemplate.class,
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "projectId"))
    public ResponseEntity<String> deleteGroupsFromProject(@ApiParam(required = true, name = "project_id") @PathVariable String projectId,
                                                     @ApiParam(required = true, name = "req") @RequestBody @Valid ProjectGroupsReq req)
    {


        projectService.deleteGroupsFromProject(projectId, req);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    private enum DisplayType
    {
        TREE,

        LIST;
    }
}
