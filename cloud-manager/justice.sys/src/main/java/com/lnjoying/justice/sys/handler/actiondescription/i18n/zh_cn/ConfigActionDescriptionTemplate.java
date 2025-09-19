package com.lnjoying.justice.sys.handler.actiondescription.i18n.zh_cn;

import com.lnjoying.justice.commonweb.handler.template.actiondescription.AbstractActionDescriptionTemplate;
import com.lnjoying.justice.schema.constant.ResourceOperationTypeConstants;

import java.util.Optional;

import static com.lnjoying.justice.sys.handler.actiondescription.i18n.zh_cn.ConfigActionDescriptionTemplate.Descriptions.ADD_CONFIG;
import static com.lnjoying.justice.sys.handler.actiondescription.i18n.zh_cn.ConfigActionDescriptionTemplate.Descriptions.UPDATE_CONFIG;

/**
 * 功能描述
 *
 * @author: Robin
 * @date: 2024年01月23日 19:07
 */
public class ConfigActionDescriptionTemplate extends AbstractActionDescriptionTemplate
{
    @Override
    public Optional<String> getDescriptionTemplate()
    {
        Optional<String> operationType = getOperationType();
        if (!operationType.isPresent())
        {
            return Optional.empty();
        }

        switch (operationType.get())
        {
            case ResourceOperationTypeConstants.RESOURCE_CREATE:
                return Optional.of(ADD_CONFIG);
            case ResourceOperationTypeConstants.RESOURCE_UPDATE:
                return Optional.of(UPDATE_CONFIG);
        }

        return Optional.empty();
    }

    public interface Descriptions
    {
        String ADD_CONFIG = "添加配置 ${RESOURCE_INSTANCE_NAME}, 组: ${group}";
        String UPDATE_CONFIG = "更新配置 ${RESOURCE_INSTANCE_NAME}, 组: ${group}";
        String DELETE_CONFIG = "删除配置 ${RESOURCE_INSTANCE_NAME}, 组: ${group}";
    }
}
