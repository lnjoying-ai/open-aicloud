package com.lnjoying.justice.iam.authz.opa.client;

import lombok.Data;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/2/23 17:18
 */

@Data
public class OpaClientConfiguration
{
    private  String endPointUrl;

    private  String allowPath;
}
