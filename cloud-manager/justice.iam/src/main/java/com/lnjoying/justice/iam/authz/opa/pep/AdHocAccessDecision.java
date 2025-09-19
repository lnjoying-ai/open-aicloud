package com.lnjoying.justice.iam.authz.opa.pep;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/3/28 11:20
 */

@Data
public class AdHocAccessDecision
{
    private List<Map<String, List<String>>> result;
}
