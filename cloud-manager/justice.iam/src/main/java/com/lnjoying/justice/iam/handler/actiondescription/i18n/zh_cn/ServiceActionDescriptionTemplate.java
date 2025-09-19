package com.lnjoying.justice.iam.handler.actiondescription.i18n.zh_cn;

import com.lnjoying.justice.commonweb.handler.template.actiondescription.AbstractActionDescriptionTemplate;
import com.lnjoying.justice.iam.db.model.TblIamService;
import com.lnjoying.justice.schema.constant.ResourceOperationTypeConstants;
import org.springframework.util.StringUtils;

import java.util.Optional;

import static com.lnjoying.justice.iam.handler.actiondescription.i18n.zh_cn.ServiceActionDescriptionTemplate.Descriptions.*;

/**
 * 功能描述
 *
 * @author: Robin
 * @date: 2024年01月30日 16:04
 */
public class ServiceActionDescriptionTemplate extends AbstractActionDescriptionTemplate
{
    @Override
    public Optional<String> getDescriptionTemplate()
    {
        String operationType = getOperationType().orElse("");
        Optional<String> templateContent = Optional.empty();
        String enable = getInputParameter("enable", null);

        switch (operationType)
        {
            case ResourceOperationTypeConstants.RESOURCE_CREATE:
                templateContent = Optional.of(ADD_SERVICE);
                break;
            case ResourceOperationTypeConstants.RESOURCE_UPDATE:
                templateContent = Optional.of(UPDATE_SERVICE);
                break;
            case ResourceOperationTypeConstants.RESOURCE_DELETE:
                templateContent = Optional.of(DELETE_SERVICE);
                break;
        }

        TblIamService resourceInstance = getResourceInstance();
        if (!StringUtils.hasText(enable) && resourceInstance != null)
        {
            enable = Optional.ofNullable(resourceInstance.getEnable()).map(x -> x.toString())
                    .orElse("");
        }

        switch (enable)
        {
            case "1":
                getContextData().put("_TMP_STATUS_NAME", "启用");
                break;
            case "-1":
                getContextData().put("_TMP_STATUS_NAME", "禁用");
                break;
        }

        return templateContent;
    }

    public interface Descriptions
    {
        String ADD_SERVICE = "添加服务 ${RESOURCE_INSTANCE_NAME},  服务描述: ${description},  服务编码: ${iamCode},  服务状态: ${_TMP_STATUS_NAME}";
        String UPDATE_SERVICE = "更新服务 ${RESOURCE_INSTANCE_NAME},  服务描述: ${description},  服务编码: ${iamCode},  服务状态: ${_TMP_STATUS_NAME}";
        String DELETE_SERVICE = "删除服务 ${RESOURCE_INSTANCE_NAME},  服务编码: ${RESOURCE_INSTANCE.iamCode}";
    }
}
