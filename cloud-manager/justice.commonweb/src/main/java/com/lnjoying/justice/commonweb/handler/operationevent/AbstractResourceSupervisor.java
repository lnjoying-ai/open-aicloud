package com.lnjoying.justice.commonweb.handler.operationevent;

import com.lnjoying.justice.commonweb.handler.operationevent.model.BizModelStateInfo;
import com.lnjoying.justice.commonweb.process.service.OperationLogOrEventSendProcessStrategy;
import com.lnjoying.justice.commonweb.util.BeanMapTool;
import com.lnjoying.justice.commonweb.util.JsonUtils;
import com.lnjoying.justice.commonweb.util.ObjectDiffLogger;
import com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils;
import com.lnjoying.justice.schema.common.ErrorLevel;
import com.lnjoying.justice.schema.common.OperationEventType;
import com.lnjoying.justice.schema.constant.UserHeadInfo;
import com.lnjoying.justice.schema.msg.MessagePack;
import com.lnjoying.justice.schema.service.omc.OmcService;
import com.micro.core.common.Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * 功能描述
 *
 * @author: Robin
 * @date: 2023年10月30日 11:37
 */
@Slf4j
public abstract class AbstractResourceSupervisor<T>
{
    private String serviceName;
    private String resourceEnName;
    private String resourceCnName;
    private OperationLogOrEventSendProcessStrategy logOrEventSendProcessStrategy;
    private IBizModelStateDesProvider bizModelStateDesProvider;

    public AbstractResourceSupervisor(String serviceName, String resourceEnName, String resourceCnName, OperationLogOrEventSendProcessStrategy logOrEventSendProcessStrategy)
    {
        this.serviceName = serviceName;
        this.resourceEnName = resourceEnName;
        this.resourceCnName = resourceCnName;
        this.logOrEventSendProcessStrategy = logOrEventSendProcessStrategy;
    }

    public AbstractResourceSupervisor(String serviceName, String resourceEnName, String resourceCnName,
                                      OperationLogOrEventSendProcessStrategy logOrEventSendProcessStrategy,
                                      IBizModelStateDesProvider bizModelStateDesProvider)
    {
        this.serviceName = serviceName;
        this.resourceEnName = resourceEnName;
        this.resourceCnName = resourceCnName;
        this.logOrEventSendProcessStrategy = logOrEventSendProcessStrategy;
        this.bizModelStateDesProvider = bizModelStateDesProvider;
    }

    public String getServiceName()
    {
        return serviceName;
    }

    public void setServiceName(String serviceName)
    {
        this.serviceName = serviceName;
    }

    public String getResourceEnName()
    {
        return resourceEnName;
    }

    public void setResourceEnName(String resourceEnName)
    {
        this.resourceEnName = resourceEnName;
    }

    public String getResourceCnName()
    {
        return resourceCnName;
    }

    public void setResourceCnName(String resourceCnName)
    {
        this.resourceCnName = resourceCnName;
    }

    public OperationLogOrEventSendProcessStrategy getOperationEventSendProcessStrategy()
    {
        return logOrEventSendProcessStrategy;
    }

    public String getCreateEventName(String customResourceName)
    {
        return String.format("%s创建", Optional.ofNullable(customResourceName)
                .orElse(this.getResourceCnName()));
    }

    public String getDeleteEventName(String customResourceName)
    {
        return String.format("%s删除", Optional.ofNullable(customResourceName)
                .orElse(this.getResourceCnName()));
    }

    public String getUpdateActionName(String customActionName)
    {
        return Optional.ofNullable(customActionName).orElse("update");
    }

    public String getDeleteActionName(String customActionName)
    {
        return Optional.ofNullable(customActionName).orElse("delete");
    }

    public OmcService.OperationEvent newCreateEvent(String customResourceName, String friendlyDescription, String customActionName, T resource)
    {
        OmcService.OperationEvent createEvent = null;
        try
        {
            createEvent = OmcService.OperationEvent.builder()
                    .id(Utils.assignUUId())
                    .triggerTime(new Date())
                    .type(OperationEventType.RESOURCE_CREATE.name())
                    .level(ErrorLevel.INFO.name())
                    .service(this.getServiceName())
                    .resource(this.getResourceEnName())
                    .action(Optional.ofNullable(customActionName)
                            .orElse("create"))
                    .name(this.getCreateEventName(customResourceName))
                    .friendlyDescription(friendlyDescription)
                    .result("ok")
                    .build();
            if (resource != null)
            {
                LinkedHashMap<String, ObjectDiffLogger.PropertyValueChangeLog> logs = new LinkedHashMap<>();
                Map<String, Object> resourceObjectMap = BeanMapTool.beanToMap(resource);
                Set<Map.Entry<String, Object>> entrySet = resourceObjectMap.entrySet();
                for (Map.Entry<String, Object> entry : entrySet)
                {
                    if (StringUtils.isEmpty(entry.getValue()))
                    {
                        continue;
                    }
                    ObjectDiffLogger.PropertyValueChangeLog propertyValueChangeLog = new ObjectDiffLogger.PropertyValueChangeLog();
                    propertyValueChangeLog.setNewValue(entry.getValue());
                    logs.put(entry.getKey(), propertyValueChangeLog);
                }

                if (!CollectionUtils.isEmpty(logs))
                {
                    if (logs.size() > 0)
                    {
                        //状态值中英文描述
                        fillStateFieldDescription(logs);
                        createEvent.setDescription(JsonUtils.toJson(logs));
                    }
                }
            }

            createEvent.setRequestPath(ServiceCombRequestUtils.getRequestPath(null));
            createEvent.setBpId(ServiceCombRequestUtils.getHeaderValue(UserHeadInfo.BPID, null));
            createEvent.setUserId(ServiceCombRequestUtils.getHeaderValue(UserHeadInfo.USERID, null));
            createEvent.setOperator(ServiceCombRequestUtils.getHeaderValue(UserHeadInfo.USERNAME, null));
        } catch (Throwable e)
        {
            log.error("创建资源创建事件时异常! error: {}", e.getMessage());
        }
        return createEvent;
    }

    public OmcService.OperationEvent newUpdateEvent(String eventName, String customActionName,
                                                    OperationEventType customEventType,
                                                    T before, T after,
                                                    String customDescription)
    {
        OmcService.OperationEvent operationEvent = null;
        try
        {
            LinkedHashMap<String, ObjectDiffLogger.PropertyValueChangeLog> logs = ObjectDiffLogger.logObjectDiff(before, after);
            operationEvent = OmcService.OperationEvent.builder()
                    .id(Utils.assignUUId())
                    .triggerTime(new Date())
                    .type(Optional.ofNullable(customEventType).map(x -> x.name())
                            .orElse(OperationEventType.RESOURCE_UPDATE.name()))
                    .level(ErrorLevel.INFO.name())
                    .service(this.getServiceName())
                    .resource(this.getResourceEnName())
                    .action(this.getUpdateActionName(customActionName))
                    .name(eventName)
                    .result("ok")
                    .build();
            if (!StringUtils.isEmpty(customDescription))
            {
                operationEvent.setFriendlyDescription(customDescription);
            }

            if (!CollectionUtils.isEmpty(logs))
            {
                removeUselessChangeLogList(logs);
                if (logs.size() > 0)
                {
                    //状态值中英文描述
                    fillStateFieldDescription(logs);
                    operationEvent.setDescription(JsonUtils.toJson(logs));
                }
            }

            operationEvent.setRequestPath(ServiceCombRequestUtils.getRequestPath(null));
            operationEvent.setBpId(ServiceCombRequestUtils.getHeaderValue(UserHeadInfo.BPID, null));
            operationEvent.setUserId(ServiceCombRequestUtils.getHeaderValue(UserHeadInfo.USERID, null));
            operationEvent.setOperator(ServiceCombRequestUtils.getHeaderValue(UserHeadInfo.USERNAME, null));
        } catch (Throwable e)
        {
            log.error("创建资源更新事件异常! stackTrace: {}, errorMessage: {}", ExceptionUtils.getStackTrace(e), e.getMessage());
        }
        return operationEvent;
    }

    private void fillStateFieldDescription(LinkedHashMap<String, ObjectDiffLogger.PropertyValueChangeLog> logs)
    {
        log.debug("enter: fillStateFieldDescription");
        if (CollectionUtils.isEmpty(logs))
        {
            log.debug("logs is empty, skipped");
            return;
        }

        if (bizModelStateDesProvider == null)
        {
            log.debug("bizModelStateDesProvider is null, skipped");
            return;
        }

        if (bizModelStateDesProvider.getStateDesDict() == null)
        {
            log.debug("bizModelStateDesProvider.getStateDesDict() is null, skipped");
            return;
        }

        for (Map.Entry<String, ObjectDiffLogger.PropertyValueChangeLog> logEntry : logs.entrySet())
        {
            if (!bizModelStateDesProvider.getStateDesDict().containsKey(logEntry.getKey()))
            {
                //不需要对值进行额外的描述说明，跳过
                log.debug("bizModelStateDesProvider does not contains the description dict of field {}", logEntry.getKey());
                continue;
            }
            //获取该字段的描述字典
            Map<Integer, BizModelStateInfo> stateDesDictMap = bizModelStateDesProvider.getStateDesDict().get(logEntry.getKey());
            if (CollectionUtils.isEmpty(stateDesDictMap))
            {
                log.debug("stateDesDictMap is empty, skipped");
                continue;
            }

            ObjectDiffLogger.PropertyValueChangeLog propertyValueChangeLog = logEntry.getValue();

            if (propertyValueChangeLog == null)
            {
                log.debug("propertyValueChangeLog is null, skipped");
                continue;
            }

            //对旧值进行处理
            if (propertyValueChangeLog.getOldValue() != null)
            {
                //字典中包含该字段值的描述信息，尝试提取
                if (stateDesDictMap.containsKey(propertyValueChangeLog.getOldValue()))
                {
                    BizModelStateInfo bizModelStateInfo = stateDesDictMap.get(propertyValueChangeLog.getOldValue());
                    if (bizModelStateInfo != null)
                    {
                        propertyValueChangeLog.setOldValueEnDescription(bizModelStateInfo.getEnDescription());
                        propertyValueChangeLog.setOldValueCnDescription(bizModelStateInfo.getCnDescription());
                    }
                }
            }

            //对新值进行处理
            if (propertyValueChangeLog.getNewValue() != null)
            {
                //字典中包含该字段值的描述信息，尝试提取
                if (stateDesDictMap.containsKey(propertyValueChangeLog.getNewValue()))
                {
                    BizModelStateInfo bizModelStateInfo = stateDesDictMap.get(propertyValueChangeLog.getNewValue());
                    if (bizModelStateInfo != null)
                    {
                        propertyValueChangeLog.setNewValueEnDescription(bizModelStateInfo.getEnDescription());
                        propertyValueChangeLog.setNewValueCnDescription(bizModelStateInfo.getCnDescription());
                    }
                }
            }
        }
    }

    public OmcService.OperationEvent newDeleteEvent(String customResourceName, T before, T after, String action,
                                                    String customDescription)
    {
        OmcService.OperationEvent operationEvent = null;
        try
        {
            LinkedHashMap<String, ObjectDiffLogger.PropertyValueChangeLog> logs = ObjectDiffLogger.logObjectDiff(before, after);
            operationEvent = OmcService.OperationEvent.builder()
                    .id(Utils.assignUUId())
                    .triggerTime(new Date())
                    .type(OperationEventType.RESOURCE_DELETE.name())
                    .level(ErrorLevel.INFO.name())
                    .service(this.getServiceName())
                    .resource(this.getResourceEnName())
                    .action(this.getDeleteActionName(action))
                    .name(this.getDeleteEventName(customResourceName))
                    .result("ok")
                    .build();
            if (!StringUtils.isEmpty(customDescription))
            {
                operationEvent.setFriendlyDescription(customDescription);
            }

            if (!CollectionUtils.isEmpty(logs))
            {
                if (logs.size() > 0)
                {
                    //状态值中英文描述
                    fillStateFieldDescription(logs);
                    operationEvent.setDescription(JsonUtils.toJson(logs));
                }
            }

            operationEvent.setRequestPath(ServiceCombRequestUtils.getRequestPath(null));
            operationEvent.setBpId(ServiceCombRequestUtils.getHeaderValue(UserHeadInfo.BPID, null));
            operationEvent.setUserId(ServiceCombRequestUtils.getHeaderValue(UserHeadInfo.USERID, null));
            operationEvent.setOperator(ServiceCombRequestUtils.getHeaderValue(UserHeadInfo.USERNAME, null));
        } catch (Throwable e)
        {
            log.error("创建资源删除事件时异常! stackTrace: {}, errorMessage: {}", ExceptionUtils.getStackTrace(e), e.getMessage());
        }
        return operationEvent;
    }

    public abstract String getResourceInstanceId(T resource);

    public abstract String getResourceInstanceName(T resource);

    public String getActualResourceName()
    {
        return this.getResourceEnName();
    }

    public void publishCreateEvent(T resource, String friendlyDescription, String customResourceInstanceName, String customActionName)
    {
        try
        {
            OmcService.OperationEvent operationEvent = newCreateEvent(null, friendlyDescription, customActionName,
                    resource);
            if (resource != null)
            {
                operationEvent.setResourceInstId(getResourceInstanceId(resource));
                operationEvent.setResourceInstName(Optional.ofNullable(customResourceInstanceName)
                        .orElse(getResourceInstanceName(resource)));
            }

            writeToOMC(operationEvent);
        } catch (Exception e)
        {
            log.error("处理" + getActualResourceName() + "资源创建事件时出错, stackTrace: {}, errorMessage: {}", ExceptionUtils.getStackTrace(e), e.getMessage());
        }
    }

    public void publishUpdateEvent(String eventName, String customResourceInstanceName, boolean recordEmptyUpdate, T before, T after, String action,
                                   String customDescription)
    {
        try
        {
            OmcService.OperationEvent operationEvent = newUpdateEvent(eventName, action, null,
                    before, after, customDescription);
            operationEvent.setResourceInstId(getResourceInstanceId(before));
            operationEvent.setResourceInstName(Optional.ofNullable(customResourceInstanceName)
                    .orElse(getResourceInstanceName(before)));
            if (!recordEmptyUpdate)
            {
                if (StringUtils.isEmpty(operationEvent.getDescription()))
                {
                    log.debug("更新的内容为空, 已忽略当前更新事件的记录! {}", operationEvent);
                    return;
                }
            }
            writeToOMC(operationEvent);
        } catch (Exception e)
        {
            log.error("处理" + getActualResourceName() + "资源更新事件时出错, stackTrace: {}, errorMessage: {}", ExceptionUtils.getStackTrace(e), e.getMessage());
        }
    }

    private void writeToOMC(OmcService.OperationEvent operationEvent)
    {
        if (operationEvent == null)
        {
            return;
        }

        MessagePack messagePack = new MessagePack();
        messagePack.setMsgType("operationEvent");
        messagePack.setMessageObj(operationEvent);
        logOrEventSendProcessStrategy.sendMessage(messagePack);
    }

    public void publishDeleteEvent(T before, T after, String customResourceInstanceName, String action,
                                   String customDescription)
    {
        try
        {
            OmcService.OperationEvent operationEvent = newDeleteEvent(null, before, after, action,
                    customDescription);
            operationEvent.setResourceInstId(getResourceInstanceId(before));
            operationEvent.setResourceInstName(Optional.ofNullable(customResourceInstanceName)
                    .orElse(getResourceInstanceName(before)));
            writeToOMC(operationEvent);
        } catch (Exception e)
        {
            log.error("处理" + getActualResourceName() + "资源删除事件时出错, stackTrace: {}, errorMessage: {}", ExceptionUtils.getStackTrace(e), e.getMessage());
        }
    }

    private static final Set<String> USELESS_LOG_FIELD_SET = new HashSet<>();

    static
    {
        USELESS_LOG_FIELD_SET.add("createTime");
        USELESS_LOG_FIELD_SET.add("createdAt");
        USELESS_LOG_FIELD_SET.add("updateTime");
        USELESS_LOG_FIELD_SET.add("updatedAt");
        USELESS_LOG_FIELD_SET.add("reportTime");
        USELESS_LOG_FIELD_SET.add("failNum");
        USELESS_LOG_FIELD_SET.add("deletedAt");
    }

    public void removeUselessChangeLogList(LinkedHashMap<String, ObjectDiffLogger.PropertyValueChangeLog> logs)
    {
        if (CollectionUtils.isEmpty(logs))
        {
            return;
        }

        try
        {
            for (String fieldName : USELESS_LOG_FIELD_SET)
            {
                logs.remove(fieldName);
            }
        } catch (Exception e)
        {

        }
    }
}
