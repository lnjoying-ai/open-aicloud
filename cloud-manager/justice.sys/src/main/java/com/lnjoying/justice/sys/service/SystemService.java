package com.lnjoying.justice.sys.service;

import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.commonweb.util.JsonUtils;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.common.ErrorLevel;
import com.lnjoying.justice.schema.common.RedisCacheField;
import com.lnjoying.justice.sys.domain.dto.resp.SystemInfo;
import com.micro.core.persistence.redis.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SystemService
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    private CombRpcService combRpcService;
    
    public SystemInfo getSystemInfo()
    {
        SystemInfo systemInfo;
        try
        {
            String systemInfoJsonStr = RedisUtil.get(RedisCacheField.SYSTEM_INFO);

            if (StringUtils.isNotEmpty(systemInfoJsonStr))
            {
                systemInfo = JsonUtils.fromJson(systemInfoJsonStr, SystemInfo.class);
            }
            else
            {
                systemInfo = new SystemInfo();
                systemInfo.setRegisterMode(combRpcService.getUmsService().getRegisterMode());

                RedisUtil.set(RedisCacheField.SYSTEM_INFO, JsonUtils.toJson(systemInfo), 60*60);
            }

            return systemInfo;
        }
        catch (Exception e)
        {
            LOGGER.error("get system info failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.SystemError, ErrorLevel.INFO, e.getMessage());
        }
    }
}
