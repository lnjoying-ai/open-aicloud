package com.lnjoying.justice.omc.controller;

import com.lnjoying.justice.commonweb.controller.RestWebController;
import com.lnjoying.justice.omc.domain.dto.req.BaseReq;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.common.ErrorLevel;

import static com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils.*;

/**
 * ims base handler
 *
 * @author merak
 **/

public class OmcWebController extends RestWebController
{

    public void setBaseReq(BaseReq baseReq, String userId, String bpId, String userName, String bpName)
    {
        baseReq.setUserId(userId);
        baseReq.setBpId(bpId);
        baseReq.setUserName(userName);
        baseReq.setBpName(bpName);
    }
}
