package com.lnjoying.justice.iam.handler.actiondescription.i18n.zh_cn;

import com.lnjoying.justice.commonweb.handler.template.actiondescription.AbstractActionDescriptionTemplate;
import com.lnjoying.justice.commonweb.handler.template.constant.ActionDescriptionContextFields;
import com.lnjoying.justice.iam.db.model.TblIamRole;
import com.lnjoying.justice.schema.constant.ResourceOperationTypeConstants;

import java.util.Optional;

import static com.lnjoying.justice.iam.handler.actiondescription.i18n.zh_cn.RoleActionDescriptionTemplate.Descriptions.*;

/**
 * 功能描述
 *
 * @author: Robin
 * @date: 2024年01月30日 10:21
 */
public class RoleActionDescriptionTemplate extends AbstractActionDescriptionTemplate
{
    @Override
    public Optional<String> getDescriptionTemplate()
    {
        String roleType = "";
        String operationType = getOperationType().orElse("");
        Optional<String> templateContent;
        switch (operationType)
        {
            case ResourceOperationTypeConstants.RESOURCE_CREATE:
                roleType = Optional.ofNullable(getContextData().get("roleType")).map(x -> x.toString()).orElse("");
                templateContent = Optional.of(ADD_ROLE);
                break;
            case ResourceOperationTypeConstants.RESOURCE_UPDATE:
                roleType = Optional.ofNullable(getContextData().get("roleType")).map(x -> x.toString()).orElse("");
                templateContent = Optional.of(UPDATE_ROLE);
                break;
            case ResourceOperationTypeConstants.RESOURCE_DELETE:
                TblIamRole tblIamRole = Optional.ofNullable(getContextData().get(ActionDescriptionContextFields.RESOURCE_INSTANCE))
                        .map(x -> (TblIamRole) x)
                        .orElse(null);
                if (tblIamRole != null)
                {
                    roleType = Optional.ofNullable(tblIamRole.getRoleType()).map(x -> x.toString()).orElse("");
                }
                templateContent = Optional.of(DELETE_ROLE);
                break;
            default:
                return Optional.empty();
        }

        switch (roleType)
        {
            case "1":
                getContextData().put("_TMP_ROLE_TYPE_NAME", "系统角色");
                break;
            case "2":
                getContextData().put("_TMP_ROLE_TYPE_NAME", "自定义角色");
                break;
            default:
                return Optional.empty();
        }

        return templateContent;
    }

    public interface Descriptions
    {
        String ADD_ROLE = "添加角色 ${RESOURCE_INSTANCE_NAME}, 角色描述: ${description}, 角色类型: ${_TMP_ROLE_TYPE_NAME}, 所属项目id: ${projectId}, 服务编码: ${platform}";
        String UPDATE_ROLE = "更新角色 ${RESOURCE_INSTANCE_NAME}, 角色描述: ${description}, 角色类型: ${_TMP_ROLE_TYPE_NAME}, 所属项目id: ${projectId}, 服务编码: ${platform}";
        String DELETE_ROLE = "删除角色 ${RESOURCE_INSTANCE_NAME}, 角色描述: ${RESOURCE_INSTANCE.description}, 角色类型: ${_TMP_ROLE_TYPE_NAME}, 所属项目id: ${RESOURCE_INSTANCE.projectId}, 服务编码: ${RESOURCE_INSTANCE.platform}";
    }
}
