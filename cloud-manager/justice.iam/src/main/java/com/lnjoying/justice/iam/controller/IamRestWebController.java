package com.lnjoying.justice.iam.controller;

import com.lnjoying.justice.commonweb.controller.RestWebController;
import com.lnjoying.justice.iam.domain.dto.request.BaseReq;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/2/10 14:44
 */

public class IamRestWebController extends RestWebController
{

    public void setBaseReq(BaseReq baseReq, String bpId, String userId, String bpName, String userName)
    {
        baseReq.setUserId(userId);
        baseReq.setBpId(bpId);
        baseReq.setUserName(userName);
        baseReq.setBpName(bpName);
    }

    public void setBaseReq(BaseReq baseReq, String bpId, String userId)
    {
        baseReq.setUserId(userId);
        baseReq.setBpId(bpId);
    }
}

