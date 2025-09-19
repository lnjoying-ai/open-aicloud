package com.lnjoying.justice.iam.authz.opa.pep.request;

import lombok.Builder;
import lombok.Data;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/3/28 11:34
 */

@Data
@Builder
public class OpaAdHocInput
{
    private String query;

    private OpaInput opaInput;
}
