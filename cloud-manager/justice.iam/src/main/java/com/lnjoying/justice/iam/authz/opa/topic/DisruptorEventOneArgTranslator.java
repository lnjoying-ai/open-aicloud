package com.lnjoying.justice.iam.authz.opa.topic;

import com.lmax.disruptor.EventTranslatorOneArg;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/3/10 19:45
 */

public class DisruptorEventOneArgTranslator implements EventTranslatorOneArg<String, Integer>
{
    @Override
    public void translateTo(String event, long sequence, Integer arg0)
    {

    }
}
