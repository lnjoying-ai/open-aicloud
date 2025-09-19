package com.lnjoying.justice.iam.authz.opa.common.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lnjoying.justice.commonweb.util.JacksonUtils;
import com.lnjoying.justice.iam.authz.opa.common.EventType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.*;

import static com.lnjoying.justice.iam.authz.opa.data.DataEvent.DATA_TYPE;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/3/17 11:13
 */

@EventType(DATA_TYPE)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OpaRule
{

    private static final String RULES_KEY = "rules";

    @JsonIgnore
    private String Key;

    private String projectId;

    private String policyId;

    private List<String> rules;

    private RuleType type;

    public String build()
    {
        Map<String, Object> ruleMap = new HashMap<>();
        ruleMap.put(RULES_KEY, this.rules);
        return JacksonUtils.objToStrDefault(ruleMap);
    }

    public enum RuleType
    {
        USER(1),

        GROUP(2),

        ROLE(3);

        private final int value;

        RuleType(int value)
        {
            this.value = value;
        }

        public int value()
        {
            return this.value;
        }
    }
}


