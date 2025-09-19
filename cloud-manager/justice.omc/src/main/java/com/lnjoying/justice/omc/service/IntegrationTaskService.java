package com.lnjoying.justice.omc.service;

import com.lnjoying.justice.omc.db.model.TblOmcMonitorInstance;
import com.lnjoying.justice.omc.domain.dto.req.AddIntegrationTaskReq;
import com.lnjoying.justice.omc.domain.dto.req.PatchPrometheusReq;
import com.lnjoying.justice.omc.domain.dto.rsp.DashboardLinksRsp;
import com.lnjoying.justice.omc.domain.dto.rsp.IntegrationTargetsRsp;
import com.lnjoying.justice.omc.domain.dto.rsp.IntegrationsRsp;
import com.lnjoying.justice.omc.domain.dto.rsp.PrometheusInstancesRsp;
import com.lnjoying.justice.omc.domain.model.PrometheusInstanceDetail;

import java.util.List;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/10/25 15:23
 */

public interface IntegrationTaskService
{
    Object addIntegrationTask(AddIntegrationTaskReq addIntegrationTaskReq);

    Object getIntegrationParams(Integer integrationType);

    IntegrationsRsp getIntegrationList(Integer integrationType, String queryBpId, String queryUserId, Integer pageNum, Integer pageSize);

    List<TblOmcMonitorInstance> getIntegrationList(Integer integrationType, Integer status, Integer pageNum, Integer pageSize);


    void action(String id, String userId, String bpId, String action);

    PrometheusInstancesRsp getPrometheusInstanceList(Integer type,  String name,  String siteId, Integer pageNum, Integer pageSize);

    DashboardLinksRsp getDashboardLinkList(Integer type, Integer pageNum, Integer pageSize);

    void processBuildStack(String monitorInstanceId);

    void processCheckStackRunningStatus(String monitorInstanceId);

    void processPortMapping(String monitorInstanceId);

    void processAddExporterScrapeTarget(String monitorInstanceId);

    void processDeleteExporterScrapeTarget(String monitorInstanceId);

    void processRemovePortMapping(String monitorInstanceId);

    void processRemoveStack(String monitorInstanceId);

    IntegrationTargetsRsp getIntegrationTargetList(Integer integrationType, String queryBpId, String queryUserId, String nodeName, String bpId, String s, Integer pageNum,
                                                   Integer pageSize);

    PrometheusInstanceDetail getPrometheus(String prometheusId);

    String getPrometheusDataQuery(String query,  String time, Integer timeout);


    String getPrometheusDataQueryRange(String query, String step, String startTime, String endTime, Integer timeout);

    Object getPrometheusJobs(String prometheusId);

    void patchPrometheus(String prometheusId, PatchPrometheusReq req);

    Object getIntegration(String id, String bpId, String userId);
}
