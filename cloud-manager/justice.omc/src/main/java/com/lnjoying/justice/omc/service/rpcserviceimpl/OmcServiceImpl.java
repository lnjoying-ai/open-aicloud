package com.lnjoying.justice.omc.service.rpcserviceimpl;

import com.lnjoying.justice.omc.db.model.TblOmcPrometheus;
import com.lnjoying.justice.omc.db.repo.MonitorTaskRepository;
import com.lnjoying.justice.omc.db.repo.OmcEventRepository;
import com.lnjoying.justice.omc.db.repo.OmcLogRepository;
import com.lnjoying.justice.omc.db.repo.PrometheusInstanceRepository;
import com.lnjoying.justice.omc.domain.model.PrometheusTypeEnum;
import com.lnjoying.justice.omc.prometheus.client.PrometheusClient;
import com.lnjoying.justice.omc.prometheus.config.PromConfigHelper;
import com.lnjoying.justice.omc.prometheus.config.RemoteReadConfig;
import com.lnjoying.justice.omc.prometheus.sd.PrometheusTargetsStoreManager;
import com.lnjoying.justice.omc.service.PrometheusRuleService;
import com.lnjoying.justice.omc.service.PrometheusService;
import com.lnjoying.justice.schema.common.CombRetPacket;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.entity.omc.*;
import com.lnjoying.justice.schema.service.omc.OmcService;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.servicecomb.provider.pojo.RpcSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

import static com.lnjoying.justice.omc.prometheus.sd.PrometheusTargetsStore.getTargetsDataContent;

/**
 * 功能描述
 *
 * @author: Robin
 * @date: 2023年10月20日 14:18
 */
@RpcSchema(schemaId = "omcService")
@Slf4j
public class OmcServiceImpl implements OmcService
{

    @Autowired
    private OmcLogRepository logRepository;
    @Autowired
    private OmcEventRepository eventRepository;

    @Autowired
    private MonitorTaskRepository monitorTaskRepository;

    @Autowired
    private PrometheusTargetsStoreManager storeManager;

    @Autowired
    private PrometheusInstanceRepository prometheusInstanceRepository;

    @Autowired
    private PrometheusRuleService prometheusRuleService;

    @Autowired
    private PrometheusClient prometheusClient;

    @Autowired
    private PrometheusService prometheusService;


    public void addLog(@ApiParam(name = "operationLog") OperationLog operationLog)
    {
        logRepository.insertLog(operationLog);
    }

    public void addEvent(@ApiParam(name = "operationEvent") OperationEvent operationEvent)
    {
        eventRepository.insertEvent(operationEvent);
    }

    @Override
  /*  @Transactional(rollbackFor = {Exception.class})*/
    public CombRetPacket receivePortMappingResult(@ApiParam(name = "monitorId")String monitorId, @ApiParam(name = "endpoint")String endpoint, @ApiParam(name = "message")String message)
    {
        log.info("receive port mapping result,monitor id:{}, endpoint:{}, message:{} ", monitorId, endpoint, message);
        if (StringUtils.hasText(message))
        {
            log.error("receive port mapping result failed, message:{}", message);
        }
        else
        {


            if (StringUtils.hasText(endpoint))
            {
                CombRetPacket combRetPacket = monitorTaskRepository.processMonitorPortMappingResult(monitorId, endpoint);
                if (combRetPacket != null) return combRetPacket;


            }
        }

        CombRetPacket combRetPacket = new CombRetPacket();
        combRetPacket.setStatus(-1);
        return combRetPacket;
    }

    @Override
    public CombRetPacket addMonitorEndpoint(@ApiParam(name = "monitorEndpointInfo") MonitorEndpointInfo monitorEndpointInfo)
    {
        log.info("add monitor endpoint, monitorEndpointInfo:{}", monitorEndpointInfo);
        boolean successful = false;
        CombRetPacket combRetPacket = new CombRetPacket();
        try
        {
            String type = monitorEndpointInfo.getType();
            if (StringUtils.hasText(type))
            {
                if ("PROMETHEUS_AGENT".equalsIgnoreCase(type))
                {
                    successful = prometheusService.setPrometheusAgentAndUpdatePrometheusServerConfig(monitorEndpointInfo);
                }
                else
                {
                    String siteId = monitorEndpointInfo.getSiteId();
                    boolean handle = false;
                    if (StringUtils.hasText(siteId))
                    {
                        List<TblOmcPrometheus> tblOmcPrometheuses = prometheusInstanceRepository.selectByType(PrometheusTypeEnum.AGENT.value(), null, siteId);
                        if (!CollectionUtils.isEmpty(tblOmcPrometheuses))
                        {
                            TblOmcPrometheus tblOmcPrometheus = tblOmcPrometheuses.get(0);

                            // If there is a prometheus agent, write the endpoints into the prometheus agent, otherwise write into the prometheus server.
                            if (Objects.nonNull(tblOmcPrometheus))
                            {
                                successful = sendTargetsDataContentToPrometheusAgent(monitorEndpointInfo,tblOmcPrometheus.getNodeId(), type);
                                handle = true;
                            }
                        }
                    }

                    if (!handle)
                    {
                        successful = storeManager.getPrometheusTargetsStore(type).addTarget(monitorEndpointInfo.getBpId(), monitorEndpointInfo.getUserId(), siteId,
                                monitorEndpointInfo.getNodeId(), monitorEndpointInfo.getEndpoint(), monitorEndpointInfo.getUniqueId());
                    }

                }

            }

            if (successful)
            {

                combRetPacket.setStatus(ErrorCode.SUCCESS.getCode());
                combRetPacket.setObj("success");
            }
            else
            {
                combRetPacket.setStatus(ErrorCode.SystemError.getCode());
                combRetPacket.setObj("fail");
            }
        }
        catch (Exception e)
        {
            log.error("add monitor endpoint failed, monitorEndpointInfo:{}", monitorEndpointInfo, e);
            combRetPacket.setStatus(ErrorCode.SystemError.getCode());
            combRetPacket.setObj("fail");
        }

        return combRetPacket;
    }




    @Override
    public CombRetPacket deleteMonitorEndpoint(@ApiParam(name = "type") String type, @ApiParam(name = "uniqueId") String uniqueId)
    {
        log.info("delete monitor endpoint, type:{}, uniqueId:{}", type, uniqueId);
        boolean successful = false;
        CombRetPacket combRetPacket = new CombRetPacket();
        if (StringUtils.hasText(type) && StringUtils.hasText(uniqueId))
        {
            if ("PROMETHEUS_AGENT".equalsIgnoreCase(type))
            {
                TblOmcPrometheus tblOmcPrometheus = prometheusInstanceRepository.selectByPrimaryKey(uniqueId);
                if (Objects.nonNull(tblOmcPrometheus))
                {
                    String externalEndpointUrl = tblOmcPrometheus.getExternalEndpointUrl();
                    if (StringUtils.hasText(externalEndpointUrl))
                    {
                        RemoteReadConfig remoteReadConfig =  RemoteReadConfig.builder().url(tblOmcPrometheus.getExternalEndpointUrl())
                                .basicAuth(RemoteReadConfig.BasicAuth.builder().build()).build();
                        PromConfigHelper.deleteRemoteReadAndSaveConfigFile(null, remoteReadConfig);
                    }
                    int num = prometheusInstanceRepository.deleteByPrimaryKey(uniqueId);
                    successful = num == 1;
                }

                if (successful)
                {
                    log.info("delete prometheus agent successful, type:{}, uniqueId:{}", type, uniqueId);
                }
                else
                {
                    log.error("delete prometheus agent failed, type:{}, uniqueId:{}", type, uniqueId);
                }

            }
            else
            {

                if (StringUtils.hasText(type))
                {
                    successful = storeManager.getPrometheusTargetsStore(type).deleteTarget(uniqueId);
                    log.info("delete monitor endpoint successful, type:{}, uniqueId:{}", type, uniqueId);
                }

            }
        }

        if (successful)
        {
            combRetPacket.setStatus(ErrorCode.SUCCESS.getCode());
            combRetPacket.setObj("success");
        }
        else
        {
            combRetPacket.setStatus(ErrorCode.SystemError.getCode());
            combRetPacket.setObj("fail");
        }
        return combRetPacket;

    }

    @Override
    public BpLastLoginTimeRsp getLastLoginTimeGroupByBpId(@ApiParam(name = "bpIds") List<String> bpIds, @ApiParam(name = "startDate")Date startDate,
                                                          @ApiParam(name = "endDate")Date endDate,
                                                          @ApiParam(name = "pageSize")Integer pageSize, @ApiParam(name = "pageNum")Integer pageNum,
                                                          @ApiParam(name = "sortField")String sortField, @ApiParam(name = "sortDirection")String sortDirection,
                                                          @ApiParam(name = "userId")String userId, @ApiParam(name = "userName")String userName)
    {
        return logRepository.getLastLoginTimeGroupByBpId(bpIds, startDate, endDate, pageSize, pageNum, sortDirection, userId, userName);
    }

    @Override
    public Map<String, Date> getSimpleLastLoginTimeGroupByBpId(@ApiParam(name = "bpIds") List<String> bpIds, @ApiParam(name = "startDate")Date startDate,
                                                                     @ApiParam(name = "endDate")Date endDate,
                                                                     @ApiParam(name = "pageSize")Integer pageSize, @ApiParam(name = "pageNum")Integer pageNum,
                                                                     @ApiParam(name = "sortField")String sortField, @ApiParam(name = "sortDirection")String sortDirection)
    {
        BpLastLoginTimeRsp bpLastLoginTimeRsp = logRepository.getLastLoginTimeGroupByBpId(bpIds, startDate, endDate, pageSize, pageNum, sortDirection, null, null);
        if (bpLastLoginTimeRsp.getTotalNum() > 0)
        {
            return bpLastLoginTimeRsp.getLastLoginTimeDTOS().stream().collect(Collectors.toMap(BpLastLoginTimeDTO::getBpId, BpLastLoginTimeDTO::getLastLoginTime));
        }

        return new HashMap<>();
    }

    @Override
    public BpLoginDetailRsp getLoginDetailByBpId(@ApiParam(name = "bpId")String bpId, @ApiParam(name = "userId")String userId,
                                                 @ApiParam(name = "startDate")Date startDate, @ApiParam(name = "endDate")Date endDate,
                                                 @ApiParam(name = "pageSize")Integer pageSize, @ApiParam(name = "pageNum")Integer pageNum,
                                                 @ApiParam(name = "sortField")String sortField, @ApiParam(name = "sortDirection")String sortDirection)
    {
        return logRepository.getLoginDetailByBpId(bpId, userId, startDate, endDate, pageSize, pageNum, sortDirection);
    }

    private boolean sendTargetsDataContentToPrometheusAgent(MonitorEndpointInfo monitorEndpointInfo, String nodeId, String type)
    {
        if (StringUtils.hasText(nodeId))
        {
            String bpId = monitorEndpointInfo.getBpId();
            if (!StringUtils.hasText(bpId))
            {
                bpId = "default";
            }
            String targetsDataContent = getTargetsDataContent(bpId, monitorEndpointInfo.getUserId(),
                    monitorEndpointInfo.getSiteId(), monitorEndpointInfo.getNodeId(), monitorEndpointInfo.getEndpoint(), monitorEndpointInfo.getUniqueId());
            // send the targets configuration file to the node where the agent resides
            boolean result = prometheusRuleService.sendFileCreateReq(type, bpId, monitorEndpointInfo.getUserId(), monitorEndpointInfo.getUniqueId()
                    , targetsDataContent, nodeId);
            log.info("send targets data content to prometheus agent, targetsDataContent:{}", targetsDataContent);
            return result;
        }
        else
        {
            log.error("send targets data content to prometheus agent failed, nodeId is empty");
            return false;
        }

    }
}
