package com.lnjoying.justice.iam.authz.opa.client.constant;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/2/23 16:43
 */

public class OpaConstants
{
    public static final String DATA_ENDPOINT = "/v1/data/";

    public static final String QUERY_ENDPOINT = "/v1/query";

    public static final String POLICY_ENDPOINT = "/v1/policies/";

    public static final String HEALTH_ENDPOINT = "/health";

    public static final String POLICY_DETAIL_ENDPOINT = POLICY_ENDPOINT + "{policy_id}";
}
