package com.lnjoying.justice.cmp.handler.actiondescription.i18n.zh_cn;

import com.lnjoying.justice.commonweb.handler.template.actiondescription.AbstractActionDescriptionTemplate;

import java.util.Optional;

/**
 * 功能描述
 *
 * @author:
 * @date:
 */
public class CmpCloudActionDescriptionTemplate extends AbstractActionDescriptionTemplate
{
    @Override
    public Optional<String> getDescriptionTemplate()
    {
        return Optional.empty();
    }

    public interface Descriptions
    {
        String ADD_CLOUD = "添加资源池 ${RESOURCE_INSTANCE_NAME}, 类型: ${RESOURCE_INSTANCE.product}";
        String UPDATE_CLOUD = "更新资源池 ${RESOURCE_INSTANCE_NAME}";
        String ACTION_CLOUD = "更新资源池 ${RESOURCE_INSTANCE_NAME}状态";
        String DELETE_CLOUD = "删除资源池 ${RESOURCE_INSTANCE_NAME}";
        String SYNC_CLOUD = "同步资源池 ${RESOURCE_INSTANCE_NAME} 数据";
    }
}
