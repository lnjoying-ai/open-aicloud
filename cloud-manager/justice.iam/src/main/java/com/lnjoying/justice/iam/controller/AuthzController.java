package com.lnjoying.justice.iam.controller;

import com.lnjoying.justice.commonweb.handler.aspect.annotation.LNJoyAuditLog;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.ResourceIdExtractConfiguration;
import com.lnjoying.justice.commonweb.handler.aspect.enums.ResourceIdLocationType;
import com.lnjoying.justice.iam.common.constant.IamResources;
import com.lnjoying.justice.iam.domain.dto.request.authz.*;
import com.lnjoying.justice.iam.domain.dto.response.authz.AttachmentsRsp;
import com.lnjoying.justice.iam.domain.dto.response.authz.CheckActionsRsp;
import com.lnjoying.justice.iam.domain.dto.response.authz.RoleAttachmentsRsp;
import com.lnjoying.justice.iam.handler.actiondescription.i18n.zh_cn.AuthzRoleActionDescriptionTemplate;
import com.lnjoying.justice.iam.handler.actiondescription.i18n.zh_cn.AuthzUserActionDescriptionTemplate;
import com.lnjoying.justice.iam.service.AuthzService;
import com.lnjoying.justice.schema.constant.ResourceOperationTypeConstants;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.lnjoying.justice.commonweb.common.SwaggerConstants.*;
import static com.lnjoying.justice.iam.db.model.TblIamAttachment.PrincipalType.*;
import static com.lnjoying.justice.iam.utils.ServiceCombRequestUtils.*;
import static com.lnjoying.justice.iam.utils.ServiceCombRequestUtils.getUserAttributes;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.ResponseEntity.status;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/2/14 15:15
 */

@RestSchema(schemaId = "authz-manager")
@RequestMapping("/api/iam/v1/authz")
@Controller
@Api(value = "authz Controller",tags = {"authz Controller"})
@Slf4j
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
        properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "authz"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "authzs"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "授权记录"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "tbl_iam_attachment")})})
public class AuthzController  extends IamRestWebController
{

    @Autowired
    private AuthzService authzService;

    @PostMapping(value = "/attach/user", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "attach user")
    @LNJoyAuditLog(value = "attach user",
            tags = ResourceOperationTypeConstants.RESOURCE_CREATE,
            resourceMapperId = IamResources.USER,
            actionDescriptionTemplateClass = AuthzUserActionDescriptionTemplate.class,
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.REQUEST_BODY,
                            resourceIdParseSPEL = "#obj?.queryUserId"))
    public ResponseEntity<Object> attachUser(@ApiParam(required = true, name = "attachUserReq") @RequestBody @Valid AttachUserReq req)
    {
        String bpId = getBpId();
        String userId = getUserId();
        log.info("add user attachment. req: {}, userId: {}", req, userId);

        req.setPrincipalId(req.getQueryUserId());
        req.setPrincipalType(USER.value());
        req.setBpId(bpId);
        req.setUserId(userId);
        authzService.addAttachments(req);
        return status(CREATED).body(null);
    }

    @PostMapping(value = "/detach/user", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "detach user")
    @LNJoyAuditLog(value = "detach user",
            resourceMapperId = IamResources.USER,
            actionDescriptionTemplateClass = AuthzUserActionDescriptionTemplate.class,
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.REQUEST_BODY,
                            resourceIdParseSPEL = "#obj?.userId"))
    public ResponseEntity<Object> detachUser(@ApiParam(required = true, name = "detachUserReq") @RequestBody @Valid DetachUserReq req)
    {
        String bpId = getBpId();
        String userId = getUserId();
        log.info("delete user attachment. req: {}, userId: {}", req, userId);

        req.setPrincipalId(req.getUserId());
        req.setPrincipalType(USER.value());
        authzService.deleteAttachments(req);
        return status(OK).body(null);
    }

    @PostMapping(value = "/attach/group", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "attach group")
    @LNJoyAuditLog(value = "attach group",
            tags = ResourceOperationTypeConstants.RESOURCE_CREATE,
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.REQUEST_BODY,
                            resourceIdParseSPEL = "#obj?.groupId"))
    public ResponseEntity<Object> attachGroup(@ApiParam(required = true, name = "attachGroupReq") @RequestBody @Valid AttachGroupReq req)
    {
        String bpId = getBpId();
        String userId = getUserId();
        log.info("add group attachment. req: {}, userId: {}", req, userId);

        req.setPrincipalId(req.getGroupId());
        req.setPrincipalType(GROUP.value());
        req.setBpId(bpId);
        req.setUserId(userId);
        authzService.addAttachments(req);
        return status(CREATED).body(null);
    }

    @PostMapping(value = "/detach/group", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "detach group")
    @LNJoyAuditLog(value = "detach group",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.REQUEST_BODY,
                            resourceIdParseSPEL = "#obj?.groupId"))
    public ResponseEntity<Object> detachGroup(@ApiParam(required = true, name = "detachGroupReq") @RequestBody @Valid DetachGroupReq req)
    {
        String bpId = getBpId();
        String userId = getUserId();
        log.info("delete group attachment. req: {}, userId: {}", req, userId);

        req.setPrincipalId(req.getGroupId());
        req.setPrincipalType(GROUP.value());
        authzService.deleteAttachments(req);
        return status(OK).body(null);
    }

    @PostMapping(value = "/attach/role", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "attach role")
    @LNJoyAuditLog(value = "attach role",
            tags = ResourceOperationTypeConstants.RESOURCE_CREATE,
            resourceMapperId = IamResources.ROLE,
            actionDescriptionTemplateClass = AuthzRoleActionDescriptionTemplate.class,
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.REQUEST_BODY,
                    resourceIdParseSPEL = "#obj?.roleId", resourcePrimaryKeyClass = Long.class, convertToResourcePrimaryKeyTypeSpEl = "T(Long).valueOf(#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> attachRole(@ApiParam(required = true, name = "attachRoleReq") @RequestBody @Valid AttachRoleReq req)
    {
        String bpId = getBpId();
        String userId = getUserId();
        log.info("add role attachment. req: {}, userId: {}", req, userId);

        req.setPrincipalId(req.getRoleId());
        req.setPrincipalType(ROLE.value());
        req.setBpId(bpId);
        req.setUserId(userId);
        authzService.addAttachments(req);
        return status(CREATED).body(null);
    }

    @PostMapping(value = "/detach/role", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "detach role")
    @LNJoyAuditLog(value = "detach role",
            resourceMapperId = IamResources.ROLE,
            actionDescriptionTemplateClass = AuthzRoleActionDescriptionTemplate.class,
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.REQUEST_BODY,
                    resourceIdParseSPEL = "#obj?.roleId", resourcePrimaryKeyClass = Long.class, convertToResourcePrimaryKeyTypeSpEl = "T(Long).valueOf(#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> detachRole(@ApiParam(required = true, name = "detachRoleReq") @RequestBody @Valid DetachRoleReq req)
    {
        String bpId = getBpId();
        String userId = getUserId();
        log.info("delete role attachment. req: {}, userId: {}", req, userId);

        req.setPrincipalId(req.getRoleId());
        req.setPrincipalType(ROLE.value());
        authzService.deleteAttachments(req);
        return status(OK).body(null);
    }

    @GetMapping(value = "/policies", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get attachments list")
    public ResponseEntity<Object> getAttachmentList(@ApiParam(name = "name") @RequestParam(value = "name", required = false) String policyName,
                                                    @ApiParam(name = "policy_type") @RequestParam(value = "policy_type", required = false) String policyType,
                                                    @ApiParam(name = "page_size") @RequestParam(value = "page_size", required = false, defaultValue = Integer.MAX_VALUE + "") Integer pageSize,
                                                    @ApiParam(name = "page_num") @RequestParam(value = "page_num", required = false, defaultValue = "1") Integer pageNum,
                                                    @ApiParam(name = "bp_id") @RequestParam(value = "bp_id", required = false) String queryBpId,
                                                    @ApiParam(name = "user_id") @RequestParam(value = "user_id", required = false) String queryUserId,
                                                    @ApiParam(name = "group_id") @RequestParam(value = "group_id", required = false) String groupId,
                                                    @ApiParam(name = "role_id") @RequestParam(value = "role_id", required = false) String roleId,
                                                    @ApiParam(name = "project_id") @RequestParam(value = "project_id", required = false) String projectId)
    {

        if (!isAdmin())
        {
            queryBpId = getUserAttributes().getLeft();
        }
        AttachmentsRsp attachmentsRsp = authzService.getAttachmentList(policyName, policyType, queryBpId, queryUserId, groupId, roleId, projectId, pageSize, pageNum);

        return status(OK).body(attachmentsRsp);
    }

    @GetMapping(value = "/roles", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get role attachments list")
    public ResponseEntity<Object> getRoleAttachmentList(@ApiParam(name = "name") @RequestParam(value = "name", required = false) String roleName,
                                                    @ApiParam(name = "role_type") @RequestParam(value = "role_type", required = false) String roleType,
                                                    @ApiParam(name = "page_size") @RequestParam(value = "page_size", required = false, defaultValue = Integer.MAX_VALUE + "") Integer pageSize,
                                                    @ApiParam(name = "page_num") @RequestParam(value = "page_num", required = false, defaultValue = "1") Integer pageNum,
                                                    @ApiParam(name = "bp_id") @RequestParam(value = "bp_id", required = false) String queryBpId,
                                                    @ApiParam(name = "user_id") @RequestParam(value = "user_id", required = false) String queryUserId,
                                                    @ApiParam(name = "group_id") @RequestParam(value = "group_id", required = false) String groupId,
                                                    @ApiParam(name = "project_id") @RequestParam(value = "project_id", required = false) String projectId)
    {

        if (!isAdmin())
        {
            queryBpId = getUserAttributes().getLeft();
        }
        RoleAttachmentsRsp attachmentsRsp = authzService.getRoleAttachmentList(roleName, roleType, queryBpId, queryUserId, groupId, projectId, pageSize, pageNum);

        return status(OK).body(attachmentsRsp);
    }


    @PostMapping(value = "/check", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "check action")
    public ResponseEntity<Object> checkActions(@ApiParam(required = true, name = "checkActionsReq")CheckActionsReq checkActionsReq)
    {
        CheckActionsRsp checkActionsRsp = authzService.checkActions(checkActionsReq);
        return status(OK).body(checkActionsRsp);
    }
}
