package com.lnjoying.justice.cmp.domain.dto.request.openstack.neutron;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class OSNetworkUpdateReq
{
    private static final long serialVersionUID = 1L;
    private OSNetworkUpdate network;

    @Data
    public static class OSNetworkUpdate
    {
        @JsonProperty("admin_state_up")
        @SerializedName("admin_state_up")
        private Boolean adminStateUp;
        @JsonProperty("dns_domain")
        @SerializedName("dns_domain")
        private String dnsDomain;
        private Integer mtu;
        @JsonProperty
        private String name;
        @JsonProperty("port_security_enabled")
        @SerializedName("port_security_enabled")
        private Boolean portSecurityEnabled;
        @JsonProperty("provider:network_type")
        @SerializedName("provider:network_type")
        private String networkType;
        @JsonProperty("provider:physical_network")
        @SerializedName("provider:physical_network")
        private String physicalNetwork;
        @JsonProperty("provider:segmentation_id")
        @SerializedName("provider:segmentation_id")
        private Integer segmentationId;
        @JsonProperty("qos_policy_id")
        @SerializedName("qos_policy_id")
        private String qosPolicyId;
        @JsonProperty("router:external")
        @SerializedName("router:external")
        private Boolean external;
        private List<String> segments;
        @JsonProperty("shared")
        @SerializedName("shared")
        private Boolean shared;
        private String description;
        @JsonProperty("is_default")
        @SerializedName("is_default")
        private Boolean isDefault;
    }
}
