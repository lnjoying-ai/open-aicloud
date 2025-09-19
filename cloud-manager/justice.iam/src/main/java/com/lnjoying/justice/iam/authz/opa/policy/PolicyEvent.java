package com.lnjoying.justice.iam.authz.opa.policy;

import com.lnjoying.justice.iam.authz.opa.topic.DisruptorEvent;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/3/2 15:09
 */

public class PolicyEvent extends DisruptorEvent
{
    public static final String POLICY_TYPE = "policy";

    @Override
    public String getType()
    {
        return POLICY_TYPE;
    }
}
