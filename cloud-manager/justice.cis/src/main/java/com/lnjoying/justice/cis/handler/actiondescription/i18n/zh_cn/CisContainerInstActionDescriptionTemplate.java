package com.lnjoying.justice.cis.handler.actiondescription.i18n.zh_cn;

import com.lnjoying.justice.cis.common.constant.InstAction;
import com.lnjoying.justice.commonweb.handler.template.actiondescription.AbstractActionDescriptionTemplate;
import org.apache.commons.lang3.EnumUtils;

import java.util.HashMap;
import java.util.Optional;

import static com.lnjoying.justice.cis.handler.actiondescription.i18n.zh_cn.CisContainerInstActionDescriptionTemplate.Descriptions.*;

/**
 * 容器实例操作的日志描述模板
 *
 * @author: Robin
 * @date: 2024年01月15日 10:42
 */
public class CisContainerInstActionDescriptionTemplate extends AbstractActionDescriptionTemplate
{
    public CisContainerInstActionDescriptionTemplate()
    {
        super("zh_CN");
    }

    public CisContainerInstActionDescriptionTemplate(String action)
    {
        super("zh_CN");
        this.setContextData(new HashMap<>());
        this.getContextData().put("action", action);
    }

    @Override
    public Optional<String> getDescriptionTemplate()
    {
        Object action = this.getContextData().get("action");
        if (action == null || !EnumUtils.isValidEnum(InstAction.class, action.toString()))
        {
            return Optional.empty();
        }

        switch (InstAction.valueOf(action.toString()))
        {
            case START:
                return Optional.of(START);
            case REMOVE:
                return Optional.of(REMOVE);
            case STOP:
                return Optional.of(STOP);
            case RESTART:
                return Optional.of(RESTART);
            case EXECUTE:
                return Optional.of(REMOTE_CMD_EXECUTE);
            case LOGS:
                return Optional.of(VIEW_LOG);
        }

        return Optional.of(OTHER);
    }

    public interface Descriptions
    {
        String CREATE_CONTAINER = "创建容器 ${RESOURCE_INSTANCE_NAME} 状态为 ${status}";
        String CONTAINER_STATUS_UPDATE = "容器 ${RESOURCE_INSTANCE_NAME} 状态更新为 ${status}";
        String START = "启动容器 ${RESOURCE_INSTANCE_NAME}";
        String REMOVE = "删除容器 ${RESOURCE_INSTANCE_NAME}";
        String REMOVE2 = "删除容器 ${RESOURCE_INSTANCE_NAME}";
        String STOP = "停止容器 ${RESOURCE_INSTANCE_NAME}";
        String RESTART = "重启容器 ${RESOURCE_INSTANCE_NAME}";
        String REMOTE_CMD_EXECUTE = "容器 ${RESOURCE_INSTANCE_NAME} 执行远程命令";
        String VIEW_LOG = "查看容器 ${RESOURCE_INSTANCE_NAME} 日志";
        String OTHER = "操作容器 ${RESOURCE_INSTANCE_NAME} 动作: {action}";
    }
}
