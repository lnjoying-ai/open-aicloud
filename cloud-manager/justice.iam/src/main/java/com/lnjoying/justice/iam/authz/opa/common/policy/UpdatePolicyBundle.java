package com.lnjoying.justice.iam.authz.opa.common.policy;

import com.lnjoying.justice.iam.authz.opa.client.OpaClient;
import com.lnjoying.justice.iam.authz.opa.client.policy.OpaPolicyRequest;
import com.lnjoying.justice.iam.authz.opa.common.EventType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.lnjoying.justice.iam.authz.opa.policy.PolicyEvent.POLICY_TYPE;

/**
 * @Description: delete then add
 * @Author: Merak
 * @Date: 2023/3/6 16:10
 */

@EventType(POLICY_TYPE)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePolicyBundle implements PolicyProcessor
{
    private List<PolicyBundle> addBundleList;

    private List<String> deleteBundleList;

    @Override
    public void process(OpaClient opaClient)
    {
        List<OpaPolicyRequest> addRequestList = getAddOpaPolicyRequests();
        List<OpaPolicyRequest> deleteRequestList = getDeleteOpaPolicyRequests();
        opaClient.update(deleteRequestList,  addRequestList);
    }

    private List<OpaPolicyRequest> getDeleteOpaPolicyRequests()
    {
        List<OpaPolicyRequest> deleteRequestList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(deleteBundleList))
        {
            List<OpaPolicyRequest> opaPolicyRequests = deleteBundleList.stream().map(deletBundle ->
            {
                OpaPolicyRequest opaPolicyRequest = new OpaPolicyRequest();
                opaPolicyRequest.setId(deletBundle);
                return opaPolicyRequest;
            }).collect(Collectors.toList());
            deleteRequestList.addAll(opaPolicyRequests);
        }
        return deleteRequestList;
    }

    private List<OpaPolicyRequest> getAddOpaPolicyRequests()
    {
        List<OpaPolicyRequest> addRequestList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(addBundleList))
        {
            List<OpaPolicyRequest> opaPolicyRequests = addBundleList.stream().map(addingBundle ->
            {
                OpaPolicyRequest opaPolicyRequest = new OpaPolicyRequest();
                opaPolicyRequest.setId(addingBundle.getOpaPolicyId());
                opaPolicyRequest.setContent(addingBundle.getRego());
                return opaPolicyRequest;
            }).collect(Collectors.toList());
            addRequestList.addAll(opaPolicyRequests);
        }
        return addRequestList;
    }
}
