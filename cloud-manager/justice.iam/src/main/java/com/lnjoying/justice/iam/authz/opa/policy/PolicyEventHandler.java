package com.lnjoying.justice.iam.authz.opa.policy;

import com.lnjoying.justice.iam.authz.opa.client.OpaClient;
import com.lnjoying.justice.iam.authz.opa.client.policy.OpaPolicyRequest;
import com.lnjoying.justice.iam.authz.opa.common.OpaProcessor;
import com.lnjoying.justice.iam.authz.opa.common.policy.DeletePolicyBundle;
import com.lnjoying.justice.iam.authz.opa.common.policy.PolicyProcessor;
import com.lnjoying.justice.iam.authz.opa.common.policy.UpdatePolicyBundle;
import com.lnjoying.justice.iam.authz.opa.topic.DisruptorEvent;
import com.lnjoying.justice.iam.authz.opa.topic.DisruptorEventHandler;
import com.lnjoying.justice.iam.authz.opa.common.policy.PolicyBundle;

import static com.lnjoying.justice.iam.authz.opa.policy.PolicyEvent.POLICY_TYPE;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/3/2 15:43
 */

public class PolicyEventHandler implements DisruptorEventHandler<PolicyEvent>
{
    private final OpaClient opaClient;

    public PolicyEventHandler(OpaClient opaClient)
    {
        this.opaClient = opaClient;
    }


    @Override
    public void doHandler(PolicyEvent event) throws Exception
    {

        Object source = event.getSource();
        if (source instanceof OpaProcessor)
        {
            OpaProcessor policyProcessor = ((OpaProcessor) source);
            policyProcessor.process(opaClient);

        }

    }

    @Override
    public boolean supports(DisruptorEvent event)
    {
        return POLICY_TYPE.equalsIgnoreCase(event.getType());
    }


}
