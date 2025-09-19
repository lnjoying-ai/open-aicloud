package com.lnjoying.justice.aos.handler.resourcesupervisor;

import com.lnjoying.justice.aos.db.model.TblStackInfo;
import com.lnjoying.justice.aos.handler.resourcesupervisor.statedict.StackStateDesProvider;
import com.lnjoying.justice.commonweb.handler.operationevent.AbstractResourceSupervisor;
import com.lnjoying.justice.commonweb.process.service.OperationLogOrEventSendProcessStrategy;
import org.springframework.stereotype.Component;

/**
 * 功能描述
 *
 * @author: Robin
 * @date: 2023年10月31日 10:52
 */
@Component
public class StackInfoResourceSupervisor extends AbstractResourceSupervisor<TblStackInfo>
{
    public StackInfoResourceSupervisor(OperationLogOrEventSendProcessStrategy operationLogOrEventSendProcessStrategy)
    {
        super("aos", "stack", "应用信息", operationLogOrEventSendProcessStrategy, StackStateDesProvider.INSTANCE);
    }

    @Override
    public String getResourceInstanceId(TblStackInfo resource)
    {
        return resource.getStackId();
    }

    @Override
    public String getResourceInstanceName(TblStackInfo resource) {
        return resource.getName();
    }

    @Override
    public String getActualResourceName()
    {
        return "stack info";
    }
}
