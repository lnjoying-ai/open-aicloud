package com.lnjoying.justice.iam.authz.opa.client.query;

import com.lnjoying.justice.iam.authz.opa.client.OpaClientException;
import com.lnjoying.justice.iam.authz.opa.client.rest.OpaRestClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.lnjoying.justice.iam.authz.opa.client.constant.OpaConstants.DATA_ENDPOINT;
import static com.lnjoying.justice.iam.authz.opa.client.constant.OpaConstants.QUERY_ENDPOINT;
import static com.lnjoying.justice.iam.authz.opa.client.rest.OpaRestClient.checkResponse;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/2/23 16:56
 */

@Slf4j
public class OpaQueryClient implements OpaQueryApi
{
    private final OpaRestClient opaRestClient;

    public OpaQueryClient(OpaRestClient client)
    {
        this.opaRestClient = client;
    }

    @Override
    public <R> R query(OpaQueryRequest request, Class<R> responseType)
    {

        try
        {
            ResponseEntity<R> responseEntity = opaRestClient.postRequest(DATA_ENDPOINT + request.getPath(), request.buildInput(), responseType);
            checkResponse(responseEntity);
            R body = responseEntity.getBody();
            return body;
        }
        catch (Exception e)
        {
            log.error("query error:{}", e);
            throw new OpaClientException(e);
        }

    }

    @Override
    public <R> R adHocQuery(OpaQueryRequest request, Class<R> responseType)
    {

        try
        {
            ResponseEntity<R> responseEntity = opaRestClient.postRequest(QUERY_ENDPOINT, request, responseType);
            checkResponse(responseEntity);

            return responseEntity.getBody();
        }
        catch (Exception e)
        {
            log.error("query error:{}", e);
            throw new OpaClientException(e);
        }
    }


}
