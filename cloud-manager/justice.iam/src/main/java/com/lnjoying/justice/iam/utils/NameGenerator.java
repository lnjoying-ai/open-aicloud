package com.lnjoying.justice.iam.utils;

import com.micro.core.common.RandomName;
import org.apache.commons.lang.StringUtils;

import static com.lnjoying.justice.iam.common.constant.IamConstants.SUBTRACT;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/3/21 17:25
 */

public final class NameGenerator
{

    private NameGenerator(){}

    public static String getName(String namePrefix, int versionNumber)
    {
        return namePrefix + versionNumber;
    }

    public static String getNextName(String name)
    {
        try
        {
            if (StringUtils.isNotBlank(name))
            {
                int lastIndex = name.lastIndexOf(SUBTRACT);
                if (lastIndex > 0)
                {
                    int next = 1;
                    String nameSuffix = name.substring(lastIndex + 1);
                    if (StringUtils.isNotBlank(nameSuffix))
                    {
                        next = Integer.parseInt(nameSuffix) + 1;
                    }

                    return name.substring(0, lastIndex) + SUBTRACT + next;
                }

                return name + SUBTRACT + 1;
            }
        }
        catch (Exception e)
        {
            return name + RandomName.getRandomName(4);
        }

        return null;
    }
}
