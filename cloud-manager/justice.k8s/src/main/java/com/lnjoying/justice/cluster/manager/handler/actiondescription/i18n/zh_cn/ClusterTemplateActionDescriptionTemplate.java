package com.lnjoying.justice.cluster.manager.handler.actiondescription.i18n.zh_cn;

import com.lnjoying.justice.commonweb.handler.template.actiondescription.AbstractActionDescriptionTemplate;

import java.util.Optional;

/**
 * 功能描述
 *
 * @author: Robin
 * @date: 2024年01月22日 17:07
 */
public class ClusterTemplateActionDescriptionTemplate extends AbstractActionDescriptionTemplate
{
    @Override
    public Optional<String> getDescriptionTemplate()
    {
        return Optional.empty();
    }

    public interface Descriptions
    {
        String ADD_TEMPLATE = "添加${RESOURCE_NAME} ${RESOURCE_INSTANCE_NAME}";
    }
}
