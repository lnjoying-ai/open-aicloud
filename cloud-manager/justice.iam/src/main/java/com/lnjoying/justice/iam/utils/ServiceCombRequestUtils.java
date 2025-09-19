package com.lnjoying.justice.iam.utils;

import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.common.ErrorLevel;
import com.lnjoying.justice.schema.constant.RoleConstants;
import com.lnjoying.justice.schema.constant.UserKindEnum;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.servicecomb.core.Invocation;
import org.apache.servicecomb.foundation.vertx.http.StandardHttpServletRequestEx;
import org.apache.servicecomb.swagger.invocation.context.ContextUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

import static com.lnjoying.justice.schema.common.ErrorCode.AUTHORITIES_NOT_FOUND;
import static com.lnjoying.justice.schema.common.ErrorLevel.ERROR;
import static com.lnjoying.justice.schema.constant.UserHeadInfo.*;
import static com.lnjoying.justice.schema.constant.UserKindEnum.*;

/**
 * request utils of servicecomb
 *
 * @author merak
 **/

public class ServiceCombRequestUtils
{

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
        String authorities = (String)getHttpServletRequest().getAttribute(AUTIORITIES);
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

    public static String getUserId()
    {
        String userId = (String)getHttpServletRequest().getAttribute(USERID);
        if (StringUtils.isBlank(userId))
        {
            throw new WebSystemException(ErrorCode.USER_ID_NOT_FOUND, ErrorLevel.ERROR);
        }
        return userId;
    }

    public static boolean isBpUserOrPersonal(String kind)
    {
        return isBpUser(kind) || isPersonal(kind) ;
    }

    public static String getUserName()
    {
        String userName = (String)getHttpServletRequest().getAttribute(USERNAME);
        if (StringUtils.isBlank(userName))
        {
            throw new WebSystemException(ErrorCode.USER_NAME_NOT_FOUND, ErrorLevel.ERROR);
        }
        return userName;
    }

    public static boolean isSystemUser()
    {
        return isAdmin() || isBpAdmin() || isBpUserOrPersonal();
    }

    public static boolean isSystemUser(String kind)
    {
        return isAdmin(kind) || isBpAdmin(kind) || isBpUserOrPersonal(kind);
    }


    public static String getBpId()
    {
        String bpId = (String)getHttpServletRequest().getAttribute(BPID);

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
        String bpName = (String)getHttpServletRequest().getAttribute(BpName);

        if (StringUtils.isBlank(bpName))
        {
            throw new WebSystemException(ErrorCode.BP_NAME_NOT_FOUND, ErrorLevel.ERROR);
        }
        return bpName;
    }

    public static String getKind()
    {
        String userKind = (String)getHttpServletRequest().getAttribute(USERKIND);

        if (StringUtils.isBlank(userKind))
        {
            throw new WebSystemException(ErrorCode.USER_KIND_NOT_FOUND, ErrorLevel.ERROR);
        }
        return userKind;
    }

    public static int getKindValue()
    {
        String kind = getKind();

        return UserKindEnum.fromMessage(kind).getCode();
    }


    public static Pair<String, String> getUserAttributes()
    {

        // left bpId, right userId
        Pair<String, String> pair;

        if (isAdmin())
        {
            pair = ImmutablePair.nullPair();
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

}
