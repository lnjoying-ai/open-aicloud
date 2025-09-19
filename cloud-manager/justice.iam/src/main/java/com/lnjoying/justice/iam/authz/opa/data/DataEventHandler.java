package com.lnjoying.justice.iam.authz.opa.data;

import com.lnjoying.justice.iam.authz.opa.client.OpaClient;
import com.lnjoying.justice.iam.authz.opa.client.policy.OpaPolicyRequest;
import com.lnjoying.justice.iam.authz.opa.common.OpaProcessor;
import com.lnjoying.justice.iam.authz.opa.common.policy.PolicyProcessor;
import com.lnjoying.justice.iam.authz.opa.topic.DisruptorEvent;
import com.lnjoying.justice.iam.authz.opa.topic.DisruptorEventHandler;

import static com.lnjoying.justice.iam.authz.opa.data.DataEvent.DATA_TYPE;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/3/2 15:41
 */

public class DataEventHandler implements DisruptorEventHandler<DataEvent>
{
    private final OpaClient opaClient;

    public DataEventHandler(OpaClient opaClient)
    {
        this.opaClient = opaClient;
    }

    @Override
    public void doHandler(DataEvent event) throws Exception
    {
        Object source = event.getSource();
        if (source instanceof OpaProcessor)
        {
            OpaProcessor dataProcessor = ((OpaProcessor) source);
            dataProcessor.process(opaClient);

        }
    }

    @Override
    public boolean supports(DisruptorEvent event)
    {
        return DATA_TYPE.equalsIgnoreCase(event.getType());
    }
}
