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
public class OpaUser
{

    private static final String ATTRIBUTES_KEY = "attributes";

    private static final String ROLE_ASSIGNMENTS_KEY = "roleAssignments";

    @JsonIgnore
    private String userKey;

    private UserAttribute attributes;

    /**
     * project id , list role
     */
    private Map<String, List<String>> roleAssignments;

    @Data
    public static final class UserAttribute
    {
        private String email;

        private String id;
    }

    public String build()
    {
        Map<String, Object> userInfo = new HashMap<>();
        if (Objects.nonNull(this.attributes))
        {
            userInfo.put(ATTRIBUTES_KEY, this.attributes);
        }

        if (!CollectionUtils.isEmpty(this.roleAssignments))
        {
            userInfo.put(ROLE_ASSIGNMENTS_KEY, this.roleAssignments);
        }
       return JacksonUtils.objToStrDefault(userInfo);
    }
}


