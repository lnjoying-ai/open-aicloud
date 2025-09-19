package com.lnjoying.justice.iam.controller;

import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import com.lnjoying.justice.commonweb.controller.RestWebController;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.LNJoyAuditLog;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.ResourceIdExtractConfiguration;
import com.lnjoying.justice.commonweb.handler.aspect.enums.ResourceIdLocationType;
import com.lnjoying.justice.iam.common.constant.IamResources;
import com.lnjoying.justice.iam.domain.dto.request.user.*;
import com.lnjoying.justice.iam.handler.actiondescription.i18n.zh_cn.UserActionDescriptionTemplate;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.common.ErrorLevel;
import com.lnjoying.justice.schema.common.RedisCacheField;
import com.lnjoying.justice.schema.constant.ResourceOperationTypeConstants;
import com.lnjoying.justice.schema.constant.UserKindEnum;
import com.lnjoying.justice.iam.config.DescriptionConfig;
import com.lnjoying.justice.iam.db.model.TblBpInfo;
import com.lnjoying.justice.iam.domain.dto.request.wx.UserInfoReq;
import com.lnjoying.justice.iam.domain.dto.response.user.UserRsp;
import com.lnjoying.justice.iam.domain.model.UserContactInfo;
import com.lnjoying.justice.iam.domain.model.search.UserSearchCritical;
import com.lnjoying.justice.iam.service.BpManagerService;
import com.lnjoying.justice.iam.service.UserManagerService;
import com.lnjoying.justice.iam.utils.ServiceCombRequestUtils;
import com.lnjoying.justice.iam.utils.ValidatorUtil;
import io.swagger.annotations.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.lnjoying.justice.commonweb.common.SwaggerConstants.*;
import static com.lnjoying.justice.schema.common.ErrorCode.*;
import static com.lnjoying.justice.iam.utils.ServiceCombRequestUtils.*;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.ResponseEntity.status;

@RestSchema(schemaId = "users-manager")
@RequestMapping("/api/iam/v1/users")
@Controller
@Api(value = "User Controller",tags = {"User Controller"})
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
        properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "user"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "users"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "用户"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "tbl_user_info")})})
public class UserController extends RestWebController
{
    private static Logger LOGGER = LogManager.getLogger();
    private static final String REG_UUID = "[0-9a-f]{32}";

    @Autowired
    private UserManagerService userManagerService;

    @Autowired
    private BpManagerService bpManagerService;

    @PostMapping(value = "/registration", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "registration", response = Object.class, notes = DescriptionConfig.REGISTER_MSG)
    @LNJoyAuditLog(value = "registration",
            actionDescriptionTemplate = UserActionDescriptionTemplate.Descriptions.REGISTER,
            isResourceInstanceNameFromArgs = true,
            resourceInstanceNameFromArgsParseSpEl = "#obj?.get('name')?.toString()",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.METHOD_RETURN_VALUE,
                            resourceIdParseSPEL = "#obj?.get('id')?.toString()"))
    @ResponseBody    @ResponseStatus(HttpStatus.CREATED)
    public Object register(
        @ApiParam(value = "UserRegReq", required = true, name = "UserRegReq") @RequestBody UserRegReq request)
    {
        try
        {
            LOGGER.info("reg user info: {}", request);
            UserContactInfo contactInfo = request.getContact_info();
            if (Objects.isNull(contactInfo) || StringUtils.isBlank(contactInfo.getPhone())) {
                throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.ERROR);
            }
            ValidatorUtil.checkValidateCode(request.getVerification_code(), RedisCacheField.REG_VER_CODE, contactInfo.getPhone());
            return userManagerService.register(request);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ALL_ADMIN')")
    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "admin add new user",
            tags = ResourceOperationTypeConstants.RESOURCE_CREATE,
            response = Object.class, notes = DescriptionConfig.ADMIN_ADD_USER_MSG)
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    @LNJoyAuditLog(value = "admin add new user",
            tags = ResourceOperationTypeConstants.RESOURCE_CREATE,
            actionDescriptionTemplate = UserActionDescriptionTemplate.Descriptions.ADD_USER,
            isResourceInstanceNameFromArgs = true,
            resourceInstanceNameFromArgsParseSpEl = "#obj?.get('name')?.toString()",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.METHOD_RETURN_VALUE,
                    resourceIdParseSPEL = "#obj?.body?.get('id')?.toString()"))
    public ResponseEntity<Object> addNewUser(
            @ApiParam(value = "UserAddReq", required = true, name = "addUserRegReq")@Valid @RequestBody UserAddReq request)
    {
        try
        {
            LOGGER.info("add user info: {}", request);
            //checkRoleTenantAdminOrAdmin();
            return status(CREATED).body(userManagerService.addUser(request));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw throwWebException(e);
        }
    }


    @PatchMapping(value = "/retrieved-password", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "retrieved password", response = Object.class, notes = DescriptionConfig.RETRIEVE_PASSWORD_MSG)
    @ResponseBody    @ResponseStatus(OK)
    @LNJoyAuditLog(value = "retrieved password",
            actionDescriptionTemplate = UserActionDescriptionTemplate.Descriptions.RETRIEVE_PASSWORD,
            response = Object.class, notes = DescriptionConfig.RETRIEVE_PASSWORD_MSG)
    public void retrievePassword(
        @ApiParam(value = "RetrievePasswordReq", required = true, name = "RetrievePasswordReq") @RequestBody RetrievePasswordReq request)
    {
        try
        {
            userManagerService.retrievePassword(request);
        }
        catch (Exception e)
        {
            throw throwWebException(e);
        }
    }

    @PatchMapping(value = "/password", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "update password", response = Object.class, notes = DescriptionConfig.UPDATE_PASSWORD_MSG)
    @ResponseBody    @ResponseStatus(OK)
    @LNJoyAuditLog(value = "update password",
            actionDescriptionTemplate = UserActionDescriptionTemplate.Descriptions.UPDATE_PASSWORD,
            notes = DescriptionConfig.UPDATE_PASSWORD_MSG)
    public void updatePassword(
            @ApiParam(value = "update password", required = true, name = "updatepassword") @RequestBody UpdatePasswordReq request)
    {
        try
        {
            userManagerService.updatePassword(getUserName(), request);
        }
        catch (Exception e)
        {
            throw throwWebException(e);
        }
    }

    @PutMapping(value = "/uniqueness", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "check uniqueness", response = Object.class, notes = DescriptionConfig.UNIQUENESS_MSG)
    @ResponseBody    @ResponseStatus(OK)
    public Object uniqueness(
        @ApiParam(value = "uniquenessRequest", required = true, name = "uniqueness")  @RequestBody UniqueReq request)
    {
        try
        {
            return userManagerService.uniqueness(request);
        }
        catch (Exception e)
        {
            throw throwWebException(e);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ALL_ADMIN')")
    @DeleteMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "delete user by userId", response = Object.class, notes = DescriptionConfig.DELETE_USER_MSG)
    @LNJoyAuditLog(value = "delete user by userId",
            resourceMapperId = IamResources.USER,
            actionDescriptionTemplate = UserActionDescriptionTemplate.Descriptions.DELETE_USER,
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "userId"))
    @ResponseBody    @ResponseStatus(OK)
    public void deleteUser(
        @ApiParam(value = "user id") @PathVariable("userId") String userId)
    {
        try
        {
            checkRoleTenantAdminOrAdmin();
            if (!isAdmin())
            {
                if (getUserId().equals(userId))
                {
                    throw new WebSystemException(DELETING_SELF_IS_NOT_SUPPORTED, ErrorLevel.ERROR);
                }
            }
            userManagerService.deleteUser(userId);
        }
        catch (Exception e)
        {
            throw throwWebException(e);
        }
    }

    @PutMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "update user by userId", response = Object.class, notes = DescriptionConfig.UPDATE_USER_BY_ID_MSG)
    @LNJoyAuditLog(value = "update user by userId",
            actionDescriptionTemplate = UserActionDescriptionTemplate.Descriptions.UPDATE_USER,
            isResourceInstanceNameFromArgs = true,
            resourceInstanceNameFromArgsParseSpEl = "#obj?.get('name')?.toString()",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "userId"))
    @ResponseBody    @ResponseStatus(OK)
    public void updateUser(
            @ApiParam(value = "user id") @PathVariable("userId")String userId,
            @ApiParam(value = "userInfo", required = true, name = "userInfo") @RequestBody UserUpdateReq request)
    {
        try
        {
            ServiceCombRequestUtils.checkRoleTenantAdminOrAdmin();

            LOGGER.info("update user id: {} user info: {}", userId, request);
            String bpId = request.getBp_id();
            boolean bpUserNoBpId = Objects.nonNull(request.getKind())
                    && (request.getKind() == UserKindEnum.BP_ADMIN.getCode() || request.getKind() == UserKindEnum.BP_SUB_USER.getCode())
                    && StringUtils.isBlank(bpId);
            if (bpUserNoBpId)
            {
                throw new WebSystemException(BP_USER_NEED_BP_ID, ErrorLevel.ERROR);
            }

            if (StringUtils.isNotBlank(bpId))
            {
                // Verify the bpid is correct
                TblBpInfo bpInfo = bpManagerService.getBpInfo(bpId);
                if (Objects.isNull(bpInfo))
                {
                    throw new WebSystemException(BP_NOT_EXIST, ErrorLevel.INFO);
                }
            }
            userManagerService.updateUser(userId, request);
        }
        catch (WebSystemException e)
        {
            throw new WebSystemException(e.getCode(), ErrorLevel.INFO, e.getMessage());
        }
        catch (Exception e)
        {
            throw throwWebException(e);
        }
    }

    @PutMapping(value = "/current", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "update current user", response = Object.class, notes = DescriptionConfig.UPDATE_CURRENT_USER_MSG)
    @ResponseBody    @ResponseStatus(OK)
    @LNJoyAuditLog(value = "update current user",
            actionDescriptionTemplate = UserActionDescriptionTemplate.Descriptions.UPDATE_CURRENT_USER,
            notes = DescriptionConfig.UPDATE_CURRENT_USER_MSG)
    public void updateCurrentUser(@ApiParam(value = "userInfo", required = true, name = "userInfo") @RequestBody CurrentUserUpdateReq request)
    {
        try
        {
            String userId = ServiceCombRequestUtils.getUserId();
            LOGGER.info("update user id: {} user info: {}", userId, request);
            userManagerService.updateCurrentUser(userId, request);
        }
        catch (Exception e)
        {
            throw throwWebException(e);
        }
    }


    /**
     * get current login user.
     */
    @GetMapping(value = "/current", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get current login user.", response = UserRsp.class, notes = DescriptionConfig.CURRENT_USER_MSG)
    @ResponseBody     @ResponseStatus(OK)
    public UserRsp  getCurrentUserInfo()
    {
        try
        {
            return userManagerService.getUserDtoInfo(ServiceCombRequestUtils.getUserId());
        }
        catch (Exception e)
        {
            throw throwWebException(e);
        }
    }

    /**
     * get user list
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get user list.", response = Object.class, notes = DescriptionConfig.USER_LIST_MSG)
    @ResponseBody     @ResponseStatus(OK)
    public Object getUserInfos(@ApiParam(value = "", required = false, name = "name") @RequestParam(required = false) String name,
                               @ApiParam(value = "", required = false, name = "bp_id") @RequestParam(value = "queryBpId", required = false) String queryBpId,
                               @ApiParam(name = "page_size") @RequestParam(required = false) Integer page_size,
                               @ApiParam(name = "page_num") @RequestParam(required = false) Integer page_num)
    {
        try
        {

            UserSearchCritical pageSearchCritical = new UserSearchCritical();
            Pair<String, String> userAttributes = getUserAttributes();

            if (ValidatorUtil.validOperationSupporter(getUserId()))
            {
                pageSearchCritical.setBpId(null);
                pageSearchCritical.setUserId(null);
                pageSearchCritical.setQueryBpId(queryBpId);
            }
            else
            {
                pageSearchCritical.setBpId(userAttributes.getLeft());
                pageSearchCritical.setUserId(userAttributes.getRight());
                pageSearchCritical.setQueryBpId(queryBpId);
            }

            pageSearchCritical.setName(name);

            if (page_num != null) pageSearchCritical.setPageNum(page_num);
            if (page_size != null) pageSearchCritical.setPageSize(page_size);
            return userManagerService.getUserDtoInfos(pageSearchCritical);
        }
        catch (Exception e)
        {
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/{user_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get user info.", response = Object.class, notes = DescriptionConfig.USER_LIST_MSG)
    @LNJoyAuditLog(value = "get user info.",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "userId"))
    @ResponseBody     @ResponseStatus(OK)
    public Object getUserInfo(@ApiParam(required = true, name = "user_id") @PathVariable String userId)
    {
        try
        {
            return userManagerService.getUserDtoInfo(userId);
        }
        catch (Exception e)
        {
            throw throwWebException(e);
        }
    }


    @PatchMapping(value = "/phone", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "patch current user phone.", response = Object.class, notes = DescriptionConfig.UPDATE_USER_PHONE_MSG)
    @ResponseBody     @ResponseStatus(OK)
    @LNJoyAuditLog(value = "patch current user phone.",
            actionDescriptionTemplate = UserActionDescriptionTemplate.Descriptions.UPDATE_PHONE,
            notes = DescriptionConfig.UPDATE_USER_PHONE_MSG)
    public void updateUserphone(@ApiParam(value = "", required = false, name = "phone") @RequestBody PhoneRawInfo phoneRawInfo)
    {
        try
        {
             userManagerService.updateUserPhone(ServiceCombRequestUtils.getUserId(), phoneRawInfo);
        }
        catch (Exception e)
        {
            throw throwWebException(e);
        }
    }

    @PatchMapping(value = "/email", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "patch current user email.", response = Object.class, notes = DescriptionConfig.UPDATE_USER_EMAIL_MSG)
    @ResponseBody     @ResponseStatus(OK)
    @LNJoyAuditLog(value = "patch current user email.",
            actionDescriptionTemplate = UserActionDescriptionTemplate.Descriptions.UPDATE_EMAIL,
            notes = DescriptionConfig.UPDATE_USER_EMAIL_MSG)
    public void updateUserEmail(@ApiParam(value = "", required = false, name = "email") @RequestBody EmailRawInfo emailRawInfo)
    {
        try
        {
            userManagerService.updateUserEmail(ServiceCombRequestUtils.getUserId(), emailRawInfo);
        }
        catch (Exception e)
        {
            throw throwWebException(e);
        }
    }

    @PostMapping(value = "/weixin/phone", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "update phone via wechat", response = Object.class)
    @ResponseBody
    @ResponseStatus(OK)
    @LNJoyAuditLog(value = "update phone via wechat",
            actionDescriptionTemplate = UserActionDescriptionTemplate.Descriptions.UPDATE_PHONE_VIA_WECHAT,
            response = Object.class)
    public ResponseEntity<Object> updateUserPhoneViaWeixin(
            @ApiParam(value = "", required = true, name = "code")@Valid @RequestParam String code)
    {
        try
        {
            LOGGER.info("update phone via wechat by code: {}", code);
            //checkRoleTenantAdminOrAdmin();
           com.micro.core.common.Pair<String, Boolean> pair = userManagerService.updateUserPhoneViaWeixin(code);
            Map<String, Boolean> res = new HashMap<>();
            if (Objects.nonNull(pair))
            {
                res.put(pair.getKey(), pair.getValue());
                return status(OK).body(res);
            }

            return status(OK).body(null);
        }
        catch (Exception e)
        {
            LOGGER.error("update phone via wechat failed:{}", e);
            throw throwWebException(e);
        }
    }

    @PostMapping(value = "/weixin/user-info", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "update userinfo via wechat", response = Object.class)
    @ResponseBody
    @ResponseStatus(OK)
    @LNJoyAuditLog(value = "update userinfo via wechat",
            actionDescriptionTemplate = UserActionDescriptionTemplate.Descriptions.UPDATE_INFO_VIA_WECHAT,
            response = Object.class)
    public ResponseEntity<Object> updateUserInfoViaWeixin(
            @ApiParam(required = true, name = "userInfoReq") @RequestBody UserInfoReq userInfoReq)
    {
        try
        {
            LOGGER.info("update info via wechat");
            //checkRoleTenantAdminOrAdmin();
            WxMaUserInfo wxMaUserInfo = userManagerService.updateUserInfoViaWeixin(userInfoReq);
            Map<String, String> res = new HashMap<>();

            if (Objects.nonNull(wxMaUserInfo))
            {
                res.put("nick_name", wxMaUserInfo.getNickName());
                res.put("avatar_url", wxMaUserInfo.getAvatarUrl());
            }
            return status(OK).body(res);
        }
        catch (Exception e)
        {
            LOGGER.error("update user info via wechat failed:{}", e);
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/invitation-link", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get invitation link")
    public ResponseEntity<Object> getInvitationLink()
    {
        Map<String, Object> refMap = userManagerService.getInvitationLink();
        return status(OK).body(refMap);
    }

    @GetMapping(value = "/invite", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "invite user")
    public ResponseEntity<Object> invite(@ApiParam(value = "", required = true, name = "ref") @RequestParam(required = false) String ref)
    {
        return userManagerService.invite(ref);
    }

    @GetMapping(value = "/invitation-code", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get invitation code")
    public ResponseEntity<Object> getInvitationCode()
    {
        Map<String, Object> refMap = userManagerService.getInvitationCode();
        return status(OK).body(refMap);
    }
}
