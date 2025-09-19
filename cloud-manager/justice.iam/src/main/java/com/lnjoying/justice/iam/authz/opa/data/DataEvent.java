package com.lnjoying.justice.iam.authz.opa.data;

import com.lnjoying.justice.iam.authz.opa.topic.DisruptorEvent;
import lombok.Data;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/3/2 15:08
 */

@Data
public class DataEvent extends DisruptorEvent
{
    public static final String DATA_TYPE = "data";

    @Override
    public String getType()
    {
       return DATA_TYPE;
    }
}
