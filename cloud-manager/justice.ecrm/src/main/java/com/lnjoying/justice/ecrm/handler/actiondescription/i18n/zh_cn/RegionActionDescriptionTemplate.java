package com.lnjoying.justice.ecrm.handler.actiondescription.i18n.zh_cn;

import com.lnjoying.justice.commonweb.handler.template.actiondescription.AbstractActionDescriptionTemplate;

import java.util.Optional;

/**
 * 功能描述
 *
 * @author: Robin
 * @date: 2024年01月25日 20:27
 */
public class RegionActionDescriptionTemplate extends AbstractActionDescriptionTemplate
{
    @Override
    public Optional<String> getDescriptionTemplate()
    {
        return Optional.empty();
    }

    public interface Descriptions
    {
        String ADD_REGION = "添加区域 ${RESOURCE_INSTANCE_NAME}";
        String UPDATE_REGION = "更新区域 ${RESOURCE_INSTANCE.regionName}";
        String DELETE_REGION = "删除区域 ${RESOURCE_INSTANCE.regionName}";
    }
}
