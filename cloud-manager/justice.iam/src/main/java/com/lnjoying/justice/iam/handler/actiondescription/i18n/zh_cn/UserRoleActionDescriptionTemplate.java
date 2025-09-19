package com.lnjoying.justice.iam.handler.actiondescription.i18n.zh_cn;

import com.lnjoying.justice.commonweb.handler.template.actiondescription.AbstractActionDescriptionTemplate;
import com.lnjoying.justice.commonweb.handler.template.constant.ActionDescriptionContextFields;
import com.lnjoying.justice.commonweb.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.lnjoying.justice.iam.handler.actiondescription.i18n.zh_cn.UserRoleActionDescriptionTemplate.Descriptions.ADD_USER_ROLE;

/**
 * 功能描述
 *
 * @author: Robin
 * @date: 2024年02月01日 14:35
 */
public class UserRoleActionDescriptionTemplate extends AbstractActionDescriptionTemplate
{
    @Override
    public Optional<String> getDescriptionTemplate()
    {
        Object requestBodyObj = getContextData().get(ActionDescriptionContextFields.RAW_INPUT_PARAMETER_MAP);
        if (!(requestBodyObj instanceof List))
        {
            return Optional.empty();
        }

        List<Map> inputRoleEntryList = (List<Map>) requestBodyObj;
        if (CollectionUtils.isEmpty(inputRoleEntryList))
        {
            return Optional.empty();
        }

        List<String> authorizedRoleList = new ArrayList<>();

        for (Map authorizedRoleEntry : inputRoleEntryList)
        {
            String role = Optional.ofNullable(authorizedRoleEntry.get("role")).map(x -> x.toString()).orElse("");
            if (!StringUtils.hasText(role))
            {
                continue;
            }
            authorizedRoleList.add(role);
        }

        if (CollectionUtils.isEmpty(authorizedRoleList))
        {
            return Optional.empty();
        }


        getContextData().put("_TMP_USER_ROLE_LIST", String.join(",", authorizedRoleList.toArray(new String[authorizedRoleList.size()])));

        return Optional.of(ADD_USER_ROLE);
    }

    public interface Descriptions
    {
        String ADD_USER_ROLE = "用户 ${USER_NAME} 授予角色: ${_TMP_USER_ROLE_LIST}";
    }
}
