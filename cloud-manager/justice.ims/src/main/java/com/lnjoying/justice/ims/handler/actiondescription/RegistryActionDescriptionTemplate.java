package com.lnjoying.justice.ims.handler.actiondescription;

import com.lnjoying.justice.commonweb.handler.template.actiondescription.AbstractActionDescriptionTemplate;
import com.lnjoying.justice.commonweb.handler.template.constant.ActionDescriptionContextFields;
import com.lnjoying.justice.ims.db.model.TblImsRegistry;
import com.lnjoying.justice.schema.constant.ResourceOperationTypeConstants;

import java.util.Optional;

import static com.lnjoying.justice.ims.handler.actiondescription.RegistryActionDescriptionTemplate.Descriptions.*;

/**
 * 功能描述
 *
 * @author: Robin
 * @date: 2024年01月26日 10:59
 */
public class RegistryActionDescriptionTemplate extends AbstractActionDescriptionTemplate
{
    @Override
    public Optional<String> getDescriptionTemplate()
    {
        Optional<String> operationType = getOperationType();
        if (!operationType.isPresent())
        {
            return Optional.empty();
        }

        String type;
        if (operationType.get().equals(ResourceOperationTypeConstants.RESOURCE_CREATE))
        {
            type = Optional.ofNullable(getContextData().get("type"))
                    .map(x -> x.toString())
                    .orElse("");
        } else
        {
            TblImsRegistry tblImsRegistry = Optional.ofNullable(getContextData().get(ActionDescriptionContextFields.RESOURCE_INSTANCE))
                    .map(x -> (TblImsRegistry) x)
                    .orElse(null);
            if (tblImsRegistry == null || tblImsRegistry.getType() == null)
            {
                return Optional.empty();
            } else
            {
                type = tblImsRegistry.getType().toString();
            }
        }

        switch (type)
        {
            case "0":
                getContextData().put("_TMP_REGISTRY_TYPE", "harbor");
                break;
            case "1":
                getContextData().put("_TMP_REGISTRY_TYPE", "其它");
                break;
            default:
                return Optional.empty();
        }

        switch (operationType.get())
        {
            case ResourceOperationTypeConstants.RESOURCE_CREATE:
                return Optional.of(ADD_REGISTRY);
            case ResourceOperationTypeConstants.RESOURCE_UPDATE:
                return Optional.of(UPDATE_REGISTRY);
            case ResourceOperationTypeConstants.RESOURCE_DELETE:
                return Optional.of(DELETE_REGISTRY);
        }

        return Optional.empty();
    }

    public interface Descriptions
    {
        String ADD_REGISTRY = "添加镜像仓库 ${registryName}, 仓库类型: ${_TMP_REGISTRY_TYPE}";
        String UPDATE_REGISTRY = "更新镜像仓库 ${RESOURCE_INSTANCE_NAME}, 仓库类型: ${_TMP_REGISTRY_TYPE}";
        String DELETE_REGISTRY = "删除镜像仓库 ${RESOURCE_INSTANCE_NAME}, 仓库类型: ${_TMP_REGISTRY_TYPE}";
    }
}
