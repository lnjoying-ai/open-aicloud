package com.lnjoying.justice.iam.authz.opa.common.policy;

import com.lnjoying.justice.iam.authz.opa.client.OpaClient;
import com.lnjoying.justice.iam.authz.opa.client.policy.OpaPolicyRequest;
import com.lnjoying.justice.iam.authz.opa.common.EventType;
import lombok.Data;

import static com.lnjoying.justice.iam.authz.opa.policy.PolicyEvent.POLICY_TYPE;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/3/6 16:10
 */

@EventType(POLICY_TYPE)
@Data
public class PolicyBundle implements PolicyProcessor
{

    private String opaPolicyId;

    private String packageName;

    /**
     * rego document
     */
    private String rego;


    @Override
    public void process(OpaClient opaClient)
    {
        OpaPolicyRequest opaPolicyRequest = new OpaPolicyRequest();
        opaPolicyRequest.setId(this.getOpaPolicyId());
        opaPolicyRequest.setContent(this.getRego());
        opaClient.createOrUpdate(opaPolicyRequest);
    }
}
