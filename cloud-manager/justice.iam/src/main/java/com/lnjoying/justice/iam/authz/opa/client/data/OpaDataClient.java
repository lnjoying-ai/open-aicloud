package com.lnjoying.justice.iam.authz.opa.client.data;

import com.lnjoying.justice.iam.authz.opa.client.OpaClientException;
import com.lnjoying.justice.iam.authz.opa.client.rest.OpaRestClient;
import lombok.extern.slf4j.Slf4j;

import static com.lnjoying.justice.iam.authz.opa.client.constant.OpaConstants.DATA_ENDPOINT;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/2/23 15:38
 */

@Slf4j
public class OpaDataClient implements OpaDataApi
{

    private final OpaRestClient opaRestClient;

    public OpaDataClient(OpaRestClient client)
    {
        this.opaRestClient = client;
    }

    @Override
    public void createOrOverride(OpaDataRequest request)
    {
        try
        {
            opaRestClient.putRequest(DATA_ENDPOINT + "/" + request.getPath(), request.getContent(), Void.class);
        }
        catch (Exception e)
        {
            log.error("create or override error:{}", e);
            throw new OpaClientException(e);
        }
    }
}
