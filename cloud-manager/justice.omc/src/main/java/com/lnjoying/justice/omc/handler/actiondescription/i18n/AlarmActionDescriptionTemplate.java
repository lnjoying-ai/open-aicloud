package com.lnjoying.justice.omc.handler.actiondescription.i18n;

import com.lnjoying.justice.commonweb.handler.template.actiondescription.AbstractActionDescriptionTemplate;
import com.lnjoying.justice.omc.domain.model.AlarmStatusEnum;
import org.springframework.util.StringUtils;

import java.util.Optional;

import static com.lnjoying.justice.omc.handler.actiondescription.i18n.AlarmActionDescriptionTemplate.Descriptions.DISABLE_ALARM;
import static com.lnjoying.justice.omc.handler.actiondescription.i18n.AlarmActionDescriptionTemplate.Descriptions.ENABLE_ALARM;

/**
 * 功能描述
 *
 * @author: Robin
 * @date: 2024年01月26日 19:12
 */
public class AlarmActionDescriptionTemplate extends AbstractActionDescriptionTemplate
{
    @Override
    public Optional<String> getDescriptionTemplate()
    {
        String action = Optional.ofNullable(getContextData().get("action"))
                .map(x -> x.toString())
                .orElse(null);
        if (!StringUtils.hasText(action))
        {
            return Optional.empty();
        }

        if (AlarmStatusEnum.ACTIVE.name().equalsIgnoreCase(action))
        {
            return Optional.of(ENABLE_ALARM);
        }

        if (AlarmStatusEnum.DEACTIVE.name().equalsIgnoreCase(action))
        {
            return Optional.of(DISABLE_ALARM);
        }

        return Optional.empty();
    }

    public interface Descriptions
    {
        String ADD_ALARM = "添加报警器 ${RESOURCE_INSTANCE_NAME}, 描述: ${description}";
        String UPDATE_ALARM = "更新报警器, 报警名称：${RESOURCE_INSTANCE_NAME}, 描述: ${RESOURCE_INSTANCE.description}";
        String DELETE_ALARM = "删除报警器, 报警名称: ${RESOURCE_INSTANCE_NAME}";
        String ENABLE_ALARM = "启用报警器, 报警名称: ${RESOURCE_INSTANCE_NAME}";
        String DISABLE_ALARM = "停用报警器, 报警名称: ${RESOURCE_INSTANCE_NAME}";
    }
}
