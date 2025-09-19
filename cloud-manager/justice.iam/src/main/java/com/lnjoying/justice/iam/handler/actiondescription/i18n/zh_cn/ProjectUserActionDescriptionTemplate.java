package com.lnjoying.justice.iam.handler.actiondescription.i18n.zh_cn;

import com.fasterxml.jackson.core.type.TypeReference;
import com.lnjoying.justice.commonweb.handler.template.actiondescription.AbstractActionDescriptionTemplate;
import com.lnjoying.justice.commonweb.handler.template.constant.ActionDescriptionContextFields;
import com.lnjoying.justice.commonweb.util.CollectionUtils;
import com.lnjoying.justice.commonweb.util.JsonUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

import static com.lnjoying.justice.iam.handler.actiondescription.i18n.zh_cn.ProjectUserActionDescriptionTemplate.Descriptions.ADD_USER_TO_PROJECT;
import static com.lnjoying.justice.iam.handler.actiondescription.i18n.zh_cn.ProjectUserActionDescriptionTemplate.Descriptions.DELETE_USER_FROM_PROJECT;

import com.lnjoying.justice.schema.constant.ResourceOperationTypeConstants;

/**
 * 功能描述
 *
 * @author: Robin
 * @date: 2024年02月01日 14:30
 */
public class ProjectUserActionDescriptionTemplate extends AbstractActionDescriptionTemplate
{
    // 添加项目用户 or 删除项目用户
    @Override
    public Optional<String> getDescriptionTemplate()
    {
        String userIds = getDataFromContext("userIds");
        ArrayList<String> userIdList = null;
        Map map;
        if (!StringUtils.hasText(userIds) || "[]".equals(userIds))
        {
            //尝试从原始参数中提取
            String rawInputJson = getDataFromContext(ActionDescriptionContextFields.RAW_INPUT_PARAMETER_MAP);
            if (!StringUtils.hasText(rawInputJson))
            {
                return Optional.empty();
            }

            map = JsonUtils.fromJson(rawInputJson, Map.class);
            if (CollectionUtils.isEmpty(map))
            {
                return Optional.empty();
            }
            userIdList = (ArrayList<String>) map.get("userIds");
        } else
        {
            List list = JsonUtils.fromJson(userIds, List.class);
            if (CollectionUtils.isEmpty(list))
            {
                return Optional.empty();
            } else
            {
                userIdList = new ArrayList<>();
                for (Object userId : list)
                {
                    if (StringUtils.isEmpty(userId))
                    {
                        continue;
                    }
                    userIdList.add(userId.toString());
                }
            }
        }


        String userIdListStr = String.join(",", userIdList.toArray(new String[userIdList.size()]));
        getContextData().put("_TMP_USER_ID_LIST", userIdListStr);

        String operationType = getOperationType().orElse("");
        switch (operationType)
        {
            case ResourceOperationTypeConstants.RESOURCE_CREATE:
                return Optional.of(ADD_USER_TO_PROJECT);
            case ResourceOperationTypeConstants.RESOURCE_DELETE:
                return Optional.of(DELETE_USER_FROM_PROJECT);
        }

        return Optional.empty();
    }

    public interface Descriptions
    {
        String ADD_USER_TO_PROJECT = "项目 ${RESOURCE_INSTANCE_NAME} 添加授权用户: ${_TMP_USER_ID_LIST}";
        String DELETE_USER_FROM_PROJECT = "项目 ${RESOURCE_INSTANCE_NAME} 删除授权用户: ${_TMP_USER_ID_LIST}";
    }
}
