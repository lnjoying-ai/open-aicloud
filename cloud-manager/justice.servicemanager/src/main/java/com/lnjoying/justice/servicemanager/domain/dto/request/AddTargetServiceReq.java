package com.lnjoying.justice.servicemanager.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class AddTargetServiceReq
{
    @SerializedName("svc_node")
    @JsonProperty("svc_node")
    private String svcNode;

    @SerializedName("svc_port")
    @JsonProperty("svc_port")
    private Integer svcPort;

    @SerializedName("protocol")
    @JsonProperty("protocol")
    private String protocol;

    @SerializedName("description")
    @JsonProperty("description")
    private String description;

    @SerializedName("site_id")
    @JsonProperty("site_id")
    private String siteId;

    @SerializedName("service")
    @JsonProperty("service")
    private String service;

    @SerializedName("target_ip")
    @JsonProperty("target_ip")
    private String targetIp;

    @SerializedName("target_port")
    @JsonProperty("target_port")
    private Integer targetPort;

    @SerializedName("cert")
    @JsonProperty("cert")
    private String cert;
}
