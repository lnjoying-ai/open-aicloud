package com.lnjoying.justice.iam.utils;

import com.lnjoying.justice.schema.common.RedisCacheField;
import com.micro.core.persistence.redis.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 验证码校验工具类
 *
 * @author: Robin
 * @date: 2023年08月14日 11:52
 */
public class VerificationCodeUtil
{

    private static final Logger LOGGER = LoggerFactory.getLogger(VerificationCodeUtil.class);

    /**
    * @Description: 检查重置密码动作是否因为多次输入错误验证码而锁定
    * @author: Robin
    * @date: 2023/8/14 11:53
    * @param phoneNumber:
    * @param emailAddr:
    * @Return: boolean
    */
    public static boolean checkIfResetPwdLocked(String phoneNumber, String emailAddr)
    {
        if (StringUtils.isNotBlank(phoneNumber))
        {
            String lockSec = RedisUtil.get(RedisCacheField.RESET_PWD_LOCK_SEC, phoneNumber);
            if (StringUtils.isNotBlank(lockSec))
            {
                LOGGER.info("reset password by phone, lock seconds left: {}s", lockSec);
                return true;
            }
        } else if (StringUtils.isNotBlank(emailAddr))
        {
            String lockSec = RedisUtil.get(RedisCacheField.RESET_PWD_LOCK_SEC, emailAddr);
            if (StringUtils.isNotBlank(lockSec))
            {
                LOGGER.info("reset password by email, lock seconds left: {}s", lockSec);
                return true;
            }
        }

        return false;
    }
}
