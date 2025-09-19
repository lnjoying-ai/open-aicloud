package com.lnjoying.justice.iam.authz.opa.common;

import com.lnjoying.justice.iam.authz.opa.client.OpaClient;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/3/17 11:10
 */

public interface OpaProcessor
{
    void process(OpaClient opaClient);
}
