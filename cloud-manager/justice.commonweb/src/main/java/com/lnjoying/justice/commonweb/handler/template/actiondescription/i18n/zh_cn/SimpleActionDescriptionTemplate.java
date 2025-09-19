package com.lnjoying.justice.commonweb.handler.template.actiondescription.i18n.zh_cn;

import com.lnjoying.justice.commonweb.handler.template.actiondescription.AbstractActionDescriptionTemplate;
import com.lnjoying.justice.schema.constant.ResourceOperationTypeConstants;

import java.util.Optional;

import static com.lnjoying.justice.commonweb.handler.template.actiondescription.i18n.zh_cn.SimpleActionDescriptionTemplate.Descriptions.*;

/**
 * 简单接口访问描述模板
 *
 * @author: Robin
 * @date: 2024年01月10日 19:01
 */
public class SimpleActionDescriptionTemplate extends AbstractActionDescriptionTemplate
{
    public SimpleActionDescriptionTemplate()
    {
        super("zh_CN");
    }

    @Override
    public Optional<String> getDescriptionTemplate()
    {
        Optional<String> operationType = getOperationType();
        if (!operationType.isPresent())
        {
            return Optional.empty();
        }

        switch (operationType.get())
        {
            case ResourceOperationTypeConstants.RESOURCE_CREATE:
                return Optional.of(CREATE_RESOURCE);
            case ResourceOperationTypeConstants.RESOURCE_UPDATE:
                //更新
                return Optional.of(UPDATE_RESOURCE);
            case ResourceOperationTypeConstants.RESOURCE_DELETE:
                //删除
                return Optional.of(DELETE_RESOURCE);
        }

        return Optional.of(OTHERS);
    }

    public interface Descriptions
    {
        String CREATE_RESOURCE = "创建${RESOURCE_NAME}: ${RESOURCE_INSTANCE_NAME}";
        String UPDATE_RESOURCE = "更新${RESOURCE_NAME}: ${RESOURCE_INSTANCE_NAME}";
        String DELETE_RESOURCE = "删除${RESOURCE_NAME}: ${RESOURCE_INSTANCE_NAME}";
        String OTHERS = "操作${RESOURCE_NAME}: ${RESOURCE_INSTANCE_NAME}";
    }
}
