package com.lnjoying.justice.omc.handler.actiondescription.i18n;

import com.lnjoying.justice.commonweb.handler.template.actiondescription.AbstractActionDescriptionTemplate;

import java.util.Optional;

/**
 * 功能描述
 *
 * @author: Robin
 * @date: 2024年01月29日 10:17
 */
public class ReceiverActionDescriptionTemplate extends AbstractActionDescriptionTemplate
{
    @Override
    public Optional<String> getDescriptionTemplate()
    {
        return Optional.empty();
    }

    public interface Descriptions
    {
        String ADD_RECEIVER = "创建通知对象 ${RESOURCE_INSTANCE_NAME}, 描述: ${description}";
        String UPDATE_RECEIVER = "更新通知对象 ${RESOURCE_INSTANCE_NAME}, 描述: ${description}";
        String DELETE_RECEIVER = "删除通知对象 ${RESOURCE_INSTANCE_NAME}";
    }
}
