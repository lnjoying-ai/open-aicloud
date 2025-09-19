package com.lnjoying.justice.iam.authz.opa.topic;

import com.lnjoying.justice.iam.authz.opa.data.DataEvent;
import com.lnjoying.justice.iam.authz.opa.policy.PolicyEvent;
import org.apache.servicecomb.registry.RegistrationManager;
import org.springframework.core.convert.converter.Converter;

import static com.lnjoying.justice.iam.authz.opa.data.DataEvent.DATA_TYPE;
import static com.lnjoying.justice.iam.authz.opa.policy.PolicyEvent.POLICY_TYPE;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/3/10 20:24
 */

public class EventConverter
{
    private EventConverter(){}

    public static EventConverter INSTANCE = new EventConverter();

    public DisruptorEvent convert(DisruptorEvent from)
    {
        if (DATA_TYPE.equalsIgnoreCase(from.getType()))
        {
            DataEvent dataEvent = new DataEvent();
            dataEvent.setType(from.getType());
            dataEvent.setSource(from.getSource());
            return dataEvent;
        }

        else if (POLICY_TYPE.equalsIgnoreCase(from.getType()))
        {
            PolicyEvent policyEvent = new PolicyEvent();
            policyEvent.setType(from.getType());
            policyEvent.setSource(from.getSource());
            return policyEvent;
        }

        throw  new DisruptorException("unsupported type conversion");
    }


}
