package com.lnjoying.justice.omc.prometheus.handler;

import com.lnjoying.justice.omc.prometheus.sd.PrometheusTargetsStore;
import com.lnjoying.justice.schema.entity.omc.MonitorEndpointInfo;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/12/15 16:40
 */

@Component
public class NodeMonitorEndpointInfoHandler implements MonitorEndpointInfoHandler
{

    private PrometheusTargetsStore nodesStore = new PrometheusTargetsStore();

    @Override
    public boolean addMonitorEndpoint(MonitorEndpointInfo monitorEndpointInfo)
    {
       return nodesStore.addTarget(monitorEndpointInfo.getBpId(), monitorEndpointInfo.getUserId(), monitorEndpointInfo.getSiteId(),
                monitorEndpointInfo.getNodeId(), monitorEndpointInfo.getEndpoint(), null);
    }

    @Override
    public PrometheusTargetsStore getTargetsStore()
    {
        return nodesStore;
    }

    @Override
    public String getType()
    {
        return "node";
    }
}
