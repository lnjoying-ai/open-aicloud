package com.lnjoying.justice.cmp.domain.dto.request.openstack.neutron;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import io.swagger.models.auth.In;
import lombok.Data;

@Data
public class OSPortForwardingUpdateReq
{
    private static final long serialVersionUID = 1L;
    @JsonProperty("port_forwarding")
    @SerializedName("port_forwarding")
    private OSPortForwardingUpdate portForwarding;

    @Data
    public static class OSPortForwardingUpdate
    {
        @JsonProperty("internal_port_id")
        @SerializedName("internal_port_id")
        private String internalPortId;
        @JsonProperty("internal_ip_address")
        @SerializedName("internal_ip_address")
        private String internalIpAddress;
        @JsonProperty("internal_port")
        @SerializedName("internal_port")
        private Integer internalPort;
        @JsonProperty("internal_port_range")
        @SerializedName("internal_port_range")
        private String internalPortRange;
        @JsonProperty("external_port")
        @SerializedName("external_port")
        private Integer externalPort;
        @JsonProperty("external_port_range")
        @SerializedName("external_port_range")
        private String externalPortRange;
        @JsonProperty("protocol")
        @SerializedName("protocol")
        private String protocol;
    }
}
