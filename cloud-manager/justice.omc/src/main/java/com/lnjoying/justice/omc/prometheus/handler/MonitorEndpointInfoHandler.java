package com.lnjoying.justice.omc.prometheus.handler;

import com.lnjoying.justice.omc.prometheus.sd.PrometheusTargetsStore;
import com.lnjoying.justice.omc.prometheus.sd.TargetsData;
import com.lnjoying.justice.schema.entity.omc.MonitorEndpointInfo;

import java.util.List;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/12/15 16:38
 */

public interface MonitorEndpointInfoHandler
{
    boolean addMonitorEndpoint(MonitorEndpointInfo monitorEndpointInfo);

    String getType();

    PrometheusTargetsStore getTargetsStore();

    default List<TargetsData>  getAllTargetsData()
    {
        return getTargetsStore().getAllTargetsData();
    }
}
