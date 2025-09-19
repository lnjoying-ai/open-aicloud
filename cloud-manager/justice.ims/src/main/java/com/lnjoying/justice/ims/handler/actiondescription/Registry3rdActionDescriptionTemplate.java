package com.lnjoying.justice.ims.handler.actiondescription;

import com.lnjoying.justice.commonweb.handler.template.actiondescription.AbstractActionDescriptionTemplate;
import com.lnjoying.justice.commonweb.handler.template.constant.ActionDescriptionContextFields;
import com.lnjoying.justice.ims.db.model.TblImsRegistry3rd;
import com.lnjoying.justice.schema.constant.ResourceOperationTypeConstants;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;
import java.util.Optional;

import static com.lnjoying.justice.ims.handler.actiondescription.Registry3rdActionDescriptionTemplate.Descriptions.*;

/**
 * 功能描述
 *
 * @author: Robin
 * @date: 2024年01月26日 10:59
 */
public class Registry3rdActionDescriptionTemplate extends AbstractActionDescriptionTemplate
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
        if (operationType.get().equals(ResourceOperationTypeConstants.RESOURCE_CREATE) ||
                operationType.get().equals(ResourceOperationTypeConstants.RESOURCE_UPDATE))
        {
            type = Optional.ofNullable(getContextData().get("type"))
                    .map(x -> x.toString())
                    .orElse("");
        } else
        {
            TblImsRegistry3rd tblImsRegistry3rd = Optional.ofNullable(getContextData().get(ActionDescriptionContextFields.RESOURCE_INSTANCE))
                    .map(x -> (TblImsRegistry3rd) x)
                    .orElse(null);
            if (tblImsRegistry3rd == null || tblImsRegistry3rd.getType() == null)
            {
                return Optional.empty();
            } else
            {
                type = tblImsRegistry3rd.getType().toString();
            }
        }

        //尝试异常修复
        if (StringUtils.isEmpty(type))
        {
            if (Objects.equals("docker.io", getContextData().get("url")))
            {
                type = "1";
            } else
            {
                type = "0";
            }
        }

        switch (type)
        {
            case "0":
                getContextData().put("_TMP_REGISTRY_TYPE", "harbor");
                break;
            case "1":
                getContextData().put("_TMP_REGISTRY_TYPE", "docker-hub");
                break;
            default:
                return Optional.empty();
        }

        switch (operationType.get())
        {
            case ResourceOperationTypeConstants.RESOURCE_CREATE:
                return Optional.of(ADD_THIRD_PARTY_REGISTRY);
            case ResourceOperationTypeConstants.RESOURCE_UPDATE:
                return Optional.of(UPDATE_THIRD_PARTY_REGISTRY);
            case ResourceOperationTypeConstants.RESOURCE_DELETE:
                return Optional.of(DELETE_THIRD_PARTY_REGISTRY);
        }

        return Optional.empty();
    }

    public interface Descriptions
    {
        String ADD_THIRD_PARTY_REGISTRY = "添加第三方镜像仓库 ${registryName}, 仓库类型: ${_TMP_REGISTRY_TYPE}";
        String UPDATE_THIRD_PARTY_REGISTRY = "更新第三方镜像仓库 ${registryName}, 仓库类型: ${_TMP_REGISTRY_TYPE}";
        String DELETE_THIRD_PARTY_REGISTRY = "删除第三方镜像仓库 ${RESOURCE_INSTANCE.registryName}, 仓库类型: ${_TMP_REGISTRY_TYPE}";
    }
}
