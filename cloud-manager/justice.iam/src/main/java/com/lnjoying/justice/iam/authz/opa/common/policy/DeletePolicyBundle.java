package com.lnjoying.justice.iam.authz.opa.common.policy;

import com.lnjoying.justice.iam.authz.opa.client.OpaClient;
import com.lnjoying.justice.iam.authz.opa.common.EventType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.lnjoying.justice.iam.authz.opa.policy.PolicyEvent.POLICY_TYPE;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/3/6 16:10
 */

@EventType(POLICY_TYPE)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeletePolicyBundle implements PolicyProcessor
{

    private String opaPolicyId;

    @Override
    public void process(OpaClient opaClient)
    {
        opaClient.delete(this.getOpaPolicyId());
    }
}
