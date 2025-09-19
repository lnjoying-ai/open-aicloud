package com.lnjoying.justice.commonweb.util;

/**
 * 功能描述
 *
 * @author: Robin
 * @date: 2023年10月27日 15:56
 */
public class DeepCopyUtils
{
    public static <T> T deepCopy(T copyFrom)
    {
        try
        {
            if (copyFrom == null)
            {
                return null;
            }
            String copyFromJson = JacksonUtils.objToStr(copyFrom);
            return (T) JacksonUtils.strToObj(copyFromJson, copyFrom.getClass());
        } catch (Throwable e)
        {
            return null;
        }
    }
}
