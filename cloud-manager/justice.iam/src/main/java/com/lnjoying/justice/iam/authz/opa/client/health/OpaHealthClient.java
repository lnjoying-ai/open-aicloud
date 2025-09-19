package com.lnjoying.justice.iam.authz.opa.client.health;

import com.lnjoying.justice.iam.authz.opa.client.rest.OpaRestClient;
import org.springframework.http.ResponseEntity;

import static com.lnjoying.justice.iam.authz.opa.client.constant.OpaConstants.HEALTH_ENDPOINT;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/4/6 10:23
 */

public class OpaHealthClient implements OpaHealthApi
{
    private final OpaRestClient opaRestClient;

    public OpaHealthClient(OpaRestClient opaRestClient)
    {
        this.opaRestClient = opaRestClient;
    }

    @Override
    public OpaHealthResponse health()
    {
        ResponseEntity<OpaHealthResponse> responseEntity = opaRestClient.get(HEALTH_ENDPOINT, OpaHealthResponse.class);
        return responseEntity.getBody();
    }
}
