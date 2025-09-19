package com.lnjoying.justice.commonweb.util;

import com.micro.core.common.Utils;
import com.micro.core.util.DateUtils;

import java.util.Date;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2024/7/10 15:34
 */

public class UniqueIDGenerator
{
    public static String generateUniqueID(String idType) {

        String currentTimestamp = DateUtils.getCurrentTimestamp();
        String uuid = Utils.assignUUId();
        String uniqueID = idType + "-" + currentTimestamp + "-" + uuid;

        if (uniqueID.length() > 64) {
            uniqueID = uniqueID.substring(0, 64);
        }


        return uniqueID;
    }
}
