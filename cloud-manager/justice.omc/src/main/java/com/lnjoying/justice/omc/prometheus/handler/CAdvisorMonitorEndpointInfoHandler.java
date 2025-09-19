package com.lnjoying.justice.omc.prometheus.handler;

import com.lnjoying.justice.omc.prometheus.sd.PrometheusTargetsStore;
import com.lnjoying.justice.schema.entity.omc.MonitorEndpointInfo;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/12/15 16:43
 */

@Component
public class CAdvisorMonitorEndpointInfoHandler implements MonitorEndpointInfoHandler
{
    private PrometheusTargetsStore cAdvisorStore = new PrometheusTargetsStore();

    @Override
    public boolean addMonitorEndpoint(MonitorEndpointInfo monitorEndpointInfo)
    {
       return cAdvisorStore.addTarget(monitorEndpointInfo.getBpId(), monitorEndpointInfo.getUserId(), monitorEndpointInfo.getSiteId(),
                monitorEndpointInfo.getNodeId(), monitorEndpointInfo.getEndpoint(), "");
    }

    @Override
    public PrometheusTargetsStore getTargetsStore()
    {
        return cAdvisorStore;
    }

    @Override
    public String getType()
    {
        return "CAdvisor";
    }
}
