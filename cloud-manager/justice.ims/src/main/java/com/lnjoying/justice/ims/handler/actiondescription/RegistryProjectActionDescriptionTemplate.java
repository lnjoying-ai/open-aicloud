package com.lnjoying.justice.ims.handler.actiondescription;

import com.lnjoying.justice.commonweb.handler.template.actiondescription.AbstractActionDescriptionTemplate;

import java.util.Optional;

/**
 * 功能描述
 *
 * @author: Robin
 * @date: 2024年01月26日 16:38
 */
public class RegistryProjectActionDescriptionTemplate extends AbstractActionDescriptionTemplate
{
    @Override
    public Optional<String> getDescriptionTemplate()
    {
        return Optional.empty();
    }

    public interface Descriptions
    {
        String ADD_REGISTRY_PROJECT = "添加项目 ${RESOURCE_INSTANCE_NAME}, 镜像仓库id: ${RAW_INPUT_PARAMETER_MAP.registry_id}";
        String UPDATE_REGISTRY_PROJECT = "更新项目 ${RESOURCE_INSTANCE_NAME}, 镜像仓库id: ${RESOURCE_INSTANCE.registryId}";
        String DELETE_REGISTRY_PROJECT = "删除项目 ${RESOURCE_INSTANCE_NAME}, 镜像仓库id: ${RESOURCE_INSTANCE.registryId}";
    }
}
