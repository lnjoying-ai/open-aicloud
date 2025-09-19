package com.lnjoying.justice.servicegw.handler.iam;

import com.lnjoying.justice.schema.common.ErrorCode;
import lombok.Data;

/**
 * @Description:
 * @Author: Regulus
 * @Date: 1/13/23 3:56 PM
 */
@Data
public class AuthRsp
{
    private String      userId;
    private String        bpId;
    private String    userName;
    private String      bpName;
    private String      userKind;
    private String authorities;
    private ErrorCode errorCode;
    ////////to do: other iam rsp info////////

    private String    projectId;

    private String   projectChain;

}
