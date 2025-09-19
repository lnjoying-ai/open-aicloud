package com.lnjoying.justice.iam.rpcserviceimpl;

import com.lnjoying.justice.iam.db.model.TblIamService;
import com.lnjoying.justice.iam.db.repo.ServiceRepository;
import com.lnjoying.justice.iam.domain.model.ActionsCache;
import com.lnjoying.justice.iam.domain.model.ServiceResourceEnCnNameCache;
import com.lnjoying.justice.iam.service.ResourceService;
import com.lnjoying.justice.schema.entity.RpcResult;
import com.lnjoying.justice.schema.service.iam.CommonResourceFeederService;
import io.jsonwebtoken.lang.Collections;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.servicecomb.core.SCBEngine;
import org.apache.servicecomb.core.SCBStatus;
import org.apache.servicecomb.provider.pojo.RpcSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.*;

import static com.lnjoying.justice.schema.service.iam.CommonResourceFeederService.ResultCode.*;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/5/6 16:18
 */

@RpcSchema(schemaId = "commonResourceFeederService")
@Slf4j
public class CommonResourceFeederServiceImpl implements CommonResourceFeederService
{
    @Autowired
    private ResourceService resourceService;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private ActionsCache actionsCache;

    @Autowired
    private ServiceResourceEnCnNameCache serviceResourceEnCnNameCache;

    @Override
    public RpcResult resourceFeeder(@ApiParam(name = "iamCode") String iamCode, @ApiParam(name = "schemaMap")Map<String, String> schemaMap)
    {

        try
        {
            if (!SCBEngine.getInstance().getStatus().equals(SCBStatus.UP))
            {
                return new RpcResult(SERVICE_NOT_UP.getCode(), "iam service is not up please try again later", null);
            }

            if (StringUtils.isBlank(iamCode) || CollectionUtils.isEmpty(schemaMap))
            {
                return new RpcResult(MISSING_PARAMETER.getCode(), "parameter is empty or missing", null);
            }

            TblIamService tblIamService = serviceRepository.selectByIamCode(iamCode);
            if (Objects.isNull(tblIamService))
            {
                return new RpcResult(SERVICE_NOT_EXIST.getCode(), "bad iam code or service does not exist", null);
            }

            resourceService.registerCommonResourceInfos(iamCode, schemaMap);
        }
        catch (Exception e)
        {

            return new RpcResult(SYSTEM_ERROR.getCode(), "bad iam code or service does not exist", null);
        }

        return new RpcResult(SUCCESS.getCode(), "success", null);
    }

    @Override
    public RpcResult<HashMap<String, String>> getServiceResourceDisplayInfo(@ApiParam(name = "serviceResources") Set<String> serviceResources)
    {
        if (serviceResources == null || Collections.isEmpty(serviceResources))
        {
            return new RpcResult(SYSTEM_ERROR.getCode(), "input is empty", "");
        }

        HashMap<String, String> serviceResourceCnNameMap = new HashMap<>();
        for (String serviceResourceEnNamePair : serviceResources)
        {
            if (serviceResourceEnNamePair == null || !serviceResourceEnNamePair.contains(":"))
            {
                continue;
            }
            String[] splitResults = serviceResourceEnNamePair.split(":", 2);
            String serviceEnName = splitResults[0];
            String resourceEnName = splitResults[1];
            Optional<ServiceResourceEnCnNameCache.ServiceResourceCnNameQueryResult> serviceResourceCnNameQueryResult = serviceResourceEnCnNameCache.queryServiceResourceCnName(serviceEnName, resourceEnName);
            if (serviceResourceCnNameQueryResult.isPresent())
            {
                ServiceResourceEnCnNameCache.ServiceResourceCnNameQueryResult serviceResourceCnNameInfo = serviceResourceCnNameQueryResult.get();
                serviceResourceCnNameMap.put(serviceResourceEnNamePair, String.format("%s:%s",
                        Optional.ofNullable(serviceResourceCnNameInfo.getServiceCnName())
                                .orElse(serviceEnName),
                        Optional.ofNullable(serviceResourceCnNameInfo.getResourceCnName())
                                .orElse(resourceEnName)
                ));
            }
        }

        return new RpcResult(SUCCESS.getCode(), "success", serviceResourceCnNameMap);
    }
}
