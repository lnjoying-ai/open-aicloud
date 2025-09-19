package com.lnjoying.justice.aos.handler.actiondescription.i18n.zh_cn;

import com.lnjoying.justice.aos.common.ActionType;
import com.lnjoying.justice.commonweb.handler.template.actiondescription.AbstractActionDescriptionTemplate;
import org.springframework.util.StringUtils;

import java.util.Optional;

import static com.lnjoying.justice.aos.handler.actiondescription.i18n.zh_cn.StackInfoActionDescriptionTemplate.Descriptions.*;

/**
 * 功能描述
 *
 * @author: Robin
 * @date: 2024年01月17日 14:50
 */
public class StackInfoActionDescriptionTemplate extends AbstractActionDescriptionTemplate
{
    @Override
    public Optional<String> getDescriptionTemplate()
    {
        Object action = getContextData().get("action");
        if (StringUtils.isEmpty(action))
        {
            return Optional.empty();
        }

        switch (action.toString())
        {
            case ActionType.START:
                return Optional.of(START);
            case ActionType.STOP:
                return Optional.of(STOP);
            case ActionType.RESTART:
                return Optional.of(RESTART);
            default:
                return Optional.of(OTHERS);
        }
    }

    public interface Descriptions
    {
        String CREATE_STACK = "创建应用 ${RESOURCE_INSTANCE_NAME}";
        String UPDATE_STACK = "更新应用 ${RESOURCE_INSTANCE_NAME}";
        String DELETE_STACK = "删除应用 ${RESOURCE_INSTANCE_NAME}";
        String STACK_STATUS_UPDATE = "应用 ${RESOURCE_INSTANCE_NAME} 状态更新为 ${status}";

        String START = "启动应用 ${RESOURCE_INSTANCE_NAME}";
        String STOP = "停止应用 ${RESOURCE_INSTANCE_NAME}";
        String RESTART = "重启应用 ${RESOURCE_INSTANCE_NAME}";
        String OTHERS = "操作应用 ${RESOURCE_INSTANCE_NAME}  动作: ${action}";
    }
}
