package com.lnjoying.justice.ecrm.handler.actiondescription.i18n.zh_cn;

import com.lnjoying.justice.commonweb.handler.template.actiondescription.AbstractActionDescriptionTemplate;
import com.lnjoying.justice.ecrm.common.constant.ActionType;

import java.util.HashMap;
import java.util.Optional;

import static com.lnjoying.justice.ecrm.handler.actiondescription.i18n.zh_cn.EdgeActionDescriptionTemplate.Descriptions.*;

/**
 * 功能描述
 *
 * @author: Robin
 * @date: 2024年01月15日 19:38
 */
public class EdgeActionDescriptionTemplate extends AbstractActionDescriptionTemplate
{
    public EdgeActionDescriptionTemplate()
    {
        super("zh_CN");
    }

    public EdgeActionDescriptionTemplate(String action)
    {
        super("zh_CN");
        this.setContextData(new HashMap<>());
        this.getContextData().put("action", action);
    }

    @Override
    public Optional<String> getDescriptionTemplate()
    {
        Object action = getContextData().get("action");
        if (action == null)
        {
            return Optional.empty();
        }

        switch (action.toString())
        {
            case ActionType.ACTIVE:
                return Optional.of(ACTIVE_NODE);
            case ActionType.DEACTIVE:
                return Optional.of(DEACTIVE_NODE);
            case ActionType.EVACUATE:
                return Optional.of(EVACUATE_NODE);
            case ActionType.REBOOT:
                return Optional.of(REBOOT_NODE);
            //节点升级确认的action
            case "confirm":
                return Optional.of(CONFIRM_UPGRADE_NODE);
            case "rollback":
                return Optional.of(CONFIRM_UPGRADE_NODE_ROLLBACK);
        }

        return Optional.of(OTHER_NODE_ACTION);
    }


    public interface Descriptions
    {
        String ACTIVE_NODE = "激活节点 ${RESOURCE_INSTANCE_NAME}";
        String DEACTIVE_NODE = "去激活节点 ${RESOURCE_INSTANCE_NAME}";
        String EVACUATE_NODE = "驱逐节点 ${RESOURCE_INSTANCE_NAME}";
        String REBOOT_NODE = "重启节点 ${RESOURCE_INSTANCE_NAME}";

        String UPDATE_NODE_ACTIVE_STATUS = "节点 ${RESOURCE_INSTANCE_NAME} 激活状态更新为 ${status}";
        String UPDATE_NODE_ONLINE_STATUS = "节点 ${RESOURCE_INSTANCE_NAME} 在线状态更新为 ${status}";

        String CONFIRM_UPGRADE_NODE = "节点 ${RESOURCE_INSTANCE_NAME} 升级确认";
        String CONFIRM_UPGRADE_NODE_ROLLBACK = "节点 ${RESOURCE_INSTANCE_NAME} 升级回滚";
        String OTHER_NODE_ACTION = "操作节点 ${RESOURCE_INSTANCE_NAME} 动作: ${action}";
        String ADD_NODE = "生成添加节点命令, 节点名称 ${node_name}";
        String UPDATE_NODE_LABEL = "更新节点 ${RESOURCE_INSTANCE_NAME} 标签";
        String UPDATE_NODE = "更新节点 ${RESOURCE_INSTANCE_NAME}";
        String UPDATE_NODE_STATUS = "节点 ${RESOURCE_INSTANCE_NAME} 状态更新为 ${status}";
        String DELETE_NODE = "删除节点 ${RESOURCE_INSTANCE_NAME}";
        String UPGRADE_NODE = "升级节点 ${RESOURCE_INSTANCE_NAME}";
        String BIND_NODE = "绑定节点";
        String BIND_SINGLE_NODE = "绑定节点 ${RESOURCE_INSTANCE_NAME}";
        String UNBIND_NODE = "解绑节点";
        String UNBIND_SINGLE_NODE = "解绑节点 ${RESOURCE_INSTANCE_NAME}";
    }
}
