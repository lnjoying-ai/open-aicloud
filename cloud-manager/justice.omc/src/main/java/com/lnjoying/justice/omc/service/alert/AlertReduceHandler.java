package com.lnjoying.justice.omc.service.alert;

import com.lnjoying.justice.omc.db.model.TblOmcAlertLog;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2024/9/23 10:07
 */

public interface AlertReduceHandler
{
    boolean reduceAlarm(TblOmcAlertLog alertLog);
}
