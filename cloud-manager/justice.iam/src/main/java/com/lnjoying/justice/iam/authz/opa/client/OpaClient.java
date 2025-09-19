package com.lnjoying.justice.iam.authz.opa.client;

import com.lnjoying.justice.iam.authz.opa.client.data.OpaDataApi;
import com.lnjoying.justice.iam.authz.opa.client.data.OpaDataClient;
import com.lnjoying.justice.iam.authz.opa.client.data.OpaDataRequest;
import com.lnjoying.justice.iam.authz.opa.client.health.OpaHealthApi;
import com.lnjoying.justice.iam.authz.opa.client.health.OpaHealthClient;
import com.lnjoying.justice.iam.authz.opa.client.health.OpaHealthResponse;
import com.lnjoying.justice.iam.authz.opa.client.policy.OpaPolicyApi;
import com.lnjoying.justice.iam.authz.opa.client.policy.OpaPolicyClient;
import com.lnjoying.justice.iam.authz.opa.client.policy.OpaPolicyRequest;
import com.lnjoying.justice.iam.authz.opa.client.policy.OpaPolicyResponse;
import com.lnjoying.justice.iam.authz.opa.client.query.OpaQueryApi;
import com.lnjoying.justice.iam.authz.opa.client.query.OpaQueryClient;
import com.lnjoying.justice.iam.authz.opa.client.query.OpaQueryRequest;
import com.lnjoying.justice.iam.authz.opa.client.rest.ObjectMapperFactory;
import com.lnjoying.justice.iam.authz.opa.client.rest.OpaRestClient;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/2/23 15:55
 */

public class OpaClient implements OpaQueryApi, OpaDataApi, OpaPolicyApi, OpaHealthApi
{
    private final OpaDataApi opaDataApi;

    private final OpaPolicyApi opaPolicyApi;

    private final OpaQueryApi opaQueryApi;

    private final OpaHealthApi opaHealthApi;

    private OpaClient(OpaDataApi opaDataApi, OpaPolicyApi opaPolicyApi, OpaQueryApi opaQueryApi, OpaHealthApi opaHealthApi)
    {
        this.opaDataApi = opaDataApi;
        this.opaPolicyApi = opaPolicyApi;
        this.opaQueryApi = opaQueryApi;
        this.opaHealthApi = opaHealthApi;
    }

    public static Builder builder() { return new Builder();}

    @Override
    public void createOrOverride(OpaDataRequest request)
    {
        this.opaDataApi.createOrOverride(request);
    }

    @Override
    public void createOrUpdate(OpaPolicyRequest request)
    {
        this.opaPolicyApi.createOrUpdate(request);
    }

    @Override
    public OpaPolicyResponse list()
    {
        return this.opaPolicyApi.list();
    }

    @Override
    public List<String> listPolicyIds()
    {
        return this.opaPolicyApi.listPolicyIds();
    }

    @Override
    public void delete(String policyId)
    {
        this.opaPolicyApi.delete(policyId);
    }

    @Override
    public void update(List<OpaPolicyRequest> deleteRequestList, List<OpaPolicyRequest> addRequestList)
    {
        this.opaPolicyApi.update(deleteRequestList, addRequestList);
    }

    @Override
    public <R> R query(OpaQueryRequest request, Class<R> responseType)
    {
        return this.opaQueryApi.query(request, responseType);
    }

    @Override
    public <R> R adHocQuery(OpaQueryRequest request, Class<R> responseType)
    {
        return this.opaQueryApi.adHocQuery(request, responseType);
    }

    @Override
    public OpaHealthResponse health()
    {
        return this.opaHealthApi.health();
    }

    public static final class Builder
    {
        private OpaClientConfiguration opaConfiguration = new OpaClientConfiguration();

        private RestTemplate restTemplate;

        public Builder endPointUrl(String url)
        {
            opaConfiguration.setEndPointUrl(url);
            return this;
        }

        public Builder allowPath(String allowPath)
        {
            opaConfiguration.setAllowPath(allowPath);
            return this;
        }

        public Builder restTemplate(RestTemplate restTemplate)
        {
            this.restTemplate = restTemplate;
            return this;
        }


        public OpaClient build()
        {
            OpaRestClient opaRestClient = new OpaRestClient(opaConfiguration, restTemplate, ObjectMapperFactory.getInstance().create());
            return new OpaClient(new OpaDataClient(opaRestClient), new OpaPolicyClient(opaRestClient), new OpaQueryClient(opaRestClient), new OpaHealthClient(opaRestClient));
        }
    }

    public OpaDataApi getOpaDataApi()
    {
        return opaDataApi;
    }

    public OpaPolicyApi getOpaPolicyApi()
    {
        return opaPolicyApi;
    }

    public OpaQueryApi getOpaQueryApi()
    {
        return opaQueryApi;
    }

    public OpaHealthApi getOpaHealthApi()
    {
        return opaHealthApi;
    }
}
