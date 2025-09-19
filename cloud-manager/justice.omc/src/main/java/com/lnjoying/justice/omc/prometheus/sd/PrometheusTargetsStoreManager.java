package com.lnjoying.justice.omc.prometheus.sd;

import com.fasterxml.jackson.core.type.TypeReference;
import com.lnjoying.justice.commonweb.util.JacksonUtils;
import com.lnjoying.justice.omc.db.model.TblOmcMonitorInstance;
import com.lnjoying.justice.omc.db.repo.MonitorTaskRepository;
import com.lnjoying.justice.omc.domain.model.ExporterStackDeploymentStatusEnum;
import com.lnjoying.justice.omc.domain.model.MonitorInstanceEndpointInfo;
import com.lnjoying.justice.omc.prometheus.handler.MonitorEndpointInfoHandler;
import com.lnjoying.justice.omc.service.CombRpcService;
import com.lnjoying.justice.omc.service.IntegrationTaskService;
import com.lnjoying.justice.schema.entity.omc.MonitorEndpointInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/11/3 20:14
 */

@Component
@Slf4j
public class PrometheusTargetsStoreManager
{

    @Autowired
    private List<MonitorEndpointInfoHandler> monitorEndpointInfoHandlers;

    private Map<String, PrometheusTargetsStore> prometheusTargetsStores = new ConcurrentHashMap<>();

    private static final int PAGE_SIZE = 50;

    @Autowired
    private MonitorTaskRepository monitorTaskRepository;

    @Autowired
    private IntegrationTaskService integrationTaskService;

    @Autowired
    private CombRpcService combRpcService;

    @Autowired
    private PrometheusTargetsStoreManager storeManager;

    public Optional<MonitorEndpointInfoHandler> getEndpointInfoHandler(String type)
    {
        return monitorEndpointInfoHandlers.stream().filter(handler -> handler.getType().equals(type)).findFirst();
    }


    public Map<String, PrometheusTargetsStore> getPrometheusTargetsStores()
    {
        return prometheusTargetsStores;
    }

    public PrometheusTargetsStore getPrometheusTargetsStore(String type)
    {
        return this.getPrometheusTargetsStores().computeIfAbsent(type, key -> new PrometheusTargetsStore());
    }

    //@PostConstruct
    public void init()
    {
        try
        {
            int startOffset = 1;
            while(true)
            {
                List<TblOmcMonitorInstance> integrationList = integrationTaskService.getIntegrationList(null, ExporterStackDeploymentStatusEnum.CREATE_DAEMONSET_STACK_SUCCEEDED.value(), startOffset++,
                        PAGE_SIZE);
                if (!CollectionUtils.isEmpty(integrationList))
                {
                    for (TblOmcMonitorInstance tblOmcMonitorInstance : integrationList)
                    {
                        handleMonitorEndpointList(tblOmcMonitorInstance);

                        TimeUnit.SECONDS.sleep(1);
                    }
                }
                else
                {
                    break;
                }
            }


        }
        catch (Exception e)
        {
            log.error("init prometheus target store error:{}", e);
        }
    }

    private void handleMonitorEndpointList(TblOmcMonitorInstance tblOmcMonitorInstance)
    {
        try
        {
            List<MonitorEndpointInfo> monitorEndpointList = combRpcService.getAosStackService().getMonitorEndpointList(tblOmcMonitorInstance.getSpecId());
            if (!CollectionUtils.isEmpty(monitorEndpointList))
            {

                monitorEndpointList.stream().forEach(monitorInstanceEndpointInfo ->
                {
                    String type = monitorInstanceEndpointInfo.getType();
                    boolean successful = storeManager.getPrometheusTargetsStore(type).addTarget(monitorInstanceEndpointInfo.getBpId(), monitorInstanceEndpointInfo.getUserId(),
                            monitorInstanceEndpointInfo.getSiteId(), monitorInstanceEndpointInfo.getNodeId(), monitorInstanceEndpointInfo.getEndpoint(), monitorInstanceEndpointInfo.getUniqueId());
                   // this.getEndpointInfoHandler(type).ifPresent(handler -> handler.addMonitorEndpoint(monitorInstanceEndpointInfo));
                    if (successful)
                    {
                        log.info("add target successful,type:{},bpId:{},userId:{},siteId:{},nodeId:{},endpoint:{},uniqueId:{}", type, monitorInstanceEndpointInfo.getBpId(), monitorInstanceEndpointInfo.getUserId(),
                                monitorInstanceEndpointInfo.getSiteId(), monitorInstanceEndpointInfo.getNodeId(), monitorInstanceEndpointInfo.getEndpoint(), monitorInstanceEndpointInfo.getUniqueId());
                    }
                    else
                    {
                        log.warn("add target failed,type:{},bpId:{},userId:{},siteId:{},nodeId:{},endpoint:{},uniqueId:{}", type, monitorInstanceEndpointInfo.getBpId(),
                                monitorInstanceEndpointInfo.getUserId(),
                                monitorInstanceEndpointInfo.getSiteId(), monitorInstanceEndpointInfo.getNodeId(), monitorInstanceEndpointInfo.getEndpoint(), monitorInstanceEndpointInfo.getUniqueId());
                    }
                });


            }
        }
        catch (Exception e)
        {
            log.error("handle monitor endpoint list error:{}", e);
        }

    }


}
