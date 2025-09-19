package com.lnjoying.justice.commonweb.util;

import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.common.ErrorLevel;
import com.lnjoying.justice.schema.constant.RequestContextInfoField;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Extension;
import io.swagger.annotations.ExtensionProperty;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.servicecomb.core.Invocation;
import org.apache.servicecomb.foundation.vertx.http.StandardHttpServletRequestEx;
import org.apache.servicecomb.foundation.vertx.http.VertxServerRequestToHttpServletRequest;
import org.apache.servicecomb.swagger.invocation.context.ContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.*;

import static com.lnjoying.justice.commonweb.common.SwaggerConstants.*;
import static com.lnjoying.justice.schema.common.ErrorCode.AUTHORITIES_NOT_FOUND;
import static com.lnjoying.justice.schema.common.ErrorLevel.ERROR;
import static com.lnjoying.justice.schema.constant.UserHeadInfo.*;
import static com.lnjoying.justice.schema.constant.UserKindEnum.*;
import static org.apache.servicecomb.common.rest.RestConst.REST_REQUEST;

/**
 * request utils of servicecomb
 *
 * @author merak
 **/

public class ServiceCombRequestUtils
{
    private static final Logger logger = LoggerFactory.getLogger(ServiceCombRequestUtils.class);

    public static HttpServletRequest getHttpServletRequest()
    {
        HttpServletRequest request = null;
        Invocation invocationContext = (Invocation) ContextUtils.getInvocationContext();
        if (Objects.nonNull(invocationContext))
        {
            request = (invocationContext).getRequestEx();
        }

        if (request == null) {
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            if (requestAttributes != null) {
                request = ((ServletRequestAttributes) requestAttributes).getRequest();
            }
        }
        return request;
    }

    public static String getAuthorities()
    {
        String authorities = (String)getHttpServletRequest().getHeader(AUTIORITIES);
        if (StringUtils.isBlank(authorities))
        {
            throw new WebSystemException(AUTHORITIES_NOT_FOUND, ERROR);
        }
        return authorities;
    }
    
    public static boolean isAdmin()
    {
        String kind = getKind();
        return ADMIN.equals(valueOf(kind)) || SYSTEM.equals(valueOf(kind));
    }

    public static boolean isAdmin(String kind)
    {
        return ADMIN.equals(valueOf(kind)) || SYSTEM.equals(valueOf(kind));
    }

    public static boolean isBpAdmin()
    {
        String kind = getKind();
        return BP_ADMIN.equals(valueOf(kind));
    }

    public static boolean isBpAdmin(String kind)
    {
        return BP_ADMIN.equals(valueOf(kind));
    }

    public static boolean isBpUser()
    {
        String kind = getKind();
        return BP_SUB_USER.equals(valueOf(kind));
    }

    public static boolean isBpUser(String kind)
    {
        return BP_SUB_USER.equals(valueOf(kind));
    }


    public static boolean isPersonal()
    {
        String kind = getKind();
        return PERSONAL.equals(valueOf(kind));
    }

    public static boolean isPersonal(String kind)
    {
        return PERSONAL.equals(valueOf(kind));
    }

    public static boolean isBpUserOrPersonal()
    {
        return isBpUser() || isPersonal() ;
    }

    public static boolean isBpUserOrPersonal(String kind)
    {
        return isBpUser(kind) || isPersonal(kind) ;
    }


    public static boolean isSystemUser()
    {
        return isAdmin() || isBpAdmin() || isBpUserOrPersonal();
    }

    public static boolean isSystemUser(String kind)
    {
        return isAdmin(kind) || isBpAdmin(kind) || isBpUserOrPersonal(kind);
    }


    public static String getUserId()
    {
        String userId = (String)getHttpServletRequest().getHeader(USERID);
        if (StringUtils.isBlank(userId))
        {
            throw new WebSystemException(ErrorCode.USER_ID_NOT_FOUND, ErrorLevel.ERROR);
        }
        return userId;
    }

    public static String getUserName()
    {
        String userName = (String)getHttpServletRequest().getHeader(USERNAME);
        if (StringUtils.isBlank(userName))
        {
            throw new WebSystemException(ErrorCode.USER_NAME_NOT_FOUND, ErrorLevel.ERROR);
        }
        return userName;
    }

    public static String getBpId()
    {
        String bpId = (String)getHttpServletRequest().getHeader(BPID);

        // role of tenant_admin must have bpId
        if (isBpAdmin())
        {
            if (StringUtils.isBlank(bpId))
            {
                throw new WebSystemException(ErrorCode.BP_ID_NOT_FOUND, ErrorLevel.ERROR);
            }
        }

        if (StringUtils.isBlank(bpId))
        {
            bpId = null;
        }

        return bpId;
    }

    public static String getBpName()
    {
        String bpName = (String)getHttpServletRequest().getHeader(BpName);

        return bpName;
    }

    public static String getKind()
    {
        String userKind = (String)getHttpServletRequest().getHeader(USERKIND);

        if (StringUtils.isBlank(userKind))
        {
            throw new WebSystemException(ErrorCode.USER_KIND_NOT_FOUND, ErrorLevel.ERROR);
        }
        return userKind;
    }


    public static Pair<String, String> getUserAttributes()
    {

        // left bpId, right userId
        Pair<String, String> pair;

        if (isAdmin())
        {
            pair = ImmutablePair.of(null, null);
        }
        else if (isBpAdmin())
        {
            pair = ImmutablePair.of(getBpId(), null);
        }
        else
        {
            pair = ImmutablePair.of(getBpId(), getUserId());
        }

        return pair;
    }

    /**
     * just for simplified  query not real user id
     * @return
     */
    public static String queryUserId()
    {
        return getUserAttributes().getRight();
    }

    /**
     * just for simplified query not real bp id
     * @return
     */
    public static String queryBpId()
    {
        return getUserAttributes().getLeft();
    }

    /**
     * get HttpServletRequest from InvocationContext
     *
     * @return
     */
    public static HttpServletRequest httpServletRequest()
    {
        return (((StandardHttpServletRequestEx) ((Invocation) ContextUtils.getInvocationContext()).getLocalContext().get("servicecomb-rest-request")));
    }

    public static void checkRoleAdmin()
    {
        if (!isAdmin())
        {
            throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
        }
    }

    public static void checkRoleTenantAdmin()
    {
        if (!isBpAdmin())
        {
            throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
        }

    }

    public static void checkRoleTenant()
    {

        if (!isBpUser())
        {
            throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
        }
    }

    public static void checkRoleTenantAdminOrAdmin()
    {
        if (!(isAdmin() || isBpAdmin()))
        {
            throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
        }
    }

    public static String getHeaderValue(@NotNull String headerName, String defaultValue)
    {
        VertxServerRequestToHttpServletRequest request = getVertxServerRequestToHttpServletRequest();
        return Optional.ofNullable(request).map(x -> x.getHeader(headerName)).orElse(defaultValue);
    }

    public static String getRequestPath(String defaultValue)
    {
        VertxServerRequestToHttpServletRequest request = getVertxServerRequestToHttpServletRequest();
        return Optional.ofNullable(request).map(x -> x.getRequestURI()).orElse(defaultValue);
    }

    public static String getQueryString(String defaultValue)
    {
        VertxServerRequestToHttpServletRequest request = getVertxServerRequestToHttpServletRequest();
        return Optional.ofNullable(request).map(x -> x.getQueryString()).orElse(defaultValue);
    }

    private static VertxServerRequestToHttpServletRequest getVertxServerRequestToHttpServletRequest()
    {
        try
        {
            VertxServerRequestToHttpServletRequest request = (VertxServerRequestToHttpServletRequest) ContextUtils.getInvocationContext()
                    .getLocalContext().get(REST_REQUEST);
            return request;
        } catch (Exception e)
        {
            if (NullPointerException.class.equals(e.getClass()))
            {
                  //a known issue, scheduler thread calling may raise this exception.
//                logger.warn("获取Request对象失败: NullPointerException(已知问题，可忽略)");
            } else
            {
                logger.warn("获取Request对象失败: stackTrace: {}, errorMessage: {}", ExceptionUtils.getStackTrace(e), e.getMessage());
            }
            return null;
        }
    }

    public static Map<String, String[]> getRequestParameterMap(Map<String, String[]> defaultValue)
    {
        VertxServerRequestToHttpServletRequest request = getVertxServerRequestToHttpServletRequest();
        return Optional.ofNullable(request).map(x -> x.getParameterMap()).orElse(defaultValue);
    }

    public static String getUserAccessIp()
    {
        VertxServerRequestToHttpServletRequest request = getVertxServerRequestToHttpServletRequest();
        return RequestUtils.getIpAddr(request);
    }

    public static Map<String, String> getRequestKeyInfoMap(Class resourceClass, @NotNull String serviceName, @NotNull String actionName)
    {
        Map<String, String> map = new HashMap<>();
        map.put(RequestContextInfoField.BP_ID, getHeaderValue(BPID, ""));
        map.put(RequestContextInfoField.SERVICE_NAME, serviceName);
        if (resourceClass != null)
        {
            map.put(RequestContextInfoField.RESOURCE_NAME, getResourceEnName(resourceClass));
        }
        map.put(RequestContextInfoField.ACTION_NAME, actionName);
        map.put(RequestContextInfoField.ACCESS_IP, getUserAccessIp());
        map.put(RequestContextInfoField.USER_AGENT, getHeaderValue(USER_AGENT, ""));
        map.put(RequestContextInfoField.REQUEST_PATH, getRequestPath(""));
        map.put(RequestContextInfoField.QUERY_STRING, getQueryString(""));

        try
        {
            map.put(RequestContextInfoField.REQUEST_PARAMETER_MAP,
                    JsonUtils.toJson(getRequestParameterMap(new HashMap<>(0))));
        } catch (Exception e)
        {
            try
            {
                HashMap<Object, Object> errorMap = new HashMap<>(1);
                errorMap.put("error", String.format("get request parameter map error: %s", e.getMessage()));
                map.put(RequestContextInfoField.REQUEST_PARAMETER_MAP, JsonUtils.toJson(errorMap));
            } catch (Exception ex)
            {
            }
            logger.error(e.getMessage());
        }

        return map;
    }

    public static String getResourceEnName(Class<?> resourceClass)
    {
        if (resourceClass == null)
        {
            return null;
        }

        String resourceName = null;
        if (resourceClass.isAnnotationPresent(ApiOperation.class))
        {
            ApiOperation apiOperation = resourceClass.getAnnotation(ApiOperation.class);
            Optional<Extension> resourceExtension = Arrays.stream(apiOperation.extensions())
                    .filter(x -> SWAGGER_X_RESOURCE.equals(x.name())).findFirst();
            if (resourceExtension.isPresent())
            {
                Optional<ExtensionProperty> resourceProperty = Arrays.stream(resourceExtension.get().properties())
                        .filter(x -> SWAGGER_RESOURCE_SINGULAR_NAME.equals(x.name())).findFirst();
                if (resourceProperty.isPresent())
                {
                    resourceName = resourceProperty.get().value();
                }
            }
        }

        return resourceName;
    }

    public static String getResourceCnName(Class<?> resourceClass)
    {
        if (resourceClass == null)
        {
            return null;
        }

        String resourceName = null;
        if (resourceClass.isAnnotationPresent(ApiOperation.class))
        {
            ApiOperation apiOperation = resourceClass.getAnnotation(ApiOperation.class);
            Optional<Extension> resourceExtension = Arrays.stream(apiOperation.extensions())
                    .filter(x -> SWAGGER_X_RESOURCE.equals(x.name())).findFirst();
            if (resourceExtension.isPresent())
            {
                Optional<ExtensionProperty> resourceProperty = Arrays.stream(resourceExtension.get().properties())
                        .filter(x -> SWAGGER_RESOURCE_DESCRIPTION.equals(x.name())).findFirst();
                if (resourceProperty.isPresent())
                {
                    resourceName = resourceProperty.get().value();
                }
            }
        }

        return resourceName;
    }
}
