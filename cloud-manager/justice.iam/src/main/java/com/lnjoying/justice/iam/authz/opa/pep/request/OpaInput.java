package com.lnjoying.justice.iam.authz.opa.pep.request;

import com.lnjoying.justice.iam.authz.opa.pep.Resource;
import com.lnjoying.justice.iam.authz.opa.pep.User;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/2/24 9:51
 */

@Data
@Builder
public class OpaInput
{
    private final User user;

    private final String action;

    private Resource resource;

    private final Map<String, String> context;
}
