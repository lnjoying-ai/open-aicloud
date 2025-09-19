package com.lnjoying.justice.cis.handler.resourcesupervisor;

import com.lnjoying.justice.cis.db.model.TblContainerInstInfo;
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
 * @date: 2023年10月30日 15:37
 */
@Component
@Slf4j
public class CisContainerInstResourceSupervisor extends AbstractResourceSupervisor<TblContainerInstInfo>
{
    @Autowired
    public CisContainerInstResourceSupervisor(OperationLogOrEventSendProcessStrategy operationLogOrEventSendProcessStrategy)
    {
        super("cis", "container", "容器实例", operationLogOrEventSendProcessStrategy, InstanceStateDesProvider.INSTANCE);
    }

    @Override
    public String getResourceInstanceId(TblContainerInstInfo resource)
    {
        return resource.getInstId();
    }

    @Override
    public String getResourceInstanceName(TblContainerInstInfo resource) {
        return resource.getContainerName();
    }

    @Override
    public String getActualResourceName()
    {
        return "container instance";
    }
}
