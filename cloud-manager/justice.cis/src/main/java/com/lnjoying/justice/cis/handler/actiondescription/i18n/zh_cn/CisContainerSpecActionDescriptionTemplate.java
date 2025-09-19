package com.lnjoying.justice.cis.handler.actiondescription.i18n.zh_cn;

import com.lnjoying.justice.commonweb.handler.template.actiondescription.AbstractActionDescriptionTemplate;

import java.util.Optional;

/**
 * 功能描述
 *
 * @author: Robin
 * @date: 2024年01月16日 16:59
 */
public class CisContainerSpecActionDescriptionTemplate extends AbstractActionDescriptionTemplate
{
    @Override
    public Optional<String> getDescriptionTemplate()
    {
        return Optional.empty();
    }

    public interface Descriptions
    {
        String ADD_CONTAINER_DEPLOY = "添加容器 ${RESOURCE_INSTANCE_NAME}  镜像: ${RESOURCE_INSTANCE.imageName}";
        String DELETE_CONTAINER_DEPLOY = "删除容器部署 ${RESOURCE_INSTANCE_NAME}  镜像: ${RESOURCE_INSTANCE.imageName}";
        String UPDATE_SPEC = "更新容器部署 ${spec_name}(${spec_id})";
        String UPDATE_SPEC_STATUS = "容器部署 ${spec_name}(${spec_id}) 状态更新为 ${status}";
    }
}
