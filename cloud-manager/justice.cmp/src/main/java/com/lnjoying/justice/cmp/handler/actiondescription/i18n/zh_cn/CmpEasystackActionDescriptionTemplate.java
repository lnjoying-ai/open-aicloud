package com.lnjoying.justice.cmp.handler.actiondescription.i18n.zh_cn;

import com.lnjoying.justice.commonweb.handler.template.actiondescription.AbstractActionDescriptionTemplate;

import java.util.Optional;

/**
 * 功能描述
 *
 * @author:
 * @date:
 */
public class CmpEasystackActionDescriptionTemplate extends AbstractActionDescriptionTemplate
{
    @Override
    public Optional<String> getDescriptionTemplate()
    {
        return Optional.empty();
    }

    public interface Descriptions
    {
        String CREATE_FIREWALL = "创建防火墙 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";
        String UPDATE_FIREWALL = "编辑防火墙 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";
        String DELETE_FIREWALL = "删除防火墙 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";

        String CREATE_FIREWALL_POLICY = "创建防火墙策略 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";
        String UPDATE_FIREWALL_POLICY = "编辑防火墙策略 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";
        String DELETE_FIREWALL_POLICY = "删除防火墙策略 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";
        String ADD_FIREWALL_RULE_TO_POLICY = "添加防火墙规则 ${firewall_rule_id} 到防火墙策略 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";
        String DELETE_FIREWALL_RULE_FROM_POLICY = "从防火墙策略 ${RESOURCE_INSTANCE_NAME} 中删除防火墙规则 ${firewall_rule_id}，资源池 ${RESOURCE_POOL_ID}";

        String CREATE_FIREWALL_RULE = "创建防火墙规则 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";
        String UPDATE_FIREWALL_RULE = "编辑防火墙规则 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";
        String DELETE_FIREWALL_RULE = "删除防火墙规则 ${RESOURCE_INSTANCE_NAME}，资源池 ${RESOURCE_POOL_ID}";
    }
}
