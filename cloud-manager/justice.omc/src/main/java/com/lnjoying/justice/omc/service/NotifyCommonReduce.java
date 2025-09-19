package com.lnjoying.justice.omc.service;

import com.lnjoying.justice.omc.db.model.TblOmcAlarm;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2024/9/25 15:48
 */

public interface NotifyCommonReduce
{

    boolean reduceAlarm(TblOmcAlarm omcAlarm);
}
