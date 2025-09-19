package com.lnjoying.justice.iam.handler.actiondescription.i18n.zh_cn;

import com.lnjoying.justice.commonweb.handler.template.actiondescription.AbstractActionDescriptionTemplate;

import java.util.Optional;

/**
 * 功能描述
 *
 * @author: Robin
 * @date: 2024年01月29日 14:24
 */
public class BpActionDescriptionTemplate extends AbstractActionDescriptionTemplate
{
    @Override
    public Optional<String> getDescriptionTemplate()
    {
        return Optional.empty();
    }

    public interface Descriptions
    {
        String ADD_BP = "添加组织 ${RESOURCE_INSTANCE_NAME}, 描述: ${description}";
        String UPDATE_BP = "更新组织 ${RESOURCE_INSTANCE_NAME}, 描述: ${description}";
        String DELETE_BP = "删除组织 ${RESOURCE_INSTANCE.bpName}";

    }
}
