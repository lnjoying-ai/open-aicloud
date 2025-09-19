package com.lnjoying.justice.ims.exception;

import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.common.ErrorLevel;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;

/**
 * ims web exception
 *
 * @author merak
 **/

public class ImsWebSystemException extends WebSystemException
{

    public ImsWebSystemException(ErrorCode code, ErrorLevel level)
    {
        super(code, level);
    }

    public static Response.Status getHeadStatus(ErrorCode status)
    {
        switch (status)
        {
            case DuplicateUser:
            case CreateUserError:
            case EmailOccupied:
            case PhoneOccupied:
            case REGISTRY_DUP:
            case REGISTRY_3RD_DUP:
                return Response.Status.CONFLICT;
            case Project_Not_Exist:
            case User_Not_Exist:
            case InvalidReq:
            case REGISTRY_NOT_EXIST:
            case REGISTRY_USER_NOT_EXIST:
            case REGISTRY_PROJECT_NOT_EXIST:
            case REGISTRY_REPO_NOT_EXIST:
            case REGISTRY_REPO_TAG_NOT_EXIST:
            case REGISTRY_3RD_NOT_EXIST:
            case USER_ID_NOT_FOUND:
            case BP_ID_NOT_FOUND:
            case REGION_REGISTRY_NOT_FOUND:
                return Response.Status.NOT_FOUND;
            case SystemError:
            case SQL_ERROR:
                return Response.Status.INTERNAL_SERVER_ERROR;
            case Redirect:
                return Response.Status.FOUND;
            case InvalidAuthority:
                return Response.Status.UNAUTHORIZED;
            default:
                return Response.Status.BAD_REQUEST;
        }
    }
}
