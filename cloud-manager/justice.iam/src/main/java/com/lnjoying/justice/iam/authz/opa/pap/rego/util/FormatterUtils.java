package com.lnjoying.justice.iam.authz.opa.pap.rego.util;

import com.google.common.base.CaseFormat;
import org.springframework.util.StringUtils;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/3/16 16:18
 */

public final  class FormatterUtils
{
    public static String lowerHyphen2LowerUnderscore(String str)
    {
        if (StringUtils.containsWhitespace(str))
        {
            str = StringUtils.replace(str, " ", "-");
        }
        CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, str);
        return CaseFormat.LOWER_HYPHEN.to(CaseFormat.LOWER_UNDERSCORE, str);
    }
}
