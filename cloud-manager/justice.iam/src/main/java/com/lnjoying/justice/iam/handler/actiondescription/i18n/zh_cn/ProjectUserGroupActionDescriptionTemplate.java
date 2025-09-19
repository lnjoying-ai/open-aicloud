package com.lnjoying.justice.iam.handler.actiondescription.i18n.zh_cn;

import com.lnjoying.justice.commonweb.handler.template.actiondescription.AbstractActionDescriptionTemplate;
import com.lnjoying.justice.commonweb.handler.template.constant.ActionDescriptionContextFields;
import com.lnjoying.justice.commonweb.util.CollectionUtils;
import com.lnjoying.justice.commonweb.util.JsonUtils;
import com.lnjoying.justice.schema.constant.ResourceOperationTypeConstants;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

import static com.lnjoying.justice.iam.handler.actiondescription.i18n.zh_cn.ProjectUserGroupActionDescriptionTemplate.Descriptions.ADD_USER_GROUP_TO_PROJECT;
import static com.lnjoying.justice.iam.handler.actiondescription.i18n.zh_cn.ProjectUserGroupActionDescriptionTemplate.Descriptions.DELETE_USER_GROUP_FROM_PROJECT;

/**
 * 功能描述
 *
 * @author: Robin
 * @date: 2024年02月01日 14:31
 */
public class ProjectUserGroupActionDescriptionTemplate extends AbstractActionDescriptionTemplate
{
    @Override
    public Optional<String> getDescriptionTemplate()
    {
        String userGroupIdList = getDataFromContext("groupIds");
        if (!StringUtils.hasText(userGroupIdList) || "[]" .equals(userGroupIdList))
        {
            //尝试从原始参数中提取
            String rawInputJson = getDataFromContext(ActionDescriptionContextFields.RAW_INPUT_PARAMETER_MAP);
            if (!StringUtils.hasText(rawInputJson))
            {
                return Optional.empty();
            }

            Map map = JsonUtils.fromJson(rawInputJson, Map.class);
            if (CollectionUtils.isEmpty(map))
            {
                return Optional.empty();
            }

            ArrayList<String> userIds = (ArrayList<String>) map.get("userIds");
            String userIdListStr = String.join(",", userIds);
            getContextData().put("_TMP_USER_GROUP_ID_LIST", userIdListStr);

            String operationType = getOperationType().orElse("");
            switch (operationType)
            {
                case ResourceOperationTypeConstants.RESOURCE_CREATE:
                    return Optional.of(ADD_USER_GROUP_TO_PROJECT);
                case ResourceOperationTypeConstants.RESOURCE_DELETE:
                    return Optional.of(DELETE_USER_GROUP_FROM_PROJECT);
            }
        }

        return Optional.empty();
    }

    public interface Descriptions
    {
        String ADD_USER_GROUP_TO_PROJECT = "项目 ${RESOURCE_INSTANCE_NAME} 添加用户组: ${_TMP_USER_GROUP_ID_LIST}";
        String DELETE_USER_GROUP_FROM_PROJECT = "项目 ${RESOURCE_INSTANCE_NAME} 删除用户组: ${_TMP_USER_GROUP_ID_LIST}";
    }
}
