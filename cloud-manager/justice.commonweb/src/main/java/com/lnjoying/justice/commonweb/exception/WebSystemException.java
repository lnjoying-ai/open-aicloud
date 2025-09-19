package com.lnjoying.justice.commonweb.exception;

import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.common.ErrorLevel;
import com.lnjoying.justice.schema.common.SystemException;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response.Status;

import static com.lnjoying.justice.schema.common.ErrorCode.HELM_REPO_NAME_EXIST;
import static com.lnjoying.justice.schema.common.ErrorCode.TTY_STACK_TEMPLATE_DUP;
import static com.lnjoying.justice.schema.service.ecrm.RegionResourceService.NodeStatus.PERMISSION_DENIED;

public class WebSystemException extends SystemException
{
    private static final long serialVersionUID = -89334987778999224L;
    public WebSystemException(ErrorCode code, ErrorLevel level) {
        super(code, level);
    }

    public WebSystemException(ErrorCode code, ErrorLevel level, String detailMsg)
    {
        super(code, level, detailMsg);
    }

    public WebSystemException(ErrorCode code, ErrorLevel level, int httpResponseCode)
    {
        super(code, level, httpResponseCode);
    }

    public WebSystemException(ErrorCode code, Throwable cause, ErrorLevel level) {
        super(code, cause, level);
    }


    public static Status getHeadStatus(ErrorCode status)
    {
        switch (status)
        {
            case DuplicateUser:
            case CreateUserError:
            case EmailOccupied:
            case PhoneOccupied:
            case TTY_STACK_TEMPLATE_DUP:
            case HELM_REPO_NAME_EXIST:
            case HELM_STACK_NAME_EXIST:
                return Status.CONFLICT;
            case Project_Not_Exist:
            case User_Not_Exist:
            case InvalidReq:
            case NODE_NOT_EXIST:
                return Status.NOT_FOUND;
            case SystemError:
            case SQL_ERROR:
                return Status.INTERNAL_SERVER_ERROR;
            case Redirect:
                return Status.FOUND;
            case InvalidAuthority:
                return Status.UNAUTHORIZED;
            case PERMISSION_DENIED:
            case User_Not_Grant:
                return Status.FORBIDDEN;
            default:
                return Status.BAD_REQUEST;
        }
    }

    public static void setResponseStatus(ErrorCode status, HttpServletResponse servletResponse)
    {
        servletResponse.setContentType("application/json;charset=utf-8");

        if (servletResponse.getStatus() >= HttpStatus.BAD_REQUEST.value())
        {
            return;
        }
        servletResponse.setStatus(getHeadStatus(status).getStatusCode());
    }
}
