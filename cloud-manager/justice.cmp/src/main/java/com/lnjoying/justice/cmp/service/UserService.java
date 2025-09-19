package com.lnjoying.justice.cmp.service;

import com.lnjoying.justice.cmp.service.rpc.CombRpcService;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    private CombRpcService combRpcService;

    public String getBpName(String bpId)
    {
        if (StringUtils.isNotBlank(bpId))
        {
            try
            {
                return combRpcService.getUmsService().getBpNameByBpId(bpId);
            } catch (Exception e)
            {
                LOGGER.error("get bp name failed");
            }
        }
        return null;
    }

    public String getUserName(String userId)
    {
        if (StringUtils.isNotBlank(userId))
        {
            try
            {
                return combRpcService.getUmsService().getUserNameByUserId(userId);
            } catch (Exception e)
            {
                LOGGER.error("get bp name failed");
            }
        }
        return null;
    }
}
