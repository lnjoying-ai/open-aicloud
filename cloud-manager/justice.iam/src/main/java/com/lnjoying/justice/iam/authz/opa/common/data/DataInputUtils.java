package com.lnjoying.justice.iam.authz.opa.common.data;

import org.apache.commons.lang.StringUtils;

import java.util.Objects;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/3/17 15:24
 */

public class DataInputUtils
{
    private static final String USER_PREFIX = "users/";

    private static final String CONDITION_SET_USER_RULES_PREFIX = "condition_set_user_rules/";

    private static final String CONDITION_SET_ROLE_RULES_PREFIX = "condition_set_role_rules/";

    public static DataBundle parseInput(OpaUser opaUser)
    {
        if (Objects.nonNull(opaUser))
        {
            String userData = opaUser.build();
            if (StringUtils.isBlank(userData))
            {
                return null;
            }
            DataBundle dataBundle = new DataBundle();
            dataBundle.setOpaDataId(USER_PREFIX + opaUser.getUserKey());
            dataBundle.setData(userData);
            return dataBundle;
        }

        return null;
    }

    public static DataBundle parseInput(OpaRule opaRule)
    {
        if (Objects.nonNull(opaRule))
        {
            String ruleData = opaRule.build();
            if (StringUtils.isBlank(ruleData))
            {
                return null;
            }
            DataBundle dataBundle = new DataBundle();
            if (OpaRule.RuleType.USER.equals(opaRule.getType()))
            {
                dataBundle.setOpaDataId(CONDITION_SET_USER_RULES_PREFIX + opaRule.getKey() + "/" +opaRule.getProjectId() + "/"+ opaRule.getPolicyId());
            }
            else if (OpaRule.RuleType.ROLE.equals(opaRule.getType()))
            {
                dataBundle.setOpaDataId(CONDITION_SET_ROLE_RULES_PREFIX + opaRule.getKey()+ "/" + opaRule.getPolicyId());
            }

            dataBundle.setData(ruleData);
            return dataBundle;
        }

        return null;
    }
}
