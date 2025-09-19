package com.lnjoying.justice.servicemanager.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.lnjoying.justice.servicemanager.common.TargetServiceStatus;
import com.lnjoying.justice.servicemanager.db.model.TblServicePortTargetServiceInfo;
import lombok.Data;

@Data
public class TargetServiceTestResult extends TargetService
{
    @SerializedName("service_gateway_id")
    @JsonProperty("service_gateway_id")
    private String serviceGatewayId;

    @SerializedName("service_agent_id")
    @JsonProperty("service_agent_id")
    private String serviceAgentId;

    @SerializedName("network_status")
    @JsonProperty("network_status")
    private int networkStatus;

    public static TargetServiceTestResult of(TblServicePortTargetServiceInfo tblServicePortTargetServiceInfo)
    {
        TargetServiceTestResult targetServiceTestResult = new TargetServiceTestResult();

        targetServiceTestResult.setId(tblServicePortTargetServiceInfo.getId());
        targetServiceTestResult.setDescription(tblServicePortTargetServiceInfo.getDescription());
        targetServiceTestResult.setSiteId(tblServicePortTargetServiceInfo.getSite());
        targetServiceTestResult.setService(tblServicePortTargetServiceInfo.getService());
        targetServiceTestResult.setTargetIp(tblServicePortTargetServiceInfo.getTargetIp());
        targetServiceTestResult.setTargetPort(tblServicePortTargetServiceInfo.getTargetPort());
        targetServiceTestResult.setCert(tblServicePortTargetServiceInfo.getCert());
        TargetServiceStatus status = TargetServiceStatus.fromCode(tblServicePortTargetServiceInfo.getStatus());
        targetServiceTestResult.setStatus(status == null ? null :status.toStatusCode());
        targetServiceTestResult.setSvcNode(tblServicePortTargetServiceInfo.getSvcNode());
        targetServiceTestResult.setSvcIp(tblServicePortTargetServiceInfo.getSvcIp());
        targetServiceTestResult.setSvcPort(tblServicePortTargetServiceInfo.getSvcPort());
        targetServiceTestResult.setProtocol(tblServicePortTargetServiceInfo.getProtocol());
        return targetServiceTestResult;
    }
}
