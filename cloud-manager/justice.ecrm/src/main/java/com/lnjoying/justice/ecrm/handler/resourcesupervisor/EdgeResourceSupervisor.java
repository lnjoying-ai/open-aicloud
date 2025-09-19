package com.lnjoying.justice.ecrm.handler.resourcesupervisor;

import com.lnjoying.justice.commonweb.handler.operationevent.AbstractResourceSupervisor;
import com.lnjoying.justice.commonweb.process.service.OperationLogOrEventSendProcessStrategy;
import com.lnjoying.justice.ecrm.db.model.TblEdgeComputeInfo;
import com.lnjoying.justice.ecrm.handler.resourcesupervisor.statedict.EdgeResourceStateDesProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 功能描述
 *
 * @author: Robin
 * @date: 2023年10月27日 14:27
 */
@Component
@Slf4j
public class EdgeResourceSupervisor extends AbstractResourceSupervisor<TblEdgeComputeInfo>
{
    @Autowired
    public EdgeResourceSupervisor(OperationLogOrEventSendProcessStrategy operationLogOrEventSendProcessStrategy)
    {
        super("ecrm", "edge", "节点", operationLogOrEventSendProcessStrategy, EdgeResourceStateDesProvider.INSTANCE);
    }

    @Override
    public String getResourceInstanceId(TblEdgeComputeInfo resource)
    {
        return resource.getNodeId();
    }

    @Override
    public String getResourceInstanceName(TblEdgeComputeInfo resource) {
        return resource.getNodeName();
    }
}
