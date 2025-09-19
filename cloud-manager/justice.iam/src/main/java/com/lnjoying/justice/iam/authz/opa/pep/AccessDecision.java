package com.lnjoying.justice.iam.authz.opa.pep;

import lombok.Data;

import static java.lang.Boolean.TRUE;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/2/24 10:15
 */

@Data
public class AccessDecision
{
    private Boolean allow;

    private String reason;

    public boolean isAllow()
    {
        return TRUE.equals(allow);
    }
}
