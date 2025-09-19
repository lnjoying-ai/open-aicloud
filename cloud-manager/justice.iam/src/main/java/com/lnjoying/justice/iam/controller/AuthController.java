package com.lnjoying.justice.iam.controller;

import com.lnjoying.justice.commonweb.controller.RestWebController;
import com.lnjoying.justice.commonweb.util.HttpContextUtils;
import com.lnjoying.justice.commonweb.util.JwtUtils;
import com.lnjoying.justice.iam.service.WxManagerService;
import com.lnjoying.justice.schema.common.RedisCacheField;
import com.lnjoying.justice.schema.constant.WebConstants;
import com.lnjoying.justice.iam.config.DescriptionConfig;
import com.lnjoying.justice.iam.domain.dto.request.wx.CodeReq;
import com.lnjoying.justice.iam.domain.dto.response.user.UserRsp;
import com.lnjoying.justice.iam.domain.model.UserContactInfo;
import com.lnjoying.justice.iam.service.WxManagerService;
import com.micro.core.common.Pair;
import com.micro.core.common.Utils;
import com.micro.core.persistence.redis.RedisUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

import static com.lnjoying.justice.iam.common.constant.WeixinConsts.*;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.ResponseEntity.status;

@RestSchema(schemaId = "auth")
@RequestMapping("/api/iam/v1/auth")
@Controller
@Api(value = "Auth Controller",tags = {"Auth Controller"})
@Slf4j
public class AuthController extends RestWebController
{
    @Autowired
    private WxManagerService wxManagerService;

    @DeleteMapping(value = "/tokens", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "logout and delete token",notes = DescriptionConfig.DELETE_TOKEN_MSG)
    void deleteToken(HttpServletResponse servletResponse)
    {
        Integer leftTime = HttpContextUtils.getIntAttribute("leftTime");
        String accessToken = HttpContextUtils.getStrAttribute("accessToken");
        if (leftTime != null && leftTime > 0 && accessToken != null)
        {
            String accessHash = Utils.getSHA(accessToken);
            RedisUtil.set(RedisCacheField.ACCESS_TOKEN_EXPIRE, accessHash, "dropped",leftTime);
        }

        servletResponse.addCookie(JwtUtils.getLogoutCookie(WebConstants.ACCESS_TOKEN_NAME, "logout", WebConstants.LNJOYING_TOKEN_INDATE, "/"));
    }

    @PostMapping(value = "/wx/tokens", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get token")
    @ResponseBody
    public ResponseEntity<Object> getWxToken(@ApiParam(required = true, name = "codeReq") @RequestBody CodeReq codeReq)
    {
        try
        {
            log.info("wx login code: {}", codeReq);

            ResponseEntity.BodyBuilder responseBuilder = status(CREATED.value());
            Pair<WxManagerService.WeixinUserStatus, Pair<String, UserRsp>> tokenInfo = wxManagerService.getTokenByCode(codeReq.getCode());
            Pair<String, UserRsp> userInfo = tokenInfo.getRight();
            responseBuilder.header(X_ACCESS_TOKEN, userInfo.getLeft());
            if (WxManagerService.WeixinUserStatus.NEW.equals(tokenInfo.getLeft()))
            {
                responseBuilder.header(REQUIRE_WX_USER_INFO, "true");
                responseBuilder.header(REQUIRE_WX_USER_PHONE, "true");
                responseBuilder.header(UPDATE_WX_USER_INFO, "false");
            }
            else
            {
                responseBuilder.header(REQUIRE_WX_USER_INFO, "false");

                responseBuilder.header(REQUIRE_WX_USER_PHONE,  Boolean.toString(isPhoneExist(userInfo)));
                responseBuilder.header(UPDATE_WX_USER_INFO, "true");
            }

            return  responseBuilder.body(userInfo.getRight());
        }
        catch (Exception e)
        {
            log.error("wx login failed:{}", e);
            throw throwWebException(e);
        }

    }

    private static boolean isPhoneExist(Pair<String, UserRsp> userInfo)
    {
        UserContactInfo contactInfo = userInfo.getRight().getContactInfo();
        if (Objects.nonNull(contactInfo))
        {
            String phone = contactInfo.getPhone();
            if (StringUtils.isNotBlank(phone))
            {
                return false;
            }
        }

        return true;
    }

}
