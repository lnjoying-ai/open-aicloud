package com.lnjoying.justice.omc.process.processor;

import com.fasterxml.jackson.core.type.TypeReference;
import com.lnjoying.justice.commonweb.util.JacksonUtils;
import com.lnjoying.justice.omc.common.OmcMsgType;
import com.lnjoying.justice.omc.db.model.TblOmcMonitorInstance;
import com.lnjoying.justice.omc.db.model.TblOmcMonitorTask;
import com.lnjoying.justice.omc.db.repo.MonitorTaskRepository;
import com.lnjoying.justice.omc.domain.model.ExporterStackDeploymentStatusEnum;
import com.lnjoying.justice.omc.domain.model.MonitorInstanceEndpointInfo;
import com.lnjoying.justice.omc.process.service.OmcMsgProcessStrategy;
import com.lnjoying.justice.schema.msg.MessagePack;
import com.micro.core.process.processor.AbstractRunnableProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/10/28 15:37
 */

@Slf4j
@Component
public class MonitorTaskTimerProcessor extends AbstractRunnableProcessor
{
    @Autowired
    private MonitorTaskRepository monitorTaskRepository;

    @Autowired
    private OmcMsgProcessStrategy omcMsgProcessStrategy;

    @Override
    public void start()
    {
       log.info("monitor task process started");
    }

    @Override
    public void stop()
    {
        log.info("monitor task process stopped");
    }

    @Override
    public void run()
    {

        try
        {
            loadPendingTask();
            checkStackRunningStatus();
            triggerPortMapping();
            addExporterScrapeTarget();
            // uninstall
            deleteExporterScrapeTarget();
            //removePortMapping();
            removeStack();
        }
        catch (Exception e)
        {
            log.error("monitor task error:{}", e);
        }

    }

    private void removeStack()
    {
        List<String> monitorInstances = monitorTaskRepository.selectIdByStatusAndTaskType(ExporterStackDeploymentStatusEnum.SCRAPE_TARGET_REMOVED.value(), null);
        if (CollectionUtils.isEmpty(monitorInstances))
        {
            return;
        }

        for (String monitorInstanceId : monitorInstances)
        {
            MessagePack messagePack = new MessagePack();
            messagePack.setMsgType(OmcMsgType.REMOVE_STACK);
            messagePack.setMessageObj(monitorInstanceId);
            omcMsgProcessStrategy.sendMessage(messagePack);
        }
    }

    private void removePortMapping()
    {
        List<String> monitorInstances = monitorTaskRepository.selectIdByStatusAndTaskType(ExporterStackDeploymentStatusEnum.SCRAPE_TARGET_REMOVED.value(), null);
        if (CollectionUtils.isEmpty(monitorInstances))
        {
            return;
        }

        for (String monitorInstanceId : monitorInstances)
        {
            MessagePack messagePack = new MessagePack();
            messagePack.setMsgType(OmcMsgType.REMOVE_PORT_MAPPING);
            messagePack.setMessageObj(monitorInstanceId);
            omcMsgProcessStrategy.sendMessage(messagePack);
        }
    }

    private void deleteExporterScrapeTarget()
    {
        List<String> monitorInstances = monitorTaskRepository.selectIdByStatusAndTaskType(ExporterStackDeploymentStatusEnum.TERMINATING.value(), null);
        if (CollectionUtils.isEmpty(monitorInstances))
        {
            return;
        }

        for (String monitorInstanceId : monitorInstances)
        {
            MessagePack messagePack = new MessagePack();
            messagePack.setMsgType(OmcMsgType.DELETE_EXPORTER_SCRAPE_TARGET);
            messagePack.setMessageObj(monitorInstanceId);
            omcMsgProcessStrategy.sendMessage(messagePack);
        }
    }

    private void addExporterScrapeTarget()
    {
        List<String> monitorInstances = monitorTaskRepository.selectIdByStatusAndTaskType(ExporterStackDeploymentStatusEnum.PORT_MAPPING_SUCCEEDED.value(), null);
        if (CollectionUtils.isEmpty(monitorInstances))
        {
            return;
        }



        for (String monitorInstanceId : monitorInstances)
        {
            if(checkEndpoints(monitorInstanceId))
            {
                MessagePack messagePack = new MessagePack();
                messagePack.setMsgType(OmcMsgType.ADD_EXPORTER_SCRAPE_TARGET);
                messagePack.setMessageObj(monitorInstanceId);
                omcMsgProcessStrategy.sendMessage(messagePack);
            }

        }
    }

    private boolean checkEndpoints(String monitorInstanceId)
    {
        TblOmcMonitorInstance tblOmcMonitorInstance = monitorTaskRepository.selectMonitorInstance(monitorInstanceId);
        if (Objects.nonNull(tblOmcMonitorInstance))
        {
            // Check for situations where multiple ports need to be exposed
            String endpointInDb = tblOmcMonitorInstance.getEndpoint();
            List<MonitorInstanceEndpointInfo> monitorInstanceEndpointInfos = JacksonUtils.strToObjTypeDefault(endpointInDb, new TypeReference<List<MonitorInstanceEndpointInfo>>()
            {
            });
            if (!CollectionUtils.isEmpty(monitorInstanceEndpointInfos))
            {
                // Statistics on how many ports have not been mapped yet
                long needMappedPort =
                        monitorInstanceEndpointInfos.stream().filter(monitorInstanceEndpointInfo -> StringUtils.isEmpty(monitorInstanceEndpointInfo.getMappedEndpoint())).count();

                if (needMappedPort == 0)
                {
                    return true;
                }

            }
        }

        return false;

    }

    private void triggerPortMapping()
    {
        try
        {
            List<String> monitorInstances = monitorTaskRepository.selectIdByStatusAndTaskType(ExporterStackDeploymentStatusEnum.RUNNING.value(), null);
            if (CollectionUtils.isEmpty(monitorInstances))
            {
                return;
            }

            for (String monitorInstanceId : monitorInstances)
            {
                MessagePack messagePack = new MessagePack();
                messagePack.setMsgType(OmcMsgType.PORT_MAPPING);
                messagePack.setMessageObj(monitorInstanceId);
                omcMsgProcessStrategy.sendMessage(messagePack);
            }
        }
        catch (Exception e)
        {
            log.error("port mapping failed:{}", e);
        }
    }

    private void checkStackRunningStatus()
    {
        try
        {
            List<String> monitorInstances = monitorTaskRepository.selectIdByStatusAndTaskType(ExporterStackDeploymentStatusEnum.PROGRESSING.value(), null);
            if (CollectionUtils.isEmpty(monitorInstances))
            {
                return;
            }

            for (String monitorInstanceId : monitorInstances)
            {
                MessagePack messagePack = new MessagePack();
                messagePack.setMsgType(OmcMsgType.CHECK_STACK_RUNNING_STATUS);
                messagePack.setMessageObj(monitorInstanceId);
                omcMsgProcessStrategy.sendMessage(messagePack);
            }
        }
        catch (Exception e)
        {
            log.error("check statck status error:{}", e);
        }
    }

    private void loadPendingTask()
    {
        try
        {
            List<String> monitorInstances = monitorTaskRepository.selectIdByStatusAndTaskType(ExporterStackDeploymentStatusEnum.PENDING.value(), null);
            if (CollectionUtils.isEmpty(monitorInstances))
            {
                return;
            }

            for (String monitorInstanceId : monitorInstances)
            {
                MessagePack messagePack = new MessagePack();
                messagePack.setMsgType(OmcMsgType.BUILD_STACK);
                messagePack.setMessageObj(monitorInstanceId);
                omcMsgProcessStrategy.sendMessage(messagePack);
            }
        }
        catch (Exception e)
        {
            log.error("load pending task error:{}", e);
        }
    }


}
