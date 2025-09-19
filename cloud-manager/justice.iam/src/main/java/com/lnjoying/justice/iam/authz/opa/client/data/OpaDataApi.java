package com.lnjoying.justice.iam.authz.opa.client.data;

/**
 * @Description:
 * <p>The Data API exposes endpoints for reading and writing documents in OPA</p>
 * <a href=https://www.openpolicyagent.org/docs/latest/rest-api/#data-api>opa data api docs</a>
 * @Author: Merak
 * @Date: 2023/2/23 15:29
 */

public interface OpaDataApi
{
    void createOrOverride(OpaDataRequest request);
}
