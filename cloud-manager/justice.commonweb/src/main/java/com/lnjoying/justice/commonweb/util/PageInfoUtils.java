package com.lnjoying.justice.commonweb.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2024/7/18 17:01
 */

public class PageInfoUtils
{

    public static <T> List<T> getPaginatedList(List<T> list, int pageNum, int pageSize) {
        if (CollectionUtils.isEmpty(list))
        {
            return new ArrayList<>();
        }

        int fromIndex = (pageNum - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, list.size());

        if (fromIndex >= list.size() || fromIndex < 0) {
            return new ArrayList<>();
        }

        return list.subList(fromIndex, toIndex);
    }
}
