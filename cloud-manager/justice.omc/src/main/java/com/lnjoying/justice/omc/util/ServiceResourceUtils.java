package com.lnjoying.justice.omc.util;

import com.lnjoying.justice.commonweb.util.CollectionUtils;
import com.lnjoying.justice.omc.domain.dto.model.ApiLogDto;
import com.lnjoying.justice.omc.domain.dto.response.QueryApiLogRsp;
import com.lnjoying.justice.omc.domain.model.Alarm;
import com.lnjoying.justice.omc.domain.model.AlarmDetail;
import com.lnjoying.justice.omc.service.CombRpcService;
import com.lnjoying.justice.schema.entity.RpcResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static org.springframework.util.StringUtils.isEmpty;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2024/9/25 19:31
 */

@Slf4j
@Component
public class ServiceResourceUtils
{
    @Autowired
    private CombRpcService combRpcService;

    private static ServiceResourceUtils serviceResourceUtils;

    @PostConstruct
    public void init() {
        serviceResourceUtils = this;
        serviceResourceUtils.combRpcService = this.combRpcService;
    }

    public static void fillAlarmResourceInfo(List<Alarm> alarmList)
    {
        try
        {
            if (CollectionUtils.isEmpty(alarmList))
            {
                return;
            }

            Set<String> serviceResourceEnNamePairSet = alarmList.stream().filter(x -> !isEmpty(x.getTargetServiceType()))
                    .map(x -> getServiceResourcePair(x.getTargetServiceType(),x.getTargetResourceType()))
                    .collect(Collectors.toSet());
            RpcResult<HashMap<String, String>> rpcResult =
                    serviceResourceUtils.combRpcService.getCommonResourceFeederService().getServiceResourceDisplayInfo(serviceResourceEnNamePairSet);
            if (rpcResult.ok())
            {
                HashMap<String, String> serviceResourceCnNameMap = rpcResult.getData();
                for (Alarm alarm : alarmList)
                {

                    String serviceResourceEnNamePair = getServiceResourcePair(alarm.getTargetServiceType(), alarm.getTargetResourceType());
                    String serviceResourceCnNamePair = serviceResourceCnNameMap.get(serviceResourceEnNamePair);
                    if (!isEmpty(serviceResourceCnNamePair))
                    {
                        String[] splitResults = serviceResourceCnNamePair.split(":", 2);
                        alarm.setTargetServiceType(splitResults[0]);
                        String resourceCnName = splitResults[1];
                        if (StringUtils.hasText(resourceCnName))
                        {
                            alarm.setTargetResourceType(resourceCnName);
                        }
                        else
                        {
                            String[] splitEnResults = serviceResourceEnNamePair.split(":", 2);
                            if (splitEnResults.length == 2)
                            {
                                alarm.setTargetResourceType(splitEnResults[1]);
                            }
                        }

                    }
                }
            }
        } catch (Exception e)
        {
            log.error("填充服务和资源名称时出错! error: {}", e.getMessage());
        }
    }

    public static void fillAlarmDetailResourceInfo(List<AlarmDetail> alarmList)
    {
        try
        {
            if (CollectionUtils.isEmpty(alarmList))
            {
                return;
            }

            Set<String> serviceResourceEnNamePairSet = alarmList.stream().filter(x -> !isEmpty(x.getTargetServiceType()))
                    .map(x -> getServiceResourcePair(x.getTargetServiceType(),x.getTargetResourceType()))
                    .collect(Collectors.toSet());
            RpcResult<HashMap<String, String>> rpcResult =
                    serviceResourceUtils.combRpcService.getCommonResourceFeederService().getServiceResourceDisplayInfo(serviceResourceEnNamePairSet);
            if (rpcResult.ok())
            {
                HashMap<String, String> serviceResourceCnNameMap = rpcResult.getData();
                for (AlarmDetail alarm : alarmList)
                {

                    String serviceResourceEnNamePair = getServiceResourcePair(alarm.getTargetServiceType(), alarm.getTargetResourceType());
                    String serviceResourceCnNamePair = serviceResourceCnNameMap.get(serviceResourceEnNamePair);
                    if (!isEmpty(serviceResourceCnNamePair))
                    {
                        String[] splitResults = serviceResourceCnNamePair.split(":", 2);
                        alarm.setTargetServiceType(splitResults[0]);
                        String resourceCnName = splitResults[1];
                        if (StringUtils.hasText(resourceCnName))
                        {
                            alarm.setTargetResourceType(resourceCnName);
                        }
                        else
                        {
                            String[] splitEnResults = serviceResourceEnNamePair.split(":", 2);
                            if (splitEnResults.length == 2)
                            {
                                alarm.setTargetResourceType(splitEnResults[1]);
                            }
                        }

                    }
                }
            }
        } catch (Exception e)
        {
            log.error("填充服务和资源名称时出错! error: {}", e.getMessage());
        }
    }

    public static  String getServiceResourcePair(String serviceName, String resourceName)
    {
        return String.format("%s:%s", serviceName, StringUtils.isEmpty(resourceName)
                ? ""
                : resourceName);
    }
}
