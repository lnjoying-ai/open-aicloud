package com.lnjoying.justice.iam.config.filter;

import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.commonweb.util.JwtUtils;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.common.RedisCacheField;
import com.lnjoying.justice.schema.constant.UserHeadInfo;
import com.lnjoying.justice.schema.constant.WebConstants;
import com.lnjoying.justice.schema.service.iam.AuthzService;
import com.micro.core.common.JwtTools;
import com.micro.core.common.Utils;
import com.micro.core.persistence.redis.RedisUtil;
import io.jsonwebtoken.impl.JwtMap;
import io.vertx.core.json.JsonObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class JwtAuthenticationFilter extends BasicAuthenticationFilter
{
    private String jwtkey;

    private AuthzService authzService;

    private static Logger LOGGER = LogManager.getLogger();

    private static final String[] urlPatterns = {"/iam/v1/health", "/logout", "/api/iam/v1/verification", "/AuthImpl", "/cse", "/login", "/error", "/exception", "/inspector/",
            "/UmsServiceImpl", "/AuthzServiceImpl",
            "/api/iam/v1/users/registration", "/api/iam/v1/users/retrieved-password", "/api/iam/v1/auth/wx/tokens",
            "/CommonResourceFeederServiceImpl", "/api/iam/v1/users/invite"};

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, String jwtkey, AuthzService authzService)
    {
        super(authenticationManager);
        this.jwtkey = jwtkey;
        this.authzService = authzService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException
    {
        String url = request.getRequestURI();

        if (url.equalsIgnoreCase("/api/iam/v1/users/registration"))
        {
            Cookie[] cookies = request.getCookies();
            if (cookies != null)
            {
                String vrfc = null;
                for (Cookie k : request.getCookies())
                {
                    if (k.getName().equals(WebConstants.LNJOYING_VRFC)  && k.getValue().startsWith(JwtUtils.getAuthorizationHeaderPrefix()))
                    {
                        vrfc = k.getValue();
                        break;
                    }
                }

                if (null != vrfc)
                {
                    request.setAttribute(WebConstants.LNJOYING_VRFC,  vrfc);
                    RequestContextHolder.setRequestAttributes(RequestContextHolder.getRequestAttributes(), true);
                }
            }

            chain.doFilter(request, response);
            return;
        }

        if (StringUtils.startsWithAny(url, urlPatterns))
        {
            chain.doFilter(request, response);
            return;
        }

        String accessToken = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null)
        {
            for (Cookie k:cookies)
            {
                if (k.getName().equals(WebConstants.ACCESS_TOKEN_NAME)  && k.getValue().startsWith(JwtUtils.getAuthorizationHeaderPrefix()))
                {
                    accessToken = k.getValue();
                    break;
                }
            }
        }

        if (accessToken == null)
        {
            accessToken = request.getHeader(WebConstants.HEADER_ACCESS_TOKEN_NAME);
        }

        if (accessToken == null)
        {
            sendErrorResponse(ErrorCode.InvalidAuthority, response);
            return;
        }

        JwtMap jwtInfo = getJwtInfo(accessToken);
        if (null == jwtInfo)
        {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Access token is empty");
            return;
        }

        if (false == checkJwt(jwtInfo, request, response))
        {
            LOGGER.info("jwt check failed.uri: {}", request.getRequestURI());
            sendErrorResponse(ErrorCode.InvalidAuthority, response);
            return;
        }

        String accessHash = Utils.getSHA(accessToken);
        if (RedisUtil.get(RedisCacheField.ACCESS_TOKEN_EXPIRE, accessHash) != null)
        {
            LOGGER.info("jwt check have been dropped for {}", jwtInfo.get(UserHeadInfo.USERNAME));
            sendErrorResponse(ErrorCode.InvalidAuthority, response);
            return;
        }

        List<Map<String,String>> role = (List<Map<String,String>>)jwtInfo.get(UserHeadInfo.AUTIORITIES);
        UsernamePasswordAuthenticationToken authenticationToken = getUsernamePasswordAuthenticationToken((String)jwtInfo.get(UserHeadInfo.USERNAME), (String)jwtInfo.get(UserHeadInfo.USERID), role);

        if (request.getAttribute(UserHeadInfo.USERNAME) != null)
        {
            sendErrorResponse(ErrorCode.InvalidAuthority, response);
            return;
        }

        request.setAttribute(UserHeadInfo.USERID,  jwtInfo.get(UserHeadInfo.USERID));
        request.setAttribute(UserHeadInfo.USERNAME, jwtInfo.get(UserHeadInfo.USERNAME));
        request.setAttribute(UserHeadInfo.BPID, jwtInfo.get(UserHeadInfo.BPID));
        request.setAttribute(UserHeadInfo.BpName, jwtInfo.get(UserHeadInfo.BpName));
        request.setAttribute(UserHeadInfo.AUTIORITIES, role.toString());
        request.setAttribute(UserHeadInfo.USERKIND, jwtInfo.get(UserHeadInfo.USERKIND));
        if (request.getMethod().equals(HttpMethod.DELETE.name()) && request.getRequestURI().endsWith("/auth/tokens"))
        {
            request.setAttribute("accessToken", accessToken);
        }

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        RequestContextHolder.setRequestAttributes(RequestContextHolder.getRequestAttributes(), true);

        if (iamCheck(request, response, jwtInfo)) return;


        chain.doFilter(request, response);
    }

    private boolean iamCheck(HttpServletRequest request, HttpServletResponse response, JwtMap jwtInfo)
    {
        //to do: iam check
        try
        {
            AuthzService.RequestInfo requestInfo = new AuthzService.RequestInfo();
            requestInfo.setPath(request.getRequestURI());
            requestInfo.setMethod(request.getMethod());

            AuthzService.JwtInfo authzJwtInfo = new AuthzService.JwtInfo();
            authzJwtInfo.setBpId((String) jwtInfo.get(UserHeadInfo.BPID));
            authzJwtInfo.setBpName((String) jwtInfo.get(UserHeadInfo.BpName));
            authzJwtInfo.setUserId((String) jwtInfo.get(UserHeadInfo.USERID));
            authzJwtInfo.setUserName((String) jwtInfo.get(UserHeadInfo.USERNAME));
            authzJwtInfo.setUserKind((String) jwtInfo.get(UserHeadInfo.USERKIND));
            AuthzService.AuthzResponse allow = authzService.allow(requestInfo, authzJwtInfo);
            if (!allow.isAllow())
            {
                LOGGER.error(allow.getDetail());
                sendErrorResponse(ErrorCode.IAM_ILLEGAL_OPERATION, response);
                return true;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("auth error:{}", e);
            sendErrorResponse(ErrorCode.IAM_ILLEGAL_OPERATION, response);
            return true;
        }
        return false;
    }

    private JwtMap getJwtInfo(String token)
    {
        try
        {
            JwtMap jwtInfo = JwtTools.getJwtInfo(token, jwtkey, JwtUtils.getAuthorizationHeaderPrefix());
            LOGGER.info("jwtInfo: {}", jwtInfo);
            return jwtInfo;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error(token + "parse error." + e);
            return null;
        }
    }

    private Boolean checkJwt(Map<String, Object> jwtInfo, HttpServletRequest request, HttpServletResponse response)
    {
        if (null == jwtInfo || jwtInfo.size() < 4)
        {
            return false;
        }

        String agent = request.getHeader("user-agent");
        String remote = request.getRemoteHost();

        String userName = jwtInfo.get(UserHeadInfo.USERNAME).toString();
        String userId = jwtInfo.get(UserHeadInfo.USERID).toString();
        Collection<? extends GrantedAuthority> role = (Collection<? extends GrantedAuthority>)jwtInfo.get(UserHeadInfo.AUTIORITIES);

        Long validBegin = Long.parseLong(jwtInfo.get("begin").toString());

        Long curTime = System.currentTimeMillis();

        Long spanTime = curTime - validBegin;
        if (spanTime > (WebConstants.LNJOYING_TOKEN_INDATE*1000))
        {
            LOGGER.info("jwt over time");
            return false;
        }


        request.setAttribute("leftTime", spanTime/1000);
        //if the remain time is less than 5 min, then update the token
        if (spanTime > ((WebConstants.LNJOYING_TOKEN_INDATE-2)*1000))
        {
            Map<String, Object> headerInfo = new HashMap<>();
            headerInfo.put("user-agent", request.getHeader("user-agent"));
            headerInfo.put(UserHeadInfo.USERNAME, userName);
            headerInfo.put(UserHeadInfo.USERID, userId);
            headerInfo.put(UserHeadInfo.AUTIORITIES, role);
            headerInfo.put(UserHeadInfo.BPID, jwtInfo.get(UserHeadInfo.BPID));
            headerInfo.put(UserHeadInfo.BpName, jwtInfo.get(UserHeadInfo.BpName));
            request.setAttribute(UserHeadInfo.USERKIND, jwtInfo.get(UserHeadInfo.USERKIND));

            String token = JwtUtils.getNewJwtToken(userName,
                                    headerInfo,
                                    WebConstants.LNJOYING_TOKEN_INDATE,
                                    jwtkey);
            response.addCookie(JwtUtils.getNewCookie(WebConstants.ACCESS_TOKEN_NAME, token, WebConstants.LNJOYING_TOKEN_INDATE, "/"));
        }

        return true;
    }

    private UsernamePasswordAuthenticationToken getUsernamePasswordAuthenticationToken(String userName, String userId, List<Map<String, String>> role)
    {
        if (null != userName)
        {
            List<SimpleGrantedAuthority> s = new ArrayList<>();
            if (role == null)
            {
                return new UsernamePasswordAuthenticationToken(userName, null, Collections.singleton(new SimpleGrantedAuthority("ROLE_TENANT")));
            }

            List<GrantedAuthority> authorities = new ArrayList<>();
            for (Map<String,String> g:role)
            {
                authorities.add(new SimpleGrantedAuthority(g.get("authority")));
            }

            UsernamePasswordAuthenticationToken userToken = new UsernamePasswordAuthenticationToken(userName, null, authorities);
            Map<String, String> detailMap =  new HashMap<>();
            detailMap.put(UserHeadInfo.USERID, userId);
            userToken.setDetails(detailMap);
            return userToken;
        }

        return null;
    }

    void sendErrorResponse(ErrorCode e, HttpServletResponse response)
    {
        WebSystemException.setResponseStatus(e, response);

        JsonObject jsonObject = new JsonObject();
        jsonObject.put("code", e.getCode());
        jsonObject.put("message", e.getMessage());
        response.setContentType("application/json;charset=UTF-8");

        PrintWriter out = null;
        try
        {
            out = response.getWriter();
        }
        catch (IOException e1)
        {
            e1.printStackTrace();
        }
        out.print(jsonObject.toString());
    }

}