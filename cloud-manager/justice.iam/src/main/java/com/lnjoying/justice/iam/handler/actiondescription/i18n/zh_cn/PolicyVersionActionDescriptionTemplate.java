package com.lnjoying.justice.iam.handler.actiondescription.i18n.zh_cn;

import com.lnjoying.justice.commonweb.handler.template.actiondescription.AbstractActionDescriptionTemplate;

import java.util.Optional;

/**
 * 功能描述
 *
 * @author: Robin
 * @date: 2024年02月01日 11:33
 */
public class PolicyVersionActionDescriptionTemplate extends AbstractActionDescriptionTemplate
{
    @Override
    public Optional<String> getDescriptionTemplate()
    {
        return Optional.empty();
    }

    public interface Descriptions
    {
        String ADD_POLICY_VERSION = "新增策略版本 ${RESOURCE_INSTANCE_NAME}, 策略id: ${policyId}, 描述: ${description}";
        String UPDATE_POLICY_VERSION = "更新策略版本 ${RESOURCE_INSTANCE_NAME}, 策略id: ${policyId}";
        String DELETE_POLICY_VERSION = "删除策略版本 ${RESOURCE_INSTANCE_NAME}, 策略id: ${policyId}";
    }
}
