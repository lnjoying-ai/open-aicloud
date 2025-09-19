package com.lnjoying.justice.iam.domain.model;

import java.util.List;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/4/18 15:42
 */

public interface PolicyParser
{
    List<String> extractConditionRuleNames(String documentText);

    String rewriteDoc(String ruleDoc, boolean system, String bpId);
}
