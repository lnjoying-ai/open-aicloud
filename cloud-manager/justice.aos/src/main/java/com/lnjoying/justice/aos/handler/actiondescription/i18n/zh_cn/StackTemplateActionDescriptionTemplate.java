package com.lnjoying.justice.aos.handler.actiondescription.i18n.zh_cn;

import com.lnjoying.justice.commonweb.handler.template.actiondescription.AbstractActionDescriptionTemplate;

import java.util.Optional;

/**
 * 功能描述
 *
 * @author: Robin
 * @date: 2024年01月17日 16:32
 */
public class StackTemplateActionDescriptionTemplate extends AbstractActionDescriptionTemplate
{
    @Override
    public Optional<String> getDescriptionTemplate()
    {
        return Optional.empty();
    }

    public interface Description
    {
        String UPDATE_TEMPLATE = "更新应用模板, 镜像名称: '${RESOURCE_INSTANCE_NAME}'";
    }
}
