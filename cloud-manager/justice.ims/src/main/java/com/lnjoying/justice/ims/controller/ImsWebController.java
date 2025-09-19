package com.lnjoying.justice.ims.controller;

import com.lnjoying.justice.commonweb.controller.RestWebController;
import com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils;
import com.lnjoying.justice.ims.domain.dto.req.BaseReq;
import com.lnjoying.justice.ims.exception.ImsWebSystemException;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.common.ErrorLevel;
import com.lnjoying.justice.schema.constant.RoleConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.servicecomb.swagger.invocation.Response;

import static com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils.*;

/**
 * ims base handler
 *
 * @author merak
 **/

public class ImsWebController extends RestWebController
{
    protected void checkRoleImsAdmin(String authorities)
    {
        boolean check = isAdmin();

        if (!check)
        {
            throw new ImsWebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
        }
    }


    protected boolean isRoleImsAdmin(String authorities)
    {
        return isAdmin();
    }

    protected void checkRoleImsTenantAdmin(String authorities)
    {
        boolean check = isBpAdmin();

        if (!check)
        {
            throw new ImsWebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
        }

    }

    protected boolean isRoleImsTenantAdmin(String authorities) throws ImsWebSystemException
    {
        return isBpAdmin();
    }

    protected void checkRoleImsTenant(String authorities)
    {
        boolean check = isBpUser();
        if (!check)
        {
            throw new ImsWebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
        }
    }

    protected boolean isRoleImsTenant(String authorities)
    {
        return isBpUser();
    }

    protected void checkRoleImsTenantOrAdmin(String authorities)
    {
        if (!(isAdmin() || isBpAdmin() || isBpUser()))
        {
            throw new ImsWebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
        }
    }

    public void setBaseReq(BaseReq baseReq, String userId, String bpId, String userName, String bpName)
    {
        baseReq.setUserId(userId);
        baseReq.setBpId(bpId);
        baseReq.setUserName(userName);
        baseReq.setBpName(bpName);
    }
}
