package com.lnjoying.justice.servicegw.handler.iam;

import com.lnjoying.justice.commonweb.util.JwtUtils;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.common.RedisCacheField;
import com.lnjoying.justice.schema.constant.UserHeadInfo;
import com.lnjoying.justice.schema.constant.WebConstants;
import com.lnjoying.justice.schema.service.iam.AuthzService;
import com.lnjoying.justice.servicegw.config.DataSourceConfig;
import com.micro.core.common.JwtTools;
import com.micro.core.common.Utils;
import com.micro.core.persistence.redis.RedisUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.impl.JwtMap;
import io.netty.handler.codec.http.HttpHeaders;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.servicecomb.foundation.common.utils.BeanUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.CollectionUtils;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import static com.lnjoying.justice.schema.constant.UserHeadInfo.PROJECT;

/**
 * @Description:
 * @Author: Regulus
 * @Date: 1/13/23 3:56 PM
 */
public class AuthUtil
{
    private static Logger LOGGER = LogManager.getLogger();
    
    public static String getToken(HttpHeaders headers)
    {
        String token = headers.get("X-Access-Token");
    
        if (StringUtils.isEmpty(token))
        {
            String cookies = headers.get("Cookie");
            if (! StringUtils.isEmpty(cookies))
            {
                cookies = cookies.trim();
                String [] cookielist = cookies.split(";");
                for (String cookie : cookielist)
                {
                    cookie = cookie.trim();
                    if (cookie.isEmpty() || ! cookie.startsWith("Access-Token="))
                    {
                        continue;
                    }
            
                    token = cookie.replace("Access-Token=","");
                }
            }
        }
        
        return token;
    }
    
    static String jwtKey= "";
    public static AuthRsp doAuth(String url, HttpHeaders headers, String method, AuthzService authzService, boolean onlyJwtTokenValid)
    {
        AuthRsp rsp = new AuthRsp();
    
        rsp.setErrorCode(ErrorCode.SUCCESS);
        
        String jwtToken = getToken(headers);
        
        if (StringUtils.isEmpty(jwtToken))
        {
            rsp.setErrorCode(ErrorCode.InvalidAuthority);
            return rsp;
        }
        else
        {
            DataSourceConfig dataSourceConfig = BeanUtils.getBean("dataSourceConfig");
            if (dataSourceConfig != null)
            {
                jwtKey = dataSourceConfig.getJwtkey();
            }
        }


        boolean checkret = checkJwt(jwtToken, headers, rsp);
        
        if (checkret != true)
        {
            rsp.setErrorCode(ErrorCode.InvalidAuthority);
            
            return rsp;
        }

        if (onlyJwtTokenValid)
        {
            return rsp;
        }
        
        //to do: iam check
        try
        {
            AuthzService.RequestInfo requestInfo = new AuthzService.RequestInfo();
            requestInfo.setPath(url);
            requestInfo.setMethod(method);

            AuthzService.JwtInfo jwtInfo = new AuthzService.JwtInfo();
            jwtInfo.setBpId(rsp.getBpId());
            jwtInfo.setBpName(rsp.getBpName());
            jwtInfo.setUserId(rsp.getUserId());
            jwtInfo.setUserName(rsp.getUserName());
            jwtInfo.setUserKind(rsp.getUserKind());
            AuthzService.AuthzResponse allow = authzService.allow(requestInfo, jwtInfo);
            if (!allow.isAllow())
            {
                LOGGER.error(allow.getDetail());
                rsp.setErrorCode(ErrorCode.IAM_ILLEGAL_OPERATION);

                return rsp;
            }
            else
            {
                Map<String, String> attributes = allow.getAttributes();
                if (!CollectionUtils.isEmpty(attributes))
                {
                    String project = attributes.get(PROJECT);
                    if (StringUtils.isNotBlank(project))
                    {
                        rsp.setProjectId(project);
                    }
                }

            }
        }
        catch (Exception e)
        {
            LOGGER.error("auth rpc error:{}", e);
            rsp.setErrorCode(ErrorCode.IAM_ILLEGAL_OPERATION);
        }
        return rsp;
    }
    
    
    static private Boolean checkJwt(String accessToken, HttpHeaders headers, AuthRsp rsp)
    {
        try
        {
            JwtMap jwtInfo = JwtTools.getJwtInfo(accessToken, jwtKey, JwtUtils.getAuthorizationHeaderPrefix());
            
            if (null == jwtInfo || jwtInfo.size() < 4)
            {
                return false;
            }
            
            String agent = headers.get("user-agent");
            Collection<? extends GrantedAuthority> role = (Collection<? extends GrantedAuthority>) jwtInfo.get(UserHeadInfo.AUTIORITIES);
            
            if (!agent.equals(jwtInfo.get("user-agent")))
            {
                return false;
            }

            
            Long validBegin = Long.parseLong(jwtInfo.get("begin").toString());
            
            Long curTime = System.currentTimeMillis();
            
            Long spanTime = curTime - validBegin;
            if (spanTime > (WebConstants.LNJOYING_TOKEN_INDATE * 1000)) {
                return false;
            }
            
            String accessHash = Utils.getSHA(accessToken);
            if (checkIsLogout(accessHash))
            {
                return false;
            }
            
            
            LOGGER.info("jwt auth info {}", jwtInfo);
    
            rsp.setUserId((String)jwtInfo.get(UserHeadInfo.USERID));
            String userName =  URLEncoder.encode((String)jwtInfo.get(UserHeadInfo.USERNAME), StandardCharsets.UTF_8.name());
            rsp.setUserName(userName);
            rsp.setBpId((String)jwtInfo.get(UserHeadInfo.BPID));
            String bpName =  URLEncoder.encode((String)jwtInfo.get(UserHeadInfo.BpName), StandardCharsets.UTF_8.name());
            rsp.setBpName(bpName);
            rsp.setAuthorities(role.toString());
            rsp.setUserKind((String)jwtInfo.get(UserHeadInfo.USERKIND));
            
            return true;
        }
        catch (ExpiredJwtException e)
        {
            e.printStackTrace();
            return false;
        }
        catch (Exception e)
        {
            return false;
        }
    }
    
    static private boolean checkIsLogout(String accessHash)
    {
        try
        {
            if (RedisUtil.get(RedisCacheField.ACCESS_TOKEN_EXPIRE, accessHash) != null)
            {
                return true;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        return false;
    }
    
    
}
