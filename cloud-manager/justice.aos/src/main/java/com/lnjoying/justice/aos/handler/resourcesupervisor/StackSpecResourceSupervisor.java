package com.lnjoying.justice.aos.handler.resourcesupervisor;

import com.lnjoying.justice.aos.db.model.TblStackSpecInfo;
import com.lnjoying.justice.aos.handler.resourcesupervisor.statedict.StackStateDesProvider;
import com.lnjoying.justice.commonweb.handler.operationevent.AbstractResourceSupervisor;
import com.lnjoying.justice.commonweb.process.service.OperationLogOrEventSendProcessStrategy;
import org.springframework.stereotype.Component;

/**
 * 功能描述
 *
 * @author: Robin
 * @date: 2023年10月31日 10:36
 */
@Component
public class StackSpecResourceSupervisor extends AbstractResourceSupervisor<TblStackSpecInfo>
{
    public StackSpecResourceSupervisor(OperationLogOrEventSendProcessStrategy operationLogOrEventSendProcessStrategy)
    {
        super("aos", "stack", "应用规格", operationLogOrEventSendProcessStrategy, StackStateDesProvider.INSTANCE);
    }

    @Override
    public String getResourceInstanceId(TblStackSpecInfo resource)
    {
        return resource.getSpecId();
    }

    @Override
    public String getResourceInstanceName(TblStackSpecInfo resource) {
        return resource.getSpecName();
    }

    @Override
    public String getActualResourceName()
    {
        return "stack spec";
    }
}
