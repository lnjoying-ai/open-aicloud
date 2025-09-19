package com.lnjoying.justice.ims.controller;

import com.lnjoying.justice.ims.domain.dto.req.AddRegistryUserReq;
import com.lnjoying.justice.ims.domain.dto.rsp.RegistryInstallInfoRsp;
import com.lnjoying.justice.ims.domain.dto.rsp.RegistryUsersRsp;
import com.lnjoying.justice.ims.facade.ImsRegistryUserFacade;
import com.lnjoying.justice.schema.constant.ResourceOperationTypeConstants;
import com.lnjoying.justice.schema.constant.UserHeadInfo;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Extension;
import io.swagger.annotations.ExtensionProperty;
import lombok.extern.slf4j.Slf4j;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.apache.servicecomb.swagger.invocation.exception.InvocationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.HashMap;
import java.util.Map;

import static com.lnjoying.justice.commonweb.common.SwaggerConstants.*;
import static com.lnjoying.justice.schema.common.ErrorCode.USER_REGISTRY_PASSWORD_HAS_NOT_BEEN_SET;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.ResponseEntity.status;

/**
 * registry user management
 *
 * @author merak
 **/

@RestSchema(schemaId = "imsRegistryUsers")
@RequestMapping(path = "/ims/v1/docker/registries/users")
@Slf4j
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
        properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "user"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "users"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "镜像用户"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "TblImsRegistryUser")})})
public class ImsRegistryUserController extends ImsWebController
{
    @Autowired
    private ImsRegistryUserFacade imsRegistryUserFacade;

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "add registry user",
            tags = ResourceOperationTypeConstants.RESOURCE_CREATE)
    public ResponseEntity<RegistryInstallInfoRsp> addRegistryUser(@ApiParam(required = true, name = "registryReq") @RequestBody @Valid AddRegistryUserReq registryUserReq,
                                                                  @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                                                  @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                                                  @RequestHeader(name = UserHeadInfo.USERNAME, required = false) String userName,
                                                                  @RequestHeader(name = UserHeadInfo.BpName, required = false) String bpName,
                                                                  @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {
        
        log.info("add registry user. req: {}, userId: {}", registryUserReq, userId);

        setBaseReq(registryUserReq, userId, bpId, userName, bpName);

        if (imsRegistryUserFacade.exist(userId))
        {
            imsRegistryUserFacade.updateRegistryUser(registryUserReq);
            return status(OK).body(null);
        }
        imsRegistryUserFacade.addRegistryUser(registryUserReq);
        return status(CREATED).body(null);
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get registry user list")
    public ResponseEntity<RegistryUsersRsp> getRegistryList(@ApiParam(name = "registry_user_name") @RequestParam(value = "registry_user_name", required = false) String registryUserName,
                                                            @ApiParam(name = "page_size") @RequestParam(value = "page_size", required = false, defaultValue = Integer.MAX_VALUE + "") Integer pageSize,
                                                            @ApiParam(name = "page_num") @RequestParam(value = "page_num", required = false, defaultValue = "1") Integer pageNum,
                                                            @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                                            @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                                            @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {
        
        RegistryUsersRsp registryUserList = RegistryUsersRsp.builder().build();
        if (isRoleImsTenant(authorities))
        {
            registryUserList = imsRegistryUserFacade.getRegistryUserList(registryUserName, bpId, userId, pageSize, pageNum);
        }
        else if (isRoleImsTenantAdmin(authorities))
        {
            registryUserList = imsRegistryUserFacade.getRegistryUserList(registryUserName, bpId, null, pageSize, pageNum);
        }
        else if (isRoleImsAdmin(authorities))
        {
            registryUserList = imsRegistryUserFacade.getRegistryUserList(registryUserName, null, null, pageSize, pageNum);
        }
        return status(OK).body(registryUserList);
    }

    @GetMapping(value = "/exist", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Check whether the harbor user exists")
    public ResponseEntity<Object> exist(@RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                                      @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {
        
        boolean exist = imsRegistryUserFacade.exist(userId);
        if (exist) {
            return status(OK).body(null);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("code" , USER_REGISTRY_PASSWORD_HAS_NOT_BEEN_SET.getCode());
        result.put("message", USER_REGISTRY_PASSWORD_HAS_NOT_BEEN_SET.getMessage());
        return status(NOT_FOUND).body(new InvocationException(javax.ws.rs.core.Response.Status.NOT_FOUND, result));
    }
}
