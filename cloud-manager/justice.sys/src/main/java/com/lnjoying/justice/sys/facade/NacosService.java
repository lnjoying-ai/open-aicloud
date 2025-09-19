package com.lnjoying.justice.sys.facade;

import com.lnjoying.justice.schema.service.sys.SysService;
import com.lnjoying.justice.sys.domain.dto.req.AddConfigReq;
import io.swagger.annotations.ApiParam;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/10/21 17:17
 */

public interface NacosService
{

    Boolean createNamespace(String namespaceId);

    Boolean checkNamespaceIdExist(String namespaceId);

    public Boolean publishConfig(AddConfigReq addConfig);

    public Object getConfig(String namespace, String group, String dataId);

    public Object getConfigList(String namespace, String group, String dataId, boolean system, Integer pageNum, Integer pageSize);

    void deleteConfig(String namespace, String dataId, String group);

    Boolean exists(String namespace, String group, String dataId);

    SysService.ConfigInfoBase getConfigInfoBase(String namespace, String group, String dataId);
}
