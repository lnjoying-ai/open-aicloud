package com.lnjoying.justice.cluster.manager.handler.actiondescription.i18n.zh_cn;

import com.lnjoying.justice.cluster.manager.common.CLusterAction;
import com.lnjoying.justice.commonweb.handler.template.actiondescription.AbstractActionDescriptionTemplate;
import org.apache.commons.lang3.EnumUtils;

import java.util.Optional;

import static com.lnjoying.justice.cluster.manager.handler.actiondescription.i18n.zh_cn.ClusterInfoActionDescriptionTemplate.Descriptions.EXPORT_TEMPLATE;
import static com.lnjoying.justice.cluster.manager.handler.actiondescription.i18n.zh_cn.ClusterInfoActionDescriptionTemplate.Descriptions.OTHERS;

/**
 * 功能描述
 *
 * @author: Robin
 * @date: 2024年01月22日 17:07
 */
public class ClusterInfoActionDescriptionTemplate extends AbstractActionDescriptionTemplate
{
    @Override
    public Optional<String> getDescriptionTemplate()
    {
        String action = (String) getContextData().get("action");
        if (action == null)
        {
            return Optional.empty();
        }

        CLusterAction clusterAction = CLusterAction.fromAction(action);
        switch (clusterAction)
        {
            case EXPORT_TEMPLATE:
                return Optional.of(EXPORT_TEMPLATE);
            default:
                return Optional.of(OTHERS);
        }
    }

    public interface Descriptions
    {
        String ADD_CLUSTER = "添加集群 ${RESOURCE_INSTANCE_NAME}";
        String UPDATE_CLUSTER = "更新集群 ${RESOURCE_INSTANCE_NAME}";
        String UPDATE_CLUSTER_STATUS = "集群 ${RESOURCE_INSTANCE_NAME} 状态更新为 ${status}";
        String UPDATE_CLUSTER_SERVICE_STATUS = "集群 ${RESOURCE_INSTANCE_NAME} 服务状态更新为 ${status}";
        String DELETE_CLUSTER = "删除集群 ${RESOURCE_INSTANCE_NAME}";
        String EXPORT_TEMPLATE = "集群 ${RESOURCE_INSTANCE_NAME} 导出模板, 模板名称: ${template_name} 版本: ${template_version}";
        String OTHERS = "操作集群 ${RESOURCE_INSTANCE_NAME} 动作: ${action}";
    }
}
