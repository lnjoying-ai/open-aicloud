package com.lnjoying.justice.aos.handler.actiondescription.i18n.zh_cn;

import com.lnjoying.justice.commonweb.handler.template.actiondescription.AbstractActionDescriptionTemplate;

import java.util.Optional;

/**
 * 功能描述
 *
 * @author: Robin
 * @date: 2024年01月17日 16:40
 */
public class StackSpecActionDescriptionTemplate extends AbstractActionDescriptionTemplate
{
    @Override
    public Optional<String> getDescriptionTemplate()
    {
        return Optional.empty();
    }

    public interface Descriptions
    {
        String ADD_SPEC = "添加应用 ${RESOURCE_INSTANCE_NAME}(${RESOURCE_INSTANCE_ID})";
        String UPDATE_SPEC_STATUS = "应用部署 ${RESOURCE_INSTANCE_NAME}(${RESOURCE_INSTANCE_ID}) 状态更新为 ${status}";
        String UPDATE_SPEC_AND_STACKS_STATUS = "应用部署 ${RESOURCE_INSTANCE_NAME}(${RESOURCE_INSTANCE_ID}) 状态更新为 ${status}";
    }
}
