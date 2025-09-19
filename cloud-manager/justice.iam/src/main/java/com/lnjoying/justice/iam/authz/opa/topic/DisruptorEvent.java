package com.lnjoying.justice.iam.authz.opa.topic;

import lombok.Data;

import java.util.EventObject;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/3/1 20:25
 */
@Data
public class DisruptorEvent
{
    private String type;

    protected transient Object source;

}
