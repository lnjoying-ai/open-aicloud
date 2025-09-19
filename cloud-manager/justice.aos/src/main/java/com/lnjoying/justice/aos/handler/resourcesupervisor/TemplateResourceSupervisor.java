package com.lnjoying.justice.aos.handler.resourcesupervisor;

import com.lnjoying.justice.aos.db.model.TblStackTemplateInfo;
import com.lnjoying.justice.aos.handler.resourcesupervisor.statedict.RecordStatusDesProvider;
import com.lnjoying.justice.aos.service.CombRpcService;
import com.lnjoying.justice.commonweb.handler.operationevent.AbstractResourceSupervisor;
import com.lnjoying.justice.commonweb.process.service.OperationLogOrEventSendProcessStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 功能描述
 *
 * @author: Robin
 * @date: 2023年10月31日 15:19
 */
@Component
public class TemplateResourceSupervisor extends AbstractResourceSupervisor<TblStackTemplateInfo>
{
    @Autowired
    private CombRpcService combRpcService;

    public TemplateResourceSupervisor(OperationLogOrEventSendProcessStrategy operationLogOrEventSendProcessStrategy)
    {
        super("aos", "template", "应用模板", operationLogOrEventSendProcessStrategy, RecordStatusDesProvider.INSTANCE);
    }

    @Override
    public String getResourceInstanceId(TblStackTemplateInfo resource)
    {
        return resource.getTemplateId();
    }

    @Override
    public String getResourceInstanceName(TblStackTemplateInfo resource) {
        return "";
    }
}
