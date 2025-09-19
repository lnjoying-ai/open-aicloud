package com.lnjoying.justice.iam.utils;

import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.iam.common.constant.CommonRole;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.common.ErrorLevel;
import com.micro.core.persistence.redis.RedisUtil;

import java.util.Objects;
import java.util.regex.Pattern;


public class ValidatorUtil
{
    public static boolean validateStr(String regx, String input)
    {
        return Pattern.matches(regx, input);
    }

    public static void checkValidateCode(String verCode,String redisMKey, String key)
    {
        if (null == verCode || verCode.isEmpty())
        {
            throw new WebSystemException(ErrorCode.Invalid_validateCode, ErrorLevel.ERROR);
        }

        String cacheValidateCode = RedisUtil.get(redisMKey+key);

        if (! verCode.equals(cacheValidateCode))
        {
            throw new WebSystemException(ErrorCode.Invalid_validateCode, ErrorLevel.ERROR);
        }

        RedisUtil.delete(redisMKey+key);
    }

    public static boolean validOperationSupporter(String userId)
    {
        if (ServiceCombRequestUtils.isAdmin() || UserInfoUtils.checkRole(CommonRole.OPERATION_SUPPORTER))
        {
            return true;
        }

        return false;
    }
}
