package com.lnjoying.justice.omc.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.commonweb.util.JacksonUtils;
import com.lnjoying.justice.omc.db.model.TblOmcMonitorInstance;
import com.lnjoying.justice.omc.db.model.TblOmcMonitorTask;
import com.lnjoying.justice.omc.db.model.TblOmcPrometheus;
import com.lnjoying.justice.omc.db.repo.MonitorTaskRepository;
import com.lnjoying.justice.omc.db.repo.PrometheusInstanceRepository;
import com.lnjoying.justice.omc.domain.dto.req.AddIntegrationTaskReq;
import com.lnjoying.justice.omc.domain.dto.req.PatchPrometheusReq;
import com.lnjoying.justice.omc.domain.dto.rsp.*;
import com.lnjoying.justice.omc.domain.model.*;
import com.lnjoying.justice.omc.prometheus.client.PrometheusClient;
import com.lnjoying.justice.omc.prometheus.config.PromConfigHelper;
import com.lnjoying.justice.omc.prometheus.grafana.GrafanaDashboardUrlStore;
import com.lnjoying.justice.omc.prometheus.sd.PrometheusTargetsStoreManager;
import com.lnjoying.justice.omc.service.CombRpcService;
import com.lnjoying.justice.omc.service.IntegrationTaskService;
import com.lnjoying.justice.omc.service.monitorstack.BaseMonitorStackService;
import com.lnjoying.justice.omc.service.monitorstack.MonitorStackFactory;
import com.lnjoying.justice.omc.util.AlarmUtils;
import com.lnjoying.justice.schema.common.CombRetPacket;
import com.lnjoying.justice.schema.common.ErrorLevel;
import com.lnjoying.justice.schema.common.scheduler.LabelType;
import com.lnjoying.justice.schema.entity.aos.AddStackReq;
import com.lnjoying.justice.schema.entity.aos.StackDeploySimpleInfo;
import com.lnjoying.justice.schema.entity.dev.LabelSelector;
import com.lnjoying.justice.schema.entity.dev.SchedulingStrategy;
import com.lnjoying.justice.schema.entity.dev.TargetNode;
import com.lnjoying.justice.schema.service.aos.AosTemplateService;
import com.lnjoying.justice.schema.service.ecrm.RegionResourceService;
import com.micro.core.common.Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.servicecomb.swagger.invocation.exception.InvocationException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

import static com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils.*;
import static com.lnjoying.justice.omc.domain.model.IntegrationActions.REINSTALL;
import static com.lnjoying.justice.omc.domain.model.IntegrationActions.UNINSTALL;
import static com.lnjoying.justice.omc.domain.model.IntegrationTaskTypeEnum.*;
import static com.lnjoying.justice.schema.common.ErrorCode.*;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/10/25 15:24
 */

@Service
@Slf4j
public class IntegrationTaskServiceImpl implements IntegrationTaskService
{
    @Autowired
    private MonitorTaskRepository monitorTaskRepository;

    @Autowired
    private MonitorStackFactory monitorTaskFactory;

    @Autowired
    private CombRpcService combRpcService;

    @Autowired
    private PrometheusInstanceRepository prometheusInstanceRepository;

    @Autowired
    private PrometheusTargetsStoreManager storeManager;

    @Autowired
    private GrafanaDashboardUrlStore dashboardUrlStore;

    @Autowired
    private PrometheusClient prometheusClient;

    @Override
    @Transactional
    public Object addIntegrationTask(AddIntegrationTaskReq req)
    {
        checkParams(req);

        TblOmcMonitorTask tblOmcMonitorTask = doBuildTblOmcMonitorTask(req);

        doBuildMonitorInstances(tblOmcMonitorTask);

        return tblOmcMonitorTask.getId();
    }

    @Override
    public Object getIntegrationParams(Integer integrationType)
    {
        IntegrationTaskTypeEnum integrationTaskTypeEnum = IntegrationTaskTypeEnum.fromValue(integrationType);

        BaseMonitorStackService monitorStackService = monitorTaskFactory.getServiceForIntegrationType(integrationTaskTypeEnum);
        AosTemplateService.StackTemplateParamsReq stackTemplateParamsByName =
                combRpcService.getAosTemplateService().getStackTemplateParamsByName(monitorStackService.getMonitorStackTemplateName());

        return stackTemplateParamsByName;
    }

    @Override
    public IntegrationsRsp getIntegrationList(Integer integrationType, String queryBpId, String queryUserId, Integer pageNum, Integer pageSize)
    {
        PageHelper.startPage(pageNum, pageSize);
        List<TblOmcMonitorInstance> tblOmcMonitorInstances = monitorTaskRepository.selectAll(queryBpId, queryUserId, null, integrationType);
        PageInfo<TblOmcMonitorInstance> pageInfo = new PageInfo<>(tblOmcMonitorInstances);

        return IntegrationsRsp.builder().totalNum(pageInfo.getTotal())
                .integrations(tblOmcMonitorInstances.parallelStream().map(MonitorIntegration::of)
                        .map(monitorIntegration -> assembleInfo(monitorIntegration))
                        .map(monitorIntegration -> assembleSpecInfo(monitorIntegration))
                        .map(monitorIntegration -> {
                            monitorIntegration.setDataSourceName(assembleDataSourceInfo(monitorIntegration.getDataSourceId()));
                            return monitorIntegration;
                        })
                        .map(instance -> BaseRsp.assembleUserInfo(instance.getBpId(), instance.getUserId(), instance, combRpcService.getUmsService()))
                        .collect(Collectors.toList())).build();
    }

    @Override
    public IntegrationTargetsRsp getIntegrationTargetList(Integer targetType, String queryBpId, String queryUserId, String nodeName, String bpId, String s, Integer pageNum,
                                                          Integer pageSize)
    {
        ExporterTargetTypeEnum exporterTargetTypeEnum = ExporterTargetTypeEnum.fromValue(targetType);
        switch (exporterTargetTypeEnum)
        {
            case LIGHTWEIGHT_NODE:
                return doGetIntegrationNodeList(targetType, queryBpId, queryUserId, pageNum, pageSize);
            case NEXTSTACK:
                return null;
            case OPENSTACK:
                return null;
            default:
                return null;
        }
    }



    @Override
    public List<TblOmcMonitorInstance> getIntegrationList(Integer integrationType, Integer status, Integer pageNum, Integer pageSize)
    {
        PageHelper.startPage(pageNum, pageSize);
        List<TblOmcMonitorInstance> tblOmcMonitorInstances = monitorTaskRepository.selectAll(null, null, status, integrationType);
        return tblOmcMonitorInstances;
    }


    private void doBuildMonitorInstances(TblOmcMonitorTask tblOmcMonitorTask)
    {
        try
        {
            Integer targetType = tblOmcMonitorTask.getTargetType();
            Integer taskType = tblOmcMonitorTask.getTaskType();
            String bpId = tblOmcMonitorTask.getBpId();
            String userId = tblOmcMonitorTask.getUserId();
            ExporterTargetTypeEnum exporterTargetTypeEnum = ExporterTargetTypeEnum.fromValue(targetType);

            switch (exporterTargetTypeEnum)
            {
                case LIGHTWEIGHT_NODE:
                case SITE:
                    List<String> targets =  (List<String>)tblOmcMonitorTask.getTargets();

                    TblOmcMonitorInstance tblOmcMonitorInstance = new TblOmcMonitorInstance();
                    tblOmcMonitorInstance.setId(Utils.assignUUId());
                    genName(tblOmcMonitorTask, tblOmcMonitorInstance);
                    tblOmcMonitorInstance.setTaskId(tblOmcMonitorTask.getId());
                    tblOmcMonitorInstance.setTaskType(taskType);
                    tblOmcMonitorInstance.setExporterType("");
                    tblOmcMonitorInstance.setStackId(null);
                    tblOmcMonitorInstance.setLabels(null);
                    tblOmcMonitorInstance.setEndpoint("");
                    tblOmcMonitorInstance.setRegionId(null);
                    tblOmcMonitorInstance.setDataSourceId(tblOmcMonitorTask.getDataSourceId());

                    tblOmcMonitorInstance.setTargetType(targetType);
                    //tblOmcMonitorInstance.setTargetId();
                    tblOmcMonitorInstance.setTargets(targets);
                    tblOmcMonitorInstance.setStatus(ExporterStackDeploymentStatusEnum.PENDING.value());
                    tblOmcMonitorInstance.setBpId(bpId);
                    tblOmcMonitorInstance.setUserId(userId);
                    tblOmcMonitorInstance.setCreateTime(new Date());
                    tblOmcMonitorInstance.setUpdateTime(tblOmcMonitorInstance.getCreateTime());
                    tblOmcMonitorInstance.setStackTemplateVersionId(tblOmcMonitorTask.getStackTemplateVersionId());
                    List<Map<String, String>> stackParams = (List<Map<String, String>>) tblOmcMonitorTask.getStackParams();
                    if (!CollectionUtils.isEmpty(stackParams))
                    {
                        Map<String, String> inputParamMap = new HashMap<>();
                        stackParams.stream().forEach(variableMap -> {
                            inputParamMap.put(variableMap.get("variable"), variableMap.get("default_value"));
                        });
                        tblOmcMonitorInstance.setStackParams(inputParamMap);
                    }

                    monitorTaskRepository.insertMonitorInstanceSelective(tblOmcMonitorInstance);

                    break;
                case OPENSTACK:
                case NEXTSTACK:
                    break;




            }
        }
        catch (Exception e)
        {
            log.error("processBuildStack failed:{}", e);
        }
    }

    @Override
    public void processBuildStack(String monitorInstanceId)
    {
        TblOmcMonitorInstance tblOmcMonitorInstance = monitorTaskRepository.selectMonitorInstance(monitorInstanceId);
        if (Objects.isNull(tblOmcMonitorInstance))
        {
            return ;
        }

        AddStackReq addStackReq = new AddStackReq();
        addStackReq.setReplica_num(1);
        addStackReq.setName(tblOmcMonitorInstance.getName());
        addStackReq.setDescription(tblOmcMonitorInstance.getId());
        addStackReq.setTemplate_id(tblOmcMonitorInstance.getStackTemplateVersionId());

        String stackParams = (String)tblOmcMonitorInstance.getStackParams();
        Map<String, String> inputParams = JacksonUtils.strToObjTypeDefault(stackParams, new TypeReference<Map<String, String>>() {});
        addStackReq.setInput_params(inputParams);
        addStackReq.setLabels(null);
        addStackReq.setAuto_run(true);
        addStackReq.setBp_id(tblOmcMonitorInstance.getBpId());
        addStackReq.setUser_id(tblOmcMonitorInstance.getUserId());
        addStackReq.setStack_type("daemonset");
        addStackReq.setUse_service_penetration(true);
        addStackReq.setDeploy_strategy(1);
        Map<String, String> extraParams = new HashMap<>();
        // image_pull_policy":"IfNotPresent"}
        extraParams.put("penetration_purpose", "inner_monitor");
        extraParams.put("image_pull_policy", "IfNotPresent");
        addStackReq.setExtra_params(extraParams);

        SchedulingStrategy schedulingStrategy = new SchedulingStrategy();
        schedulingStrategy.setLabelSelectorMap(new HashMap<>());
        List<LabelSelector> nodeLabelSelector = schedulingStrategy.getLabelSelectorMap().getOrDefault(LabelType.NODE.getValue(), new ArrayList<>());
        nodeLabelSelector.add(new LabelSelector("Must", "In", "NO_MONITOR", "false"));
        addStackReq.setScheduling_strategy(schedulingStrategy);
        schedulingStrategy.getLabelSelectorMap().put(LabelType.NODE.getValue(), nodeLabelSelector);

        String targets = (String)tblOmcMonitorInstance.getTargets();
        List<String> targetList = JacksonUtils.strToObjTypeDefault(targets, new TypeReference<List<String>>()
        {
        });

        // if the targets is empty all sites
        List<TargetNode> targetNodeList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(targetList))
        {
            targetNodeList = targetList.stream().map(target ->
            {
                TargetNode targetNode = new TargetNode("", target, "");
                return targetNode;
            }).collect(Collectors.toList());
        }

        addStackReq.setTarget_nodes(targetNodeList);

        try
        {
            String stackSpec = combRpcService.getAosStackService().createStackSpec(addStackReq);
            if (StringUtils.isNotBlank(stackSpec))
            {
                updateOmcMonitorInstance(monitorInstanceId, stackSpec, ExporterStackDeploymentStatusEnum.CREATE_DAEMONSET_STACK_SUCCEEDED.value());
            }
        }
        catch (Exception e)
        {
            if (e instanceof InvocationException)
            {
                Object errorData = ((InvocationException) e).getErrorData();
                if (errorData instanceof LinkedHashMap)
                {
                    LinkedHashMap<String, Object> errorDataMap = (LinkedHashMap<String, Object>) errorData;
                    if (!CollectionUtils.isEmpty(errorDataMap))
                    {
                        if (STACK_DUP.getCode() == (int)errorDataMap.get("code"))
                        {
                            updateOmcMonitorInstance(monitorInstanceId, null, ExporterStackDeploymentStatusEnum.CREATE_DAEMONSET_STACK_SUCCEEDED.value());
                        }
                    }
                }
                else
                {
                    log.error("processBuildStack failed:{}", e);
                }


            }
        }

    }

    private void updateOmcMonitorInstance(String monitorInstanceId, String specId, int status)
    {
        log.info("build daemonset stack for:{}, status:{}", monitorInstanceId, status);
        TblOmcMonitorInstance updateTblOmcMonitorInstance = new TblOmcMonitorInstance();
        updateTblOmcMonitorInstance.setId(monitorInstanceId);
        updateTblOmcMonitorInstance.setStatus(status);
        updateTblOmcMonitorInstance.setUpdateTime(new Date());
        updateTblOmcMonitorInstance.setSpecId(specId);
        monitorTaskRepository.updateMonitorInstance(updateTblOmcMonitorInstance);
    }

    @Override
    public void processCheckStackRunningStatus(String monitorInstanceId)
    {

        TblOmcMonitorInstance tblOmcMonitorInstance = monitorTaskRepository.selectMonitorInstance(monitorInstanceId);
        if (Objects.isNull(tblOmcMonitorInstance))
        {
            return ;
        }

        boolean isStackRunning  = combRpcService.getAosStackService().isStackRunningStatus(tblOmcMonitorInstance.getStackId());
        if (isStackRunning)
        {
            TblOmcMonitorInstance updateTblOmcMonitorInstance = new TblOmcMonitorInstance();
            updateTblOmcMonitorInstance.setId(monitorInstanceId);
            updateTblOmcMonitorInstance.setStatus(ExporterStackDeploymentStatusEnum.RUNNING.value());
            List<MonitorInstanceEndpointInfo> portList = doGetPortList(tblOmcMonitorInstance);
            if (!CollectionUtils.isEmpty(portList))
            {
                updateTblOmcMonitorInstance.setEndpoint(JacksonUtils.objToStrDefault(portList));
            }
            updateTblOmcMonitorInstance.setUpdateTime(new Date());
            monitorTaskRepository.updateMonitorInstance(updateTblOmcMonitorInstance);
        }
        else
        {
            log.info("monitorInstanceId:{} stack:{} not running state", monitorInstanceId, tblOmcMonitorInstance.getStackId());
        }
    }

    @Override
    public void processPortMapping(String monitorInstanceId)
    {

        doProcessPortMapping(monitorInstanceId);

    }

    @Deprecated
    @Transactional(rollbackFor = Exception.class)
    public void doProcessPortMapping(String monitorInstanceId)
    {
        TblOmcMonitorInstance tblOmcMonitorInstance = monitorTaskRepository.selectMonitorInstanceForUpdate(monitorInstanceId);
        if (Objects.isNull(tblOmcMonitorInstance))
        {
            return;
        }
        if (tblOmcMonitorInstance.getStatus()!= ExporterStackDeploymentStatusEnum.RUNNING.value())
        {
            return;
        }

        List<MonitorInstanceEndpointInfo> monitorInstanceEndpointInfos = JacksonUtils.strToObjTypeDefault(tblOmcMonitorInstance.getEndpoint(),
                new TypeReference<List<MonitorInstanceEndpointInfo>>()
        {
        });
        if (!CollectionUtils.isEmpty(monitorInstanceEndpointInfos))
        {
            int sum = monitorInstanceEndpointInfos.stream().mapToInt(monitorInstanceEndpointInfo ->
            {
                Integer originalPort = monitorInstanceEndpointInfo.getOriginalPort();
                String monitorIdPort = tblOmcMonitorInstance.getId() + "-" + String.valueOf(originalPort);
                // monitorIdPort, tblOmcMonitorInstance.getNodeId(), originalPort
                CombRetPacket combRetPacket = combRpcService.getServiceManagerService().addServicePort(null);
                log.info("add server port:{}, status:{},message:{}", originalPort, combRetPacket.getStatus(), combRetPacket.getObj());
                if (combRetPacket.getStatus() != null && combRetPacket.getStatus() == 0)
                {
                    return 1;
                }
                else
                {
                    return 0;
                }
            }).sum();


            if (sum == monitorInstanceEndpointInfos.size())
            {
                log.warn("add port mapping success, number of needs:{},  number of successes:{}", monitorInstanceEndpointInfos.size(), sum);
            }
            else
            {
                log.warn("add port mapping number of needs:{},  number of successes:{}", monitorInstanceEndpointInfos.size(), sum);
            }

        }
    }


    @Override
    public void processAddExporterScrapeTarget(String monitorInstanceId)
    {
        TblOmcMonitorInstance tblOmcMonitorInstance = monitorTaskRepository.selectMonitorInstance(monitorInstanceId);
        if (Objects.isNull(tblOmcMonitorInstance))
        {
            return ;
        }

        String endpoints = tblOmcMonitorInstance.getEndpoint();
        if (StringUtils.isNotBlank(endpoints))
        {
            List<MonitorInstanceEndpointInfo> monitorInstanceEndpointInfos = JacksonUtils.strToObjTypeDefault(endpoints, new TypeReference<List<MonitorInstanceEndpointInfo>>()
            {
            });
            if (!CollectionUtils.isEmpty(monitorInstanceEndpointInfos))
            {
                boolean allSuccessful = monitorInstanceEndpointInfos.stream().allMatch(monitorEndpointInfo -> {
                    String name = monitorEndpointInfo.getName();
                    boolean successful = storeManager.getPrometheusTargetsStore(name).addTarget(tblOmcMonitorInstance.getBpId(), tblOmcMonitorInstance.getUserId(),
                            tblOmcMonitorInstance.getSiteId(),
                            tblOmcMonitorInstance.getNodeId(), monitorEndpointInfo.getMappedEndpoint(), "");

                    // todo
                    return successful;
                });

                if (allSuccessful)
                {
                    TblOmcMonitorInstance updateTblOmcMonitorInstance = new TblOmcMonitorInstance();
                    updateTblOmcMonitorInstance.setId(monitorInstanceId);
                    updateTblOmcMonitorInstance.setStatus(ExporterStackDeploymentStatusEnum.SUCCEEDED.value());
                    updateTblOmcMonitorInstance.setUpdateTime(new Date());
                    monitorTaskRepository.updateMonitorInstance(updateTblOmcMonitorInstance);
                }

            }



        }


    }

    @Override
    public void processDeleteExporterScrapeTarget(String monitorInstanceId)
    {
        TblOmcMonitorInstance tblOmcMonitorInstance = monitorTaskRepository.selectMonitorInstance(monitorInstanceId);
        if (Objects.isNull(tblOmcMonitorInstance))
        {
            return ;
        }

        String endpoints = tblOmcMonitorInstance.getEndpoint();

        if (StringUtils.isNotBlank(endpoints))
        {
            String[] endpointArr = endpoints.split(",");
            Arrays.stream(endpointArr).forEach(endpoint ->
            {
                storeManager.getPrometheusTargetsStore("").deleteTarget(tblOmcMonitorInstance.getBpId(), tblOmcMonitorInstance.getUserId(), endpoint);
            });
        }
        TblOmcMonitorInstance updateTblOmcMonitorInstance = new TblOmcMonitorInstance();
        updateTblOmcMonitorInstance.setId(monitorInstanceId);
        updateTblOmcMonitorInstance.setStatus(ExporterStackDeploymentStatusEnum.SCRAPE_TARGET_REMOVED.value());
        updateTblOmcMonitorInstance.setUpdateTime(new Date());
        monitorTaskRepository.updateMonitorInstance(updateTblOmcMonitorInstance);

    }

    @Override
    public void processRemovePortMapping(String monitorInstanceId)
    {
        TblOmcMonitorInstance tblOmcMonitorInstance = monitorTaskRepository.selectMonitorInstance(monitorInstanceId);
        if (Objects.isNull(tblOmcMonitorInstance))
        {
            return ;
        }

        String endpoints = tblOmcMonitorInstance.getEndpoint();
        if (StringUtils.isNotBlank(endpoints))
        {
            List<MonitorInstanceEndpointInfo> monitorInstanceEndpointInfos = JacksonUtils.strToObjTypeDefault(endpoints, new TypeReference<List<MonitorInstanceEndpointInfo>>()
            {
            });
            if (!CollectionUtils.isEmpty(monitorInstanceEndpointInfos))
            {
                boolean allSuccessful = monitorInstanceEndpointInfos.stream().allMatch(monitorInstanceEndpointInfo -> {
                    Integer originalPort = monitorInstanceEndpointInfo.getOriginalPort();

                    CombRetPacket combRetPacket = combRpcService.getServiceManagerService().deleteServicePort(monitorInstanceId + "-" + originalPort.intValue());
                    if (Objects.nonNull(combRetPacket) && combRetPacket.getStatus() != null && combRetPacket.getStatus() == 0)
                    {
                        return true;
                    }
                    else
                    {
                        log. error("remove endpoint from server manager failed,status:{}, message:{}", combRetPacket.getStatus(), combRetPacket.getObj());
                        return false;
                    }

                });

                if (allSuccessful)
                {
                    log.info("remove port mapping for:{}", monitorInstanceId);
                    TblOmcMonitorInstance updateTblOmcMonitorInstance = new TblOmcMonitorInstance();
                    updateTblOmcMonitorInstance.setId(monitorInstanceId);
                    updateTblOmcMonitorInstance.setStatus(ExporterStackDeploymentStatusEnum.PORT_MAPPING_REMOVED.value());
                    updateTblOmcMonitorInstance.setUpdateTime(new Date());
                    monitorTaskRepository.updateMonitorInstance(updateTblOmcMonitorInstance);
                }

            }
        }
    }

    @Override
    public void processRemoveStack(String monitorInstanceId)
    {
        TblOmcMonitorInstance tblOmcMonitorInstance = monitorTaskRepository.selectMonitorInstance(monitorInstanceId);
        if (Objects.isNull(tblOmcMonitorInstance))
        {
            return ;
        }

        String stackSpecId = tblOmcMonitorInstance.getSpecId();
        try
        {
            int deleted = combRpcService.getAosStackService().removeStackSpec(stackSpecId);
            if (deleted == 0)
            {
                TblOmcMonitorInstance updateTblOmcMonitorInstance = new TblOmcMonitorInstance();
                updateTblOmcMonitorInstance.setId(monitorInstanceId);
                updateTblOmcMonitorInstance.setStatus(ExporterStackDeploymentStatusEnum.DELETED.value());
                updateTblOmcMonitorInstance.setUpdateTime(new Date());
                monitorTaskRepository.updateMonitorInstance(updateTblOmcMonitorInstance);
            }
        }
       catch (Exception e)
       {
           log.error("remove stack spec failed,specId:{}", stackSpecId, e);
       }

    }

    @Override
    public void action(String id, String userId, String bpId, String action)
    {


        TblOmcMonitorInstance tblOmcMonitorInstance = new TblOmcMonitorInstance();
        tblOmcMonitorInstance.setId(id);
        tblOmcMonitorInstance.setUpdateTime(new Date());
        if (REINSTALL.equalsIgnoreCase(action))
        {
            // todo
        }

        if (UNINSTALL.equalsIgnoreCase(action))
        {
            tblOmcMonitorInstance.setStatus(ExporterStackDeploymentStatusEnum.TERMINATING.value());
        }

        monitorTaskRepository.updateMonitorInstance(tblOmcMonitorInstance);
        // todo
    }

    @Override
    public PrometheusInstancesRsp getPrometheusInstanceList(Integer type, String name, String siteId, Integer pageNum, Integer pageSize)
    {
        PageHelper.startPage(pageNum, pageSize);
        List<TblOmcPrometheus> tblOmcPrometheuses = prometheusInstanceRepository.selectByType(type, name, siteId);
        PageInfo<TblOmcPrometheus> pageInfo = new PageInfo<>(tblOmcPrometheuses);

        return PrometheusInstancesRsp.builder().totalNum(pageInfo.getTotal())
                .prometheusInstances(tblOmcPrometheuses.parallelStream().map(PrometheusInstance::of)
                        .map(instance -> {
                            // 0:server;1:agent
                            if (type != null && type == 0)
                            {
                                try
                                {
                                    JsonNode jobs = (JsonNode)prometheusClient.getJobs(instance.getId());
                                    if (Objects.nonNull(jobs))
                                    {
                                        JsonNode activeTargets = jobs.get("activeTargets");
                                        if (Objects.nonNull(activeTargets) && activeTargets.isArray())
                                        {
                                            instance.setTargetNum(activeTargets.size());
                                        }
                                    }

                                }
                                catch (Exception e)
                                {
                                    log.error("get prometheus jobs error", e);
                                }
                            }
                            if (type!= null && type == 1)
                            {
                                try
                                {
                                    String nodeId = instance.getNodeId();
                                    if (StringUtils.isNotBlank(nodeId))
                                    {
                                        RegionResourceService.NodeInfo nodeInfoByNodeId = combRpcService.getRegionResourceService().getNodeInfoByNodeId(nodeId);
                                        if (Objects.nonNull(nodeInfoByNodeId))
                                        {
                                            instance.setNodeName(nodeInfoByNodeId.getNodeName());
                                            instance.setSiteName(nodeInfoByNodeId.getSiteName());
                                        }
                                    }
                                }
                                catch (Exception e)
                                {
                                    log.error("get prometheus node info error", e);
                                }
                            }
                            return instance;
                        })
                        .map(instance -> BaseRsp.assembleUserInfo(instance.getBpId(), instance.getUserId(), instance, combRpcService.getUmsService())).collect(Collectors.toList())).build();
    }

    @Override
    public PrometheusInstanceDetail getPrometheus(String prometheusId)
    {
        TblOmcPrometheus tblOmcPrometheus = prometheusInstanceRepository.selectByPrimaryKey(prometheusId);
        if (Objects.nonNull(tblOmcPrometheus))
        {
            PrometheusInstanceDetail detail = PrometheusInstanceDetail.of(tblOmcPrometheus);
            try
            {
                String config = prometheusClient.getConfig();
                detail.setConfig(config);
            }
            catch (Exception e)
            {
                log.error("get prometheus instance detail error", e);
            }
            return detail;
        }


        return null;
    }

    @Override
    public String getPrometheusDataQuery(String query,  String time, Integer timeout)
    {
        if (!isAdmin())
        {
            query = AlarmUtils.modifyAlertExpr(query, getBpId(), getUserId());
        }
        return prometheusClient.query(query, time, timeout);
    }

    @Override
    public String getPrometheusDataQueryRange(String query, String step, String startTime, String endTime, Integer timeout)
    {
        if (!isAdmin())
        {
            query = AlarmUtils.modifyAlertExpr(query, getBpId(), getUserId());
        }
        return prometheusClient.queryRange(query, step, startTime, endTime, timeout);
    }

    @Override
    public Object getPrometheusJobs(String prometheusId)
    {
        return prometheusClient.getJobs(prometheusId);
    }

    @Override
    public void patchPrometheus(String prometheusId, PatchPrometheusReq req)
    {
        try
        {
            if (StringUtils.isNotBlank(req.getConfig()))
            {
                PromConfigHelper.backUpAndSaveConfigFile(req.getConfig());
            }
            prometheusClient.performConfigReload();
        }
        catch (Exception e)
        {
            log.error("patch prometheus error", e);
            throw new WebSystemException(PROMETHEUS_CONFIG_UPDATE_FAILED, ErrorLevel.ERROR);
        }

    }

    @Override
    public Object getIntegration(String id, String bpId, String userId)
    {
        TblOmcMonitorInstance tblOmcMonitorInstance = monitorTaskRepository.selectMonitorInstance(id);
        if (Objects.nonNull(tblOmcMonitorInstance))
        {
            MonitorIntegration monitorIntegration = MonitorIntegration.of(tblOmcMonitorInstance);
            MonitorIntegration monitorIntegration1 = assembleSpecInfo(assembleInfo(monitorIntegration));
            monitorIntegration1.setDataSourceName(assembleDataSourceInfo(monitorIntegration1.getDataSourceId()));
            return BaseRsp.assembleUserInfo(bpId, userId, monitorIntegration1, combRpcService.getUmsService());
        }

        return null;
    }

    @Override
    public DashboardLinksRsp getDashboardLinkList(Integer type, Integer pageNum, Integer pageSize)
    {
        // http://192.168.1.250:13000/d/cedcff0f-0d60-40ab-b496-bd8d04f0ba66/docker-containers?orgId=1&refresh=10s&from=1699345368965&to=1699346268965&kiosk
        Collection<DashboardLink> dashboardLinks = dashboardUrlStore.getDashboardLinks(type);
        return null;
    }

    private TblOmcMonitorTask doBuildTblOmcMonitorTask(AddIntegrationTaskReq req)
    {
        TblOmcMonitorTask tblOmcMonitorTask = new TblOmcMonitorTask();
        BeanUtils.copyProperties(req, tblOmcMonitorTask);
        tblOmcMonitorTask.setId(Utils.assignUUId());
        String name = req.getName();
        if (StringUtils.isNotBlank(name))
        {
            tblOmcMonitorTask.setName(name);
        }
        else
        {
            tblOmcMonitorTask.setName(fromValue(req.getIntegrationType()).getName());
        }

        tblOmcMonitorTask.setDescription(req.getDescription());

        tblOmcMonitorTask.setDataSourceId(req.getDataSourceId());
        tblOmcMonitorTask.setDeploymentStatus(ExporterStackDeploymentStatusEnum.PENDING.value());
        tblOmcMonitorTask.setStackTemplateVersionId(req.getStackTemplateVersionId());
        if (StringUtils.isNotBlank(req.getStackTemplateVersionId()))
        {
            tblOmcMonitorTask.setTaskType(TEMPLATE_TASK.value());
        }
        else
        {
            tblOmcMonitorTask.setTaskType(req.getIntegrationType());
        }
        tblOmcMonitorTask.setTargetType(fromIntegrationTaskType(req.getIntegrationType()).value());
        tblOmcMonitorTask.setTargets(req.getTargets());


        List<Map<String, String>> customParams = req.getCustomParams();
        tblOmcMonitorTask.setCustomParams(customParams);

        List<Map<String, String>> stackParams = req.getStackParams();
        tblOmcMonitorTask.setStackParams(stackParams);

        tblOmcMonitorTask.setCreateTime(new Date());
        tblOmcMonitorTask.setUpdateTime(tblOmcMonitorTask.getCreateTime());
        monitorTaskRepository.insert(tblOmcMonitorTask);
        return tblOmcMonitorTask;
    }

    private void checkParams(AddIntegrationTaskReq addIntegrationTaskReq)
    {
        // todo
        checkTargetType();

        checkDataSourceId();

        checkTargets(addIntegrationTaskReq);
    }

    private MonitorIntegration assembleSpecInfo(MonitorIntegration monitorIntegration)
    {
        String specId = monitorIntegration.getSpecId();
        if (StringUtils.isNotBlank(specId))
        {
            try
            {
                StackDeploySimpleInfo stackDeployInfo = combRpcService.getAosStackService().getStackDeployInfo(specId);
                if (Objects.nonNull(stackDeployInfo))
                {
                    monitorIntegration.setReplicaNum(stackDeployInfo.getReplicaNum());
                    monitorIntegration.setReadyNum(stackDeployInfo.getReadyNum());
                    monitorIntegration.setAvailableNum(stackDeployInfo.getAvailableNum());
                    monitorIntegration.setProcessingNum(stackDeployInfo.getProcessingNum());
                    monitorIntegration.setFailedNum(stackDeployInfo.getFailedNum());
                }
            }
            catch (Exception e)
            {
                log.error("assemble spec info error", e);
            }

        }

        return monitorIntegration;
    }

    private String assembleDataSourceInfo(String dataSourceId)
    {
        if (StringUtils.isNotBlank(dataSourceId))
        {
            TblOmcPrometheus tblOmcPrometheus = prometheusInstanceRepository.selectByPrimaryKey(dataSourceId);
            if (Objects.nonNull(tblOmcPrometheus))
            {
                return tblOmcPrometheus.getName();
            }
        }

        return "";
    }

    private MonitorIntegration assembleInfo(MonitorIntegration monitorIntegration)
    {
        //String targetId = monitorIntegration.getTargetId();
        List<String> targets = monitorIntegration.getTargets();
        if (!CollectionUtils.isEmpty(targets))
        {
            try
            {
                Integer targetType = monitorIntegration.getTargetType();
                ExporterTargetTypeEnum exporterTargetTypeEnum = ExporterTargetTypeEnum.fromValue(targetType);

                List<String> targetNames = targets.stream().map(siteId ->
                {
                    try
                    {
                        switch (exporterTargetTypeEnum)
                        {
                            case SITE:
                                String siteNameById = combRpcService.getRegionResourceService().getSiteNameById(siteId);
                                return siteNameById;

                            case LIGHTWEIGHT_NODE:
                                RegionResourceService.NodeInfo nodeInfo = combRpcService.getRegionResourceService().getNodeInfoByNodeId(siteId);
                                if (Objects.nonNull(nodeInfo))
                                {
                                   return nodeInfo.getSiteName();
                                }
                                break;
                            case NEXTSTACK:
                                break;
                            case OPENSTACK:
                                break;
                            default:
                                break;
                        }

                    }
                    catch (Exception e)
                    {
                        log.error("assemble site info error", e);
                        return "";
                    }
                    return "";

                }).filter(StringUtils::isNotBlank).collect(Collectors.toList());

                if (!CollectionUtils.isEmpty(targetNames))
                {
                    monitorIntegration.setTargetNames(targetNames);
                }

            }
            catch (Exception e)
            {
                log.error("assemble info error", e);
            }

        }

        return monitorIntegration;
    }

    private MonitorIntegration assembleNodeInfo(MonitorIntegration monitorIntegration)
    {
        String nodeId = monitorIntegration.getNodeId();
        if (StringUtils.isNotBlank(nodeId))
        {
            try
            {
                RegionResourceService.NodeInfo nodeInfo = combRpcService.getRegionResourceService().getNodeInfoByNodeId(nodeId);
                if (Objects.nonNull(nodeInfo))
                {
                    monitorIntegration.setNodeName(nodeInfo.getNodeName());
                    monitorIntegration.setSiteName(nodeInfo.getSiteName());
                    monitorIntegration.setRegionName(nodeInfo.getRegionName());
                }
            }
            catch (Exception e)
            {
                log.error("assemble node info error", e);
            }

        }

        return monitorIntegration;
    }


    private void checkDataSourceId()
    {
    }

    private void checkTargets(AddIntegrationTaskReq addIntegrationTaskReq)
    {

    }

    private void checkTargetType()
    {

    }

    private static int extractPortFromAddress(String address) {
        String[] parts = address.split(":");
        if (parts.length == 2) {
            try {
                return Integer.parseInt(parts[1]);
            } catch (NumberFormatException e) {
            }
        }

        if (parts.length == 1) {
            try {
                return Integer.parseInt(parts[0]);
            } catch (NumberFormatException e) {
            }
        }
        return -1;
    }

    private static List<MonitorInstanceEndpointInfo> doGetPortList(TblOmcMonitorInstance tblOmcMonitorInstance)
    {
        Object stackParams = tblOmcMonitorInstance.getStackParams();
        Map<String, String> inputParamsMap =  JacksonUtils.strToObjTypeDefault((String) stackParams, new TypeReference<Map<String, String>>() {});

        List<MonitorInstanceEndpointInfo> monitorInstanceEndpointInfos = new ArrayList<>();

        if (!CollectionUtils.isEmpty(inputParamsMap))
        {
            for (Map.Entry<String, String> entry : inputParamsMap.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

                if (key.contains("address") || key.contains("ADDRESS")) {
                    int port = extractPortFromAddress(value);
                    if (port > 0)
                    {
                        MonitorInstanceEndpointInfo endpointInfo = MonitorInstanceEndpointInfo.builder().name(key).originalPort(port).build();
                        monitorInstanceEndpointInfos.add(endpointInfo);
                    }
                } else if (key.contains("port") || key.contains("PORT")) {

                    MonitorInstanceEndpointInfo endpointInfo = MonitorInstanceEndpointInfo.builder().name(key).originalPort(Integer.valueOf(value)).build();
                    monitorInstanceEndpointInfos.add(endpointInfo);
                }
            }
        }

        return monitorInstanceEndpointInfos;
    }

    private IntegrationTargetsRsp doGetIntegrationNodeList(Integer targetType, String queryBpId, String queryUserId, Integer pageNum, Integer pageSize)
    {
        return null;
    }

    private static void genName(TblOmcMonitorTask tblOmcMonitorTask, TblOmcMonitorInstance tblOmcMonitorInstance)
    {
        String name = tblOmcMonitorTask.getName();
        if (StringUtils.isNotBlank(name))
        {
            name = name + "-" + Utils.assignUUId();
            if (name.length() > 63)
            {
                name = name.substring(0, 63);
            }

        }
        else
        {
            name = Utils.assignUUId();
        }
        tblOmcMonitorInstance.setName(name);
    }
}
