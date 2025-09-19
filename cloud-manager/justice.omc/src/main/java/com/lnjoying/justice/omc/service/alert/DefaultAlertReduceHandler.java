package com.lnjoying.justice.omc.service.alert;

import com.lnjoying.justice.omc.db.model.TblOmcAlertLog;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2024/9/26 10:18
 */

@Component
public class DefaultAlertReduceHandler implements AlertReduceHandler
{
    private List<AlertFilter> filters;

    public DefaultAlertReduceHandler(List<AlertFilter> filters)
    {
        this.filters = filters;
    }
    @Override
    public boolean reduceAlarm(TblOmcAlertLog alertLog)
    {
        for(AlertFilter filter: filters)
        {
            if(!filter.filter(alertLog))
            {
                return false;
            }
        }
        return true;
    }
}
