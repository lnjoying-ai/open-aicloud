package com.lnjoying.justice.cluster.manager.service.data;

import com.lnjoying.justice.cluster.manager.config.K3sConfigBean;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.commonweb.util.CollectionUtils;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.common.ErrorLevel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/8/12 16:48
 */

@Service("k3sClusterDataService")
public class K3sClusterDataService
{
    @Autowired
    private K3sConfigBean k3sConfigBean;

    public String getImage(String version,String serviceName) throws WebSystemException
    {

        Map<String,String> versionMap = k3sConfigBean.getK3sJkeSystemImages().get(version);
        if (CollectionUtils.isEmpty(versionMap))
        {
            throw new WebSystemException(ErrorCode.CLUSTER_K3S_VERSION_EMPTY, ErrorLevel.INFO);
        }

        String imageName = versionMap.get(serviceName);
        if (StringUtils.isEmpty(imageName))
        {
            throw new WebSystemException(ErrorCode.CLUSTER_K8S_IMAGE_NOT_CFG, ErrorLevel.INFO);
        }

        return imageName;
    }
}
