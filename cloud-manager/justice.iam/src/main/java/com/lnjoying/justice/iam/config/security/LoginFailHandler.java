package com.lnjoying.justice.iam.config.security;

import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.commonweb.swagger.ScbSchemaUtils;
import com.lnjoying.justice.commonweb.util.RequestUtils;
import com.lnjoying.justice.iam.common.constant.ActionTypeEnum;
import com.lnjoying.justice.iam.db.model.TblIamAction;
import com.lnjoying.justice.iam.service.CombRpcSerice;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.iam.exception.InvalidCodeException;
import com.lnjoying.justice.schema.constant.UserHeadInfo;
import com.lnjoying.justice.schema.service.omc.OmcService;
import com.micro.core.common.Utils;
import io.jsonwebtoken.impl.JwtMap;
import io.vertx.core.json.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.logging.LogLevel;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

@Component("loginFailHandler")
public class LoginFailHandler extends SimpleUrlAuthenticationFailureHandler {

    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    private CombRpcSerice combRpcService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException
    {
        LOGGER.info("error auth. {}", exception.getMessage());

        JsonObject jsonObject = new JsonObject();
        ErrorCode errorCode = ErrorCode.InvalidAuthority;
        if (exception.getMessage().equals("user have been disabled"))
        {
            errorCode = ErrorCode.USER_NOT_ACTIVE;
        }

        if (exception instanceof UsernameNotFoundException)
        {
            errorCode = ErrorCode.User_Not_Exist;
        }
        else if (exception instanceof InternalAuthenticationServiceException)
        {
            Throwable cause = exception.getCause();
            if (cause instanceof DisabledException)
            {
                errorCode = ErrorCode.USER_IS_DISABLED;
            }
            else if (cause instanceof InvalidCodeException)
            {
                errorCode = ErrorCode.Invalid_validateCode;
            }
        }
        else if (exception instanceof LockedException)
        {
            errorCode = ErrorCode.USER_IS_LOCKED;
        }
        else if (exception instanceof BadCredentialsException)
        {
            errorCode = ErrorCode.INVALID_USERNAME_OR_PASSWORD;
        }

        jsonObject.put("code", errorCode.getCode());
        WebSystemException.setResponseStatus(errorCode, response);
        jsonObject.put("message", ErrorCode.fromCode(errorCode.getCode()).getMessage());
        String userName = (String)request.getAttribute(UserHeadInfo.USERNAME);
        writeLogToOMC(request, LogLevel.ERROR, errorCode.getMessage() + ", user:" + userName);
        PrintWriter out = response.getWriter();
        out.print(jsonObject.toString());
    }

    private void writeLogToOMC(HttpServletRequest request, LogLevel logLevel, String message)
    {
        try
        {
            OmcService.OperationLog.OperationLogBuilder logBuilder = OmcService.OperationLog.builder()
                    .id(Utils.assignUUId())
                    .triggerTime(new Date())
                    .level(logLevel.name())
                    .bpId(null)
                    .service(ScbSchemaUtils.getMicrosoftServiceName())
                    .action(ActionTypeEnum.LOGIN.getActionEn())
                    .accessIp(RequestUtils.getIpAddr(request))
                    .userAgent(request.getHeader(UserHeadInfo.USER_AGENT))
                    .httpMethod(request.getMethod())
                    .requestPath(org.springframework.util.StringUtils.isEmpty(request.getQueryString()) ? request.getRequestURI()
                            : request.getRequestURI() + "?" + request.getQueryString())
                    .userId(null)
                    .operator(null)
                    .resource(ActionTypeEnum.LOGIN.getResource())
                    .operationType(ActionTypeEnum.LOGIN.getActionEn())
                    .result(message)
                    .description("login fail");
            OmcService.OperationLog operationLog = logBuilder.build();
            combRpcService.getOmcService().addLog(operationLog);
        }
        catch (Throwable e)
        {
            LOGGER.error("login log failed: {}", e.getMessage());
        }
    }
}