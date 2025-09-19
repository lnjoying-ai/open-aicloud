package com.lnjoying.justice.ims.util;

import com.lnjoying.justice.ims.harbor.model.UserCreationReq;
import com.lnjoying.justice.ims.service.CombRpcService;
import com.lnjoying.justice.schema.service.ums.UmsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;


/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/6/22 21:26
 */

@Slf4j
public class HarborUserUtils
{
    public static void doSetEmail(CombRpcService combRpcService, String userName, UserCreationReq userCreationReq, String userId)
    {
        String email = "";
        try
        {
            if (StringUtils.isNotBlank(userId))
            {
                UmsService.UserInfo userInfo = combRpcService.getUmsService().getUserInfoByUseId(userId);
                String emailInfo = userInfo.getEmail();
                if (StringUtils.isNotBlank(emailInfo))
                {
                    email = emailInfo;
                }
            }

        }
        catch (Exception e)
        {
            log.error("get user info error: {}", e);
        }

        if (StringUtils.isBlank(email))
        {
            email = userName.replaceAll("@", "") + "@lnjoying.com";
        }

        userCreationReq.setEmail(email);
    }
}
