package com.lnjoying.justice.cmp.service.rpc;

import com.lnjoying.justice.cmp.db.model.TblCmpFlavor;
import com.lnjoying.justice.cmp.db.repo.FlavorRepository;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.repo.RpcFlavorInfo;
import com.lnjoying.justice.cmp.service.nextstack.repo.ImageServiceBiz;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RpcFlavorServiceImpl
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageServiceBiz.class);

    @Autowired
    private FlavorRepository flavorRepository;

    public RpcFlavorInfo getFlavorInfo(String cloudId, String flavorId)
    {
        RpcFlavorInfo flavorInfo = new RpcFlavorInfo();

        TblCmpFlavor tblFlavor = flavorRepository.getFlavorById(cloudId, flavorId);
        if (null == tblFlavor)
        {
            LOGGER.error("get flavor null, flavorId:{}", flavorId);
            return null;
        }
        flavorInfo.setFlavorId(flavorId);
        flavorInfo.setCpu(tblFlavor.getCpu());
        flavorInfo.setMem(tblFlavor.getMem());
        flavorInfo.setType(tblFlavor.getType() == null ? null : tblFlavor.getType().intValue());
        flavorInfo.setName(tblFlavor.getName());
        flavorInfo.setGpuCount(null == tblFlavor.getGpuCount() ? 0 : tblFlavor.getGpuCount());
        flavorInfo.setGpuName(null == tblFlavor.getGpuName() ? "" : tblFlavor.getGpuName());
        flavorInfo.setNeedIb(null != tblFlavor.getNeedIb() && tblFlavor.getNeedIb());
        return flavorInfo;
    }
}
