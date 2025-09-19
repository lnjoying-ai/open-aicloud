package com.lnjoying.justice.iam.handler.actiondescription.i18n.zh_cn;

import com.lnjoying.justice.commonweb.handler.template.actiondescription.AbstractActionDescriptionTemplate;
import com.lnjoying.justice.commonweb.handler.template.constant.ActionDescriptionContextFields;
import com.lnjoying.justice.iam.db.model.TblIamProject;
import com.lnjoying.justice.schema.constant.ResourceOperationTypeConstants;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Optional;

import static com.lnjoying.justice.iam.handler.actiondescription.i18n.zh_cn.ProjectActionDescriptionTemplate.Descriptions.*;

/**
 * 功能描述
 *
 * @author: Robin
 * @date: 2024年01月29日 21:00
 */
public class ProjectActionDescriptionTemplate extends AbstractActionDescriptionTemplate
{
    @Override
    public Optional<String> getDescriptionTemplate()
    {
        TblIamProject resourceInstance = getResourceInstance();
        String action = getDataFromContext("action");
        //　启用/禁用 项目
        if (StringUtils.hasText(action))
        {
            if (resourceInstance == null)
            {
                return Optional.empty();
            }
            switch (action)
            {
                case "active":
                    if (!StringUtils.hasText(resourceInstance.getParentId()))
                    {
                        return Optional.of(ENABLE_PROJECT);
                    } else
                    {
                        return Optional.of(ENABLE_SUB_PROJECT);
                    }
                case "deactive":
                    if (!StringUtils.hasText(resourceInstance.getParentId()))
                    {
                        return Optional.of(DISABLE_PROJECT);
                    } else
                    {
                        return Optional.of(DISABLE_SUB_PROJECT);
                    }
                default:
                    return Optional.empty();
            }
        }

        // 状态翻译
        String enable = getDataFromContext("enable");
        if ("1".equals(enable))
        {
            getContextData().put("_TMP_STATUS_NAME", "启用");
        } else
        {
            getContextData().put("_TMP_STATUS_NAME", "禁用");
        }

        // 根据资源操作类型选择合适模板
        String operationType = getOperationType().orElse("");
        String parentId = getInputParameter("parentId", "parent_id");
        if (!StringUtils.hasText(parentId) && resourceInstance != null)
        {
            //尝试从实例中获取
            parentId = resourceInstance.getParentId();
        }

        Optional<String> templateContent = Optional.empty();
        switch (operationType)
        {
            case ResourceOperationTypeConstants.RESOURCE_CREATE:
                if (!StringUtils.hasText(parentId))
                {
                    templateContent = Optional.of(ADD_PROJECT);
                } else
                {
                    templateContent = Optional.of(ADD_SUB_PROJECT);
                }
                break;
            case ResourceOperationTypeConstants.RESOURCE_UPDATE:
                if (!StringUtils.hasText(parentId))
                {
                    templateContent = Optional.of(UPDATE_PROJECT);
                } else
                {
                    templateContent = Optional.of(UPDATE_SUB_PROJECT);
                }
                break;
            case ResourceOperationTypeConstants.RESOURCE_DELETE:
                if (!StringUtils.hasText(parentId))
                {
                    templateContent = Optional.of(DELETE_PROJECT);
                } else
                {
                    templateContent = Optional.of(DELETE_SUB_PROJECT);
                }
                break;
        }

        return templateContent;
    }

    public interface Descriptions
    {
        String ADD_PROJECT = "添加项目 ${RESOURCE_INSTANCE_NAME}, 项目描述: ${description}, 项目状态: ${_TMP_STATUS_NAME}";
        String UPDATE_PROJECT = "更新项目 ${RESOURCE_INSTANCE_NAME}, 项目描述: ${description}, 项目状态: ${_TMP_STATUS_NAME}";
        String DELETE_PROJECT = "删除项目 ${RESOURCE_INSTANCE_NAME}";
        String ENABLE_PROJECT = "启用项目 ${RESOURCE_INSTANCE_NAME}";
        String DISABLE_PROJECT = "禁用项目 ${RESOURCE_INSTANCE_NAME}";

        String ADD_SUB_PROJECT = "添加子项目 ${RESOURCE_INSTANCE_NAME}, 项目描述: ${description}, 项目状态: ${_TMP_STATUS_NAME}, 父项目编号: ${parentId}";
        String UPDATE_SUB_PROJECT = "更新子项目 ${RESOURCE_INSTANCE_NAME}, 项目描述: ${description}, 项目状态: ${_TMP_STATUS_NAME}, 父项目编号: ${RESOURCE_INSTANCE.parentId}";
        String DELETE_SUB_PROJECT = "删除子项目 ${RESOURCE_INSTANCE_NAME}, 父项目编号: ${RESOURCE_INSTANCE.parentId}";
        String ENABLE_SUB_PROJECT = "启用子项目 ${RESOURCE_INSTANCE_NAME}, 父项目编号: ${RESOURCE_INSTANCE.parentId}";
        String DISABLE_SUB_PROJECT = "禁用子项目 ${RESOURCE_INSTANCE_NAME}, 父项目编号: ${RESOURCE_INSTANCE.parentId}";

    }
}
