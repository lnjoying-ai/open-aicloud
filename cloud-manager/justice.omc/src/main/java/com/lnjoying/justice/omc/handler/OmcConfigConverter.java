package com.lnjoying.justice.omc.handler;

import com.alibaba.nacos.api.config.convert.NacosConfigConverter;
import com.lnjoying.justice.commonweb.util.YamlParserUtil;
import com.lnjoying.justice.omc.config.OmcConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.Objects;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2024/3/9 17:19
 */
@Slf4j
public class OmcConfigConverter implements NacosConfigConverter<OmcConfig>
{
    @Override
    public boolean canConvert(Class<OmcConfig> targetType)
    {
        return OmcConfig.class.equals(targetType);
    }

    @Override
    public OmcConfig convert(String config)
    {
        return buildOmcConfig(config);
    }

    private OmcConfig buildOmcConfig(String omcConfig)
    {
        try
        {
            Map<String, Object> load = YamlParserUtil.loadObject(omcConfig, Map.class);
            if (CollectionUtils.isEmpty(load))
            {
                return null;
            }

            OmcConfig omcConfigBean = new OmcConfig();
            String ruleFilePath = (String)load.get("prometheus.setting.rule.path");
            omcConfigBean.setRuleFilePath(ruleFilePath);

            String prometheusServerInternalAddress = (String)load.get("prometheus.server.internal.address");
            omcConfigBean.setPrometheusServerInternalAddress(prometheusServerInternalAddress);

            String prometheusServerExternalAddress = (String)load.get("prometheus.server.external.address");
            omcConfigBean.setPrometheusServerExternalAddress(prometheusServerExternalAddress);

            String prometheusServerUsername = (String)load.get("prometheus.server.username");
            omcConfigBean.setPrometheusServerUsername(prometheusServerUsername);

            String prometheusServerPassword = (String)load.get("prometheus.server.password");
            omcConfigBean.setPrometheusServerPassword(prometheusServerPassword);

            String monitorAdminUsername = (String)load.get("monitor.admin.username");
            omcConfigBean.setMonitorAdminUsername(monitorAdminUsername);

            String monitorAdminPassword = (String)load.get("monitor.admin.password");
            omcConfigBean.setMonitorAdminPassword(monitorAdminPassword);

            String grafanaAddress = (String)load.get("grafana.address");
            omcConfigBean.setGrafanaAddress(grafanaAddress);

            Object sendSms = load.get("send.sms");
            omcConfigBean.setSendSms(sendSms instanceof Boolean ? String.valueOf((Boolean)sendSms) : (String)sendSms);

            Object sendEmail = load.get("send.email");
            omcConfigBean.setSendEmail(sendEmail instanceof Boolean ? String.valueOf((Boolean)sendEmail) : (String)sendEmail);
            return omcConfigBean;
        }
        catch (Exception e)
        {
            log.error("build omc config error:{}", e);
            return null;
        }


    }
}
