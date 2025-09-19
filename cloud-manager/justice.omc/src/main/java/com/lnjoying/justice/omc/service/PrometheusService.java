package com.lnjoying.justice.omc.service;

import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.commonweb.util.JacksonUtils;
import com.lnjoying.justice.commonweb.util.SystemUtils;
import com.lnjoying.justice.commonweb.util.YamlConfigUtils;
import com.lnjoying.justice.commonweb.util.YamlParserUtil;
import com.lnjoying.justice.omc.config.OmcConfig;
import com.lnjoying.justice.omc.db.model.TblOmcPrometheus;
import com.lnjoying.justice.omc.db.repo.PrometheusInstanceRepository;
import com.lnjoying.justice.omc.domain.dto.rsp.AlertGroupsRsp;
import com.lnjoying.justice.omc.prometheus.client.PrometheusClient;
import com.lnjoying.justice.omc.prometheus.config.PromConfigHelper;
import com.lnjoying.justice.omc.prometheus.config.RemoteReadConfig;
import com.lnjoying.justice.omc.prometheus.model.PrometheusRuleGroups;
import com.lnjoying.justice.omc.prometheus.sd.PrometheusTargetsStore;
import com.lnjoying.justice.omc.prometheus.sd.PrometheusTargetsStoreManager;
import com.lnjoying.justice.omc.prometheus.sd.TargetsData;
import com.lnjoying.justice.omc.prometheus.util.PrometheusUtils;
import com.lnjoying.justice.schema.common.ErrorLevel;
import com.lnjoying.justice.schema.entity.omc.MonitorEndpointInfo;
import com.micro.core.common.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Tag;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.lnjoying.justice.schema.common.ErrorCode.OMC_ALARM_DELETE_FAILED;
import static com.lnjoying.justice.schema.common.ErrorCode.PROMETHEUS_RULE_CREATE_FAILED;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/11/3 16:33
 */

@Slf4j
@Service
public class PrometheusService
{

    @Autowired
    private OmcConfig omcConfig;

    @Autowired
    private PrometheusClient prometheusClient;

    @Autowired
    private PrometheusTargetsStoreManager storeManager;

    @Autowired
    private PrometheusInstanceRepository prometheusInstanceRepository;

    public  void writeAlertRuleToYAML(PrometheusRuleGroups alertRule, String alertRulePath)
    {

        PrometheusUtils.writeAlertRuleToYAML(alertRule, alertRulePath);
    }

    public String generateRuleFilePath(String bpId, String userId, String alarmId)
    {
        if (!StringUtils.hasText(bpId))
        {
            bpId = "default";
        }
       return  Paths.get(omcConfig.getRuleFilePath(), "/" + bpId + "-" + userId + "-" + alarmId + ".yml").normalize().toString();
    }

    public void addAlertRule(PrometheusRuleGroups alertRule, String bpId, String userId, String alarmId)
    {

        String alertRulePath = generateRuleFilePath(bpId, userId, alarmId);
        writeAlertRuleToYAML(alertRule, alertRulePath);

        boolean reloadSuccess = prometheusClient.performConfigReload();
        if (!reloadSuccess)
        {
           deleteRuleFile(alertRulePath);
           throw new WebSystemException(PROMETHEUS_RULE_CREATE_FAILED, ErrorLevel.ERROR, "prometheus reload failed");
        }

    }

    public boolean deleteAlertRule(String bpId, String userId, String alarmId)
    {
        String alertRulePath = generateRuleFilePath(bpId, userId, alarmId);
        if(PrometheusUtils.deleteAlertRule(alertRulePath))
        {
            boolean reloadSuccess = prometheusClient.performConfigReload();
            return reloadSuccess;
        }
        else
        {
            return false;
        }

    }

    public boolean deleteRuleFile(String alertRulePath)
    {
        Path ruleFilePath = Paths.get(alertRulePath);
        if (Files.exists(ruleFilePath))
        {
            try
            {
                Files.delete(ruleFilePath);
            }
            catch (IOException e)
            {
               log.error("delete alert rule file error:{}", e);
               return false;
            }
            return true;
        }

        return true;
    }


    public List<TargetsData> httpSdByType(String type)
    {
        return storeManager.getPrometheusTargetsStore(type).getAllTargetsData();
    }

    public void loadPrometheusServer()
    {

        List<TblOmcPrometheus> prometheusList = prometheusInstanceRepository.selectByType(0, "prometheus server", null);
        if (!CollectionUtils.isEmpty(prometheusList))
        {
            return;
        }
        TblOmcPrometheus tblOmcPrometheus = new TblOmcPrometheus();
        tblOmcPrometheus.setId(Utils.assignUUId());
        tblOmcPrometheus.setName("prometheus server" + "-" + Utils.getRandomStr(8));
        tblOmcPrometheus.setStatus(1);
        tblOmcPrometheus.setType(0);
        tblOmcPrometheus.setGlobal(true);
        tblOmcPrometheus.setSystemDefault(true);
        tblOmcPrometheus.setSiteId(null);
        tblOmcPrometheus.setNodeId(null);
        setBasicAuth(omcConfig, tblOmcPrometheus);
        String cloudPublicIp = SystemUtils.getConfig("CLOUD_PUBLIC_IP");
        if (org.apache.commons.lang3.StringUtils.isNotBlank(cloudPublicIp))
        {
            tblOmcPrometheus.setExternalEndpointUrl("https://" + cloudPublicIp + ":19090");
        }

        String cloudInternalIp = SystemUtils.getConfig("CLOUD_INTERNAL_IP");
        if (org.apache.commons.lang3.StringUtils.isNotBlank(cloudInternalIp))
        {
            tblOmcPrometheus.setInternalEndpointUrl("https://" + cloudInternalIp + ":19090");
        }
        tblOmcPrometheus.setCreateTime(new Date());
        tblOmcPrometheus.setUpdateTime(new Date());
        prometheusInstanceRepository.insertSelective(tblOmcPrometheus);
    }

    public void updatePrometheusServer(OmcConfig omcConfig)
    {
        try
        {

            if (Objects.isNull(omcConfig))
            {
                return ;
            }

            List<TblOmcPrometheus> prometheusList = prometheusInstanceRepository.selectByType(0, "prometheus server", null);
            if (!CollectionUtils.isEmpty(prometheusList))
            {
                TblOmcPrometheus tblOmcPrometheus = prometheusList.get(0);
                TblOmcPrometheus updateTblOmcPrometheus = new TblOmcPrometheus();
                updateTblOmcPrometheus.setId(tblOmcPrometheus.getId());
                updateTblOmcPrometheus.setExternalEndpointUrl(omcConfig.getPrometheusServerExternalAddress());
                updateTblOmcPrometheus.setInternalEndpointUrl(omcConfig.getPrometheusServerInternalAddress());
                setBasicAuth(omcConfig, updateTblOmcPrometheus);
                updateTblOmcPrometheus.setUpdateTime(new Date());
                prometheusInstanceRepository.updateByPrimaryKeySelective(updateTblOmcPrometheus);
            }
        }
        catch (Exception e)
        {
            log.error("update prometheus server config error:{}", e);
        }


    }


    @Transactional(rollbackFor = {Exception.class})
    public boolean setPrometheusAgentAndUpdatePrometheusServerConfig(MonitorEndpointInfo monitorEndpointInfo)
    {
        TblOmcPrometheus tblOmcPrometheus = new TblOmcPrometheus();
        tblOmcPrometheus.setId(monitorEndpointInfo.getUniqueId());
        tblOmcPrometheus.setName("prometheus agent" + "-" + Utils.getRandomStr(32));
        tblOmcPrometheus.setStatus(1);
        tblOmcPrometheus.setType(1);
        tblOmcPrometheus.setGlobal(false);
        tblOmcPrometheus.setSiteId(monitorEndpointInfo.getSiteId());
        tblOmcPrometheus.setNodeId(monitorEndpointInfo.getNodeId());
        tblOmcPrometheus.setExternalEndpointUrl("http://" + monitorEndpointInfo.getEndpoint());
        tblOmcPrometheus.setCreateTime(new Date());
        tblOmcPrometheus.setUpdateTime(new Date());
        int result = prometheusInstanceRepository.insertSelective(tblOmcPrometheus);

        RemoteReadConfig remoteReadConfig =  RemoteReadConfig.builder().url(tblOmcPrometheus.getExternalEndpointUrl())
                .basicAuth(RemoteReadConfig.BasicAuth.builder().build()).build();
        PromConfigHelper.modifyRemoteReadAndSaveConfigFile(null, remoteReadConfig);
        prometheusClient.performConfigReload();

        boolean successful = result == 1;
        log.info("set prometheus agent and update prometheus server config successful");
        return successful;
    }


    private static void setBasicAuth(OmcConfig omcConfigBean, TblOmcPrometheus updateTblOmcPrometheus)
    {
        RemoteReadConfig.BasicAuth basicAuth = new RemoteReadConfig.BasicAuth();
        String prometheusServerUsername = omcConfigBean.getPrometheusServerUsername();
        if (StringUtils.hasText(prometheusServerUsername))
        {
            basicAuth.setUsername(prometheusServerUsername);
        }
        String prometheusServerPassword = omcConfigBean.getPrometheusServerPassword();
        if (StringUtils.hasText(prometheusServerPassword))
        {
            basicAuth.setPassword(prometheusServerPassword);
        }
        updateTblOmcPrometheus.setAuth(basicAuth);
    }
}
