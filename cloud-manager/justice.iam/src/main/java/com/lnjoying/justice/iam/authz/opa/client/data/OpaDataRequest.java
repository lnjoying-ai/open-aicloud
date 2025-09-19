package com.lnjoying.justice.iam.authz.opa.client.data;

import lombok.Data;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/2/23 15:28
 */

@Data
public class OpaDataRequest
{
    private String path;

    private String content;
}
