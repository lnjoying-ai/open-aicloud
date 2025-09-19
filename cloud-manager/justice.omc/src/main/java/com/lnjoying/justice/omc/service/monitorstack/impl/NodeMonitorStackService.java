package com.lnjoying.justice.omc.service.monitorstack.impl;

import com.google.common.collect.Lists;
import com.lnjoying.justice.commonweb.util.YamlConfigUtils;
import com.lnjoying.justice.omc.domain.model.IntegrationTaskTypeEnum;
import com.lnjoying.justice.omc.prometheus.grafana.GrafanaDashboardUrlStore;
import com.lnjoying.justice.omc.service.AosStackTemplateService;
import com.lnjoying.justice.omc.service.monitorstack.BaseMonitorStackService;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.service.aos.AosTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import static com.lnjoying.justice.commonweb.util.FileUtils.FILE_SEPARATOR;
import static com.lnjoying.justice.omc.domain.model.IntegrationTaskTypeEnum.LIGHTWEIGHT_NODE_DEPLOYMENT_TASK;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/10/27 19:35
 */

@Slf4j
@Service
public class NodeMonitorStackService implements BaseMonitorStackService
{


    public static final String NODE_MONITOR_STACK_TEMPLATE = "node-monitor-system-default-stack";
    public static final String NVIDIA_GPU_MONITOR_STACK_TEMPLATE = "nvidia-gpu-monitor-system-default-stack";
    public static final String PROMETHEUS_AGENT_MONITOR_STACK_TEMPLATE = "prometheus-agent-monitor-system-default-stack";


    @Autowired
    private AosStackTemplateService aosTemplateService;

    @Autowired
    private GrafanaDashboardUrlStore grafanaDashboardUrlStore;

    public void checkMonitorStackTemplate()
    {
        doCheckStackTemplate(NODE_MONITOR_STACK_TEMPLATE, "轻量级节点exporter应用，包含node exporter和cAdvisor exporter，用于节点和容器监控数据的采集。" , Lists.newArrayList("prometheus_monitor",
                "service_light_node", "ANY_IN_SITE"));
    }

    public void doCheckStackTemplate(String name, String description, List<String> labels)
    {
        // If not in the db, create it from the template file
        int count = aosTemplateService.countStackByName(name);
        if (count > 0)
        {
            log.warn("node monitor stack template already deployed");
            return;
        }

        try
        {
            List<String> res = YamlConfigUtils.readMultiYamlConfig(getStackTemplateConfigPath(name + ".yaml"));
            if (!CollectionUtils.isEmpty(res) && res.size() == 2)
            {
                String dockerCompose = res.get(0);
                String justiceCompose = res.get(1);
                AosTemplateService.StackTemplateReq addTemplateReq = buildAddStackTemplateReq(name, dockerCompose, justiceCompose, description, labels);
                int resultCode = aosTemplateService.addStackTemplate(addTemplateReq);
                if (!ErrorCode.SUCCESS.equals(resultCode))
                {
                    log.error("add {} stack template error, result code:{}", NODE_MONITOR_STACK_TEMPLATE, resultCode);
                }
            }
            else
            {
                log.error("node template parse failed:{}");
            }
        }
        catch (Exception e)
        {
            log.error("add {} stack template error:{}", NODE_MONITOR_STACK_TEMPLATE, e );
        }
    }



    public void checkPrometheusAgentMonitorStackTemplate()
    {
    }

    private AosTemplateService.StackTemplateReq buildAddStackTemplateReq(String name, String dockerCompose, String justiceCompose, String description, List<String> labels)
    {
        AosTemplateService.StackTemplateReq addTemplateReq = new  AosTemplateService.StackTemplateReq();
        addTemplateReq.setName(name);
        addTemplateReq.setVersion("v1");
        addTemplateReq.setDescription(description);
        addTemplateReq.setLogo_url("");
        addTemplateReq.setVendor("system");
        addTemplateReq.setAos_type("docker-compose");
        addTemplateReq.setContent_type("yaml");
        addTemplateReq.setStack_compose(dockerCompose);
        addTemplateReq.setJustice_compose(justiceCompose);
        if (!CollectionUtils.isEmpty(labels))
        {
            addTemplateReq.setLabels(labels);
        }

        addTemplateReq.setUserId(ADMIN_USER_ID);
        return addTemplateReq;
    }

    @Override
    public String getMonitorStackTemplateName()
    {
        return NODE_MONITOR_STACK_TEMPLATE;
    }

    @Override
    public IntegrationTaskTypeEnum getIntegrationTaskType()
    {
        return LIGHTWEIGHT_NODE_DEPLOYMENT_TASK;
    }

    public static String getStackTemplateConfigPath(String configPath)
    {
        return  "stack_template" + FILE_SEPARATOR + configPath;
    }

}
