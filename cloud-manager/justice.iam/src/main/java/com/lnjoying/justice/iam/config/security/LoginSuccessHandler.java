package com.lnjoying.justice.iam.config.security;

import com.lnjoying.justice.commonweb.swagger.ScbSchemaUtils;
import com.lnjoying.justice.commonweb.util.JwtUtils;
import com.lnjoying.justice.commonweb.util.RequestUtils;
import com.lnjoying.justice.iam.common.constant.ActionTypeEnum;
import com.lnjoying.justice.iam.service.CombRpcSerice;
import com.lnjoying.justice.schema.constant.UserHeadInfo;
import com.lnjoying.justice.schema.constant.UserKindEnum;
import com.lnjoying.justice.schema.constant.WebConstants;
import com.lnjoying.justice.iam.db.model.TblBpInfo;
import com.lnjoying.justice.iam.db.model.TblUserInfo;
import com.lnjoying.justice.iam.db.repo.BpRepository;
import com.lnjoying.justice.iam.db.repo.UserRepository;
import com.lnjoying.justice.schema.service.omc.OmcService;
import com.micro.core.common.Utils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.logging.LogLevel;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginSuccessHandler.class);

    @Autowired
    private MecUserDetailsService mecUserDetailsService;

    @Value("${jwtkey}")
    private String jwtkey;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BpRepository bpRepository;


    @Autowired
    private CombRpcSerice combRpcService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException, ServletException
    {
        String userName = authentication.getName();
        TblUserInfo tblUserInfo = userRepository.getUserByUserName(userName);

        TblBpInfo tblBpInfo = null;
        if ( !StringUtils.isEmpty(tblUserInfo.getBpId()))
        {
            tblBpInfo = bpRepository.getBpInfo(tblUserInfo.getBpId());
        }

        mecUserDetailsService.clearFailedCount(userName);
        User user= (User) authentication.getPrincipal();
        buildHttpRsp(user, request, response, tblUserInfo, tblBpInfo);
        writeLogToOMC(request, tblUserInfo, tblBpInfo, LogLevel.INFO, "ok");
    }

    void buildHttpRsp(User user, HttpServletRequest request, HttpServletResponse response, TblUserInfo tblUserInfo, TblBpInfo tblBpInfo)
    {
        Object authorities = user.getAuthorities();

        Map<String, Object> headerInfo = new HashMap<>();
        headerInfo.put("user-agent", request.getHeader("user-agent"));
//        headerInfo.put("remote",  request.getRemoteHost());
        headerInfo.put(UserHeadInfo.USERNAME, user.getUsername());
        headerInfo.put(UserHeadInfo.USERID, tblUserInfo.getUserId());
        headerInfo.put(UserHeadInfo.AUTIORITIES, authorities);
        headerInfo.put(UserHeadInfo.USERKIND, UserKindEnum.fromCode(tblUserInfo.getKind()).getMessage());

        String bpId = (tblBpInfo != null)   ? tblBpInfo.getBpId() : "";
        String bpName = (tblBpInfo != null) ? tblBpInfo.getBpName() : "";
        headerInfo.put(UserHeadInfo.BPID, bpId);
        headerInfo.put(UserHeadInfo.BpName, bpName);

        String token = JwtUtils.getNewJwtToken(user.getUsername(),
                headerInfo,
                WebConstants.LNJOYING_TOKEN_INDATE,
                jwtkey);
        response.addHeader("X-Access-Token", JwtUtils.getAuthorizationHeaderPrefix()+token);
        response.addCookie(JwtUtils.getNewCookie(WebConstants.ACCESS_TOKEN_NAME, token, WebConstants.LNJOYING_TOKEN_INDATE, "/"));
    }

    private void writeLogToOMC(HttpServletRequest request,TblUserInfo tblUserInfo, TblBpInfo tblBpInfo, LogLevel logLevel, String message)
    {
        try
        {
            String bpId = (tblBpInfo != null)   ? tblBpInfo.getBpId() : null;
            String bpName = (tblBpInfo != null) ? tblBpInfo.getBpName() : null;
            OmcService.OperationLog.OperationLogBuilder logBuilder = OmcService.OperationLog.builder()
                    .id(Utils.assignUUId())
                    .triggerTime(new Date())
                    .level(logLevel.name())
                    .bpId(bpId)
                    .service(ScbSchemaUtils.getMicrosoftServiceName())
                    .action(ActionTypeEnum.LOGIN.getActionEn())
                    .accessIp(RequestUtils.getIpAddr(request))
                    .userAgent(request.getHeader(UserHeadInfo.USER_AGENT))
                    .httpMethod(request.getMethod())
                    .requestPath(org.springframework.util.StringUtils.isEmpty(request.getQueryString()) ? request.getRequestURI()
                            : request.getRequestURI() + "?" + request.getQueryString())
                    .userId(tblUserInfo.getUserId())
                    .operator(tblUserInfo.getUserName())
                    .resource(ActionTypeEnum.LOGIN.getResource())
                    .operationType(ActionTypeEnum.LOGIN.getActionEn())
                    .result(message)
                    .description("login success");
            OmcService.OperationLog operationLog = logBuilder.build();
            combRpcService.getOmcService().addLog(operationLog);
        }
        catch (Throwable e)
        {
            LOGGER.error("login log failed: {}", e.getMessage());
        }
    }
}