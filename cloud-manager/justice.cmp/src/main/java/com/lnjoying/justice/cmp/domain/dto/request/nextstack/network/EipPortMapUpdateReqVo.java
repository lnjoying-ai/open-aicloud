package com.lnjoying.justice.cmp.domain.dto.request.nextstack.network;

import lombok.Data;

import javax.validation.constraints.AssertTrue;
import java.util.List;

@Data
public class EipPortMapUpdateReqVo
{

    @Data
    public static class portMap {
        Integer globalPort;
        Integer localPort;
        Integer protocol;
        String portMapId;
        String createTime;
        String updateTime;
    }

    Boolean oneToOne;

    List<portMap> portMaps;

    @AssertTrue(message = "port is invalid")
    public boolean isValidPort()
    {
        if (portMaps == null || portMaps.isEmpty())
        {
            if (oneToOne == null)
                return true;
            return oneToOne;
        }
        for (portMap portMap : portMaps)
        {
            if (portMap.getGlobalPort() == null || portMap.getLocalPort() == null || portMap.getProtocol() == null)
                return false;
            if (portMap.getGlobalPort() < 1 || portMap.getGlobalPort() > 65535 || portMap.getLocalPort() < 1 || portMap.getLocalPort() > 65535)
                return false;
        }
        return true;
    }
}
