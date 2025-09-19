package com.lnjoying.justice.omc.service.impl;

import com.lnjoying.justice.omc.db.model.TblOmcAlarm;
import com.lnjoying.justice.omc.service.NotifyCommonReduce;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2024/9/25 15:51
 */

@Service
@Slf4j
public class NotifyCommonReduceImpl implements NotifyCommonReduce
{
    @Override
    public boolean reduceAlarm(TblOmcAlarm omcAlarm)
    {
        return false;
    }
}
