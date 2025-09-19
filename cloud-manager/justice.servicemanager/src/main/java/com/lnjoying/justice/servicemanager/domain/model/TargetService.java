package com.lnjoying.justice.servicemanager.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.lnjoying.justice.schema.entity.StatusCode;
import com.lnjoying.justice.servicemanager.common.TargetServiceStatus;
import com.lnjoying.justice.servicemanager.db.model.TblServicePortTargetServiceInfo;
import lombok.Data;

@Data
public class TargetService
{
    @SerializedName("id")
    @JsonProperty("id")
    private String id;

    @SerializedName("description")
    @JsonProperty("description")
    private String description;

    @SerializedName("site_id")
    @JsonProperty("site_id")
    private String siteId;

    @SerializedName("site_name")
    @JsonProperty("site_name")
    private String siteName;

    @SerializedName("service")
    @JsonProperty("service")
    private String service;

    @SerializedName("service_name")
    @JsonProperty("service_name")
    private String serviceName;

    @SerializedName("target_ip")
    @JsonProperty("target_ip")
    private String targetIp;

    @SerializedName("target_port")
    @JsonProperty("target_port")
    private Integer targetPort;

    @SerializedName("cert")
    @JsonProperty("cert")
    private String cert;

    @SerializedName("status")
    @JsonProperty("status")
    private StatusCode status;

    @SerializedName("target_svc_node")
    @JsonProperty("target_svc_node")
    private String targetSvcNode;

    @SerializedName("target_svc_node_name")
    @JsonProperty("target_svc_node_name")
    private String targetSvcNodeName;

    @SerializedName("target_svc_port")
    @JsonProperty("target_svc_port")
    private Integer targetSvcPort;

    @SerializedName("svc_node")
    @JsonProperty("svc_node")
    private String svcNode;

    @SerializedName("svc_node_name")
    @JsonProperty("svc_node_name")
    private String svcNodeName;

    @SerializedName("svc_ip")
    @JsonProperty("svc_ip")
    private String svcIp;

    @SerializedName("svc_port")
    @JsonProperty("svc_port")
    private Integer svcPort;

    @SerializedName("protocol")
    @JsonProperty("protocol")
    private String protocol;

    public static TargetService of(TblServicePortTargetServiceInfo tblServicePortTargetServiceInfo)
    {
        TargetService targetService = new TargetService();
        targetService.setId(tblServicePortTargetServiceInfo.getId());
        targetService.setDescription(tblServicePortTargetServiceInfo.getDescription());
        targetService.setSiteId(tblServicePortTargetServiceInfo.getSite());
        targetService.setService(tblServicePortTargetServiceInfo.getService());
        targetService.setTargetIp(tblServicePortTargetServiceInfo.getTargetIp());
        targetService.setTargetPort(tblServicePortTargetServiceInfo.getTargetPort());
        targetService.setCert(tblServicePortTargetServiceInfo.getCert());
        TargetServiceStatus status = TargetServiceStatus.fromCode(tblServicePortTargetServiceInfo.getStatus());
        targetService.setStatus(status.toStatusCode());
        targetService.setTargetSvcNode(tblServicePortTargetServiceInfo.getTargetSvcNode());
        targetService.setTargetSvcPort(tblServicePortTargetServiceInfo.getTargetSvcPort());
        targetService.setSvcNode(tblServicePortTargetServiceInfo.getSvcNode());
        targetService.setSvcIp(tblServicePortTargetServiceInfo.getSvcIp());
        targetService.setSvcPort(tblServicePortTargetServiceInfo.getSvcPort());
        targetService.setProtocol(tblServicePortTargetServiceInfo.getProtocol());
        return targetService;
    }
}
