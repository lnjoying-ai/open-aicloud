package com.lnjoying.justice.iam.authz.opa.client.policy;

import com.google.common.collect.Lists;
import com.lnjoying.justice.iam.authz.opa.client.OpaClientException;
import com.lnjoying.justice.iam.authz.opa.client.rest.OpaRestClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.lnjoying.justice.iam.authz.opa.client.constant.OpaConstants.POLICY_DETAIL_ENDPOINT;
import static com.lnjoying.justice.iam.authz.opa.client.constant.OpaConstants.POLICY_ENDPOINT;
import static com.lnjoying.justice.iam.authz.opa.client.rest.OpaRestClient.checkResponse;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/2/23 16:35
 */

@Slf4j
public class OpaPolicyClient implements OpaPolicyApi
{


    private final OpaRestClient opaRestClient;

    public OpaPolicyClient(OpaRestClient client)
    {
        this.opaRestClient = client;
    }

    @Override
    public void createOrUpdate(OpaPolicyRequest request)
    {
        try
        {
            opaRestClient.putRequest(POLICY_ENDPOINT + request.getId(), request.getContent(), Void.class);
        }
        catch (Exception e)
        {
            log.error("create or update error:{}", e);
            throw new OpaClientException(e);
        }
    }

    @Override
    public OpaPolicyResponse list()
    {
        try
        {
            ResponseEntity<OpaPolicyResponse> responseEntity = opaRestClient.get(POLICY_ENDPOINT, OpaPolicyResponse.class);
            checkResponse(responseEntity);
            return responseEntity.getBody();
        }
        catch (Exception e)
        {
            log.error("query policy list error:{}", e);
            throw new OpaClientException(e);
        }
    }

    // todo check more params to filter data
    @Override
    public List<String> listPolicyIds()
    {
        OpaPolicyResponse opaPolicyResponse = list();
        if (Objects.nonNull(opaPolicyResponse))
        {
            return opaPolicyResponse.getResult().stream().map(policyV1 -> policyV1.getId()).collect(Collectors.toList());
        }

        return Collections.EMPTY_LIST;
    }

    @Override
    public void delete(String policyId)
    {
        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("policy_id", policyId);
        opaRestClient.delete(POLICY_DETAIL_ENDPOINT, uriVariables);
    }

    @Override
    public void update(List<OpaPolicyRequest> deleteRequestList, List<OpaPolicyRequest> addRequestList)
    {
        // todo add more processing after failure
        delete(deleteRequestList);
        add(addRequestList);

    }

    private void add(List<OpaPolicyRequest> addRequestList)
    {
        AtomicInteger AddFailNums = new AtomicInteger();
        if (!CollectionUtils.isEmpty(addRequestList))
        {
            addRequestList.forEach(opaPolicyRequest ->
            {
                try
                {
                    createOrUpdate(opaPolicyRequest);
                }
                catch (Exception e)
                {
                    AddFailNums.getAndIncrement();
                    log.error("add policy failed:{}", e);
                }

            });
        }

        int addFails = AddFailNums.get();
        if (addFails > 0)
        {
            log.error("add policy failed. The num of failures is:{}", addFails);
        }
    }

    private void delete(List<OpaPolicyRequest> deleteRequestList)
    {
        AtomicInteger deleteFailNums = new AtomicInteger();
        if (!CollectionUtils.isEmpty(deleteRequestList))
        {
            deleteRequestList.forEach(opaPolicyRequest ->
            {
                try
                {
                    delete(opaPolicyRequest.getId());
                }
                catch (Exception e)
                {
                    deleteFailNums.getAndIncrement();
                    log.error("delete policy failed:{}", e);
                }

            });
        }

        int deleteFails = deleteFailNums.get();
        if (deleteFails > 0)
        {
            log.error("delete policy failed. The num of failures is:{}", deleteFails);
        }
    }
}
