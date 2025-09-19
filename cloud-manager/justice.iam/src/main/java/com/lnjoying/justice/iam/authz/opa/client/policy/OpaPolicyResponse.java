package com.lnjoying.justice.iam.authz.opa.client.policy;

import lombok.Data;

import java.util.List;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/3/7 11:05
 */

@Data
public class OpaPolicyResponse
{
    private List<PolicyV1> result;

    @Data
    public static final class PolicyV1
    {
        private String id;

        private String raw;

       // private String ast;
    }
}
