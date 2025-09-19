package com.lnjoying.justice.cmp.domain.dto.request.openstack.neutron;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class OSFloatingIPCreateReq
{
    private static final long serialVersionUID = 1L;

    private OSFloatingIPCreate floatingip;

    @Data
    public static class OSFloatingIPCreate
    {
        @JsonProperty("tenant_id")
        @SerializedName("tenant_id")
        private String tenantId;
        @JsonProperty("project_id")
        @SerializedName("project_id")
        private String projectId;
        @JsonProperty("floating_network_id")
        @SerializedName("floating_network_id")
        private String floatingNetworkId;
        @JsonProperty("fixed_ip_address")
        @SerializedName("fixed_ip_address")
        private String fixedIpAddress;
        @JsonProperty("floating_ip_address")
        @SerializedName("floating_ip_address")
        private String floatingIpAddress;
        @JsonProperty("port_id")
        @SerializedName("port_id")
        private String portId;
        @JsonProperty("subnet_id")
        @SerializedName("subnet_id")
        private String subnetId;
        private Boolean distributed;
        private String description;
        @JsonProperty("dns_domain")
        @SerializedName("dns_domain")
        private String dnsDomain;
        @JsonProperty("dns_name")
        @SerializedName("dns_name")
        private String dnsName;
        @JsonProperty("qos_policy_id")
        @SerializedName("qos_policy_id")
        private String qosPolicyId;
    }
}
