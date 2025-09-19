package com.lnjoying.justice.iam.authz.opa.client.policy;

import java.util.List;

/**
 * @Description:
 * <p>The Policy API exposes CRUD endpoints for managing policy modules. Policy modules can be added, removed, and modified at any time</p>
 * <a href=https://www.openpolicyagent.org/docs/latest/rest-api/#policy-api> opa policy api docs</a>
 * @Author: Merak
 * @Date: 2023/2/23 15:34
 */

public interface OpaPolicyApi
{

    void createOrUpdate(OpaPolicyRequest request);

    OpaPolicyResponse list();

    List<String> listPolicyIds();

    void delete(String policyId);

    void update(List<OpaPolicyRequest> deleteRequestList, List<OpaPolicyRequest> addRequestList);
}
