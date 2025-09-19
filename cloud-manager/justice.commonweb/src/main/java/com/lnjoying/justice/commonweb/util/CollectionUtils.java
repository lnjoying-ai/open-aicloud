package com.lnjoying.justice.commonweb.util;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Description: provide common method for collection
 * @Author: Regulus
 * @Date: 1/7/22 11:11 AM
 */
public class CollectionUtils
{
    public static boolean  hasContent(String content)
    {
        if (StringUtils.isEmpty(content))
        {
            return false;
        }
        
        if (StringUtils.isBlank(content))
        {
            return false;
        }
        return true;
    }
    
    public static boolean  isEmpty(List<?> tList)
    {
        if (tList == null || tList.isEmpty())
        {
            return true;
        }
        return false;
    }
    
    public static boolean  isEmpty(Set<?> tList)
    {
        if (tList == null || tList.isEmpty())
        {
            return true;
        }
        return false;
    }
    
    public static boolean  isEmpty(Map<?,?> tMap)
    {
        if (tMap == null || tMap.isEmpty())
        {
            return true;
        }
        return false;
    }
}
