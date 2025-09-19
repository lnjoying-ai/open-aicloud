package com.lnjoying.justice.cis.handler.resourcesupervisor;

import com.lnjoying.justice.cis.db.model.TblContainerSpecInfo;
import com.lnjoying.justice.cis.handler.resourcesupervisor.statedict.InstanceStateDesProvider;
import com.lnjoying.justice.commonweb.handler.operationevent.AbstractResourceSupervisor;
import com.lnjoying.justice.commonweb.process.service.OperationLogOrEventSendProcessStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 功能描述
 *
 * @author: Robin
 * @date: 2023年10月30日 15:20
 */
@Component
@Slf4j
public class CisContainerSpecResourceSupervisor extends AbstractResourceSupervisor<TblContainerSpecInfo>
{
    @Autowired
    public CisContainerSpecResourceSupervisor(OperationLogOrEventSendProcessStrategy operationLogOrEventSendProcessStrategy)
    {
        super("cis", "container", "容器规格", operationLogOrEventSendProcessStrategy, InstanceStateDesProvider.INSTANCE);
    }

    @Override
    public String getResourceInstanceId(TblContainerSpecInfo resource)
    {
        return resource.getSpecId();
    }

    @Override
    public String getResourceInstanceName(TblContainerSpecInfo resource) {
        return resource.getSpecName();
    }


    @Override
    public String getActualResourceName()
    {
        return "container spec";
    }
}
