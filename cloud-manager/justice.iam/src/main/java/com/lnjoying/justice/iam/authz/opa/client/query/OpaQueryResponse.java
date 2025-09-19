package com.lnjoying.justice.iam.authz.opa.client.query;

import lombok.Data;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/4/12 14:30
 */

@Data
public class OpaQueryResponse
{
    private Result result;

    @Data
    public static final class Result
    {
       private boolean allow;

       private Object debug;
    }
}
