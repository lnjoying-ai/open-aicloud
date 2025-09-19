package com.lnjoying.justice.commonweb.handler.template.actiondescription;

import com.lnjoying.justice.commonweb.handler.template.constant.ActionDescriptionContextFields;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.Optional;

public interface ActionDescriptionTemplate
{
    Optional<String> getLanguage();

    void setContextData(Map<String, Object> contextData);

    Map<String, Object> getContextData();

    default Optional<String> getOperationType()
    {
        Map<String, Object> contextData = getContextData();
        if (CollectionUtils.isEmpty(contextData))
        {
            return Optional.empty();
        }

        Object operationType = contextData.get(ActionDescriptionContextFields.OPERATION_TYPE);
        return Optional.ofNullable((String) operationType);
    }

    Optional<String> getDescriptionTemplate();

//    String getCreateActionDescription();
//    String getUpdateActionDescription();
//    String getDeleteActionDescription();
//    String getOtherActionDescription();
}
