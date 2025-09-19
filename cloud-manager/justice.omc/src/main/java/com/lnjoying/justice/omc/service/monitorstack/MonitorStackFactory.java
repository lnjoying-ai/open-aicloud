package com.lnjoying.justice.omc.service.monitorstack;

import com.lnjoying.justice.omc.domain.model.IntegrationTaskTypeEnum;
import com.lnjoying.justice.omc.service.monitorstack.impl.NodeMonitorStackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/10/30 14:54
 */

@Service
public class MonitorStackFactory
{

    @Autowired
    private NodeMonitorStackService monitorStackService;


    public BaseMonitorStackService getServiceForIntegrationType(IntegrationTaskTypeEnum taskType)
    {
        if (IntegrationTaskTypeEnum.LIGHTWEIGHT_NODE_DEPLOYMENT_TASK.equals(taskType))
        {
            return monitorStackService;
        }

        // more integration type
        return null;
    }
}
