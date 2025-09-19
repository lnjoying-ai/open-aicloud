package com.lnjoying.justice.iam.handler.actiondescription.i18n.zh_cn;

import com.lnjoying.justice.commonweb.handler.template.actiondescription.AbstractActionDescriptionTemplate;
import com.lnjoying.justice.commonweb.handler.template.constant.ActionDescriptionContextFields;
import com.lnjoying.justice.iam.db.model.TblIamPolicy;
import com.lnjoying.justice.schema.constant.ResourceOperationTypeConstants;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Optional;

import static com.lnjoying.justice.iam.handler.actiondescription.i18n.zh_cn.PolicyActionDescriptionTemplate.Descriptions.*;

/**
 * 功能描述
 *
 * @author: Robin
 * @date: 2024年01月29日 20:20
 */
public class PolicyActionDescriptionTemplate extends AbstractActionDescriptionTemplate
{
    @Override
    public Optional<String> getDescriptionTemplate()
    {
        String operationType = getOperationType().orElse("");
        //从入参中读取策略类型
        String policyType = Optional.ofNullable(getContextData().get("policyType"))
                .map(x -> x.toString())
                .orElse(Optional.ofNullable(getContextData().get(ActionDescriptionContextFields.RAW_INPUT_PARAMETER_MAP))
                        .map(x -> Optional.ofNullable(((Map<String, Object>) x).get("policy_type"))
                                .map(x2 -> x2.toString()).orElse(""))
                        .orElse(""));
        TblIamPolicy resourceInstance = Optional.ofNullable(getContextData().get(ActionDescriptionContextFields.RESOURCE_INSTANCE))
                .map(x -> (TblIamPolicy) x)
                .orElse(null);
        if (!StringUtils.hasText(policyType) && resourceInstance != null)
        {
            //从资源实例中读取策略类型
            policyType = Optional.ofNullable(resourceInstance.getPolicyType())
                    .map(x -> x.toString()).orElse("");
        }
        Optional<String> templateContent;
        switch (operationType)
        {
            case ResourceOperationTypeConstants.RESOURCE_CREATE:
                templateContent = Optional.of(ADD_POLICY);
                break;
            case ResourceOperationTypeConstants.RESOURCE_UPDATE:
                templateContent = Optional.of(UPDATE_POLICY);
                break;
            case ResourceOperationTypeConstants.RESOURCE_DELETE:

                if (resourceInstance == null)
                {
                    return Optional.empty();
                }
                policyType = Optional.ofNullable(resourceInstance.getPolicyType()).map(x -> x.toString())
                        .orElse("");
                templateContent = Optional.of(DELETE_POLICY);
                break;
            default:
                return Optional.empty();
        }

        String policyTypeName;
        switch (policyType)
        {
            case "1":
                policyTypeName = "系统策略";
                break;
            case "2":
                policyTypeName = "自定义策略";
                break;
            default:
                return Optional.empty();
        }

        getContextData().put("_TMP_POLICY_TYPE_NAME", policyTypeName);
        return templateContent;
    }

    public interface Descriptions
    {
        String ADD_POLICY = "添加策略, 名称: ${RESOURCE_INSTANCE_NAME}, 策略类型: ${_TMP_POLICY_TYPE_NAME}, 策略描述: ${description}";
        String UPDATE_POLICY = "更新策略, 名称: ${RESOURCE_INSTANCE_NAME}, 策略类型: ${_TMP_POLICY_TYPE_NAME}, 策略描述: ${description}";
        String DELETE_POLICY = "删除策略, 名称: ${RESOURCE_INSTANCE_NAME}";
    }
}
