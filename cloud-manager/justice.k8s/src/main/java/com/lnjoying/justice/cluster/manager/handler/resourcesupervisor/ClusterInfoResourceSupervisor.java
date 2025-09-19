package com.lnjoying.justice.cluster.manager.handler.resourcesupervisor;

import com.lnjoying.justice.cluster.manager.db.model.TblClusterInfo;
import com.lnjoying.justice.cluster.manager.handler.resourcesupervisor.statedict.ClusterStatusDesProvider;
import com.lnjoying.justice.commonweb.handler.operationevent.AbstractResourceSupervisor;
import com.lnjoying.justice.commonweb.process.service.OperationLogOrEventSendProcessStrategy;
import org.springframework.stereotype.Component;

/**
 * 功能描述
 *
 * @author: Robin
 * @date: 2024年01月22日 19:06
 */
@Component
public class ClusterInfoResourceSupervisor extends AbstractResourceSupervisor<TblClusterInfo>
{
    public ClusterInfoResourceSupervisor(OperationLogOrEventSendProcessStrategy operationLogOrEventSendProcessStrategy)
    {
        super("cls", "cluster", "集群", operationLogOrEventSendProcessStrategy, ClusterStatusDesProvider.INSTANCE);
    }

    @Override
    public String getResourceInstanceId(TblClusterInfo resource)
    {
        return resource.getId();
    }

    @Override
    public String getResourceInstanceName(TblClusterInfo resource)
    {
        return resource.getName();
    }
}
