package com.lnjoying.justice.cluster.agent.common;

import lombok.Data;

/**
 * @Description:
 * @Author: Regulus
 * @Date: 5/25/22 10:29 AM
 */
@Data
public class CmdArgsInfo
{
    private String region;
    private String gateway;
    private String token;
    private String workerType;
}
