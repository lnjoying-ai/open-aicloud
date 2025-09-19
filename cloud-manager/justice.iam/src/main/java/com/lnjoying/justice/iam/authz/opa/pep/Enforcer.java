package com.lnjoying.justice.iam.authz.opa.pep;

import com.lnjoying.justice.iam.authz.opa.pep.request.Context;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/2/24 10:03
 */

public interface Enforcer
{
    AccessDecision check(User user, String action, Resource resource, Context context) throws PepException;

    AccessDecision check(User user, String action, Resource resource) throws PepException;

    AdHocAccessDecision query(User user, Resource resource);
}
