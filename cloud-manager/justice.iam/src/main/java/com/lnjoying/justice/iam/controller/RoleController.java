package com.lnjoying.justice.iam.controller;

import com.lnjoying.justice.commonweb.handler.aspect.annotation.LNJoyAuditLog;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.ResourceIdExtractConfiguration;
import com.lnjoying.justice.commonweb.handler.aspect.enums.ResourceIdLocationType;
import com.lnjoying.justice.iam.common.constant.IamResources;
import com.lnjoying.justice.iam.config.DescriptionConfig;
import com.lnjoying.justice.iam.db.model.TblIamRole;
import com.lnjoying.justice.iam.domain.dto.request.role.AddRoleReq;
import com.lnjoying.justice.iam.domain.dto.request.role.UpdateRoleReq;
import com.lnjoying.justice.iam.domain.dto.response.role.RoleDto;
import com.lnjoying.justice.iam.domain.dto.response.role.RoleRsp;
import com.lnjoying.justice.iam.handler.actiondescription.i18n.zh_cn.RoleActionDescriptionTemplate;
import com.lnjoying.justice.iam.handler.actiondescription.i18n.zh_cn.UserRoleActionDescriptionTemplate;
import com.lnjoying.justice.iam.service.RoleService;
import com.lnjoying.justice.iam.service.UserManagerService;
import com.lnjoying.justice.iam.utils.ServiceCombRequestUtils;
import com.lnjoying.justice.schema.constant.ResourceOperationTypeConstants;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

import static com.lnjoying.justice.commonweb.common.SwaggerConstants.*;
import static com.lnjoying.justice.iam.utils.ServiceCombRequestUtils.*;
import static com.lnjoying.justice.iam.utils.ServiceCombRequestUtils.getUserAttributes;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.ResponseEntity.status;

@RestSchema(schemaId = "role-manager")
@RequestMapping("/api/iam/v1/roles")
@Controller
@Api(value = "Role Controller",tags = {"Role Controller"})
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
        properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "role"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "roles"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "角色"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "tbl_iam_role")})})
@Slf4j
public class RoleController  extends IamRestWebController
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    private UserManagerService userManagerService;

    @Autowired
    private RoleService roleService;

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "add role")
    @LNJoyAuditLog(value = "add role",
            tags = ResourceOperationTypeConstants.RESOURCE_CREATE,
            actionDescriptionTemplateClass = RoleActionDescriptionTemplate.class,
            isResourceInstanceNameFromArgs = true,
            resourceInstanceNameFromArgsParseSpEl = "#obj?.get('role')?.toString()",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.METHOD_RETURN_VALUE,
                            resourceIdParseSPEL = "#obj?.body?.toString()"))
    public ResponseEntity<Object> addRole(@ApiParam(required = true, name = "roleReq") @RequestBody @Valid AddRoleReq req)
    {
        String bpId = getBpId();
        String userId = getUserId();
        log.info("add role. req: {}, userId: {}", req, userId);

        setBaseReq(req, bpId, userId);
        if (!isAdmin())
        {
            req.setRoleType(TblIamRole.RoleType.CUSTOM.value());
        }
        return status(CREATED).body(roleService.addRole(req));
    }

    @PutMapping(value = "/{role_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "update role")
    @LNJoyAuditLog(value = "update role",
            actionDescriptionTemplateClass = RoleActionDescriptionTemplate.class,
            isResourceInstanceNameFromArgs = true,
            resourceInstanceNameFromArgsParseSpEl = "#obj?.get('role')?.toString()",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "roleId"))
    public ResponseEntity<Map<String, String>> updateRole(@ApiParam(required = true, name = "role_id") @PathVariable long roleId,
                                                             @ApiParam(required = true, name = "req") @RequestBody @Valid UpdateRoleReq req)
    {
        String bpId = getBpId();
        String userId = getUserId();
        log.info("update role. req: {}, userId: {}", req, userId);
        if (!isAdmin())
        {
            req.setRoleType(TblIamRole.RoleType.CUSTOM.value());
        }
        // update userId and bpId
        setBaseReq(req, bpId, userId);
        roleService.updateRole(roleId, req);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get role list")
    public ResponseEntity<Object> getRoleList(@ApiParam(name = "name") @RequestParam(value = "name", required = false) String name,
                                              @ApiParam(name = "project_id") @RequestParam(value = "project_id", required = false) String projectId,
                                              @ApiParam(name = "bp_id") @RequestParam(value = "bp_id", required = false) String queryBpId,
                                              @ApiParam(name = "role_type") @RequestParam(value = "role_type", required = false) Integer roleType,
                                              @ApiParam(name = "page_size") @RequestParam(value = "page_size", required = false, defaultValue = Integer.MAX_VALUE + "") Integer pageSize,
                                              @ApiParam(name = "page_num") @RequestParam(value = "page_num", required = false, defaultValue = "1") Integer pageNum)
    {

        if (!isAdmin())
        {
            queryBpId = getUserAttributes().getLeft();
        }
        RoleRsp roleRsp =  roleService.getRoleList(name, projectId, queryBpId, roleType, pageSize, pageNum);

        return status(OK).body(roleRsp);
    }

    @GetMapping(value = "/{role_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get role info")
    @LNJoyAuditLog(value = "get role info",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "roleId"))
    public ResponseEntity<Object> getRole(@ApiParam(required = true, name = "role_id") @PathVariable long roleId)
    {
        String queryBpId = "";
        if (!isAdmin())
        {
            queryBpId = getUserAttributes().getLeft();
        }
        return status(OK).body(roleService.getRole(roleId, queryBpId));

    }

    @DeleteMapping(value = "/{role_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "delete role")
    @LNJoyAuditLog(value = "delete role",
            resourceMapperId = IamResources.ROLE,
            actionDescriptionTemplateClass = RoleActionDescriptionTemplate.class,
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "roleId",
                    resourcePrimaryKeyClass = Long.class, convertToResourcePrimaryKeyTypeSpEl = "T(Long).valueOf(#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<String> deleteRole(@ApiParam(required = true, name = "role_id") @PathVariable long roleId)
    {

        log.info("delete role: {}, userId: {}", roleId, getUserId());
        String queryBpId = "";
        if (!isAdmin())
        {
            queryBpId = getUserAttributes().getLeft();
        }
        roleService.deleteRole(roleId, queryBpId);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PostMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "add roles to user by userId", response = Object.class, notes = DescriptionConfig.ADD_USER_ROLE_MSG)
    @LNJoyAuditLog(value = "add roles to user by userId",
            actionDescriptionTemplateClass = UserRoleActionDescriptionTemplate.class)
    @ResponseBody    @ResponseStatus(HttpStatus.OK)
    public void addUserRoles(
            @ApiParam(value = "", name = "roleRaw") @RequestBody List<RoleDto> roleRawReq,
            @ApiParam(value = "", required = true, name = "userId")@PathVariable String userId)
    {
        try
        {
            LOGGER.debug("add user role {}", roleRawReq);
            userManagerService.addRolesByUserId(userId, roleRawReq);
            return;
        }
        catch (Exception e)
        {
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get user's roles by userId", response = Object.class, notes = DescriptionConfig.GET_USER_ROLE_MSG)
    @LNJoyAuditLog(value = "get user's roles by userId",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "userId"))
    @ResponseBody    @ResponseStatus(HttpStatus.OK)
    public Object getUserRole(
            @ApiParam(value = "", required = true, name = "userId")@PathVariable String userId)
    {
        try
        {
            ServiceCombRequestUtils.checkRoleTenantAdminOrAdmin();

            LOGGER.debug("add user role, userId: {}", userId);
            return userManagerService.getRolesByUserId(userId);
        }
        catch (Exception e)
        {
            throw throwWebException(e);
        }
    }
}
